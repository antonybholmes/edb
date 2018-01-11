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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.database.DatabaseResultsTable;
import org.jebtk.database.JDBCConnection;

/**
 * The Class Organism.
 */
public class Species extends org.jebtk.bioinformatics.annotation.Species {

  /** The Constant SQL. */
  private static final String SQL = "SELECT organisms.id, organisms.name, organisms.scientific_name FROM organisms ORDER BY organisms.name";

  /**
   * Instantiates a new organism.
   *
   * @param name the name
   * @param scientificName the scientific name
   */
  public Species(String name, String scientificName) {
    this(-1, name, scientificName);
  }

  /**
   * Instantiates a new organism.
   *
   * @param name the name
   */
  public Species(String name) {
    this(-1, name);
  }

  /**
   * Instantiates a new organism.
   *
   * @param id the id
   * @param name the name
   */
  public Species(int id, String name) {
    this(id, name, name);
  }

  /**
   * Instantiates a new organism.
   *
   * @param id the id
   * @param name the name
   * @param scientificName the scientific name
   */
  public Species(int id, String name, String scientificName) {
    super(id, name, scientificName);
  }

  public String getScientificName() {
    return super.getScientificName();
  }

  /**
   * Gets the organisms.
   *
   * @param connection the connection
   * @return the organisms
   * @throws SQLException the SQL exception
   */
  public static Map<Integer, Species> getSpecies(Connection connection)
      throws SQLException {
    Map<Integer, Species> ret = new HashMap<Integer, Species>();

    PreparedStatement statement = connection.prepareStatement(SQL);

    try {
      DatabaseResultsTable table = JDBCConnection.getTable(statement);

      for (int i = 0; i < table.getRowCount(); ++i) {
        Species o = new Species(table.getInt(i, 0), table.getString(i, 1),
            table.getString(i, 2));

        ret.put(o.getId(), o);
      }
    } finally {
      statement.close();
    }

    return ret;
  }

}
