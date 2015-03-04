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
package com.verisign.epp.serverstub;


// Log4j Imports
import org.apache.log4j.Logger;

// Java imports
import java.io.*;
import java.util.*;

import com.verisign.epp.codec.gen.EPPDcp;
import com.verisign.epp.codec.gen.EPPGreeting;
import com.verisign.epp.codec.gen.EPPPurpose;
import com.verisign.epp.codec.gen.EPPRecipient;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPResult;
import com.verisign.epp.codec.gen.EPPStatement;
import com.verisign.epp.codec.gen.EPPTransId;
import com.verisign.epp.framework.EPPAssemblerException;

// EPP imports
import com.verisign.epp.framework.EPPDispatcher;
import com.verisign.epp.framework.EPPEventException;
import com.verisign.epp.transport.ServerEventHandler;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;


/**
 * The <code>EPPClientConnectionHandler</code> class manages a single client
 * session. A connection is logically started when the handleConnection()
 * method is invoked by a listening server socket.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.10 $
 */
public class ClientConnectionHandler implements ServerEventHandler, Cloneable {
	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 ClientConnectionHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** The idle timeout. */
	private static final int TIMEOUT_MINUTES = 3;

	/**
	 * The state this connection is in.  If bRunning is true then the session
	 * should still be accepting commands
	 */
	private boolean bRunning;

	/** The number of commands that have been completed during the session */
	private int SessionCommandCount;

	/**
	 * The Session data for this session.  Session data is sent to the event
	 * handlers in the server
	 */
	private SessionData thisSession;

	/** Connection idle timeout */
	private GregorianCalendar idleTimeOutTime;

	/**
	 * Creates a new ClientConnectionHandler instance.
	 */
	public ClientConnectionHandler() {
		bRunning			    = true;
		SessionCommandCount     = 0;

		thisSession		    = new SessionData();
		idleTimeOutTime     = new GregorianCalendar();
		idleTimeOutTime.add(Calendar.MINUTE, TIMEOUT_MINUTES);
	}

	/**
	 * Makes a bitwise copy of this ClientConnectionHandler
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws CloneNotSupportedException DOCUMENT ME!
	 */
	public Object clone() throws CloneNotSupportedException {
		ClientConnectionHandler theCopy =
			(ClientConnectionHandler) super.clone();
		theCopy.thisSession		    = (SessionData) thisSession.clone();
		theCopy.idleTimeOutTime     = (GregorianCalendar) idleTimeOutTime.clone();

		return theCopy;
	}

	/**
	 * Invoked when a new cleint connects
	 *
	 * @param newInputStream The inputStream of the new connection
	 * @param newOutputStream The outputStream of the new connection
	 */
	public void handleConnection(
								 InputStream newInputStream,
								 OutputStream newOutputStream) {
		/**
		 * Implementing Pete's handler.  Here it's as if we're a new Thread
		 * with a connection spawned from a ServerSocket.accept() call.
		 */
		/**
		 * Adding some debug log information to log new connections
		 */
		cat.debug("Server accepted new connection. Thread id is: "
				  + Thread.currentThread().hashCode());

		/**
		 * The default constructor of the EPPGreeting takes care of the messy
		 * details of everything that a greeting contains. The only thing that
		 * must be set is the server name.
		 */
		EPPGreeting greeting = new EPPGreeting();

		int		    threadId = Thread.currentThread().hashCode();

		greeting.setServer(EPPEnv.getGreetingServerName());
		cat.debug("Server name = " + greeting.getServer());

		// Set DCP
		EPPDcp theDCP = new EPPDcp();
		theDCP.setAccess(EPPDcp.ACCESS_ALL);

		EPPStatement theStatement = new EPPStatement();

		EPPPurpose   thePurpose = new EPPPurpose();
		thePurpose.setAdmin(true);
		thePurpose.setProv(true);
		theStatement.setPurpose(thePurpose);

		EPPRecipient theRecipient = new EPPRecipient();
		theRecipient.addOurs(null);
		theRecipient.setPublic(true);
		theStatement.setRecipient(theRecipient);

		theStatement.setRetention(EPPStatement.RETENTION_STATED);

		theDCP.addStatement(theStatement);

		cat.debug("Adding DCP to Greeting: " + theDCP);

		greeting.setDcp(theDCP);

		thisSession.setGreeting(greeting);

		idleTimeOutTime = new GregorianCalendar();
		idleTimeOutTime.add(Calendar.MINUTE, TIMEOUT_MINUTES);

		EPPDispatcher theDispatcher = EPPDispatcher.getInstance();

		/**
		 * First, call processConnection to send the greeting.
		 */
		theDispatcher.processConnection(
										newInputStream, newOutputStream,
										thisSession);

		/**
		 * Now loop and process each arriving Message on the Stream.
		 */
		while (bRunning) {
			try {
				theDispatcher.processMessage(
											 newInputStream, newOutputStream,
											 thisSession);
				SessionCommandCount++;
				this.resetIdleTimeOut();

				/**
				 * If a logout has occurred we should not bRunning. Ha!
				 */
				bRunning = !thisSession.hasLogoutOccured();
			}
			 catch (EPPEventException e) {
				/**
				 * Something bad happened. Return an error response with as
				 * much info as possible
				 */
				cat.error("EPP Event Exception", e);

				sendErrorResponse(
								  EPPResult.COMMAND_FAILED,
								  "Internal Server Error, "
								  + "EPP Event Exception" + e.getMessage(),
								  newOutputStream, thisSession);
			}
			 catch (EPPAssemblerException ex) {
				/**
				 * Something went wrong the assembly of the EPP Message.
				 * Attempt to find out what it was and return the appropriate
				 * response if possible...
				 */
				if (ex.equals(EPPAssemblerException.FATAL)) {
					cat.fatal(
							  "Fatal EPPAssemblerException caught "
							  + "stopping client thread", ex);

					sendErrorResponse(
									  EPPResult.COMMAND_FAILED,
									  "Internal Server Error,"
									  + " EPPAssemblerException: "
									  + ex.getMessage(), newOutputStream,
									  thisSession);

					bRunning = false;
				}

				// Command Syntax Error? 2001
				else if (ex.equals(EPPAssemblerException.XML)) {
					cat.error("EPPAssemblerException.XML, sending "
							  + "COMMAND_SYNTAX_ERROR response to client");

					sendErrorResponse(
									  EPPResult.COMMAND_SYNTAX_ERROR,
									  "XML Schema Validation Error, "
									  + ex.getMessage(), newOutputStream,
									  thisSession);
				}

				// Command Syntax Error? 2001
				else if (ex.equals(EPPAssemblerException.MISSINGPARAMETER)) {
					cat.error("EPPAssemblerException.MISSINGPARAMETER,"
							  + " sending MISSINGPARAMETER response to client");

					sendErrorResponse(
									  EPPResult.MISSING_PARAMETER,
									  "Command processing error, , "
									  + ex.getMessage(), newOutputStream,
									  thisSession);
				}
				else if (ex.equals(EPPAssemblerException.COMMANDNOTFOUND)) {
					cat.error("EPPAssemblerException.COMMANDNOTFOUND,"
							  + " sending UNIMPLEMENTED_COMMAND response to client");

					sendErrorResponse(
									  EPPResult.UNIMPLEMENTED_COMMAND,
									  "Command not found, "
									  + ex.getMessage(), newOutputStream,
									  thisSession);
				}
				else if (ex.equals(EPPAssemblerException.EXTENSIONNOTFOUND)) {
					cat.error("EPPAssemblerException.EXTENSIONNOTFOUND,"
							  + " sending UNIMPLEMENTED_EXTENSION response to client");

					sendErrorResponse(
									  EPPResult.UNIMPLEMENTED_EXTENSION,
									  "Extension not found, "
									  + ex.getMessage(), newOutputStream,
									  thisSession);
				}
				// Client closed the connection?
				else if (ex.equals(EPPAssemblerException.CLOSECON)) {
					cat.error("EPPAssemblerException.CLOSECON caught, "
							  + "stopping thread");
					bRunning = false;
				}

				// Interrupted IO?
				else if (ex.equals(EPPAssemblerException.INTRUPTEDIO)) {
					cat.debug("EPPAssemblerException.INTRUPTEDIO caught, "
							  + "no command received");
				}

				// Who knows what happened?  Send the old Internal Server Error.
				else {
					cat.error("Unknown EPPAssemblerException type");

					sendErrorResponse(
									  EPPResult.COMMAND_FAILED,
									  "Internal Server Error, "
									  + "Unknown EPPAssemblerException"
									  + ex.getMessage(), newOutputStream,
									  thisSession);
				}
			}
		}

		/**
		 * Adding some debug log information to log closing connections
		 */
		cat.debug("Server closed connection. Thread id is: "
				  + Thread.currentThread().hashCode());
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param aCode DOCUMENT ME!
	 * @param aDescription DOCUMENT ME!
	 * @param out DOCUMENT ME!
	 * @param aSessionData DOCUMENT ME!
	 */
	private void sendErrorResponse(
								   int aCode, String aDescription,
								   OutputStream out, SessionData aSessionData) {
		cat.debug("<<<<<<<<<<<<<<<<<<<<<<  Enter sendErrorResponse()"
				  + ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		cat.debug("Sending error response to client, code = " + aCode
				  + ", description = " + aDescription);

		// Create the result
		EPPResult theResult = new EPPResult(aCode);
		theResult.addExtValueReason(aDescription);

		EPPTransId theTransId = new EPPTransId("svrError1");

		// Create the response
		EPPResponse response = new EPPResponse(theTransId, theResult);

		// Send the response
		try {
			EPPDispatcher.getInstance().send(response, out);
			out.flush();
		}
		 catch (Exception e) {
			cat.debug(".sendErrorResponse() "
					  + "Error sending error response to client: ");
		}

		cat.debug(".sendErrorResponse() " + "Return");
	}

	/**
	 * Makes the current session stop receiving commands
	 */
	public void close() {
		bRunning = false;
	}

	/**
	 * Resets the idle Timeout
	 */
	protected void resetIdleTimeOut() {
		idleTimeOutTime.setTime(new Date());
		idleTimeOutTime.add(Calendar.MINUTE, TIMEOUT_MINUTES);
	}
}
