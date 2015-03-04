/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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
package com.verisign.epp.codec.defReg;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

// EPP Imports
import com.verisign.epp.codec.gen.*;


/**
 * Represents an EPPDefReg &lt;create&gt; command, which provides a transform
 * operation that allows a client to create a defReg object.  In addition to
 * the standard EPP command elements, the &lt;create&gt; command MUST contain
 * a &lt;defReg:create&gt; element that identifies the defReg namespace and
 * the location of the defReg schema.     The &lt;defReg:create&gt; element
 * MUST contain the following child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A &lt;defReg:name&gt; element that contains name information associated with
 * the defReg object.   Use <code>getName</code> and <code>setName</code> to
 * get and set the defReg object.
 * </li>
 * <li>
 * An  &lt;defReg:registrant&gt; element that contains the identifier for the
 * human or organizational social information (contact) object to be
 * associated with the defReg object as the object registrant. Use
 * <code>getRegistrant</code> and <code>setRegistrant</code> to get     and
 * set the elements.
 * </li>
 * <li>
 * A &lt;defReg:tm&gt; OPTIONAL element that contains trademark information to
 * be associated with the defReg object.
 * </li>
 * <li>
 * A &lt;defReg:tmcountry&gt; OPTIONAL element that contains trademark country
 * information to be associated with the defReg object.
 * </li>
 * <li>
 * A &lt;defReg:tmdate&gt; OPTIONAL element that contains tradeamark date
 * information to be associated with the defReg object.
 * </li>
 * <li>
 * A &lt;defReg:admincontact&gt; element that contains admincontact information
 * to be associated with the defReg object.
 * </li>
 * <li>
 * An OPTIONAL &lt;defReg:period&gt; element that contains the initial
 * registration period of the defReg object. Use <code>getPeriod</code> and
 * <code>setPeriod</code> to get and set the element. If return
 * <code>null</code>, period has not been specified yet.
 * </li>
 * <li>
 * A &lt;defReg:authInfo&gt; element that contains authorization information to
 * be associated with the defReg object.
 * </li>
 * </ul>
 * 
 * <br> It is important to note that the transaction identifier associated with
 * successful creation of a defReg object becomes the authorization identifier
 * required to transfer sponsorship of the defReg object.  A client MUST
 * retain all transaction identifiers associated with defReg object creation
 * and protect them from disclosure.  A client MUST also provide a copy of the
 * transaction identifier information to the defReg registrant, who will need
 * this information to request a defReg transfer through a different client. <br>
 * <br>
 * <code>EPPDefRegCreateResp</code> is the concrete <code>EPPReponse</code>
 * associated     with <code>EPPDefRegCreateCmd</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see com.verisign.epp.codec.defReg.EPPDefRegCreateResp
 */
public class EPPDefRegCreateCmd extends EPPCreateCmd {
	/** XML Element Name of <code>EPPDefRegCreateCmd</code> root element. */
	final static String ELM_NAME = "defReg:create";

	/** XML Element Name for the <code>name</code> attribute. */
	private final static String ELM_DEFREG_NAME = "defReg:name";

	/** XML Element Name of <code>registrant</code> root element. */
	private final static String ELM_DEFREG_REGISTRANT = "defReg:registrant";

	/** XML Element Name for the <code>tm</code> attribute. */
	private final static String ELM_DEFREG_TM = "defReg:tm";

	/** XML Element Name for the <code>tmcountry</code> attribute. */
	private final static String ELM_DEFREG_TMCOUNTRY = "defReg:tmCountry";

	/** XML Element Name for the <code>tmdate</code> attribute. */
	private final static String ELM_DEFREG_TMDATE = "defReg:tmDate";

	/** XML tag name for the <code>admincontact</code> attribute. */
	private final static String ELM_DEFREG_ADMINCONTACT = "defReg:adminContact";

	/**
	 * XML Element roid attribute name of <code>EPPAuthInfo</code> root
	 * element.
	 */
	protected final static java.lang.String ATTR_LEVELID = "level";

	/** DefReg Name of defReg to create. */
	private String level = null;

	/** DefReg Name of defReg to create. */
	private EPPDefRegName defRegName = null;

	/** DefReg TradeMark of defReg to create . */
	private String tm = null;

	/** DefReg TradeMarkCountry of defReg to create . */
	private String tmcountry = null;

	/** DefReg TradeMarkDate of defReg to create . */
	private Date tmdate = null;

	/** DefReg AdminContact of defReg to create . */
	private String admincontact = null;

	/** Registration Period. */
	private EPPDefRegPeriod period = null;

	/** authorization information. */
	private EPPAuthInfo authInfo = null;

	/** registrant information. */
	private java.lang.String registrant = null;

	/**
	 * Allocates a new <code>EPPDefRegCreateCmd</code> with default attribute
	 * values.     the defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * name is set to <code>null</code>
	 * </li>
	 * <li>
	 * registrant is set to <code>null</code>
	 * </li>
	 * <li>
	 * period is set to <code>UNSPEC_PERIOD</code>
	 * </li>
	 * <li>
	 * tm is set to<code>null</code>.
	 * </li>
	 * <li>
	 * tmcountry is set to<code>null</code>.
	 * </li>
	 * <li>
	 * tmdate is set to<code>null</code>.
	 * </li>
	 * <li>
	 * admincontact is set to<code>null</code>.
	 * </li>
	 * </ul>
	 * 
	 * <br>     The name must be set before invoking <code>encode</code>.
	 */
	public EPPDefRegCreateCmd() {
		defRegName		 = null;
		registrant		 = null;
		tm				 = null;
		tmcountry		 = null;
		tmdate			 = null;
		period			 = null;
		admincontact     = null;
	}

	// End EPPDefRegCreateCmd.EPPDefRegCreateCmd()

	/**
	 * Allocates a new <code>EPPDefRegCreateCmd</code> with all attributes
	 * specified     by the arguments.
	 *
	 * @param aTransId Transaction Id associated with command.
	 * @param aDefRegName EPPDefRegName name
	 * @param aRegistrant DefReg Registrant
	 * @param aTm DefReg TradeMark
	 * @param aTmCountry DefReg TradeMarkCountry
	 * @param aTmDate DefReg TradeMarkDate
	 * @param aAdminContact DefReg AdminContact
	 * @param aPeriod DefReg Registration Period.
	 * @param aAuthInfo EPPAuthInfo    authorization information
	 */
	public EPPDefRegCreateCmd(
							  String aTransId, EPPDefRegName aDefRegName,
							  String aRegistrant, String aTm, String aTmCountry,
							  Date aTmDate, String aAdminContact,
							  EPPDefRegPeriod aPeriod, EPPAuthInfo aAuthInfo) {
		super(aTransId);
		defRegName		 = aDefRegName;
		registrant		 = aRegistrant;
		tm				 = aTm;
		tmcountry		 = aTmCountry;
		tmdate			 = aTmDate;
		admincontact     = aAdminContact;
		authInfo		 = aAuthInfo;
		authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		period = aPeriod;
	}

	// End EPPDefRegCreateCmd.EPPDefRegCreateCmd(String, EPPDefRegName, String, String,String,Date,String,EPPAuthInfo)

	/**
	 * Get the EPP command Namespace associated with EPPDefRegCreateCmd.
	 *
	 * @return <code>EPPDefRegMapFactory.NS</code>
	 */
	public String getNamespace() {
		return EPPDefRegMapFactory.NS;
	}

	// End EPPDefRegCreateCmd.getNamespace()

	/**
	 * Validate the state of the <code>EPPDefRegCreateCmd</code> instance.  A
	 * valid state means that all of the required attributes have been set. If
	 * validateState     returns without an exception, the state is valid. If
	 * the state is not     valid, the <code>EPPCodecException</code> will
	 * contain a description of the error.     throws     EPPCodecException
	 * State error.  This will contain the name of the     attribute that is
	 * not valid.
	 *
	 * @throws EPPCodecException DOCUMENT ME!
	 */
	void validateState() throws EPPCodecException {
		if (defRegName == null) {
			throw new EPPCodecException("name required attribute is not set");
		}

		if (registrant == null) {
			throw new EPPCodecException("registrant required attribute is not set");
		}

		if (admincontact == null) {
			throw new EPPCodecException("admincontact required attribute is not set");
		}

		if (authInfo == null) {
			throw new EPPCodecException("authInfo required attribute is not set");
		}
	}

	// End EPPDefRegCreateCmd.isValid()

	/**
	 * Get the EPPDefRegName information.
	 *
	 * @return EPPDefRegName element
	 */
	public EPPDefRegName getDefRegName() {
		return defRegName;
	}

	// End EPPDefRegCreateCmd.getDefRegName()

	/**
	 * Set the defRegName information.
	 *
	 * @param aName EPPDefRegName name object
	 */
	public void setDefRegName(EPPDefRegName aName) {
		defRegName = aName;
	}

	// End EPPDefRegCreateCmd.setForwardTo(String)

	/**
	 * Get the registrant information.
	 *
	 * @return DefReg registrant
	 */
	public String getRegistrant() {
		return registrant;
	}

	// End EPPDefRegCreateCmd.getRegistrant()

	/**
	 * Set the registrant information.
	 *
	 * @param aRegistrant DefReg registrant
	 */
	public void setRegistrant(String aRegistrant) {
		registrant = aRegistrant;
	}

	// End EPPDefRegCreateCmd.setForwardTo(String)

	/**
	 * Get the trademark information.
	 *
	 * @return DefReg tradeMark
	 */
	public String getTm() {
		return tm;
	}

	// End EPPDefRegCreateCmd.getTm()

	/**
	 * Set the trademark information.
	 *
	 * @param aTm DefReg registrant
	 */
	public void setTm(String aTm) {
		tm = aTm;
	}

	// End EPPDefRegCreateCmd.setTm(String)

	/**
	 * Get the trademark country information.
	 *
	 * @return DefReg trademark country
	 */
	public String getTmCountry() {
		return tmcountry;
	}

	// End EPPDefRegCreateCmd.getTmCountry()

	/**
	 * Set the trademark country information.
	 *
	 * @param aTmCountry DefReg    trademark country
	 */
	public void setTmCountry(String aTmCountry) {
		tmcountry = aTmCountry;
	}

	// End EPPDefRegCreateCmd.setTmCountry(String)

	/**
	 * Get the trademark date information.
	 *
	 * @return DefReg trademark date
	 */
	public Date getTmDate() {
		return tmdate;
	}

	// End EPPDefRegCreateCmd.getTmDate()

	/**
	 * Set the trademark date information.
	 *
	 * @param aTmDate DefReg    trademark date
	 */
	public void setTmDate(Date aTmDate) {
		tmdate = aTmDate;
	}

	// End EPPDefRegCreateCmd.setTmDate(String)

	/**
	 * Get the AdminContact information.
	 *
	 * @return DefReg AdminContact
	 */
	public String getAdminContact() {
		return admincontact;
	}

	// End EPPDefRegCreateCmd.getAdminContact()

	/**
	 * Get authorization information
	 *
	 * @return EPPAuthInfo
	 */
	public EPPAuthInfo getAuthInfo() {
		return authInfo;
	}

	// End EPPDefRegCreateCmd.getAuthInfo()

	/**
	 * Gets the registration period in years.
	 *
	 * @return Registration Period in years.
	 */
	public EPPDefRegPeriod getPeriod() {
		return period;
	}

	// End EPPDefRegCreateCmd.getPeriod()

	/**
	 * Set authorization information
	 *
	 * @param newAuthInfo java.lang.String
	 */
	public void setAuthInfo(EPPAuthInfo newAuthInfo) {
		if (newAuthInfo != null) {
			authInfo = newAuthInfo;
			authInfo.setRootName(EPPDefRegMapFactory.NS, EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO);
		}
	}

	// End EPPDefRegCreateCmd.setAuthInfo(EPPAuthInfo)

	/**
	 * Sets the registration period in years.
	 *
	 * @param aPeriod Registration Period in years.
	 */
	public void setPeriod(EPPDefRegPeriod aPeriod) {
		period = aPeriod;
	}

	// End EPPDefRegCreateCmd.setPeriod(EPPDefRegPeriod)

	/**
	 * Compare an instance of <code>EPPDefRegCreateCmd</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPDefRegCreateCmd)) {
			return false;
		}

		if (!super.equals(aObject)) {
			return false;
		}

		EPPDefRegCreateCmd theComp = (EPPDefRegCreateCmd) aObject;

		// Name
		if (
			!(
					(defRegName == null) ? (theComp.defRegName == null)
											 : defRegName.equals(theComp.defRegName)
				)) {
			return false;
		}

		// Registrant
		if (
			!(
					(registrant == null) ? (theComp.registrant == null)
											 : registrant.equals(theComp.registrant)
				)) {
			return false;
		}

		// tm
		if (!((tm == null) ? (theComp.tm == null) : tm.equals(theComp.tm))) {
			return false;
		}

		// tmcountry
		if (
			!(
					(tmcountry == null) ? (theComp.tmcountry == null)
											: tmcountry.equals(theComp.tmcountry)
				)) {
			return false;
		}

		// tmdate
		if (
			!(
					(tmdate == null) ? (theComp.tmdate == null)
										 : tmdate.equals(theComp.tmdate)
				)) {
			return false;
		}

		//admincontact
		if (
			!(
					(admincontact == null) ? (theComp.admincontact == null)
											   : admincontact.equals(theComp.admincontact)
				)) {
			return false;
		}

		// Period
		if (
			!(
					(period == null) ? (theComp.period == null)
										 : period.equals(theComp.period)
				)) {
			return false;
		}

		// AuthInfo
		if (
			!(
					(authInfo == null) ? (theComp.authInfo == null)
										   : authInfo.equals(theComp.authInfo)
				)) {
			return false;
		}

		return true;
	}

	// End EPPDefRegCreateCmd.equals(Object)

	/**
	 * Clone <code>EPPDefRegCreateCmd</code>.
	 *
	 * @return clone of <code>EPPDefRegCreateCmd</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPDefRegCreateCmd clone = (EPPDefRegCreateCmd) super.clone();

		if (authInfo != null) {
			clone.authInfo = (EPPAuthInfo) authInfo.clone();
		}

		if (period != null) {
			clone.period = (EPPDefRegPeriod) period.clone();
		}

		return clone;
	}

	// End EPPDefRegCreateCmd.clone()

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

	// End EPPDefRegCreateCmd.toString()

	/**
	 * Encode a DOM Element tree from the attributes of the EPPDefRegCreateCmd
	 * instance.
	 *
	 * @param aDocument DOM Document that is being built.  Used as an Element
	 * 		  factory.
	 *
	 * @return Root DOM Element representing the EPPDefRegCreateCmd instance.
	 *
	 * @exception EPPEncodeException Unable to encode EPPDefRegCreateCmd
	 * 			  instance.
	 */
	protected Element doEncode(Document aDocument) throws EPPEncodeException {
		try {
			//Validate States
			validateState();
		}
		 catch (EPPCodecException e) {
			throw new EPPEncodeException("EPPDefRegCreateCmd invalid state: "
										 + e);
		}

		Element root =
			aDocument.createElementNS(EPPDefRegMapFactory.NS, ELM_NAME);

		root.setAttribute("xmlns:defReg", EPPDefRegMapFactory.NS);
		root.setAttributeNS(
							EPPCodec.NS_XSI, "xsi:schemaLocation",
							EPPDefRegMapFactory.NS_SCHEMA);

		//Name
		EPPUtil.encodeComp(aDocument, root, defRegName);

		// Registrant
		EPPUtil.encodeString(
							 aDocument, root, registrant, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_REGISTRANT);

		//TradeMark
		EPPUtil.encodeString(
							 aDocument, root, tm, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_TM);

		//TrademMark Country
		EPPUtil.encodeString(
							 aDocument, root, tmcountry, EPPDefRegMapFactory.NS,
							 ELM_DEFREG_TMCOUNTRY);

		//TradeMarkDate
		EPPUtil.encodeDate(
						   aDocument, root, tmdate, EPPDefRegMapFactory.NS,
						   ELM_DEFREG_TMDATE);

		//TrademMark AdminContact
		EPPUtil.encodeString(
							 aDocument, root, admincontact,
							 EPPDefRegMapFactory.NS, ELM_DEFREG_ADMINCONTACT);

		// Period with Attribute of Unit
		if ((period != null) && !period.isPeriodUnspec()) {
			EPPUtil.encodeComp(aDocument, root, period);
		}

		// authInfo
		EPPUtil.encodeComp(aDocument, root, authInfo);

		return root;
	}

	// End EPPDefRegCreateCmd.doEncode(Document)

	/**
	 * Decode the EPPDefRegCreateCmd attributes from the aElement DOM Element
	 * tree.
	 *
	 * @param aElement Root DOM Element to decode EPPDefELM_DEFREGRegCreateCmd
	 * 		  from.
	 *
	 * @exception EPPDecodeException Unable to decode aElement
	 */
	protected void doDecode(Element aElement) throws EPPDecodeException {
		//level
		// DefReg Name
		defRegName =
			(EPPDefRegName) EPPUtil.decodeComp(
											   aElement, EPPDefRegMapFactory.NS,
											   ELM_DEFREG_NAME,
											   EPPDefRegName.class);

		// DefReg Registrant
		registrant =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_REGISTRANT);

		// DefReg TradeMark
		tm     = EPPUtil.decodeString(
									  aElement, EPPDefRegMapFactory.NS,
									  ELM_DEFREG_TM);

		// DefReg TMCountry
		tmcountry =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_TMCOUNTRY);

		// DefReg TMDate
		tmdate =
			EPPUtil.decodeDate(
							   aElement, EPPDefRegMapFactory.NS,
							   ELM_DEFREG_TMDATE);

		// DefReg AdminContact
		admincontact =
			EPPUtil.decodeString(
								 aElement, EPPDefRegMapFactory.NS,
								 ELM_DEFREG_ADMINCONTACT);

		// Period
		period =
			(EPPDefRegPeriod) EPPUtil.decodeComp(
												 aElement,
												 EPPDefRegMapFactory.NS,
												 EPPDefRegPeriod.ELM_NAME,
												 EPPDefRegPeriod.class);

		// authInfo
		authInfo =
			(EPPAuthInfo) EPPUtil.decodeComp(
											 aElement, EPPDefRegMapFactory.NS,
											 EPPDefRegMapFactory.ELM_DEFREG_AUTHINFO,
											 EPPAuthInfo.class);
	}

	// End EPPDefRegCreateCmd.doDecode(Element)
}
