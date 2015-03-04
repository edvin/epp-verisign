/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/

package com.verisign.epp.codec.rgpext;

// DOM imports
import org.w3c.dom.*;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;



/**
 * The EPPRgpExtReportText is the EPPCodecComponent that knows how to encode and
 * decode RGP report text elements from/to XML and object instance. Does both
 * statements and reasons.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPRgpExtReportText implements EPPCodecComponent {

    /**
     * Log4j category for logging
     */
    private static Logger cat =
        Logger.getLogger( EPPRgpExtReportText.class.getName(),
        EPPCatFactory.getInstance().getFactory());

    /** Default Language -- English "en" */
    public final static java.lang.String ELM_DEFAULT_LANG = "en";

    /**
     * XML Element Language Attribute Name of <code>EPPRgpExtStatus</code> root
     * element.
     */
    final static java.lang.String ELM_LANG = "lang";

    /**
     * Element tag name for the resReason Element
     */
    public static final String ELM_RES_REASON = "resReason";

    /**
     * Element tag name for the statement Element
     */
    public static final String ELM_STATEMENT = "statement";

    /**
     * The default language - "en"
     */
    private String lang = ELM_DEFAULT_LANG;

    /**
     * The body of this report text
     */
    private String message;


    /**
     * Utility tag name attribute so we can switch encoding back and forth
     * from statements and reasons using the local name.
     */
    private String localName;

    /**
     * Create a new instance of EPPRgpExtReportText
     */
    public EPPRgpExtReportText() {}

    /**
     * Create a new instance of EPPRgpExtReportText with the
     * given message string.
     *
     * @param aMessage The message value to set
     */
    public EPPRgpExtReportText(String aMessage) {
        message = aMessage;
    }

    /**
     * Attach all data to the DOM Document passed in.
     *
     * @param aDocument The DOM Document to append data to
     * @return Encoded DOM <code>Element</code>
     * @throws EPPEncodeException Thrown when errors occur during the
     * encode attempt or if the instance is invalid.
     */
    public Element encode(Document aDocument) throws EPPEncodeException {

        try {
            //Validate States
            validateState();
        }
         catch (EPPCodecException e) {
            cat.error("EPPRgpExtReport.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpExtReport invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " on in EPPRgpExtReport.encode(Document)");
        }

        Element root = null;
		if (localName.equals(ELM_RES_REASON)) {
			root = aDocument.createElementNS(EPPRgpExtFactory.NS,
					EPPRgpExtFactory.NS_PREFIX + ":" + ELM_RES_REASON);
		}
		else if (localName.equals(ELM_STATEMENT)) {
			root = aDocument.createElementNS(EPPRgpExtFactory.NS,
					EPPRgpExtFactory.NS_PREFIX + ":" + ELM_STATEMENT);
		}
		else {
			throw new EPPEncodeException(
					"Method encode() value for tagname is invalid.  "
							+ "Should be either rgp:statement or rgp:resReason");
		}

        // Description specified?
        if (message != null) {
            // Non-default language specified?
            if (!lang.equals(ELM_DEFAULT_LANG)) {
                root.setAttribute(ELM_LANG, lang);
            }

            Text descVal = aDocument.createTextNode(message);
            root.appendChild(descVal);
        }
        return root;
    }

    /**
     * Validate the state of the <code>EPPRgpExtReportText</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPRgpExtReportText</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //status
        if (message == null) {
            throw new EPPCodecException("EPPRgpExtReportText required attribute" +
                                        " message is not set " );
        }
    }
    /**
     * Populate the data of this instance with the data stored in the
     * given Element of the DOM tree
     *
     * @param aElement The root element of the report fragment of XML
     * @throws EPPDecodeException Thrown if any errors occur during decoding.
     */
    public void decode(Element aElement) throws EPPDecodeException {

        localName = aElement.getLocalName();

        // msgNode
        Node msgNode = aElement.getFirstChild();

        if (msgNode != null) {

            message     = msgNode.getNodeValue();
            // Description Language
            lang = aElement.getAttribute(ELM_LANG);
        }
    }

    /**
     * Implements a deep <code>EPPRgpExtReportText/code> compare.
     *
     * @param aObject <code>EPPRgpExtReportText</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPRgpExtReportText)) {
            return false;
        }
        EPPRgpExtReportText theComp = (EPPRgpExtReportText) aObject;

        // message
        if (! ( (message == null) ? (theComp.message == null) :
               message.equals(theComp.message)
               )) {
            return false;
        }
        return true;
    }


    /**
     * Clone <code>EPPRgpExtReportText</code>.
     *
     * @return clone of <code>EPPRgpExtReportText</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpExtReportText clone = null;
        clone = (EPPRgpExtReportText)super.clone();

        clone.localName = localName;
        clone.message = message;
        clone.lang = lang;

        return clone;
    }

    /**
     * Get the value of the lang attribute
     * @return the value of the lang attribute
     */
    public String getLang() {
        return lang;
    }

    /**
     * Sets the value of the long attribute
     * @param lang the new value of the lang attribute
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Get the value of the message attribute
     * @return the value of the message attribute
     */
    public String getMessage() {
        return message;
    }

    /**
     * set the value of the message attribute
     * @param message the value of the message attribute
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the current local name without the namespace prefix. "statement" or "reason"
     * @return the current tage name.
     */
    public String getLocalName() {
        return localName;
    }

    /**
     * Set the current local name without the namespace prefix.
     * @param localName the new value of the tag name.
     */
    public void setLocalName(String localName) {
        this.localName = localName;
  }
    

}