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
package com.verisign.epp.interfaces;


// JUNIT Imports
import junit.framework.*;

// Log4j imports
import org.apache.log4j.*;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.GregorianCalendar;
import java.util.Random;

import com.verisign.epp.codec.domain.*;
import com.verisign.epp.codec.emailFwd.*;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.persreg.*;

// EPP Imports
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPDomain;
import com.verisign.epp.interfaces.EPPEmailFwd;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the using the Personal Registration Extension classes with
 * the <code>EPPDomain</code> class <code>EPPEmailFwd</code> class. The unit
 * test will initialize a session with an EPP Server, will invoke
 * <code>EPPDomain</code> operations and <code>EPPEmailFwd</code> with
 * Personal Registration Extensions, and will end a session with an EPP
 * Server. The configuration file used by the unit test defaults to
 * epp.config, but can be changed by passing the file path as the first
 * command line argument. The unit test can be run in multiple threads by
 * setting the "threads" system property. For example, the unit test can be
 * run in 2 threads with the configuration file ../../epp.config with the
 * following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPPersRegTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPPersRegTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance
	 * (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app =
		EPPApplicationSingle.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat =
		Logger.getLogger(
						 EPPPersRegTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Domain associated with test */
	private EPPDomain domain = null;

	/** EPP Email Forwarding associated with test */
	private EPPEmailFwd email = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Connection to the EPP Server. */
	private EPPClientCon connection = null;

	/** Current test iteration */
	private int iteration = 0;

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPPersRegTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPPersRegTst(String name) {
		super(name);
	}

	// End EPPPersRegTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPPersRegTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPDomain
	 * interface.
	 */
	public void testPersReg() {
		int    numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}

		for (
			 iteration = 0;
				 (numIterations == 0) || (iteration < numIterations);
				 iteration++) {
			printStart("Test Suite");

			persRegInfo();

			persRegCreate();

			persRegErrCreate();

			persRegRenew();

			persRegTransfer();

			printEnd("Test Suite");
		}
	}

	// End EPPPersRegTst.testPersReg()

	/**
	 * Unit test of using the Personal Registration Extensions with
	 * <code>EPPDomain</code> Info and <code>EPPEmailFwd</code> Info.
	 */
	public void persRegInfo() {
		printStart("persRegInfo");

		try {
			EPPDomainInfoResp domainResponse;

			System.out.println("\npersRegInfo: Domain Info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domainResponse = domain.sendInfo();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegInfo: Response = [" + domainResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (domainResponse.hasExtension(EPPPersRegInfoData.class)) {
				EPPPersRegInfoData ext =
					(EPPPersRegInfoData) domainResponse.getExtension(EPPPersRegInfoData.class);

				System.out.println("persRegInfo: consent id = "
								   + ext.getConsentID());
			}

			else {
				System.out.println("persRegInfo: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			EPPEmailFwdInfoResp emailResponse;

			System.out.println("\npersRegInfo: EmailFwd Info");

			email.setTransId("ABC-12345-XYZ");

			email.addEmailFwdName(this.makeEmail());

			emailResponse = email.sendInfo();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegInfo: Response = [" + emailResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (emailResponse.hasExtension(EPPPersRegInfoData.class)) {
				EPPPersRegInfoData ext =
					(EPPPersRegInfoData) emailResponse.getExtension(EPPPersRegInfoData.class);

				System.out.println("persRegInfo: consent id = "
								   + ext.getConsentID());
			}

			else {
				System.out.println("persRegInfo: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("persRegInfo");
	}

	// End EPPPersRegTst.persRegInfo()

	/**
	 * Unit test of using the Personal Registration Extensions with
	 * <code>EPPDomain</code> Create and <code>EPPEmailFwd</code> Create.
	 */
	public void persRegCreate() {
		printStart("persRegCreate");

		// Try Successful Create
		try {
			EPPDomainCreateResp domainResponse;

			System.out.println("persRegCreate: Domain Create");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setAuthString("ClientX");

			// Set consent identifier
			domain.addExtension(new EPPPersRegCreate("ID:12345"));

			domainResponse = domain.sendCreate();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegCreate: Response = [" + domainResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (domainResponse.hasExtension(EPPPersRegCreateData.class)) {
				EPPPersRegCreateData ext =
					(EPPPersRegCreateData) domainResponse.getExtension(EPPPersRegCreateData.class);

				System.out.println("persRegCreate: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegCreate: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			EPPEmailFwdCreateResp emailResponse;

			System.out.println("persRegCreate: EmailFwd Create");

			email.setTransId("ABC-12345-XYZ");

			email.addEmailFwdName(this.makeEmail());

			email.setForwardTo("jdoe@example.com");

			email.setAuthString("ClientX");

			// Set consent identifier
			email.addExtension(new EPPPersRegCreate("ID:12345"));

			emailResponse = email.sendCreate();

			//-- Output all of the emailResponse attributes
			System.out.println("persRegCreate: Response = [" + emailResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (emailResponse.hasExtension(EPPPersRegCreateData.class)) {
				EPPPersRegCreateData ext =
					(EPPPersRegCreateData) emailResponse.getExtension(EPPPersRegCreateData.class);

				System.out.println("persRegCreate: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegCreate: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("persRegCreate");
	}

	// End EPPPersRegTst.persRegCreate()

	/**
	 * Unit test of using the Personal Registration Extensions with
	 * <code>EPPDomain</code> Create and <code>EPPEmailFwd</code> Create for
	 * error response. The Server Stub will use the Personal Registration
	 * Extension when passing a Domain or Email containing the string "error".
	 */
	public void persRegErrCreate() {
		printStart("persRegErrCreate");

		// Try Successful Create
		try {
			EPPDomainCreateResp domainResponse;

			System.out.println("persRegErrCreate: Domain Create");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName("error" + this.makeDomainName());

			domain.setAuthString("ClientX");

			domainResponse = domain.sendCreate();

			Assert.fail("persRegErrCreate: Unexpected success with create: "
						+ domainResponse);
		}

		 catch (EPPCommandException e) {
			EPPResponse errResponse = e.getResponse();

			System.out.println("persRegErrCreate: Error Response = ["
							   + errResponse + "]\n\n");

			//-- Output extension attribute(s)
			if (errResponse.hasExtension(EPPPersRegCreateErrData.class)) {
				EPPPersRegCreateErrData ext =
					(EPPPersRegCreateErrData) errResponse.getExtension(EPPPersRegCreateErrData.class);

				if (ext.getCode() == EPPPersRegCreateErrData.ERROR_CS_EXISTS) {
					System.out.println("persRegErrCreate: Corresponding Service Exists");
				}

				else if (
						 ext.getCode() == EPPPersRegCreateErrData.ERROR_DEFREG_EXISTS) {
					System.out.println("persRegErrCreate: Defensive Registration Exists");
				}
			}

			else {
				System.out.println("persRegErrCreate: Response has no extension");
			}
		}

		try {
			EPPEmailFwdCreateResp emailResponse;

			System.out.println("persRegErrCreate: EmailFwd Create");

			email.setTransId("ABC-12345-XYZ");

			email.addEmailFwdName("error" + this.makeEmail());

			email.setForwardTo("jdoe@example.com");

			email.setAuthString("ClientX");

			emailResponse = email.sendCreate();

			Assert.fail("persRegErrCreate: Unexpected success with create: "
						+ emailResponse);
		}

		 catch (EPPCommandException e) {
			EPPResponse errResponse = e.getResponse();

			System.out.println("persRegErrCreate: Error Response = ["
							   + errResponse + "]\n\n");

			//-- Output extension attribute(s)
			if (errResponse.hasExtension(EPPPersRegCreateErrData.class)) {
				EPPPersRegCreateErrData ext =
					(EPPPersRegCreateErrData) errResponse.getExtension(EPPPersRegCreateErrData.class);

				if (ext.getCode() == EPPPersRegCreateErrData.ERROR_CS_EXISTS) {
					System.out.println("persRegErrCreate: Corresponding Service Exists");
				}

				else if (
						 ext.getCode() == EPPPersRegCreateErrData.ERROR_DEFREG_EXISTS) {
					System.out.println("persRegErrCreate: Defensive Registration Exists");
				}
			}

			else {
				System.out.println("persRegErrCreate: Response has no extension");
			}
		}

		printEnd("persRegErrCreate");
	}

	// End EPPPersRegTst.persRegErrCreate()

	/**
	 * Unit test of using the Personal Registration Extensions with
	 * <code>EPPDomain</code> Renew and <code>EPPEmailFwd</code> Renew.
	 */
	public void persRegRenew() {
		printStart("persRegRenew");

		try {
			EPPDomainRenewResp domainResponse;

			System.out.println("\npersRegRenew: Domain Renew");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setExpirationDate(new GregorianCalendar(2004, 2, 3).getTime());

			domain.setPeriodLength(1);

			domain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			domainResponse = domain.sendRenew();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegRenew: Response = [" + domainResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (domainResponse.hasExtension(EPPPersRegRenewData.class)) {
				EPPPersRegRenewData ext =
					(EPPPersRegRenewData) domainResponse.getExtension(EPPPersRegRenewData.class);

				System.out.println("persRegRenew: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegRenew: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			EPPEmailFwdRenewResp emailResponse;

			System.out.println("\npersRegRenew: EmailFwd Renew");

			email.setTransId("ABC-12345-XYZ");

			email.addEmailFwdName(this.makeEmail());

			email.setExpirationDate(new GregorianCalendar(2004, 2, 3).getTime());

			email.setPeriodLength(1);

			email.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			emailResponse = email.sendRenew();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegRenew: Response = [" + emailResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (emailResponse.hasExtension(EPPPersRegRenewData.class)) {
				EPPPersRegRenewData ext =
					(EPPPersRegRenewData) emailResponse.getExtension(EPPPersRegRenewData.class);

				System.out.println("persRegRenew: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegRenew: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("persRegRenew");
	}

	// End EPPPersRegTst.persRegRenew()

	/**
	 * Unit test of using the Personal Registration Extensions with
	 * <code>EPPDomain</code> Transfer and <code>EPPEmailFwd</code> Transfer.
	 */
	public void persRegTransfer() {
		printStart("persRegTransfer");

		try {
			EPPDomainTransferResp domainResponse;

			System.out.println("\npersRegTransfer: Domain Transfer");

			domain.setTransferOpCode(EPPDomain.TRANSFER_REQUEST);

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setAuthString("ClientX");

			domain.setPeriodLength(1);

			domain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			domainResponse = domain.sendTransfer();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegTransfer: Response = [" + domainResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (domainResponse.hasExtension(EPPPersRegTransferData.class)) {
				EPPPersRegTransferData ext =
					(EPPPersRegTransferData) domainResponse.getExtension(EPPPersRegTransferData.class);

				System.out.println("persRegTransfer: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegTransfer: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			EPPEmailFwdTransferResp emailResponse;

			System.out.println("\npersRegTransfer: EmailFwd Transfer");

			email.setTransferOpCode(EPPEmailFwd.TRANSFER_REQUEST);

			email.setTransId("ABC-12345-XYZ");

			email.addEmailFwdName(this.makeEmail());

			email.setAuthString("ClientX");

			email.setPeriodLength(1);

			email.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			emailResponse = email.sendTransfer();

			//-- Output all of the domainResponse attributes
			System.out.println("persRegTransfer: Response = [" + emailResponse
							   + "]\n\n");

			//-- Output extension attribute(s)
			if (emailResponse.hasExtension(EPPPersRegTransferData.class)) {
				EPPPersRegTransferData ext =
					(EPPPersRegTransferData) emailResponse.getExtension(EPPPersRegTransferData.class);

				System.out.println("persRegTransfer: bundled rate = "
								   + ext.isBundledRate());
			}

			else {
				System.out.println("persRegTransfer: Response has no extension");
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("persRegTransfer");
	}

	// End EPPPersRegTst.persRegTransfer()

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
	}

	// End EPPPersRegTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the
	 * EPP Server will be terminated.
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
	}

	// End EPPPersRegTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			session = new EPPSession();

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
		}

		 catch (Exception e) {
			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		domain     = new EPPDomain(session);

		email = new EPPEmailFwd(session);
	}

	// End EPPPersRegTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPPersRegTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPPersRegTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPPersRegTst.class);

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

		return suite;
	}

	// End EPPPersRegTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by
	 * a server error, "Server Error :Response XML" will be specified. If the
	 * exception was caused by a general algorithm error, "General Error
	 * :Exception Description" will be specified.
	 *
	 * @param aException Exception thrown during test
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
	}

	// End EPPPersRegTst.handleException(EPPCommandException)

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
	 * (<code>false</code>). If validate is not specified, validation will be
	 * off.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

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
								   "EPPSessionTst Thread " + i,
								   EPPPersRegTst.suite());

				thread.start();
			}
		}

		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPPersRegTst.suite());
		}

		try {
			app.endApplication();
		}

		 catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPPersRegTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
						  + String.valueOf(tm + rd.nextInt(12)).substring(10)
						  + ".second.name");
	}

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeEmail() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
						  + String.valueOf(tm + rd.nextInt(12)).substring(10)
						  + "@second.name");
	}

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	private void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
							 + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration "
					 + iteration + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);

		System.out.println("****************************************************************\n");
	}

	// End EPPPersRegTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	private void printEnd(String aTest) {
		System.out.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
							 + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration "
					 + iteration + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);

		System.out.println("\n");
	}

	// End EPPPersRegTst.testEnd(String)

	/**
	 * Print message
	 *
	 * @param aMsg message to print
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
	}

	// End EPPPersRegTst.printMsg(String)

	/**
	 * Print error message
	 *
	 * @param aMsg errpr message to print
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
	}

	// End EPPPersRegTst.printError(String)
}


// End class EPPPersRegTst
