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

// W3C Imports
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;

import com.verisign.epp.util.EPPCatFactory;


/**
 * Represents an EPP Greeting message, which an EPP server uses in response to
 * a successful connection from an EPP client.  The EPP Greeting contains the
 * following elements:<br>
 * 
 * <ul>
 * <li>
 * A &ltgreeting&gt element that identifies the start of the greeting.
 * </li>
 * <li>
 * A &ltsvID&gt element that contains the name of the server.     Use
 * <code>getServer</code> and <code>setServer</code> to get and set the
 * element.
 * </li>
 * <li>
 * A &ltsvDate&gt element that contains the server's current date     and time
 * in UTC.  Use <code>getServerDate</code> and <code>setServerData</code> to
 * get and set the element.
 * </li>
 * <li>
 * A &ltsvcMenu&gt element that identifies the features supported by     the
 * server.  Use <code>getServiceMenu</code> and <code>setServiceMenu</code> to
 * get and set the element.
 * </li>
 * </ul>
 * 
 *
 * @author $Author: jim $
 * @version $Revision: 1.5 $
 *
 * @see com.verisign.epp.codec.gen.EPPFactory
 */
public class EPPGreeting implements EPPMessage {
	/** Minimum length of the server attribute. */
	public final static short MIN_SERVER_LEN = 3;

	/** Maximum length of the server attribute. */
	public final static short MAX_SERVER_LEN = 64;

	/** XML root tag name for <code>EPPGreeting</code>. */
	static final String ELM_NAME = "greeting";

	/** XML tag name for the <code>server</code> attribute. */
	private final static String ELM_SERVER = "svID";

	/** XML tag name for the <code>serverDate</code> attribute. */
	private final static String ELM_SERVER_DATE = "svDate";

	/** XML tag name for the <code>serviceMenu</code> attribute. */
	private final static String ELM_SERVICE_MENU = "svcMenu";

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPGreeting.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * name of the server.  The server length needs to be &gt;=
	 * <code>MIN_SERVER_LEN</code>     and &lt;= <code>MAX_SERVER_LEN</code>.
	 * This attribute is required to encode the     greeting, and can be set
	 * with <code>EPPGreeting(String, Date, EPPService)</code>     or with
	 * <code>setServer</code>.
	 */
	private String server;

	/**
	 * server's current date and time.  The value is a Java <code>Date</code>
	 * representing     the server's current date and time in UTC.
	 */
	private Date serverDate;

	/**
	 * features supported by the server.  This attribute includes the EPP
	 * protocol versions     supported, the languages supported, and the EPP
	 * services supported.
	 */
	private EPPServiceMenu serviceMenu;

	/**
	 * DataCollectionPolicy supported by the server,which defines the access
	 * and statement supported by the server
	 */
	private EPPDcp dcp;

	/**
	 * Allocates a new <code>EPPGreeting</code> with default attribute values.
	 * The defaults include the following:     <br><br>
	 * 
	 * <ul>
	 * <li>
	 * server is set to <code>null</code>
	 * </li>
	 * <li>
	 * server date is set to now
	 * </li>
	 * <li>
	 * service menu is allocated with the default <code>EPPServiceMenu</code>
	 * constructor
	 * </li>
	 * </ul>
	 * 
	 * <br><code>setServer</code> needs to be called before <code>encode</code>
	 * when     using this constructor.
	 */
	public EPPGreeting() {
		server		    = null;
		serverDate	    = new Date();
		serviceMenu     = new EPPServiceMenu();
	}

	// End EPPGreeting.EPPGreeting()

	/**
	 * Allocates a new <code>EPPGreeting</code> and sets the name and service
	 * menu     instance attributes with the arguments.  The server date is
	 * defaulted to now.
	 *
	 * @param aServer name of the server
	 * @param aServerDate current server date and time
	 * @param aServiceMenu service menu instance associated with the greeting
	 * @param aDcp Dcp instance associated with the greeting
	 */
	public EPPGreeting(
					   String aServer, Date aServerDate,
					   EPPServiceMenu aServiceMenu, EPPDcp aDcp) {
		server		    = aServer;
		serverDate	    = new Date();
		serviceMenu     = aServiceMenu;
		dcp			    = aDcp;
	}

	// End EPPGreeting.EPPGreeting(String,Date,EPPServiceMenu,EPPDcp)

	/**
	 * Allocates a new <code>EPPGreeting</code> and sets all of the instance
	 * attributes     with the arguments.
	 *
	 * @param aServer name of the server
	 * @param aServerDate current server date and time
	 * @param aServiceMenu service menu instance associated with the greeting
	 */
	public EPPGreeting(
					   String aServer, Date aServerDate,
					   EPPServiceMenu aServiceMenu) {
		server		    = aServer;
		serverDate	    = aServerDate;
		serviceMenu     = aServiceMenu;
	}

	// End EPPGreeting.EPPGreeting(String, Date, EPPServiceMenu)

	/**
	 * Gets the associated EPP namespace.  The general EPP namespace is
	 * returned,     which is defined as <code>EPPCodec.NS</code>.
	 *
	 * @return namespace URI
	 */
	public String getNamespace() {
		return EPPCodec.NS;
	}

	// End EPPGreeting.getNamespace()

	/**
	 * Gets the name of the server.  The server length is &gt=
	 * <code>MIN_SERVER_LEN</code>     and &lt= <code>MAX_SERVER_LEN</code>.
	 *
	 * @return server <code>String</code> instance if defined; null otherwise.
	 */
	public String getServer() {
		return server;
	}

	// End EPPGreeting.getServer()

	/**
	 * Sets the name of the server.  The server must be &gt=
	 * <code>MIN_SERVER_LEN</code>     and &lt= <code>MAX_SERVER_LEN</code>.
	 *
	 * @param aServer unique server name.
	 */
	public void setServer(String aServer) {
		server = aServer;
	}

	// End EPPGreeting.setServer(String)

	/**
	 * Gets the server current date and time.
	 *
	 * @return Current server data and time.
	 */
	public Date getServerDate() {
		return serverDate;
	}

	// End EPPGreeting.getServerDate()

	/**
	 * Sets the server current date and time.
	 *
	 * @param aServerDate Current server data and time.
	 */
	public void setServerDate(Date aServerDate) {
		serverDate = aServerDate;
	}

	// End EPPGreeting.setServerDate(Date)

	/**
	 * Gets the service menu associated with the greeting, which defines the
	 * features supported by the server.
	 *
	 * @return service menu instance associated with the greeting.
	 */
	public EPPServiceMenu getServiceMenu() {
		return serviceMenu;
	}

	// End EPPGreeting.getServiceMenu()

	/**
	 * Sets the service menu associated with the greeting, which defines the
	 * features supported by the server.
	 *
	 * @param aServiceMenu service menu instance to associate with the
	 * 		  greeting.
	 */
	public void setServiceMenu(EPPServiceMenu aServiceMenu) {
		serviceMenu = aServiceMenu;
	}

	// End EPPGreeting.setServiceMenu(EPPServiceMenu)

	/**
	 * Sets the DataCollectionPolciy associated with the greeting, which
	 * defines the     access and statement supported by the server.
	 *
	 * @param aDcp service menu instance to associate with the greeting.
	 */
	public void setDcp(EPPDcp aDcp) {
		dcp = aDcp;
	}

	// End EPPGreeting.setServiceMenu(EPPServiceMenu)

	/*
	 *gets the DataCollection policy associated with the server
	             @return  dcp    DataCollectionPolicy instance  associated with the greeting.
	 */
	public EPPDcp getDcp() {
		return dcp;
	}

	/**
	 * encode <code>EPPGreeting</code> into a DOM element tree.  The
	 * &ltgreeting&gt element     is created and the attribute nodes are
	 * appended as children.
	 *
	 * @param aDocument DOCUMENT ME!
	 *
	 * @return &ltgreeting&gt root element tree.
	 *
	 * @exception EPPEncodeException Error encoding the DOM element tree.
	 */
	public Element encode(Document aDocument) throws EPPEncodeException {
		cat.debug("encode: enter");

		try {
			validateState();
		}
		 catch (EPPCodecException e) {
			cat.error("EPPGreeting.encode(): Invalid state");
			throw new EPPEncodeException("EPPGreeting invalid state: " + e);
		}

		Element root = aDocument.createElementNS(EPPCodec.NS, ELM_NAME);

		// Server
		EPPUtil.encodeString(aDocument, root, server, EPPCodec.NS, ELM_SERVER);

		// Server Date
		EPPUtil.encodeTimeInstant(
								  aDocument, root, serverDate, EPPCodec.NS,
								  ELM_SERVER_DATE);

		// Service Menu
		EPPUtil.encodeComp(aDocument, root, serviceMenu);

		if (dcp != null) {
			//dcp
			EPPUtil.encodeComp(aDocument, root, dcp);
		}

		cat.debug("encode: exit");

		return root;
	}

	// End EPPGreeting.encode(Document)

	/**
	 * decode <code>EPPGreeting</code> from a DOM element tree.  The "greeting"
	 * element needs to be the value of the <code>aElement</code> argument.
	 *
	 * @param aElement &ltgreeting&gt root element tree.
	 *
	 * @exception EPPDecodeException Error decoding the DOM element tree.
	 */
	public void decode(Element aElement) throws EPPDecodeException {
		cat.debug("decode: enter");

		// Server Date
		serverDate =
			EPPUtil.decodeTimeInstant(aElement, EPPCodec.NS, ELM_SERVER_DATE);

		// Server
		server     = EPPUtil.decodeString(aElement, EPPCodec.NS, ELM_SERVER);

		// Service Menu
		serviceMenu =
			(EPPServiceMenu) EPPUtil.decodeComp(
												aElement, EPPCodec.NS,
												ELM_SERVICE_MENU,
												EPPServiceMenu.class);

		//Dcp
		dcp = (EPPDcp) EPPUtil.decodeComp(
										  aElement, EPPCodec.NS, EPPDcp.ELM_NAME,
										  EPPDcp.class);

		cat.debug("decode: exit");
	}

	// End EPPGreeting.decode(Element)

	/**
	 * implements a deep <code>EPPGreeting</code> compare.
	 *
	 * @param aObject <code>EPPGreeting</code> instance to compare with
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPGreeting)) {
			cat.error("EPPGreeting.equals(): " + aObject.getClass().getName()
					  + " not EPPGreeting instance");

			return false;
		}

		EPPGreeting theGreeting = (EPPGreeting) aObject;

		// server
		if (!server.equals(theGreeting.server)) {
			cat.error("EPPGreeting.equals(): server not equal");

			return false;
		}

		// serverDate
		if (!serverDate.equals(theGreeting.serverDate)) {
			cat.error("EPPGreeting.equals(): serverDate not equal");

			return false;
		}

		// serviceMenu
		if (!serviceMenu.equals(theGreeting.serviceMenu)) {
			cat.error("EPPGreeting.equals(): serviceMenu not equal");

			return false;
		}

		// dcp
		if (!dcp.equals(theGreeting.dcp)) {
			cat.error("EPPGreeting.equals(): dcp not equal");

			return false;
		}

		return true;
	}

	// End EPPGreeting.equals(Object)

	/**
	 * Clone <code>EPPGreeting</code>.
	 *
	 * @return clone of <code>EPPGreeting</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPGreeting clone = null;

		clone				  = (EPPGreeting) super.clone();
		clone.serviceMenu     = (EPPServiceMenu) serviceMenu.clone();
		clone.dcp			  = (EPPDcp) dcp.clone();

		return clone;
	}

	// End EPPGreeting.clone()

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

	// End EPPGreeting.toString()

	/**
	 * validate the state of the <code>EPPGreeting</code> instance.  A valid
	 * state means that all of the required attributes are set and have valid
	 * values.     If <code>validateState</code> returns without an exception,
	 * the state is valid.     If the state is not valid, an
	 * <code>EPPCodecException</code> is thrown, which contains     a
	 * description of the attribute error.
	 *
	 * @throws EPPCodecException State error with a description of the
	 * 		   attribute error.
	 */
	private void validateState() throws EPPCodecException {
		// Server
		if (server == null) {
			cat.error("EPPGreeting.validateState(): server is null");
			throw new EPPCodecException("Required attribute \"server\" is null");
		}

		if (
			(server.length() < MIN_SERVER_LEN)
				|| (server.length() > MAX_SERVER_LEN)) {
			cat.error("EPPGreeting.validateState(): Invalid server attribute length of "
					  + server.length());
			throw new EPPCodecException("Invalid server attribute length of "
										+ server.length());
		}
	}

	// End EPPGreeting.validateState()
}
