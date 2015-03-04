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

// Log4j Imports
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 *
 * The EPPRgpExtInfData is the EPPCodecComponent that knows how to encode and
 * decode RGP infData elements from/to XML and object instance.
 */

public class EPPRgpExtInfData implements EPPCodecComponent {

    /** Log4j category for logging */
    private static Logger cat =
        Logger.getLogger(
                         EPPRgpExtInfData.class.getName(),
                         EPPCatFactory.getInstance().getFactory());

     /**
      * Element tag name for the infData
      */
    public static final String ELM_NAME = "rgp:infData";

    /**
     * The RGP statuses contained in the inf data of type <code>EPPRgpExtStatus</code>
     */
    private Vector statuses = new Vector();

    /**
     * The namespace associated with this RGP inf data.
     * @return The namespace associated with RGP component
     */
    public String getNamespace() {
        return EPPRgpExtFactory.NS;
    }

    /**
     * Instantiate a new instance of EPPRgpExtInfData
     */
    public EPPRgpExtInfData() {}

    /**
     * Creates a new instance of the <code>EPPRgpExtInfData</code>
     * with a status element.
     * 
     * @param aStatus Associated status element.
     */
    public EPPRgpExtInfData(EPPRgpExtStatus aStatus) {
    	this.statuses.add(aStatus);
    }
 
    /**
     * Creates a new instance of the <code>EPPRgpExtInfData</code>
     * with a <code>Vector</code> of status elements.
     * 
     * @param aStatuses <code>Vector</code> of {@link EPPRgpExtStatus} status elements.  Only a non-<code>null</code> 
     * value will be used.
     */
    public EPPRgpExtInfData(Vector aStatuses) {
    	if (aStatuses != null) {
    		this.statuses = aStatuses;
    	}
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
            cat.error("EPPRgpExtInfData.doEncode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPRgpExtInfData invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " on in EPPRgpExtInfData.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPRgpExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:rgp", EPPRgpExtFactory.NS);
        root.setAttributeNS(
                            EPPCodec.NS_XSI, "xsi:schemaLocation",
                            EPPRgpExtFactory.NS_SCHEMA);

		// Statuses
		EPPUtil.encodeCompVector(aDocument, root, this.statuses);

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

		this.statuses =
			EPPUtil.decodeCompVector(
									 aElement, EPPRgpExtFactory.NS,
									 EPPRgpExtStatus.ELM_NAME, EPPRgpExtStatus.class);
     }

    /**
     * implements a deep <code>EPPRgpExtInfData</code> compare.
     *
     * @param aObject <code>EPPRgpExtInfData</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPRgpExtInfData)) {
            return false;
        }
        EPPRgpExtInfData theComp = (EPPRgpExtInfData) aObject;

		// Statuses
		if (!EPPUtil.equalVectors(this.statuses, theComp.statuses)) {
			return false;
		}
        
        return true;
    }


    /**
     * Validate the state of the <code>EPPRgpExtInfData</code> instance. A
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
        if (this.statuses == null || this.statuses.size() == 0) {
            throw new EPPCodecException("At least one EPPRgpExtInfData statuses is required");
        }
    }

    /**
     * Clone <code>EPPRgpExtInfData</code>.
     *
     * @return clone of <code>EPPRgpExtInfData</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPRgpExtInfData clone = null;

        clone = (EPPRgpExtInfData) super.clone();

		clone.statuses = (Vector) statuses.clone();
 
        return clone;
    }

    /**
     * Gets the first status if there is one; <code>null</code> otherwise.
     * 
     * @return First status of type <code>EPPRgpExtStatus</code> if set;<code>null</code> otherwise.
     */
    public EPPRgpExtStatus getStatus() {
    	if (this.statuses.size() >= 1) {
    		return (EPPRgpExtStatus) this.statuses.get(0);
    	}
    	else {
    		return null;
    	}
    }

    /**
     * Adds the status of type {@link EPPRgpExtStatus} to the <code>Vector</code> 
     * of statuses.
     * 
     * @deprecated {@link #addStatus(EPPRgpExtStatus)} 
     */
    public void setStatus(EPPRgpExtStatus aStatus) {
        this.statuses.add(aStatus);
    }

    /**
     * Adds the status of type {@link EPPRgpExtStatus} to the <code>Vector</code> 
     * of statuses.
     * @param aStatus Status to add 
     */
    public void addStatus(EPPRgpExtStatus aStatus) {
        this.statuses.add(aStatus);
    }
    
    
    /**
     * Gets the <code>Vector</code> of statuses of type {@link EPPRgpExtStatus}.  
     * 
     * @return Non-<code>null</code> <code>Vector</code> of {@link EPPRgpExtStatus} instances.
     */
    public Vector getStatuses() {
    	return this.statuses;
    }

    /**
     * Sets the <code>Vector</code> of statuses of type {@link EPPRgpExtStatus}.  
     * 
     * @param aStatuses Statuses to use.  If <code>null</code> the parameter will be ignored.
     */
    public void setStatuses(Vector aStatuses) {
    	if (aStatuses != null) {
    		this.statuses = aStatuses;
    	}
     }
    

}