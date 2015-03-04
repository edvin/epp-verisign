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

// W3C Imports
import org.w3c.dom.*;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;


/**
 * The EPPLowBalancePollThreshold is the EPPCodecComponent that knows how to encode and
 * decode Credit Threshold Type elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 Low Balance </p>
 * <p>Description: Low Balance Poll Mapping for the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: VeriSign</p>
 * @author majain
 * @version 1.0
 */

public class EPPLowBalancePollThreshold implements EPPCodecComponent {


    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPLowBalancePollThreshold.class.getName(),
                                                       EPPCatFactory.getInstance().getFactory());


  /**
   * The constant value for FIXED
   */
	public static final String FIXED = "FIXED";

	/**
	 * The constant value for PERCENT
	 */
	public static final String PERCENT = "PERCENT";



    /** XML Element Name of <code>EPPLowBalancePollThreshold</code> root element. */
    final static java.lang.String ELM_NAME = "lowbalance-poll:creditThreshold";

    /**
     * XML Element Type Attribute Name of <code>EPPLowBalancePollThreshold</code> root element.
     */
    final static java.lang.String ATTR_NAME = "type";


    /**
     * The type value of this Type component
     */
    private String type = FIXED;

    /**
	 * The creditThresholdValue value of this Type component
	 */
    private String creditThresholdValue = null;



    /**
      * Create a new instance of EPPLowBalancePollThreshold
      *
      */
    public EPPLowBalancePollThreshold() {}

    /**
      * Create a new instance of EPPLowBalancePollThreshold with the given
      * threshold type
      * @param aType the type value to use for this instance.  Should
      * use one of the static constants defined for this class as a value.
      */

    public EPPLowBalancePollThreshold(String aType, String aCreditThresholdValue) {
        type = aType;
        creditThresholdValue = aCreditThresholdValue;
    }

    /**
     * Append all data from this Low Balance data to the given DOM Document
     *
     * @param aDocument The DOM Document to append data to
     * @return DOM <code>Element</code> encoded
     * @throws EPPEncodeException Thrown when errors occur during the
     * encode attempt or if the instance is invalid.
     */

    public Element encode(Document aDocument) throws EPPEncodeException {

        try {
            //Validate States
            validateState();
        }
         catch (EPPCodecException e) {
            cat.error("EPPLowBalancePollThreshold.doEncode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPLowBalancePollThreshold invalid state: "
                                         + e);
        }

        // creditThreshold with Attributes
        Element root =
            aDocument.createElementNS(EPPLowBalancePollMapFactory.NS, ELM_NAME);

        // add attribute type
        root.setAttribute(ATTR_NAME, type);

		Text descVal = aDocument.createTextNode(creditThresholdValue);
        root.appendChild(descVal);


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

        // Type
        type = aElement.getAttribute(ATTR_NAME);

		//creditThresholdValue
		creditThresholdValue = aElement.getFirstChild().getNodeValue();

   }

    /**
     * implements a deep <code>EPPLowBalancePollThreshold</code> compare.
     *
     * @param aObject <code>EPPLowBalancePollThreshold</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {
        if (! (aObject instanceof EPPLowBalancePollThreshold)) {
            return false;
        }

        EPPLowBalancePollThreshold theComp = (EPPLowBalancePollThreshold) aObject;

        // type
        if (!type.equals(theComp.type)) {
            return false;
        }

		// creditThresholdValue
		if (!creditThresholdValue.equals(theComp.creditThresholdValue)) {
			return false;
        }



        return true;
    }

    /**
     * Validate the state of the <code>EPPLowBalancePollThreshold</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        //type
        if (type == null) {
            throw new EPPCodecException("EPPLowBalancePollThreshold1 required attribute is not set");
        }

        //creditThresholdValue
		if (creditThresholdValue == null) {
			throw new EPPCodecException("EPPLowBalancePollThreshold2 required attribute is not set");
        }
    }

    /**
     * Clone <code>EPPLowBalancePollThreshold</code>.
     *
     * @return clone of <code>EPPLowBalancePollThreshold</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPLowBalancePollThreshold clone = null;

        clone = (EPPLowBalancePollThreshold) super.clone();

        clone.type = type;
        clone.creditThresholdValue = creditThresholdValue;

        return clone;
    }



    public String getType() {
        return type;
    }
    public void setType(String aType) {
        this.type = aType;
    }

    public String getCreditThresholdValue() {
		return creditThresholdValue;
	}
	public void setCreditThresholdValue(String aCeditThresholdValue) {
		this.creditThresholdValue = aCeditThresholdValue;
    }


}