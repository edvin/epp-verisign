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
package com.verisign.epp.codec.defReg;

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
import com.verisign.epp.codec.idnext.EPPIdnLangTag;
import com.verisign.epp.interfaces.EPPDefReg;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.defReg package. The unit test
 * will  execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.defReg package concrete <code>EPPCommand</code>'s
 * and their  expected <code>EPPResponse</code>. The unit test is dependent on
 * the  use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * Change Hostory:  Modified test codes to accomdate the latest epp-defReg-03
 * protocol  <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPDefRegTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPDefRegTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPDefRegTst(String name) {
		super(name);
	}

	// End EPPDefRegTst(String)

	/**
	 * Unit test of <code>EPPDefRegInfoCmd</code>. The response to
	 * <code>EPPDefRegInfoCmd</code>  is <code>EPPDefRegInfoResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegInfo() {
		EPPCodecTst.printStart("testDefRegInfo");

		EPPDefRegInfoCmd theCommand =
			new EPPDefRegInfoCmd("ABC-12349", "EXAMPLE1-REP");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Encode EPPDefRegInfo Response
		EPPDefRegInfoResp    theResponse;

		EPPEncodeDecodeStats responseStats;

		Vector				 statuses = new Vector();

		statuses.addElement(new EPPDefRegStatus(EPPDefRegStatus.ELM_STATUS_OK));

		// Encode EPPDefRegInfoResp Full Response
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theTmDate = theCal.getTime();
		
		theResponse =
			new EPPDefRegInfoResp(
								  respTransId,
								  new EPPDefRegName(EPPDefRegName.LEVEL_PREMIUM, "ibm"),
								  "EXAMPLE1-REP", "registrant", "ibm", "US",
								  theTmDate, "registrant", statuses, "ClientY",
								  "registrant", new Date(),
								  new EPPAuthInfo("2fooBAR"));

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);
		
		
		// Encode EPPDefRegInfoResp Partial Response
		respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		theResponse =
			new EPPDefRegInfoResp();
		
		theResponse.setName(new EPPDefRegName(EPPDefRegName.LEVEL_PREMIUM, "ibm"));
		theResponse.setRoid("EXAMPLE1-REP");
		theResponse.setClientId("ClientX");
		theResponse.setResult(EPPResult.SUCCESS);
		theResponse.setTransId(respTransId);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);


		EPPCodecTst.printEnd("testDefRegInfo");
	}

	// End EPPDefRegTst.testDefRegInfo()

	/**
	 * Unit test of <code>EPPDefRegCreateCmd</code>. The response to
	 * <code>EPPDefRegCreateCmd</code>  is <code>EPPDefRegCreateResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegCreate() {
		EPPDefRegCreateCmd   theCommand;

		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDefRegCreate");
		
		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theTmDate = theCal.getTime();
		

		// Create a defReg with just the defReg name.
		theCommand =
			new EPPDefRegCreateCmd(
								   "ABC-12345",
								   new EPPDefRegName("premium", "ibm"), "XYZ",
								   "ibm", "US",
								   theTmDate,
								   "registrant", new EPPDefRegPeriod(10),
								   new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println("the command status is" + commandStats);

		System.out.println("coming until here");

		// Encode EPPDefRegCreate Response (EPPDefRegCreateResp)
		EPPDefRegCreateResp  theResponse;

		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDefRegCreateResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		theResponse =
			new EPPDefRegCreateResp(
									respTransId,
									new EPPDefRegName("premium", "verisign"),
									"XYZ-ABC",
									new GregorianCalendar(2001, 5, 5).getTime(),
									new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDefRegCreate");
	}

	// End EPPDefRegTst.testDefRegCreate()


	/**
	 * Unit test of <code>EPPDefRegCreateCmd</code> with <idnextension>. The response to
	 * <code>EPPDefRegCreateCmd</code>  is <code>EPPDefRegCreateResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegCreateIDN() {

        EPPCodecTst.printStart("testDefRegCreateIDN");

		//-- Extend Domain Create Command
        EPPDefRegCreateCmd theDefRegCommand ;
        Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theTmDate = theCal.getTime();
		
		
		theDefRegCommand = new  EPPDefRegCreateCmd("ABC-12345-XYZ", new EPPDefRegName(EPPDefReg.LEVEL_PREMIUM, "xn--gya"),
				           "sh8013", "Example", "US", theTmDate,
				          "SH8013", new EPPDefRegPeriod(2),new EPPAuthInfo("2fooBAR"));
		
		// Set IDN Extension
		theDefRegCommand.addExtension(new EPPIdnLangTag("GRE"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst.testEncodeDecode(theDefRegCommand);
		System.out.println(commandStats);
       
        EPPCodecTst.printEnd("testDefRegCreateIDN");
    }
	
	
	/**
	 * Unit test of <code>EPPDefRegDeleteCmd</code>. The response to
	 * <code>EPPDefRegDeleteCmd</code>  is <code>EPPDefRegCreateResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegDelete() {
		EPPCodecTst.printStart("testDefRegDelete");

		EPPDefRegDeleteCmd theCommand =
			new EPPDefRegDeleteCmd("XYZ", "EXAMPLE1-REP");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Encode Login Response (Standard EPPResponse)
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		EPPResponse theResponse = new EPPResponse(respTransId);

		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDefRegDelete");
	}

	// End EPPDefRegTst.testDefRegDelete()

	/**
	 * Unit test of <code>EPPDefRegRenewCmd</code>. The response to
	 * <code>EPPDefRegRenewCmd</code>  is <code>EPPDefRegRenewResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegRenew() {
		EPPCodecTst.printStart("testDefRegRenew");

		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theDate = theCal.getTime();
		
		EPPDefRegRenewCmd theCommand =
			new EPPDefRegRenewCmd(
								  "ABC-12345", "EXAMPLE1-REP",
								  theDate,
								  new EPPDefRegPeriod(5));

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		EPPDefRegRenewResp   theResponse;

		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDefRegRenewResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		theResponse =
			new EPPDefRegRenewResp(respTransId, "EXAMPLE1-REP", new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDefRegRenew");
	}

	// End EPPDefRegTst.testDefRegRenew()

	/**
	 * Unit test of <code>EPPDefRegUpdateCmd</code>. The response to
	 * <code>EPPDefRegUpdateCmd</code>  is <code>EPPResponse</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegUpate() {
		EPPCodecTst.printStart("testDefRegUpate");

		// add statuses
		Vector addStatuses = new Vector();

		addStatuses.addElement(new EPPDefRegStatus(
												   EPPDefRegStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED,
												   "en"));

		// remove statuses
		Vector removeStatuses = new Vector();

		removeStatuses.addElement(new EPPDefRegStatus(EPPDefRegStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED));

		EPPDefRegAddRemove addItems = new EPPDefRegAddRemove(addStatuses);

		EPPDefRegAddRemove removeItems = new EPPDefRegAddRemove(removeStatuses);

		Calendar theCal = Calendar.getInstance();
		theCal.setTimeZone(TimeZone.getTimeZone("UTC"));
		theCal.set(2000, 6, 22, 0, 0, 0);
		theCal.set(Calendar.MILLISECOND, 0);
		Date theTmDate = theCal.getTime();
		
		// change
		EPPDefRegAddRemove changeItems =
			new EPPDefRegAddRemove(
								   "IBMREGISTRANT", "ibm", "US",
								   theTmDate,
								   "registrant", new EPPAuthInfo("2fooBAR"));

		EPPDefRegUpdateCmd theCommand =
			new EPPDefRegUpdateCmd(
								   "ABC-12345-XYZ", "EXAMPLE1-REP", addItems,
								   removeItems, changeItems);

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Encode Update Response (Standard EPPResponse)
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		EPPResponse theResponse = new EPPResponse(respTransId);

		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDefRegUpate");
	}

	// End EPPDefRegTst.testDefRegUpate()

	/**
	 * Unit test of <code>EPPDefRegTransferCmd</code>. The response to
	 * <code>EPPDefRegTransferCmd</code>  is
	 * <code>EPPDefRegTransferResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegTransfer() {
		EPPDefRegTransferCmd theCommand;

		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDefRegTransfer");

		// Test Transfer Request Command
		theCommand =
			new EPPDefRegTransferCmd(
									 "ABC-12345", EPPCommand.OP_REQUEST,
									 "EXAMPLE1-REP", new EPPAuthInfo("2fooBAR"),
									 new EPPDefRegPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Encode EPPDefRegTransferResp
		EPPDefRegTransferResp theResponse;

		EPPEncodeDecodeStats  responseStats;

		EPPTransId			  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		theResponse = new EPPDefRegTransferResp(respTransId, "EXAMPLE1-REP");

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
			new EPPDefRegTransferCmd(
									 "ABC-12345", EPPCommand.OP_CANCEL,
									 "EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Transfer query
		theCommand =
			new EPPDefRegTransferCmd(
									 "ABC-12345", EPPCommand.OP_QUERY,
									 "EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// Transfer reject
		theCommand =
			new EPPDefRegTransferCmd(
									 "ABC-12345", EPPCommand.OP_REJECT,
									 "EXAMPLE1-REP");

		commandStats     = EPPCodecTst.testEncodeDecode(theCommand);

		// Transfer approve
		theCommand =
			new EPPDefRegTransferCmd(
									 "ABC-12345", EPPCommand.OP_APPROVE,
									 "EXAMPLE1-REP");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		EPPCodecTst.printEnd("testDefRegTransfer");
	}

	// End EPPDefRegTst.testDefRegTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and  initializes the <code>EPPDefRegMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPDefRegTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPDefRegTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPDefRegTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPDefRegTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDefRegMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.defReg.EPPDefRegMapFactory");
			 EPPFactory.getInstance().addExtFactory("com.verisign.epp.codec.idnext.EPPIdnExtFactory");
		}

		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDefRegMapFactory to EPPCodec: "
						+ e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.

		/*
		 *
		 * try
		 *  {
		 *
		 * EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory");
		 *  }
		 *
		 * catch (EPPCodecException e)
		 *  {
		 *
		 * Assert.fail("EPPCodecException adding EPPContactMapFactory to EPPCodec: " + e);
		 *
		 */
		return suite;
	}

	// End EPPDefRegTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:
	 * <br>
	 * 
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off
	 * (<code>false</code>).  If validate is not specified, validation will be
	 * off.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		System.out.println("entered here");

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
								   "EPPDefRegTst Thread " + i,
								   EPPDefRegTst.suite());

				thread.start();
			}
		}

		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPDefRegTst.suite());
		}
	}

	// End EPPDefRegTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPDefRegTst.setNumIterations(long)

	/**
	 * Unit test of <code>EPPDefRegCheckCmd</code>. The response to
	 * <code>EPPDefRegCheckCmd</code>  is <code>EPPDefRegCheckResp</code>.  <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is  a public method.
	 */
	public void testDefRegCheck() {
		EPPDefRegCheckCmd    theCommand;

		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDefRegCheck");

		// Check multiple defReg names
		Vector defRegs = new Vector();

		defRegs.addElement(new EPPDefRegName("premium", "ibm"));

		defRegs.addElement(new EPPDefRegName("premium", "verisign"));

		defRegs.addElement(new EPPDefRegName("standard", "java.sun.name"));

		theCommand     = new EPPDefRegCheckCmd("ABC-12345-XYZ", defRegs);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		System.out.println(commandStats);

		// DefReg Check Responses
		EPPDefRegCheckResp   theResponse;

		EPPEncodeDecodeStats responseStats;

		// Response for a single defReg name
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");

		Vector     defReg = new Vector();

		defReg.addElement(new EPPDefRegCheckResult(
												   "ash.SUNW****com", "standard",
												   true));

		///theResponse = new EPPDefRegCheckResp(respTransId, new EPPDefRegCheckResult("ash.SUNW****com","standard",
		// true));
		theResponse = new EPPDefRegCheckResp(respTransId, defReg);

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println("now the response is" + responseStats);

		// Response for multiple defReg names
		Vector defRegResults = new Vector();

		defRegResults.addElement(new EPPDefRegCheckResult(
														  "ibm", "premium", true));

		//defRegResults.addElement(new EPPDefRegCheckResult("example2.com", false));
		// Testing DefReg Reason
		//EPPDefRegName name1=new EPPDefRegName("standard","ash.ibm.com");
		EPPDefRegCheckResult defRegResult =
			new EPPDefRegCheckResult("ash.ibm.com", "standard", false);

		defRegResult.setDefRegReason("In Use");

		defRegResult.setLanguage("fr");

		defRegResults.addElement(defRegResult);

		//defRegResults.addElement(new EPPDefRegCheckResult("ash.verisign****.com", "standard", true));
		theResponse = new EPPDefRegCheckResp(respTransId, defRegResults);

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDefRegCheck");
	}

	// End EPPDefRegTst.testDefRegCheck()
}
