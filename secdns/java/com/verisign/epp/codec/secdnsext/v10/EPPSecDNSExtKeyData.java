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

// W3C Imports
import org.w3c.dom.*;

// Log4j Imports
import org.apache.log4j.Logger;

// EPP imports
import com.verisign.epp.util.*;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtFactory;

/**
 * The EPPSecDNSExtKeyData is the EPPCodecComponent that knows how to encode and
 * decode secDNS keyData elements from/to XML and object instance.
 *
 * <p>Title: EPP 1.0 secDNS </p>
 * <p>Description: secDNS Extension to the EPP SDK</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: VeriSign</p>
 * @version 1.0
 */

public class EPPSecDNSExtKeyData implements EPPCodecComponent {


    /**
     * Log4j category for logging
     */
    private static Logger cat = Logger.getLogger(EPPSecDNSExtKeyData.class.getName(),
                                                       EPPCatFactory.getInstance().getFactory());

    /** XML Element Name of <code>EPPSecDNSExtKeyData</code> root element. */
    static final String ELM_NAME = EPPSecDNSExtFactory.NS_PREFIX + ":keyData";
    
    /**
     * The element tag name for flags
     */
    private static final String ELM_FLAGS = EPPSecDNSExtFactory.NS_PREFIX + ":flags";

    /**
     * The element tag name for protocol
     */
    private static final String ELM_PROTOCOL = EPPSecDNSExtFactory.NS_PREFIX + ":protocol";

    /**
     * The element tag name for algorithm
     */
    private static final String ELM_ALG = EPPSecDNSExtFactory.NS_PREFIX + ":alg";

    /**
     * The element tag name for public key
     */
    private static final String ELM_PUB_KEY = EPPSecDNSExtFactory.NS_PREFIX + ":pubKey";

    /**
     * The default protocol vale.
     * http://www.ietf.org/rfc/rfc4034.txt
     * 2.1.2.  The Protocol Field
     * The Protocol Field MUST have value 3, and the DNSKEY RR MUST be
     * treated as invalid during signature verification if it is found to be
     * some value other than 3.
     */
    public static final int DEFAULT_PROTOCOL = 3;

 
    /**
     * Bit 7 of the Flags field is the Zone Key flag.  
     * If bit 7 has value 1, then the DNSKEY record holds a DNS zone key.
     */
    public static final int FLAGS_ZONE_KEY = 256;
 
    /**
     * Bit 15 of the Flags field is the Secure Entry Point flag.
     * If bit 15 has value 1, then the DNSKEY record holds a
     * key intended for use as a secure entry point (SEP).
     */
    public static final int FLAGS_ZONE_KEY_SEP = 257;

    /** Unspecified int */
	private static final int UNSPEC_INT = -1;
    
    /** Unspecified flags value */
	public static final int UNSPEC_FLAGS = UNSPEC_INT;
	
    /** Minimum flags value */
	public static final int MIN_FLAGS = 0;
	
    /** Maximum flags value */
	public static final int MAX_FLAGS = 65535;
	
	/** Unspecified protocol value */
	public static final int UNSPEC_PROTOCOL = UNSPEC_INT;
	
    /** Minimum protocol value */
	public static final int MIN_PROTOCOL = 0;
	
    /** Maximum protocol value */
	public static final int MAX_PROTOCOL = 255;

	/** Unspecified alg value */
	public static final int UNSPEC_ALG = UNSPEC_INT;
	
    /** Minimum alg value */
	public static final int MIN_ALG = 0;
	
    /** Maximum alg value */
	public static final int MAX_ALG = 255;
	
    /**
     * The flags field value of this keyData component
     */
    private int flags = UNSPEC_INT;

    /**
     * The protocol value of this keyData component
     */
    private int protocol = DEFAULT_PROTOCOL;

    /**
     * The algorithm value of this keyData component
     */
    private int alg = UNSPEC_INT;

    /**
     * The public key value of this keyData component
     */
    private String pubKey = null;

    /**
     * Create a new instance of EPPSecDNSExtKeyData
     */
    public EPPSecDNSExtKeyData() {}

    /**
     * Create a new instance (copy) of EPPSecDNSExtKeyData
     * @param keyData <code>EPPSecDNSExtKeyData</code> instance
     */
    public EPPSecDNSExtKeyData(EPPSecDNSExtKeyData keyData) {
    	this(keyData.getFlags(),
    			keyData.getProtocol(),
    			keyData.getAlg(),
    			keyData.getPubKey()
    		);
    }
    
    /**
     * Create a new instance of EPPSecDNSExtKeyData with the given values.
     * @param flags the flags value to use for this instance. 
     * @param protocol the protocol value to use for this instance. 
     * @param alg the algorithm value to use for this instance. 
     * @param pubKey the public key value to use for this instance.  
     */
    public EPPSecDNSExtKeyData(int flags, int protocol, int alg, String pubKey) {
        setFlags(flags);
        setProtocol(protocol);
        setAlg(alg);
        setPubKey(pubKey);
    }

    /**
     * Append all data from this secDNS:keyData to the given DOM Document
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
            cat.error("EPPSecDNSExtKeyData.encode(): Invalid state on encode: "
                      + e);
            throw new EPPEncodeException("EPPSecDNSExtKeyData invalid state: "
                                         + e);
        }

        // secDNS:keyData
        Element root =
            aDocument.createElementNS(EPPSecDNSExtFactory.NS, ELM_NAME);

        // secDNS:flags
        EPPUtil.encodeString(aDocument, root, flags + "", EPPSecDNSExtFactory.NS, ELM_FLAGS);

        // secDNS:protocol
        EPPUtil.encodeString(aDocument, root, protocol + "", EPPSecDNSExtFactory.NS, ELM_PROTOCOL);
        
        // secDNS:alg
        EPPUtil.encodeString(aDocument, root, alg + "", EPPSecDNSExtFactory.NS, ELM_ALG);
        
        // secDNS:pubKey
        EPPUtil.encodeString(aDocument, root, pubKey, EPPSecDNSExtFactory.NS, ELM_PUB_KEY);
        
        return root;
    }
    /**
     * Populate the data of this instance with the data stored in the
     * given Element of the DOM tree
     *
     * @param aElement The root element of the fragment of XML
     * @throws EPPDecodeException Thrown if any errors occur during decoding.
     */
    public void decode(Element aElement) throws EPPDecodeException {

    	Integer flagsInt = EPPUtil.decodeInteger(aElement, EPPSecDNSExtFactory.NS, ELM_FLAGS);
    	if (flagsInt == null) {
    		flags = UNSPEC_INT;
    	}
    	else {
    		flags = flagsInt.intValue();
    	}
    	
    	Integer protocolInt = EPPUtil.decodeInteger(aElement, EPPSecDNSExtFactory.NS, ELM_PROTOCOL);
    	if (protocolInt == null) {
    		protocol = UNSPEC_INT;
    	}
    	else {
    		protocol = protocolInt.intValue();
    	}
    	
    	Integer algInt = EPPUtil.decodeInteger(aElement, EPPSecDNSExtFactory.NS, ELM_ALG);
    	if (algInt == null) {
    		alg = UNSPEC_INT;
    	}
    	else {
    		alg = algInt.intValue();
    	}
    	
    	setPubKey(EPPUtil.decodeString(aElement, EPPSecDNSExtFactory.NS, ELM_PUB_KEY));
    	
    }

    /**
     * implements a deep <code>EPPSecDNSExtKeyData</code> compare.
     *
     * @param aObject <code>EPPSecDNSExtKeyData</code> instance to compare with
     *
     * @return true if equal false otherwise
     */
    public boolean equals(Object aObject) {
        if (! (aObject instanceof EPPSecDNSExtKeyData)) {
            return false;
        }

        EPPSecDNSExtKeyData theComp = (EPPSecDNSExtKeyData) aObject;

        // flags
		if (flags != theComp.flags) {
			return false;
		}

        // protocol
		if (protocol != theComp.protocol) {
			return false;
		}

        // alg
		if (alg != theComp.alg) {
			return false;
		}

        // pubKey
        if (
            ! (
            (pubKey == null) ? (theComp.pubKey == null)
            : pubKey.equals(theComp.pubKey)
            )) {
            return false;
        }
        
        return true;
    }

    /**
     * Validate the state of the <code>EPPSecDNSExtKeyData</code> instance. A
     * valid state means that all of the required attributes have been set. If
     * validateState returns without an exception, the state is valid. If the
     * state is not valid, the <code>EPPCodecException</code> will contain a
     * description of the error.  throws EPPCodecException State error. This
     * will contain the name of the attribute that is not valid.
     *
     * @throws EPPCodecException Thrown if the instance is in an invalid state
     */
    void validateState() throws EPPCodecException {
        // flags
        if (flags == UNSPEC_FLAGS) {
            throw new EPPCodecException("EPPSecDNSExtKeyData required element flags is not set");
        }
        else if ((flags < MIN_FLAGS) && (flags > MAX_FLAGS)) {
				throw new EPPCodecException("EPPSecDNSExtKeyData flags of " + flags
											+ " is out of range, must be between "
											+ MIN_FLAGS + " and " + MAX_FLAGS);

		}
        
        // protocol
        if (protocol == UNSPEC_PROTOCOL) {
            throw new EPPCodecException("EPPSecDNSExtKeyData required element protocol is not set");
        }
        else if ((protocol < MIN_PROTOCOL) && (flags > MAX_PROTOCOL)) {
			throw new EPPCodecException("EPPSecDNSExtKeyData protocol of " + protocol
										+ " is out of range, must be between "
										+ MIN_PROTOCOL + " and " + MAX_PROTOCOL);

        }
        
        // alg
        if (alg == UNSPEC_ALG) {
            throw new EPPCodecException("EPPSecDNSExtKeyData required element alg is not set");
        }
        else if ((alg < MIN_ALG) && (alg > MAX_ALG)) {
			throw new EPPCodecException("EPPSecDNSExtKeyData alg of " + alg
										+ " is out of range, must be between "
										+ MIN_ALG + " and " + MAX_ALG);

        }
        
        // pubKey
        if (pubKey == null) {
            throw new EPPCodecException("EPPSecDNSExtKeyData required element pubKey is not set");
        }
        else {
        	// validate base64Binary pubKey?
        }
    }

    /**
     * Clone <code>EPPSecDNSExtKeyData</code>.
     *
     * @return clone of <code>EPPSecDNSExtKeyData</code>
     *
     * @exception CloneNotSupportedException standard Object.clone exception
     */
    public Object clone() throws CloneNotSupportedException {

        EPPSecDNSExtKeyData clone = null;

        clone = (EPPSecDNSExtKeyData) super.clone();

        clone.flags = flags;
        clone.protocol = protocol;
        clone.alg = alg;
        clone.pubKey = pubKey;

        return clone;
    }


    /**
     * Get secDNS:flags value
     * @return an <code>int</code> value representing secDNS:flags
     * @see <code>UNSPEC_FLAGS</code>
     */
    public int getFlags() {
        return flags;
    }
    
    /**
     * Set secDNS:flags value
     * @param flags an <code>int</code> value representing secDNS:flags
     * @see <code>FLAGS_ZONE_KEY_SEP</code>
     * @see <code>FLAGS_ZONE_KEY</code>
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }
    
    /**
     * Get secDNS:protocol value
     * @return an <code>int</code> value representing secDNS:protocol
     * @see <code>DEFAULT_PROTOCOL</code>
     * @see <code>UNSPEC_PROTOCOL</code>
     */
    public int getProtocol() {
        return protocol;
    }
    
    /**
     * Set secDNS:protocol value
     * @param protocol an <code>int</code> value representing secDNS:protocol
     */
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }
    
    /**
     * Get secDNS:alg value
     * @return an <code>int</code> value representing secDNS:alg
     * @see <code>UNSPEC_ALG</code>
     */
    public int getAlg() {
        return alg;
    }
    
    /**
     * Set secDNS:alg value
     * @param alg an <code>int</code> value representing secDNS:alg
     */
    public void setAlg(int alg) {
        this.alg = alg;
    }
    
    /**
     * Get secDNS:pubKey value
     * @return the canonical representation of the base64Binary secDNS:pubKey
     * @see com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtKeyData#setPubKey(String pubKey)
     */
    public String getPubKey() {
        return pubKey;
    }
    
    /**
     * Set secDNS:pubKey value
     * This method will remove all whitespace
     * in an effort to produce the canonical representation for base64Binary
     * as defined by http://www.w3.org/TR/xmlschema-2/#base64Binary
     * @param pubKey an <code>String</code> value representing the base64Binary secDNS:pubKey
     * @see com.verisign.epp.codec.gen.EPPUtil#removeWhitespace(String inString)
     */
    public void setPubKey(String pubKey) {
    	this.pubKey = EPPUtil.removeWhitespace(pubKey);
    }
    
	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
    public String toString() {
    	return EPPUtil.toString(this);
    }
    
}