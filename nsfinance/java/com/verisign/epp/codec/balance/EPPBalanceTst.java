/***********************************************************
Copyright (C) 2011 VeriSign, Inc.

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
package com.verisign.epp.codec.balance;

// JUNIT Imports
import java.math.BigDecimal;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.balance package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.balance command and response classes.
 */
public class EPPBalanceTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPBalanceTst object.
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public EPPBalanceTst(String name) {
		super(name);
	}

	/**
	 * Test the <code>EPPBalanceInfoCmd</code>, which has no attributes 
	 * beyond that of an <code>EPPCommand</code>.  The only test is to 
	 * create the <code>EPPBalanceInfoCmd</code> and then ensure that 
	 * it can be encoded and decoded propertly.  
	 */
	public void testBalanceInfoCmd() {

		EPPCodecTst.printStart("testBalanceInfoCmd");

		EPPBalanceInfoCmd cmd = new EPPBalanceInfoCmd();
		EPPCodecTst.testEncodeDecode(cmd);

		cmd = new EPPBalanceInfoCmd("ABC-12345");
		EPPCodecTst.testEncodeDecode(cmd);

		EPPCodecTst.printEnd("testBalanceInfoCmd");
	}

	/**
	 * Test the <code>EPPBalanceInfoResp</code>.  The only 
	 * real variance with the <code>EPPBalanceInfoResp</code> is whether the   
	 * <code>creditThreshold</code> attribute is a fixed dollar 
	 * amount or a percentage.  
	 */
	public void testBalanceInfoResp() {

		EPPCodecTst.printStart("testBalanceInfoResp");

		// Test balance information with fixed credit threshold
		EPPBalanceInfoResp resp = new EPPBalanceInfoResp(
				new EPPTransId("ABC-12345", "54322-XYZ"),
				new BigDecimal("1000.00"), // credit limit
				new BigDecimal("200.00"),  // balance
				new BigDecimal("800.00"),  // available credit
				new EPPCreditThreshold(EPPCreditThreshold.FIXED, new BigDecimal("500.00"))
				);
		EPPCodecTst.testEncodeDecode(resp);

		// Test balance information with percent credit threshold
		resp = new EPPBalanceInfoResp(
				new EPPTransId("ABC-12345", "54322-XYZ"),
				new BigDecimal("1000.00"), // credit limit
				new BigDecimal("200.00"),  // balance
				new BigDecimal("800.00"),  // available credit
				new EPPCreditThreshold(EPPCreditThreshold.PERCENT, new BigDecimal("50"))
				);
		EPPCodecTst.testEncodeDecode(resp);

		EPPCodecTst.printEnd("testBalanceInfoResp");
	}
	
	
	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {
	}

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPBalanceTst</code>.
	 * 
	 * @return Tests to run
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPBalanceTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPLowBalancePollMapFactory to the EPPCodec.
		try {
			EPPFactory
					.getInstance()
					.addMapFactory(
							"com.verisign.epp.codec.balance.EPPBalanceMapFactory");
		}
		catch (EPPCodecException e) {
			Assert
					.fail("EPPCodecException adding EPPBalanceMapFactory to EPPCodec: "
							+ e);
		}

		return suite;
	}

	/**
	 * Main for the com.verisign.epp.codec.balance.EPPBalanceTst unit tests.
	 */
	public static void main(String[] args) {
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
				TestThread thread = new TestThread("EPPBalanceTst Thread " + i,
						EPPBalanceTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPBalanceTst.suite());
		}
	}

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *            number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

}
