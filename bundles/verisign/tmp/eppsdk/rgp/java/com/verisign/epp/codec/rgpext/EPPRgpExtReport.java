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

// JDK imports
import java.util.Date;

// DOM imports
import org.w3c.dom.Element;
import org.w3c.dom.Document;

// Java imports
import java.util.Vector;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;


/**
 * The EPPRgpExtReport is the EPPCodecComponent that knows how to encode and
 * decode RGP report elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPRgpExtReport implements EPPCodecComponent {

    /**
     * Log4j category for logging
     */
    private static Logger cat =
        Logger.getLogger( EPPRgpExtReport.class.getName(),
        EPPCatFactory.getInstance().getFactory());

    /**
     * Element tag name for the report
     */
    public static final String ELM_NAME = "rgp:report";

    /**
     * Element tag name for the preData element
     */
    public static final String ELM_PRE_DATA = "rgp:preData";

    /**
     * Element tag name for the postData Element
     */
    public static final String ELM_POST_DATA = "rgp:postData";

    /**
     * Element tag name for the preWhois element
     * @deprecated Use <code>ELM_PRE_DATA</code>
     */
    public static final String ELM_PRE_WHOIS = "rgp:preWhois";

    /**
     * Element tag name for the postWhois Element
      * @deprecated Use <code>ELM_POST_DATA</code>
     */
    public static final String ELM_POST_WHOIS = "rgp:postWhois";
    
    
    /**
     * Element tag name for the delTime element
     */
    public static final String ELM_DEL_TIME = "rgp:delTime";

    /**
     * Element tag name for the resTime element
     */
    public static final String ELM_RES_TIME = "rgp:resTime";

    /**
     * Element tag name for the other element
     */
    public static final String ELM_OTHER = "rgp:other";
    

    /**
     * The preData value
     */
    private String preData;

    /**
     * the postData value
     */
    private String postData;

    /**
     * The delete time Value
     */
    private java.util.Date deleteTime;
    /**
     * The restore time value
     */
    private java.util.Date restoreTime;

    /**
     * The statement1 value
     */
    private EPPRgpExtReportText statement1;

    /**
     * The statement 2 value
     */
    private EPPRgpExtReportText statement2;

    /**
     * The restore reason value
     */
    private EPPRgpExtReportText restoreReason;

    /**
     * The other value
     */
    private String other;

    /**
     * Instantiate a new instance of the EPPCodecComponent EPPRgpExtReport
     */
    public EPPRgpExtReport() {}

    /**
     * Creates <code>EPPRgpExtReport</code> with required attributes.
     * 
     * @param aPreData Pre data / whois
     * @param aPostData Post data / whois
     * @param aDeleteTime Deletion time
     * @param aRestoreTime Restore time
     * @param aRestoreReason Restore reason 
     * @param aStatement1 Statement 
     */
    public EPPRgpExtReport(String aPreData, String aPostData,
    			Date aDeleteTime, Date aRestoreTime, 
    			EPPRgpExtReportText aRestoreReason, EPPRgpExtReportText aStatement1) {
    		this.preData = aPreData;
    		this.postData = aPostData;
    		this.deleteTime = aDeleteTime;
    		this.restoreTime = aRestoreTime;
    		this.restoreReason = aRestoreReason;
    		this.statement1 = aStatement1;
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

        Element root =
            aDocument.createElementNS(EPPRgpExtFactory.NS, ELM_NAME);

        // preData
        EPPUtil.encodeString(aDocument, root, preData, EPPRgpExtFactory.NS, ELM_PRE_DATA);

        // postData
        EPPUtil.encodeString(aDocument, root, postData, EPPRgpExtFactory.NS, ELM_POST_DATA);

        // delete time
        EPPUtil.encodeTimeInstant(aDocument, root, deleteTime, EPPRgpExtFactory.NS, ELM_DEL_TIME);

        // restore time
        EPPUtil.encodeTimeInstant(aDocument, root, restoreTime, EPPRgpExtFactory.NS, ELM_RES_TIME);

        // restore reason
        EPPUtil.encodeComp(aDocument,root,restoreReason);

        // restore statement1
        EPPUtil.encodeComp(aDocument,root,statement1);

        // restore statement2
        EPPUtil.encodeComp(aDocument,root,statement2);

        // optional
        EPPUtil.encodeString(aDocument, root, other, EPPRgpExtFactory.NS, ELM_OTHER);

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

        preData = EPPUtil.decodeString(aElement, EPPRgpExtFactory.NS, ELM_PRE_DATA);
        
        // Only for backward compatible change to the latest RFC 3915
        if (preData == null) {
            preData = EPPUtil.decodeString(aElement, EPPRgpExtFactory.NS, ELM_PRE_WHOIS);       	
        }

        postData = EPPUtil.decodeString(aElement, EPPRgpExtFactory.NS, ELM_POST_DATA);

        // Only for backward compatible change to the latest RFC 3915
        if (postData == null) {
        	postData = EPPUtil.decodeString(aElement, EPPRgpExtFactory.NS, ELM_POST_WHOIS);       	
        }
        
        deleteTime = EPPUtil.decodeTimeInstant(aElement, EPPRgpExtFactory.NS, ELM_DEL_TIME);

        restoreTime = EPPUtil.decodeTimeInstant(aElement, EPPRgpExtFactory.NS, ELM_RES_TIME);


        restoreReason = (EPPRgpExtReportText)EPPUtil.decodeComp(aElement, EPPRgpExtFactory.NS,
                EPPRgpExtReportText.ELM_RES_REASON, EPPRgpExtReportText.class);

        Vector statements = EPPUtil.decodeCompVector(aElement,
            EPPRgpExtFactory.NS,
            EPPRgpExtReportText.ELM_STATEMENT,
            EPPRgpExtReportText.class);


        if (statements != null) {
            // just have one statement
            if (statements.size() == 1) {

                statement1 = (EPPRgpExtReportText)statements.elementAt(0);
            }
            // have 2 statements
            else {
                statement1 = (EPPRgpExtReportText)statements.elementAt(0);
                statement2 = (EPPRgpExtReportText)statements.elementAt(1);
            }
        }

        other = EPPUtil.decodeString(aElement, EPPRgpExtFactory.NS, ELM_OTHER);
    }

    /**
     * Implements a deep <code>EPPRgpExtReport/code> compare.
     *
     * @param aObject <code>EPPRgpExtReport</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPRgpExtReport)) {
            return false;
        }
        EPPRgpExtReport theComp = (EPPRgpExtReport) aObject;

        // preData
        if (! ( (preData == null) ? (theComp.preData == null) :
               preData.equals(theComp.preData)
               )) {
            return false;
        }

        // postData
        if (! ( (postData == null) ? (theComp.postData == null) :
               postData.equals(theComp.postData)
               )) {
            return false;
        }

        // deleteTime
        if (! ( (deleteTime == null) ? (theComp.deleteTime == null) :
               deleteTime.equals(theComp.deleteTime)
               )) {
            return false;
        }

        // restoreTime
        if (! ( (restoreTime == null) ? (theComp.restoreTime == null) :
               restoreTime.equals(theComp.restoreTime)
               )) {
            return false;
        }

        // restoreReason
        if (! ( (restoreReason == null) ? (theComp.restoreReason == null) :
               restoreReason.equals(theComp.restoreReason)
               )) {
            return false;
        }

        // statement1
        if (! ( (statement1 == null) ? (theComp.statement1 == null) :
               statement1.equals(theComp.statement1)
               )) {
            return false;
        }

        // statement2
        if (! ( (statement2 == null) ? (theComp.statement2 == null) :
               statement2.equals(theComp.statement2)
               )) {
            return false;
        }

        return true;
    }

    /**
     * Clone <code>EPPRgpExtReport</code>.
     *
     * @return clone of <code>EPPRgpExtReport</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpExtReport clone = null;

        clone = (EPPRgpExtReport) super.clone();

        if (deleteTime != null)
            clone.deleteTime = (java.util.Date) deleteTime.clone();
        if (restoreTime != null)
            clone.restoreTime = (java.util.Date)restoreTime.clone();

        clone.preData = preData;
        clone.postData = postData;

        if (restoreReason != null)
            clone.restoreReason = (EPPRgpExtReportText)restoreReason.clone();

        if (statement1 != null)
            clone.statement1 = (EPPRgpExtReportText)statement1.clone();

        if (statement2 != null)
            clone.statement2 = (EPPRgpExtReportText)statement2.clone();

        return clone;
    }

    /**
     * Validate the state of the <code>EPPRgpExtReport</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //preWhois
        if (preData == null) {
            throw new EPPCodecException("preData required attribute is not set");
        }
        //postWhois
        if (postData == null) {
            throw new EPPCodecException("postData required attribute is not set");
        }
        //deleteTime
        if (deleteTime == null) {
            throw new EPPCodecException("deleteTime required attribute is not set");
        }
        //restoreTime
        if (restoreTime == null) {
            throw new EPPCodecException("restoreTime required attribute is not set");
        }

        // restoreReason
        if (restoreReason == null) {
            throw new EPPCodecException("restoreReason required attribute is not set");
        }

        // statement1
        if (statement1 == null) {
            throw new EPPCodecException("statement1 required attribute is not set");
        }
    }

    /**
     * Gets the preWhois value
     * @return the preWhois value
     * 
     * @deprecated Use {@link #getPreData()}
     */
    public String getPreWhois() {
        return preData;
    }
    /**
     * Set the preWhois value
     * @param preWhois the new preWhois value
     * 
     * @deprecated Use {@link #setPreWhois(String)}
     */
    public void setPreWhois(String preWhois) {
        this.preData = preWhois;
    }


    /**
     * Gets the preWhois value
     * @return the preWhois value
     */
    public String getPreData() {
        return this.preData;
    }
    /**
     * Set the preWhois value
     * @param aPreData the new preWhois value
     */
    public void setPreData(String aPreData) {
        this.preData = aPreData;
    }
   
    
    
    /**
     * get the postWhois value
     * @return the postWhois value
     * 
     * @deprecated Use {@link #getPostData()}
     */
    public String getPostWhois() {

        return postData;
    }

    /**
     * Set the postWhois value
     * @param postWhois The new postWhois value
     * 
     * @deprecated Use {@link #setPostData(String)}
     */
    public void setPostWhois(String postWhois) {
        this.postData = postWhois;
    }

    /**
     * get the postData value
     * @return the postData value
     */
    public String getPostData() {

        return this.postData;
    }
     
    
    /**
     * Set the postWhois value
     * @param aPostData The new postWhois value
     */
    public void setPostData(String aPostData) {
        this.postData = aPostData;
    }

    /**
     * Get the deleteTime value
     * @return the deleteTime value
     */
    public java.util.Date getDeleteTime() {
        return deleteTime;
    }
    
    /**
     * Set the deleteTime value
     *
     * @param deleteTime the new deleteTime value
     */
    public void setDeleteTime(java.util.Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    /**
     * Get the restoreTime value
     * @return the restoreTime value
     */
    public java.util.Date getRestoreTime() {
        return restoreTime;
    }

    /**
     * Set the restoreTime value
     * @param restoreTime the new restoreTime value
     */
    public void setRestoreTime(java.util.Date restoreTime) {
        this.restoreTime = restoreTime;
    }

    /**
     * get the statement1 value
     * @return the statement1 value
     */
    public EPPRgpExtReportText getStatement1() {
        return statement1;
    }

    /**
     * Set the statement1 value
     * @param statement1 the new statement1 value
     */
    public void setStatement1(EPPRgpExtReportText statement1) {
        if (statement1 != null) {
            statement1.setLocalName(EPPRgpExtReportText.ELM_STATEMENT);
            this.statement1 = statement1;
        }
    }

    /**
     * get the statement2 value
     *
     * @return the statement2 value
     */
    public EPPRgpExtReportText getStatement2() {
        return statement2;
    }

    /**
     * Set the statement 2 value.
     *
     * @param statement2 The new statement2 value
     */
    public void setStatement2(EPPRgpExtReportText statement2) {

        if (statement2 != null) {
            statement2.setLocalName(EPPRgpExtReportText.ELM_STATEMENT);
            this.statement2 = statement2;
        }
    }

    /**
     * Get the restoreReason value
     *
     * @return the current restoreReason value
     */
    public EPPRgpExtReportText getRestoreReason() {
        return restoreReason;
    }

    /**
     * Set the restoreReason value
     *
     * @param restoreReason the new restoreReason Value
     */
    public void setRestoreReason(EPPRgpExtReportText restoreReason) {
        if (restoreReason != null) {
            restoreReason.setLocalName(EPPRgpExtReportText.ELM_RES_REASON);
            this.restoreReason = restoreReason;
        }
    }

    /**
     * Get the other value (optional)
     * @return the other value
     */
    public String getOther() {
        return other;
    }

    /**
     * Set the other value on this instance
     * @param other the new other value
     */
    public void setOther(String other) {
        this.other = other;
  }
}