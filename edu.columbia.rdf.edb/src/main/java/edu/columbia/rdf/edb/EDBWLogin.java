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

import org.abh.common.network.UrlBuilder;
import org.abh.common.settings.Settings;
import org.abh.common.settings.SettingsService;
import org.abh.common.xml.XmlRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * Logins entries for a connection to a caArray server.
 *
 * @author Antony Holmes Holmes
 */
public class EDBWLogin implements XmlRepresentation, Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The m server. */
	private String mServer = null;
	
	/** The m auth url. */
	private UrlBuilder mAuthUrl;

	/** The m key. */
	private String mKey;

	/** The m rest auth url. */
	private UrlBuilder mRestAuthUrl;

	/** The m user. */
	private String mUser;

	/** The m OTK auth url. */
	private OTKAuthUrl mOTKAuthUrl;

	/** The m url. */
	private UrlBuilder mUrl;

	/** The m api url. */
	private UrlBuilder mApiUrl;
	
	/** The Constant LOG. */
	private static final Logger LOG = 
			LoggerFactory.getLogger(EDBWLogin.class);

	/**
	 * Instantiates a new EDBW login.
	 *
	 * @param server the server
	 * @param user the user
	 * @param key the key
	 * @param epoch the epoch
	 * @param step the step
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public EDBWLogin(String server, 
			String user,
			String key,
			long epoch,
			long step) throws UnsupportedEncodingException {
		mServer = server;
		mUser = user;
		mKey = key;
		
		mUrl = new UrlBuilder(server);
		
		mApiUrl = new UrlBuilder(mUrl).resolve("api").resolve("v1");
		
		mAuthUrl = new UrlBuilder(mApiUrl).resolve("auth");
		
		LOG.info("Login URL: {}", mAuthUrl);
		
		mRestAuthUrl = new UrlBuilder(mAuthUrl).resolve(getKey());
		
		mOTKAuthUrl = new OTKAuthUrl(mAuthUrl, user, key, epoch, step);
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public final UrlBuilder getUrl() {
		return mUrl;
	}
	
	/**
	 * Gets the api url.
	 *
	 * @return the api url
	 */
	public final UrlBuilder getApiUrl() {
		return mApiUrl;
	}
	
	/**
	 * Gets the auth url.
	 *
	 * @return the auth url
	 */
	public final UrlBuilder getAuthUrl() {
		return mRestAuthUrl;
	}
	
	/**
	 * Returns the authentication url an embedded time changing authentication 
	 * key.
	 *
	 * @return the OTK auth url
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public final UrlBuilder getOTKAuthUrl() throws UnsupportedEncodingException {
		return mOTKAuthUrl.getOTKAuthUrl();
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	public final String getServer() {
		return mServer;
	}
	
	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey() {
		return mKey;
	}

	/* (non-Javadoc)
	 * @see org.abh.common.xml.XmlRepresentation#toXml()
	 */
	@Override
	public Element toXml(Document doc) {
		Element serverElement = doc.createElement("server");

		serverElement.setAttribute("name", mServer);
		//serverElement.setAttribute("port", Integer.toString(mPort));
		//element.addAttribute("type", login.getType() == LoginType.CAARRAY ? "caarray" : "array_server");
		
		return serverElement;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return mUser;
	}

	/**
	 * Gets the epoch.
	 *
	 * @return the epoch
	 */
	public long getEpoch() {
		return mOTKAuthUrl.getEpoch();
	}
	
	/**
	 * Gets the step.
	 *
	 * @return the step
	 */
	public long getStep() {
		return mOTKAuthUrl.getStep();
	}
	
	/**
	 * Creates the.
	 *
	 * @param server the server
	 * @param user the user
	 * @param key the key
	 * @param epoch the epoch
	 * @param step the step
	 * @return the EDBW login
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static EDBWLogin create(String server, 
			String user,
			String key,
			long epoch,
			long step) throws UnsupportedEncodingException {
		return new EDBWLogin(server, user, key, epoch, step);
	}
	
	/**
	 * Load from settings.
	 *
	 * @return the EDBW login
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static EDBWLogin loadFromSettings() throws UnsupportedEncodingException {
		return loadFromSettings(SettingsService.getInstance());
	}

	/**
	 * Load from settings.
	 *
	 * @param settings the settings
	 * @return the EDBW login
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static EDBWLogin loadFromSettings(Settings settings) throws UnsupportedEncodingException {
		return create(settings.getAsString("edbw.server"),
				settings.getAsString("edbw.user"),
				settings.getAsString("edbw.key"),
				settings.getAsInt("edbw.totp.epoch"),
				settings.getAsInt("edbw.totp.step-size"));
	}
}
