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

/**
 * Represents an EPP object that can be sent or received by an EPP Client or
 * EPP Server.     There are three concrete <code>EPPMessage</code> classes,
 * which include:     <br>
 * 
 * <ul>
 * <li>
 * <code>EPPCommand</code>     - EPP Command encoded by client and decoded by
 * server
 * </li>
 * <li>
 * <code>EPPResponse</code>    - EPP Response encoded by server and decoded by
 * client
 * </li>
 * <li>
 * <code>EPPGreeting</code>     - EPP Greeting encoded by server and decoded by
 * client
 * </li>
 * </ul>
 * 
 * <br><br>
 * An <code>EPPMessage</code> can be encoded and decoded by
 * <code>EPPCodec</code> using the <code>EPPMessage.encode(Document)</code>
 * and <code>EPPMessage.decode(Document)</code> methods.      An
 * <code>EPPMessage</code> is also <code>Serializable</code> and is associated
 * with     an EPP XML namespace.  For example, the EPP XML namespace
 * associated with the general     <code>EPPMessages</code> is
 * "urn:iana:xmlns:epp".      <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see com.verisign.epp.codec.gen.EPPCodec
 * @see com.verisign.epp.codec.gen.EPPGreeting
 * @see com.verisign.epp.codec.gen.EPPCommand
 * @see com.verisign.epp.codec.gen.EPPResponse
 */
public interface EPPMessage extends EPPCodecComponent {
	/**
	 * Gets the EPP Namespace URI associated with the <code>EPPMessage</code>.
	 * This will be either one of the following:     <br>
	 * 
	 * <ul>
	 * <li>
	 * EPP namespace - <code>EPPCodec.NS</code>
	 * </li>
	 * <li>
	 * EPP Command Mapping namespace - <code><i>mapping</i>MapFactory.NS</code>
	 * </li>
	 * </ul>
	 * 
	 * <br><br>
	 *
	 * @return Namespace URI associated with the EPPMessage.
	 */
	String getNamespace();
}
