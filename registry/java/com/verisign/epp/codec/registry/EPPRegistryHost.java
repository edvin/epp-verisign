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
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;

/**
 * Represents the host object policy information per RFC 5732. The
 * <registry:host> element contains the following child elements:<br>
 * <br>
 * <ul>
 * <li>
 * &lt;registry:internal&gt; - Defines the minimum and maximum number of IP
 * addresses supported for an internal host. Use {@link #getInternal()} and
 * {@link #setInternal(EPPRegistryInternalHost)} to get and set the element.</li>
 * <li>&lt;registry:external&gt; - Defines the policies for external hosts. Use
 * {@link #getExternal()} and {@link #setExternal(EPPRegistryExternalHost)} to
 * get and set the element.</li>
 * <li>&lt;registry:nameRegex&gt; - Zero or more <registry:nameRegex> elements
 * that define the regular expressions used to validate the host name value. Use
 * {@link #getNameRegex()} and {@link #setNameRegex(List)} to get and set the
 * element. Use {@link #addNameRegex(EPPRegistryRegex)} to add one name regex to
 * an existing list.</li>
 * <li>&lt;registry:maxCheckHost&gt; - The maximum number of host names
 * (&lt;domain:name&gt; elements) that can be included in a host check command
 * defined in RFC 5732. Use {@link #getMaxCheckHost()} and
 * {@link #setMaxCheckHost(Integer)} to get and set the element.</li>
 * <li>&lt;registry:supportedStatus&gt; - The OPTIONAL set of supported host
 * statuses defined in RFC 5732. Use {@link #getSupportedStatus()} and
 * {@link #setSupportedStatus(EPPRegistrySupportedStatus)} to get and set the
 * element.</li>
 * <li>&lt;registry:customData&gt; - The OPTIONAL set of custom data using key,
 * value pairs.</li>
 * </ul>
 * 
 * @author ljia
 * @version 1.4
 * 
 * @see com.verisign.epp.codec.registry.EPPRegistryInternalHost
 * @see com.verisign.epp.codec.registry.EPPRegistryExternalHost
 * @see com.verisign.epp.codec.registry.EPPRegistryRegex
 * @see com.verisign.epp.codec.registry.EPPRegistrySupportedStatus
 */

public class EPPRegistryHost implements EPPCodecComponent {
	private static final long serialVersionUID = 3439673342895779324L;

	/** XML Element Name of <code>EPPRegistryHost</code> root element. */
	public static final String ELM_NAME = "registry:host";

	/** XML Element Name of <code>nameRegex</code> attribute. */
	public static final String ELM_REGEX = "nameRegex";
	/** XML Element Name of <code>maxCheckHost</code> attribute. */
	public static final String ELM_MAX_CHECK = "registry:maxCheckHost";
	/** XML Element Name of <code>customData</code> attribute. */
	public final static String ELM_CUSTOM_DATA = "registry:customData";
	/** XML Element Name of <code>authInfoRegex</code> attribute. */
	public final static String ELM_AUTH_INFO_REGEX = "registry:authInfoRegex";

	/** Defines the internal host attributes */
	private EPPRegistryInternalHost internal = null;
	/** Defines the external host attributes */
	private EPPRegistryExternalHost external = null;
	/**
	 * {@code List} of {@link EPPRegistryRegex} that defines host name regular
	 * expression.
	 */
	private List nameRegex = new ArrayList();
	/**
	 * The maximum number of host names (<domain:name> elements) that can be
	 * included in a host check command defined in RFC 5732
	 */
	private Integer maxCheckHost = null;
	/** Set of supported host statuses defined in RFC 5732 */
	private EPPRegistrySupportedStatus supportedStatus = null;
	/** Set of custom data using key, value pairs */
	private EPPRegistryCustomData customData = null;

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * {@code EPPRegistryHost} instance.
	 * 
	 * @param aDocument
	 *            DOM Document that is being built. Used as an Element factory.
	 * 
	 * @return Element Root DOM Element representing the {@code EPPRegistryHost}
	 *         instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode {@code EPPRegistryHost} instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		try {
			validateState();
		} catch (EPPCodecException e) {
			throw new EPPEncodeException(
					"Invalid state on EPPRegistryHost.encode: " + e);
		}
		Element root = aDocument.createElementNS(EPPRegistryMapFactory.NS,
				ELM_NAME);

		EPPUtil.encodeComp(aDocument, root, internal);
		EPPUtil.encodeComp(aDocument, root, external);
		if (nameRegex != null && nameRegex.size() > 0) {
			EPPUtil.encodeCompList(aDocument, root, nameRegex);
		}
		EPPUtil.encodeString(aDocument, root, maxCheckHost.toString(),
				EPPRegistryMapFactory.NS, ELM_MAX_CHECK);
		if (supportedStatus != null) {
			EPPUtil.encodeComp(aDocument, root, supportedStatus);
		}
		if (customData != null) {
			EPPUtil.encodeComp(aDocument, root, customData);
		}

		return root;
	}

	/**
	 * Decode the {@code EPPRegistryHost} attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *            Root DOM Element to decode {@code EPPRegistryHost} from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		internal = (EPPRegistryInternalHost) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryInternalHost.ELM_NAME,
				EPPRegistryInternalHost.class);
		external = (EPPRegistryExternalHost) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryExternalHost.ELM_NAME,
				EPPRegistryExternalHost.class);
		this.setNameRegex(EPPUtil.decodeCompList(aElement,
				EPPRegistryMapFactory.NS, ELM_REGEX, EPPRegistryRegex.class));
		maxCheckHost = EPPUtil.decodeInteger(aElement,
				EPPRegistryMapFactory.NS, ELM_MAX_CHECK);
		supportedStatus = (EPPRegistrySupportedStatus) EPPUtil.decodeComp(
				aElement, EPPRegistryMapFactory.NS,
				EPPRegistrySupportedStatus.ELM_NAME,
				EPPRegistrySupportedStatus.class);
		customData = (EPPRegistryCustomData) EPPUtil.decodeComp(aElement,
				EPPRegistryMapFactory.NS, EPPRegistryCustomData.ELM_NAME,
				EPPRegistryCustomData.class);
	}

	/**
	 * Validate the state of the <code>EPPRegistryHost</code> instance. A valid
	 * state means that all of the required attributes have been set. If
	 * validateState returns without an exception, the state is valid. If the
	 * state is not valid, the EPPCodecException will contain a description of
	 * the error. throws EPPCodecException State error. This will contain the
	 * name of the attribute that is not valid.
	 * 
	 * @throws EPPCodecException
	 */
	void validateState() throws EPPCodecException {
		if (internal == null) {
			throw new EPPCodecException("internal required element is not set");
		}
		if (external == null) {
			throw new EPPCodecException("external required element is not set");
		}
		if (maxCheckHost == null || maxCheckHost.intValue() <= 0) {
			throw new EPPCodecException(
					"maxCheckHost is required and should be greater than 0");
		}
	}

	/**
	 * Clone <code>EPPRegistryHost</code>.
	 * 
	 * @return clone of <code>EPPRegistryHost</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPRegistryHost clone = (EPPRegistryHost) super.clone();

		if (internal != null) {
			clone.internal = (EPPRegistryInternalHost) internal.clone();
		}

		if (external != null) {
			clone.external = (EPPRegistryExternalHost) external.clone();
		}

		if (nameRegex != null) {
			clone.nameRegex = (List) ((ArrayList) nameRegex).clone();
		}

		if (supportedStatus != null) {
			clone.supportedStatus = (EPPRegistrySupportedStatus) supportedStatus
					.clone();
		}

		if (customData != null) {
			clone.customData = (EPPRegistryCustomData) customData.clone();
		}

		return clone;
	}

	/**
	 * implements a deep <code>EPPRegistryHost</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPRegistryHost</code> instance to compare with
	 * 
	 * @return {@code true} if this object is the same as the aObject argument;
	 *         {@code false} otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPRegistryHost)) {
			return false;
		}

		EPPRegistryHost theComp = (EPPRegistryHost) aObject;

		if (!((internal == null) ? (theComp.internal == null) : internal
				.equals(theComp.internal))) {
			return false;
		}
		if (!((external == null) ? (theComp.external == null) : external
				.equals(theComp.external))) {
			return false;
		}
		if (!((nameRegex == null) ? (theComp.nameRegex == null) : EPPUtil
				.equalLists(nameRegex, theComp.nameRegex))) {
			return false;
		}
		if (!((maxCheckHost == null) ? (theComp.maxCheckHost == null)
				: maxCheckHost.equals(theComp.maxCheckHost))) {
			return false;
		}
		if (!((supportedStatus == null) ? (theComp.supportedStatus == null)
				: supportedStatus.equals(theComp.supportedStatus))) {
			return false;
		}
		if (!((customData == null) ? (theComp.customData == null) : customData
				.equals(theComp.customData))) {
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
	 * Gets the internal host attributes.
	 * 
	 * @return the internal host attributes
	 */
	public EPPRegistryInternalHost getInternal() {
		return internal;
	}

	/**
	 * Sets the internal host attributes.
	 * 
	 * @param internal
	 *            the internal host attributes
	 */
	public void setInternal(EPPRegistryInternalHost internal) {
		this.internal = internal;
	}

	/**
	 * Gets the external host attributes.
	 * 
	 * @return the external host attributes
	 */
	public EPPRegistryExternalHost getExternal() {
		return external;
	}

	/**
	 * Sets the external host attributes.
	 * 
	 * @param external
	 *            the external host attributes
	 */
	public void setExternal(EPPRegistryExternalHost external) {
		this.external = external;
	}

	/**
	 * Gets host name regular expressions.
	 * 
	 * @return {@code List} of name regular expressions
	 */
	public List getNameRegex() {
		return nameRegex;
	}

	/**
	 * Sets host name regular expressions.
	 * 
	 * @param nameRegex
	 *            {@code List} of name regular expressions
	 */
	public void setNameRegex(List nameRegex) {
		if (nameRegex != null) {
			for (Iterator it = nameRegex.iterator(); it.hasNext();) {
				EPPRegistryRegex regex = (EPPRegistryRegex) it.next();
				if (regex != null) {
					regex.setRootName(ELM_REGEX);
				}
			}
		}
		this.nameRegex = nameRegex;
	}

	/**
	 * Adds one host name regular expression to the existing {@code List}.
	 * 
	 * @param regex
	 *            one name regular expressions
	 */
	public void addNameRegex(EPPRegistryRegex regex) {
		if (regex == null) {
			return;
		}
		regex.setRootName(ELM_REGEX);
		if (this.nameRegex == null) {
			this.nameRegex = new ArrayList();
		}
		this.nameRegex.add(regex);
	}

	/**
	 * Gets maximum number of host names that can be included in a host check
	 * command.
	 * 
	 * @return maximum number of host names that can be included in a host check
	 *         command
	 */
	public Integer getMaxCheckHost() {
		return maxCheckHost;
	}

	/**
	 * Sets maximum number of host names that can be included in a host check
	 * command.
	 * 
	 * @param maxCheckHost
	 *            maximum number of host names that can be included in a host
	 *            check command
	 */
	public void setMaxCheckHost(Integer maxCheckHost) {
		this.maxCheckHost = maxCheckHost;
	}

	/**
	 * Gets set of custom data using key, value pairs.
	 * 
	 * @return instance of {@link EPPRegistryCustomData} that gives users the
	 *         ability to specify custom data with key/value pairs
	 */
	public EPPRegistryCustomData getCustomData() {
		return customData;
	}

	/**
	 * Sets set of custom data using key, value pairs.
	 * 
	 * @param customData
	 *            instance of {@link EPPRegistryCustomData} that gives users the
	 *            ability to specify custom data with key/value pairs
	 */
	public void setCustomData(EPPRegistryCustomData customData) {
		this.customData = customData;
	}

	/**
	 * Gets a set of supported host statuses defined in RFC 5732.
	 * 
	 * @return set of supported host statuses defined in RFC 5732
	 */
	public EPPRegistrySupportedStatus getSupportedStatus() {
		return supportedStatus;
	}

	/**
	 * Sets a set of supported host statuses defined in RFC 5732.
	 * 
	 * @param supportedStatus
	 *            set of supported host statuses defined in RFC 5732
	 */
	public void setSupportedStatus(EPPRegistrySupportedStatus supportedStatus) {
		this.supportedStatus = supportedStatus;
	}
}
