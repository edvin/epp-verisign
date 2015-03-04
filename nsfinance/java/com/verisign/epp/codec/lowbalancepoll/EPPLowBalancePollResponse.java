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

package com.verisign.epp.codec.lowbalancepoll;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.lowbalancepoll.*;

/**
 *
 * A concrete EPPResponse that knows how to encode/decode LowBalance Poll responses
 * from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 LowBalance </p>
 * <p>Description: LowBalance Poll Mapping for the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author majain
 * @version 1.0
 */

public class EPPLowBalancePollResponse
    extends EPPResponse {

    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPLowBalancePollResponse.class.getName(),
                                                       EPPCatFactory.getInstance().getFactory());


    /**
     * The poll data XML tag name
     */
    public static final String ELM_NAME= "lowbalance-poll:pollData";

    /**
     * The registrarName XML tag name
     */
    public static final String ELM_REGISTRAR_NAME = "lowbalance-poll:registrarName";

    /**
     * The creditLimit XML tag name
     */
    public static final String ELM_CREDIT_LIMIT = "lowbalance-poll:creditLimit";


    /**
	 * The availableCredit XML tag name
	 */
	public static final String ELM_AVAILABLE_CREDIT = "lowbalance-poll:availableCredit";


    /**
     * The registrarName
     */
    private String registrarName;

    /**
     * The creditLimit
     */
    private String creditLimit;

    /**
     * creditThreshold
     */
	private EPPLowBalancePollThreshold creditThreshold;


    /**
	 * The availableCredit
	 */
	private String availableCredit;


    /**
     * Create a new instance of EPPLowBalancePollResponse
     */
    public EPPLowBalancePollResponse() {}

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
            cat.error("EPPLowBalancePollData.doEncode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPLowBalancePollData invalid state: "
                                         + e);
        }



        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
                " on in EPPLowBalancePollData.doEncode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPLowBalancePollMapFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:lowbalance-poll", EPPLowBalancePollMapFactory.NS);
        root.setAttributeNS(
            EPPCodec.NS_XSI, "xsi:schemaLocation",
            EPPLowBalancePollMapFactory.NS_SCHEMA);

        // registrarName
        EPPUtil.encodeString(aDocument, root, registrarName,
        						EPPLowBalancePollMapFactory.NS, ELM_REGISTRAR_NAME);

        // creditLimit
		EPPUtil.encodeString(aDocument, root, creditLimit,
								EPPLowBalancePollMapFactory.NS, ELM_CREDIT_LIMIT);


		// creditThreshold
        EPPUtil.encodeComp(aDocument, root, creditThreshold);


        // availableCredit
        EPPUtil.encodeString(aDocument, root, availableCredit,
                              	EPPLowBalancePollMapFactory.NS, ELM_AVAILABLE_CREDIT);

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

        registrarName = EPPUtil.decodeString(aElement, EPPLowBalancePollMapFactory.NS, ELM_REGISTRAR_NAME);
        creditLimit = EPPUtil.decodeString(aElement, EPPLowBalancePollMapFactory.NS, ELM_CREDIT_LIMIT);

		creditThreshold = (EPPLowBalancePollThreshold)EPPUtil.decodeComp(aElement, EPPLowBalancePollMapFactory.NS,
                                  EPPLowBalancePollThreshold.ELM_NAME, EPPLowBalancePollThreshold.class);

		availableCredit = EPPUtil.decodeString(aElement, EPPLowBalancePollMapFactory.NS, ELM_AVAILABLE_CREDIT);

    }

    /**
        * implements a deep <code>EPPLowBalancePollResponse</code> compare.
        *
        * @param aObject <code>EPPLowBalancePollResponse</code> instance to compare with
        *
        * @return true if equal false otherwise
        */
       public boolean equals(Object aObject) {
           if (! (aObject instanceof EPPLowBalancePollResponse)) {
               return false;
           }

           EPPLowBalancePollResponse theComp = (EPPLowBalancePollResponse) aObject;


           // registrarName
           if (!((registrarName == null) ? (theComp.registrarName == null) : registrarName.equals(theComp.registrarName)
               )) {
               return false;
           }

           // creditLimit
           if (!((creditLimit == null) ? (theComp.creditLimit == null) : creditLimit.equals(theComp.creditLimit)
               )) {
               return false;
           }


           // creditThreshold
           if (!((creditThreshold == null) ? (theComp.creditThreshold == null) : creditThreshold.equals(theComp.creditThreshold)
               )) {
               return false;
           }


           // availableCredit
		   if (!((availableCredit == null) ? (theComp.availableCredit == null) : availableCredit.equals(theComp.availableCredit)
			  )) {
			  return false;
           }

           return true;
       }

       /**
        * Clone <code>EPPLowBalancePollResponse</code>.
        *
        * @return clone of <code>EPPLowBalancePollResponse</code>
        *
        * @exception CloneNotSupportedException standard Object.clone exception
        */
       public Object clone() throws CloneNotSupportedException {

           EPPLowBalancePollResponse clone = null;

           clone = (EPPLowBalancePollResponse) super.clone();

           clone.registrarName = registrarName;
           clone.creditLimit = creditLimit;

           if (creditThreshold != null) {
		   		clone.creditThreshold = (EPPLowBalancePollThreshold)creditThreshold.clone();
           }


           clone.availableCredit = availableCredit;

           return clone;
       }


    /**
     * Validate the state of the <code>EPPLowBalancePollData</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //registrarName
        if (registrarName == null) {
            throw new EPPCodecException("EPPLowBalancePollData required attribute is not set");
        }

        //creditLimit
		if (creditLimit == null) {
			throw new EPPCodecException("EPPLowBalancePollData required attribute is not set");
        }

		//creditThreshold
		if (creditThreshold == null) {
			throw new EPPCodecException("EPPLowBalancePollData required attribute is not set");
        }

        //availableCredit
		if (availableCredit == null) {
			throw new EPPCodecException("EPPLowBalancePollData required attribute is not set");
        }

    }



    public String getRegistrarName() {
        return registrarName;
    }

    public void setRegistrarName(String registrarName) {
        this.registrarName = registrarName;
    }


    public String getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
    }


	public EPPLowBalancePollThreshold getCreditThreshold() {
		return creditThreshold;
	}

	public void setCreditThreshold(EPPLowBalancePollThreshold aCreditThreshold) {
		creditThreshold = aCreditThreshold;
    }


    public String getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(String availableCredit) {
		this.availableCredit = availableCredit;
    }

}