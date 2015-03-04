/***********************************************************
 Copyright (C) 2004 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.codec.suggestion;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.codec.suggestion.util.RandomHelper;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.suggestion package. The unit
 * test will execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.suggestion package concrete
 * <code>EPPCommand</code>'s and their expected <code>EPPResponse</code>.
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 * @author jcolosi
 */
public class EPPSuggestionTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 100;

	/**
	 * Creates a new EPPSuggestionTst object.
	 */
	public EPPSuggestionTst(String name) {
		super(name);
	}

	public void testSuggestionInfo() {
		EPPCodecTst.printStart("testSuggestionInfo");

		try {
			/**
			 * The test is randomized so we'll run it many times.
			 */
			for (int i = 0; i < numIterations; i++) {
				/**
				 * The test can be set to a specific seed so that errant
				 * behaviour can be reproduced. Comment the following line to
				 * randomize the test. Uncomment the following line to make the
				 * test deterministic.
				 */
				RandomHelper.reset(i);

				EPPSuggestionInfoCmd cmd = RandomHelper.getCommand();
				EPPCodecTst.testEncodeDecode(cmd);

				EPPSuggestionInfoResp rsp = RandomHelper.getResponse(cmd.getTransId());
				EPPCodecTst.testEncodeDecode(rsp);
			}
		} catch (InvalidValueException e) {
			e.printStackTrace();
		}

		try {
			EPPSuggestionInfoCmd cmd = RandomHelper.getCommand();
			cmd.setLanguage(null);
			System.out.println("cmd with no language set");
			EPPCodecTst.testEncodeDecode(cmd);

		}
		catch (InvalidValueException e) {
			e.printStackTrace();
		}
		
		try {
			EPPSuggestionInfoCmd cmd = RandomHelper.getCommand();
			cmd.setLanguage(EPPSuggestionConstants.SPANISH_CODE);
			System.out.println("cmd with " + EPPSuggestionConstants.SPANISH_CODE + " language set");
			EPPCodecTst.testEncodeDecode(cmd);

		}
		catch (InvalidValueException e) {
			e.printStackTrace();
		}
		
		try {
			EPPSuggestionInfoCmd cmd = RandomHelper.getCommand();
			cmd.setLanguage("FRE");
			System.out.println("cmd with " + "FRE" + " language set");
			EPPCodecTst.testEncodeDecode(cmd);

		}
		catch (InvalidValueException e) {
			e.printStackTrace();
		}
		
		try {
			EPPSuggestionInfoCmd cmd = RandomHelper.getCommand();
			cmd.setKey("01234567890123456789012345678901234567890123456789");
			System.out.println("cmd with " + "01234567890123456789012345678901234567890123456789" + " key set");
			EPPCodecTst.testEncodeDecode(cmd);

		}
		catch (InvalidValueException e) {
			e.printStackTrace();
		}
		
		EPPCodecTst.printEnd("testSuggestionInfo");
	}

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPSuggestionMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {}

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {}

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPSuggestionTst</code>.
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPSuggestionTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPSuggestionMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.suggestion.EPPSuggestionMapFactory");
		} catch (EPPCodecException e) {
			Assert
					.fail("EPPCodecException adding EPPSuggestionMapFactory to EPPCodec: "
							+ e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.

		/*
		 * try {
		 * EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory"); }
		 * catch (EPPCodecException e) { Assert.fail("EPPCodecException adding
		 * EPPContactMapFactory to EPPCodec: " + e); }
		 */
		return suite;
	}

	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * <ul>
	 * <li> iterations Number of unit test iterations to run </li>
	 * <li> validate Turn XML validation on (<code>true</code>) or off (<code>false</code>).
	 * If validate is not specified, validation will be off. </li>
	 * </ul>
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
				TestThread thread = new TestThread("EPPSuggestionTst Thread " + i,
						EPPSuggestionTst.suite());
				thread.start();
			}
		} else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSuggestionTst.suite());
		}
	}

	/**
	 * Sets the number of iterations to run per test.
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}
}
