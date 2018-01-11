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

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.Function;
import org.jebtk.core.collections.UniqueArrayList;
import org.jebtk.core.stream.Stream;

/**
 * The Class Person.
 */
public class Group extends Type {

  private static final Color DEFAULT_COLOR = Color.BLACK;

  private Color mColor;

  /**
   * Instantiates a new person.
   *
   * @param id the id
   * @param firstName the first name
   * @param lastName the last name
   */
  public Group(int id, String name) {
    this(id, name, DEFAULT_COLOR);
  }

  public Group(int id, String name, Color color) {
    super(id, name);

    mColor = color;
  }

  public Color getColor() {
    return mColor;
  }

  /**
   * Create a comma separated string of group names.
   * 
   * @param groups
   * @return
   */
  public static String formatNames(Collection<Group> groups) {
    return Stream.of(groups).map(new Function<Group, String>() {

      @Override
      public String apply(Group g) {
        return g.getName();
      }
    }).join(", ");
  }

  /**
   * Return a list of unique colors ordered by the given group collection.
   * 
   * @param groups
   * @return
   */
  public static List<Color> formatColors(Collection<Group> groups) {

    List<Color> ret = new UniqueArrayList<Color>(groups.size());

    /*
     * Map<String, Color> map = new HashMap<String, Color>();
     * 
     * 
     * for (Group group : groups) { map.put(group.getName(), group.getColor());
     * }
     * 
     * for (String name : CollectionUtils.sortKeys(map)) {
     * ret.add(map.get(name)); }
     */

    for (Group group : groups) {
      ret.add(group.getColor());
    }

    return ret;
  }
}
