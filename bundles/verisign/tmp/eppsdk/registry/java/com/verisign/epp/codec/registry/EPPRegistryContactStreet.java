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
package com.verisign.epp.codec.registry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class EPPRegistryContactStreet extends EPPRegistryMinMaxLength {
	/** DOCUMENT ME! */
	private static final long serialVersionUID = -4168392487627210996L;

	/** DOCUMENT ME! */
	public static final String ELM_NAME = "registry:street";

	/** DOCUMENT ME! */
	public static final String ELM_MIN_ENTRY = "registry:minEntry";

	/** DOCUMENT ME! */
	public static final String ELM_MAX_ENTRY = "registry:maxEntry";

	/** DOCUMENT ME! */
	private Integer minEntry = null;

	/** DOCUMENT ME! */
	private Integer maxEntry = null;

	/**
	 * Creates a new EPPRegistryContactStreet object.
	 */
	public EPPRegistryContactStreet() {
		super();
		this.rootName = ELM_NAME;
	}

	/**
	 * Creates a new EPPRegistryContactStreet object.
	 * 
	 * @param minLength
	 *            DOCUMENT ME!
	 * @param maxLength
	 *            DOCUMENT ME!
	 * @param minEntry
	 *            DOCUMENT ME!
	 * @param maxEntry
	 *            DOCUMENT ME!
	 */
	public EPPRegistryContactStreet(Integer minLength, Integer maxLength,
			Integer minEntry, Integer maxEntry) {
		this();
		this.min = minLength;
		this.max = maxLength;
		this.minEntry = minEntry;
		this.maxEntry = maxEntry;
	}

	/**
	 * Creates a new EPPRegistryContactStreet object.
	 * 
	 * @param minLength
	 *            DOCUMENT ME!
	 * @param maxLength
	 *            DOCUMENT ME!
	 * @param minEntry
	 *            DOCUMENT ME!
	 * @param maxEntry
	 *            DOCUMENT ME!
	 */
	public EPPRegistryContactStreet(int minLength, int maxLength, int minEntry,
			int maxEntry) {
		this(new Integer(minLength), new Integer(maxLength), new Integer(
				minEntry), new Integer(maxEntry));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aDocument
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws EPPEncodeException
	 *             DOCUMENT ME!
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if ((minEntry == null) || (minEntry.intValue() < 0)) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryContactStreet.encode: minEntry is required and should be greater than or equal to 0");
		}

		if ((maxEntry == null) || (maxEntry.intValue() < minEntry.intValue())
				|| maxEntry.intValue() > 3) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryContactStreet.encode: maxEntry is required and should be greater than or equal to min, and less than or equal to 3");
		}

		Element root = super.encode(aDocument);
		EPPUtil.encodeString(aDocument, root, minEntry.toString(),
				EPPRegistryMapFactory.NS, ELM_MIN_ENTRY);
		EPPUtil.encodeString(aDocument, root, maxEntry.toString(),
				EPPRegistryMapFactory.NS, ELM_MAX_ENTRY);

		return root;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aElement
	 *            DOCUMENT ME!
	 * 
	 * @throws EPPDecodeException
	 *             DOCUMENT ME!
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		super.decode(aElement);
		minEntry = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS,
				ELM_MIN_ENTRY);
		maxEntry = EPPUtil.decodeInteger(aElement, EPPRegistryMapFactory.NS,
				ELM_MAX_ENTRY);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @throws CloneNotSupportedException
	 *             DOCUMENT ME!
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryContactStreet) super.clone();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param aObject
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!super.equals(aObject)) {
			return false;
		}

		if (!(aObject instanceof EPPRegistryContactStreet)) {
			return false;
		}

		EPPRegistryContactStreet theComp = (EPPRegistryContactStreet) aObject;

		if (!((minEntry == null) ? (theComp.minEntry == null) : minEntry
				.equals(theComp.minEntry))) {
			return false;
		}

		if (!((maxEntry == null) ? (theComp.maxEntry == null) : maxEntry
				.equals(theComp.maxEntry))) {
			return false;
		}

		return true;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	public Integer getMinEntry() {
		return minEntry;
	}

	public void setMinEntry(Integer minEntry) {
		this.minEntry = minEntry;
	}

	public Integer getMaxEntry() {
		return maxEntry;
	}

	public void setMaxEntry(Integer maxEntry) {
		this.maxEntry = maxEntry;
	}

	protected void validateState() throws EPPEncodeException {
		if (min == null || min.intValue() < 0) {
			throw new EPPEncodeException(
					"Invalid state on "
							+ getClass().getName()
							+ ".encode: min is required and should be greater than or equal to 0");
		}
		if (max == null || max.intValue() < min.intValue()) {
			throw new EPPEncodeException(
					"Invalid state on "
							+ getClass().getName()
							+ ".encode: max is required and should be greater than or equal to min");
		}
		if (max.intValue() > 255) {
			throw new EPPEncodeException("Invalid state on "
					+ getClass().getName()
					+ ".encode: max should be less than or equal to 255");
		}
	}
}
