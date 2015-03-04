/***********************************************************
Copyright (C) 2007 VeriSign, Inc.

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
package com.verisign.epp.codec.jobscontact;

// JUNIT Imports
import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.contact.EPPContactAddChange;
import com.verisign.epp.codec.contact.EPPContactAddress;
import com.verisign.epp.codec.contact.EPPContactCreateCmd;
import com.verisign.epp.codec.contact.EPPContactDisclose;
import com.verisign.epp.codec.contact.EPPContactDiscloseAddress;
import com.verisign.epp.codec.contact.EPPContactDiscloseName;
import com.verisign.epp.codec.contact.EPPContactDiscloseOrg;
import com.verisign.epp.codec.contact.EPPContactInfoCmd;
import com.verisign.epp.codec.contact.EPPContactInfoResp;
import com.verisign.epp.codec.contact.EPPContactPostalDefinition;
import com.verisign.epp.codec.contact.EPPContactStatus;
import com.verisign.epp.codec.contact.EPPContactUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.jobscontact package. The unit
 * test will execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.jobscontact package. The unit test is dependent
 * on the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 */
public class EPPJobsContactTst extends TestCase {
	private static long numIterations = 1;

	/**
	 * Creates a new EPPJobsContactTst object.
	 * 
	 * @param name
	 *            Name of test to execute
	 */
	public EPPJobsContactTst(String name) {
		super(name);
	}

	
	/**
	 * Tests the <code>EPPJobsContact</code> create command extension.
	 */
	public void testJobsContactCreate() {

		EPPCodecTst.printStart("testJobsContactCreate");

		// -- Contact create command
		EPPContactCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testContactCreate");

		// Streets
		Vector streets = new Vector();
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address = new EPPContactAddress(null, "Dulles", "VA",
				"20166-6503", "US");

		// Postal Definition
		EPPContactPostalDefinition name = new EPPContactPostalDefinition(
				"John Doe", "Example Inc.",
				EPPContactPostalDefinition.ATTR_TYPE_LOC, address);

		// Contact Command
		theCommand = new EPPContactCreateCmd("ABC-12345", "sh8013", name,
				"jdoe@example.com", new EPPAuthInfo("2fooBAR"));
		theCommand.setFax("+1.7035555556");
		theCommand.setFaxExt("123");
		theCommand.setVoice("+1.7035555555");
		theCommand.setVoiceExt("456");

		// disclose names
		Vector names = new Vector();

		// names.addElement(new
		// EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(
				EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("0");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		theCommand.setDisclose(disclose);

		theCommand.addExtension(new EPPJobsContactCreateCmd("create-title", "website",
				"IT", "Yes", "Yes"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPCodecTst.printEnd("testJobsContactCreate");
	}

	/**
	 * Tests the <code>EPPJobsContact</code> info command extension.
	 */
	public void testJobsContactUpdate() {

		EPPCodecTst.printStart("testJobsContactUpdate");

		EPPContactUpdateCmd theCommand = null;
		EPPEncodeDecodeStats commandStats = null;

		EPPCodecTst.printStart("testContactUpate");

		// Streets
		Vector streets = new Vector();

		// fixed to make address element optional in CodecTest
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address = new EPPContactAddress(streets, "Dulles",
				"VA", "20166-6503", "US");

		// Change Definition
		EPPContactPostalDefinition name = new EPPContactPostalDefinition(
				"John", EPPContactPostalDefinition.ATTR_TYPE_LOC, address);

		// Contact Change
		EPPContactAddChange change = new EPPContactAddChange(name,
				"+1.7034444444", new EPPAuthInfo("2BARfoo"));

		change.setVoiceExt("678");
		change.setFax("+1.7037777777");
		change.setFaxExt("678");

		// disclose names
		Vector names = new Vector();

		// names.addElement(new
		// EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(
				EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("0");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		change.setDisclose(disclose);

		// statuses
		Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPContactStatus(
				EPPContactStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED));
		addStatuses.addElement(new EPPContactStatus(
				EPPContactStatus.ELM_STATUS_CLIENT_DELETE_PROHIBITED,
				"Hello World", EPPContactStatus.ELM_DEFAULT_LANG));

		// Contact Add
		EPPContactAddChange add = new EPPContactAddChange(addStatuses);

		// Update Command
		theCommand = new EPPContactUpdateCmd("ABC-12345", "sh8013", add, null,
				change);

		theCommand.addExtension(new EPPJobsContactUpdateCmd("update-title", "website",
				"IT", "Yes", "Yes"));
		
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);
		EPPCodecTst.printEnd("testJobsContactUpdate");
	}

	/**
	 * Tests the <code>EPPJobsContact</code> info command extension.
	 */
	public void testJobsContactInfo() {

		EPPCodecTst.printStart("testJobsContactInfo");
		EPPContactInfoResp theResponse;

		// Encode EPPContactInfo Response
		EPPEncodeDecodeStats responseStats;

		// Streets
		Vector streets = new Vector();
		streets.addElement("123 Example Dr.");
		streets.addElement("Suite 100");

		// Address
		EPPContactAddress address = new EPPContactAddress(streets, "Dulles",
				"VA", "20166-6503", "US");

		// Name Postal Definition
		EPPContactPostalDefinition name = new EPPContactPostalDefinition(
				"John Doe", "Example Inc.",
				EPPContactPostalDefinition.ATTR_TYPE_LOC, address);

		// i15d Streets
		Vector i15dStreets = new Vector();
		i15dStreets.addElement("i15d 123 Example Dr.");
		i15dStreets.addElement("i15d Suite 100");

		// Address
		EPPContactAddress i15dAddress = new EPPContactAddress(i15dStreets,
				"Dulles", "VA", "20166-6503", "US");

		// Name Postal Definition
		EPPContactPostalDefinition i15dName = new EPPContactPostalDefinition(
				"i15d John Doe", "i15d Example Inc.",
				EPPContactPostalDefinition.ATTR_TYPE_INT, i15dAddress);

		// infoStatuses
		Vector infoStatuses = new Vector();
		infoStatuses.addElement(new EPPContactStatus(
				EPPContactStatus.ELM_STATUS_LINKED));
		infoStatuses.addElement(new EPPContactStatus(
				EPPContactStatus.ELM_STATUS_CLIENT_UPDATE_PROHIBITED));

		// Test with just required EPPContactInfoResp attributes.
		EPPTransId respTransId = new EPPTransId("144", "54321-XYZ");
		theResponse = new EPPContactInfoResp(respTransId, "SH8013-VRSN",
				"sh8013", infoStatuses, name, "jdoe@example.com", "ClientY",
				"ClientX", new Date(), new EPPAuthInfo("2fooBAR"));

		theResponse.setVoice("+1.7035555555");
		// theResponse.setVoiceExt("123");
		theResponse.setFax("+1.7035555556");
		// theResponse.setFaxExt("456");

		theResponse.addPostalInfo(i15dName);

		// disclose names
		Vector names = new Vector();

		// names.addElement(new
		// EPPContactDiscloseName(EPPContactDiscloseName.ATTR_TYPE_LOC));
		names.addElement(new EPPContactDiscloseName(
				EPPContactDiscloseName.ATTR_TYPE_INT));

		// disclose orgs
		Vector orgs = new Vector();
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_LOC));
		orgs.addElement(new EPPContactDiscloseOrg(
				EPPContactDiscloseOrg.ATTR_TYPE_INT));

		// disclose addresses
		Vector addresses = new Vector();
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_LOC));
		addresses.addElement(new EPPContactDiscloseAddress(
				EPPContactDiscloseAddress.ATTR_TYPE_INT));

		// disclose
		EPPContactDisclose disclose = new EPPContactDisclose();
		disclose.setFlag("1");
		disclose.setNames(names);
		disclose.setOrgs(orgs);
		disclose.setAddresses(addresses);
		disclose.setVoice("");
		disclose.setFax("");
		disclose.setEmail("");

		theResponse.setDisclose(disclose);

		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.addExtension(new EPPJobsContactInfoResp("Info-title",
				"whois.example.com", "IT", "Yes", "Yes"));

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Test with all EPPContactInfoResp attributes set.
		theResponse.setLastUpdatedBy("ClientX");
		theResponse.setLastUpdatedDate(new Date());
		theResponse.setLastUpdatedDate(new Date());

		EPPCodecTst.printEnd("testJobsContactInfo");
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

		TestSuite suite = new TestSuite(EPPJobsContactTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.contact.EPPContactMapFactory");
			EPPFactory
					.getInstance()
					.addExtFactory(
							"com.verisign.epp.codec.jobscontact.EPPJobsContactExtFactory");
		} catch (EPPCodecException e) {
			Assert
					.fail("EPPCodecException adding EPPContactMapFactory or EPPJobsContactExtFactory to EPPCodec: "
							+ e);
		}
		return suite;
	}

	// End EPPJobsContactTst.suite()

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
				TestThread thread = new TestThread("EPPJobsContactTst Thread "
						+ i, EPPJobsContactTst.suite());
				thread.start();
			}
		} else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPJobsContactTst.suite());
		}
	}

	// End EPPJobsContactTst.main(String [])

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
