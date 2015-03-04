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

package com.verisign.epp.codec.coaext;

import java.util.Date;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainCreateCmd;
import com.verisign.epp.codec.domain.EPPDomainInfoResp;
import com.verisign.epp.codec.domain.EPPDomainStatus;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPAuthInfo;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPEncodeDecodeStats;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.coaext package. The unit test
 * will execute, gather statistics, and output the results of a test of each
 * com.verisign.epp.codec.coaext package concrete <code>EPPCoaExt</code>'s and
 * their expected <code>EPPResponse</code>. The unit test is dependent on the
 * use of <a href=http://www.mcwestcorp.com/Junit.html>JUNIT 3.5</a><br>
 * 
 * @author jfaust
 * @version 1.0 Feb 23, 2011
 */
public class EPPCoaExtTst extends TestCase {
	/**
	 * Number of unit test iterations to run. This is set in
	 * <code>EPPCodecTst.main</code>
	 */
	static private long numIterations = 1;


	/**
	 * Creates a new EPPCoaExtTst object.
	 * 
	 * @param name
	 *        DOCUMENT ME!
	 */
	public EPPCoaExtTst ( String name ) {
		super( name );
	}


	// End EPPCoaExtTst(String)

	public void testDomainCreateCommandWithCoaExt () {

		EPPCodecTst.printStart( "testDomainCreateCommandWithCoaExt" );

		// -- Extend Domain Create Command
		EPPDomainCreateCmd theDomainCommand;
		theDomainCommand =
				new EPPDomainCreateCmd( "ABC-12345", "john.doe.com", new EPPAuthInfo(
						"2fooBAR@" ) );

		// Extension
		Vector commandExtensions = new Vector();

		EPPCoaExtAttr attr = new EPPCoaExtAttr();
		attr.setKey( new EPPCoaExtKey( "KEY1" ) );
		attr.setValue( new EPPCoaExtValue( "value1" ) );
		EPPCoaExtCreate create = new EPPCoaExtCreate();
		create.appendAttr( attr );
		commandExtensions.add( create );

		theDomainCommand.setExtensions( commandExtensions );

		EPPEncodeDecodeStats commandStats =
				EPPCodecTst.testEncodeDecode( theDomainCommand );
		System.out.println( commandStats );

		EPPCodecTst.printEnd( "testDomainCreateCommandWithCoaExt" );
	}


	public void testDomainUpdateCommandWithCoaExtPut () {

		EPPCodecTst.printStart( "testDomainUpdateCommandWithCoaExt" );

		// -- Extend Domain Create Command
		EPPDomainUpdateCmd theDomainCommand;
		theDomainCommand = new EPPDomainUpdateCmd( "john.doe.name" );

		// Extension
		Vector commandExtensions = new Vector();

		EPPCoaExtAttr addAttr = new EPPCoaExtAttr();
		addAttr.setKey( new EPPCoaExtKey( "KEY1" ) );
		addAttr.setValue( new EPPCoaExtValue( "value1" ) );
		EPPCoaExtUpdate updateCmd = new EPPCoaExtUpdate();
		updateCmd.appendPutAttr( addAttr );
		commandExtensions.add( updateCmd );

		theDomainCommand.setExtensions( commandExtensions );

		EPPEncodeDecodeStats commandStats =
				EPPCodecTst.testEncodeDecode( theDomainCommand );
		System.out.println( commandStats );

		EPPCodecTst.printEnd( "testDomainUpdateCommandWithCoaExt" );
	}


	public void testDomainUpdateCommandWithCoaExtRem () {

		EPPCodecTst.printStart( "testDomainUpdateCommandWithCoaExtRem" );

		// -- Extend Domain Create Command
		EPPDomainUpdateCmd theDomainCommand;
		theDomainCommand = new EPPDomainUpdateCmd( "john.doe.name" );

		// Extension
		Vector commandExtensions = new Vector();

		EPPCoaExtKey remKey = new EPPCoaExtKey( "KEY1" );
		EPPCoaExtUpdate updateCmd = new EPPCoaExtUpdate();
		updateCmd.appendRemAttr( remKey );
		commandExtensions.add( updateCmd );

		theDomainCommand.setExtensions( commandExtensions );

		EPPEncodeDecodeStats commandStats =
				EPPCodecTst.testEncodeDecode( theDomainCommand );
		System.out.println( commandStats );

		EPPCodecTst.printEnd( "testDomainUpdateCommandWithCoaExtRem" );
	}


	public void testDomainUpdateCommandWithCoaExtAll () {

		EPPCodecTst.printStart( "testDomainUpdateCommandWithCoaExtAll" );

		// -- Extend Domain Create Command
		EPPDomainUpdateCmd theDomainCommand;
		theDomainCommand = new EPPDomainUpdateCmd( "john.doe.name" );

		// Extension
		Vector commandExtensions = new Vector();
		EPPCoaExtUpdate updateCmd = new EPPCoaExtUpdate();

		EPPCoaExtKey remKey = new EPPCoaExtKey( "KEY1" );
		updateCmd.appendRemAttr( remKey );

		EPPCoaExtAttr addAttr = new EPPCoaExtAttr();
		addAttr.setKey( new EPPCoaExtKey( "KEY2" ) );
		addAttr.setValue( new EPPCoaExtValue( "value2" ) );
		updateCmd.appendPutAttr( addAttr );

		commandExtensions.add( updateCmd );
		theDomainCommand.setExtensions( commandExtensions );

		EPPEncodeDecodeStats commandStats =
				EPPCodecTst.testEncodeDecode( theDomainCommand );
		System.out.println( commandStats );

		EPPCodecTst.printEnd( "testDomainUpdateCommandWithCoaExtAll" );
	}


	public void testDomainInfoRespWithCoaExtRem () {

		EPPCodecTst.printStart( "testDomainInfoRespWithCoaExtRem" );

		// trans id of the response
		EPPTransId respTransId = new EPPTransId( "54321-CLI", "54321-SER" );

		Vector statuses = new Vector();
		statuses.addElement( new EPPDomainStatus( EPPDomainStatus.ELM_STATUS_OK ) );

		// EPPDomainInfo Response
		EPPDomainInfoResp theResponse =
				new EPPDomainInfoResp( respTransId, "EXAMPLE1-VRSN", "example.tv",
						"ClientX", statuses, "ClientY", new Date(), new EPPAuthInfo(
								"2fooBAR" ) );

		theResponse.setResult( EPPResult.SUCCESS );

		EPPCoaExtAttr attr = new EPPCoaExtAttr();
		attr.setKey( new EPPCoaExtKey( "KEY1" ) );
		attr.setValue( new EPPCoaExtValue( "value1" ) );
		EPPCoaExtInfData infData = new EPPCoaExtInfData();
		infData.appendAttr( attr );

		theResponse.addExtension( infData );

		EPPEncodeDecodeStats responseStats =
				EPPCodecTst.testEncodeDecode( theResponse );
		System.out.println( responseStats );

		EPPCodecTst.printEnd( "testDomainInfoRespWithCoaExtRem" );
	}


	/**
	 * JUNIT <code>setUp</code> method, which sets the default client Id to
	 * "theRegistrar" and initializes the <code>EPPDomainMapFactory</code> with
	 * the <code>EPPCodec</code>.
	 */
	protected void setUp () {
	}


	// End EPPCoaExtTst.setUp();

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown () {
	}


	// End EPPCoaExtTst.tearDown();

	/**
	 * JUNIT <code>suite</code> static method, which returns the tests associated
	 * with <code>EPPCoaExtTst</code>.
	 * 
	 * @return DOCUMENT ME!
	 */
	public static Test suite () {

		TestSuite suite = new TestSuite( EPPCoaExtTst.class );

		// iterations Property
		String numIterProp = System.getProperty( "iterations" );

		if ( numIterProp != null ) {
			numIterations = Integer.parseInt( numIterProp );
		}

		// Add the EPPDomainMapFactory to the EPPCodec.
		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory" );
			EPPFactory.getInstance().addExtFactory(
					"com.verisign.epp.codec.coaext.EPPCoaExtFactory" );
		}
		catch ( EPPCodecException e ) {
			Assert
					.fail( "EPPCodecException adding EPPDomainMapFactory or EPPCoaExtFactory to EPPCodec: "
							+ e );
		}
		return suite;
	}


	// End EPPCoaExtTst.suite()

	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li>iterations Number of unit test iterations to run</li>
	 * <li>validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be off.</li>
	 * </ul>
	 * 
	 * @param args
	 *        DOCUMENT ME!
	 */
	public static void main ( String[] args ) {
		// Number of Threads
		int numThreads = 1;
		String threadsStr = System.getProperty( "threads" );

		if ( threadsStr != null ) {
			numThreads = Integer.parseInt( threadsStr );
		}

		// Run test suite in multiple threads?
		if ( numThreads > 1 ) {
			// Spawn each thread passing in the Test Suite
			for ( int i = 0; i < numThreads; i++ ) {
				TestThread thread =
						new TestThread( "EPPCoaExtTst Thread " + i, EPPCoaExtTst
								.suite() );
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run( EPPCoaExtTst.suite() );
		}
	}


	// End EPPCoaExtTst.main(String [])

	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *        number of iterations to run per test
	 */
	public static void setNumIterations ( long aNumIterations ) {
		numIterations = aNumIterations;
	}

	// End EPPCoaExtTst.setNumIterations(long)
}
