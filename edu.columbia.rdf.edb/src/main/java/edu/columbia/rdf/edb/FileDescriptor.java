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

import java.util.Date;

import org.abh.common.file.Io;

/**
 * A file descriptor represents a file available on the EDB. It does not
 * map to a file on a the local file system, but rather is a reference to
 * a file available through the database. Furthermore the file referenced
 * is by a VFS file id which can be used to determine the 
 */
public class FileDescriptor extends Dated {

	/** The m type. */
	private FileType mType;

	/**
	 * Instantiates a new file descriptor.
	 *
	 * @param id the id
	 * @param name the name
	 * @param type the type
	 * @param date the date
	 */
	public FileDescriptor(int id, String name, FileType type, Date date) {
		super(id, name, date);
		
		mType = type;
	}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public FileType getType() {
		return mType;
	}

	/**
	 * Gets the ext.
	 *
	 * @return the ext
	 */
	public String getExt() {
		return Io.getFileExt(mName);
	}

}