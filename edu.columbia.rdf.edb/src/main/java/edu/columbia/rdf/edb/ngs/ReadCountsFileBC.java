/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.edb.ngs;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterHashMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.io.ByteStream;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.text.TextUtils;

/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes
 */
public class ReadCountsFileBC extends ReadCountsFile {

  private static final int MAGIC_NUMBER_OFFSET_BYTES = 0;
  private static final int BIN_SIZE_OFFSET_BYTES = 
      MAGIC_NUMBER_OFFSET_BYTES + 4;
  private static final int BIN_WIDTH_OFFSET_BYTES = BIN_SIZE_OFFSET_BYTES + 1;
  private static final int N_BINS_OFFSET_BYTES = BIN_WIDTH_OFFSET_BYTES + 4;
  private static final int BINS_OFFSET_BYTES = N_BINS_OFFSET_BYTES + 4;
  private static final int MIN_BIN_WIDTH = 100;
  private static final Map<Integer, Integer> POWER_MAP = 
      new HashMap<Integer, Integer>();

  static {
    POWER_MAP.put(100, 2);
    POWER_MAP.put(1000, 3);
    POWER_MAP.put(10000, 4);
    POWER_MAP.put(100000, 5);
    POWER_MAP.put(1000000, 6);
    POWER_MAP.put(10000000, 7);
    POWER_MAP.put(100000000, 8);
    POWER_MAP.put(1000000000, 9);
  }

  private Path mDir = null;

  private IterMap<Chromosome, IterMap<Integer, Path>> mFileMap = 
      DefaultHashMap.create(new HashMapCreator<Integer, Path>());

  private IterMap<Integer, Integer> mCountMap = 
      new IterHashMap<Integer, Integer>();

  private RandomAccessFile mFile = null;

  private Chromosome mChr = null;
  private int mPower = -1;
  private int mReadCount = -1;
  private String mMode;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param file the file
   */
  public ReadCountsFileBC(Path bciFile) {
    this(bciFile, BCMode.COUNT);
  }
  
  public ReadCountsFileBC(Path bciFile, BCMode mode) {
    mDir = bciFile.toAbsolutePath().getParent();
    mMode = mode.toString().toLowerCase();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public int[] getCounts(GenomicRegion region, int window) throws IOException {
    return getCounts(region.getChr(),
        region.getStart(),
        region.getEnd(),
        window);
  }

  private static int[] _getCounts(RandomAccessFile file,
      Chromosome chr,
      int start,
      int end,
      int binWidth,
      int binSize) throws IOException {
    int sb = start / binWidth;
    int eb = end / binWidth;
    int n = Math.max(1, eb - sb);


    int sa = sb;
    int sn = n;

    if (binSize > 1) {
      sa *= binSize;
      sn *= binSize;
    }

    file.seek(BINS_OFFSET_BYTES + sa);

    byte[] d = new byte[sn];

    file.read(d);
    
    //ByteBuffer buffer = ByteBuffer.wrap(d);
    ByteStream buffer = new ByteStream(d);

    int[] ret = new int[n]; // np.zeros(n, dtype=int)

    switch (binSize) {
    case 4:
      // 4 byte int
      for (int i = 0; i < n; ++i) {
        ret[i] = buffer.readInt();
      }
      break;
    case 2:
      // 2 byte short
      for (int i = 0; i < n; ++i) {
        ret[i] = buffer.readShort();
      }
      break;
    default:
      // a byte
      for (int i = 0; i < n; ++i) {
        ret[i] = buffer.read();
      }
      break;
    }
    
    // Scale to the number of bins
    //for (int i = 0; i < ret.length; ++i) {
    //  ret[i] *= binWidth;
    //}

    return ret;
  }

  /**
   * Gets the counts.
   *
   * @param chr the chr
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getCounts(Chromosome chr, int start, int end, int window)
      throws IOException {

    int power = POWER_MAP.get(window);
    
    RandomAccessFile file = getFile(chr, power);

    int s = start;
    int e = end;

    int binSize = getBinSize(file);

    int[] d = _getCounts(file, chr, s, e, window, binSize);

    if (window < MIN_BIN_WIDTH) {
      int sb = start / window;
      int eb = end / window;


      int i = 0;
      int f;
      int n;
      int[] d2 = null;

      n = Math.max(1, eb - sb);
      f = MIN_BIN_WIDTH / window;
      d2 = new int[n];

      for (int i2 = 0; i2 < n; ++i2) {
        d2[i2] = d[i];

        if ((i2 + 1) % f == 0) {
          i += 1;
        }
      }

      d = d2;
    }

    return d;
  }

  private RandomAccessFile getFile(Chromosome chr, int power) throws IOException {

    if (!mFileMap.get(chr).containsKey(power)) {
      String c = chr.toString() + ".";
      String p = power + "bw";

      for (Path file : FileUtils.ls(mDir)) {
        String name = PathUtils.getName(file);

        if (name.contains(c) && name.contains(p)) {
          mFileMap.get(chr).put(power, file);

          break;
        }
      }
    }

    if (mChr != null && mChr.equals(chr) && mPower == power) {
      return mFile;
    }

    
    mFile = FileUtils.newRandomAccess(mFileMap.get(chr).get(power));
    mChr = chr;
    mPower = power;
    return mFile;
  }

  /**
   * Returns the number of reads in the representation that can be used for
   * normalization purposes.
   * 
   * @param power
   * @return
   * @throws IOException
   */
  private int getBinCount(int power) throws IOException {
    
    System.err.println("get bin counts");
    
    if (!mCountMap.containsKey(power)) {
      String p = power + "bw";

      for (Path file : FileUtils.ls(mDir)) {
        String name = PathUtils.getName(file);

        if (name.startsWith("counts") && name.contains(p) && name.endsWith("bc")) {
          DataInputStream r = FileUtils.newDataInputStream(file);

          int c = r.readInt();
          
          //System.err.println("found " + name + " "  + power + " " + c);

          r.close();

          mCountMap.put(power, c);
          
          break;
        }
      }
    }

    return mCountMap.get(power);
  }

  public void close() {
    if (mFile != null) {
      try {
        mFile.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getReadCount()
   */
  @Override
  public int getReadCount(Genome genome, int window) throws IOException {
    if (mReadCount < 1) {
      Path file = mDir.resolve(TextUtils.cat("reads.", genome, ".", mMode, ".bc"));

      DataInputStream r = FileUtils.newDataInputStream(file);
      
      mReadCount = r.readInt();
      
      //System.err.println("Get read count from " + file + " " + mReadCount);
      
      r.close();
    }
    
    return mReadCount;
  }

  private static int getPower(int window) {
    return POWER_MAP.get(window);
  }

  private static int getBinSize(RandomAccessFile file) throws IOException {
    file.seek(BIN_SIZE_OFFSET_BYTES);

    return file.read();
  }
}