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

import org.w3c.dom.Element;
import org.w3c.dom.Document;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;

/**
 *
 * A concrete EPPResponse that knows how to encode/decode RGP Poll responses
 * from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Poll Mapping for the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPRgpPollResponse
    extends EPPResponse {

    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPRgpPollResponse.class.getName(),
                                                       EPPCatFactory.getInstance().getFactory());


          /**
           * The poll data XML tag name
           */
    public static final String ELM_NAME= "rgp-poll:pollData";

    /**
     * The domain name XML tag name
     */
    public static final String ELM_DOMAIN_NAME = "rgp-poll:name";

    /**
     * The request Date XML tag name
     */
    public static final String ELM_REQ_DATE = "rgp-poll:reqDate";

    /**
     * The report Due Date XML tag name
     */
    public static final String ELM_DUE_DATE = "rgp-poll:reportDueDate";

    /**
     * The RGP status of the domain name
     */
    private EPPRgpPollStatus status;

    /**
     * The domain name
     */
    private String name;

    /**
     * The date the restore was requested
     */
    private java.util.Date reqDate;

    /**
     * The date the restore report is due
     */
    private java.util.Date reportDueDate;

    /**
     * Create a new instance of EPPRgpPollResponse
     */
    public EPPRgpPollResponse() {}

    /**
     * Put all data contained in this poll resonse into the given XML document
     *
     * @param aDocument the DOM Document to attach data to.
     * @return the element that is a parent of the poll data
     * @throws EPPEncodeException Thrown if any errors occur during the encode
     * process
     */
    public Element doEncode(Document aDocument) throws EPPEncodeException {

        try {
            //Validate State
            validateState();
        }
        catch (EPPCodecException e) {
            cat.error("EPPRgpPollData.doEncode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpPollData invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
                " on in EPPRgpPollData.doEncode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPRgpPollMapFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:rgp-poll", EPPRgpPollMapFactory.NS);
        root.setAttributeNS(
            EPPCodec.NS_XSI, "xsi:schemaLocation",
            EPPRgpPollMapFactory.NS_SCHEMA);

        // name
        EPPUtil.encodeString(aDocument, root, name, EPPRgpPollMapFactory.NS,
                             ELM_DOMAIN_NAME);

        // status
        EPPUtil.encodeComp(aDocument, root, status);

        // reqDate
        EPPUtil.encodeTimeInstant(aDocument, root, reqDate,
                                  EPPRgpPollMapFactory.NS, ELM_REQ_DATE);

        // dueDate
        EPPUtil.encodeTimeInstant(aDocument, root, reportDueDate,
                                  EPPRgpPollMapFactory.NS, ELM_DUE_DATE);

        return root;

    }

    /**
     * Populates the data of this instance from the given XML Element which is
     * part of a DOM Document
     *
     * @param aElement the element that is a parent of the poll data
     * @throws EPPDecodeException thrown if any errors occur during the decode operation
     */
    public void doDecode(Element aElement) throws EPPDecodeException {

        name = EPPUtil.decodeString(aElement, EPPRgpPollMapFactory.NS, ELM_DOMAIN_NAME);

        status = (EPPRgpPollStatus)EPPUtil.decodeComp(aElement, EPPRgpPollMapFactory.NS,
                                    EPPRgpPollStatus.ELM_NAME, EPPRgpPollStatus.class);

        reqDate = EPPUtil.decodeTimeInstant(aElement,EPPRgpPollMapFactory.NS, ELM_REQ_DATE);



        reportDueDate = EPPUtil.decodeTimeInstant(aElement,EPPRgpPollMapFactory.NS, ELM_DUE_DATE);
    }

    /**
        * implements a deep <code>EPPRgpPollResponse</code> compare.
        *
        * @param aObject <code>EPPRgpPollResponse</code> instance to compare with
        *
        * @return true if equal false otherwise
        */
       public boolean equals(Object aObject) {
           if (! (aObject instanceof EPPRgpPollResponse)) {
               return false;
           }

           EPPRgpPollResponse theComp = (EPPRgpPollResponse) aObject;

           // status
           if (!((status == null) ? (theComp.status == null) : status.equals(theComp.status)
               )) {
               return false;
           }

           // name
           if (!((name == null) ? (theComp.name == null) : name.equals(theComp.name)
               )) {
               return false;
           }

           // reqDate
           if (!((reqDate == null) ? (theComp.reqDate == null) : reqDate.equals(theComp.reqDate)
               )) {
               return false;
           }

           // dueDate
           if (!((reportDueDate == null) ? (theComp.reportDueDate == null) : reportDueDate.equals(theComp.reportDueDate)
               )) {
               return false;
           }
           return true;
       }

       /**
        * Clone <code>EPPRgpPollResponse</code>.
        *
        * @return clone of <code>EPPRgpPollResponse</code>
        *
        * @exception CloneNotSupportedException standard Object.clone exception
        */
       public Object clone() throws CloneNotSupportedException {

           EPPRgpPollResponse clone = null;

           clone = (EPPRgpPollResponse) super.clone();

           clone.name = name;
           clone.reqDate = reqDate;
           clone.reportDueDate = reportDueDate;

           if (status != null) {
               clone.status = (EPPRgpPollStatus)status.clone();
           }
           return clone;
       }


    /**
     * Validate the state of the <code>EPPRgpPollData</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //name
        if (name == null) {
            throw new EPPCodecException("EPPRgpPollData required attribute is not set");
        }
        //status
        if (status == null) {
            throw new EPPCodecException("EPPRgpPollData required attribute is not set");
        }

        //reqDate
        if (reqDate == null) {
            throw new EPPCodecException("EPPRgpPollData required attribute is not set");
        }

        //reportDueDate
        if (reportDueDate == null) {
            throw new EPPCodecException("EPPRgpPollData required attribute is not set");
        }
    }

    public EPPRgpPollStatus getStatus() {
        return status;
    }

    public void setStatus(EPPRgpPollStatus aStatus) {
        status = aStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public java.util.Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(java.util.Date reqDate) {
        this.reqDate = reqDate;
    }

    public java.util.Date getReportDueDate() {
        return reportDueDate;
    }

    public void setReportDueDate(java.util.Date reportDueDate) {
        this.reportDueDate = reportDueDate;
    }
}