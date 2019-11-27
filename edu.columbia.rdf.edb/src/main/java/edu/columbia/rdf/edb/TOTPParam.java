package edu.columbia.rdf.edb;

import java.io.UnsupportedEncodingException;

import org.jebtk.core.TimeUtils;
import org.jebtk.core.cryptography.TOTP;
import org.jebtk.core.http.Param;

/**
 * Generates a TOTP param that creates random 6 digit verification codes
 * using a choosen phrase and epoch. This will dynamically add a 'totp'
 * param into a URL.
 * 
 * @author Antony Holmes
 */
public class TOTPParam extends Param {

  /** The m salt. */
  long mCounter = -1;

  /** The m key. */
  private String mTotpPhrase;

  /** The m step. */
  private long mStep;


  /** The m epoch. */
  private long mEpoch;

  private String mValue;

  /**
   * Instantiates a new totp auth url.
   *
   * @param url the url
   * @param key the user
   * @param phrase the key
   * @param epoch the epoch
   * @param step the step
   */
  public TOTPParam(String phrase, long epoch, long step) {
    mTotpPhrase = phrase;
    mEpoch = epoch;
    mStep = step;
  }

  public TOTPParam(EDBWLogin login) {
    this(login.getTOTPPhrase(), login.getEpoch(), login.getStep());
  }
  
  @Override
  public String getName() {
    return "totp";
  }
  
  /**
   * Gets the totp auth url.
   *
   * @return the totp auth url
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  @Override
  public String getValue() {
    long time = TimeUtils.getCurrentTimeMs();

    long counter = TOTP.getCounter(time, mEpoch, mStep);

    if (counter != mCounter) {
      // Only update the auth url object when we change counter bins
      // since it will not change during the bin duration.

      // Generate an 6 digit totp code
      int totp = TOTP.generateCTOTP6(mTotpPhrase, counter); // toptCounter256(mKey,
                                                     // counter);

      // Format the totp to ensure 6 digits
      mValue = String.format("%06d", totp);
      
      // .resolve(mUser)
      // .resolve(formattedTotp);

      mCounter = counter;
    }

    return mValue;
  }

  /**
   * Gets the epoch.
   *
   * @return the epoch
   */
  public long getEpoch() {
    return mEpoch;
  }

  /**
   * Gets the step.
   *
   * @return the step
   */
  public long getStep() {
    return mStep;
  }
}
