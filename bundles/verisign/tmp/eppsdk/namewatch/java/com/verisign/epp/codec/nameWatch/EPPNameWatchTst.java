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
package com.verisign.epp.codec.nameWatch;

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
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.nameWatch package.  The unit
 * test will     execute, gather statistics, and output the results of a test
 * of each com.verisign.epp.codec.nameWatch package concrete
 * <code>EPPCommand</code>'s and their     expected <code>EPPResponse</code>.
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * Change Hostory: Modified test codes to accomdate the latest
 * epp-nameWatch-03 protocol     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPNameWatchTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPNameWatchTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPNameWatchTst(String name) {
		super(name);
	}

	// End EPPNameWatchTst(String)

	/**
	 * Unit test of <code>EPPNameWatchInfoCmd</code>.  The response to
	 * <code>EPPNameWatchInfoCmd</code>     is
	 * <code>EPPNameWatchInfoResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchInfo() {
		EPPCodecTst.printStart("testNameWatchInfo");

		EPPNameWatchInfoCmd  theCommand =
			new EPPNameWatchInfoCmd("ABC-12349", "EXAMPLE1-REP");
		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPNameWatchInfo Full Response
		EPPNameWatchInfoResp theResponse;
		EPPEncodeDecodeStats responseStats;

		Vector				 statuses = new Vector();
		statuses.addElement(new EPPNameWatchStatus(EPPNameWatchStatus.ELM_STATUS_OK));

		// Test with just required EPPNameWatchInfoResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPNameWatchInfoResp(
									 respTransId, "doe", "EXAMPLE1-REP",
									 "jd1234",
									 new EPPNameWatchRptTo(
														   "weekly",
														   "jdoe@example.com"),
									 statuses, "ClientX", "ClientY",
									 new GregorianCalendar(1999, 4, 3).getTime(),
									 new GregorianCalendar(2002, 4, 3).getTime());

		theResponse.setAuthInfo(new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// test all
		theResponse.setLastUpdatedBy("ClientX");
		theResponse.setLastUpdatedDate(new GregorianCalendar(1999, 12, 3)
									   .getTime());
		theResponse.setLastTransferDate(new GregorianCalendar(2000, 4, 8)
										.getTime());

		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse.setTransId(respTransId);

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Encode EPPNameWatchInfo Partail Response
		theResponse = new EPPNameWatchInfoResp();
		
		theResponse.setName("doe");
		theResponse.setRoid("EXAMPLE1-REP");
		theResponse.setClientId("ClientX");

		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse.setTransId(respTransId);

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);
		
		EPPCodecTst.printEnd("testNameWatchInfo");
	}

	// End EPPNameWatchTst.testNameWatchInfo()

	/**
	 * Unit test of <code>EPPNameWatchCreateCmd</code>.  The response to
	 * <code>EPPNameWatchCreateCmd</code>     is
	 * <code>EPPNameWatchCreateResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchCreate() {
		EPPNameWatchCreateCmd theCommand;
		EPPEncodeDecodeStats  commandStats;

		EPPCodecTst.printStart("testNameWatchCreate");

		// Create a nameWatch with just the nameWatch name.
		theCommand =
			new EPPNameWatchCreateCmd(
									  "ABC-12345", "doe", "jd1234",
									  new EPPNameWatchRptTo(
															"weekly",
															"jdoe@example.com"),
									  new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a nameWatch with all of the attributes
		theCommand.setPeriod(new EPPNameWatchPeriod(1));
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPNameWatchCreate Response (EPPNameWatchCreateResp)
		EPPNameWatchCreateResp theResponse;
		EPPEncodeDecodeStats   responseStats;

		// Test with just required EPPNameWatchCreateResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPNameWatchCreateResp(
									   respTransId, "doe", "EXAMPLE1-REP",
									   new GregorianCalendar(2001, 5, 5)
									   .getTime(), new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testNameWatchCreate");
	}

	// End EPPNameWatchTst.testNameWatchCreate()

	/**
	 * Unit test of <code>EPPNameWatchDeleteCmd</code>.  The response to
	 * <code>EPPNameWatchDeleteCmd</code>     is
	 * <code>EPPNameWatchCreateResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchDelete() {
		EPPCodecTst.printStart("testNameWatchDelete");

		EPPNameWatchDeleteCmd theCommand =
			new EPPNameWatchDeleteCmd("ABC-12345", "EXAMPLE1-REP");

		EPPEncodeDecodeStats  commandStats =
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

		EPPCodecTst.printEnd("testNameWatchDelete");
	}

	// End EPPNameWatchTst.testNameWatchDelete()

	/**
	 * Unit test of <code>EPPNameWatchRenewCmd</code>.  The response to
	 * <code>EPPNameWatchRenewCmd</code>     is
	 * <code>EPPNameWatchRenewResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchRenew() {
		EPPCodecTst.printStart("testNameWatchRenew");

		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theDate = theCal.getTime();
		
		
		EPPNameWatchRenewCmd theCommand =
			new EPPNameWatchRenewCmd(
									 "ABC-12345", "EXAMPLE1-REP",
									 theDate,
									 new EPPNameWatchPeriod(5));

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPNameWatchRenewResp theResponse;
		EPPEncodeDecodeStats  responseStats;

		// Test with just required EPPNameWatchRenewResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPNameWatchRenewResp(
									  respTransId, "EXAMPLE1-REP",
									  new GregorianCalendar(2000, 4, 3).getTime());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testNameWatchRenew");
	}

	// End EPPNameWatchTst.testNameWatchRenew()

	/**
	 * Unit test of <code>EPPNameWatchUpdateCmd</code>.  The response to
	 * <code>EPPNameWatchUpdateCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchUpate() {
		EPPCodecTst.printStart("testNameWatchUpate");

		// add statuses
		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPNameWatchStatus(
													  EPPNameWatchStatus.ELM_STATUS_CLIENT_HOLD,
													  "en"));

		// remove statuses
		Vector removeStatuses = new Vector();
		removeStatuses.addElement(new EPPNameWatchStatus(EPPNameWatchStatus.ELM_STATUS_CLIENT_HOLD));

		// Change
		EPPNameWatchAddRemove addItems    =
			new EPPNameWatchAddRemove(addStatuses);
		EPPNameWatchAddRemove removeItems =
			new EPPNameWatchAddRemove(removeStatuses);

		EPPNameWatchAddRemove changeItems =
			new EPPNameWatchAddRemove(
									  "SH8013",
									  new EPPNameWatchRptTo(
															"daily",
															"jdoe@example.com"),
									  new EPPAuthInfo("2fooBAR"));

		EPPNameWatchUpdateCmd theCommand =
			new EPPNameWatchUpdateCmd(
									  "ABC-12345-XYZ", "EXAMPLE1-REP", addItems,
									  removeItems, changeItems);

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

		EPPCodecTst.printEnd("testNameWatchUpate");
	}

	// End EPPNameWatchTst.testNameWatchUpate()

	/**
	 * Unit test of <code>EPPNameWatchTransferCmd</code>.  The response to
	 * <code>EPPNameWatchTransferCmd</code>     is
	 * <code>EPPNameWatchTransferResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testNameWatchTransfer() {
		EPPNameWatchTransferCmd theCommand;
		EPPEncodeDecodeStats    commandStats;

		EPPCodecTst.printStart("testNameWatchTransfer");

		// Test Transfer Request Command
		theCommand =
			new EPPNameWatchTransferCmd(
										"ABC-12345", EPPCommand.OP_REQUEST,
										"EXAMPLE1-REP",
										new EPPAuthInfo("2fooBAR"),
										new EPPNameWatchPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPNameWatchTransferResp
		EPPNameWatchTransferResp theResponse;
		EPPEncodeDecodeStats     responseStats;

		EPPTransId				 respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPNameWatchTransferResp(respTransId, "EXAMPLE1-REP");
		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.setRequestClient("ClientX");
		theResponse.setActionClient("ClientY");
		theResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);
		theResponse.setRequestDate(new GregorianCalendar(2000, 6, 8).getTime());
		theResponse.setActionDate(new GregorianCalendar(2000, 6, 13).getTime());
		theResponse.setExpirationDate(new GregorianCalendar(2002, 9, 8).getTime());

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer cancel
		theCommand =
			new EPPNameWatchTransferCmd(
										"ABC-12345", EPPCommand.OP_CANCEL,
										"EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer query
		theCommand =
			new EPPNameWatchTransferCmd(
										"ABC-12345", EPPCommand.OP_QUERY,
										"EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer reject
		theCommand =
			new EPPNameWatchTransferCmd(
										"ABC-12345", EPPCommand.OP_REJECT,
										"EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer approve
		theCommand =
			new EPPNameWatchTransferCmd(
										"ABC-12345", EPPCommand.OP_APPROVE,
										"EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testNameWatchTransfer");
	}

	// End EPPNameWatchTst.testNameWatchTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the
	 * <code>EPPNameWatchMapFactory</code> with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPNameWatchTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPNameWatchTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPNameWatchTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPNameWatchTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPNameWatchMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.nameWatch.EPPNameWatchMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPNameWatchMapFactory to EPPCodec: "
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

	// End EPPNameWatchTst.suite()

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
								   "EPPNameWatchTst Thread " + i,
								   EPPNameWatchTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPNameWatchTst.suite());
		}
	}

	// End EPPNameWatchTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPNameWatchTst.setNumIterations(long)
}
