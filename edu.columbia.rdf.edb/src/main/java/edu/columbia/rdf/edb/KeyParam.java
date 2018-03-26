package edu.columbia.rdf.edb;

import org.jebtk.core.network.StaticParam;

/**
 * Generates base authentication URLs. Attempts to only create new URLs if the
 * current one becomes invalid to reduce the need to run SHA-256 repeatedly.
 * 
 * @author Antony Holmes Holmes
 */
public class KeyParam extends StaticParam {

  public KeyParam(String value) {
    super("key", value);
  }

}
