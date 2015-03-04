/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/

package com.verisign.epp.codec.whowas;

/**
 * All WhoWas constants.
 * 
 * @author Deepak Deshpande
 * @version 1.0
 */
public interface EPPWhoWasConstants {

	/** XML Element Name for the <code>type</code>. */
	public static final String ELM_TYPE = "whowas:type";

	/** XML Element Name for the <code>name</code>. */
	public final static String ELM_NAME = "whowas:name";

	/** XML Element Name for the <code>roid</code>. */
	public final static String ELM_ROID = "whowas:roid";

	/** XML Element Name of {@link EPPWhoWasHistory} root element. */
	public static final String ELM_HISTORY = "whowas:history";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC = "whowas:rec";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC_DATE = "whowas:date";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC_NEW_NAME = "whowas:newName";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC_OP = "whowas:op";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC_CLID = "whowas:clID";

	/** XML Element Name of {@link EPPWhoWasRecord} root element. */
	public static final String ELM_REC_CLNAME = "whowas:clName";

	/** Default type for {@link EPPWhoWasInfoCmd} and {@link EPPWhoWasInfoResp}. */
	public static final String TYPE_DOMAIN = "domain";

	/** {@link EPPWhoWasRecord} Create operation name. */
	public static final String OP_CREATE = "CREATE";

	/** {@link EPPWhoWasRecord} Delete operation name. */
	public static final String OP_DELETE = "DELETE";

	/** {@link EPPWhoWasRecord} Transfer operation name. */
	public static final String OP_TRANSFER = "TRANSFER";

	/** {@link EPPWhoWasRecord} Server Transfer operation name. */
	public static final String OP_SERVER_TRANSFER = "SERVER TRANSFER";
}
