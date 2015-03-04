package com.verisign.epp.codec.registry;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Class that represents a related zone member that includes 
 * a type and zone name attribute.
 */
public class EPPRegistryZoneMember implements EPPCodecComponent {
	
	private static final long serialVersionUID = 3094793192262086070L;

	private static Logger cat = Logger.getLogger(EPPRegistryZoneMember.class);

	/**
	 * Constant for the phase local name
	 */
	public static final String ELM_LOCALNAME = "zoneMember";

	/**
	 * Constant for the qualified name (prefix and local name)
	 */
	public static final String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Constant where all domain names in the zone MUST be a primary domain
	 * name.
	 */
	public final static String TYPE_PRIMARY = "primary";

	/**
	 * Constant where domain names of the zone can only be created when the
	 * primary domain name exists.
	 */
	public final static String TYPE_ALTERNATE = "alternate";

	/**
	 * Constant where a domain name in the zone can be either a primary or
	 * alternate domain name based on the earliest created date.
	 */
	public final static String TYPE_PRIMARY_BASED_ON_CR_DATE = "primaryBasedOnCrDate";

	/**
	 * Constant where there is no concept of primary and alternate domain names,
	 * so the related zones are treated as equal. Domain names can be created
	 * and deleted in any order.
	 */
	public final static String TYPE_EQUAL = "equal";

	private final static String ATTR_TYPE = "type";

	/**
	 * Type of zone member using one of the <code>TYPE</code> constant values.
	 */
	private String type = null;

	/**
	 * Name of the zone.
	 */
	private String zoneName = null;

	/**
	 * Default constructor. Both the <code>type</code> and the
	 * <code>zoneName</code> MUST be set.
	 */
	public EPPRegistryZoneMember() {
	}

	/**
	 * Constructor that takes the required attributes including the zone name
	 * and the type of the zone member.
	 * 
	 * @param aZoneName
	 *            Name of the zone.
	 * @param aType
	 *            Type of the zone member using one of the <code>TYPE</code>
	 *            constants.
	 */
	public EPPRegistryZoneMember(String aZoneName, String aType) {
		super();
		this.zoneName = aZoneName;
		this.type = aType;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPRegistryZoneMember</code> instance.
	 * 
	 * @param aDocument
	 *            - DOM Document that is being built. Used as an Element
	 *            factory.
	 * 
	 * @return Element - Root DOM Element representing the
	 *         <code>EPPRegistryZoneMember</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode <code>EPPRegistryZoneMember</code>
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			this.validateState();
		}
		catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryZoneMember.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		Text currVal = aDocument.createTextNode(zoneName);
		root.appendChild(currVal);
		root.setAttribute(ATTR_TYPE, type);
		return root;
	}

	/**
	 * Decode the <code>EPPRegistryZoneMember</code> element aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            - Root DOM Element to decode <code>EPPRegistryZoneMember</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		zoneName = aElement.getFirstChild().getNodeValue();
		type = aElement.getAttribute(ATTR_TYPE);
	}
	

	/**
	 * implements a deep <code>EPPRegistryZoneMember</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryZoneMember</code> instance to compare with
	 * 
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryZoneMember)) {
			return false;
		}

		EPPRegistryZoneMember theComp = (EPPRegistryZoneMember) aObject;

		if (!((zoneName == null) ? (theComp.zoneName == null) : zoneName
				.equals(theComp.zoneName))) {
			cat.error("EPPRegistryZoneMemeber.equals(): zoneName not equal");

			return false;
		}
		if (!((type == null) ? (theComp.type == null) : type
				.equals(theComp.type))) {
			cat.error("EPPRegistryZoneMemeber.equals(): type not equal");

			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryZoneMember</code>.
	 * 
	 * @return clone of <code>EPPRegistryZoneMember</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryZoneMember) super.clone();
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
	 * Validate the state of the <code>EPPRegistryZoneMember</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPRegistryZoneMember will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 *             If there is an invalid state
	 */
	void validateState() throws EPPCodecException {
		if (zoneName == null || zoneName.length() == 0) {
			throw new EPPCodecException("zoneName attribute is not set");
		}
		if (!TYPE_PRIMARY.equals(type) && !TYPE_ALTERNATE.equals(type) && 
			!TYPE_PRIMARY_BASED_ON_CR_DATE.equals(type) &&
			!TYPE_EQUAL.equals(type)) {
			throw new EPPCodecException("zone member has an invalid type: "
					+ type );
		}
	}

	/**
	 * Gets the zone name of the related zone.
	 * 
	 * @return Zone name if set; <code>null</code> otherwise.
	 */
	public String getZoneName() {
		return zoneName;
	}

	/**
	 * Sets the zone name of the related zone.
	 * 
	 * @param aZoneName Zone name
	 */
	public void setZoneName(String aZoneName) {
		this.zoneName = aZoneName;
	}

	/**
	 * Gets the type of the related zone.
	 * 
	 * @return One of the <code>TYPE</code> constant values if set; <code>null</code> otherwise.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type of the related zone.
	 * 
	 * @param aType One of the <code>TYPE</code> constant values.
	 */
	public void setType(String aType) {
		this.type = aType;
	}
}
