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
package com.verisign.epp.codec.whois;

// JUNIT Imports
import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.whois package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.whois package. The unit test is dependent on the use
 * of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 */
public class EPPWhoisTst extends TestCase {
	private static long numIterations = 1;

	/**
	 * Creates a new EPPWhoisTst object.
	 * 
	 * @param name
	 *            Name of test to execute
	 */
	public EPPWhoisTst(String name) {
		super(name);
	}

	/**
	 * Tests the <code>EPPWhoisInfo</code> info command extension. The tests
	 * include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test adding the extension with a <code>true</code> flag value
	 * <li>Test adding the extension with a <code>false</code> flag value
	 * </ol>
	 */
	public void testWhoisInf() {

		EPPCodecTst.printStart("testWhoisInf");

		// -- Create domain info command with flag as true
		EPPDomainInfoCmd theCommand;
		theCommand = new EPPDomainInfoCmd("ABC-12345", "whois-true.com");

		theCommand.setHosts(EPPDomainInfoCmd.HOSTS_ALL);

		theCommand.addExtension(new EPPWhoisInf(true));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// -- Create domain info command with flag as false
		theCommand = new EPPDomainInfoCmd("ABC-67890", "whois-false.com");

		theCommand.setHosts(EPPDomainInfoCmd.HOSTS_DELEGATED);

		theCommand.addExtension(new EPPWhoisInf(false));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testWhoisInf");
	}

	/**
	 * Tests the <code>EPPWhoisInfoData</code> info response extension. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test adding the extension with most common attributes set
	 * <li>Test adding the extension with all attributes set
	 * </ol>
	 */
	public void testWhoisInfData() {

		EPPCodecTst.printStart("testWhoisInfData");
		EPPDomainInfoResp theResponse;

		Vector statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));
		EPPTransId respTransId;

		// -- Create domain info response with most common attributes
		respTransId = new EPPTransId("ABC-67890", "12345-XYZ");
		theResponse = new EPPDomainInfoResp(respTransId, "EXAMPLE1-VRSN",
				"example.com", "ClientX", statuses, "ClientY", new Date(),
				new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.addExtension(new EPPWhoisInfData("Example Registrar Inc.",
				"whois.example.com", "http://www.example.com"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(commandStats);

		// -- Create domain info response with all attributes
		respTransId = new EPPTransId("ABC-12345", "54321-XYZ");
		theResponse = new EPPDomainInfoResp(respTransId, "EXAMPLE1-VRSN",
				"example.com", "ClientX", statuses, "ClientY", new Date(),
				new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.addExtension(new EPPWhoisInfData("Example Registrar Inc.",
				"whois.example.com", "http://www.example.com",
				"iris.example.com"));

		commandStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testWhoisInfData");
	}

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPWhoisTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPWhoisTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPWhoisTst</code>.
	 * 
	 * @return <code>Junit</code> tests
	 */
	public static Test suite() {

		TestSuite suite = new TestSuite(EPPWhoisTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addExtFactory(
					"com.verisign.epp.codec.whois.EPPWhoisExtFactory");
		}
		catch (EPPCodecException e) {
			Assert
					.fail("EPPCodecException adding EPPDomainMapFactory or EPPWhoisExtFactory to EPPCodec: "
							+ e);
		}
		return suite;
	}

	// End EPPWhoisTst.suite()

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * 
	 * <ul>
	 * <li> iterations Number of unit test iterations to run </li>
	 * <li> validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off. </li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            Command line arguments
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
				TestThread thread = new TestThread("EPPWhoisTst Thread " + i,
						EPPWhoisTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPWhoisTst.suite());
		}
	}

	// End EPPWhoisTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *            number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPWhoisTst.setNumIterations(long)
}
