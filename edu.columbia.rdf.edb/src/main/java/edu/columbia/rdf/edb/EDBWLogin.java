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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.jebtk.core.http.URLPath;
import org.jebtk.core.settings.Settings;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.Join;
import org.jebtk.core.text.TextUtils;
import org.jebtk.core.xml.XmlRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * Logins entries for a connection to a caArray server.
 *
 * @author Antony Holmes
 */
public class EDBWLogin implements XmlRepresentation, Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m server. */
  private String mServer = null;

  /** The m auth url. */
  // private UrlBuilder mAuthUrl;

  /** The m key. */
  private String mPhrase = null;

  /** The m rest auth url. */
  // private UrlBuilder mRestAuthUrl;

  /** The m user. */
  private String mKey = null;

  /** The m totp auth url. */
  //private TOTPAuthUrl mTOTPAuthUrl;

  /** The m url. */
  private URLPath mUrl;

  /** The m api url. */
 // private UrlBuilder mApiUrl;

  private String mTotp;

  private long mEpoch;

  private long mStep;

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory.getLogger(EDBWLogin.class);

  public static final String SERVER_SETTING = "edbw.server";

  public static final String KEY_SETTING = "edbw.key";

  public static final String TOTP_SETTING = "edbw.totp";

  public static final String EPOCH_SETTING = "edbw.totp.epoch.offset.ms";

  public static final String TOTP_STEP_SIZE_SETTING = "edbw.totp.step-size";

  private static final long DEFAULT_EPOCH_OFFSET_MS = 0;

  private static final long DEFAULT_STEP_SIZE_MS = 3600000;

  
  public EDBWLogin(EDBWLogin login) {
    this(login.mServer, login.mKey, login.mTotp, login.mPhrase, login.mEpoch, login.mStep);
  }
  
  /**
   * Instantiates a new EDBW login.
   *
   * @param server the server
   * @param key the user
   * @param phrase the  TOTP phrase
   * @param epoch the epoch
   * @param step the step
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public EDBWLogin(String server, String key, String totp, String phrase,
      long epoch, long step) {
    mServer = server;
    mKey = key;
    mTotp = totp;
    mPhrase = phrase;
    mEpoch = epoch;
    mStep = step;

    //mUrl = UrlBuilder.fromUrl(server).resolve("api").resolve("v1").param(new KeyParam(mKey)); //.param(new TOTPParam(phrase, epoch, step));
    mUrl = URLPath.fromUrl(server).join("api").join("v1").key(mKey).authKey(mKey);
    // mAuthUrl = UrlBuilder.fromUrl(mApiUrl).resolve("auth");

    LOG.info("Login URL: {}", mUrl);

    // mRestAuthUrl = mApiUrl.resolve(getKey());

    //mTOTPAuthUrl = new TOTPAuthUrl(mApiUrl, key, totpPhrase, epoch, step);
  }

  /**
   * Gets the url.
   *
   * @return the url
   */
  public final URLPath getURL() {
    return mUrl;
  }

  /**
   * Gets the api url.
   *
   * @return the api url
   */
  //public final UrlBuilder getUrl() {
  //  return mApiUrl;
  //}

  /**
   * Gets the auth url.
   *
   * @return the auth url
   */
  // public final UrlBuilder getAuthUrl() {
  // return mRestAuthUrl;
  // }

  /**
   * Returns the authentication url an embedded time changing authentication
   * key.
   *
   * @return the totp auth url
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  //@Override
  //public UrlBuilder getTOTPAuthUrl() {
  //  return mTOTPAuthUrl.getTOTPAuthUrl();
  //}

  /**
   * Gets the server.
   *
   * @return the server
   */
  public final String getServer() {
    return mServer;
  }

  public String getTOTP() {
    return mTotp;
  }

  /**
   * Gets the key.
   *
   * @return the key
   */
  public String getTOTPPhrase() {
    return mPhrase;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.xml.XmlRepresentation#toXml()
   */
  @Override
  public Element toXml(Document doc) {
    Element serverElement = doc.createElement("server");

    serverElement.setAttribute("name", mServer);
    // serverElement.setAttribute("port", Integer.toString(mPort));
    // element.addAttribute("type", login.getType() == LoginType.CAARRAY ?
    // "caarray"
    // : "array_server");

    return serverElement;
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  public String getKey() {
    return mKey;
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

  /**
   * Load from settings.
   *
   * @return the EDBW login
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public static EDBWLogin loadFromSettings()
      throws UnsupportedEncodingException {
    return loadFromSettings(SettingsService.getInstance());
  }

  /**
   * Load from settings.
   *
   * @param settings the settings
   * @return the EDBW login
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  public static EDBWLogin loadFromSettings(Settings settings)
      throws UnsupportedEncodingException {
    long epoch = DEFAULT_EPOCH_OFFSET_MS;
    long step = DEFAULT_STEP_SIZE_MS;

    String totp = settings.getString(TOTP_SETTING);

    List<String> tokens = TextUtils.fastSplit(totp,
        TextUtils.COLON_DELIMITER);

    String phrase = tokens.get(0);

    if (tokens.size() > 1) {
      epoch = Integer.parseInt(tokens.get(1));
    }

    if (tokens.size() > 2) {
      step = Integer.parseInt(tokens.get(2));
    }

    return create(settings.getString(SERVER_SETTING),
        settings.getString(KEY_SETTING),
        totp,
        phrase,
        epoch,
        step);
  }

  public static void saveSettings(EDBWLogin login) {
    SettingsService.getInstance().setAutoSave(false);

    // Key is of the form key:epoch:step
    String totp = Join.on(TextUtils.COLON_DELIMITER)
        .values(login.getTOTPPhrase(), login.getEpoch(), login.getStep()).toString();

    SettingsService.getInstance().update(SERVER_SETTING, login.getServer());
    SettingsService.getInstance().update(KEY_SETTING, login.getKey());
    SettingsService.getInstance().update(TOTP_SETTING, totp);
    // SettingsService.getInstance().update(EPOCH_SETTING,
    // login.getEpoch());
    // SettingsService.getInstance().update(TOTP_STEP_SIZE_SETTING,
    // login.getStep());
    SettingsService.getInstance().setAutoSave(true);
  }

  /**
   * Create a new login from the server, user, and key details.
   * 
   * @param server
   * @param key
   * @param totp Consists of a 32 character key, an epoch offset in ms and a
   *          step size in ms in the form key:epoch:step which can be parsed.
   * @return
   * @throws UnsupportedEncodingException
   */
  public static EDBWLogin create(String server, String key, String totp)
      throws UnsupportedEncodingException {
    long epoch = DEFAULT_EPOCH_OFFSET_MS;
    long step = DEFAULT_STEP_SIZE_MS;

    List<String> tokens = TextUtils.fastSplit(totp,
        TextUtils.COLON_DELIMITER);

    String totpPhrase = tokens.get(0);

    if (tokens.size() > 1) {
      epoch = Integer.parseInt(tokens.get(1));
    }

    if (tokens.size() > 2) {
      step = Integer.parseInt(tokens.get(2));
    }

    return create(server, key, totp, totpPhrase, epoch, step);
  }

  /**
   * Creates the.
   *
   * @param server the server
   * @param key the user
   * @param totpPhrase the key
   * @param epoch the epoch
   * @param step the step
   * @return the EDBW login
   * @throws UnsupportedEncodingException the unsupported encoding exception
   */
  private static EDBWLogin create(String server,
      String key,
      String totp,
      String totpPhrase,
      long epoch,
      long step) throws UnsupportedEncodingException {
    return new EDBWLogin(server, key, totp, totpPhrase, epoch, step);
  }

}
