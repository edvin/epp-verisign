/***********************************************************
Copyright (C) 2012 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 ***********************************************************/
package com.verisign.epp.codec.launch;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Launch application status constants that are returned by
 * {@link EPPLaunchInfData#getStatus()} or set with
 * {@link EPPLaunchInfData#setStatus(EPPLaunchStatus)}.
 */
public class EPPLaunchStatus implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchStatus.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * The initial state of a newly-created application object. The application
	 * requires validation, but the validation process has not yet completed.
	 */
	public static final String STATUS_PENDING_VALIDATION = "pendingValidation";

	/**
	 * The application meets relevant registry rules.
	 */
	public static final String STATUS_VALIDATED = "validated";

	/**
	 * The application does not validate according to registry rules.
	 */
	public static final String STATUS_INVALID = "invalid";

	/**
	 * The allocation of the application is pending based on the results of some
	 * out-of-band process (for example, an auction).
	 */
	public static final String STATUS_PENDING_ALLOCATION = "pendingAllocation";

	/**
	 * One of two possible end states of an application object; the object
	 * corresponding to the application has been provisioned.
	 */
	public static final String STATUS_ALLOCATED = "allocated";

	/**
	 * The other possible end state; the object was not provisioned.
	 */
	public static final String STATUS_REJECTED = "rejected";

	/**
	 * A custom status that is defined using the "name" attribute.
	 */
	public static final String STATUS_CUSTOM = "custom";

	/**
	 * Constant for the status local name
	 */
	public static final String ELM_LOCALNAME = "status";

	/**
	 * Constant for the status qualified name (prefix and local name)
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * Status attribute key
	 */
	public static final String ATTR_STATUS = "s";

	/**
	 * OPTIONAL attribute name that can define a sub-status or the full name of
	 * the status when the &lt;launch:status&gt; element has the "custom" value.
	 */
	public static final String ATTR_NAME = "name";

	/**
	 * Launch status name using using of the <code>STATUS</code> constants.
	 */
	private String status;

	/**
	 * OPTIONAL "name" attribute that can define a sub-status or the full name
	 * of the status when the &lt;launch:status&gt; element has the "custom"
	 * value.
	 */
	private String name;

	/**
	 * Default constructor. The status value MUST be set using the
	 * {@link #setStatus(String)} method.
	 */
	public EPPLaunchStatus() {
	}

	/**
	 * Create <code>EPPLaunchStatus</code> instance with a defined status value.
	 * 
	 * @param aStatus
	 *            Status value using one of the <code>STATUS</code> constants.
	 */
	public EPPLaunchStatus(String aStatus) {
		this.status = aStatus;
	}

	/**
	 * Create <code>EPPLaunchStatus</code> instance with a defined status value
	 * and with the status name value.
	 * 
	 * @param aStatus
	 *            Status value using one of the <code>STATUS</code> constants.
	 * @param aName
	 *            Name of sub-status or full name of status when
	 *            <code>aStatus</code> is set to <code>STATUS_CUSTOM</code>.
	 */
	public EPPLaunchStatus(String aStatus, String aName) {
		this.status = aStatus;
		this.name = aName;
	}

	/**
	 * Gets the status value, which should match one of the <code>STATUS</code>
	 * constants.
	 * 
	 * @return Status value
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * Sets the status value.
	 * 
	 * @param aStatus
	 *            Status value, which should be one of the <code>STATUS</code>
	 *            constants.
	 */
	public void setStatus(String aStatus) {
		this.status = aStatus;
	}

	/**
	 * Gets the OPTIONAL status name or sub-status name.
	 * 
	 * @return The status name or sub-status name if defined; </code>null</code>
	 *         otherwise.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the OPTIONAL status name or sub-status name.
	 * 
	 * @param aName
	 *            Status name
	 */
	public void setName(String aName) {
		this.name = aName;
	}

	/**
	 * Clone <code>EPPLaunchStatus</code>.
	 * 
	 * @return clone of <code>EPPLaunchStatus</code>
	 * 
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPLaunchStatus clone = null;

		clone = (EPPLaunchStatus) super.clone();

		return clone;
	}

	/**
	 * Decode the <code>EPPLaunchStatus</code> element aElement DOM Element
	 * tree.
	 * 
	 * @param aElement
	 *            - Root DOM Element to decode <code>EPPLaunchStatus</code>
	 *            from.
	 * 
	 * @exception EPPDecodeException
	 *                Unable to decode aElement
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		// status
		this.status = aElement.getAttribute(ATTR_STATUS);

		// name
		String theName = aElement.getAttribute(ATTR_NAME);
		if (theName != null && !theName.isEmpty()) {
			this.name = theName;
		}
		else {
			this.name = null;
		}

	}

	/**
	 * Encode a DOM Element tree from the attributes of the
	 * <code>EPPLaunchStatus</code> instance.
	 * 
	 * @param aDocument
	 *            - DOM Document that is being built. Used as an Element
	 *            factory.
	 * 
	 * @return Element - Root DOM Element representing the
	 *         <code>EPPLaunchStatus</code> instance.
	 * 
	 * @exception EPPEncodeException
	 *                - Unable to encode <code>EPPLaunchStatus</code> instance.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPLaunchStatus.encode(Document)");
		}
		if (this.status == null) {
			throw new EPPEncodeException("status is null"
					+ " on in EPPLaunchStatus.encode(Document)");
		}

		// status
		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);
		root.setAttribute(ATTR_STATUS, this.status);

		// name
		if (this.name != null) {
			root.setAttribute(ATTR_NAME, this.name);
		}

		return root;
	}

	/**
	 * implements a deep <code>EPPLaunchStatus</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchStatus</code> instance to compare with
	 * 
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPLaunchStatus)) {
			return false;
		}

		EPPLaunchStatus other = (EPPLaunchStatus) aObject;

		// status
		if (!EqualityUtil.equals(this.status, other.status)) {
			cat.error("EPPLaunchStatus.equals(): status not equal");
			return false;
		}

		// name
		if (!EqualityUtil.equals(this.name, other.name)) {
			cat.error("EPPLaunchStatus.equals(): name not equal");
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

}
