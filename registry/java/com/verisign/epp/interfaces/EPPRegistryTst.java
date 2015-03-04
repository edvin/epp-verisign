package com.verisign.epp.interfaces;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.registry.EPPRegistryCheckResp;
import com.verisign.epp.codec.registry.EPPRegistryCheckResult;
import com.verisign.epp.codec.registry.EPPRegistryContact;
import com.verisign.epp.codec.registry.EPPRegistryCreateResp;
import com.verisign.epp.codec.registry.EPPRegistryDomain;
import com.verisign.epp.codec.registry.EPPRegistryFields;
import com.verisign.epp.codec.registry.EPPRegistryHost;
import com.verisign.epp.codec.registry.EPPRegistryInfoResp;
import com.verisign.epp.codec.registry.EPPRegistryPhase;
import com.verisign.epp.codec.registry.EPPRegistryRelated;
import com.verisign.epp.codec.registry.EPPRegistryServices.EPPRegistryObjURI;
import com.verisign.epp.codec.registry.EPPRegistryServicesExt.EPPRegistryExtURI;
import com.verisign.epp.codec.registry.EPPRegistryZone;
import com.verisign.epp.codec.registry.EPPRegistryZoneInfo;
import com.verisign.epp.codec.registry.EPPRegistryZoneMember;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.Environment;
import com.verisign.epp.util.TestThread;

public class EPPRegistryTst extends TestCase {
	/**
	 * Handle to the Singleton EPP Application instance (
	 * <code>EPPApplicationSingle</code>)
	 */
	private static EPPApplicationSingle app = EPPApplicationSingle
			.getInstance();

	/** Name of configuration file to use for test (default = epp.config). */
	private static String configFileName = "epp.config";

	/** Logging category */
	private static final Logger cat = Logger.getLogger(EPPRegistryTst.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** EPP Registry associated with test */
	private EPPRegistry registry = null;

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
	private Random rd = new SecureRandom();

	/**
	 * Allocates an <code>EPPRegistryTst</code> with a logical name. The
	 * constructor will initialize the base class <code>TestCase</code> with the
	 * logical name.
	 * 
	 * @param name
	 *            Logical name of the test
	 */
	public EPPRegistryTst(String name) {
		super(name);
	}

	public void testRegistry() {
		int numIterations = 1;

		String iterationsStr = System.getProperty("iterations");

		if (iterationsStr != null) {
			try {
				numIterations = Integer.parseInt(iterationsStr);
				numIterations = (numIterations < 1) ? 1 : numIterations;
			} catch (Exception e) {
				numIterations = 1;
			}
		}

		printStart("Test Suite");

		registryCreate("com", true);
		registryCreate("newtld1", true);
		// Create existing tld will result in error
		registryCreate("newtld1", false);
		registryCreate("newtld2", true);
		registryCreate("newtld3", true);

		registryUpdate("newtld1", true);
		registryUpdate("newtld1", true);
		registryUpdate("newtld2", true);
		registryUpdate("newtld3", true);
		// Updating non-existing TLD will result in error
		registryUpdate("newtld4", false);

		Map tldAvails = new HashMap();
		tldAvails.put("newtld1", Boolean.FALSE);
		tldAvails.put("newtld2", Boolean.FALSE);
		tldAvails.put("newtld3", Boolean.FALSE);
		// newtld4 is available
		tldAvails.put("newtld4", Boolean.TRUE);
		registryCheck(tldAvails);

		// Get all tld info.
		registryInfo(null);
		registryInfo("newtld2");
		registryInfo("com");

		registryDelete("newtld1", true);
		registryDelete("newtld1", false);
		registryDelete("newtld10", false);

		printEnd("Test Suite");
	}

	/**
	 * Unit test of <code>EPPRegistry.sendCheck</code>.
	 */
	public void registryCheck(Map tldNames) {
		printStart("registryCheck");

		EPPRegistryCheckResp response;
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.print("registryCheck:");
			registry.resetRegistry();
			registry.setTransId("ABC-12345-XYZ");

			for (Iterator it = tldNames.entrySet().iterator(); it.hasNext();) {
				String name = (String) ((Entry) it.next()).getKey();
				System.out.print(" " + name + " ");
				registry.addTld(name);
			}
			System.out.println("");

			response = registry.sendCheck();

			System.out.println("registryCheck: Response = [" + response
					+ "]\n\n");

			for (Iterator it = tldNames.entrySet().iterator(); it.hasNext();) {
				Entry entry = (Entry) it.next();
				String name = (String) entry.getKey();
				Boolean available = (Boolean) entry.getValue();
				inner: for (Iterator itt = response.getCheckResults()
						.iterator(); itt.hasNext();) {
					EPPRegistryCheckResult result = (EPPRegistryCheckResult) itt
							.next();
					if (result.getName().equals(name)) {
						if (result.isAvailable().booleanValue() == available
								.booleanValue()) {
							break inner;
						} else {
							fail("Expected availability for tld \"" + name
									+ "\": " + available.booleanValue()
									+ ", but got: " + result.isAvailable());
						}
					}
				}
			}

			assertTrue(response != null && response.isSuccess());
		} catch (EPPCommandException e) {
			handleException(e);
		}

		printEnd("registryCheck");
	}

	/**
	 * Unit test of <code>EPPRegistry.sendCreate</code>.
	 */
	public void registryCreate(String tldName, boolean shouldSucceed) {
		printStart("registryCreate");

		EPPRegistryCreateResp response = null;
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("registryCreate: " + tldName);
			registry.resetRegistry();
			registry.setTransId("ABC-12345-XYZ");
			registry.addTld(tldName);
			registry.setAuthString("ClientX");

			response = registry.sendCreate();
			System.out.println("registryCreate: Response = [" + response
					+ "]\n\n");

			if (shouldSucceed) {
				assertTrue(response != null && response.isSuccess());
			} else {
				fail("Expecting error in response");
			}
		} catch (EPPCommandException e) {
			if (shouldSucceed) {
				handleException(e);
			}
		}

		printEnd("registryCreate");
	}

	/**
	 * Unit test of <code>EPPRegistry.sendDelete</code>.
	 */
	public void registryDelete(String tldName, boolean shouldSucceed) {
		printStart("registryDelete");

		EPPResponse response = null;
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("registryDelete: " + tldName);
			registry.resetRegistry();
			registry.setTransId("ABC-12345-XYZ");
			registry.addTld(tldName);
			registry.setAuthString("ClientX");

			response = registry.sendDelete();
			System.out.println("registryDelete: Response = [" + response
					+ "]\n\n");

			if (shouldSucceed) {
				assertTrue(response != null && response.isSuccess());
			} else {
				fail("Expecting error in response");
			}
		} catch (EPPCommandException e) {
			if (shouldSucceed) {
				handleException(e);
			}
		}

		printEnd("registryDelete");
	}

	/**
	 * Unit test of <code>EPPRegistry.sendUpdate</code>.
	 */
	public void registryUpdate(String tldName, boolean shouldSucceed) {
		printStart("registryUpdate");

		EPPResponse response = null;
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			System.out.println("registryUpdate: " + tldName);
			registry.resetRegistry();
			registry.setTransId("ABC-12345-XYZ");
			registry.addTld(tldName);
			registry.setAuthString("ClientX");

			response = registry.sendUpdate();
			System.out.println("registryUpdate: Response = [" + response
					+ "]\n\n");

			if (shouldSucceed) {
				assertTrue(response != null && response.isSuccess());
			} else {
				fail("Expecting error in response");
			}
		} catch (EPPCommandException e) {
			if (shouldSucceed) {
				handleException(e);
			}
		}

		printEnd("registryUpdate");
	}

	/**
	 * Unit test of <code>EPPRegistry.sendInfo</code>.
	 */
	public void registryInfo(String tldName) {
		printStart("registryInfo");

		boolean all = false;
		if (tldName == null || tldName.trim().length() == 0) {
			all = true;
		}

		EPPRegistryInfoResp response;
		try {
			System.out
					.println("\n----------------------------------------------------------------");
			if (all) {
				System.out.println("registryInfo: all tlds");
			} else {
				System.out.println("registryInfo: single tld");
			}

			registry.setTransId("ABC-12345-XYZ");
			if (all) {
				registry.setAllTlds(true);
			} else {
				System.out.println("\nregistryInfo: " + tldName);
				registry.addTld(tldName);
			}

			response = registry.sendInfo();

			System.out.println("registryInfo: Response = [" + response
					+ "]\n\n");

			if (all) {
				assertTrue(response.getZoneList() != null
						&& response.getZoneList().getZoneList() != null
						&& response.getZoneInfo() == null
						&& response.getZoneList().getZoneList().size() > 0);
				System.out.println("All TLDs: ");
				for (Iterator it = response.getZoneList().getZoneList()
						.iterator(); it.hasNext();) {
					EPPRegistryZone tld = (EPPRegistryZone) it.next();
					assertTrue(tld.getName() != null
							&& tld.getName().length() > 0);
					assertTrue(tld.getCreateDate() != null);
					System.out.print(tld.getName() + "\tcreated on "
							+ tld.getCreateDate());
					if (tld.getUpdateDate() != null) {
						System.out.println("\tupdated on "
								+ tld.getUpdateDate());
					} else {
						System.out.println();
					}
				}
			} else {
				assertTrue(response.getZoneList() == null
						&& response.getZoneInfo() != null);
				EPPRegistryZoneInfo info = response.getZoneInfo();
				System.out.println("Zone name: " + info.getName());
				if (info.hasRelated()) {
					System.out.println("Related zones: ");
					EPPRegistryRelated related = info.getRelated();
					if (related.hasFields()) {
						EPPRegistryFields fields = related.getFields();
						
						System.out.println("\ttype:" + fields.getType());
						for (Iterator it = fields.getFields().iterator(); it.hasNext();) {
							System.out.println("\tfield: " + (String) it.next());							
						}
					}
						System.out.println("\tMembers:");
					for (Iterator it = info.getRelated().getMembers().iterator(); it
							.hasNext();) {
						EPPRegistryZoneMember member = (EPPRegistryZoneMember) it
								.next();
						System.out.println("\t\tname: " + member.getZoneName()
								+ ", type: " + member.getType());
					}
				}
				if (info.getPhases() != null) {
					System.out.println("Phases:");
					for (Iterator it = info.getPhases().iterator(); it
							.hasNext();) {
						EPPRegistryPhase phase = (EPPRegistryPhase) it.next();
						System.out.println("\ttype: " + phase.getType()
								+ ", start: " + phase.getStartDate()
								+ ", end: " + phase.getEndDate());
					}
				}
				if (info.getServices() != null) {
					System.out.println("Services:");
					for (Iterator it = info.getServices().getObjURIs()
							.iterator(); it.hasNext();) {
						EPPRegistryObjURI objUri = (EPPRegistryObjURI) it
								.next();
						System.out.println("\tobjURI: " + objUri.getUri()
								+ ", required: " + objUri.getRequired());
					}
				}
				if (info.getServices() != null
						&& info.getServices().getExtension() != null) {
					System.out.println("Services extension:");
					for (Iterator it = info.getServices().getExtension()
							.getExtURIs().iterator(); it.hasNext();) {
						EPPRegistryExtURI extUri = (EPPRegistryExtURI) it
								.next();
						System.out.println("\textURI: " + extUri.getUri()
								+ ", required: " + extUri.getRequired());
					}
				}
				System.out.println("crId: " + info.getCreatedBy());
				System.out.println("crDate: " + info.getCreatedDate());
				System.out.println("upId: " + info.getLastUpdatedBy());
				System.out.println("upDate: " + info.getLastUpdatedDate());

				EPPRegistryDomain domain = info.getDomain();
				assertTrue(domain != null);
				System.out.println("Domain: " + domain);
				EPPRegistryHost host = info.getHost();
				assertTrue(host != null);
				EPPRegistryContact contact = info.getContact();
				assertTrue(contact != null);
			}

			assertTrue(response != null && response.isSuccess());

			printEnd("registryInfo");
		} catch (EPPCommandException e) {
			handleException(e);
		}
	}

	public String makeTldName() {
		int len = rd.nextInt(15);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append('a' + rd.nextInt(26));
		}
		return sb.toString();
	}

	/**
	 * Handle an <code>EPPCommandException</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by a
	 * server error, "Server Error :&lt;Response XML&gt;" will be specified. If
	 * the exception was caused by a general algorithm error, "General Error
	 * :&lt;Exception Description&gt;" will be specified.
	 * 
	 * @param aException
	 *            Exception thrown during test
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

	public static Test suite() {
		TestSuite suite = new TestSuite(EPPRegistryTst.class);

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
				} catch (Exception ex) {
					Assert.fail("Exception instantiating EPP.SessionClass value "
							+ theSessionClassName + ": " + ex);
				}
			} else {
				session = new EPPSession();
			}

			session.setClientID(Environment.getProperty("EPP.Test.clientId",
					"ClientX"));
			session.setPassword(Environment.getProperty("EPP.Test.password",
					"foo-BAR2"));
		}

		catch (Exception e) {
			e.printStackTrace();

			Assert.fail("Error initializing the session: " + e);
		}

		initSession();

		// System.out.println("out init");
		registry = new EPPRegistry(session);
	}

	/**
	 * JUNIT <code>tearDown</code>, which currently does nothing.
	 */
	protected void tearDown() {
		endSession();
	}

	/**
	 * Unit test of <code>EPPSession.initSession</code>. The session attribute
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
			} else {
				e.printStackTrace();

				Assert.fail("initSession Error : " + e);
			}
		}

		printEnd("initSession");
	}

	/**
	 * Unit test of <code>EPPSession.endSession</code>. The session with the EPP
	 * Server will be terminated.
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

	/**
	 * Print the end of a test with the <code>Thread</code> name if the current
	 * thread is a <code>TestThread</code>.
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
}
