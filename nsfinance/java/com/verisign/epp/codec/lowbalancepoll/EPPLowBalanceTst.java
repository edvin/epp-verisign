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
package com.verisign.epp.codec.lowbalancepoll;


// JUNIT Imports
import junit.framework.*;

// Log4J Imports
import org.apache.log4j.*;


import java.io.IOException;
import java.util.*;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;
import java.util.Vector;

// JAXP Imports
import javax.xml.parsers.DocumentBuilderFactory;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.util.TestErrorHandler;
import com.verisign.epp.util.TestThread;

import com.verisign.epp.codec.lowbalancepoll.*;


/**
 * Is a unit test of the com.verisign.epp.codec.lowbalancepoll package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.lowbalancepoll package concrete <code>EPPLowBalance</code>'s
 * and their expected <code>EPPResponse</code>. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPLowBalanceTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPLowBalanceTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPLowBalanceTst(String name) {
		super(name);
	}

	// End EPPLowBalanceTst(String)



    public void testPoll() {

        EPPCodecTst.printStart("testPoll");

        EPPLowBalancePollResponse pollResponse = new EPPLowBalancePollResponse();

        EPPTransId  respTransId =
            new EPPTransId("abc-123", "54321-XYZ");

        pollResponse.setTransId(respTransId);
        pollResponse.setResult(EPPResult.SUCCESS_POLL_MSG);

        pollResponse.setMsgQueue(new EPPMsgQueue(
                                                new Long(5), "12345", new Date(),
                                                "Low Account Balance (SRS)"));

        pollResponse.setRegistrarName("Test Registar");
        pollResponse.setCreditLimit("1000");
        pollResponse.setCreditThreshold(new EPPLowBalancePollThreshold(EPPLowBalancePollThreshold.FIXED, "50"));
		pollResponse.setAvailableCredit("40");


        EPPEncodeDecodeStats commandStats =
            EPPCodecTst.testEncodeDecode(pollResponse);


        EPPCodecTst.printEnd("testPoll");
    }



	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {
	}

	// End EPPLowBalanceTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPLowBalanceTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPLowBalanceTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPLowBalanceTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPLowBalancePollMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.lowbalancepoll.EPPLowBalancePollMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPLowBalanceFactory to EPPCodec: "
						+ e);
		}


		return suite;
	}

	// End EPPLowBalanceTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:
	 * <br>
	 *
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.
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
								   "EPPLowBalanceTst Thread " + i,
								   EPPLowBalanceTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPLowBalanceTst.suite());
		}
	}

	// End EPPLowBalanceTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPLowBalanceTst.setNumIterations(long)

}
