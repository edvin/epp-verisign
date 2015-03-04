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
package com.verisign.epp.codec.emailFwd;

import org.w3c.dom.Document;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// W3C Imports
import org.w3c.dom.Element;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPP EmailFwd &lt;info&gt; command that is used to retrieve
 * information associated with  a emailFwd.  The &lt;emailFwd:info&gt; element
 * MUST     contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;emailFwd:name&gt; element that contains the fully qualified emailFwd
 * name for which information is requested. Use <code>getName</code> and
 * <code>setName</code> to get and set the element.
 * </li>
 * </ul>
 * 
 * <br><code>EPPEmailFwdInfoResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPEmailFwdInfoCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp
 */
public class EPPEmailFwdInfoCmd extends EPPInfoCmd {
	/** XML Element Name of <code>EPPEmailFwdInfoCmd</code> root element. */
	final static String ELM_NAME = "emailFwd:info";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_EMAILFWD_NAME = "emailFwd:name";

	/** EmailFwd Name to get information on. */
	private String name;
//EPPUPGRADE01 CR
/** authorization information. */
        private EPPAuthInfo authInfo = null;




	/**
	 * <code>EPPEmailFwdInfoCmd</code> default constructor.  The name is
	 * initialized to <code>null</code>.     The name must be set before
	 * invoking <code>encode</code>.
	 */
	public EPPEmailFwdInfoCmd() {
		name = null;
	}

	// End EPPEmailFwdInfoCmd.EPPEmailFwdInfoCmd()

	/**
	 * <code>EPPEmailFwdInfoCmd</code> constructor that takes the emailFwd name
	 * as an argument.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aName EmailFwd name to get information on.
	 */
	public EPPEmailFwdInfoCmd(String aTransId, String aName) {
		super(aTransId);

		name = aName;
	}

	// End EPPEmailFwdInfoCmd.EPPEmailFwdInfoCmd(String, String)

/**
         * <code>EPPEmailFwdInfoCmd</code> constructor that takes the emailFwd name, authInfo as
         *  arguments.
         *
         * @param aTransId Transaction Id associated with command.
         * @param aName EmailFwd name to get information on.
         */
        public EPPEmailFwdInfoCmd(String aTransId, String aName, EPPAuthInfo aAuthInfo) {
                this(aTransId, aName);

                authInfo = aAuthInfo;
                authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
        }





	/**
	 * Gets the EPP command Namespace associated with
	 * <code>EPPEmailFwdInfoCmd</code>.
	 *
	 * @return <code>EPPEmailFwdMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPEmailFwdMapFactory.NS;
	}

	// End EPPEmailFwdInfoCmd.getNamespace()

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPEmailFwdInfoCmd</code> instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the
	 * 		   <code>EPPEmailFwdInfoCmd</code> instance.
	 *
	 * @exception EPPEncodeException Unable to encode
	 * 			  <code>EPPEmailFwdInfoCmd</code> instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		// Validate state
		if (name == null) {
			throw new EPPEncodeException("required attribute name is not set");
		}

		Element root =
			aDocument.createElementNS(EPPEmailFwdMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:emailFwd", EPPEmailFwdMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPEmailFwdMapFactory.NS_SCHEMA);

		// Name
		EPPUtil.encodeString(
							 aDocument, root, name, EPPEmailFwdMapFactory.NS,
							 ELM_EMAILFWD_NAME);
 // Authorization Info
                if (authInfo != null) {
                        EPPUtil.encodeComp(aDocument, root, authInfo);
                }

		return root;
	}

	// End EPPEmailFwdInfoCmd.doEncode(Document)

	/**
	 * Decode the <code>EPPEmailFwdInfoCmd</code> attributes from the aElement
	 * DOM Element tree.
	 *
	 * @param aElement Root DOM Element to decode
	 * 		  <code>EPPEmailFwdInfoCmd</code> from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		// EmailFwd Name
		name =
			EPPUtil.decodeString(
								 aElement, EPPEmailFwdMapFactory.NS,
								 ELM_EMAILFWD_NAME);
 // Authorization Info
                authInfo =     (EPPAuthInfo) EPPUtil.decodeComp(
                                                                                         aElement, EPPEmailFwdMapFactory.NS,
                                                                                         EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO,
                                                                                         EPPAuthInfo.class);

	}

	// End EPPEmailFwdInfoCmd.doDecode(Node)

	/**
	 * Gets the emailFwd name to get information on.
	 *
	 * @return EmailFwd Name    <code>String</code> instance if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getName() {
		return name;
	}

	// End EPPEmailFwdInfoCmd.getName()

	/**
	 * Sets the emailFwd name to get information on.
	 *
	 * @param aName EmailFwd Name
	 */
	public void setName(String aName) {
		name = aName;
	}

	// End EPPEmailFwdInfoCmd.setName(String)

 /**
         * Get authorization information
         *
         * @return Authorization information if defined; <code>null</code>
         *                 otherwise;
         */
        public EPPAuthInfo getAuthInfo() {
                return authInfo;
        }

        // End EPPEmailFwdInfoCmd.getAuthInfo()

  /**
         * Set authorization information
         *
         * @param aAuthInfo EPPAuthInfo
         */
        public void setAuthInfo(EPPAuthInfo aAuthInfo) {
                if (aAuthInfo != null) {
                        authInfo = aAuthInfo;
                        authInfo.setRootName(EPPEmailFwdMapFactory.NS, EPPEmailFwdMapFactory.ELM_EMAILFWD_AUTHINFO);
                }
        }

        // End EPPEmailFwdInfoCmd.setAuthInfo(EPPAuthInfo)



	/**
	 * Compare an instance of <code>EPPEmailFwdInfoCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPEmailFwdInfoCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPEmailFwdInfoCmd theComp = (EPPEmailFwdInfoCmd) aObject;

		// Name
		if (
			!(
					(name == null) ? (theComp.name == null)
									   : name.equals(theComp.name)
				)) {
			return false;
		}
 // Authorization Info
                if (
                        !(
                                        (authInfo == null) ? (theComp.authInfo == null)
                                                                                   : authInfo.equals(theComp.authInfo)
                                )) {
                        //cat.error("EPPEmailFwdInfoCmd.equals(): authInfo not equal");
                        return false;
                }




		return true;
	}

	// End EPPEmailFwdInfoCmd.equals(Object)

	/**
	 * Clone <code>EPPEmailFwdInfoCmd</code>.
	 *
	 * @return clone of <code>EPPEmailFwdInfoCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPEmailFwdInfoCmd clone = (EPPEmailFwdInfoCmd) super.clone();

		return clone;
	}

	// End EPPEmailFwdInfoCmd.clone()

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

	// End EPPEmailFwdInfoCmd.toString()
}
