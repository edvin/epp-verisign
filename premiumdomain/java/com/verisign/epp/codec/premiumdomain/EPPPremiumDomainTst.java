/*******************************************************************************
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced, or disclosed without the
 * written approval of the General Manager of VeriSign Information Services.
 * 
 * PRIVILEGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION (REGISTRY
 * SENSITIVE INFORMATION)
 * Copyright (c) 2007 VeriSign, Inc. All rights reserved.
 * **********************************************************
 */

package com.verisign.epp.codec.premiumdomain;

import java.math.BigDecimal;
import java.util.Vector;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.verisign.epp.codec.domain.EPPDomainCheckCmd;
import com.verisign.epp.codec.domain.EPPDomainCheckResp;
import com.verisign.epp.codec.domain.EPPDomainCheckResult;
import com.verisign.epp.codec.domain.EPPDomainUpdateCmd;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPCodecTst;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.util.TestThread;

/**
 * Is a unit test of the com.verisign.epp.codec.premiumdomain package. The unit
 * test will execute, gather statistics, and output the results of a test of
 * each com.verisign.epp.codec.premiumdomain package. <br>
 */
public class EPPPremiumDomainTst extends TestCase {

	private static long numIterations = 1;


	/**
	 * Creates a new EPPPremiumDomainTst object.
	 * 
	 * @param name
	 *        Name of test to execute
	 */
	public EPPPremiumDomainTst ( String name ) {
		super( name );
	}


	/**
	 * JUNIT <code>setUp</code> method.
	 */
	protected void setUp () {
	}


	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown () {
	}

	/**
	 * Tests the <code>EPPPremiumDomainCheck</code> check command extension. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Premium Domain Check with flag as true
	 * <li>Premium Domain Check with flag as false
	 * </ol>
	 */
	public void testSinglePremiumDomainCheck () {
		EPPCodecTst.printStart( "testSinglePremiumDomainCheck" );

		EPPDomainCheckCmd theCommand;
		EPPDomainCheckResp theResponse;
		EPPCodecTst.setNumIterations(numIterations);
										
		// Create single domain check command with flag as true
		theCommand = new EPPDomainCheckCmd("ABC-12345", "example-flag-true.tv");
		theCommand.addExtension(new EPPPremiumDomainCheck(true));		 
		EPPCodecTst.testEncodeDecode(theCommand);
								
		// Response for a single domain check command with flag as true
		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPDomainCheckResp(respTransId, new EPPDomainCheckResult(theCommand.getNames().get( 0 ).toString(), true));
		theResponse.setResult(EPPResult.SUCCESS);
				
		EPPPremiumDomainCheckResult eppPremiumDomainCheckResult = null;
		eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(theCommand.getNames().get( 0 ).toString(), true);
		eppPremiumDomainCheckResult.setPrice( new BigDecimal( "125.00" ) );
		eppPremiumDomainCheckResult.setRenewalPrice( new BigDecimal( "75.00" ) );
		eppPremiumDomainCheckResult.setPriceUnit( EPPPremiumDomainCheckResult.PRICE_UNIT_USD );		
		
		EPPPremiumDomainCheckResp respExt = new EPPPremiumDomainCheckResp( eppPremiumDomainCheckResult );
		theResponse.addExtension(respExt);		
		EPPCodecTst.testEncodeDecode(theResponse);
				
		// Create single domain check command with flag as false
		theCommand = new EPPDomainCheckCmd("ABC-12345", "example-flag-false.tv");
		theCommand.addExtension(new EPPPremiumDomainCheck(false));		 
		EPPCodecTst.testEncodeDecode(theCommand);
						
		// Response for a single domain check command with flag as false
		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPDomainCheckResp(respTransId, new EPPDomainCheckResult(theCommand.getNames().get( 0 ).toString(), true));
		theResponse.setResult(EPPResult.SUCCESS);						
		EPPCodecTst.testEncodeDecode(theResponse);		
		
		EPPCodecTst.printEnd( "testSinglePremiumDomainCheck" );
	}
	

	/**
	 * Tests the <code>EPPPremiumDomainCheck</code> check command extension for multiple domains. The
	 * tests include the following:<br>
	 * <br>
	 * <ol>
	 * <li>Premium Domains Check without flag value (uses default)
	 * <li>Premium Domains Check with flag as true
	 * <li>Premium Domains Check with flag as false
	 * </ol>
	 */
	public void testMultiplePremiumDomainCheck () {
		EPPCodecTst.printStart( "testMultiplePremiumDomainCheck" );

		EPPDomainCheckCmd theCommand;
		EPPDomainCheckResp theResponse;
		EPPPremiumDomainCheckResp respExt = null;
		EPPPremiumDomainCheckResult eppPremiumDomainCheckResult = null;
		Vector domains = null;
		Vector domainResults = null;
		Vector premiumResults = null;
		boolean available = true;
		
		EPPCodecTst.setNumIterations(numIterations);
										
		// Create multiple domains check command with flag as true
		domains = new Vector();
		for (int i = 1; i <= 3; i++) {
			domains.addElement("example-flag-true-" + i +".tv");
		}
		theCommand = new EPPDomainCheckCmd("ABC-12345", domains);
		theCommand.addExtension(new EPPPremiumDomainCheck(true));		 
		EPPCodecTst.testEncodeDecode(theCommand);
								
		// Response for a single domain check command with flag as true
		domainResults = new Vector();		
		for (int i = 0; i < domains.size(); i++) {
			domainResults.addElement(new EPPDomainCheckResult(theCommand.getNames().get( i ).toString(), available));
			available = !available;
		}	
		
		EPPTransId respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPDomainCheckResp(respTransId, domainResults);
		theResponse.setResult(EPPResult.SUCCESS);
				
		premiumResults = new Vector();
		for (int i = 0; i < theResponse.getCheckResults().size(); i++) {
			EPPDomainCheckResult currResult =
				(EPPDomainCheckResult) theResponse.getCheckResults().elementAt(i);

			eppPremiumDomainCheckResult = new EPPPremiumDomainCheckResult(currResult.getName(), true);
			
			if (currResult.isAvailable()) {
				eppPremiumDomainCheckResult.setPrice( new BigDecimal( "125.00" ) );
				eppPremiumDomainCheckResult.setRenewalPrice( new BigDecimal( "75.00" ) );
				eppPremiumDomainCheckResult.setPriceUnit( EPPPremiumDomainCheckResult.PRICE_UNIT_USD );
			}
			premiumResults.addElement(eppPremiumDomainCheckResult);	
		}
		
		respExt = new EPPPremiumDomainCheckResp( premiumResults );
		theResponse.addExtension(respExt);		
		EPPCodecTst.testEncodeDecode(theResponse);
		
				
		// Create multiple domains check command with flag as false
		domains = new Vector();
		for (int i = 1; i <= 3; i++) {
			domains.addElement("example-flag-false-" + i +".tv");
		}
		theCommand = new EPPDomainCheckCmd("ABC-12345", domains);
		theCommand.addExtension(new EPPPremiumDomainCheck(false));		 
		EPPCodecTst.testEncodeDecode(theCommand);
						
		// Response for a single domain check command with flag as false
		domainResults = new Vector();		
		for (int i = 0; i < domains.size(); i++) {
			domainResults.addElement(new EPPDomainCheckResult(theCommand.getNames().get( i ).toString(), available));
			available = !available;
		}	
		
		respTransId = new EPPTransId(theCommand.getTransId(), "54321-XYZ");
		theResponse = new EPPDomainCheckResp(respTransId, domainResults);
		theResponse.setResult(EPPResult.SUCCESS);
		EPPCodecTst.testEncodeDecode(theResponse);
		
		EPPCodecTst.printEnd( "testMultiplePremiumDomainCheck" );
	}
		
	/**
	 * Tests the <code>EPPPremiumDomainReAssignCmd</code> Update command extension.
	 */
	public void testPremiumDomainReAssign () {
		EPPCodecTst.printStart( "testPremiumDomainReAssign" );

		EPPDomainUpdateCmd theCommand;
		EPPCodecTst.setNumIterations(numIterations);
		
		theCommand = new EPPDomainUpdateCmd( "premium.tv" );
		theCommand.setTransId( "ABC-12345-XYZ" );
		theCommand.addExtension( new EPPPremiumDomainReAssignCmd(
				"testregistrar" ) );
		EPPCodecTst.testEncodeDecode( theCommand );


		// Encode Update Response (Standard EPPResponse)
		EPPTransId respTransId = new EPPTransId( theCommand.getTransId(),
				"54321-XYZ" );
		EPPResponse theResponse = new EPPResponse( respTransId );
		theResponse.setResult( EPPResult.SUCCESS );

		EPPCodecTst.testEncodeDecode( theResponse );

		EPPCodecTst.printEnd( "testPremiumDomainReAssign" );
	}


	/**
	 * JUNIT <code>suite</code> static method, which returns the tests
	 * associated with <code>EPPPremiumDomainTst</code>.
	 * 
	 * @return <code>Junit</code> tests
	 */
	public static Test suite () {

		System.setProperty( "validate", "true" );
		System.setProperty( "fullschemachecking", "false" );

		EPPCodecTst.initEnvironment();

		TestSuite suite = new TestSuite( EPPPremiumDomainTst.class );

		// iterations Property
		String numIterProp = System.getProperty( "iterations" );

		if ( numIterProp != null ) {
			numIterations = Integer.parseInt( numIterProp );
		}

		try {
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.host.EPPHostMapFactory" );
			EPPFactory.getInstance().addMapFactory(
					"com.verisign.epp.codec.domain.EPPDomainMapFactory" );
			EPPFactory
					.getInstance()
					.addExtFactory(
							"com.verisign.epp.codec.premiumdomain.EPPPremiumDomainExtFactory" );
		}
		catch ( EPPCodecException e ) {
			Assert
					.fail( "EPPCodecException adding EPPDomainMapFactory or EPPPremiumDomainExtFactory to EPPCodec: "
							+ e );
		}
		return suite;
	}


	/**
	 * Unit test main, which accepts the following system property options: <br>
	 * <ul>
	 * <li> iterations Number of unit test iterations to run </li>
	 * <li> validate Turn XML validation on (<code>true</code>) or off (
	 * <code>false</code>). If validate is not specified, validation will be
	 * off. </li>
	 * </ul>
	 * 
	 * @param args
	 *        Command line arguments
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
				TestThread thread = new TestThread(
						"EPPPremiumDomainTst Thread " + i, EPPPremiumDomainTst
								.suite() );
				thread.start();
			}
		}
		else { // Single threaded mode.
			junit.textui.TestRunner.run( EPPPremiumDomainTst.suite() );
		}
	}


	/**
	 * Sets the number of iterations to run per test.
	 * 
	 * @param aNumIterations
	 *        number of iterations to run per test
	 */
	public static void setNumIterations ( long aNumIterations ) {
		numIterations = aNumIterations;
	}
}
