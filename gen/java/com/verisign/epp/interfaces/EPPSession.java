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


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPCommand;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPGreeting;
import com.verisign.epp.codec.gen.EPPHello;
import com.verisign.epp.codec.gen.EPPLoginCmd;
import com.verisign.epp.codec.gen.EPPLogoutCmd;
import com.verisign.epp.codec.gen.EPPPollCmd;
import com.verisign.epp.codec.gen.EPPResponse;
import com.verisign.epp.codec.gen.EPPService;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.transport.EPPClientCon;
import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.EPPConFactorySingle;
import com.verisign.epp.transport.client.EPPSSLContext;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.EPPXMLStream;


/**
 * <code>EPPSession</code> manages a session with an EPP Server.  An
 * initialized instance of <code>EPPSession</code> is required for using any
 * of the EPP interface classes (i.e. <code>EPPDomain</code>).
 * <code>EPPSession</code> can invoke the following EPP operations:<br>
 * 
 * <ul>
 * <li>
 * Login  - Login to an EPP Server
 * </li>
 * <li>
 * Logout - Logout from an EPP Server
 * </li>
 * <li>
 * Hello  - Request Greeting from the EPP Server
 * </li>
 * <li>
 * Poll   - Discover and retrieve client service messages
 * </li>
 * </ul>
 * 
 *
 * @author $Author: jim $
 * @version $Revision: 1.7 $
 *
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public class EPPSession {
	/**
	 * Poll operation type indicating that the client is requesting information
	 * from the server.
	 */
	public final static String OP_REQ = EPPPollCmd.OP_REQ;

	/**
	 * Poll operation type indicating that the client has received a message
	 * and that the server can remove the message.
	 */
	public final static String OP_ACK = EPPPollCmd.OP_ACK;

	/** Log4j category for logging */
	private static final Logger cat =
		Logger.getLogger(
						 EPPSession.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Synchronous mode constant in processing commands/responses, where 
	 * for each command sent the <code>EPPSession</code> will immediately 
	 * read for the response.  This is the default mode, but can be 
	 * overridden with the {@link #setMode(int)} method. 
	 */
	public static final int MODE_SYNC = 0;

	/**
	 * Asynchronous mode contant in processing commands/responses, where 
	 * for the {@link #processDocument(EPPCommand)} will send the command 
	 * and will not wait for the response.  The client must call {@link #readResponse()} 
	 * to read the response.  The mode can be set with the {@link #setMode(int)} method.  
	 */
	public static final int MODE_ASYNC = 1;
	
	
	/** EPP Codec used to encode and decode EPP messages */
	protected EPPCodec myCodec = EPPCodec.getInstance();

	/** Client transaction identifier */
	protected String myTransId = null;

	/** Override EPP version */
	protected String myVersion = null;

	/** Override EPP language */
	protected String myLanguage = null;

	/** Client identifier to use in login. */
	protected String myClientID = null;

	/** Client password */
	protected String myPassword = null;

	/** New password to use */
	protected String myNewPassword = null;

	/** EPP response associated with last EPP command */
	protected EPPResponse myResponse = null;

	/** EPP services */
	protected Vector myServices = null;

	/** EPP extension services */
	protected Vector myExtensionServices = null;

	/** Session connection */
	protected EPPClientCon myConnection = null;

	/** Session input stream */
	protected InputStream myInputStream = null;

	/** Session output stream */
	protected OutputStream myOutputStream = null;

	/** Used to read and write XML packets from/to streams. */
	protected EPPXMLStream myXMLStream = null;

	/** Poll Operation Command */
	protected String myPollOp = null;

	/**
	 * Message identifier associated with a <code>EPPPollCmd.OP_ACK</code> poll
	 * command.
	 */
	protected String msgID = null;

	/** Status transaction identifier */
	private String statusTransId = null;

	/** Status command type */
	private String statusCommandType = null;
	
	/**
	 * What mode should the EPPSession process command/responses.  The 
	 * mode will default to <code>MODE_SYNC</code> which will send a command 
	 * an immediately wait for the response.  
	 */
	private int mode = MODE_SYNC;  

	/**
	 * Construct and initialize a new instance of EPPSession using the 
	 * host name and port number defined in <code>EPPEnv</code>.
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	public EPPSession() throws EPPCommandException {
		init();
	}

	
	/**
	 * Construct and initialize a new instance of EPPSession with a specific 
	 * host and port.  
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	public EPPSession(String aHostName, int aPortNumber) throws EPPCommandException {
		init(aHostName, aPortNumber, null);
	}
	
	/**
	 * Construct and initialize a new instance of EPPSession with a specific 
	 * server host, server port, client host name, and client port.  
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHostName Host name or IP address to connect from
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	public EPPSession(String aHostName, int aPortNumber, String aClientHostName) throws EPPCommandException {
		init(aHostName, aPortNumber, aClientHostName, null);
	}
	
	/**
	 * Construct and initialize a new instance of EPPSession with a specific 
	 * host and port.  
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aSSLContext Optional specific SSL context to use
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	public EPPSession(String aHostName, int aPortNumber, EPPSSLContext aSSLContext) throws EPPCommandException {
		init(aHostName, aPortNumber, aSSLContext);
	}
	
	/**
	 * Construct and initialize a new instance of EPPSession with a specific 
	 * server host, server port, client host name, and client port.  
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHostName Host name or IP address to connect from
	 * @param aSSLContext Optional specific SSL context to use
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	public EPPSession(String aHostName, int aPortNumber, String aClientHostName, EPPSSLContext aSSLContext) throws EPPCommandException {
		init(aHostName, aPortNumber, aClientHostName, aSSLContext);
	}
	
	/**
	 * Initializes an <code>EPPSession</code> using the default <code>EPPEnv</code> 
	 * properties, which will do the following:<br>
	 * <br>
	 * <ol>
	 * <li>Gets concrete connection factory
	 * <li>Makes connection to EPP Server
	 * <li>Initialize the streams for XML processing
	 * </ol>
	 * <br>
	 * <br><code>EPPSession.initSession</code> needs to be called to fully
	 * initialize a session with the EPP Server.
	 *
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	protected void init() throws EPPCommandException {
		cat.debug("init(): enter");
		this.myConnection = this.getConnection();
		
		// Initialize the connection with the EPP Server
		try {
			this.myConnection.initialize();
		}
		 catch (EPPConException ex) {
			cat.error("init(): Unable to initialize connection to the EPP Server: "
					  + ex);
			throw new EPPCommandException("EPPSession() Unable to initialize connection to the EPP Server: "
										  + ex);
		}
		 
		// Initialize the input/output and XML streams
		this.initStreams();
		cat.debug("init(): exit");
	}
	
	/**
	 * Initializes an <code>EPPSession</code> to connect to a specific host and port, 
	 * which will do the following:<br>
	 * <br>
	 * <ol>
	 * <li>Gets concrete connection factory
	 * <li>Makes connection to EPP Server
	 * <li>Initialize the streams for XML processing
	 * </ol>
	 * <br>
	 * <br><code>EPPSession.initSession</code> needs to be called to fully
	 * initialize a session with the EPP Server.
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aSSLContext Optional specific SSL context to use
	 * 
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	protected void init(String aHostName, int aPortNumber, EPPSSLContext aSSLContext) throws EPPCommandException {
		cat.debug("init(String, int, EPPSSLContext): enter");
		this.myConnection = this.getConnection();
		
		// Initialize the connection with the EPP Server
		try {
			this.myConnection.initialize(aHostName, aPortNumber, aSSLContext);
		}
		 catch (EPPConException ex) {
			cat.error("init(String, int, EPPSSLContext): Unable to initialize connection to the EPP Server (host = " + 
					aHostName + ", port = " + aPortNumber + ": "
					  + ex);
			throw new EPPCommandException("EPPSession() Unable to initialize connection to the EPP Server (host = " + 
					aHostName + ", port = " + aPortNumber + ": "
										  + ex);
		}
		 
		// Initialize the input/output and XML streams
		this.initStreams();
		cat.debug("init(String, int, EPPSSLContext): exit");
	}
	
	
	/**
	 * Initializes an <code>EPPSession</code> to connect to a specific server host, server port from 
	 * a specific client host name/IP address.
	 * which will do the following:<br>
	 * <br>
	 * <ol>
	 * <li>Gets concrete connection factory
	 * <li>Makes connection to EPP Server
	 * <li>Initialize the streams for XML processing
	 * </ol>
	 * <br>
	 * <br><code>EPPSession.initSession</code> needs to be called to fully
	 * initialize a session with the EPP Server.
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHostName Host name or IP address to connect from
	 * @param aSSLContext Optional specific SSL context to use
	 * 
	 * @throws EPPCommandException Thrown if there's an error initializing the
	 * 		   <code>EPPSession</code>
	 */
	protected void init(String aHostName, int aPortNumber, String aClientHostName, EPPSSLContext aSSLContext) throws EPPCommandException {
		cat.debug("init(int, int, String, EPPSSLContext): enter");
		this.myConnection = this.getConnection();
		
		// Initialize the connection with the EPP Server
		try {
			this.myConnection.initialize(aHostName, aPortNumber, aClientHostName, aSSLContext);
		}
		 catch (EPPConException ex) {
			cat.error("init(int, int, String, EPPSSLContext): Unable to initialize connection to the EPP Server (host = " + 
					aHostName + ", port = " + aPortNumber + " from client host " + aClientHostName  + ": "
					  + ex);
			throw new EPPCommandException("EPPSession() Unable to initialize connection to the EPP Server (host = " + 
					aHostName + ", port = " + aPortNumber + " from client host " + aClientHostName + 
					": " + ex);
		}
		 
		// Initialize the input/output and XML streams
		this.initStreams();
		cat.debug("init(int, int, String, EPPSSLContext): exit");
	}
	
	
	/**
	 * Gets the client connection.  The connection needs to be initialized 
	 * by calling the <code>initialize</code> methods of the returned connection.
	 * 
	 * @return Client connection
	 * @throws EPPCommandException Error creating the connection
	 */
	protected EPPClientCon getConnection() throws EPPCommandException {
		cat.debug("getConnection(): enter");
		
		EPPClientCon theConnection = null;
		
		// Gets connection instance from connection factory
		try {
			theConnection = EPPConFactorySingle.getInstance().getEPPConnection();
		}

		 catch (EPPConException ex) {
			cat.error("getConnection(): Unable to get connection instance from factory: "
					  + ex);
			throw new EPPCommandException("EPPSession() Unable to get connection instance from factory: "
										  + ex);
		}
		 
		cat.debug("getConnection(): exit");
		return theConnection;
	}
	
	
	/**
	 * Initializes the input stream, output stream, and XML stream from the 
	 * previously initialized client connection (<code>myConnection</code>).
	 * 
	 * @throws EPPCommandException Error initializing streams
	 */
	protected void initStreams() throws EPPCommandException {
		
		// Get streams from connection
		try {
			/**
			 * JG 2/16/05 - 
			 * Ensured that the output stream is buffered so the EPP header and packet 
			 * are sent in a single packet.  20480 bytes was chosen to ensure that all 
			 * EPP packets can be held in the buffered output stream.  
			 */ 
			myOutputStream     = new BufferedOutputStream(myConnection.getOutputStream(), 20480);
			myInputStream	   = myConnection.getInputStream();
		}
		 catch (EPPConException ex) {
			cat.error("EPPSession() Unable to get streams from connection: "
					  + ex);
			throw new EPPCommandException("EPPSession() Unable to get streams from connection: "
										  + ex);
		}

		// Initialize the XML stream
		myXMLStream = new EPPXMLStream(EPPSchemaCachingParser.POOL);
	}

	
	
	
	/**
	 * This methods does a session login.  The following steps are
	 * followed:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * receives a greeting and processes it.
	 * </li>
	 * <li>
	 * sends login
	 * </li>
	 * <li>
	 * processed login response
	 * </li>
	 * </ul>
	 * 
	 *
	 * @exception EPPCommandException Error with login
	 */
	protected void login() throws EPPCommandException {
		cat.debug("login(): enter");
		Document    myDoc	   = null;
		EPPGreeting myGreeting = null;
		EPPLoginCmd myCommand  = null;

		/**
		 * Read the incomming Greeting.
		 */
		myDoc = recDocument();

		/**
		 * Encode the generated Document
		 */
		try {
			myGreeting = myCodec.decodeGreeting(myDoc);
			cat.debug("the greeting received is [" + myGreeting + "]");
		}
		 catch (EPPDecodeException myException) {
			cat.error("login(): Exception decoding greeting: " + myException);
			throw new EPPCommandException("EPPSession.Login() decode [EppDecodeException]: "
										  + myException.getMessage());
		}
		 catch (NullPointerException myException) {
			cat.error("login(): Exception decoding greeting: " + myException);
			throw new EPPCommandException("EPPSession.Login () decode [Null Pointer Exception] : "
										  + myException.getMessage());
		}

		/**
		 * Need to Create a Command Object and set Attributes.
		 */
		if (myNewPassword == null) {
			myCommand = new EPPLoginCmd(myTransId, myClientID, myPassword);
		}
		else {
			myCommand =
				new EPPLoginCmd(
								myTransId, myClientID, myPassword, myNewPassword);
		}

		if (myVersion != null) {
			myCommand.setVersion(myVersion);
		}

		if (myLanguage != null) {
			myCommand.setLang(myLanguage);
		}
		
		// Merge greeting services and extension services with the default 
		// services configured in the EPP SDK.
		myCommand.mergeServicesAndExtensionServices(myGreeting);
		
		
		// Set the client specified extensions (setExtensions) 
		if (this.myExtensionServices != null) {
			myCommand.setExtensions(this.myExtensionServices);
		}

		// Set the client specified services (setServices)
		if (myServices != null) {
			myCommand.setServices(myServices);
		}

		// Greeting and Login services are not compatible?
		if (cat.isDebugEnabled() && !myCommand.isValidServices(myGreeting)) {
			cat.debug("Login services does not match the greeting services, greeting = ["
										  + myGreeting + "], login = ["
										  + myCommand + "]");
		}

		/**
		 * now we need to send the message (doc --> CommandObject)
		 */
		try {
			myDoc = myCodec.encode(myCommand);
		}
		 catch (EPPEncodeException myException) {
			cat.error("login(): Exception encoding login command: " + myException);
			throw new EPPCommandException("EPPSession.login() decode : "
										  + myException.getMessage());
		}

		/**
		 * Write the Login into the Stream.
		 */
		sendDocument(myDoc);

		/**
		 * Read the Response
		 */
		myDoc = recDocument();

		/**
		 * Process the Incomming Document
		 */
		try {
			myResponse = myCodec.decodeResponse(myDoc);
			cat.debug("the response is [" + myResponse + "]");
		}
		 catch (EPPDecodeException myException) {
			cat.error("login(): Exception decoding login response: " + myException);
			throw new EPPCommandException("EPPSession.initialize() decode : "
										  + myException.getMessage());
		}

		/**
		 * Server specified error?
		 */
		if (!myResponse.isSuccess()) {
			cat.error("login(): Login failure response: " + myResponse);
			throw new EPPCommandException(
										  "EPPSession.login() : Error in response from Server ",
										  myResponse);
		}

		/**
		 * Make Sure Trans Id Mathes
		 */
		validateClientTransId(myCommand, myResponse);

		// Reset the transaction id
		myTransId = null;
		
		cat.debug("login(): exit");
	}

	/**
	 * Sends a Hello Command to the EPP Server.  The EPP Greeting sent from the
	 * EPP Server will be returned.   <br><br>
	 * 
	 * <ul>
	 * <li>
	 * <b>pre-condition</b> -  A connection has been established with the EPP
	 * Server
	 * </li>
	 * <li>
	 * <b>post-condition</b> -  Error or the EPP Greeting sent by the EPP
	 * Server
	 * </li>
	 * <li>
	 * <b>error condition</b> -  connection time out/ bad connection.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @return Server greeting
	 *
	 * @exception EPPCommandException Unexpected error information
	 */
	public EPPGreeting hello() throws EPPCommandException {
		cat.debug("hello(): enter");
		
		Document    myDoc	   = null;
		EPPHello    myHello    = null;
		EPPGreeting myGreeting = null;

		/**
		 * Need to Create a Command Object and set Attributes.
		 */
		myHello = new EPPHello();

		/**
		 * now we need to send the message (doc --> CommandObject)
		 */
		try {
			myDoc = myCodec.encode(myHello);
		}
		 catch (EPPEncodeException myException) {
			throw new EPPCommandException("EPPSession.Hello() decode : "
										  + myException.getMessage());
		}

		/**
		 * Write the Hello Command into the Stream.
		 */
		sendDocument(myDoc);

		/**
		 * Read the Response
		 */
		myDoc = recDocument();

		/**
		 * Encode the generated Document
		 */
		try {
			myGreeting = myCodec.decodeGreeting(myDoc);
			cat.debug("hello greeting response = [" + myGreeting + "]");
		}
		 catch (EPPDecodeException myException) {
			cat.error("hello(): Exception decoding greeting: " + myException);
			throw new EPPCommandException("EPPSession.Hello() decode [EppDecodeException]: "
										  + myException.getMessage());
		}
		 catch (NullPointerException myException) {
			cat.error("hello(): Exception decoding greeting: " + myException);
			throw new EPPCommandException("EPPSession.Hello() decode [Null Pointer Exception] : "
										  + myException.getMessage());
		}

		cat.debug("hello(): exit");
		return myGreeting;
	}

	/**
	 * This method creates an instance of EPPPollCmd and sets the given
	 * attributes and invokes the send method associated with the command.
	 *
	 * @return the response from the Command
	 *
	 * @exception EPPCommandException
	 */
	public EPPResponse sendPoll() throws EPPCommandException {
		cat.debug("sendPoll(): enter");
		EPPPollCmd myCommand = new EPPPollCmd(myTransId, myPollOp);

		if (myPollOp.equals(EPPPollCmd.OP_ACK)) {
			myCommand.setMsgID(msgID);
		}

		cat.debug("sendPoll(): exit");
		return processDocument(myCommand, EPPResponse.class);
	}

	/**
	 * logout from the session.
	 *
	 * @exception EPPCommandException
	 */
	protected void logout() throws EPPCommandException {
		cat.debug("logout(): enter");
		
		Document     myDoc     = null;
		EPPLogoutCmd myCommand = null;

		/**
		 * Need to Create a CommandObejct and set Attributes.
		 */
		myCommand = new EPPLogoutCmd(myTransId);

		/**
		 * now we need to encode the message (doc --> CommandObject)
		 */
		try {
			myDoc = myCodec.encode(myCommand);
		}
		 catch (EPPEncodeException myException) {
			cat.error("logout(): Exception encoding command: " + myException);
			throw new EPPCommandException("EPPSession.logout() decode : "
										  + myException.getMessage());
		}

		/**
		 * Write the Login into the Stream.
		 */
		try {
			myXMLStream.write(myDoc, myOutputStream);
		}
		 catch (Exception myException) {
			cat.error("logout(): Exception writing to stream: " + myException);
			throw new EPPCommandException("EPPSession.logout() : "
										  + myException.getMessage());
		}

		/**
		 * Read the incoming Response
		 */
		try {
			myDoc = myXMLStream.read(myInputStream);
		}
		 catch (Exception myException) {
			cat.error("logout(): Exception reading from stream: " + myException);
			throw new EPPCommandException("EPPCommand.logout() : "
										  + myException.getMessage());
		}

		/**
		 * Parse the incomming Response
		 */
		try {
			myResponse = myCodec.decodeResponse(myDoc);
		}
		 catch (EPPDecodeException myException) {
			cat.error("logout(): Exception decoding response: " + myException);
			throw new EPPCommandException("EPPSession.logout() decode : "
										  + myException.getMessage());
		}

		/**
		 * Server specified error?
		 */
		if (!myResponse.isSuccess()) {
			cat.error("logout(): Logout failed due to server error: " + myResponse);
			throw new EPPCommandException(
										  "EPPSession.logout() : Error in response from Server ",
										  myResponse);
		}

		/**
		 * Make Sure Trans Id Mathes
		 */
		validateClientTransId(myCommand, myResponse);
		
		cat.debug("logout(): exit");
	}

	/**
	 * Validates that the response client transaction identifier matches the
	 * command's client transaction identifer. Client transaction identifiers
	 * are optional, but if specified, need to be mirrored back in the
	 * response.
	 *
	 * @param myCommand Command sent
	 * @param myResponse Response received
	 *
	 * @exception EPPCommandException transaction ids don't match
	 */
	public void validateClientTransId(
										 EPPCommand myCommand,
										 EPPResponse myResponse)
								  throws EPPCommandException {
		/** Make Sure Trans Id Matches */
		String theRespTransId = myResponse.getTransId().getClientTransId();
		String theCmdTransId = myCommand.getTransId();

		// If the client didn't send a transId, we don't validate what the server sent
		if (theCmdTransId == null) {
			// Allowed
		} else if (theRespTransId == null) {
			throw new EPPCommandException(
										  "null check, Response trans id of ["
										  + theRespTransId
										  + "] != Command trans id of ["
										  + theCmdTransId + "]", myResponse);
		}

		else if (theRespTransId.compareTo(theCmdTransId) != 0) {
			throw new EPPCommandException(
										  "Response trans id of ["
										  + theRespTransId
										  + "] != Command trans id of ["
										  + theCmdTransId + "]", myResponse);
		}
	}

	/**
	 * Gets an validated EPP DOM Document from the session input stream.
	 *
	 * @return DOM <code>Document</code> instance read from stream
	 *
	 * @exception EPPCommandException Error reading document from stream
	 */
	Document recDocument() throws EPPCommandException {
		Document myDoc;

		/**
		 * Read the incomming Response
		 */
		try {
			myDoc = myXMLStream.read(myInputStream);
		}
		 catch (EPPException myException) {
			cat.error("recDocument(): Exception reading from stream: " + myException);
			throw new EPPCommandException("EPPCommand.recDocument : "
										  + myException.getMessage());
		}
		 catch (IOException myException) {
			cat.error("recDocument(): Exception reading from stream: " + myException);
			throw new EPPCommandException("EPPCommand.recDocument : "
										  + myException.getMessage());
		}

		return myDoc;
	}

	/**
	 * Sends an EPP DOM Document to the session output stream.
	 *
	 * @param newDoc DOM <code>Document</code> instance to write to stream
	 *
	 * @exception EPPCommandException Error writing document to stream
	 */
	void sendDocument(Document newDoc) throws EPPCommandException {
		/**
		 * Write the Login into the Stream.
		 */
		try {
			myXMLStream.write(newDoc, myOutputStream);
		}
		 catch (EPPException myException) {
			cat.error("sendDocument(): Exception writing to stream: " + myException);
			throw new EPPCommandException("EPPSession.sendDocument() : "
										  + myException.getMessage());
		}
	}
	
	
	/**
	 * Process an <code>EPPCommand</code> instance by writing the command to
	 * the session output stream and reading an <code>EPPResponse</code>
	 * instance from the sessin input stream.
	 *
	 * @param aCommand Command to write to output stream
	 *
	 * @return Response associated with passed in command
	 *
	 * @exception EPPCommandException error processing the command.  This can
	 * 			  include an error specified from the server or encountered
	 * 			  while attempting to process the command.  If the exception
	 * 			  contains an <code>EPPResponse</code> than it was a server
	 * 			  specified error.
	 */
	public EPPResponse processDocument(EPPCommand aCommand)
								throws EPPCommandException {	
		return processDocument(aCommand, null);
	}

	
	/**
	 * Reads a response from the server.  This method does no post-processing 
	 * of the response, but simply reads the response from the connection, decodes it 
	 * and returns the concrete <code>EPPResponse</code>.  The {@link #myResponse} attribute 
	 * is set as a side-effect of calling <code>readResponse</code> so that 
	 * {@link #getResponse()} can be called later.  
	 * 
	 * @return Response from server
	 * 
	 * @throws EPPCommandException Error reading or decoding the server response
	 */
	public EPPResponse readResponse() throws EPPCommandException {
		// Reset response to null 
		this.myResponse = null;
		
		Document    theDoc	   = null;
		
		// Read response from server and parse the XML to DOM Document
		theDoc = recDocument();

		// Decode DOM Document to <code>EPPResponse</code> instance
		try {
			myResponse = myCodec.decodeResponse(theDoc);
		}
		 catch (EPPDecodeException myException) {
			throw new EPPCommandException("EPPSession.readResponse: On Response "
										  + myException.getMessage());
		}
		 
		 return this.myResponse;
	}
	
	
	/**
	 * Process an <code>EPPCommand</code> instance by writing the command to
	 * the session output stream and reading an <code>EPPResponse</code>
	 * instance from the sessin input stream and validate that the  
	 * <code>EPPResponse</code> is of the specified type.
	 *
	 * @param aCommand Command to write to output stream
	 * @param aExpectedResponse Expected type of <code>EPPResponse</code>.  If 
	 * <code>aExpectedResponse</code> is non-<code>null</code> and the response 
	 * is not of the specified type, than an <code>EPPCommandException</code> will 
	 * be thrown.  
	 *
	 * @return Response associated with passed in command if mode is {@link #MODE_SYNC};
	 * <code>null</code> otherwise.
	 *
	 * @exception EPPCommandException error processing the command.  This can
	 * 			  include an error specified from the server or encountered
	 * 			  while attempting to process the command.  If the exception
	 * 			  contains an <code>EPPResponse</code> than it was a server
	 * 			  specified error.
	 */
	public EPPResponse processDocument(EPPCommand aCommand, Class aExpectedResponse)
								throws EPPCommandException {
		Document    theDoc;

		// Encode aCommand to DOM Document (theDoc)
		try {
			theDoc = myCodec.encode(aCommand);
		}
		 catch (Exception myException) {
			throw new EPPCommandException("EPPSession.processDocument: On Command "
										  + myException.getMessage());
		}

		// Send command to server
		this.sendDocument(theDoc);
		
		// Asynchronous mode? 
		if (this.mode == MODE_ASYNC) {
			// Immediately return <code>null</code> without reading response.
			return null;
		}
		
		EPPResponse theResponse = this.readResponse();
		theResponse.setDocument(theDoc);

		if (!theResponse.isSuccess()) {
			throw new EPPCommandException(
										  "EPPSession.processDocument() : Error in response from Server",
										  myResponse);
		}
		
		 // Specific response expected and response does not match expected type?
		if ((aExpectedResponse != null) && !aExpectedResponse.isInstance(theResponse)) {
			throw new EPPCommandException("Unexpected response type of "
					  + theResponse.getClass().getName()
					  + ", expecting "
					  + aExpectedResponse);			
		}

		// Client transaction's match?
		validateClientTransId(aCommand, theResponse);

		return theResponse;
	}
	

	/**
	 * Ends a session by logging out from the server and closing the connection
	 * with the server.
	 *
	 * @exception EPPCommandException Error ending session
	 */
	public void endSession() throws EPPCommandException {
		cat.debug("endSession(): enter");
		try {
			logout();
		}
		finally {
			// Ensure that the physical connection is closed
			try {
				endConnection();
			}
			catch (Exception ex) {
				// Ignore
			}
		}
		cat.debug("endSession(): exit");
	}

	/**
	 * Closes the connection with the server.
	 *
	 * @exception EPPCommandException Error closing connection.
	 */
	public void endConnection() throws EPPCommandException {
		cat.debug("endConnection(): enter");

		try {
			if (myInputStream != null) {
				myInputStream.close();
				myInputStream = null;
			}
		}
		catch (Exception ex) {
			// Ingore
		}
		
		try {
			if (myOutputStream != null) {
				myOutputStream.close();
				myOutputStream = null;
			}
		}
		catch (Exception ex) {
			// Ingore
		}
		
		
		try {
			if (myConnection != null) {
				myConnection.close();
				myConnection = null;
			}
		}
		 catch (Exception ex) {
			cat.error("endConnection(): Exception closing connection: " + ex);
			throw new EPPCommandException("EPPApplication.CloseConnection() : "
										  + ex.getMessage());
		}
		 
		cat.debug("endConnection(): exit");
	}

	/**
	 * Initialize an authenticated session with the EPP Server.<br>
	 * <br>
	 * The required attributes have been set with the following
	 * methods:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setClientID</code> - Sets the client idenfifier/user name
	 * </li>
	 * <li>
	 * <code>setPassword</code> - Sets the password
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 * The optional attributes have been set with the following:<br><br>
	 * 
	 * <ul>
	 * <li>
	 * <code>setNewPassword</code> - Changes the password
	 * </li>
	 * <li>
	 * <code>setVersion</code> - Override the default EPP version. A default
	 * setting is provided (i.e. 1.0).
	 * </li>
	 * <li>
	 * <code>setLang</code> - Override the default language of "us".
	 * </li>
	 * <li>
	 * <code>setServices</code> - Sets desired set of client EPP services by
	 * EPP XML Namespace.  A default setting is automatically provided based
	 * on the <code>EPP.MapFactories</code> configuration setting
	 * </li>
	 * <li>
	 * <code>setExtensions</code> - Sets desired set of client EPP services.
	 * This can be a subset of the EPP services loaded in the client.  A
	 * default setting is automatically provided based on the
	 * <code>EPP.ProtocolExtension</code> and the
	 * <code>EPP.CmdRspExtensions</code> configuration settings.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @exception EPPCommandException Error initializing the session.
	 */
	public void initSession() throws EPPCommandException {
		cat.debug("initSession(): enter");
		// Validate attributes
		if (myClientID == null) {
			throw new EPPCommandException("EPPSession.initSession Login requires ClientID");
		}

		if (myPassword == null) {
			throw new EPPCommandException("EPPSession.initSession Login requires Password");
		}

		login();
		cat.debug("initSession(): exit");
	}

	/**
	 * Gets the EPP version used in <code>initSession</code>.
	 *
	 * @return EPP version to use
	 */
	public String getVersion() {
		return myVersion;
	}

	/**
	 * Overrides the default EPP version used in <code>initSession</code>.
	 *
	 * @param newVersion EPP version to use
	 */
	public void setVersion(String newVersion) {
		myVersion = newVersion;
	}

	/**
	 * Gets the language of "us" used by <code>initSession</code>.
	 *
	 * @return Language following RFC3066 format
	 */
	public String setLang() {
		return myLanguage;
	}

	/**
	 * Overrides the default language of "us" used by <code>initSession</code>.
	 *
	 * @param newLanguage Language following RFC3066 format
	 */
	public void setLang(String newLanguage) {
		myLanguage = newLanguage;
	}

	/**
	 * Gets an optional client transaction identifier used when sending an EPP
	 * command (i.e. EPP &ltlogin&gt or EPP &ltlogout&gt).
	 *
	 * @return Client transaction identifer if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getTransId() {
		return myTransId;
	}

	/**
	 * Sets an optional client transaction identifier used when sending an EPP
	 * command (i.e. EPP &ltlogin&gt or EPP &ltlogout&gt). It is recommended
	 * to use a unique transaction identifier per command.
	 *
	 * @param newTransId Client transaction identifer
	 */
	public void setTransId(String newTransId) {
		myTransId = newTransId;
	}

	/**
	 * Gets response associated with the last command. For example, this could
	 * be the response associated with the EPP &ltlogin&gt command sent in
	 * <code>initSession</code>.
	 *
	 * @return response if defined; <code>null</code> otherwise.
	 */
	public EPPResponse getResponse() {
		return myResponse;
	}

	/**
	 * Gets the session input stream.
	 *
	 * @return Session input stream if defined; <code>null</code> otherwise.
	 */
	public InputStream getInputStream() {
		return myInputStream;
	}

	/**
	 * Sets the input stream for the session.  A default input stream is
	 * created in the <code>EPPSession</code> constructor.  It is recommended
	 * to use the default input stream.
	 *
	 * @param newInput Input stream to use in session.
	 */
	public void setInputStream(InputStream newInput) {
		myInputStream = newInput;
	}

	/**
	 * Gets the session output stream.
	 *
	 * @return Session output stream
	 */
	public OutputStream getOutputStream() {
		return myOutputStream;
	}
	
	/**
	 * Gets the client connection used for the <code>EPPSession</code>.
	 * 
	 * @return Client connection if defined;<code>null</code> otherwise
	 */
	public EPPClientCon getClientCon() {
		return this.myConnection;
	}

	/**
	 * Sets the output stream for the session.  A default output stream is
	 * created in the <code>EPPSession</code> constructor.  It is recommended
	 * to use the default output stream.
	 *
	 * @param newOutput Output stream to use in session.
	 */
	public void setOutputStream(OutputStream newOutput) {
		myOutputStream = newOutput;
	}

	/**
	 * Gets the client identifier/name used in <code>initSession</code>.
	 *
	 * @return Client identifier
	 */
	public String getClientID() {
		return myClientID;
	}

	/**
	 * Sets the client identifier/name used in <code>initSession</code>.
	 *
	 * @param newClientID Client identifier
	 */
	public void setClientID(String newClientID) {
		myClientID = newClientID;
	}

	/**
	 * Gets the client password used in <code>initSession</code>.
	 *
	 * @return Client password
	 */
	public String getPassword() {
		return myPassword;
	}

	/**
	 * Sets the client password used in <code>initSession</code>.
	 *
	 * @param newPassword Client password
	 */
	public void setPassword(String newPassword) {
		myPassword = newPassword;
	}

	/**
	 * Gets the new client password used in <code>initSession</code>.
	 *
	 * @return New client password
	 */
	public String getnewPassword() {
		return myNewPassword;
	}

	/**
	 * Sets the new client password used in <code>initSession</code>.
	 *
	 * @param newPassword New client password
	 */
	public void setNewPassword(String newPassword) {
		myNewPassword = newPassword;
	}

	/**
	 * Set the services to use with this session by EPP XML namespace URIs.
	 * This must be called before calling <code>initSession</code> or
	 * <code>login</code>. The default setting is to use all of the services
	 * loaded in <code>EPPFactory</code>.  This method allows a client to
	 * specify a subset of the services loaded in the <code>EPPFactory</code>,
	 * which is useful for synchronizing with the services supported by a
	 * specific server. For example, to initialize a session that will manage
	 * domains, <code>newServicesNS</code> would be set to <code>new
	 * String[]{"urn:iana:xml:ns:domain-1.0"}</code>.  Only a subset of the
	 * services loaded in the <code>EPPFactory</code> can be specified.
	 *
	 * @param newServiceNS An array of EPP XML namespace URIs to use in
	 * 		  sesssion
	 *
	 * @exception EPPCommandException Invalid namespace specified.
	 */
	public void setServices(String[] newServiceNS) throws EPPCommandException {
		// Desired services that are loaded in EPPFactory
		Vector theServices = new Vector();

		// Get the services loaded in EPPFactory
		Vector factoryServices = EPPFactory.getInstance().getServices();

		// For each desired service
		for (int newService = 0; newService < newServiceNS.length;
				 newService++) {
			boolean found = false;

			// For each factory services
			for (
				 int facService = 0;
					 !found && (facService < factoryServices.size());
					 facService++) {
				EPPService currFacService =
					(EPPService) factoryServices.elementAt(facService);

				// Found desired service namespace?
				if (
					newServiceNS[newService].equals(currFacService
														.getNamespaceURI())) {
					theServices.addElement(currFacService);
					found = true;
				}
			}

			// Current service not loaded in EPPFactory?
			if (!found) {
				throw new EPPCommandException("EppSession.setServices() : Invalid service: "
											  + newServiceNS[newService]
											  + " specified");
			}
		}

		// end For each desired service
		// Set the services attributes, which is used in login().
		myServices = theServices;
	}

	/**
	 * Set the Extension Services to use with this session by EPP XML namespace
	 * URIs. This must be called before calling <code>initSession</code> or
	 * <code>login</code>. The default setting is to use all of the services
	 * loaded in <code>EPPFactory</code>.  This method allows a client to
	 * specify a subset of the extensionservices loaded in the
	 * <code>EPPFactory</code>, which is useful for synchronizing with the
	 * extensionservices supported by a specific server. Either of the
	 * Paramerter ProtocolExtenions or the CommandResponseExtenisons can be
	 * null or both the parameteres can be null in which case there are no
	 * extenisons to be set.
	 *
	 * @param ProtocolExtensions ProtocolExtensions An vector
	 * 		  EPPPortocolExtension XML namespace URIs to use in sesssion
	 * @param CommandResponseExtensions CommandResponseExtensions An vector of
	 * 		  CommandResponseExtension XML namespace URIs to use in session
	 *
	 * @exception EPPCommandException Invalid namespace specified.
	 */
	public void setExtensions(
							  Vector ProtocolExtensions,
							  Vector CommandResponseExtensions)
					   throws EPPCommandException {
		// Desired Extensions that are loaded in EPPFactory
		Vector theExtensions = new Vector();

		// Get the Extensions loaded in EPPFactory
		Vector factoryExtensions = EPPFactory.getInstance().getExtensions();

		//Now combine the extensions(the ProtocolExtensions and The Command Responses)
		Vector extensionsVector = new Vector();

		if (
			(ProtocolExtensions != null)
				&& ProtocolExtensions.elements().hasMoreElements()) {
			for (int i = 0; i < ProtocolExtensions.size(); i++) {
				extensionsVector.addElement((String) ProtocolExtensions
											.elementAt(i));
			}
		}

		//end if
		if (
			(CommandResponseExtensions != null)
				&& CommandResponseExtensions.elements().hasMoreElements()) {
			for (int j = 0; j < CommandResponseExtensions.size(); j++) {
				extensionsVector.addElement((String) CommandResponseExtensions
											.elementAt(j));
			}
		}

		//end if
		if (
			(extensionsVector != null)
				&& extensionsVector.elements().hasMoreElements()) {
			// For each desired extension
			for (
				 int newExtension = 0; newExtension < extensionsVector.size();
					 newExtension++) {
				boolean found = false;

				// For each extensionfactory services
				for (
					 int facExtension = 0;
						 !found && (facExtension < factoryExtensions.size());
						 facExtension++) {
					EPPService currFacExtension =
						(EPPService) factoryExtensions.elementAt(facExtension);

					String     extensionNameSpace =
						(String) extensionsVector.elementAt(facExtension);

					// Found desired extension namespace?
					if (
						extensionNameSpace.equals(currFacExtension
													  .getNamespaceURI())) {
						theExtensions.addElement(currFacExtension);
						found = true;
					}
				}

				// Current service not loaded in EPPFactory?
				if (!found) {
					throw new EPPCommandException("EppSession.setExtenisons() : Invalid extension: "
												  + (String) (
													  extensionsVector
											 .elementAt(newExtension)
												  ) + " specified");
				}
			}

			// end For each desired extenison
		}

		//end outer if
		// Set the extensions attributes, which is used in login().
		myExtensionServices = theExtensions;

		for (int k = 0; k < myExtensionServices.size(); k++) {
			EPPService currFacService =
				(EPPService) myExtensionServices.elementAt(k);
		}
	}

	/**
	 * Sets the poll operation to either <code>OP_REQ</code> or
	 * <code>OP_ACK</code>
	 *
	 * @param aOp <code>OP_REQ</code> or <code>OP_ACK</code>.
	 */
	public void setPollOp(String aOp) {
		myPollOp = aOp;
	}

	/**
	 * Gets the poll operation, which should be either <code>OP_REQ</code> or
	 * <code>OP_ACK</code>
	 *
	 * @return Either <code>OP_REQ</code> or <code>OP_ACK</code>
	 */
	public String getPollOp() {
		return myPollOp;
	}

	/**
	 * Gets the status client transaction identifier.
	 *
	 * @return Status client transaction identifier if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public String getStatusTransId() {
		return statusTransId;
	}

	/**
	 * Sets the status client transaction identifier.
	 *
	 * @param aStatusTrans Status client transaction identifier.
	 */
	public void setStatusTransId(String aStatusTrans) {
		statusTransId = aStatusTrans;
	}

	/**
	 * Gets the status command type.  The command type should be one of the
	 * valid <code>EPPCommand.TYPE_</code> constants.
	 *
	 * @return Status status command type if defined; <code>null</code>
	 * 		   otherwise.
	 */
	public String getStatusCommandType() {
		return statusCommandType;
	}

	/**
	 * Sets the status command type.
	 *
	 * @param aStatusCommandType Status command type, which should be one of
	 * 		  the following:<br>
	 * 		  <ul><li><code>EPPCommand.TYPE_CREATE</code>
	 * 		  <li><code>EPPCommand.TYPE_DELETE</code>
	 * 		  <li><code>EPPCommand.TYPE_RENEW</code>
	 * 		  <li><code>EPPCommand.TYPE_TRANSFER</code>
	 * 		  <li><code>EPPCommand.TYPE_UPDATE</code> </ul>
	 */
	public void setStatusCommandType(String aStatusCommandType) {
		statusCommandType = aStatusCommandType;
	}

	/**
	 * Gets the poll Message Id.
	 *
	 * @return Message Id if defined; null otherwise.
	 */
	public String getMsgID() {
		return msgID;
	}

	/**
	 * Sets the poll Message Id.
	 *
	 * @param aMsgID Message Id
	 */
	public void setMsgID(String aMsgID) {
		msgID = aMsgID;
	}


	/**
	 * Gets the command/response processing mode, which should be 
	 * either {@link #MODE_SYNC} or {@link #MODE_ASYNC}.
	 * 
	 * @return {@link #MODE_SYNC} or {@link #MODE_ASYNC}
	 */
	public int getMode() {
		return this.mode;
	}


	/**
	 * Sets the command/response processing mode to either 
	 * {@link #MODE_SYNC} or {@link #MODE_ASYNC}.
	 * 
	 * @param aMode {@link #MODE_SYNC} or {@link #MODE_ASYNC}
	 * 
	 * @return Previous mode
	 */
	public int setMode(int aMode) {
		// Is specified mode supported?
		if (isModeSupported(aMode)) {
			int thePrevMode = this.mode;
			this.mode = aMode;
			return thePrevMode;
		}
		else {
			cat.error("EPPSession.setMode(): Mode " + aMode
					+ " NOT supported, current mode " + this.mode
					+ " unchanged");
			return this.mode;
		}
	}
	
	/**
	 * Does the session support the specified mode {@link #MODE_SYNC} or {@link #MODE_ASYNC}?
	 * A derived class of <code>EPPSession</code> could support a subset of the modes, so 
	 * this method is provide to provide the client with the ability to check whether a 
	 * mode is supported.
	 * 
	 * @param aMode {@link #MODE_SYNC} or {@link #MODE_ASYNC}
	 * @return <code>true</code> if supported; <code>false</code> otherwise.
	 */
	public boolean isModeSupported(int aMode) {
		if (aMode == MODE_SYNC || aMode == MODE_ASYNC) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
}


// End class EPPSession
