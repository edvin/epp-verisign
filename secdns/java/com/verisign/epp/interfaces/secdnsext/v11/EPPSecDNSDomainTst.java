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
package com.verisign.epp.interfaces.secdnsext.v11;


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
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;

import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSAlgorithm;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtCreate;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtInfData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtKeyData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtUpdate;


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
	 * Unit test of <code>EPPDomain.sendInfo</code> 
	 * including secDNS:infData response extension using a defined set 
	 * of domain names returning different responses by the 
	 * EPP Stub Server including:<br>
	 * <ol>
	 * <li><code>key-data-interface.com</code> - Return response with two keys
	 * using the Key Data Interface.
	 * <li><code>ds-data-interface-with-key.com</code> - Return response with
	 * two DS including the associated keys using the DS Data Interface.
	 * <li><code>ds-data-interface-with-maxsiglife.com</code> - Return response
	 * with two DS using the DS Data Interface and with the
	 * &lt;secDNS:maxSigLife&gt; element.
	 * <li><code>example.com</code> - Return response with two DS using DS Data Interface.
	 * </ol>
	 */
	public void testDomainInfo() {
		printStart("EPPSecDNSDomainTst testDomainInfo");

		EPPDomainInfoResp response;

		try {
			//-- Domain info of key-data-interface.com
			System.out.println("\ntestDomainInfo: Domain info of key-data-interface.com");

			domain.addDomainName("key-data-interface.com");

			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("testDomainInfo: Response = [" + response + "]\n\n");

			//-- Output the secDNS:infData extension
			if (response.hasExtension(EPPSecDNSExtInfData.class)) {
	            EPPSecDNSExtInfData infData =(EPPSecDNSExtInfData)
                response.getExtension(EPPSecDNSExtInfData.class).orElse(null);
	            
	            Assert.assertEquals(2, infData.getKeyData().size());
	            
	            Iterator keyIter = infData.getKeyData().iterator();
	            
	            while (keyIter.hasNext()) {
	            	EPPSecDNSExtKeyData currKey = (EPPSecDNSExtKeyData) keyIter.next();
	            	
	            	System.out.println("testDomainInfo: keyData = " + currKey);
	            }
			}
			else {
				Assert.fail("EPPSecDNSExtInfData extension not included for domain info of key-data-interface.com");
			}
			
			//-- Domain info of ds-data-interface-with-key.com
			System.out.println("\ntestDomainInfo: Domain info of ds-data-interface-with-key.com");

			domain.addDomainName("ds-data-interface-with-key.com");

			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("testDomainInfo: Response = [" + response + "]\n\n");

			//-- Output the secDNS:infData extension
			if (response.hasExtension(EPPSecDNSExtInfData.class)) {
	            EPPSecDNSExtInfData infData =(EPPSecDNSExtInfData)
                response.getExtension(EPPSecDNSExtInfData.class).orElse(null);
	            
	            Assert.assertEquals(2, infData.getDsData().size());
	            
	            Iterator dsIter = infData.getDsData().iterator();
	            
	            while (dsIter.hasNext()) {
	            	EPPSecDNSExtDsData currKey = (EPPSecDNSExtDsData) dsIter.next();
	            	
	            	System.out.println("testDomainInfo: dsData = " + currKey);
	            }
			}
			else {
				Assert.fail("EPPSecDNSExtInfData extension not included for domain info of ds-data-interface-with-key.com");
			}

			
			//-- Domain info of ds-data-interface-with-maxsiglife.com
			System.out.println("\ntestDomainInfo: Domain info of ds-data-interface-with-maxsiglife.com");

			domain.addDomainName("ds-data-interface-with-maxsiglife.com");

			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("testDomainInfo: Response = [" + response + "]\n\n");

			//-- Output the secDNS:infData extension
			if (response.hasExtension(EPPSecDNSExtInfData.class)) {
	            EPPSecDNSExtInfData infData = response.getExtension(EPPSecDNSExtInfData.class).orElse(null);
	            
	            Assert.assertEquals(2, infData.getDsData().size());
	            
	            Iterator dsIter = infData.getDsData().iterator();
	            
	            while (dsIter.hasNext()) {
	            	EPPSecDNSExtDsData currKey = (EPPSecDNSExtDsData) dsIter.next();
	            	
	            	System.out.println("testDomainInfo: dsData = " + currKey);
	            }
	            
	            System.out.println("testDomainInfo: maxSigLife = " + infData.getMaxSigLife());
			}
			else {
				Assert.fail("EPPSecDNSExtInfData extension not included for domain info of ds-data-interface-with-key.com");
			}
			

		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("EPPSecDNSDomainTst testDomainInfo");
	}


	/**
	 * Unit test of <code>EPPDomain.sendCreate</code> for secDNS 1.1 using the DS Data Interface.  
	 * The VeriSign servers only support the DS Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Create for a Secure Delegation example.com using the DS Data Interface with one DS.
	 * <li>Create for a Secure Delegation example.com using the DS Data Interface with two DS.
	 * <li>Create for a Secure Delegation example.com using the DS Data Interface with OPTIONAL key data.
	 * </ol>
	 */
	public void testCreateDsDataInterface() {
		printStart("EPPSecDNSDomainTst testCreateDsDataInterface");
		try {
			EPPDomainCreateResp response;

			// -- Using the DS Data Interface with one DS
			System.out
					.println("testCreateDsDataInterface(1): Create for a Secure Delegation example.com using the DS Data Interface with one DS");

			domain.addDomainName("testCreateDsDataInterface1.com");
			domain.setPeriodLength(2);
			domain.addHostName("ns1.example.com");
			domain.addHostName("ns1.example.com");

			// Is the contact mapping supported?
			if (EPPFactory.getInstance().hasService(
					EPPDomainMapFactory.NS_CONTACT)) {
				domain.setRegistrant("jd1234");
				domain.addContact("sh8013", EPPDomain.CONTACT_ADMINISTRATIVE);
				domain.addContact("sh8013", EPPDomain.CONTACT_TECHNICAL);
			}
			domain.setAuthString("2fooBAR");

			// Add DS
			EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData(12345,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
			EPPSecDNSExtCreate secDNSExt = new EPPSecDNSExtCreate();
			secDNSExt.setMaxSigLife(604800);
			secDNSExt.appendDsData(dsData);
			domain.addExtension(secDNSExt);

			response = domain.sendCreate();
			System.out.println("testCreateDsDataInterface(1): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());

			
			// -- Using the DS Data Interface with two DS
			System.out
					.println("testCreateDsDataInterface(2): Create for a Secure Delegation example.com using the DS Data Interface with two DS");

			domain.addDomainName("testCreateDsDataInterface2.com");
			domain.setPeriodLength(2);
			domain.addHostName("ns1.example.com");
			domain.addHostName("ns1.example.com");

			// Is the contact mapping supported?
			if (EPPFactory.getInstance().hasService(
					EPPDomainMapFactory.NS_CONTACT)) {
				domain.setRegistrant("jd1234");
				domain.addContact("sh8013", EPPDomain.CONTACT_ADMINISTRATIVE);
				domain.addContact("sh8013", EPPDomain.CONTACT_TECHNICAL);
			}
			domain.setAuthString("2fooBAR");

			// Add DS
			dsData = new EPPSecDNSExtDsData(12345,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
			secDNSExt = new EPPSecDNSExtCreate();
			secDNSExt.setMaxSigLife(604800);
			secDNSExt.appendDsData(new EPPSecDNSExtDsData(12345,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC"));
			secDNSExt.appendDsData(new EPPSecDNSExtDsData(12346,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
			domain.addExtension(secDNSExt);

			response = domain.sendCreate();
			System.out.println("testCreateDsDataInterface(2): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
			
			
			// -- Using the DS Data Interface with OPTIONAL key data
			System.out
					.println("testCreateDsDataInterface(3): Create for a Secure Delegation example.com using the DS Data Interface with OPTIONAL key data");

			domain.addDomainName("testCreateDsDataInterface3.com");
			domain.setPeriodLength(2);
			domain.addHostName("ns1.example.com");
			domain.addHostName("ns1.example.com");

			// Is the contact mapping supported?
			if (EPPFactory.getInstance().hasService(
					EPPDomainMapFactory.NS_CONTACT)) {
				domain.setRegistrant("jd1234");
				domain.addContact("sh8013", EPPDomain.CONTACT_ADMINISTRATIVE);
				domain.addContact("sh8013", EPPDomain.CONTACT_TECHNICAL);
			}
			domain.setAuthString("2fooBAR");

			// Key associated with DS
			EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
			keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
			keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
			keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
			keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
					+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
					+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
					+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
					+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
			
			try {
				dsData = keyData.toDsData("testCreateDsDataInterface.com",
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
			}
			catch (EPPCodecException e) {
				handleException(e);
			}
			
			// Add OPTIONAL keyData to generated dsData
			dsData.setKeyData(keyData);
			
			
			secDNSExt = new EPPSecDNSExtCreate();
			secDNSExt.setMaxSigLife(604800);
			secDNSExt.appendDsData(dsData);
			domain.addExtension(secDNSExt);

			response = domain.sendCreate();
			System.out.println("testCreateDsDataInterface(3): Response = ["
					+ response + "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

	}
	
	/**
	 * Unit test of <code>EPPDomain.sendCreate</code> for secDNS 1.1 using the Key Data Interface.  
	 * The VeriSign servers dot NOT support the Key Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Create for a Secure Delegation example.com using the Key Data Interface with one key.
	 * <li>Create for a Secure Delegation example.com using the Key Data Interface with two keys.
	 * </ol>
	 */
	public void testCreateKeyDataInterface() {
		printStart("EPPSecDNSDomainTst testCreateKeyDataInterface");
		try {
			EPPDomainCreateResp response;


			// -- Using the Key Data Interface with one key
			System.out
					.println("testCreateKeyDataInterface(1): Create for a Secure Delegation example.com using the Key Data Interface with one key");

			domain.addDomainName("example.com");
			domain.setPeriodLength(2);
			domain.addHostName("ns1.example.com");
			domain.addHostName("ns1.example.com");

			// Is the contact mapping supported?
			if (EPPFactory.getInstance().hasService(
					EPPDomainMapFactory.NS_CONTACT)) {
				domain.setRegistrant("jd1234");
				domain.addContact("sh8013", EPPDomain.CONTACT_ADMINISTRATIVE);
				domain.addContact("sh8013", EPPDomain.CONTACT_TECHNICAL);
			}
			domain.setAuthString("2fooBAR");

			EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
			keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
			keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
			keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
			keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
					+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
					+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
					+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
					+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
			
			EPPSecDNSExtCreate secDNSExt = new EPPSecDNSExtCreate();
			secDNSExt.setMaxSigLife(604800);
			secDNSExt.appendKeyData(keyData);
			domain.addExtension(secDNSExt);

			response = domain.sendCreate();
			System.out.println("testCreateKeyDataInterface(1): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
			
			// -- Using the Key Data Interface with two keys
			System.out
					.println("testCreateKeyDataInterface(2): Create for a Secure Delegation example.com using the Key Data Interface with two keys");

			domain.addDomainName("example.com");

			// Is the contact mapping supported?
			if (EPPFactory.getInstance().hasService(
					EPPDomainMapFactory.NS_CONTACT)) {
				domain.setRegistrant("jd1234");
				domain.addContact("sh8013", EPPDomain.CONTACT_ADMINISTRATIVE);
				domain.addContact("sh8013", EPPDomain.CONTACT_TECHNICAL);
			}
			domain.setAuthString("2fooBAR");
		
			
			EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData();
			keyData2.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
			keyData2.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
			keyData2.setAlg(EPPSecDNSAlgorithm.RSAMD5);
			keyData2.setPubKey("AQPJ////4Q==");
			
			secDNSExt = new EPPSecDNSExtCreate();
			secDNSExt.appendKeyData(keyData);
			secDNSExt.appendKeyData(keyData2);
			domain.addExtension(secDNSExt);

			response = domain.sendCreate();
			System.out.println("testCreateKeyDataInterface(2): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(response.getResult().getCode(),
					EPPResult.SUCCESS);
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

	}
	

	/**
	 * Unit test of <code>EPPDomain.sendCreate</code> for secDNS 1.1 with negative cases.
	 * These tests will only work against the Stub Server since specific domains key 
	 * off server-side behavior.
	 * The following negative tests will be executed:<br>
	 * <ol>
	 * <li>Passing DS data to server that only supports the Key Data Interface with the domain <code>key-data-interface.com</code>.
	 * <li>Passing Key data to server that only supports the DS Data Interface with the domain <code>ds-data-interface.com</code>.
	 * <li>Setting maxSigLife for a server that does not support maxSigLife with the domain <code>maxsiglife-not-suported.com</code>.
	 * </ol>
	 */
	public void testCreateNegativeTests() {
		printStart("EPPSecDNSDomainTst testCreateNegativeTests");
		
		EPPDomainCreateResp response;

		// -- Passing DS data to server that only supports the Key Data Interface
		System.out
				.println("testCreateNegativeTests(1): Passing DS data to server that only supports the Key Data Interface with domain key-data-interface.com");

		domain.addDomainName("key-data-interface.com");
		domain.setAuthString("2fooBAR");

		// Add DS
		EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData(12345,
				EPPSecDNSAlgorithm.DSA, EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
				"49FD46E6C4B45C55D4AC");
		EPPSecDNSExtCreate secDNSExt = new EPPSecDNSExtCreate();
		secDNSExt.appendDsData(dsData);
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendCreate();
			Assert
					.fail("Passing DS data to server that only supports the Key Data Interface was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testCreateNegativeTests(1): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.PARAM_VALUE_POLICY_ERROR, ex
					.getResponse().getResult().getCode());
		}

		// -- Passing Key data to server that only supports the DS Data
		// Interface
		System.out
				.println("testCreateNegativeTests(2): Passing Key data to server that only supports the DS Data Interface with domain ds-data-interface.com");

		domain.addDomainName("ds-data-interface.com");
		domain.setAuthString("2fooBAR");

		EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
		keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
		keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
		keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
				+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
				+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
				+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
				+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");

		secDNSExt = new EPPSecDNSExtCreate();
		secDNSExt.appendKeyData(keyData);
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendCreate();
			Assert
					.fail("Passing Key data to server that only supports the DS Data Interface was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testCreateNegativeTests(2): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(ex.getResponse().getResult().getCode(),
					EPPResult.PARAM_VALUE_POLICY_ERROR);
		}

		// -- Setting maxSigLife for a server that does not support maxSigLife
		System.out
				.println("testCreateNegativeTests(3): Setting maxSigLife for a server that does not support maxSigLife with domain maxsiglife-not-suported.com");

		domain.addDomainName("maxsiglife-not-supported.com");
		domain.setAuthString("2fooBAR");

		// Add DS
		dsData = new EPPSecDNSExtDsData(12345, EPPSecDNSAlgorithm.DSA,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
		secDNSExt = new EPPSecDNSExtCreate();
		secDNSExt.setMaxSigLife(604800);
		secDNSExt.appendDsData(dsData);
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendCreate();
			Assert
					.fail("Setting maxSigLife for a server that does not support maxSigLife was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testCreateNegativeTests(3): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.UNIMPLEMENTED_OPTION, ex
					.getResponse().getResult().getCode());
		}

	}
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> for secDNS 1.1 using the DS Data Interface.  
	 * The VeriSign servers only support the DS Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Adding and Removing DS Data using the DS Data Interface.
	 * <li>Removing DS Data with &lt;secDNS:dsData&gt; using the DS Data Interface.
	 * <li>Remove all DS and Key Data using &lt;secDNS:rem&gt; with &lt;secDNS:all&gt;.
	 * <li>Replacing all DS Data using the DS Data Interface.
	 * <li>Update the maxSigLife.
	 * </ol>
	 */
	public void testUpdateDsDataInterface() {
		printStart("testUpdateDsDataInterface");
		
		try {
			EPPResponse response;

			// -- Using the DS Data Interface with one DS
			System.out
					.println("testUpdateDsDataInterface(1): Adding and Removing DS Data using the DS Data Interface");
			EPPSecDNSExtUpdate secDNSExt = new EPPSecDNSExtUpdate();
			
			domain.addDomainName("testUpdateDsDataInterface1.com");
			domain.setTransId("ABC-12345");

			// Add DS
			EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData(12345,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
			secDNSExt.appendAddDsData(dsData);
			// Remove DS
			secDNSExt.appendRemDsData(new EPPSecDNSExtDsData(12346,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateDsDataInterface(1): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());

			
			// -- Removing DS Data with <secDNS:dsData> using the DS Data Interface.
			System.out
					.println("testUpdateDsDataInterface(2): Removing DS Data with <secDNS:dsData> using the DS Data Interface");
			secDNSExt = new EPPSecDNSExtUpdate();

			domain.addDomainName("testUpdateDsDataInterface2.com");
			domain.setTransId("ABC-12345");

			// Remove DS
			secDNSExt.appendRemDsData(new EPPSecDNSExtDsData(12346,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateDsDataInterface(2): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
			
			
			// -- Remove all DS and Key Data using <secDNS:rem> with <secDNS:all>
			System.out
					.println("testUpdateDsDataInterface(3): Remove all DS and Key Data using <secDNS:rem> with <secDNS:all>");
			secDNSExt = new EPPSecDNSExtUpdate();

			domain.addDomainName("testUpdateDsDataInterface3.com");
			domain.setTransId("ABC-12345");

			// Set urgent flag
			secDNSExt.setUrgent(true);			
			
			// Remove all DS and Key Data
			secDNSExt.setRemAllData(true);
			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateDsDataInterface(3): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
			
			// -- Replacing all DS Data using the DS Data Interface
			System.out
					.println("testUpdateDsDataInterface(4): Replacing all DS Data using the DS Data Interface");
			secDNSExt = new EPPSecDNSExtUpdate();

			domain.addDomainName("testUpdateDsDataInterface4.com");
			domain.setTransId("ABC-12345");
			
			// Set urgent flag
			secDNSExt.setUrgent(true);

			// Remove all DS and Key Data
			secDNSExt.setRemAllData(true);
			
			// Add DS
			secDNSExt.appendAddDsData(new EPPSecDNSExtDsData(12346,
					EPPSecDNSAlgorithm.DSA,
					EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateDsDataInterface(4): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult().getCode());
			
			// -- Update the maxSigLife
			System.out
					.println("testUpdateDsDataInterface(5): Update the maxSigLife");
			secDNSExt = new EPPSecDNSExtUpdate();

			domain.addDomainName("testUpdateDsDataInterface5.com");
			domain.setTransId("ABC-12345");
			
			// Set maxSigLife
			secDNSExt.setMaxSigLife(605900);

			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateDsDataInterface(5): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());
			
		}
		catch (EPPCommandException e) {
			handleException(e);
		}
		printEnd("testUpdateDsDataInterface");
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> for secDNS 1.1 using the Key Data Interface.  
	 * The VeriSign servers do not support the Key Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Adding and Removing Key Data using the Key Data Interface.
	 * </ol>
	 */
	public void testUpdateKeyDataInterface() {
		printStart("testUpdateKeyDataInterface");

		
		try {
			EPPResponse response;

			// -- Using the DS Data Interface with one DS
			System.out
					.println("testUpdateKeyDataInterface(1): Adding and Removing Key Data using the Key Data Interface");
			EPPSecDNSExtUpdate secDNSExt = new EPPSecDNSExtUpdate();
			
			domain.addDomainName("testUpdateKeyDataInterface1.com");
			domain.setTransId("ABC-12345");

			// Add Key
			secDNSExt.appendAddKeyData(new EPPSecDNSExtKeyData(
					EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
					EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
					EPPSecDNSAlgorithm.RSAMD5, "AQPJ////4QQQ"));
			
			// Remove Key
			secDNSExt.appendRemKeyData(new EPPSecDNSExtKeyData(
					EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
					EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
					EPPSecDNSAlgorithm.RSAMD5, "AQPJ////4Q=="));
			
			domain.addExtension(secDNSExt);

			response = domain.sendUpdate();
			System.out.println("testUpdateKeyDataInterface(1): Response = [" + response
					+ "]\n\n");
			Assert.assertEquals(EPPResult.SUCCESS, response.getResult()
					.getCode());

			
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		
		printEnd("testUpdateKeyDataInterface");		
	}
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> for secDNS 1.1 with negative cases.
	 * These tests will only work against the Stub Server since specific domains key 
	 * off server-side behavior.
	 * The following negative tests will be executed:<br>
	 * <ol>
	 * <li>Pass an urgent flag of true to a server that does not support it with the domain <code>urgent-not-supported.com</code>.
	 * <li>Pass an urgent flag of true to a server that does support but unable to complete the command with high priority with the domain <code>urgent-supported-cannot-be-urgent.com</code>.
	 * <li>Pass DS Data to a server that only supports the Key Data Interface with the domain <code>key-data-interface.com</code>.
	 * <li>Pass Key Data to a server that only supports the DS Data Interface with the domain <code>ds-data-interface.com</code>.
	 * <li>Pass the maxSigLife to a server that does not support it with the domain <code>maxsiglife-not-supported.com</code>.
	 * </ol>
	 */
	public void testUpdateNegativeTests() {
		printStart("testUpdateNegativeTests");

		EPPResponse response = null;	
		
		// -- Pass an urgent flag of true to a server that does not support it with the domain urgent-not-supported.com
		System.out
				.println("testUpdateNegativeTests(1): Pass an urgent flag of true to a server that does not support it with the domain urgent-not-supported.com");
		
		EPPSecDNSExtUpdate secDNSExt = new EPPSecDNSExtUpdate();
		
		domain.addDomainName("urgent-not-supported.com");
		
		secDNSExt.setUrgent(true);

		// Add DS
		EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData(12345,
				EPPSecDNSAlgorithm.DSA,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
		secDNSExt.appendAddDsData(dsData);
		
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendUpdate();
			Assert
					.fail("Passing urgent flag to server that doesn't support it was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testUpdateNegativeTests(1): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.UNIMPLEMENTED_OPTION, ex
					.getResponse().getResult().getCode());
		}
		
		
		// -- Pass an urgent flag of true to a server that does support but unable to complete the command with high priority with the domain urgent-supported-cannot-be-urgent.com
		System.out
				.println("testUpdateNegativeTests(2): Pass an urgent flag of true to a server that does support but unable to complete the command with high priority with the domain urgent-supported-cannot-be-urgent.com");
		
		secDNSExt = new EPPSecDNSExtUpdate();
		
		domain.addDomainName("urgent-supported-cannot-be-urgent.com");
		
		secDNSExt.setUrgent(true);

		// Remove DS
		dsData = new EPPSecDNSExtDsData(12345,
				EPPSecDNSAlgorithm.DSA,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
		secDNSExt.appendRemDsData(dsData);
		
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendUpdate();
			Assert
					.fail("Passing urgent flag to server that supports it but can't complete it with high priority was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testUpdateNegativeTests(1): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.PARAM_VALUE_POLICY_ERROR, ex
					.getResponse().getResult().getCode());
		}
		
		
		
		// -- Passing DS data to server that only supports the Key Data Interface
		System.out
				.println("testUpdateNegativeTests(3): Pass DS Data to a server that only supports the Key Data Interface with the domain key-data-interface.com");
		
		secDNSExt = new EPPSecDNSExtUpdate();
		
		domain.addDomainName("key-data-interface.com");

		// Add DS
		dsData = new EPPSecDNSExtDsData(12345,
				EPPSecDNSAlgorithm.DSA,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC");
		secDNSExt.appendAddDsData(dsData);
		// Remove DS
		secDNSExt.appendRemDsData(new EPPSecDNSExtDsData(12346,
				EPPSecDNSAlgorithm.DSA,
				EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendUpdate();
			Assert
					.fail("Passing DS data to server that only supports the Key Data Interface was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testUpdateNegativeTests(3): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.PARAM_VALUE_POLICY_ERROR, ex
					.getResponse().getResult().getCode());
		}
	
		// -- Passing Key data to server that only supports the DS Data Interface
		System.out
				.println("testUpdateNegativeTests(4): Pass Key Data to a server that only supports the DS Data Interface with the domain ds-data-interface.com");
		
		secDNSExt = new EPPSecDNSExtUpdate();
		
		domain.addDomainName("ds-data-interface.com");

		// Rem Key
		EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
		keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
		keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
		keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
		keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
				+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
				+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
				+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
				+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
		
		secDNSExt.appendRemKeyData(keyData);
		
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendUpdate();
			Assert
					.fail("Passing Key data to server that only supports the DS Data Interface was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testUpdateNegativeTests(4): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.PARAM_VALUE_POLICY_ERROR, ex
					.getResponse().getResult().getCode());
		}
		
		
		// -- Pass the maxSigLife to a server that does not support it with the domain maxsiglife-not-supported.com
		System.out
				.println("testUpdateNegativeTests(5): Pass the maxSigLife to a server that does not support it with the domain maxsiglife-not-supported.com");
		
		secDNSExt = new EPPSecDNSExtUpdate();
		
		domain.addDomainName("maxsiglife-not-supported.com");
		
		secDNSExt.setMaxSigLife(604800);
	
		domain.addExtension(secDNSExt);

		try {
			response = domain.sendUpdate();
			Assert
					.fail("Passing maxSigLife to server that doesn't support it was unexpectedly successful");
		}
		catch (EPPCommandException ex) {
			System.out.println("testUpdateNegativeTests(5): Response = ["
					+ ex.getResponse() + "]\n\n");
			Assert.assertEquals(EPPResult.UNIMPLEMENTED_OPTION, ex
					.getResponse().getResult().getCode());
		}
				
		
		printEnd("testUpdateNegativeTests");		
	}
	
	


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

		domain = new EPPDomain(session);
	}


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}


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
