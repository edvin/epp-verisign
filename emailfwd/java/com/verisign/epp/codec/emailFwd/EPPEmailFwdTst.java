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
package com.verisign.epp.codec.emailFwd;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

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
 * Is a unit test of the com.verisign.epp.codec.emailFwd package.  The unit
 * test will     execute, gather statistics, and output the results of a test
 * of each com.verisign.epp.codec.emailFwd package concrete
 * <code>EPPCommand</code>'s and their     expected <code>EPPResponse</code>.
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * Change History: Modified test codes to accomdate the latest epp-emailFwd-05
 * protocol     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPEmailFwdTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/** DOCUMENT ME! */
	private static boolean _initLogging = true;

	/**
	 * Creates a new EPPEmailFwdTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPEmailFwdTst(String name) {
		super(name);
	}

	// End EPPEmailFwdTst(String)

	/**
	 * Unit test of <code>EPPEmailFwdInfoCmd</code>.  The response to
	 * <code>EPPEmailFwdInfoCmd</code>     is
	 * <code>EPPEmailFwdInfoResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdInfo() {
		EPPCodecTst.printStart("testEmailFwdInfo");

		EPPEmailFwdInfoCmd theCommand =
			new EPPEmailFwdInfoCmd("ABC-12349", "john@example.name");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPEmailFwdInfo Full Response
		EPPEmailFwdInfoResp  theResponse;
		EPPEncodeDecodeStats responseStats;

		Vector				 statuses = new Vector();
		statuses.addElement(new EPPEmailFwdStatus(EPPEmailFwdStatus.ELM_STATUS_OK));

		// Test with just required EPPEmailFwdInfoResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		theResponse =
			new EPPEmailFwdInfoResp(
									respTransId, "EXAMPLE1-VRSN",
									"john@smith.name", "john@example.name",
									"ClientX", statuses, "ClientY", new Date(),
									new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Test with all EPPEmailFwdInfoResp attributes set.
		theResponse.setRegistrant("JD1234-VRSN");

		// Is contacts supported?
		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			Vector contacts = new Vector();
			contacts.addElement(new EPPEmailFwdContact(
													   "SH8013-VRSN",
													   EPPEmailFwdContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPEmailFwdContact(
													   "SH8013-VRSN",
													   EPPEmailFwdContact.TYPE_TECHNICAL));
			theResponse.setContacts(contacts);
		}

		theResponse.setLastUpdatedBy("ClientX");
		theResponse.setLastUpdatedDate(new Date());
		theResponse.setLastTransferDate(new Date());

		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse.setTransId(respTransId);
		theResponse.setRoid("EXAMPLE1-VRSN");

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);
		
		// Encode EPPEmailFwdInfo Partial Response
		theResponse =
			new EPPEmailFwdInfoResp();
		
		theResponse.setName("john@doe.name");
		theResponse.setRoid("EXAMPLE1-VRSN");
		theResponse.setClientId("ClientX");
		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse.setTransId(respTransId);
		
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);


		EPPCodecTst.printEnd("testEmailFwdInfo");
	}

	// End EPPEmailFwdTst.testEmailFwdInfo()

	/**
	 * Unit test of <code>EPPEmailFwdCreateCmd</code>.  The response to
	 * <code>EPPEmailFwdCreateCmd</code>     is
	 * <code>EPPEmailFwdCreateResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdCreate() {
		EPPEmailFwdCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testEmailFwdCreate");

		// Create a emailFwd with just the emailFwd name.
		theCommand =
			new EPPEmailFwdCreateCmd(
									 "ABC-12345", "john@example.name",
									 "john@yahoo.com",
									 new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		Vector contacts = null;

		// Is contacts supported?
		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			contacts = new Vector();
			contacts.addElement(new EPPEmailFwdContact(
													   "SH8013-VRSN",
													   EPPEmailFwdContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPEmailFwdContact(
													   "SH8013-VRSN",
													   EPPEmailFwdContact.TYPE_TECHNICAL));
		}

		theCommand =
			new EPPEmailFwdCreateCmd(
									 "ABC-12345", "john@example.name",
									 "john@yahoo.com", contacts,
									 new EPPEmailFwdPeriod(2),
									 new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		// Encode EPPEmailFwdCreate Response (EPPEmailFwdCreateResp)
		EPPEmailFwdCreateResp theResponse;
		EPPEncodeDecodeStats  responseStats;

		// Test with just required EPPEmailFwdCreateResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPEmailFwdCreateResp(
									  respTransId, "john@yahoo.com",
									  new GregorianCalendar(2001, 5, 5).getTime(),
									  new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testEmailFwdCreate");
	}

	// End EPPEmailFwdTst.testEmailFwdCreate()

	/**
	 * Unit test of <code>EPPEmailFwdDeleteCmd</code>.  The response to
	 * <code>EPPEmailFwdDeleteCmd</code>     is
	 * <code>EPPEmailFwdCreateResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdDelete() {
		EPPCodecTst.printStart("testEmailFwdDelete");

		EPPEmailFwdDeleteCmd theCommand =
			new EPPEmailFwdDeleteCmd("ABC-12345", "john@example.name");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Login Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testEmailFwdDelete");
	}

	// End EPPEmailFwdTst.testEmailFwdDelete()

	/**
	 * Unit test of <code>EPPEmailFwdRenewCmd</code>.  The response to
	 * <code>EPPEmailFwdRenewCmd</code>     is
	 * <code>EPPEmailFwdRenewResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdRenew() {
		EPPCodecTst.printStart("testEmailFwdRenew");
		
		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theDate = theCal.getTime();

		EPPEmailFwdRenewCmd theCommand =
			new EPPEmailFwdRenewCmd(
									"ABC-12345", "john@exampleZ.com",
									theDate,
									new EPPEmailFwdPeriod(5));

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPEmailFwdRenewResp theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPEmailFwdRenewResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPEmailFwdRenewResp(
									 respTransId, "john@exampleX.com",
									 new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testEmailFwdRenew");
	}

	// End EPPEmailFwdTst.testEmailFwdRenew()

	/**
	 * Unit test of <code>EPPEmailFwdUpdateCmd</code>.  The response to
	 * <code>EPPEmailFwdUpdateCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdUpate() {
		EPPCodecTst.printStart("testEmailFwdUpate");

		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPEmailFwdStatus(
													 EPPEmailFwdStatus.ELM_STATUS_CLIENT_HOLD,
													 "en"));

		Vector addContacts = null;

		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			addContacts = new Vector();
			addContacts.addElement(new EPPEmailFwdContact(
														  "MAK21-VRSN",
														  EPPEmailFwdContact.TYPE_TECHNICAL));
		}

		Vector removeStatuses = new Vector();
		removeStatuses.addElement(new EPPEmailFwdStatus(EPPEmailFwdStatus.ELM_STATUS_CLIENT_HOLD));

		Vector removeContacts = null;

		if (
			EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
			removeContacts = new Vector();
			removeContacts.addElement(new EPPEmailFwdContact(
															 "SH8013-VRSN",
															 EPPEmailFwdContact.TYPE_TECHNICAL));
		}

		// change
		// ...
		EPPEmailFwdAddRemove addItems =
			new EPPEmailFwdAddRemove(addContacts, addStatuses);
		EPPEmailFwdAddRemove removeItems =
			new EPPEmailFwdAddRemove(removeContacts, removeStatuses);
		EPPEmailFwdAddRemove changeItems =
			new EPPEmailFwdAddRemove(
									 "SH8013-VRSN", "asampath@verisign.com",
									 new EPPAuthInfo("2fooBAR"));

		EPPEmailFwdUpdateCmd theCommand =
			new EPPEmailFwdUpdateCmd(
									 "ABC-12345-XYZ", "john@example.name",
									 addItems, removeItems, changeItems);

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Update Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testEmailFwdUpate");
	}

	// End EPPEmailFwdTst.testEmailFwdUpate()

	/**
	 * Unit test of <code>EPPEmailFwdTransferCmd</code>.  The response to
	 * <code>EPPEmailFwdTransferCmd</code>     is
	 * <code>EPPEmailFwdTransferResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdTransfer() {
		EPPEmailFwdTransferCmd theCommand;
		EPPEncodeDecodeStats   commandStats;

		EPPCodecTst.printStart("testEmailFwdTransfer");

		// Test Transfer Request Command
		theCommand =
			new EPPEmailFwdTransferCmd(
									   "ABC-12345", EPPCommand.OP_REQUEST,
									   "john@example.name",
									   new EPPAuthInfo("2fooBAR"),
									   new EPPEmailFwdPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPEmailFwdTransferResp
		EPPEmailFwdTransferResp theResponse;
		EPPEncodeDecodeStats    responseStats;

		EPPTransId			    respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPEmailFwdTransferResp(respTransId, "john@example.name");
		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.setRequestClient("ClientX");
		theResponse.setActionClient("ClientY");
		theResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);
		theResponse.setRequestDate(new Date());
		theResponse.setActionDate(new Date());
		theResponse.setExpirationDate(new Date());

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer cancel
		theCommand =
			new EPPEmailFwdTransferCmd(
									   "ABC-12345", EPPCommand.OP_CANCEL,
									   "john@example.name");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer query
		theCommand =
			new EPPEmailFwdTransferCmd(
									   "ABC-12345", EPPCommand.OP_QUERY,
									   "john@example.name");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer reject
		theCommand =
			new EPPEmailFwdTransferCmd(
									   "ABC-12345", EPPCommand.OP_REJECT,
									   "john@example.name");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer approve
		theCommand =
			new EPPEmailFwdTransferCmd(
									   "ABC-12345", EPPCommand.OP_APPROVE,
									   "john@example.name");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testEmailFwdTransfer");
	}

	// End EPPEmailFwdTst.testEmailFwdTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the
	 * <code>EPPEmailFwdMapFactory</code> with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPEmailFwdTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPEmailFwdTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPEmailFwdTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPEmailFwdTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPEmailFwdMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.emailFwd.EPPEmailFwdMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPEmailFwdMapFactory to EPPCodec: "
						+ e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.

		/*
		   try
		   {
		       EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory");
		   }
		   catch (EPPCodecException e)
		   {
		       Assert.fail("EPPCodecException adding EPPContactMapFactory to EPPCodec: " + e);
		   }*/
		return suite;
	}

	// End EPPEmailFwdTst.suite()

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
								   "EPPEmailFwdTst Thread " + i,
								   EPPEmailFwdTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPEmailFwdTst.suite());
		}
	}

	// End EPPEmailFwdTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPEmailFwdTst.setNumIterations(long)

	/**
	 * Unit test of <code>EPPEmailFwdCheckCmd</code>.  The response to
	 * <code>EPPEmailFwdCheckCmd</code>     is
	 * <code>EPPEmailFwdCheckResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testEmailFwdCheck() {
		EPPEmailFwdCheckCmd  theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testEmailFwdCheck");

		// Check single emailFwd name
		theCommand     = new EPPEmailFwdCheckCmd(
												 "ABC-12345", "as@example1.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check multiple emailFwd names
		Vector emailFwds = new Vector();
		emailFwds.addElement("as@example1.com");
		emailFwds.addElement("as@example2.com");
		emailFwds.addElement("as@example3.com");

		theCommand     = new EPPEmailFwdCheckCmd("ABC-12345-XYZ", emailFwds);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// EmailFwd Check Responses
		EPPEmailFwdCheckResp theResponse;
		EPPEncodeDecodeStats responseStats;

		// Response for a single emailFwd name
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		// Testing EmailFwd Reason
		//EPPEmailFwdCheckResult emailFwdResult = new EPPEmailFwdCheckResult("example1.com", true);
		//emailFwdResult.setEmailFwdReason("In Use");
		//emailFwdResult.setLanguage("en");
		//
		//theResponse = new EPPEmailFwdCheckResp(respTransId, emailFwdResult);
		theResponse =
			new EPPEmailFwdCheckResp(
									 respTransId,
									 new EPPEmailFwdCheckResult(
																"as@example1.com",
																true));

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Response for multiple emailFwd names
		Vector emailFwdResults = new Vector();
		emailFwdResults.addElement(new EPPEmailFwdCheckResult(
															  "john@example1.com",
															  true));

		//emailFwdResults.addElement(new EPPEmailFwdCheckResult("example2.com", false));
		// Testing EmailFwd Reason
		EPPEmailFwdCheckResult emailFwdResult =
			new EPPEmailFwdCheckResult("john@example2.com", false);
		emailFwdResult.setEmailFwdReason("In Use");
		emailFwdResult.setLanguage("fr");
		emailFwdResults.addElement(emailFwdResult);

		emailFwdResults.addElement(new EPPEmailFwdCheckResult(
															  "john@example3.com",
															  true));

		theResponse = new EPPEmailFwdCheckResp(respTransId, emailFwdResults);
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testEmailFwdCheck");
	}

	// End EPPEmailFwdTst.testEmailFwdCheck()
}
