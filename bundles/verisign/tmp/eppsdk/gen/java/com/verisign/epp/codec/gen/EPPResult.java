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
package com.verisign.epp.codec.gen;


// Log4j Imports
import org.apache.log4j.Logger;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.util.Hashtable;
import java.util.Iterator;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Vector;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an individual result that is contained in an
 * <code>EPPResponse</code>. A result documents the success or failure of
 * command execution.  If the command was processed successfully, only one
 * &lt;result&gt; element MUST be returned.  If the command was not processed
 * successfully, multiple &lt;result&gt; elements MAY be returned to document
 * failure conditions.  Each &lt;result&gt; element contains the following
 * attribute and child elements:<br><br>
 * 
 * <ul>
 * <li>
 * A "code" attribute whose value is a four-digit, decimal number that
 * describes the success or failure of the command.
 * </li>
 * <li>
 * A &lt;msg&gt; element containing a human-readable description of the
 * response code.  The language of the response is identified via an OPTIONAL
 * "lang" attribute.  If not specified, the default attribute value MUST be
 * "en" (English).
 * </li>
 * <li>
 * Zero or more OPTIONAL &lt;value&gt; elements that identify a client-
 * provided element (including XML tag and value) that caused a server error
 * condition.
 * </li>
 * <li>
 * Zero or more OPTIONAL &lt;extValue&gt; elements that can be used to provide
 * additional error diagnostic information.
 * </li>
 * </ul>
 */
public class EPPResult implements EPPCodecComponent {
	/** The default language of the result message "en". */
	public static final String DEFAULT_LANG = "en";

	//--- Command Success Constants

	/**
	 * Command completed successfully <br>
	 * <br>
	 * This is the usual response code for a successfully completed command
	 * that is not addressed by any other 1xxx-series response code.
	 */
	public static final int SUCCESS = 1000;

	/**
	 * Command completed successfully; action pending <br>
	 * <br>
	 * This response code MUST be returned when responding to a command the
	 * requires offline activity before the requested action can be completed.
	 */
	public static final int SUCCESS_PENDING = 1001;

	/**
	 * Command completed successfully; no messages <br>
	 * <br>
	 * This response code MUST be returned when responding to a poll request
	 * command and the server message queue is empty.
	 */
	public static final int SUCCESS_POLL_NO_MSGS = 1300;

	/**
	 * Command completed successfully; ack to dequeue <br>
	 * <br>
	 * This response code MUST be returned when responding to a &lt;poll&gt;
	 * request command and a message has been retrieved from the server
	 * message queue.
	 */
	public static final int SUCCESS_POLL_MSG = 1301;

	/**
	 * Command completed successfully; ending session <br>
	 * <br>
	 * This response code MUST be returned when responding to a successful
	 * logout command
	 */
	public static final int SUCCESS_END_SESSION = 1500;

	//--- Command Error Constants

	/**
	 * Unknown command <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * element that is not defined by EPP.
	 */
	public static final int UNKNOWN_COMMAND = 2000;

	/**
	 * Command syntax error <br>
	 * <br>
	 * This response code MUST be returned when a server receives an
	 * improperly formed command element.
	 */
	public static final int COMMAND_SYNTAX_ERROR = 2001;

	/**
	 * Command use error <br>
	 * <br>
	 * This response code MUST be returned when a server receives a properly
	 * formed command element, but the command can not be executed due to a
	 * sequencing or context error.  For example, a logout command can not be
	 * executed without having first completed a login command.
	 */
	public static final int COMMAND_USE_ERROR = 2002;

	/**
	 * Required parameter missing <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * for which a required parameter value has not been provided.
	 */
	public static final int MISSING_PARAMETER = 2003;

	/**
	 * Parameter value range error <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * parameter whose value is outside the range of values specified by the
	 * protocol.  The error value SHOULD be returned via a value element in
	 * the EPP response.
	 */
	public static final int PARAM_OUT_OF_RANGE = 2004;

	/**
	 * Parameter value syntax error <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * containing a parameter whose value is improperly formed.  The error
	 * value SHOULD be returned via a value element in the EPP response.
	 */
	public static final int PARAM_SYNTAX_ERROR = 2005;

	/**
	 * Unimplemented protocol version <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * element specifying a protocol version that is not implemented by the
	 * server.
	 */
	public static final int UNIMPLEMENTED_VERSION = 2100;

	/**
	 * Unimplemented command <br>
	 * <br>
	 * This response code MUST be returned when a server receives a valid EPP
	 * command element that is not implemented by the server.  For example, a
	 * transfer command MAY be unimplemented for certain object types.
	 */
	public static final int UNIMPLEMENTED_COMMAND = 2101;

	/**
	 * Unimplemented option <br>
	 * <br>
	 * This response code MUST be returned when a server receives a valid EPP
	 * command element that contains a protocol option that is not implemented
	 * by the server.  For example, a server MAY not implement the protocol's
	 * session-less operating mode.
	 */
	public static final int UNIMPLEMENTED_OPTION = 2102;

	/**
	 * Unimplemented extension <br>
	 * <br>
	 * This response code MUST be returned when a server receives a valid EPP
	 * command element that contains a protocol command extension that is not
	 * implemented by the server.
	 */
	public static final int UNIMPLEMENTED_EXTENSION = 2103;

	/**
	 * Billing failure <br>
	 * <br>
	 * This response code MUST be returned when a server attempts to execute a
	 * billable operation and the command can not be completed due to a client
	 * billing failure.
	 */
	public static final int BILLING_ERROR = 2104;

	/**
	 * Object is not eligible for renewal <br>
	 * <br>
	 * This response code MUST be returned when a client attempts to renew an
	 * object that is not eligible for renewal in accordance with server
	 * policy.
	 */
	public static final int NOT_RENEWABLE = 2105;

	/**
	 * Object is not eligible for transfer <br>
	 * <br>
	 * This response code MUST be returned when a client attempts to transfer
	 * an object that is not eligible for transfer in accordance with server
	 * policy.
	 */
	public static final int NOT_TRANSFERABLE = 2106;

	/**
	 * Authentication error <br>
	 * <br>
	 * This response code MUST be returned when a server notes an error when
	 * validating client credentials.
	 */
	public static final int AUTHENTICATION_ERROR = 2200;

	/**
	 * Authorization error <br>
	 * <br>
	 * This response code MUST be returned when a server notes a client
	 * authorization error when executing a command.  This error is used to
	 * note that a client lacks privileges to execute the requested command.
	 */
	public static final int AUTHORIZATION_ERROR = 2201;

	/**
	 * Invalid authorization information <br>
	 * <br>
	 * This response code MUST be returned when a server receives invalid
	 * command authorization information required to confirm authorization to
	 * execute a command.  This error is used to note that a client has the
	 * privileges required to execute the requested command, but the
	 * authorization information provided by the client does not match the
	 * authorization information archived by the server.
	 */
	public static final int INVALID_AUTHORIZATION_INFO = 2202;

	/**
	 * Object pending transfer <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * transfer an object that is pending transfer due to an earlier transfer
	 * request.
	 */
	public static final int OBJECT_PENDING_TRANSFER = 2300;

	/**
	 * Object not pending transfer <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * confirm, reject, or cancel the transfer an object when no command has
	 * been made to transfer the object.
	 */
	public static final int OBJECT_NOT_PENDING_TRANSFER = 2301;

	/**
	 * Object exists <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * create an object that already exists in the repository.
	 */
	public static final int OBJECT_EXISTS = 2302;

	/**
	 * Object does not exist <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * query or transform an object that does not exist in the repository.
	 */
	public static final int OBJECT_DOES_NOT_EXIST = 2303;

	/**
	 * Object status prohibits operation <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * transform an object that can not be completed due to server policy or
	 * business practices.  For example, a server MAY disallow transfer
	 * commands under terms and conditions that are matters of local policy,
	 * or the server may have received a delete command for an object whose
	 * status prohibits deletion.
	 */
	public static final int STATUS_PROHIBITS_OP = 2304;

	/**
	 * Object association prohibits operation <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * transform an object that can not be completed due to dependencies on
	 * other objects that are associated with the target object.  For example,
	 * a server MAY disallow delete commands while an object has active
	 * associations with other objects.
	 */
	public static final int ASSOC_PROHIBITS_OP = 2305;

	/**
	 * Parameter value policy error <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * containing a parameter value that is syntactically valid, but
	 * semantically invalid due to local policy.  For example, the server MAY
	 * support a subset of a range of valid protocol parameter values. The
	 * error value SHOULD be returned via a &lt;value&gt; element in the EPP
	 * response.
	 */
	public static final int PARAM_VALUE_POLICY_ERROR = 2306;

	/**
	 * Unimplemented object service <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command to
	 * operate on an object service that is not supported by the server.
	 */
	public static final int UNIMPLEMENTED_OBJECT_SERVICE = 2307;

	/**
	 * Data management policy violation <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * whose execution results in a violation of server data management
	 * policies.  For example, removing all attribute values or object
	 * associations from an object MAY be a violation of a server's data
	 * management policies.
	 */
	public static final int DATA_MGT_POLICY_VIOLATION = 2308;

	/**
	 * Command failed <br>
	 * <br>
	 * This response code MUST be returned when a server is unable to execute
	 * a command due to an internal server error that is not related to the
	 * protocol.  The failure MAY be transient.  The server MUST keep any
	 * ongoing session active.
	 */
	public static final int COMMAND_FAILED = 2400;

	/**
	 * Command failed; server closing connection <br>
	 * <br>
	 * This response code MUST be returned when a server receives a command
	 * that can not be completed due to an internal server error that is not
	 * related to the protocol.  The failure is not transient, and will cause
	 * other commands to fail as well.  The server MUST end the active session
	 * and close the existing connection.
	 */
	public static final int COMMAND_FAILED_END = 2500;

	/**
	 * Authentication error; server closing connection <br>
	 * <br>
	 * This response code MUST be returned when a server notes an error when
	 * validating client credentials and a server-defined limit on the number
	 * of allowable failures has been exceeded.  The server MUST close the
	 * existing connection.
	 */
	public static final int AUTHENTICATION_ERROR_END = 2501;

	/**
	 * Session limit exceeded; server closing connection <br>
	 * <br>
	 * This response code MUST be retunred when a server receives a login
	 * command, and the command can not be completed because the client has
	 * exceeded a system-defined limit on the number of session that the
	 * client can establish.  It may be possible to establish a session by
	 * ending existing unused sessions.
	 */
	public static final int SESSION_LIMIT_END = 2502;

	/**
	 * Hash that maps the pre-defined result codes to their associated message
	 * in     the default language of "en".
	 */
	private static Hashtable defaultMsg;

	// Static Initializer for EPPResult
	static {
		defaultMsg = new Hashtable();
		defaultMsg.put(new Integer(SUCCESS), "Command completed successfully");
		defaultMsg.put(
					   new Integer(SUCCESS_PENDING),
					   "Command completed successfully; action pending");
		defaultMsg.put(
					   new Integer(SUCCESS_POLL_NO_MSGS),
					   "Command completed successfully; no messages");
		defaultMsg.put(
					   new Integer(SUCCESS_POLL_MSG),
					   "Command completed successfully; ack to dequeue");
		defaultMsg.put(
					   new Integer(SUCCESS_END_SESSION),
					   "Command completed successfully; ending session");
		defaultMsg.put(new Integer(UNKNOWN_COMMAND), "Unknown command");
		defaultMsg.put(
					   new Integer(COMMAND_SYNTAX_ERROR), "Command syntax error");
		defaultMsg.put(new Integer(COMMAND_USE_ERROR), "Command use error");
		defaultMsg.put(
					   new Integer(MISSING_PARAMETER),
					   "Required parameter missing");
		defaultMsg.put(
					   new Integer(PARAM_OUT_OF_RANGE),
					   "Parameter value range error");
		defaultMsg.put(
					   new Integer(PARAM_SYNTAX_ERROR),
					   "Parameter value syntax error");
		defaultMsg.put(
					   new Integer(UNIMPLEMENTED_VERSION),
					   "Unimplemented protocol version");
		defaultMsg.put(
					   new Integer(UNIMPLEMENTED_COMMAND),
					   "Unimplemented command");
		defaultMsg.put(
					   new Integer(UNIMPLEMENTED_OPTION), "Unimplemented option");
		defaultMsg.put(
					   new Integer(UNIMPLEMENTED_EXTENSION),
					   "Unimplemented extension");
		defaultMsg.put(new Integer(BILLING_ERROR), "Billing failure");
		defaultMsg.put(
					   new Integer(NOT_RENEWABLE),
					   "Object is not eligible for renewal");
		defaultMsg.put(
					   new Integer(NOT_TRANSFERABLE),
					   "Object is not eligible for transfer");
		defaultMsg.put(
					   new Integer(AUTHENTICATION_ERROR), "Authentication error");
		defaultMsg.put(new Integer(AUTHORIZATION_ERROR), "Authorization error");
		defaultMsg.put(
					   new Integer(INVALID_AUTHORIZATION_INFO),
					   "Invalid authorization information");
		defaultMsg.put(
					   new Integer(OBJECT_PENDING_TRANSFER),
					   "Object pending transfer");
		defaultMsg.put(
					   new Integer(OBJECT_NOT_PENDING_TRANSFER),
					   "Object not pending transfer");
		defaultMsg.put(new Integer(OBJECT_EXISTS), "Object exists");
		defaultMsg.put(
					   new Integer(OBJECT_DOES_NOT_EXIST),
					   "Object does not exist");
		defaultMsg.put(
					   new Integer(STATUS_PROHIBITS_OP),
					   "Object status prohibits operation");
		defaultMsg.put(
					   new Integer(ASSOC_PROHIBITS_OP),
					   "Object association prohibits operation");
		defaultMsg.put(
					   new Integer(PARAM_VALUE_POLICY_ERROR),
					   "Parameter value policy error");
		defaultMsg.put(
					   new Integer(UNIMPLEMENTED_OBJECT_SERVICE),
					   "Unimplemented object service");
		defaultMsg.put(
					   new Integer(DATA_MGT_POLICY_VIOLATION),
					   "Data management policy violation");
		defaultMsg.put(new Integer(COMMAND_FAILED), "Command failed");
		defaultMsg.put(
					   new Integer(COMMAND_FAILED_END),
					   "Command failed; server closing connection");
		defaultMsg.put(
					   new Integer(AUTHENTICATION_ERROR_END),
					   "Authentication error; server closing connection");
		defaultMsg.put(
					   new Integer(SESSION_LIMIT_END),
					   "Session limit exceeded; server closing connection");
	}

	/** XML root tag name for <code>EPPResult</code>. */
	final static String ELM_NAME = "result";

	/** XML tag name for the <code>message</code> element. */
	private final static String ELM_MSG = "msg";

	/** XML tag name for the optional <code>data</code> element. */
	private final static String ELM_DATA = "data";

	/** XML tag name for the optional <code>extValue</code> elements. */
	private final static String ELM_EXT_VALUE = "extValue";

	/** XML attribute name for the result <code>code</code> attribute. */
	private final static String ATTR_CODE = "code";

	/**
	 * XML attribute name for the optional result <code>lang</code> attribute.
	 */
	private final static String ATTR_LANG = "lang";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPResult.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/** Result code that must be in the range 1000-9999. */
	private int code = -1;

	/**
	 * Result message.  <code>defaultMsg</code> defines the default  message
	 * values for the pre-defined result codes.
	 */
	private String message = "";

	/**
	 * Optional Vector of <code>EPPValue</code> instances that caused the
	 * error.
	 */
	private Vector values = null;

	/**
	 * Optional Vector of <code>EPPExtValue</code> instances that caused the
	 * error.
	 */
	private Vector extValues = null;

	/**
	 * Language of the <code>message</code> attribute.  A value of null
	 * indicates     the default value of "en".
	 */
	private String lang = DEFAULT_LANG;

	/**
	 * Allocates a new <code>EPPResult</code> with default attribute values.
	 * The defaults are set to:<br>
	 * 
	 * <ul>
	 * <li>
	 * code is set to <code>SUCCESS</code>
	 * </li>
	 * <li>
	 * values is set to <code>null</code>
	 * </li>
	 * <li>
	 * message is set to "Command completed successfully"
	 * </li>
	 * <li>
	 * lang is set to "en"
	 * </li>
	 * </ul>
	 */
	public EPPResult() {
		code	    = SUCCESS;
		message     = (String) defaultMsg.get(new Integer(code));
	}

	// End EPPResult.EPPResult()

	/**
	 * Allocates a new <code>EPPResult</code> with a result code.  The other
	 * attributes will be set as follows:<br>
	 * 
	 * <ul>
	 * <li>
	 * values is set to <code>null</code>
	 * </li>
	 * <li>
	 * message is set to "en" equivalent of <code>aCode</code> if code is a
	 * pre-defined code; otherwise is set to <code>null</code>.
	 * </li>
	 * <li>
	 * lang is set to <code>DEFAULT_LANG</code>.
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aCode result code that must be in the range 1000-9999.
	 */
	public EPPResult(int aCode) {
		code	    = aCode;
		message     = (String) defaultMsg.get(new Integer(aCode));
	}

	// End EPPResult.EPPResult(int)

	/**
	 * Allocates a new <code>EPPResult</code> with a result code and result
	 * message.  The other attributes     will be set as follows:<br>
	 * 
	 * <ul>
	 * <li>
	 * values is set to <code>null</code>
	 * </li>
	 * <li>
	 * lang is set to <code>DEFAULT_LANG</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aCode result code that must be in the range 1000-9999.
	 * @param aMessage result message in the default "en" language.
	 */
	public EPPResult(int aCode, String aMessage) {
		code	    = aCode;
		message     = aMessage;
	}

	// End EPPResult.EPPResult(int, String)

	/**
	 * Allocates a new <code>EPPResult</code> with a result code, result
	 * message, and     result message language.  The other attributes will be
	 * set as follows:<br>
	 * 
	 * <ul>
	 * <li>
	 * values is set to <code>null</code>
	 * </li>
	 * </ul>
	 * 
	 *
	 * @param aCode result code that must be in the range 1000-9999.
	 * @param aMessage result message in the default "en" language.
	 * @param aLang Language of result message.
	 */
	public EPPResult(int aCode, String aMessage, String aLang) {
		code	    = aCode;
		message     = aMessage;
		lang	    = aLang;
	}

	// End EPPResult.EPPResult(int, String, String)

	/**
	 * Allocates a new <code>EPPResult</code> with a result code, result
	 * message, and result values. <br>
	 * This method should only be called for error results, since successful
	 * results don't include values.
	 *
	 * @param aCode result code that must be in the range 1000-9999.
	 * @param aMessage result message in the default "en" language.
	 * @param aValues Vector of either <code>EPPValue</code> or
	 * 		  <code>EPPExtValue</code> instances.
	 */
	public EPPResult(int aCode, String aMessage, Vector aValues) {
		code	    = aCode;
		message     = aMessage;
		this.setAllValues(aValues);
	}

	// End EPPResult.EPPResult(int, String, Vector)

	/**
	 * Allocates a new <code>EPPResult</code> with all of the attribute set
	 * with the arguments.  This method should only be called for  error
	 * results, since successful results don't include values.
	 *
	 * @param aCode result code that must be in the range 1000-9999.
	 * @param aMessage result message in the default "en" language.
	 * @param aLang Language of result message.
	 * @param aValues Vector of either <code>EPPValue</code> or
	 * 		  <code>EPPExtValue</code> instances.
	 */
	public EPPResult(int aCode, String aMessage, String aLang, Vector aValues) {
		code	    = aCode;
		message     = aMessage;
		lang	    = aLang;
		this.setAllValues(aValues);
	}

	// End EPPResult.EPPResult(int, String, String, Vector)

	/**
	 * Adds an individual XML value to the list of XML values.  The XML prefix
	 * and XML prefix will be set to the default values.
	 *
	 * @param aValue XML <code>String</code> value.  For example,
	 * 		  "&lt;epp:login/&gt;".
	 */
	public void addValue(String aValue) {
		if (this.values == null) {
			this.values = new Vector();
		}

		this.values.addElement(new EPPValue(aValue));
	}

	// End EPPResult.addValue(String)

	/**
	 * Adds an <code>EPPValue</code> instance to the list of client error
	 * elements.   This method allows for the specification of an XML prefix
	 * and XML namespace for the error value.
	 *
	 * @param aValue <code>EPPValue</code> instance that includes the client
	 * 		  element XML and its associated XML prefix and namespace.
	 */
	public void addValue(EPPValue aValue) {
		if (this.values == null) {
			this.values = new Vector();
		}

		this.values.addElement(aValue);
	}

	// End EPPResult.addValue(EPPValue)

	/**
	 * Adds an <code>extValue</code> reason, which will set the value, XML
	 * namespace, and XML prefix to default values.  The default values will
	 * not specify a client element that caused the error.  The reason is free
	 * form text that can be  used to provide more information about the error
	 * from the server business logic.
	 *
	 * @param aReason Human-readable message that describes the reason for the
	 * 		  error.
	 */
	public void addExtValueReason(String aReason) {
		if (this.extValues == null) {
			this.extValues = new Vector();
		}

		extValues.addElement(new EPPExtValue(aReason));
	}

	// End EPPResult.addExtValueReason(String)

	/**
	 * Adds an <code>EPPExtValue</code> instance to the list of client error
	 * elements.   This method allows for the specification of an XML prefix
	 * and XML namespace for the error value and a reason.
	 *
	 * @param aExtValue <code>EPPExtValue</code> instance that includes the
	 * 		  client element XML and its associated XML prefix and namespace.
	 */
	public void addExtValue(EPPExtValue aExtValue) {
		if (this.extValues == null) {
			this.extValues = new Vector();
		}

		this.extValues.addElement(aExtValue);
	}

	// End EPPResult.addValue(EPPExtValue)

	/**
	 * Clone <code>EPPResult</code>.
	 *
	 * @return Deep copy clone of <code>EPPResult</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPResult clone = null;

		clone = (EPPResult) super.clone();

		if (values != null) {
			clone.values = (Vector) values.clone();

			for (int i = 0; i < values.size(); i++) {
				clone.values.set(i, ((EPPValue) values.elementAt(i)).clone());
			}
		}

		if (extValues != null) {
			clone.extValues = (Vector) extValues.clone();

			for (int i = 0; i < extValues.size(); i++) {
				clone.extValues.set(
									i,
									((EPPExtValue) extValues.elementAt(i))
									.clone());
			}
		}

		return clone;
	}

	// End EPPResult.clone()

	/**
	 * decode <code>EPPResult</code> from a DOM element tree.  The
	 * <code>aElement</code> argument needs to be the &ltresult&gt element.
	 *
	 * @param aElement The &ltresult&gt XML element.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		NodeList currElmList;
		Element  currElm;

		// Code
		code     = Integer.parseInt(aElement.getAttribute(ATTR_CODE));

		// Message
		currElmList = aElement.getElementsByTagNameNS(EPPCodec.NS, ELM_MSG);

		if (currElmList.getLength() != 0) {
			currElm = (Element) currElmList.item(0);
		}
		else {
			throw new EPPDecodeException("Required EPPResult element "
										 + ELM_MSG + " not found");
		}

		message = currElm.getFirstChild().getNodeValue();

		if (message == null) {
			throw new EPPDecodeException("Required message value of EPPResult element "
										 + ELM_MSG + " not found");
		}

		// Lang
		setLang(currElm.getAttribute(ATTR_LANG));

		// Values
		this.values =
			EPPUtil.decodeCompVector(
									 aElement, EPPCodec.NS, EPPValue.ELM_NAME,
									 EPPValue.class);

		if (this.values.size() == 0) {
			this.values = null;
		}

		// ExtValues
		this.extValues =
			EPPUtil.decodeCompVector(
									 aElement, EPPCodec.NS, EPPExtValue.ELM_NAME,
									 EPPExtValue.class);

		if (this.extValues.size() == 0) {
			this.extValues = null;
		}

		// Data
		currElmList = aElement.getElementsByTagNameNS(EPPCodec.NS, ELM_DATA);
	}

	// End EPPResult.decode(Element)

	/**
	 * encode <code>EPPResult</code> into a DOM element tree. The result     is
	 * created and the attribute nodes are     appended as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltresult&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		Element currElm;
		Text    currVal;

		// Code
		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);
		root.setAttribute(ATTR_CODE, code + "");

		// Message
		currElm     = aDocument.createElementNS(EPPCodec.NS, ELM_MSG);
		currVal     = aDocument.createTextNode(message);

		// Lang
		if (!lang.equals(DEFAULT_LANG)) {
			currElm.setAttribute(ATTR_LANG, lang);
		}

		currElm.appendChild(currVal);
		root.appendChild(currElm);

		// Values
		EPPUtil.encodeCompVector(aDocument, root, this.values);

		// ExtValues
		EPPUtil.encodeCompVector(aDocument, root, this.extValues);

		return root;
	}

	// End EPPResult.encode(Document)

	/**
	 * implements a deep <code>EPPResult</code> compare.
	 *
	 * @param aObject <code>EPPResult</code> instance to compare with
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPResult)) {
			cat.error("EPPResult.equals(): " + aObject.getClass().getName()
					  + " not EPPResult instance");

			return false;
		}

		EPPResult theResult = (EPPResult) aObject;

		// code
		if (code != theResult.code) {
			cat.error("EPPResult.equals(): code not equal");

			return false;
		}

		// message
		if (
			!(
					(message == null) ? (theResult.message == null)
										  : message.equals(theResult.message)
				)) {
			cat.error("EPPResult.equals(): message not equal");

			return false;
		}

		// lang
		if (!lang.equals(theResult.lang)) {
			cat.error("EPPResult.equals(): lang not equal");

			return false;
		}

		// values
		if (!EPPUtil.equalVectors(values, theResult.values)) {
			cat.error("EPPResult.equals(): values not equal");

			return false;
		}

		// extValues
		if (!EPPUtil.equalVectors(extValues, theResult.extValues)) {
			cat.error("EPPResult.equals(): extValues not equal");

			return false;
		}

		return true;
	}

	// End EPPResult.equals(Object)

	/**
	 * Gets the result code.
	 *
	 * @return Result code that must be in the range 1000-9999.
	 */
	public int getCode() {
		return code;
	}

	// End EPPResult.getCode()

	/**
	 * Gets the message language of the result.   The Language must be
	 * structured as documented in <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @return Language of the message.
	 */
	public String getLang() {
		return lang;
	}

	// End EPPResult.getLang()

	/**
	 * Gets the message of the result.  The language of the message can be
	 * retreived by <code>getLang</code>.
	 *
	 * @return Message <code>String</code> describing the result.
	 */
	public String getMessage() {
		return message;
	}

	// End EPPResult.getMessage()

	/**
	 * Gets the <code>EPPValue</code> instances associated with the result.
	 *
	 * @return Vector error value <code>EPPValue</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getValues() {
		return this.values;
	}

	// End EPPResult.getValues()

	/**
	 * Gets the <code>EPPExtValue</code> instances associated with the result.
	 *
	 * @return Vector error value <code>EPPExtValue</code> instances if
	 * 		   defined; <code>null</code> otherwise.
	 */
	public Vector getExtValues() {
		return this.extValues;
	}

	// End EPPResult.getExtValues()

	/**
	 * Gets a <code>Vector</code> of all of the <code>EPPValue</code> and
	 * <code>EPPExtValue</code> defined in the result.
	 *
	 * @return Vector error value <code>EPPValue</code> or
	 * 		   <code>EPPExtValue</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 */
	public Vector getAllValues() {
		if ((this.values == null) && (this.extValues == null)) {
			return null;
		}
		else if (this.values == null) {
			return this.extValues;
		}
		else if (this.extValues == null) {
			return this.values;
		}
		else // both are not null
		 {
			Vector theValues = new Vector();
			theValues.addAll(this.values);
			theValues.addAll(this.extValues);

			return theValues;
		}
	}

	// End EPPResult.getAllValues()

	/**
	 * Sets the value and extValue lists based on a single <code>Vector</code>
	 * of <code>EPPValue</code> or <code>EPPExtValue</code> instances.
	 *
	 * @param aValues <code>Vector</code> of <code>EPPValue</code> or
	 * 		  <code>EPPExtValue</code> instances.
	 */
	public void setAllValues(Vector aValues) {
		if (aValues != null) {
			Iterator theIter = aValues.iterator();

			while (theIter.hasNext()) {
				Object currValue = theIter.next();

				if (currValue instanceof EPPValue) {
					this.addValue((EPPValue) currValue);
				}
				else if (currValue instanceof EPPExtValue) {
					this.addExtValue((EPPExtValue) currValue);
				}
				else {
					cat.error("EPPResult.setAllValues(): "
							  + currValue.getClass().getName()
							  + " not EPPValue or EPPExtValue instance");
				}
			}
		}
	}

	// End EPPResult.setAllValues(Vector)

	/**
	 * Gets the value strings associated with the result.
	 *
	 * @return Vector error value <code>String</code> instances if defined;
	 * 		   <code>null</code> otherwise.
	 *
	 * @since EPP 1.0
	 */
	public Vector getStrValues() {
		if (this.values == null) {
			return null;
		}

		Vector   theStrValues = new Vector();

		Iterator theIter = this.values.iterator();

		while (theIter.hasNext()) {
			EPPValue currValue = (EPPValue) theIter.next();

			theStrValues.add(currValue.getValue());
		}

		return theStrValues;
	}

	// End EPPResult.getStrValues()

	/**
	 * Was the result a succcess?
	 *
	 * @return <code>true</code> if success; <code>false</code> otherwise.
	 */
	public boolean isSuccess() {
		if ((code >= 1000) && (code < 2000)) {
			return true;
		}
		else {
			return false;
		}
	}

	// End EPPResult.isSuccess()

	
	/**
	 * Should the EPP session be closed based on the result code?
	 * 
	 * @return <code>true</code> if the session should be closed; <code>false</code> otherwise
	 */
	public boolean shouldCloseSession() {
		if ((this.code == COMMAND_FAILED_END) ||
			(this.code == AUTHENTICATION_ERROR_END) ||
			(this.code == SESSION_LIMIT_END) || 
			(this.code == SUCCESS_END_SESSION))
			return true;
		else
			return false;
	}
	
	/**
	 * Sets the result code.
	 *
	 * @param aCode Result code that must be in the range 1000-9999.
	 */
	public void setCode(int aCode) {
		code = aCode;
	}

	// End EPPResult.setCode(int)

	/**
	 * Sets the result code and the default en message associated with the
	 * result code if <code>aUseDefaultMessage</code> is set to
	 * <code>true</code>; otherwise only the result code is set.
	 *
	 * @param aCode Result code that must be in the range 1000-9999.
	 * @param aUseDefaultMessage Use the default en message associated with
	 * 		  aCode?
	 */
	public void setCode(int aCode, boolean aUseDefaultMessage) {
		code = aCode;

		if (aUseDefaultMessage) {
			message = (String) defaultMsg.get(new Integer(aCode));
		}
	}

	// End EPPResult.setCode(int, boolean)

	/**
	 * Sets the message language of the result.   The Language must be
	 * structured as documented in <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @param aLang Language of the message.
	 */
	public void setLang(String aLang) {
		if ((aLang == null) || (aLang.equals(""))) {
			lang = DEFAULT_LANG;
		}
		else {
			lang = aLang;
		}
	}

	// End EPPResult.setMessage(String)

	/**
	 * Sets the message of the result in the default language of "en".
	 *
	 * @param aMessage Message <code>String</code> describing the result.
	 */
	public void setMessage(String aMessage) {
		message = aMessage;
	}

	// End EPPResult.setMessage(String)

	/**
	 * Sets the message of the result along with the language of the message.
	 * The Language must be   structured as documented in <a
	 * href="http://www.ietf.org/rfc/rfc1766.txt?number=1766">[RFC1766]</a>.
	 *
	 * @param aMessage Message <code>String</code> describing the result.
	 * @param aLang Language of the message
	 */
	public void setMessage(String aMessage, String aLang) {
		message = aMessage;

		setLang(aLang);
	}

	// End EPPResult.setMessage(String, String)

	/**
	 * Sets the values associated with the result.
	 *
	 * @param aValues Array of <code>EPPValue</code> instances.
	 */
	public void setValues(EPPValue[] aValues) {
		this.values = new Vector();
		this.values.copyInto(aValues);
	}

	// End EPPResult.setValues(EPPValue[])

	/**
	 * Sets the values associated with the result.
	 *
	 * @param aValues Vector of <code>EPPValue</code> instances.
	 */
	public void setValues(Vector aValues) {
		this.values = aValues;
	}

	// End EPPResult.setValues(Vector)

	/**
	 * Sets the extended values associated with the result using an array. In
	 * the specification, an extended value can be used to provide additional
	 * error diagnostic information, including a value identifying a
	 * client-provided element (including XML tag and value) that caused a
	 * server error condition, and a reason containing a human-readable
	 * message that describes the reason for the error.
	 *
	 * @param aExtValues Array of <code>EPPExtValue</code> instances.
	 *
	 * @since EPP 1.0
	 */
	public void setExtValues(EPPExtValue[] aExtValues) {
		this.extValues = new Vector();
		this.extValues.copyInto(aExtValues);
	}

	// End EPPResult.setExtValues(EPPExtValue[])

	/**
	 * Sets the extValue's associated with the result.
	 *
	 * @param aExtValues Vector of <code>EPPExtValue</code> instances.
	 */
	public void setExtValues(Vector aExtValues) {
		this.extValues = aExtValues;
	}

	// End EPPResult.setValues(Vector)

	/**
	 * Sets a <code>Vector</code> of <code>extValue</code> reasons.  
	 * Each reason will set the value, 
	 * XML namespace, and XML prefix to default values.  The default values will
	 * not specify a client element that caused the error.  The reason is free
	 * form text that can be  used to provide more information about the error
	 * from the server business logic.
	 *
	 * @param aReasons <code>Vector</code> of human-readable <code>String</code> 
	 * messages that describe the reason for the error.
	 */
	public void setExtValueReasons(Vector aReasons) {
		if (aReasons != null) {
			Iterator theIter = aReasons.iterator();
			
			while (theIter.hasNext()) {
				this.addExtValueReason((String) theIter.next());
			}
		}
	}
	
	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 *
	 * @return Indented XML <code>String</code> if successful;
	 * 		   <code>ERROR</code> otherwise.
	 */
	public String toString() {
		return EPPUtil.toString(this);
	}

	// End EPPResult.toString()
}


// End class EPPResult
