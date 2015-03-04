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

package com.verisign.epp.codec.namestoreext;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------

// JUNIT Imports
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.host.EPPHostAddress;
import com.verisign.epp.codec.host.EPPHostCreateCmd;
import com.verisign.epp.codec.host.EPPHostCreateResp;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.namestoreext package.  The unit
 * test will execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.namestoreext package concrete extension
 * <code>EPPCodecComponent</code>'s.  The Domain and Host commands and
 * responses are used to test extending both.<br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.4 $
 */
public class EPPNamestoreExtTst extends TestCase {
	/**
	 * Number of unit test iterations to run.  This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;


	/**
	 * Creates a new EPPNamestoreExtTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPNamestoreExtTst(String name) {
		super(name);
	}
	 // End EPPNamestoreExtTst(String)

	/**
	 * Unit test of extending the Domain and host create commands and responses
	 * to specify the sub-product.     <br>
	 * This test will be invoked by JUNIT, since it is prefixed with
	 * "test" and is     a public method.
	 */
	public void testNamestoreExtNamestoreExt() {
		EPPCodecTst.printStart("testNSProductProduct");

		// Generic objects
		EPPTransId			 respTransId =
			new EPPTransId("ABC-12345", "54321-XYZ");
		EPPEncodeDecodeStats theStats;

		//-- Extend Host Create Command
		EPPHostCreateCmd theHostCommand;
		Vector			 theHosts = new Vector();
		theHosts.addElement(new EPPHostAddress("192.1.2.3"));
		theHosts.addElement(new EPPHostAddress("192.1.2.4"));

		theHostCommand =
			new EPPHostCreateCmd("ABC-12345", "ns1.example.com", theHosts);

		// Extension
		theHostCommand.addExtension(new EPPNamestoreExtNamestoreExt("dottv"));

		theStats = EPPCodecTst.testEncodeDecode(theHostCommand);
		System.out.println(theStats);

		//-- Extend Host Create Response
		EPPHostCreateResp theHostResponse;

		// Test with just required EPPNSProductPrdData attributes.
		theHostResponse =
			new EPPHostCreateResp(respTransId, "ns1.example.com", new Date());
		theHostResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theHostResponse.addExtension(new EPPNamestoreExtNamestoreExt("dottv"));

		theStats = EPPCodecTst.testEncodeDecode(theHostResponse);
		System.out.println(theStats);

		//-- Extend Domain Create Command
		EPPDomainCreateCmd theDomainCommand;
		theDomainCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345", "john.doe.name",
								   new EPPAuthInfo("2fooBAR"));

		// Extension
		theDomainCommand.addExtension(new EPPNamestoreExtNamestoreExt("dottv"));

		theStats = EPPCodecTst.testEncodeDecode(theDomainCommand);
		System.out.println(theStats);

		//-- Extend Domain Create Response
		EPPDomainCreateResp theDomainResponse;

		// Test with just required EPPNSProductPrdData attributes.
		theDomainResponse =
			new EPPDomainCreateResp(
									respTransId, "john.doe.name",
									new GregorianCalendar(2001, 5, 5).getTime(),
									new Date());
		theDomainResponse.setResult(EPPResult.SUCCESS);

		// Extension
		theDomainResponse.addExtension(new EPPNamestoreExtNamestoreExt("dottv"));

		theStats = EPPCodecTst.testEncodeDecode(theDomainResponse);
		System.out.println(theStats);

		EPPCodecTst.printEnd("testNamestoreExtNamestoreExt");
	}
	 // End EPPNamestoreExtTst.testNamestoreExtNamestoreExt()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and     initializes the
	 * <code>EPPNamestoreExtMapFactory</code> with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}
	 // End EPPNamestoreExtTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}
	 // End EPPNamestoreExtTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPNamestoreExtTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPNamestoreExtTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPNSProductExtFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addExtFactory("com.verisign.epp.codec.namestoreext.EPPNamestoreExtExtFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding factories to EPPCodec: " + e);
		}

		return suite;
	}
	 // End EPPNamestoreExtTst.suite()

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
								   "EPPNamestoreExtTst Thread " + i,
								   EPPNamestoreExtTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPNamestoreExtTst.suite());
		}
	}
	 // End EPPNamestoreExtTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}
	 // End EPPNamestoreExtTst.setNumIterations(long)

}
