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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the Key Data Interface, as defined in RFC 5910, policies. The
 * &lt;registry:keyDataInterface&gt; element contains the following child
 * elements: <br>
 * <br>
 * <ul>
 * <li>&lt;registry:min&gt; - the minimum number of keys associated with the
 * domain object. Use {@link #getMin()} and {@link #setMin(Integer)} to get and
 * set the element.</li>
 * <li>&lt;registry:max&gt; - the maximum number of keys associated with the
 * domain object. Use {@link #getMax()} and {@link #setMax(Integer)} to get and
 * set the element.</li>
 * <li>&lt;registry:alg&gt; - Zero or more &lt;registry:alg&gt; elements that
 * define the supported algorithms as described in section 2.1.3 of RFC 4034.
 * Use {@link #getAlgorithms()} and {@link #setAlgorithms(List)} to get and set
 * the element. Use {@link #addAlgorithm(String)} to add an algorithm
 * 
 * @code String} to an existing {@code List}.</li>
 *       </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDNSSEC
 */
public class EPPRegistryKey implements EPPCodecComponent {
	private static final long serialVersionUID = 7898309423292043587L;

	/** XML Element Name of <code>EPPRegistryKey</code> root element. */
	public static final String ELM_NAME = "registry:keyDataInterface";

	/** XML Element Name of <code>min</code> attribute. */
	public static final String ELM_MIN = "registry:min";

	/** XML Element Name of <code>max</code> attribute. */
	public static final String ELM_MAX = "registry:max";

	/** XML Element Name of <code>algorithms</code> attribute. */
	public static final String ALGORITHM = "registry:alg";

	/** minimum number of keys */
	private Integer min = null;

	/** maximum number of keys */
	private Integer max = null;

	/** {@code List} of algorithms in {@code String} */
	private List algorithms = new ArrayList();

	/**
	 * Default constructor. Must call {@link #setMin(Integer)} and
	 * {@link #setMax(Integer)} before calling {@link #encode(Document)} method.
	 */
	public EPPRegistryKey() {
	}

	/**
	 * Constructs an instance with {@code min}, {@code max} and {@code List} of
	 * {@code algorithms}.
	 * 
	 * @param min
	 *            minimum number of keys associated with the domain object
	 * @param max
	 *            maximum number of keys associated with the domain object
	 * @param algorithms
	 *            supported algorithms as described in section 2.1.3 of RFC 4034
	 */
	public EPPRegistryKey(Integer min, Integer max, List algorithms) {
		this.min = min;
		this.max = max;
		this.algorithms = algorithms;
	}

	/**
	 * Constructs an instance with {@code min}, {@code max} and {@code List} of
	 * {@code algorithms}.
	 * 
	 * @param min
	 *            minimum number of keys associated with the domain object
	 * @param max
	 *            maximum number of keys associated with the domain object
	 * @param algorithms
	 *            supported algorithms as described in section 2.1.3 of RFC 4034
	 */
	public EPPRegistryKey(int min, int max, List algorithms) {
		this(new Integer(min), new Integer(max), algorithms);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryKey} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryKey}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryKey} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryKey.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				getRootName());
		EPPUtil.encodeString(aDocument, root, min.toString(),
				EPPRegistryMapFactory.NS, ELM_MIN);
		EPPUtil.encodeString(aDocument, root, max.toString(),
				EPPRegistryMapFactory.NS, ELM_MAX);
		if (algorithms != null && algorithms.size() > 0) {
			EPPUtil.encodeList(aDocument, root, algorithms,
					EPPRegistryMapFactory.NS, ALGORITHM);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryKey} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryKey} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		min = EPPUtil
				.decodeInteger(aElement, EPPRegistryMapFactory.NS, ELM_MIN);
		max = EPPUtil
				.decodeInteger(aElement, EPPRegistryMapFactory.NS, ELM_MAX);
		algorithms = EPPUtil.decodeList(aElement, EPPRegistryMapFactory.NS,
				ALGORITHM);
	}

	/**
	 * Validate the state of the <code>EPPRegistryKey</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (min == null || min.intValue() < 0) {
			throw new EPPCodecException(
					"min is required and should be greater than or equal to 0");
		}
		if (max == null || max.intValue() < min.intValue()) {
			throw new EPPCodecException(
					"max is required and should be greater than or equal to min");
		}
	}

	/**
	 * Clone <code>EPPRegistryKey</code>.
	 * 
	 * @return clone of <code>EPPRegistryKey</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryKey clone = (EPPRegistryKey) super.clone();
		if (algorithms != null) {
			clone.algorithms = (List) ((ArrayList) algorithms).clone();
		}
		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryKey</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryKey</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryKey)) {
			return false;
		}

		EPPRegistryKey theComp = (EPPRegistryKey) aObject;

		if (!((min == null) ? (theComp.min == null) : min.equals(theComp.min))) {
			return false;
		}
		if (!((max == null) ? (theComp.max == null) : max.equals(theComp.max))) {
			return false;
		}
		if (!((algorithms == null) ? (theComp.algorithms == null) : EPPUtil
				.equalLists(algorithms, theComp.algorithms))) {
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
	 * Get the minimum number of keys.
	 * 
	 * @return minimum number of keys associated with the domain object.
	 */
	public Integer getMin() {
		return min;
	}

	/**
	 * Set the minimum number of keys.
	 * 
	 * @param min
	 *            minimum number of keys associated with the domain object.
	 */
	public void setMin(Integer min) {
		this.min = min;
	}

	/**
	 * Get the maximum number of keys.
	 * 
	 * @return maximum number of keys associated with the domain object.
	 */
	public Integer getMax() {
		return max;
	}

	/**
	 * Set the maximum number of keys.
	 * 
	 * @param max
	 *            maximum number of keys associated with the domain object.
	 */
	public void setMax(Integer max) {
		this.max = max;
	}

	/**
	 * Get the supported algorithms.
	 * 
	 * @return {@code List} of supported algorithms as dscsribed in section
	 *         2.1.3 of RFC 4034
	 */
	public List getAlgorithms() {
		return algorithms;
	}

	/**
	 * Set the supported algorithms.
	 * 
	 * @param algorithms
	 *            {@code List} of supported algorithms as dscsribed in section
	 *            2.1.3 of RFC 4034
	 */
	public void setAlgorithms(List algorithms) {
		this.algorithms = algorithms;
	}

	/**
	 * Add one algorithm to existing supported algorithms.
	 * 
	 * @param altorithm
	 *            supported algorithms as dscsribed in section 2.1.3 of RFC 4034
	 */
	public void addAlgorithm(String altorithm) {
		if (this.algorithms == null) {
			this.algorithms = new ArrayList();
		}
		this.algorithms.add(altorithm);
	}

	/**
	 * Get the root element tag name.
	 * 
	 * @return {@link #ELM_NAME}
	 */
	public String getRootName() {
		return ELM_NAME;
	}
}
