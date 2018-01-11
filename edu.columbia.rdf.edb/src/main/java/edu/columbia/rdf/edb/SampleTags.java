/**
 * Copyright (c) 2016, Antony Holmes
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
package edu.columbia.rdf.edb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jebtk.core.path.Path;

/**
 * The Class SampleTags.
 */
public class SampleTags implements Iterable<SampleTag> {

  /** The m map. */
  protected Map<Path, SampleTag> mMap = new HashMap<Path, SampleTag>();

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  public Iterator<SampleTag> iterator() {
    return mMap.values().iterator();
  }

  /**
   * Gets the tag.
   *
   * @param path the path
   * @return the tag
   */
  public SampleTag getTag(String path) {
    return getTag(Path.create(path));
  }

  /**
   * Maps a field to a specific tag object.
   * 
   * @param field
   * @return
   */
  public SampleTag getTag(DataViewField field) {
    return getTag(field.getPath());
  }

  /**
   * Gets the tag.
   *
   * @param path the path
   * @return the tag
   */
  public SampleTag getTag(Path path) {
    return mMap.get(path);
  }

  /**
   * Adds the.
   *
   * @param tag the tag
   */
  public void add(SampleTag tag) {
    mMap.put(tag.getTag().getPath(), tag);
  }

  public int size() {
    return mMap.size();
  }
}
