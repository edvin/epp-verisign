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
package com.verisign.epp.namestore.interfaces;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import junit.extensions.TestSetup;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.coaext.EPPCoaExtAttr;
import com.verisign.epp.codec.coaext.EPPCoaExtInfData;
import com.verisign.epp.codec.coaext.EPPCoaExtKey;
import com.verisign.epp.codec.coaext.EPPCoaExtValue;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheck;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResp;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainCheckResult;
import com.verisign.epp.codec.premiumdomain.EPPPremiumDomainReAssignCmd;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtAuthInfo;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtCreateResp;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtDomain;
import com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtPeriod;
import com.verisign.epp.codec.rgpext.EPPRgpExtInfData;
import com.verisign.epp.codec.rgpext.EPPRgpExtReport;
import com.verisign.epp.codec.rgpext.EPPRgpExtReportText;
import com.verisign.epp.codec.rgpext.EPPRgpExtStatus;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSAlgorithm;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtDsData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtInfData;
import com.verisign.epp.codec.secdnsext.v11.EPPSecDNSExtKeyData;
import com.verisign.epp.codec.whois.EPPWhoisInfData;
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPDomain;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.pool.EPPSessionPool;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.InvalidateSessionException;
import com.verisign.epp.util.TestThread;
import com.verisign.epp.util.TestUtil;

/**
 * Test of the use of the <code>NSDomain</code> interface.  This
 * test utilizes the EPP session pool and exercises all of the 
 * operations defined in <code>NSDomain</code> and the base
 * class <code>EPPDomain</code>.  
 * 
 * @see com.verisign.epp.namestore.interfaces.NSDomain
 * @see com.verisign.epp.interfaces.EPPDomain
 */
public class NSDomainTst extends TestCase {

	/**
	 * Handle to the Singleton EPP Application instance
	 * (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat = Logger.getLogger(NSDomainTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** EPP Session pool associated with test */
	private static EPPSessionPool sessionPool = null;

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());
	

	/**
	 * Allocates an <code>NSDomainTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public NSDomainTst(String name) {
		super(name);
	}

	// End NSDomainTst(String)


	/**
	 * Unit test of <code>NSDomain.sendCreate</code> command.
	 */
	public void testDomainCreate() {
		printStart("testDomainCreate");

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();

				System.out.println("domainCreate: Create " + theDomainName
						+ " with no optional attributes");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setAuthString("ClientX");

				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("domainCreate: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainCreate: name = "
						+ theResponse.getName());

				System.out.println("domainCreate: expiration date = "
						+ theResponse.getExpirationDate());

			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("domainCreate: Create " + theDomainName
						+ " with all optional attributes");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				for (int i = 0; i <= 20; i++) {
					theDomain.addHostName(this.makeHostName(theDomainName));
				}

				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					// Add domain contacts
					theDomain.addContact("SH0000",
							EPPDomain.CONTACT_ADMINISTRATIVE);

					theDomain.addContact("SH0000", EPPDomain.CONTACT_TECHNICAL);

					theDomain.addContact("SH0000", EPPDomain.CONTACT_BILLING);
				}

				theDomain.setPeriodLength(10);

				theDomain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

				theDomain.setAuthString("ClientX");

				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("domainCreate: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainCreate: name = "
						+ theResponse.getName());

				System.out.println("domainCreate: expiration date = "
						+ theResponse.getExpirationDate());
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			try {
				System.out
						.println( "\n----------------------------------------------------------------" );

				String theDomainName = this.makeDomainName();

				System.out.println( "domainCreate: Create " + theDomainName
						+ " with SecDNS Extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( theDomainName );
				theDomain.setSubProductID( NSSubProduct.COM );

				theDomain.setAuthString( "ClientX" );

				// -- Add secDNS Extension
				// instantiate a secDNS:keyData object
				EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
				keyData.setFlags( EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP );
				keyData.setProtocol( EPPSecDNSExtKeyData.DEFAULT_PROTOCOL );
				keyData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				keyData.setPubKey( "AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
						+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
						+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
						+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
						+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==" );

				// instantiate another secDNS:keyData object
				EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
						EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
						EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
						EPPSecDNSAlgorithm.RSASHA1,
						"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
								+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
								+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
								+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
								+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==" );

				// instantiate a secDNS:dsData object
				EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
				dsData.setKeyTag( 34095 );
				dsData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				dsData.setDigestType( EPPSecDNSExtDsData.SHA1_DIGEST_TYPE );
				dsData.setDigest( "6BD4FFFF11566D6E6A5BA44ED0018797564AA289" );
				dsData.setKeyData( keyData );

				// instantiate another secDNS:dsData object
				EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData( 10563,
						EPPSecDNSAlgorithm.RSASHA1,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
						"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2 );

				// dsData Records
				List dsDataRecords = new ArrayList();
				dsDataRecords.add( dsData );
				dsDataRecords.add( dsData2 );

				theDomain.setSecDNSCreate( dsDataRecords );
				theResponse = theDomain.sendCreate();

				// -- Output all of the response attributes
				System.out.println( "domainCreate: Response = [" + theResponse
						+ "]\n\n" );

				// -- Output response attributes using accessors
				System.out.println( "domainCreate: name = "
						+ theResponse.getName() );

				System.out.println( "domainCreate: expiration date = "
						+ theResponse.getExpirationDate() );

			}
			catch ( Exception ex ) {
				TestUtil.handleException( theSession, ex );
			}
			
			try {
				System.out
						.println( "\n----------------------------------------------------------------" );

				String theDomainName = this.makeDomainName();

				System.out.println( "domainCreate: Create " + theDomainName
						+ " with SecDNS and COA Extensions" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( theDomainName );
				theDomain.setSubProductID( NSSubProduct.COM );

				theDomain.setAuthString( "ClientX" );

				// -- Add secDNS Extension
				// instantiate a secDNS:keyData object
				EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
				keyData.setFlags( EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP );
				keyData.setProtocol( EPPSecDNSExtKeyData.DEFAULT_PROTOCOL );
				keyData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				keyData.setPubKey( "AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
						+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
						+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
						+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
						+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==" );

				// instantiate another secDNS:keyData object
				EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
						EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
						EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
						EPPSecDNSAlgorithm.RSASHA1,
						"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
								+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
								+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
								+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
								+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==" );

				// instantiate a secDNS:dsData object
				EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
				dsData.setKeyTag( 34095 );
				dsData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				dsData.setDigestType( EPPSecDNSExtDsData.SHA1_DIGEST_TYPE );
				dsData.setDigest( "6BD4FFFF11566D6E6A5BA44ED0018797564AA289" );
				dsData.setKeyData( keyData );

				// instantiate another secDNS:dsData object
				EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData( 10563,
						EPPSecDNSAlgorithm.RSASHA1,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
						"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2 );

				// dsData Records
				List dsDataRecords = new ArrayList();
				dsDataRecords.add( dsData );
				dsDataRecords.add( dsData2 );

				theDomain.setSecDNSCreate( dsDataRecords );
				
				// Client Object Attributes
				EPPCoaExtKey key = new EPPCoaExtKey("KEY1");
				EPPCoaExtValue value = new EPPCoaExtValue( "value1" );
				EPPCoaExtAttr attr = new EPPCoaExtAttr();
				attr.setKey( key );
				attr.setValue( value );
				
				List attrList = new ArrayList();
				attrList.add(attr);

				theDomain.setCoaCreate(attrList);
		
				
				theResponse = theDomain.sendCreate();

				// -- Output all of the response attributes
				System.out.println( "domainCreate: Response = [" + theResponse
						+ "]\n\n" );

				// -- Output response attributes using accessors
				System.out.println( "domainCreate: name = "
						+ theResponse.getName() );

				System.out.println( "domainCreate: expiration date = "
						+ theResponse.getExpirationDate() );

			}
			catch ( Exception ex ) {
				TestUtil.handleException( theSession, ex );
			}
			
			try {
				System.out
						.println( "\n----------------------------------------------------------------" );

				String theDomainName = this.makeDomainName();

				System.out.println( "domainCreate: Create " + theDomainName
						+ " with COA Extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( theDomainName );
				theDomain.setSubProductID( NSSubProduct.COM );

				theDomain.setAuthString( "ClientX" );
				
				// Client Object Attributes
				EPPCoaExtKey key = new EPPCoaExtKey("KEY1");
				EPPCoaExtValue value = new EPPCoaExtValue( "value1" );
				EPPCoaExtAttr attr = new EPPCoaExtAttr();
				attr.setKey( key );
				attr.setValue( value );
				
				List attrList = new ArrayList();
				attrList.add(attr);

				theDomain.setCoaCreate(attrList);
		
				
				theResponse = theDomain.sendCreate();

				// -- Output all of the response attributes
				System.out.println( "domainCreate: Response = [" + theResponse
						+ "]\n\n" );

				// -- Output response attributes using accessors
				System.out.println( "domainCreate: name = "
						+ theResponse.getName() );

				System.out.println( "domainCreate: expiration date = "
						+ theResponse.getExpirationDate() );

			}
			catch ( Exception ex ) {
				TestUtil.handleException( theSession, ex );
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainCreate");
	}
	
	/**
	 * Unit test of <code>NSDomain.sendRelatedCreate</code>.
	 */
	public void testRelatedDomainCreate () {
		printStart( "testRelatedDomainCreate " );

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

		try {
			System.out
					.println( "\ntestRelatedDomainCreate: Domain create of example.com and related domains" );

			theDomain.addDomainName( "example.com" );
			theDomain.setTransId( "ABC-12349" );
			theDomain.setAuthString( "2fooBAR" );

			
			final EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
					"relDom123!");
			final EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(
					5);
			theDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
					"domain1.com", authInfo, period));
			theDomain.addRelatedDomain(new EPPRelatedDomainExtDomain(
					"domain2.com", authInfo, period));
			theDomain.addRelatedDomain(new EPPRelatedDomainExtDomain("xn--idn.com", authInfo,
					period, "CHI"));		
			
			theResponse = theDomain.sendRelatedCreate();

			// -- Output all of the response attributes
			System.out
					.println( "testRelatedDomainCreate: Response = [" + theResponse + "]\n\n" );

			// -- Output the relDom:infData extension
			if ( theResponse.hasExtension( EPPRelatedDomainExtCreateResp.class ) ) {
				final EPPRelatedDomainExtCreateResp relatedDomainCreData =
						(EPPRelatedDomainExtCreateResp) theResponse
								.getExtension( EPPRelatedDomainExtCreateResp.class );
				System.out.println( "testRelatedDomainCreate: EPPRelatedDomainExtCreateResp = ["
						+ relatedDomainCreData + "]\n\n" );

			}
			else {
				Assert
						.fail( "testRelatedDomainCreate: EPPRelatedDomainExtCreateResp extension not included for domain-create with related domains." );
			}
		}
		catch ( Exception ex ) {
			TestUtil.handleException( theSession, ex );
		}
		

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd( "testRelatedDomainCreate" );
	}
	

	
	/**
	 * Unit test of <code>EPPDomain.sendCreate</code> for secDNS 1.1 using the DS Data Interface.  
	 * The VeriSign servers only support the DS Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Create for a Secure Delegation using the DS Data Interface with one DS.
	 * <li>Create for a Secure Delegation using the DS Data Interface with two DS.  One DS created from key data.
	 * </ol>
	 */
	public void testCreateDsDataInterface() {
		printStart("testCreateDsDataInterface");

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();

				System.out
				.println("testCreateDsDataInterface(1): domain = " + theDomainName + ", Create for a Secure Delegation using the DS Data Interface with one DS");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				
				// Add DS
				List dsDataList = new ArrayList();
				dsDataList.add(new EPPSecDNSExtDsData(12345,
						EPPSecDNSAlgorithm.DSA,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC"));
				theDomain.setSecDNSCreate(dsDataList);

				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("testCreateDsDataInterface(1): Response = [" + theResponse
						+ "]\n\n");
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out
				.println("testCreateDsDataInterface(2): domain = " + theDomainName + ", Create for a Secure Delegation using the DS Data Interface with two DS");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				
				// Add DS
				List dsDataList = new ArrayList();
				dsDataList.add(new EPPSecDNSExtDsData(12345,
						EPPSecDNSAlgorithm.DSA,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC"));
				
				// Key Data associated with DS to add
				EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
				keyData.setFlags(EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP);
				keyData.setProtocol(EPPSecDNSExtKeyData.DEFAULT_PROTOCOL);
				keyData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
				keyData.setPubKey("AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
						+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
						+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
						+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
						+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==");
				
				dsDataList.add(keyData.toDsData("testCreateDsDataInterface.com",
							EPPSecDNSExtDsData.SHA1_DIGEST_TYPE));
				
				
				theDomain.setSecDNSCreate(dsDataList);
				
				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("testCreateDsDataInterface(2): Response = [" + theResponse
						+ "]\n\n");
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testCreateDsDataInterface");
	}
	
	
	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code> for secDNS 1.1 using the DS Data Interface.  
	 * The VeriSign servers only support the DS Data Interface.
	 * The following tests will be executed:<br>
	 * <ol>
	 * <li>Adding and Removing DS Data using the DS Data Interface.
	 * <li>Remove all DS using &lt;secDNS:rem&gt; with &lt;secDNS:all&gt;.
	 * <li>Replacing all DS Data using the DS Data Interface.
	 * </ol>
	 */
	public void testUpdateDsDataInterface() {
		printStart("testUpdateDsDataInterface");

		EPPSession theSession = null;
		EPPResponse theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();

				System.out
				.println("testUpdateDsDataInterface(1): domain = " + theDomainName + ", Adding and Removing DS Data using the DS Data Interface");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				
				List addDsDataList = new ArrayList();
				addDsDataList.add(new EPPSecDNSExtDsData(12345,
						EPPSecDNSAlgorithm.DSA,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC"));
				List remDsDataList = new ArrayList();
				remDsDataList.add(new EPPSecDNSExtDsData(12345,
						EPPSecDNSAlgorithm.DSA,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "38EC35D5B3A34B44C39B"));
				// Add and remove DS data
				theDomain.setSecDNSUpdate(addDsDataList, remDsDataList);

				theResponse = theDomain.sendUpdate();

				//-- Output all of the response attributes
				System.out.println("testUpdateDsDataInterface(1): Response = [" + theResponse
						+ "]\n\n");
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out
				.println("testUpdateDsDataInterface(2): domain = " + theDomainName + ", Remove all DS and Key Data using <secDNS:rem> with <secDNS:all>");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				
				// Remove all DS data
				theDomain.setSecDNSUpdate(null, NSDomain.REM_ALL_DS);
				
				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("testUpdateDsDataInterface(2): Response = [" + theResponse
						+ "]\n\n");
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out
				.println("testUpdateDsDataInterface(3): domain = " + theDomainName + ", Replacing all DS Data using the DS Data Interface");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				
				List addDsDataList = new ArrayList();
				addDsDataList.add(new EPPSecDNSExtDsData(12345,
						EPPSecDNSAlgorithm.DSA,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE, "49FD46E6C4B45C55D4AC"));
				// Replace all DS data
				theDomain.setSecDNSUpdate(addDsDataList, NSDomain.REM_ALL_DS);
				
				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("testUpdateDsDataInterface(3): Response = [" + theResponse
						+ "]\n\n");
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
		
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testUpdateDsDataInterface");
	}
	
	
	/**
	 * Unit test of <code>NSDomain.sendDomainCheck</code> command.
	 */
	public void testDomainCheck() {
		printStart("testDomainCheck");

		EPPSession theSession = null;
		EPPDomainCheckResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {

				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();
				System.out.println("domainCheck: Check single domain name ("
						+ theDomainName + ")");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theResponse = theDomain.sendCheck();

				System.out.println("Response Type = " + theResponse.getType());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getServerTransId());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getClientTransId());

				// Output all of the response attributes
				System.out.println("\ndomainCheck: Response = [" + theResponse
						+ "]");

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPDomainCheckResult currResult = (EPPDomainCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is available");
					}
					else {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				// Check multiple domain names
				System.out
						.println("\n----------------------------------------------------------------");
				System.out
						.println("domainCheck: Check multiple domain names (example1.com, example2.com, example3.com)");
				theDomain.setTransId("ABC-12345-XYZ");

				/**
				 * Add example(1-3).com
				 */
				theDomain.addDomainName("example1.com");
				theDomain.addDomainName("example2.com");
				theDomain.addDomainName("example3.com");
				theDomain.setSubProductID(NSSubProduct.COM);

				for (int i = 0; i <= 10; i++) {
					theDomain.addDomainName(this.makeDomainName());
				}

				theResponse = theDomain.sendCheck();

				// Output all of the response attributes
				System.out.println("\ndomainCheck: Response = [" + theResponse
						+ "]");
				System.out.println("Client Transaction Id = "
						+ theResponse.getTransId().getClientTransId());
				System.out.println("Server Transaction Id = "
						+ theResponse.getTransId().getServerTransId());

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPDomainCheckResult currResult = (EPPDomainCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is available");
					}
					else {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {

				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = "non-premiumdomain.tv";
				System.out.println("nonPremiumDomainCheck: Check single domain name With Flag True ("
						+ theDomainName + ")");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.TV);

				EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
				theDomain.addExtension( extension );
				
				theResponse = theDomain.sendCheck();

				// Output all of the response attributes
				System.out.println("\nnonPremiumDomainCheck: Response = [" + theResponse
						+ "]");
				
				this.handleResponse(theResponse);
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			try {

				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = "premium.tv";
				System.out.println("premiumDomainCheck: Check single domain name With Flag True ("
						+ theDomainName + ")");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.TV);

				EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
				theDomain.addExtension( extension );
				
				theResponse = theDomain.sendCheck();

				// Output all of the response attributes
				System.out.println("\npremiumDomainCheck: Response = [" + theResponse
						+ "]");

				this.handleResponse(theResponse);
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			try {
				// Check multiple domain names
				System.out
						.println("\n----------------------------------------------------------------");
				System.out
						.println("premiumDomainCheck: Check multiple domain names With Flag True ");
				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.setSubProductID(NSSubProduct.TV);

				for (int i = 1; i <= 3; i++) {
					theDomain.addDomainName("premium" + i + ".tv");
				}

				EPPPremiumDomainCheck extension = new EPPPremiumDomainCheck( true );
				theDomain.addExtension( extension );
				
				theResponse = theDomain.sendCheck();

				// Output all of the response attributes
				System.out.println("\npremiumDomainCheck: Response = [" + theResponse
						+ "]");


				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPDomainCheckResult currResult = (EPPDomainCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is available");
					}
					else {
						System.out.println("domainCheck: Domain "
								+ currResult.getName() + " is not available");
					}
				}
				
				if (theResponse.hasExtension(EPPPremiumDomainCheckResp.class)) {
					EPPPremiumDomainCheckResp premiumDomainCheckResponse = (EPPPremiumDomainCheckResp) theResponse.getExtension(EPPPremiumDomainCheckResp.class);

					// For each result
					for (int i = 0; i < premiumDomainCheckResponse.getCheckResults().size(); i++) {
						EPPPremiumDomainCheckResult currResult = (EPPPremiumDomainCheckResult) premiumDomainCheckResponse
								.getCheckResults().elementAt(i);

						if (currResult.isPremium()) {
							System.out.println("domainCheck: Domain "
									+ currResult.getName() + " is premium");
							if(currResult.getPrice() != null) {
								System.out.println("domainCheck: Premium price is $"
										+ currResult.getPrice());
								System.out.println("domainCheck: Premium renewal price is $"
										+ currResult.getRenewalPrice());
							}
						}
						else {
							System.out.println("domainCheck: Domain "
									+ currResult.getName() + " is not premium");
						}
					}
				}
				this.handleResponse(theResponse);
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainCheck");
	}

	/**
	 * Unit test of <code>NSDomain.sendDomainInfo</code> command.
	 */
	public void testDomainInfo() {
		printStart("testDomainInfo");

		EPPSession theSession = null;
		EPPDomainInfoResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out.println("\ndomainInfo: Standard Domain info");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(this.makeDomainName());
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setHosts(NSDomain.HOSTS_ALL);

				theResponse = theDomain.sendInfo();

				//-- Output all of the response attributes
				System.out.println("domainInfo: Response = [" + theResponse
						+ "]\n\n");

				//-- Output required response attributes using accessors
				System.out.println("domainInfo: name            = "
						+ theResponse.getName());

				System.out.println("domainInfo: client id       = "
						+ theResponse.getClientId());

				System.out.println("domainInfo: created by      = "
						+ theResponse.getCreatedBy());

				System.out.println("domainInfo: create date     = "
						+ theResponse.getCreatedDate());

				System.out.println("domainInfo: expiration date = "
						+ theResponse.getExpirationDate());

				System.out.println("domainInfo: Registrant      = "
						+ theResponse.getRegistrant());

				/**
				 * Process Contacts
				 */
				if (theResponse.getContacts() != null) {
					for (int i = 0; i < theResponse.getContacts().size(); i++) {
						EPPDomainContact myContact = (EPPDomainContact) theResponse
								.getContacts().elementAt(i);

						System.out.println("Contact Name : "
								+ myContact.getName());

						System.out.println("Contact Type : "
								+ myContact.getType());
					}
				}

				/**
				 * Get AuthInfo
				 */
				if (theResponse.getAuthInfo() != null) {
					System.out.println("Authorization        : "
							+ theResponse.getAuthInfo().getPassword());

					System.out.println("Authorization (Roid) : "
							+ theResponse.getAuthInfo().getRoid());
				}

				/**
				 * Get Hosts
				 */
				if (theResponse.getHosts() != null) {
					for (int i = 0; i < theResponse.getHosts().size(); i++) {
						System.out.println("Host Name : "
								+ theResponse.getHosts().elementAt(i));
					}
				}

				/**
				 * Get Ns
				 */
				if (theResponse.getNses() != null) {
					for (int i = 0; i < theResponse.getNses().size(); i++) {
						System.out.println("Name Server : "
								+ theResponse.getNses().elementAt(i));
					}
				}

				/**
				 * Get Status
				 */
				if (theResponse.getStatuses() != null) {
					for (int i = 0; i < theResponse.getStatuses().size(); i++) {
						EPPDomainStatus myStatus = (EPPDomainStatus) theResponse
								.getStatuses().elementAt(i);

						System.out.println("Lang     : " + myStatus.getLang());

						System.out
								.println("Status   : " + myStatus.getStatus());
					}
				}

				this.handleResponse(theResponse);

			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			

			// Request whois information with domain info response.  
			// NOTE - This might not be supported by the target Registry server.
			// Check that the server supports the Whois Info Extension.
			try {
				System.out.println("\ndomainInfo: Domain info with whois information");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(this.makeDomainName());
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setWhoisInfo(true);

				theResponse = theDomain.sendInfo();

				//-- Output all of the response attributes
				System.out.println("domainInfo: Response = [" + theResponse
						+ "]\n\n");

				// Output the whois information
				if (theResponse.hasExtension(EPPWhoisInfData.class)) {
					EPPWhoisInfData theWhoisInf = (EPPWhoisInfData) theResponse.getExtension(EPPWhoisInfData.class);
					
					System.out.println("domainInfo: registrar    = " 
							+ theWhoisInf.getRegistrar());
					
					System.out.println("domainInfo: whois server = " 
							+ theWhoisInf.getWhoisServer());
					
					System.out.println("domainInfo: url          = " 
							+ theWhoisInf.getURL());

					System.out.println("domainInfo: iris server  = " 
							+ theWhoisInf.getIrisServer());
				}
				
			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			try {
				System.out
						.println( "\ndomainInfo: Domain info with SecDNS extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( "secdns.com" );
				theDomain.setSubProductID( NSSubProduct.COM );
				theResponse = theDomain.sendInfo();

				// -- Output all of the response attributes
				System.out.println( "domainInfo: Response DNSSEC = ["
						+ theResponse + "]\n\n" );

				// -- Output the secDNS:infData extension
				if ( theResponse.hasExtension( EPPSecDNSExtInfData.class ) ) {
					EPPSecDNSExtInfData infData = (EPPSecDNSExtInfData) theResponse
							.getExtension( EPPSecDNSExtInfData.class );

					Collection dsDataVec = infData.getDsData();
					EPPSecDNSExtDsData dsData = null;
					if ( dsDataVec == null ) {
						System.out
								.println( "domainInfo: secDNS:infData dsDataVec = "
										+ dsDataVec );
					}
					else {
						int i = 0;
						Iterator iter = dsDataVec.iterator();
						while ( iter.hasNext() ) {
							dsData = (EPPSecDNSExtDsData) iter.next();
							// System.out.println("domainInfo:
							// secDNS:infData/dsData[" + i + "] = "
							// + dsData);
							System.out
									.println( "domainInfo: secDNS:infData/dsData["
											+ i
											+ "]/keyTag = "
											+ dsData.getKeyTag() );
							System.out
									.println( "domainInfo: secDNS:infData/dsData["
											+ i + "]/alg = " + dsData.getAlg() );
							System.out
									.println( "domainInfo: secDNS:infData/dsData["
											+ i
											+ "]/digestType = "
											+ dsData.getDigestType() );
							System.out
									.println( "domainInfo: secDNS:infData/dsData["
											+ i
											+ "]/digest = "
											+ dsData.getDigest() );

							EPPSecDNSExtKeyData keyData = dsData.getKeyData();
							if ( keyData == null ) {
								System.out
										.println( "domainInfo: secDNS:infData/dsData["
												+ i + "]/keyData = " + keyData );
							}
							else {
								// System.out.println("domainInfo:
								// secDNS:infData/dsData[" + i + "]/keyData = "
								// + keyData);
								System.out
										.println( "domainInfo: secDNS:infData/dsData["
												+ i
												+ "]/keyData/flags = "
												+ keyData.getFlags() );
								System.out
										.println( "domainInfo: secDNS:infData/dsData["
												+ i
												+ "]/keyData/protocol = "
												+ keyData.getProtocol() );
								System.out
										.println( "domainInfo: secDNS:infData/dsData["
												+ i
												+ "]/keyData/alg = "
												+ keyData.getAlg() );
								System.out
										.println( "domainInfo: secDNS:infData/dsData["
												+ i
												+ "]/keyData/pubKey = "
												+ keyData.getPubKey() );
							}

							i++;

						} // end while
					}

				}
				else {
					Assert.fail( "domainInfo: no EPPSecDNSExtInfData extension" );
				}
			}
			catch ( EPPCommandException ex ) {
				TestUtil.handleException( theSession, ex );
			}
			
			try {
				System.out
						.println( "\ndomainInfo: Domain info with COA extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( "coa-full-info-owned.com" );
				theDomain.setSubProductID( NSSubProduct.COM );
				theResponse = theDomain.sendInfo();

				// -- Output all of the response attributes
				System.out.println( "domainInfo: Response COA = ["
						+ theResponse + "]\n\n" );
				
				if ( theResponse.hasExtension(EPPCoaExtInfData.class))  {
					
					EPPCoaExtInfData coaInfData = (EPPCoaExtInfData)theResponse
					.getExtension(EPPCoaExtInfData.class);

					for ( Iterator iterator = coaInfData.getAttrs().iterator(); iterator.hasNext(); ) {
						EPPCoaExtAttr attr = (EPPCoaExtAttr) iterator.next();
						String key = attr.getKey().getKey();
						String value = attr.getValue().getValue();
						System.out
						.println( "Client Object Attribute: key='" + key +"', value='" + value +"'" );

					}
				}
				else {
					Assert.fail( "domainInfo: no EPPCoaExtInfData extension" );
				}

			}
			catch ( EPPCommandException ex ) {
				TestUtil.handleException( theSession, ex );
			}
					
			try {
				System.out
						.println( "\ndomainInfo: Domain info for domain with RGP statuses" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( "graceperiod.com" );
				theDomain.setSubProductID( NSSubProduct.COM );
				theResponse = theDomain.sendInfo();

				// -- Output all of the response attributes
				System.out.println( "domainInfo: Response for graceperiod.com = ["
						+ theResponse + "]\n\n" );
				
				this.printRgpStatuses(theResponse);

				theDomain.addDomainName( "pendingperiod.com" );
				theDomain.setSubProductID( NSSubProduct.COM );
				theResponse = theDomain.sendInfo();

				// -- Output all of the response attributes
				System.out.println( "domainInfo: Response for pendingperiod.com = ["
						+ theResponse + "]\n\n" );
				
				this.printRgpStatuses(theResponse);
				
			}
			catch ( EPPCommandException ex ) {
				TestUtil.handleException( theSession, ex );
			}
			
			
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainInfo");
	}
	
	/**
	 * Unit test of <code>NSDomain.sendDelete</code> command.
	 */
	public void testDomainDelete() {
		printStart("testDomainDelete");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out.println("\ndomainDelete: Domain delete");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(this.makeDomainName());
				theDomain.setSubProductID(NSSubProduct.COM);

				theResponse = theDomain.sendDelete();

				//-- Output all of the response attributes
				System.out.println("domainDelete: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainDelete");
	}

	/**
	 * Unit test of <code>NSDomain.sendDomainRenew</code> command.
	 */
	public void testDomainRenew() {
		printStart("testDomainRenew");

		EPPSession theSession = null;
		EPPDomainRenewResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainRenew: Domain " + theDomainName
						+ " renew");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setExpirationDate(new GregorianCalendar(2004, 2, 3)
						.getTime());

				theDomain.setPeriodLength(10);

				theDomain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

				theResponse = theDomain.sendRenew();

				//-- Output all of the response attributes
				System.out.println("domainRenew: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainRenew: name = "
						+ theResponse.getName());

				System.out.println("domainRenew: expiration date = "
						+ theResponse.getExpirationDate());

			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			this.handleResponse(theResponse);

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainRenew");
	}

	/**
	 * Unit test of <code>NSDomain.sendUpdate</code> command.
	 */
	public void testDomainUpdate() {
		printStart("testDomainUpdate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainUpdate: Domain " + theDomainName
						+ " update");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Add attributes
				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.ADD);
				}

				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.ADD);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.ADD);

				// Remove attributes
				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.REMOVE);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.REMOVE);

				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.REMOVE);
				}

				// Update the authInfo value
				theDomain.setAuthString("new-auth-info-123");

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainUpdate: Domain " + theDomainName
						+ " update/add with SecDNS extension");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

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

				// instantiate the add DS Data
				List addDsData = new ArrayList();
				addDsData.add(dsData);
				addDsData.add(dsData2);

				theDomain.setSecDNSUpdate(addDsData, null);

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

				// Send DNSSEC update with urgent set to true
				theDomain.setTransId("ABC-12345-XYZ-2");

				theDomainName = this.makeDomainName();

				System.out
						.println("\ndomainUpdate: Domain "
								+ theDomainName
								+ " update/add with SecDNS extension and urgent = true");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setSecDNSUpdate(addDsData, null);

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainUpdate: Domain " + theDomainName
						+ " update/rem with SecDNS extension");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// instantiate a secDNS:dsData object
				EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
				dsData.setKeyTag(34095);
				dsData.setAlg(EPPSecDNSAlgorithm.RSASHA1);
				dsData.setDigestType(EPPSecDNSExtDsData.SHA1_DIGEST_TYPE);
				dsData.setDigest("6BD4FFFF11566D6E6A5BA44ED0018797564AA289");

				// instantiate another secDNS:dsData object
				EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData(10563,
						EPPSecDNSAlgorithm.RSASHA1,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
						"9C20674BFF957211D129B0DFE9410AF753559D4B");

				// instantiate the secDNS:update object
				List rmvDsData = new ArrayList();
				rmvDsData.add(dsData);
				rmvDsData.add(dsData2);

				theDomain.setSecDNSUpdate(null, rmvDsData);

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				theDomain.setTransId("ABC-12345-XYZ");
				String theDomainName = "premium.tv";

				System.out.println("\npremiumDomainUpdate: Domain "
						+ theDomainName + " update with ReAssign extension");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.TV);

				EPPPremiumDomainReAssignCmd extension = new EPPPremiumDomainReAssignCmd();
				extension.setShortName("testregistrar");
				theDomain.addExtension(extension);

				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}

		printEnd("testDomainUpdate");
	}

	/**
	 * Unit test of <code>NSDomain.sendTransfer</code> command.
	 */
	public void testDomainTransfer() {
		printStart("testDomainTransfer");

		EPPSession theSession = null;
		EPPDomainTransferResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);
			String theDomainName = this.makeDomainName();

			try {

				System.out.println("\ndomainTransfer: Domain " + theDomainName
						+ " transfer request");

				theDomain.setTransferOpCode(EPPDomain.TRANSFER_REQUEST);

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.setAuthString("ClientX");

				theDomain.addDomainName(this.makeDomainName());
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setPeriodLength(10);

				theDomain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

				// Execute the transfer query
				theResponse = theDomain.sendTransfer();

				//-- Output all of the response attributes
				System.out.println("domainTransfer: Response = [" + theResponse
						+ "]\n\n");

				//-- Output required response attributes using accessors
				System.out.println("domainTransfer: name = "
						+ theResponse.getName());

				System.out.println("domainTransfer: request client = "
						+ theResponse.getRequestClient());

				System.out.println("domainTransfer: action client = "
						+ theResponse.getActionClient());

				System.out.println("domainTransfer: transfer status = "
						+ theResponse.getTransferStatus());

				System.out.println("domainTransfer: request date = "
						+ theResponse.getRequestDate());

				System.out.println("domainTransfer: action date = "
						+ theResponse.getActionDate());

				//-- Output optional response attributes using accessors
				if (theResponse.getExpirationDate() != null) {
					System.out.println("domainTransfer: expiration date = "
							+ theResponse.getExpirationDate());
				}

				this.handleResponse(theResponse);

			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			// Transfer Query
			try {
				System.out
						.println("\n----------------------------------------------------------------");

				System.out.println("\ndomainTransfer: Domain " + theDomainName
						+ " transfer query");

				theDomain.setTransferOpCode(EPPDomain.TRANSFER_QUERY);

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Execute the transfer query
				theResponse = theDomain.sendTransfer();

				//-- Output all of the response attributes
				System.out.println("domainTransferQuery: Response = ["
						+ theResponse + "]\n\n");

				//-- Output required response attributes using accessors
				System.out.println("domainTransferQuery: name = "
						+ theResponse.getName());

				System.out.println("domainTransferQuery: request client = "
						+ theResponse.getRequestClient());

				System.out.println("domainTransferQuery: action client = "
						+ theResponse.getActionClient());

				System.out.println("domainTransferQuery: transfer status = "
						+ theResponse.getTransferStatus());

				System.out.println("domainTransferQuery: request date = "
						+ theResponse.getRequestDate());

				System.out.println("domainTransferQuery: action date = "
						+ theResponse.getActionDate());

				//-- Output optional response attributes using accessors
				if (theResponse.getExpirationDate() != null) {
					System.out
							.println("domainTransferQuery: expiration date = "
									+ theResponse.getExpirationDate());
				}
			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			// Transfer Cancel
			try {
				System.out
						.println("\n----------------------------------------------------------------");

				System.out.println("\ndomainTransfer: Domain " + theDomainName
						+ " transfer cancel");

				theDomain.setTransferOpCode(EPPDomain.TRANSFER_CANCEL);

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Execute the transfer cancel
				theResponse = theDomain.sendTransfer();

				//-- Output all of the response attributes
				System.out.println("domainTransfer: Response = [" + theResponse
						+ "]\n\n");
			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			// Transfer Reject
			try {
				System.out
						.println("\n----------------------------------------------------------------");

				System.out.println("\ndomainTransfer: Domain " + theDomainName
						+ " transfer reject");

				theDomain.setTransferOpCode(EPPDomain.TRANSFER_REJECT);

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Execute the transfer cancel
				theResponse = theDomain.sendTransfer();

				//-- Output all of the response attributes
				System.out.println("domainTransfer: Response = [" + theResponse
						+ "]\n\n");
			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			// Transfer Approve
			try {
				System.out
						.println("\n----------------------------------------------------------------");

				System.out.println("\ndomainTransfer: Domain transfer approve");

				theDomain.setTransferOpCode(EPPDomain.TRANSFER_APPROVE);

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Execute the transfer cancel
				theResponse = theDomain.sendTransfer();

				//-- Output all of the response attributes
				System.out.println("domainTransfer: Response = [" + theResponse
						+ "]\n\n");
			}

			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainTransfer");
	}

	/**
	 * Unit test of <code>NSDomain.sendSync</code> command.
	 */
	public void testDomainSync() {
		printStart("testDomainSync");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainSync: Domain " + theDomainName
						+ " sync");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Set to June 15th
				theDomain.setDay(15);
				theDomain.setMonth(Calendar.JUNE);

				// Execute update
				theResponse = theDomain.sendSync();

				//-- Output all of the response attributes
				System.out.println("domainSync: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainSync");
	}

	/**
	 * Unit test of <code>NSDomain.sendRestoreRequest</code> command.
	 */
	public void testDomainRestoreRequest() {
		printStart("testDomainRestoreRequest");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);
			String theDomainName = this.makeDomainName();

			try {
				theDomain.setTransId("ABC-12345-XYZ");

				System.out.println("\ndomainRestoreRequest: Domain "
						+ theDomainName + " restore request");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Execute restore request
				theResponse = theDomain.sendRestoreRequest();

				//-- Output all of the response attributes
				System.out.println("domainRestoreRequest: Response = ["
						+ theResponse + "]\n\n");

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			this.handleResponse(theResponse);

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainRestoreRequest");
	}

	/**
	 * Unit test of <code>NSDomain.sendRestoreReport</code> command.
	 */
	public void testDomainRestoreReport() {
		printStart("testDomainRestoreReport");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);
			String theDomainName = this.makeDomainName();

			try {
				theDomain.setTransId("ABC-12345-XYZ");

				System.out.println("\ndomainRestoreReport: Domain "
						+ theDomainName + " restore request");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				EPPRgpExtReport theReport = new EPPRgpExtReport();
				theReport
						.setPreData("Pre-delete whois data goes here. Both XML and free text are allowed");
				theReport
						.setPostData("Post-delete whois data goes here. Both XML and free text are allowed");
				theReport.setDeleteTime(new Date());
				theReport.setRestoreTime(new Date());

				theReport.setRestoreReason(new EPPRgpExtReportText(
						"Registrant Error"));

				theReport
						.setStatement1(new EPPRgpExtReportText(
								"This registrar has not"
										+ " restored the Registered Domain in order to "
										+ "assume the rights to use or sell the Registered"
										+ " Name for itself or for any third party"));

				theReport
						.setStatement2(new EPPRgpExtReportText(
								"The information in this report "
										+ " is true to best of this registrar's knowledge, and this"
										+ "registrar acknowledges that intentionally supplying false"
										+ " information in this report shall "
										+ "constitute  an incurable material breach of the Registry-Registrar"
										+ " Agreement"));

				theReport.setOther("other stuff");

				// Execute restore report
				theDomain.setReport(theReport);
				theResponse = theDomain.sendRestoreReport();

				//-- Output all of the response attributes
				System.out.println("domainRestoreReport: Response = ["
						+ theResponse + "]\n\n");

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			this.handleResponse(theResponse);

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainRestoreReport");
	}

	
	/**
	 * Unit test of <code>NSDomain.sendCreate</code> command with IDN tag extension.
	 */
	public void testDomainIDNCreate() {
		printStart("testDomainIDNCreate");

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();

				System.out.println("domainCreate: Create " + theDomainName
						+ " with IDN tag");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setAuthString("ClientX");

				theDomain.setIDNLangTag("en");
				
				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("domainCreate: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainCreate: name = "
						+ theResponse.getName());

				System.out.println("domainCreate: expiration date = "
						+ theResponse.getExpirationDate());

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}


		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainIDNCreate");
	}
	
	
	/**
	 * Unit test of <code>EPPSession.endSession</code>. One session in the
	 * session pool wil be ended.
	 */
	public void testEndSession() {
		printStart("testEndSession");

		EPPSession theSession = null;
		try {
			theSession = this.borrowSession();
			sessionPool.invalidateObject(theSession);
			theSession = null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("testEndSession(): Exception invalidating session: "
					+ ex);
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testEndSession");
	}

	// End NSDomainTst.endSession()

	/**
	 * Unit test processing responses asynchronous from the commands.  
	 * This is a test of the use of pipelining.
	 */
	public void testAsyncCommands() {
		printStart("testAsyncCommands");

		EPPSession theSession = null;
		int previousSessionMode = EPPSession.MODE_SYNC;
		try {
			List clientTransIdQueue = new ArrayList();
			
			theSession = this.borrowSession();
			if (!theSession.isModeSupported(EPPSession.MODE_ASYNC)) {
				System.out.println("testAsyncCommands: Session " + theSession.getClass().getName() + " does not support MODE_ASYNC, skipping test");
				printEnd("testAsyncCommands (skipped)");
				return;
			}
			previousSessionMode = theSession.setMode(EPPSession.MODE_ASYNC);
			NSDomain theDomain = new NSDomain(theSession);
			String theDomainName = this.makeDomainName();

			// Send 3 commands first
			try {
				// Async domain check
				String theClientTransId = "ASYNC-CMD-" + System.currentTimeMillis();
				clientTransIdQueue.add(theClientTransId);
				theDomain.setTransId(theClientTransId);
				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				System.out.println("domainCheck: Async check of domain name ("
						+ theDomainName + ")");
				theDomain.sendCheck();
				
				// Async domain create
				theClientTransId = "ASYNC-CMD-" + System.currentTimeMillis();
				clientTransIdQueue.add(theClientTransId);
				theDomain.setTransId(theClientTransId);
				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
				theDomain.setAuthString("ClientX");
				System.out.println("domainCheck: Async create of domain name ("
						+ theDomainName + ")");
				theDomain.sendCreate();
				
				// Async domain delete
				theClientTransId = "ASYNC-CMD-" + System.currentTimeMillis();
				clientTransIdQueue.add(theClientTransId);
				theDomain.setTransId(theClientTransId);
				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);
					System.out.println("domainCheck: Async delete of domain name ("
						+ theDomainName + ")");
				theDomain.sendDelete();
				
			}
			catch (EPPCommandException ex) {
				Assert.fail("testAsyncCommands(): Exception sending asynchronous command: "
						+ ex);
			}
			
			// Receive 3 responses
			try {
				while (clientTransIdQueue.size() > 0) {
					String theClientTransId = (String) clientTransIdQueue.get(0);
					clientTransIdQueue.remove(0);
					EPPResponse theResponse = theSession.readResponse();
					if (theClientTransId.equals(theResponse.getTransId().getClientTransId())) {
						System.out.println("Successfully received client transaction id " + theClientTransId + " asynchronously");
					}
					else {
						Assert.fail("testAsyncCommands(): Received response with client transid " + theResponse.getTransId().getClientTransId() + " != expected transid " + theClientTransId);
					}
				}	
			}
			catch (EPPCommandException ex) {
				Assert.fail("testAsyncCommands(): Exception receiving asynchronous response: "
						+ ex);
			}
			

		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("testAsyncCommands(): Exception invalidating session: "
					+ ex);
		}
		finally {
			
			if (theSession != null) {
				theSession.setMode(previousSessionMode);
				this.returnSession(theSession);
			}
		}

		printEnd("testAsyncCommands");
	}
	
	/**
	 * Unit test of support secDNS-1.0 with NSDomain for backward compatibility.
	 */
	public void testSecDNS10() {
		printStart("testSecDNS10");

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;

		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theDomainName = this.makeDomainName();

				System.out.println("domainCreate: Create " + theDomainName
						+ " with no optional attributes");

				theDomain.setTransId("ABC-12345-XYZ");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				theDomain.setAuthString("ClientX");

				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("domainCreate: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainCreate: name = "
						+ theResponse.getName());

				System.out.println("domainCreate: expiration date = "
						+ theResponse.getExpirationDate());

			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("domainCreate: Create " + theDomainName
						+ " with all optional attributes");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				for (int i = 0; i <= 20; i++) {
					theDomain.addHostName(this.makeHostName(theDomainName));
				}

				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					// Add domain contacts
					theDomain.addContact("SH0000",
							EPPDomain.CONTACT_ADMINISTRATIVE);

					theDomain.addContact("SH0000", EPPDomain.CONTACT_TECHNICAL);

					theDomain.addContact("SH0000", EPPDomain.CONTACT_BILLING);
				}

				theDomain.setPeriodLength(10);

				theDomain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

				theDomain.setAuthString("ClientX");

				theResponse = theDomain.sendCreate();

				//-- Output all of the response attributes
				System.out.println("domainCreate: Response = [" + theResponse
						+ "]\n\n");

				//-- Output response attributes using accessors
				System.out.println("domainCreate: name = "
						+ theResponse.getName());

				System.out.println("domainCreate: expiration date = "
						+ theResponse.getExpirationDate());
			}
			catch (Exception ex) {
				TestUtil.handleException(theSession, ex);
			}
			
			try {
				System.out
						.println( "\n----------------------------------------------------------------" );

				String theDomainName = this.makeDomainName();

				System.out.println( "domainCreate: Create " + theDomainName
						+ " with SecDNS Extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( theDomainName );
				theDomain.setSubProductID( NSSubProduct.COM );

				theDomain.setAuthString( "ClientX" );

				// -- Add secDNS Extension
				// instantiate a secDNS:keyData object
				EPPSecDNSExtKeyData keyData = new EPPSecDNSExtKeyData();
				keyData.setFlags( EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP );
				keyData.setProtocol( EPPSecDNSExtKeyData.DEFAULT_PROTOCOL );
				keyData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				keyData.setPubKey( "AQPmsXk3Q1ngNSzsH1lrX63mRIhtwkkK+5Zj"
						+ "vxykBCV1NYne83+8RXkBElGb/YJ1n4TacMUs"
						+ "poZap7caJj7MdOaADKmzB2ci0vwpubNyW0t2"
						+ "AnaQqpy1ce+07Y8RkbTC6xCeEw1UQZ73PzIO"
						+ "OvJDdjwPxWaO9F7zSxnGpGt0WtuItQ==" );

				// instantiate another secDNS:keyData object
				EPPSecDNSExtKeyData keyData2 = new EPPSecDNSExtKeyData(
						EPPSecDNSExtKeyData.FLAGS_ZONE_KEY_SEP,
						EPPSecDNSExtKeyData.DEFAULT_PROTOCOL,
						EPPSecDNSAlgorithm.RSASHA1,
						"AQOxXpFbRp7+zPBoTt6zL7Af0aEKzpS4JbVB"
								+ "5ofk5E5HpXuUmU+Hnt9hm2kMph6LZdEEL142"
								+ "nq0HrgiETFCsN/YM4Zn+meRkELLpCG93Cu/H"
								+ "hwvxfaZenUAAA6Vb9FwXQ1EMYRW05K/gh2Ge"
								+ "w5Sk/0o6Ev7DKG2YiDJYA17QsaZtFw==" );

				// instantiate a secDNS:dsData object
				EPPSecDNSExtDsData dsData = new EPPSecDNSExtDsData();
				dsData.setKeyTag( 34095 );
				dsData.setAlg( EPPSecDNSAlgorithm.RSASHA1 );
				dsData.setDigestType( EPPSecDNSExtDsData.SHA1_DIGEST_TYPE );
				dsData.setDigest( "6BD4FFFF11566D6E6A5BA44ED0018797564AA289" );
				dsData.setKeyData( keyData );

				// instantiate another secDNS:dsData object
				EPPSecDNSExtDsData dsData2 = new EPPSecDNSExtDsData( 10563,
						EPPSecDNSAlgorithm.RSASHA1,
						EPPSecDNSExtDsData.SHA1_DIGEST_TYPE,
						"9C20674BFF957211D129B0DFE9410AF753559D4B", keyData2 );

				// dsData Records
				List dsDataRecords = new ArrayList();
				dsDataRecords.add( dsData );
				dsDataRecords.add( dsData2 );

				theDomain.setSecDNSCreate( dsDataRecords );
				theResponse = theDomain.sendCreate();

				// -- Output all of the response attributes
				System.out.println( "domainCreate: Response = [" + theResponse
						+ "]\n\n" );

				// -- Output response attributes using accessors
				System.out.println( "domainCreate: name = "
						+ theResponse.getName() );

				System.out.println( "domainCreate: expiration date = "
						+ theResponse.getExpirationDate() );

			}
			catch ( Exception ex ) {
				TestUtil.handleException( theSession, ex );
			}

		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testSecDNS10");
	}
	
	
	
	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {

	}

	// End NSDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End NSDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>NSDomainTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		return new NSDomainTstSetup(new TestSuite(NSDomainTst.class));
	}

	// End NSDomainTst.suite()
	
	/**
	 * Setup framework from running NSDomainTst tests.
	 */
	private static class NSDomainTstSetup extends TestSetup {
		
		/**
		 * Creates setup instance for passed in tests.
		 * 
		 * @param aTest Tests to execute
		 */
		public NSDomainTstSetup(Test aTest) {
			super(aTest);
		}

		/**
		 * Setup framework for running NSDomainTst tests.
		 */
		protected void setUp() throws Exception {
			super.setUp();
			
			String theConfigFileName = System.getProperty("EPP.ConfigFile");
			if (theConfigFileName != null) configFileName = theConfigFileName;

			try {
				app.initialize(configFileName);
			}
			catch (EPPCommandException e) {
				e.printStackTrace();
				Assert.fail("Error initializing the EPP Application: " + e);
			}

			// Initialize the session pool
			try {
				sessionPool = EPPSessionPool.getInstance();
				sessionPool.init();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail("Error initializing the session pool: " + ex);
			}

		}

		/**
		 * Tear down framework from running NSDomainTst tests.
		 */
		protected void tearDown() throws Exception {
			super.tearDown();
			EPPSessionPool.getInstance().close();
		}
	}

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * 
	 * <ul>
	 * <li>iterations Number of unit test iterations to run</li>
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.</li>
	 * </ul>
	 * 
	 * 
	 * @param args
	 *            DOCUMENT ME!
	 */
	public static void main(String[] args) {
		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

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
				TestThread thread = new TestThread("NSDomainTst Thread " + i,
						NSDomainTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(NSDomainTst.suite());
		}

		try {
			app.endApplication();
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}


	/**
	 * Print the start of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat
					.info(Thread.currentThread().getName() + ": " + aTest
							+ " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	}

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 *
	 * @param aTest name for the test
	 */
	public static void printEnd(String aTest) {
		System.out
				.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat.info(Thread.currentThread().getName() + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	
	/**
	 * Utility method to borrow a session from the session pool.
	 * All exceptions will result in the test failing.  This method
	 * should only be used for positive session pool tests.
	 * 
	 * @return Session from the session pool
	 */
	private EPPSession borrowSession() {
		EPPSession theSession = null;
		try {
			theSession = sessionPool.borrowObject();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("borrowSession(): Exception borrowing session: " + ex);
		}

		return theSession;
	}

	/**
	 * Utility method to return a session to the session pool.  
	 * This should be placed in a finally block.  All exceptions will
	 * result in the test failing.
	 * 
	 * @param aSession Session to return to the pool
	 */
	private void returnSession(EPPSession aSession) {
		try {
			if (aSession != null) sessionPool.returnObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception returning session: " + ex);
		}
	}

	/**
	 * Utility method to invalidate a session in the session pool.  
	 * This should be placed in an exception block.  
	 * 
	 * @param aSession Session to invalidate in the pool
	 */
	private void invalidateSession(EPPSession aSession) {
		try {
			if (aSession != null) sessionPool.invalidateObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("invalidateSession(): Exception invalidating session: " + ex);
		}
	}
	

	/**
	 * Handle a response by printing out the result details.
	 * 
	 * @param aResponse the response to handle
	 */
	private void handleResponse(EPPResponse aResponse) {

		for (int i = 0; i < aResponse.getResults().size(); i++) {
			EPPResult theResult = (EPPResult) aResponse.getResults().elementAt(
					i);

			System.out.println("Result Code    : " + theResult.getCode());
			System.out.println("Result Message : " + theResult.getMessage());
			System.out.println("Result Lang    : " + theResult.getLang());

			if (theResult.isSuccess()) {
				System.out.println("Command Passed ");
			}
			else {
				System.out.println("Command Failed ");
			}

			if (theResult.getAllValues() != null) {
				for (int k = 0; k < theResult.getAllValues().size(); k++) {
					System.out.println("Result Values  : "
							+ theResult.getAllValues().elementAt(k));
				}
			}
		}
	} // End handleResponse(EPPResponse)

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 *
	 * @return Unique domain name
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
				+ String.valueOf(tm + rd.nextInt(12)).substring(10) + ".com");
	}

	/**
	 * Makes a unique IP address based off of the current time.
	 *
	 * @return Unique IP address <code>String</code>
	 */
	public String makeIP() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(50)).substring(10)
				+ "." + String.valueOf(tm + rd.nextInt(50)).substring(10) + "."
				+ String.valueOf(tm + rd.nextInt(50)).substring(10) + "."
				+ String.valueOf(tm + rd.nextInt(50)).substring(10));
	}

	/**
	 * Makes a unique host name for a domain using the current time.
	 *
	 * @param newDomainName DOCUMENT ME!
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
	 * Inspect the <code>EPPDomainInfoResp</code> and print out the 
	 * RGP status information contained in the response.
	 * 
	 * @param aResp Response to inspect
	 */
	private void printRgpStatuses(EPPDomainInfoResp aResp) {
		// Check for the RGP grace period statuses
		if (aResp.hasExtension(EPPRgpExtInfData.class)) {
			EPPRgpExtInfData theRgpInf = (EPPRgpExtInfData) aResp
					.getExtension(EPPRgpExtInfData.class);
			List rgpStatuses = theRgpInf.getStatuses();
			System.out.println("domainInfo: rgpStatuses.size = "
					+ rgpStatuses.size());
			for (int i = 0; i < rgpStatuses.size(); i++) {
				EPPRgpExtStatus rgpStatus = (EPPRgpExtStatus) rgpStatuses
						.get(i);

				if (rgpStatus.getStatus().equals(EPPRgpExtStatus.ADD_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.AUTO_RENEW_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.RENEW_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.TRANSFER_PERIOD)) {
					System.out.println("domainInfo: rgp grace period status "
							+ rgpStatus.getStatus());
					System.out
							.println("domainInfo: rgp grace period end date = "
									+ EPPUtil.decodeTimeInstant(rgpStatus
											.getMessage().substring(8)));
				}
				else if (rgpStatus.getStatus().equals(
						EPPRgpExtStatus.REDEMPTION_PERIOD)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.PENDING_RESTORE)
						|| rgpStatus.getStatus().equals(
								EPPRgpExtStatus.PENDING_DELETE)) {
					System.out.println("domainInfo: rgp pending period status "
							+ rgpStatus.getStatus());

				}

			}
		}

	}
	
	/**
	 * Unit test of <code>NSDomain.sendCreate</code> command with COA extension.
	 */
	public void testDomainCoaCreate() {
		printStart("testDomainCreate");

		EPPSession theSession = null;
		EPPDomainCreateResp theResponse = null;
			
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);
			
			try {
				System.out
						.println( "\n----------------------------------------------------------------" );

				String theDomainName = this.makeDomainName();

				System.out.println( "domainCreate: Create " + theDomainName
						+ " with COA Extension" );

				theDomain.setTransId( "ABC-12345-XYZ" );

				theDomain.addDomainName( theDomainName );
				theDomain.setSubProductID( NSSubProduct.COM );

				theDomain.setAuthString( "ClientX" );
				
				// Client Object Attributes
				EPPCoaExtAttr attr = new EPPCoaExtAttr( "KEY1", "value1" );
				List attrList = new ArrayList();
				attrList.add(attr);
				theDomain.setCoaCreate(attrList);
		
				
				theResponse = theDomain.sendCreate();

				// -- Output all of the response attributes
				System.out.println( "domainCreate: Response = [" + theResponse
						+ "]\n\n" );

				// -- Output response attributes using accessors
				System.out.println( "domainCreate: name = "
						+ theResponse.getName() );

				System.out.println( "domainCreate: expiration date = "
						+ theResponse.getExpirationDate() );

			}
			catch ( Exception ex ) {
				TestUtil.handleException( theSession, ex );
			}
			
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null) this.returnSession(theSession);
		}

		printEnd("testDomainCoaCreate");
	}

	
	/**
	 * Unit test of <code>NSDomain.sendUpdate</code> command with COA extension.
	 */
	public void testDomainCoaUpdate() {
		printStart("testDomainCoaUpdate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSDomain theDomain = new NSDomain(theSession);

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainUpdate: Domain " + theDomainName
						+ " update adding a COA.");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Add attributes
				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.ADD);
				}

				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.ADD);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.ADD);

				// Remove attributes
				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.REMOVE);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.REMOVE);

				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.REMOVE);
				}

				// Update the authInfo value
				theDomain.setAuthString("new-auth-info-123");
				
				
				EPPCoaExtAttr attr = new EPPCoaExtAttr("KEY1", "value1");
				List addAttrs = new ArrayList();
				addAttrs.add(attr);
				
				theDomain.setCoaUpdateForPut(addAttrs);

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate adding a COA: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}

			try {

				theDomain.setTransId("ABC-12345-XYZ");

				String theDomainName = this.makeDomainName();

				System.out.println("\ndomainUpdate: Domain " + theDomainName
						+ " update removing a COA.");

				theDomain.addDomainName(theDomainName);
				theDomain.setSubProductID(NSSubProduct.COM);

				// Add attributes
				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.ADD);
				}

				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.ADD);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.ADD);

				// Remove attributes
				theDomain.setUpdateAttrib(EPPDomain.HOST, this
						.makeHostName(theDomainName), EPPDomain.REMOVE);

				theDomain.setUpdateAttrib(EPPDomain.STATUS,
						new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
						EPPDomain.REMOVE);

				// Is the contact mapping supported?
				if (EPPFactory.getInstance().hasService(
						EPPDomainMapFactory.NS_CONTACT)) {
					theDomain.setUpdateAttrib(EPPDomain.CONTACT, "SH0000",
							EPPDomain.CONTACT_BILLING, EPPDomain.REMOVE);
				}

				// Update the authInfo value
				theDomain.setAuthString("new-auth-info-123");
				

				EPPCoaExtKey key = new EPPCoaExtKey("KEY1");
				List remAttrs = new ArrayList();
				remAttrs.add(key);
				
				theDomain.setCoaUpdateForRem(remAttrs);

				// Execute update
				theResponse = theDomain.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("domainUpdate removing a COA: Response = [" + theResponse
						+ "]\n\n");

				this.handleResponse(theResponse);

			}
			catch (EPPCommandException ex) {
				TestUtil.handleException(theSession, ex);
			}
		}
		catch (InvalidateSessionException ex) {
			this.invalidateSession(theSession);
			theSession = null;
		}
		finally {
			if (theSession != null)
				this.returnSession(theSession);
		}

		printEnd("testDomainCoaUpdate");
	}
	

} // End class NSDomainTst
