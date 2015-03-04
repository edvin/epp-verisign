package com.verisign.epp.util;

import junit.framework.Assert;

import com.verisign.epp.interfaces.EPPCommandException;
import com.verisign.epp.interfaces.EPPSession;

/**
 * Test utility methods. This class contains a set of utility static methods 
 * useful for interface tests.
 */
public class TestUtil {
	
	/**
	 * Handle an <code>Exception</code>, which can be either a server
	 * generated error or a general exception. If the exception was caused by
	 * a server error, "Server Error :&lt;Response XML&gt;" will be specified.
	 * If the exception was caused by a general algorithm error, "General
	 * Error :&lt;Exception Description&gt;" will be specified.
	 *
	 * @param aSession Session being used
	 * @param aException <code>Exception</code> thrown during test
	 * 
	 * @throws InvalidateSessionException The session should be invalidated by calling method
	 */
	public static void handleException(EPPSession aSession, Exception aException) throws InvalidateSessionException {
		aException.printStackTrace();

		// Is a server specified error?
		if (aException instanceof EPPCommandException
				&& ((EPPCommandException) aException).hasResponse()) {
			EPPCommandException theCommandException = (EPPCommandException) aException;

			// Comment out Assert.fail call in non-test client
			Assert.fail("Server Error : " + theCommandException.getResponse());

			// Should the session be invalidated?
			if (theCommandException.getResponse().getResult()
					.shouldCloseSession()) {
				throw new InvalidateSessionException(
						"Server response indicates that the session should be closed",
						aException);
			}
		}
		else {
			// Comment out Assert.fail call in non-test client
			Assert.fail("General Error : " + aException);
			
			throw new InvalidateSessionException(
					"General exception that could be a session issue",
					aException);
		}
	} 

}
