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

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Defines the policies for external hosts. The &lt;registry:external&gt;
 * elements contains the following child elements: <br>
 * <ul>
 * <li>
 * &lt;registry:minIP&gt; - Minimum number of IP addresses supported for an
 * external host.</li>
 * <li>&lt;registry:maxIP&gt; - Maximum number of IP addresses supported for an
 * external host.</li>
 * <li>&lt;registry:sharePolicy&gt; - The OPTIONAL policy for the sharing of
 * external hosts in the server.The possible shared policy values include:
 * <ul>
 * <li>"perRegistrar" - The external hosts are shared across domains sponsored
 * by an individual registrar client. Each registrar client will have its own
 * set of external hosts to manage.</li>
 * <li>"perZone" - The external hosts are shared across all domains of the zone.
 * There is a single pool of external hosts defined for the zone.</li>
 * <li>"perSystem" - The external hosts are shared across all zones of the
 * system. There is a single pool of external hosts across all of the zones
 * supported by the system.</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryHost
 */
public class EPPRegistryExternalHost extends EPPRegistryMinMaxLength {
	private static final long serialVersionUID = -2511130498112734292L;

	/**
	 * Constant for the status local name
	 */
	public static final String ELM_LOCALNAME = "external";

	/**
	 * Constant for the external host (prefix and local name)
	 */
	public static final String ELM_NAME = EPPRegistryMapFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	public static final String ELM_SHARE_POLICY = "sharePolicy";

	/** {@code List} of valid share policies */
	public static final List VALID_POLICIES;

	/** Constant for perRegistrar policy */
	public static final String TYPE_PER_REGISTRAR = "perRegistrar";

	/** Constant for perZone policy */
	public static final String TYPE_PER_ZONE = "perZone";

	/** Constant for perSystem policy */
	public static final String TYPE_PER_SYSTEM = "perSystem";

	static {
		VALID_POLICIES = new ArrayList();
		VALID_POLICIES.add(TYPE_PER_REGISTRAR);
		VALID_POLICIES.add(TYPE_PER_ZONE);
		VALID_POLICIES.add(TYPE_PER_SYSTEM);
	}

	/** Share policy attribute */
	private String sharePolicy = null;

	/**
	 * Default constructor
	 */
	public EPPRegistryExternalHost() {
		this.rootName = ELM_NAME;
		this.elmMin = "registry:minIP";
		this.elmMax = "registry:maxIP";
	}

	/**
	 * Constructor that takes {@code min}, {@code max} and {@code sharePolicy}.
	 * 
	 * @param min
	 *            minimum number of IPs supported for an external host
	 * @param max
	 *            maximum number of IPs supported for an external host
	 * @param sharePolicy
	 *            "perRegistrar", "perZone" or "perSystem"
	 */
	public EPPRegistryExternalHost(Integer min, Integer max, String sharePolicy) {
		this();
		this.min = min;
		this.max = max;
		this.sharePolicy = sharePolicy;
	}

	/**
	 * Constructor that takes {@code min}, {@code max} and {@code sharePolicy}.
	 * 
	 * @param min
	 *            minimum number of IPs supported for an external host
	 * @param max
	 *            maximum number of IPs supported for an external host
	 * @param sharePolicy
	 *            "perRegistrar", "perZone" or "perSystem"
	 */
	public EPPRegistryExternalHost(int min, int max, String sharePolicy) {
		this(new Integer(min), new Integer(max), sharePolicy);
	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryExternalHost} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the
	 *         {@code EPPRegistryExternalHost} instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryExternalHost}
	 *                instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		if (this.sharePolicy != null
				&& !VALID_POLICIES.contains(this.sharePolicy)) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryExternalHost.encode: "
							+ "Invalid sharePolicy value. "
							+ "Valid values are: perRegistrar/perZone/perSystem");
		}
		Element root = super.encode(aDocument);

		EPPUtil.encodeString(aDocument, root, sharePolicy,
				EPPRegistryMapFactory.NS, EPPRegistryMapFactory.NS_PREFIX + ":"
						+ ELM_SHARE_POLICY);

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryExternalHost} attributes from the aElement
	 * DOM Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryExternalHost}
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		super.decode(aElement);
		sharePolicy = EPPUtil.decodeString(aElement, EPPRegistryMapFactory.NS,
				ELM_SHARE_POLICY);
	}

	/**
	 * Clone <code>EPPRegistryExternalHost</code>.
	 * 
	 * @return clone of <code>EPPRegistryExternalHost</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		return (EPPRegistryExternalHost) super.clone();
	}

	/**
	 * implements a deep <code>EPPRegistryExternalHost</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryExternalHost</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!super.equals(aObject)) {
			return false;
		}

		if (!(aObject instanceof EPPRegistryExternalHost)) {
			return false;
		}

		EPPRegistryExternalHost theComp = (EPPRegistryExternalHost) aObject;

		if (!((sharePolicy == null) ? (theComp.sharePolicy == null)
				: sharePolicy.equals(theComp.sharePolicy))) {
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
	 * Gets the shared policy.
	 * 
	 * @return shared policy {@code String}
	 */
	public String getSharePolicy() {
		return sharePolicy;
	}

	/**
	 * Sets the shared policy.
	 * 
	 * @param sharePolicy
	 *            shared policy {@code String}
	 */
	public void setSharePolicy(String sharePolicy) {
		this.sharePolicy = sharePolicy;
	}

	/**
	 * Validate the state of the <code>EPPRegistryExternalHost</code> instance.
	 * A valid state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
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
	}

}
