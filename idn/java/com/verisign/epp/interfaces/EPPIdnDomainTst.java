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

// JUNIT Imports
import junit.framework.*;

// Log4j imports
import org.apache.log4j.*;

// EPP Imports
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPDomain;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;
import com.verisign.epp.codec.domain.*;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Is a unit test of the using the IDN Lang Extension classes with the <code>EPPDomain</code> class.
 * The unit test will initialize a session with an EPP Server, will invoke <code>EPPDomain</code>
 * operations with IDN Lang Extensions, and will end a session with an EPP
 * Server. The configuration file used by the unit test defaults to epp.config, but can be changed by passing the file
 * path as the first command line argument. The unit test can be run in multiple threads by setting the "threads"
 * system property. For example, the unit test can be run in 2 threads with the configuration file ../../epp.config
 * with the following command: <br><br>java com.verisign.epp.interfaces.EPPPersRegTst -Dthreads=2 ../../epp.config
 * <br><br>The unit test is dependent on the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */

public class EPPIdnDomainTst extends TestCase {

	/**
	 * Allocates an <code>EPPIdnDomainTst</code> with a logical name. The constructor will initialize the base class
	 * <code>TestCase</code> with the logical name.
	 * 
	 * @param name Logical name of the test
	 */

	public EPPIdnDomainTst(String name) {

		super(name);

	} // End EPPIdnDomainTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPIdnDomainTst TestCase</code>. Each sub-test will be invoked in
	 * order to satisfy testing the EPPDomain interface.
	 */

	public void testIdnLang() {

		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {

			numIterations = Integer.parseInt(iterationsStr);

		}

		for (iteration = 0;(numIterations == 0) || (iteration < numIterations); iteration++) {

			printStart("Test Suite");

			idnLangCreate();

			printEnd("Test Suite");

		}

	} // End EPPIdnDomainTst.testIdnLang()

	

	/**
	 * Unit test of using the IDN Lang Extension with <code>EPPDomain</code> Create .
	 */

	public void idnLangCreate() {

		printStart("idnLangCreate");

		// Try Successful Create

		try {

			EPPDomainCreateResp domainResponse;

			System.out.println("idnLangCreate: Domain Create");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setAuthString("ClientX");

			// Set consent identifier

			domain.addExtension(new EPPIdnLangTag("en"));

			domainResponse = domain.sendCreate();

			//-- Output all of the domainResponse attributes

			System.out.println("idnLangCreate: Response = [" + domainResponse + "]\n\n");

			//-- Output extension attribute(s)

			if (domainResponse.hasExtension(EPPIdnLangTag.class)) {

//				EPPIdnLangTag ext = domainResponse.getExtension(EPPIdnLangTag.class);

//				System.out.println("idnLangCreate: Language Code = " + ext.getLang());

			}

			else {

				System.out.println("idnLangCreate: Response has no extension");

			}

		}

		catch (EPPCommandException e) {

			handleException(e);

		}

		printEnd("idnLangCreate");

	} // End EPPIdnDomainTst.idnLangCreate()

	
	
	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute is initialized with the attributes
	 * defined in the EPP sample files.
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

	} // End EPPIdnDomainTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the EPP Server will be terminated.
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

	} // End EPPIdnDomainTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to "theRegistrar".
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

	} // End EPPIdnDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */

	protected void tearDown() {

		endSession();

	} // End EPPIdnDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated with <code>EPPIdnDomainTst</code>.
	 */

	public static Test suite() {

		TestSuite suite = new TestSuite(EPPIdnDomainTst.class);

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

	} // End EPPIdnDomainTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server generated error or a general
	 * exception. If the exception was caused by a server error, "Server Error :<Response XML>" will be specified. If
	 * the exception was caused by a general algorithm error, "General Error :<Exception Description>" will be
	 * specified.
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

	} // End EPPIdnDomainTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run
	 * <li>validate Turn XML validation on (<code>true</code>) or off (<code>false</code>). If validate is not
	 * specified, validation will be off.
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

				TestThread thread = new TestThread("EPPSessionTst Thread " + i, EPPIdnDomainTst.suite());

				thread.start();

			}

		}

		else // Single threaded mode.

			junit.textui.TestRunner.run(EPPIdnDomainTst.suite());

		try {

			app.endApplication();

		}

		catch (EPPCommandException e) {

			e.printStackTrace();

			Assert.fail("Error ending the EPP Application: " + e);

		}

	} // End EPPIdnDomainTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name and Name Server
	 */

	public String makeDomainName() {

		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread() + String.valueOf(tm + rd.nextInt(12)).substring(10) + ".second.name");

	}

	/**
	 * This method tries to generate a unique String as Domain Name and Name Server
	 */

	public String makeEmail() {

		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread() + String.valueOf(tm + rd.nextInt(12)).substring(10) + "@second.name");

	}

	/**
	 * Print the start of a test with the <code>Thread</code> name if the current thread is a <code>TestThread</code>.
	 * 
	 * @param Logical name for the test
	 */

	private void printStart(String aTest) {

		if (Thread.currentThread() instanceof TestThread) {

			System.out.print(Thread.currentThread().getName() + ", iteration " + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aTest + " Start");

		}

		System.out.println("Start of " + aTest);

		System.out.println("****************************************************************\n");

	} // End EPPIdnDomainTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current thread is a <code>TestThread</code>.
	 * 
	 * @param Logical name for the test
	 */

	private void printEnd(String aTest) {

		System.out.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {

			System.out.print(Thread.currentThread().getName() + ", iteration " + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aTest + " End");

		}

		System.out.println("End of " + aTest);

		System.out.println("\n");

	} // End EPPIdnDomainTst.testEnd(String)

	/**
	 * Print message
	 * 
	 * @param aMsg message to print
	 */

	private void printMsg(String aMsg) {

		if (Thread.currentThread() instanceof TestThread) {

			System.out.println(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

			cat.info(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

		}

		else {

			System.out.println(aMsg);

			cat.info(aMsg);

		}

	} // End EPPIdnDomainTst.printMsg(String)

	/**
	 * Print error message
	 * 
	 * @param aMsg errpr message to print
	 */

	private void printError(String aMsg) {

		if (Thread.currentThread() instanceof TestThread) {

			System.err.println(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

			cat.error(Thread.currentThread().getName() + ", iteration " + iteration + ": " + aMsg);

		}

		else {

			System.err.println(aMsg);

			cat.error(aMsg);

		}

	} // End EPPIdnDomainTst.printError(String)

	/**
	 * EPP Domain associated with test
	 */

	private EPPDomain domain = null;

	
	/**
	 * EPP Session associated with test
	 */

	private EPPSession session = null;

	/**
	 * Connection to the EPP Server.
	 */

	private EPPClientCon connection = null;

	/**
	 * Current test iteration
	 */

	private int iteration = 0;

	/**
	 * Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)
	 */

	private static EPPApplicationSingle app = EPPApplicationSingle.getInstance();

	/**
	 * Name of configuration file to use for test (default = epp.config).
	 */

	private static String configFileName = "epp.config";

	/**
	 * Random instance for the generation of unique objects (hosts, IP addresses, etc.).
	 */

	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Logging category
	 */

	private static final Logger cat =
		Logger.getLogger(EPPIdnDomainTst.class.getName(), EPPCatFactory.getInstance().getFactory());

} // End class EPPIdnDomainTst
