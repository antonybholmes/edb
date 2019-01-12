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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.FileSequenceReader;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.ArrayUtils;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.io.ByteStream;
import org.jebtk.core.io.FileUtils;

/**
 * Decodes counts per base from a 2 byte per base count file.
 *
 * @author Antony Holmes
 *
 */
public class ReadCountsFile32Bit extends ReadCountsFile {

  /**
   * The member directory.
   */
  private Path mDir;

  /**
   * The member file map.
   */
  private Map<Chromosome, IterMap<Integer, Path>> mFileMap = DefaultHashMap
      .create(new HashMapCreator<Integer, Path>());

  private Map<Chromosome, IterMap<Integer, Integer>> mBitMap = DefaultHashMap
      .create(new HashMapCreator<Integer, Integer>());

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile the meta file
   */
  public ReadCountsFile32Bit(Path metaFile) {
    mDir = metaFile;
  }

  /**
   * Gets the dir.
   *
   * @return the dir
   */
  public Path getDir() {
    return mDir;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public int[] getCounts(GenomicRegion region, int window)
      throws IOException {
    Chromosome chr = region.getChr();

    if (!mFileMap.get(chr).containsKey(window)) {
      Path file = mDir.resolve(chr + ".counts.win." + window + ".4bit");

      if (FileUtils.exists(file)) {
        mFileMap.get(chr).put(window, file);
        mBitMap.get(chr).put(window, 4);
      } else {
        file = mDir.resolve(chr + ".counts.win." + window + ".8bit");

        if (FileUtils.exists(file)) {
          mFileMap.get(chr).put(window, file);
          mBitMap.get(chr).put(window, 8);
        } else {
          file = mDir.resolve(chr + ".counts.win." + window + ".12bit");

          if (FileUtils.exists(file)) {
            mFileMap.get(chr).put(window, file);
            mBitMap.get(chr).put(window, 12);
          } else {
            file = mDir.resolve(chr + ".counts.win." + window + ".16bit");

            if (FileUtils.exists(file)) {
              mFileMap.get(chr).put(window, file);
              mBitMap.get(chr).put(window, 16);
            } else {
              file = mDir.resolve(chr + ".counts.win." + window + ".20bit");

              if (FileUtils.exists(file)) {
                mFileMap.get(chr).put(window, file);
                mBitMap.get(chr).put(window, 20);
              } else {
                file = mDir.resolve(chr + ".counts.win." + window + ".24bit");

                if (FileUtils.exists(file)) {
                  mFileMap.get(chr).put(window, file);
                  mBitMap.get(chr).put(window, 24);
                } else {
                  file = mDir.resolve(chr + ".counts.win." + window + ".32bit");

                  if (FileUtils.exists(file)) {
                    mFileMap.get(chr).put(window, file);
                    mBitMap.get(chr).put(window, 32);
                  }
                }
              }
            }
          }
        }
      }
    }

    System.err.println("read 32 " + chr + " " + window + " " + mFileMap.get(chr).containsKey(window));
    
    // Extract from file with appropriate bit depth
    if (mFileMap.get(chr).containsKey(window)) {
      switch (mBitMap.get(chr).get(window)) {
      case 24:
        return getCounts24(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      case 20:
        return getCounts20(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      case 16:
        return getCounts16(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      case 12:
        return getCounts12(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      case 8:
        return getCounts8(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      case 4:
        return getCounts4(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      default:
        // Default assume 32bit numbers
        return getCounts32(mFileMap.get(chr).get(window),
            region.getStart(),
            region.getEnd(),
            window);
      }
    } else {
      return ArrayUtils.EMPTY_INT_ARRAY; //Collections.emptyList();
    }
  }

  private static int[] getCounts4(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] buf = FileSequenceReader.getBytes(file, s / 2, e / 2);

    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    boolean even = true;

    int p = 0;

    for (int i = 0; i < l; ++i) {
      if (even) {
        scores[i] = (buf[p] & 0b11110000) >> 4;
      } else {
        scores[i] = buf[p] & 0b1111;

        ++p;
      }
    }

    return scores;
  }

  private static int[] getCounts8(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] buf = FileSequenceReader.getBytes(file, s, e);

    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    for (int i = 0; i < l; ++i) {
      scores[i] = (int) buf[i];
    }

    return scores;
  }

  private static int[] getCounts12(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    // We need one extra byte since the last coordinate can span 2 bytes
    byte[] buf = FileSequenceReader.getBytes(file, s * 3 / 2, e * 3 / 2 + 1);

    //List<Integer> scores = new ArrayList<Integer>(l);
    
    int[] scores = new int[l];
    
    int p = 0;

    // Whether to start at the beginning or end of the byte
    boolean even = s % 2 == 0;

    // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + even);

    for (int i = 0; i < l; ++i) {
      // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + p + " " +
      // Integer.toBinaryString(buf[p]) + " " + even);

      int score = 0;

      if (even) {
        score = (buf[p] << 4) | ((buf[p + 1] & 0b11110000) >> 4);
      } else {
        score = ((buf[p] & 0b1111) << 8) | (buf[p + 1] & 0b11111111);

        ++p;
      }

      scores[i] = score; //scores.add(score);

      // if (even) {
      // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + p + " " +
      // String.format("%8s", Integer.toBinaryString(buf[p])).replace(" ", "0")
      // + " "
      // + score + " " + even);
      // } else {
      // int pp = p - 1;
      // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + pp + " " +
      // String.format("%8s", Integer.toBinaryString(buf[pp])).replace(" ", "0")
      // + " "
      // + String.format("%8s", Integer.toBinaryString(buf[pp + 1])).replace("
      // ", "0")
      // + " " + score + " " + even);
      // }

      ++p;

      even = !even;
    }

    return scores;
  }

  /**
   * Gets the counts.
   *
   * @param file the file
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static int[] getCounts16(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] buf = FileSequenceReader.getBytes(file, s * 2, e * 2 + 1);

    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    int p = 0;

    for (int i = 0; i < l; ++i) {
      scores[i] = ((buf[p] << 8) | (buf[p + 1] & 0b11111111));

      p += 2;
    }

    return scores;
  }

  private static int[] getCounts20(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    // We need one extra byte since the last coordinate can span 2 bytes
    byte[] buf = FileSequenceReader.getBytes(file, s * 5 / 2, e * 5 / 2 + 2);

    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    int p = 0;

    // Whether to start at the beginning or end of the byte
    boolean even = s % 2 == 0;

    // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + even);

    for (int i = 0; i < l; ++i) {
      // System.err.println("12bit " + s + " " + (s * 3 / 2) + " " + p + " " +
      // Integer.toBinaryString(buf[p]) + " " + even);

      int score = 0;

      if (even) {
        score = (buf[p] << 12) | (buf[p + 1] << 4) | ((buf[p + 2] & 0xF0) >> 4);
      } else {
        score = ((buf[p] & 0xF) << 16) | (buf[p + 1] << 8) | (buf[p + 2] & 0xFF);

        ++p;
      }

      scores[i] = score;

      ++p;

      even = !even;
    }

    return scores;
  }

  private static int[] getCounts24(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] d = FileSequenceReader.getBytes(file, s * 3, e * 3 + 2);
    
    ByteStream buf = new ByteStream(d);

    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    for (int i = 0; i < l; ++i) {
      scores[i] = buf.readInt24(); //((buf[p] << 16) | (buf[p + 1] << 8) | (buf[p + 2] & 0xFF));
    }

    return scores;
  }

  private static int[] getCounts32(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] d = FileSequenceReader.getBytes(file, s * 4, e * 4 + 3);

    ByteBuffer buf = ByteBuffer.wrap(d);
    
    int[] scores = new int[l]; //List<Integer> scores = new ArrayList<Integer>(l);

    for (int i = 0; i < l; ++i) {
      scores[i] = buf.getInt();
    }

    return scores;
  }
}
