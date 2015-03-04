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
package com.verisign.epp.namestore.interfaces;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports

import java.util.Random;

import junit.extensions.TestSetup;
import junit.framework.*;

// Log4j imports
import org.apache.log4j.*;

// EPP Imports
import com.verisign.epp.codec.host.EPPHostAddress;
import com.verisign.epp.codec.host.EPPHostCheckResp;
import com.verisign.epp.codec.host.EPPHostCheckResult;
import com.verisign.epp.codec.host.EPPHostInfoResp;
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPHost;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.InvalidateSessionException;
import com.verisign.epp.util.TestThread;
import com.verisign.epp.util.TestUtil;
import com.verisign.epp.pool.*;

/**
 * Test of the use of the <code>NSHost</code> interface.  This
 * test utilizes the EPP session pool and exercises all of the 
 * operations defined in <code>NSHost</code> and the base
 * class <code>EPPHost</code>.  
 * 
 * @see com.verisign.epp.namestore.interfaces.NSHost
 * @see com.verisign.epp.interfaces.EPPHost
 */
public class NSHostTst extends TestCase {

	/**
	 * Handle to the Singleton EPP Application instance
	 * (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat = Logger.getLogger(NSHostTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** EPP Session pool associated with test */
	private static EPPSessionPool sessionPool = null;

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>NSHostTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public NSHostTst(String name) {
		super(name);
	}

	// End NSHostTst(String)



	/**
	 * Unit test of <code>NSHost.sendCreate</code> command.
	 */
	public void testHostCreate() {
		printStart("testHostCreate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;

		try {
			theSession = this.borrowSession();
			NSHost theHost = new NSHost(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theHostName = this.makeInternalHost();

				System.out.println("hostCreate: Create internal " + theHostName);

				theHost.setTransId("ABC-12345-XYZ");

				theHost.addHostName(theHostName);
				theHost.setSubProductID(NSSubProduct.COM);

				theHost.addIPV4Address(this.makeIP());
				theHost.addIPV6Address("1080:0:0:0:8:800:200C:417A");
				theHost.addIPV6Address("::FFFF:129.144.52.38");

				theResponse = theHost.sendCreate();

				//-- Output all of the response attributes
				System.out.println("hostCreate: Response = [" + theResponse
						+ "]\n\n");

			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theHost.setTransId("ABC-12345-XYZ");

				String theHostName = this.makeExternalHost();

				System.out.println("hostCreate: Create " + theHostName
						+ " with all optional attributes");

				theHost.addHostName(theHostName);
				theHost.setSubProductID(NSSubProduct.COM);

				theResponse = theHost.sendCreate();

				//-- Output all of the response attributes
				System.out.println("hostCreate: Response = [" + theResponse
						+ "]\n\n");
			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testHostCreate");
	}

	/**
	 * Unit test of <code>NSHost.sendHostCheck</code> command.
	 */
	public void testHostCheck() {
		printStart("testHostCheck");

		EPPSession theSession = null;
		EPPHostCheckResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSHost theHost = new NSHost(theSession);

			try {

				System.out
						.println("\n----------------------------------------------------------------");

				String theHostName = this.makeInternalHost();
				System.out.println("hostCheck: Check single host name ("
						+ theHostName + ")");

				theHost.setTransId("ABC-12345-XYZ");

				theHost.addHostName(theHostName);
				theHost.setSubProductID(NSSubProduct.COM);

				theResponse = theHost.sendCheck();

				System.out.println("Response Type = " + theResponse.getType());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getServerTransId());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getClientTransId());

				// Output all of the response attributes
				System.out.println("\nhostCheck: Response = [" + theResponse
						+ "]");

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPHostCheckResult currResult = (EPPHostCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("hostCheck: Host "
								+ currResult.getName() + " is available");
					}
					else {
						System.out.println("hostCheck: Host "
								+ currResult.getName() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (Exception e) {
				TestUtil.handleException(theSession, e);
			}

			try {
				// Check multiple host names
				System.out
						.println("\n----------------------------------------------------------------");
				System.out
						.println("hostCheck: Check multiple host names (ns1.example.com, ns2.example.com, ns3.example.com)");
				theHost.setTransId("ABC-12345-XYZ");

				/**
				 * Add ns(1-3).example.com
				 */
				theHost.addHostName("ns1.example.com");
				theHost.addHostName("ns2.example.com");
				theHost.addHostName("ns3.example.com");
				theHost.setSubProductID(NSSubProduct.COM);

				for (int i = 0; i <= 10; i++) {
					theHost.addHostName(this.makeInternalHost());
				}

				theResponse = theHost.sendCheck();

				// Output all of the response attributes
				System.out.println("\nhostCheck: Response = [" + theResponse
						+ "]");
				System.out.println("Client Transaction Id = "
						+ theResponse.getTransId().getClientTransId());
				System.out.println("Server Transaction Id = "
						+ theResponse.getTransId().getServerTransId());

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPHostCheckResult currResult = (EPPHostCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("hostCheck: Host "
								+ currResult.getName() + " is available");
					}
					else {
						System.out.println("hostCheck: Host "
								+ currResult.getName() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (EPPCommandException e) {
				TestUtil.handleException(theSession, e);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testHostCheck");
	}

	/**
	 * Unit test of <code>NSHost.sendHostInfo</code> command.
	 */
	public void testHostInfo() {
		printStart("testHostInfo");

		EPPSession theSession = null;
		EPPHostInfoResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSHost theHost = new NSHost(theSession);

			try {
				System.out.println("\nhostInfo: Host info");

				theHost.setTransId("ABC-12345-XYZ");

				theHost.addHostName(this.makeInternalHost());
				theHost.setSubProductID(NSSubProduct.COM);

				theResponse = theHost.sendInfo();

				//-- Output all of the response attributes
				System.out.println("hostInfo: Response = [" + theResponse + "]\n\n");

				//-- Output required response attributes using accessors
				System.out.println("hostInfo: name = " + theResponse.getName());
				System.out.println("hostInfo: client id = "
								   + theResponse.getClientId());
				System.out.println("hostInfo: created by = "
								   + theResponse.getCreatedBy());
				System.out.println("hostInfo: create date = "
								   + theResponse.getCreatedDate());

				//-- Output optional response attributes using accessors
				// Addresses
				if (theResponse.getAddresses() != null) {
					// For each Address
					for (int i = 0; i < theResponse.getAddresses().size(); i++) {
						EPPHostAddress currAddress =
							(EPPHostAddress) theResponse.getAddresses().elementAt(i);

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
				if (theResponse.getLastUpdatedBy() != null) {
					System.out.println("hostInfo: last updated by = "
									   + theResponse.getLastUpdatedBy());
				}

				// Last Updated Date
				if (theResponse.getLastUpdatedDate() != null) {
					System.out.println("hostInfo: last updated date = "
									   + theResponse.getLastUpdatedDate());
				}

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testHostInfo");
	}

	/**
	 * Unit test of <code>NSHost.sendDelete</code> command.
	 */
	public void testHostDelete() {
		printStart("testHostDelete");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSHost theHost = new NSHost(theSession);

			try {
				System.out.println("\nhostDelete: Host delete");

				theHost.setTransId("ABC-12345-XYZ");

				theHost.addHostName(this.makeInternalHost());
				theHost.setSubProductID(NSSubProduct.COM);

				theResponse = theHost.sendDelete();

				//-- Output all of the response attributes
				System.out.println("hostDelete: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testHostDelete");
	}


	/**
	 * Unit test of <code>NSHost.sendUpdate</code> command.
	 */
	public void testHostUpdate() {
		printStart("testHostUpdate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSHost theHost = new NSHost(theSession);

			try {

				theHost.setTransId("ABC-12345-XYZ");

				String theHostName = this.makeInternalHost();

				System.out.println("\nhostUpdate: Host " + theHostName
						+ " update");

				theHost.addHostName(theHostName);
				theHost.setSubProductID(NSSubProduct.COM);

				// Add attributes
				theHost.addIPV4Address(this.makeIP());

				// Remove attributes
				theHost.removeIPV6Address("1080:0:0:0:8:800:200C:417A");

				theHost.addStatus(
							   EPPHost.STAT_OK, "Hello_World", EPPHost.DEFAULT_LANG);

				theHost.removeStatus(
								  EPPHost.STAT_OK, "Hello World with spaces",
								  EPPHost.DEFAULT_LANG);

				// Execute update
				theResponse = theHost.sendUpdate();

				//-- Output all of the response attributes
				System.out.println("hostUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testHostUpdate");
	}
	
	/**
	 * Unit test of <code>EPPSession.endSession</code>. One session in the
	 * session pool wil be ended.
	 */
	public void testEndSession() {
		printStart("testEndSession");

		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			sessionPool.invalidateObject(theSession);
			theSession = null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("testEndSession(): Exception invalidating session: "
					+ ex);
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testEndSession");
	}

	// End NSHostTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {

	}

	// End NSHostTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End NSHostTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>NSHostTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		return new NSHostTstSetup(new TestSuite(NSHostTst.class));
	}

	// End NSHostTst.suite()
	
	/**
	 * Setup framework from running NSHostTst tests.
	 */
	private static class NSHostTstSetup extends TestSetup {
		
		/**
		 * Creates setup instance for passed in tests.
		 * 
		 * @param aTest Tests to execute
		 */
		public NSHostTstSetup(Test aTest) {
			super(aTest);
		}

		/**
		 * Setup framework for running NSHostTst tests.
		 */
		protected void setUp() throws Exception {
			super.setUp();
			
			String theConfigFileName = System.getProperty("EPP.ConfigFile");
			if (theConfigFileName != null) configFileName = theConfigFileName;

			try {
				app.initialize(configFileName);
			}
			catch (EPPCommandException e) {
				e.printStackTrace();
				Assert.fail("Error initializing the EPP Application: " + e);
			}

			// Initialize the session pool
			try {
				sessionPool = EPPSessionPool.getInstance();
				sessionPool.init();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail("Error initializing the session pool: " + ex);
			}

		}

		/**
		 * Tear down framework from running NSHostTst tests.
		 */
		protected void tearDown() throws Exception {
			super.tearDown();
			EPPSessionPool.getInstance().close();
		}
	}

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * 
	 * <ul>
	 * <li>iterations Number of unit test iterations to run</li>
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.</li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty("threads");

		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread = new TestThread("NSHostTst Thread " + i,
						NSHostTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(NSHostTst.suite());
		}

		try {
			app.endApplication();
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End NSHostTst.main(String [])


	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat
					.info(Thread.currentThread().getName() + ": " + aTest
							+ " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	}

	// End NSHostTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printEnd(String aTest) {
		System.out
				.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat.info(Thread.currentThread().getName() + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	// End NSHostTst.testEnd(String)

	/**
	 * Utility method to borrow a session from the session pool.
	 * All exceptions will result in the test failing.  This method
	 * should only be used for positive session pool tests.
	 * 
	 * @return Session from the session pool
	 */
	private EPPSession borrowSession() {
		EPPSession theSession = null;
		try {
			theSession = sessionPool.borrowObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("borrowSession(): Exception borrowing session: " + ex);
		}

		return theSession;
	}

	/**
	 * Utility method to return a session to the session pool.  
	 * This should be placed in a finally block.  All exceptions will
	 * result in the test failing.
	 * 
	 * @param aSession Session to return to the pool
	 */
	private void returnSession(EPPSession aSession) {
		try {
			if (aSession != null) sessionPool.returnObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception returning session: " + ex);
		}
	}

	/**
	 * Utility method to invalidate a session in the session pool.  
	 * This should be placed in an exception block.  
	 * 
	 * @param aSession Session to invalidate in the pool
	 */
	private void invalidateSession(EPPSession aSession) {
		try {
			if (aSession != null) sessionPool.invalidateObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("invalidateSession(): Exception invalidating session: " + ex);
		}
	}
	
	

	/**
	 * Handle a response by printing out the result details.
	 * 
	 * @param aResponse the response to handle
	 */
	private void handleResponse(EPPResponse aResponse) {

		for (int i = 0; i < aResponse.getResults().size(); i++) {
			EPPResult theResult = (EPPResult) aResponse.getResults().elementAt(
					i);

			System.out.println("Result Code    : " + theResult.getCode());
			System.out.println("Result Message : " + theResult.getMessage());
			System.out.println("Result Lang    : " + theResult.getLang());

			if (theResult.isSuccess()) {
				System.out.println("Command Passed ");
			}
			else {
				System.out.println("Command Failed ");
			}

			if (theResult.getAllValues() != null) {
				for (int k = 0; k < theResult.getAllValues().size(); k++) {
					System.out.println("Result Values  : "
							+ theResult.getAllValues().elementAt(k));
				}
			}
		}
	} // End handleResponse(EPPResponse)

	/**
	 * This method generates a unique domain name.
	 *
	 * @return Unique domain name
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
						  + String.valueOf(tm + rd.nextInt(12)).substring(10)
						  + ".com");
	}

	/**
	 * This method generates a unique IPV4 address.
	 *
	 * @return Unique IPV4 address
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
	 * This method generates a unique internal Host Name for a given Domain Name
	 *
	 * @param newDomainName Domain name to based host name on
	 *
	 * @return Unique host name
	 */
	public String makeInternalHost(String newDomainName) {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + "." + newDomainName);
	}

	/**
	 * This method generates a unique internal Host Name.
	 *
	 * @return Unique internal host name
	 */
	public String makeInternalHost() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + "." + this.makeDomainName());
	}

	/**
	 * This method generates a unique external Host Name (i.e. BIZ).
	 *
	 * @return Unique external host name
	 */
	public String makeExternalHost() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + ".sample.biz");
	}

} // End class NSHostTst
