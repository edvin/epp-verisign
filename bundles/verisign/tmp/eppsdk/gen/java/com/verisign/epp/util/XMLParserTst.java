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

package com.verisign.epp.util;


// PoolMan Imports
import com.codestudio.util.*;

// JUnit
import junit.framework.*;

import org.apache.xerces.impl.Constants;

// Xerces Imports
import org.apache.xerces.parsers.*;

import org.w3c.dom.Document;

import org.xml.sax.*;

// Java imports
import java.io.*;
import java.util.*;

// XML API
import javax.xml.parsers.DocumentBuilder;

// EPP SDK imports
import com.verisign.epp.util.*;


/**
 * JUnit test of XML Parsing implementations used by the EPP SDK.  The Test
 * uses a pool of parsers an outputs various performance metrics. Requires the
 * following System Properties to be defined: eppsdk.gen.xml.dir  - the
 * directory where EPP XML instance documents are located
 * eppsdk.gen.xml.instance  - the XML document to use for the parsing tests
 * eppsdk.gen.xml.test.parserImpl - the DocumentBuilder instance to test
 * eppsdk.gen.xml.test.iterations - the number of times to parse the instance
 * doc per thread. eppsdk.gen.xml.test.threads - the number of threads to use
 * in the test
 * 
 * <p>
 * Title: EPP SDK
 * </p>
 * 
 * <p>
 * Description: EPP SDK for 1.0 Spec
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: VeriSign
 * </p>
 *
 * @author clloyd
 * @version 1.0
 */
public class XMLParserTst extends TestCase {
	/** The directory where EPP XML instance documents are located */
	private static final String TEST_DIR =
		System.getProperty("eppsdk.gen.xml.dir");

	/** The number of iterations to run */
	private static final String ITERATIONS =
		System.getProperty("eppsdk.gen.xml.test.iterations");

	/** Number of threads to run */
	private static final String THREADS =
		System.getProperty("eppsdk.gen.xml.test.threads");

	/** The parser implementation to test */
	private static final String PARSER_IMPL =
		System.getProperty("eppsdk.gen.xml.test.parserImpl");

	/** The symbol table size */
	private static final String SYMBOL_TABLE_SIZE =
		System.getProperty("eppsdk.gen.xml.test.symbolTableSize");

	/** The name of the pool that this test uses to get XML Parsers */
	public static final String POOL_NAME =
		"com.verisign.epp.codec.gen.XMLParserTst";

	/** Number of parsers that are in the pool used by this test */
	public static final int NUM_PARSERS_IN_POOL = 10;

	/** The test file that this test will use to excercise the parser */
	private String testFile = null;

	/**
	 * Create a new instance of the JUnit test XMLParserTst
	 *
	 * @param name
	 * @param aXmlFile DOCUMENT ME!
	 */
	public XMLParserTst(String name, String aXmlFile) {
		super(name);
		testFile = aXmlFile;
	}

	/**
	 * Prints the results.
	 *
	 * @param out DOCUMENT ME!
	 * @param uri DOCUMENT ME!
	 * @param time DOCUMENT ME!
	 * @param tagginess DOCUMENT ME!
	 * @param repetition DOCUMENT ME!
	 */
	public static void printResults(
									PrintWriter out, String uri, long time,
									boolean tagginess, int repetition) {
		// filename.xml: 631 ms (4 elems, 0 attrs, 78 spaces, 0 chars)
		out.print(uri);
		out.print(": ");

		if (repetition == 1) {
			out.print(time);
		}
		else {
			out.print(time);
			out.print('/');
			out.print(repetition);
			out.print('=');
			out.print((float) time / repetition);
		}

		out.print(" ms");
		out.println();
		out.flush();
	}

	/**
	 * Tests turning of the validation feature of the parser
	 */
	public void testTurnOffValidation() {
		String				   xmlInstance = getFileContents(testFile);

		byte[]				   bytesToParse = xmlInstance.getBytes();

		EPPSchemaCachingParser parser = null;

		try {
			parser =
				(EPPSchemaCachingParser) GenericPoolManager.getInstance()
														   .getPool(POOL_NAME)
														   .requestObject();

			parser.setFeature(
							  EPPSchemaCachingParser.VALIDATION_FEATURE_ID,
							  false);

			Assert.assertEquals(false, parser.isValidating());

			Document doc = null;
			doc = parser.parse(new InputSource(new ByteArrayInputStream(bytesToParse)));

			Assert.assertNotNull(doc);
		}
		 catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		 finally {
			GenericPoolManager.getInstance().getPool(POOL_NAME).returnObject(parser);
		}
	}

	/**
	 * Test using a parser from the pool with multiple threads. Test just
	 * displays parse times for the default configuration.
	 */
	public void testParsing() {
		String xmlInstance = getFileContents(testFile);

		// Hold the set of threads in an array so we can
		// call join() on each one.  This lets us wait until
		// all threads have completed.
		ArrayList threadArray = new ArrayList();

		long	  startTime    = System.currentTimeMillis();
		long	  memoryBefore = Runtime.getRuntime().freeMemory();

		for (int i = 0; i < Integer.parseInt(THREADS); i++) {
			// Instantiate the runner thread and start it
			RunnerThread runner =
				new RunnerThread(Integer.parseInt(ITERATIONS), xmlInstance);

			threadArray.add(runner);
			runner.start();
		}

		// wait for threads to finish
		try {
			for (int j = 0; j < threadArray.size(); j++) {
				Thread runningThread = (Thread) threadArray.get(j);
				runningThread.join();
			}
		}
		 catch (InterruptedException e) {
			e.printStackTrace();
		}

		long finishTime = System.currentTimeMillis();
		long totalTime = finishTime - startTime;

		System.out.println("Total time was: " + totalTime);

		long memoryAfter = Runtime.getRuntime().freeMemory();
		long memory = memoryBefore - memoryAfter;
	}

	/**
	 * DOCUMENT ME!
	 */
	public void testSetSymbolTableSize() {
		String				   xmlInstance = getFileContents(testFile);

		byte[]				   bytesToParse = xmlInstance.getBytes();

		EPPSchemaCachingParser parser = null;

		try {
			parser =
				(EPPSchemaCachingParser) GenericPoolManager.getInstance()
														   .getPool(POOL_NAME)
														   .requestObject();

			parser.setSymbolTableSize(1299709);

			Document doc = null;
			doc = parser.parse(new InputSource(new ByteArrayInputStream(bytesToParse)));

			Assert.assertNotNull(doc);

			parser.setSymbolTableSize(EPPSchemaCachingParser.BIG_PRIME);

			doc = parser.parse(new InputSource(new ByteArrayInputStream(bytesToParse)));
			Assert.assertNotNull(doc);
		}
		 catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		 finally {
			GenericPoolManager.getInstance().getPool(POOL_NAME).returnObject(parser);
		}
	}

	/**
	 * Set the symbol table size to 0 and test parsing with an
	 * XMLGrammarCachingConfiguration.  Verify the memory usage is low.
	 */
	public void testSetNoSymbolTableSize() {
		/**
		 * @todo implement this test
		 */
	}

	/**
	 * DOCUMENT ME!
	 */
	public void testLockSchemaCache() {
		/**
		 * @todo implement this test
		 */
	}

	/**
	 * DOCUMENT ME!
	 */
	public void testSchemaNotFoundInClasspath() {
		/**
		 * @todo implement this test
		 */
	}

	/**
	 * Tests that a non validating DOMParser instance from Xerces will return a
	 * DOM Document
	 */
	public void testDomParser() {
		String    xmlInstance = getFileContents(testFile);

		byte[]    bytesToParse = xmlInstance.getBytes();

		DOMParser domParser = new DOMParser();

		Document  doc = null;

		try {
			domParser.setFeature(
								 EPPSchemaCachingParser.VALIDATION_FEATURE_ID,
								 false);
			domParser.parse(new InputSource(new ByteArrayInputStream(bytesToParse)));
		}
		 catch (Exception e) {
			Assert.fail(e.getMessage());
		}

		doc = domParser.getDocument();
		Assert.assertNotNull(doc);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static Test suite() {
		System.out.println("suite");

		TestSuite suite = new TestSuite();

		String    testFile = System.getProperty("eppsdk.gen.xml.instance");

		if (testFile == null) {
			testFile = File.separator + "login.xml";
			System.out.println("No testfile XML instance specified.  This "
							   + "can set by setting the eppsdk.gen.xml.instance"
							   + " System property." + " Using default: "
							   + testFile);
		}

		System.out.println("Using XML instance file: " + testFile);

		/**
		 * @todo Have 2 possible suites to return.  One for xerces143 and one
		 * 		 of xerces26.  Return the one that is set by some system
		 * 		 property.
		 */
		suite.addTest(new XMLParserTst(
									   "testParsing",
									   TEST_DIR + File.separator + testFile));
		suite.addTest(new XMLParserTst(
									   "testTurnOffValidation",
									   TEST_DIR + File.separator + testFile));
		suite.addTest(new XMLParserTst(
									   "testDomParser",
									   TEST_DIR + File.separator + testFile));
		suite.addTest(new XMLParserTst(
									   "testSetSymbolTableSize",
									   TEST_DIR + File.separator + testFile));

		return suite;
	}

	/**
	 * Returns the contents of a file as a  String
	 *
	 * @param aFilename
	 *
	 * @return DOCUMENT ME!
	 */
	public String getFileContents(String aFilename) {
		StringBuffer commandBuffer = new StringBuffer();

		try {
			BufferedReader br = new BufferedReader(new FileReader(aFilename));
			String		   nextLine = null;

			while ((nextLine = br.readLine()) != null) {
				commandBuffer.append(nextLine);
			}
		}
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 catch (IOException e) {
			e.printStackTrace();
		}

		return commandBuffer.toString();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws Exception DOCUMENT ME!
	 */
	protected void setUp() throws Exception {
		super.setUp();

		initParserPool();
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @throws Exception DOCUMENT ME!
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		GenericPoolManager.getInstance().removePool(POOL_NAME);
	}

	/**
	 * DOCUMENT ME!
	 */
	private void initParserPool() {
		GenericPoolMetaData meta = new GenericPoolMetaData();

		meta.setName(POOL_NAME);
		meta.setObjectType(PARSER_IMPL);
		meta.setInitialObjects(NUM_PARSERS_IN_POOL);
		meta.setMinimumSize(NUM_PARSERS_IN_POOL);
		meta.setMaximumSize(NUM_PARSERS_IN_POOL);
		meta.setUserTimeout(0);
		meta.setDebugging(false);

		GenericPool newPool = new GenericPool(meta);
		GenericPoolManager.getInstance().addPool(POOL_NAME, newPool);
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author $author$
	 * @version $Revision: 1.3 $
	 */
	class RunnerThread extends Thread {
		/** DOCUMENT ME! */
		int iterations = 1;

		/** DOCUMENT ME! */
		String xmlInstance = null;

		/**
		 * Creates a new RunnerThread object.
		 *
		 * @param aIterations DOCUMENT ME!
		 * @param aXmlInstance DOCUMENT ME!
		 */
		RunnerThread(int aIterations, String aXmlInstance) {
			iterations	    = aIterations;
			xmlInstance     = aXmlInstance;
		}

		/**
		 * DOCUMENT ME!
		 */
		public void run() {
			System.out.println("Thread " + getName() + " started");
			System.out.println(getName() + " executing " + iterations
							   + " iterations");

			byte[]		    bytesToParse = xmlInstance.getBytes();

			long		    timeBefore   = System.currentTimeMillis();
			long		    memoryBefore = Runtime.getRuntime().freeMemory();

			DocumentBuilder parser = null;
			Document	    doc    = null;

			try {
				// get the parser from the pool
				parser =
					(DocumentBuilder) GenericPoolManager.getInstance()
														.getPool(POOL_NAME)
														.requestObject();

				for (int i = 0; i < iterations; i++) {
					InputSource inSource =
						new InputSource(new ByteArrayInputStream(bytesToParse));

					doc = parser.parse(inSource);

					// output the doc here???
					Assert.assertNotNull(doc);
				}
			}
			 catch (org.xml.sax.SAXException e) {
				e.printStackTrace();
			}
			 catch (java.io.IOException e) {
				e.printStackTrace();
			}
			 finally {
				GenericPoolManager.getInstance().getPool(POOL_NAME)
								  .returnObject(parser);
			}

			long timeAfter = System.currentTimeMillis();
			long time = timeAfter - timeBefore;

			XMLParserTst.printResults(
									  new PrintWriter(System.out), getName(),
									  time, false, iterations);
		}
	}
}
