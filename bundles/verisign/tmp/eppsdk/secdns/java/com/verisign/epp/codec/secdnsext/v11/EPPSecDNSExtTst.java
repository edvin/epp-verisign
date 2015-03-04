/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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
package com.verisign.epp.codec.secdnsext.v11;

// JUNIT Imports
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.secdnsxt package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.secdnsext package concrete <code>EPPSecDNSExt</code>'s
 * and their expected <code>EPPResponse</code>. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.9 $
 */
public class EPPSecDNSExtTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPSecDNSExtTst object.
	 * 
	 * @param name
	 *            DOCUMENT ME!
	 */
	public EPPSecDNSExtTst(String name) {
		super(name);
	}

	// End EPPSecDNSExtTst(String)

	/**
	 * Unit test of <code>testDomainInfoRespWithSecDNSExt</code>. The response
	 * to <code>testDomainInfoRespWithSecDNSExt</code> is
	 * <code>EPPDomainInfoResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */

	public void testDomainInfoRespWithSecDNSExt() {

		EPPCodecTst.printStart("testDomainInfoRespWithSecDNSExt");

		// trans id of the response
		EPPTransId respTransId = new EPPTransId("54321-CLI", "54321-SER");

		Vector statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

		// --- DS Data Interface ---

		// EPPDomainInfo Response
		EPPDomainInfoResp theResponse = new EPPDomainInfoResp(respTransId,
				"EXAMPLE1-VRSN", "example.tv", "ClientX", statuses, "ClientY",
				new Date(), new EPPAuthInfo("2fooBAR"));

		theResponse.setResult(EPPResult.SUCCESS);

		// instantiate a secDNS:keyData object
		EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
		keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
		keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
		keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
				+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
				+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
				+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
				+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");

		// instantiate another secDNS:keyData object
		EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
				EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
				EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
				EPPSecDNSAlgorithm.RSASHA1,
				"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
						+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
						+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
						+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
						+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");

		// instantiate a secDNS:dsData object
		EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
		dsData.setKeyTag(34095);
		dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
		dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
		dsData.setKeyData(keyData);

		// instantiate another secDNS:dsData object
		EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(10563,
				EPPSecDNSAlgorithm.RSASHA1,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2);

		// instantiate the secDNS:infData object
		EPPSecDNSExtInfData infData = new EPPSecDNSExtInfData();
		infData.setMaxSigLife(604800);
		List dsDataVec = new ArrayList();
		dsDataVec.add(dsData);
		infData.setDsData(dsDataVec);
		infData.appendDsData(dsData2);

		// set the secDNS:infData in the response as an extension
		theResponse.addExtension(infData);

		EPPEncodeDecodeStats responseStats;
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Key Data Interface
		// EPPDomainInfo Response
		theResponse = new EPPDomainInfoResp(respTransId, "EXAMPLE1-VRSN",
				"example.tv", "ClientX", statuses, "ClientY", new Date(),
				new EPPAuthInfo("2fooBAR"));

		theResponse.setResult(EPPResult.SUCCESS);

		// instantiate the secDNS:infData object
		infData = new EPPSecDNSExtInfData();
		infData.setMaxSigLife(604800);
		List keyDataList = new ArrayList();
		keyDataList.add(keyData);
		infData.setKeyData(keyDataList);
		infData.appendKeyData(keyData2);

		// set the secDNS:infData in the response as an extension
		theResponse.addExtension(infData);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainInfoRespWithSecDNSExt");
	} // End EPPSecDNSExtTst.testDomainInfoRespWithSecDNSExt()

	/**
	 * Unit test of <code>EPPDomainCreateCmd</code> including secDNS extensions.
	 * The response to <code>EPPDomainCreateCmd</code> is
	 * <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainCreate() {
		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainCreate");

		// --- DS Data Interface ---
		theCommand = new EPPDomainCreateCmd("ABC-12345-XYZ",
				"dsdata-interface.com", new EPPAuthInfo("2fooBAR"));

		// -- Add secDNS Extension with DS Data Interface
		// instantiate a secDNS:keyData object
		EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
		keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
		keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
		keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
				+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
				+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
				+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
				+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
		
		EPPSecDNSExtDsData dsData = null;
		try {
			dsData = keyData.toDsData("dsdata-interface.com", EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
			dsData.setKeyData(keyData);
		}
		catch (EPPCodecException ex) {
			Assert.fail("Error generating DS data from key data:" + ex);
		}

		// instantiate another secDNS:keyData object
		EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
				EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
				EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
				EPPSecDNSAlgorithm.RSASHA1,
				"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
						+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
						+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
						+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
						+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");

		// instantiate another secDNS:dsData object
		EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(10563,
				EPPSecDNSAlgorithm.RSASHA1,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2);

		// instantiate the secDNS:create object
		EPPSecDNSExtCreate create = new EPPSecDNSExtCreate();
		create.setMaxSigLife(604800);
		List dsDataList = new ArrayList();
		dsDataList.add(dsData);
		create.setDsData(dsDataList);
		create.appendDsData(dsData2);

		// set the secDNS:create in the command as an extension
		theCommand.addExtension(create);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// --- Key Data Interface ---
		theCommand = new EPPDomainCreateCmd("ABC-12345-XYZ",
				"keydata-interface.com", new EPPAuthInfo("2fooBAR"));

		// -- Add secDNS Extension with Key Data Interface

		// instantiate the secDNS:create object
		create = new EPPSecDNSExtCreate();
		create.setMaxSigLife(604800);
		List keyDataList = new ArrayList();
		keyDataList.add(keyData);
		create.setKeyData(keyDataList);
		create.appendKeyData(keyData2);

		// set the secDNS:create in the command as an extension
		theCommand.addExtension(create);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testDomainCreate");
	} // End EPPSecDNSExtTst.testDomainCreate()

	/**
	 * Unit test of <code>EPPDomainUpdateCmd</code> including secDNS extensions.
	 * The response to <code>EPPDomainUpdateCmd</code> is
	 * <code>EPPResponse</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainUpdate() {
		EPPDomainUpdateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainUpdate");

		
		// --- Adding and Removing DS using the DS Data Interface ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		EPPSecDNSExtUpdate update = new EPPSecDNSExtUpdate();

		update.appendRemDsData(new EPPSecDNSExtDsData(12345,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"38EC35D5B3A34B33C99B"));
		update.appendAddDsData(new EPPSecDNSExtDsData(12346,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"38EC35D5B3A34B44C39B"));

		theCommand.addExtension(update);

		// Test Adding and Removing DS using the DS Data Interface
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		
		// --- Update maxSigLife ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();
		update.setMaxSigLife(605900);
		
		theCommand.addExtension(update);

		// Test Update maxSigLife
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		// --- Adding and Removing Key Data using the Key Data Interface and set maxSigLife ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();

		update.appendRemKeyData(new EPPSecDNSExtKeyData(256,
				EPPSecDNSExtKeyData.DEFAULT_PROTOCOL, EPPSecDNSAlgorithm.RSAMD5, "AQPJ////4QQQ"));
		update.appendAddKeyData(new EPPSecDNSExtKeyData(256,
				EPPSecDNSExtKeyData.DEFAULT_PROTOCOL, EPPSecDNSAlgorithm.RSAMD5, "AQPJ////4Q=="));
		
		update.setMaxSigLife(605900);
		
		theCommand.addExtension(update);
		
		// Test Adding and Removing Key Data using the Key Data Interface and set maxSigLife
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		
		// --- Removing DS using the DS Data Interface ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();

		update.appendRemDsData(new EPPSecDNSExtDsData(12346,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"38EC35D5B3A34B44C39B"));

		theCommand.addExtension(update);

		// Test Removing DS using the DS Data Interface
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		

		// --- Removing all DS or Key Data ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();

		update.setUrgent(true);		
		
		update.setRemAllData(true);

		theCommand.addExtension(update);

		// Test Removing all DS or Key Data
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		
		// --- Replacing all DS Data using the DS Data Interface ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();
		
		update.setUrgent(true);

		update.setRemAllData(true);

		update.appendAddDsData(new EPPSecDNSExtDsData(12346,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"38EC35D5B3A34B44C39B"));
		
		theCommand.addExtension(update);

		// Test just adding DS Data using the DS Data Interface
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		// --- Add DS Data ---
		theCommand = new EPPDomainUpdateCmd("example.com");
		theCommand.setTransId("ABC-12345");
		
		update = new EPPSecDNSExtUpdate();
		
		update.appendAddDsData(new EPPSecDNSExtDsData(12346,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"38EC35D5B3A34B44C39B"));
		
		theCommand.addExtension(update);

		// Test just adding DS Data
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		
		EPPCodecTst.printEnd("testDomainUpdate");
	} // End EPPSecDNSExtTst.testDomainUpdate()

	/**
	 * Test the generation of DS data from the Key data.  The steps of the test includes:<br>
	 * <ol>
	 * <li>Create test key for example.com using RSASHA1.  
	 * <li>Generate SHA1 DS for test key and assert the DS attributes.
	 * <li>Generate SHA256 DS for test key and assert the DS attributes.
	 * </ol>
	 */
	public void testDsDataGeneration() {

		EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
		keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
		keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
		keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
				+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
				+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
				+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
				+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");

			
		System.out.println("Source Key Data = [" + keyData + "]");

		// Generate the DS Data for SHA-1
		try {
			String domainName = "example.com";
			int digestType = EPPSecDNSExtDsData.SHA1_DIGEST_TYPE;

			EPPSecDNSExtDsData dsData = keyData
					.toDsData(domainName, digestType);
			
			Assert.assertEquals(34095, dsData.getKeyTag());
			Assert.assertEquals(EPPSecDNSAlgorithm.RSASHA1, dsData.getAlg());
			Assert.assertEquals(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, dsData.getDigestType());
			Assert.assertEquals("F65DDFF9F2CC042EC2FEE0914320D80FD2315963", dsData.getDigest());
			
			System.out.println("Generated DS Data for (domain = " + domainName
					+ ", digest type = " + digestType + ") = [" + dsData + "]");
		}
		catch (EPPCodecException ex) {
			Assert.fail("Exception generating DS data for key:" + ex);
		}

		// Generate the DS Data for SHA-256
		try {
			String domainName = "example.com";
			int digestType = EPPSecDNSExtDsData.SHA256_DIGEST_TYPE;

			EPPSecDNSExtDsData dsData = keyData
					.toDsData(domainName, digestType);
			
			Assert.assertEquals(34095, dsData.getKeyTag());
			Assert.assertEquals(EPPSecDNSAlgorithm.RSASHA1, dsData.getAlg());
			Assert.assertEquals(EPPSecDNSExtDsData.SHA256_DIGEST_TYPE, dsData.getDigestType());
			Assert.assertEquals("34B79BF4F0326560AA6D61490C9E65A775C530AD44BC7773162BF2E97BE649D4", dsData.getDigest());
			
			
			System.out.println("Generated DS Data for (domain = " + domainName
					+ ", digest type = " + digestType + ") = [" + dsData + "]");
		}
		catch (EPPCodecException ex) {
			Assert.fail("Exception generating DS data for key:" + ex);
		}
		
	}
	
	
	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code> with
	 * the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	} // End EPPSecDNSExtTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	} // End EPPSecDNSExtTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPSecDNSExtTst</code>.
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPSecDNSExtTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add each required Factory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addExtFactory(
					EPPSecDNSExtFactory.class.getName());
		}
		catch (EPPCodecException e) {
			Assert
					.fail("EPPCodecException adding EPPDomainMapFactory or EPPSecDNSExtFactory to EPPCodec: "
							+ e);
		}

		return suite;
	} // End EPPSecDNSExtTst.suite()

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * 
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run</li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.</li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            DOCUMENT ME!
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
				TestThread thread = new TestThread("EPPSecDNSExtTst Thread "
						+ i, EPPSecDNSExtTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSecDNSExtTst.suite());
		}
	} // End EPPSecDNSExtTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *            number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	} // End EPPSecDNSExtTst.setNumIterations(long)

}
