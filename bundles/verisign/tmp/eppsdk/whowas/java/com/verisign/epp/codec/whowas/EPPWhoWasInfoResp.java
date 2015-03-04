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
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents an EPP WhoWas &lt;whowas:infData&gt; response
 * {@link EPPWhoWasInfoCmd}. When an &lt;info&gt; command has been processed
 * successfully, the EPP &lt;resData&gt; element MUST contain a child
 * &lt;whowas:infData&gt; element that identifies the whowas namespace and the
 * location of the whowas schema. The &lt;whowas:infData&gt; element contains
 * the following child elements: <br>
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
 * <li>A &lt;whowas:history&gt; element that contains records with history
 * information of the given entity. The <whowas:history> element MUST contain
 * one or more of &lt;whowas:rec&gt;elements.
 * <ul>
 * <li>A &lt;whowas:rec&gt; element contains a single history record for the
 * given entity name or identifier.The <whowas:rec> element MUST contain
 * following elements:</li>
 * <ul>
 * <li>A &lt;whowas:date&gt; element containing the date and time when the
 * operation has been executed.</li>
 * <li>A &lt;whowas:name&gt; element containing the name of a entity whose
 * information has been looked up.</li>
 * <li>A &lt;whowas:newName&gt; OPTIONAL element that contains a new name of the
 * entity.</li>
 * <li>A &lt;whowas:roid&gt; element containing the identifier of an entity
 * whose information has been looked up.</li>
 * <li>A &lt;whowas:op&gt; element containing the name of an operation that has
 * been executed on an entity.</li>
 * <li>A &lt;whowas:clID&gt; element contain the identifier of an owner client
 * or, in case of TRANSFER related operation, the identifier of gaining client.</li>
 * <li>A &lt;whowas:clName&gt; element contain the full name of the client for
 * the id returned in the &lt;whowas:clID&gt;element.</li>
 * </ul>
 * </ul> </ul>
 * 
 * @author Deepak Deshpande
 * @version 1.0
 * @see EPPWhoWasInfoCmd
 */
public class EPPWhoWasInfoResp extends EPPResponse {

	/** XML Element Name of {@link EPPWhoWasInfoResp} root element. */
	static final String ELM_NAME = "whowas:infData";

	/**
	 * The WhoWas entity type with default of
	 * <code>EPPWhoWasConstants.TYPE_DOMAIN</code>.
	 */
	private String whowasType = EPPWhoWasConstants.TYPE_DOMAIN;

	/** The WhoWas entity name. */
	private String name = null;

	/** The WhoWas entity Registry Object IDentifier (roid). */
	private String roid = null;

	/** The WhoWas History. */
	private EPPWhoWasHistory history = null;

	/** Log4j category for logging */
	private static Logger cat =
			Logger.getLogger( EPPWhoWasInfoResp.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );


	/**
	 * Default constructor that needs the <code>type</code>, either
	 * <code>name</code> or <code>roid</code> attributes and the transid attribute
	 * set prior to calling <code>encode</code>.
	 */
	public EPPWhoWasInfoResp () {
		// default constructor
	}


	/**
	 * Creates an {@link EPPWhoWasInfoResp} only the transaction id set. The
	 * <code>type</code> and either <code>name</code> or <code>roid</code>
	 * attributes must be set prior to calling <code>encode</code> .
	 * 
	 * @param aTransId
	 *        The transaction id containing the server transaction and optionally
	 *        the client transaction id
	 */
	public EPPWhoWasInfoResp ( EPPTransId aTransId ) {
		super( aTransId );
	}


	/**
	 * Creates an {@link EPPWhoWasInfoResp} only the transaction id and type set.
	 * Either <code>name</code> or <code>roid</code> attributes must be set prior
	 * to calling <code>encode</code> .
	 * 
	 * @param aTransId
	 *        The transaction id containing the server transaction and optionally
	 *        the client transaction id
	 * @param aWhoWasType
	 *        The entity WhoWas type
	 */
	public EPPWhoWasInfoResp ( EPPTransId aTransId, String aWhoWasType ) {
		super( aTransId );
		this.whowasType = aWhoWasType;
	}


	/**
	 * Does a deep clone of the {@link EPPWhoWasInfoResp} instance.
	 * 
	 * @return Cloned instance
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPWhoWasInfoResp clone = (EPPWhoWasInfoResp) super.clone();

		if ( this.history != null ) {
			clone.history = (EPPWhoWasHistory) this.history.clone();
		}

		return clone;
	}


	/**
	 * Compares two {@link EPPWhoWasInfoResp} instances.
	 * 
	 * @return <code>true</code> if equal;<code>false</code> otherwise.
	 */
	public boolean equals ( Object o ) {

		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {

			if ( !super.equals( o ) ) {
				cat.error( "EPPWhoWasInfoResp.equals(): super class not equal" );
				return false;
			}

			EPPWhoWasInfoResp other = (EPPWhoWasInfoResp) o;

			if ( !EqualityUtil.equals( this.whowasType, other.whowasType ) ) {
				cat.error( "EPPWhoWasInfoResp.equals(): type not equal" );
				return false;
			}

			if ( !EqualityUtil.equals( this.name, other.name ) ) {
				cat.error( "EPPWhoWasInfoResp.equals(): name not equal" );
				return false;
			}

			if ( !EqualityUtil.equals( this.roid, other.roid ) ) {
				cat.error( "EPPWhoWasInfoResp.equals(): roid not equal" );
				return false;
			}

			if ( !EqualityUtil.equals( this.history, other.history ) ) {
				cat.error( "EPPWhoWasInfoResp.equals(): history not equal" );
				return false;
			}

			return true;
		}
		else {
			cat.error( "EPPWhoWasInfoCmd.equals(): not EPPDomainInfoResp instance" );
			return false;
		}
	}


	/**
	 * Gets the EPP command namespace associated with {@link EPPWhoWasInfoResp}.
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
	 * Decode the {@link EPPWhoWasInfoResp} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *        Root DOM Element to decode {@link EPPWhoWasInfoResp} from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	protected void doDecode ( Element aElement ) throws EPPDecodeException {

		this.whowasType =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_TYPE );

		if ( this.whowasType == null ) {
			throw new EPPDecodeException( "Expected type element" );
		}

		this.name =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_NAME );

		this.roid =
				EPPUtil.decodeString( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_ROID );

		if ( this.name == null && this.roid == null ) {
			throw new EPPDecodeException( "Expected one of the name or roid elements" );
		}

		this.history =
				(EPPWhoWasHistory) EPPUtil.decodeComp( aElement,
						EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_HISTORY,
						EPPWhoWasHistory.class );
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@link EPPWhoWasInfoResp} instance.
	 * 
	 * @param aDocument
	 *        DOM Document that is being built. Used as an Element factory.
	 * @return Element Root DOM Element representing the {@link EPPWhoWasInfoResp}
	 *         instance.
	 * @exception EPPEncodeException
	 *            Unable to encode {@link EPPWhoWasInfoResp} instance.
	 */
	protected Element doEncode ( Document aDocument ) throws EPPEncodeException {

		if ( aDocument == null ) {
			throw new EPPEncodeException(
					"aDocument is null in EPPWhoWasInfoResp.encode(Document)" );
		}

		// Required type attribute not set?
		if ( this.whowasType == null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoResp type attribute must be set." );
		}

		// Required name or roid choice attributes not set?
		if ( this.name == null && this.roid == null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoResp name or roid attributes must be set" );
		}

		// Both name and roid choice attributes set?
		if ( this.name != null && this.roid != null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoResp both name and roid attributes must not be set" );
		}

		if ( this.history == null ) {
			throw new EPPEncodeException(
					"EPPWhoWasInfoResp is missing required element:history" );
		}

		Element root = aDocument.createElementNS( EPPWhoWasMapFactory.NS, ELM_NAME );
		root.setAttribute( "xmlns:whowas", EPPWhoWasMapFactory.NS );
		root.setAttributeNS( EPPCodec.NS_XSI, "xsi:schemaLocation",
				EPPWhoWasMapFactory.NS_SCHEMA );

		EPPUtil.encodeString( aDocument, root, this.whowasType,
				EPPWhoWasMapFactory.NS, EPPWhoWasConstants.ELM_TYPE );

		EPPUtil.encodeString( aDocument, root, this.name, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_NAME );

		EPPUtil.encodeString( aDocument, root, this.roid, EPPWhoWasMapFactory.NS,
				EPPWhoWasConstants.ELM_ROID );

		EPPUtil.encodeComp( aDocument, root, this.history );

		return root;
	}


	/**
	 * Gets the EPP response type associated with
	 * <code>EPPSuggestionInfoResp</code>.
	 * 
	 * @return <code>EPPSuggestionInfoResp.ELM_NAME</code>
	 */
	public String getType () {
		return ELM_NAME;
	}


	/**
	 * Returns the name
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
	 * Returns the history
	 * 
	 * @return the history
	 */
	public EPPWhoWasHistory getHistory () {
		return this.history;
	}


	/**
	 * Sets history value to <code>aHistory</code>.
	 * 
	 * @param aHistory
	 *        the history to set
	 */
	public void setHistory ( EPPWhoWasHistory aHistory ) {
		this.history = aHistory;
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
