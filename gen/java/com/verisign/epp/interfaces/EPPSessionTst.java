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
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the EPPSession class.  The unit test will initialize a
 * session with an EPP Server and end a session with an EPP Server.  The
 * configuration file used by the unit test defaults to epp.config, but can be
 * changed by passing the file path as the first command line argument.  The
 * unit test can be run in multiple threads by setting the "threads" system
 * property.  For example, the unit test can be run in 2 threads with the
 * configuration file ../../epp.config with the following command:<br>
 * <br>
 * java com.verisign.epp.interfaces.EPPSessionTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * NOTE: The test will only work if there is at least one EPP Command Mapping
 * configured (i.e. Domain).  Without one EPP Command Mapping, the XML Schema
 * validation will fail when the client parses the greeting message from the
 * EPP Server Stub. <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 */
public class EPPSessionTst extends TestCase {
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
						 EPPSessionTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Session associated with test */
	private EPPSession session = null;

	/**
	 * Allocates an <code>EPPSessionTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPSessionTst(String name) {
		super(name);
	}

	// End EPPSessionTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPSessionTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the
	 * initialization and ending of an EPP Session with an EPP Server.
	 */
	public void testSession() {
		initSession();
		doHello();
		doPoll();
		doAsyncPoll();
		endSession();
	}

	/**
	 * Unit test of <code>EPPSession.initSession</code>.  The session attribute
	 * is initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession() {
		printStart("initSession");

		// Get client id and password from configuration if defined
		String theClientId = Environment.getProperty("EPP.Test.clientId", "ClientX");
		String thePassword = Environment.getProperty("EPP.Test.password", "foo-BAR2");
			
		// Set attributes for initSession
		session.setClientID(theClientId);
		session.setPassword(thePassword);
		session.setTransId("ABC-12345-XYZ");
		session.setVersion("1.0");
		session.setLang("en");

		// Initialize the session
		try {
			session.initSession();
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("initSession");
	}

	// End EPPSessionTst.initSession()

	/**
	 * Unit test of <code>EPPSession.sendHello</code> command.  The session
	 * attribute is initialized with the attributes defined in the EPP sample
	 * files.
	 */
	private void doHello() {
		printStart("Session Hello");

		try {
			session.hello();
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("Session Hello");
	}

	/**
	 * Unit test of <code>EPPSession.sendHello</code> command.
	 */
	private void doPoll() {
		printStart("doPoll");

		EPPResponse response = null;
		Long	    numMsgs = null;

		try {
			session.setTransId("ABC-12345-XYZ");
			session.setPollOp(EPPSession.OP_REQ);
			response = session.sendPoll();

			//-- Output all of the response attributes
			System.out.println("doPoll: Response = [" + response + "]\n\n");

			numMsgs = response.getMsgQueueCount();

			if (numMsgs != null) {
				System.out.println("doPoll: # messages = " + numMsgs);
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			session.setTransId("ABC-12345-XYZ");
			session.setPollOp(EPPSession.OP_ACK);
			session.setMsgID("1234");
			response = session.sendPoll();

			//-- Output all of the response attributes
			System.out.println("doPoll: Response = [" + response + "]\n\n");
		}
		 catch (EPPCommandException e) {
			// Expect error
			System.out.println("doPoll: Error Response = [" + e.getResponse()
							   + "]\n\n");
		}

		printEnd("doPoll");
	}

	/**
	 * Unit test processing poll responses asynchronous from the commands.  
	 * This is a test of the use of pipelining.
	 */
	private void doAsyncPoll() {
		printStart("doAsyncPoll");

		if (!session.isModeSupported(EPPSession.MODE_ASYNC)) {
			System.out.println("doAsyncPoll: Session " + session.getClass().getName() + " does not support MODE_ASYNC, skipping test");
			printEnd("doAsyncPoll (skipped)");
			return;
		}
		int previousSessionMode = session.setMode(EPPSession.MODE_ASYNC);
		
		
		try {
			String theClientTransId = "ASYNC-CMD-" + System.currentTimeMillis();
			session.setTransId(theClientTransId);
			session.setPollOp(EPPSession.OP_REQ);
			EPPResponse thePollResp = session.sendPoll();
			
			Assert.assertNull("doAsyncPoll: sendPoll() response " + thePollResp + " != null", thePollResp);

			// Asynchronously read the poll response
			EPPResponse theResponse = session.readResponse();
			
			Assert.assertEquals("doAsyncPoll: clientTransId's don't Match (command and response)", theClientTransId, theResponse.getTransId().getClientTransId());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}
		finally {		
			session.setMode(previousSessionMode);
		}

		printEnd("doAsyncPoll");
	}
	
	
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
			handleException(e);
		}

		printEnd("endSession");
	}

	// End EPPSessionTst.endSession()

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
	}

	// End EPPSessionTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPSessionTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPSessionTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPSessionTst.class);

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

	// End EPPSessionTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * 
	 * <ul>
	 * <li>
	 * iterations    Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate    Turn XML validation on (<code>true</code>) or off
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
								   EPPSessionTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSessionTst.suite());
		}

		try {
			app.endApplication();
		}
		 catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPSessionTst.main(String [])

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception.  If the exception was caused by
	 * a server error, "Server Error : &lt;Response XML&gt;" will be
	 * specified. If the exception was caused by a general algorithm error,
	 * "General Error : &lt;Exception Description&gt;" will be specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(EPPCommandException aException) {
		EPPResponse response = session.getResponse();

		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		}
		else {
			aException.printStackTrace();
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPSessionTst.handleException(EPPCommandException)

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat.info(Thread.currentThread().getName() + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);
		System.out.println("****************************************************************\n");
	}

	// End EPPSessionTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printEnd(String aTest) {
		System.out.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat.info(Thread.currentThread().getName() + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	// End EPPSessionTst.testEnd(String)
}


// End class EPPSessionTst
