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

import java.util.Date;

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
 * Represents the summary info of a zone object. Zone summary info of all
 * supported zones is returned is a list when the server receives an
 * &lt;info&gt; command, with a &lt;registry:all&gt; element in it.<br>
 * <br>
 * 
 * 
 * The zone summary info element contains:
 * 
 * <ul>
 * <li>
 * &lt;registry:name&gt; - fully qualified name of the zone. Use
 * {@link #setName(String)} and {@link #getName()} to access the attribute.</li>
 * <li>
 * &lt;registry:crDate&gt; - date of zone object creation. Use
 * {@link #setCreateDate(Date)} and {@link #getCreateDate()} to access the
 * attribute.</li>
 * <li>
 * &lt;registry:upDate&gt; - optional date of last update. Use
 * {@link #setUpdateDate(Date)} and {@link #getUpdateDate()} to access the
 * attribute.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryZoneList
 */
public class EPPRegistryZone implements EPPCodecComponent {
	private static final long serialVersionUID = 3697102804288584732L;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPRegistryZone.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** XML Element Name of <code>EPPRegistryZone</code> root element. */
	final static String ELM_NAME = "registry:zone";

	/** XML Element Name of <code>name</code> attribute. */
	final static String ELM_ZONE_NAME = "registry:name";

	/** XML Element Name of <code>createDate</code> attribute. */
	final static String ELM_CREATE_DATE = "registry:crDate";

	/** XML Element Name of <code>updateDate</code> attribute. */
	final static String ELM_UPDATE_DATE = "registry:upDate";

	/** fully qualified name of the zone object */
	private String name;

	/** date of object creation */
	private Date createDate;

	/** date of last update */
	private Date updateDate;

	/**
	 * Default constructor. Attributes are set to:
	 * <ul>
	 * <li>{@code name} - null</li>
	 * <li>{@code createDate} - null</li>
	 * <li>{@code updateDate} - null</li>
	 * </ul>
	 * 
	 * Use {@link #setName(String)} and {@link #setCreateDate(Date)} before
	 * calling {@link #encode(Document)}
	 */
	public EPPRegistryZone() {
		super();
	}

	/**
	 * Construct {@code EPPRegistryZone} with name and create date.
	 * {@code updateDate} is set to null.
	 * 
	 * @param name
	 *            fully qualified name of zone object
	 * @param createDate
	 *            creation date of zone object
	 */
	public EPPRegistryZone(String name, Date createDate) {
		this();
		this.name = name;
		this.createDate = createDate;
	}

	/**
	 * Construct {@code EPPRegistryZone} with name, craete date and last update
	 * date.
	 * 
	 * @param name
	 *            fully qualified name of zone object
	 * @param createDate
	 *            creation date of zone object
	 * @param updateDate
	 *            date of last update
	 */
	public EPPRegistryZone(String name, Date createDate, Date updateDate) {
		this(name, createDate);
		this.updateDate = updateDate;
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryZone} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryZone}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryZone} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			cat.error(this.getClass().getName()
					+ ".encode(): Invalid state on encode: " + e);
			throw new EPPEncodeException("Invalid state on "
					+ this.getClass().getName() + ".encode(): " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		// EPPUtil.encodeString(aDocument, root, tldId,
		// EPPRegistryMapFactory.NS, ELM_TLD_ID);
		EPPUtil.encodeString(aDocument, root, name, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);
		EPPUtil.encodeTimeInstant(aDocument, root, createDate,
				EPPRegistryMapFactory.NS, ELM_CREATE_DATE);
		if (this.updateDate != null) {
			EPPUtil.encodeTimeInstant(aDocument, root, updateDate,
					EPPRegistryMapFactory.NS, ELM_UPDATE_DATE);
		}

		return root;
	}

	/**
	 * Validate the state of the <code>EPPRegistryZone</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		// if (tldId == null) {
		// throw new EPPCodecException("tldId required attribute is not set");
		// }
		if (name == null) {
			throw new EPPCodecException("name required attribute is not set");
		}
		if (createDate == null) {
			throw new EPPCodecException(
					"createDate required attribute is not set");
		}
	}

	/**
	 * Decode the {@code EPPRegistryZone} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryZone} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// this.tldId = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
		// ELM_TLD_ID);
		this.name = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_ZONE_NAME);
		this.createDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_CREATE_DATE);
		this.updateDate = EPPUtil.decodeTimeInstant(aElement,
				EPPRegistryMapFactory.NS, ELM_UPDATE_DATE);
	}

	/**
	 * implements a deep <code>EPPRegistryZone</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryZone</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryZone)) {
			return false;
		}

		EPPRegistryZone theComp = (EPPRegistryZone) aObject;

		// if (!((tldId == null) ? (theComp.tldId == null) : tldId
		// .equals(theComp.tldId))) {
		// return false;
		// }
		if (!((name == null) ? (theComp.name == null) : name
				.equals(theComp.name))) {
			return false;
		}
		if (!((createDate == null) ? (theComp.createDate == null) : createDate
				.equals(theComp.createDate))) {
			return false;
		}
		if (!((updateDate == null) ? (theComp.updateDate == null) : updateDate
				.equals(theComp.updateDate))) {
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryZone</code>.
	 * 
	 * @return clone of <code>EPPRegistryZone</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryZone clone = (EPPRegistryZone) super.clone();

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
	 * Get name of the zone object.
	 * 
	 * @return fully qualified name of the zone object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of the zone object.
	 * 
	 * @param name
	 *            fully qualified name of the zone object
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get creation date of zone object.
	 * 
	 * @return creation date of zone object
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Set creation date of zone object.
	 * 
	 * @param createDate
	 *            creation date of zone object
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Get last update date of zone object.
	 * 
	 * @return last update date of zone object
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Set last update date of zone object.
	 * 
	 * @param updateDate
	 *            last update date of zone object
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
}
