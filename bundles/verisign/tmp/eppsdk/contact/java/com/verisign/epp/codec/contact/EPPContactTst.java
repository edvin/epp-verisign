/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

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
package com.verisign.epp.codec.contact;

import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.contact package.  The unit test
 * will     execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.contact package concrete
 * <code>EPPCommand</code>'s and their     expected <code>EPPResponse</code>.
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPContactTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPContactTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPContactTst(String name) {
		super(name);
	}

	// End EPPContactTst(String)

	/**
	 * Unit test of <code>EPPContactInfoCmd</code>.  The response to
	 * <code>EPPContactInfoCmd</code>     is <code>EPPContactInfoResp</code>. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactInfo() {
		EPPCodecTst.printStart("testContactInfo");

		// Info Command
		EPPContactInfoCmd theCommand =
			new EPPContactInfoCmd("ABC-12345", "sh8013");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPContactInfo Response
		EPPContactInfoResp   theResponse;
		EPPEncodeDecodeStats responseStats;

		// Streets
		Vector streets = new Vector();
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address =
			new EPPContactAddress(streets, "Dulles", "VA", "20166-6503", "US");

		// Name Postal Definition
		EPPContactPostalDefinition name =
			new EPPContactPostalDefinition(
										   "John Doe", "Example Inc.",
										   EPPContactPostalDefinition.ATTR_TYPE_LOC,
										   address);

		//i15d Streets
		Vector i15dStreets = new Vector();
		i15dStreets.addElement("i15d 123 Example Dr.");
		i15dStreets.addElement("i15d Suite 100");

		// Address
		EPPContactAddress i15dAddress =
			new EPPContactAddress(
								  i15dStreets, "Dulles", "VA", "20166-6503",
								  "US");

		// Name Postal Definition
		EPPContactPostalDefinition i15dName =
			new EPPContactPostalDefinition(
										   "i15d John Doe", "i15d Example Inc.",
										   EPPContactPostalDefinition.ATTR_TYPE_INT,
										   i15dAddress);

		// infoStatuses
		Vector infoStatuses = new Vector();
		infoStatuses.addElement(new EPPContactStatus(EPPContactStatus.ELM_STATUS_LINKED));
		infoStatuses.addElement(new EPPContactStatus(EPPContactStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED));

		// Test with just required EPPContactInfoResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPContactInfoResp(
								   respTransId, "SH8013-VRSN", "sh8013",
								   infoStatuses, name, "jdoe@example.com",
								   "ClientY", "ClientX", new Date(),
								   new EPPAuthInfo("2fooBAR"));

		theResponse.setVoice("+1.7035555555");
		theResponse.setVoiceExt("123");
		theResponse.setFax("+1.7035555556");
		theResponse.setFaxExt("456");

		theResponse.addPostalInfo(i15dName);

		// disclose names
		Vector names = new Vector();

		//names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose		
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("1");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		theResponse.setDisclose(disclose);

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Test with all EPPContactInfoResp attributes set.
		theResponse.setLastUpdatedBy("ClientX");
		theResponse.setLastUpdatedDate(new Date());
		theResponse.setLastUpdatedDate(new Date());

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactInfo");
	}

	// End EPPContactTst.testContactInfo()

	/**
	 * Unit test of <code>EPPContactCreateCmd</code>.  The response to
	 * <code>EPPContactCreateCmd</code>     is
	 * <code>EPPContactExpireResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactCreate() {
		EPPContactCreateCmd  theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testContactCreate");

		// Streets
		Vector streets = new Vector();
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address =
			new EPPContactAddress(null, "Dulles", "VA", "20166-6503", "US");

		// Postal Definition
		EPPContactPostalDefinition name =
			new EPPContactPostalDefinition(
										   "John Doe", "Example Inc.",
										   EPPContactPostalDefinition.ATTR_TYPE_LOC,
										   address);

		// Contact Command
		theCommand =
			new EPPContactCreateCmd(
									"ABC-12345", "sh8013", name,
									"jdoe@example.com",
									new EPPAuthInfo("2fooBAR"));
		theCommand.setFax("+1.7035555556");
		theCommand.setFaxExt("123");
		theCommand.setVoice("+1.7035555555");
		theCommand.setVoiceExt("456");

		// disclose names
		Vector names = new Vector();

		//names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose		
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("0");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		theCommand.setDisclose(disclose);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Create Response (Standard EPPResponse)
		EPPTransId			 respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPContactCreateResp theResponse =
			new EPPContactCreateResp(respTransId, "sh8013", new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactCreate");
	}

	// End EPPContactTst.testContactCreate()

	/**
	 * Unit test of <code>EPPContactDeleteCmd</code>.  The response to
	 * <code>EPPContactDeleteCmd</code>     is
	 * <code>EPPContactExpireResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactDelete() {
		EPPCodecTst.printStart("testContactDelete");

		EPPContactDeleteCmd theCommand =
			new EPPContactDeleteCmd("ABC-12345", "sh8013");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Create Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactDelete");
	}

	// End EPPContactTst.testContactDelete()

	/**
	 * Unit test of <code>EPPContactUpdateCmd</code>.  The response to
	 * <code>EPPContactUpdateCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactUpate() {
		EPPContactUpdateCmd  theCommand   = null;
		EPPEncodeDecodeStats commandStats = null;

		EPPCodecTst.printStart("testContactUpate");

		// Streets
		Vector streets = new Vector();

		//fixed to make address element optional in CodecTest
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address =
			new EPPContactAddress(streets, "Dulles", "VA", "20166-6503", "US");

		// Change Definition
		EPPContactPostalDefinition name =
			new EPPContactPostalDefinition(
										   "John",
										   EPPContactPostalDefinition.ATTR_TYPE_LOC,
										   address);

		// Contact Change
		EPPContactAddChange change =
			new EPPContactAddChange(
									name, "+1.7034444444",
									new EPPAuthInfo("2BARfoo"));

		change.setVoiceExt("678");
		change.setFax("+1.7037777777");
		change.setFaxExt("678");

		//		 disclose names
		Vector names = new Vector();

		//names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose		
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("0");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		change.setDisclose(disclose);

		// statuses
		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPContactStatus(EPPContactStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED));
		addStatuses.addElement(new EPPContactStatus(
													EPPContactStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED,
													"Hello World",
													EPPContactStatus.ELM_DEFAULT_LANG));

		// Contact Add
		EPPContactAddChange add = new EPPContactAddChange(addStatuses);

		// Update Command
		theCommand =
			new EPPContactUpdateCmd("ABC-12345", "sh8013", add, null, change);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Update Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactUpate");
	}

	// End EPPContactTst.testContactUpate()

	/**
	 * Unit test of <code>EPPContactTransferCmd</code>.  The response to
	 * <code>EPPContactTransferCmd</code>     is
	 * <code>EPPContactTransferResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactTransfer() {
		EPPContactTransferCmd theCommand;
		EPPEncodeDecodeStats  commandStats;

		EPPCodecTst.printStart("testContactTransfer");

		// Test Transfer Request Command
		theCommand =
			new EPPContactTransferCmd(
									  "ABC-12345", EPPCommand.OP_REQUEST,
									  "sh8013");

		theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// EPPContactTransferResp
		EPPContactTransferResp theResponse;
		EPPEncodeDecodeStats   responseStats;

		EPPTransId			   respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPContactTransferResp(
									   respTransId, "SH0000",
									   EPPResponse.TRANSFER_PENDING);
		theResponse.setResult(EPPResult.SUCCESS);
		theResponse.setRequestClient("ClientX");
		theResponse.setActionClient("ClientY");
		theResponse.setRequestDate(new Date());
		theResponse.setActionDate(new Date());

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer cancel
		theCommand =
			new EPPContactTransferCmd(
									  "ABC-12345", EPPCommand.OP_CANCEL,
									  "sh8013");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer query
		theCommand =
			new EPPContactTransferCmd(
									  "ABC-12345", EPPCommand.OP_QUERY, "sh8013");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer reject
		theCommand =
			new EPPContactTransferCmd(
									  "ABC-12345", EPPCommand.OP_REJECT,
									  "sh8013");

		commandStats     = EPPCodecTst.testEncodeDecode(theCommand);

		// Transfer approve
		theCommand =
			new EPPContactTransferCmd(
									  "ABC-12345", EPPCommand.OP_APPROVE,
									  "sh8013");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testContactTransfer");
	}

	// End EPPContactTst.testContactTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the
	 * <code>EPPContactMapFactory</code> with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPContactTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPContactTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPContactTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPContactTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPContactMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPContactMapFactory to EPPCodec: "
						+ e);
		}

		return suite;
	}

	// End EPPContactTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * 
	 * <ul>
	 * <li>
	 * iterations    Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate    Turn XML validation on (<code>true</code>) or off
	 * (<code>false</code>).     If validate is not specified, validation will
	 * be off.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Number of Threads
		int    numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread =
					new TestThread(
								   "EPPContactTst Thread " + i,
								   EPPContactTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPContactTst.suite());
		}
	}

	// End EPPContactTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPContactTst.setNumIterations(long)
	// End EPPContactTst.setFactory(DocumentBuilderFactory)

	/**
	 * Unit test of <code>EPPContactCheckCmd</code>.  The response to
	 * <code>EPPContactCheckCmd</code>     is
	 * <code>EPPContactCheckResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testContactCheck() {
		EPPContactCheckCmd   theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testContactCheck");

		// Check single contact name
		theCommand     = new EPPContactCheckCmd("ABC-12345", "sh8013");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check multiple contact names
		Vector contacts = new Vector();
		contacts.addElement("sh8013");
		contacts.addElement("sah8013");
		contacts.addElement("8013sah");

		theCommand     = new EPPContactCheckCmd("ABC-12345", contacts);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Contact Check Responses
		EPPContactCheckResp  theResponse;
		EPPEncodeDecodeStats responseStats;

		// Response for a single contact name
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPContactCheckResp(
									respTransId,
									new EPPContactCheckResult("sh8013", true));
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Response for multiple contact names
		Vector contactResults = new Vector();
		contactResults.addElement(new EPPContactCheckResult("sh8013", true));

		EPPContactCheckResult contactResult =
			new EPPContactCheckResult("sah8013", false);
		contactResult.setContactReason("In Use");
		contactResults.addElement(contactResult);

		//contactResults.addElement(new EPPContactCheckResult("sah8013", false));
		contactResults.addElement(new EPPContactCheckResult("8013sah", true));

		theResponse = new EPPContactCheckResp(respTransId, contactResults);
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactCheck");
	}

	// End EPPContactTst.testContactCheck()
	
	
	/**
	 * Unit test of <code>EPPContactPendActionMsg</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testContactPendActionMsg() {
		EPPCodecTst.printStart("testContactPendActionMsg");

		EPPTransId thePollTransId = new EPPTransId("ABC-12349", "54321-XYZ");
		EPPTransId thePendingTransId = new EPPTransId("DEF-12349", "12345-XYZ");

		EPPContactPendActionMsg theMsg =
			new EPPContactPendActionMsg(
									   thePollTransId, "sh8013", true,
									   thePendingTransId, new Date());
		theMsg.setResult(EPPResult.SUCCESS_POLL_MSG);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theMsg);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testContactPendActionMsg");
	}

	// End EPPContactTst.testContactPendActionMsg()

}
