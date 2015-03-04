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
package com.verisign.epp.codec.domain;


// JUNIT Imports
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
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
import com.verisign.epp.codec.host.EPPHostAddress;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.domain package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.domain package concrete <code>EPPCommand</code>'s
 * and their expected <code>EPPResponse</code>. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * Change Hostory: Modified test codes to accomdate the latest epp-domain-03
 * protocol <br><br>
 */
public class EPPDomainTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;


	/**
	 * Creates a new EPPDomainTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPDomainTst(String name) {
		super(name);
	}

	// End EPPDomainTst(String)

	/**
	 * Unit test of <code>EPPDomainInfoCmd</code>. The response to
	 * <code>EPPDomainInfoCmd</code> is <code>EPPDomainInfoResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainInfo() {
		EPPCodecTst.printStart("testDomainInfo");
		
		EPPDomainInfoCmd theCommand;
		theCommand = new EPPDomainInfoCmd("ABC-12345", "example.com");
		
		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		theCommand =
			new EPPDomainInfoCmd("ABC-12349", "example.com");
		theCommand.setHosts(EPPDomainInfoCmd.HOSTS_DELEGATED);
		theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));

		commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPDomainInfo Response
		EPPDomainInfoResp    theResponse;
		EPPEncodeDecodeStats responseStats;

		Vector				 statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

		// Test with just required EPPDomainInfoResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPDomainInfoResp(
								  respTransId, "EXAMPLE1-VRSN", "example.com",
								  "ClientX", statuses, "ClientY", new Date(),
								  new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Test with all EPPDomainInfoResp attributes set.
		theResponse.setRegistrant("JD1234-VRSN");

		Vector servers = new Vector();
		servers.addElement("ns1.example.com");
		servers.addElement("ns2.example.com");
		theResponse.setNses(servers);

		Vector hosts = new Vector();
		hosts.addElement("ns1.example.com");
		hosts.addElement("ns2.example.com");
		theResponse.setHosts(hosts);

		// Is contacts supported?
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			Vector contacts = new Vector();
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_TECHNICAL));
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

		EPPCodecTst.printEnd("testDomainInfo");
	}

	// End EPPDomainTst.testDomainInfo()

	/**
	 * Unit test of <code>EPPDomainPendActionMsg</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainPendActionMsg() {
		EPPCodecTst.printStart("testDomainPendActionMsg");

		EPPTransId thePollTransId = new EPPTransId("ABC-12349", "54321-XYZ");
		EPPTransId thePendingTransId = new EPPTransId("DEF-12349", "12345-XYZ");

		EPPDomainPendActionMsg theMsg =
			new EPPDomainPendActionMsg(
									   thePollTransId, "example.com", false,
									   thePendingTransId, new Date());
		theMsg.setResult(EPPResult.SUCCESS_POLL_MSG);
		
		// Check succcess values
		Assert.assertEquals("testDomainPendActionMsg: response success is incorrect",
				theMsg.isSuccess(), true);
		Assert.assertEquals("testDomainPendActionMsg: PA success is incorrect", 
				theMsg.isPASuccess(), false);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theMsg);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainPendActionMsg");
	}

	// End EPPDomainTst.testDomainPendActionMsg()

	/**
	 * Unit test of <code>EPPDomainCreateCmd</code>. The response to
	 * <code>EPPDomainCreateCmd</code> is <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainCreate() {
		EPPDomainCreateCmd   theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainCreate");

		// Create a domain with just the domain name.
		theCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345", "example.com",
								   new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a domain with all of the attributes accept for contacts
		Vector servers = new Vector();
		servers.addElement("ns1.example.com");
		servers.addElement("ns2.example.com");

		Vector contacts = null;

		// Is contacts supported?
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			contacts = new Vector();
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_TECHNICAL));
		}

		theCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345-XYZ", "example.com", servers,
								   contacts, new EPPDomainPeriod(2),
								   new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a domain with server attribute
		theCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345", "example.com",
								   new EPPAuthInfo("2fooBAR"));

		Vector serverAttrs = new Vector();
		serverAttrs.add(new EPPHostAttr("ns1.example.com"));

		Vector hostAddresses = new Vector();
		hostAddresses.add(new EPPHostAddress("197.162.10.10"));
		serverAttrs.add(new EPPHostAttr("ns2.example.com", hostAddresses));
		theCommand.setServers(serverAttrs);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPDomainCreate Response (EPPDomainCreateResp)
		EPPDomainCreateResp  theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDomainCreateResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPDomainCreateResp(
									respTransId, "example.com",
									new GregorianCalendar(2001, 5, 5).getTime(),
									new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainCreate");
	}

	// End EPPDomainTst.testDomainCreate()

	/**
	 * Unit test of <code>EPPDomainDeleteCmd</code>. The response to
	 * <code>EPPDomainDeleteCmd</code> is <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainDelete() {
		EPPCodecTst.printStart("testDomainDelete");

		EPPDomainDeleteCmd theCommand =
			new EPPDomainDeleteCmd("ABC-12345", "example.com");

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

		EPPCodecTst.printEnd("testDomainDelete");
	}

	// End EPPDomainTst.testDomainDelete()

	/**
	 * Unit test of <code>EPPDomainRenewCmd</code>. The response to
	 * <code>EPPDomainRenewCmd</code> is <code>EPPDomainRenewResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainRenew() {
		EPPCodecTst.printStart("testDomainRenew");
		
		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theDate = theCal.getTime();
		
		EPPDomainRenewCmd theCommand =
			new EPPDomainRenewCmd(
								  "ABC-12345", "exampleZ.com",
								  theDate,
								  new EPPDomainPeriod(5));

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPDomainRenewResp   theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDomainRenewResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPDomainRenewResp(respTransId, "exampleX.com", new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainRenew");
	}

	// End EPPDomainTst.testDomainRenew()

	/**
	 * Unit test of <code>EPPDomainUpdateCmd</code>. The response to
	 * <code>EPPDomainUpdateCmd</code> is <code>EPPResponse</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainUpdate() {
		EPPCodecTst.printStart("testDomainUpdate");

		//add
		Vector addServers = new Vector();
		addServers.addElement("ns1.example.com");

		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPDomainStatus(
												   EPPDomainStatus.ELM_STATUS_CLIENT_HOLD,
												   "The description"));

		Vector addContacts = null;

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			addContacts = new Vector();
			addContacts.addElement(new EPPDomainContact(
														"MAK21-VRSN",
														EPPDomainContact.TYPE_TECHNICAL));
		}

		//remove
		Vector removeServers = new Vector();
		removeServers.addElement("ns2.example.com");

		Vector removeStatuses = new Vector();
		removeStatuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_CLIENT_HOLD));

		Vector removeContacts = null;

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			removeContacts = new Vector();
			removeContacts.addElement(new EPPDomainContact(
														   "SH8013-VRSN",
														   EPPDomainContact.TYPE_TECHNICAL));
		}

		// change
		// ...
		EPPDomainAddRemove addItems =
			new EPPDomainAddRemove(addServers, addContacts, addStatuses);
		EPPDomainAddRemove removeItems =
			new EPPDomainAddRemove(
								   removeServers, removeContacts, removeStatuses);
		EPPDomainAddRemove changeItems =
			new EPPDomainAddRemove("SH8013-VRSN", new EPPAuthInfo("2fooBAR"));

		EPPDomainUpdateCmd theCommand =
			new EPPDomainUpdateCmd(
								   "ABC-12345-XYZ", "example.com", addItems,
								   removeItems, changeItems);
		


		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check empty EPPDomainAddRemove
		EPPDomainAddRemove emptyChangeItems =
			new EPPDomainAddRemove();
		Assert.assertTrue(emptyChangeItems.isEmpty());
		
		
		// Encode Update Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainUpdate");
	}

	// End EPPDomainTst.testDomainUpdate()

	/**
	 * Unit test of <code>EPPDomainTransferCmd</code>. The response to
	 * <code>EPPDomainTransferCmd</code> is
	 * <code>EPPDomainTransferResp</code>. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainTransfer() {
		EPPDomainTransferCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainTransfer");

		// Test Transfer Request Command (without authInfo roid)
		theCommand =
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_REQUEST,
									 "example.com", new EPPAuthInfo("2fooBAR"),
									 new EPPDomainPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test Transfer Request Command (with authInfo roid)
		EPPAuthInfo theAuthInfo = new EPPAuthInfo("2fooBAR");
		theAuthInfo.setRoid("JD1234-REP");
		
		theCommand =
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_REQUEST,
									 "example.com", theAuthInfo,
									 new EPPDomainPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		
		// Encode EPPDomainTransferResp
		EPPDomainTransferResp theResponse;
		EPPEncodeDecodeStats  responseStats;

		EPPTransId			  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPDomainTransferResp(respTransId, "example.com");
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
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_CANCEL,
									 "example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer query
		theCommand =
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_QUERY,
									 "example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer reject
		theCommand =
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_REJECT,
									 "example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer approve
		theCommand =
			new EPPDomainTransferCmd(
									 "ABC-12345", EPPCommand.OP_APPROVE,
									 "example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testDomainTransfer");
	}

	// End EPPDomainTst.testDomainTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPDomainTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPDomainTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}


		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.domain.EPPDomainMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDomainMapFactory to EPPCodec: "
						+ e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.

		/*
		 * try {
		 * EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory"); }
		 * catch (EPPCodecException e) { Assert.fail("EPPCodecException adding
		 * EPPContactMapFactory to EPPCodec: " + e);
		 */
		return suite;
	}

	// End EPPDomainTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.
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
								   "EPPDomainTst Thread " + i,
								   EPPDomainTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPDomainTst.suite());
		}
	}

	// End EPPDomainTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPDomainTst.setNumIterations(long)

	/**
	 * Unit test of <code>EPPDomainCheckCmd</code>. The response to
	 * <code>EPPDomainCheckCmd</code> is <code>EPPDomainCheckResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainCheck() {
		EPPDomainCheckCmd    theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainCheck");

		// Check single domain name
		theCommand     = new EPPDomainCheckCmd("ABC-12345", "example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check multiple domain names
		Vector domains = new Vector();
		domains.addElement("example1.com");
		domains.addElement("example2.com");
		domains.addElement("example3.com");

		theCommand     = new EPPDomainCheckCmd("ABC-12345-XYZ", domains);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Domain Check Responses
		EPPDomainCheckResp   theResponse;
		EPPEncodeDecodeStats responseStats;

		// Response for a single domain name
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		// Testing Domain Reason
		//EPPDomainCheckResult domainResult = new
		// EPPDomainCheckResult("example1.com", true);
		//domainResult.setDomainReason("In Use");
		//domainResult.setLanguage("en");
		//
		//theResponse = new EPPDomainCheckResp(respTransId, domainResult);
		theResponse =
			new EPPDomainCheckResp(
								   respTransId,
								   new EPPDomainCheckResult(
															"example1.com", true));

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Response for multiple domain names
		Vector domainResults = new Vector();
		domainResults.addElement(new EPPDomainCheckResult("example1.com", true));

		//domainResults.addElement(new EPPDomainCheckResult("example2.com",
		// false));
		// Testing Domain Reason
		EPPDomainCheckResult domainResult =
			new EPPDomainCheckResult("example2.com", false);
		domainResult.setDomainReason("In Use");
		domainResult.setLanguage("fr");
		domainResults.addElement(domainResult);

		domainResults.addElement(new EPPDomainCheckResult("example3.com", true));

		theResponse = new EPPDomainCheckResp(respTransId, domainResults);
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainCheck");
	}

	// End EPPDomainTst.testDomainCheck()
}
