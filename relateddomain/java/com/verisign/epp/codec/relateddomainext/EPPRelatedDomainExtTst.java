/**************************************************************************
 *                                                                        *
 * The information in this document is proprietary to VeriSign, Inc.      *
 * It may not be used, reproduced or disclosed without the written        *
 * approval of VeriSign.                                                  *
 *                                                                        *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                        *
 *                                                                        *
 *                                                                        *
 * Copyright (c) 2011 VeriSign, Inc.  All rights reserved.                *
 *                                                                        *
 *************************************************************************/

package com.verisign.epp.codec.relateddomainext;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.domain.EPPDomainAddRemove;
import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainCreateResp;
import com.verisign.epp.codec.domain.EPPDomainDeleteCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainPeriod;
import com.verisign.epp.codec.domain.EPPDomainRenewCmd;
import com.verisign.epp.codec.domain.EPPDomainRenewResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainTransferCmd;
import com.verisign.epp.codec.domain.EPPDomainTransferResp;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.domain.EPPHostAttr;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.host.EPPHostAddress;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.releateddomainext package. The
 * unit test will execute, gather statistics, and output the results of a test
 * of each concrete classes in com.verisign.epp.codec.releateddomainext package
 * and their expected <code>EPPResponse</code>. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * 
 * @author nchigurupati
 * @version 1.0
 */
public class EPPRelatedDomainExtTst extends TestCase {
	private static Logger cat = Logger.getLogger(EPPRelatedDomainExtTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run</li>
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.</li>
	 * </ul>
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		// Number of Threads
		int numThreads = 1;
		final String threadsStr = System.getProperty("threads");

		if (threadsStr != null) {
			numThreads = Integer.parseInt(threadsStr);
		}

		// Run test suite in multiple threads?
		if (numThreads > 1) {
			// Spawn each thread passing in the Test Suite
			for (int i = 0; i < numThreads; i++) {
				final TestThread thread = new TestThread(
						"EPPRelatedDomainExtTst Thread " + i,
						EPPRelatedDomainExtTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPRelatedDomainExtTst.suite());
		}
	}

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *            number of iterations to run per test
	 */
	public static void setNumIterations(final long aNumIterations) {
		numIterations = aNumIterations;
	}

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPRelatedDomainExtTst</code>.
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();
		final TestSuite suite = new TestSuite(EPPRelatedDomainExtTst.class);

		// iterations Property
		final String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.host.EPPHostMapFactory");
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory
					.getInstance()
					.addExtFactory(
							"com.verisign.epp.codec.relateddomainext.EPPRelatedDomainExtFactory");
		}
		catch (final EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDomainMapFactory or EPPRelatedDomainExtFactory to EPPCodec: "
					+ e);
		}
		return suite;
	}

	/**
	 * Unit test of <code>EPPDomainInfoCmd</code>. The response to
	 * <code>EPPDomainInfoCmd</code> is <code>EPPDomainInfoResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainInfoCmd() {
		EPPCodecTst.printStart("testDomainInfo");

		// Domain Info Form
		EPPDomainInfoCmd theCommand = new EPPDomainInfoCmd("ABC-12349",
				"xn--test.tld1");
		theCommand.setHosts(EPPDomainInfoCmd.HOSTS_DELEGATED);
		theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));
		theCommand.addExtension(new EPPRelatedDomainExtInfo(
				EPPRelatedDomainExtInfo.TYPE_DOMAIN));

		EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		final Vector statuses = new Vector();
		statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

		// Test with just required EPPDomainInfoResp attributes.
		final EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		EPPResponse theResponse = new EPPDomainInfoResp(respTransId,
				"EXAMPLE1-VRSN", "xn--test.tld1", "ClientX", statuses,
				"ClientY", new Date(), new EPPAuthInfo("2fooBAR"));
		theResponse.setResult(EPPResult.SUCCESS);

		encodeDecodeInfoResponse(theResponse);

		// Related Info Form
		theCommand = new EPPDomainInfoCmd("ABC-12349", "xn--test.tld2");
		theCommand.setHosts(EPPDomainInfoCmd.HOSTS_DELEGATED);
		theCommand.setAuthInfo(new EPPAuthInfo("2fooBAR"));
		theCommand.addExtension(new EPPRelatedDomainExtInfo(
				EPPRelatedDomainExtInfo.TYPE_RELATED));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		encodeDecodeInfoResponse(theResponse);

		EPPCodecTst.printEnd("testDomainInfo");
	}

	/**
	 * Unit test of <code>EPPDomainCreateCmd</code>. The response to
	 * <code>EPPDomainCreateCmd</code> is <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainCreate() {
		EPPDomainCreateCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainCreate");

		// Create a domain with just the domain name.
		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.com",
				new EPPAuthInfo("2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a domain with all of the attributes accept for contacts
		final Vector servers = new Vector();
		servers.addElement("ns1.example.com");
		servers.addElement("ns2.example.com");

		Vector contacts = null;

		// Is contacts supported?
		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			contacts = new Vector();
			contacts.addElement(new EPPDomainContact("SH8013-VRSN",
					EPPDomainContact.TYPE_ADMINISTRATIVE));
			contacts.addElement(new EPPDomainContact("SH8013-VRSN",
					EPPDomainContact.TYPE_TECHNICAL));
		}

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getFiveYearsFutureDate();

		theCommand = new EPPDomainCreateCmd("ABC-12345-XYZ", "example.com",
				servers, contacts, new EPPDomainPeriod(5), new EPPAuthInfo(
						"2fooBAR"));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Create a domain with server attribute
		theCommand = new EPPDomainCreateCmd("ABC-12345", "example.com",
				new EPPAuthInfo("2fooBAR"));

		final Vector serverAttrs = new Vector();
		serverAttrs.add(new EPPHostAttr("ns1.example.com"));

		final Vector hostAddresses = new Vector();
		hostAddresses.add(new EPPHostAddress("197.162.10.10"));
		serverAttrs.add(new EPPHostAttr("ns2.example.com", hostAddresses));
		theCommand.setServers(serverAttrs);

		final EPPRelatedDomainExtCreate create = new EPPRelatedDomainExtCreate();
		final EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
				"relDom123!");
		final EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(
				5);
		create.addDomain(new EPPRelatedDomainExtDomain(
				"domain1.com", authInfo, period));
		create.addDomain(new EPPRelatedDomainExtDomain(
				"domain2.com", authInfo, period));
		create.addDomain(new EPPRelatedDomainExtDomain("xn--idn.com", authInfo,
				period, "CHI"));		
		
		theCommand.addExtension(create);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPDomainCreate Response (EPPDomainCreateResp)
		EPPDomainCreateResp theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDomainCreateResp attributes.
		final EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		theResponse = new EPPDomainCreateResp(respTransId, "example.com",
				theDate, expDate);
		theResponse.setResult(EPPResult.SUCCESS);

		EPPRelatedDomainExtCreateResp resp = new EPPRelatedDomainExtCreateResp();
		resp.addDomain(new EPPRelatedDomainExtDomainData("domain1.com",
				theDate, expDate));
		resp.addDomain(new EPPRelatedDomainExtDomainData("domain2.com",
				theDate, expDate));
		resp.addDomain(new EPPRelatedDomainExtDomainData("xn--idn.com",
				theDate, expDate));
		
		theResponse.addExtension(resp);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainCreate");
	}

	/**
	 * Unit test of <code>EPPDomainDeleteCmd</code>. The response to
	 * <code>EPPDomainDeleteCmd</code> is <code>EPPDomainCreateResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainDelete() {
		EPPDomainDeleteCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainDelete");

		// Create a domain with just the domain name.
		theCommand = new EPPDomainDeleteCmd("ABC-12345", "example.com");

		EPPRelatedDomainExtDelete delete = new EPPRelatedDomainExtDelete();
		delete.addDomain("domain1.com");
		delete.addDomain("domain2.com");
		theCommand.addExtension(delete);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPDomainCreate Response (EPPDomainCreateResp)
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDomainCreateResp attributes.
		final EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		final EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS_PENDING);

		final EPPRelatedDomainExtDeleteResp resp = new EPPRelatedDomainExtDeleteResp();
		resp.addDomain(new EPPRelatedDomainExtDomainData(
				"domain1.com", "deleted"));
		resp.addDomain(new EPPRelatedDomainExtDomainData("domain2.com",
				"pendingDelete"));
		resp.addDomain(new EPPRelatedDomainExtDomainData("xn--idn.com",
				"pendingDelete"));
		theResponse.addExtension(resp);

		responseStats = EPPCodecTst.testEncodeDecode(theResponse);

		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainDelete");
	}

	/**
	 * Unit test of <code>EPPDomainUpdateCmd</code>. The response to
	 * <code>EPPDomainUpdateCmd</code> is <code>EPPResponse</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainUpdate() {
		EPPCodecTst.printStart("testDomainUpdate");

		// add
		final Vector addServers = new Vector();
		addServers.addElement("ns1.example.com");

		final Vector addStatuses = new Vector();
		addStatuses.addElement(new EPPDomainStatus(
				EPPDomainStatus.ELM_STATUS_CLIENT_HOLD, "The description"));

		Vector addContacts = null;

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			addContacts = new Vector();
			addContacts.addElement(new EPPDomainContact("MAK21-VRSN",
					EPPDomainContact.TYPE_TECHNICAL));
		}

		// remove
		final Vector removeServers = new Vector();
		removeServers.addElement("ns2.example.com");

		final Vector removeStatuses = new Vector();
		removeStatuses.addElement(new EPPDomainStatus(
				EPPDomainStatus.ELM_STATUS_CLIENT_HOLD));

		Vector removeContacts = null;

		if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
			removeContacts = new Vector();
			removeContacts.addElement(new EPPDomainContact("SH8013-VRSN",
					EPPDomainContact.TYPE_TECHNICAL));
		}

		// change
		// ...
		final EPPDomainAddRemove addItems = new EPPDomainAddRemove(addServers,
				addContacts, addStatuses);
		final EPPDomainAddRemove removeItems = new EPPDomainAddRemove(
				removeServers, removeContacts, removeStatuses);
		final EPPDomainAddRemove changeItems = new EPPDomainAddRemove(
				"SH8013-VRSN", new EPPAuthInfo("2fooBAR"));

		final EPPDomainUpdateCmd theCommand = new EPPDomainUpdateCmd(
				"ABC-12345-XYZ", "example.com", addItems, removeItems,
				changeItems);
		final EPPRelatedDomainExtUpdate update = new EPPRelatedDomainExtUpdate();
		update.addDomain("domain1.com");
		update.addDomain("domain2.com");
		theCommand.addExtension(update);

		final EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Check empty EPPDomainAddRemove
		final EPPDomainAddRemove emptyChangeItems = new EPPDomainAddRemove();
		Assert.assertTrue(emptyChangeItems.isEmpty());

		// Encode Update Response (Standard EPPResponse)
		final EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		final EPPResponse theResponse = new EPPResponse(respTransId);
		theResponse.setResult(EPPResult.SUCCESS);

		final EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainUpdate");
	}

	/**
	 * Unit test of <code>EPPDomainRenewCmd</code>. The response to
	 * <code>EPPDomainRenewCmd</code> is <code>EPPDomainRenewResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainRenew() {

		EPPCodecTst.printStart("testDomainRenew");

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getFiveYearsFutureDate();

		final EPPDomainRenewCmd theCommand = new EPPDomainRenewCmd("ABC-12345",
				"example.com", theDate, new EPPDomainPeriod(5));

		final EPPRelatedDomainExtRenew renew = new EPPRelatedDomainExtRenew();
		theCommand.addExtension(renew);

		final EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
				"relDom123!");

		final EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(
				5);

		renew.addDomain(new EPPRelatedDomainExtDomain("domain1.com", theDate,
				period));

		renew.addDomain(new EPPRelatedDomainExtDomain("domain2.com", theDate,
				period));

		final EPPEncodeDecodeStats commandStats = EPPCodecTst
				.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		EPPDomainRenewResp theResponse;
		EPPEncodeDecodeStats responseStats;

		// Test with just required EPPDomainRenewResp attributes.
		final EPPTransId respTransId = new EPPTransId(theCommand.getTransId(),
				"54321-XYZ");
		theResponse = new EPPDomainRenewResp(respTransId, "example.com",
				expDate);
		theResponse.setResult(EPPResult.SUCCESS);

		final EPPRelatedDomainExtRenewResp resp = new EPPRelatedDomainExtRenewResp();
		resp.addDomain(new EPPRelatedDomainExtDomainData("domain1.com", expDate));
		resp.addDomain(new EPPRelatedDomainExtDomainData("domain2.com", expDate));
		theResponse.addExtension(resp);
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainRenew");
	}

	/**
	 * Unit test of <code>EPPDomainTransferCmd</code>. The response to
	 * <code>EPPDomainTransferCmd</code> is <code>EPPDomainTransferResp</code>. <br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test" and
	 * is a public method.
	 */
	public void testDomainTransfer() {
		EPPDomainTransferCmd theCommand;
		EPPEncodeDecodeStats commandStats;

		EPPCodecTst.printStart("testDomainTransfer");

		// Test Transfer Request Command (without authInfo roid)
		theCommand = new EPPDomainTransferCmd("ABC-12345",
				EPPCommand.OP_REQUEST, "example.com",
				new EPPAuthInfo("2fooBAR"), new EPPDomainPeriod(1));

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Test Transfer Request Command (with authInfo roid)
		final EPPAuthInfo theAuthInfo = new EPPAuthInfo("2fooBAR");
		theAuthInfo.setRoid("JD1234-REP");

		theCommand = new EPPDomainTransferCmd("ABC-12345",
				EPPCommand.OP_REQUEST, "example.com", theAuthInfo,
				new EPPDomainPeriod(1));

		EPPRelatedDomainExtTransfer transfer = new EPPRelatedDomainExtTransfer();
		final EPPRelatedDomainExtAuthInfo authInfo = new EPPRelatedDomainExtAuthInfo(
				"relDom123!");
		final EPPRelatedDomainExtPeriod period = new EPPRelatedDomainExtPeriod(
				1);
		transfer.addDomain(new EPPRelatedDomainExtDomain(
				"domain1.com", authInfo, period));
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain2.com", authInfo));

		
		theCommand.addExtension(transfer);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Encode EPPDomainTransferResp
		EPPDomainTransferResp theResponse = getEPPDomainTransferResp(
				"example.com", EPPCommand.OP_REQUEST);
		EPPEncodeDecodeStats responseStats;

		EPPRelatedDomainExtTransferResp respExt = new EPPRelatedDomainExtTransferResp();
		respExt.addDomain(getDomainDataForTransferResp("domain1.com",
				EPPCommand.OP_REQUEST));
		respExt.addDomain(getDomainDataForTransferResp("domain2.com",
				EPPCommand.OP_REQUEST));
		theResponse.addExtension(respExt);


		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer cancel command
		theCommand = new EPPDomainTransferCmd("ABC-12345",
				EPPCommand.OP_CANCEL, "example.com");

		transfer = new EPPRelatedDomainExtTransfer();
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain1.com"));
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain2.com"));
		
		theCommand.addExtension(transfer);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer cancel response
		theResponse = getEPPDomainTransferResp("example.com",
				EPPCommand.OP_CANCEL);
		respExt = new EPPRelatedDomainExtTransferResp();
		respExt.addDomain(getDomainDataForTransferResp("domain1.com",
				EPPCommand.OP_CANCEL));
		respExt.addDomain(getDomainDataForTransferResp("domain2.com",
				EPPCommand.OP_CANCEL));
		theResponse.addExtension(respExt);


		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer query command
		theCommand = new EPPDomainTransferCmd("ABC-12345", EPPCommand.OP_QUERY,
				"example.com");
		transfer = new EPPRelatedDomainExtTransfer();
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain1.com"));
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain2.com"));
		theCommand.addExtension(transfer);
		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer query response
		theResponse = getEPPDomainTransferResp("example.com",
				EPPCommand.OP_QUERY);
		respExt = new EPPRelatedDomainExtTransferResp();
		respExt.addDomain(getDomainDataForTransferResp("domain1.com",
				EPPCommand.OP_QUERY));
		respExt.addDomain(getDomainDataForTransferResp("domain2.com",
				EPPCommand.OP_QUERY));
		theResponse.addExtension(respExt);


		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer reject command
		theCommand = new EPPDomainTransferCmd("ABC-12345",
				EPPCommand.OP_REJECT, "example.com");

		transfer = new EPPRelatedDomainExtTransfer();
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain1.com"));
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain2.com"));
		theCommand.addExtension(transfer);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer reject response
		theResponse = getEPPDomainTransferResp("example.com",
				EPPCommand.OP_REJECT);
		respExt = new EPPRelatedDomainExtTransferResp();
		respExt.addDomain(getDomainDataForTransferResp("domain1.com",
				EPPCommand.OP_REJECT));
		respExt.addDomain(getDomainDataForTransferResp("domain2.com",
				EPPCommand.OP_REJECT));
		theResponse.addExtension(respExt);


		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		// Transfer approve command
		theCommand = new EPPDomainTransferCmd("ABC-12345",
				EPPCommand.OP_APPROVE, "example.com");

		transfer = new EPPRelatedDomainExtTransfer();
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain1.com"));
		transfer.addDomain(new EPPRelatedDomainExtDomain("domain2.com"));
		theCommand.addExtension(transfer);

		commandStats = EPPCodecTst.testEncodeDecode(theCommand);
		System.out.println(commandStats);

		// Transfer approve response
		theResponse = getEPPDomainTransferResp("example.com",
				EPPCommand.OP_APPROVE);
		respExt = new EPPRelatedDomainExtTransferResp();
		respExt.addDomain(getDomainDataForTransferResp("domain1.com",
				EPPCommand.OP_APPROVE));
		respExt.addDomain(getDomainDataForTransferResp("domain2.com",
				EPPCommand.OP_APPROVE));
		theResponse.addExtension(respExt);


		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);
		EPPCodecTst.printEnd("testDomainTransfer");
	}

	/**
	 * Utility method to return a populated <code>EPPDomainTransferResp</code>
	 * object with the given domain name and the appropriate transfer status
	 * based on the value of the parameter <code>aCommandType</code>
	 * 
	 * @param aDomainName
	 * @param aCommandType
	 * @return <code>EPPDomainTransferResp</code>
	 */
	private EPPDomainTransferResp getEPPDomainTransferResp(
			final String aDomainName, final String aCommandType) {

		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getOneYearFutureDate();

		final EPPTransId respTransId = new EPPTransId("ABC-12345", "54321-XYZ");
		final EPPDomainTransferResp theResponse = new EPPDomainTransferResp(
				respTransId, aDomainName);
		theResponse.setResult(EPPResult.SUCCESS);

		theResponse.setRequestClient("ClientX");
		theResponse.setActionClient("ClientY");
		theResponse.setTransferStatus(getTransferStatus(aCommandType));
		theResponse.setRequestDate(theDate);
		theResponse.setActionDate(theDate);
		theResponse.setExpirationDate(expDate);
		return theResponse;
	}

	/**
	 * Utility method to return a populated
	 * <code>EPPRelatedDomainExtDomainData</code> object with the given domain
	 * name and the appropriate transfer status based on the value of the
	 * parameter <code>aCommandType</code>
	 * 
	 * @param aDomainName
	 * @param aCommandType
	 * @return <code>EPPDomainTransferResp</code>
	 */
	private EPPRelatedDomainExtDomainData getDomainDataForTransferResp(
			final String aDomainName, final String aCommandType) {
		final Date theDate = getTodaysUTCDateAtMidnight();
		final Date expDate = getOneYearFutureDate();
		final EPPRelatedDomainExtDomainData result = new EPPRelatedDomainExtDomainData();
		result.setName(aDomainName);
		result.setRequestClient("ClientX");
		result.setActionClient("ClientY");
		result.setTransferStatus(getTransferStatus(aCommandType));
		result.setRequestDate(theDate);
		result.setActionDate(theDate);
		result.setExpirationDate(expDate);
		return result;
	}

	/**
	 * Utility method to return a transfer status corresponding to the
	 * appropriate transfer command
	 * 
	 * @param aCommandType
	 * @return a transfer status corresponding to the appropriate transfer
	 *         command type (QUERY, REQUEST, REJECT, CANCEL, APPROVE).
	 */
	private String getTransferStatus(final String aCommandType) {
		if (aCommandType.equals(EPPCommand.OP_REQUEST)
				|| aCommandType.equals(EPPCommand.OP_QUERY)) {
			return EPPResponse.TRANSFER_PENDING;
		}
		else if (aCommandType.equals(EPPCommand.OP_CANCEL)) {
			return EPPResponse.TRANSFER_CLIENT_CANCELLED;
		}
		else if (aCommandType.equals(EPPCommand.OP_REJECT)) {
			return EPPResponse.TRANSFER_CLIENT_REJECTED;
		}
		else if (aCommandType.equals(EPPCommand.OP_APPROVE)) {
			return EPPResponse.TRANSFER_CLIENT_APPROVED;
		}

		return EPPResponse.TRANSFER_PENDING;
	}

	/**
	 * Method to encode/decode domain info response.
	 * 
	 * @param aResponse
	 *            Response to add the extension to.
	 */

	private void encodeDecodeInfoResponse(EPPResponse aResponse) {

		EPPCodecTst.printStart("testDomainInfo");

		final EPPRelatedDomainExtInfData infData = new EPPRelatedDomainExtInfData();

		EPPRelatedDomainExtFields fields = new EPPRelatedDomainExtFields();
		fields.setInSync(false);
		fields.addField(new EPPRelatedDomainExtField("registrar", false));
		fields.addField(new EPPRelatedDomainExtField("registrant", true));
		fields.addField(new EPPRelatedDomainExtField("ns", false));

		EPPRelatedDomainExtRegistered registered = new EPPRelatedDomainExtRegistered();
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test.tld2"));

		infData.addGroup(new EPPRelatedDomainExtGroup(
				EPPRelatedDomainExtGroup.TYPE_TLD, fields, null, registered));

		fields = new EPPRelatedDomainExtFields();
		fields.setInSync(true);
		fields.addField(new EPPRelatedDomainExtField("registrar", true));
		fields.addField(new EPPRelatedDomainExtField("registrant", true));
		fields.addField(new EPPRelatedDomainExtField("ns", true));

		registered = new EPPRelatedDomainExtRegistered();
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant1.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant2.tld1"));
		registered.addRegisteredDomain(new EPPRelatedDomainExtName(
				"xn--test-variant3.tld1"));

		final EPPRelatedDomainExtAvailable avail = new EPPRelatedDomainExtAvailable();
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant4.tld1"));
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant5.tld1"));
		avail.addAvailableDomain(new EPPRelatedDomainExtName(
				"xn--test-variant6.tld1"));

		infData.addGroup(new EPPRelatedDomainExtGroup(
				EPPRelatedDomainExtGroup.TYPE_VARIANT, fields, avail,
				registered));

		aResponse.addExtension(infData);

		EPPEncodeDecodeStats responseStats = EPPCodecTst
				.testEncodeDecode(aResponse);
		System.out.println(responseStats);
	}

	/**
	 * JUNIT <code>setUp</code>, which currently does nothing.
	 */
	@Override
	protected void setUp() {
	}

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	@Override
	protected void tearDown() {
	}

	/**
	 * @return UTC date as of midnight
	 */
	private Date getTodaysUTCDateAtMidnight() {
		final Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	/**
	 * @return UTC date five years into the future
	 */
	private Date getFiveYearsFutureDate() {
		final Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		cal.add(Calendar.YEAR, 5);
		return cal.getTime();
	}

	/**
	 * @return UTC date one year into the future
	 */
	private Date getOneYearFutureDate() {
		final Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		cal.add(Calendar.YEAR, 1);
		return cal.getTime();
	}

}
