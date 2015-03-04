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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.EPPCatFactory;

/**
 *
 * The EPPSecDNSExtUpdate is the EPPCodecComponent that knows how to encode and
 * decode secDNS update elements from/to XML and object instance.
 *
 *
 *
 * <p>Title: EPP 1.0 secDNS </p>
 * <p>Description: secDNS Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: VeriSign</p>
 * @version 1.0
 */

public class EPPSecDNSExtUpdate implements EPPCodecComponent {

    /** Log4j category for logging */
    private static Logger cat =
        Logger.getLogger(
                         EPPSecDNSExtUpdate.class.getName(),
                         EPPCatFactory.getInstance().getFactory());

     /**
      * Element tag name for the update
      */
    public static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":update";

    /**
     * Element tag name for the add
     */
    public static final String ADD_ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":add";

    /**
     * Element tag name for the chg
     */
    public static final String CHG_ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":chg";

    /**
     * Element tag name for the rem
     */
    public static final String REM_ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":rem";

	/** XML Attribute Name for the urgent attribute */
	private final static String ATTR_URGENT = "urgent";
	
	/** 
	 * <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to add
	 */
    private List addDsData = null;

	/** 
	 * <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to chg
	 */
    private List chgDsData = null;
    
	/** 
	 * <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
	 */
    private List remKeyTag = null;
    
	/**
	 * High priority request?  A value of <code>true</code>
	 * indicates that the client has asked the server operator to process
	 * the update command with a high priority. Default value is
	 * <code>false</code>.
	 */
	private boolean urgent = false;
    
    /**
     * The namespace associated with this secDNS update.
     * @return The namespace associated with secDNS component
     */
    public String getNamespace() {
        return EPPSecDNSExtFactory.NS;
    }

    /**
     * Instantiate a new instance of EPPSecDNSExtUpdate
     */
    public EPPSecDNSExtUpdate() {}

    
    /**
     * Creates a new instance of the <code>EPPSecDNSExtUpdate</code>
     * with the add, chg, and rem elements.
     * 
     * @param addDsData <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to add
     * @param chgDsData <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to chg
     * @param remKeyTag <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
     */
    public EPPSecDNSExtUpdate(List addDsData, List chgDsData, List remKeyTag) {
    	this.addDsData = addDsData;
       	this.chgDsData = chgDsData;
       	this.remKeyTag = remKeyTag;
    }
    
    /**
     * Append all data from this secDNS update to the given DOM Document
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
            cat.error("EPPSecDNSExtUpdate.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPSecDNSExtUpdate invalid state: "
                                         + e);
        }

        if (aDocument == null) {
            throw new EPPEncodeException("aDocument is null" +
            " in EPPSecDNSExtUpdate.encode(Document)");
        }

        Element root =
            aDocument.createElementNS(EPPSecDNSExtFactory.NS, ELM_NAME);

        root.setAttribute("xmlns:" + EPPSecDNSExtFactory.NS_PREFIX, EPPSecDNSExtFactory.NS);
        root.setAttributeNS(
                            EPPCodec.NS_XSI, "xsi:schemaLocation",
                            EPPSecDNSExtFactory.NS_SCHEMA);

        if (addDsData != null) {
        	Element add =
                aDocument.createElementNS(EPPSecDNSExtFactory.NS, ADD_ELM_NAME);
        	EPPUtil.encodeCompList(aDocument, add, addDsData);
        	root.appendChild(add);
        }
        
        if (chgDsData != null) {
        	Element chg =
                aDocument.createElementNS(EPPSecDNSExtFactory.NS, CHG_ELM_NAME);
        	EPPUtil.encodeCompList(aDocument, chg, chgDsData);
        	root.appendChild(chg);
        }
        
        if (remKeyTag != null) {
        	Element rem =
                aDocument.createElementNS(EPPSecDNSExtFactory.NS, REM_ELM_NAME);
        	EPPUtil.encodeIntegerList(aDocument, rem, remKeyTag, EPPSecDNSExtFactory.NS, EPPSecDNSExtDsData.ELM_KEY_TAG);
        	root.appendChild(rem);
        }

        EPPUtil.encodeBooleanAttr(root, ATTR_URGENT, this.urgent);

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

    	Element addElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, ADD_ELM_NAME);
    	addDsData = null;
    	if (addElm != null) {
    		addDsData = EPPUtil.decodeCompList(
    				addElm, EPPSecDNSExtFactory.NS,
    				EPPSecDNSExtDsData.ELM_NAME, EPPSecDNSExtDsData.class);
 
    		if (addDsData != null && addDsData.size() == 0) {
    			addDsData = null;
    		}
    	}
    	
    	Element chgElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, CHG_ELM_NAME);
    	chgDsData = null;
    	if (chgElm != null) {
    		chgDsData = EPPUtil.decodeCompList(
    				chgElm, EPPSecDNSExtFactory.NS,
    				EPPSecDNSExtDsData.ELM_NAME, EPPSecDNSExtDsData.class);
    		if (chgDsData != null && chgDsData.size() == 0) {
    			chgDsData = null;
    		}
    	}
    	
    	Element remElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPSecDNSExtFactory.NS, REM_ELM_NAME);
    	remKeyTag = null;
    	if (remElm != null) {
    		remKeyTag = EPPUtil.decodeIntegerList(
    				remElm, EPPSecDNSExtFactory.NS,
    				EPPSecDNSExtDsData.ELM_KEY_TAG);
    		if ( (remKeyTag != null && remKeyTag.size() == 0) ) {
    			remKeyTag = null;
    		}

    	}
    	
    	this.urgent = EPPUtil.decodeBooleanAttr(aElement, ATTR_URGENT);
    }

    /**
     * implements a deep <code>EPPSecDNSExtUpdate</code> compare.
     *
     * @param aObject <code>EPPSecDNSExtUpdate</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {

        if (! (aObject instanceof EPPSecDNSExtUpdate)) {
            return false;
        }
        EPPSecDNSExtUpdate theComp = (EPPSecDNSExtUpdate) aObject;

        // add
		if (!EPPUtil.equalLists(this.addDsData, theComp.addDsData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): addDsData not equal");
			return false;
		}

        // chg
		if (!EPPUtil.equalLists(this.chgDsData, theComp.chgDsData)) {
			cat.error("EPPSecDNSExtUpdate.equals(): chgDsData not equal");
			return false;
		}
        
        // rem
		if (!EPPUtil.equalLists(this.remKeyTag, theComp.remKeyTag)) {
			cat.error("EPPSecDNSExtUpdate.equals(): remKeyTag not equal");
			return false;
		}
        
        // urgent
        if (! urgent == theComp.urgent ) {
            return false;
        }
        
        return true;
    }


    /**
     * Validate the state of the <code>EPPSecDNSExtUpdate</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        // add/chg/rem
        if ((addDsData == null) && (chgDsData == null) && (remKeyTag == null)) {
            throw new EPPCodecException("EPPSecDNSExtUpdate required attribute missing");
        }
        
        // Ensure there is only one non-null add, chg, or rem
		if (((addDsData != null) && ((chgDsData != null) || (remKeyTag != null)))
				|| ((chgDsData != null) && ((addDsData != null) || (remKeyTag != null)))
				|| ((remKeyTag != null) && ((chgDsData != null) || (addDsData != null)))) {
			throw new EPPCodecException("Only one add, chg, or rem is allowed");
		}
    }

    /**
	 * Clone <code>EPPSecDNSExtUpdate</code>.
	 * 
	 * @return clone of <code>EPPSecDNSExtUpdate</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
    public Object clone() throws CloneNotSupportedException {

        EPPSecDNSExtUpdate clone = null;

        clone = (EPPSecDNSExtUpdate) super.clone();

		if (addDsData != null) {
			clone.addDsData = new ArrayList(addDsData);
		}

		if (chgDsData != null) {
			clone.chgDsData = new ArrayList(chgDsData);
		}

		if (remKeyTag != null) {
			clone.remKeyTag = new ArrayList(remKeyTag);
		}

		clone.urgent = urgent;
		
        return clone;
    }

	/** 
	 * Get the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to add
	 * @return <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
    public List getAdd() {
        return addDsData;
    }

	/** 
	 * Set the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to add
	 * @param addDsData <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
    public void setAdd(List addDsData) {
        this.addDsData = addDsData;
    }
    
	/** 
	 * Add/Append to the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to add
	 * @param dsData <code>EPPSecDNSExtDsData</code> instance
	 */
    public void appendAdd(EPPSecDNSExtDsData dsData) {
		if (addDsData == null) {
			addDsData = new ArrayList();
		}
		addDsData.add(dsData);
    }

	/** 
	 * Get the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to chg
	 * @return <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
    public List getChg() {
        return chgDsData;
    }

	/** 
	 * Set the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to chg
	 * @param chgDsData <code>List</code> of <code>EPPSecDNSExtDsData</code> instances
	 */
    public void setChg(List chgDsData) {
        this.chgDsData = chgDsData;
    }

	/** 
	 * Add/Append to the <code>List</code> of DS Data <code>EPPSecDNSExtDsData</code> instances to chg
	 * @param dsData <code>EPPSecDNSExtDsData</code> instance
	 */
    public void appendChg(EPPSecDNSExtDsData dsData) {
		if (chgDsData == null) {
			chgDsData = new ArrayList();
		}
		chgDsData.add(dsData);
    }
    
	/** 
	 * Get the <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
	 * @return <code>List</code> of <code>Integer</code> instances specifying keyTag to rem
	 */
    public List getRem() {
        return remKeyTag;
    }

	/** 
	 * Set the <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
	 * @param remKeyTag <code>List</code> of <code>Integer</code> instances specifying keyTag to rem
	 */
    public void setRem(List remKeyTag) {
        this.remKeyTag = remKeyTag;
    }

	/** 
	 * Add/Append to the <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
	 * @param keyTag an <code>Integer</code> instance representing secDNS:keyTag
	 */
    public void appendRem(Integer keyTag) {
		if (remKeyTag == null) {
			remKeyTag = new ArrayList();
		}
    	if (keyTag == null) {
    		remKeyTag.add(new Integer(EPPSecDNSExtDsData.UNSPEC_KEY_TAG));
    	}
    	else {
    		remKeyTag.add(keyTag);
    	}
    }
    
	/** 
	 * Add/Append to the <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem
	 * @param keyTag an <code>int</code> value representing secDNS:keyTag
	 */
    public void appendRem(int keyTag) {
    	appendRem(new Integer(keyTag));
    }
    
	/** 
	 * Add/Append to the <code>List</code> of DS keyTag <code>Integer</code> instances specifying keyTag to rem.
	 * This convenience method will extract the secDNS:keyTag element from the <code>EPPSecDNSExtDsData</code> provided.
	 * @param dsData <code>EPPSecDNSExtDsData</code> instance specifying keyTag to rem
	 */
    public void appendRem(EPPSecDNSExtDsData dsData) {

    	int keyTag = EPPSecDNSExtDsData.UNSPEC_KEY_TAG;
    	if (dsData != null) {
    		keyTag = dsData.getKeyTag();
    	}
 
		appendRem(new Integer(keyTag));

    }
      
	/**
	 * Is the update request urgent?
	 *
	 * @return Returns <code>true</code> if the client has asked the server operator to process
	 * the update command with a high priority; <code>false</code> otherwise.
	 */
	public boolean isUrgent() {
		return urgent;
	}

	/**
	 * Sets the urgent attribute.
	 *
	 * @param urgent The urgent value to set.
	 */
	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}


}