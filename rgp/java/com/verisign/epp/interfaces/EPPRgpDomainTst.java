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
package com.verisign.epp.interfaces;


// JUNIT Imports
import java.util.Date;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.rgpext.EPPRgpExtInfData;
import com.verisign.epp.codec.rgpext.EPPRgpExtReport;
import com.verisign.epp.codec.rgpext.EPPRgpExtReportText;
import com.verisign.epp.codec.rgpext.EPPRgpExtRestore;
import com.verisign.epp.codec.rgpext.EPPRgpExtStatus;
import com.verisign.epp.codec.rgpext.EPPRgpExtUpdate;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the <code>EPPDomain</code> class with RGP update,
 * inf data and report extensions.
 *
 * The unit test will
 * initialize a session with an EPP Server, will invoke <code>EPPDomain</code>
 * operations, and will end a session with an EPP Server. The configuration
 * file used by the unit test defaults to epp.config, but can be changed by
 * passing the file path as the first command line argument. The unit test can
 * be run in multiple threads by setting the "threads" system property. For
 * example, the unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPRgpDomainTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br><br><br>
 */
public class EPPRgpDomainTst extends TestCase {
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
						 EPPRgpDomainTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Domain associated with test */
	private EPPDomain domain = null;

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
	 * Allocates an <code>EPPRgpDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPRgpDomainTst(String name) {
		super(name);
	}

	// End EPPRgpDomainTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPRgpDomainTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPDomain
	 * interface.
	 */
	public void testDomain() {
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

			domainRestoreRequest();

			domainRestoreReport();

            domainInfo();

			domainPoll();

			printEnd("Test Suite");
		}
	}

	// End EPPRgpDomainTst.testDomain()

	/**
	 * Unit test of <code>EPPDomain.sendInfo</code>.
	 */
	public void domainInfo() {
		printStart("domainInfo");

		EPPDomainInfoResp response;

		try {
			System.out.println("\ndomainInfo: Domain info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("domainInfo: Response = [" + response + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("domainInfo: name = " + response.getName());

			System.out.println("domainInfo: client id = "
							   + response.getClientId());

			System.out.println("domainInfo: created by = "
							   + response.getCreatedBy());

			System.out.println("domainInfo: create date = "
							   + response.getCreatedDate());

			System.out.println("domainInfo: expiration date = "
							   + response.getExpirationDate());

			this.printRgpStatuses(response);

			domain.setTransId( "ABC-12345-XYZ" );

			domain.addDomainName( "graceperiod.com" );
			response = domain.sendInfo();

			// -- Output all of the response attributes
			System.out.println( "domainInfo: Response for graceperiod.com = ["
					+ response + "]\n\n" );
			
			this.printRgpStatuses(response);

		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainInfo");
	}

	// End EPPRgpDomainTst.domainInfo()


	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> with RGP restore request extension.
	 */
	public void domainRestoreRequest() {
		printStart("domainRestoreRequest");

		EPPResponse response;

		try {
			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

           domain.addExtension(new EPPRgpExtUpdate(new EPPRgpExtRestore()));

			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainRestoreRequest: Response = [" + response
							   + "]\n\n");

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult =
					(EPPResult) response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());

				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				}

				else {
					System.out.println("Command Failed ");
				}

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainRestoreRequest");
	}

	// End EPPRgpDomainTst.domainRestoreRequest()


	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> with RGP restore request extension.
	 */
	public void domainRestoreReport() {
		printStart("domainRestoreReport");

		EPPResponse response;

		try {
			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);


 
              EPPRgpExtReport report = new EPPRgpExtReport();
              report.setPreData("Pre-delete whois data goes here. Both XML and free text are allowed");
              report.setPostData("Post-delete whois data goes here. Both XML and free text are allowed");
              report.setDeleteTime(new Date());
              report.setRestoreTime(new Date());

              report.setRestoreReason(new EPPRgpExtReportText("Registrant Error"));

              report.setStatement1(new EPPRgpExtReportText("This registrar has not" +
                  " restored the Registered Domain in order to " +
                  "assume the rights to use or sell the Registered" +
                  " Name for itself or for any third party"));

              report.setStatement2(new EPPRgpExtReportText("The information in this report " +
                  " is true to best of this registrar's knowledge, and this" +
                  "registrar acknowledges that intentionally supplying false" +
                  " information in this report shall " +
                  "constitute  an incurable material breach of the Registry-Registrar" +
                  " Agreement"));

              report.setOther("other stuff");

              EPPRgpExtRestore restore = new EPPRgpExtRestore(report);
          
              domain.addExtension(new EPPRgpExtUpdate(restore));


			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainRestoreReport: Response = [" + response
							   + "]\n\n");

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult =
					(EPPResult) response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());

				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				}

				else {
					System.out.println("Command Failed ");
				}

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainRestoreReport");
	}

	// End EPPRgpDomainTst.domainRestoreReport()
	
	
	/**
	 * Unit test of <code>EPPDomain.sendPoll</code>.
	 */
	public void domainPoll() {
		printStart("domainPoll");

		EPPResponse response = null;

		try {
			System.out.println("\ndomainPoll: Domain Poll");

			// req
			session.setTransId("AB-12345");

			session.setPollOp(EPPSession.OP_REQ);

			response = session.sendPoll();

			System.out.println("domainPoll: Response = [" + response + "]\n\n");

			// ack
			session.setPollOp(EPPSession.OP_ACK);

			session.setMsgID(response.getMsgQueue().getId());

			response = session.sendPoll();

			System.out.println("domainPoll: Response = [" + response + "]\n\n");
		}

		 catch (EPPCommandException ex) {
			handleException(ex);
		}

		printEnd("domainPoll");
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
			EPPResponse response = session.getResponse();

			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			}
			else {
				e.printStackTrace();

				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	}

	// End EPPRgpDomainTst.initSession()

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
			EPPResponse response = session.getResponse();

			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			}

			else // Other error
			 {
				e.printStackTrace();

				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("endSession");
	}

	// End EPPRgpDomainTst.endSession()

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

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
		}

		 catch (Exception e) {
			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		//System.out.println("out init");
		domain = new EPPDomain(session);
	}

	// End EPPRgpDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPRgpDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPRgpDomainTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPRgpDomainTst.class);

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

	// End EPPRgpDomainTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by
	 * a server error, "Server Error :&lt;Response XML&gt;" will be specified.
	 * If the exception was caused by a general algorithm error, "General
	 * Error :&lt;Exception Description&gt;" will be specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(Exception aException) {
		EPPResponse response = session.getResponse();

		aException.printStackTrace();

		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		}

		else {
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPRgpDomainTst.handleException(EPPCommandException)

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

			// Run test suite in multiple threads?
		}

		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread =
					new TestThread(
								   "EPPSessionTst Thread " + i,
								   EPPRgpDomainTst.suite());

				thread.start();
			}
		}

		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPRgpDomainTst.suite());
		}

		try {
			app.endApplication();
		}

		 catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPRgpDomainTst.main(String [])

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
						  + ".com");
	}

	/**
	 * Makes a unique IP address based off of the current time.
	 *
	 * @return Unique IP address <code>String</code>
	 */
	public String makeIP() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10));
	}

	/**
	 * Makes a unique host name for a domain using the current time.
	 *
	 * @param newDomainName DOCUMENT ME!
	 *
	 * @return Unique host name <code>String</code>
	 */
	public String makeHostName(String newDomainName) {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + "." + newDomainName);
	}

	/**
	 * Makes a unique contact name using the current time.
	 *
	 * @return Unique contact name <code>String</code>
	 */
	public String makeContactName() {
		long tm = System.currentTimeMillis();

		return new String("Con"
						  + String.valueOf(tm + rd.nextInt(5)).substring(7));
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

	// End EPPRgpDomainTst.testStart(String)

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

	// End EPPRgpDomainTst.testEnd(String)

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

	// End EPPRgpDomainTst.printMsg(String)

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
	} // End EPPRgpDomainTst.printError(String)

	/**
	 * Inspect the <code>EPPDomainInfoResp</code> and print out the 
	 * RGP status information contained in the response.
	 * 
	 * @param aResp Response to inspect
	 */
	private void printRgpStatuses(EPPDomainInfoResp aResp) {
		// Check for the RGP grace period statuses
		if (aResp.hasExtension(EPPRgpExtInfData.class)) {
			EPPRgpExtInfData theRgpInf = (EPPRgpExtInfData) aResp
					.getExtension(EPPRgpExtInfData.class).orElse(null);
			List rgpStatuses = theRgpInf.getStatuses();
			System.out.println("domainInfo: rgpStatuses.size = "
					+ rgpStatuses.size());
			for (int i = 0; i < rgpStatuses.size(); i++) {
				EPPRgpExtStatus rgpStatus = (EPPRgpExtStatus) rgpStatuses
						.get(i);

				if (rgpStatus.getStatus().equals(EPPRgpExtStatus.ADD_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.AUTO_RENEW_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.RENEW_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.TRANSFER_PERIOD)) {
					System.out.println("domainInfo: rgp grace period status "
							+ rgpStatus.getStatus());
					System.out
							.println("domainInfo: rgp grace period end date = "
									+ EPPUtil.decodeTimeInstant(rgpStatus
											.getMessage().substring(8)));
				}
				else if (rgpStatus.getStatus().equals(
						EPPRgpExtStatus.REDEMPTION_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.PENDING_RESTORE)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.PENDING_DELETE)) {
					System.out.println("domainInfo: rgp pending period status "
							+ rgpStatus.getStatus());

				}

			}
		}

	}

	
} // End class EPPRgpDomainTst
