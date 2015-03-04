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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents the domain name object policy information per RFC 5731. Instance
 * of this class is encoded into the &lt;registry:domain&gt; element in the
 * &lt;registry:zone&gt; element when the server responds with the detailed
 * information of the zone object. The &lt;registry:domain&gt; must contain the
 * following child elements:<br>
 * <br>
 * 
 * <ul>
 * <li>
 * &lt;registry:domainName&gt; - The domain name object policy information per
 * RFC 5731. Use {@link #getDomainNames()} and {@link #setDomainNames(List)} to
 * get and set the element.</li>
 * 
 * <li>
 * &lt;registry:idn&gt; - OPTIONAL Internationalized Domain Name (IDN) policy
 * information. Use {@link #getIdn()} and {@link #setIdn(EPPRegistryIDN)} to get
 * and set the element.</li>
 * 
 * <li>
 * &lt;registry:premiumSupport&gt; - OPTIONAL boolean value that indicates
 * whether the server supports premium domain names. Default value is
 * {@code false}. Use {@link #getPremiumSupport()} and
 * {@link #setPremiumSupport(Boolean)} to get and set the element.</li>
 * 
 * <li>
 * &lt;registry:contactsSupported&gt; - OPTIONAL boolean value that indicates
 * whether contacts are supported. Default value is {@code true}. Use
 * {@link #getContactsSupported()} and {@link #setContactsSupported(Boolean)} to
 * get and set the element.</li>
 * 
 * <li>
 * &lt;registry:contact&gt; - Zero to three elements that define the minimum and
 * maximum numbers of contacts by contact type. Valid contact types are: admin,
 * tech and billing. Use {@link #getContacts()} and {@link #setContacts(List)}
 * to get and set the element. Use {@link #addContact(EPPRegistryDomainContact)}
 * to append a contact to the existing contact list.</li>
 * 
 * <li>
 * &lt;registry:ns&gt; - Defines the minimum and maximum number of delegated
 * host objects (name servers) that can be associated with a domain object. Use
 * {@link #getNameServerLimit()} and
 * {@link #setNameServerLimit(EPPRegistryDomainNSLimit)} to get and set the
 * element.</li>
 * 
 * <li>
 * &lt;registry:childHost&gt; - Defines the minimum and maximum number of
 * subordinate host objects (child hosts) for a domain object. Use
 * {@link #getChildHostLimit()} and
 * {@link #setChildHostLimit(EPPRegistryDomainHostLimit)} to get and set the
 * element.</li>
 * 
 * <li>
 * &lt;registry:period&gt; - Zero or more elements that defines the supported
 * min/max registration periods and default periods by command type. The
 * required "command" attribute defines the command type with sample values of
 * "create", "renew", and "transfer". Use {@link #getPeriods()} and
 * {@link #setPeriods(List)} to get and set the element.</li>
 * 
 * <li>
 * &lt;registry:transferHoldPeriod&gt; - The period of time a domain object is
 * in the pending transfer before the transfer is auto approved by the server.
 * This element MUST have the "unit" attribute with the possible values of "y"
 * for year, "m" for month, and "d" for day. Use
 * {@link #getTransferHoldPeriod()} and
 * {@link #setTransferHoldPeriod(EPPRegistryTransferHoldPeriodType)} to get and
 * set the element.</li>
 * 
 * <li>
 * &lt;registry:gracePeriod&gt; - Zero or more elements that defines the grace
 * periods by operation type. The required "command" attribute defines the
 * operation type with the sample values of "create", "renew", "transfer", and
 * "autoRenew". This element requires the "unit" attribute with the possible
 * values of "d" for day, "h" for hour, and "m" for minute. Use
 * {@link #getGracePeriods()} and {@link #setGracePeriods(List)} to get and set
 * the element.</li>
 * 
 * <li>
 * &lt;registry:rgp&gt; - OPTIONAL Registry Grace Period (RGP) status periods.
 * Use {@link #getRgp()} and {@link #setRgp(EPPRegistryRGP)} to get and set the
 * element.</li>
 * 
 * <li>
 * &lt;registry:dnssec&gt; - OPTIONAL DNS Security Extensions (DNSSEC) policies
 * for the server. Use {@link #getDnssec()} and
 * {@link #setDnssec(EPPRegistryDNSSEC)} to get and set the element.</li>
 * 
 * <li>
 * &lt;registry:maxCheckDomain&gt; - The maximum number of domain names
 * (&lt;domain:name&gt; elements) that can be included in a domain check command
 * defined in RFC 5731 Use {@link #setMaxCheckDomain(Integer)} and
 * {@link #getMaxCheckDomain()} to get and set the element.</li>
 * 
 * <li>
 * &lt;registry:supportedStatus&gt; - The OPTIONAL set of supported domain
 * status defined in RFC 5731 Use {@link #getSupportedStatus()} and
 * {@link #setSupportedStatus(EPPRegistrySupportedStatus)} to get and set the
 * element.</li>
 * 
 * <li>
 * &lt;registry:authInfoRegEx&gt; - The OPTIONAL regular expression used to
 * validate the domain object authorization information value. Use
 * {@link #getAuthInfoRegex()} and {@link #setAuthInfoRegex(EPPRegistryRegex)}
 * to get and set the element.</li>
 * </ul>
 * 
 * <li>
 * &lt;registry:customDate&gt; - The OPTIONAL set of custom data using key,
 * value pairs. Use {@link #getCustomData()} and
 * {@link #setCustomData(EPPRegistryCustomData)} to get and set the element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryZoneInfo
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainName
 * @see com.verisign.epp.codec.registry.EPPRegistryIDN
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainContact
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainPeriod
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainNSLimit
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainHostLimit
 * @see com.verisign.epp.codec.registry.EPPRegistryDomainPeriod
 * @see com.verisign.epp.codec.registry.EPPRegistryTransferHoldPeriodType
 * @see com.verisign.epp.codec.registry.EPPRegistryGracePeriod
 * @see com.verisign.epp.codec.registry.EPPRegistryRGP
 * @see com.verisign.epp.codec.registry.EPPRegistryDNSSEC
 * @see com.verisign.epp.codec.registry.EPPRegistrySupportedStatus
 * @see com.verisign.epp.codec.registry.EPPRegistryCustomData
 */
public class EPPRegistryDomain implements EPPCodecComponent {
	private static final long serialVersionUID = 8674568245554756710L;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryDomain.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** XML Element Name of <code>EPPRegistryDomain</code> root element. */
	public final static String ELM_NAME = "registry:domain";

	/** XML Element Name of <code>premiumSupport</code> attribute. */
	public final static String ELM_PREMIUM_SUPPORT = "registry:premiumSupport";

	/** XML Element Name of <code>contactsSupported</code> attribute. */
	public final static String ELM_REGISTRANT = "registry:contactsSupported";

	/* TODO: remove ELM_URGENT */
	/** @deprecated XML Element Name of <code>urgent</code> attribute. */
	@Deprecated
	public final static String ELM_URGENT = "registry:urgent";

	/** XML Element Name of <code>maxCheckDomain</code> attribute. */
	public final static String ELM_MAX_CHECK_DOMAIN = "registry:maxCheckDomain";

	/** XML Element Name of <code>authInfoRegex</code> attribute. */
	public final static String ELM_AUTH_INFO_REGEX = "authInfoRegex";

	/**
	 * {@code List} of {@code EPPRegistryDomainName} that specifies the domain
	 * name object policy
	 */
	private List domainNames = new ArrayList();

	/** Internationalized Domain Name (IDN) policy information. */
	private EPPRegistryIDN idn = null;

	/** indicates whether the server supports premium domain names. */
	private Boolean premiumSupport = Boolean.FALSE;

	/** indicates whether contacts are supported */
	private Boolean contactsSupported = Boolean.TRUE;

	// EPPRegistryDomainContact
	/** {@code List} of domain contact policy */
	private List contacts = new ArrayList();

	/**
	 * defines min and max number of delegated host objects that can be
	 * associated with a domain object
	 */
	private EPPRegistryDomainNSLimit nameServerLimit = null;

	/**
	 * Defines the minimum and maximum number of subordinate host objects (child
	 * hosts) for a domain object.
	 */
	private EPPRegistryDomainHostLimit childHostLimit = null;

	// EPPRegistryDomainPeriod
	/**
	 * {@code List} of {@link EPPRegistryDomainPeriod} instances that define the
	 * supported min/max/default registration periods by command type. Command
	 * type must be one of "create, "renew" and "transfer".
	 */
	private List periods = new ArrayList();

	/** Transfer hold policy attribute */
	private EPPRegistryTransferHoldPeriodType transferHoldPeriod = null;

	/** {@code List} of {@link EPPRegistryGracePeriod} */
	private List gracePeriods = new ArrayList();

	/** Attribute for Registry Grace Period (RGP) status */
	private EPPRegistryRGP rgp = null;

	/** DNS Security Extensions attribute */
	private EPPRegistryDNSSEC dnssec = null;

	/**
	 * Attribute that defines the maximum number of domain names
	 * (&lt;domain:name&gt; elements) that can be included in a domain check
	 * command defined in RFC 5731.
	 */
	private Integer maxCheckDomain = null;

	/** List of domain status supported by the server */
	private EPPRegistrySupportedStatus supportedStatus = null;

	/**
	 * Attribute about regular expression used to validate the domain object
	 * authorization information value
	 */
	private EPPRegistryRegex authInfoRegex = null;

	/** Set of customer data using key, value pairs */
	private EPPRegistryCustomData customData = null;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDomain} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryDomain} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDomain} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryDomain.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		EPPUtil.encodeCompList(aDocument, root, domainNames);
		if (idn != null) {
			EPPUtil.encodeComp(aDocument, root, idn);
		}
		if (premiumSupport == null) {
			premiumSupport = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, premiumSupport.toString(),
				EPPRegistryMapFactory.NS, ELM_PREMIUM_SUPPORT);
		if (contactsSupported == null) {
			contactsSupported = Boolean.TRUE;
		}
		EPPUtil.encodeString(aDocument, root, contactsSupported.toString(),
				EPPRegistryMapFactory.NS, ELM_REGISTRANT);
		if (contacts != null && contacts.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, contacts);
		}
		EPPUtil.encodeComp(aDocument, root, nameServerLimit);
		EPPUtil.encodeComp(aDocument, root, childHostLimit);
		if (periods != null && periods.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, periods);
		}
		EPPUtil.encodeComp(aDocument, root, transferHoldPeriod);
		if (gracePeriods != null && gracePeriods.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, gracePeriods);
		}
		if (rgp != null) {
			EPPUtil.encodeComp(aDocument, root, rgp);
		}
		if (dnssec != null) {
			EPPUtil.encodeComp(aDocument, root, dnssec);
		}
		EPPUtil.encodeString(aDocument, root, maxCheckDomain.toString(),
				EPPRegistryMapFactory.NS, ELM_MAX_CHECK_DOMAIN);
		if (supportedStatus != null) {
			EPPUtil.encodeComp(aDocument, root, supportedStatus);
		}
		if (authInfoRegex != null) {
			EPPUtil.encodeComp(aDocument, root, authInfoRegex);
		}
		if (customData != null) {
			EPPUtil.encodeComp(aDocument, root, customData);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDomain} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDomain} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		domainNames = EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryDomainName.ELM_NAME,
				EPPRegistryDomainName.class);
		idn = (EPPRegistryIDN) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryIDN.ELM_NAME,
				EPPRegistryIDN.class);
		premiumSupport = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_PREMIUM_SUPPORT);
		if (premiumSupport == null) {
			premiumSupport = Boolean.FALSE;
		}
		contactsSupported = EPPUtil.decodeBoolean(aElement,
				EPPRegistryMapFactory.NS, ELM_REGISTRANT);
		if (contactsSupported == null) {
			contactsSupported = Boolean.TRUE;
		}
		contacts = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistryDomainContact.ELM_NAME,
				EPPRegistryDomainContact.class);
		nameServerLimit = (EPPRegistryDomainNSLimit) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistryDomainNSLimit.ELM_NAME,
				EPPRegistryDomainNSLimit.class);
		childHostLimit = (EPPRegistryDomainHostLimit) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistryDomainHostLimit.ELM_NAME,
				EPPRegistryDomainHostLimit.class);
		periods = EPPUtil
				.decodeCompList(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryDomainPeriod.ELM_NAME,
						EPPRegistryDomainPeriod.class);
		transferHoldPeriod = (EPPRegistryTransferHoldPeriodType) EPPUtil
				.decodeComp(aElement, EPPRegistryMapFactory.NS,
						EPPRegistryTransferHoldPeriodType.ELM_NAME,
						EPPRegistryTransferHoldPeriodType.class);
		gracePeriods = EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryGracePeriod.ELM_NAME,
				EPPRegistryGracePeriod.class);
		rgp = (EPPRegistryRGP) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryRGP.ELM_NAME,
				EPPRegistryRGP.class);
		dnssec = (EPPRegistryDNSSEC) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryDNSSEC.ELM_NAME,
				EPPRegistryDNSSEC.class);
		supportedStatus = (EPPRegistrySupportedStatus) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistrySupportedStatus.ELM_NAME,
				EPPRegistrySupportedStatus.class);
		maxCheckDomain = EPPUtil.decodeInteger(aElement,
				EPPRegistryMapFactory.NS, ELM_MAX_CHECK_DOMAIN);
		customData = (EPPRegistryCustomData) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryCustomData.ELM_NAME,
				EPPRegistryCustomData.class);
		this.setAuthInfoRegex((EPPRegistryRegex) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, ELM_AUTH_INFO_REGEX,
				EPPRegistryRegex.class));
	}

	/**
	 * implements a deep <code>EPPRegistryDomain</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDomain</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryDomain)) {
			return false;
		}

		EPPRegistryDomain theComp = (EPPRegistryDomain) aObject;
		if (!((domainNames == null) ? (theComp.domainNames == null) : EPPUtil
				.equalLists(domainNames, theComp.domainNames))) {
			cat.error("EPPRegistryDomain.equals(): domainNames not equal");
			return false;
		}
		if (!((premiumSupport == null) ? (theComp.premiumSupport == null)
				: premiumSupport.equals(theComp.premiumSupport))) {
			cat.error("EPPRegistryDomain.equals(): premiumSupport not equal");
			return false;
		}
		if (!((idn == null) ? (theComp.idn == null) : idn.equals(theComp.idn))) {
			cat.error("EPPRegistryDomain.equals(): idn not equal");
			return false;
		}
		if (!((contactsSupported == null) ? (theComp.contactsSupported == null)
				: contactsSupported.equals(theComp.contactsSupported))) {
			cat.error("EPPRegistryDomain.equals(): contactsSupported not equal");
			return false;
		}
		if (!((contacts == null) ? (theComp.contacts == null) : EPPUtil
				.equalLists(contacts, theComp.contacts))) {
			cat.error("EPPRegistryDomain.equals(): contacts not equal");
			return false;
		}
		if (!((nameServerLimit == null) ? (theComp.nameServerLimit == null)
				: nameServerLimit.equals(theComp.nameServerLimit))) {
			cat.error("EPPRegistryDomain.equals(): nameServerLimit not equal");
			return false;
		}
		if (!((childHostLimit == null) ? (theComp.childHostLimit == null)
				: childHostLimit.equals(theComp.childHostLimit))) {
			cat.error("EPPRegistryDomain.equals(): childHostLimit not equal");
			return false;
		}
		if (!((periods == null) ? (theComp.periods == null) : EPPUtil
				.equalLists(periods, theComp.periods))) {
			cat.error("EPPRegistryDomain.equals(): periods not equal");
			return false;
		}
		if (!((transferHoldPeriod == null) ? (theComp.transferHoldPeriod == null)
				: transferHoldPeriod.equals(theComp.transferHoldPeriod))) {
			cat.error("EPPRegistryDomain.equals(): transferHoldPeriod not equal");
			return false;
		}
		if (!((gracePeriods == null) ? (theComp.gracePeriods == null) : EPPUtil
				.equalLists(gracePeriods, theComp.gracePeriods))) {
			cat.error("EPPRegistryDomain.equals(): gracePeriods not equal");
			return false;
		}
		if (!((rgp == null) ? (theComp.rgp == null) : rgp.equals(theComp.rgp))) {
			cat.error("EPPRegistryDomain.equals(): rgp not equal");
			return false;
		}
		if (!((dnssec == null) ? (theComp.dnssec == null) : dnssec
				.equals(theComp.dnssec))) {
			cat.error("EPPRegistryDomain.equals(): dnssec not equal");
			return false;
		}
		if (!((maxCheckDomain == null) ? (theComp.maxCheckDomain == null)
				: maxCheckDomain.equals(theComp.maxCheckDomain))) {
			cat.error("EPPRegistryDomain.equals(): maxCheckDomain not equal");
			return false;
		}
		if (!((supportedStatus == null) ? (theComp.supportedStatus == null)
				: supportedStatus.equals(theComp.supportedStatus))) {
			cat.error("EPPRegistryDomain.equals(): supportedStatus not equal");
			return false;
		}
		if (!((authInfoRegex == null) ? (theComp.authInfoRegex == null)
				: authInfoRegex.equals(theComp.authInfoRegex))) {
			cat.error("EPPRegistryDomain.equals(): authInfoRegex not equal");
			return false;
		}
		if (!((customData == null) ? (theComp.customData == null) : customData
				.equals(theComp.customData))) {
			cat.error("EPPRegistryDomain.equals(): customData not equal");
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPRegistryDomain</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (domainNames == null || domainNames.size() == 0) {
			throw new EPPCodecException("domainNames element is not set");
		}
		if (contacts != null && contacts.size() > 3) {
			throw new EPPCodecException(
					"number of contact element cannot exceed 3");
		}
		if (nameServerLimit == null) {
			throw new EPPCodecException("ns element is not set");
		}
		if (childHostLimit == null) {
			throw new EPPCodecException("childHost element is not set");
		}
		if (transferHoldPeriod == null) {
			throw new EPPCodecException("transferHoldPeriod element is not set");
		}
		if (maxCheckDomain == null || maxCheckDomain.intValue() <= 0) {
			throw new EPPCodecException(
					"maxCheckDomain is required and should be greater than 0");
		}
	}

	/**
	 * Clone <code>EPPRegistryDomain</code>.
	 * 
	 * @return clone of <code>EPPRegistryDomain</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDomain clone = (EPPRegistryDomain) super.clone();
		if (domainNames != null) {
			clone.domainNames = (List) ((ArrayList) domainNames).clone();
		}
		if (idn != null) {
			clone.idn = (EPPRegistryIDN) idn.clone();
		}
		if (contacts != null) {
			clone.contacts = (List) ((ArrayList) contacts).clone();
		}
		if (nameServerLimit != null) {
			clone.nameServerLimit = (EPPRegistryDomainNSLimit) nameServerLimit
					.clone();
		}
		if (childHostLimit != null) {
			clone.childHostLimit = (EPPRegistryDomainHostLimit) childHostLimit
					.clone();
		}
		if (periods != null) {
			clone.periods = (List) ((ArrayList) periods).clone();
		}
		if (transferHoldPeriod != null) {
			clone.transferHoldPeriod = (EPPRegistryTransferHoldPeriodType) this.transferHoldPeriod
					.clone();
		}
		if (gracePeriods != null) {
			clone.gracePeriods = (List) ((ArrayList) gracePeriods).clone();
		}
		if (rgp != null) {
			clone.rgp = (EPPRegistryRGP) rgp.clone();
		}
		if (dnssec != null) {
			clone.dnssec = (EPPRegistryDNSSEC) dnssec.clone();
		}
		if (supportedStatus != null) {
			clone.supportedStatus = (EPPRegistrySupportedStatus) supportedStatus
					.clone();
		}
		if (authInfoRegex != null) {
			clone.authInfoRegex = (EPPRegistryRegex) authInfoRegex.clone();
		}
		if (customData != null) {
			clone.customData = (EPPRegistryCustomData) customData.clone();
		}

		return clone;
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
	 * Add a domain name object policy to the list of domain name policies.
	 * 
	 * @param aDomainName Domain name policy to add
	 */
	public void addDomainName(EPPRegistryDomainName aDomainName) {
		if (this.domainNames == null) {
			this.domainNames = new ArrayList();
		}
		
		this.domainNames.add(aDomainName);
	}
	
	/**
	 * Get the {@code List} of {@code EPPRegistryDomainName} that specifies the
	 * domain name object policy.
	 * 
	 * @return the {@code List} of {@code EPPRegistryDomainName} that specifies
	 *         the domain name object policy
	 */
	public List getDomainNames() {
		return domainNames;
	}

	/**
	 * Set the {@code List} of {@code EPPRegistryDomainName} that specifies the
	 * domain name object policy.
	 * 
	 * @param domainNames
	 *            the {@code List} of {@code EPPRegistryDomainName} that
	 *            specifies the domain name object policy
	 */
	public void setDomainNames(List domainNames) {
		this.domainNames = domainNames;
	}

	/**
	 * Get the Internationalized Domain Name (IDN) policy information.
	 * 
	 * @return Internationalized Domain Name (IDN) policy information
	 */
	public EPPRegistryIDN getIdn() {
		return idn;
	}

	/**
	 * Set the Internationalized Domain Name (IDN) policy information.
	 * 
	 * @param idn
	 *            the Internationalized Domain Name (IDN) policy information.
	 */
	public void setIdn(EPPRegistryIDN idn) {
		this.idn = idn;
	}

	/**
	 * Get premium support flag.
	 * 
	 * @return flag that indicates whether the server supports premium domain
	 *         names
	 */
	public Boolean getPremiumSupport() {
		return premiumSupport;
	}

	/**
	 * Set premium support flag.
	 * 
	 * @param premiumSupport
	 *            flag that indicates whether the server supports premium domain
	 *            names
	 */
	public void setPremiumSupport(Boolean premiumSupport) {
		this.premiumSupport = premiumSupport;
	}

	/**
	 * Get the contact supported flag.
	 * 
	 * @return flag that indicates whether contacts are supported
	 */
	public Boolean getContactsSupported() {
		return contactsSupported;
	}

	/**
	 * Set the contact supported flag.
	 * 
	 * @param contactsSupported
	 *            flag that indicates whether contacts are supported
	 */
	public void setContactsSupported(Boolean contactsSupported) {
		this.contactsSupported = contactsSupported;
	}

	/**
	 * Get domain contact policy.
	 * 
	 * @return {@code List} of domain contact policy
	 */
	public List getContacts() {
		return contacts;
	}

	/**
	 * Set domain contact policy.
	 * 
	 * @param contacts
	 *            {@code List} of domain contact policy
	 */
	public void setContacts(List contacts) {
		this.contacts = contacts;
	}

	/**
	 * Append a domain contact policy to the existing list.
	 * 
	 * @param contact
	 *            domain contact policy for one of the "admin", "tech", or
	 *            "billing" contact.
	 */
	public void addContact(EPPRegistryDomainContact contact) {
		if (this.contacts == null) {
			this.contacts = new ArrayList();
		}
		this.contacts.add(contact);
	}

	/**
	 * Get NS limit definition.
	 * 
	 * @return instance of {@code EPPRegistryDomainNSLimit} that defines min/max
	 *         number of delegated host objects (name servers) that can be
	 *         associated with a domain object
	 */
	public EPPRegistryDomainNSLimit getNameServerLimit() {
		return nameServerLimit;
	}

	/**
	 * Set NS limit definition.
	 * 
	 * @param nameServerLimit
	 *            instance of {@code EPPRegistryDomainNSLimit} that defines
	 *            min/max number of delegated host objects (name servers) that
	 *            can be associated with a domain object
	 */
	public void setNameServerLimit(EPPRegistryDomainNSLimit nameServerLimit) {
		this.nameServerLimit = nameServerLimit;
	}

	/**
	 * Get child host limit.
	 * 
	 * @return Instance of {@code EPPRegistryDomainHostLimit} that defines the
	 *         minimum and maximum number of subordinate host objects (child
	 *         hosts) for a domain object.
	 */
	public EPPRegistryDomainHostLimit getChildHostLimit() {
		return childHostLimit;
	}

	/**
	 * Set child host limit.
	 * 
	 * @param childHostLimit
	 *            Instance of {@code EPPRegistryDomainHostLimit} that defines
	 *            the minimum and maximum number of subordinate host objects
	 *            (child hosts) for a domain object.
	 */
	public void setChildHostLimit(EPPRegistryDomainHostLimit childHostLimit) {
		this.childHostLimit = childHostLimit;
	}

	/**
	 * Get {@code List} of {@link EPPRegistryDomainPeriod} instances that define
	 * the supported min/max/default registration periods by command type.
	 * Command type must be one of "create, "renew" and "transfer".
	 * 
	 * @return {@code List} of {@link EPPRegistryDomainPeriod} instances
	 */
	public List getPeriods() {
		return periods;
	}

	/**
	 * Set {@code List} of {@link EPPRegistryDomainPeriod} instances that define
	 * the supported min/max/default registration periods by command type.
	 * Command type must be one of "create, "renew" and "transfer".
	 * 
	 * @param periods
	 *            {@code List} of {@link EPPRegistryDomainPeriod} instances
	 */
	public void setPeriods(List periods) {
		this.periods = periods;
	}

	public void addPeriod(EPPRegistryDomainPeriod period) {
		if (this.periods == null) {
			this.periods = new ArrayList();
		}
		this.periods.add(period);
	}

	/**
	 * Get the period of time a domain object is in the pending transfer before
	 * the transfer is auto approved by the server
	 * 
	 * @return instance of {@link EPPRegistryTransferHoldPeriodType}
	 */
	public EPPRegistryTransferHoldPeriodType getTransferHoldPeriod() {
		return transferHoldPeriod;
	}

	/**
	 * Set the period of time a domain object is in the pending transfer before
	 * the transfer is auto approved by the server
	 * 
	 * @param transferHoldPeriod
	 *            instance of {@link EPPRegistryTransferHoldPeriodType}
	 */
	public void setTransferHoldPeriod(
			EPPRegistryTransferHoldPeriodType transferHoldPeriod) {
		this.transferHoldPeriod = transferHoldPeriod;
	}

	/**
	 * Get the {@code List} of attributes that defines the grace periods by
	 * operation type.
	 * 
	 * @return {@code List} of {@link EPPRegistryGracePeriod}
	 */
	public List getGracePeriods() {
		return gracePeriods;
	}

	/**
	 * Set the {@code List} of attributes that defines the grace periods by
	 * operation type.
	 * 
	 * @param gracePeriods
	 *            {@code List} of {@link EPPRegistryGracePeriod}
	 */
	public void setGracePeriods(List gracePeriods) {
		this.gracePeriods = gracePeriods;
	}

	/**
	 * Append one instance of {@link EPPRegistryGracePeriod} to the existing
	 * {@code List}.
	 * 
	 * @param gracePeriod
	 *            instance of {@link EPPRegistryGracePeriod}
	 */
	public void addGracePeriod(EPPRegistryGracePeriod gracePeriod) {
		if (gracePeriod == null) {
			return;
		}
		if (this.gracePeriods == null) {
			this.gracePeriods = new ArrayList();
		}
		this.gracePeriods.add(gracePeriod);
	}

	/**
	 * Get the information about Registry Grace Period (RGP).
	 * 
	 * @return instance of {@link EPPRegistryRGP}.
	 */
	public EPPRegistryRGP getRgp() {
		return rgp;
	}

	/**
	 * Set the information about Registry Grace Period (RGP).
	 * 
	 * @param rgp
	 *            instance of {@link EPPRegistryRGP}.
	 */
	public void setRgp(EPPRegistryRGP rgp) {
		this.rgp = rgp;
	}

	/**
	 * Get the DNS Security Extensions (DNSSEC) policies.
	 * 
	 * @return instance of {@link EPPRegistryDNSSEC} that defines the DNS
	 *         Security Extensions (DNSSEC) policies.
	 */
	public EPPRegistryDNSSEC getDnssec() {
		return dnssec;
	}

	/**
	 * Set the DNS Security Extensions (DNSSEC) policies.
	 * 
	 * @param dnssec
	 *            instance of {@link EPPRegistryDNSSEC} that defines the DNS
	 *            Security Extensions (DNSSEC) policies.
	 */
	public void setDnssec(EPPRegistryDNSSEC dnssec) {
		this.dnssec = dnssec;
	}

	/**
	 * Get the attribute that defines the maximum number of domain names
	 * (&lt;domain:name&gt; elements) that can be included in a domain check
	 * command defined in RFC 5731.
	 * 
	 * @return maximum number of domain names (&lt;domain:name&gt; elements)
	 *         that can be included in a domain check command defined in RFC
	 *         5731.
	 */
	public Integer getMaxCheckDomain() {
		return maxCheckDomain;
	}

	/**
	 * Set the attribute that defines the maximum number of domain names
	 * (&lt;domain:name&gt; elements) that can be included in a domain check
	 * command defined in RFC 5731.
	 * 
	 * @param maxCheckDomain
	 *            maximum number of domain names (&lt;domain:name&gt; elements)
	 *            that can be included in a domain check command defined in RFC
	 *            5731.
	 */
	public void setMaxCheckDomain(Integer maxCheckDomain) {
		this.maxCheckDomain = maxCheckDomain;
	}

	/**
	 * Get set of custom data using key, value pairs.
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
	 * Get info about regular expression used to validate the domain object
	 * authorization information value.
	 * 
	 * @return instance of {@link EPPRegistryRegex} that specifies regular
	 *         expression used to validate the domain object authorization
	 *         information value
	 */
	public EPPRegistryRegex getAuthInfoRegex() {
		return authInfoRegex;
	}

	/**
	 * Set info about regular expression used to validate the domain object
	 * authorization information value.
	 * 
	 * @param authInfoRegex
	 *            instance of {@link EPPRegistryRegex} that specifies regular
	 *            expression used to validate the domain object authorization
	 *            information value
	 */
	public void setAuthInfoRegex(EPPRegistryRegex authInfoRegex) {
		if (authInfoRegex != null) {
			authInfoRegex.setRootName(ELM_AUTH_INFO_REGEX);
		}
		this.authInfoRegex = authInfoRegex;
	}

	/**
	 * Get domain status supported by the server.
	 * 
	 * @return instance of {@link EPPRegistrySupportedStatus} that contains a
	 *         list of supported domain status by the server
	 */
	public EPPRegistrySupportedStatus getSupportedStatus() {
		return supportedStatus;
	}

	/**
	 * Set domain status supported by the server.
	 * 
	 * @param supportedStatus
	 *            instance of {@link EPPRegistrySupportedStatus} that contains a
	 *            list of supported domain status by the server
	 */
	public void setSupportedStatus(EPPRegistrySupportedStatus supportedStatus) {
		this.supportedStatus = supportedStatus;
	}
}
