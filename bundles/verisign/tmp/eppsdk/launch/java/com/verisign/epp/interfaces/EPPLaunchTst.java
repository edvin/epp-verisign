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

//----------------------------------------------
//
// package
//
//----------------------------------------------
package com.verisign.epp.interfaces;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------

// Java Core Imports
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.PKIXParameters;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;

import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainPendActionMsg;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPUtil;
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
import com.verisign.epp.codec.mark.EPPCourt;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.codec.mark.EPPMarkAddress;
import com.verisign.epp.codec.mark.EPPMarkContact;
import com.verisign.epp.codec.mark.EPPProtection;
import com.verisign.epp.codec.mark.EPPTrademark;
import com.verisign.epp.codec.mark.EPPTreatyOrStatute;
import com.verisign.epp.codec.signedMark.EPPEncodedSignedMark;
import com.verisign.epp.codec.signedMark.EPPIssuer;
import com.verisign.epp.codec.signedMark.EPPSignedMark;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the LaunchPhase Extension classes with the
 * <code>EPPDomain</code> class. The unit test will initialize a session with an
 * EPP Server, will invoke <code>EPPDomain</code> operations with LaunchPhase
 * Extensions, and will end a session with an EPP Server. The configuration file
 * used by the unit test defaults to epp.config, but can be changed by passing
 * the file path as the first command line argument. The unit test can be run in
 * multiple threads by setting the "threads" system property. For example, the
 * unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPLaunchTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */
public class EPPLaunchTst extends TestCase {

	/**
	 * PKIX parameters passed to PKIX <code>CertPathValidator</code> algorithm.
	 */
	private static PKIXParameters pkixParameters;
	
	/**
	 * Root directory containing the sample Signed Mark Data (SMD)'s.  
	 */
	private static String smdsDir;

	/**
	 * Allocates an <code>EPPLaunchTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with the
	 * logical name.
	 * 
	 * @param aName
	 *            Logical name of the test
	 */
	public EPPLaunchTst(String aName) {
		super(aName);
	} // End EPPLaunchTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPLaunchTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPDomain
	 * interface.
	 */
	public void testLaunchPhase() {

		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {

			numIterations = Integer.parseInt(iterationsStr);

		}

		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) {

			printStart("Test Suite");

			launchCreateActiveSMD();
			launchCreateGeneric();
			launchCreateSignedMark();
			launchCreateCodeMark();
			launchCreateNotice();
			launchCheck();
			launchInfo();
			launchUpdate();
			launchDelete();
			launchPollMessaging();

			printEnd("Test Suite");

		}

	} // End EPPLaunchTst.testLaunchPhase()

	/**
	 * Unit test of using using the <code>EPPLaunchCheck</code> Extension with
	 * <code>EPPDomain</code> info command with the following tests:<br>
	 * <br>
	 * <ol>
	 * <li>Claims check with one domain name and implicitly setting the type.
	 * <li>Claims check with two domain names and explicitly setting the type.
	 * <li>Available check with two domain names for the custom "idn-release"
	 * launch phase.
	 * </ol>
	 */
	public void launchCheck() {
		printStart("launchCheck");

		EPPResponse claimsCheckResp;

		try {
			// Claims check with one domain name and implicitly setting the
			// type.
			System.out
					.println("\n----------------------------------------------------------------");

			System.out
					.println("launchCheck: Claims Check for example1.tld with implicit type");

			launch.setTransId("ABC-12345");

			launch.addDomainName("example1.tld");
			launch.setPhase(EPPLaunch.PHASE_CLAIMS);
			claimsCheckResp = launch.sendCheck();

			System.out.println("Response Type = " + claimsCheckResp.getType());

			System.out.println("Response.TransId.ServerTransId = "
					+ claimsCheckResp.getTransId().getServerTransId());

			System.out.println("Response.TransId.ServerTransId = "
					+ claimsCheckResp.getTransId().getClientTransId());

			// Output all of the response attributes
			System.out.println("\nlaunchCheck: Response = [" + claimsCheckResp
					+ "]");

			// No EPPLaunchChkData extension in response?
			if (!claimsCheckResp.hasExtension(EPPLaunchChkData.class)) {
				Assert.fail("launchCheck: No EPPLaunchChkData extension found in claims check response");
			}

			EPPLaunchChkData theExt = (EPPLaunchChkData) claimsCheckResp
					.getExtension(EPPLaunchChkData.class);
			List<EPPLaunchCheckResult> results = theExt.getCheckResults();

			for (EPPLaunchCheckResult result : results) {

				if (result.isExists()) {
					System.out.println("launchCheck: Domain + "
							+ result.getName() + ", mark exists, claimKey = ["
							+ result.getClaimKey() + "]");
				}
				else {
					System.out.println("launchCheck: Domain + "
							+ result.getName() + ", mark DOES NOT exist");
				}

			}

			// Claims check with two domain names and explicitly setting the
			// type.
			System.out
					.println("\n----------------------------------------------------------------");

			System.out
					.println("launchCheck: Claims Check for example1.tld and example2.tld with explicit type");

			launch.setTransId("ABC-12345");

			launch.addDomainName("example1.tld");
			launch.addDomainName("example2.tld");
			launch.setPhase(EPPLaunch.PHASE_CLAIMS);
			launch.setType(EPPLaunch.TYPE_CLAIMS);
			claimsCheckResp = launch.sendCheck();

			System.out.println("Response Type = " + claimsCheckResp.getType());

			System.out.println("Response.TransId.ServerTransId = "
					+ claimsCheckResp.getTransId().getServerTransId());

			System.out.println("Response.TransId.ServerTransId = "
					+ claimsCheckResp.getTransId().getClientTransId());

			// Output all of the response attributes
			System.out.println("\nlaunchCheck: Response = [" + claimsCheckResp
					+ "]");

			// No EPPLaunchChkData extension in response?
			if (!claimsCheckResp.hasExtension(EPPLaunchChkData.class)) {
				Assert.fail("launchCheck: No EPPLaunchChkData extension found in claims check response");
			}

			theExt = (EPPLaunchChkData) claimsCheckResp
					.getExtension(EPPLaunchChkData.class);

			results = theExt.getCheckResults();

			for (EPPLaunchCheckResult result : results) {

				if (result.isExists()) {
					System.out.println("launchCheck: Domain + "
							+ result.getName() + ", mark exists, claimKey = ["
							+ result.getClaimKey() + "]");
				}
				else {
					System.out.println("launchCheck: Domain + "
							+ result.getName() + ", mark DOES NOT exist");
				}

			}

			// Available check with two domain names for the custom
			// "idn-release" launch phase.
			System.out
					.println("\n----------------------------------------------------------------");

			System.out
					.println("launchCheck: Availability Check for example1.tld and example2.tld with idn-release custom phase");

			launch.setTransId("ABC-12345");

			launch.addDomainName("example1.tld");
			launch.addDomainName("example2.tld");
			launch.setPhase(EPPLaunch.PHASE_CUSTOM);
			launch.setPhaseName("idn-release");
			launch.setType(EPPLaunch.TYPE_AVAILABILITY);

			EPPDomainCheckResp availCheckResp = (EPPDomainCheckResp) launch
					.sendCheck();

			// Output all of the response attributes
			System.out.println("\nlaunchCheck: Response = [" + availCheckResp
					+ "]");
		}
		catch (Exception e) {
			handleException(e);
		}

		printEnd("launchCheck");
	}

	/**
	 * Unit test of using using the <code>EPPLaunchInfo</code> Extension with
	 * <code>EPPDomain</code> info command with the following tests:<br>
	 * <br>
	 * <ol>
	 * <li>Info command for sunrise application "abc123"
	 * <li>Info command for sunrise registration
	 * <li>Info command for landrush application
	 * <li>Info command for unsupported landrush registration
	 * <li>Info command for unsupported phase "CLAIMS"
	 * </ol>
	 */
	public void launchInfo() {

		printStart("launchInfo");

		// Try Successful Info

		try {

			EPPDomainInfoResp domainResponse;

			System.out
					.println("launchInfo: Info command for Sunrise Application \"abc123\"");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

			domainResponse = domain.sendInfo();

			// -- Output all of the domain info response attributes
			System.out.println("launchInfo: Sunrise Application Response = ["
					+ domainResponse + "]\n\n");

			if (domainResponse.hasExtension(EPPLaunchInfData.class)) {
				EPPLaunchInfData infData = (EPPLaunchInfData) domainResponse
						.getExtension(EPPLaunchInfData.class);

				if (!infData.getPhase().getPhase()
						.equals(EPPLaunchPhase.PHASE_SUNRISE)) {
					Assert.fail("launchInfo: phase "
							+ infData.getPhase().getPhase() + " != "
							+ EPPLaunchPhase.PHASE_SUNRISE);
				}
				System.out.println("Sunrise Application Id = "
						+ infData.getApplicationId());
				System.out.println("Sunrise Application Status = "
						+ infData.getStatus().getStatus());
				System.out.println("Sunrise Application Mark = "
						+ infData.getMark());
			}

			System.out
					.println("launchInfo: Info command for Sunrise Registration");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE)));

			domainResponse = domain.sendInfo();

			// -- Output all of the domain info response attributes
			System.out.println("launchInfo: Sunrise Registration Response = ["
					+ domainResponse + "]\n\n");

			if (domainResponse.hasExtension(EPPLaunchInfData.class)) {
				EPPLaunchInfData infData = (EPPLaunchInfData) domainResponse
						.getExtension(EPPLaunchInfData.class);

				if (!infData.getPhase().getPhase()
						.equals(EPPLaunchPhase.PHASE_SUNRISE)) {
					Assert.fail("launchInfo: phase "
							+ infData.getPhase().getPhase() + " != "
							+ EPPLaunchPhase.PHASE_SUNRISE);
				}
				if (infData.getApplicationId() != null) {

					Assert.fail("launchInfo: applicationId should be null for sunrise registration");
				}
				if (infData.getStatus() != null) {

					Assert.fail("launchInfo: status should be null for sunrise registration");
				}
				if (infData.getMark() != null) {
					Assert.fail("launchInfo: Mark should be null with includeMark = false");
				}
			}

			System.out
					.println("launchInfo: Info command for Sunrise Registration with includeMark = true");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			EPPLaunchInfo theExt = new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE));
			theExt.setIncludeMark(true);
			domain.addExtension(theExt);

			domainResponse = domain.sendInfo();

			// -- Output all of the domain info response attributes
			System.out
					.println("launchInfo: Sunrise Registration with includeMark=true Response = ["
							+ domainResponse + "]\n\n");

			if (domainResponse.hasExtension(EPPLaunchInfData.class)) {
				EPPLaunchInfData infData = (EPPLaunchInfData) domainResponse
						.getExtension(EPPLaunchInfData.class);

				if (!infData.getPhase().getPhase()
						.equals(EPPLaunchPhase.PHASE_SUNRISE)) {
					Assert.fail("launchInfo: phase "
							+ infData.getPhase().getPhase() + " != "
							+ EPPLaunchPhase.PHASE_SUNRISE);
				}
				if (infData.getApplicationId() != null) {
					Assert.fail("launchInfo: applicationId should be null for sunrise registration");
				}
				if (infData.getStatus() != null) {
					Assert.fail("launchInfo: status should be null for sunrise registration");
				}
				if (infData.getMark() == null) {
					Assert.fail("launchInfo: mark should not be null");
				}
				System.out.println("Sunrise Registration Mark = "
						+ infData.getMark());
			}

			System.out
					.println("launchInfo: Info command for landrush application \"abc123\"");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH), "abc123"));

			domainResponse = domain.sendInfo();

			// -- Output all of the domain info response attributes
			System.out.println("launchInfo: Landrush Application Response = ["
					+ domainResponse + "]\n\n");

			if (domainResponse.hasExtension(EPPLaunchInfData.class)) {
				EPPLaunchInfData infData = (EPPLaunchInfData) domainResponse
						.getExtension(EPPLaunchInfData.class);

				if (!infData.getPhase().getPhase()
						.equals(EPPLaunchPhase.PHASE_LANDRUSH)) {
					Assert.fail("EPPLaunchInfData phase "
							+ infData.getPhase().getPhase() + " != "
							+ EPPLaunchPhase.PHASE_LANDRUSH);
				}
				System.out.println("Landrush Application Id = "
						+ infData.getApplicationId());
				System.out.println("Landrush Application Status = "
						+ infData.getStatus().getStatus());
				if (infData.getMark() != null) {
					Assert.fail("launchInfo: mark should be null for landrush application");
				}
			}

			System.out
					.println("launchInfo: Info command for unsupported landrush registration");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH)));

			domainResponse = null;
			try {
				domainResponse = domain.sendInfo();
			}
			catch (EPPCommandException ex) {
				EPPResponse response = ex.getResponse();
				System.out
						.println("launchInfo: Landrush Registration Expected Error Response = ["
								+ response + "]\n\n");
			}

			if (domainResponse != null) {
				Assert.fail("launchInfo: landrush registration info should have failed");
			}

			System.out
					.println("launchInfo: Info command for unsupported phase \"CLAIMS\"");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS)));

			domainResponse = null;
			try {
				domainResponse = domain.sendInfo();
			}
			catch (EPPCommandException ex) {
				EPPResponse response = ex.getResponse();
				System.out
						.println("launchInfo: Info with Unsupported Phase Expected Error Response = ["
								+ response + "]\n\n");
			}

			if (domainResponse != null) {
				Assert.fail("launchInfo: Info with unsupported Phase should have failed");
			}

			System.out
					.println("launchInfo: Info command for unsupported phase \"CLAIMS\"");

			domain.setTransId("ABC-12345");

			domain.addDomainName("example.tld");

			// Add extension
			domain.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS)));

			domainResponse = null;
			try {
				domainResponse = domain.sendInfo();
			}
			catch (EPPCommandException ex) {
				EPPResponse response = ex.getResponse();
				System.out
						.println("launchInfo: Info with Unsupported Phase Expected Error Response = ["
								+ response + "]\n\n");
			}

			if (domainResponse != null) {
				Assert.fail("launchInfo: Info with unsupported Phase should have failed");
			}

		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("launchInfo");

	} // End EPPLaunchTst.launchInfo()

	/**
	 * Unit test of using using the <code>EPPLaunchCreate</code> Extension with
	 * <code>EPPDomain</code> create command for sunrise with the use of
	 * codeMark's. The following tests are executed:<br>
	 * <br>
	 * <ol>
	 * <li>Create command for sunrise application with code
	 * <li>Create command for sunrise application with list of codes
	 * <li>Create command for sunrise application with mark
	 * <li>Create command for sunrise application with list of marks
	 * <li>Create command for sunrise application with code and mark
	 * <li>Create command for sunrise application with list of codes and marks
	 * <li>Create command for sunrise registration with list of codes and marks
	 * <li>Create command for mix of sunrise and claims create model
	 * </ol>
	 */
	public void launchCreateCodeMark() {
		printStart("launchCreateCodeMark");

		EPPResponse response;
		EPPLaunchCreate theExt;

		try {
			// Mark Owner
			EPPMarkContact holder = new EPPMarkContact();
			holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
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

			// Mark Contact
			EPPMarkContact contact = new EPPMarkContact();
			contact.setType(EPPMarkContact.TYPE_OWNER);
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

			// Trademark
			EPPTrademark trademark = new EPPTrademark();
			trademark.setId("1234-2");
			trademark.setName("Example One");
			trademark.addHolder(holder);
			trademark.addContact(contact);
			trademark.setJurisdiction("US");
			trademark.addClass("35");
			trademark.addClass("36");
			trademark.addLabel("example-one");
			trademark.addLabel("exampleone");
			trademark
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			trademark.setRegNum("234235");
			trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
			trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

			// Treaty or Statute
			EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
			treatyOrStatute.setId("1234-2");
			treatyOrStatute.setName("Example One");
			treatyOrStatute.addHolder(holder);
			treatyOrStatute.addContact(contact);
			treatyOrStatute.addProtection(new EPPProtection("US", "Reston",
					"US"));
			treatyOrStatute.addLabel("example-one");
			treatyOrStatute.addLabel("exampleone");
			treatyOrStatute
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			treatyOrStatute.setRefNum("234235");
			treatyOrStatute.setProDate(new GregorianCalendar(2009, 8, 16)
					.getTime());
			treatyOrStatute.setTitle("Example Title");
			treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
					.getTime());

			// Court
			EPPCourt court = new EPPCourt();
			court.setId("1234-2");
			court.setName("Example One");
			court.addHolder(holder);
			court.addContact(contact);
			court.addLabel("example-one");
			court.addLabel("exampleone");
			court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			court.setRefNum("234235");
			court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
			court.setCc("US");
			court.addRegions("Reston");
			court.setCourtName("Test Court");

			// Mark
			EPPMark mark = new EPPMark();
			mark.addTrademark(trademark);
			mark.addTreatyOrStatute(treatyOrStatute);
			mark.addCourt(court);

			EPPTrademark trademark2 = (EPPTrademark) trademark.clone();

			trademark2.setName("Hello World 2");
			trademark2.setLabels(null);
			trademark2.addLabel("helloworld2");
			trademark2.addLabel("hello-world2");
			trademark2.addLabel("hello-world-2");
			EPPMark mark2 = new EPPMark();
			mark2.addTrademark(trademark2);

			/**
			 * TEST - Create a sunrise application with code.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with code.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(
					"49FD46E6C4B45C55D4AC"), EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: sunrise application with code: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise application with list of codes.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with list of codes.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_APPLICATION);
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AC", "sample1"));
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AD", "sample2"));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: Sunrise application with list of codes: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create a sunrise application with mark.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with mark.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(mark),
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: sunrise application with mark: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise application with list of marks.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with list of marks.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_APPLICATION);
			theExt.addCodeMark(new EPPLaunchCodeMark(mark));
			theExt.addCodeMark(new EPPLaunchCodeMark(mark2));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: Sunrise application with list of codes: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create a sunrise application with code and mark.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with code and mark.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(
					"49FD46E6C4B45C55D4AC", "sample", mark),
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: sunrise application with code and mark: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise application with list of codes
			 * and marks.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise application with list of codes and marks.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_APPLICATION);
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AC",
					"sample", mark));
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AD",
					mark2));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: Sunrise application with list of codes and marks: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise registration with list of codes
			 * and marks.
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for sunrise registration with list of codes and marks.");

			// Set domain update attributes
			domain.addDomainName("REG1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_REGISTRATION);
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AC",
					"sample", mark));
			theExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AD",
					mark2));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: Sunrise registration with list of codes and marks: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS);
			assert (!response.hasExtension(EPPLaunchCreData.class));

			/**
			 * TEST - Create command for mix of sunrise and claims create model
			 */
			System.out
					.println("\nlaunchCreateCodeMark: Create command for mix of sunrise and claims create model.");

			// Set domain update attributes
			domain.addDomainName("APP1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			EPPLaunchCreate createExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CUSTOM, "non-tmch-sunrise"),
					EPPLaunchCreate.TYPE_APPLICATION);

			createExt.addCodeMark(new EPPLaunchCodeMark(mark));
			createExt.setNotice(new EPPLaunchNotice("49FD46E6C4B45C55D4AC",
					new Date(), new Date(), "custom"));

			domain.addExtension(createExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateCodeMark: Create command for mix of sunrise and claims create model: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		catch (CloneNotSupportedException e) {
			handleException(e);
		}

		printEnd("launchCreateCodeMark");
	}

	/**
	 * Unit test of using the <code>EPPLaunchCreate</code> Extension with
	 * <code>EPPDomain</code> create command for sunrise with the use of
	 * signedMark's. The following tests are executed:<br>
	 * <br>
	 * <ol>
	 * <li>Create a sunrise application with a signed mark in XML
	 * <li>Create a sunrise application with a signed mark in Base64
	 * <li>Create a sunrise application with list of signed marks in Base64
	 * <li>Create a sunrise registration with list of signed marks in Base64
	 * <li>Create command for sunrise registration with revoked certificate
	 * </ol>
	 */
	public void launchCreateSignedMark() {
		printStart("launchCreateSignedMark");

		EPPResponse response;
		EPPLaunchCreate theExt;

		try {
			// Mark Owner
			EPPMarkContact holder = new EPPMarkContact();
			holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
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

			// Mark Contact
			EPPMarkContact contact = new EPPMarkContact();
			contact.setType(EPPMarkContact.TYPE_OWNER);
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

			// Trademark
			EPPTrademark trademark = new EPPTrademark();
			trademark.setId("1234-2");
			trademark.setName("Example One");
			trademark.addHolder(holder);
			trademark.addContact(contact);
			trademark.setJurisdiction("US");
			trademark.addClass("35");
			trademark.addClass("36");
			trademark.addLabel("example-one");
			trademark.addLabel("exampleone");
			trademark
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			trademark.setRegNum("234235");
			trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
			trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

			// Treaty or Statute
			EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
			treatyOrStatute.setId("1234-2");
			treatyOrStatute.setName("Example One");
			treatyOrStatute.addHolder(holder);
			treatyOrStatute.addContact(contact);
			treatyOrStatute.addProtection(new EPPProtection("US", "Reston",
					"US"));
			treatyOrStatute.addLabel("example-one");
			treatyOrStatute.addLabel("exampleone");
			treatyOrStatute
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			treatyOrStatute.setRefNum("234235");
			treatyOrStatute.setProDate(new GregorianCalendar(2009, 8, 16)
					.getTime());
			treatyOrStatute.setTitle("Example Title");
			treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
					.getTime());

			// Court
			EPPCourt court = new EPPCourt();
			court.setId("1234-2");
			court.setName("Example One");
			court.addHolder(holder);
			court.addContact(contact);
			court.addLabel("example-one");
			court.addLabel("exampleone");
			court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			court.setRefNum("234235");
			court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
			court.setCc("US");
			court.addRegions("Reston");
			court.setCourtName("Test Court");

			// Mark
			EPPMark mark = new EPPMark();
			mark.addTrademark(trademark);
			mark.addTreatyOrStatute(treatyOrStatute);
			mark.addCourt(court);

			EPPIssuer issuer = new EPPIssuer("2", "Example Inc.",
					"support@example.tld");
			issuer.setUrl("http://www.example.tld");
			issuer.setVoice("+1.7035555555");
			issuer.setVoiceExt("1234");

			EPPSignedMark signedMark = new EPPSignedMark("1-2", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			signedMark.sign(this.privateKey, this.certChain);
			
			EPPTrademark trademark2 = (EPPTrademark) trademark.clone();
			trademark2.setName("Hello World 2");
			trademark2.setLabels(null);
			trademark2.addLabel("helloworld2");
			trademark2.addLabel("hello-world2");
			trademark2.addLabel("hello-world-2");
			EPPMark mark2 = new EPPMark();
			mark2.addTrademark(trademark2);

			EPPSignedMark signedMark2 = new EPPSignedMark("123457-1", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark2);
			signedMark2.sign(this.privateKey, this.certChain);
			
			/**
			 * TEST - Create a sunrise application with signed mark in XML.
			 */
			System.out
					.println("\nlaunchCreateSignedMark: Create command for sunrise application with signed mark in XML.");

			// Set domain update attributes
			domain.addDomainName("XMLSIGNEDMARK.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			if (!signedMark.validate(pkixParameters)) {
				Assert.fail("launchCreateSignedMark(): XML signed mark signature is NOT valid");
			}
			
			Document doc = new DocumentImpl();
			String signedMarkXML = EPPUtil.toStringNoIndent(signedMark
					.encode(doc));
			System.out.println("\nlaunchCreateSignedMark: signed mark = \n"
					+ signedMarkXML);
			System.out
					.println("\nlaunchCreateSignedMark: base64 signed mark = \n"
							+ new String(Base64.encodeBase64(
									signedMarkXML.getBytes(), true)));			

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateSignedMark: sunrise application with signed mark in XML: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create a sunrise application with signed mark in Base64.
			 */
			System.out
					.println("\nlaunchCreateSignedMark: Create command for sunrise application with signed mark in Base64.");

			// Set domain update attributes
			domain.addDomainName("BASE64SIGNEDMARK.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");
			
			EPPEncodedSignedMark encodedSignedMark = new EPPEncodedSignedMark(signedMark); 			

			if (!encodedSignedMark.validate(pkixParameters)) {
				Assert.fail("launchCreateSignedMark(): Encoded signed mark signature is NOT valid");
			}

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), encodedSignedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateSignedMark: sunrise application with signed mark in Base64: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise application with list of signed
			 * marks in XML.
			 */
			System.out
					.println("\nlaunchCreateSignedMark: Create command for sunrise application with list of signed marks in Base64.");

			// Set domain update attributes
			domain.addDomainName("BASE64SIGNEDMARKLIST.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_APPLICATION);

			theExt.addSignedMark(new EPPEncodedSignedMark(signedMark));
			theExt.addSignedMark(new EPPEncodedSignedMark(signedMark2));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateSignedMark: Sunrise application with list of signed marks in Base64: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			/**
			 * TEST - Create command for sunrise registration with list of
			 * signed marks in Base64.
			 */
			System.out
					.println("\nlaunchCreateSignedMark: Create command for sunrise registration with list of signed marks in Base64.");

			// Set domain update attributes
			domain.addDomainName("REG1.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_REGISTRATION);

			theExt.addSignedMark(new EPPEncodedSignedMark(signedMark));

			theExt.addSignedMark(new EPPEncodedSignedMark(signedMark2));

			// Add extension
			domain.addExtension(theExt);

			// Execute create
			response = domain.sendCreate();

			System.out
					.println("launchCreateSignedMark: Sunrise registration with list of signed marks in Base64: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS);
			assert (!response.hasExtension(EPPLaunchCreData.class));

			/**
			 * TEST - Create command for sunrise registration with revoked
			 * certificate.
			 */
			System.out
					.println("\nlaunchCreateSignedMark: Create command for sunrise registration with revoked certificate.");

			// Set domain update attributes
			domain.addDomainName("REVOKEDCERT.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			String revokedKeystore = Environment
					.getProperty("EPP.SignedMark.revokedKeystore");
			assert (revokedKeystore != null);

			String keyAlias = Environment.getOption("EPP.SignedMark.keyAlias");
			assert (keyAlias != null);

			String passphrase = Environment
					.getOption("EPP.SignedMark.passphrase");

			PrivateKey revokedPrivateKey = null;
			Certificate[] revokedCertChain = null;
			assert (passphrase != null);
			try {
				KeyStore.PrivateKeyEntry keyEntry = loadPrivateKeyEntry(
						revokedKeystore, keyAlias, passphrase);
				revokedPrivateKey = keyEntry.getPrivateKey();
				revokedCertChain = keyEntry.getCertificateChain();

			}
			catch (Exception ex) {
				Assert.fail("launchCreateSignedMark: Error loading the revoked private key and certificate chain: + "
						+ ex);
			}

			EPPSignedMark signedMark3 = new EPPSignedMark("1-2", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			signedMark3.sign(revokedPrivateKey, revokedCertChain);

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_REGISTRATION);
			theExt.addSignedMark(signedMark3);

			// Add extension
			domain.addExtension(theExt);

			// Execute create
			try {
				response = domain.sendCreate();
				Assert.fail("launchCreateSignedMark: Create command with revoked certificate should have failed");

			}
			catch (EPPCommandException e) {
				assert (e.getResponse().getResult().getCode() == EPPResult.PARAM_OUT_OF_RANGE);
			}

			System.out
					.println("launchCreateSignedMark: Create command for sunrise registration with revoked certificate successful\n\n");

		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		catch (CloneNotSupportedException e) {
			handleException(e);
		}
		catch (EPPException e) {
			handleException(e);
		}

		printEnd("launchCreateSignedMark");
	}

	
	/**
	 * Unit test of using the <code>EPPLaunchCreate</code> Extension with
	 * <code>EPPDomain</code> create command for sunrise with the use of an
	 * active Signed Mark Data (SMD).
	 */
	public void launchCreateActiveSMD() {
		printStart("launchCreateActiveSMD");

		EPPResponse response;
		EPPLaunchCreate theExt;

		try {

			InputStream activeSMDStream = new FileInputStream(
					smdsDir + "/smd-active/smd-active-25nov13-en.smd");

			EPPEncodedSignedMark encodedSignedMark = new EPPEncodedSignedMark(
					activeSMDStream);

			/**
			 * TEST - Create a sunrise application with encoded signed mark.
			 */
			System.out
					.println("\nlaunchCreateActiveSMD: Create command for ACTIVESMD.TLD sunrise application with encoded signed mark.");

			// Set domain update attributes
			domain.addDomainName("ACTIVESMD.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			if (!encodedSignedMark.validate(pkixParameters)) {
				Assert.fail("launchCreateActiveSMD(): Active encoded signed mark NOT valid");
			}

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), encodedSignedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateActiveSMD: sunrise application with active encoded signed mark: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);
			
			
			/**
			 * TEST - Create a sunrise application with signed mark in XML.
			 */
			System.out
					.println("\nlaunchCreateActiveSMD: Create command for ACTIVESMD.TLD sunrise application with signed mark.");

			EPPSignedMark signedMark = new EPPSignedMark(encodedSignedMark);
			
			System.out
			.println("\nlaunchCreateActiveSMD: Signed mark XML = [" + new String(signedMark.encode()) + "]");
			
			
			// Set domain update attributes
			domain.addDomainName("ACTIVESMD.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			if (!signedMark.validate(pkixParameters)) {
				Assert.fail("launchCreateActiveSMD(): Active signed mark NOT valid");
			}

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateActiveSMD: sunrise application with active signed mark: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);
		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		catch (EPPException e) {
			handleException(e);
		}
		catch (FileNotFoundException e) {
			handleException(e);
		}

		printEnd("launchCreateActiveSMD");
	}	
	
	/**
	 * Unit test of the poll messaging for intermediate and terminal status
	 * transitions The following tests are executed:<br>
	 * <br>
	 * <ol>
	 * <li>Create a sunrise application APPPENDINGALLOCATION.TLD that
	 * transitions from "pendingValidation" to validated to "pendingAllocation".
	 * There will be two poll messages.
	 * <li>Create a sunrise application APPREJECTEDINVALID.TLD that transitions
	 * from "pendingValidation" to "invalid" to "rejected". There will be one
	 * standard poll message and one pending action poll message.
	 * <li>Create a sunrise application APPALLOCATEDALLSTATES.TLD that
	 * transitions from "pendingValidation" to "validated" to
	 * "pendingAllocation" to "allocated". There will be two poll message from
	 * the transitions from "pendingAllocation" to "validated" and from
	 * "validated" to "pendingAllocation". There will be one pending action poll
	 * message from the transition from "pendingAllocation" to "allocated".
	 * </ol>
	 */
	public void launchPollMessaging() {
		printStart("launchPollMessaging");

		EPPResponse response;
		EPPLaunchCreate theExt;

		try {
			// Clear poll queue
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();

			while (response.getResult().getCode() == EPPResult.SUCCESS_POLL_MSG) {
				this.session.setPollOp(EPPSession.OP_ACK);
				this.session.setMsgID(response.getMsgQueue().getId());
				response = this.session.sendPoll();
			}

			// Mark Owner
			EPPMarkContact holder = new EPPMarkContact();
			holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
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

			// Mark Contact
			EPPMarkContact contact = new EPPMarkContact();
			contact.setType(EPPMarkContact.TYPE_OWNER);
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

			// Trademark
			EPPTrademark trademark = new EPPTrademark();
			trademark.setId("1234-2");
			trademark.setName("Example One");
			trademark.addHolder(holder);
			trademark.addContact(contact);
			trademark.setJurisdiction("US");
			trademark.addClass("35");
			trademark.addClass("36");
			trademark.addLabel("example-one");
			trademark.addLabel("exampleone");
			trademark
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			trademark.setRegNum("234235");
			trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
			trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

			// Mark
			EPPMark mark = new EPPMark();
			mark.addTrademark(trademark);

			EPPIssuer issuer = new EPPIssuer("2", "Example Inc.",
					"support@example.tld");
			issuer.setUrl("http://www.example.tld");
			issuer.setVoice("+1.7035555555");
			issuer.setVoiceExt("1234");

			EPPSignedMark signedMark = new EPPSignedMark("1-2", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			signedMark.sign(this.privateKey, this.certChain);

			/**
			 * TEST - Create a sunrise application APPPENDINGALLOCATION.TLD
			 */
			System.out
					.println("\nlaunchPollMessaging: Create a sunrise application APPPENDINGALLOCATION.TLD.");

			// Set domain update attributes
			domain.addDomainName("APPPENDINGALLOCATION.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute create
			response = domain.sendCreate();

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			// -- Verify the poll messages (expected two poll messages)
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();
			assert (response.getMsgQueue().getCount() == 2);

			// Validate the "validated" poll message
			assert (response instanceof EPPDomainInfoResp);
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus().equals(EPPLaunchStatus.STATUS_VALIDATED));

			// Validate the "pendingAllocation" poll message
			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();

			assert (response instanceof EPPDomainInfoResp);
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus()
					.equals(EPPLaunchStatus.STATUS_PENDING_ALLOCATION));

			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();

			System.out
					.println("launchPollMessaging: Create a sunrise application APPPENDINGALLOCATION.TLD successful\n\n");

			/**
			 * TEST - Create a sunrise application APPREJECTEDINVALID.TLD
			 */
			System.out
					.println("\nlaunchPollMessaging: Create a sunrise application APPREJECTEDINVALID.TLD.");

			// Set domain update attributes
			domain.addDomainName("APPREJECTEDINVALID.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute create
			response = domain.sendCreate();

			String createServerTransId = response.getTransId()
					.getServerTransId();
			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			// -- Verify the poll messages (expected two poll messages)
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();
			assert (response.getMsgQueue().getCount() == 2);

			// Validate the "invalid" poll message
			assert (response instanceof EPPDomainInfoResp);
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus().equals(EPPLaunchStatus.STATUS_INVALID));

			// Validate the "rejected" poll message
			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();

			assert (response instanceof EPPDomainPendActionMsg);
			EPPDomainPendActionMsg pendActionMsg = (EPPDomainPendActionMsg) response;
			assert (!pendActionMsg.isPASuccess());
			assert (pendActionMsg.getPendingTransId().getServerTransId()
					.equals(createServerTransId));
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus().equals(EPPLaunchStatus.STATUS_REJECTED));

			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();

			System.out
					.println("launchPollMessaging: Create a sunrise application APPREJECTEDINVALID.TLD successful\n\n");

			/**
			 * TEST - Create a sunrise application ALLOCATEDALLSTATES.TLD
			 */
			System.out
					.println("\nlaunchPollMessaging: Create a sunrise application APPALLOCATEDALLSTATES.TLD.");

			// Set domain update attributes
			domain.addDomainName("APPALLOCATEDALLSTATES.TLD");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute create
			response = domain.sendCreate();

			createServerTransId = response.getTransId().getServerTransId();
			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			// -- Verify the poll messages (expected two poll messages)
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();
			assert (response.getMsgQueue().getCount() == 3);

			// Validate the "invalid" poll message
			assert (response instanceof EPPDomainInfoResp);
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus().equals(EPPLaunchStatus.STATUS_VALIDATED));

			// Validate the "pendingAllocation" poll message
			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();

			assert (response instanceof EPPDomainInfoResp);
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus()
					.equals(EPPLaunchStatus.STATUS_PENDING_VALIDATION));

			// Validate the "allocated" poll message
			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();
			this.session.setPollOp(EPPSession.OP_REQ);
			response = this.session.sendPoll();

			assert (response instanceof EPPDomainPendActionMsg);
			pendActionMsg = (EPPDomainPendActionMsg) response;
			assert (pendActionMsg.isPASuccess());
			assert (pendActionMsg.getPendingTransId().getServerTransId()
					.equals(createServerTransId));
			assert (response.hasExtension(EPPLaunchInfData.class));
			assert (((EPPLaunchInfData) response
					.getExtension(EPPLaunchInfData.class)).getStatus()
					.getStatus().equals(EPPLaunchStatus.STATUS_ALLOCATED));

			this.session.setPollOp(EPPSession.OP_ACK);
			this.session.setMsgID(response.getMsgQueue().getId());
			response = this.session.sendPoll();

			System.out
					.println("launchPollMessaging: Create a sunrise application APPALLOCATEDALLSTATES.TLD successful\n\n");

		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		catch (EPPException e) {
			handleException(e);
		}

		printEnd("launchPollMessaging");
	}

	/**
	 * Unit test of using using the <code>EPPLaunchCreate</code> Extension with
	 * <code>EPPDomain</code> create command using the Create Generic Form. The
	 * following tests are executed:<br>
	 * <br>
	 * <ol>
	 * <li>Create command for landrush with no type defined
	 * <li>Create create command for a landrush application
	 * <li>Create create command for a landrush registration
	 * </ol>
	 */
	public void launchCreateGeneric() {
		printStart("launchCreateGeneric");

		EPPResponse response;

		try {

			/**
			 * TEST - Create command for landrush with no type defined
			 */
			System.out
					.println("launchCreateGeneric: Create command for landrush with no type defined.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH)));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateGeneric: landrush with no type defined: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS);

			/**
			 * TEST - Create create command for a landrush application
			 */
			System.out
					.println("launchCreateGeneric: Create create command for a landrush application.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH),
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateGeneric: Create create command for a landrush application: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);

			/**
			 * TEST - Create create command for a landrush registration.
			 */
			System.out
					.println("launchCreateGeneric: Create create command for a landrush registration.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH),
					EPPLaunchCreate.TYPE_REGISTRATION));

			// Execute create
			response = domain.sendCreate();

			System.out
					.println("launchCreateGeneric: Create create command for a landrush application: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS);

		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("launchCreateGeneric");
	}

	/**
	 * Unit test of using using the <code>EPPLaunchCreate</code> Extension with
	 * <code>EPPDomain</code> create command for CLAIMS and claims2 phases with
	 * the <code>EPPLaunchNotice</code>. The following tests are executed:<br>
	 * <br>
	 * <ol>
	 * <li>Create command for CLAIMS with notice information.
	 * <li>Create command for CLAIMS with notice information and validatorID.
	 * <li>Create command for CLAIMS without notice information.
	 * </ol>
	 */
	public void launchCreateNotice() {
		printStart("launchCreateNotice");

		EPPResponse response;

		try {

			/**
			 * TEST - Create command for CLAIMS with notice information.
			 */
			System.out
					.println("\nlaunchCreateNotice: Create command for CLAIMS with notice information.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS), new EPPLaunchNotice(
					"49FD46E6C4B45C55D4AC", new Date(), new Date()),
					EPPLaunchCreate.TYPE_REGISTRATION));

			// Execute update
			response = domain.sendCreate();
			
			/**
			 * TEST - Create command for CLAIMS with notice information and validatorID.
			 */
			System.out
					.println("\nlaunchCreateNotice: Create command for CLAIMS with notice information and validatorID.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS), new EPPLaunchNotice(
					"49FD46E6C4B45C55D4AC", new Date(), new Date(), "tmch"),
					EPPLaunchCreate.TYPE_REGISTRATION));

			// Execute update
			response = domain.sendCreate();
			

			System.out
					.println("launchCreateNotice: CLAIMS registration with notice information: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS);

			/**
			 * TEST - Create command for CLAIMS without notice information.
			 */
			System.out
					.println("launchCreateNotice: Create command for CLAIMS application without notice information.");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS),
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchCreateNotice: Create command for CLAIMS without notice information: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);

		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("launchCreateNotice");
	}

	/**
	 * Unit test of using using the <code>EPPLaunchUpdate</code> Extension with
	 * <code>EPPDomain</code> update command with the following tests:<br>
	 * <br>
	 * <ol>
	 * <li>Update command for sunrise application "abc123"
	 * <li>Update command for landrush application "abc123"
	 * <li>Update command for unsupported phase "CLAIMS"
	 * </ol>
	 */
	public void launchUpdate() {
		printStart("launchUpdate");

		EPPResponse response;

		try {
			/**
			 * TEST - Update command for sunrise application "abc123".
			 */
			System.out
					.println("\nlaunchUpdate: Update command for sunrise application \"abc123\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			domain.setUpdateAttrib(EPPDomain.HOST, "ns2.example.tld",
					EPPDomain.ADD);
			domain.setUpdateAttrib(EPPDomain.HOST, "ns1.example.tld",
					EPPDomain.REMOVE);

			// Add extension
			domain.addExtension(new EPPLaunchUpdate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

			// Execute update
			response = domain.sendUpdate();

			// -- Output all of the response attributes
			System.out
					.println("launchUpdate: Sunrise application update response = ["
							+ response + "]\n\n");

			/**
			 * TEST - Update command for landrush application "abc123".
			 */
			System.out
					.println("\nlaunchUpdate: Update command for landrush application \"abc123\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			domain.setUpdateAttrib(EPPDomain.HOST, "ns1.example.tld",
					EPPDomain.ADD);
			domain.setUpdateAttrib(EPPDomain.HOST, "ns2.example.tld",
					EPPDomain.REMOVE);

			// Add extension
			domain.addExtension(new EPPLaunchUpdate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH), "abc123"));

			// Execute update
			response = domain.sendUpdate();

			// -- Output all of the response attributes
			System.out
					.println("launchUpdate: Landrush application update response = ["
							+ response + "]\n\n");

			/**
			 * TEST - Update command for unsupported phase "CLAIMS".
			 */
			System.out
					.println("\nlaunchUpdate: Update command for unsupported phase \"CLAIMS\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			// Add extension
			domain.addExtension(new EPPLaunchUpdate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS), "abc123"));

			response = null;
			try {
				response = domain.sendUpdate();
			}
			catch (EPPCommandException ex) {
				System.out
						.println("launchUpdate: Update with Unsupported Phase Expected Error Response = ["
								+ ex.getResponse() + "]\n\n");
			}

			if (response != null) {
				Assert.fail("launchUpdate: Update with unsupported Phase should have failed");
			}

		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("launchUpdate");
	}

	/**
	 * Unit test of using using the <code>EPPLaunchDelete</code> Extension with
	 * <code>EPPDomain</code> delete command with the following tests:<br>
	 * <br>
	 * <ol>
	 * <li>Delete command for sunrise application "abc123"
	 * <li>Delete command for landrush application "abc123"
	 * <li>Delete command for unsupported phase "CLAIMS"
	 * </ol>
	 */
	public void launchDelete() {
		printStart("launchDelete");

		EPPResponse response;

		try {
			/**
			 * TEST - Update command for sunrise application "abc123".
			 */
			System.out
					.println("\nlaunchDelete: Update command for sunrise application \"abc123\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			// Add extension
			domain.addExtension(new EPPLaunchDelete(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

			// Execute delete
			response = domain.sendDelete();

			// -- Output all of the response attributes
			System.out
					.println("launchUpdate: Sunrise application delete response = ["
							+ response + "]\n\n");

			/**
			 * TEST - Delete command for landrush application "abc123".
			 */
			System.out
					.println("\nlaunchUpdate: Delete command for landrush application \"abc123\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			// Add extension
			domain.addExtension(new EPPLaunchDelete(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_LANDRUSH), "abc123"));

			// Execute update
			response = domain.sendDelete();

			// -- Output all of the response attributes
			System.out
					.println("launchUpdate: Landrush application delete response = ["
							+ response + "]\n\n");

			/**
			 * TEST - Delete command for unsupported phase "CLAIMS".
			 */
			System.out
					.println("\nlaunchUpdate: Delete command for unsupported phase \"CLAIMS\"");

			// Set domain update attributes
			domain.addDomainName("example.tld");
			domain.setTransId("ABC-12345");

			// Add extension
			domain.addExtension(new EPPLaunchDelete(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_CLAIMS), "abc123"));

			response = null;
			try {
				response = domain.sendDelete();
			}
			catch (EPPCommandException ex) {
				System.out
						.println("launchUpdate: Delete with Unsupported Phase Expected Error Response = ["
								+ ex.getResponse() + "]\n\n");
			}

			if (response != null) {
				Assert.fail("launchUpdate: Delete with unsupported Phase should have failed");
			}

		}

		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("launchDelete");
	}

	/**
	 * Unit test for executing a TMCH pending sunrise flow that includes the
	 * following:<br>
	 * <br>
	 * <ol>
	 * <li>Create a sunrise application with a signed mark in XML for
	 * application 1
	 * <li>Create a sunrise application with a signed mark in Base64 for
	 * application 2
	 * <li>Execute an info of the sunrise application 1 using the application id
	 * <li>Update the sunrise application that is chosen for allocation
	 * (application 1), that allocates application 1 and rejects application 2.
	 * The server keys off of the domain names on the domain update to allocate
	 * and insert the appropriate poll messages. This is obviously not expected
	 * of a real server.
	 * <li>Get the poll messages for the allocated / accepted application and
	 * the rejected application.
	 * <li>Info both the accepted and rejected applications using the
	 * application id.
	 * <li>Info the domain name of the allocated domain name without the
	 * application id.
	 * </ol>
	 */
	public void launchTMCHPendingSunrise() {
		printStart("launchTMCHPendingSunrise");

		EPPResponse response;
		EPPLaunchCreate theExt;
		String domainName = "APPTMCHPENDINGSUNRISE.TLD";

		try {
			// Mark Owner
			EPPMarkContact holder = new EPPMarkContact();
			holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
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

			// Mark Contact
			EPPMarkContact contact = new EPPMarkContact();
			contact.setType(EPPMarkContact.TYPE_OWNER);
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

			// Trademark
			EPPTrademark trademark = new EPPTrademark();
			trademark.setId("1234-2");
			trademark.setName("Example One");
			trademark.addHolder(holder);
			trademark.addContact(contact);
			trademark.setJurisdiction("US");
			trademark.addClass("35");
			trademark.addClass("36");
			trademark.addLabel("example-one");
			trademark.addLabel("exampleone");
			trademark
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			trademark.setRegNum("234235");
			trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
			trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

			// Treaty or Statute
			EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
			treatyOrStatute.setId("1234-2");
			treatyOrStatute.setName("Example One");
			treatyOrStatute.addHolder(holder);
			treatyOrStatute.addContact(contact);
			treatyOrStatute.addProtection(new EPPProtection("US", "Reston",
					"US"));
			treatyOrStatute.addLabel("example-one");
			treatyOrStatute.addLabel("exampleone");
			treatyOrStatute
					.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			treatyOrStatute.setRefNum("234235");
			treatyOrStatute.setProDate(new GregorianCalendar(2009, 8, 16)
					.getTime());
			treatyOrStatute.setTitle("Example Title");
			treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
					.getTime());

			// Court
			EPPCourt court = new EPPCourt();
			court.setId("1234-2");
			court.setName("Example One");
			court.addHolder(holder);
			court.addContact(contact);
			court.addLabel("example-one");
			court.addLabel("exampleone");
			court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
			court.setRefNum("234235");
			court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
			court.setCc("US");
			court.addRegions("Reston");
			court.setCourtName("Test Court");

			// Mark
			EPPMark mark = new EPPMark();
			mark.addTrademark(trademark);
			mark.addTreatyOrStatute(treatyOrStatute);
			mark.addCourt(court);

			EPPIssuer issuer = new EPPIssuer("2", "Example Inc.",
					"support@example.tld");
			issuer.setUrl("http://www.example.tld");
			issuer.setVoice("+1.7035555555");
			issuer.setVoiceExt("1234");

			EPPSignedMark signedMark = new EPPSignedMark("1-2", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			signedMark.sign(this.privateKey, this.certChain);

			EPPTrademark trademark2 = (EPPTrademark) mark.clone();
			trademark2.setId("1234-21");
			EPPMark mark2 = new EPPMark();
			mark2.addTrademark(trademark2);

			EPPSignedMark signedMark2 = new EPPSignedMark("123457", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark2);
			signedMark2.sign(this.privateKey, this.certChain);

			/**
			 * TEST - Create a sunrise application with signed mark in XML.
			 */
			System.out
					.println("\nlaunchTMCHPendingSunrise: Create command for sunrise application 1 with signed mark in XML.");

			// Set domain update attributes
			domain.addDomainName(domainName);
			domain.setTransId("ABC-123451");
			domain.setAuthString("ClientX");

			// Add extension
			domain.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE), signedMark,
					EPPLaunchCreate.TYPE_APPLICATION));

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchTMCHPendingSunrise: sunrise application 1 with signed mark in XML: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			String applicationId1 = ((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId();

			/**
			 * TEST - Create command for sunrise application 2 with signed mark
			 * in Base64.
			 */
			System.out
					.println("\nlaunchTMCHPendingSunrise: Create command for sunrise application 2 with signed mark in Base64.");

			// Set domain update attributes
			domain.addDomainName(domainName);
			domain.setTransId("ABC-123452");
			domain.setAuthString("ClientX");

			theExt = new EPPLaunchCreate(new EPPLaunchPhase(
					EPPLaunchPhase.PHASE_SUNRISE),
					EPPLaunchCreate.TYPE_APPLICATION);

			theExt.addSignedMark(new EPPEncodedSignedMark(signedMark));

			// Add extension
			domain.addExtension(theExt);

			// Execute update
			response = domain.sendCreate();

			System.out
					.println("launchTMCHPendingSunrise: Sunrise application 2 with signed mark in Base64: Response = ["
							+ response + "]\n\n");

			assert (response.getResult().getCode() == EPPResult.SUCCESS_PENDING);
			assert (response.hasExtension(EPPLaunchCreData.class));
			assert (((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId() != null);

			String applicationId2 = ((EPPLaunchCreData) response
					.getExtension(EPPLaunchCreData.class)).getApplicationId();

		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		catch (CloneNotSupportedException e) {
			handleException(e);
		}
		catch (EPPException e) {
			handleException(e);
		}

		printEnd("launchTMCHPendingSunrise");
	}

	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute
	 * is initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession() {

		printStart("initSession");
		// Set attributes for initSession
		session.setTransId("ABC-12345-XYZ");
		session.setVersion("1.0");
		session.setLang("en");

		// Initialize the session
		try {
			session.initSession();
		}
		catch (EPPCommandException e) {
			EPPResponse domainResponse = session.getResponse();

			// Is a server specified error?
			if ((domainResponse != null) && (!domainResponse.isSuccess())) {
				Assert.fail("Server Error : " + domainResponse);
			}
			else {
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	} // End EPPLaunchTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the EPP
	 * Server will be terminated.
	 */
	private void endSession() {
		printStart("endSession");
		session.setTransId("ABC-12345-XYZ");

		// End the session
		try {
			session.endSession();
		}
		catch (EPPCommandException e) {
			EPPResponse domainResponse = session.getResponse();

			// Is a server specified error?
			if ((domainResponse != null) && (!domainResponse.isSuccess())) {
				Assert.fail("Server Error : " + domainResponse);
			}
			else // Other error
			{
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("endSession");
	} // End EPPLaunchTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			String theSessionClassName = System.getProperty("EPP.SessionClass");

			if (theSessionClassName != null) {
				try {
					Class theSessionClass = Class.forName(theSessionClassName);

					if (!EPPSession.class.isAssignableFrom((theSessionClass))) {
						Assert.fail(theSessionClassName
								+ " is not a subclass of EPPSession");
					}

					session = (EPPSession) theSessionClass.newInstance();
				}
				catch (Exception ex) {
					Assert.fail("Exception instantiating EPP.SessionClass value "
							+ theSessionClassName + ": " + ex);
				}
			}
			else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId",
					"ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password",
					"foo-BAR2"));

			// Load the public key used for signedMark verification
			String keystore = Environment
					.getProperty("EPP.SignedMark.keystore");
			assert (keystore != null);

			String keyAlias = Environment.getOption("EPP.SignedMark.keyAlias");
			assert (keyAlias != null);

			String passphrase = Environment
					.getOption("EPP.SignedMark.passphrase");
			assert (passphrase != null);

			try {
				KeyStore.PrivateKeyEntry keyEntry = loadPrivateKeyEntry(
						keystore, keyAlias, passphrase);
				this.privateKey = keyEntry.getPrivateKey();
				this.certChain = keyEntry.getCertificateChain();

			}
			catch (Exception ex) {
				cat.error("LaunchDomainHandler.LaunchDomainHandler(): Error loading the private key and certificate chain: "
						+ ex);
				ex.printStackTrace();
				System.exit(1);
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error initializing the session: " + e);
		}

		initSession();
		domain = new EPPDomain(session);
		launch = new EPPLaunch(session);

	} // End EPPLaunchTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	} // End EPPLaunchTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPLaunchTst</code>.
	 */
	public static Test suite() {

		TestSuite suite = new TestSuite(EPPLaunchTst.class);

		String theConfigFileName = System.getProperty("EPP.ConfigFile");
		if (theConfigFileName != null)
			configFileName = theConfigFileName;

		try {
			app.initialize(configFileName);
		}

		catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error initializing the EPP Application: " + e);
		}

		try {
			List<String> crls = new ArrayList<String>();

			crls.add("eppsdk.crl");
			crls.add("tmch-pilot.crl");

			pkixParameters = com.verisign.epp.codec.launch.EPPLaunchTst
					.loadPKIXParameters("signedMarkTrust.jks", crls);
		}
		catch (Exception ex) {
			Assert.fail("Error loading trust store: " + ex);
		}
		
		smdsDir = Environment
				.getProperty("EPP.SMD.dir");
		assert (smdsDir != null);


		return suite;

	} // End EPPLaunchTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by a
	 * server error, "Server Error :<Response XML>" will be specified. If the
	 * exception was caused by a general algorithm error,
	 * "General Error :<Exception Description>" will be specified.
	 * 
	 * @param aException
	 *            Exception thrown during test
	 */
	public void handleException(Exception aException) {
		EPPResponse domainResponse = session.getResponse();
		aException.printStackTrace();

		// Is a server specified error?
		if ((domainResponse != null) && (!domainResponse.isSuccess())) {
			Assert.fail("Server Error : " + domainResponse);
		}
		else {
			Assert.fail("General Error : " + aException);
		}

	} // End EPPLaunchTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.
	 * </ul>
	 */
	public static void main(String args[]) {

		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null)
			numThreads = Integer.parseInt(threadsStr);

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread = new TestThread("EPPSessionTst Thread " + i,
						EPPLaunchTst.suite());
				thread.start();
			}

		}
		else
			// Single threaded mode.
			junit.textui.TestRunner.run(EPPLaunchTst.suite());
		try {
			app.endApplication();
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}

	} // End EPPLaunchTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();
		return new String(Thread.currentThread()
				+ String.valueOf(tm + rd.nextInt(12)).substring(10) + ".tld");
	}

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *            name for the test
	 */
	private void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	} // End EPPLaunchTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param Logical
	 *            name for the test
	 */
	private void printEnd(String aTest) {
		System.out
				.println("****************************************************************");
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	} // End EPPLaunchTst.testEnd(String)

	/**
	 * Print message
	 * 
	 * @param aMsg
	 *            message to print
	 */
	private void printMsg(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.println(Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg);
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg);
		}
		else {
			System.out.println(aMsg);
			cat.info(aMsg);
		}
	} // End EPPLaunchTst.printMsg(String)

	/**
	 * Print error message
	 * 
	 * @param aMsg
	 *            errpr message to print
	 */
	private void printError(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.err.println(Thread.currentThread().getName()
					+ ", iteration " + iteration + ": " + aMsg);
			cat.error(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aMsg);
		}
		else {
			System.err.println(aMsg);
			cat.error(aMsg);
		}
	} // End EPPLaunchTst.printError(String)

	/**
	 * Loads the private key used to digitally sign from a Java KeyStore with
	 * the alias of the <code>PrivateKeyEntry</code> and the password to access
	 * the Keystore and the key.
	 * 
	 * @param aKeyStoreName
	 *            Java Keystore to load the key from
	 * @param aKeyAliasName
	 *            Java Keystore alias of key
	 * @param aPassword
	 *            Password to access Java Keystore and key
	 * 
	 * @return Loaded <code>KeyStore.PrivateKeyEntry</code> that can be used to
	 *         get the private key and it's associated certificate chain.
	 * 
	 * @throws Exception
	 *             Error loading private key
	 */
	private static KeyStore.PrivateKeyEntry loadPrivateKeyEntry(
			String aKeyStoreName, String aKeyAliasName, String aPassword)
			throws Exception {

		// Load KeyStore
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream keyStoreFile = new FileInputStream(aKeyStoreName);
		keyStore.load(keyStoreFile, aPassword.toCharArray());

		// Get Private Key
		assert keyStore.isKeyEntry(aKeyAliasName);
		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore
				.getEntry(aKeyAliasName, new KeyStore.PasswordProtection(
						aPassword.toCharArray()));

		return keyEntry;
	}

	/**
	 * EPP Domain associated with test
	 */
	private EPPDomain domain = null;

	/**
	 * EPP Launch associated with test
	 */
	private EPPLaunch launch = null;

	/**
	 * EPP Session associated with test
	 */
	private EPPSession session = null;

	/**
	 * Current test iteration
	 */
	private int iteration = 0;

	/**
	 * Private key used to create signatures for signed marks.
	 */
	private PrivateKey privateKey;

	/**
	 * Certificate chain associated with the private key
	 */
	private Certificate[] certChain;

	/**
	 * Handle to the Singleton EPP Application instance (
	 * <code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */
	private static String configFileName = "epp.config";

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Logging category
	 */
	private static final Logger cat = Logger.getLogger(EPPLaunchTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

} // End class EPPLaunchTst
