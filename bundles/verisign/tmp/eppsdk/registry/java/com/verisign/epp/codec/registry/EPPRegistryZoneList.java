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
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents a list of zone summary info. Upon receiving an &lt;info&gt;
 * command, with a &lt;registry:all&gt; element in it, the server puts a
 * &lt;registry:zoneList&gt; element in the response. The list contains the zone
 * summary attributes of supported zones. Use {@code setZoneList} and
 * {@code getZoneList} to set and get zone list; use {@code addZone} to add zone
 * to existing list; and use {@code setZone} to add one zone in the zone list (
 * {@code setZone} will empty the existing zone list).<br>
 * <br>
 * 
 * 
 * Each element in the list contains the following info:
 * 
 * <ul>
 * <li>
 * &lt;registry:name&gt; - fully qualified name of the zone</li>
 * <li>
 * &lt;registry:crDate&gt; - date of zone object creation</li>
 * <li>
 * &lt;registry:upDate&gt; - optional date of last update</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryZone
 */
public class EPPRegistryZoneList implements EPPCodecComponent {
	private static final long serialVersionUID = 6046461410231870288L;

	private static Logger cat = Logger.getLogger(EPPRegistryZoneList.class);

	/** XML Element Name of <code>EPPRegistryZoneList</code> root element. */
	final static String ELM_NAME = "registry:zoneList";

	/**
	 * {@code List} of {@link EPPRegistryZone} instance, which contains zone
	 * summary info
	 */
	// EPPRegistryZone
	List zoneList = new ArrayList();

	/**
	 * Default constructor. {@code zoneList} is initialized as an empty
	 * {@code List}.
	 */
	public EPPRegistryZoneList() {
		super();
		this.zoneList = new ArrayList();
	}

	/**
	 * Constructor that takes a zone list.
	 * 
	 * @param zoneList
	 *            {@code List} of zone summary
	 */
	public EPPRegistryZoneList(List zoneList) {
		this();
		this.zoneList = zoneList;
	}

	/**
	 * Constructor that takes one {@link EPPRegistryZone} instance.
	 * {@code zoneList} is initialized to have one element.
	 * 
	 * @param zoneSummary
	 *            summary attributes of one zone
	 */
	public EPPRegistryZoneList(EPPRegistryZone zoneSummary) {
		this();
		this.zoneList.add(zoneSummary);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the EPPRegistryZoneList
	 * instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the EPPRegistryZoneList
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode EPPRegistryZoneList instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		if (this.zoneList != null && this.zoneList.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, zoneList);
		}

		return root;
	}

	/**
	 * Decode the EPPRegistryZoneList attributes from the aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            - Root DOM Element to decode EPPRegistryZoneList from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		zoneList = EPPUtil.decodeCompList(aElement, EPPRegistryMapFactory.NS,
				EPPRegistryZone.ELM_NAME, EPPRegistryZone.class);

	}

	/**
	 * implements a deep <code>EPPRegistryZoneList</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryZoneList</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryZoneList)) {
			return false;
		}

		EPPRegistryZoneList theComp = (EPPRegistryZoneList) aObject;

		if (!((zoneList == null) ? (theComp.zoneList == null) : EPPUtil
				.equalLists(zoneList, theComp.zoneList))) {
			cat.error("EPPRegistryZoneList.equals(): zoneList not equal");
			return false;
		}

		return true;
	}

	/**
	 * Clone <code>EPPRegistryZoneList</code>.
	 * 
	 * @return clone of <code>EPPRegistryZoneList</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryZoneList clone = (EPPRegistryZoneList) super.clone();

		if (zoneList != null) {
			clone.zoneList = (List) ((ArrayList) zoneList).clone();
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
	 * Get the zone summary list.
	 * 
	 * @return {@code List} of {@code EPPRegistryZone}
	 */
	public List getZoneList() {
		return zoneList;
	}

	/**
	 * Set the zone summary list.
	 * 
	 * @param zoneList
	 *            {@code List} of {@code EPPRegistryZone}
	 */
	public void setZoneList(List zoneList) {
		this.zoneList = zoneList;
	}

	/**
	 * Append one zone to the existing zone list.
	 * 
	 * @param zone
	 *            zone to add
	 */
	public void addZone(EPPRegistryZone zone) {
		if (this.zoneList == null) {
			this.zoneList = new ArrayList();
		}
		this.zoneList.add(zone);
	}

	/**
	 * Clear the existing zone list and add add one zone to the list
	 * 
	 * @param zone
	 *            zone to add
	 */
	public void setZone(EPPRegistryZone zone) {
		this.zoneList = new ArrayList();
		this.zoneList.add(zone);
	}

	/**
	 * Append one zone to the existing zone list.
	 * 
	 * @deprecated As of v1.2, use {@link #addZone(EPPRegistryZone)}
	 * 
	 * @param zone
	 *            zone to add
	 */
	@Deprecated
	public void addTld(EPPRegistryZone zone) {
		if (this.zoneList == null) {
			this.zoneList = new ArrayList();
		}
		this.zoneList.add(zone);
	}
}
