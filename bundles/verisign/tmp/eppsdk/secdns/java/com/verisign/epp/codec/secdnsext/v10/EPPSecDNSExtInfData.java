/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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

package com.verisign.epp.codec.secdnsext.v10;

// Log4j Imports
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;

/**
 *
 * The EPPSecDNSExtInfData is the EPPCodecComponent that knows how to encode and
 * decode secDNS infData elements from/to XML and object instance.
 *
 *
 *
 * <p>Title: EPP 1.0 secDNS </p>
 * <p>Description: secDNS Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: VeriSign</p>
 * @version 1.0
 */

public class EPPSecDNSExtInfData implements EPPCodecComponent {

    /** Log4j category for logging */
    private static Logger cat =
        Logger.getLogger(
                         EPPSecDNSExtInfData.class.getName(),
                         EPPCatFactory.getInstance().getFactory());

     /**
      * Element tag name for the infData
      */
    public static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":infData";

    /**
     * The secDNS dsData contained in the infData
     */
    private List dsData;

    /**
     * The namespace associated with this secDNS inf data.
     * @return The namespace associated with secDNS component
     */
    public String getNamespace() {
        return EPPSecDNSExtFactory.NS;
    }

    /**
     * Instantiate a new instance of EPPSecDNSExtInfData
     */
    public EPPSecDNSExtInfData() {}

    /**
     * Creates a new instance of the <code>EPPSecDNSExtInfData</code>
     * with the dsData element.
     * 
     * @param dsData List of dsData <code>EPPSecDNSExtDsData</code> instances.
     */
    public EPPSecDNSExtInfData(List dsData) {
    	this.dsData = dsData;
    }
     
    
    /**
     * Append all data from this secDNS inf data to the given DOM Document
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
            cat.error("EPPSecDNSExtInfData.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPSecDNSExtInfData invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " in EPPSecDNSExtInfData.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPSecDNSExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:" + EPPSecDNSExtFactory.NS_PREFIX, EPPSecDNSExtFactory.NS);
        root.setAttributeNS(
                            EPPCodec.NS_XSI, "xsi:schemaLocation",
                            EPPSecDNSExtFactory.NS_SCHEMA);

        EPPUtil.encodeCompList(aDocument, root, dsData);

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

    	dsData =
			EPPUtil.decodeCompList(
									 aElement, EPPSecDNSExtFactory.NS,
									 EPPSecDNSExtDsData.ELM_NAME, EPPSecDNSExtDsData.class);
    }

    /**
     * implements a deep <code>EPPSecDNSExtInfData</code> compare.
     *
     * @param aObject <code>EPPSecDNSExtInfData</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPSecDNSExtInfData)) {
            return false;
        }
        EPPSecDNSExtInfData theComp = (EPPSecDNSExtInfData) aObject;

        // dsData
		if (!EPPUtil.equalLists(dsData, theComp.dsData)) {
			cat.error("EPPSecDNSExtInfData.equals(): dsData not equal");

			return false;
		}

        return true;
    }


    /**
     * Validate the state of the <code>EPPSecDNSExtInfData</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //dsData
        if ((dsData == null) || (dsData.size() == 0)) {
            throw new EPPCodecException("EPPSecDNSExtInfData required attribute dsData is not set");
        }
    }

    /**
     * Clone <code>EPPSecDNSExtInfData</code>.
     *
     * @return clone of <code>EPPSecDNSExtInfData</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPSecDNSExtInfData clone = null;

        clone = (EPPSecDNSExtInfData) super.clone();

		if (dsData != null) {
			clone.dsData = new ArrayList(dsData);
		}

        return clone;
    }

    /**
     * Get the dsData that is contained in this inf data
     * @return List of dsData <code>EPPSecDNSExtDsData</code> instances.
     */
    public List getDsData() {
        return dsData;
    }

    /**
     * Set the dsData of this inf data
     * @param dsData List of dsData <code>EPPSecDNSExtDsData</code> instances
     */
    public void setDsData(List dsData) {
        this.dsData = dsData;
    }

	/** 
	 * Append to the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances
	 * @param dsData <code>EPPSecDNSExtDsData</code> instance
	 */
    public void appendDsData(EPPSecDNSExtDsData dsData) {
		if (this.dsData == null) {
			this.dsData = new ArrayList();
		}
		this.dsData.add(dsData);
    }
}