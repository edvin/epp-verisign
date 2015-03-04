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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the DS Data Interface, as defined in RFC 5910, policies. The
 * &lt;registry:dsDataInterface&gt; element contains the following child
 * elements: <br>
 * <br>
 * 
 * <ul>
 * <li>&lt;registry:min&gt; - the minimum number of DS associated with the
 * domain object. Use {@link #getMin()} and {@link #setMin(Integer)} to get and
 * set the element.</li>
 * <li>&lt;registry:max&gt; - the maximum number of DS associated with the
 * domain object. Use {@link #getMax()} and {@link #setMax(Integer)} to get and
 * set the element.</li>
 * <li>&lt;registry:alg&gt; - zero or more &lt;registry:alg&gt; elements that
 * define the supported algorithms as described in section 5.1.2 of RFC 4034.
 * Use {@link #getAlgorithms()} and {@link #setAlgorithms(List)} to get and set
 * the element.</li>
 * <li>&lt;registry:digestType&gt; - zero or more &lt;registry:digestType&gt;
 * elements that define the supported digest types as described in section 5.1.3
 * of RFC 4034. Use {@link #getDigestTypes()} and {@link #setDigestTypes(List)}
 * to get and set the element.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDNSSEC
 */
public class EPPRegistryDS extends EPPRegistryKey {
	private static final long serialVersionUID = -1180818480629088110L;

	/** XML Element Name of <code>EPPRegistryDS</code> root element. */
	public static final String ELM_NAME = "registry:dsDataInterface";

	/** XML tag name for the <code>digestTypeF</code> attribute. */
	public static final String ELM_DIGEST = "registry:digestType";

	/** {@code List} of digest type in {@code String} */
	private List digestTypes = new ArrayList();

	/**
	 * Constructs a new {@code EPPRegistryDS} object. All attributes are set to
	 * default. Must call {@link #setMin(Integer)} and {@link #setMax(Integer)}
	 * before calling {@link #encode(Document)} method.
	 */
	public EPPRegistryDS() {
		super();
	}

	/**
	 * Constructs a new {@code EPPRegistryDS} with given values.
	 * 
	 * @param min
	 *            minimum number of DS associated with the domain object
	 * @param max
	 *            maximum number of DS associated with the domain object
	 * @param algorithms
	 *            {@code List} of algorithm {@code String}
	 * @param digestTypes
	 *            {@code List} of digest type {@code String}
	 */
	public EPPRegistryDS(Integer min, Integer max, List algorithms,
			List digestTypes) {
		super(min, max, algorithms);
		this.digestTypes = digestTypes;
	}

	/**
	 * Constructs a new {@code EPPRegistryDS} with given values.
	 * 
	 * @param min
	 *            minimum number of DS associated with the domain object
	 * @param max
	 *            maximum number of DS associated with the domain object
	 * @param algorithms
	 *            {@code List} of algorithm {@code String}
	 * @param digestTypes
	 *            {@code List} of digest type {@code String}
	 */
	public EPPRegistryDS(int min, int max, List algorithms, List digestTypes) {
		this(new Integer(min), new Integer(max), algorithms, digestTypes);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDS} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryDS}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDS} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element root = super.encode(aDocument);

		if ((digestTypes != null) && (digestTypes.size() > 0)) {
			EPPUtil.encodeList(aDocument, root, digestTypes,
					EPPRegistryMapFactory.NS, ELM_DIGEST);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDS} attributes from the aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDS} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		super.decode(aElement);
		digestTypes = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ELM_DIGEST);
	}

	/**
	 * Clone <code>EPPRegistryDS</code>.
	 * 
	 * @return clone of <code>EPPRegistryDS</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDS clone = (EPPRegistryDS) super.clone();

		if (digestTypes != null) {
			clone.digestTypes = (List) ((ArrayList) digestTypes).clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryDS</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDS</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!super.equals(aObject)) {
			return false;
		}

		if (!(aObject instanceof EPPRegistryDS)) {
			return false;
		}

		EPPRegistryDS theComp = (EPPRegistryDS) aObject;

		if (!((digestTypes == null) ? (theComp.digestTypes == null) : EPPUtil
				.equalLists(digestTypes, theComp.digestTypes))) {
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
	 * Get digest types.
	 * 
	 * @return {@code List} of digest type in {@code String}
	 */
	public List getDigestTypes() {
		return digestTypes;
	}

	/**
	 * Set digest types.
	 * 
	 * @param digestTypes
	 *            {@code List} of digest type in {@code String}
	 */
	public void setDigestTypes(List digestTypes) {
		this.digestTypes = digestTypes;
	}

	/**
	 * Add one digest type to an existing list.
	 * 
	 * @param digestType
	 *            digest type in {@code String}
	 */
	public void addDigestType(String digestType) {
		if (this.digestTypes == null) {
			this.digestTypes = new ArrayList();
		}

		this.digestTypes.add(digestType);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String getRootName() {
		return ELM_NAME;
	}
}
