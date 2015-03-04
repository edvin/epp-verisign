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

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the DNS Security Extensions (DNSSEC) policies for the server. The
 * &lt;registry:dnssec&gt; element contains the following child elements: <br>
 * <br>
 * <ul>
 * <li>
 * &lt;registry:dsDataInterface&gt; - defines the DS Data Interface, as defined
 * in RFC 5910, policies. Use {@link #getDs()} and {@link #setDs(EPPRegistryDS)}
 * to get and set this element.</li>
 * <li>
 * &lt;registry:keyDataInterface&gt; - defines the Key Data Interface, as
 * defined in RFC 5910, policies. Use {@link #getKey()} and
 * {@link #setKey(EPPRegistryKey)} to get and set this element.</li>
 * <li>
 * &lt;registry:maxSigLife&gt; - defines the maximum signature life policies.
 * Use {@link #getMaxSigLife()} and {@link #setMaxSigLife(EPPRegistryMaxSig)} to
 * get and set this element.</li>
 * <li>
 * &lt;registry:urgent&gt; - whether the client can specify the urgent attribute
 * for DNSSEC updates with a default value of {@code false}.. Use {@link #getUrgent()}
 * and {@link #setUrgent(Boolean)} to get and set this element.</li>
 * </ul>
 * 
 * In one &lt;registry:dnssec&gt; element, only one of
 * &lt;registry:dsDataInterface&gt; or &lt;registry:keyDataInterface&gt; may
 * exist. <br>
 * <br>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryDomain
 * @see com.verisign.epp.codec.registry.EPPRegistryDS
 * @see com.verisign.epp.codec.registry.EPPRegistryKey
 * @see com.verisign.epp.codec.registry.EPPRegistryMaxSig
 */
public class EPPRegistryDNSSEC implements EPPCodecComponent {
	private static final long serialVersionUID = -8343531131323431532L;

	/** XML Element Name of <code>EPPRegistryDNSSEC</code> root element. */
	public static final String ELM_NAME = "registry:dnssec";

	/** XML tag name for the <code>urgent</code> attribute. */
	public static final String ELM_URGENT = "registry:urgent";

	/** attributes for DS Data interface */
	private EPPRegistryDS ds = null;

	/** attributes for Key Data interface */
	private EPPRegistryKey key = null;

	/** attributes for max signature life */
	private EPPRegistryMaxSig maxSigLife = null;

	/**
	 * Whether client can specify the urgent attribute for DNSSEC update.
	 * Default value is {@code false}.
	 */
	private Boolean urgent = Boolean.FALSE;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryDNSSEC} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryDNSSEC} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryDNSSEC} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryDNSSEC.encode: " + e);
		}

		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);
		if (ds != null) {
			EPPUtil.encodeComp(aDocument, root, ds);
		} else {
			EPPUtil.encodeComp(aDocument, root, key);
		}
		EPPUtil.encodeComp(aDocument, root, maxSigLife);

		if (urgent == null) {
			urgent = Boolean.FALSE;
		}
		EPPUtil.encodeString(aDocument, root, urgent.toString(),
				EPPRegistryMapFactory.NS, ELM_URGENT);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryDNSSEC} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryDNSSEC} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		ds = (EPPRegistryDS) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryDS.ELM_NAME,
				EPPRegistryDS.class);
		key = (EPPRegistryKey) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryKey.ELM_NAME,
				EPPRegistryKey.class);
		maxSigLife = (EPPRegistryMaxSig) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryMaxSig.ELM_NAME,
				EPPRegistryMaxSig.class);

		urgent = EPPUtil.decodeBoolean(aElement, EPPRegistryMapFactory.NS,
				ELM_URGENT);
		if (urgent == null) {
			urgent = Boolean.FALSE;
		}
	}

	/**
	 * Validate the state of the <code>EPPRegistryDNSSEC</code> instance. A
	 * valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if ((ds == null && key == null) || (ds != null && key != null)) {
			throw new EPPCodecException(
					"only one of dsDataInterface or keyDataInterface element is required");
		}

		if (maxSigLife == null) {
			throw new EPPCodecException("maxSigLife element is not set");
		}
	}

	/**
	 * Clone <code>EPPRegistryDNSSEC</code>.
	 * 
	 * @return clone of <code>EPPRegistryDNSSEC</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryDNSSEC clone = (EPPRegistryDNSSEC) super.clone();

		if (ds != null) {
			clone.ds = (EPPRegistryDS) ds.clone();
		}

		if (key != null) {
			clone.key = (EPPRegistryKey) key.clone();
		}

		if (maxSigLife != null) {
			clone.maxSigLife = (EPPRegistryMaxSig) maxSigLife.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryDNSSEC</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryDNSSEC</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryDNSSEC)) {
			return false;
		}

		EPPRegistryDNSSEC theComp = (EPPRegistryDNSSEC) aObject;

		if (!((ds == null) ? (theComp.ds == null) : ds.equals(theComp.ds))) {
			return false;
		}

		if (!((key == null) ? (theComp.key == null) : key.equals(theComp.key))) {
			return false;
		}

		if (!((maxSigLife == null) ? (theComp.maxSigLife == null) : maxSigLife
				.equals(theComp.maxSigLife))) {
			return false;
		}

		if (!((urgent == null) ? (theComp.urgent == null) : urgent
				.equals(theComp.urgent))) {
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
	 * Get DS Data interface attributes.
	 * 
	 * @return instance of {@link EPPRegistryDS}
	 */
	public EPPRegistryDS getDs() {
		return ds;
	}

	/**
	 * Set DS Data interface attributes.
	 * 
	 * @param ds
	 *            instance of {@link EPPRegistryDS}
	 */
	public void setDs(EPPRegistryDS ds) {
		this.ds = ds;
	}

	/**
	 * Get Key Data interface attributes.
	 * 
	 * @return instance of {@link EPPRegistryKey}
	 */
	public EPPRegistryKey getKey() {
		return key;
	}

	/**
	 * Set Key Data interface attributes.
	 * 
	 * @param key
	 *            instance of {@link EPPRegistryKey}
	 */
	public void setKey(EPPRegistryKey key) {
		this.key = key;
	}

	/**
	 * Get max signature life policy.
	 * 
	 * @return instance of {@link EPPRegistryMaxSig}
	 */
	public EPPRegistryMaxSig getMaxSigLife() {
		return maxSigLife;
	}

	/**
	 * Set max signature life policy.
	 * 
	 * @param maxSigLife
	 *            instance of {@link EPPRegistryMaxSig}
	 */
	public void setMaxSigLife(EPPRegistryMaxSig maxSigLife) {
		this.maxSigLife = maxSigLife;
	}

	/**
	 * Get the urgent flag.
	 * 
	 * @return {@code true} if the client can specify the urgent attribute for
	 *         DNSSEC updates. {@code false} if the client CANNOT specify the
	 *         urgent attribute for DNSSEC updates.
	 */
	public Boolean getUrgent() {
		return urgent;
	}

	/**
	 * Set the urgent flag.
	 * 
	 * @param urgent
	 *            {@code true} if the client can specify the urgent attribute
	 *            for DNSSEC updates. {@code false} if the client CANNOT specify
	 *            the urgent attribute for DNSSEC updates.
	 */
	public void setUrgent(Boolean urgent) {
		this.urgent = urgent;
	}
}
