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
package com.verisign.epp.interfaces.secdnsext.v10;


// JUNIT Imports
import junit.framework.*;

// Log4j imports
import org.apache.log4j.*;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import com.verisign.epp.codec.domain.*;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;

import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSAlgorithm;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtCreate;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtDsData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtInfData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtKeyData;
import com.verisign.epp.codec.secdnsext.v10.EPPSecDNSExtUpdate;


// EPP Imports
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPDomain;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the <code>EPPDomain</code> class including the
 * secDNS:create and secDNS:update (add, chg, rem) command extensions,
 * as well as the secDNS:infData response extension.
 *
 * The unit test will
 * initialize a session with an EPP Server, will invoke <code>EPPDomain</code>
 * operations, and will end a session with an EPP Server. The configuration
 * file used by the unit test defaults to epp.config, but can be changed by
 * passing the file path as the first command line argument. The unit test can
 * be run in multiple threads by setting the "threads" system property. For
 * example, the unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPSecDNSDomainTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br><br><br>
 */
public class EPPSecDNSDomainTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance
	 * (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app =
		EPPApplicationSingle.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat =
		Logger.getLogger(
						 EPPSecDNSDomainTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Domain associated with test */
	private EPPDomain domain = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Current test iteration */
	private int iteration = 0;

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPSecDNSDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPSecDNSDomainTst(String name) {
		super(name);
	}

	// End EPPSecDNSDomainTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPSecDNSDomainTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPDomain
	 * interface.
	 */
	public void testDomain() {
		int    numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}

		for (
			 iteration = 0;
				 (numIterations == 0) || (iteration < numIterations);
				 iteration++) {
			printStart("EPPSecDNSDomainTst Test Suite");

			domainCreate();
			
			domainInfo();
			
			domainUpdateSecDNSUpdateAdd();
			
			domainUpdateSecDNSUpdateChg();
			
			domainUpdateSecDNSUpdateRem();
			
			printEnd("EPPSecDNSDomainTst Test Suite");
		}
	}

	// End EPPSecDNSDomainTst.testDomain()

	/**
	 * Unit test of <code>EPPDomain.sendInfo</code> 
	 * including secDNS:infData response extension
	 */
	public void domainInfo() {
		printStart("EPPSecDNSDomainTst domainInfo");

		EPPDomainInfoResp response;

		try {
			System.out.println("\ndomainInfo: Domain info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("domainInfo: Response = [" + response + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("domainInfo: name = " + response.getName());

			System.out.println("domainInfo: client id = "
							   + response.getClientId());

			System.out.println("domainInfo: created by = "
							   + response.getCreatedBy());

			System.out.println("domainInfo: create date = "
							   + response.getCreatedDate());

			System.out.println("domainInfo: expiration date = "
							   + response.getExpirationDate());

			//-- Output the secDNS:infData extension
			if (response.hasExtension(EPPSecDNSExtInfData.class)) {
	            EPPSecDNSExtInfData infData = response.getExtension(EPPSecDNSExtInfData.class).orElse(null);

	            
	            Collection dsDataVec = infData.getDsData();
	            EPPSecDNSExtDsData dsData = null;
	            if (dsDataVec == null) {
	            	System.out.println("domainInfo: secDNS:infData dsDataVec = "
							   + dsDataVec);
	            }
	            else {
	            	int i = 0;
	            	Iterator iter = dsDataVec.iterator();
	            	while (iter.hasNext()) {
	            		dsData = (EPPSecDNSExtDsData)iter.next();
	            		//System.out.println("domainInfo: secDNS:infData/dsData[" + i + "] = "
								   //+ dsData);
	            		System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyTag = "
								   + dsData.getKeyTag());
	            		System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/alg = "
								   + dsData.getAlg());
	            		System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/digestType = "
								   + dsData.getDigestType());
	            		System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/digest = "
								   + dsData.getDigest());
	            		System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/maxSigLife = "
								   + dsData.getMaxSigLife());
	            		
	            		EPPSecDNSExtKeyData keyData = dsData.getKeyData();
	            		if (keyData == null) {
	            			System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData = "
									   + keyData);
	            		}
	            		else {
	            			//System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData = "
									   //+ keyData);
	            			System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData/flags = "
									   + keyData.getFlags());
	            			System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData/protocol = "
									   + keyData.getProtocol());
	            			System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData/alg = "
									   + keyData.getAlg());
	            			System.out.println("domainInfo: secDNS:infData/dsData[" + i + "]/keyData/pubKey = "
									   + keyData.getPubKey());
	            		}
	            		
	            		i++;
	            		
	            	} // end while
	            }

			}
			else {
				System.out.println("domainInfo: no EPPSecDNSExtInfData extension");
			}

		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("EPPSecDNSDomainTst domainInfo");
	}

	// End EPPSecDNSDomainTst.domainInfo()

	/**
	 * Unit test of <code>EPPDomain.sendCreate</code>.
	 */
	public void domainCreate() {
		printStart("EPPSecDNSDomainTst domainInfo");
	try {
		System.out.println("\n----------------------------------------------------------------");

		EPPDomainCreateResp response;
		
		domain.setTransId("ABC-12345-XYZ");

		String theDomainName = this.makeDomainName();

		System.out.println("domainCreate: Create " + theDomainName + " with all optional attributes");

		domain.addDomainName(theDomainName);

		for (int i = 0; i <= 20; i++) {
			domain.addHostName(this.makeHostName(theDomainName));
		}

		// Is the contact mapping supported?
		if (
			EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			// Add domain contacts
			domain.addContact("SH0000", EPPDomain.CONTACT_ADMINISTRATIVE);

			domain.addContact("SH0000", EPPDomain.CONTACT_TECHNICAL);

			domain.addContact("SH0000", EPPDomain.CONTACT_BILLING);
		}

		domain.setPeriodLength(10);

		domain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

		domain.setAuthString("ClientX");

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
        domain.addExtension(create);
		//-- end secDNS Extension
        
		response = domain.sendCreate();

		//-- Output all of the response attributes
		System.out.println("domainCreate: Response = [" + response
						   + "]\n\n");

		//-- Output response attributes using accessors
		System.out.println("domainCreate: name = " + response.getName());

		System.out.println("domainCreate: expiration date = "
						   + response.getExpirationDate());
	}

	 catch (EPPCommandException e) {
		handleException(e);
	}

	}
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> with secDNS:update(secDNS:add) extension.
	 */
	public void domainUpdateSecDNSUpdateAdd() {
		printStart("domainUpdateSecDNSUpdateAdd");

		EPPResponse response;

		try {
			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

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
	        
	        // instantiate the secDNS:update object
	        EPPSecDNSExtUpdate update = new EPPSecDNSExtUpdate();
	        Vector dsDataVec = new Vector();
	        dsDataVec.add(dsData);
	        update.setAdd(dsDataVec);
	        update.appendAdd(dsData2);
	        
	        // set the secDNS:update in the command as an extension
	        domain.addExtension(update);
			//-- end secDNS Extension

			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainUpdateSecDNSUpdateAdd: Response = [" + response
							   + "]\n\n");

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult =
					(EPPResult) response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());

				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				}

				else {
					System.out.println("Command Failed ");
				}

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainUpdateSecDNSUpdateAdd");
	}

	// End EPPSecDNSDomainTst.domainUpdateSecDNSUpdateAdd()


	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> with secDNS:update(secDNS:chg) extension.
	 */
	public void domainUpdateSecDNSUpdateChg() {
		printStart("domainUpdateSecDNSUpdateChg");

		EPPResponse response;

		try {
			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

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
	        
	        // instantiate the secDNS:update object
	        EPPSecDNSExtUpdate update = new EPPSecDNSExtUpdate();
	        Vector dsDataVec = new Vector();
	        dsDataVec.add(dsData);
	        update.setChg(dsDataVec);
	        update.appendChg(dsData2);
	        
	        // set the secDNS:update in the command as an extension
	        domain.addExtension(update);
			//-- end secDNS Extension

			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainUpdateSecDNSUpdateChg: Response = [" + response
							   + "]\n\n");

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult =
					(EPPResult) response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());

				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				}

				else {
					System.out.println("Command Failed ");
				}

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainUpdateSecDNSUpdateChg");
	}

	// End EPPSecDNSDomainTst.domainUpdateSecDNSUpdateChg()

	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> with secDNS:update(secDNS:rem) extension.
	 */
	public void domainUpdateSecDNSUpdateRem() {
		printStart("domainUpdateSecDNSUpdateRem");

		EPPResponse response;

		try {
			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

			//-- Add secDNS Extension
			// instantiate a secDNS:keyTag
			Integer keyTag = new Integer(34095);
			EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData(keyTag);
			
			// instantiate another secDNS:keyTag 
			Integer keyTag2 = new Integer(10563);
			EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(keyTag2);
       
	        // instantiate the secDNS:update object
	        EPPSecDNSExtUpdate update = new EPPSecDNSExtUpdate();
	        Vector keyTagVec = new Vector();
	        keyTagVec.add(keyTag);
	        keyTagVec.add(keyTag2);
	        update.setRem(keyTagVec);
	        //or alternatively:
	        	update.appendRem(keyTag);  // Integer secDNS:keyTag
	        	update.appendRem(keyTag2); // Integer secDNS:keyTag
	        //or alternatively:
	        	update.appendRem(dsData);  // convenience EPPSecDNSExtDsData secDNS:keyTag
	        	update.appendRem(dsData2.getKeyTag()); // int EPPSecDNSExtDsData secDNS:keyTag
	        
	        // set the secDNS:update in the command as an extension
	        domain.addExtension(update);
			//-- end secDNS Extension

			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainUpdateSecDNSUpdateRem: Response = [" + response
							   + "]\n\n");

			/**
			 * Result Set
			 */
			for (int i = 0; i < response.getResults().size(); i++) {
				EPPResult myResult =
					(EPPResult) response.getResults().elementAt(i);

				System.out.println("Result Code    : " + myResult.getCode());

				System.out.println("Result Message : " + myResult.getMessage());

				System.out.println("Result Lang    : " + myResult.getLang());

				if (myResult.isSuccess()) {
					System.out.println("Command Passed ");
				}

				else {
					System.out.println("Command Failed ");
				}

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainUpdateSecDNSUpdateRem");
	}

	// End EPPSecDNSDomainTst.domainUpdateSecDNSUpdateRem()


	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute
	 * is initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession() {
		printStart("initSession");

		// Set attributes for initSession
		session.setTransId("ABC-12345-XYZ");

		session.setVersion("1.0");

		session.setLang("en");

		// Initialize the session
		try {
			session.initSession();
		}

		 catch (EPPCommandException e) {
			EPPResponse response = session.getResponse();

			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			}
			else {
				e.printStackTrace();

				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	}

	// End EPPSecDNSDomainTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the
	 * EPP Server will be terminated.
	 */
	private void endSession() {
		printStart("endSession");

		session.setTransId("ABC-12345-XYZ");

		// End the session
		try {
			session.endSession();
		}

		 catch (EPPCommandException e) {
			EPPResponse response = session.getResponse();

			// Is a server specified error?
			if ((response != null) && (!response.isSuccess())) {
				Assert.fail("Server Error : " + response);
			}

			else // Other error
			 {
				e.printStackTrace();

				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("endSession");
	}

	// End EPPSecDNSDomainTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			String theSessionClassName = System.getProperty("EPP.SessionClass");

			if (theSessionClassName != null) {
				try {
					Class theSessionClass = Class.forName(theSessionClassName);

					if (!EPPSession.class.isAssignableFrom((theSessionClass))) {
						Assert.fail(theSessionClassName
									+ " is not a subclass of EPPSession");
					}

					session = (EPPSession) theSessionClass.newInstance();
				}
				 catch (Exception ex) {
					Assert.fail("Exception instantiating EPP.SessionClass value "
								+ theSessionClassName + ": " + ex);
				}
			}
			else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
		}

		 catch (Exception e) {
			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		//System.out.println("out init");
		domain = new EPPDomain(session);
	}

	// End EPPSecDNSDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPSecDNSDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the test
	 * associated with <code>EPPSecDNSDomainTst</code>.
	 *
	 * @return the test associated with <code>EPPSecDNSDomainTst</code>.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPSecDNSDomainTst.class);

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

		return suite;
	}

	// End EPPSecDNSDomainTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by
	 * a server error, "Server Error :&lt;Response XML&gt;" will be specified.
	 * If the exception was caused by a general algorithm error, "General
	 * Error :&lt;Exception Description&gt;" will be specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(Exception aException) {
		EPPResponse response = session.getResponse();

		aException.printStackTrace();

		// Is a server specified error?
		if ((response != null) && (!response.isSuccess())) {
			Assert.fail("Server Error : " + response);
		}

		else {
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPSecDNSDomainTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options:
	 * <br>
	 *
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off
	 * (<code>false</code>). If validate is not specified, validation will be
	 * off.
	 * </li>
	 * </ul>
	 *
	 *
	 * @param args program arguments 
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

			// Run test suite in multiple threads?
		}

		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread =
					new TestThread(
								   "EPPSessionTst Thread " + i,
								   EPPSecDNSDomainTst.suite());

				thread.start();
			}
		}

		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPSecDNSDomainTst.suite());
		}

		try {
			app.endApplication();
		}

		 catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPSecDNSDomainTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name
	 * 
	 *
	 * @return Unique domain name <code>String</code>
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread().getName()
						  + String.valueOf(tm + rd.nextInt(12)).substring(5)
						  + ".tv");
	}

	/**
	 * Makes a unique IP address based off of the current time.
	 *
	 * @return Unique IP address <code>String</code>
	 */
	public String makeIP() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10)
						  + "."
						  + String.valueOf(tm + rd.nextInt(50)).substring(10));
	}

	/**
	 * Makes a unique host name for a domain using the current time.
	 *
	 * @param newDomainName a domain name on which to create a child host name
	 *
	 * @return Unique host name <code>String</code>
	 */
	public String makeHostName(String newDomainName) {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
						  + "." + newDomainName);
	}

	/**
	 * Makes a unique contact name using the current time.
	 *
	 * @return Unique contact name <code>String</code>
	 */
	public String makeContactName() {
		long tm = System.currentTimeMillis();

		return new String("Con"
						  + String.valueOf(tm + rd.nextInt(5)).substring(7));
	}

	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	private void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
							 + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration "
					 + iteration + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);

		System.out.println("****************************************************************\n");
	}

	// End EPPSecDNSDomainTst.testStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	private void printEnd(String aTest) {
		System.out.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
							 + iteration + ": ");

			cat.info(Thread.currentThread().getName() + ", iteration "
					 + iteration + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);

		System.out.println("\n");
	}

	// End EPPSecDNSDomainTst.testEnd(String)

	/**
	 * Print message
	 *
	 * @param aMsg message to print
	 */
	private void printMsg(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.println(Thread.currentThread().getName()
							   + ", iteration " + iteration + ": " + aMsg);

			cat.info(Thread.currentThread().getName() + ", iteration "
					 + iteration + ": " + aMsg);
		}

		else {
			System.out.println(aMsg);

			cat.info(aMsg);
		}
	}

	// End EPPSecDNSDomainTst.printMsg(String)

	/**
	 * Print error message
	 *
	 * @param aMsg errpr message to print
	 */
	private void printError(String aMsg) {
		if (Thread.currentThread() instanceof TestThread) {
			System.err.println(Thread.currentThread().getName()
							   + ", iteration " + iteration + ": " + aMsg);

			cat.error(Thread.currentThread().getName() + ", iteration "
					  + iteration + ": " + aMsg);
		}

		else {
			System.err.println(aMsg);

			cat.error(aMsg);
		}
	}

	// End EPPSecDNSDomainTst.printError(String)
}


// End class EPPSecDNSDomainTst
