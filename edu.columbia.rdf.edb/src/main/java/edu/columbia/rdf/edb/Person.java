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
import org.jebtk.bioinformatics.annotation.Type;
import org.jebtk.core.text.TextUtils;

/**
 * The Class Person.
 */
public class Person extends Type {

  /** The m first name. */
  private String mFirstName;

  /** The m last name. */
  private String mLastName;

  /** The m email. */
  private String mEmail;

  /**
   * Instantiates a new person.
   *
   * @param id the id
   * @param firstName the first name
   * @param lastName the last name
   */
  public Person(int id, String firstName, String lastName) {
    this(id, firstName, lastName, TextUtils.EMPTY_STRING);
  }

  /**
   * Instantiates a new person.
   *
   * @param id the id
   * @param firstName the first name
   * @param lastName the last name
   * @param email the email
   */
  public Person(int id, String firstName, String lastName, String email) {
    super(id, firstName + " " + lastName);

    mFirstName = firstName;
    mLastName = lastName;
    mEmail = email;
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return mFirstName;
  }

  /**
   * Gets the lat name.
   *
   * @return the lat name
   */
  public String getLatName() {
    return mLastName;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public String getEmail() {
    return mEmail;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.bioinformatics.annotation.Type#compareTo(org.jebtk.
   * bioinformatics. annotation.Type)
   */
  @Override
  public int compareTo(Entity t) {
    if (t instanceof Person) {
      Person p = (Person) t;

      int c = mLastName.compareTo(p.mLastName);

      if (c == 0) {
        c = mFirstName.compareTo(p.mFirstName);
      }

      return c;
    } else {
      return super.compareTo(t);
    }
  }
}
