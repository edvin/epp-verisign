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

//----------------------------------------------
//
// imports...
//
//----------------------------------------------

// JUNIT Imports
import junit.framework.*;

// Log4j imports
import org.apache.log4j.*;

// Java Core Imports
import java.util.Enumeration;
import java.util.Random;

// EPP Imports
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.host.*;
import com.verisign.epp.codec.namestoreext.*;

import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPHost;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the EPPSession class.  The unit test will initialize a
 * session with an EPP Server and end a session with an EPP Server.  The
 * configuration file used by the unit test defaults to epp.config, but can be
 * changed by passing the file path as the first command line argument.  The
 * unit test can be run in multiple threads by setting     the "threads"
 * system property.  For example, the unit test can     be run in 2 threads
 * with the configuration file ../../epp.config with     the following command:<br>
 * <br>
 * java com.verisign.epp.interfaces.EPPNamestoreExtHostTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * <br>
 * The test <code>EPPResponse</code> is duplicated     for
 * <code>EPPMessage</code> that do not contain a specialized
 * <code>EPPResponse</code>     to provide an example of a full
 * <code>EPPMessage</code> transaction.  All of the com.verisign.epp.codec.gen
 * package <code>EPPMessage</code> classes are associated     with
 * <code>EPPResponse</code>.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPNamestoreExtHostTst extends TestCase {
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
						 EPPNamestoreExtHostTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Host associated with test */
	private EPPHost host = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Current test iteration */
	private int iteration = 0;

	/** Need this for testing */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPNamestoreExtHostTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPNamestoreExtHostTst(String name) {
		super(name);
	}

	// End EPPNamestoreExtHostTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPNamestoreExtHostTst TestCase</code>.
	 * Each     sub-test will be invoked in order to satisfy testing the
	 * EPPHost interface.
	 */
	public void testHost() {
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

			hostCheck();
			hostInfo();
			hostCreate();
			hostDelete();
			hostUpdate();

			printEnd("Test Suite");
		}
	}

	// End EPPNamestoreExtHostTst.testHost()

	/**
	 * Unit test of <code>EPPHost.sendCheck</code>.
	 */
	private void hostCheck() {
		printStart("hostCheck");

		EPPHostCheckResp response;

		try {
			// Check single host name
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("hostCheck: Check single host name (ns1.example.com)");
			host.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();
			host.addHostName(this.makeHostName(myDomainName));

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendCheck();

			// Output all of the response attributes
			System.out.println("\nhostCheck: Response = [" + response + "]");

			// Correct number of results?
			Assert.assertEquals(1, response.getCheckResults().size());

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPHostCheckResult currResult =
					(EPPHostCheckResult) response.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("hostCheck: Host "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("hostCheck: Host "
									   + currResult.getName() + " is unavailable");
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			// Check multiple host names
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("hostCheck: Check multiple host names (ns1.example.com, ns2.example.com, ns3.example.com)");

			host.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			for (int i = 0; i <= 15; i++) {
				host.addHostName(this.makeHostName(myDomainName));
			}

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendCheck();

			// Output all of the response attributes
			System.out.println("\nhostCheck: Response = [" + response + "]");

			// Get ping result information
			Enumeration results = response.getResults().elements();

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPHostCheckResult currResult =
					(EPPHostCheckResult) response.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("hostCheck: Host "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("hostCheck: Host "
									   + currResult.getName() + " is unavailable");
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("hostCheck");
	}

	// End EPPNamestoreExtHostTst.hostCheck()

	/**
	 * Unit test of <code>EPPHost.sendInfo</code>.
	 */
	public void hostInfo() {
		printStart("hostInfo");

		EPPHostInfoResp response;

		try {
			System.out.println("\nhostInfo: Host info for ns1.example.com");
			host.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			host.addHostName(this.makeHostName(myDomainName));

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendInfo();

			//-- Output all of the response attributes
			System.out.println("hostInfo: Response = [" + response + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("hostInfo: name = " + response.getName());
			System.out.println("hostInfo: client id = "
							   + response.getClientId());
			System.out.println("hostInfo: created by = "
							   + response.getCreatedBy());
			System.out.println("hostInfo: create date = "
							   + response.getCreatedDate());

			//-- Output optional response attributes using accessors
			// Addresses
			if (response.getAddresses() != null) {
				// For each Address
				for (int i = 0; i < response.getAddresses().size(); i++) {
					EPPHostAddress currAddress =
						(EPPHostAddress) response.getAddresses().elementAt(i);

					System.out.print("hostInfo: address " + (i + 1));

					// Address Name
					System.out.print(" name = " + currAddress.getName());

					// IPV4 Address?
					if (currAddress.getType() == EPPHostAddress.IPV4) {
						System.out.println(", type = IPV4");
					}

					// IPV6 Address?
					else if (currAddress.getType() == EPPHostAddress.IPV4) {
						System.out.println(", type = IPV6");
					}
				}
			}

			// Last Updated By
			if (response.getLastUpdatedBy() != null) {
				System.out.println("hostInfo: last updated by = "
								   + response.getLastUpdatedBy());
			}

			// Last Updated Date
			if (response.getLastUpdatedDate() != null) {
				System.out.println("hostInfo: last updated date = "
								   + response.getLastUpdatedDate());
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("hostInfo");
	}

	// End EPPNamestoreExtHostTst.hostInfo()

	/**
	 * Unit test of <code>EPPHost.sendCreate</code>.
	 */
	public void hostCreate() {
		printStart("hostCreate");

		EPPResponse response;

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("hostCreate: Create ns1.example.com with no optional attributes");

			host.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();
			host.addHostName(this.makeHostName(myDomainName));

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendCreate();

			//-- Output all of the response attributes
			System.out.println("hostCreate: Response = [" + response + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("hostCreate: Create ns1.example.com with all optional attributes");

			host.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();
			host.addHostName(this.makeHostName(myDomainName));
			host.addIPV4Address(this.makeIP());
			host.addIPV6Address("1080:0:0:0:8:800:200C:417A");
			host.addIPV6Address("::FFFF:129.144.52.38");

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendCreate();

			//-- Output all of the response attributes
			System.out.println("hostCreate: Response = [" + response + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("hostCreate");
	}

	// End EPPNamestoreExtHostTst.hostCreate()

	/**
	 * Unit test of <code>EPPHost.sendDelete</code>.
	 */
	public void hostDelete() {
		printStart("hostDelete");

		EPPResponse response;

		try {
			System.out.println("\nhostDelete: Host delete for ns1.example.com");
			host.setTransId("ABC-12345-XYZ");
			host.addHostName(this.makeHostName(this.makeDomainName()));

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = host.sendDelete();

			//-- Output all of the response attributes
			System.out.println("hostDelete: Response = [" + response + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("hostDelete");
	}

	// End EPPNamestoreExtHostTst.hostDelete()

	/**
	 * Unit test of <code>EPPHost.sendUpdate</code>.
	 */
	public void hostUpdate() {
		printStart("hostUpdate");

		EPPResponse response;

		try {
			System.out.println("\nhostUpdate: Host update for ns1.example.com");
			host.setTransId("ABC-12345-XYZ");
			host.addHostName(this.makeHostName(this.makeDomainName()));

			// Add attributes
			host.addIPV4Address(this.makeIP());

			// Remove attributes
			host.removeIPV6Address("1080:0:0:0:8:800:200C:417A");

			host.addStatus(
						   EPPHost.STAT_OK, "Hello_World", EPPHost.DEFAULT_LANG);

			host.removeStatus(
							  EPPHost.STAT_OK, "Hello World with spaces",
							  EPPHost.DEFAULT_LANG);

			// Set destination registry
			host.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute update
			response = host.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("hostUpdate: Response = [" + response + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("hostUpdate");
	}

	// End EPPNamestoreExtHostTst.hostUpdate()

	/**
	 * Unit test of <code>EPPSession.initSession</code>.  The session attribute
	 * is initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession() {
		printStart("initSession");

		// Set attributes for initSession
		session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
		session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
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
			else // Other error
			 {
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	}

	// End EPPNamestoreExtHostTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>.  The session with the
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

	// End EPPNamestoreExtHostTst.endSession()

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

		}
		 catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		host = new EPPHost(session);
	}

	// End EPPNamestoreExtHostTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPNamestoreExtHostTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPNamestoreExtHostTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPNamestoreExtHostTst.class);

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

	// End EPPNamestoreExtHostTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception.  If the exception     was
	 * caused by a server error, "Server Error : &lt;Response XML&gt;" will be
	 * specified.     If the exception was caused by a general     algorithm
	 * error, "General Error : &lt;Exception Description&gt;" will     be
	 * specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(EPPCommandException aException) {
		EPPResponse response = session.getResponse();

		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		}
		else // Other error
		 {
			aException.printStackTrace();
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPNamestoreExtHostTst.handleException(EPPCommandException)

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
								   EPPNamestoreExtHostTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPNamestoreExtHostTst.suite());
		}

		try {
			app.endApplication();
		}
		 catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPNamestoreExtHostTst.main(String [])

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
	 * This method tries to generate a unique IP address
	 *
	 * @return DOCUMENT ME!
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
	 * This method tries to generate a unique Host Name for a given Domain Name
	 *
	 * @param newDomainName DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeHostName(String newDomainName) {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + "." + newDomainName);
	}

	/**
	 * This method tries to generate a unique String as contact Name
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeHostName() {
		long tm = System.currentTimeMillis();

		return new String("Con"
						  + String.valueOf(tm + rd.nextInt(5)).substring(7));
	}

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current     thread is a <code>TestThread</code>.
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

	// End EPPNamestoreExtHostTst.printStart(String)

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

	// End EPPNamestoreExtHostTst.testEnd(String)

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

	// End EPPNamestoreExtHostTst.printMsg(String)

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

	// End EPPNamestoreExtHostTst.printError(String)
}


// End class EPPNamestoreExtHostTst
