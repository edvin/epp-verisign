/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

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
package com.verisign.epp.codec.registry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents the optional contact object policy information per RFC 5733. The
 * &lt;registry:contact&gt; element contains the following child elements: <br>
 * <ul>
 * <li>
 * &lt;registry:contactIdRegEx&gt; - The OPTIONAL regular expression used to
 * validate the &lt;contact:id&gt; element defined in RFC 5733.</li>
 * <li>
 * &lt;registry:sharePolicy&gt; - The OPTIONAL policy for the sharing of
 * contacts in the server. The possible shared policy values include:
 * <ul>
 * <li>
 * "perZone" - The contacts are shared across all objects of the zone. There is
 * a single pool of contacts defined for the zone.</li>
 * <li>"perSystem" - The contacts are shared across all zones of the system.
 * There is a single pool of contacts across all of the zones supported by the
 * system.</li>
 * </ul>
 * </li>
 * <li>
 * &lt;registry:intSupport&gt; - A boolean value that defines whether the server
 * supports the internationalized form of postal-address information using the
 * type="int" attribute of RFC 5733.</li>
 * <li>
 * &lt;registry:locSupport&gt; - A boolean value that defines whether the server
 * supports the localized form of postal-address information using the
 * type="loc" attribute of RFC 5733.</li>
 * <li>
 * &lt;registry:postalInfo&gt; - The postal-address information policy
 * information.</li>
 * <li>
 * &lt;registry:maxCheckContact&gt; - The maximum number of contact identifiers
 * (&lt;contact:id&gt; elements) that can be included in a contact check command
 * defined in RFC 5733.</li>
 * <li>&lt;registry:authInfoRegex&gt; - The OPTIONAL regular expression used to
 * validate the contact object authorization information value.</li>
 * <li>&lt;registry:clientDisclosureSupported&gt; - The OPTIONAL flag that
 * indicates whether the server supports the client to identify elements that
 * require exception server-operator handling to allow or restrict disclosure to
 * third parties defined in RFC 5733. Default value is {@code false}.</li>
 * <li>
 * &lt;registry:supportedStatus&gt; - The OPTIONAL set of supported contact
 * statuses defined in RFC 5733.</li>
 * <li>
 * &lt;registry:transferHoldPeriod&gt; - The OPTIONAL period of time a contact
 * object is in the pending transfer before the transfer is auto approved by the
 * server. The &lt;registry:transferHoldPeriod&gt; element MUST have the "unit"
 * attribute with the possible values of "y" for year, "m" for month, and "d"
 * for day.</li>
 * <li>
 * &lt;registry:customData&gt; - The OPTIONAL set of custom data using key,
 * value pairs.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryRegex
 * @see com.verisign.epp.codec.registry.EPPRegistryPostal
 * @see com.verisign.epp.codec.registry.EPPRegistrySupportedStatus
 * @see com.verisign.epp.codec.registry.EPPRegistryTransferHoldPeriodType
 * @see com.verisign.epp.codec.registry.EPPRegistryCustomData
 */
public class EPPRegistryContact implements EPPCodecComponent {

	/** Serialization constant */
	private static final long serialVersionUID = -2835245153428894020L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPRegistryContact.class);

	/**
	 * Constant for share policy where contacts are shared across all objects of
	 * the zone. There is a single pool of contacts defined for the zone.
	 */
	public static final String TYPE_PER_ZONE = "perZone";

	/**
	 * Constant for share policy where contacts are shared across all zones of
	 * the system. There is a single pool of contacts across all of the zones
	 * supported by the system.
	 */
	public static final String TYPE_PER_SYSTEM = "perSystem";

	/**
	 * Constant for the status local name
	 */
	public static final String ELM_LOCALNAME = "contact";

	/**
	 * Constant for the contact (prefix and local name)
	 */
	public static final String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * XML local name for the contactIdRegex
	 */
	public final static String ELM_CONTACT_ID_REGEX = "contactIdRegex";

	/**
	 * XML local name for the sharePolicy
	 */
	public static final String ELM_SHARE_POLICY = "sharePolicy";

	/**
	 * XML local name for the intSupport
	 */
	public static final String ELM_INT_SUPPORT = "intSupport";

	/**
	 * XML local name for the locSupport
	 */
	public static final String ELM_LOC_SUPPORT = "locSupport";

	/**
	 * XML local name for the maxCheckContact
	 */
	public static final String ELM_MAX_CHECK = "maxCheckContact";

	/**
	 * XML local name for the authInfoRegex
	 */
	public final static String ELM_AUTH_INFO_REGEX = "authInfoRegex";

	/**
	 * XML local name for the customData
	 */
	public final static String ELM_CUSTOM_DATA = "customData";

	/**
	 * XML local name for the clientDisclosureSupported
	 */
	public final static String ELM_CUSTOM_CLIENT_DISCLOSURE_SUPPORTED = "clientDisclosureSupported";

	/**
	 * Optional contact Id regular expression
	 */
	private EPPRegistryRegex contactIdRegex = null;

	/**
	 * The policy for the sharing of contacts in the server.
	 */
	private String sharePolicy = null;

	/**
	 * Whether the server supports the internationalized form of postal-address
	 * information using the type="int" attribute of RFC 5733.
	 */
	private Boolean intSupport = null;

	/**
	 * Whether the server supports the localized form of postal-address
	 * information using the type="loc" attribute of RFC 5733.
	 */
	private Boolean locSupport = null;

	/** The postal-address information policy information */
	private EPPRegistryPostal postalInfo = null;

	/**
	 * The maximum number of contact identifiers (&lt;contact:id&gt; elements)
	 * that can be included in a contact check command defined in RFC 5733.
	 */
	private Integer maxCheckContact = null;

	/**
	 * The regular expression used to validate the contact object authorization
	 * information value
	 */
	private EPPRegistryRegex authInfoRegex = null;

	/** Set of customer data using key, value pairs */
	private EPPRegistryCustomData customData = null;

	/**
	 * Whether the server supports the client to identify elements that require
	 * exception server-operator handling to allow or restrict disclosure to
	 * third parties defined in RFC 5733.
	 */
	private Boolean clientDisclosureSupported = Boolean.FALSE;

	/** Transfer hold policy attribute */
	private EPPRegistryTransferHoldPeriodType transferHoldPeriod = null;

	/** {@code List} of contact status supported by the server */
	private EPPRegistrySupportedStatus supportedStatus = null;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryContact} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryContact} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryContact} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryContact.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		// Contact Id Regex
		EPPUtil.encodeComp(aDocument, root, this.contactIdRegex);

		// Share Policy
		EPPUtil.encodeString(aDocument, root, this.sharePolicy,
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_SHARE_POLICY);

		// Int Support
		EPPUtil.encodeString(aDocument, root, this.intSupport.toString(),
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_INT_SUPPORT);

		// Loc Support
		EPPUtil.encodeString(aDocument, root, this.locSupport.toString(),
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_LOC_SUPPORT);

		// Postal Info
		EPPUtil.encodeComp(aDocument, root, postalInfo);

		// Max Check Contact
		EPPUtil.encodeString(aDocument, root, this.maxCheckContact.toString(),
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_MAX_CHECK);

		// Auth Info Regex
		if (authInfoRegex != null) {
			EPPUtil.encodeComp(aDocument, root, authInfoRegex);
		}

		// Client Disclosure Supported
		if (clientDisclosureSupported == null) {
			clientDisclosureSupported = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root,
				clientDisclosureSupported.toString(), EPPRegistryMapFactory.NS,
				EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_CUSTOM_CLIENT_DISCLOSURE_SUPPORTED);

		// Supported Status
		if (supportedStatus != null) {
			EPPUtil.encodeComp(aDocument, root, supportedStatus);
		}

		// Transfer Hold Period
		EPPUtil.encodeComp(aDocument, root, transferHoldPeriod);

		// Custom Data
		if (customData != null) {
			EPPUtil.encodeComp(aDocument, root, customData);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryContact} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryContact} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Contact Id Regex
		this.setContactIdRegex((EPPRegistryRegex) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, ELM_CONTACT_ID_REGEX,
				EPPRegistryRegex.class));

		// Share Policy
		this.sharePolicy = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_SHARE_POLICY);

		// Int Support
		intSupport = EPPUtil.decodeBoolean(aElement, EPPRegistryMapFactory.NS,
				ELM_INT_SUPPORT);

		// Loc Support
		locSupport = EPPUtil.decodeBoolean(aElement, EPPRegistryMapFactory.NS,
				ELM_LOC_SUPPORT);

		// Postal Info
		postalInfo = (EPPRegistryPostal) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryPostal.ELM_NAME,
				EPPRegistryPostal.class);

		// Max Check Contact
		maxCheckContact = EPPUtil.decodeInteger(aElement,
				EPPRegistryMapFactory.NS, ELM_MAX_CHECK);

		// Auth Info Regex
		this.setAuthInfoRegex((EPPRegistryRegex) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, ELM_AUTH_INFO_REGEX,
				EPPRegistryRegex.class));

		// Client Disclosure Supported
		clientDisclosureSupported = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS,
				ELM_CUSTOM_CLIENT_DISCLOSURE_SUPPORTED);
		if (clientDisclosureSupported == null) {
			clientDisclosureSupported = Boolean.FALSE;
		}

		// Supported Status
		supportedStatus = (EPPRegistrySupportedStatus) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistrySupportedStatus.ELM_NAME,
				EPPRegistrySupportedStatus.class);

		// Transfer Hold Period
		transferHoldPeriod = (EPPRegistryTransferHoldPeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryTransferHoldPeriodType.ELM_NAME,
						EPPRegistryTransferHoldPeriodType.class);

		// Custom Data
		customData = (EPPRegistryCustomData) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryCustomData.ELM_NAME,
				EPPRegistryCustomData.class);
	}

	/**
	 * Validate the state of the <code>EPPRegistryContact</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {

		if (this.intSupport == null) {
			throw new EPPCodecException(
					"intSupport required element is not set");
		}

		if (this.locSupport == null) {
			throw new EPPCodecException(
					"locSupport required element is not set");
		}

		if (this.postalInfo == null) {
			throw new EPPCodecException(
					"postalInfo required element is not set");
		}

		if (this.maxCheckContact == null
				|| this.maxCheckContact.intValue() <= 0) {
			throw new EPPCodecException(
					"maxCheckContact is required and should be greater than 0");
		}
	}

	/**
	 * Clone <code>EPPRegistryContact</code>.
	 * 
	 * @return clone of <code>EPPRegistryContact</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryContact clone = (EPPRegistryContact) super.clone();

		if (this.contactIdRegex != null) {
			clone.contactIdRegex = (EPPRegistryRegex) contactIdRegex.clone();
		}

		if (authInfoRegex != null) {
			clone.authInfoRegex = (EPPRegistryRegex) authInfoRegex.clone();
		}

		if (supportedStatus != null) {
			clone.supportedStatus = (EPPRegistrySupportedStatus) supportedStatus
					.clone();
		}

		if (transferHoldPeriod != null) {
			clone.transferHoldPeriod = (EPPRegistryTransferHoldPeriodType) this.transferHoldPeriod
					.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryContact</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryContact</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryContact)) {
			return false;
		}

		EPPRegistryContact other = (EPPRegistryContact) aObject;

		// Contact Id Regex
		if (!EqualityUtil.equals(this.contactIdRegex, other.contactIdRegex)) {
			cat.error("EPPRegistryContact.equals(): contactIdRegex not equal");
			return false;
		}

		// Share Policy
		if (!EqualityUtil.equals(this.sharePolicy, other.sharePolicy)) {
			cat.error("EPPRegistryContact.equals(): sharePolicy not equal");
			return false;
		}

		// Int Support
		if (!EqualityUtil.equals(this.intSupport, other.intSupport)) {
			cat.error("EPPRegistryContact.equals(): intSupport not equal");
			return false;
		}

		// Loc Support
		if (!EqualityUtil.equals(this.locSupport, other.locSupport)) {
			cat.error("EPPRegistryContact.equals(): locSupport not equal");
			return false;
		}

		// Postal Info
		if (!EqualityUtil.equals(this.postalInfo, other.postalInfo)) {
			cat.error("EPPRegistryContact.equals(): postalInfo not equal");
			return false;
		}

		// Max Check Contact
		if (!EqualityUtil.equals(this.maxCheckContact, other.maxCheckContact)) {
			cat.error("EPPRegistryContact.equals(): maxCheckContact not equal");
			return false;
		}

		// Auth Info Regex
		if (!EqualityUtil.equals(this.authInfoRegex, other.authInfoRegex)) {
			cat.error("EPPRegistryContact.equals(): authInfoRegex not equal");
			return false;
		}

		// Supported Status
		if (!EqualityUtil.equals(this.supportedStatus, other.supportedStatus)) {
			cat.error("EPPRegistryContact.equals(): supportedStatus not equal");
			return false;
		}

		// Transfer Hold Period
		if (!EqualityUtil.equals(this.transferHoldPeriod,
				other.transferHoldPeriod)) {
			cat.error("EPPRegistryContact.equals(): transferHoldPeriod not equal");
			return false;
		}

		// Custom Data
		if (!EqualityUtil.equals(this.customData, other.customData)) {
			cat.error("EPPRegistryContact.equals(): customData not equal");
			return false;
		}

		return true;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	/**
	 * Get info about regular expression used to validate the contact object
	 * contact Id value.
	 * 
	 * @return instance of {@link EPPRegistryRegex} that specifies regular
	 *         expression used to validate the domain object authorization
	 *         information value
	 */
	public EPPRegistryRegex getContactIdRegex() {
		return this.contactIdRegex;
	}

	/**
	 * Set info about regular expression used to validate the contact object
	 * contact Id value.
	 * 
	 * @param aContactIdRegex
	 *            instance of {@link EPPRegistryRegex} that specifies regular
	 *            expression used to validate the contact object contact Id
	 *            value
	 */
	public void setContactIdRegex(EPPRegistryRegex aContactIdRegex) {
		if (aContactIdRegex != null) {
			aContactIdRegex.setRootName(ELM_CONTACT_ID_REGEX);
		}
		this.contactIdRegex = aContactIdRegex;
	}

	/**
	 * Gets the policy for the sharing of contacts in the server.
	 * 
	 * @return {@link #TYPE_PER_ZONE} to share contacts per zone,
	 *         {@link #TYPE_PER_SYSTEM} to share contact for all zones of the
	 *         system if defined; <code>null</code> otherwise.
	 */
	public String getSharePolicy() {
		return this.sharePolicy;
	}

	/**
	 * Sets the policy for the sharing of contacts in the server.
	 * 
	 * @param aSharePolicy
	 *            {@link #TYPE_PER_ZONE} to share contacts per zone or
	 *            {@link #TYPE_PER_SYSTEM} to share contact for all zones of the
	 *            system
	 */
	public void setSharePolicy(String aSharePolicy) {
		this.sharePolicy = aSharePolicy;
	}

	/**
	 * Gets flag for internationalization support
	 * 
	 * @return {@code true} if internationalized form of postal-address is
	 *         supported. {@code false} otherwise
	 */
	public Boolean getIntSupport() {
		return intSupport;
	}

	/**
	 * Sets flag for internationalization support
	 * 
	 * @param intSupport
	 *            {@code true} if internationalized form of postal-address is
	 *            supported. {@code false} otherwise
	 */
	public void setIntSupport(Boolean intSupport) {
		this.intSupport = intSupport;
	}

	/**
	 * Gets flag for localization support
	 * 
	 * @return {@code true} if localized form of postal-address is supported.
	 *         {@code false} otherwise
	 */
	public Boolean getLocSupport() {
		return locSupport;
	}

	/**
	 * Sets flag for localization support
	 * 
	 * @param locSupport
	 *            {@code true} if localized form of postal-address is supported.
	 *            {@code false} otherwise
	 */
	public void setLocSupport(Boolean locSupport) {
		this.locSupport = locSupport;
	}

	/**
	 * Gets maximum number of contacts allowed in the check command.
	 * 
	 * @return maximum number of contact identifiers (&lt;contact:id&gt;
	 *         elements) that can be included in a contact check command defined
	 *         in RFC 5733
	 */
	public Integer getMaxCheckContact() {
		return maxCheckContact;
	}

	/**
	 * Gets maximum number of contacts allowed in the check command.
	 * 
	 * parm maxCheckContact maximum number of contact identifiers
	 * (&lt;contact:id&gt; elements) that can be included in a contact check
	 * command defined in RFC 5733
	 */
	public void setMaxCheckContact(Integer maxCheckContact) {
		this.maxCheckContact = maxCheckContact;
	}

	/**
	 * Gets authInfo regular expression.
	 * 
	 * @return regular expression used to validate the contact object
	 *         authorization information value
	 */
	public EPPRegistryRegex getAuthInfoRegex() {
		return authInfoRegex;
	}

	/**
	 * Gets authInfo regular expression.
	 * 
	 * @param authInfoRegex
	 *            regular expression used to validate the contact object
	 *            authorization information value
	 */
	public void setAuthInfoRegex(EPPRegistryRegex authInfoRegex) {
		if (authInfoRegex != null) {
			authInfoRegex.setRootName(ELM_AUTH_INFO_REGEX);
		}

		this.authInfoRegex = authInfoRegex;
	}

	/**
	 * Gets set of custom data using key, value pairs.
	 * 
	 * @return instance of {@link EPPRegistryCustomData} that gives users the
	 *         ability to specify custom data with key/value pairs
	 */
	public EPPRegistryCustomData getCustomData() {
		return customData;
	}

	/**
	 * Set set of custom data using key, value pairs.
	 * 
	 * @param customData
	 *            instance of {@link EPPRegistryCustomData} that gives users the
	 *            ability to specify custom data with key/value pairs
	 */
	public void setCustomData(EPPRegistryCustomData customData) {
		this.customData = customData;
	}

	/**
	 * Gets postal-address information policy information.
	 * 
	 * @return the postal-address information policy information
	 */
	public EPPRegistryPostal getPostalInfo() {
		return postalInfo;
	}

	/**
	 * Sets postal-address information policy information.
	 * 
	 * @param postalInfo
	 *            the postal-address information policy information
	 */
	public void setPostalInfo(EPPRegistryPostal postalInfo) {
		this.postalInfo = postalInfo;
	}

	/**
	 * Gets client disclosure flag.
	 * 
	 * @return {@code true} if the server supports the client to identify
	 *         elements that require exception server-operator handling to allow
	 *         or restrict disclosure to third parties defined in RFC 5733.
	 *         {@code false} otherwise
	 */
	public Boolean getClientDisclosureSupported() {
		return clientDisclosureSupported;
	}

	/**
	 * Sets client disclosure flag.
	 * 
	 * @param clientDisclosureSupported
	 *            {@code true} if the server supports the client to identify
	 *            elements that require exception server-operator handling to
	 *            allow or restrict disclosure to third parties defined in RFC
	 *            5733. {@code false} otherwise
	 */
	public void setClientDisclosureSupported(Boolean clientDisclosureSupported) {
		this.clientDisclosureSupported = clientDisclosureSupported;
	}

	/**
	 * Gets a set of supported host statuses defined in RFC 5733.
	 * 
	 * @return set of supported host statuses defined in RFC 5733
	 */
	public EPPRegistrySupportedStatus getSupportedStatus() {
		return supportedStatus;
	}

	/**
	 * Sets a set of supported host statuses defined in RFC 5733.
	 * 
	 * @param supportedStatus
	 *            set of supported host statuses defined in RFC 5733
	 */
	public void setSupportedStatus(EPPRegistrySupportedStatus supportedStatus) {
		this.supportedStatus = supportedStatus;
	}

	/**
	 * Get the period of time a contact object is in the pending transfer before
	 * the transfer is auto approved by the server
	 * 
	 * @return instance of {@link EPPRegistryTransferHoldPeriodType}
	 */
	public EPPRegistryTransferHoldPeriodType getTransferHoldPeriod() {
		return transferHoldPeriod;
	}

	/**
	 * Set the period of time a contact object is in the pending transfer before
	 * the transfer is auto approved by the server
	 * 
	 * @param transferHoldPeriod
	 *            instance of {@link EPPRegistryTransferHoldPeriodType}
	 */
	public void setTransferHoldPeriod(
			EPPRegistryTransferHoldPeriodType transferHoldPeriod) {
		this.transferHoldPeriod = transferHoldPeriod;
	}

}
