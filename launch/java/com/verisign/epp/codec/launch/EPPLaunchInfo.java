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

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Extension to the domain info command to retrieve information for a launch
 * phase registration or application.
 */
public class EPPLaunchInfo implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchInfo.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the launch phase info extension local name
	 */
	public static final String ELM_LOCALNAME = "info";

	/**
	 * Constant for the launch phase info extension tag
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * XML local name for the application id
	 */
	private static final String ELM_APPLICATION_ID = "applicationID";

	/**
	 * XML Attribute name for the include mark attribute.
	 */
	private static final String ATTR_INCLUDE_MARK = "includeMark";

	/**
	 * The phase during which the application or registration was submitted or
	 * is associated with.
	 */
	EPPLaunchPhase phase;

	/**
	 * Application identifier of the launch application.
	 */
	String applicationId;

	/**
	 * Indicate whether or not to include the mark in the response with a
	 * default of <code>false</code>.
	 */
	boolean includeMark = false;

	/**
	 * Create an EPPLaunchInf instance
	 */
	public EPPLaunchInfo() {
	}

	/**
	 * Create a <code>EPPLaunchInfo</code> instance with the required phase
	 * attribute value.
	 * 
	 * @param aPhase
	 *            Phase during which the application or registration was
	 *            submitted or is associated with
	 */
	public EPPLaunchInfo(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Create a <code>EPPLaunchInfo</code> instance with both the required phase
	 * and optional application identifier attribute values.
	 * 
	 * @param aPhase
	 *            Phase during which the application or registration was
	 *            submitted or is associated with
	 * @param aApplicationId
	 *            Application identifier of the launch application
	 */
	public EPPLaunchInfo(EPPLaunchPhase aPhase, String aApplicationId) {
		this.phase = aPhase;
		this.applicationId = aApplicationId;
	}

	/**
	 * Gets phase during which the application or registration was submitted or
	 * is associated with.
	 * 
	 * @return phase during which the application or registration was submitted
	 *         or is associated with if defined; <code>null</code> otherwise.
	 */
	public EPPLaunchPhase getPhase() {
		return this.phase;
	}

	/**
	 * Sets the phase during which the application or registration was submitted
	 * or is associated with.
	 * 
	 * @param aPhase
	 *            Phase during which the application or registration was
	 *            submitted or is associated with
	 */
	public void setPhase(EPPLaunchPhase aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Sets the phase with one of the <code>EPPLaunchPhase</code>
	 * <code>PHASE</code> constants.
	 * 
	 * @param aPhaseString
	 *            One of the <code>EPPLaunchPhase</code> <code>PHASE</code>
	 *            constants.
	 */
	public void setPhase(String aPhaseString) {
		this.phase = new EPPLaunchPhase(aPhaseString);
	}

	/**
	 * Gets the application identifier of the launch application.
	 * 
	 * @return Application identifier if defined; <code>null</code> otherwise.
	 */
	public String getApplicationId() {
		return this.applicationId;
	}

	/**
	 * Sets the application identifier of the launch application.
	 * 
	 * @param aApplicationId
	 *            Application identifier of the launch application
	 */
	public void setApplicationId(String aApplicationId) {
		this.applicationId = aApplicationId;
	}

	/**
	 * Get the flag indicating whether or not to include the mark in the
	 * response.
	 * 
	 * @return <code>true</code> to include the mark; <code>false</code>
	 *         otherwise.
	 */
	public boolean isIncludeMark() {
		return this.includeMark;
	}

	/**
	 * Sets the flag for indicating whether or not to include the mark in the
	 * response.
	 * 
	 * @param aIncludeMark
	 *            <code>true</code> to include the mark; <code>false</code>
	 *            otherwise.
	 */
	public void setIncludeMark(boolean aIncludeMark) {
		this.includeMark = aIncludeMark;
	}

	/**
	 * Clone <code>EPPLaunchInfo</code>.
	 * 
	 * @return clone of <code>EPPLaunchInfo</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPLaunchInfo clone = (EPPLaunchInfo) super.clone();
		return clone;
	}

	/**
	 * Encode the <code>EPPLaunchInfo</code> to a DOM Element
	 * 
	 * @param aDocument
	 *            a DOM Document to attach data to.
	 * @return The root element of this component.
	 * 
	 * @throws EPPEncodeException
	 *             Thrown if any errors prevent encoding.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {

		if (aDocument == null) {
			throw new EPPEncodeException("aDocument is null"
					+ " on in EPPLaunchInfo.encode(Document)");
		}

		// Required phase attribute not set?
		if (this.phase == null) {
			throw new EPPEncodeException(
					"EPPLaunchInfo phase attribute must be set.");
		}

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);
		
		// Include Mark
		if (this.includeMark) {
			root.setAttribute(ATTR_INCLUDE_MARK, "1");
		}
		else {
			root.setAttribute(ATTR_INCLUDE_MARK, "0");
		}

		// Phase
		EPPUtil.encodeComp(aDocument, root, this.phase);

		// Application Id
		EPPUtil.encodeString(aDocument, root, this.applicationId,
				EPPLaunchExtFactory.NS, EPPLaunchExtFactory.NS_PREFIX + ":"
						+ ELM_APPLICATION_ID);

		return root;
	}

	/**
	 * Decode the DOM element to the <code>EPPLaunchInfo</code>.
	 * 
	 * @param aElement
	 *            DOM Element to decode the attribute values
	 * @throws EPPDecodeException
	 *             Error decoding the DOM Element
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Include Mark
		this.includeMark = false;
		String includeMarkVal = aElement.getAttribute(ATTR_INCLUDE_MARK);
		if (includeMarkVal != null && (includeMarkVal.equals("1") || 
				includeMarkVal.equalsIgnoreCase("true"))) {
			this.includeMark = true;
		}
		
		// Phase
		this.phase = (EPPLaunchPhase) EPPUtil.decodeComp(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchPhase.ELM_NAME,
				EPPLaunchPhase.class);

		if (this.phase == null) {
			throw new EPPDecodeException(
					"EPPLaunchInfo phase element not found.");
		}

		// Application Id
		this.applicationId = EPPUtil.decodeString(aElement,
				EPPLaunchExtFactory.NS, ELM_APPLICATION_ID);

	}

	/**
	 * implements a deep <code>EPPLaunchInfo</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchInfo</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPLaunchInfo)) {
			cat.error("EPPLaunchInfo.equals(): " + aObject.getClass().getName()
					+ " not EPPLaunchInfo instance");

			return false;
		}

		EPPLaunchInfo other = (EPPLaunchInfo) aObject;
		
		// Include Mark
		if (!EqualityUtil.equals(this.includeMark, other.includeMark)) {
			cat.error("EPPLaunchInfo.equals(): includeMark not equal");
			return false;
		}
		

		// Phase
		if (!EqualityUtil.equals(this.phase, other.phase)) {
			cat.error("EPPLaunchInfo.equals(): phase not equal");
			return false;
		}

		// Application Id
		if (!EqualityUtil.equals(this.applicationId, other.applicationId)) {
			cat.error("EPPLaunchInfo.equals(): applicationId not equal");
			return false;
		}

		return true;
	}

}