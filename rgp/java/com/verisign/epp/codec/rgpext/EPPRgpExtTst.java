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
package com.verisign.epp.codec.rgpext;


// JUNIT Imports
import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainContact;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainMapFactory;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPMsgQueue;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.codec.rgppoll.EPPRgpPollResponse;
import com.verisign.epp.codec.rgppoll.EPPRgpPollStatus;
import com.verisign.epp.util.TestThread;


/**
 * Is a unit test of the com.verisign.epp.codec.rgpext package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.rgpext package concrete <code>EPPRgpExt</code>'s
 * and their expected <code>EPPResponse</code>. The unit test is dependent on
 * the use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.9 $
 */
public class EPPRgpExtTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;

	/**
	 * Creates a new EPPRgpExtTst object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public EPPRgpExtTst(String name) {
		super(name);
	}

	// End EPPRgpExtTst(String)



    public void testPoll() {

        EPPCodecTst.printStart("testPoll");

        EPPRgpPollResponse pollResponse = new EPPRgpPollResponse();

        EPPTransId  respTransId =
            new EPPTransId("abc-123", "54321-XYZ-1");

        pollResponse.setTransId(respTransId);
        pollResponse.setResult(EPPResult.SUCCESS_POLL_MSG);

        pollResponse.setMsgQueue(new EPPMsgQueue(
                                                new Long(5), "12345", new Date(),
                                                "Transfer request pending"));

        pollResponse.setName("foobar.com");
        pollResponse.setStatus(new EPPRgpPollStatus(EPPRgpPollStatus.PENDING_DELETE));
        pollResponse.setReportDueDate(new Date());
        pollResponse.setReqDate(new Date());

        EPPEncodeDecodeStats commandStats =
            EPPCodecTst.testEncodeDecode(pollResponse);

        EPPCodecTst.printEnd("testPoll");
    }

	/**
	 * Tests sending a restore report using the <code>EPPRgpExtUpdate</code>
	 * domain update extension.
	 */
	public void testRestoreReport() {

        EPPCodecTst.printStart("testRestoreReport");

        EPPDomainUpdateCmd theCommand =
            new EPPDomainUpdateCmd(
                                   "ABC-12345-XYZ", "example.com", null,
                                   null, null);
        
       EPPRgpExtReport report = new EPPRgpExtReport();
       report.setPreData("Pre-delete whois data goes here. Both XML and free text are allowed");
       report.setPostData("Post-delete whois data goes here. Both XML and free text are allowed");
       report.setDeleteTime(new Date());
       report.setRestoreTime(new Date());

       report.setRestoreReason(new EPPRgpExtReportText("Registrant Error"));

       report.setStatement1(new EPPRgpExtReportText("This registrar has not" +
           " restored the Registered Domain in order to " +
           "assume the rights to use or sell the Registered" +
           " Name for itself or for any third party"));

       report.setStatement2(new EPPRgpExtReportText("The information in this report " +
           " is true to best of this registrar's knowledge, and this" +
           "registrar acknowledges that intentionally supplying false" +
           " information in this report shall " +
           "constitute  an incurable material breach of the Registry-Registrar" +
           " Agreement"));

       report.setOther("other stuff");

       // instantiate the rgp update object
       EPPRgpExtRestore restore = new EPPRgpExtRestore(report);
 
       theCommand.addExtension(new EPPRgpExtUpdate(restore));
    

        EPPEncodeDecodeStats commandStats =
            EPPCodecTst.testEncodeDecode(theCommand);

        System.out.println(commandStats);

        EPPCodecTst.printEnd("testRestoreReport");
    }

	
	/**
	 * Tests sending a restore request using the <code>EPPRgpExtUpdate</code>
	 * domain update extension.
	 */
	public void testRestoreRequest() {

        EPPCodecTst.printStart("testRestoreRequest");

        EPPDomainUpdateCmd theCommand =
            new EPPDomainUpdateCmd(
                                   "ABC-12345-XYZ", "example.com", null,
                                   null, null);
         
       theCommand.addExtension(new EPPRgpExtUpdate(new EPPRgpExtRestore()));
    

        EPPEncodeDecodeStats commandStats =
            EPPCodecTst.testEncodeDecode(theCommand);

        System.out.println(commandStats);

        EPPCodecTst.printEnd("testRestoreRequest");
    }
	
	
	/**
	 * Unit test of <code>testDomainInfoRespWithRgpExt</code>. The response to
	 * <code>testDomainInfoRespWithRgpExt</code> is <code>EPPDomainInfoResp</code>.<br>
	 * This test will be invoked by JUNIT, since it is prefixed with "test"
	 * and is a public method.
	 */

	public void testDomainInfoRespWithRgpExt() {

        EPPCodecTst.printStart("testDomainInfoRespWithRgpExt");

        // trans id of the response
        EPPTransId respTransId =
            new EPPTransId("54321-CLI", "54321-SER");

        Vector statuses = new Vector();
        statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

        // EPPDomainInfo Response
        EPPDomainInfoResp theResponse =
            new EPPDomainInfoResp(
            respTransId, "EXAMPLE1-VRSN", "example.com",
            "ClientX", statuses, "ClientY", new Date(),
                                  new EPPAuthInfo("2fooBAR"));


        // Test with all EPPDomainInfoResp attributes set.
        theResponse.setRegistrant("JD1234-VRSN");

        Vector servers = new Vector();
        servers.addElement("ns1.example.com");
        servers.addElement("ns2.example.com");
        theResponse.setNses(servers);

        Vector hosts = new Vector();
        hosts.addElement("ns1.example.com");
        hosts.addElement("ns2.example.com");
        theResponse.setHosts(hosts);

        // Is contacts supported?
        if (EPPFactory.getInstance().hasService(EPPDomainMapFactory.NS_CONTACT)) {
            Vector contacts = new Vector();
            contacts.addElement(new EPPDomainContact(
                                                     "SH8013-VRSN",
                                                     EPPDomainContact.TYPE_ADMINISTRATIVE));
            contacts.addElement(new EPPDomainContact(
                                                     "SH8013-VRSN",
                                                     EPPDomainContact.TYPE_TECHNICAL));
            theResponse.setContacts(contacts);
        }

        theResponse.setLastUpdatedBy("ClientX");
        theResponse.setLastUpdatedDate(new Date());
        theResponse.setLastTransferDate(new Date());

        respTransId = new EPPTransId("54321-CLI", "54321-SER");;
        theResponse.setTransId(respTransId);
        theResponse.setRoid("EXAMPLE1-VRSN");

        theResponse.setResult(EPPResult.SUCCESS);

        // instantiate the rgp inf data object
        EPPRgpExtInfData infData = new EPPRgpExtInfData();

        // Set individual status
        EPPRgpExtStatus rgpStatus = new EPPRgpExtStatus();
        rgpStatus.setMessage("human readable message");
        rgpStatus.setLang("fr");
        rgpStatus.setStatus(EPPRgpExtStatus.AUTO_RENEW_PERIOD);
        infData.addStatus(rgpStatus);

        // set the inf data in the response as an extension
        Vector commandExtensions = new Vector();
        commandExtensions.add(infData);

        theResponse.setExtensions(commandExtensions);

		EPPEncodeDecodeStats responseStats;
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainInfoRespWithRgpExt");
	}

	// End EPPRgpExtTst.testDomainInfo()

	/**
	 * Unit test of <code>testDomainInfoRespWithRgpExt</code>. This tests 
	 * a <code>EPPRgpExtInfData</code> with multiple statuses.
	 */

	public void testDomainInfoRespWithRgpExtStatuses() {

        EPPCodecTst.printStart("testDomainInfoRespWithRgpExtStatuses");

        // trans id of the response
        EPPTransId respTransId =
            new EPPTransId("54321-CLI", "54321-SER");

        Vector statuses = new Vector();
        statuses.addElement(new EPPDomainStatus(EPPDomainStatus.ELM_STATUS_OK));

        // EPPDomainInfo Response
        EPPDomainInfoResp theResponse =
            new EPPDomainInfoResp(
            respTransId, "EXAMPLE1-VRSN", "example.com",
            "ClientX", statuses, "ClientY", new Date(),
                                  new EPPAuthInfo("2fooBAR"));


         theResponse.setLastUpdatedBy("ClientX");
        theResponse.setLastUpdatedDate(new Date());
        theResponse.setLastTransferDate(new Date());

        respTransId = new EPPTransId("54321-CLI", "54321-SER");;
        theResponse.setTransId(respTransId);
        theResponse.setRoid("EXAMPLE1-VRSN");

        theResponse.setResult(EPPResult.SUCCESS);

        // instantiate the rgp inf data object
        EPPRgpExtInfData infData = new EPPRgpExtInfData();

        // Set individual status
       infData.addStatus(new EPPRgpExtStatus(EPPRgpExtStatus.RENEW_PERIOD));
       infData.addStatus(new EPPRgpExtStatus(EPPRgpExtStatus.ADD_PERIOD));

        Vector commandExtensions = new Vector();
        commandExtensions.add(infData);

        theResponse.setExtensions(commandExtensions);

		EPPEncodeDecodeStats responseStats;
		responseStats = EPPCodecTst.testEncodeDecode(theResponse);
		System.out.println(responseStats);

		EPPCodecTst.printEnd("testDomainInfoRespWithRgpExtStatuses");
	}


	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code>
	 * with the <code>EPPCodec</code>.
	 */
	protected void setUp() {
	}

	// End EPPRgpExtTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
	}

	// End EPPRgpExtTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPRgpExtTst</code>.
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite(EPPRgpExtTst.class);

		// iterations Property
		String numIterProp = System.getProperty("iterations");

		if (numIterProp != null) {
			numIterations = Integer.parseInt(numIterProp);
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.domain.EPPDomainMapFactory");
			EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.rgppoll.EPPRgpPollMapFactory");
            EPPFactory.getInstance().addExtFactory("com.verisign.epp.codec.rgpext.EPPRgpExtFactory");
		}
		 catch (EPPCodecException e) {
			Assert.fail("EPPCodecException adding EPPDomainMapFactory or EPPRgpExtFactory to EPPCodec: "
						+ e);
		}

		// Add the EPPContactMapFactory to the EPPCodec.

		/*
		 * try {
		 * EPPFactory.getInstance().addMapFactory("com.verisign.epp.codec.contact.EPPContactMapFactory"); }
		 * catch (EPPCodecException e) { Assert.fail("EPPCodecException adding
		 * EPPContactMapFactory to EPPCodec: " + e);
		 */
		return suite;
	}

	// End EPPRgpExtTst.suite()

	/**
	 * Unit test main, which accepts the following system property options:
	 * <br>
	 *
	 * <ul>
	 * <li>
	 * iterations Number of unit test iterations to run
	 * </li>
	 * <li>
	 * validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off.
	 * </li>
	 * </ul>
	 *
	 *
	 * @param args DOCUMENT ME!
	 */
	public static void main(String[] args) {
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
								   "EPPRgpExtTst Thread " + i,
								   EPPRgpExtTst.suite());
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run(EPPRgpExtTst.suite());
		}
	}

	// End EPPRgpExtTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 *
	 * @param aNumIterations number of iterations to run per test
	 */
	public static void setNumIterations(long aNumIterations) {
		numIterations = aNumIterations;
	}

	// End EPPRgpExtTst.setNumIterations(long)

}
