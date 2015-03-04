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
package com.verisign.epp.codec.nameWatch;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP NameWatch &lt;info&gt; command that is used to retrieve
 * information associated with  a nameWatch.  The &lt;nameWatch:info&gt;
 * element MUST     contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;nameWatch:roid&gt; element that contains the fully qualified nameWatch
 * name for which information is requested. Use <code>getRoid</code> and
 * <code>setRoid</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPNameWatchInfoResp</code> is the concrete
 * <code>EPPReponse</code> associated     with
 * <code>EPPNameWatchInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 *
 * @see com.verisign.epp.codec.nameWatch.EPPNameWatchInfoResp
 */
public class EPPNameWatchInfoCmd extends EPPInfoCmd {
	/** XML Element Name of <code>EPPNameWatchInfoCmd</code> root element. */
	final static String ELM_NAME = "nameWatch:info";

	/** XML Element Name for the <code>roid</code> attribute. */
	private final static String ELM_ROID = "nameWatch:roid";

	/** NameWatch roid to get information on. */
	private String roid;

	//EPPUPGRADE10
	/** authorization information. */
        private EPPAuthInfo authInfo = null;

	/**
	 * <code>EPPNameWatchInfoCmd</code> default constructor.  The roid is
	 * initialized to <code>null</code>.     The roid must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPNameWatchInfoCmd() {
		roid = null;
	}

	// End EPPNameWatchInfoCmd.EPPNameWatchInfoCmd()

	/**
	 * <code>EPPNameWatchInfoCmd</code> constructor that takes the nameWatch
	 * roid as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aRoid NameWatch roid to get information on.
	 */
	public EPPNameWatchInfoCmd(String aTransId, String aRoid) {
		super(aTransId);

		roid = aRoid;
	}

	/**
         * <code>EPPNameWatchInfoCmd</code> constructor that takes the roid and 
         * authinfo of the NameWatch object.  
         *
         * @param aTransId Transaction Id associated with command.
	     * @param aRoid NameWatch roid to get information on.
         * @param aAuthInfo Authorization info for the NameWatch object
         */
        public EPPNameWatchInfoCmd(String aTransId, String aRoid, EPPAuthInfo aAuthInfo) {
                this(aTransId, aRoid);

                authInfo = aAuthInfo;
                authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
        }


	// End EPPNameWatchInfoCmd.EPPNameWatchInfoCmd(String, String)

	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPNameWatchInfoCmd</code>.
	 *
	 * @return <code>EPPNameWatchMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPNameWatchMapFactory.NS;
	}

	// End EPPNameWatchInfoCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPNameWatchInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPNameWatchInfoCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPNameWatchInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (roid == null) {
			throw new EPPEncodeException("required attribute roid is not set");
		}

		Element root =
			aDocument.createElementNS(EPPNameWatchMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:nameWatch", EPPNameWatchMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPNameWatchMapFactory.NS_SCHEMA);

		// roid
		EPPUtil.encodeString(
							 aDocument, root, roid, EPPNameWatchMapFactory.NS,
							 ELM_ROID);

		// Authorization Info
                if (authInfo != null) {
                        EPPUtil.encodeComp(aDocument, root, authInfo);
                }

		return root;
	}

	// End EPPNameWatchInfoCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPNameWatchInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPNameWatchInfoCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// NameWatch roid
		roid =
			EPPUtil.decodeString(aElement, EPPNameWatchMapFactory.NS, ELM_ROID);

		if (roid == null) {
			throw new EPPDecodeException("roid required attribute is not set");
		}

		//EPPUPGRADE10
		// Authorization Info
                authInfo =
                        (EPPAuthInfo) EPPUtil.decodeComp(
                                aElement, EPPNameWatchMapFactory.NS,EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO,
                                                                   EPPAuthInfo.class);

	}

	// End EPPNameWatchInfoCmd.doDecode(Node)

	/**
	 * Gets the nameWatch roid
	 *
	 * @return NameWatch roid
	 */
	public String getRoid() {
		return roid;
	}

	// End EPPNameWatchInfoCmd.getRoid()

	/**
	 * Sets the nameWatch roid
	 *
	 * @param aRoid NameWatch roid
	 */
	public void setRoid(String aRoid) {
		roid = aRoid;
	}
	// End EPPNameWatchInfoCmd.setRoid(String)
	
	/**
         * Get authorization information
         *
         * @return Authorization information if defined; <code>null</code>
         *                 otherwise;
         */
	//EPPUPGRADE10
        public EPPAuthInfo getAuthInfo() {
                return authInfo;
        }

	/**
         * Set authorization information
         *
         * @param aAuthInfo EPPAuthInfo
         */
        public void setAuthInfo(EPPAuthInfo aAuthInfo) {
                if (aAuthInfo != null) {
                        authInfo = aAuthInfo;
                        authInfo.setRootName(EPPNameWatchMapFactory.NS, EPPNameWatchMapFactory.ELM_NAMEWATCH_AUTHINFO);
                }
        }


	/**
	 * Compare an instance of <code>EPPNameWatchInfoCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPNameWatchInfoCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPNameWatchInfoCmd theComp = (EPPNameWatchInfoCmd) aObject;

		// roid
		if (
			!(
					(roid == null) ? (theComp.roid == null)
									   : roid.equals(theComp.roid)
				)) {
			return false;
		}

		 // Authorization Info
		//EPPUPGRADE10
                if (
                        !(
                                        (authInfo == null) ? (theComp.authInfo == null)
                                                                                   : authInfo.equals(theComp.authInfo)
                                )) {
                        return false;
                }

		return true;
	}

	// End EPPNameWatchInfoCmd.equals(Object)

	/**
	 * Clone <code>EPPNameWatchInfoCmd</code>.
	 *
	 * @return clone of <code>EPPNameWatchInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPNameWatchInfoCmd clone = (EPPNameWatchInfoCmd) super.clone();

		 if (authInfo != null) {
                        clone.authInfo = (EPPAuthInfo) authInfo.clone();
                }

		return clone;
	}

	// End EPPNameWatchInfoCmd.clone()

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPNameWatchInfoCmd.toString()
}
