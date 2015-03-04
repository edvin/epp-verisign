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

package com.verisign.epp.codec.rgppoll;

// W3C Imports
import org.w3c.dom.*;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;


/**
 * The EPPRgpPollStatus is the EPPCodecComponent that knows how to encode and
 * decode RGP Poll status elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Poll Mapping for the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPRgpPollStatus implements EPPCodecComponent {


    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPRgpPollStatus.class.getName(),
                                                       EPPCatFactory.getInstance().getFactory());


          /**
           * The constant value for addPeriod
           */
            public static final String ADD_PERIOD = "addPeriod";

            /**
             * The constant value for autoRenewPeriod
             */
            public static final String AUTO_RENEW_PERIOD = "autoRenewPeriod";

            /**
             * The constant value for renewPeriod
             */
            public static final String RENEW_PERIOD = "renewPeriod";

            /**
             * The constant value for transferPeriod
             */
            public static final String TRANSFER_PERIOD = "transferPeriod";

            /**
             * The constant value for pendingDelete
             */
            public static final String PENDING_DELETE = "pendingDelete";

            /**
             * The constant value for pendingRestore
             */
            public static final String PENDING_RESTORE = "pendingRestore";

            /**
             * The constant value for redemptionPeriod
             */
            public static final String REDEMPTION_PERIOD = "redemptionPeriod";


    /** Default Language -- English "en" */
	public final static java.lang.String ELM_DEFAULT_LANG = "en";

    /**
     * XML Element Language Attribute Name of <code>EPPRgpPollStatus</code> root
     * element.
     */
    final static java.lang.String ELM_LANG = "lang";


    /** XML Element Name of <code>EPPRgpPollStatus</code> root element. */
    final static java.lang.String ELM_NAME = "rgp-poll:rgpStatus";

    /**
     * XML Element Status Attribute Name of <code>EPPRgpPollStatus</code> root
     * element.
     */
    final static java.lang.String ELM_STATUS = "s";


    /**
     * The message of this status.
     */
    private String message = null;

    /**
     * The status value of this status comonent
     */
    private String status = ADD_PERIOD;

    /**
     * The language value of this status component
     */
    private String lang = "en";


    /**
      * Create a new instance of EPPRgpPollStatus
      *
      */
    public EPPRgpPollStatus() {}

    /**
      * Create a new instance of EPPRgpPollStatus with the given
      * status
      * @param aStatus the status value to use for this instance.  Should
      * use one of the static constants defined for this class as a value.
      */

    public EPPRgpPollStatus(String aStatus) {
        status = aStatus;
    }

    /**
     * Append all data from this RGP inf data to the given DOM Document
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
            cat.error("EPPRgpPollStatus.doEncode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpPollStatus invalid state: "
                                         + e);
        }

        // Status with Attributes
        Element root =
            aDocument.createElementNS(EPPRgpPollMapFactory.NS, ELM_NAME);

        // add attribute status
        root.setAttribute(ELM_STATUS, status);

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
     * Populate the data of this instance with the data stored in the
     * given Element of the DOM tree
     *
     * @param aElement The root element of the report fragment of XML
     * @throws EPPDecodeException Thrown if any errors occur during decoding.
     */
    public void decode(Element aElement) throws EPPDecodeException {

        // Status
        status = aElement.getAttribute(ELM_STATUS);

        // msgNode
        Node msgNode = aElement.getFirstChild();

        if (msgNode != null) {

            message     = msgNode.getNodeValue();
            // Description Language
            lang = aElement.getAttribute(ELM_LANG);
        }
    }

    /**
     * implements a deep <code>EPPRgpPollStatus</code> compare.
     *
     * @param aObject <code>EPPRgpPollStatus</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {
        if (! (aObject instanceof EPPRgpPollStatus)) {
            return false;
        }

        EPPRgpPollStatus theComp = (EPPRgpPollStatus) aObject;

        // status
        if (!status.equals(theComp.status)) {
            return false;
        }

        // description
        if (
            ! (
            (message == null) ? (theComp.message == null)
            : message.equals(theComp.message)
            )) {
            return false;
        }

        // lang
        if (!lang.equals(theComp.lang)) {
            return false;
        }
        return true;
    }

    /**
     * Validate the state of the <code>EPPRgpPollStatus</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //status
        if (status == null) {
            throw new EPPCodecException("EPPRgpPollStatus required attribute is not set");
        }
    }

    /**
     * Clone <code>EPPRgpPollStatus</code>.
     *
     * @return clone of <code>EPPRgpPollStatus</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpPollStatus clone = null;

        clone = (EPPRgpPollStatus) super.clone();

        clone.message = message;
        clone.lang = lang;
        clone.status = status;

        return clone;
    }



    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getLang() {
        return lang;
    }
    public void setLang(String lang) {
        this.lang = lang;
    }

}