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
import java.util.Date;
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
import com.verisign.epp.util.EqualityUtil;

/**
 * Represents the detailed information of a zone object. Upon receiving an
 * &lt;info&gt; command, with a &lt;registry:name&gt; element in it, the server
 * puts a &lt;registry:zone&gt; element in the response. <br>
 * <br>
 * 
 * Each element in the list contains the following info:
 * 
 * <ul>
 * <li>
 * &lt;registry:name&gt; - fully qualified name of the zone object. Zone name
 * can be at any level (top level, second level, third level, etc.). Use
 * {@link #getName()} and {@link #setName(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:group&gt; - An OPTIONAL server defined grouping of zones. Zone
 * in one group share similar features and policies. Use {@link #getGroup()} and
 * {@link #setGroup(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:subProduct&gt; - An OPTIONAL sub-product identifier used for the
 * zone and used as the value of the <namestoreExt:subProduct> element of the
 * NameStore Extension. Use {@link #getSubProduct()} and
 * {@link #setSubProduct(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:family&gt; - An OPTIONAL definition of the family of related
 * zones, where there can be a primary and alternate zone relationship on a per
 * domain basis. The primary domain name is the first one created and the last
 * one deleted in the family of domain names. The alternate domain names can be
 * enabled / disabled when the primary domain name exists. Use
 * {@link #getRelated()} and {@link #setRelated(EPPRegistryRelated)} to get and set
 * the element.</li>
 * <li>
 * &lt;services&gt; - Zero or more elements that defines a phase of the zone
 * based on the phases defined in the Launch Phase Mapping for the Extensible
 * Provisioning Protocol (EPP). Use {@link #getPhases()} and
 * {@link #setPhases(List)} to get and set the element. Use
 * {@link #addPhase(EPPRegistryPhase)} to append on phase to an existing phase
 * list.</li>
 * <li>
 * &lt;registry:services&gt; - The OPTIONAL EPP namespace URIs of the objects
 * and object extensions supported by the server based on RFC 5730. Use
 * {@link #getServices()} and {@link #setServices(EPPRegistryServices)} to get
 * and set the element.</li>
 * <li>
 * &lt;registry:slaInfo&gt; - The OPTIONAL Service-Level Agreement (SLA)
 * information for the zone. The SLA information CAN include availability as
 * well as response time SLA's. Use {@link #getSlaInfo()} and
 * {@link #setSlaInfo(EPPRegistrySLAInfo)} to get and set the element.</li>
 * <li>
 * &lt;registry:crID&gt; - The OPTIONAL identifier of the client that created
 * the zone. Use {@link #getCreatedBy()} and {@link #setCreatedBy(String)} to
 * get and set the element.</li>
 * <li>
 * &lt;registry:crDate&gt; - The date and time of zone object creation. Use
 * {@link #getCreatedDate()} and {@link #setCreatedDate(Date)} to get and set
 * the element.</li>
 * <li>
 * &lt;registry:upID&gt; - The OPTIONAL identifier of the client that last
 * updated the zone object.This element MUST NOT be present if the zone has
 * never been modified. Use {@link #getLastUpdatedBy()} and
 * {@link #setLastUpdatedBy(String)} to get and set the element.</li>
 * <li>
 * &lt;registry:upDate&gt; - The OPTIONAL date and time of the most recent zone
 * object modification. This element MUST NOT be present if the zone object has
 * never been modified. Use {@link #getLastUpdatedDate()} and
 * {@link #setLastUpdatedDate(Date)} to get and set the element.</li>
 * <li>
 * &lt;registry:domain&gt; - The domain name object policy information per RFC
 * 5731. Use {@link #getDomain()} and {@link #setDomain(EPPRegistryDomain)} to
 * get and set the element.</li>
 * <li>
 * &lt;registry:host&gt; - The host object policy information per RFC 5732. Use
 * {@link #getHost()} and {@link #setHost(EPPRegistryHost)} to get and set the
 * element.</li>
 * <li>
 * &lt;registry:contact&gt; - The contact object policy information per RFC
 * 5733. Use {@link #getContact()} and {@link #setContact(EPPRegistryContact)}
 * to get and set the element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryZone
 * @see com.verisign.epp.codec.registry.EPPRegistryPhase
 * @see com.verisign.epp.codec.registry.EPPRegistryServices
 * @see com.verisign.epp.codec.registry.EPPRegistrySLAInfo
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryHost
 * @see com.verisign.epp.codec.registry.EPPRegistryContact
 */
public class EPPRegistryZoneInfo implements EPPCodecComponent {
	private static final long serialVersionUID = 8689168181168142683L;

	/** XML Element Name of <code>EPPRegistryZoneInfo</code> root element. */
	final static String ELM_NAME = "registry:zone";

	/** XML tag name for the <code>name</code> attribute. */
	private final static String ELM_REGISTRY_NAME = "registry:name";

	/** XML tag name for the <code>group</code> attribute. */
	private final static String ELM_GROUP = "registry:group";

	/** XML tag name for the <code>subProduct</code> attribute. */
	private final static String ELM_SUB_PRODUCT = "registry:subProduct";

	/** XML tag name for the <code>createdDate</code> attribute. */
	private final static String ELM_CRDATE = "registry:crDate";

	/** XML tag name for the <code>createdBy</code> attribute. */
	private final static String ELM_CRID = "registry:crID";

	/** XML tag name for the <code>lastUpdatedDate</code> attribute. */
	private final static String ELM_UPDATE = "registry:upDate";

	/** XML tag name for the <code>lastUpdatedBy</code> attribute. */
	private final static String ELM_UPID = "registry:upID";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryZoneInfo.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	private String name = null;
	private String group = null;
	private String subProduct = null;
	private EPPRegistryRelated related = null;
	// EPPRegistryPhase
	private List phases = new ArrayList();
	private EPPRegistryServices services = null;
	private EPPRegistrySLAInfo slaInfo = null;
	private String createdBy = null;
	private Date createdDate = null;
	private String lastUpdatedBy = null;
	private Date lastUpdatedDate = null;

	private EPPRegistryDomain domain = null;
	private EPPRegistryHost host = null;
	private EPPRegistryContact contact = null;

	/**
	 * Default constructor. All non list attributes are initialized to
	 * {@code null}. {@code phases} is initialized to empty {@code List}. <br>
	 * <br>
	 * Please make sure to set the required attributes before calling the
	 * {@link #encode(Document)} method:
	 * 
	 * <ul>
	 * <li>{@link #setName(String)}</li>
	 * <li>{@link #setCreatedDate(Date)}</li>
	 * <li>{@link #setDomain(EPPRegistryDomain)}</li>
	 * <li>{@link #setHost(EPPRegistryHost)}</li>
	 * <li>{@link #setContact(EPPRegistryContact)}</li>
	 * </ul>
	 */
	public EPPRegistryZoneInfo() {
		super();
	}

	/**
	 * Construct an {@code EPPRegistryZoneInfo} instance using a zone name. <br>
	 * <br>
	 * All other non list attributes are initialized to {@code null}.
	 * {@code phases} is initialized to empty {@code List}. <br>
	 * <br>
	 * Please make sure to set the required attributes before calling the
	 * {@link #encode(Document)} method:
	 * 
	 * <ul>
	 * <li>{@link #setCreatedDate(Date)}</li>
	 * <li>{@link #setDomain(EPPRegistryDomain)}</li>
	 * <li>{@link #setHost(EPPRegistryHost)}</li>
	 * <li>{@link #setContact(EPPRegistryContact)}</li>
	 * </ul>
	 * 
	 * @param name
	 *            fully qualified name of the zone object
	 */
	public EPPRegistryZoneInfo(String name) {
		this();
		this.name = name;
	}

	/**
	 * Construct an {@code EPPRegistryZoneInfo} instance using a zone name, a
	 * create id and a create date. <br>
	 * <br>
	 * All other non list attributes are initialized to {@code null}.
	 * {@code phases} is initialized to empty {@code List}. <br>
	 * <br>
	 * Please make sure to set the required attributes before calling the
	 * {@link #encode(Document)} method:
	 * 
	 * <ul>
	 * <li>{@link #setDomain(EPPRegistryDomain)}</li>
	 * <li>{@link #setHost(EPPRegistryHost)}</li>
	 * <li>{@link #setContact(EPPRegistryContact)}</li>
	 * </ul>
	 * 
	 * @param name
	 *            fully qualified name of the zone
	 * @param aCreatedBy
	 *            identifier of the client that created the zone
	 * @param aCreatedDate
	 *            creation date of the zone
	 */
	public EPPRegistryZoneInfo(String name, String aCreatedBy, Date aCreatedDate) {
		this(name);
		this.createdBy = aCreatedBy;
		this.createdDate = aCreatedDate;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryZoneInfo} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryZoneInfo} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryZoneInfo} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			cat.error("EPPRegistryInfoResp.doEncode(): Invalid state on encode: "
					+ e);
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryInfoResp.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		EPPUtil.encodeString(aDocument, root, name, EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);

		if (group != null && group.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, group,
					EPPRegistryMapFactory.NS, ELM_GROUP);
		}

		if (subProduct != null && subProduct.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, subProduct,
					EPPRegistryMapFactory.NS, ELM_SUB_PRODUCT);
		}

		if (related != null) {
			EPPUtil.encodeComp(aDocument, root, related);
		}

		if (phases != null && phases.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, phases);
		}

		if (services != null) {
			EPPUtil.encodeComp(aDocument, root, services);
		}

		if (slaInfo != null) {
			EPPUtil.encodeComp(aDocument, root, slaInfo);
		}

		if (createdBy != null && createdBy.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, createdBy,
					EPPRegistryMapFactory.NS, ELM_CRID);
		}

		if (createdDate != null) {
			EPPUtil.encodeTimeInstant(aDocument, root, createdDate,
					EPPRegistryMapFactory.NS, ELM_CRDATE);
		}

		if (lastUpdatedBy != null && lastUpdatedBy.trim().length() > 0) {
			EPPUtil.encodeString(aDocument, root, lastUpdatedBy,
					EPPRegistryMapFactory.NS, ELM_UPID);
		}

		if (lastUpdatedDate != null) {
			EPPUtil.encodeTimeInstant(aDocument, root, lastUpdatedDate,
					EPPRegistryMapFactory.NS, ELM_UPDATE);
		}

		EPPUtil.encodeComp(aDocument, root, domain);
		EPPUtil.encodeComp(aDocument, root, host);
		EPPUtil.encodeComp(aDocument, root, contact);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryZoneInfo} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryZoneInfo} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		name = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_REGISTRY_NAME);
		group = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_GROUP);
		subProduct = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_SUB_PRODUCT);
		related = (EPPRegistryRelated) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryRelated.ELM_NAME,
				EPPRegistryRelated.class);
		phases = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistryPhase.ELM_NAME, EPPRegistryPhase.class);
		services = (EPPRegistryServices) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryServices.ELM_NAME,
				EPPRegistryServices.class);
		slaInfo = (EPPRegistrySLAInfo) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistrySLAInfo.ELM_NAME,
				EPPRegistrySLAInfo.class);
		createdBy = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_CRID);
		createdDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_CRDATE);
		lastUpdatedBy = EPPUtil.decodeString(aElement,
				EPPRegistryMapFactory.NS, ELM_UPID);
		lastUpdatedDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_UPDATE);
		domain = (EPPRegistryDomain) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryDomain.ELM_NAME,
				EPPRegistryDomain.class);
		host = (EPPRegistryHost) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryHost.ELM_NAME,
				EPPRegistryHost.class);
		contact = (EPPRegistryContact) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryContact.ELM_NAME,
				EPPRegistryContact.class);
	}

	/**
	 * implements a deep <code>EPPRegistryZoneInfo</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryZoneInfo</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryZoneInfo)) {
			return false;
		}

		EPPRegistryZoneInfo theComp = (EPPRegistryZoneInfo) aObject;

		if (!EqualityUtil.equals(this.name, theComp.name)) {
			cat.error("EPPRegistryZoneInfo.equals(): name not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.group, theComp.group)) {
			cat.error("EPPRegistryZoneInfo.equals(): group not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.subProduct, theComp.subProduct)) {
			cat.error("EPPRegistryZoneInfo.equals(): subProduct not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.domain, theComp.domain)) {
			cat.error("EPPRegistryZoneInfo.equals(): domain not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.host, theComp.host)) {
			cat.error("EPPRegistryZoneInfo.equals(): host not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.contact, theComp.contact)) {
			cat.error("EPPRegistryZoneInfo.equals(): contact not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.createdBy, theComp.createdBy)) {
			cat.error("EPPRegistryZoneInfo.equals(): createdBy not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.createdDate, theComp.createdDate)) {
			cat.error("EPPRegistryZoneInfo.equals(): createdDate not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.related, theComp.related)) {
			cat.error("EPPRegistryZoneInfo.equals(): family not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.lastUpdatedBy, theComp.lastUpdatedBy)) {
			cat.error("EPPRegistryZoneInfo.equals(): lastUpdatedBy not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.lastUpdatedDate, theComp.lastUpdatedDate)) {
			cat.error("EPPRegistryZoneInfo.equals(): lastUpdatedDate not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.phases, theComp.phases)) {
			cat.error("EPPRegistryZoneInfo.equals(): phases not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.services, theComp.services)) {
			cat.error("EPPRegistryZoneInfo.equals(): services not equal");
			return false;
		}
		if (!EqualityUtil.equals(this.slaInfo, theComp.slaInfo)) {
			cat.error("EPPRegistryZoneInfo.equals(): slaInfo not equal");
			return false;
		}

		return true;
	}

	/**
	 * Validate the state of the <code>EPPRegistryZoneInfo</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {

		if (name == null) {
			throw new EPPCodecException("name required element is not set");
		}

		if (domain == null) {
			throw new EPPCodecException("domain required element is not set");
		}

		if (host == null) {
			throw new EPPCodecException("host required element is not set");
		}
		
	}

	/**
	 * Clone <code>EPPRegistryZoneInfo</code>.
	 * 
	 * @return clone of <code>EPPRegistryZoneInfo</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryZoneInfo clone = (EPPRegistryZoneInfo) super.clone();

		if (related != null) {
			clone.related = (EPPRegistryRelated) related.clone();
		}
		if (phases != null) {
			clone.phases = (List) ((ArrayList) phases).clone();
		}
		if (services != null) {
			clone.services = (EPPRegistryServices) services.clone();
		}
		if (slaInfo != null) {
			clone.slaInfo = (EPPRegistrySLAInfo) slaInfo.clone();
		}
		if (domain != null) {
			clone.domain = (EPPRegistryDomain) domain.clone();
		}

		if (host != null) {
			clone.host = (EPPRegistryHost) host.clone();
		}

		if (contact != null) {
			clone.contact = (EPPRegistryContact) contact.clone();
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
	 * Get name of zone.
	 * 
	 * @return fully qualified zone name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of zone.
	 * 
	 * @param name
	 *            fully qualified zone name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the related zones.
	 * 
	 * @return Related zones if defined; <code>null</code> otherwise.
	 */
	public EPPRegistryRelated getRelated() {
		return this.related;
	}
	
	/**
	 * Is related defined?
	 * 
	 * @return <code>true</code> if related is defined; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasRelated() {
		return (this.related != null ? true : false);
	}
	

	/**
	 * Sets the related zones.
	 * 
	 * @param aRelated
	 *            Related zones
	 */
	public void setRelated(EPPRegistryRelated aRelated) {
		this.related = aRelated;
	}

	/**
	 * Get {@code List} of zone phases.
	 * 
	 * @return List of zone phases defined in the "Launch Phase Mapping for the
	 *         Extensible Provisioning Protocol (EPP)"
	 */
	public List getPhases() {
		return phases;
	}

	/**
	 * Set {@code List} of zone phases.
	 * 
	 * @param phases
	 *            List of zone phases defined in the "Launch Phase Mapping for
	 *            the Extensible Provisioning Protocol (EPP)"
	 */
	public void setPhases(List phases) {
		this.phases = phases;
	}

	/**
	 * Append a zone phase to existing {@code List} of phases.
	 * 
	 * @param phase
	 *            define attributes of one phase
	 */
	public void addPhase(EPPRegistryPhase phase) {
		if (this.phases == null) {
			this.phases = new ArrayList();
		}
		this.phases.add(phase);
	}

	/**
	 * Get services supported by the zone
	 * 
	 * @return instance of {@link EPPRegistryServices} that lists namespace URIs
	 *         of the objects and object extensions supported by the zone
	 */
	public EPPRegistryServices getServices() {
		return services;
	}

	/**
	 * Set services supported by the zone
	 * 
	 * @param services
	 *            instance of {@link EPPRegistryServices} that lists namespace
	 *            URIs of the objects and object extensions supported by the
	 *            zone
	 */
	public void setServices(EPPRegistryServices services) {
		this.services = services;
	}

	/**
	 * Get the identifier of the client that created the zone.
	 * 
	 * @return the identifier of the client that created the zone
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * Set the identifier of the client that created the zone
	 * 
	 * @param createdBy
	 *            the identifier of the client that created the zone
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Get zone creation date.
	 * 
	 * @return zone creation date
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * set zone create date.
	 * 
	 * @param createdDate
	 *            zone creation date
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * Get the identifier of the client that last updated the zone object.
	 * 
	 * @return the identifier of the client that last updated the zone object,
	 *         or {@code null} if the zone object has never been updated.
	 */
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	/**
	 * Set the identifier of the client that last updated the zone object.
	 * 
	 * @param lastUpdatedBy
	 *            the identifier of the client that last updated the zone object
	 */
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	/**
	 * Get the zone last updated date.
	 * 
	 * @return the last updated date of the zone object, or {@code null} if the
	 *         zone has never been updated.
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * Set the zone last updated date.
	 * 
	 * @param lastUpdatedDate
	 *            the last updated date of the zone object
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * Get the domain name object policy information.
	 * 
	 * @return the domain name object policy information per RFC 5731
	 */
	public EPPRegistryDomain getDomain() {
		return domain;
	}

	/**
	 * Set the domain name object policy information.
	 * 
	 * @param domain
	 *            the domain name object policy information per RFC 5731
	 */
	public void setDomain(EPPRegistryDomain domain) {
		this.domain = domain;
	}

	/**
	 * Get the host object policy information.
	 * 
	 * @return the host object policy information per RFC 5732
	 */
	public EPPRegistryHost getHost() {
		return host;
	}

	/**
	 * Set the host object policy information.
	 * 
	 * @param host
	 *            the host object policy information per RFC 5732
	 */
	public void setHost(EPPRegistryHost host) {
		this.host = host;
	}

	/**
	 * Get the contact object policy information.
	 * 
	 * @return the contact object policy information per RFC 5733.
	 */
	public EPPRegistryContact getContact() {
		return contact;
	}

	/**
	 * Set the contact object policy information.
	 * 
	 * @param contact
	 *            the contact object policy information per RFC 5733.
	 */
	public void setContact(EPPRegistryContact contact) {
		this.contact = contact;
	}

	/**
	 * Get the Service-Level Agreement (SLA) information for the zone.
	 * 
	 * @return the Service-Level Agreement (SLA) information for the zone.
	 */
	public EPPRegistrySLAInfo getSlaInfo() {
		return slaInfo;
	}

	/**
	 * Set the Service-Level Agreement (SLA) information for the zone.
	 * 
	 * @param slaInfo
	 *            the Service-Level Agreement (SLA) information for the zone.
	 */
	public void setSlaInfo(EPPRegistrySLAInfo slaInfo) {
		this.slaInfo = slaInfo;
	}

	/**
	 * Get the sub-product identifier used for the zone.
	 * 
	 * @return sub-product identifier used for the zone and used as the value of
	 *         the &lt;namestoreExt:subProduct&gt; element of the NameStore
	 *         Extension
	 */
	public String getSubProduct() {
		return subProduct;
	}

	/**
	 * Set the sub-product identifier used for the zone.
	 * 
	 * @param subProduct
	 *            sub-product identifier used for the zone and used as the value
	 *            of the &lt;namestoreExt:subProduct&gt; element of the
	 *            NameStore Extension
	 */
	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	/**
	 * Get zone group.
	 * 
	 * @return server defined grouping of zones that the zone belongs to with
	 *         similar features and policies
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Set zone group.
	 * 
	 * @param group
	 *            server defined grouping of zones that the zone belongs to with
	 *            similar features and policies
	 */
	public void setGroup(String group) {
		this.group = group;
	}
}
