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
package com.verisign.epp.pool;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.InvalidateSessionException;
import com.verisign.epp.util.TestThread;
import com.verisign.epp.util.TestUtil;


/**
 */
public class EPPSessionPoolTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance
	 * (<code>EPPApplicationSingle</code>)
	 */
	protected static EPPApplicationSingle app =
		EPPApplicationSingle.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	protected static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat =
		Logger.getLogger(
						 EPPSessionPoolTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Session pool associated with test */
	protected static EPPSessionPool sessionPool = null;

	/**
	 * Allocates an <code>EPPSessionPoolTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPSessionPoolTst(String name) {
		super(name);
	}

	// End EPPSessionPoolTst(String)


	/**
	 * Unit test of <code>EPPSession.sendHello</code> command.  The session
	 * attribute is initialized with the attributes defined in the EPP sample
	 * files.
	 */
	public void testHello() {
		printStart("testHello");

		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			theSession.hello();
		}
		catch (EPPCommandException ex) {
			try {
				sessionPool.invalidateObject(theSession);
				theSession = null;
			}
			catch (EPPSessionPoolException e) {
				// ignore
			}			
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}

		printEnd("testHello");
	}
	

	/**
	 * Unit test of <code>EPPSession.sendPoll</code> command.
	 */
	public void testPoll() {
		printStart("testPoll");

		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			
			EPPResponse response = null;
			Long	    numMsgs = null;

			try {
				theSession.setTransId("ABC-12345-XYZ");
				theSession.setPollOp(EPPSession.OP_REQ);
				response = theSession.sendPoll();

				//-- Output all of the response attributes
				System.out.println("doPoll: Response = [" + response + "]\n\n");

				numMsgs = response.getMsgQueueCount();

				if (numMsgs != null) {
					System.out.println("doPoll: # messages = " + numMsgs);
				}
			}
			 catch (EPPCommandException e) {
				TestUtil.handleException(theSession, e);
			}

			try {
				theSession.setTransId("ABC-12345-XYZ");
				theSession.setPollOp(EPPSession.OP_ACK);
				theSession.setMsgID("1234");
				response = theSession.sendPoll();

				//-- Output all of the response attributes
				System.out.println("doPoll: Response = [" + response + "]\n\n");
			}
			 catch (EPPCommandException e) {
				// Expect error
				System.out.println("doPoll: Error Response = [" + e.getResponse()
								   + "]\n\n");
			}
			
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}


		printEnd("testPoll");
	}
	
	
	/**
	 * Test the handling of a session that is closed by the server by 
	 * sending a poll aock with the message ID &quot;CLOSE-SESSION-TEST&quot;.  
	 * This test will only work against the Stub Server. 
	 */
	public void testCloseSession() {
		printStart("testCloseSession");
		
		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			
			try {
				theSession.setTransId("CLOSE-SESSION-TEST");
				theSession.setPollOp(EPPSession.OP_ACK);
				theSession.setMsgID("CLOSE-SESSION-TEST");
				theSession.sendPoll();
				
				Assert.fail("testCloseSession: Expcted error for poll ack of CLOSE-SESSION-TEST");
			}
			 catch (EPPCommandException e) {
				System.out.println("testCloseSession: Got expected exception = [" + e + "], check for any CLOSE_WAIT connections\n\n");
				this.invalidateSession(theSession);
				Assert.assertNull(theSession.getInputStream());
				theSession = null;
			}
			 
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}
		printEnd("testCloseSession");
	}
	

	/**
	 * Unit test of <code>EPPSession.endSession</code>.  One session in the
	 * session pool wil be ended.
	 */
	public void testEndSession() {
		printStart("testEndSession");
		
		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			this.invalidateSession(theSession);
			theSession = null;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("testEndSession(): Exception invalidating session: " + ex);			
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}


		printEnd("testEndSession");
	}

	// End EPPSessionPoolTst.endSession()

	/**
	 * Print out the number of active and idle sessions prior to 
	 * sleeping for 1 second past the absolute timeout, which should result 
	 * in the sessions getting refreshed.  Look to the logs to ensure that the 
	 * sessions were refreshed since there is no API to ensure that the 
	 * sessions were refreshed.  The test does nothing if the absolute timeout 
	 * is set to a value higher than 10000 (10 seconds) since the Production 
	 * configuration would result in the test sleeping for 1 day.
	 */
	public void testAbsoluteTimeout() {
		printStart("testAbsoluteTimeout");
		
		System.out.println("testAbsolutionTimeout: active = " + sessionPool.getGenericObjectPool().getNumActive() + 
				", idle = " + sessionPool.getGenericObjectPool().getNumIdle());
			
		if (this.sessionPool.getAbsoluteTimeout() > 10000) {
			System.out.println("testAbsolutionTimeout: Do nothing since absolute timeout of " +  
					this.sessionPool.getAbsoluteTimeout() + " > 10000");
		}
		else {
			
			System.out.println("Sleeping for " + (sessionPool.getAbsoluteTimeout() + 1000) + " ms");
			try {
				Thread.currentThread().sleep(sessionPool.getAbsoluteTimeout() + 1000);
			}
			catch (Exception ex) {
				// Ignore
			}
			
			System.out.println("testAbsolutionTimeout: active = " + sessionPool.getGenericObjectPool().getNumActive() + 
					", idle = " + sessionPool.getGenericObjectPool().getNumIdle());
		}
		
		printEnd("testAbsoluteTimeout");
	}

	/**
	 * Test that the idle timeout is working by sleeping for 1 second past 
	 * the idle timeout.  There a log message containing the text 
	 * &qt;is past idle timeout, sending hello&qt; for each idle session 
	 * in the pool in &qt;epp.log&qt;.  This test will only run if the 
	 * idle timeout is lower than or equal to 10 seconds.  
	 */
	public void testIdleTimeout() {
		printStart("testIdleTimeout");
				
		if (this.sessionPool.getIdleTimeout() > 10000) {
			System.out.println("testIdleTimeout: Do nothing since idle timeout of " +  
					this.sessionPool.getIdleTimeout() + " > 10000");
		}
		else {
			
			System.out.println("Sleeping for " + (sessionPool.getIdleTimeout() + 1000) + " ms");
			try {
				Thread.currentThread().sleep(sessionPool.getIdleTimeout() + 1000);
			}
			catch (Exception ex) {
				// Ignore
			}
			
			System.out.println("testIdleTimeout: Look for epp.log messages containing text \"is past idle timeout, sending hello\"");
		}
		
		printEnd("testIdleTimeout");
	}
	
	
	
	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {
		
	}

	// End EPPSessionPoolTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPSessionPoolTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPSessionPoolTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPSessionPoolTst.class);

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

		// Initialize the session pool
		try {
			sessionPool = EPPSessionPool.getInstance();
			sessionPool.init();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Error initializing the session pool: " + ex);			
		}
		 
		 
		return suite;
	}

	// End EPPSessionPoolTst.suite()

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
								   "EPPSessionPoolTst Thread " + i,
								   EPPSessionPoolTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSessionPoolTst.suite());
		}

		try {
			app.endApplication();
		}
		 catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPSessionPoolTst.main(String [])


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

		cat.info(aTest + ": start");
		System.out.println("Start of " + aTest);
		System.out.println("****************************************************************\n");
	}

	// End EPPSessionPoolTst.testStart(String)

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

		cat.info(aTest + ": end");
		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	// End EPPSessionPoolTst.testEnd(String)
	
	/**
	 * Utility method to borrow a session from the session pool.
	 * All exceptions will result in the test failing.  This method
	 * should only be used for positive session pool tests.
	 * 
	 * @return Session from the session pool
	 */
	protected EPPSession borrowSession() {
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
	 * Utility method to invalidate a session in the session pool.  
	 * 
	 * @param aSession Session to invalidate in the pool
	 */
	protected void invalidateSession(EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.invalidateObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception invalidating session: " + ex);			
		}
	}
	
	/**
	 * Utility method to return a session to the session pool.  
	 * This should be placed in a finally block.  All exceptions will
	 * result in the test failing.
	 * 
	 * @param aSession Session to return to the pool
	 */
	protected void returnSession(EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.returnObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception returning session: " + ex);			
		}
	}
	
}



// End class EPPSessionPoolTst
