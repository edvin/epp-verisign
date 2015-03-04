/***********************************************************

 The information in this document is proprietary
 to VeriSign and the VeriSign Registry Business.
 It may not be used, reproduced or disclosed without
 the written approval of the General Manager of
 VeriSign Global Registry Services.

 PRIVILEDGED AND CONFIDENTIAL
 VERISIGN PROPRIETARY INFORMATION
 REGISTRY SENSITIVE INFORMATION

 Copyright (c) 2002 VeriSign, Inc.  All rights reserved.

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
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.whois.EPPWhoisInf;
import com.verisign.epp.codec.whois.EPPWhoisInfData;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the using the Whois Extension classes with the
 * <code>EPPDomain</code> class. The unit test will initialize a session with
 * an EPP Server, will invoke <code>EPPDomain</code> operations with Whois
 * Extensions, and will end a session with an EPP Server. The configuration file
 * used by the unit test defaults to epp.config, but can be changed by passing
 * the file path as the first command line argument. The unit test can be run in
 * multiple threads by setting the "threads" system property. For example, the
 * unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPWhoisDomainTst -Dthreads=2
 * ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */
public class EPPWhoisDomainTst extends TestCase {

	/**
	 * Allocates an <code>EPPWhoisDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public EPPWhoisDomainTst(String name) {
		super(name);
	} // End EPPWhoisDomainTst(String)

	/**
	 * JUNIT test method to implement the
	 * <code>EPPWhoisDomainTst TestCase</code>. Each sub-test will be invoked
	 * in order to satisfy testing the EPPDomain interface.
	 */
	public void testWhois() {

		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {

			numIterations = Integer.parseInt(iterationsStr);

		}

		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) {

			printStart("Test Suite");

			whoisInfo();

			printEnd("Test Suite");

		}

	} // End EPPWhoisDomainTst.testWhois()

	/**
	 * Unit test of using the <code>EPPWhoisInf</code> Extension with
	 * <code>EPPDomain</code> info command and to validate the existence and
	 * attributes of the <code>EPPWhoisInfoData</code> extension in the info
	 * response.
	 */
	public void whoisInfo() {

		printStart("whoisInfo");

		// Try Successful Info

		try {

			EPPDomainInfoResp domainResponse;

			System.out.println("whoisInfo: Domain Info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Add extension
			domain.addExtension(new EPPWhoisInf(true));

			domainResponse = domain.sendInfo();

			// -- Output all of the domain info response attributes
			System.out.println("whoisInfo: Response = [" + domainResponse
					+ "]\n\n");

			// -- Output extension attribute(s)
			if (domainResponse.hasExtension(EPPWhoisInfData.class)) {

				EPPWhoisInfData ext = (EPPWhoisInfData) domainResponse
						.getExtension(EPPWhoisInfData.class);

				System.out.println("whoisInfo: Registrar = "
						+ ext.getRegistrar());
				System.out.println("whoisInfo: Whois Server = "
						+ ext.getWhoisServer());
				System.out.println("whoisInfo: URL = " + ext.getURL());
				System.out.println("whoisInfo: IRIS Server = "
						+ ext.getIrisServer());
			}
			else {
				Assert.fail("whoisInfo: EPPWhoisInfoData extension NOT found");
			}

		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("whoisInfo");

	} // End EPPWhoisDomainTst.whoisInfo()

	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session
	 * attribute is initialized with the attributes defined in the EPP sample
	 * files.
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
	} // End EPPWhoisDomainTst.initSession()

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
	} // End EPPWhoisDomainTst.endSession()

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
					Assert
							.fail("Exception instantiating EPP.SessionClass value "
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
		domain = new EPPDomain(session);

	} // End EPPWhoisDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	} // End EPPWhoisDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPWhoisDomainTst</code>.
	 */
	public static Test suite() {

		TestSuite suite = new TestSuite(EPPWhoisDomainTst.class);

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

	} // End EPPWhoisDomainTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a
	 * server generated error or a general exception. If the exception was
	 * caused by a server error, "Server Error :<Response XML>" will be
	 * specified. If the exception was caused by a general algorithm error,
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

	} // End EPPWhoisDomainTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (<code>false</code>).
	 * If validate is not specified, validation will be off.
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
						EPPWhoisDomainTst.suite());
				thread.start();
			}

		}
		else
			// Single threaded mode.
			junit.textui.TestRunner.run(EPPWhoisDomainTst.suite());
		try {
			app.endApplication();
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}

	} // End EPPWhoisDomainTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();
		return new String(Thread.currentThread()
				+ String.valueOf("EPPWhoisDomainTst" + tm + rd.nextInt(12)).substring(10)
				+ ".com");
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
	} // End EPPWhoisDomainTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
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
	} // End EPPWhoisDomainTst.testEnd(String)

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
	} // End EPPWhoisDomainTst.printMsg(String)

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
	} // End EPPWhoisDomainTst.printError(String)

	/**
	 * EPP Domain associated with test
	 */
	private EPPDomain domain = null;

	/**
	 * EPP Session associated with test
	 */
	private EPPSession session = null;

	/**
	 * Current test iteration
	 */
	private int iteration = 0;

	/**
	 * Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)
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
	private static final Logger cat = Logger.getLogger(EPPWhoisDomainTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

} // End class EPPWhoisDomainTst
