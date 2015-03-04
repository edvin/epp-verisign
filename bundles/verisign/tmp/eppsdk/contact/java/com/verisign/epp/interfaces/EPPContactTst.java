/***********************************************************
 Copyright (C) 2004 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.interfaces;

// JUNIT Imports
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.contact.EPPContactAddress;
import com.verisign.epp.codec.contact.EPPContactCheckResp;
import com.verisign.epp.codec.contact.EPPContactCheckResult;
import com.verisign.epp.codec.contact.EPPContactDisclose;
import com.verisign.epp.codec.contact.EPPContactDiscloseAddress;
import com.verisign.epp.codec.contact.EPPContactDiscloseName;
import com.verisign.epp.codec.contact.EPPContactDiscloseOrg;
import com.verisign.epp.codec.contact.EPPContactInfoResp;
import com.verisign.epp.codec.contact.EPPContactPostalDefinition;
import com.verisign.epp.codec.contact.EPPContactTransferResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the <code>EPPContact</code> class. The unit test will
 * initialize a session with an EPP Server, will invoke <code>EPPContact</code>
 * operations, and will end a session with an EPP Server. The configuration file
 * used by the unit test defaults to epp.config, but can be changed by passing
 * the file path as the first command line argument. The unit test can be run in
 * multiple threads by setting the "threads" system property. For example, the
 * unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command:<br>
 * <br>
 * java com.verisign.epp.interfaces.EPPContact -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * <br>
 * <br>
 * <br>
 * 
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 */
public class EPPContactTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance (<code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat = Logger.getLogger(EPPContactTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** EPP Contact associated with test */
	private EPPContact contact = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Current test iteration */
	private int iteration = 0;

	/** Need this for testing */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPContactTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public EPPContactTst(String name) {
		super(name);
	}

	// End EPPContactTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPContactTst TestCase</code>.
	 * Each sub-test will be invoked in order to satisfy testing the EPPContact
	 * interface.
	 */
	public void testContact() {
		int numIterations = 1;
		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}

		for (iteration = 0; (numIterations == 0) || (iteration < numIterations); iteration++) {
			printStart("Test Suite");

			contactCheck();
			contactCreate();
			contactInfo();
			contactUpdate();
			contactTransfer();
			contactDelete();

			printEnd("Test Suite");
		}
	}

	// End EPPContactTst.testContact()

	/**
	 * Unit test of <code>EPPContact.sendCheck</code>.
	 */
	private void contactCheck() {
		printStart("contactCheck");

		EPPContactCheckResp response;

		String theName = this.makeContactName();

		try {
			// Check single contact name
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("contactCheck: Check single contact id "
					+ theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(theName);

			response = contact.sendCheck();

			// Output all of the response attributes
			System.out.println("\ncontactCheck: Response = [" + response + "]");

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPContactCheckResult currResult = (EPPContactCheckResult) response
						.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("contactCheck: Contact "
							+ currResult.getId() + " is available");
				}
				else {
					System.out.println("contactCheck: Contact "
							+ currResult.getId() + " is unavailable");
				}
			}
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			// Check multiple contact names
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("contactCheck: Check multiple contact ids");

			contact.setTransId("ABC-12345-XYZ");

			for (int i = 0; i <= 20; i++) {
				contact.addContactId(this.makeContactName());
			}

			response = contact.sendCheck();

			// Output all of the response attributes
			System.out.println("\ncontactCheck: Response = [" + response + "]");

			// Get ping result information
			Enumeration results = response.getResults().elements();

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPContactCheckResult currResult = (EPPContactCheckResult) response
						.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("contactCheck: Contact "
							+ currResult.getId() + " is available");
				}
				else {
					System.out.println("contactCheck: Contact "
							+ currResult.getId() + " is unavailable");
				}
			}
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactCheck");
	}

	// End EPPContactTst.contactCheck()

	/**
	 * Unit test of <code>EPPContact.sendInfo</code>.
	 */
	public void contactInfo() {
		printStart("contactInfo");

		EPPContactInfoResp response;

		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("\ncontactInfo: Contact info for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(theName);

			response = contact.sendInfo();

			// -- Output all of the response attributes
			System.out
					.println("contactInfo: Response = [" + response + "]\n\n");

			// -- Output required response attributes using accessors
			System.out.println("contactInfo: id = " + response.getId());

			Vector postalContacts = null;

			if (response.getPostalInfo().size() > 0) {
				postalContacts = response.getPostalInfo();

				for (int j = 0; j < postalContacts.size(); j++) {
					// Name
					System.out.println("contactInfo:\t\tname = "
							+ ((EPPContactPostalDefinition) postalContacts
									.elementAt(j)).getName());

					// Organization
					System.out.println("contactInfo:\t\torganization = "
							+ ((EPPContactPostalDefinition) postalContacts
									.elementAt(j)).getOrg());

					EPPContactAddress address = ((EPPContactPostalDefinition) postalContacts
							.elementAt(j)).getAddress();

					for (int i = 0; i < address.getStreets().size(); i++) {
						System.out.println("contactInfo:\t\tstreet" + (i + 1)
								+ " = " + address.getStreets().elementAt(i));
					}

					// Address City
					System.out.println("contactInfo:\t\tcity = "
							+ address.getCity());

					// Address State/Province
					System.out.println("contactInfo:\t\tstate province = "
							+ address.getStateProvince());

					// Address Postal Code
					System.out.println("contactInfo:\t\tpostal code = "
							+ address.getPostalCode());

					// Address County
					System.out.println("contactInfo:\t\tcountry = "
							+ address.getCountry());
				}
			}

			// Contact E-mail
			System.out.println("contactInfo:\temail = " + response.getEmail());

			// Contact Voice
			System.out.println("contactInfo:\tvoice = " + response.getVoice());

			// Contact Voice Extension
			System.out.println("contactInfo:\tvoice ext = "
					+ response.getVoiceExt());

			// Contact Fax
			System.out.println("contactInfo:\tfax = " + response.getFax());

			// Contact Fax Extension
			System.out.println("contactInfo:\tfax ext = "
					+ response.getFaxExt());

			// Client Id
			System.out.println("contactInfo: client id = "
					+ response.getClientId());

			// Created By
			System.out.println("contactInfo: created by = "
					+ response.getCreatedBy());

			// Created Date
			System.out.println("contactInfo: create date = "
					+ response.getCreatedDate());

			// -- Output optional response attributes using accessors
			// Contact Fax
			if (response.getFax() != null) {
				System.out.println("contactInfo:\tfax = " + response.getFax());
			}

			// Contact Voice
			if (response.getVoice() != null) {
				System.out.println("contactInfo:\tVoice = "
						+ response.getVoice());
			}

			// Last Updated By
			if (response.getLastUpdatedBy() != null) {
				System.out.println("contactInfo: last updated by = "
						+ response.getLastUpdatedBy());
			}

			// Last Updated Date
			if (response.getLastUpdatedDate() != null) {
				System.out.println("contactInfo: last updated date = "
						+ response.getLastUpdatedDate());
			}

			// Last Transfer Date
			if (response.getLastTransferDate() != null) {
				System.out.println("contactInfo: last updated date = "
						+ response.getLastTransferDate());
			}

			// Authorization Id
			if (response.getAuthInfo() != null) {
				System.out.println("contactInfo: authorization info = "
						+ response.getAuthInfo().getPassword());
			}

			// Disclose
			if (response.getDisclose() != null) {
				System.out.println("contactInfo: disclose info = "
						+ response.getDisclose());
			}
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactInfo");
	}

	// End EPPContactTst.contactInfo()

	/**
	 * Unit test of <code>EPPContact.sendCreate</code>.
	 */
	public void contactCreate() {
		printStart("contactCreate");

		EPPResponse response;

		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("contactCreate: Contact create for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.setAuthorizationId("ClientXYZ");
			contact.addContactId(theName);
			contact.setVoicePhone("+1.7035555555");
			contact.setVoiceExt("123");
			contact.setFaxNumber("+1.7035555556");
			contact.setFaxExt("456");
			contact.setEmail("jdoe@example.com");

			// Streets
			Vector streets = new Vector();
			streets.addElement("123 Example Dr.");
			streets.addElement("Suite 100");
			streets.addElement("This is third line");

			EPPContactAddress address = new EPPContactAddress();

			address.setStreets(streets);
			address.setCity("Dulles");
			address.setStateProvince("VA");
			address.setPostalCode("20166-6503");
			address.setCountry("US");

			EPPContactPostalDefinition name = new EPPContactPostalDefinition(
					EPPContactPostalDefinition.ATTR_TYPE_LOC);

			name.setName("John Doe");
			name.setOrg("Example Inc.");
			name.setAddress(address);

			contact.addPostalInfo(name);

			// this is not a valid Example but it will do
			EPPContactAddress Intaddress = new EPPContactAddress();

			Intaddress.setStreets(streets);
			Intaddress.setCity("Dulles");
			Intaddress.setStateProvince("VA");
			Intaddress.setPostalCode("20166-6503");
			Intaddress.setCountry("US");

			EPPContactPostalDefinition Intname = new EPPContactPostalDefinition(
					EPPContactPostalDefinition.ATTR_TYPE_INT);

			Intname.setName("John Doe");
			Intname.setOrg("Example Inc.");
			Intname.setAddress(Intaddress);

			contact.addPostalInfo(Intname);

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

			contact.setDisclose(disclose);

			response = contact.sendCreate();

			// -- Output all of the response attributes
			System.out.println("contactCreate: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactCreate");
	}

	// End EPPContactTst.contactCreate()

	/**
	 * Unit test of <code>EPPContact.sendDelete</code>.
	 */
	public void contactDelete() {
		printStart("contactDelete");

		EPPResponse response;

		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactDelete: Contact delete for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(theName);

			response = contact.sendDelete();

			// -- Output all of the response attributes
			System.out.println("contactDelete: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactDelete");
	}

	// End EPPContactTst.contactDelete()

	/**
	 * Unit test of <code>EPPContact.sendUpdate</code>.
	 */
	public void contactUpdate() {
		printStart("contactUpdate");

		EPPResponse response;

		String theName = this.makeContactName();

		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactDelete: Contact update for " + theName);
			contact.setTransId("ABC-12345-XYZ");
			contact.addContactId(theName);

			// Streets
			Vector streets = new Vector();
			streets.addElement("123 Example Dr.");
			streets.addElement("Suite 100");
			streets.addElement("This is third line");

			// Address
			EPPContactAddress address = new EPPContactAddress(streets,
					"Dulles", "VA", "20166-6503", "US");

			EPPContactPostalDefinition postal = new EPPContactPostalDefinition(
					"Joe Brown", "Example Corp.",
					EPPContactPostalDefinition.ATTR_TYPE_LOC, address);

			// statuses
			contact.addStatus(EPPContact.STAT_PENDING_DELETE);

			contact.addPostalInfo(postal);
			contact.setVoicePhone("+1.7035555555");
			contact.setVoiceExt("456");
			contact.setFaxNumber("+1.7035555555");
			contact.setFaxExt("789");
			contact.setAuthorizationId("ClientXYZ");

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

			contact.setDisclose(disclose);

			response = contact.sendUpdate();

			// -- Output all of the response attributes
			System.out.println("contactUpdate: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactUpdate");
	}

	// End EPPContactTst.contactUpdate()

	/**
	 * Unit test of <code>EPPContact.sendTransfer</code> for a transfer query.
	 */
	public void contactTransfer() {
		printStart("contactTransfer");

		EPPContactTransferResp response;

		String theName = this.makeContactName();

		// Transfer Request
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactTransfer: Contact transfer request for "
							+ theName);
			contact.setTransferOpCode(EPPContact.TRANSFER_REQUEST);
			contact.setTransId("ABC-12345-XYZ");
			contact.setAuthorizationId("ClientX");
			contact.addContactId(this.makeContactName());

			// Execute the transfer request
			response = contact.sendTransfer();

			// -- Output all of the response attributes
			System.out.println("contactTransfer: Response = [" + response
					+ "]\n\n");

			// -- Output required response attributes using accessors
			System.out.println("contactTransfer: id = " + response.getId());
			System.out.println("contactTransfer: request client = "
					+ response.getRequestClient());
			System.out.println("contactTransfer: action client = "
					+ response.getActionClient());
			System.out.println("contactTransfer: transfer status = "
					+ response.getTransferStatus());
			System.out.println("contactTransfer: request date = "
					+ response.getRequestDate());
			System.out.println("contactTransfer: action date = "
					+ response.getActionDate());
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Query
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("\ncontactTransfer: Contact transfer query for "
					+ theName);
			contact.setTransferOpCode(EPPContact.TRANSFER_QUERY);
			contact.addContactId(this.makeContactName());

			// Execute the transfer query
			response = contact.sendTransfer();

			// -- Output all of the response attributes
			System.out.println("contactTransfer: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Cancel
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactTransfer: Contact transfer cancel for "
							+ theName);
			contact.setTransferOpCode(EPPContact.TRANSFER_CANCEL);
			contact.addContactId(this.makeContactName());

			// Execute the transfer cancel
			response = contact.sendTransfer();

			// -- Output all of the response attributes
			System.out.println("contactTransfer: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Reject
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactTransfer: Contact transfer reject for "
							+ theName);
			contact.setTransferOpCode(EPPContact.TRANSFER_REJECT);
			contact.addContactId(this.makeContactName());

			// Execute the transfer reject
			response = contact.sendTransfer();

			// -- Output all of the response attributes
			System.out.println("contactTransfer: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Approve
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out
					.println("\ncontactTransfer: Contact transfer approve for "
							+ theName);
			contact.setTransferOpCode(EPPContact.TRANSFER_APPROVE);
			contact.addContactId(this.makeContactName());

			// Execute the transfer approve
			response = contact.sendTransfer();

			// -- Output all of the response attributes
			System.out.println("contactTransfer: Response = [" + response
					+ "]\n\n");
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			// Handle transfer poll messages
			EPPResponse pollResponse;
			do {
				session.setTransId("CONTACT-POLL");
				session.setPollOp(EPPSession.OP_REQ);

				pollResponse = session.sendPoll();
				System.out.println("contactTransfer: Poll Response = ["
						+ pollResponse + "]\n\n");

				// Need to ack the message?
				if (pollResponse.getResult().getCode() == EPPResult.SUCCESS_POLL_MSG) {
					session.setPollOp(EPPSession.OP_ACK);
					session.setMsgID(pollResponse.getMsgQueue().getId());
					EPPResponse pollAckResponse = session.sendPoll();

					System.out.println("contactTransfer: Poll Ack Response = ["
							+ pollAckResponse + "]\n\n");

				}
			}
			while (pollResponse.getResult().getCode() == EPPResult.SUCCESS_POLL_MSG);
		}
		catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("contactTransfer");
	}

	// End EPPContactTstcontactTransfer()

	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session
	 * attribute is initialized with the attributes defined in the EPP sample
	 * files.
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

	// End EPPContactTst.initSession()

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
			else {
				e.printStackTrace();
				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("endSession");
	}

	// End EPPContactTst.endSession()

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
					Assert
							.fail("Exception instantiating EPP.SessionClass value "
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

		contact = new EPPContact(session);
	}

	// End EPPContactTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPContactTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPContact</code>.
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPContactTst.class);

		String theConfigFileName = System.getProperty("EPP.ConfigFile");

		if (theConfigFileName != null) {
			configFileName = theConfigFileName;
		}

		try {
			app.initialize(configFileName);
		}
		catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error initializing the EPP Application: " + e);
		}

		return suite;
	}

	// End EPPContactTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a
	 * server generated error or a general exception. If the exception was
	 * caused by a server error, "Server Error : &lt;Response XML&gt;" will be
	 * specified. If the exception was caused by a general algorithm error,
	 * "General Error : &lt;Exception Description&gt;" will be specified.
	 * 
	 * @param aException
	 *            Exception thrown during test
	 */
	public void handleException(EPPCommandException aException) {
		EPPResponse theResponse = null;
		if (aException instanceof EPPCommandException) {
			theResponse = ((EPPCommandException) aException).getResponse();
		}
		
		aException.printStackTrace();

		// Is a server specified error?
		if ((theResponse != null) && (!theResponse.isSuccess())) {
			Assert.fail("Server Error : " + theResponse);
		}

		else {
			Assert.fail("General Error : " + aException);
		}
	}

	// End EPPContactTst.handleException(EPPCommandException)

	/**
	 * Unit test main, which accepts the following system property options:<br>
	 * 
	 * <ul>
	 * <li> iterations Number of unit test iterations to run </li>
	 * <li> validate Turn XML validation on (<code>true</code>) or off (<code>false</code>).
	 * If validate is not specified, validation will be off. </li>
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
				TestThread thread = new TestThread("EPPSessionTst Thread " + i,
						EPPContactTst.suite());
				thread.start();
			}
		}
		else {
			junit.textui.TestRunner.run(EPPContactTst.suite());
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
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 * 
	 * @return DOCUMENT ME!
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
				+ String.valueOf(tm + rd.nextInt(12)).substring(10) + ".com");
	}

	/**
	 * This method tries to generate a unique IP address
	 * 
	 * @return DOCUMENT ME!
	 */
	public String makeIP() {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(50)).substring(10)
				+ "." + String.valueOf(tm + rd.nextInt(50)).substring(10) + "."
				+ String.valueOf(tm + rd.nextInt(50)).substring(10) + "."
				+ String.valueOf(tm + rd.nextInt(50)).substring(10));
	}

	/**
	 * This method tries to generate a unique Host Name for a given Domain Name
	 * 
	 * @param newDomainName
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public String makeHostName(String newDomainName) {
		long tm = System.currentTimeMillis();

		return new String(String.valueOf(tm + rd.nextInt(10)).substring(10)
				+ "." + newDomainName);
	}

	/**
	 * This method tries to generate a unique String as contact Name
	 * 
	 * @return DOCUMENT ME!
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
	 * @param aTest
	 *            name for the test
	 */
	private void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	}

	// End EPPContactTst.printStart(String)

	/**
	 * Print the end of a test with the <code>Thread</code> name if the
	 * current thread is a <code>TestThread</code>.
	 * 
	 * @param aTest
	 *            name for the test
	 */
	private void printEnd(String aTest) {
		System.out
				.println("****************************************************************");

		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": ");
			cat.info(Thread.currentThread().getName() + ", iteration "
					+ iteration + ": " + aTest + " End");
		}

		System.out.println("End of " + aTest);
		System.out.println("\n");
	}

	// End EPPContactTst.testEnd(String)

	/**
	 * Print message
	 * 
	 * @param aMsg
	 *            message to print
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

	// End EPPContactTst.printMsg(String)

	/**
	 * Print error message
	 * 
	 * @param aMsg
	 *            errpr message to print
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

	// End EPPContactTst.printError(String)
}

// End class EPPContactTst
