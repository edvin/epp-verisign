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

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.GregorianCalendar;
import java.util.Random;

import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCheckResult;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdContact;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdCreateResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdInfoResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdMapFactory;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdRenewResp;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdStatus;
import com.verisign.epp.codec.emailFwd.EPPEmailFwdTransferResp;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the <code>EPPEmailFwd</code> class.  The unit test will
 * initialize a     session with an EPP Server, will invoke
 * <code>EPPEmailFwd</code> operations,     and will end a session with an EPP
 * Server.  The     configuration file used by the unit test defaults to
 * epp.config, but     can be changed by passing the file path as the first
 * command line     argument.  The unit test can be run in multiple threads by
 * setting     the "threads" system property.  For example, the unit test can
 * be run in 2 threads with the configuration file ../../epp.config with
 * the following command:<br>
 * <br>
 * java com.verisign.epp.interfaces.EPPEmailFwdTst -Dthreads=2
 * ../../epp.config     <br>
 * <br>
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPEmailFwdTst extends TestCase {
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
						 EPPEmailFwdTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP EmailFwd associated with test */
	private EPPEmailFwd emailFwd = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Connection to the EPP Server. */
	private EPPClientCon connection = null;

	/** Current test iteration */
	private int iteration = 0;

	/** Random instance for the generation of unique objects (). */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPEmailFwdTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code>
	 * with the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPEmailFwdTst(String name) {
		super(name);
	}
	 // End EPPEmailFwdTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPEmailFwdTst TestCase</code>.
	 * Each     sub-test will be invoked in order to satisfy testing the
	 * EPPEmailFwd interface.
	 */
	public void testEmailFwd() {
		int    numIterations = 1;
		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			numIterations = Integer.parseInt(iterationsStr);
		}

		for (
			 iteration = 0;
				 (numIterations == 0) || (iteration < numIterations);
				 iteration++) {
			printStart("Test Suite");

			emailFwdCheck();
			emailFwdInfo();
			emailFwdCreate();
			emailFwdDelete();
			emailFwdRenew();
			emailFwdUpdate();
			emailFwdTransferQuery();
			emailFwdTransfer();

			printEnd("Test Suite");
		}
	}
	 // End EPPEmailFwdTst.testEmailFwd()

	/**
	 * Unit test of <code>EPPEmailFwd.sendCheck</code>.
	 */
	private void emailFwdCheck() {
		printStart("emailFwdCheck");

		EPPEmailFwdCheckResp response;

		try {
			// Check single emailFwd name
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("emailFwdCheck: Check single emailFwd name (example.com)");
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			response = emailFwd.sendCheck();

			System.out.println("Response Type = " + response.getType());

			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getServerTransId());
			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getClientTransId());

			// Output all of the response attributes
			System.out.println("\nemailFwdCheck: Response = [" + response + "]");

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPEmailFwdCheckResult currResult =
					(EPPEmailFwdCheckResult) response.getCheckResults()
													 .elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("emailFwdCheck: EmailFwd "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("emailFwdCheck: EmailFwd "
									   + currResult.getName() + " is unavailable");
				}
			}

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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
		}
		 catch (Exception e) {
			handleException(e);
		}

		try {
			// Check multiple emailFwd names
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("emailFwdCheck: Check multiple emailFwd names (example1.com, example2.com, example3.com)");

			emailFwd.setTransId("ABC-12345-XYZ");

			/**
			 * Add example(1-3).com
			 */
			emailFwd.addEmailFwdName("ash@example1.com");
			emailFwd.addEmailFwdName("ash@example2.com");
			emailFwd.addEmailFwdName("ash@example3.com");

			for (int i = 0; i <= 10; i++) {
				emailFwd.addEmailFwdName(this.makeEmailFwdName());
			}

			response = emailFwd.sendCheck();

			// Output all of the response attributes
			System.out.println("\nemailFwdCheck: Response = [" + response + "]");

			System.out.println("Client Transaction Id = "
							   + response.getTransId().getClientTransId());
			System.out.println("Server Transaction Id = "
							   + response.getTransId().getServerTransId());

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPEmailFwdCheckResult currResult =
					(EPPEmailFwdCheckResult) response.getCheckResults()
													 .elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("emailFwdCheck: EmailFwd "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("emailFwdCheck: EmailFwd "
									   + currResult.getName() + " is unavailable");
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdCheck");
	}
	 // End EPPEmailFwdTst.emailFwdCheck()

	/**
	 * Unit test of <code>EPPEmailFwd.sendInfo</code>.
	 */
	public void emailFwdInfo() {
		printStart("emailFwdInfo");

		EPPEmailFwdInfoResp response;

		try {
			System.out.println("\nemailFwdInfo: EmailFwd info");
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			response = emailFwd.sendInfo();

			//-- Output all of the response attributes
			System.out.println("emailFwdInfo: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("emailFwdInfo: name            = "
							   + response.getName());
			System.out.println("emailFwdInfo: client id       = "
							   + response.getClientId());
			System.out.println("emailFwdInfo: created by      = "
							   + response.getCreatedBy());
			System.out.println("emailFwdInfo: create date     = "
							   + response.getCreatedDate());
			System.out.println("emailFwdInfo: expiration date = "
							   + response.getExpirationDate());
			System.out.println("emailFwdInfo: Registrant      = "
							   + response.getRegistrant());

			/**
			 * Process Contacts
			 */
			if (response.getContacts() != null) {
				for (int i = 0; i < response.getContacts().size(); i++) {
					EPPEmailFwdContact myContact =
						(EPPEmailFwdContact) response.getContacts().elementAt(i);
					System.out.println("Contact Name : " + myContact.getName());
					System.out.println("Contact Type : " + myContact.getType());
				}
			}

			/**
			 * Get AuthInfo
			 */
			if (response.getAuthInfo() != null) {
				System.out.println("Authorization        : "
								   + response.getAuthInfo().getPassword());
				System.out.println("Authorization (Roid) : "
								   + response.getAuthInfo().getRoid());
			}

			/**
			 * Get Status
			 */
			if (response.getStatuses() != null) {
				for (int i = 0; i < response.getStatuses().size(); i++) {
					EPPEmailFwdStatus myStatus =
						(EPPEmailFwdStatus) response.getStatuses().elementAt(i);
					System.out.println("Lang     : " + myStatus.getLang());
					System.out.println("Status   : " + myStatus.getStatus());
				}
			}

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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			System.out.println("\nemailFwdInfo: EmailFwd info");
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			response = emailFwd.sendInfo();

			//-- Output all of the response attributes
			System.out.println("emailFwdInfo: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("emailFwdInfo: name = " + response.getName());
			System.out.println("emailFwdInfo: client id = "
							   + response.getClientId());
			System.out.println("emailFwdInfo: created by = "
							   + response.getCreatedBy());
			System.out.println("emailFwdInfo: create date = "
							   + response.getCreatedDate());
			System.out.println("emailFwdInfo: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdInfo");
	}
	 // End EPPEmailFwdTst.emailFwdInfo()

	/**
	 * Unit test of <code>EPPEmailFwd.sendCreate</code>.
	 */
	public void emailFwdCreate() {
		printStart("emailFwdCreate");

		EPPEmailFwdCreateResp response;

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("emailFwdCreate: Create example.com with no optional attributes");

			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());
			emailFwd.setForwardTo("asampath@verisign.com");
			emailFwd.setAuthString("ClientX");

			response = emailFwd.sendCreate();

			//-- Output all of the response attributes
			System.out.println("emailFwdCreate: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("emailFwdCreate: name = " + response.getName());
			System.out.println("emailFwdCreate: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("emailFwdCreate: Create example.com with all optional attributes");

			emailFwd.setTransId("ABC-12345-XYZ");

			String myEmailFwdName = this.makeEmailFwdName();
			emailFwd.addEmailFwdName(myEmailFwdName);
			emailFwd.setForwardTo("asampath@verisign.com");

			// Is the contact mapping supported?
			if (
				EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
				// Add emailFwd contacts
				emailFwd.addContact(
									"SH0000", EPPEmailFwd.CONTACT_ADMINISTRATIVE);
				emailFwd.addContact("SH0000", EPPEmailFwd.CONTACT_TECHNICAL);
				emailFwd.addContact("SH0000", EPPEmailFwd.CONTACT_BILLING);
			}

			emailFwd.setPeriodLength(10);
			emailFwd.setPeriodUnit(EPPEmailFwd.PERIOD_YEAR);
			emailFwd.setAuthString("ClientX");

			response = emailFwd.sendCreate();

			//-- Output all of the response attributes
			System.out.println("emailFwdCreate: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("emailFwdCreate: name = " + response.getName());
			System.out.println("emailFwdCreate: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdCreate");
	}
	 // End EPPEmailFwdTst.emailFwdCreate()

	/**
	 * Unit test of <code>EPPEmailFwd.sendDelete</code>.
	 */
	public void emailFwdDelete() {
		printStart("emailFwdDelete");

		EPPResponse response;

		try {
			System.out.println("\nemailFwdDelete: EmailFwd delete");
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			response = emailFwd.sendDelete();

			//-- Output all of the response attributes
			System.out.println("emailFwdDelete: Response = [" + response
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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdDelete");
	}
	 // End EPPEmailFwdTst.emailFwdDelete()

	/**
	 * Unit test of <code>EPPEmailFwd.sendRenew</code>.
	 */
	public void emailFwdRenew() {
		printStart("emailFwdRenew");

		EPPEmailFwdRenewResp response;

		try {
			System.out.println("\nemailFwdRenew: EmailFwd delete");
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());
			emailFwd.setExpirationDate(new GregorianCalendar(2004, 2, 3)
									   .getTime());
			emailFwd.setPeriodLength(10);
			emailFwd.setPeriodUnit(EPPEmailFwd.PERIOD_YEAR);

			response = emailFwd.sendRenew();

			//-- Output all of the response attributes
			System.out.println("emailFwdRenew: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("emailFwdRenew: name = " + response.getName());
			System.out.println("emailFwdRenew: expiration date = "
							   + response.getExpirationDate());

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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdRenew");
	}
	 // End EPPEmailFwdTst.emailFwdRenew()

	/**
	 * Unit test of <code>EPPEmailFwd.sendUpdate</code>.
	 */
	public void emailFwdUpdate() {
		printStart("emailFwdUpdate");

		EPPResponse response;

		try {
			System.out.println("\nemailFwdUpdate: EmailFwd update");
			emailFwd.setTransId("ABC-12345-XYZ");

			String myEmailFwdName = this.makeEmailFwdName();

			emailFwd.addEmailFwdName(myEmailFwdName);

			// Add attributes
			// Is the contact mapping supported?
			if (
				EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
				emailFwd.setUpdateAttrib(
										 EPPEmailFwd.CONTACT, "SH0000",
										 EPPEmailFwd.CONTACT_BILLING,
										 EPPEmailFwd.ADD);
			}

			emailFwd.setUpdateAttrib(
									 EPPEmailFwd.STATUS,
									 new EPPEmailFwdStatus(EPPEmailFwd.STATUS_CLIENT_HOLD),
									 EPPEmailFwd.ADD);

			// Remove attributes
			emailFwd.setUpdateAttrib(
									 EPPEmailFwd.STATUS,
									 new EPPEmailFwdStatus(EPPEmailFwd.STATUS_CLIENT_HOLD),
									 EPPEmailFwd.REMOVE);

			// Is the contact mapping supported?
			if (
				EPPFactory.getInstance().hasService(EPPEmailFwdMapFactory.NS_CONTACT)) {
				emailFwd.setUpdateAttrib(
										 EPPEmailFwd.CONTACT, "SH0000",
										 EPPEmailFwd.CONTACT_BILLING,
										 EPPEmailFwd.REMOVE);
			}

			// Execute update
			response = emailFwd.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("emailFwdUpdate: Response = [" + response
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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
			
			// Changed just the forward to address
			emailFwd.addEmailFwdName(myEmailFwdName);
			emailFwd.setForwardTo("hello@world.com");
	
			// Execute update
			response = emailFwd.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("emailFwdUpdate: Change forward to address Response = [" + response
							   + "]\n\n");

		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdUpdate");
	}
	 // End EPPEmailFwdTst.emailFwdUpdate()

	/**
	 * Unit test of <code>EPPEmailFwd.sendTransfer</code> for a transfer query.
	 */
	public void emailFwdTransferQuery() {
		printStart("emailFwdTransferQuery");

		EPPEmailFwdTransferResp response;

		try {
			System.out.println("\nemailFwdTransferQuery: EmailFwd transfer query");
			emailFwd.setTransferOpCode(EPPEmailFwd.TRANSFER_QUERY);
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			// Execute the transfer query
			response = emailFwd.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("emailFwdTransferQuery: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("emailFwdTransferQuery: name = "
							   + response.getName());
			System.out.println("emailFwdTransferQuery: request client = "
							   + response.getRequestClient());
			System.out.println("emailFwdTransferQuery: action client = "
							   + response.getActionClient());
			System.out.println("emailFwdTransferQuery: transfer status = "
							   + response.getTransferStatus());
			System.out.println("emailFwdTransferQuery: request date = "
							   + response.getRequestDate());
			System.out.println("emailFwdTransferQuery: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("emailFwdTransferQuery: expiration date = "
								   + response.getExpirationDate());
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdTransferQuery");
	}
	 // End EPPEmailFwdTst.emailFwdTransferQuery()

	/**
	 * Unit test of <code>EPPEmailFwd.sendTransfer</code> for a transfer query.
	 */
	public void emailFwdTransfer() {
		printStart("emailFwdTransfer");

		EPPEmailFwdTransferResp response;

		try {
			System.out.println("\nemailFwdTransfer: EmailFwd transfer request");
			emailFwd.setTransferOpCode(EPPEmailFwd.TRANSFER_REQUEST);
			emailFwd.setTransId("ABC-12345-XYZ");
			emailFwd.setAuthString("ClientX");
			emailFwd.addEmailFwdName(this.makeEmailFwdName());
			emailFwd.setPeriodLength(10);
			emailFwd.setPeriodUnit(EPPEmailFwd.PERIOD_YEAR);

			// Execute the transfer query
			response = emailFwd.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("emailFwdTransfer: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("emailFwdTransfer: name = " + response.getName());
			System.out.println("emailFwdTransfer: request client = "
							   + response.getRequestClient());
			System.out.println("emailFwdTransfer: action client = "
							   + response.getActionClient());
			System.out.println("emailFwdTransfer: transfer status = "
							   + response.getTransferStatus());
			System.out.println("emailFwdTransfer: request date = "
							   + response.getRequestDate());
			System.out.println("emailFwdTransfer: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("emailFwdTransfer: expiration date = "
								   + response.getExpirationDate());
			}

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

				if (myResult.getValues() != null) {
					for (int k = 0; k < myResult.getValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getValues().elementAt(k));
					}
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Cancel
		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("\nemailFwdTransfer: EmailFwd transfer cancel");
			emailFwd.setTransferOpCode(EPPEmailFwd.TRANSFER_CANCEL);
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			// Execute the transfer cancel
			response = emailFwd.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("emailFwdTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Reject
		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("\nemailFwdTransfer: EmailFwd transfer reject");
			emailFwd.setTransferOpCode(EPPEmailFwd.TRANSFER_REJECT);
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			// Execute the transfer cancel
			response = emailFwd.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("emailFwdTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Accept
		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("\nemailFwdTransfer: EmailFwd transfer approve");
			emailFwd.setTransferOpCode(EPPEmailFwd.TRANSFER_APPROVE);
			emailFwd.addEmailFwdName(this.makeEmailFwdName());

			// Execute the transfer cancel
			response = emailFwd.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("emailFwdTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("emailFwdTransfer");
	}
	 // End EPPEmailFwdTst.emailFwdTransfer()

	/**
	 * Unit test of <code>EPPSession.initSession</code>.  The session attribute
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
	 // End EPPEmailFwdTst.initSession()

	/**
	 * Unit test of <code>EPPSession.endSession</code>.  The session with the
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
	 // End EPPEmailFwdTst.endSession()

	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar".
	 */
	protected void setUp() {
		try {
			session = new EPPSession();
			session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
		}
		 catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		//System.out.println("out init");
		emailFwd = new EPPEmailFwd(session);
	}
	 // End EPPEmailFwdTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}
	 // End EPPEmailFwdTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPEmailFwdTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPEmailFwdTst.class);

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
	 // End EPPEmailFwdTst.suite()

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception.  If the exception     was
	 * caused by a server error, "Server Error : Response XML" will be
	 * specified.     If the exception was caused by a general     algorithm
	 * error, "General Error : Exception Description" will     be specified.
	 *
	 * @param aException Exception thrown during test
	 */
	public void handleException(Exception aException) {
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
	 // End EPPEmailFwdTst.handleException(EPPCommandException)

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
		// Override the default configuration file name?
		if (args.length > 0) {
			configFileName = args[0];
		}

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
								   "EPPSessionTst Thread " + i,
								   EPPEmailFwdTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPEmailFwdTst.suite());
		}

		try {
			app.endApplication();
		}
		 catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}
	 // End EPPEmailFwdTst.main(String [])

	/**
	 * This method tries to generate a unique String as EmailFwd Name and Name
	 * Server
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeEmailFwdName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
						  + String.valueOf(tm + rd.nextInt(12)).substring(10)
						  + "@verisign.com");
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
	 * current     thread is a <code>TestThread</code>.
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
	 // End EPPEmailFwdTst.testStart(String)

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
	 // End EPPEmailFwdTst.testEnd(String)

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
	 // End EPPEmailFwdTst.printMsg(String)

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
	 // End EPPEmailFwdTst.printError(String)
}
 // End class EPPEmailFwdTst
