/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/

package com.verisign.epp.codec.whowas;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPInfoCmd;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents an EPP WhoWas &lt;info&gt; command that is used to retrieve
 * history information based on the supplied entity identifier or entity name.
 * The &lt;whowas:info&gt; element MUST contain the following child elements: <br>
 * <br>
 * <ul>
 * <li>A &lt;whowas:type&gt; element that contains type of the entity whose
 * history needs to be looked up.</li>
 * <li>Followed by either:</li>
 * <ul>
 * <li>A &lt;whowas:name&gt; element that contains the name of an entity whose
 * history needs to be looked up.</li>
 * <li>A &lt;whowas:roid&gt; element that contains the identifier of an entity
 * whose history needs to be looked up.</li>
 * </ul>
 * </ul> <br> {@link EPPWhoWasInfoResp} is the concrete
 * {@link com.verisign.epp.codec.gen.EPPResponse} associated with
 * {@link EPPWhoWasInfoCmd}.<br>
 * <br>
 * 
 * @author Deepak Deshpande
 * @version 1.0
 * @see EPPWhoWasInfoResp
 */
public class EPPWhoWasInfoCmd extends EPPInfoCmd {

	/** XML Element Name of {@link EPPWhoWasInfoCmd} root element. */
	static final String ELM_NAME = "whowas:info";

	/** The WhoWas entity type with default of <code>EPPWhoWasConstants.TYPE_DOMAIN</code>. */
	private String whowasType = EPPWhoWasConstants.TYPE_DOMAIN;

	/** The WhoWas entity name. */
	private String name = null;

	/** The WhoWas entity Registry Object IDentifier (roid). */
	private String roid = null;
	
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPWhoWasInfoCmd.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Default constructor
	 */
	public EPPWhoWasInfoCmd () {
		// default constructor
	}


	/**
	 * This constructor calls the super <code>EPPInfoCmd(String)</code> method to
	 * set the transaction id for the command with default <code>type</code> of <code>EPPWhoWasConstants.TYPE_DOMAIN</code>.
	 * 
	 * @param aTransId
	 *        Transaction Id associated with command.
	 */
	public EPPWhoWasInfoCmd ( String aTransId ) {
		super( aTransId );
	}


	/**
	 * Creates an {@link EPPWhoWasInfoCmd} only the transaction id and type set.
	 * Either <code>name</code> or <code>roid</code> attributes must be set prior
	 * to calling <code>encode</code> .
	 * 
	 * @param aTransId
	 *        Transaction Id associated with command.
	 * @param aWhoWasType
	 *        The entity WhoWas type
	 */
	public EPPWhoWasInfoCmd ( String aTransId, String aWhoWasType ) {
		super( aTransId );
		this.whowasType = aWhoWasType;
	}


	/**
	 * Clone {@link EPPWhoWasInfoCmd}.
	 * 
	 * @return clone of {@link EPPWhoWasInfoCmd}.
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {
		return super.clone();
	}


	/**
	 * Compare an instance of {@link EPPWhoWasInfoCmd} with this instance.
	 */
	public boolean equals ( Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			if ( !super.equals( o ) ) {
				cat.error("EPPWhoWasInfoCmd.equals(): super class not equal");
				return false;
			}
			
			EPPWhoWasInfoCmd other = (EPPWhoWasInfoCmd) o;
			
			if ( !EqualityUtil.equals( this.whowasType, other.whowasType ) ) {
				cat.error("EPPWhoWasInfoCmd.equals(): type not equal");
				return false;
			}
			
			if ( !EqualityUtil.equals( this.name, other.name ) ) {
				cat.error("EPPWhoWasInfoCmd.equals(): name not equal");
				return false;
			}
			
			if ( !EqualityUtil.equals( this.roid, other.roid ) ) {
				cat.error("EPPWhoWasInfoCmd.equals(): roid not equal");
				return false;
			}
			
			return true;
		}
		else {
			cat.error("EPPWhoWasInfoCmd.equals(): not EPPWhoWasInfoCmd instance");
			return false;
		}
	}


	/**
	 * Gets the EPP command Namespace associated with {@link EPPWhoWasInfoCmd}.
	 * 
	 * @return <code>EPPWhoWasMapFactory.NS</code>
	 */
	public String getNamespace () {
		return EPPWhoWasMapFactory.NS;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * {@link com.verisign.epp.codec.gen.EPPCodecComponent}.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	public String toString () {
		return EPPUtil.toString( this );
	}


	/**
	 * Decode the {@link EPPWhoWasInfoCmd} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode {@link EPPWhoWasInfoCmd} from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	protected void doDecode ( Element aElement ) throws EPPDecodeException {

		this.whowasType =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_TYPE );

		if ( this.whowasType == null ) {
			throw new EPPDecodeException(
				"Expected type element" );
		}

		this.name =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_NAME );

		this.roid =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_ROID );

		if ( this.name == null && this.roid == null ) {
			throw new EPPDecodeException(
					"Expected one of the name or roid elements" );
		}
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@link EPPWhoWasInfoCmd} instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built. Used as an Element factory.
	 * @return Root DOM Element representing the {@link EPPWhoWasInfoCmd}
	 *         instance.
	 * @exception EPPEncodeException
	 *            Unable to encode {@link EPPWhoWasInfoCmd} instance.
	 */
	protected Element doEncode ( Document aDocument ) throws EPPEncodeException {

		// Required type attribute not set?
		if ( this.whowasType == null ) {
			throw new EPPEncodeException(
				"EPPWhoWasInfoCmd type attribute must be set." );
		}

		// Required name or roid choice attributes not set?
		if ( this.name == null && this.roid == null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoCmd name or roid attributes must be set" );
		}

		// Both name and roid choice attributes set?
		if ( this.name != null && this.roid != null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoCmd both name and roid attributes must not be set" );
		}

		Element root = aDocument.createElementNS( EPPWhoWasMapFactory.NS, ELM_NAME );
		root.setAttribute( "xmlns:whowas", EPPWhoWasMapFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPWhoWasMapFactory.NS_SCHEMA );

		EPPUtil.encodeString( aDocument, root, this.whowasType, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_TYPE );

		EPPUtil.encodeString( aDocument, root, this.name, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_NAME );

		EPPUtil.encodeString( aDocument, root, this.roid, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_ROID );

		return root;
	}


	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName () {
		return this.name;
	}


	/**
	 * Sets name value to <code>aName</code>.
	 * 
	 * @param aName
	 *        the name to set
	 */
	public void setName ( String aName ) {
		this.name = aName;
	}


	/**
	 * Returns the Registry Object IDentifier (roid).
	 * 
	 * @return the roid
	 */
	public String getRoid () {
		return this.roid;
	}


	/**
	 * Sets Registry Object IDentifier (roid) value to <code>aRoid</code>.
	 * 
	 * @param aRoid
	 *        the roid to set
	 */
	public void setRoid ( String aRoid ) {
		this.roid = aRoid;
	}


	/**
	 * Returns the whowasType
	 * 
	 * @return the whowasType
	 */
	public String getWhowasType () {
		return this.whowasType;
	}


	/**
	 * Sets whowasType value to aWhowasType. The default value is
	 * <code>EPPWhoWasConstants.TYPE_DOMAIN</code>.
	 * 
	 * @param aWhowasType
	 *        the whowasType to set
	 */
	public void setWhowasType ( String aWhowasType ) {
		this.whowasType = aWhowasType;
	}

}