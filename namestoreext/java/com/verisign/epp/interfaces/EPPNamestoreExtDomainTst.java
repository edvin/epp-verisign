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
package com.verisign.epp.interfaces;


// JUNIT Imports
import java.util.GregorianCalendar;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

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
import com.verisign.epp.codec.namestoreext.EPPNamestoreExtNamestoreExt;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the <code>EPPDomain</code> class. The unit test will
 * initialize a session with an EPP Server, will invoke <code>EPPDomain</code>
 * operations, and will end a session with an EPP Server. The configuration
 * file used by the unit test defaults to epp.config, but can be changed by
 * passing the file path as the first command line argument. The unit test can
 * be run in multiple threads by setting the "threads" system property. For
 * example, the unit test can be run in 2 threads with the configuration file
 * ../../epp.config with the following command: <br>
 * <br>
 * java com.verisign.epp.interfaces.EPPNamestoreExtDomainTst -Dthreads=2 ../../epp.config <br>
 * <br>
 * The unit test is dependent on the use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPNamestoreExtDomainTst extends TestCase {
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
						 EPPNamestoreExtDomainTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP Domain associated with test */
	private EPPDomain domain = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Connection to the EPP Server. */
	private EPPClientCon connection = null;

	/** Current test iteration */
	private int iteration = 0;

	/**
	 * Random instance for the generation of unique objects (hosts, IP
	 * addresses, etc.).
	 */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPNamestoreExtDomainTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with
	 * the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPNamestoreExtDomainTst(String name) {
		super(name);
	}

	// End EPPNamestoreExtDomainTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPNamestoreExtDomainTst TestCase</code>.
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
			printStart("Test Suite");

			domainCheck();

			domainInfo();

			domainCreate();

			domainDelete();

			domainRenew();

			domainUpdate();

			domainTransferQuery();

			domainTransfer();

			domainPoll();

			printEnd("Test Suite");
		}
	}

	// End EPPNamestoreExtDomainTst.testDomain()

	/**
	 * Unit test of <code>EPPDomain.sendCheck</code>.
	 */
	private void domainCheck() {
		printStart("domainCheck");

		EPPDomainCheckResp response;

		try {
			// Check single domain name
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("domainCheck: Check single domain name (example.com)");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = domain.sendCheck();

			System.out.println("Response Type = " + response.getType());

			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getServerTransId());

			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getClientTransId());

			// Output all of the response attributes
			System.out.println("\ndomainCheck: Response = [" + response + "]");

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPDomainCheckResult currResult =
					(EPPDomainCheckResult) response.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("domainCheck: Domain "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("domainCheck: Domain "
									   + currResult.getName()
									   + " is not available");
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

				if (myResult.getAllValues() != null) {
					for (int k = 0; k < myResult.getAllValues().size(); k++) {
						System.out.println("Result Values  : "
										   + myResult.getAllValues().elementAt(k));
					}
				}
			}
		}
		 catch (Exception e) {
			handleException(e);
		}

		try {
			// Check multiple domain names
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("domainCheck: Check multiple domain names (example1.com, example2.com, example3.com)");
			domain.setTransId("ABC-12345-XYZ");

			/**
			 * Add example(1-3).com
			 */
			domain.addDomainName("example1.com");
			domain.addDomainName("example2.com");
			domain.addDomainName("example3.com");

			for (int i = 0; i <= 10; i++) {
				domain.addDomainName(this.makeDomainName());
			}

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = domain.sendCheck();

			// Output all of the response attributes
			System.out.println("\ndomainCheck: Response = [" + response + "]");
			System.out.println("Client Transaction Id = "
							   + response.getTransId().getClientTransId());
			System.out.println("Server Transaction Id = "
							   + response.getTransId().getServerTransId());

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPDomainCheckResult currResult =
					(EPPDomainCheckResult) response.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("domainCheck: Domain "
									   + currResult.getName() + " is available");
				}
				else {
					System.out.println("domainCheck: Domain "
									   + currResult.getName()
									   + " is not available");
				}
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainCheck");
	}

	// End EPPNamestoreExtDomainTst.domainCheck()

	/**
	 * Unit test of <code>EPPDomain.sendInfo</code>.
	 */
	public void domainInfo() {
		printStart("domainInfo");

		EPPDomainInfoResp response;

		try {
			System.out.println("\ndomainInfo: Domain info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = domain.sendInfo();

			//-- Output all of the response attributes
			System.out.println("domainInfo: Response = [" + response + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("domainInfo: name            = "
							   + response.getName());

			System.out.println("domainInfo: client id       = "
							   + response.getClientId());

			System.out.println("domainInfo: created by      = "
							   + response.getCreatedBy());

			System.out.println("domainInfo: create date     = "
							   + response.getCreatedDate());

			System.out.println("domainInfo: expiration date = "
							   + response.getExpirationDate());

			System.out.println("domainInfo: Registrant      = "
							   + response.getRegistrant());

			/**
			 * Process Contacts
			 */
			if (response.getContacts() != null) {
				for (int i = 0; i < response.getContacts().size(); i++) {
					EPPDomainContact myContact =
						(EPPDomainContact) response.getContacts().elementAt(i);

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
			 * Get Hosts
			 */
			if (response.getHosts() != null) {
				for (int i = 0; i < response.getHosts().size(); i++) {
					System.out.println("Host Name : "
									   + response.getHosts().elementAt(i));
				}
			}

			/**
			 * Get Ns
			 */
			if (response.getNses() != null) {
				for (int i = 0; i < response.getNses().size(); i++) {
					System.out.println("Name Server : "
									   + response.getNses().elementAt(i));
				}
			}

			/**
			 * Get Status
			 */
			if (response.getStatuses() != null) {
				for (int i = 0; i < response.getStatuses().size(); i++) {
					EPPDomainStatus myStatus =
						(EPPDomainStatus) response.getStatuses().elementAt(i);

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

		try {
			System.out.println("\ndomainInfo: Domain info");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
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
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainInfo");
	}

	// End EPPNamestoreExtDomainTst.domainInfo()

	/**
	 * Unit test of <code>EPPDomain.sendCreate</code>.
	 */
	public void domainCreate() {
		printStart("domainCreate");

		EPPDomainCreateResp response;

		try {
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("domainCreate: Create example.com with no optional attributes");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setAuthString("ClientX");

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
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

		try {
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("domainCreate: Create example.com with all optional attributes");

			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

			for (int i = 0; i <= 20; i++) {
				domain.addHostName(this.makeHostName(myDomainName));
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

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
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

		printEnd("domainCreate");
	}

	// End EPPNamestoreExtDomainTst.domainCreate()

	/**
	 * Unit test of <code>EPPDomain.sendDelete</code>.
	 */
	public void domainDelete() {
		printStart("domainDelete");

		EPPResponse response;

		try {
			System.out.println("\ndomainDelete: Domain delete");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = domain.sendDelete();

			//-- Output all of the response attributes
			System.out.println("domainDelete: Response = [" + response
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

		printEnd("domainDelete");
	}

	// End EPPNamestoreExtDomainTst.domainDelete()

	/**
	 * Unit test of <code>EPPDomain.sendRenew</code>.
	 */
	public void domainRenew() {
		printStart("domainRenew");

		EPPDomainRenewResp response;

		try {
			System.out.println("\ndomainRenew: Domain delete");

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			domain.setExpirationDate(new GregorianCalendar(2004, 2, 3).getTime());

			domain.setPeriodLength(10);

			domain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			response = domain.sendRenew();

			//-- Output all of the response attributes
			System.out.println("domainRenew: Response = [" + response + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("domainRenew: name = " + response.getName());

			System.out.println("domainRenew: expiration date = "
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

		printEnd("domainRenew");
	}

	// End EPPNamestoreExtDomainTst.domainRenew()

	/**
	 * Unit test of <code>EPPDomain.sendUpdate</code>.
	 */
	public void domainUpdate() {
		printStart("domainUpdate");

		EPPResponse response;

		try {
			System.out.println("\ndomainUpdate: Domain update");

			domain.setTransId("ABC-12345-XYZ");

			String myDomainName = this.makeDomainName();

			domain.addDomainName(myDomainName);

			// Add attributes
			// Is the contact mapping supported?
			if (
				EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
				domain.setUpdateAttrib(
									   EPPDomain.CONTACT, "SH0000",
									   EPPDomain.CONTACT_BILLING, EPPDomain.ADD);
			}

			domain.setUpdateAttrib(
								   EPPDomain.HOST,
								   this.makeHostName(myDomainName),
								   EPPDomain.ADD);

			domain.setUpdateAttrib(
								   EPPDomain.STATUS,
								   new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
								   EPPDomain.ADD);

			// Remove attributes
			domain.setUpdateAttrib(
								   EPPDomain.HOST,
								   this.makeHostName(myDomainName),
								   EPPDomain.REMOVE);

			domain.setUpdateAttrib(
								   EPPDomain.STATUS,
								   new EPPDomainStatus(EPPDomain.STATUS_CLIENT_HOLD),
								   EPPDomain.REMOVE);

			// Is the contact mapping supported?
			if (
				EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
				domain.setUpdateAttrib(
									   EPPDomain.CONTACT, "SH0000",
									   EPPDomain.CONTACT_BILLING,
									   EPPDomain.REMOVE);
			}

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute update
			response = domain.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("domainUpdate: Response = [" + response
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

		printEnd("domainUpdate");
	}

	// End EPPNamestoreExtDomainTst.domainUpdate()

	/**
	 * Unit test of <code>EPPDomain.sendPoll</code>.
	 */
	public void domainPoll() {
		printStart("domainPoll");

		EPPResponse response = null;

		try {
			System.out.println("\ndomainPoll: Domain Poll");

			// req
			session.setTransId("AB-12345");

			session.setPollOp(EPPSession.OP_REQ);

			response = session.sendPoll();

			System.out.println("domainPoll: Response = [" + response + "]\n\n");

			// ack
			session.setPollOp(EPPSession.OP_ACK);

			session.setMsgID(response.getMsgQueue().getId());

			response = session.sendPoll();

			System.out.println("domainPoll: Response = [" + response + "]\n\n");
		}

		 catch (EPPCommandException ex) {
			handleException(ex);
		}

		printEnd("domainPoll");
	}

	/**
	 * Unit test of <code>EPPDomain.sendTransfer</code> for a transfer query.
	 */
	public void domainTransferQuery() {
		printStart("domainTransferQuery");

		EPPDomainTransferResp response;

		try {
			System.out.println("\ndomainTransferQuery: Domain transfer query");

			domain.setTransferOpCode(EPPDomain.TRANSFER_QUERY);

			domain.setTransId("ABC-12345-XYZ");

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute the transfer query
			response = domain.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("domainTransferQuery: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("domainTransferQuery: name = "
							   + response.getName());

			System.out.println("domainTransferQuery: request client = "
							   + response.getRequestClient());

			System.out.println("domainTransferQuery: action client = "
							   + response.getActionClient());

			System.out.println("domainTransferQuery: transfer status = "
							   + response.getTransferStatus());

			System.out.println("domainTransferQuery: request date = "
							   + response.getRequestDate());

			System.out.println("domainTransferQuery: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("domainTransferQuery: expiration date = "
								   + response.getExpirationDate());
			}
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainTransferQuery");
	}

	// End EPPNamestoreExtDomainTst.domainTransferQuery()

	/**
	 * Unit test of <code>EPPDomain.sendTransfer</code> for a transfer query.
	 */
	public void domainTransfer() {
		printStart("domainTransfer");

		EPPDomainTransferResp response;

		try {
			System.out.println("\ndomainTransfer: Domain transfer request");

			domain.setTransferOpCode(EPPDomain.TRANSFER_REQUEST);

			domain.setTransId("ABC-12345-XYZ");

			domain.setAuthString("ClientX");

			domain.addDomainName(this.makeDomainName());

			domain.setPeriodLength(10);

			domain.setPeriodUnit(EPPDomain.PERIOD_YEAR);

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute the transfer query
			response = domain.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("domainTransfer: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("domainTransfer: name = " + response.getName());

			System.out.println("domainTransfer: request client = "
							   + response.getRequestClient());

			System.out.println("domainTransfer: action client = "
							   + response.getActionClient());

			System.out.println("domainTransfer: transfer status = "
							   + response.getTransferStatus());

			System.out.println("domainTransfer: request date = "
							   + response.getRequestDate());

			System.out.println("domainTransfer: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("domainTransfer: expiration date = "
								   + response.getExpirationDate());

				/**
				 * Result Set
				 */
			}

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

		// Transfer Cancel
		try {
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\ndomainTransfer: Domain transfer cancel");

			domain.setTransferOpCode(EPPDomain.TRANSFER_CANCEL);

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute the transfer cancel
			response = domain.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("domainTransfer: Response = [" + response
							   + "]\n\n");
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Reject
		try {
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\ndomainTransfer: Domain transfer reject");

			domain.setTransferOpCode(EPPDomain.TRANSFER_REJECT);

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute the transfer cancel
			response = domain.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("domainTransfer: Response = [" + response
							   + "]\n\n");
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Approve
		try {
			System.out.println("\n----------------------------------------------------------------");

			System.out.println("\ndomainTransfer: Domain transfer approve");

			domain.setTransferOpCode(EPPDomain.TRANSFER_APPROVE);

			domain.addDomainName(this.makeDomainName());

			// Set destination registry
			domain.addExtension(new EPPNamestoreExtNamestoreExt("CC"));
			
			// Execute the transfer cancel
			response = domain.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("domainTransfer: Response = [" + response
							   + "]\n\n");
		}

		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("domainTransfer");
	}

	// End EPPNamestoreExtDomainTst.domainTransfer()

	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute
	 * is initialized with the attributes defined in the EPP sample files.
	 */
	private void initSession() {
		printStart("initSession");
			
		// Set attributes for initSession
		session.setClientID(Environment.getProperty("EPP.Test.clientId", "ClientX"));
		session.setPassword(Environment.getProperty("EPP.Test.password", "foo-BAR2"));
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

	// End EPPNamestoreExtDomainTst.initSession()

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

	// End EPPNamestoreExtDomainTst.endSession()

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

		}

		 catch (Exception e) {
			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		//System.out.println("out init");
		domain = new EPPDomain(session);
	}

	// End EPPNamestoreExtDomainTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	// End EPPNamestoreExtDomainTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPNamestoreExtDomainTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPNamestoreExtDomainTst.class);

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

	// End EPPNamestoreExtDomainTst.suite()

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

	// End EPPNamestoreExtDomainTst.handleException(EPPCommandException)

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

			// Run test suite in multiple threads?
		}

		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				TestThread thread =
					new TestThread(
								   "EPPSessionTst Thread " + i,
								   EPPNamestoreExtDomainTst.suite());

				thread.start();
			}
		}

		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPNamestoreExtDomainTst.suite());
		}

		try {
			app.endApplication();
		}

		 catch (EPPCommandException e) {
			e.printStackTrace();

			Assert.fail("Error ending the EPP Application: " + e);
		}
	}

	// End EPPNamestoreExtDomainTst.main(String [])

	/**
	 * This method tries to generate a unique String as Domain Name and Name
	 * Server
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeDomainName() {
		long tm = System.currentTimeMillis();

		return new String(Thread.currentThread()
						  + String.valueOf(tm + rd.nextInt(12)).substring(10)
						  + ".com");
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

	// End EPPNamestoreExtDomainTst.testStart(String)

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

	// End EPPNamestoreExtDomainTst.testEnd(String)

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

	// End EPPNamestoreExtDomainTst.printMsg(String)

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

	// End EPPNamestoreExtDomainTst.printError(String)
}


// End class EPPNamestoreExtDomainTst
