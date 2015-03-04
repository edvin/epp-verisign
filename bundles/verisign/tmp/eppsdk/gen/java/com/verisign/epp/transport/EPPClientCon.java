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
package com.verisign.epp.transport;

import com.verisign.epp.transport.EPPConException;
import com.verisign.epp.transport.client.EPPSSLContext;


/**
 * Interface implmented by all client connection classes.  The life cycle of
 * the connection is defined by this interface, including initializing/opening
 * the connection, getting input/ouput stream for interacting with the
 * connection, and closing the connection.
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public interface EPPClientCon {
	/**
	 * Initializes the client connection.  The connection will be opened and
	 * the input/output streams will be set.
	 *
	 * @exception EPPConException On error
	 */
	public void initialize() throws EPPConException;

	/**
	 * Initializes the client connection with a specific host and port.  
	 * The connection will be opened and
	 * the input/output streams will be set.
	 *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aSSLContext Optional specific SSL context to use
	 * 
	 * @exception EPPConException On error
	 */
	public void initialize(String aHostName, int aPortNumber, EPPSSLContext aSSLContext) throws EPPConException;
	
	/**
	 * Initializes the client connection with a specific host and port.  
	 * The connection will be opened and
	 * the input/output streams will be set.
     *
	 * @param aHostName Host name or IP address of host to connect to
	 * @param aPortNumber Port number to connect to
	 * @param aClientHostName Host name or IP address to connect from
 	 * @param aSSLContext Optional specific SSL context to use
	 *
	 * @exception EPPConException On error
	 */
	public void initialize(String aHostName, int aPortNumber, String aClientHostName, EPPSSLContext aSSLContext) throws EPPConException;
	
	
	/**
	 * Closes the client connection.
	 *
	 * @exception EPPConException On error
	 */
	public void close() throws EPPConException;

	/**
	 * Gets the output stream associated with the active connection.
	 *
	 * @return output stream associated with the active connection
	 *
	 * @exception EPPConException Output stream is <code>null</code>, meaning
	 * 			  that the connection is not active.
	 */
	public java.io.OutputStream getOutputStream() throws EPPConException;

	/**
	 * Gets the input stream associated with the active connection.
	 *
	 * @return input stream associated with the active connection
	 *
	 * @exception EPPConException Input stream is <code>null</code>, meaning
	 * 			  that the connection is not active.
	 */
	public java.io.InputStream getInputStream() throws EPPConException;
}
