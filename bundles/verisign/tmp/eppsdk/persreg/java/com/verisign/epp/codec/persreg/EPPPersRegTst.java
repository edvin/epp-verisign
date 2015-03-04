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

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateCmd;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdStatus;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.persreg package.  The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.persreg package concrete extension
 * <code>EPPCodecComponent</code>'s.  The Domain and Email Forwarding commands
 * and responses are used to test extending both.<br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPPersRegTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/** Is XML validation on?  This is set in <code>EPPCodecTst.main</code> */
	static private boolean validate = true;

	/**
	 * JAXP DOM Document Builder, which abstracts the XML parser used to encode
	 * and decode     the XML documents.
	 */
	static private DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();

	// Need to initialize the logging facility?  The initialization is
	// done in <code>suite()</code>.

	/** DOCUMENT ME! */
	private static boolean _initLogging = true;

	/**
	 * Creates a new EPPPersRegTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPPersRegTst(String name) {
		super(name);
	}

	// End EPPPersRegTst(String)

	/**
	 * Unit test of extending the Domain and Email Forwarding info responses. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testPersRegInfo() {
		EPPCodecTst.printStart("testPersRegInfo");

		// Generic objects
		EPPTransId			 respTransId =
			new EPPTransId("ABC-12345", "54321-XYZ");
		EPPEncodeDecodeStats theStats;

		//-- Extend Email Forwarding Info Response
		EPPEmailFwdInfoResp theEmailResponse;

		Vector			    emailStatuses = new Vector();
		emailStatuses.addElement(new EPPEmailFwdStatus(EPPEmailFwdStatus.ELM_STATUS_OK));

		theEmailResponse =
			new EPPEmailFwdInfoResp(
									respTransId, "EXAMPLE1-VRSN",
									"john@doe.name", "jdoe@example.com",
									"ClientX", emailStatuses, "ClientY",
									new Date(), new EPPAuthInfo("2fooBAR"));
		theEmailResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theEmailResponse.addExtension(new EPPPersRegInfoData("ID:12345"));

		theStats = EPPCodecTst.testEncodeDecode(theEmailResponse);
		System.out.println(theStats);

		//-- Extend Domain Info Response
		EPPDomainInfoResp theDomainResponse;

		Vector			  domainStatuses = new Vector();
		domainStatuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

		theDomainResponse =
			new EPPDomainInfoResp(
								  respTransId, "EXAMPLE1-VRSN", "example.com",
								  "ClientX", domainStatuses, "ClientY",
								  new Date(), new EPPAuthInfo("2fooBAR"));
		theDomainResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theDomainResponse.addExtension(new EPPPersRegInfoData("ID:12345"));

		theStats = EPPCodecTst.testEncodeDecode(theDomainResponse);
		System.out.println(theStats);

		EPPCodecTst.printEnd("testPersRegInfo");
	}

	// End EPPPersRegTst.testPersRegInfo()

	/**
	 * Unit test of extending the Domain and Email Forwarding create commands
	 * and responses.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testPersRegCreate() {
		EPPCodecTst.printStart("testPersRegCreate");

		// Generic objects
		EPPTransId			 respTransId =
			new EPPTransId("ABC-12345", "54321-XYZ");
		EPPEncodeDecodeStats theStats;

		//-- Extend Email Forwarding Create Command
		EPPEmailFwdCreateCmd theEmailCommand;
		theEmailCommand =
			new EPPEmailFwdCreateCmd(
									 "ABC-12345", "john@doe.name",
									 "jdoe@example.com",
									 new EPPAuthInfo("2fooBAR"));

		// Extension
		theEmailCommand.addExtension(new EPPPersRegCreate("ID:12345"));

		theStats = EPPCodecTst.testEncodeDecode(theEmailCommand);
		System.out.println(theStats);

		//-- Extend Email Forwarding Create Response
		EPPEmailFwdCreateResp theEmailResponse;

		// Test with just required EPPPersRegCreateResp attributes.
		theEmailResponse =
			new EPPEmailFwdCreateResp(
									  respTransId, "john@doe.name",
									  new GregorianCalendar(2001, 5, 5).getTime(),
									  new Date());
		theEmailResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theEmailResponse.addExtension(new EPPPersRegCreateData(true));

		theStats = EPPCodecTst.testEncodeDecode(theEmailResponse);
		System.out.println(theStats);

		//-- Extend Domain Create Command
		EPPDomainCreateCmd theDomainCommand;
		theDomainCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345", "john.doe.name",
								   new EPPAuthInfo("2fooBAR"));

		// Extension
		theDomainCommand.addExtension(new EPPPersRegCreate("ID:12345"));

		theStats = EPPCodecTst.testEncodeDecode(theDomainCommand);
		System.out.println(theStats);

		//-- Extend Domain Create Response
		EPPDomainCreateResp theDomainResponse;

		// Test with just required EPPPersRegCreateResp attributes.
		theDomainResponse =
			new EPPDomainCreateResp(
									respTransId, "john.doe.name",
									new GregorianCalendar(2001, 5, 5).getTime(),
									new Date());
		theDomainResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theDomainResponse.addExtension(new EPPPersRegCreateData(false));

		theStats = EPPCodecTst.testEncodeDecode(theDomainResponse);
		System.out.println(theStats);

		EPPCodecTst.printEnd("testPersRegCreate");
	}

	// End EPPPersRegTst.testPersRegCreate()

	/**
	 * Unit test of extending the Domain and Email Forwarding renew responses. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testPersRegRenew() {
		EPPCodecTst.printStart("testPersRegRenew");

		// Generic objects
		EPPTransId			 respTransId =
			new EPPTransId("ABC-12345", "54321-XYZ");
		EPPEncodeDecodeStats theStats;

		//-- Extend Email Forwarding Renew Response
		EPPEmailFwdRenewResp theEmailResponse;

		theEmailResponse =
			new EPPEmailFwdRenewResp(respTransId, "john@doe.name", new Date());
		theEmailResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theEmailResponse.addExtension(new EPPPersRegRenewData(true));

		theStats = EPPCodecTst.testEncodeDecode(theEmailResponse);
		System.out.println(theStats);

		//-- Extend Domain Renew Response
		EPPDomainRenewResp theDomainResponse;

		theDomainResponse =
			new EPPDomainRenewResp(respTransId, "john.doe.name", new Date());
		theDomainResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theDomainResponse.addExtension(new EPPPersRegRenewData(false));

		theStats = EPPCodecTst.testEncodeDecode(theDomainResponse);
		System.out.println(theStats);

		EPPCodecTst.printEnd("testPersRegRenew");
	}

	// End EPPPersRegTst.testPersRegRenew()

	/**
	 * Unit test of extending the Domain and Email Forwarding transfer
	 * responses. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testPersRegTransfer() {
		EPPCodecTst.printStart("testPersRegTransfer");

		// Generic objects
		EPPTransId			 respTransId =
			new EPPTransId("ABC-12345", "54321-XYZ");
		EPPEncodeDecodeStats theStats;

		//-- Extend Email Forwarding Transfer Response
		EPPEmailFwdTransferResp theEmailResponse;

		theEmailResponse =
			new EPPEmailFwdTransferResp(respTransId, "john@doe.name");
		theEmailResponse.setResult(EPPResult.SUCCESS);

		theEmailResponse.setRequestClient("ClientX");
		theEmailResponse.setActionClient("ClientY");
		theEmailResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);
		theEmailResponse.setRequestDate(new Date());
		theEmailResponse.setActionDate(new Date());
		theEmailResponse.setExpirationDate(new Date());

		// Extension
		theEmailResponse.addExtension(new EPPPersRegTransferData(true));

		theStats = EPPCodecTst.testEncodeDecode(theEmailResponse);
		System.out.println(theStats);

		//-- Extend Domain Transfer Response
		EPPDomainTransferResp theDomainResponse;

		theDomainResponse =
			new EPPDomainTransferResp(respTransId, "john.doe.name");
		theDomainResponse.setResult(EPPResult.SUCCESS);

		theDomainResponse.setRequestClient("ClientX");
		theDomainResponse.setActionClient("ClientY");
		theDomainResponse.setTransferStatus(EPPResponse.TRANSFER_PENDING);
		theDomainResponse.setRequestDate(new Date());
		theDomainResponse.setActionDate(new Date());
		theDomainResponse.setExpirationDate(new Date());

		// Extension
		theDomainResponse.addExtension(new EPPPersRegTransferData(false));

		theStats = EPPCodecTst.testEncodeDecode(theDomainResponse);
		System.out.println(theStats);

		EPPCodecTst.printEnd("testPersRegTransfer");
	}

	// End EPPPersRegTst.testPersRegTransfer()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the
	 * <code>EPPPersRegMapFactory</code> with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPPersRegTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPPersRegTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPPersRegTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPPersRegTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPPersRegMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.emailFwd.EPPEmailFwdMapFactory");
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addExtFactory("com.verisign.epp.codec.persreg.EPPPersRegExtFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding factories to EPPCodec: " + e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.
		return suite;
	}

	// End EPPPersRegTst.suite()

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
								   "EPPPersRegTst Thread " + i,
								   EPPPersRegTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPPersRegTst.suite());
		}
	}

	// End EPPPersRegTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPPersRegTst.setNumIterations(long)

	/**
	 * Sets XML parser validation on/off.
	 *
	 * @param aValidate <code>true</code> to turn XML parser validation on;
	 * 		  <code>false</code> otherwise.
	 */
	public static void setValidate(boolean aValidate) {
		validate = aValidate;
	}

	// End EPPPersRegTst.setValidate(boolean)

	/**
	 * Sets the XML document builder factory to use during the test.
	 *
	 * @param aFactory Initialized instance of
	 * 		  <code>DocumentBuilderFactory</code>.  The
	 * 		  <code>DocumentBuilderFactory</code> is used to turn on/off
	 * 		  attributes like validation and namespace awareness.
	 */
	public static void setFactory(DocumentBuilderFactory aFactory) {
		factory = aFactory;
	}

	// End EPPPersRegTst.setFactory(DocumentBuilderFactory)
}
