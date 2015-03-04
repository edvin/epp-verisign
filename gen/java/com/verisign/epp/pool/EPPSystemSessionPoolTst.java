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


// JUNIT Imports
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.TestThread;


/**
 */
public class EPPSystemSessionPoolTst extends EPPSessionPoolTst {

	/** Logging category */
	private static final Logger cat =
		Logger.getLogger(
						 EPPSystemSessionPoolTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());


	/**
	 * Allocates an <code>EPPSessionPoolTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPSystemSessionPoolTst(String name) {
		super(name);
	}

	// End EPPSessionPoolTst(String)

	
	/**
	 * Test interfacing with two system session pools 
	 * (test1 and test2 systems).  A session is borrowed 
	 * from each pool, a hello command is sent, and 
	 * the sessions are retured to their pools.
	 */
	public void testTwoSystemPools() {
		printStart("testTwoSystemPools");

		EPPSession theSession1 = null;
		EPPSession theSession2 = null;
		try {
			theSession1 = this.borrowSession(EPPSessionPool.DEFAULT);
			theSession1.hello();
			
			theSession2 = this.borrowSession("test");
			theSession2.hello();
		}
		catch (EPPCommandException ex) {
			try {
				sessionPool.invalidateObject(EPPSessionPool.DEFAULT,theSession1);
				sessionPool.invalidateObject("test",theSession2);
				theSession1 = null;
			}
			catch (EPPSessionPoolException e) {
				// ignore
			}			
		}
		finally {
			if (theSession1 != null)
				this.returnSession(EPPSessionPool.DEFAULT, theSession1);
			
			if (theSession2 != null) 
				this.returnSession("test", theSession2);
		}

		printEnd("testTwoSystemPools");
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
		TestSuite suite = new TestSuite(EPPSystemSessionPoolTst.class);

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
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception.  If the exception was caused by
	 * a server error, "Server Error : &lt;Response XML&gt;" will be
	 * specified. If the exception was caused by a general algorithm error,
	 * "General Error : &lt;Exception Description&gt;" will be specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(EPPCommandException aException, EPPSession aSession) {
		EPPResponse response = aSession.getResponse();

		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		}
		else {
			aException.printStackTrace();
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPSessionPoolTst.handleException(EPPCommandException)

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

		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	// End EPPSessionPoolTst.testEnd(String)

	/**
	 * Utility method to borrow a session from the session pool.
	 * All exceptions will result in the test failing.  This method
	 * should only be used for positive session pool tests.
	 * 
	 * @param aSystem System session pool
	 * 
	 * @return Session from the session pool
	 */
	protected EPPSession borrowSession(String aSystem) {
		EPPSession theSession = null;
		try {
			theSession = sessionPool.borrowObject(aSystem);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("borrowSession(" + aSystem + "): Exception borrowing session: " + ex);			
		}
		
		return theSession;
	}

	/**
	 * Utility method to return a session to the session pool.  
	 * This should be placed in a finally block.  All exceptions will
	 * result in the test failing.
	 * 
	 * @param aSystem session pool name
	 * @param aSession Session to return to the pool
	 */
	protected void returnSession(String aSystem, EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.returnObject(aSystem, aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception returning session: " + ex);			
		}
	}


		
	/**
	 * Utility method to invalidate a session in the session pool.  
	 * 
	 * @param aSystem System session pool name
	 * @param aSession Session to invalidate in the pool
	 */
	protected void invalidateSession(String aSystem, EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.invalidateObject(aSystem, aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception invalidating session: " + ex);			
		}
	}
	
}



// End class EPPSessionPoolTst
