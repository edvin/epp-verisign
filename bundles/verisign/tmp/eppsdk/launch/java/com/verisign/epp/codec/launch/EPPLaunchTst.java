/***********************************************************
Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/
package com.verisign.epp.codec.launch;

// JUNIT Imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.codec.binary.Base64;

import com.verisign.epp.codec.domain.EPPDomainAddRemove;
import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.mark.EPPCourt;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.codec.mark.EPPMarkAddress;
import com.verisign.epp.codec.mark.EPPMarkContact;
import com.verisign.epp.codec.mark.EPPProtection;
import com.verisign.epp.codec.mark.EPPTrademark;
import com.verisign.epp.codec.mark.EPPTreatyOrStatute;
import com.verisign.epp.codec.signedMark.EPPEncodedSignedMark;
import com.verisign.epp.codec.signedMark.EPPIssuer;
import com.verisign.epp.codec.signedMark.EPPSignedMark;
import com.verisign.epp.codec.signedMark.SMDRevocationList;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.launch package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.launch package.
 */
public class EPPLaunchTst extends TestCase {

	/**
	 * Number of iterations to run the test.
	 */
	private static long numIterations = 1;

	/**
	 * Private key used for signing
	 */
	private static PrivateKey privateKey;

	/**
	 * Public key used for verification
	 */
	private static PublicKey publicKey;

	/**
	 * PKIX parameters passed to PKIX <code>CertPathValidator</code> algorithm.
	 */
	private static PKIXParameters pkixParameters;

	/**
	 * SMD revocation list
	 */
	private static SMDRevocationList smdRevocationList = new SMDRevocationList();

	/**
	 * Certificate chain associated with the private key
	 */
	private static Certificate[] certChain;

	/**
	 * Keystore containing valid certificate and private key used to sign the
	 * mark.
	 */
	private static final String KEYSTORE_FILENAME = "signedMark.jks";

	/**
	 * Keystore containing revoked certificate and private key based on the
	 * <code>CRL_FILENAME</code>.
	 */
	private static final String KEYSTORE_REVOKED_FILENAME = "signedMarkRevoked.jks";

	/**
	 * Password used to access <code>KEYSTORE_FILENAME</code> and
	 * <code>KEYSTORE_REVOKED_FILENAME</code>.
	 */
	private static final String KEYSTORE_PASSWORD = "changeit";

	/**
	 * Alias for PrivateKeyEntry containing certificate and private used for
	 * signing
	 */
	private static final String KEYSTORE_KEY_ALIAS = "signedMark";

	/**
	 * Alias for trustedCertEntry containing certificate used to verify
	 * signature, when not using certificate included in XML Signature.
	 */
	private static final String KEYSTORE_CERT_ALIAS = "signedMarkCert";

	/**
	 * Keystore containing valid certificate and private key used to signed the
	 * marks.
	 */
	private static final String TRUSTSTORE_FILENAME = "signedMarkTrust.jks";
	
	/**
	 * Root directory containing the sample Signed Mark Data (SMD)'s.  
	 */
	private static String smdsDir;


	/**
	 * Creates a new EPPLaunchTst object.
	 * 
	 * @param name
	 *            Name of test to execute
	 */
	public EPPLaunchTst(String name) {
		super(name);
	}

	
	
	/**
	 * Tests the <code>EPPSignedMark</code> class. The tests include the
	 * following:<br>
	 * <br>
	 * <ol>
	 * <li>Test signing with private key, without certificates and verification
	 * using public key
	 * <li>Test signing with private key with certificates and verification
	 * using CA certificate
	 * <li>Test signed mark XML encode / decode with XML signature validation
	 * <li>Test base64 encoding and decoding and XML signature validation
	 * <li>Test signing and verification with revoked certificate
	 * </ol>
	 */
	public void testSignedMark() {
		EPPCodecTst.printStart("testSignedMark");

		// Mark Owner
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setOrg("Example Inc.");
		holder.setEmail("holder@example.tld");
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("123 Example Dr.");
		holderAddress.addStreet("Suite 100");
		holderAddress.setCity("Reston");
		holderAddress.setSp("VA");
		holderAddress.setPc("20190");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Mark Contact
		EPPMarkContact contact = new EPPMarkContact();
		contact.setType(EPPMarkContact.TYPE_OWNER);
		contact.setName("John Doe");
		contact.setOrg("Example Inc."); // Address
		EPPMarkAddress contactAddress = new EPPMarkAddress();
		contactAddress.addStreet("123 Example Dr.");
		contactAddress.addStreet("Suite 100");
		contactAddress.setCity("Reston");
		contactAddress.setSp("VA");
		contactAddress.setPc("20166-6503");
		contactAddress.setCc("US");
		contact.setAddress(contactAddress);
		contact.setVoice("+1.7035555555");
		contact.setVoiceExt("1234");
		contact.setFax("+1.7035555556");
		contact.setEmail("jdoe@example.tld");

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("1234-2");
		trademark.setName("Example One");
		trademark.addHolder(holder);
		trademark.addContact(contact);
		trademark.setJurisdiction("US");
		trademark.addClass("35");
		trademark.addClass("36");
		trademark.addLabel("example-one");
		trademark.addLabel("exampleone");
		trademark
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		trademark.setRegNum("234235");
		trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
		trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

		// Treaty or Statute
		EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
		treatyOrStatute.setId("1234-2");
		treatyOrStatute.setName("Example One");
		treatyOrStatute.addHolder(holder);
		treatyOrStatute.addContact(contact);
		treatyOrStatute.addProtection(new EPPProtection("US", "Reston", "US"));
		treatyOrStatute.addLabel("example-one");
		treatyOrStatute.addLabel("exampleone");
		treatyOrStatute
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		treatyOrStatute.setRefNum("234235");
		treatyOrStatute
				.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		treatyOrStatute.setTitle("Example Title");
		treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
				.getTime());

		// Court
		EPPCourt court = new EPPCourt();
		court.setId("1234-2");
		court.setName("Example One");
		court.addHolder(holder);
		court.addContact(contact);
		court.addLabel("example-one");
		court.addLabel("exampleone");
		court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		court.setRefNum("234235");
		court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		court.setCc("US");
		court.addRegions("Reston");
		court.setCourtName("Test Court");

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);
		mark.addTreatyOrStatute(treatyOrStatute);
		mark.addCourt(court);

		EPPIssuer issuer = new EPPIssuer("2", "Example Inc.",
				"support@example.tld");
		issuer.setUrl("http://www.example.tld");
		issuer.setVoice("+1.7035555555");
		issuer.setVoiceExt("1234");

		/**
		 * [1] Test signing with private key, without certificates and
		 * verification using public key
		 */
		EPPSignedMark signedMark = null;
		try {
			signedMark = new EPPSignedMark("1-2", issuer,
					new GregorianCalendar(2009, 8, 16).getTime(),
					new GregorianCalendar(2010, 8, 16).getTime(), mark);
			
			signedMark.sign(privateKey);
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [1] Error signing the signed mark with private key: "
					+ ex);
		}

		if (!signedMark.validate(publicKey)) {
			Assert.fail("testSignedMark(): [1] Error validating the signed mark using public key");
		}

		System.out.println("testSignedMark(): [1] Success");

		/**
		 * [2] Test signing with private key with certificates and verification
		 * using CA certificate
		 */
		try {
			signedMark = new EPPSignedMark("2-2", issuer, new GregorianCalendar(
					2009, 8, 16).getTime(),
					new GregorianCalendar(2010, 8, 16).getTime(), mark);
			
			signedMark.sign(privateKey, certChain);
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [2] Error signing the signed mark with private key and certificates: "
					+ ex);
		}
		
		// Create SMD of signed mark to vrsnSignedMark.smd
		try {
			byte[] smdXml = signedMark.encode();
			byte[] smdBase64 = Base64.encodeBase64(smdXml, true);
			FileOutputStream outStream = new FileOutputStream("vrsnSignedMark.smd");
			outStream.write("-----BEGIN ENCODED SMD-----\n".getBytes());
			outStream.write(smdBase64);
			outStream.write("-----END ENCODED SMD-----\n".getBytes());
			outStream.close();
		}
		catch (Exception ex) {
			Assert.fail("testSignedMark(): [2] Error creating vrsnSignedMark.smd SMD file for signed mark: "
					+ ex);
		}
		
		// Create XML signed mark pretty printed
		try {
			FileOutputStream outStream = new FileOutputStream("vrsnSignedMark.xml");
			outStream.write(signedMark.toString().getBytes());
			outStream.close();
		}
		catch (Exception ex) {
			Assert.fail("testSignedMark(): [2] Error creating vrsnSignedMark.xml file for signed mark: "
					+ ex);
		}
		
		

		if (!signedMark.validate(pkixParameters)) {
			Assert.fail("testSignedMark(): [2] Error validating with CA certificate");
		}

		System.out.println("testSignedMark(): [2] Success");

		/**
		 * [3] Test signed mark XML encode / decode with XML signature
		 * validation
		 */

		// Test signed mark XML encode / decode
		EPPSignedMark signedMark2 = null;
		byte[] signedMarkXml = {};
		try {
			signedMarkXml = signedMark.encode();

			System.out.println("testSignedMark(): [3] Signed Mark XML 1 = ["
					+ new String(signedMarkXml) + "]");

			signedMark2 = new EPPSignedMark(signedMarkXml);
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [3] Error encoding and decoding from XML: "
					+ ex);
		}

		byte[] signedMarkXml2 = {};
		try {
			signedMarkXml2 = signedMark2.encode();

			System.out.println("testSignedMark(): [3] Signed Mark XML 2 = ["
					+ new String(signedMarkXml2) + "]");

			// Original signed mark matches decoded signed mark?
//			int compareVal = (new String(signedMarkXml)).compareTo(new String(
//					signedMarkXml2));
//			if (compareVal != 0) {
//				Assert.fail("testSignedMark(): [3] The signed mark XML is not equal, with compare value = "
//						+ compareVal);
//			}

			if (!signedMark2.validate(publicKey)) {
				Assert.fail("testSignedMark(): [3] Signed mark validation using public key error with encode/decode");
			}
			if (!signedMark2.validate(pkixParameters)) {
				Assert.fail("testSignedMark(): [3] Signed mark validation using PKIXParameters (trust store) error with encode/decode");
			}
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [3] Error validating the signed mark after encode / decode of XML: "
					+ ex);
		}

		System.out.println("testSignedMark(): [3] Success");
		
		
		/**
		 * [3.1] Test creating immutable signed mark from a signed mark and validating it.  
		 */
		EPPSignedMark signedMark3 = null;
		byte[] signedMarkXml3 = {};
		try {
			signedMark3 = new EPPSignedMark(signedMarkXml);			
		}
		catch (EPPException e) {
			Assert.fail("testSignedMark(): [3.1] Error creating decoding signed mark: "
					+ e);
		}
		if (!signedMark3.validate(publicKey)) {
			Assert.fail("testSignedMark(): [3.1] Signed mark validation using public key error with encode/decode");
		}
		if (!signedMark2.validate(pkixParameters)) {
			Assert.fail("testSignedMark(): [3.1] Signed mark validation using PKIXParameters (trust store) error with encode/decode");
		}
		
			

		/**
		 * [4] Test base64 encoding and decoding and XML signature validation
		 */
		EPPEncodedSignedMark encodedSignedMark = null;
		try {
			encodedSignedMark = new EPPEncodedSignedMark(signedMark);

			System.out
					.println("testSignedMark(): [4] Signed Encoded Mark XML 1 = ["
							+ new String(signedMarkXml) + "]");

			signedMark2 = new EPPSignedMark(signedMarkXml);
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [4] Error encoding and decoding from XML: "
					+ ex);
		}

		try {
			signedMarkXml2 = signedMark2.encode();

			System.out
					.println("testSignedMark(): [4] Signed Encoded Mark XML 2 = ["
							+ new String(signedMarkXml2) + "]");

//			int compareVal = (new String(signedMarkXml)).compareTo(new String(
//					signedMarkXml2));
//			if (compareVal != 0) {
//				Assert.fail("testSignedMark(): [4] The encoded signed mark XML is not equal, with compare value = "
//						+ compareVal);
//			}

			if (!signedMark2.validate(publicKey)) {
				Assert.fail("testSignedMark(): [4] Encoded signed mark validation using public key error with encode/decode");
			}
			if (!signedMark2.validate(pkixParameters)) {
				Assert.fail("testSignedMark(): [4] Signed mark validation using PKIXParameters (trust store) error with encode/decode");
			}
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [4] Error validating the signed mark after encode / decode of XML: "
					+ ex);
		}

		System.out.println("testSignedMark(): [4] Success");
		
		/**
		 * [5] Test signing and verification with revoked certificate
		 */

		// Load the revoked certificate private key and certificate chain
		PrivateKey revokedPrivateKey = null;
		Certificate[] revokedCertChain = null;
		try {
			KeyStore.PrivateKeyEntry keyEntry = loadPrivateKeyEntry(
					KEYSTORE_REVOKED_FILENAME, KEYSTORE_KEY_ALIAS,
					KEYSTORE_PASSWORD);
			revokedPrivateKey = keyEntry.getPrivateKey();
			revokedCertChain = keyEntry.getCertificateChain();
		}
		catch (Exception ex) {
			Assert.fail("testSignedMark(): [5] Error loading removed private key and certificate chain: "
					+ ex);
		}


		try {
			signedMark = new EPPSignedMark("5-2", issuer, new GregorianCalendar(
					2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			
			signedMark.sign(revokedPrivateKey);
		}
		catch (EPPException ex) {
			Assert.fail("testSignedMark(): [5] Error signing the signed mark with private key: "
					+ ex);
		}

		if (signedMark.validate(pkixParameters)) {
			Assert.fail("testSignedMark(): [5] Validating signature with revoked certificate should have failed");
		}

		System.out.println("testSignedMark(): [5] Success");
		

		EPPCodecTst.printEnd("testSignedMark");
	}
	
	
	/**
	 * Tests the <code>EPPSignedMark</code> class using the 
	 * exact attributes defined in the SMD sample 
	 * Trademark-Holder-English-Active.smd. 
	 */
	public void testTrademarkHolderEnglishActive() {
		EPPCodecTst.printStart("testTrademarkHolderEnglishActive");

		// Mark Owner
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setName("Frank White");
		holder.setOrg("Test Organization");
		holder.setEmail("info@example.example");
		holder.setVoice("+1.3014556600");
		holder.setVoice("+1.3014556601");
		
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("101 West Arques Avenue");
		holderAddress.setCity("Sunnyvale");
		holderAddress.setSp("CA");
		holderAddress.setPc("10023-3241");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("00051913734666981373466698-1");
		trademark.setName("Test & Validate");
		trademark.addHolder(holder);
		trademark.setJurisdiction("US");
		trademark.addClass("15");
		trademark.addLabel("test---validate");
		trademark.addLabel("test--validate");
		trademark.addLabel("test-et-validate");
		trademark.addLabel("test-etvalidate");
		trademark.addLabel("test-validate");
		trademark.addLabel("testand-validate");
		trademark.addLabel("testandvalidate");
		trademark.addLabel("testet-validate");
		trademark.addLabel("testetvalidate");
		trademark.addLabel("testvalidate");
		trademark
				.setGoodsAndServices("guitar");
		trademark.setRegNum("1234");
		trademark.setRegDate(EPPUtil.decodeTimeInstant("2012-12-31T23:00:00.000Z"));
		trademark.setExDate(EPPUtil.decodeTimeInstant("2014-12-31T23:00:00.000Z"));

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);

		EPPIssuer issuer = new EPPIssuer("65535", "ICANN TMCH TESTING TMV",
				"notavailable@example.com");
		issuer.setUrl("http://www.example.tld");
		issuer.setVoice("+32.000000");

		/**
		 * [1] Test signing with private key, without certificates and
		 * verification using public key
		 */
		EPPSignedMark signedMark = null;
		// TODO - Get a way to explicitly set the ID attribute.
//		signedMark.setAttrIdValue("_b1462c28-996c-45b0-a053-a23d76e7836f");
		try {
			signedMark = new EPPSignedMark("0000001701373633628125-65535", issuer,
					EPPUtil.decodeTimeInstant("2013-07-12T12:53:48.125Z"),
					EPPUtil.decodeTimeInstant("2017-07-09T22:00:00.000Z"), mark);

			signedMark.sign(privateKey, certChain);
		}
		catch (EPPException ex) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error signing the signed mark with private key and certificates: "
					+ ex);
		}
		
		if (!signedMark.validate(pkixParameters)) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error validating with CA certificate");
		}		
		
		// Create SMD of signed mark to vrsnSignedMark.smd
		byte[] smdXml = null;
		try {
			smdXml = signedMark.encode();
			byte[] smdBase64 = Base64.encodeBase64(smdXml, true);
			FileOutputStream outStream = new FileOutputStream("VRSN-Trademark-Holder-English-Active.smd");
			outStream.write("-----BEGIN ENCODED SMD-----\n".getBytes());
			outStream.write(smdBase64);
			outStream.write("-----END ENCODED SMD-----\n".getBytes());
			outStream.close();
			
		}
		catch (Exception ex) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error creating Trademark-Holder-English-Active.smd SMD file for signed mark: "
					+ ex);
		}
		
		// Create XML signed mark pretty printed
		try {
			FileOutputStream outStream = new FileOutputStream("VRSN-Trademark-Holder-English-Active.xml");
			outStream.write(signedMark.toString().getBytes());
			outStream.close();
		}
		catch (Exception ex) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error creating Trademark-Holder-English-Active.smd file for signed mark: "
					+ ex);
		}
		
		EPPSignedMark signedMark2 = null;
		try {
			signedMark2 = new EPPSignedMark(signedMark.encode());
		}
		catch (EPPException ex) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error creating signed mark 2 from signed mark: "
					+ ex);
		}
		if (!signedMark2.validate(pkixParameters)) {
			Assert.fail("testTrademarkHolderEnglishActive(): Error validating signed mark 2");
		}		
				
		System.out.println("testTrademarkHolderEnglishActive(): Success");
		
		EPPCodecTst.printEnd("testTrademarkHolderEnglishActive");
	}
	
	
	
	
	/**
	 * Tests the <code>EPPLaunchInfo</code> info command extension. The tests
	 * include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test info command for launch application
	 * <li>Test info command for launch registration
	 * </ol>
	 */
	public void testLaunchInfo() {

		EPPCodecTst.printStart("testLaunchInfo");

		// Create domain info command for sunrise application abc123 of
		// example.tld
		EPPDomainInfoCmd theCommand;
		theCommand = new EPPDomainInfoCmd("ABC-12345", "example.tld");
		theCommand.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create domain info command for sunrise registration of example.tld
		theCommand = new EPPDomainInfoCmd("ABC-12345", "example.tld");
		theCommand.addExtension(new EPPLaunchInfo(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE)));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		// Create domain info command for sunrise registration of example.tld
		// with includeMark set to true.
		theCommand = new EPPDomainInfoCmd("ABC-12345", "example.tld");
		EPPLaunchInfo theExt = new EPPLaunchInfo(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE));
		theExt.setIncludeMark(true);
		theCommand.addExtension(theExt);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);

		EPPCodecTst.printEnd("testLaunchInfo");
	}

	/**
	 * Tests the <code>EPPLaunchInfo</code> info command extension. The tests
	 * include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test info command for launch application
	 * <li>Test info command for launch registration
	 * </ol>
	 */
	public void testLaunchInfData() {

		EPPCodecTst.printStart("testLaunchInfData");

		// Create domain info response for sunrise application abc123 of
		// example.tld
		EPPDomainInfoResp theResponse;
		theResponse = new EPPDomainInfoResp();
		theResponse.setName("example.tld");
		theResponse.setTransId(new EPPTransId("ABC-12345", "54322-XYZ"));
		theResponse.setRoid("EXAMPLE1-REP");
		theResponse.setCreatedDate(new Date());
		theResponse.setCreatedBy("ClientY");
		theResponse.setClientId("ClientX");

		EPPLaunchInfData launchInfData = new EPPLaunchInfData(
				new EPPLaunchPhase(EPPLaunchPhase.PHASE_SUNRISE), "abc123",
				new EPPLaunchStatus(EPPLaunchStatus.STATUS_PENDING_VALIDATION));

		// Mark Owner
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setOrg("Example Inc.");
		holder.setEmail("holder@example.tld");
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("123 Example Dr.");
		holderAddress.addStreet("Suite 100");
		holderAddress.setCity("Reston");
		holderAddress.setSp("VA");
		holderAddress.setPc("20190");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Mark Contact
		EPPMarkContact contact = new EPPMarkContact();
		contact.setType(EPPMarkContact.TYPE_OWNER);
		contact.setName("John Doe");
		contact.setOrg("Example Inc."); // Address
		EPPMarkAddress contactAddress = new EPPMarkAddress();
		contactAddress.addStreet("123 Example Dr.");
		contactAddress.addStreet("Suite 100");
		contactAddress.setCity("Reston");
		contactAddress.setSp("VA");
		contactAddress.setPc("20166-6503");
		contactAddress.setCc("US");
		contact.setAddress(contactAddress);
		contact.setVoice("+1.7035555555");
		contact.setVoiceExt("1234");
		contact.setFax("+1.7035555556");
		contact.setEmail("jdoe@example.tld");

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("1234-2");
		trademark.setName("Example One");
		trademark.addHolder(holder);
		trademark.addContact(contact);
		trademark.setJurisdiction("US");
		trademark.addClass("35");
		trademark.addClass("36");
		trademark.addLabel("example-one");
		trademark.addLabel("exampleone");
		trademark
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		trademark.setRegNum("234235");
		trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
		trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

		// Treaty or Statute
		EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
		treatyOrStatute.setId("1234-2");
		treatyOrStatute.setName("Example One");
		treatyOrStatute.addHolder(holder);
		treatyOrStatute.addContact(contact);
		treatyOrStatute.addProtection(new EPPProtection("US", "Reston", "US"));
		treatyOrStatute.addLabel("example-one");
		treatyOrStatute.addLabel("exampleone");
		treatyOrStatute
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		treatyOrStatute.setRefNum("234235");
		treatyOrStatute
				.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		treatyOrStatute.setTitle("Example Title");
		treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
				.getTime());

		// Court
		EPPCourt court = new EPPCourt();
		court.setId("1234-2");
		court.setName("Example One");
		court.addHolder(holder);
		court.addContact(contact);
		court.addLabel("example-one");
		court.addLabel("exampleone");
		court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		court.setRefNum("234235");
		court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		court.setCc("US");
		court.addRegions("Reston");
		court.setCourtName("Test Court");

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);
		mark.addTreatyOrStatute(treatyOrStatute);
		mark.addCourt(court);

		launchInfData.addMark(mark);
		theResponse.addExtension(launchInfData);

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(commandStats);

		// Create domain info response for sunrise registration of
		// example.tld
		theResponse = new EPPDomainInfoResp();
		theResponse.setName("example.tld");
		theResponse.setTransId(new EPPTransId("ABC-12345", "54322-XYZ"));
		theResponse.setRoid("EXAMPLE1-REP");
		theResponse.setCreatedDate(new Date());
		theResponse.setCreatedBy("ClientY");
		theResponse.setClientId("ClientX");

		launchInfData = new EPPLaunchInfData(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), null, null, mark);
		theResponse.addExtension(launchInfData);

		commandStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testLaunchInfData");
	}

	/**
	 * Tests the <code>EPPLaunchUpdate</code> update command extension. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test update command for launch application
	 * </ol>
	 */
	public void testLaunchUpdate() {

		EPPCodecTst.printStart("testLaunchUpdate");

		// Create domain update command for sunrise application abc123 of
		// example.tld

		// add
		Vector addServers = new Vector();
		addServers.addElement("ns2.example.com");

		// remove
		Vector removeServers = new Vector();
		removeServers.addElement("ns1.example.com");

		EPPDomainAddRemove addItems = new EPPDomainAddRemove(addServers, null,
				null);
		EPPDomainAddRemove removeItems = new EPPDomainAddRemove(removeServers,
				null, null);

		EPPDomainUpdateCmd theCommand = new EPPDomainUpdateCmd("ABC-12345",
				"example.tkd", addItems, removeItems, null);

		theCommand.addExtension(new EPPLaunchUpdate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testLaunchUpdate");
	}

	/**
	 * Tests the <code>EPPLaunchDelete</code> delete command extension. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test delete command for launch application
	 * </ol>
	 */
	public void testLaunchDelete() {

		EPPCodecTst.printStart("testLaunchDelete");

		EPPDomainDeleteCmd theCommand = new EPPDomainDeleteCmd("ABC-12345",
				"example.tld");

		theCommand.addExtension(new EPPLaunchUpdate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), "abc123"));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testLaunchDelete");
	}

	/**
	 * Tests the <code>EPPLaunchCheck</code> check command extension. The tests
	 * include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test claims check command for "claims" form and "claims" launch phase
	 * <li>Test claims check response for "claims" launch phase
	 * <li>Test availability check command the custom "idn-release" launch phase
	 * </ol>
	 */
	public void testLaunchCheck() {

		EPPCodecTst.printStart("testLaunchCheck");

		// Claims Check Command for "claims"
		Vector domains = new Vector();
		domains.addElement("example1.tld");
		domains.addElement("example2.tld");

		EPPDomainCheckCmd theCommand = new EPPDomainCheckCmd("ABC-12345",
				domains);

		theCommand.addExtension(new EPPLaunchCheck(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CLAIMS), EPPLaunchCheck.TYPE_CLAIMS));

		EPPEncodeDecodeStats stats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(stats);

		// Claims Check Command for "claims"
		EPPResponse theResponse;
		EPPTransId respTransId = new EPPTransId("ABC-12345", "54322-XYZ");

		theResponse = new EPPResponse(respTransId);
		EPPLaunchChkData theExt = new EPPLaunchChkData(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CLAIMS));
		theExt.addCheckResult(new EPPLaunchCheckResult("example1.tld", false));
		theExt.addCheckResult(new EPPLaunchCheckResult("example2.tld", true,
				"2013041500/2/6/9/rJ1NrDO92vDsAzf7EQzgjX4R0000000001"));
		theExt.addCheckResult(new EPPLaunchCheckResult("example2.tld", true,
				"2013111200/2/6/9/rJ1NrDO92vDsAzf7EQzgjX4R0000000002", "tmch"));

		theResponse.addExtension(theExt);

		stats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(stats);

		// Test availability check command the custom "idn-release" launch phase
		theCommand = new EPPDomainCheckCmd("ABC-12345", domains);

		theCommand.addExtension(new EPPLaunchCheck(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CUSTOM, "idn-release"),
				EPPLaunchCheck.TYPE_AVAILABILITY));

		stats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(stats);

		EPPCodecTst.printEnd("testLaunchCheck");
	}

	/**
	 * Tests the <code>EPPLaunchCreate</code> create command extension with use
	 * of the code and mark in Sunrise Create Form. The tests include the
	 * following:<br>
	 * <br>
	 * <ol>
	 * <li>Test create command for sunrise create using just one code
	 * <li>Test create command for sunrise create using multiple codes
	 * <li>Test create command for sunrise create using just one mark
	 * <li>Test create command for sunrise create using one code and mark
	 * </ol>
	 */
	public void testLaunchCreateCodeMark() {

		EPPCodecTst.printStart("testLaunchCreateCodeMark");

		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.tld",
				new EPPAuthInfo("2fooBAR"));

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			theCommand.setRegistrant("jd1234");
			Vector contacts = new Vector();
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_TECHNICAL));
			theCommand.setContacts(contacts);
		}

		// Test create command for sunrise create using just one code
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(
				"49FD46E6C4B45C55D4AC"), EPPLaunchCreate.TYPE_REGISTRATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for sunrise create using multiple codes
		EPPLaunchCreate createExt = new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE));

		createExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AC", "sample1"));
		createExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AD"));
		createExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AE", "sample2"));

		theCommand.setExtensions(null);
		theCommand.addExtension(createExt);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for sunrise create using just one mark

		// Mark Owner
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setOrg("Example Inc.");
		holder.setEmail("holder@example.tld");
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("123 Example Dr.");
		holderAddress.addStreet("Suite 100");
		holderAddress.setCity("Reston");
		holderAddress.setSp("VA");
		holderAddress.setPc("20190");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Mark Contact
		EPPMarkContact contact = new EPPMarkContact();
		contact.setType(EPPMarkContact.TYPE_OWNER);
		contact.setName("John Doe");
		contact.setOrg("Example Inc."); // Address
		EPPMarkAddress contactAddress = new EPPMarkAddress();
		contactAddress.addStreet("123 Example Dr.");
		contactAddress.addStreet("Suite 100");
		contactAddress.setCity("Reston");
		contactAddress.setSp("VA");
		contactAddress.setPc("20166-6503");
		contactAddress.setCc("US");
		contact.setAddress(contactAddress);
		contact.setVoice("+1.7035555555");
		contact.setVoiceExt("1234");
		contact.setFax("+1.7035555556");
		contact.setEmail("jdoe@example.tld");

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("1234-2");
		trademark.setName("Example One");
		trademark.addHolder(holder);
		trademark.addContact(contact);
		trademark.setJurisdiction("US");
		trademark.addClass("35");
		trademark.addClass("36");
		trademark.addLabel("example-one");
		trademark.addLabel("exampleone");
		trademark
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		trademark.setRegNum("234235");
		trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
		trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

		// Treaty or Statute
		EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
		treatyOrStatute.setId("1234-2");
		treatyOrStatute.setName("Example One");
		treatyOrStatute.addHolder(holder);
		treatyOrStatute.addContact(contact);
		treatyOrStatute.addProtection(new EPPProtection("US", "Reston", "US"));
		treatyOrStatute.addLabel("example-one");
		treatyOrStatute.addLabel("exampleone");
		treatyOrStatute
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		treatyOrStatute.setRefNum("234235");
		treatyOrStatute
				.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		treatyOrStatute.setTitle("Example Title");
		treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
				.getTime());

		// Court
		EPPCourt court = new EPPCourt();
		court.setId("1234-2");
		court.setName("Example One");
		court.addHolder(holder);
		court.addContact(contact);
		court.addLabel("example-one");
		court.addLabel("exampleone");
		court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		court.setRefNum("234235");
		court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		court.setCc("US");
		court.addRegions("Reston");
		court.setCourtName("Test Court");

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);
		mark.addTreatyOrStatute(treatyOrStatute);
		mark.addCourt(court);

		theCommand.setExtensions(null);
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(mark),
				EPPLaunchCreate.TYPE_APPLICATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for sunrise create using one code and mark
		theCommand.setExtensions(null);
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), new EPPLaunchCodeMark(
				"49FD46E6C4B45C55D4AC", "sample", mark)));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for sunrise create using multiple codes and marks
		theCommand.setExtensions(null);
		createExt = new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE));

		createExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AC",
				"sample1", mark));
		createExt.addCodeMark(new EPPLaunchCodeMark("49FD46E6C4B45C55D4AD",
				"sample2", mark));

		theCommand.setExtensions(null);
		theCommand.addExtension(createExt);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command with mix of sunrise create model and claims
		// create model
		theCommand.setExtensions(null);
		createExt = new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CUSTOM, "non-tmch-sunrise"),
				EPPLaunchCreate.TYPE_APPLICATION);

		createExt.addCodeMark(new EPPLaunchCodeMark(mark));
		createExt.setNotice(new EPPLaunchNotice("49FD46E6C4B45C55D4AC",
				new Date(), new Date()));

		theCommand.setExtensions(null);
		theCommand.addExtension(createExt);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		
		// Test create command with mix of sunrise create model and claims
		// create model with validatorID
		theCommand.setExtensions(null);
		createExt = new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CUSTOM, "non-tmch-sunrise"),
				EPPLaunchCreate.TYPE_APPLICATION);

		createExt.addCodeMark(new EPPLaunchCodeMark(mark));
		createExt.setNotice(new EPPLaunchNotice("49FD46E6C4B45C55D4AC",
				new Date(), new Date(), "custom"));

		theCommand.setExtensions(null);
		theCommand.addExtension(createExt);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		
		EPPCodecTst.printEnd("testLaunchCreateCodeMark");
	}

	/**
	 * Tests the <code>EPPLaunchCreate</code> create command extension with use
	 * of the Generic Create Form:<br>
	 * <br>
	 * <ol>
	 * <li>Test create command for landrush with no type defined
	 * <li>Test create command for a landrush application
	 * <li>Test create command for a landrush registration
	 * </ol>
	 */
	public void testLaunchCreateGeneric() {

		EPPCodecTst.printStart("testLaunchCreateGeneric");

		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		// Test create command for a landrush with no type defined
		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.tld",
				new EPPAuthInfo("2fooBAR"));

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			theCommand.setRegistrant("jd1234");
			Vector contacts = new Vector();
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_TECHNICAL));
			theCommand.setContacts(contacts);
		}

		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_LANDRUSH)));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for a landrush application
		theCommand.setExtensions(null);
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_LANDRUSH),
				EPPLaunchCreate.TYPE_APPLICATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test create command for a landrush registration
		theCommand.setExtensions(null);
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_LANDRUSH),
				EPPLaunchCreate.TYPE_REGISTRATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testLaunchCreateGeneric");
	}
	

	/**
	 * Tests the <code>EPPLaunchCreate</code> create command extension with use
	 * of the code and mark in Sunrise Create Form. The tests include the
	 * following:<br>
	 * <br>
	 * <ol>
	 * <li>Test create signed mark in XML, sign it, validate it without
	 * encode/decode
	 * <li>Test create signed mark in Base64, sign it, validate it without
	 * encode/decode
	 * </ol>
	 */
	public void testLaunchCreateSignedMark() {

		EPPCodecTst.printStart("testLaunchCreateSignedMark");

		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.tld",
				new EPPAuthInfo("2fooBAR"));

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			theCommand.setRegistrant("jd1234");
			Vector contacts = new Vector();
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_TECHNICAL));
			theCommand.setContacts(contacts);
		}

		// Test create command for sunrise create using just one signed mark

		// Mark Owner
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setOrg("Example Inc.");
		holder.setEmail("holder@example.tld");
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("123 Example Dr.");
		holderAddress.addStreet("Suite 100");
		holderAddress.setCity("Reston");
		holderAddress.setSp("VA");
		holderAddress.setPc("20190");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Mark Contact
		EPPMarkContact contact = new EPPMarkContact();
		contact.setType(EPPMarkContact.TYPE_OWNER);
		contact.setName("John Doe");
		contact.setOrg("Example Inc."); // Address
		EPPMarkAddress contactAddress = new EPPMarkAddress();
		contactAddress.addStreet("123 Example Dr.");
		contactAddress.addStreet("Suite 100");
		contactAddress.setCity("Reston");
		contactAddress.setSp("VA");
		contactAddress.setPc("20166-6503");
		contactAddress.setCc("US");
		contact.setAddress(contactAddress);
		contact.setVoice("+1.7035555555");
		contact.setVoiceExt("1234");
		contact.setFax("+1.7035555556");
		contact.setEmail("jdoe@example.tld");

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("1234-2");
		trademark.setName("Example One");
		trademark.addHolder(holder);
		trademark.addContact(contact);
		trademark.setJurisdiction("US");
		trademark.addClass("35");
		trademark.addClass("36");
		trademark.addLabel("example-one");
		trademark.addLabel("exampleone");
		trademark
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		trademark.setRegNum("234235");
		trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
		trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

		// Treaty or Statute
		EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
		treatyOrStatute.setId("1234-2");
		treatyOrStatute.setName("Example One");
		treatyOrStatute.addHolder(holder);
		treatyOrStatute.addContact(contact);
		treatyOrStatute.addProtection(new EPPProtection("US", "Reston", "US"));
		treatyOrStatute.addLabel("example-one");
		treatyOrStatute.addLabel("exampleone");
		treatyOrStatute
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		treatyOrStatute.setRefNum("234235");
		treatyOrStatute
				.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		treatyOrStatute.setTitle("Example Title");
		treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
				.getTime());

		// Court
		EPPCourt court = new EPPCourt();
		court.setId("1234-2");
		court.setName("Example One");
		court.addHolder(holder);
		court.addContact(contact);
		court.addLabel("example-one");
		court.addLabel("exampleone");
		court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		court.setRefNum("234235");
		court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		court.setCc("US");
		court.addRegions("Reston");
		court.setCourtName("Test Court");

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);
		mark.addTreatyOrStatute(treatyOrStatute);
		mark.addCourt(court);

		EPPIssuer issuer = new EPPIssuer("2", "Example Inc.",
				"support@example.tld");
		issuer.setUrl("http://www.example.tld");
		issuer.setVoice("+1.7035555555");
		issuer.setVoiceExt("1234");

		EPPSignedMark signedMark = null;
		
		// Sign the signed mark
		try {
			signedMark = new EPPSignedMark("1-123456", issuer,
					new GregorianCalendar(2012, 8, 16).getTime(),
					new GregorianCalendar(2013, 8, 16).getTime(), mark);
			
			signedMark.sign(privateKey, certChain);
		}
		catch (EPPException ex) {
			Assert.fail("Error signing the signed mark: " + ex);
		}

		if (!signedMark.validate(publicKey)) {
			Assert.fail("Signed mark validation error without encode/decode");
		}
		
		

		theCommand.setExtensions(null);
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_SUNRISE), signedMark));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		if (!signedMark.validate(pkixParameters)) {
			Assert.fail("testLaunchCreateSignedMark(): XML signed mark signature is NOT valid after command encode/decode");
		}
				
		

		// Base64 encode the signed mark
		EPPEncodedSignedMark encodedSignedMark = null;
		try {
			encodedSignedMark = new EPPEncodedSignedMark(signedMark);
		}
		catch (EPPEncodeException e1) {
			Assert.fail("testLaunchCreateSignedMark(): Error creating EPPEncodedSignedMark from signed mark");
		}
		catch ( EPPDecodeException e ) {
			Assert.fail("testLaunchCreateSignedMark(): Error creating EPPEncodedSignedMark from signed mark");
		}

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		if (!signedMark.validate(pkixParameters)) {
			Assert.fail("testLaunchCreateSignedMark(): encoded signed mark signature is NOT valid after command encode/decode");
		}
				

		EPPCodecTst.printEnd("testLaunchCreateSignedMark");
	}

	/**
	 * Tests the <code>EPPLaunchCreate</code> create command extension with use
	 * of the notice information in Claims Create Form. The tests include the
	 * following:<br>
	 * <br>
	 * <ol>
	 * <li>Test create command for sunrise create using the notice information
	 * </ol>
	 */
	public void testLaunchCreateNotice() {

		EPPCodecTst.printStart("testLaunchCreateNotice");

		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.tld",
				new EPPAuthInfo("2fooBAR"));

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			theCommand.setRegistrant("jd1234");
			Vector contacts = new Vector();
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact("sh8013",
					EPPDomainContact.TYPE_TECHNICAL));
			theCommand.setContacts(contacts);
		}

		// Test create command for claims create using the notice information
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CLAIMS), new EPPLaunchNotice(
				"49FD46E6C4B45C55D4AC", new Date(), new Date()),
				EPPLaunchCreate.TYPE_APPLICATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		
		theCommand.setExtensions(null);
		
		// Test create command for claims create using the notice information and validatorID
		theCommand.addExtension(new EPPLaunchCreate(new EPPLaunchPhase(
				EPPLaunchPhase.PHASE_CLAIMS), new EPPLaunchNotice(
				"49FD46E6C4B45C55D4AC", new Date(), new Date(), "tmch"),
				EPPLaunchCreate.TYPE_REGISTRATION));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		

		EPPCodecTst.printEnd("testLaunchCreateNotice");
	}

	/**
	 * Tests the <code>EPPLaunchCreData</code> create response extension. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test create response for launch application
	 * </ol>
	 */
	public void testLaunchCreData() {

		EPPCodecTst.printStart("testLaunchCreData");

		EPPDomainCreateResp theResponse;
		EPPTransId respTransId = new EPPTransId("example:epp:239332",
				"server-8551292e23b");
		theResponse = new EPPDomainCreateResp(respTransId, "example.tld",
				new GregorianCalendar(2010, 8, 10).getTime(),
				new GregorianCalendar(2012, 8, 10).getTime());
		theResponse.setResult(EPPResult.SUCCESS_PENDING);

		EPPLaunchCreData launchCreData = new EPPLaunchCreData(
				new EPPLaunchPhase(EPPLaunchPhase.PHASE_SUNRISE),
				"2393-9323-E08C-03B1");

		theResponse.addExtension(launchCreData);

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testLaunchCreData");
	}

	/**
	 * Tests the <code>EPPMark</code> class. The tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Test and encode/decode of mark
	 * </ol>
	 */
	public void testMark() {
		EPPCodecTst.printStart("testMark");

		// Mark Holder
		EPPMarkContact holder = new EPPMarkContact();
		holder.setEntitlement(EPPMarkContact.ENTITLEMENT_OWNER);
		holder.setOrg("Example Inc.");
		holder.setEmail("holder@example.tld");
		// Address
		EPPMarkAddress holderAddress = new EPPMarkAddress();
		holderAddress.addStreet("123 Example Dr.");
		holderAddress.addStreet("Suite 100");
		holderAddress.setCity("Reston");
		holderAddress.setSp("VA");
		holderAddress.setPc("20190");
		holderAddress.setCc("US");
		holder.setAddress(holderAddress);

		// Mark Contact
		EPPMarkContact contact = new EPPMarkContact();
		contact.setType(EPPMarkContact.TYPE_OWNER);
		contact.setName("John Doe");
		contact.setOrg("Example Inc."); // Address
		EPPMarkAddress contactAddress = new EPPMarkAddress();
		contactAddress.addStreet("123 Example Dr.");
		contactAddress.addStreet("Suite 100");
		contactAddress.setCity("Reston");
		contactAddress.setSp("VA");
		contactAddress.setPc("20166-6503");
		contactAddress.setCc("US");
		contact.setAddress(contactAddress);
		contact.setVoice("+1.7035555555");
		contact.setVoiceExt("1234");
		contact.setFax("+1.7035555556");
		contact.setEmail("jdoe@example.tld");

		// Trademark
		EPPTrademark trademark = new EPPTrademark();
		trademark.setId("1234-2");
		trademark.setName("Example One");
		trademark.addHolder(holder);
		trademark.addContact(contact);
		trademark.setJurisdiction("US");
		trademark.addClass("35");
		trademark.addClass("36");
		trademark.addLabel("example-one");
		trademark.addLabel("exampleone");
		trademark
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		trademark.setRegNum("234235");
		trademark.setRegDate(new GregorianCalendar(2009, 8, 16).getTime());
		trademark.setExDate(new GregorianCalendar(2015, 8, 16).getTime());

		// Treaty or Statute
		EPPTreatyOrStatute treatyOrStatute = new EPPTreatyOrStatute();
		treatyOrStatute.setId("1234-2");
		treatyOrStatute.setName("Example One");
		treatyOrStatute.addHolder(holder);
		treatyOrStatute.addContact(contact);
		treatyOrStatute.addProtection(new EPPProtection("US", "Reston", "US"));
		treatyOrStatute.addLabel("example-one");
		treatyOrStatute.addLabel("exampleone");
		treatyOrStatute
				.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		treatyOrStatute.setRefNum("234235");
		treatyOrStatute
				.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		treatyOrStatute.setTitle("Example Title");
		treatyOrStatute.setExecDate(new GregorianCalendar(2015, 8, 16)
				.getTime());

		// Court
		EPPCourt court = new EPPCourt();
		court.setId("1234-2");
		court.setName("Example One");
		court.addHolder(holder);
		court.addContact(contact);
		court.addLabel("example-one");
		court.addLabel("exampleone");
		court.setGoodsAndServices("Dirigendas et eiusmodi featuring infringo in airfare et cartam servicia.");
		court.setRefNum("234235");
		court.setProDate(new GregorianCalendar(2009, 8, 16).getTime());
		court.setCc("US");
		court.addRegions("Reston");
		court.setCourtName("Test Court");

		// Mark
		EPPMark mark = new EPPMark();
		mark.addTrademark(trademark);
		mark.addTreatyOrStatute(treatyOrStatute);
		mark.addCourt(court);

		// Test encode/decode
		EPPMark mark2 = new EPPMark();
		byte[] markXml = {};
		try {
			markXml = mark.encode();

			System.out.println("Mark XML 1 = [" + new String(markXml) + "]");

			mark2.decode(markXml);
		}
		catch (EPPException ex) {
			Assert.fail("Error encoding and decoding from XML: " + ex);
		}

		byte[] markXml2 = {};
		try {
			markXml2 = mark2.encode();

			System.out.println("Mark XML 2 = [" + new String(markXml2) + "]");

			int compareVal = (new String(markXml)).compareTo(new String(
					markXml2));
			if (compareVal != 0) {
				Assert.fail("The mark XML is not equal, with compare value = "
						+ compareVal);
			}
		}
		catch (EPPException ex) {
			Assert.fail("Error validating encode / decode of mark XML: " + ex);
		}

		EPPCodecTst.printEnd("testMark");
	}


	private static class SMDFilenameFilter implements FilenameFilter {
		public boolean accept(File aDir, String aName) {
			if (aName.endsWith(".smd")) {
				return true;
			}
			else {
				return false;
			}
		}
	}

	
	/**
	 * Read and validate the active SMD's.
	 */
	public void testActiveSMDs() {
		EPPCodecTst.printStart("testActiveSMDs");

		// Validate the active SMD's
		File activeSMDDir = new File(smdsDir + "/smd-active");

		if (activeSMDDir.exists() && activeSMDDir.isDirectory()) {

			InputStream smdStream = null;

			File[] activeSMDFiles = activeSMDDir
					.listFiles(new SMDFilenameFilter());

			for (int i = 0; i < activeSMDFiles.length; i++) {
				try {
					smdStream = new FileInputStream(activeSMDFiles[i]);
				}
				catch (FileNotFoundException e1) {
					Assert.fail("testActiveSMDs(): Error opening SMD \""
							+ activeSMDFiles[i].getName() + "\"");
				}

				EPPSignedMark signedMark = null;  

				try {
					signedMark = new EPPEncodedSignedMark(smdStream);
				}
				catch (EPPException e) {
					e.printStackTrace();
					Assert.fail("testActiveSMDs(): Error decoding \""
							+ activeSMDFiles[i].getName() + "\": " + e);
				}

				// Validate the DSIG of the SMD
				if (!signedMark.validate(pkixParameters)) {
					Assert.fail("testActiveSMDs(): Error validating \""
							+ activeSMDFiles[i].getName() + "\"");
				}

				// Signed Mark is revoked?
				if (smdRevocationList.isRevoked(signedMark)) {
					Assert.fail("testActiveSMDs(): SMD \""
							+ activeSMDFiles[i].getName() + "\" is revoked");

				}
				
				System.out.println("testActiveSMDs: Success with SMD \""
						+ activeSMDFiles[i].getName() + "\"");
			}

		}

		EPPCodecTst.printEnd("testActiveSMDs");
	}
	

	/**
	 * Read and ensure SMD's signed by revoked certificate do not pass
	 * validation.
	 */
	public void testRevokedCertSMDs() {
		EPPCodecTst.printStart("testRevokedCertSMDs");

		// Validate the active SMD's
		File revokedCertSMDDir = new File(smdsDir + "/tmv-cert-revoked");

		if (revokedCertSMDDir.exists() && revokedCertSMDDir.isDirectory()) {

			InputStream smdStream = null;

			File[] revokedCertSMDFiles = revokedCertSMDDir
					.listFiles(new SMDFilenameFilter());

			for (int i = 0; i < revokedCertSMDFiles.length; i++) {
				try {
					smdStream = new FileInputStream(revokedCertSMDFiles[i]);
				}
				catch (FileNotFoundException e1) {
					Assert.fail("testRevokedCertSMDs(): Error opening SMD \""
							+ revokedCertSMDFiles[i].getName() + "\"");
				}

				EPPSignedMark signedMark = null;  

				try {
					signedMark = new EPPEncodedSignedMark(smdStream);
				}
				catch (EPPException e) {
					e.printStackTrace();
					Assert.fail("testRevokedCertSMDs(): Error decoding \""
							+ revokedCertSMDFiles[i].getName() + "\": " + e);
				}

				// Ensure invalid SMD does not pass validation
				if (signedMark.validate(pkixParameters)) {
					Assert.fail("testRevokedCertSMDs(): Revoked Certificate SMD \""
							+ revokedCertSMDFiles[i].getName()
							+ "\" incorrectly validated");
				}

				System.out
						.println("testRevokedCertSMDs: Success checking Revoked Certificate SMD \""
								+ revokedCertSMDFiles[i].getName() + "\"");
			}

		}

		EPPCodecTst.printEnd("testRevokedCertSMDs");
	}


	/**
	 * Read and ensure invalid SMD's do not pass validation.
	 */
	public void testInvalidSMDs() {
		EPPCodecTst.printStart("testInvalidSMDs");

		// Validate the active SMD's
		File invalidSMDDir = new File(smdsDir + "/smd-invalid");

		if (invalidSMDDir.exists() && invalidSMDDir.isDirectory()) {

			InputStream smdStream = null;

			File[] invalidSMDFiles = invalidSMDDir
					.listFiles(new SMDFilenameFilter());

			for (int i = 0; i < invalidSMDFiles.length; i++) {
				try {
					smdStream = new FileInputStream(invalidSMDFiles[i]);
				}
				catch (FileNotFoundException e1) {
					Assert.fail("testInvalidSMDs(): Error opening SMD \""
							+ invalidSMDFiles[i].getName() + "\"");
				}

				EPPSignedMark signedMark = null;  

				try {
					signedMark = new EPPEncodedSignedMark(smdStream);
				}
				catch (EPPException e) {
					e.printStackTrace();
					Assert.fail("testInvalidSMDs(): Error decoding \""
							+ invalidSMDFiles[i].getName() + "\": " + e);
				}

				// Ensure invalid SMD does not pass validation
				if (signedMark.validate(pkixParameters)) {
					Assert.fail("testInvalidSMDs(): Invalid SMD \""
							+ invalidSMDFiles[i].getName()
							+ "\" incorrectly validated");
				}

				System.out
						.println("testInvalidSMDs: Success checking invalid SMD \""
								+ invalidSMDFiles[i].getName() + "\"");
			}

		}

		EPPCodecTst.printEnd("testInvalidSMDs");
	}

	/**
	 * Read and ensure revoked SMD's are valid and are in the revocation list.
	 */
	public void testRevokedSMDs() {
		EPPCodecTst.printStart("testRevokedSMDs");

		// Validate the active SMD's
		File revokedSMDDir = new File(smdsDir + "/smd-revoked");

		if (revokedSMDDir.exists() && revokedSMDDir.isDirectory()) {

			InputStream smdStream = null;

			File[] revokedSMDFiles = revokedSMDDir
					.listFiles(new SMDFilenameFilter());

			for (int i = 0; i < revokedSMDFiles.length; i++) {

				try {
					smdStream = new FileInputStream(revokedSMDFiles[i]);
				}
				catch (FileNotFoundException e1) {
					Assert.fail("testRevokedSMDs(): Error opening SMD \""
							+ revokedSMDFiles[i].getName() + "\"");
				}

				EPPSignedMark signedMark = null;  

				try {
					signedMark = new EPPEncodedSignedMark(smdStream);
				}
				catch (EPPException e) {
					e.printStackTrace();
					Assert.fail("testRevokedSMDs(): Error decoding \""
							+ revokedSMDFiles[i].getName() + "\": " + e);
				}

				// Ensure invalid SMD does not pass validation
				if (!signedMark.validate(pkixParameters)) {
					Assert.fail("testRevokedSMDs(): Error validating \""
							+ revokedSMDFiles[i].getName() + "\"");
				}

				// Signed Mark is not revoked?
				if (!smdRevocationList.isRevoked(signedMark)) {
					Assert.fail("testRevokedSMDs(): Revoked \""
							+ revokedSMDFiles[i].getName()
							+ "\" incorrectly not in revocation list");

				}

				System.out
						.println("testInvalidSMDs: Success checking revoked SMD \""
								+ revokedSMDFiles[i].getName() + "\"");
			}

		}

		EPPCodecTst.printEnd("testRevokedSMDs");
	}
	
	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code> with
	 * the <code>EPPCodec</code>.
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
	 * associated with <code>EPPLaunchTst</code>.
	 * 
	 * @return <code>Junit</code> tests
	 */
	public static Test suite() {

		EPPCodecTst.initEnvironment();

		// Initialize the PKIX parameters
		try {
			KeyStore.PrivateKeyEntry keyEntry = loadPrivateKeyEntry(
					KEYSTORE_FILENAME, KEYSTORE_KEY_ALIAS, KEYSTORE_PASSWORD);
			privateKey = keyEntry.getPrivateKey();
			certChain = keyEntry.getCertificateChain();

			publicKey = loadPublicKey(KEYSTORE_FILENAME, KEYSTORE_CERT_ALIAS);
		}
		catch (Exception ex) {
			Assert.fail("Error loading keys for signing and validating: " + ex);
		}

		try {
			List<String> crls = new ArrayList<String>();

			crls.add("eppsdk.crl");
			crls.add("tmch-pilot.crl");

			pkixParameters = loadPKIXParameters(TRUSTSTORE_FILENAME, crls);
		}
		catch (Exception ex) {
			Assert.fail("Error loading trust store: " + ex);
		}

		// Initialize the SMD revocation list
		File smdRevocationListFile = new File("smd-test-revocation.csv");

		if (smdRevocationListFile.exists()) {

			try {
				FileInputStream smdRevocationListStream = new FileInputStream(
						smdRevocationListFile);
				smdRevocationList.decode(smdRevocationListStream);

				System.out.println("SMD Revocation List = [\n"
						+ smdRevocationList + "]");
			}
			catch (FileNotFoundException e) {
				Assert.fail("Error opening SMD Revocation List File: " + e);
			}
			catch (EPPDecodeException e) {
				Assert.fail("Error decoding SMD Revocation List File: " + e);
			}

		}

		TestSuite suite = new TestSuite(EPPLaunchTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addExtFactory(
					"com.verisign.epp.codec.launch.EPPLaunchExtFactory");
		}
		catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDomainMapFactory or EPPLaunchExtFactory to EPPCodec: "
					+ e);
		}
		
		// Get the appropriate smds directory.
		smdsDir = "../lib/smds";
		File smdsDirFile = new File(smdsDir);
		if (!smdsDirFile.exists() || !smdsDirFile.isDirectory()) {
			smdsDir = "../../lib/smds";
			smdsDirFile = new File(smdsDir);
			if (!smdsDirFile.exists() || !smdsDirFile.isDirectory()) {
				Assert.fail("EPPLaunchTst.suite(): Could not find the smds directory.");
			}
		}
		
		
		return suite;
	}

	/**
	 * Main method for running tests using stand alone program.
	 * 
	 * @param args
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
				TestThread thread = new TestThread("EPPLaunchTst Thread " + i,
						EPPLaunchTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPLaunchTst.suite());
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

	/**
	 * Loads the trust store file and the Certificate Revocation List (CRL) file
	 * into the <code>PKIXParameters</code> used to verify the certificate chain
	 * and verify the certificate against the CRL. Both the Java Trust Store is
	 * loaded with the trusted root CA certificates (trust anchors) and the CRL
	 * file is attempted to be loaded to identify the revoked certificates. If
	 * the CRL file is not found, then no CRL checking will be done.
	 * 
	 * @param aTrustStoreName
	 *            Trust store file name
	 * @param aCrls
	 *            List of Certificate Revocation List (CRL) file names
	 * 
	 * @return Initialized <code>PKIXParameters</code> instance.
	 * 
	 * @throws Exception
	 *             Error initializing the PKIX parameters
	 */
	public static PKIXParameters loadPKIXParameters(String aTrustStoreName,
			List<String> aCrls) throws Exception {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream trustStoreFile = new FileInputStream(aTrustStoreName);
		trustStore.load(trustStoreFile, null);
		PKIXParameters pkixParameters = new PKIXParameters(trustStore);

		CertificateFactory certFactory = CertificateFactory
				.getInstance("X.509");

		Collection crlContentsList = new ArrayList();

		for (String currCrl : aCrls) {
			File crlFile = new File(currCrl);
			if (crlFile.exists()) {
				InputStream inStream = null;

				try {
					inStream = new FileInputStream(currCrl);
					crlContentsList.add(certFactory.generateCRL(inStream));
				}
				finally {
					if (inStream != null) {
						inStream.close();
					}
				}
			}
			else {
				System.err.println("CRL file \"" + currCrl + "\" NOT found.");
			}

		}

		// At least 1 CRL was loaded
		if (crlContentsList.size() != 0) {

			List<CertStore> certStores = new ArrayList<CertStore>();
			certStores.add(CertStore.getInstance("Collection",
					new CollectionCertStoreParameters(crlContentsList)));

			pkixParameters.setCertStores(certStores);
			pkixParameters.setRevocationEnabled(true);
			System.out.println("Revocation enabled");
		}
		else {
			pkixParameters.setRevocationEnabled(false);
			System.out.println("Revocation disabled.");

		}

		return pkixParameters;
	}

	/**
	 * Loads the private key used to digitally sign from a Java KeyStore with
	 * the alias of the <code>PrivateKeyEntry</code> and the password to access
	 * the Keystore and the key.
	 * 
	 * @param aKeyStoreName
	 *            Java Keystore to load the key from
	 * @param aKeyAliasName
	 *            Java Keystore alias of key
	 * @param aPassword
	 *            Password to access Java Keystore and key
	 * 
	 * @return Loaded <code>KeyStore.PrivateKeyEntry</code> that can be used to
	 *         get the private key and it's associated certificate chain.
	 * 
	 * @throws Exception
	 *             Error loading private key
	 */
	private static KeyStore.PrivateKeyEntry loadPrivateKeyEntry(
			String aKeyStoreName, String aKeyAliasName, String aPassword)
			throws Exception {

		// Load KeyStore
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream keyStoreFile = new FileInputStream(aKeyStoreName);
		keyStore.load(keyStoreFile, aPassword.toCharArray());

		// Get Private Key
		assert keyStore.isKeyEntry(aKeyAliasName);
		KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore
				.getEntry(aKeyAliasName, new KeyStore.PasswordProtection(
						aPassword.toCharArray()));

		return keyEntry;
	}

	/**
	 * Loads the public key used to verify a digital signature signed with the
	 * associated private key, loaded by
	 * {@link #loadPrivateKeyEntry(String, String, String)}.
	 * 
	 * @param aKeyStoreName
	 *            Java Keystore containing the certificate
	 * @param aPublicKeyAlias
	 *            Java Keystore alias of the <code>trustedCertEntry</code>
	 *            containing the public key
	 * 
	 * @return Loaded <code>PublicKey</code> instance
	 * 
	 * @throws Exception
	 *             Error loading the public key
	 */
	public static PublicKey loadPublicKey(String aKeyStoreName,
			String aPublicKeyAlias) throws Exception {

		// Load KeyStore
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		FileInputStream keyStoreFile = new FileInputStream(aKeyStoreName);
		keyStore.load(keyStoreFile, null);

		assert keyStore.isCertificateEntry(aPublicKeyAlias);

		KeyStore.TrustedCertificateEntry certEntry = (KeyStore.TrustedCertificateEntry) keyStore
				.getEntry(aPublicKeyAlias, null);

		return certEntry.getTrustedCertificate().getPublicKey();
	}

}
