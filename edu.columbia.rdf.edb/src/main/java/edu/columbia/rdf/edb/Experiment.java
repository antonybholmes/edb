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

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.TreeSetCreator;

/**
 * The Class Experiment.
 */
public class Experiment extends Descriptive {

  /** The m public id. */
  private String mPublicId;

  /**
   * Instantiates a new experiment.
   *
   * @param id the id
   * @param publicId the public id
   * @param name the name
   * @param description the description
   * @param created the created
   */
  public Experiment(int id, String publicId, String name, String description,
      Date created) {
    super(id, name, description, created);

    mPublicId = publicId;
  }

  /**
   * Gets the public id.
   *
   * @return the public id
   */
  public String getPublicId() {
    return mPublicId;
  }

  /**
   * Sort samples by experiment.
   *
   * @param samples the samples
   * @return the map
   */
  public static Map<Experiment, Set<Sample>> sortSamplesByExperiment(
      final Collection<Sample> samples) {
    Map<Experiment, Set<Sample>> map = DefaultHashMap
        .create(new TreeSetCreator<Sample>());

    for (Sample sample : samples) {
      map.get(sample.getExperiment()).add(sample);
    }

    return map;
  }
}
