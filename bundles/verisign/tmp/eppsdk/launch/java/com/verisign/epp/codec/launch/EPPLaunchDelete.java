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
 * Extension to the domain delete command to delete a launch
 * phase application.
 */
public class EPPLaunchDelete implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchDelete.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the launch phase delete extension local name
	 */
	public static final String ELM_LOCALNAME = "delete";

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
	 * The phase during which the application was submitted or
	 * is associated with.
	 */
	EPPLaunchPhase phase;

	/**
	 * Application identifier of the launch application.
	 */
	String applicationId;

	/**
	 * Create an EPPLaunchDelete instance
	 */
	public EPPLaunchDelete() {
	}


	/**
	 * Create a <code>EPPLaunchDelete</code> instance with both the required phase
	 * and required application identifier attribute values.
	 * 
	 * @param aPhase
	 *            Phase during which the application was
	 *            submitted or is associated with
	 * @param aApplicationId
	 *            Application identifier of the launch application
	 */
	public EPPLaunchDelete(EPPLaunchPhase aPhase, String aApplicationId) {
		this.phase = aPhase;
		this.applicationId = aApplicationId;
	}

	/**
	 * Gets phase during which the application was submitted or
	 * is associated with.
	 * 
	 * @return phase during which the application was submitted
	 *         or is associated with if defined; <code>null</code> otherwise.
	 */
	public EPPLaunchPhase getPhase() {
		return this.phase;
	}

	/**
	 * Sets the phase during which the application was submitted
	 * or is associated with.
	 * 
	 * @param aPhase
	 *            Phase during which the application was
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
	 * Clone <code>EPPLaunchDelete</code>.
	 * 
	 * @return clone of <code>EPPLaunchDelete</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPLaunchDelete clone = (EPPLaunchDelete) super.clone();
		return clone;
	}

	/**
	 * Encode the <code>EPPLaunchDelete</code> to a DOM Element
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
					+ " on in EPPLaunchDelete.encode(Document)");
		}

		// Required phase attribute not set?
		if (this.phase == null) {
			throw new EPPEncodeException(
					"EPPLaunchDelete phase attribute must be set.");
		}
		
		// Required applicationId attribute not set?
		if (this.applicationId == null) {
			throw new EPPEncodeException(
					"EPPLaunchDelete applicationId attribute must be set.");
		}
		

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);
		
		// Phase
		EPPUtil.encodeComp(aDocument, root, this.phase);

		// Application Id
		EPPUtil.encodeString(aDocument, root, this.applicationId,
				EPPLaunchExtFactory.NS, EPPLaunchExtFactory.NS_PREFIX + ":"
						+ ELM_APPLICATION_ID);

		return root;
	}

	/**
	 * Decode the DOM element to the <code>EPPLaunchDelete</code>.
	 * 
	 * @param aElement
	 *            DOM Element to decode the attribute values
	 * @throws EPPDecodeException
	 *             Error decoding the DOM Element
	 */
	public void decode(Element aElement) throws EPPDecodeException {

		// Phase
		this.phase = (EPPLaunchPhase) EPPUtil.decodeComp(aElement, EPPLaunchExtFactory.NS,
				EPPLaunchPhase.ELM_NAME, EPPLaunchPhase.class);

		if (this.phase == null) {
			throw new EPPDecodeException(
					"EPPLaunchDelete phase element not found.");
		}

		// Application Id
		this.applicationId = EPPUtil.decodeString(aElement,
				EPPLaunchExtFactory.NS, ELM_APPLICATION_ID);

		if (this.applicationId == null) {
			throw new EPPDecodeException(
					"EPPLaunchDelete applicationId element not found.");
		}
		
	}

	/**
	 * implements a deep <code>EPPLaunchDelete</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchDelete</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPLaunchDelete)) {
			cat.error("EPPLaunchDelete.equals(): "
					  + aObject.getClass().getName()
					  + " not EPPLaunchDelete instance");

			return false;
		}

		EPPLaunchDelete other = (EPPLaunchDelete) aObject;

		// Phase
		if (!EqualityUtil.equals(this.phase, other.phase)) {
			cat.error("EPPLaunchDelete.equals(): phase not equal");
			return false;
		}

		// Application Id
		if (!EqualityUtil.equals(this.applicationId, other.applicationId)) {
			cat.error("EPPLaunchDelete.equals(): applicationId not equal");
			return false;
		}

		return true;
	}

}