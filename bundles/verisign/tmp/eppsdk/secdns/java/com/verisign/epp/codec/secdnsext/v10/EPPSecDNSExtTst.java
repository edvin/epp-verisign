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
package com.verisign.epp.codec.secdnsext.v10;


// JUNIT Imports
import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.w3c.dom.Document;

import com.verisign.epp.codec.domain.EPPDomainAddRemove;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainPeriod;
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
	 * @param name DOCUMENT ME!
	 */
	public EPPSecDNSExtTst(String name) {
		super(name);
	}

	// End EPPSecDNSExtTst(String)



 

	
	/**
	 * Unit test of <code>testDomainInfoRespWithSecDNSExt</code>. The response to
	 * <code>testDomainInfoRespWithSecDNSExt</code> is <code>EPPDomainInfoResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */

	public void testDomainInfoRespWithSecDNSExt() {

        EPPCodecTst.printStart("testDomainInfoRespWithSecDNSExt");

        // trans id of the response
        EPPTransId respTransId =
            new EPPTransId("54321-CLI", "54321-SER");

        Vector statuses = new Vector();
        statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

        // EPPDomainInfo Response
        EPPDomainInfoResp theResponse =
            new EPPDomainInfoResp(
            respTransId, "EXAMPLE1-VRSN", "example.tv",
            "ClientX", statuses, "ClientY", new Date(),
                                  new EPPAuthInfo("2fooBAR"));


        // Test with all EPPDomainInfoResp attributes set.
        theResponse.setRegistrant("JD1234-VRSN");

        Vector servers = new Vector();
        servers.addElement("ns1.example.tv");
        servers.addElement("ns2.example.tv");
        theResponse.setNses(servers);

        Vector hosts = new Vector();
        hosts.addElement("ns1.example.tv");
        hosts.addElement("ns2.example.tv");
        theResponse.setHosts(hosts);

        // Is contacts supported?
        if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
            Vector contacts = new Vector();
            contacts.addElement(new EPPDomainContact(
                                                     "SH8013-VRSN",
                                                     EPPDomainContact.TYPE_ADMINISTRATIVE));
            contacts.addElement(new EPPDomainContact(
                                                     "SH8013-VRSN",
                                                     EPPDomainContact.TYPE_TECHNICAL));
            theResponse.setContacts(contacts);
        }

        theResponse.setLastUpdatedBy("ClientX");
        theResponse.setLastUpdatedDate(new Date());
        theResponse.setLastTransferDate(new Date());

        respTransId = new EPPTransId("54321-CLI", "54321-SER");;
        theResponse.setTransId(respTransId);
        theResponse.setRoid("EXAMPLE1-VRSN");

        theResponse.setResult(EPPResult.SUCCESS);

		// instantiate a secDNS:keyData object
        EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
        keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
        keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
        keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj" +
                    "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs" +
                    "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2" +
                    "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO" +
                    "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
        
        // instantiate another secDNS:keyData object
        EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
        		EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
        		EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
        		EPPSecDNSAlgorithm.RSASHA1,
        		"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB" +
        			"5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142" +
                    "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H" +
                    "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge" +
                    "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");
        
        // instantiate a secDNS:dsData object
        EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
        dsData.setKeyTag(34095);
        dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
        dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
        dsData.setMaxSigLife(604800);
        dsData.setKeyData(keyData);
        
        // instantiate another secDNS:dsData object
        EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(
        		10563, 
        		EPPSecDNSAlgorithm.RSASHA1, 
        		EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
        		"9C20674BFF957211D129B0DFE9410AF753559D4B",
        		604800,
        		keyData2);
        
        // instantiate the secDNS:infData object
        EPPSecDNSExtInfData infData = new EPPSecDNSExtInfData();
        Vector dsDataVec = new Vector();
        dsDataVec.add(dsData);
        infData.setDsData(dsDataVec);
        infData.appendDsData(dsData2);
        
        // set the secDNS:infData in the response as an extension
        theResponse.addExtension(infData);
        
 		EPPEncodeDecodeStats responseStats;
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainInfoRespWithSecDNSExt");
	}

	// End EPPSecDNSExtTst.testDomainInfoRespWithSecDNSExt()

	/**
	 * Unit test of <code>EPPDomainCreateCmd</code> including secDNS extensions. The response to
	 * <code>EPPDomainCreateCmd</code> is <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainCreate() {
		EPPDomainCreateCmd   theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainCreate");

		// Create a domain with all of the attributes accept for contacts
		Vector servers = new Vector();
		servers.addElement("ns1.example.tv");
		servers.addElement("ns2.example.tv");

		Vector contacts = null;

		// Is contacts supported?
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			contacts = new Vector();
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact(
													 "SH8013-VRSN",
													 EPPDomainContact.TYPE_TECHNICAL));
		}
		
		theCommand =
			new EPPDomainCreateCmd(
								   "ABC-12345-XYZ", "example.tv", servers,
								   contacts, new EPPDomainPeriod(2),
								   new EPPAuthInfo("2fooBAR"));
		
		//-- Add secDNS Extension
		// instantiate a secDNS:keyData object
        EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
        keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
        keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
        keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj" +
                    "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs" +
                    "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2" +
                    "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO" +
                    "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
        
        // instantiate another secDNS:keyData object
        EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
        		EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
        		EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
        		EPPSecDNSAlgorithm.RSASHA1,
        		"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB" +
        			"5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142" +
                    "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H" +
                    "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge" +
                    "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");
        
        // instantiate a secDNS:dsData object
        EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
        dsData.setKeyTag(34095);
        dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
        dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
        dsData.setMaxSigLife(604800);
        dsData.setKeyData(keyData);
        
        // instantiate another secDNS:dsData object
        EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(
        		10563, 
        		EPPSecDNSAlgorithm.RSASHA1, 
        		EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
        		"9C20674BFF957211D129B0DFE9410AF753559D4B",
        		604800,
        		keyData2);
        
        // instantiate the secDNS:create object
        EPPSecDNSExtCreate create = new EPPSecDNSExtCreate();
        Vector dsDataVec = new Vector();
        dsDataVec.add(dsData);
        create.setDsData(dsDataVec);
        create.appendDsData(dsData2);
        
        // set the secDNS:create in the command as an extension
        theCommand.addExtension(create);
		
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testDomainCreate");
	}

	// End EPPDomainTst.testDomainCreate()

	/**
	 * Unit test of <code>EPPDomainUpdateCmd</code> including secDNS extensions. The response to
	 * <code>EPPDomainUpdateCmd</code> is <code>EPPResponse</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */
	public void testDomainUpdate() {
		EPPDomainUpdateCmd   theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainUpdate");


		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-1");


		// instantiate a secDNS:keyData object
        EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
        keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
        keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
        keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj" +
                    "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs" +
                    "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2" +
                    "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO" +
                    "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
        
        // instantiate another secDNS:keyData object
        EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
        		EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
        		EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
        		EPPSecDNSAlgorithm.RSASHA1,
        		"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB" +
        			"5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142" +
                    "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H" +
                    "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge" +
                    "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==");
        
        // instantiate a secDNS:dsData object
        EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
        dsData.setKeyTag(34095);
        dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
        dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
        dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");
        dsData.setMaxSigLife(604800);
        dsData.setKeyData(keyData);
        
        // instantiate another secDNS:dsData object
        EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(
        		10563, 
        		EPPSecDNSAlgorithm.RSASHA1, 
        		EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
        		"9C20674BFF957211D129B0DFE9410AF753559D4B",
        		604800,
        		keyData2);
        
        // instantiate the add DS Data
        Vector addDsDataVec = new Vector();
        addDsDataVec.add(dsData);
        addDsDataVec.add(dsData2);
         
        //instantiate the chg DS Data
        Vector chgDsDataVec = new Vector();
        chgDsDataVec.add(dsData);
        
		// instantiate a secDNS:keyTag
		Integer keyTag = new Integer(34095);
		Integer keyTag2 = new Integer(10563);
		
        // instantiate the rem DS Key Tag List
        Vector remKeyTagVec = new Vector();
        remKeyTagVec.add(keyTag);
        remKeyTagVec.add(keyTag2);
        
        // instantiate the secDNS:update object for add
        EPPSecDNSExtUpdate update = new EPPSecDNSExtUpdate();
        update.setAdd(addDsDataVec);
        
        // Set urgent flag
        update.setUrgent(true);
                
        // set the secDNS:update in the command as an extension
        theCommand.addExtension(update);
 		
		// test the secDNS:update/secDNS:add
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		
		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-2");
		
	    // instantiate the secDNS:update object for chg
        update = new EPPSecDNSExtUpdate();
        update.setChg(chgDsDataVec);
        
        // Explicitly set urgent flag to false
        update.setUrgent(false);
        
        // set the secDNS:update in the command as an extension
        theCommand.addExtension(update);
		
        // test the secDNS:update/secDNS:chg
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-3");
		
	    // instantiate the secDNS:update object for rem
        update = new EPPSecDNSExtUpdate();
        update.setRem(remKeyTagVec);
        //or alternatively:
        	update.appendRem(keyTag);  // Integer secDNS:keyTag
        	update.appendRem(keyTag2); // Integer secDNS:keyTag
        //or alternatively:
        	update.appendRem(dsData);  // convenience EPPSecDNSExtDsData secDNS:keyTag
        	update.appendRem(dsData2.getKeyTag()); // (int) EPPSecDNSExtDsData secDNS:keyTag
        
        // set the secDNS:update in the command as an extension
        theCommand.addExtension(update);
 		
        // test the secDNS:update/secDNS:rem
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		
		// instantiate the secDNS:update object for add&rem
		System.out.println( "\ndomainUpdate: Negative Test - DNSSEC Update with add & rem" );
		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-3");
		
		update = new EPPSecDNSExtUpdate();
		update.setAdd( addDsDataVec );
		update.setRem( remKeyTagVec );
		theCommand.addExtension(update);
		
		try {
			// test the secDNS:update with secDNS:add&secDNS:rem
			Document doc = EPPCodec.getInstance().encode( theCommand );
		}
		catch ( EPPEncodeException ex ) {
			Assert.assertEquals("EPPSecDNSExtUpdate invalid state: com.verisign.epp.codec.gen.EPPCodecException: Only one add, chg, or rem is allowed" , ex.getMessage() );
		}
		
		
		// instantiate the secDNS:update object for chg&rem
		System.out.println( "\ndomainUpdate: Negative Test - DNSSEC Update with chg & rem" );
		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-4");
		
		update = new EPPSecDNSExtUpdate();
		update.setChg( chgDsDataVec );
		update.setRem( remKeyTagVec );
		theCommand.addExtension(update);
		
		try {
			// test the secDNS:update/secDNS:chg&secDNS:rem
			Document doc = EPPCodec.getInstance().encode( theCommand );
		}
		catch ( EPPEncodeException ex ) {
			Assert.assertEquals("EPPSecDNSExtUpdate invalid state: com.verisign.epp.codec.gen.EPPCodecException: Only one add, chg, or rem is allowed" , ex.getMessage() );
		}
		
		// instantiate the secDNS:update object for add&chg
		System.out.println( "\ndomainUpdate: Negative Test - DNSSEC Update with add & chg" );
		theCommand =
			new EPPDomainUpdateCmd("example.tv");
		theCommand.setTransId("ABC-12345-XYZ-5");
		
		update = new EPPSecDNSExtUpdate();
		update.setAdd( addDsDataVec );
		update.setChg( chgDsDataVec );
		theCommand.addExtension(update);
		
		try {
			// test the secDNS:update/secDNS:add&secDNS:chg
			Document doc = EPPCodec.getInstance().encode( theCommand );
		}
		catch ( EPPEncodeException ex ) {
			Assert.assertEquals("EPPSecDNSExtUpdate invalid state: com.verisign.epp.codec.gen.EPPCodecException: Only one add, chg, or rem is allowed" , ex.getMessage() );
		}
		
		// instantiate the secDNS:update object for add&rem&chg
		System.out.println( "\ndomainUpdate: Negative Test - DNSSEC Update with add & chg & rem" );
		update = new EPPSecDNSExtUpdate();
		update.setAdd( addDsDataVec );
		update.setChg( chgDsDataVec );
		update.setRem( remKeyTagVec );
		theCommand.addExtension(update);
		
		try {
			// test the secDNS:update/secDNS:add&secDNS:chg&secDNS:rem
			Document doc = EPPCodec.getInstance().encode( theCommand );
		}
		catch ( EPPEncodeException ex ) {
			Assert.assertEquals("EPPSecDNSExtUpdate invalid state: com.verisign.epp.codec.gen.EPPCodecException: Only one add, chg, or rem is allowed" , ex.getMessage() );
		}
		
		EPPCodecTst.printEnd("testDomainUpdate");
	}

	// End EPPDomainTst.testDomainUpdate()
	
	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPSecDNSExtTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPSecDNSExtTst.tearDown();

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
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.domain.EPPDomainMapFactory");
            EPPFactory.getInstance().addExtFactory(EPPSecDNSExtFactory.class.getName());
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDomainMapFactory or EPPSecDNSExtFactory to EPPCodec: "
						+ e);
		}


		return suite;
	}

	// End EPPSecDNSExtTst.suite()

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
								   "EPPSecDNSExtTst Thread " + i,
								   EPPSecDNSExtTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSecDNSExtTst.suite());
		}
	}

	// End EPPSecDNSExtTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPSecDNSExtTst.setNumIterations(long)

}
