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
package com.verisign.epp.codec.gen;


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

import java.util.Date;
import java.util.HashMap;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

// EPP Imports
import com.verisign.epp.types.Duration;
import com.verisign.epp.util.EPPCatFactory;


/**
 * An Optional &lt;dcp&gt; (data collection policy) element that contains child
 * elements used to describe the server's policy for data collection and
 * management.Polcy elements should be discolsed to all entities directly and
 * indirectly invloved in subsequent server interactions, Child elements
 * include the following
 * 
 * <ul>
 * <li>
 * An access &ltaccess&gt element that describes the acccess provided by the
 * server
 * </li>
 * <li>
 * One or more statement &ltstatement&gt elements that describe the data
 * collection purpouses     supported by the server. Use methods
 * <code>getAccess</code> and <code>setAccess</code> to get and set the Access
 * element(s), Use methods <code>getStatement</code>  and
 * <code>setStatement</code> to get and set the Statement elements(s).
 * </li>
 * </ul>
 * 
 *
 * @author $Author: jim $
 * @version $Revision: 1.7 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPService
 */
public class EPPDcp implements EPPCodecComponent {
	/** No expiry specified.  This is the default value. */
	public static final short EXPIRY_NONE = 0;

	/**
	 * Constant used with the Expiry attribute that means  the policy is valid
	 * from the current date and time until it expires on the specified date
	 * and time.
	 */
	public static final short EXPIRY_ABSOLUTE = 1;

	/**
	 * Constant used with the Expiry attribute that means  the policy is valid
	 * from the current date and time until the end of the specified duration.
	 */
	public static final short EXPIRY_RELATIVE = 2;

	/**
	 * Constant used with the Access attribute that means access is given to
	 * all identified data.
	 */
	public static final short ACCESS_ALL = 0;

	/**
	 * Constant used with the Access attribute that means No access is provided
	 * to identified data.
	 */
	public static final short ACCESS_NONE = 1;

	/**
	 * Constant used with the Access attribute that means data is not
	 * persistent, so no access is possible.
	 */
	public static final short ACCESS_NULL = 2;

	/**
	 * Constant used with the Access attribute that means access is given to
	 * identified data relating to individuals and organizational entities.
	 */
	public static final short ACCESS_PERSONAL = 3;

	/**
	 * Constant used with the Access attribute that means access is given to
	 * identified data relating to individuals, organizational entities, and
	 * other data of a non-personal nature.
	 */
	public static final short ACCESS_PERSONAL_AND_OTHER = 4;

	/**
	 * Constant used with the Access attribute that means access is given to
	 * other identified data of a non- personal nature.
	 */
	public static final short ACCESS_OTHER = 5;

	/**
	 * Default XML root tag name for <code>EPPDcp</code>.  This is the tag name
	 * used in <code>EPPGreeting</code>.
	 */
	static final String ELM_NAME = "dcp";

	/** XML tag name for the <code>access</code> element. */
	private static final String ELM_ACCESS = "access";

	/** XML tag name for the access <code>all</code> element. */
	private static final String ELM_ACCESS_ALL = "all";

	/** XML tag name for the access <code>none</code> element. */
	private static final String ELM_ACCESS_NONE = "none";

	/** XML tag name for the access <code>null</code> element. */
	private static final String ELM_ACCESS_NULL = "null";

	/** XML tag name for the access <code>personal</code> element. */
	private static final String ELM_ACCESS_PERSONAL = "personal";

	/** XML tag name for the access <code>personalAndOther</code> element. */
	private static final String ELM_ACCESS_PERSONAL_AND_OTHER =
		"personalAndOther";

	/** XML tag name for the access <code>other</code> element. */
	private static final String ELM_ACCESS_OTHER = "other";

	/** XML tag name for the <code>expiry</code> element. */
	private static final String ELM_EXPIRY = "expiry";

	/** XML tag name for the expiry <code>absolute</code> element. */
	private static final String ELM_EXPIRY_ABSOLUTE = "absolute";

	/** XML tag name for the expiry <code>relative</code> element. */
	private static final String ELM_EXPIRY_RELATIVE = "relative";

	/**
	 * Elements that match the <code>ACCESS_</code> constant values of the
	 * Access attribute.
	 */
	private final static String[] accessElms =
	{
		ELM_ACCESS_ALL, ELM_ACCESS_NONE, ELM_ACCESS_NULL, ELM_ACCESS_PERSONAL,
		ELM_ACCESS_PERSONAL_AND_OTHER, ELM_ACCESS_OTHER
	};

	/** Look up access constant given access value tag name */
	private static HashMap accessElmHash;

	static {
		// Setup access element hash
		accessElmHash = new HashMap();

		accessElmHash.put(EPPUtil.getLocalName(ELM_ACCESS_ALL), new Short(ACCESS_ALL));
		accessElmHash.put(EPPUtil.getLocalName(ELM_ACCESS_NONE), new Short(ACCESS_NONE));
		accessElmHash.put(EPPUtil.getLocalName(ELM_ACCESS_NULL), new Short(ACCESS_NULL));
		accessElmHash.put(EPPUtil.getLocalName(ELM_ACCESS_PERSONAL), new Short(ACCESS_PERSONAL));
		accessElmHash.put(
				EPPUtil.getLocalName(ELM_ACCESS_PERSONAL_AND_OTHER),
						  new Short(ACCESS_PERSONAL_AND_OTHER));
		accessElmHash.put(EPPUtil.getLocalName(ELM_ACCESS_OTHER), new Short(ACCESS_OTHER));
	}

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPDcp.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * describes the access provided by the server to the client on behalf of
	 * the originating data source. Uses one of the <code>ACCESS_</code>
	 * constants.  Defaults to all (<code>ACCESS_ALL</code>).
	 */
	private short access = ACCESS_ALL;

	/** Vector of <code>EPPStatement</code> instances. */
	private Vector statements = null;

	/**
	 * Expiry absolute date.  This is an optional attribute  that is mutually
	 * exclusive with <code>expiryRelative</code>.
	 */
	private Date expiryAbsolute = null;

	/**
	 * Expiry relative duration.  This is an optional attribute  that is
	 * mutually exclusive with <code>expiryAbsolute</code>.
	 */
	private Duration expiryRelative = null;

	/**
	 * Default constructor.
	 */
	public EPPDcp() {
	}

	// End EPPDcp.EPPDcp()

	/**
	 * Allocates a new <code>EPPDcp</code> and sets all of the required
	 * attributes to the arguments values.  The expiry is set to
	 * <code>EXPIRY_NONE</code>.
	 *
	 * @param aAccess Using one of the <code>ACCESS_</code> constants
	 * @param aStatements vector of <code>EPPStatement</code> instances
	 */
	public EPPDcp(short aAccess, Vector aStatements) {
		this.access		    = aAccess;
		this.statements     = aStatements;
	}

	// End EPPDcp.EPPDcp(short, Vector)

	/**
	 * Allocates a new <code>EPPDcp</code> and sets all of the required
	 * attributes and an absolute expiry.
	 *
	 * @param aAccess Using one of the <code>ACCESS_</code> constants
	 * @param aStatements vector of <code>EPPStatement</code> instances
	 * @param aExpiryAbsolute Absolute expiry date
	 */
	public EPPDcp(short aAccess, Vector aStatements, Date aExpiryAbsolute) {
		this.access		    = aAccess;
		this.statements     = aStatements;
		this.setExpiryAbsolute(aExpiryAbsolute);
	}

	// End EPPDcp.EPPDcp(short, Vector, Date)

	/**
	 * Allocates a new <code>EPPDcp</code> and sets all of the required
	 * attributes and a relative expiry duration.
	 *
	 * @param aAccess Using one of the <code>ACCESS_</code> constants
	 * @param aStatements vector of <code>EPPStatement</code> instances
	 * @param aExpiryRelative Relative expiry duration
	 */
	public EPPDcp(short aAccess, Vector aStatements, Duration aExpiryRelative) {
		this.access		    = aAccess;
		this.statements     = aStatements;
		this.setExpiryRelative(aExpiryRelative);
	}

	// End EPPDcp.EPPDcp(short, Vector, Duration)

	/*
	 * gets the Statements Object assocaited with the DataCollectionPolicy Object
	 *
	 * @return <code>Vector</code> of <code>EPPStatement</code> instances if defiend;
	 * <code>null</code> otherwise.
	 */
	public Vector getStatements() {
		return this.statements;
	}

	/**
	 * Sets the list of supported/desired Statement objects.  An EPP Client
	 * will set the list of statement objects associated with the     EPP
	 * Server.
	 *
	 * @param aStatements Vector of <code>EPPStatement</code> instances.
	 */
	public void setStatements(Vector aStatements) {
		this.statements = aStatements;
	}

	// End EPPDcp.setStatements(Vector)

	/**
	 * Adds a supported/desired Statement object.  An EPP Client     will set
	 * the list of statement objects associated with the     EPP Server.
	 *
	 * @param aStatement Statements to add
	 */
	public void addStatement(EPPStatement aStatement) {
		if (this.statements == null) {
			this.statements = new Vector();
		}

		this.statements.addElement(aStatement);
	}

	// End EPPDcp.setStatements(EPPStatement)

	/*
	 * Gets the Access Policy associated with the Data Collection Policy Object
	 *
	 * @return Access provided by the server if defined; <code>null</code> otherwise.
	 */
	public short getAccess() {
		return this.access;
	}

	/**
	 * Sets the Access service associated with the DataCollectionPolciy Objecat
	 *
	 * @param aAccess Describes access provided by the server
	 */
	public void setAccess(short aAccess) {
		this.access = aAccess;
	}

	// End EPPDcp.setAccess(short)

	/**
	 * Gets the expiry absolute date.
	 *
	 * @return Absolute expiry date if defined; <code>null</code> otherwise.
	 */
	public Date getExpiryAbsolute() {
		return this.expiryAbsolute;
	}

	/**
	 * Sets the expiry to an absolute date.  This is will set the  expiry
	 * relative attribute to <code>null</code> since they are mutually
	 * exclusive.
	 *
	 * @param aExpiryDate Date when DCP expires
	 */
	public void setExpiryAbsolute(Date aExpiryDate) {
		this.expiryAbsolute     = aExpiryDate;
		this.expiryRelative     = null;
	}

	/**
	 * Gets the expiry relative duration.
	 *
	 * @return Relative expiry duration if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public Duration getExpiryyRelative() {
		return this.expiryRelative;
	}

	/**
	 * Sets the expiry to an relative duration.  This is will set the  expiry
	 * absolute attribute to <code>null</code> since they are mutually
	 * exclusive.
	 *
	 * @param aDuration Duration that the DCP is valid
	 */
	public void setExpiryRelative(Duration aDuration) {
		this.expiryRelative     = aDuration;
		this.expiryAbsolute     = null;
	}

	/**
	 * Gets the expiry type by returning one of the <code>EXPIRY_</code>
	 * constants.  Use the appropriate <code>getExpiry</code> method based on
	 * the type.
	 *
	 * @return <code>EXPIRY_</code> constant value.
	 */
	public short getExpiryType() {
		if ((this.expiryAbsolute == null) && (this.expiryRelative == null)) {
			return EXPIRY_NONE;
		}
		else if (this.expiryAbsolute != null) {
			return EXPIRY_ABSOLUTE;
		}
		else {
			return EXPIRY_RELATIVE;
		}
	}

	// End getExpiryType()

	/**
	 * encode <code>EPPDcp</code> into a DOM element tree.  The "dcp" element
	 * is created and the <code>access</code>,<code>statement</code> nodes are
	 * appended as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return dcp root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element theElm = null;
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Access
		if ((access < 0) || (access >= accessElms.length)) {
			cat.error("EPPDcp.encode(): Unknown access value of " + access);
			throw new EPPEncodeException("EPPDcp.encode(): Unknown access value of "
										 + access);
		}

		Element theAccessElm =
			aDocument.createElementNS(EPPCodec.NS, ELM_ACCESS);
		root.appendChild(theAccessElm);
		theElm = aDocument.createElementNS(EPPCodec.NS, accessElms[access]);
		theAccessElm.appendChild(theElm);

		// Statements
		EPPUtil.encodeCompVector(aDocument, root, this.statements);

		Element theExpiryElm = null;

		// Expiry 
		switch (this.getExpiryType()) {
			case EXPIRY_NONE:

				// Do nothing
				break;

			case EXPIRY_ABSOLUTE:
				theExpiryElm =
					aDocument.createElementNS(EPPCodec.NS, ELM_EXPIRY);
				root.appendChild(theExpiryElm);
				EPPUtil.encodeString(
									 aDocument, theExpiryElm,
									 EPPUtil.encodeTimeInstant(this.expiryAbsolute),
									 EPPCodec.NS, ELM_EXPIRY_ABSOLUTE);

				break;

			case EXPIRY_RELATIVE:
				theExpiryElm =
					aDocument.createElementNS(EPPCodec.NS, ELM_EXPIRY);
				root.appendChild(theExpiryElm);
				EPPUtil.encodeString(
									 aDocument, theExpiryElm,
									 this.expiryRelative.toString(), EPPCodec.NS,
									 ELM_EXPIRY_RELATIVE);

				break;

			default:
				cat.error("EPPDcp.encode(): Invalid expiry type of "
						  + this.getExpiryType());
				throw new EPPEncodeException("EPPDcp.encode(): Invalid expiry type of "
											 + this.getExpiryType());
		} // end switch (this.getExpiryType())

		return root;
	}


	/**
	 * decode <code>EPPDcp</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltdcp&gt element
	 *
	 * @param aElement root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		Element theElm = null;

		// Access
		theElm = EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_ACCESS);

		if (theElm == null) {
			cat.error("EPPDcp.decode(): could not find access element");
			throw new EPPDecodeException("EPPDcp could not find access element");
		}

		Element theAccessValueElm = EPPUtil.getFirstElementChild(theElm);

		if (theAccessValueElm == null) {
			cat.error("EPPDcp.decode(): could not find access element value");
			throw new EPPDecodeException("EPPDcp could not find access element value");
		}

		Short theAccessValue =
			(Short) accessElmHash.get(theAccessValueElm.getLocalName());

		if (theAccessValue == null) {
			cat.error("EPPDcp.decode(): could not find valid access element value");
			throw new EPPDecodeException("EPPDcp could not find valid access element value");
		}

		this.access     = theAccessValue.shortValue();

		// Statements
		this.statements =
			EPPUtil.decodeCompVector(
									 aElement, EPPCodec.NS,
									 EPPStatement.ELM_NAME, EPPStatement.class);

		// Expiry
		Element theExpiryElm =
			EPPUtil.getElementByTagNameNS(aElement, EPPCodec.NS, ELM_EXPIRY);

		if (theExpiryElm != null) {
			Element theExpiryValue =
				EPPUtil.getElementByTagNameNS(theExpiryElm, EPPCodec.NS, ELM_EXPIRY_ABSOLUTE);

			// Absolute expiry?
			if (theExpiryValue != null) {
				this.setExpiryAbsolute(EPPUtil.decodeTimeInstant(theExpiryValue.getFirstChild()
																			   .getNodeValue()));
			}
			else // Relative expiry
			 {
				theExpiryValue =
					EPPUtil.getElementByTagNameNS(
												theExpiryElm, EPPCodec.NS, 
												ELM_EXPIRY_RELATIVE);

				if (theExpiryValue == null) {
					cat.error("EPPDcp.decode(): Could not find valid expiry element");
					throw new EPPDecodeException("EPPDcp.decode(): Could not find valid expiry element");
				}

				try {
					this.setExpiryRelative(Duration.parseDuration(theExpiryValue.getFirstChild()
																				.getNodeValue()));
				}
				 catch (java.text.ParseException ex) {
					cat.error("EPPDcp.decode(): xception parsing relative expiry value "
							  + theExpiryValue.getFirstChild().getNodeValue()
							  + ": " + ex);
					throw new EPPDecodeException("EPPDcp.decode(): Exception parsing relative expiry value "
												 + theExpiryValue.getFirstChild()
																 .getNodeValue()
												 + ": " + ex);
				}
			}
		}
		else {
			this.expiryAbsolute     = null;
			this.expiryRelative     = null;
		}
	}

	/**
	 * implements a deep <code>EPPDcp</code> compare.
	 *
	 * @param aObject <code>EPPDcp</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDcp)) {
			cat.error("EPPDCP.equals(): " + aObject.getClass().getName()
					  + " not EPPDcp instance");

			return false;
		}

		EPPDcp theDCP = (EPPDcp) aObject;

		// Access
		if (this.access != theDCP.access) {
			cat.error("EPPDCP.equals(): access not equal");

			return false;
		}

		// statements
		if (!EPPUtil.equalVectors(this.statements, theDCP.statements)) {
			cat.error("EPPDCP.equals(): statements not equal");

			return false;
		}

		// Expiry
		if (
			!(
					(this.expiryAbsolute == null)
						? (theDCP.expiryAbsolute == null)
							: this.expiryAbsolute.equals(theDCP.expiryAbsolute)
				)) {
			cat.error("EPPDCP.equals(): expiryAbsolute not equal");

			return false;
		}

		if (
			!(
					(this.expiryRelative == null)
						? (theDCP.expiryRelative == null)
							: this.expiryRelative.equals(theDCP.expiryRelative)
				)) {
			cat.error("EPPDCP.equals(): expiryRelative not equal");

			return false;
		}

		return true;
	}

	// End EPPDcp.equals(Object)

	/**
	 * Clone <code>EPPDcp</code>.
	 *
	 * @return clone of <code>EPPDcp</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDcp clone = null;

		clone     = (EPPDcp) super.clone();

		//access
		clone.access     = this.access;

		// Statments
		clone.statements = (Vector) this.statements.clone();

		for (int i = 0; i < this.statements.size(); i++)
			clone.statements.setElementAt(
										  ((EPPStatement) this.statements
										   .elementAt(i)).clone(), i);

		// Expiry
		if (this.expiryAbsolute != null) {
			clone.expiryAbsolute = (Date) this.expiryAbsolute.clone();
		}

		if (this.expiryRelative != null) {
			clone.expiryRelative = new Duration(this.expiryRelative.toLong());
		}

		return clone;
	}

	// End EPPDcp.clone()

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

	// End EPPDcp.toString()
}


// End class EPPDcp
