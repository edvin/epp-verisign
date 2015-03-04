/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.serverstub;

// Logging Imports
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.coaext.EPPCoaExtAttr;
import com.verisign.epp.codec.coaext.EPPCoaExtCreate;
import com.verisign.epp.codec.coaext.EPPCoaExtInfData;
import com.verisign.epp.codec.coaext.EPPCoaExtKey;
import com.verisign.epp.codec.coaext.EPPCoaExtUpdate;
import com.verisign.epp.codec.coaext.EPPCoaExtValue;
import com.verisign.epp.codec.contact.EPPContactTransferResp;
import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainRenewCmd;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;
import com.verisign.epp.codec.launch.EPPLaunchCheck;
import com.verisign.epp.codec.launch.EPPLaunchCreate;
import com.verisign.epp.codec.launch.EPPLaunchDelete;
import com.verisign.epp.codec.launch.EPPLaunchInfo;
import com.verisign.epp.codec.launch.EPPLaunchUpdate;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollMapFactory;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollResponse;
import com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollThreshold;
import com.verisign.epp.codec.namestoreext.EPPNamestoreExtNSExtErrData;
import com.verisign.epp.codec.namestoreext.EPPNamestoreExtNamestoreExt;
import com.verisign.epp.codec.persreg.EPPPersRegCreateData;
import com.verisign.epp.codec.persreg.EPPPersRegCreateErrData;
import com.verisign.epp.codec.persreg.EPPPersRegRenewData;
import com.verisign.epp.codec.persreg.EPPPersRegTransferData;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheck;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResp;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResult;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainReAssignCmd;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreate;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDelete;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtRenew;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtTransfer;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtUpdate;
import com.verisign.epp.codec.rgpext.EPPRgpExtFactory;
import com.verisign.epp.codec.rgpext.EPPRgpExtInfData;
import com.verisign.epp.codec.rgpext.EPPRgpExtReport;
import com.verisign.epp.codec.rgpext.EPPRgpExtRestore;
import com.verisign.epp.codec.rgpext.EPPRgpExtStatus;
import com.verisign.epp.codec.rgpext.EPPRgpExtUpData;
import com.verisign.epp.codec.rgpext.EPPRgpExtUpdate;
import com.verisign.epp.codec.rgppoll.EPPRgpPollMapFactory;
import com.verisign.epp.codec.rgppoll.EPPRgpPollResponse;
import com.verisign.epp.codec.rgppoll.EPPRgpPollStatus;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSAlgorithm;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtInfData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtKeyData;
import com.verisign.epp.codec.syncext.EPPSyncExtFactory;
import com.verisign.epp.codec.syncext.EPPSyncExtUpdate;
import com.verisign.epp.codec.whois.EPPWhoisInf;
import com.verisign.epp.codec.whois.EPPWhoisInfData;
import com.verisign.epp.framework.EPPDomainHandler;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPHandleEventException;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>NSDomainHandler</code> class is a concrete implementation of the
 * abstract <code>com.verisign.epp.framework.EPPDomainHandler</code> class. It
 * defines the Server's response to all received EPP Host Commands.
 * 
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPEventResponse
 */
public class NSDomainHandler extends EPPDomainHandler {
	/** Constant server transaction id */
	private static final String svrTransId = "54322-XYZ";

	/** Constant ROID */
	private static final String roid = "NS1EXAMPLE1-VRSN";

	private static long ONE_YEAR_TIME = 365l * 24 * 60 * 60 * 1000l;

	/** Logger catagory */
	private static Logger cat = Logger.getLogger(NSDomainHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** Sub-handler for handling version 1.0 extension messages. */
	SecDNSV10SubDomainHandler v10SubHandler = new SecDNSV10SubDomainHandler();

	/** Sub-handler for handling version 1.1 extension messages. */
	SecDNSV11SubDomainHandler v11SubHandler = new SecDNSV11SubDomainHandler();

	/** Personal Registration Domain Handler used for the .name create, transfer, and info commands. */
	PersRegDomainHandler pergRegDomainHandler = new PersRegDomainHandler();
	
	/** Launch Phase Domain Handler used when the launch extension is passed. */
	LaunchDomainHandler launchDomainHandler = new LaunchDomainHandler();

	/** Related Domain Handler used when the related domain extension is passed. */
	RelatedDomainHandler relatedDomainHandler = new RelatedDomainHandler();
	
	/**
	 * Constructs an instance of NSDomainHandler
	 */
	public NSDomainHandler() {

		// Add NameStore domain extensions and poll message mappings
		try {
			cat.info("NSDomainHandler(): Registering EPPLowBalancePollMapFactory");
			EPPFactory.getInstance().addMapFactory(
					EPPLowBalancePollMapFactory.class.getName());
			cat.info("NSDomainHandler(): Registering EPPRgpPollMapFactory");
			EPPFactory.getInstance().addMapFactory(
					EPPRgpPollMapFactory.class.getName());
			cat.info("NSDomainHandler(): Registering EPPRgpExtFactory");
			EPPFactory.getInstance().addExtFactory(
					EPPRgpExtFactory.class.getName());

			cat.info("NSDomainHandler(): Registering EPPSyncExtFactory");
			EPPFactory.getInstance().addExtFactory(
					EPPSyncExtFactory.class.getName());

		}
		catch (EPPCodecException ex) {
			cat.error("NSDomainHandler(): Couldn't load the Map Factory", ex);
			System.exit(1);
		}
	}

	/**
	 * Handles any common behavior that all Domain commands need to execute
	 * before they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomainHandler</code> This is assumed to be an
	 *            instance of SessionData here.
	 * 
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {
		SessionData sessionData = (SessionData) aData;
		EPPCommand theMessage = (EPPCommand) aEvent.getMessage();

		if (!sessionData.isLoggedIn()) {
			/**
			 * The client isn't logged in so they can't successfully invoke a
			 * command. Sending COMMAND_FAILED_END
			 */
			/**
			 * Create the transId for the response with the client trans id and
			 * the server trans id.
			 */
			EPPTransId transId = new EPPTransId(theMessage.getTransId(),
					svrTransId);

			// Create the Response (Standard EPPResponse)
			EPPResponse theResponse = new EPPResponse(transId);

			theResponse.setResult(EPPResult.COMMAND_FAILED_END);
			throw new EPPHandleEventException(
					"The client has not established a session", theResponse);
		}

		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			EPPNamestoreExtNamestoreExt theExt = (EPPNamestoreExtNamestoreExt) theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class);

			if (theExt.getSubProductID().equals("BAD")) {
				EPPTransId transId = new EPPTransId(theMessage.getTransId(),
						svrTransId);
				EPPResult result = new EPPResult(
						EPPResult.PARAM_VALUE_POLICY_ERROR);
				result.addExtValueReason("Invalid sub-product id "
						+ theExt.getSubProductID());
				EPPResponse theResponse = new EPPResponse(transId, result);

				theResponse
						.addExtension(new EPPNamestoreExtNSExtErrData(
								EPPNamestoreExtNSExtErrData.ERROR_SUB_PRODUCT_NOT_EXISTS));
				throw new EPPHandleEventException("Invalid sub-product id "
						+ theExt.getSubProductID(), theResponse);
			}

		}
	}

	/**
	 * Handles any common behavior that all Domain commands need to execute
	 * after they execute their command specific behavior.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomainHandler</code>
	 * 
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {
	}

	/**
	 * Invoked when a Domain Check command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCheck(EPPEvent aEvent, Object aData) {
		EPPDomainCheckCmd theMessage = (EPPDomainCheckCmd) aEvent.getMessage();
		
		cat.debug("doDomainCheck: command = [" + theMessage + "]");
		
		
		EPPResponse theResponse;
		
		// Claims Check Command?
		if (theMessage.hasExtension(EPPLaunchCheck.class)) {
			EPPEventResponse launchResp = launchDomainHandler.doDomainCheck(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) launchResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return launchResp;
		}
		
		

		// This is just a vector of strings representing Domain Names
		Vector vNames = theMessage.getNames();
		Enumeration eNames = vNames.elements();

		Vector vResults = new Vector();

		// create a Vector of Ping Results.
		boolean available = true;

		while (eNames.hasMoreElements()) {
			String domainName = (String) eNames.nextElement();
			vResults.addElement(new EPPDomainCheckResult(domainName, available));
			available = !available;
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Response for many domain names
		theResponse = new EPPDomainCheckResp(transId, vResults);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPPremiumDomainCheckResult eppPremiumDomainCheckResult = null;
		EPPPremiumDomainCheck theExt = (EPPPremiumDomainCheck) theMessage
				.getExtension(EPPPremiumDomainCheck.class);
		Vector premiumResults = new Vector();

		if (theResponse.isSuccess() && theExt != null
				&& theExt.getFlag().booleanValue()) {

			EPPDomainCheckResp thecheckResponse = (EPPDomainCheckResp) theResponse;
			for (int i = 0; i < thecheckResponse.getCheckResults().size(); i++) {

				EPPDomainCheckResult currResult = (EPPDomainCheckResult) thecheckResponse
						.getCheckResults().elementAt(i);

				if (currResult.getName().equals("non-premiumdomain.tv")) {
					eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(
							currResult.getName(), false);
				}
				else {
					eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(
							currResult.getName(), true);
					if (currResult.isAvailable()) {
						eppPremiumDomainCheckResult.setPrice(new BigDecimal(
								"125.00"));
						eppPremiumDomainCheckResult
								.setRenewalPrice(new BigDecimal("75.00"));
					}
				}
				premiumResults.addElement(eppPremiumDomainCheckResult);
			}

			if (premiumResults.size() > 0) {
				theResponse.addExtension(new EPPPremiumDomainCheckResp(
						premiumResults));
			}
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Renew command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainRenew(EPPEvent aEvent, Object aData) {
		EPPDomainRenewCmd theMessage = (EPPDomainRenewCmd) aEvent.getMessage();
		
		cat.debug("doDomainRenew: command = [" + theMessage + "]");
		
		EPPResponse theResponse;
		
		// Related Domain Renew?  
		if (theMessage.hasExtension(EPPRelatedDomainExtRenew.class)) {
			EPPEventResponse relatedResp = relatedDomainHandler.doDomainRenew(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedResp;
		}

		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Test with transId, domain name, roid, and expiration date
		theResponse = new EPPDomainRenewResp(transId, theMessage.getName(),
				new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		if (theMessage.getName().startsWith("TESTBUNDLE")
				&& theMessage.getName().endsWith(".NAME")) {
			theResponse.addExtension(new EPPPersRegRenewData(true));
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Delete command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainDelete(EPPEvent aEvent, Object aData) {
		EPPDomainDeleteCmd theMessage = (EPPDomainDeleteCmd) aEvent
				.getMessage();
		
		cat.debug("doDomainDelete: command = [" + theMessage + "]");
				
		
		// Launch Domain Delete Command?
		if (theMessage.hasExtension(EPPLaunchDelete.class)) {
			EPPEventResponse launchResp = launchDomainHandler.doDomainDelete(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) launchResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return launchResp;
		}
		
		// Related Domain Delete?  
		if (theMessage.hasExtension(EPPRelatedDomainExtDelete.class)) {
			EPPEventResponse relatedResp = relatedDomainHandler.doDomainDelete(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedResp;
		}


		/**
		 * Create the transId for the response with the client trans id and the
		 * server trans id.
		 */
		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Create Delete Response (Standard EPPResponse)
		EPPResponse theResponse = new EPPResponse(transId);
		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Create command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCreate(EPPEvent aEvent, Object aData) {
		EPPDomainCreateCmd theMessage = (EPPDomainCreateCmd) aEvent
				.getMessage();
		
		cat.debug("doDomainCreate: command = [" + theMessage + "]");
		
	
		// Launch Domain Create Command?
		if (theMessage.hasExtension(EPPLaunchCreate.class)) {
			EPPEventResponse launchResp = launchDomainHandler.doDomainCreate(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) launchResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return launchResp;
		}
		
		// Related Domain Create?  
		if (theMessage.hasExtension(EPPRelatedDomainExtCreate.class)) {
			EPPEventResponse relatedResp = relatedDomainHandler.doDomainCreate(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedResp;
		}

		
		// .name domain?
		if (theMessage.getName().endsWith(".name")) {
			EPPEventResponse persRegResp = pergRegDomainHandler.doDomainCreate(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) persRegResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return persRegResp;
		}
		

		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Is an IDN?
		if (theMessage.hasExtension(EPPIdnLangTag.class)) {
			EPPIdnLangTag theExt = (EPPIdnLangTag) theMessage
					.getExtension(EPPIdnLangTag.class);
			cat.debug("doDomainCreate: Request for IDN domain with langauge tag = "
					+ theExt.getLang());
		}

		EPPResponse theResponse;

		if (theMessage.hasExtension(EPPCoaExtCreate.class)) {
			// Get the coa:create
			EPPCoaExtCreate create = (EPPCoaExtCreate) theMessage
					.getExtension(EPPCoaExtCreate.class);

			// Print the key/value pairs, if present.
			if (create != null) {
				// The requirement that there be one or more attributes is
				// handled by
				// the xsd.
				List attrList = create.getAttrs();
				for (Iterator iterator = attrList.iterator(); iterator
						.hasNext();) {
					EPPCoaExtAttr attr = (EPPCoaExtAttr) iterator.next();
					String key = attr.getKey().getKey();
					String value = attr.getValue().getValue();

					// The requirement that the key and value not be null or
					// blank is
					// handled by the xsd.
					// Just log what came in for informational purposes.
					cat.info("Client Object Attribute to create: key='" + key
							+ "', value='" + value + "'");
				}
			}
		}

		// Both dnssec extension versions set?
		if (theMessage
				.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate.class)
				&& theMessage
						.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate.class)) {
			cat.error("NSDomainHandler.doDomainCreate: both v10 and v11 of EPPSecDNSExtCreate passed.");

			EPPResult theResult = new EPPResult(
					EPPResult.PARAM_VALUE_POLICY_ERROR);
			theResult
					.addExtValueReason("Both v10 and v11 of secDNS extension passed");
			theResponse = new EPPResponse(null, theResult);
		} // v10 extension set?
		else if (theMessage
				.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate.class)) {
			theResponse = this.v10SubHandler.doDomainCreate(theMessage, aData);
		} // v11 extension set?
		else if (theMessage
				.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate.class)) {
			theResponse = this.v11SubHandler.doDomainCreate(theMessage, aData);
		} // no extension set
		else {
			cat.info("NSDomainHandler.doDomainCreate: no EPPSecDNSExtCreate extension");
			theResponse = new EPPDomainCreateResp(transId,
					theMessage.getName(), new Date());
		}

		theResponse.setTransId(transId);

		// Error response from dnssec extension?
		if (!theResponse.isSuccess()) {
			cat.error("doDomainCreate: Error response in handling dnssec extension");
			return new EPPEventResponse(theResponse);
		}

		((EPPDomainCreateResp) theResponse).setExpirationDate(new Date());

		if (theMessage.getName().equals("NSPollTst.com")) {
			// Create a set of poll messages for NSPollTst
			EPPResponse thePollMsg = null;

			try {
				EPPDomainTransferResp theTransMsg = new EPPDomainTransferResp();

				theTransMsg.setActionClient("ActionClient");
				theTransMsg.setActionDate(new Date());
				theTransMsg.setExpirationDate(new Date());
				theTransMsg.setName(theMessage.getName());
				theTransMsg.setRequestClient("RequestClient");
				theTransMsg.setRequestDate(new Date());

				// Domain Transfer request
				theTransMsg
						.setTransferStatus(EPPDomainTransferResp.TRANSFER_PENDING);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Domain Transfer approve
				theTransMsg
						.setTransferStatus(EPPDomainTransferResp.TRANSFER_CLIENT_APPROVED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Domain Transfer cancelled
				theTransMsg
						.setTransferStatus(EPPDomainTransferResp.TRANSFER_CLIENT_CANCELLED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Domain Transfer rejected
				theTransMsg
						.setTransferStatus(EPPDomainTransferResp.TRANSFER_CLIENT_REJECTED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);
				// Domain Transfer approved
				theTransMsg
						.setTransferStatus(EPPDomainTransferResp.TRANSFER_SERVER_APPROVED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Contact Transfer request
				theTransMsg
						.setTransferStatus(EPPContactTransferResp.TRANSFER_PENDING);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Contact Transfer approve
				theTransMsg
						.setTransferStatus(EPPContactTransferResp.TRANSFER_CLIENT_APPROVED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Contact Transfer cancelled
				theTransMsg
						.setTransferStatus(EPPContactTransferResp.TRANSFER_CLIENT_CANCELLED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Contact Transfer rejected
				theTransMsg
						.setTransferStatus(EPPContactTransferResp.TRANSFER_CLIENT_REJECTED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);
				// Contact Transfer approved
				theTransMsg
						.setTransferStatus(EPPContactTransferResp.TRANSFER_SERVER_APPROVED);
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theTransMsg, null);

				// Low Balance Notification
				EPPLowBalancePollResponse theLowBalanceMsg = new EPPLowBalancePollResponse();
				theLowBalanceMsg.setRegistrarName("Test Registar");
				theLowBalanceMsg.setCreditLimit("1000");
				theLowBalanceMsg
						.setCreditThreshold(new EPPLowBalancePollThreshold(
								EPPLowBalancePollThreshold.PERCENT, "10"));
				theLowBalanceMsg.setAvailableCredit("80");

				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theLowBalanceMsg, null);

				// RGP Pending Restore Notification
				EPPRgpPollResponse theRGPMsg = new EPPRgpPollResponse();
				theRGPMsg.setName(theMessage.getName());
				theRGPMsg.setReportDueDate(new Date(
						System.currentTimeMillis() + 3600));
				theRGPMsg.setTransId(transId);
				theRGPMsg.setReqDate(new Date(System.currentTimeMillis()));
				theRGPMsg.setStatus(new EPPRgpPollStatus(
						EPPRgpPollStatus.PENDING_RESTORE));
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theRGPMsg, null);

				// Domain Pending Approved Action Notification
				com.verisign.epp.codec.domain.EPPDomainPendActionMsg thePendActionMsg = new com.verisign.epp.codec.domain.EPPDomainPendActionMsg(
						transId, theMessage.getName(), true, new EPPTransId(
								theMessage.getTransId(), svrTransId),
						new Date());
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						thePendActionMsg, null);

				// Domain Pending Denied Action Notification
				thePendActionMsg = new com.verisign.epp.codec.domain.EPPDomainPendActionMsg(
						transId, theMessage.getName(), false, new EPPTransId(
								theMessage.getTransId(), svrTransId),
						new Date());
				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						thePendActionMsg, null);
			}
			catch (EPPPollQueueException ex) {
				cat.error("doDomainCreate: Error putting message ["
						+ thePollMsg + "]");

				EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
				theResponse = new EPPResponse(transId, theResult);

				return new EPPEventResponse(theResponse);
			}

		}
		else if (theMessage.getName().equals("lowbalancepoll.com")) {
			EPPLowBalancePollResponse theLowBalanceMsg = new EPPLowBalancePollResponse();

			try {
				theLowBalanceMsg.setRegistrarName("Test Registar");
				theLowBalanceMsg.setCreditLimit("1000");
				theLowBalanceMsg
						.setCreditThreshold(new EPPLowBalancePollThreshold(
								EPPLowBalancePollThreshold.PERCENT, "10"));
				theLowBalanceMsg.setAvailableCredit("80");

				EPPPollQueueMgr.getInstance().put(null, NSPollHandler.KIND,
						theLowBalanceMsg, null);
			}
			catch (EPPPollQueueException e) {
				cat.error("doDomainCreate: Error low balance putting message ["
						+ theLowBalanceMsg + "]");

				EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
				theResponse = new EPPResponse(transId, theResult);

				return new EPPEventResponse(theResponse);
			}

		} 
		else {
			theResponse.setResult(EPPResult.SUCCESS);
		}
		
		
		if (theMessage.getName().startsWith("ERRORTESTBUNDLE")  && theMessage.getName().endsWith(".NAME")) {
			 theResponse.setResult(EPPResult.ASSOC_PROHIBITS_OP);
			 theResponse.addExtension(new EPPPersRegCreateErrData(EPPPersRegCreateErrData.ERROR_DEFREG_EXISTS));
		}else if (theMessage.getName().startsWith("TESTBUNDLE ")  && theMessage.getName().endsWith(".NAME")) {
			 theResponse.addExtension(new EPPPersRegCreateData(true));
			
		}
		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Transfer command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainTransfer(EPPEvent aEvent, Object aData) {
		EPPDomainTransferCmd theMessage = (EPPDomainTransferCmd) aEvent
				.getMessage();
		
		cat.debug("doDomainTransfer: command = [" + theMessage + "]");
				
		// Related Domain Transfer?  
		if (theMessage.hasExtension(EPPRelatedDomainExtTransfer.class)) {
			EPPEventResponse relatedResp = relatedDomainHandler.doDomainTransfer(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedResp;
		}
		
		
		// .name domain?
		if (theMessage.getName().endsWith(".name")) {
			EPPEventResponse persRegResp = pergRegDomainHandler
					.doDomainTransfer(aEvent, aData);

			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) persRegResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return persRegResp;
		}

		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Is a transfer request?
		if (theMessage.getOp().equals(EPPCommand.OP_REQUEST)) {
			// Insert transfer response in poll queue
			EPPDomainTransferResp thePollMsg = new EPPDomainTransferResp();

			thePollMsg.setName("example");
			thePollMsg.setTransferStatus(EPPResponse.TRANSFER_PENDING);
			thePollMsg.setRequestClient("ClientX");
			thePollMsg.setRequestDate(new Date());
			thePollMsg.setActionClient("ClientY");
			thePollMsg.setActionDate(new Date());
			thePollMsg.setExpirationDate(new Date());
			thePollMsg.setResult(EPPResult.SUCCESS);

			try {
				EPPPollQueueMgr.getInstance().put(null, EPPDomainMapFactory.NS,
						thePollMsg, null);
			}
			catch (EPPPollQueueException ex) {
				cat.error("doDomainTransfer: Error putting message ["
						+ thePollMsg + "]");

				EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
				EPPResponse theResponse = new EPPResponse(transId, theResult);

				return new EPPEventResponse(theResponse);
			}
		}

		EPPDomainTransferResp theResponse = new EPPDomainTransferResp(transId,
				theMessage.getName());
		theResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);

		theResponse.setRequestClient("ClientX");
		theResponse.setRequestDate(new Date());

		theResponse.setActionClient("ClientY");
		theResponse.setActionDate(new Date());

		/**
		 * The Expiration date is optional
		 */
		theResponse.setExpirationDate(new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		// Is a transfer request?
		if (theMessage.getOp().equals(EPPCommand.OP_REQUEST)
				&& theMessage.getName().startsWith("TESTBUNDLE")
				&& theMessage.getName().endsWith(".NAME")) {
			theResponse.addExtension(new EPPPersRegTransferData(true));
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Update command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainUpdate(EPPEvent aEvent, Object aData) {

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		cat.debug("doDomainUpdate: command = [" + theMessage + "]");
		
		// Launch Domain Update Command?
		if (theMessage.hasExtension(EPPLaunchUpdate.class)) {
			EPPEventResponse launchResp = launchDomainHandler.doDomainUpdate(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) launchResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return launchResp;
		}
		
		// Related Domain Update?  
		if (theMessage.hasExtension(EPPRelatedDomainExtUpdate.class)) {
			EPPEventResponse relatedResp = relatedDomainHandler.doDomainUpdate(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedResp;
		}
		

		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		if (theMessage.hasExtension(EPPCoaExtUpdate.class)) {
			EPPCoaExtUpdate update = (EPPCoaExtUpdate) theMessage
					.getExtension(EPPCoaExtUpdate.class);
			List attrsToAdd = update.getPutAttrs();
			List attrsToRem = update.getRemAttrs();

			if (attrsToRem != null) {
				for (Iterator iterator = attrsToRem.iterator(); iterator
						.hasNext();) {
					EPPCoaExtKey key = (EPPCoaExtKey) iterator.next();
					cat.info("Client Object Attribute to remove: key='"
							+ key.getKey() + "'");
				}
			}

			if (attrsToAdd != null) {
				for (Iterator iterator = attrsToAdd.iterator(); iterator
						.hasNext();) {
					EPPCoaExtAttr attr = (EPPCoaExtAttr) iterator.next();
					String key = attr.getKey().getKey();
					String value = attr.getValue().getValue();
					cat.info("Client Object Attribute to add: key='" + key
							+ "', value='" + value + "'");
				}
			}
		}

		// RGP command?
		if (theMessage.hasExtension(EPPRgpExtUpdate.class)) {
			cat.debug("doDomainUpdate: Is RGP command");

			EPPRgpExtUpdate theExt = (EPPRgpExtUpdate) theMessage
					.getExtension(EPPRgpExtUpdate.class);

			// Request?
			if (theExt.getRestore().getOp().equals(EPPRgpExtRestore.REQUEST)) {
				return this.doDomainRestoreRequest(aEvent,
						theMessage.getName(), transId, aData);
			} // Report?
			else {
				return this.doDomainRestoreReport(aEvent, theMessage.getName(),
						transId, theExt.getRestore().getReport(), aData);
			}

		} // Sync command?
		else if (theMessage.hasExtension(EPPSyncExtUpdate.class)) {
			cat.debug("doDomainUpdate: Is update command");
			EPPSyncExtUpdate theExt = (EPPSyncExtUpdate) theMessage
					.getExtension(EPPSyncExtUpdate.class);

			return this.doDomainSync(aEvent, theMessage.getName(), transId,
					theExt, aData);
		} // Premium domain ext command

		else if (theMessage.hasExtension(EPPPremiumDomainReAssignCmd.class)) {

			PremiumDomainHandler handler = new PremiumDomainHandler();
			return handler.doDomainUpdate(aEvent, aData);

		}
		else {

			EPPResponse theResponse;

			cat.debug("NSDomainHandler: message = [" + theMessage + "]");

			// Both dnssec extension versions set?
			if (theMessage
					.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate.class)
					&& theMessage
							.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate.class)) {
				cat.error("NSDomainHandler.doDomainUpdate: both v10 and v11 of EPPSecDNSExtUpdate passed.");

				EPPResult theResult = new EPPResult(
						EPPResult.PARAM_VALUE_POLICY_ERROR);
				theResult
						.addExtValueReason("Both v10 and v11 of secDNS extension passed");
				theResponse = new EPPResponse(null, theResult);
			} // v10 extension set?
			else if (theMessage
					.hasExtension(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate.class)) {
				theResponse = this.v10SubHandler.doDomainUpdate(theMessage,
						aData);
			} // v11 extension set?
			else if (theMessage
					.hasExtension(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate.class)) {
				theResponse = this.v11SubHandler.doDomainUpdate(theMessage,
						aData);
			} // no extension set
			else {
				cat.info("NSDomainHandler.doDomainCreate: no EPPSecDNSExtUpdate extension");
				theResponse = new EPPResponse(transId);
				theResponse.setResult(EPPResult.SUCCESS);
			}

			theResponse.setTransId(transId);

			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				theResponse.addExtension(theMessage
						.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			return new EPPEventResponse(theResponse);
		}

	}

	protected EPPEventResponse doDomainRestoreRequest(EPPEvent aEvent,
			String aDomainName, EPPTransId aTransId, Object aData) {

		cat.info("domain restore request: domain name = " + aDomainName);

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		EPPResponse theResponse = null;

		// Add restore poll message
		EPPRgpPollResponse thePollMsg = new EPPRgpPollResponse();

		thePollMsg.setName(aDomainName);
		thePollMsg
				.setReportDueDate(new Date(System.currentTimeMillis() + 3600));
		thePollMsg.setReqDate(new Date(System.currentTimeMillis()));
		thePollMsg.setStatus(new EPPRgpPollStatus(
				EPPRgpPollStatus.PENDING_RESTORE));

		try {
			EPPPollQueueMgr.getInstance().put(null, EPPRgpPollMapFactory.NS,
					thePollMsg, null);
		}
		catch (EPPPollQueueException ex) {
			cat.error("doDomainUpdate: Error putting message [" + thePollMsg
					+ "]");

			EPPResult theResult = new EPPResult(EPPResult.COMMAND_FAILED);
			theResponse = new EPPResponse(aTransId, theResult);

			return new EPPEventResponse(theResponse);
		}

		// Success response
		theResponse = new EPPResponse(aTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		// Add upData extension with pendingRestore status
		theResponse.addExtension(new EPPRgpExtUpData(new EPPRgpExtStatus(
				EPPRgpExtStatus.PENDING_RESTORE)));

		return new EPPEventResponse(theResponse);
	}

	protected EPPEventResponse doDomainRestoreReport(EPPEvent aEvent,
			String aDomainName, EPPTransId aTransId, EPPRgpExtReport aReport,
			Object aData) {

		cat.info("domain restore report: domain name = " + aDomainName
				+ ", report = " + aReport);

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		// Success response
		EPPResponse theResponse = new EPPResponse(aTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		return new EPPEventResponse(theResponse);
	}

	protected EPPEventResponse doDomainSync(EPPEvent aEvent,
			String aDomainName, EPPTransId aTransId, EPPSyncExtUpdate aSyncExt,
			Object aData) {

		cat.info("domain sync: domain name = " + aDomainName + ", month = "
				+ aSyncExt.getMonth() + ", day = " + aSyncExt.getDay());

		EPPDomainUpdateCmd theMessage = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		// Success response
		EPPResponse theResponse = new EPPResponse(aTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Info command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPDomaindHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		EPPDomainInfoCmd theMessage = (EPPDomainInfoCmd) aEvent.getMessage();
		
		cat.debug("doDomainInfo: command = [" + theMessage + "]");		
		
		// Launch Domain Info Command?
		if (theMessage.hasExtension(EPPLaunchInfo.class)) {
			EPPEventResponse launchResp = launchDomainHandler.doDomainInfo(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) launchResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return launchResp;
		}
		
		// Related Domain Info Command?
		if (theMessage.hasExtension(EPPRelatedDomainExtInfo.class)) {
			EPPEventResponse relatedDomainResp = relatedDomainHandler.doDomainInfo(aEvent, aData);
			
			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) relatedDomainResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return relatedDomainResp;
		}
		
		
		
		// .name domain?
		if (theMessage.getName().endsWith(".name")) {
			EPPEventResponse persRegResp = pergRegDomainHandler
					.doDomainInfo(aEvent, aData);

			// Mirror NameStore Extension in response
			if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
				((EPPResponse) persRegResp.getResponse())
						.addExtension(theMessage
								.getExtension(EPPNamestoreExtNamestoreExt.class));
			}
			
			return persRegResp;
		}
		

		// EPPDomainInfo Response requires a vector of status
		Vector statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));
		statuses.addElement(new EPPDomainStatus(
				EPPDomainStatus.ELM_STATUS_CLIENT_HOLD));

		EPPTransId transId = new EPPTransId(theMessage.getTransId(), svrTransId);

		// Required EPPDomainInfoResp attributes.
		// trans id, domain name, roid, client id, statuses, created by id,
		// expiration date, Auth Info
		EPPDomainInfoResp theResponse = new EPPDomainInfoResp(transId, roid,
				"example.com", "ClientX", statuses, "ClientY", new Date(),
				new EPPAuthInfo("2fooBAR"));

		// Add Name Servers
		Vector theNses = new Vector();
		theNses.addElement("ns1.example.com");
		theNses.addElement("ns2.example.com");
		theResponse.setNses(theNses);

		// Add Subordinate Hosts
		Vector theHosts = new Vector();
		theHosts.addElement("ns1.example.com");
		theHosts.addElement("ns2.example.com");
		theResponse.setHosts(theHosts);

		// Add Contacts
		Vector theContacts = new Vector();
		theContacts.addElement(new EPPDomainContact("sh8013",
				EPPDomainContact.TYPE_ADMINISTRATIVE));
		theContacts.addElement(new EPPDomainContact("sh8013",
				EPPDomainContact.TYPE_BILLING));
		theContacts.addElement(new EPPDomainContact("sh8013",
				EPPDomainContact.TYPE_TECHNICAL));
		theResponse.setContacts(theContacts);

		// Set the expiration date to today plus one year
		GregorianCalendar theCal = new GregorianCalendar();
		theCal.setTime(new Date());
		theCal.add(Calendar.YEAR, 1);

		theResponse.setExpirationDate(theCal.getTime());

		// Set the Registrant
		theResponse.setRegistrant("jd1234");

		theResponse.setResult(EPPResult.SUCCESS);

		// Mirror NameStore Extension in response
		if (theMessage.hasExtension(EPPNamestoreExtNamestoreExt.class)) {
			theResponse.addExtension(theMessage
					.getExtension(EPPNamestoreExtNamestoreExt.class));
		}

		// Add EPPRgpExtUpData extension if domain is pendingrestore.com
		if (theMessage.getName().equals("pendingrestore.com")) {
			theResponse.addExtension(new EPPRgpExtInfData(new EPPRgpExtStatus(
					EPPRgpExtStatus.PENDING_RESTORE)));
		}

		EPPWhoisInf theWhoisExt = (EPPWhoisInf) theMessage
				.getExtension(EPPWhoisInf.class);

		// Add extension to response if command extension was passed,
		// the extension flag is true and the response is a successful response.
		if (theWhoisExt != null && theWhoisExt.getFlag().booleanValue()
				&& theResponse.isSuccess()) {
			theResponse.addExtension(new EPPWhoisInfData(
					"Example Registrar Inc.", "whois.example.com",
					"http://www.example.com", "iris.example.com"));
		}

		// Add secdns extension in info response if domain is secdns.com
		if (theMessage.getName().equalsIgnoreCase("secdns.com")) {
			// instantiate a secDNS:keyData object
			EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
			keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
			keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
			keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
			keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
					+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
					+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
					+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
					+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");

			// instantiate another secDNS:keyData object
			EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
					EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
					EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
					EPPSecDNSAlgorithm.RSASHA1,
					"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
							+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
							+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
							+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
							+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");

			// instantiate a secDNS:dsData object
			EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
			dsData.setKeyTag(34095);
			dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
			dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
			dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
			dsData.setKeyData(keyData);

			// instantiate another secDNS:dsData object
			EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(10563,
					EPPSecDNSAlgorithm.RSASHA1,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
					"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2);

			// instantiate the secDNS:infData object
			EPPSecDNSExtInfData infData = new EPPSecDNSExtInfData();
			Vector dsDataVec = new Vector();
			dsDataVec.add(dsData);
			infData.setDsData(dsDataVec);
			infData.appendDsData(dsData2);

			// set the secDNS:infData in the response as an extension
			theResponse.addExtension(infData);
		}

		// Add coa extension in info response if domain is coa.com
		if (theMessage.getName().equalsIgnoreCase("coa-full-info-owned.com")) {

			EPPCoaExtKey key = new EPPCoaExtKey("KEY1");
			EPPCoaExtValue value = new EPPCoaExtValue("value1");
			EPPCoaExtAttr attr = new EPPCoaExtAttr();
			attr.setKey(key);
			attr.setValue(value);
			EPPCoaExtInfData coaInfData = new EPPCoaExtInfData();
			coaInfData.appendAttr(attr);

			theResponse.addExtension(coaInfData);

		}

		if (theMessage.getName().equalsIgnoreCase("graceperiod.com")) {
			EPPRgpExtInfData rgpExt = new EPPRgpExtInfData();

			// Auto Renew Period
			EPPRgpExtStatus autoRenewPeriodStatus = new EPPRgpExtStatus(
					EPPRgpExtStatus.AUTO_RENEW_PERIOD);
			autoRenewPeriodStatus.setMessage("endDate="
					+ EPPUtil.encodeTimeInstant(new Date()));
			rgpExt.addStatus(autoRenewPeriodStatus);

			// Renew Period
			EPPRgpExtStatus renewPeriodStatus = new EPPRgpExtStatus(
					EPPRgpExtStatus.RENEW_PERIOD);
			renewPeriodStatus.setMessage("endDate="
					+ EPPUtil.encodeTimeInstant(new Date()));
			rgpExt.addStatus(renewPeriodStatus);

			theResponse.addExtension(rgpExt);
		}

		if (theMessage.getName().equalsIgnoreCase("pendingperiod.com")) {
			// Set EPP status to pendingDelete
			statuses = new Vector();
			statuses.addElement(new EPPDomainStatus(
					EPPDomainStatus.ELM_STATUS_PENDING_DELETE));
			theResponse.setStatuses(statuses);

			// Add RGP redemptionPeriod status
			EPPRgpExtInfData rgpExt = new EPPRgpExtInfData();
			rgpExt.addStatus(new EPPRgpExtStatus(
					EPPRgpExtStatus.REDEMPTION_PERIOD));
			theResponse.addExtension(rgpExt);
		}

		// Determine what version of the secDNS extension is supported
		SessionData theSessionData = (SessionData) aData;
		boolean hasV10ExtService = false;
		boolean hasV11ExtService = false;
		Enumeration extSvcEnum = theSessionData.getLoginCmd()
				.getExtensionServices().elements();
		while (extSvcEnum.hasMoreElements()) {
			EPPService theExtService = (EPPService) extSvcEnum.nextElement();

			if (theExtService
					.getNamespaceURI()
					.equals(com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtFactory.NS)) {
				hasV10ExtService = true;
			}
			else if (theExtService
					.getNamespaceURI()
					.equals(com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtFactory.NS)) {
				hasV11ExtService = true;
			}
		}

		// Version 1.1 specified? This applies if both version 1.0 and 1.1 was
		// specified.
		if (hasV11ExtService) {
			theResponse = this.v11SubHandler.doDomainInfo(theMessage,
					theResponse, aData);
		} // Version 1.0 specified only?
		else if (hasV10ExtService) {
			theResponse = this.v10SubHandler.doDomainInfo(theMessage,
					theResponse, aData);
		} // No secDNS extension URI specified in login
		else {
			cat.info("NSDomainHandler.doDomainInfo: no secDNS extension URI specified in login");
		}


		return new EPPEventResponse(theResponse);
	}
}
