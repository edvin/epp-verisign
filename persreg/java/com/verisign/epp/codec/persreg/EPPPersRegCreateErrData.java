/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

import java.util.Hashtable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;


/**
 * Personal Registration &ltcreErrData&gt extension element to  an error EPP
 * Create Response.  The error code and message is currently associated with
 * an EPP response code of  2305 "Object association prohibits operation", and
 * can have one of the <code>EPPPersRegCreateErrData</code><code>ERROR</code>
 * constant values.  Optionally, a "lang" attribute can be provide to indicate
 * the language.  The default value for "lang" is "en" (English). <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPPersRegCreateErrData implements EPPCodecComponent {
	/** The default language of the error message "en". */
	public static final String DEFAULT_LANG = "en";

	/** Corresponding service exists */
	public static final int ERROR_CS_EXISTS = 1;

	/** Conflicting defensive registration exists */
	public static final int ERROR_DEFREG_EXISTS = 2;

	/** XML root tag for <code>EPPPersRegCreateErrData</code>. */
	public static final String ELM_NAME = "persReg:creErrData";

	/**
	 * Hash that maps the pre-defined result codes to their associated message
	 * in     the default language of "en".
	 */
	private static Hashtable _defaultMsg;

	// Static Initializer for EPPPersRegCreateErrData
	static {
		_defaultMsg = new Hashtable();
		_defaultMsg.put(
						new Integer(ERROR_CS_EXISTS),
						"Corresponding service exists");
		_defaultMsg.put(
						new Integer(ERROR_DEFREG_EXISTS),
						"Conflicting defensive registration exists");
	}

	/** XML tag name for the <code>_message</code> element. */
	private final static String ELM_MSG = "persReg:msg";

	/** XML attribute name for the result <code>_code</code> attribute. */
	private final static String ATTR_CODE = "code";

	/**
	 * XML attribute name for the optional result <code>_lang</code> attribute.
	 */
	private final static String ATTR_LANG = "lang";

	/** Error code */
	private int _code = 0;

	/**
	 * Error message associated with _code.  A default English  message is set
	 * by default.
	 */
	private String _message = "";

	/** Language of the <code>_message</code> attribute. */
	private String _lang = DEFAULT_LANG;

	/**
	 * Default constructor.  The error code is set to -1.
	 */
	public EPPPersRegCreateErrData() {
		// Do nothing
	}

	// End EPPPersRegCreateErrData.EPPPersRegCreateErrData()

	/**
	 * Constructor that sets the error code.  Use of the the <code>ERROR</code>
	 * constants for the error code.
	 *
	 * @param aCode Error code
	 */
	public EPPPersRegCreateErrData(int aCode) {
		_code		 = aCode;
		_message     = (String) _defaultMsg.get(new Integer(aCode));
	}

	// End EPPPersRegCreateErrData.EPPPersRegCreateErrData(int)

	/**
	 * Gets the error code.
	 *
	 * @return Error code that should be one of the <code>ERROR</code> constant
	 * 		   values.
	 */
	public int getCode() {
		return _code;
	}

	// End EPPPersRegCreateErrData.getCode()

	/**
	 * Sets the error code.
	 *
	 * @param aCode Error code that should be one of the <code>ERROR</code>
	 * 		  constant values.
	 */
	public void setCode(int aCode) {
		_code = aCode;
	}

	// End EPPPersRegCreateErrData.setCode(int)

	/**
	 * Sets the error code and the default "en" message associated with the
	 * error code if <code>aUserDefaultMessage</code> is set to
	 * <code>true</code>.
	 *
	 * @param aCode Error code that should be one of the <code>ERROR</code>
	 * 		  constant values.
	 * @param aUseDefaultMessage Use the default en message associated with
	 * 		  aCode?
	 */
	public void setCode(int aCode, boolean aUseDefaultMessage) {
		_code = aCode;

		if (aUseDefaultMessage) {
			_message = (String) _defaultMsg.get(new Integer(aCode));
		}
	}

	// End EPPPersRegCreateErrData.setCode(int, boolean)

	/**
	 * Gets the error message.
	 *
	 * @return Error message associated with the error code in the specified
	 * 		   language.
	 */
	public String getMessage() {
		return _message;
	}

	// End EPPPersRegCreateErrData.getMessage()

	/**
	 * Sets the error message.  This should only be called if  the default "en"
	 * language message is not valid.
	 *
	 * @param aMessage Error message associated with the error code in the
	 * 		  specified language.
	 */
	public void setMessage(String aMessage) {
		_message = aMessage;
	}

	// End EPPPersRegCreateErrData.setMessage(String)

	/**
	 * Gets the error message language.  The Language must be   structured as
	 * documented in  <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @return Error message language.
	 */
	public String getLang() {
		return _lang;
	}

	// End EPPPersRegCreateErrData.getLang()

	/**
	 * Sets the error message language.  The Language must be   structured as
	 * documented in  <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @param aLang DOCUMENT ME!
	 */
	public void setLang(String aLang) {
		_lang = aLang;
	}

	// End EPPPersRegCreateErrData.setLang(String)

	/**
	 * encode instance into a DOM element tree.    A DOM Document is passed as
	 * an argument and functions  as a factory for DOM objects.  The root
	 * element associated  with the instance is created and each instance
	 * attributeis  appended as a child node.
	 *
	 * @param aDocument DOM Document, which acts is an Element factory
	 *
	 * @return Element Root element associated with the object
	 *
	 * @exception EPPEncodeException Error encoding
	 * 			  <code>EPPPersRegCreate</code>
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element currElm;
		Text    currVal;

		// Validate state
		if (_code == -1) {
			throw new EPPEncodeException("code required attribute is not set");
		}

		if (_message == null) {
			throw new EPPEncodeException("message required attribute is not set");
		}

		Element root =
			aDocument.createElementNS(EPPPersRegExtFactory.NS, ELM_NAME);
		root.setAttribute("xmlns:persReg", EPPPersRegExtFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPPersRegExtFactory.NS_SCHEMA);

		// Message
		currElm     = aDocument.createElementNS(
												EPPPersRegExtFactory.NS, ELM_MSG);
		currVal = aDocument.createTextNode(_message);

		// Code
		currElm.setAttribute(ATTR_CODE, _code + "");

		// Lang
		if (!_lang.equals(DEFAULT_LANG)) {
			currElm.setAttribute(ATTR_LANG, _lang);
		}

		currElm.appendChild(currVal);
		root.appendChild(currElm);

		return root;
	}

	// End EPPPersRegCreateErrData.encode(Document)

	/**
	 * decode a DOM element tree to initialize the  instance attributes.  The
	 * <code>aElement</code> argument  represents the root DOM element and is
	 * used to traverse  the DOM nodes for instance attribute values.
	 *
	 * @param aElement <code>Element</code> to decode
	 *
	 * @exception EPPDecodeException Error decoding <code>Element</code>
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element  currElm;

		// Message
		currElm = EPPUtil.getElementByTagNameNS(aElement,
				EPPPersRegExtFactory.NS, ELM_MSG);

		if (currElm == null) {
			throw new EPPDecodeException("Required EPPPersRegCreateErrData element "
										 + ELM_MSG + " not found");
		}

		_message = currElm.getFirstChild().getNodeValue();

		if (_message == null) {
			throw new EPPDecodeException("Required message value of EPPPersRegCreateErrData element "
										 + ELM_MSG + " not found");
		}

		// Lang
		setLang(currElm.getAttribute(ATTR_LANG));

		// Code
		_code = Integer.parseInt(currElm.getAttribute(ATTR_CODE));
	}

	// End EPPPersRegCreateErrData.decode(Element)

	/**
	 * Compare an instance of <code>EPPPersRegCreateErrData</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPPersRegCreateErrData)) {
			return false;
		}

		EPPPersRegCreateErrData theComp = (EPPPersRegCreateErrData) aObject;

		// _code
		if (_code != theComp._code) {
			return false;
		}

		// _message
		if (
			!(
					(_message == null) ? (theComp._message == null)
										   : _message.equals(theComp._message)
				)) {
			return false;
		}

		// _lang
		if (!_lang.equals(theComp._lang)) {
			return false;
		}

		return true;
	}

	// End EPPPersRegCreateErrData.equals(Object)

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 *
	 * @return clone of concrete <code>EPPPersRegCreateErrData</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPersRegCreateErrData clone = (EPPPersRegCreateErrData) super.clone();

		return clone;
	}

	// End EPPPersRegCreateErrData.clone()

	/**
	 * Gets the root element name.
	 *
	 * @return "persReg:creErrData"
	 */
	protected String getRootElm() {
		return ELM_NAME;
	}

	// End EPPPersRegCreateErrData.getRootElm()
}


// End class EPPPersRegCreateErrData
