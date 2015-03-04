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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EqualityUtil;

/**
 * Extension to the domain info response to return the launch phase application
 * or registration information. The {@link EPPLaunchInfo} domain info command
 * extension defines the application or registration information to return.
 * 
 * @see com.verisign.epp.codec.launch.EPPLaunchInfo
 */
public class EPPLaunchInfData implements EPPCodecComponent {

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger(EPPLaunchInfData.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Constant for the launch phase info response extension local name
	 */
	public static final String ELM_LOCALNAME = "infData";

	/**
	 * Constant for the launch phase info response extension tag
	 */
	public static final String ELM_NAME = EPPLaunchExtFactory.NS_PREFIX + ":"
			+ ELM_LOCALNAME;

	/**
	 * XML local name for the application id
	 */
	private static final String ELM_APPLICATION_ID = "applicationID";

	/**
	 * XML local name for the status
	 */
	private static final String ELM_STATUS = "status";

	/**
	 * The phase during which the application or registration was submitted or
	 * is associated with.
	 */
	EPPLaunchPhase phase;

	/**
	 * OPTIONAL application identifier of the launch application.
	 */
	String applicationId;

	/**
	 * OPTIONAL status of the launch application using one of the supported
	 * status values
	 */
	EPPLaunchStatus status;

	/**
	 * OPTIONAL list of marks associated with launch application / registration
	 */
	List<EPPMark> marks = new ArrayList<EPPMark>();

	/**
	 * Create an <code>EPPLaunchInfData</code> instance
	 */
	public EPPLaunchInfData() {
	}

	/**
	 * Create a EPPLaunchInfData instance for a registration with the phase and
	 * list of marks.
	 * 
	 * @param aPhase
	 *            The phase during which the registration was submitted or is
	 *            associated with.
	 * @param aMarks
	 *            List of marks
	 */
	public EPPLaunchInfData(EPPLaunchPhase aPhase, List<EPPMark> aMarks) {
		this.phase = aPhase;
		this.marks = aMarks;
	}

	/**
	 * Create a EPPLaunchInfData instance for a registration with the phase and
	 * an individual mark.
	 * 
	 * @param aPhase
	 *            The phase during which the registration was submitted or is
	 *            associated with.
	 * @param aMark
	 *            An individual mark
	 */
	public EPPLaunchInfData(EPPLaunchPhase aPhase, EPPMark aMark) {
		this.phase = aPhase;
		this.setMark(aMark);
	}

	/**
	 * Create a EPPLaunchInfData instance for an application with the phase,
	 * application identifier, and application status values.
	 * 
	 * @param aPhase
	 *            The phase during which the application was submitted or is
	 *            associated with.
	 * @param aApplicationId
	 *            Application identifier of the returned application
	 * @param aStatus
	 *            Status of the launch application
	 */
	public EPPLaunchInfData(EPPLaunchPhase aPhase, String aApplicationId,
			EPPLaunchStatus aStatus) {
		this.phase = aPhase;
		this.applicationId = aApplicationId;
		this.status = aStatus;
	}

	/**
	 * Create a EPPLaunchInfData instance for an application with the phase,
	 * application identifier, application status, and list of marks values.
	 * 
	 * @param aPhase
	 *            The phase during which the application was submitted or is
	 *            associated with.
	 * @param aApplicationId
	 *            Application identifier of the returned application
	 * @param aStatus
	 *            Status of the launch application
	 * @param aMarks
	 *            List of marks
	 */
	public EPPLaunchInfData(EPPLaunchPhase aPhase, String aApplicationId,
			EPPLaunchStatus aStatus, List<EPPMark> aMarks) {
		this.phase = aPhase;
		this.applicationId = aApplicationId;
		this.status = aStatus;
		this.marks = aMarks;
	}

	/**
	 * Create a EPPLaunchInfData instance for an application with the phase,
	 * application identifier, application status, and an individual of mark
	 * values.
	 * 
	 * @param aPhase
	 *            The phase during which the application was submitted or is
	 *            associated with.
	 * @param aApplicationId
	 *            Application identifier of the returned application
	 * @param aStatus
	 *            Status of the launch application
	 * @param aMark
	 *            An individual mark
	 */
	public EPPLaunchInfData(EPPLaunchPhase aPhase, String aApplicationId,
			EPPLaunchStatus aStatus, EPPMark aMark) {
		this.phase = aPhase;
		this.applicationId = aApplicationId;
		this.status = aStatus;
		this.setMark(aMark);
	}

	/**
	 * Clone <code>EPPLaunchInfData</code>.
	 * 
	 * @return clone of <code>EPPLaunchInfData</code>
	 * @exception CloneNotSupportedException
	 *                standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {

		EPPLaunchInfData clone = (EPPLaunchInfData) super.clone();

		// Application Id
		clone.applicationId = this.applicationId;

		// Phase
		if (this.phase == null) {
			clone.phase = null;
		}
		else {
			clone.phase = (EPPLaunchPhase) this.phase.clone();
		}

		// Status
		if (this.status == null) {
			clone.status = null;
		}
		else {
			clone.status = (EPPLaunchStatus) this.status.clone();
		}

		// Claims
		if (this.marks != null) {
			clone.marks = new ArrayList(this.marks);
		}

		return clone;
	}

	/**
	 * Sets all this instance's data in the given XML document
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
					+ " on in EPPLaunchInfData.encode(Document)");
		}

		// Required phase attribute not set?
		if (this.phase == null) {
			throw new EPPEncodeException(
					"EPPLaunchInfData phase attribute must be set.");
		}

		Element root = aDocument.createElementNS(EPPLaunchExtFactory.NS,
				ELM_NAME);

		// Phase
		EPPUtil.encodeComp(aDocument, root, this.phase);

		// Application Id
		EPPUtil.encodeString(aDocument, root, this.applicationId,
				EPPLaunchExtFactory.NS, EPPLaunchExtFactory.NS_PREFIX + ":"
						+ ELM_APPLICATION_ID);

		// Status
		EPPUtil.encodeComp(aDocument, root, this.status);

		// Marks
		EPPUtil.encodeCompList(aDocument, root, this.marks);

		return root;
	}

	/**
	 * Decode the EPPLaunchInfData component
	 * 
	 * @param aElement
	 * @throws EPPDecodeException
	 */
	public void decode(Element aElement) throws EPPDecodeException {

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

		// Status
		this.status = (EPPLaunchStatus) EPPUtil.decodeComp(aElement,
				EPPLaunchExtFactory.NS, EPPLaunchStatus.ELM_NAME,
				EPPLaunchStatus.class);

		// Marks
		this.marks = EPPUtil.decodeCompList(aElement, EPPMark.NS,
				EPPMark.ELM_NAME, EPPMark.class);
	}

	/**
	 * implements a deep <code>EPPLaunchInfData</code> compare.
	 * 
	 * @param aObject
	 *            <code>EPPLaunchInfData</code> instance to compare with
	 * 
	 * @return true if equal false otherwise
	 */
	public boolean equals(Object aObject) {

		if (!(aObject instanceof EPPLaunchInfData)) {
			cat.error("EPPLaunchInfData.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPLaunchInfData instance");

			return false;
		}

		EPPLaunchInfData other = (EPPLaunchInfData) aObject;

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

		// Status
		if (!EqualityUtil.equals(this.status, other.status)) {
			cat.error("EPPLaunchInfo.equals(): status not equal");
			return false;
		}

		// Marks
		if (!EPPUtil.equalLists(this.marks, other.marks)) {
			cat.error("EPPLaunchInfo.equals(): marks not equal");

			return false;
		}

		return true;
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
	 * Gets the application status.
	 * 
	 * @return Launch status if defined; <code>null</code> otherwise.
	 */
	public EPPLaunchStatus getStatus() {
		return this.status;
	}

	/**
	 * Sets the application status.
	 * 
	 * @param aStatus
	 *            The application status
	 */
	public void setStatus(EPPLaunchStatus aStatus) {
		this.status = aStatus;
	}
	
	/**
	 * Sets the status with one of the <code>EPPLaunchStatus</code>
	 * <code>STATUS</code> constants.
	 * 
	 * @param aStatusString
	 *            One of the <code>EPPLaunchStatus</code> <code>STATUS</code>
	 *            constants.
	 */
	public void setStatus(String aStatusString) {
		if (aStatusString == null) {
			this.status = null;
		}
		else {
			this.status = new EPPLaunchStatus(aStatusString);
		}
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
	 * Sets an individual mark.
	 * 
	 * @param aMark
	 *            An individual mark
	 */
	public void setMark(EPPMark aMark) {
		this.marks = new ArrayList<EPPMark>();
		this.marks.add(aMark);
	}

	/**
	 * Gets an individual mark. If there are more then one mark in the marks
	 * list, only the first mark will be returned.
	 * 
	 * @return Gets the individual mark
	 */
	public EPPMark getMark() {
		if (this.marks == null || this.marks.isEmpty()) {
			return null;
		}
		else {
			return this.marks.get(0);
		}
	}
	
	/**
	 * Add a mark to the list of marks.
	 * 
	 * @param aMark Mark to add to list
	 */
	public void addMark(EPPMark aMark) {
		if (this.marks == null) {
			this.marks = new ArrayList<EPPMark>();
		}
		this.marks.add(aMark);
	}

	/**
	 * Gets the list of marks
	 * 
	 * @return List of marks if defined; empty list otherwise.
	 */
	public List<EPPMark> getMarks() {
		return this.marks;
	}

	/**
	 * Sets the list of marks.
	 * 
	 * @param aMarks
	 *            List of marks
	 */
	public void setMarks(List<EPPMark> aMarks) {
		this.marks = aMarks;
	}

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}
	
}