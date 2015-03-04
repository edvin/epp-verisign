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

import java.util.Random;
import java.util.Vector;

import junit.extensions.TestSetup;
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
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.interfaces.EPPApplicationSingle;
import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPContact;
import com.verisign.epp.interfaces.EPPSession;
import com.verisign.epp.pool.EPPSessionPool;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.InvalidateSessionException;
import com.verisign.epp.util.TestThread;
import com.verisign.epp.util.TestUtil;

/**
 * Test of the use of the <code>NSContact</code> interface. This test utilizes
 * the EPP session pool and exercises all of the operations defined in
 * <code>NSContact</code> and the base class <code>EPPContact</code>.
 * 
 * @see com.verisign.epp.namestore.interfaces.NSContact
 * @see com.verisign.epp.interfaces.EPPContact
 */
public class NSContactTst extends TestCase {

	/**
	 * Handle to the Singleton EPP Application instance (
	 * <code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat = Logger.getLogger(NSContactTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** EPP Session pool associated with test */
	private static EPPSessionPool sessionPool = null;

	/**
	 * Random instance for the generation of unique objects (contacts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>NSContactTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with the
	 * logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public NSContactTst(String name) {
		super(name);
	}

	/**
	 * Unit test of <code>NSContact.sendCreate</code> command.
	 */
	public void testContactCreate() {
		printStart("testContactCreate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;

		try {
			theSession = this.borrowSession();
			NSContact theContact = new NSContact(theSession);

			try {
				System.out
						.println("\n----------------------------------------------------------------");

				String theContactName = this.makeContactName();

				System.out.println("testContactCreate: Create " + theContactName);

				theContact.setTransId("ABC-12345-XYZ");
				theContact.setAuthorizationId("ClientXYZ");
				theContact.addContactId(theContactName);
				theContact.setVoicePhone("+1.7035555555");
				theContact.setVoiceExt("123");
				theContact.setFaxNumber("+1.7035555556");
				theContact.setFaxExt("456");
				theContact.setEmail("jdoe@example.com");

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

				theContact.addPostalInfo(name);

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

				theContact.addPostalInfo(Intname);

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

				theContact.setDisclose(disclose);

				theResponse = theContact.sendCreate();

				// -- Output all of the response attributes
				System.out.println("testContactCreate: Response = [" + theResponse
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
			if (theSession != null)
				this.returnSession(theSession);
		}

		printEnd("testContactCreate");
	}

	/**
	 * Unit test of <code>NSContact.sendContactCheck</code> command.
	 */
	public void testContactCheck() {
		printStart("testContactCheck");

		EPPSession theSession = null;
		EPPContactCheckResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSContact theContact = new NSContact(theSession);

			try {

				System.out
						.println("\n----------------------------------------------------------------");

				String theContactName = this.makeContactName();
				System.out.println("testContactCheck: Check single contact id ("
						+ theContactName + ")");
				theContact.setTransId("ABC-12345-XYZ");

				theContact.addContactId(theContactName);
				theContact.setSubProductID(NSSubProduct.COM);

				theResponse = theContact.sendCheck();

				System.out.println("Response Type = " + theResponse.getType());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getServerTransId());

				System.out.println("Response.TransId.ServerTransId = "
						+ theResponse.getTransId().getClientTransId());

				// Output all of the response attributes
				System.out.println("\ntestContactCheck: Response = [" + theResponse
						+ "]");

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPContactCheckResult currResult = (EPPContactCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("testContactCheck: Contact "
								+ currResult.getId() + " is available");
					}
					else {
						System.out.println("testContactCheck: Contact "
								+ currResult.getId() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (Exception e) {
				TestUtil.handleException(theSession, e);
			}

			try {
				// Check multiple contact names
				System.out
						.println("\n----------------------------------------------------------------");
				System.out
						.println("testContactCheck: Check multiple contact names (ns1.example.com, ns2.example.com, ns3.example.com)");
				theContact.setTransId("ABC-12345-XYZ");

				/**
				 * Add multiple contact ids
				 */
				for (int i = 0; i <= 10; i++) {
					theContact.addContactId(this.makeContactName());
				}
				theContact.setSubProductID(NSSubProduct.COM);

				theResponse = theContact.sendCheck();

				// Output all of the response attributes
				System.out.println("\ntestContactCheck: Response = [" + theResponse
						+ "]");
				System.out.println("Client Transaction Id = "
						+ theResponse.getTransId().getClientTransId());
				System.out.println("Server Transaction Id = "
						+ theResponse.getTransId().getServerTransId());

				// For each result
				for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
					EPPContactCheckResult currResult = (EPPContactCheckResult) theResponse
							.getCheckResults().elementAt(i);

					if (currResult.isAvailable()) {
						System.out.println("testContactCheck: Contact "
								+ currResult.getId() + " is available");
					}
					else {
						System.out.println("testContactCheck: Contact "
								+ currResult.getId() + " is not available");
					}
				}

				this.handleResponse(theResponse);
			}
			catch (EPPCommandException e) {
				TestUtil.handleException(theSession, e);
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

		printEnd("testContactCheck");
	}

	/**
	 * Unit test of <code>NSContact.sendContactInfo</code> command.
	 */
	public void testContactInfo() {
		printStart("testContactInfo");

		EPPSession theSession = null;
		EPPContactInfoResp theResponse = null;
		try {
			theSession = this.borrowSession();
			NSContact theContact = new NSContact(theSession);

			try {
				System.out.println("\ntestContactInfo: Contact info");

				theContact.setTransId("ABC-12345-XYZ");

				theContact.addContactId(this.makeContactName());
				theContact.setSubProductID(NSSubProduct.COM);

				theResponse = theContact.sendInfo();

				// -- Output all of the response attributes
				System.out
						.println("testContactInfo: Response = [" + theResponse + "]\n\n");

				// -- Output required response attributes using accessors
				System.out.println("testContactInfo: id = " + theResponse.getId());

				Vector postalContacts = null;

				if (theResponse.getPostalInfo().size() > 0) {
					postalContacts = theResponse.getPostalInfo();

					for (int j = 0; j < postalContacts.size(); j++) {
						// Name
						System.out.println("testContactInfo:\t\tname = "
								+ ((EPPContactPostalDefinition) postalContacts
										.elementAt(j)).getName());

						// Organization
						System.out.println("testContactInfo:\t\torganization = "
								+ ((EPPContactPostalDefinition) postalContacts
										.elementAt(j)).getOrg());

						EPPContactAddress address = ((EPPContactPostalDefinition) postalContacts
								.elementAt(j)).getAddress();

						for (int i = 0; i < address.getStreets().size(); i++) {
							System.out.println("testContactInfo:\t\tstreet" + (i + 1)
									+ " = " + address.getStreets().elementAt(i));
						}

						// Address City
						System.out.println("testContactInfo:\t\tcity = "
								+ address.getCity());

						// Address State/Province
						System.out.println("testContactInfo:\t\tstate province = "
								+ address.getStateProvince());

						// Address Postal Code
						System.out.println("testContactInfo:\t\tpostal code = "
								+ address.getPostalCode());

						// Address County
						System.out.println("testContactInfo:\t\tcountry = "
								+ address.getCountry());
					}
				}

				// Contact E-mail
				System.out.println("testContactInfo:\temail = " + theResponse.getEmail());

				// Contact Voice
				System.out.println("testContactInfo:\tvoice = " + theResponse.getVoice());

				// Contact Voice Extension
				System.out.println("testContactInfo:\tvoice ext = "
						+ theResponse.getVoiceExt());

				// Contact Fax
				System.out.println("testContactInfo:\tfax = " + theResponse.getFax());

				// Contact Fax Extension
				System.out.println("testContactInfo:\tfax ext = "
						+ theResponse.getFaxExt());

				// Client Id
				System.out.println("testContactInfo: client id = "
						+ theResponse.getClientId());

				// Created By
				System.out.println("testContactInfo: created by = "
						+ theResponse.getCreatedBy());

				// Created Date
				System.out.println("testContactInfo: create date = "
						+ theResponse.getCreatedDate());

				// -- Output optional response attributes using accessors
				// Contact Fax
				if (theResponse.getFax() != null) {
					System.out.println("testContactInfo:\tfax = " + theResponse.getFax());
				}

				// Contact Voice
				if (theResponse.getVoice() != null) {
					System.out.println("testContactInfo:\tVoice = "
							+ theResponse.getVoice());
				}

				// Last Updated By
				if (theResponse.getLastUpdatedBy() != null) {
					System.out.println("testContactInfo: last updated by = "
							+ theResponse.getLastUpdatedBy());
				}

				// Last Updated Date
				if (theResponse.getLastUpdatedDate() != null) {
					System.out.println("testContactInfo: last updated date = "
							+ theResponse.getLastUpdatedDate());
				}

				// Last Transfer Date
				if (theResponse.getLastTransferDate() != null) {
					System.out.println("testContactInfo: last updated date = "
							+ theResponse.getLastTransferDate());
				}

				// Authorization Id
				if (theResponse.getAuthInfo() != null) {
					System.out.println("testContactInfo: authorization info = "
							+ theResponse.getAuthInfo().getPassword());
				}

				// Disclose
				if (theResponse.getDisclose() != null) {
					System.out.println("testContactInfo: disclose info = "
							+ theResponse.getDisclose());
				}

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

		printEnd("testContactInfo");
	}

	/**
	 * Unit test of <code>NSContact.sendDelete</code> command.
	 */
	public void testContactDelete() {
		printStart("testContactDelete");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSContact theContact = new NSContact(theSession);

			try {
				System.out.println("\ntestContactDelete: Contact delete");

				theContact.setTransId("ABC-12345-XYZ");

				theContact.addContactId(this.makeContactName());
				theContact.setSubProductID(NSSubProduct.COM);

				theResponse = theContact.sendDelete();

				// -- Output all of the response attributes
				System.out.println("testContactDelete: Response = [" + theResponse
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

		printEnd("testContactDelete");
	}

	/**
	 * Unit test of <code>NSContact.sendUpdate</code> command.
	 */
	public void testContactUpdate() {
		printStart("testContactUpdate");

		EPPSession theSession = null;
		EPPResponse theResponse = null;
		try {
			theSession = this.borrowSession();
			NSContact theContact = new NSContact(theSession);

			try {

				theContact.setTransId("ABC-12345-XYZ");

				String theContactName = this.makeContactName();

				System.out.println("\ncontactUpdate: Contact " + theContactName
						+ " update");

				theContact.addContactId(theContactName);
				theContact.setSubProductID(NSSubProduct.COM);

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
				theContact.addStatus(EPPContact.STAT_PENDING_DELETE);

				theContact.addPostalInfo(postal);
				theContact.setVoicePhone("+1.7035555555");
				theContact.setVoiceExt("456");
				theContact.setFaxNumber("+1.7035555555");
				theContact.setFaxExt("789");
				theContact.setAuthorizationId("ClientXYZ");

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

				theContact.setDisclose(disclose);

				// Execute update
				theResponse = theContact.sendUpdate();

				// -- Output all of the response attributes
				System.out.println("contactUpdate: Response = [" + theResponse
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

		printEnd("testContactUpdate");
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
			if (theSession != null)
				this.returnSession(theSession);
		}

		printEnd("testEndSession");
	}

	/**
	 * JUNIT <code>setUp</code> method
	 */
	protected void setUp() {

	}

	// End NSContactTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>NSContactTst</code>.
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		return new NSContactTstSetup(new TestSuite(NSContactTst.class));
	}

	/**
	 * Setup framework from running NSContactTst tests.
	 */
	private static class NSContactTstSetup extends TestSetup {

		/**
		 * Creates setup instance for passed in tests.
		 * 
		 * @param aTest
		 *            Tests to execute
		 */
		public NSContactTstSetup(Test aTest) {
			super(aTest);
		}

		/**
		 * Setup framework for running NSContactTst tests.
		 */
		protected void setUp() throws Exception {
			super.setUp();

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
		 * Tear down framework from running NSContactTst tests.
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
				TestThread thread = new TestThread("NSContactTst Thread " + i,
						NSContactTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(NSContactTst.suite());
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
	 * @param aTest
	 *            name for the test
	 */
	public static void printStart(String aTest) {
		if (Thread.currentThread() instanceof TestThread) {
			System.out.print(Thread.currentThread().getName() + ": ");
			cat.info(Thread.currentThread().getName() + ": " + aTest + " Start");
		}

		System.out.println("Start of " + aTest);
		System.out
				.println("****************************************************************\n");
	}

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
	 * 
	 * @param aTest
	 *            name for the test
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
	 * Utility method to borrow a session from the session pool. All exceptions
	 * will result in the test failing. This method should only be used for
	 * positive session pool tests.
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
	 * Utility method to return a session to the session pool. This should be
	 * placed in a finally block. All exceptions will result in the test
	 * failing.
	 * 
	 * @param aSession
	 *            Session to return to the pool
	 */
	private void returnSession(EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.returnObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("returnSession(): Exception returning session: " + ex);
		}
	}

	/**
	 * Utility method to invalidate a session in the session pool. This should
	 * be placed in an exception block.
	 * 
	 * @param aSession
	 *            Session to invalidate in the pool
	 */
	private void invalidateSession(EPPSession aSession) {
		try {
			if (aSession != null)
				sessionPool.invalidateObject(aSession);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("invalidateSession(): Exception invalidating session: "
					+ ex);
		}
	}

	/**
	 * Handle a response by printing out the result details.
	 * 
	 * @param aResponse
	 *            the response to handle
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
	 * This method generates a unique Contact Name.
	 * 
	 * @return Unique contact name
	 */
	public String makeContactName() {
		long tm = System.currentTimeMillis();

		return new String("Con"
				+ String.valueOf(tm + rd.nextInt(5)).substring(7));
	}

} // End class NSContactTst
