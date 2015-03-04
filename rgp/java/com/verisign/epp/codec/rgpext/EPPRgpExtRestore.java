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
import org.w3c.dom.Element;
import org.w3c.dom.Document;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;

/**
 *
 * The EPPRgpExtRestore is the EPPCodecComponent that knows how to encode and
 * decode RGP restore elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 RGP </p>
 * <p>Description: RGP Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author clloyd
 * @version 1.0
 */

public class EPPRgpExtRestore
    implements EPPCodecComponent {

    /**
     * Constant value for request operation
     */
    public static final String REQUEST = "request";

    /**
     * Constant value for report operation
     */
    public static final String REPORT = "report";

    /**
     * The restore XML element name
     */
    public static final String ELM_NAME = "rgp:restore";

    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPRgpExtRestore.class.getName(),
                                                 EPPCatFactory.getInstance().
                                                 getFactory());

    /**
     * The EPPCodecComponet report that is contained in this
     * restore instance
     */
    private EPPRgpExtReport report;

    /**
     * The operation to perform with this restore - either "request" or "report"
     */
    private String op;

    /**
     * Default constructor that will set the 
     * <code>op</code> to <code>REQUEST</code>.
     */
    public EPPRgpExtRestore() {
    		this.op = REQUEST;
    	}
    

    /**
     * Creates a restore report extension.  The <code>op</code> is set to 
     * <code>REPORT</code>.
     * 
     * @param aReport Report information
     */
    public EPPRgpExtRestore(EPPRgpExtReport aReport) {
    		this.op = REPORT;
    		this.report = aReport;
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
            cat.error("EPPRgpExtRestore.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpExtRestore invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " on in EPPRgpExtRestore.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPRgpExtFactory.NS, ELM_NAME);


        root.setAttribute("op", op);

        // only encode the report if the operation is for a report
        if (op.equals(REPORT))
            EPPUtil.encodeComp(aDocument, root, report);

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

        op = aElement.getAttribute("op");

        report = (EPPRgpExtReport)EPPUtil.decodeComp(aElement, EPPRgpExtFactory.NS,
            EPPRgpExtReport.ELM_NAME, EPPRgpExtReport.class);
    }

    /**
     * Validate the state of the <code>EPPRgpExtRestore</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {

        //op
        if (op == null) {
            throw new EPPCodecException("op required attribute is not set");
        }

        if (op.equals(REPORT) && report == null) {
            throw new EPPCodecException("op attribute has value 'report' but no report object is set");
        }
    }

    /**
     * implements a deep <code>EPPRgpExtRestore</code> compare.
     *
     * @param aObject <code>EPPRgpExtRestore</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPRgpExtRestore)) {
            return false;
        }
        EPPRgpExtRestore theComp = (EPPRgpExtRestore) aObject;

        if (op == null && theComp.op != null)
            return false;

        // op
        if (!op.equals(theComp.op)){
            return false;
        }

        // report
        if (op.equals(REPORT)) {

            if (report != null && !report.equals(theComp.report)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Clone <code>EPPRgpExtRestore</code>.
     *
     * @return clone of <code>EPPRgpExtRestore</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpExtRestore clone = null;

        clone = (EPPRgpExtRestore) super.clone();

        clone.op = op;

        if (report != null)
            clone.report = (EPPRgpExtReport)report.clone();
        return clone;
    }


    /**
     * Get the RGP report instance contained in this RGP restore instance
     * @return the RGP report instance contained in this RGP restore instance
     */
    public EPPRgpExtReport getReport() {
        return report;
    }

    /**
     * Set the RGP report instance in this RGP restore instance
     * @param report the new RGP report instance
     */
    public void setReport(EPPRgpExtReport report) {
        this.report = report;
    }

    /**
     * Get the value of the current op attibut.  Should be either "request" or
     * "report"
     * @return the value of the current op attibute
     *
     */
    public String getOp() {
        return op;
    }

    /**
     * Set the value of the current op attribute. Should be either "request" or
     * "report".  Use the REQUEST or REPORT static final constants contained in this
     * class to set.
     * @param op the value of the current op attribute
     */
    public void setOp(String op) {
        this.op = op;
    }
}