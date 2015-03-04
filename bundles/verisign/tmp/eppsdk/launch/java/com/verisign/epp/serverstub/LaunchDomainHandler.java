/***********************************************************
Copyright (C) 2011 VeriSign, Inc.

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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertStore;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainPendActionMsg;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.launch.EPPLaunchCheck;
import com.verisign.epp.codec.launch.EPPLaunchCheckResult;
import com.verisign.epp.codec.launch.EPPLaunchChkData;
import com.verisign.epp.codec.launch.EPPLaunchCodeMark;
import com.verisign.epp.codec.launch.EPPLaunchCreData;
import com.verisign.epp.codec.launch.EPPLaunchCreate;
import com.verisign.epp.codec.launch.EPPLaunchDelete;
import com.verisign.epp.codec.launch.EPPLaunchInfData;
import com.verisign.epp.codec.launch.EPPLaunchInfo;
import com.verisign.epp.codec.launch.EPPLaunchNotice;
import com.verisign.epp.codec.launch.EPPLaunchPhase;
import com.verisign.epp.codec.launch.EPPLaunchStatus;
import com.verisign.epp.codec.launch.EPPLaunchUpdate;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.codec.mark.EPPMarkAddress;
import com.verisign.epp.codec.mark.EPPMarkContact;
import com.verisign.epp.codec.mark.EPPTrademark;
import com.verisign.epp.codec.signedMark.EPPEncodedSignedMark;
import com.verisign.epp.codec.signedMark.EPPSignedMark;
import com.verisign.epp.codec.signedMark.SMDRevocationList;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;
import com.verisign.epp.framework.EPPPollQueueException;
import com.verisign.epp.framework.EPPPollQueueMgr;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.Environment;

/**
 * Extension to the standard <code>DomainHandler</code> that looks for the
 * <code>EPPLaunchInf</code> command extension with the info command and adds
 * the <code>EPPLaunchInfData</code> extension to the response.
 */
public class LaunchDomainHandler extends DomainHandler {

	/** Logging category */
	private static Logger cat = Logger.getLogger(LaunchDomainHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * PKIX parameters used to validate the certificate path in signed mark.
	 */
	private PKIXParameters pkixParameters;

	/**
	 * SMD revocation list
	 */
	private SMDRevocationList smdRevocationList = new SMDRevocationList();

	/**
	 * Constructs an instance of LaunchDomainHandler
	 */
	public LaunchDomainHandler() {
		cat.debug("LaunchDomainHandler.LaunchDomainHandler(): enter");

		// Load the PKIXParameters using the truststore and the optional CRL
		// file
		String truststore = Environment
				.getProperty("EPP.SignedMark.truststore");
		if (truststore == null) {
			cat.error("LaunchDomainHandler.LaunchDomainHandler(): EPP.SignedMark.truststore NOT defined in configuration");
			System.err
					.println("LaunchDomainHandler.LaunchDomainHandler(): EPP.SignedMark.truststore NOT defined in configuration");
			System.exit(1);
		}

		cat.debug("LaunchDomainHandler.LaunchDomainHandler(): Signed Mark Truststore = " + truststore);

		// Load the CRL's
		String crlsProp = Environment.getOption("EPP.SignedMark.crls");

		List<String> crls = new ArrayList<String>();

		if (crlsProp != null) {
			StringTokenizer tokenizer = new StringTokenizer(crlsProp, ",");
			while (tokenizer.hasMoreTokens()) {
				crls.add(tokenizer.nextToken());
			}
		}

		try {
			this.pkixParameters = this.loadPKIXParameters(truststore, crls);
		}
		catch (Exception ex) {
			cat.error("LaunchDomainHandler.LaunchDomainHandler(): Error loading the public key: "
					+ ex);
			ex.printStackTrace();
			System.exit(1);
		}

		// Initialize the SMD revocation list
		String revocationListProp = Environment
				.getOption("EPP.SignedMark.revocationList");

		if (revocationListProp == null) {

			File smdRevocationListFile = new File(revocationListProp);

			if (smdRevocationListFile.exists()) {

				try {
					FileInputStream smdRevocationListStream = new FileInputStream(
							smdRevocationListFile);
					this.smdRevocationList.decode(smdRevocationListStream);
				}
				catch (Exception ex) {
					cat.error("LaunchDomainHandler.LaunchDomainHandler(): Error loading SMD revocation list : "
							+ ex);
					ex.printStackTrace();
					System.exit(1);
				}

				cat.debug("LaunchDomainHandler.LaunchDomainHandler(): No SMD revocation list found");

			}
			else {
				cat.error("LaunchDomainHandler.LaunchDomainHandler(): Error finding SMD revocation list \""
						+ revocationListProp + "\"");
				System.exit(1);
			}
		}
		else {
			cat.debug("LaunchDomainHandler.LaunchDomainHandler(): EPP.SignedMark.revocationList property not defined");
		}

		cat.debug("LaunchDomainHandler.LaunchDomainHandler(): exit");
	}

	/**
	 * Loads the trust store file and the Certificate Revocation List (CRL) file
	 * into the <code>PKIXParameters</code> used to verify the certificate chain
	 * and verify the certificate against the CRL. Both the Java Trust Store is
	 * loaded with the trusted root CA certificates (trust anchors) and the CRL
	 * file is attempted to be loaded to identify the revoked certificates. If
	 * the CRL file is not found, then no CRL checking will be done.
	 * 
	 * @param aTrustStoreName
	 *            Trust store file name
	 * @param aCrls
	 *            List of Certificate Revocation List (CRL) file names
	 * 
	 * @return Initialized <code>PKIXParameters</code> instance.
	 * 
	 * @throws Exception
	 *             Error initializing the PKIX parameters
	 */
	private PKIXParameters loadPKIXParameters(String aTrustStoreName,
			List<String> aCrls) throws Exception {
		cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): enter");

		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream trustStoreFile = new FileInputStream(aTrustStoreName);
		trustStore.load(trustStoreFile, null);
		trustStoreFile.close();
		cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): truststore = " + aTrustStoreName);
		PKIXParameters pkixParameters = new PKIXParameters(trustStore);

		CertificateFactory certFactory = CertificateFactory
				.getInstance("X.509");

		Collection crlContentsList = new ArrayList();

		for (String currCrl : aCrls) {
			File crlFile = new File(currCrl);
			if (crlFile.exists()) {
				InputStream inStream = null;

				try {
					cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): adding CRL " + currCrl);
					inStream = new FileInputStream(currCrl);
					crlContentsList.add(certFactory.generateCRL(inStream));
				}
				finally {
					if (inStream != null) {
						inStream.close();
					}
				}
			}
			else {
				throw new EPPException("CRL file " + currCrl
						+ " does not exist.");
			}

		}

		// At least 1 CRL was loaded
		if (crlContentsList.size() != 0) {

			List<CertStore> certStores = new ArrayList<CertStore>();
			certStores.add(CertStore.getInstance("Collection",
					new CollectionCertStoreParameters(crlContentsList)));

			pkixParameters.setCertStores(certStores);
			pkixParameters.setRevocationEnabled(true);
			cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): Revocation enabled");
		}
		else {
			pkixParameters.setRevocationEnabled(false);
			cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): Revocation disabled");
		}

		cat.debug("LaunchDomainHandler.loadPKIXParameters(String, String): exit");
		return pkixParameters;
	}

	/**
	 * Create an error <code>EPPEventResponse</code> to return from one of the
	 * handler methods.
	 * 
	 * @param aCode
	 *            <code>EPPResult</code> error constants
	 * @param aClientTransId
	 *            Optional client transaction identifier. Set to
	 *            <code>null</code> if there is no client transaction
	 *            identifier.
	 * @param aReason
	 *            Free-form text reason for the error.
	 * @return <code>EPPEventResponse</code> instance to return from one of the
	 *         handler methods.
	 */
	private EPPEventResponse returnError(int aCode, String aClientTransId,
			String aReason) {

		cat.error("LaunchDomainHandler.returnError: code = " + aCode
				+ ", reason = " + aReason);
		EPPResponse theResponse = new EPPResponse();
		EPPResult theResult = new EPPResult(aCode);
		theResult.addExtValueReason(aReason);
		theResponse.setResult(theResult);
		theResponse.setTransId(new EPPTransId(aClientTransId, "54321-XYZ"));
		return new EPPEventResponse(theResponse);
	}

	/**
	 * Invoked when a Domain Check command is received that includes support for
	 * the Claims Check Command with the {@link EPPLaunchCheck} extension.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>LaunchDomainHandler</code>
	 * 
	 * @return The <code>EPPEventResponse</code> that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCheck(EPPEvent aEvent, Object aData) {
		cat.debug("LaunchDomainHandler.doDomainCheck: enter");

		EPPEventResponse theEventResponse;

		EPPDomainCheckCmd theCommand = (EPPDomainCheckCmd) aEvent.getMessage();

		// EPPLaunchCheck Extension provided?
		if (theCommand.hasExtension(EPPLaunchCheck.class)) {

			EPPLaunchCheck theLaunchCheck = (EPPLaunchCheck) theCommand
					.getExtension(EPPLaunchCheck.class);

			// Claims Check Command?
			if (!theLaunchCheck.hasType()
					|| theLaunchCheck.getType().equals(
							EPPLaunchCheck.TYPE_CLAIMS)) {

				cat.debug("LaunchDomainHandler.doDomainCheck: Launch Claims Check Form for Phase = "
						+ theLaunchCheck.getPhase());

				// Get phase
				String phase = theLaunchCheck.getPhase().getPhase();

				// Claims Check Command?
				if (phase.equals(EPPLaunchPhase.PHASE_CLAIMS)) {

					EPPResponse theResponse = new EPPResponse(new EPPTransId(
							theCommand.getTransId(), "54321-XYZ"));

					EPPLaunchChkData theExt = new EPPLaunchChkData(
							theLaunchCheck.getPhase());

					boolean exists = true;
					boolean validatorId = true;

					Vector vDomainNames = theCommand.getNames();
					Enumeration eDomainNames = vDomainNames.elements();

					// Anymore domain names?
					while (eDomainNames.hasMoreElements()) {
						String domainName = (String) eDomainNames.nextElement();

						// Is there a matching mark for domain name?
						if (exists) {
							
							// Include validatorID attribute?
							if (validatorId) {
							theExt.addCheckResult(new EPPLaunchCheckResult(
									domainName, true, Base64
											.encodeBase64String(domainName
													.getBytes()), "tmch"));
							}
							else {
								theExt.addCheckResult(new EPPLaunchCheckResult(
										domainName, true, Base64
												.encodeBase64String(domainName
														.getBytes())));
							}
							
							validatorId = !validatorId;
							
						} // No matching mark for domain name
						else {
							theExt.addCheckResult(new EPPLaunchCheckResult(
									domainName, false));
						}
						
						exists = !exists;
					}

					theResponse.addExtension(theExt);
					theEventResponse = new EPPEventResponse(theResponse);

				} // Unsupported phase
				else {
					return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
							theCommand.getTransId(),
							"Unsupported check Phase = " + phase);
				}

			} // Available Check Form with passed phase
			else {
				cat.debug("LaunchDomainHandler.doDomainCheck: Launch Availability Check Form for Phase = "
						+ theLaunchCheck.getPhase());

				theEventResponse = super.doDomainCheck(aEvent, aData);

			}

		}
		else { // Domain Check Command
			theEventResponse = super.doDomainCheck(aEvent, aData);
		}

		cat.debug("LaunchDomainHandler.doDomainCheck: exit");
		return theEventResponse;
	}

	/**
	 * Invoked when a Domain Info command is received that includes support for
	 * the {@link EPPLaunchInfo} extension. When the {@link EPPLaunchInfo}
	 * extension is passed, the phases <code>EPPLaunchPhase.PHASE_SUNRISE</code>
	 * and <code>EPPLaunchPhase.PHASE_LANDRUSH</code> is supported in returning
	 * a Domain Info Response with a {@link EPPLaunchInfData} extension.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>LaunchDomainHandler</code>
	 * 
	 * @return The <code>EPPEventResponse</code> that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainInfo(EPPEvent aEvent, Object aData) {
		cat.debug("LaunchDomainHandler.doDomainInfo: enter");

		EPPLaunchInfData theRespExt = null;

		EPPDomainInfoCmd theCommand = (EPPDomainInfoCmd) aEvent.getMessage();

		// Extension provided?
		if (theCommand.hasExtension(EPPLaunchInfo.class)) {

			EPPLaunchInfo theCmdExt = (EPPLaunchInfo) theCommand
					.getExtension(EPPLaunchInfo.class);

			// Get attributes
			String applicationId = theCmdExt.getApplicationId();
			String phase = theCmdExt.getPhase().getPhase();

			// Sunrise phase?
			if (phase.equals(EPPLaunchPhase.PHASE_SUNRISE)) {

				theRespExt = new EPPLaunchInfData();

				theRespExt.setPhase(EPPLaunchPhase.PHASE_SUNRISE);

				// Include mark in response?
				if (theCmdExt.isIncludeMark()) {
					// Trademark
					EPPTrademark trademark = new EPPTrademark();
					trademark.setId("1234-2");
					trademark.setName("Example One");
					trademark.setJurisdiction("US");
					trademark.addClass("35");
					trademark.addClass("36");
					trademark.addLabel("example-one");
					trademark.addLabel("exampleone");
					trademark
							.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
					trademark.setRegNum("234235");
					trademark.setRegDate(new GregorianCalendar(2009, 8, 16)
							.getTime());
					trademark.setExDate(new GregorianCalendar(2015, 8, 16)
							.getTime());

					// Mark Owner
					EPPMarkContact holder = new EPPMarkContact();
					holder.setOrg("Example Inc.");
					// Address
					EPPMarkAddress holderAddress = new EPPMarkAddress();
					holderAddress.addStreet("123 Example Dr.");
					holderAddress.addStreet("Suite 100");
					holderAddress.setCity("Reston");
					holderAddress.setSp("VA");
					holderAddress.setPc("20190");
					holderAddress.setCc("US");
					holder.setAddress(holderAddress);
					trademark.addHolder(holder);

					// Mark Contact
					EPPMarkContact contact = new EPPMarkContact();
					contact.setName("John Doe");
					contact.setOrg("Example Inc."); // Address
					EPPMarkAddress contactAddress = new EPPMarkAddress();
					contactAddress.addStreet("123 Example Dr.");
					contactAddress.addStreet("Suite 100");
					contactAddress.setCity("Reston");
					contactAddress.setSp("VA");
					contactAddress.setPc("20166-6503");
					contactAddress.setCc("US");
					contact.setAddress(contactAddress);
					contact.setVoice("+1.7035555555");
					contact.setVoiceExt("1234");
					contact.setFax("+1.7035555556");
					contact.setEmail("jdoe@example.tld");
					trademark.addContact(contact);

					// Mark
					EPPMark mark = new EPPMark();
					mark.addTrademark(trademark);

					theRespExt.addMark(mark);
				}

				// Sunrise application?
				if (applicationId != null) {
					// Add application attributes to response
					theRespExt
							.setStatus(EPPLaunchStatus.STATUS_PENDING_VALIDATION);
					theRespExt.setApplicationId(applicationId);
				}

			} // Landrush phase?
			else if (phase.equals(EPPLaunchPhase.PHASE_LANDRUSH)) {

				theRespExt = new EPPLaunchInfData();

				theRespExt.setPhase(EPPLaunchPhase.PHASE_LANDRUSH);

				if (applicationId != null) {
					// Add application attributes to response
					theRespExt
							.setStatus(EPPLaunchStatus.STATUS_PENDING_VALIDATION);
					theRespExt.setApplicationId(applicationId);
				}
				else {
					return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
							theCommand.getTransId(), "Info with "
									+ EPPLaunchPhase.PHASE_LANDRUSH
									+ " phase must include applicationId");
				}

			} // Unsupported phase
			else {
				return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
						theCommand.getTransId(), "Unsupported Info Phase = "
								+ phase);
			}

		}
		else {
			cat.debug("LaunchDomainHandler.doDomainInfo: No EPPLaunchInfo extension passed");
		}

		EPPEventResponse theEventResponse = super.doDomainInfo(aEvent, aData);
		EPPResponse theResponse = (EPPResponse) theEventResponse.getResponse();
		if (theRespExt != null) {
			theResponse.addExtension(theRespExt);
		}

		cat.debug("LaunchDomainHandler.doDomainInfo: exit");
		return theEventResponse;
	}

	/**
	 * Invoked when a Domain Create command is received that includes support
	 * for the {@link EPPLaunchCreate} extension. When the
	 * {@link EPPLaunchCreate} two different forms are supported including:<br>
	 * <br>
	 * <ol>
	 * <li>Sunrise Create Form - Supported when the phase is set to
	 * <code>EPPLaunchPhase.PHASE_SUNRISE</code>. The domain name passed will
	 * drive the type of sunrise used, where if a domain name starts with "app"
	 * it will treat it as a sunrise application in <code>pendingCreate</code>
	 * status and with an application identifier returned. If a domain name
	 * starts with "reg" it will treat it as a sunrise registration in
	 * <code>ok</code> status and without an application identifier returned.
	 * This form supports the passing of mark information provided either one of
	 * the models:<br>
	 * <br>
	 * <ol>
	 * <li>codeMark - Passing a code, passing a mark, or passing both a code and
	 * a mark.
	 * <li>signedMark - Passing a digitally signed mark.
	 * <li>encodedSignedMark - Passing a Base64 encoded digitally signed mark.
	 * </ol>
	 * <li>Claims Create Form - Supported when the phase is set to
	 * <code>EPPLaunchPhase.PHASE_CLAIMS1</code> or
	 * <code>EPPLaunchPhase.PHASE_CLAIMS2</code> along with the claims notice
	 * information using the &lt;launch:notice&gt; element.
	 * </ol>
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this <code>
	 *            LaunchDomainHandler</code>
	 * 
	 * @return The <code>EPPEventResponse</code> that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainCreate(EPPEvent aEvent, Object aData) {
		cat.debug("LaunchDomainHandler.doDomainCreate: enter");

		EPPEventResponse theEventResponse = null;

		EPPDomainCreateCmd theCommand = (EPPDomainCreateCmd) aEvent
				.getMessage();

		// Extension provided?
		if (theCommand.hasExtension(EPPLaunchCreate.class)) {

			EPPLaunchCreate theCmdExt = (EPPLaunchCreate) theCommand
					.getExtension(EPPLaunchCreate.class);

			String phase = theCmdExt.getPhase().getPhase();

			boolean isApp = (new Random()).nextBoolean();

			// Is the type defined?
			if (theCmdExt.hasType()) {

				// Application?
				if (theCmdExt.getType()
						.equals(EPPLaunchCreate.TYPE_APPLICATION)) {
					isApp = true;
				} // Registration
				else {
					isApp = false;
				}
			}

			// Sunrise Create Form
			if (phase.equals(EPPLaunchPhase.PHASE_SUNRISE)) {

				String domainName = theCommand.getName().toUpperCase();

				// codeMarks passed?
				if (theCmdExt.hasCodeMarks()) {
					// This could return an error if the server does not support
					// this model.

					List<EPPLaunchCodeMark> codeMarks = theCmdExt
							.getCodeMarks();

					// The server could check the number of codeMarks passed
					// against it's policy
					if (codeMarks.size() == 0) {
						return this.returnError(
								EPPResult.PARAM_VALUE_POLICY_ERROR,
								theCommand.getTransId(),
								"No codeMarks provided");
					}

					for (EPPLaunchCodeMark codeMark : codeMarks) {

						if (codeMark.hasCodeMark()) {
							cat.debug("LaunchDomainHandler.doDomainCreate: code = ["
									+ codeMark.getCode() + "]");
							cat.debug("LaunchDomainHandler.doDomainCreate: mark = ["
									+ codeMark.getMark() + "]");

						}
						else if (codeMark.hasCode()) {
							cat.debug("LaunchDomainHandler.doDomainCreate: code = ["
									+ codeMark.getCode() + "]");
						}
						else if (codeMark.hasMark()) {
							cat.debug("LaunchDomainHandler.doDomainCreate: mark = ["
									+ codeMark.getMark() + "]");
						}
						else {
							return this
									.returnError(EPPResult.PARAM_OUT_OF_RANGE,
											theCommand.getTransId(),
											"codeMark does not having any codes or marks");

						}
					}

				} // Signed marks passed?
				else if (theCmdExt.hasSignedMarks()) {
					
					

					List<EPPSignedMark> signedMarks = theCmdExt
							.getSignedMarks();

					// The server could check the number of codeMarks passed
					// against it's policy
					if (signedMarks.size() == 0) {
						return this.returnError(
								EPPResult.PARAM_VALUE_POLICY_ERROR,
								theCommand.getTransId(),
								"No signed marks provided");
					}
					
					// If signed marks were passed via XML instead of Base64
					// encoded?
					if (!(signedMarks.get(0) instanceof EPPEncodedSignedMark)
							&& (aData != null)
							&& (aData instanceof SessionData)
							&& ((SessionData) aData).getAttribute("PACKET") != null) {
						// Re-parse the XML without normalization and set the
						// signed marks for validation
						EPPSchemaCachingParser theParser = new EPPSchemaCachingParser();
						try {
							theParser.setFeature(
									EPPSchemaCachingParser.NORMALIZE_DATA,
									false);
							
							SessionData sessionData = (SessionData) aData;
														
							byte[] packet = (byte []) sessionData.getAttribute("PACKET");
							
							cat.debug("Command with signed mark = [" + new String(packet) + "]");

							Document theDoc = theParser
									.parse(new ByteArrayInputStream(packet));
							EPPDomainCreateCmd theCreateCmd = (EPPDomainCreateCmd) EPPCodec
									.getInstance().decode(theDoc);

							signedMarks = ((EPPLaunchCreate) theCreateCmd
									.getExtension(EPPLaunchCreate.class))
									.getSignedMarks();
						}
						catch (Exception e) {
							cat.error("LaunchDomainHandler.doDomainCreate: Error decoding XML signed mark: "
									+ e);
							return this.returnError(EPPResult.COMMAND_FAILED,
									theCommand.getTransId(),
									"Error decoding XML signed mark");
						}

					}

					// For each signed mark
					for (EPPSignedMark signedMark : signedMarks) {

						
						cat.debug("LaunchDomainHandler.doDomainCreate: signedMark = ["
								+ signedMark + "]");
						
						Document doc = new DocumentImpl();
						String signedMarkXML;
						try {
							signedMarkXML = EPPUtil.toStringNoIndent(signedMark
									.encode(doc));
						}
						catch (EPPEncodeException e) {
							cat.error("LaunchDomainHandler.doDomainCreate: Error re-encoding signed mark: "
									+ e);
							return this.returnError(EPPResult.COMMAND_FAILED,
									theCommand.getTransId(),
									"Error re-encoding signed mark");
						}
						System.out
								.println("\nLaunchDomainHandler.doDomainCreate: signed mark = \n"
										+ signedMarkXML);
						System.out
								.println("\nLaunchDomainHandler.doDomainCreate: base64 signed mark = \n"
										+ new String(Base64.encodeBase64(
												signedMarkXML.getBytes(), true)));						

						// Signature valid?
						if (signedMark.validate(this.pkixParameters)) {
							cat.debug("LaunchDomainHandler.doDomainCreate: Signature is valid");
						}
						else {
							cat.error("LaunchDomainHandler.doDomainCreate: Signature is NOT valid");
							return this.returnError(
									EPPResult.PARAM_VALUE_POLICY_ERROR,
									theCommand.getTransId(),
									"Signed mark signature invalid");
						}
						
						// SMD is not revoked?
						if (!this.smdRevocationList.isRevoked(signedMark)) {
							cat.debug("LaunchDomainHandler.doDomainCreate: signed mark is not revoked");
						}
						else {
							cat.debug("LaunchDomainHandler.doDomainCreate: signed mark is revoked");
							return this.returnError(
									EPPResult.PARAM_VALUE_POLICY_ERROR,
									theCommand.getTransId(),
									"Signed mark is revoked");
						}
						

					}

				}
				else {
					return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
							theCommand.getTransId(),
							"No mark information provided");
				}

				theEventResponse = super.doDomainCreate(aEvent, aData);
				EPPResponse theResponse = (EPPResponse) theEventResponse
						.getResponse();

				// Is application?
				if (isApp) {
					// Add extension to response and set to pendingCreate

					// Generate unique application identifier
					Random random = new Random();
					String applicationId = "" + System.currentTimeMillis()
							+ "-" + random.nextInt();

					EPPLaunchCreData respExt = new EPPLaunchCreData(
							theCmdExt.getPhase(), applicationId);

					theResponse.addExtension(respExt);
					theResponse.setResult(EPPResult.SUCCESS_PENDING);

					try {

						EPPTransId transId = new EPPTransId(
								theCommand.getTransId(), "54322-XYZ");

						// Insert required poll messages
						if (theCommand.getName().equals(
								"APPPENDINGALLOCATION.TLD")) {

							// "validated" poll message
							EPPDomainInfoResp thePollMsg = new EPPDomainInfoResp();
							thePollMsg.setTransId(transId);
							thePollMsg.setName(theCommand.getName());
							thePollMsg.setRoid("EXAMPLE1-REP");
							thePollMsg.setClientId("ClientX");

							thePollMsg.addExtension(new EPPLaunchInfData(
									theCmdExt.getPhase(), applicationId,
									new EPPLaunchStatus(
											EPPLaunchStatus.STATUS_VALIDATED)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND, thePollMsg, null);

							// "pendingAllocation" poll message
							thePollMsg = new EPPDomainInfoResp();
							thePollMsg.setTransId(transId);
							thePollMsg.setName(theCommand.getName());
							thePollMsg.setRoid("EXAMPLE1-REP");
							thePollMsg.setClientId("ClientX");

							thePollMsg
									.addExtension(new EPPLaunchInfData(
											theCmdExt.getPhase(),
											applicationId,
											new EPPLaunchStatus(
													EPPLaunchStatus.STATUS_PENDING_ALLOCATION)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND, thePollMsg, null);
						}
						else if (theCommand.getName().equals(
								"APPREJECTEDINVALID.TLD")) {

							// "invalid" poll message
							EPPDomainInfoResp thePollMsg = new EPPDomainInfoResp();
							thePollMsg.setTransId(transId);
							thePollMsg.setName(theCommand.getName());
							thePollMsg.setRoid("EXAMPLE1-REP");
							thePollMsg.setClientId("ClientX");

							thePollMsg.addExtension(new EPPLaunchInfData(
									theCmdExt.getPhase(), applicationId,
									new EPPLaunchStatus(
											EPPLaunchStatus.STATUS_INVALID)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND, thePollMsg, null);

							// "rejected" poll message
							EPPResponse cmdResp = (EPPResponse) theEventResponse
									.getResponse();
							EPPTransId cmdRespTransId = (EPPTransId) cmdResp
									.getTransId().clone();
							EPPDomainPendActionMsg thePendActionPollMsg = new EPPDomainPendActionMsg(
									transId, theCommand.getName(), false,
									cmdRespTransId, new Date());

							thePendActionPollMsg
									.addExtension(new EPPLaunchInfData(
											theCmdExt.getPhase(),
											applicationId,
											new EPPLaunchStatus(
													EPPLaunchStatus.STATUS_REJECTED)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND,
									thePendActionPollMsg, null);

						}
						else if (theCommand.getName().equals(
								"APPALLOCATEDALLSTATES.TLD")) {

							// "validated" poll message
							EPPDomainInfoResp thePollMsg = new EPPDomainInfoResp();
							thePollMsg.setName(theCommand.getName());
							thePollMsg.setRoid("EXAMPLE1-REP");
							thePollMsg.setClientId("ClientX");

							thePollMsg.addExtension(new EPPLaunchInfData(
									theCmdExt.getPhase(), applicationId,
									new EPPLaunchStatus(
											EPPLaunchStatus.STATUS_VALIDATED)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND, thePollMsg, null);

							// "pendingAllocation" poll message
							thePollMsg = new EPPDomainInfoResp();
							thePollMsg.setName(theCommand.getName());
							thePollMsg.setRoid("EXAMPLE1-REP");
							thePollMsg.setClientId("ClientX");

							thePollMsg
									.addExtension(new EPPLaunchInfData(
											theCmdExt.getPhase(),
											applicationId,
											new EPPLaunchStatus(
													EPPLaunchStatus.STATUS_PENDING_ALLOCATION)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND, thePollMsg, null);

							// "allocated" poll message
							EPPResponse cmdResp = (EPPResponse) theEventResponse
									.getResponse();
							EPPTransId cmdRespTransId = (EPPTransId) cmdResp
									.getTransId().clone();
							EPPDomainPendActionMsg thePendActionPollMsg = new EPPDomainPendActionMsg(
									transId, theCommand.getName(), true,
									cmdRespTransId, new Date());

							thePendActionPollMsg
									.addExtension(new EPPLaunchInfData(
											theCmdExt.getPhase(),
											applicationId,
											new EPPLaunchStatus(
													EPPLaunchStatus.STATUS_ALLOCATED)));

							EPPPollQueueMgr.getInstance().put(null,
									LaunchPollHandler.KIND,
									thePendActionPollMsg, null);
						}

					}
					catch (EPPPollQueueException ex) {
						return this.returnError(EPPResult.COMMAND_FAILED,
								theCommand.getTransId(), "Poll queue error: "
										+ ex);
					}
					catch (CloneNotSupportedException ex) {
						return this.returnError(EPPResult.COMMAND_FAILED,
								theCommand.getTransId(),
								"Error cloning object: " + ex);
					}
				}

			} // Claims Create Form
			else if (phase.equals(EPPLaunchPhase.PHASE_CLAIMS)) {

				// Notice information included?
				if (theCmdExt.hasNotice()) {

					EPPLaunchNotice notice = theCmdExt.getNotice();

					cat.debug("LaunchDomainHandler.doDomainCreate: notice id = ["
							+ notice.getNoticeId() + "]");
					cat.debug("LaunchDomainHandler.doDomainCreate: notice not after date = ["
							+ EPPUtil.encodeTimeInstant(notice
									.getNotAfterDate()) + "]");
					cat.debug("LaunchDomainHandler.doDomainCreate: notice accepted date = ["
							+ EPPUtil.encodeTimeInstant(notice
									.getAcceptedDate()) + "]");
				}

				// Is application?
				if (isApp) {
					// Generate unique application identifier
					Random random = new Random();
					String applicationId = "" + System.currentTimeMillis()
							+ "-" + random.nextInt();

					EPPLaunchCreData respExt = new EPPLaunchCreData(
							theCmdExt.getPhase(), applicationId);

					theEventResponse = super.doDomainCreate(aEvent, aData);
					EPPResponse theResponse = (EPPResponse) theEventResponse
							.getResponse();
					theResponse.addExtension(respExt);
					theResponse.setResult(EPPResult.SUCCESS_PENDING);
				} // registration
				else {
					theEventResponse = super.doDomainCreate(aEvent, aData);
				}

			}
			else {
				cat.debug("LaunchDomainHandler.doDomainCreate: phase = "
						+ phase);

				// Is application?
				if (isApp) {
					// Generate unique application identifier
					Random random = new Random();
					String applicationId = "" + System.currentTimeMillis()
							+ "-" + random.nextInt();

					EPPLaunchCreData respExt = new EPPLaunchCreData(
							theCmdExt.getPhase(), applicationId);

					theEventResponse = super.doDomainCreate(aEvent, aData);
					EPPResponse theResponse = (EPPResponse) theEventResponse
							.getResponse();
					theResponse.addExtension(respExt);
					theResponse.setResult(EPPResult.SUCCESS_PENDING);
				} // registration
				else {
					theEventResponse = super.doDomainCreate(aEvent, aData);
				}
			}
		}
		else {
			theEventResponse = super.doDomainCreate(aEvent, aData);
		}

		cat.debug("LaunchDomainHandler.doDomainDelete: exit");
		return theEventResponse;
	}

	/**
	 * Invoked when a Domain Delete command is received that includes support
	 * for the {@link EPPLaunchDelete} extension. When the
	 * {@link EPPLaunchDelete} extension is passed, the phases
	 * <code>EPPLaunchPhase.PHASE_SUNRISE</code> and
	 * <code>EPPLaunchPhase.PHASE_LANDRUSH</code> is supported.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>LaunchDomainHandler</code>
	 * 
	 * @return The <code>EPPEventResponse</code> that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainDelete(EPPEvent aEvent, Object aData) {
		cat.debug("LaunchDomainHandler.doDomainDelete: enter");

		EPPDomainDeleteCmd theCommand = (EPPDomainDeleteCmd) aEvent
				.getMessage();

		// Extension provided?
		if (theCommand.hasExtension(EPPLaunchDelete.class)) {

			EPPLaunchDelete theCmdExt = (EPPLaunchDelete) theCommand
					.getExtension(EPPLaunchDelete.class);

			// Get attributes
			String applicationId = theCmdExt.getApplicationId();
			String phase = theCmdExt.getPhase().getPhase();

			// Sunrise or Landrush phase?
			if (phase.equals(EPPLaunchPhase.PHASE_SUNRISE)
					|| phase.equals(EPPLaunchPhase.PHASE_LANDRUSH)) {

				if (applicationId != null) {
					// Delete sunrise or landrush application
					// ADD DELETE LOGIC HERE

					// Create Delete Response (Standard EPPResponse)
					EPPTransId transId = new EPPTransId(
							theCommand.getTransId(), "54321-XYZ");

					EPPResponse theResponse = new EPPResponse(transId);
					theResponse.setResult(EPPResult.SUCCESS);

					cat.debug("LaunchDomainHandler.doDomainDelete: exit - application");
					return new EPPEventResponse(theResponse);
				}
				else {
					return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
							theCommand.getTransId(), "Delete with " + phase
									+ " phase must include applicationId");
				}

			}
			else {
				return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
						theCommand.getTransId(), "Unsupported Delete Phase = "
								+ phase);
			}

		}
		else {
			cat.debug("LaunchDomainHandler.doDomainDelete: No EPPLaunchDelete extension passed");
		}

		EPPEventResponse theEventResponse = super.doDomainDelete(aEvent, aData);

		cat.debug("LaunchDomainHandler.doDomainDelete: exit");
		return theEventResponse;
	}

	/**
	 * Invoked when a Domain Update command is received that includes support
	 * for the {@link EPPLaunchUpdate} extension. When the
	 * {@link EPPLaunchUpdate} extension is passed, the phases
	 * <code>EPPLaunchPhase.PHASE_SUNRISE</code> and
	 * <code>EPPLaunchPhase.PHASE_LANDRUSH</code> is supported.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>LaunchDomainHandler</code>
	 * 
	 * @return The <code>EPPEventResponse</code> that should be sent back to the
	 *         client.
	 */
	protected EPPEventResponse doDomainUpdate(EPPEvent aEvent, Object aData) {
		cat.debug("LaunchDomainHandler.doDomainUpdate: enter");

		EPPDomainUpdateCmd theCommand = (EPPDomainUpdateCmd) aEvent
				.getMessage();

		// Extension provided?
		if (theCommand.hasExtension(EPPLaunchUpdate.class)) {

			EPPLaunchUpdate theCmdExt = (EPPLaunchUpdate) theCommand
					.getExtension(EPPLaunchUpdate.class);

			// Get attributes
			String applicationId = theCmdExt.getApplicationId();
			String phase = theCmdExt.getPhase().getPhase();

			// Sunrise or Landrush phase?
			if (phase.equals(EPPLaunchPhase.PHASE_SUNRISE)
					|| phase.equals(EPPLaunchPhase.PHASE_LANDRUSH)) {

				if (applicationId != null) {
					// Update sunrise or landrush application
					// ADD UPDATE LOGIC HERE

					// Create Update Response (Standard EPPResponse)
					EPPTransId transId = new EPPTransId(
							theCommand.getTransId(), "54321-XYZ");

					EPPResponse theResponse = new EPPResponse(transId);
					theResponse.setResult(EPPResult.SUCCESS);

					cat.debug("LaunchDomainHandler.doDomainUpdate: exit - application");
					return new EPPEventResponse(theResponse);
				}
				else {
					return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
							theCommand.getTransId(), "Update with " + phase
									+ " phase must include applicationId");
				}

			}
			else {
				return this.returnError(EPPResult.PARAM_VALUE_POLICY_ERROR,
						theCommand.getTransId(), "Unsupported Update Phase = "
								+ phase);
			}

		}
		else {
			cat.debug("LaunchDomainHandler.doDomainUpdate: No EPPLaunchUpdate extension passed");
		}

		EPPEventResponse theEventResponse = super.doDomainUpdate(aEvent, aData);

		cat.debug("LaunchDomainHandler.doDomainUpdate: exit");
		return theEventResponse;
	}

}
