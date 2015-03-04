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
package com.verisign.epp.codec.host;


// JUNIT Imports
import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.host package.  The unit test
 * will     execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.host package concrete <code>EPPCommand</code>'s
 * and their     expected <code>EPPResponse</code>.  The unit test is
 * dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a>
 */
public class EPPHostTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPHostTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPHostTst(String name) {
		super(name);
	}

	// End EPPHostTst(String)

	/**
	 * Unit test of <code>EPPHostInfoCmd</code>.  The response to
	 * <code>EPPHostInfoCmd</code>     is <code>EPPHostInfoResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHostInfo() {
		EPPCodecTst.printStart("testHostInfo");

		EPPHostInfoCmd theCommand =
			new EPPHostInfoCmd("ABC-12345", "ns1.example.com");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPHostInfo Response
		EPPHostInfoResp		 theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPHostInfoResp attributes.
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPHostInfoResp(
								respTransId, "ns1.example.com",
								"NS1EXAMPLE1-VRSN",
								new EPPHostStatus(EPPHostStatus.ELM_STATUS_PENDING_TRANSFER),
								"ClientY", "ClientX", new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Test with all EPPHostInfoResp attributes set.
		Vector addresses = new Vector();
		addresses.addElement(new EPPHostAddress(
												"192.1.2.3", EPPHostAddress.IPV4));
		addresses.addElement(new EPPHostAddress(
												"1080:0:0:0:8:800:200C:417A",
												EPPHostAddress.IPV6));
		theResponse.setAddresses(addresses);

		theResponse.setLastUpdatedBy("ClientX");
		theResponse.setLastUpdatedDate(new Date());
		theResponse.setTrDate(new Date());

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostInfo");
	}

	// End EPPHostTst.testHostInfo()

	/**
	 * Unit test of <code>EPPHostPendActionMsg</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testHostPendActionMsg() {
		EPPCodecTst.printStart("testHostPendActionMsg");

		EPPTransId thePollTransId = new EPPTransId("ABC-12349", "54321-XYZ");
		EPPTransId thePendingTransId = new EPPTransId("DEF-12349", "12345-XYZ");

		EPPHostPendActionMsg theMsg =
			new EPPHostPendActionMsg(
									 thePollTransId, "ns1.example.com", true,
									 thePendingTransId, new Date());
		theMsg.setResult(EPPResult.SUCCESS_POLL_MSG);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theMsg);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostPendActionMsg");
	}

	// End EPPDomainTst.testHostPendActionMsg()

	/**
	 * Unit test of <code>EPPHostCreateCmd</code>.  The response to
	 * <code>EPPHostCreateCmd</code>     is <code>EPPHostExpireResp</code>. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHostCreate() {
		EPPHostCreateCmd     theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testHostCreate");

		// Create a host with just the host name.
		theCommand     = new EPPHostCreateCmd("ABC-12345", "ns1.example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a host with all of the attributes accept for contacts
		Vector addresses = new Vector();

		// Test that the default IPV4 type is assigned.
		addresses.addElement(new EPPHostAddress("192.1.2.3"));
		addresses.addElement(new EPPHostAddress(
												"198.1.2.3", EPPHostAddress.IPV4));
		addresses.addElement(new EPPHostAddress(
												"1080:0:0:0:8:800:200C:417A",
												EPPHostAddress.IPV6));
		addresses.addElement(new EPPHostAddress(
												"::FFFF:129.144.52.38",
												EPPHostAddress.IPV6));

		theCommand =
			new EPPHostCreateCmd("ABC-12345", "ns1.example.com", addresses);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Create Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse =
			new EPPHostCreateResp(respTransId, "ns1.example.com", new Date());
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostCreate");
	}

	// End EPPHostTst.testHostCreate()

	/**
	 * Unit test of <code>EPPHostDeleteCmd</code>.  The response to
	 * <code>EPPHostDeleteCmd</code>     is <code>EPPHostExpireResp</code>. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHostDelete() {
		EPPCodecTst.printStart("testHostDelete");

		EPPHostDeleteCmd theCommand =
			new EPPHostDeleteCmd("ABC-12345", "ns1.example.com");

		EPPEncodeDecodeStats commandStats =
			EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Create Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostDelete");
	}

	// End EPPHostTst.testHostDelete()

	/**
	 * Unit test of <code>EPPHostUpdateCmd</code>.  The response to
	 * <code>EPPHostUpdateCmd</code>     is <code>EPPResponse</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHostUpdate() {
		// Encode Update Command
		EPPHostUpdateCmd     theCommand   = null;
		EPPEncodeDecodeStats commandStats = null;

		EPPCodecTst.printStart("testHostUpdate");

		Vector addAddresses = new Vector();
		addAddresses.addElement(new EPPHostAddress("192.1.2.3"));

		Vector removeAddresses = new Vector();
		removeAddresses.addElement(new EPPHostAddress(
													  "1080:0:0:0:8:800:200C:417A",
													  EPPHostAddress.IPV6));

		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPHostStatus(
												 EPPHostStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED,
												 "Hello World",
												 EPPHostStatus.ELM_DEFAULT_LANG));
		addStatuses.addElement(new EPPHostStatus(EPPHostStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED));

		EPPHostAddRemove addItems =
			new EPPHostAddRemove(addAddresses, addStatuses);
		EPPHostAddRemove removeItems = new EPPHostAddRemove(removeAddresses);
		EPPHostAddRemove changeItems = new EPPHostAddRemove("ns2.example.com");

		theCommand =
			new EPPHostUpdateCmd(
								 "ABC-12345", "ns1.example.com", addItems,
								 removeItems, changeItems);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode Update Response (Standard EPPResponse)
		EPPTransId  respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPEncodeDecodeStats responseStats =
			EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostUpdate");
	}

	// End EPPHostTst.testHostUpdate()

	/**
	 * Unit test of <code>EPPHostCheckCmd</code>.  The response to
	 * <code>EPPHostCheckCmd</code>     is <code>EPPHostCheckResp</code>.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is     a public method.
	 */
	public void testHostCheck() {
		EPPHostCheckCmd		 theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testHostCheck");

		// Check single host name
		theCommand     = new EPPHostCheckCmd("ABC-12345", "ns1.example.com");

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check multiple host names
		Vector hosts = new Vector();
		hosts.addElement("ns1.example.com");
		hosts.addElement("ns2.example.com");
		hosts.addElement("ns3.example.com");

		theCommand     = new EPPHostCheckCmd("ABC-12345", hosts);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Host Check Responses
		EPPHostCheckResp     theResponse;
		EPPEncodeDecodeStats responseStats;

		// Response for a single host name
		EPPTransId respTransId =
			new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse =
			new EPPHostCheckResp(
								 respTransId,
								 new EPPHostCheckResult("ns1.david.com", true));

		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Response for multiple host names
		Vector hostResults = new Vector();
		hostResults.addElement(new EPPHostCheckResult("ns1.example.com", true));

		// Testing Domain Reason
		EPPHostCheckResult hostResult =
			new EPPHostCheckResult("example2.com", false);
		hostResult.setHostReason("In Use");
		hostResult.setLanguage("fr");
		hostResults.addElement(hostResult);

		hostResults.addElement(new EPPHostCheckResult("ns3.example.com", true));

		theResponse = new EPPHostCheckResp(respTransId, hostResults);
		theResponse.setResult(EPPResult.SUCCESS);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testHostCheck");
	}

	// End EPPHostTst.testHostCheck()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the <code>EPPHostMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPHostTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPHostTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPHostTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPHostTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPContactMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.host.EPPHostMapFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPHostMapFactory to EPPCodec: "
						+ e);
		}

		return suite;
	}

	// End EPPHostTst.suite()

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
								   "EPPHostTst Thread " + i, EPPHostTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPHostTst.suite());
		}
	}

	// End EPPHostTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPHostTst.setNumIterations(long)

}
