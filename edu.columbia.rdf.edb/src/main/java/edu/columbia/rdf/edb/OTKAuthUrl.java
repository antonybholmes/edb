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

import java.io.UnsupportedEncodingException;

import org.abh.common.TimeUtils;
import org.abh.common.cryptography.TOTP;
import org.abh.common.network.UrlBuilder;

/**
 * Generates base authentication URLs. Attempts to only create new URLs
 * if the current one becomes invalid to reduce the need to run SHA-256
 * repeatedly.
 * 
 * @author Antony Holmes Holmes
 */
public class OTKAuthUrl {

	/** The m salt. */
	long mCounter = -1;

	/** The m user. */
	private String mUser;

	/** The m key. */
	private String mKey;

	/** The m step. */
	private long mStep;

	/** The m rest auth url. */
	private UrlBuilder mRestAuthUrl;

	/** The m url. */
	private UrlBuilder mUrl;

	/** The m epoch. */
	private long mEpoch;

	/**
	 * Instantiates a new totp auth url.
	 *
	 * @param url the url
	 * @param user the user
	 * @param key the key
	 * @param epoch the epoch
	 * @param step the step
	 */
	public OTKAuthUrl(UrlBuilder url, 
			String user, 
			String key,
			long epoch,
			long step) {
		mUrl = url;
		mUser = user;
		mKey = key;
		mEpoch = epoch;
		mStep = step;
	}

	/**
	 * Gets the totp auth url.
	 *
	 * @return the totp auth url
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public final UrlBuilder getOTKAuthUrl() throws UnsupportedEncodingException {
		long time = TimeUtils.getCurrentTimeMs();

		long counter = TOTP.getCounter(time, mEpoch, mStep);

		if (counter != mCounter) {
			// Only update the auth url object when we change counter bins
			// since it will not change during the bin duration.
			
			//Generate an 6 digit totp code
			int totp = TOTP.generateCTOTP6(mKey, counter); //toptCounter256(mKey, counter);
			
			// Format the totp to ensure 8 digits
			String formattedTotp = String.format("%06d", totp);
			
			mRestAuthUrl = mUrl
					.param("user", mUser)
					.param("totp", formattedTotp);
					//.resolve(mUser)
					//.resolve(formattedTotp);

			mCounter = counter;
		}

		return mRestAuthUrl;
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
