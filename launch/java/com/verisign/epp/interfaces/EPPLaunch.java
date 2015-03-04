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
package com.verisign.epp.interfaces;

// W3C Imports
import java.util.Vector;

import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.launch.EPPLaunchCheck;
import com.verisign.epp.codec.launch.EPPLaunchChkData;
import com.verisign.epp.codec.launch.EPPLaunchPhase;

/**
 * <code>EPPLaunch</code> is the primary client interface class used for launch
 * EPP extension. This interface class is only used for sending the Claims Check
 * Command via the {@link #sendCheck()} method. An instance of
 * <code>EPPLaunch</code> is created with an initialized <code>EPPSession</code>
 * , and can be used for more than one request within a single thread. A set of
 * setter methods are provided to set the attributes before a call to one of the
 * send action methods. The response returned from the send action methods are
 * either instances of <code>EPPResponse</code> or instances of response classes
 * in the <code>com.verisign.epp.codec.launch</code> package.
 * 
 * @see com.verisign.epp.codec.gen.EPPResponse
 * @see com.verisign.epp.codec.launch.EPPLaunchChkData
 */
public class EPPLaunch {

	/**
	 * Phase when trademark holders can submit registrations or applications
	 * with trademark information that can be validated by.
	 */
	public final static String PHASE_SUNRISE = EPPLaunchPhase.PHASE_SUNRISE;

	/**
	 * Post sunrise phase when non-trademark holders are allowed to register
	 * domain names with steps taken to address a large volume of initial
	 * registrations.
	 */
	public final static String PHASE_LANDRUSH = EPPLaunchPhase.PHASE_LANDRUSH;

	/**
	 * Trademark claims phase 1 as defined by Trademark Clearinghouse model of
	 * displaying a claims notice to clients for domain names that match
	 * trademarks.
	 */
	public final static String PHASE_CLAIMS = EPPLaunchPhase.PHASE_CLAIMS;

	/**
	 * Post launch phase that is also referred to as "steady state". Servers MAY
	 * require additional trademark protection with this phase.
	 */
	public final static String PHASE_OPEN = EPPLaunchPhase.PHASE_OPEN;

	/**
	 * Post launch phase that is also referred to as "steady state". Servers MAY
	 * require additional trademark protection with this phase.
	 */
	public final static String PHASE_CUSTOM = EPPLaunchPhase.PHASE_CUSTOM;

	/**
	 * Constant used to specify the claims check form type
	 */
	public static final String TYPE_CLAIMS = EPPLaunchCheck.TYPE_CLAIMS;

	/**
	 * Constant used to specify the availability check form type
	 */
	public static final String TYPE_AVAILABILITY = EPPLaunchCheck.TYPE_AVAILABILITY;

	/** List of domain names used for the Claims Check {@link #sendCheck()}. */
	private Vector domainList = new Vector();

	/** An instance of a session for sending Claims Check Command. */
	private EPPSession session = null;

	/** Transaction Id provided by client. */
	private String transId = null;

	/**
	 * Extension objects associated with the command. This is a
	 * <code>Vector</code> of <code>EPPCodecComponent</code> objects.
	 */
	private Vector extensions = null;

	/**
	 * Launch phase name using using of the <code>PHASE</code> constants. The
	 * default value is set to {@link #PHASE_CLAIMS}.
	 */
	private String phase = PHASE_CLAIMS;

	/**
	 * Phase name that represents either a sub-phase or custom phase name.
	 */
	private String phaseName;

	/**
	 * Defines check form type with one of the <code>TYPE</code> constants.
	 */
	private String type;

	/**
	 * Constructs an <code>EPPLaunch</code> given an initialized EPP session.
	 * 
	 * @param aSession
	 *            Server session to use.
	 */
	public EPPLaunch(EPPSession aSession) {
		this.session = aSession;
	}

	/**
	 * Adds a command extension object.
	 * 
	 * @param aExtension
	 *            command extension object associated with the command
	 */
	public void addExtension(EPPCodecComponent aExtension) {
		if (this.extensions == null) {
			this.extensions = new Vector();
		}

		this.extensions.addElement(aExtension);
	}

	/**
	 * Sets the command extension objects.
	 * 
	 * @param aExtensions
	 *            command extension objects associated with the command
	 */
	public void setExtensions(Vector aExtensions) {
		this.extensions = aExtensions;
	}

	/**
	 * Gets the command extensions.
	 * 
	 * @return <code>Vector</code> of concrete <code>EPPCodecComponent</code>
	 *         associated with the command if exists; <code>null</code>
	 *         otherwise.
	 */
	public Vector getExtensions() {
		return this.extensions;
	}

	/**
	 * Adds a domain name for use with {@link #sendCheck()} method.
	 * 
	 * @param aDomain
	 *            Domain name to add
	 */
	public void addDomainName(String aDomain) {
		this.domainList.addElement(aDomain);
	}

	/**
	 * Sends a Claim Check Command, which is an extension of the Domain Check
	 * with the <code>EPPLaunchCheck</code> extension and the type set to the
	 * whether to execute a claims check command or an availability check
	 * command for a given phase.<br>
	 * <br>
	 * The required attributes have been set with the following methods:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>addDomainName</code> - Adds a domain name to check if there is a
	 * matching trademark that requires a claims notice. More than one domain
	 * name can be checked in <code>sendCheck</code>.</li>
	 * <code>setPhase</code> - Sets the Claims Check Command phase. The value
	 * should be set to {@link #PHASE_CLAIMS} when type is set to
	 * {@link #TYPE_CLAIMS}; otherwise when type is set to
	 * {@link #TYPE_AVAILABILITY} the availability check will be executed in the
	 * context of the defined phase.</li>
	 * </ul>
	 * 
	 * <br>
	 * <br>
	 * The optional attributes have been set with the following:<br>
	 * <ul>
	 * <li><code>setType</code> - Sets the type of the check as either a
	 * {@link #TYPE_CLAIMS} for a claims check command or
	 * {@link #TYPE_AVAILABILITY} for an availability check for a specific
	 * phase. The default type is {@link #TYPE_CLAIMS}.</li>
	 * <li>
	 * </ul>
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setTransId</code> - Sets the client transaction identifier</li>
	 * </ul>
	 * 
	 * 
	 * @return <code>EPPResponse</code> containing <code>EPPLaunchChkData</code>
	 *         extension when the type attribute is <code>TYPE_CLAIMS</code> or
	 *         <code>EPPDomainCheckResp</code> when the type attribute is
	 *         <code>TYPE_AVAILABILITY</code>.
	 * 
	 * @exception EPPCommandException
	 *                Error executing the check command. Use
	 *                <code>getResponse</code> to get the associated server
	 *                error response.
	 */
	public EPPResponse sendCheck() throws EPPCommandException {
		// Create the command
		EPPDomainCheckCmd theCommand = new EPPDomainCheckCmd(this.transId,
				this.domainList);

		// Create phase with optional phase name
		EPPLaunchPhase thePhase;
		if (this.phaseName != null) {
			thePhase = new EPPLaunchPhase(this.phase, this.phaseName);
		}
		else {
			thePhase = new EPPLaunchPhase(this.phase);
		}

		// Create launch check extension
		EPPLaunchCheck theExt = new EPPLaunchCheck(thePhase);

		// Set launch form type if defined
		if (this.type != null) {
			theExt.setType(this.type);
		}

		// Add extension
		this.addExtension(theExt);
		theCommand.setExtensions(this.extensions);

		resetLaunch();

		// process the command and response
		EPPResponse theResponse = this.session.processDocument(theCommand,
				EPPResponse.class);

		// Invalid response type?
		if (!((theResponse instanceof EPPResponse) && (!theResponse
				.hasExtension(EPPLaunchChkData.class.getClass())))
				&& !(theResponse instanceof EPPDomainCheckResp)) {
			throw new EPPCommandException("Unexpected response type of "
					+ theResponse.getClass().getName() + ", expecting either "
					+ EPPResponse.class.getName() + " with "
					+ EPPLaunchChkData.class.getName() + " extension or "
					+ EPPDomainCheckResp.class.getName());
		}

		return theResponse;
	}

	/**
	 * Resets the domain instance to its initial state.
	 */
	protected void resetLaunch() {
		this.domainList = new Vector();
		this.transId = null;
		this.extensions = null;
		this.phase = PHASE_CLAIMS;
		this.phaseName = null;
		this.type = null;
	}

	/**
	 * Gets the response associated with the last command. This method can be
	 * used to retrieve the server error response in the catch block of
	 * EPPCommandException.
	 * 
	 * @return Response associated with the last command
	 */
	public EPPResponse getResponse() {
		return this.session.getResponse();
	}

	/**
	 * Sets the client transaction identifier.
	 * 
	 * @param aTransId
	 *            Client transaction identifier
	 */
	public void setTransId(String aTransId) {
		this.transId = aTransId;
	}

	/**
	 * Sets the phase value.
	 * 
	 * @param aPhase
	 *            Phase value, which should be one of the <code>PHASE</code>
	 *            constants.
	 */
	public void setPhase(String aPhase) {
		this.phase = aPhase;
	}

	/**
	 * Sets the phase name value. The phase name represents either the sub-phase
	 * of the phase value or the custom phase name.
	 * 
	 * @param aPhaseName
	 *            Phase name
	 */
	public void setPhaseName(String aPhaseName) {
		this.phaseName = aPhaseName;
	}

	/**
	 * Sets the check form type. The XML schema defines the default as
	 * {@link #TYPE_CLAIMS} if undefined.
	 * 
	 * @param aType
	 *            {@link #TYPE_CLAIMS} or {@link #TYPE_AVAILABILITY}
	 */
	public void setType(String aType) {
		this.type = aType;
	}

}