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

import org.jebtk.bioinformatics.annotation.Entity;

/**
 * The Class GEO.
 */
public class GEO extends Entity {

  /** The m geo series acc. */
  private String mGeoSeriesAcc;

  /** The m geo acc. */
  private String mGeoAcc;

  /** The m geo plat. */
  private String mGeoPlat;

  /**
   * Instantiates a new geo.
   *
   * @param id the id
   * @param geoSeriesAcc the geo series acc
   * @param geoAcc the geo acc
   * @param geoPlat the geo plat
   */
  public GEO(int id, String geoSeriesAcc, String geoAcc, String geoPlat) {
    super(id);

    mGeoSeriesAcc = geoSeriesAcc;
    mGeoAcc = geoAcc;
    mGeoPlat = geoPlat;
  }

  /**
   * Gets the GEO series accession.
   *
   * @return the GEO series accession
   */
  public String getGEOSeriesAccession() {
    return mGeoSeriesAcc;
  }

  /**
   * Gets the GEO accession.
   *
   * @return the GEO accession
   */
  public String getGEOAccession() {
    return mGeoAcc;
  }

  /**
   * Gets the GEO platform.
   *
   * @return the GEO platform
   */
  public String getGEOPlatform() {
    return mGeoPlat;
  }

}
