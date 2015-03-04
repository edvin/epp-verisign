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
import java.util.GregorianCalendar;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.defReg.EPPDefRegCheckResp;
import com.verisign.epp.codec.defReg.EPPDefRegCheckResult;
import com.verisign.epp.codec.defReg.EPPDefRegCreateResp;
import com.verisign.epp.codec.defReg.EPPDefRegInfoResp;
import com.verisign.epp.codec.defReg.EPPDefRegRenewResp;
import com.verisign.epp.codec.defReg.EPPDefRegStatus;
import com.verisign.epp.codec.defReg.EPPDefRegTransferResp;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.idnext.EPPIdnLangTag;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the <code>EPPDefReg</code> class.  The unit test will
 * initialize a     session with an EPP Server, will invoke
 * <code>EPPDefReg</code> operations,     and will end a session with an EPP
 * Server.  The     configuration file used by the unit test defaults to
 * epp.config, but     can be changed by passing the file path as the first
 * command line     argument.  The unit test can be run in multiple threads by
 * setting     the "threads" system property.  For example, the unit test can
 * be run in 2 threads with the configuration file ../../epp.config with
 * the following command:<br>
 * <br>
 * java com.verisign.epp.interfaces.EPPDefRegTst -Dthreads=2
 * ../../epp.config     <br>
 * <br>
 * The unit test is dependent on the     use of <a
 * href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br><br><br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 */
public class EPPDefRegTst extends TestCase {
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
						 EPPDefRegTst.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** EPP DefReg associated with test */
	private EPPDefReg defReg = null;

	/** EPP Session associated with test */
	private EPPSession session = null;

	/** Connection to the EPP Server. */
	private EPPClientCon connection = null;

	/** Current test iteration */
	private int iteration = 0;

	/** Random instance for the generation of unique objects (). */
	private Random rd = new Random(System.currentTimeMillis());

	/**
	 * Allocates an <code>EPPDefRegTst</code> with a logical name.  The
	 * constructor will initialize the base class <code>TestCase</code>
	 * with the logical name.
	 *
	 * @param name Logical name of the test
	 */
	public EPPDefRegTst(String name) {
		super(name);
	}
	 // End EPPDefRegTst(String)

	/**
	 * JUNIT test method to implement the <code>EPPDefRegTst TestCase</code>.
	 * Each     sub-test will be invoked in order to satisfy testing the
	 * EPPDefReg interface.
	 */
	public void testDefReg() {
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

			defRegCheck();
			defRegInfo();
			defRegCreate();
			defRegCreateIDN();
			defRegDelete();
			defRegRenew();
			defRegUpdate();
			defRegTransferQuery();
			defRegTransfer();

			printEnd("Test Suite");
		}
	}
	 // End EPPDefRegTst.testDefReg()

	/**
	 * Unit test of <code>EPPDefReg.sendCheck</code>.
	 */
	private void defRegCheck() {
		printStart("defRegCheck");

		EPPDefRegCheckResp response;

		try {
			// Check single defReg name
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("defRegCheck: Check defReg names (smith, doe, john.smith)");
			defReg.setTransId("ABC-12345-XYZ");
			defReg.addDefRegName(EPPDefReg.LEVEL_PREMIUM, "smith");
			defReg.addDefRegName(EPPDefReg.LEVEL_PREMIUM, "doe");
			defReg.addDefRegName(EPPDefReg.LEVEL_STANDARD, "john.smith");

			response = defReg.sendCheck();

			System.out.println("Response Type = " + response.getType());

			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getServerTransId());
			System.out.println("Response.TransId.ServerTransId = "
							   + response.getTransId().getClientTransId());

			// Output all of the response attributes
			System.out.println("\ndefRegCheck: Response = [" + response + "]");

			// For each result
			for (int i = 0; i < response.getCheckResults().size(); i++) {
				EPPDefRegCheckResult currResult =
					(EPPDefRegCheckResult) response.getCheckResults().elementAt(i);

				if (currResult.isAvailable()) {
					System.out.println("defRegCheck: DefReg "
									   + currResult.getName() + " is known");
				}
				else {
					System.out.println("defRegCheck: DefReg "
									   + currResult.getName() + " is not known");
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

		printEnd("defRegCheck");
	}
	 // End EPPDefRegTst.defRegCheck()

	/**
	 * Unit test of <code>EPPDefReg.sendInfo</code>.
	 */
	public void defRegInfo() {
		printStart("defRegInfo");

		EPPDefRegInfoResp response;

		try {
			System.out.println("\ndefRegInfo: DefReg info for EXAMPLE1-REP");
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setRoid("EXAMPLE1-REP");

			response = defReg.sendInfo();

			//-- Output all of the response attributes
			System.out.println("defRegInfo: Response = [" + response + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("defRegInfo: name            = "
							   + response.getName());
			System.out.println("defRegInfo: client id       = "
							   + response.getClientId());
			System.out.println("defRegInfo: created by      = "
							   + response.getCreatedBy());
			System.out.println("defRegInfo: create date     = "
							   + response.getCreatedDate());
			System.out.println("defRegInfo: expiration date = "
							   + response.getExpirationDate());
			System.out.println("defRegInfo: Registrant      = "
							   + response.getRegistrant());

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
					EPPDefRegStatus myStatus =
						(EPPDefRegStatus) response.getStatuses().elementAt(i);
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

		printEnd("defRegInfo");
	}
	 // End EPPDefRegTst.defRegInfo()

	/**
	 * Unit test of <code>EPPDefReg.sendCreate</code>.
	 */
	public void defRegCreate() {
		printStart("defRegCreate");

		EPPDefRegCreateResp response;

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("defRegCreate: Create doe premium defreg");

			defReg.setTransId("ABC-12345-XYZ");
			defReg.addDefRegName(EPPDefReg.LEVEL_PREMIUM, "doe");
			defReg.setRegistrant("sh8013");
			defReg.setTm("Example");
			defReg.setTmCountry("US");
			defReg.setTmDate(new GregorianCalendar(1990, 4, 3).getTime());
			defReg.setAdminContact("sh8013");
			defReg.setAuthString("2fooBAR");

			response = defReg.sendCreate();

			//-- Output all of the response attributes
			System.out.println("defRegCreate: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("defRegCreate: name = " + response.getName());
			System.out.println("defRegCreate: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("defRegCreate: Create john.smith with all optional attributes");

			defReg.setTransId("ABC-12345-XYZ");
			defReg.addDefRegName(EPPDefReg.LEVEL_STANDARD, "john.smith");
			defReg.setRegistrant("sh8013");
			defReg.setTm("Example");
			defReg.setTmCountry("US");
			defReg.setTmDate(new GregorianCalendar(1990, 4, 3).getTime());
			defReg.setAdminContact("sh8013");
			defReg.setAuthString("2fooBAR");
			defReg.setPeriodLength(10);

			response = defReg.sendCreate();

			//-- Output all of the response attributes
			System.out.println("defRegCreate: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("defRegCreate: name = " + response.getName());
			System.out.println("defRegCreate: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("defRegCreate");
	}
	 // End EPPDefRegTst.defRegCreate()

	
	/**
	 * Unit test of using the IDN Lang Extension with <code>EPPDefReg</code> Create .
	 */
	public void defRegCreateIDN() {

		printStart("defRegCreateIDN");
		
		EPPDefRegCreateResp response;

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("defRegCreate: Create xn--gya premium defreg");

			defReg.setTransId("ABC-12345-XYZ");
			defReg.addDefRegName(EPPDefReg.LEVEL_PREMIUM, "xn--gya");
			defReg.setRegistrant("sh8013");
			defReg.setTm("Example");
			defReg.setTmCountry("US");
			defReg.setTmDate(new GregorianCalendar(1990, 4, 3).getTime());
			defReg.setAdminContact("sh8013");
			defReg.setAuthString("2fooBAR");
			
			// Set consent identifier
			defReg.addExtension(new EPPIdnLangTag("GRE"));

			response = defReg.sendCreate();

			//-- Output all of the response attributes
			System.out.println("defRegCreate: Response = [" + response
							   + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("defRegCreate: name = " + response.getName());
			System.out.println("defRegCreate: expiration date = "
							   + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("defRegCreate: Create xn--2xa.xn--2xa with all optional attributes");

			defReg.setTransId("ABC-12345-XYZ");
			defReg.addDefRegName(EPPDefReg.LEVEL_STANDARD, "xn--2xa.xn--2xa");
			defReg.setRegistrant("sh8013");
			defReg.setTm("Example");
			defReg.setTmCountry("US");
			defReg.setTmDate(new GregorianCalendar(1990, 4, 3).getTime());
			defReg.setAdminContact("sh8013");
			defReg.setAuthString("2fooBAR");
			defReg.setPeriodLength(10);
			// Set IDN Extension
			defReg.addExtension(new EPPIdnLangTag("GRE"));

			response = defReg.sendCreate();

			//-- Output all of the response attributes
			System.out.println("defRegCreate: Response = [" + response + "]\n\n");
			//-- Output response attributes using accessors
			System.out.println("defRegCreate: name = " + response.getName());
			System.out.println("defRegCreate: expiration date = " + response.getExpirationDate());
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}
		printEnd("defRegCreateIDN");

	} // End EPPDefRegTst.defRegCreateIDN()

	
	/**
	 * Unit test of <code>EPPDefReg.sendDelete</code>.
	 */
	public void defRegDelete() {
		printStart("defRegDelete");

		EPPResponse response;

		try {
			System.out.println("\ndefRegDelete: DefReg delete for EXAMPLE1-REP");
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setRoid("EXAMPLE1-REP");
			response = defReg.sendDelete();

			//-- Output all of the response attributes
			System.out.println("defRegDelete: Response = [" + response
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

		printEnd("defRegDelete");
	}
	 // End EPPDefRegTst.defRegDelete()

	/**
	 * Unit test of <code>EPPDefReg.sendRenew</code>.
	 */
	public void defRegRenew() {
		printStart("defRegRenew");

		EPPDefRegRenewResp response;

		try {
			System.out.println("\ndefRegRenew: DefReg delete for EXAMPLE1-REP");
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setRoid("EXAMPLE1-REP");
			defReg.setExpirationDate(new GregorianCalendar(2004, 2, 3).getTime());
			defReg.setPeriodLength(10);
			defReg.setPeriodUnit(EPPDefReg.PERIOD_YEAR);

			response = defReg.sendRenew();

			//-- Output all of the response attributes
			System.out.println("defRegRenew: Response = [" + response + "]\n\n");

			//-- Output response attributes using accessors
			System.out.println("defRegRenew: roid = " + response.getRoid());
			System.out.println("defRegRenew: expiration date = "
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

		printEnd("defRegRenew");
	}
	 // End EPPDefRegTst.defRegRenew()

	/**
	 * Unit test of <code>EPPDefReg.sendUpdate</code>.
	 */
	public void defRegUpdate() {
		printStart("defRegUpdate");

		EPPResponse response;

		try {
			System.out.println("\ndefRegUpdate: DefReg update for EXAMPLE1-REP");
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setRoid("EXAMPLE1-REP");
			defReg.setRegistrant("sh8013");
			defReg.setAdminContact("sh8013");
			defReg.addStatus(new EPPDefRegStatus(EPPDefReg.STATUS_CLIENT_DELETE_PROHIBITED));
			defReg.removeStatus(new EPPDefRegStatus(EPPDefReg.STATUS_CLIENT_UPDATE_PROHIBITED));

			// Execute update
			response = defReg.sendUpdate();

			//-- Output all of the response attributes
			System.out.println("defRegUpdate: Response = [" + response
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

		printEnd("defRegUpdate");
	}
	 // End EPPDefRegTst.defRegUpdate()

	/**
	 * Unit test of <code>EPPDefReg.sendTransfer</code> for a transfer query.
	 */
	public void defRegTransferQuery() {
		printStart("defRegTransferQuery");

		EPPDefRegTransferResp response;

		try {
			System.out.println("\ndefRegTransferQuery: DefReg transfer query for EXAMPLE1-REP");
			defReg.setTransferOpCode(EPPDefReg.TRANSFER_QUERY);
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setRoid("EXAMPLE1-REP");

			// Execute the transfer query
			response = defReg.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("defRegTransferQuery: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("defRegTransferQuery: roid = "
							   + response.getRoid());
			System.out.println("defRegTransferQuery: request client = "
							   + response.getRequestClient());
			System.out.println("defRegTransferQuery: action client = "
							   + response.getActionClient());
			System.out.println("defRegTransferQuery: transfer status = "
							   + response.getTransferStatus());
			System.out.println("defRegTransferQuery: request date = "
							   + response.getRequestDate());
			System.out.println("defRegTransferQuery: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("defRegTransferQuery: expiration date = "
								   + response.getExpirationDate());
			}
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("defRegTransferQuery");
	}
	 // End EPPDefRegTst.defRegTransferQuery()

	/**
	 * Unit test of <code>EPPDefReg.sendTransfer</code> for a transfer query.
	 */
	public void defRegTransfer() {
		printStart("defRegTransfer");

		EPPDefRegTransferResp response;

		try {
			System.out.println("\ndefRegTransfer: DefReg transfer request for EXAMPLE1-REP");
			defReg.setTransferOpCode(EPPDefReg.TRANSFER_REQUEST);
			defReg.setTransId("ABC-12345-XYZ");
			defReg.setAuthString("ClientX");
			defReg.setRoid("EXAMPLE1-REP");
			defReg.setPeriodLength(10);
			defReg.setPeriodUnit(EPPDefReg.PERIOD_YEAR);

			// Execute the transfer query
			response = defReg.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("defRegTransfer: Response = [" + response
							   + "]\n\n");

			//-- Output required response attributes using accessors
			System.out.println("defRegTransfer: roid = " + response.getRoid());
			System.out.println("defRegTransfer: request client = "
							   + response.getRequestClient());
			System.out.println("defRegTransfer: action client = "
							   + response.getActionClient());
			System.out.println("defRegTransfer: transfer status = "
							   + response.getTransferStatus());
			System.out.println("defRegTransfer: request date = "
							   + response.getRequestDate());
			System.out.println("defRegTransfer: action date = "
							   + response.getActionDate());

			//-- Output optional response attributes using accessors
			if (response.getExpirationDate() != null) {
				System.out.println("defRegTransfer: expiration date = "
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
			System.out.println("\ndefRegTransfer: DefReg transfer cancel for EXAMPLE1-REP");
			defReg.setTransferOpCode(EPPDefReg.TRANSFER_CANCEL);
			defReg.setRoid("EXAMPLE1-REP");

			// Execute the transfer cancel
			response = defReg.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("defRegTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Reject
		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("\ndefRegTransfer: DefReg transfer reject for EXAMPLE1-REP");
			defReg.setTransferOpCode(EPPDefReg.TRANSFER_REJECT);
			defReg.setRoid("EXAMPLE1-REP");

			// Execute the transfer cancel
			response = defReg.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("defRegTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		// Transfer Approve
		try {
			System.out.println("\n----------------------------------------------------------------");
			System.out.println("\ndefRegTransfer: DefReg transfer approve for EXAMPLE1-REP");
			defReg.setTransferOpCode(EPPDefReg.TRANSFER_APPROVE);
			defReg.setRoid("EXAMPLE1-REP");

			// Execute the transfer cancel
			response = defReg.sendTransfer();

			//-- Output all of the response attributes
			System.out.println("defRegTransfer: Response = [" + response
							   + "]\n\n");
		}
		 catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("defRegTransfer");
	}
	 // End EPPDefRegTst.defRegTransfer()

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
	 // End EPPDefRegTst.initSession()

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
	 // End EPPDefRegTst.endSession()

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
		defReg = new EPPDefReg(session);
	}
	 // End EPPDefRegTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}
	 // End EPPDefRegTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPDefRegTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(EPPDefRegTst.class);

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
	 // End EPPDefRegTst.suite()

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
	 // End EPPDefRegTst.handleException(EPPCommandException)

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
								   EPPDefRegTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPDefRegTst.suite());
		}

		try {
			app.endApplication();
		}
		 catch (EPPCommandException e) {
			e.printStackTrace();
			Assert.fail("Error ending the EPP Application: " + e);
		}
	}
	 // End EPPDefRegTst.main(String [])

	/**
	 * This method tries to generate a unique String as DefReg Name and Name
	 * Server
	 *
	 * @return DOCUMENT ME!
	 */
	public String makeDefRegName() {
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
	 // End EPPDefRegTst.testStart(String)

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
	 // End EPPDefRegTst.testEnd(String)

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
	 // End EPPDefRegTst.printMsg(String)

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
	 // End EPPDefRegTst.printError(String)
}
 // End class EPPDefRegTst
