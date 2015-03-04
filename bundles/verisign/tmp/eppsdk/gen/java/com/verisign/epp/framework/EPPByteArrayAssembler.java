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

package com.verisign.epp.framework;

import com.verisign.epp.framework.EPPAssemblerException;
import com.verisign.epp.framework.EPPEvent;
import com.verisign.epp.framework.EPPEventResponse;

/**
 * The EPPByteArrayAssembler interface defines an interface for serializing
 * EPPEvent objects and EPPEventResponse objects to and from byte arrays. <br>
 * <br>
 * 
 * @author Srikanth Veeramachaneni
 * @version 1.0 Dec 04, 2006
 * @see com.verisign.epp.framework.EPPXMLByteArrayAssembler
 * @see com.verisign.epp.framework.EPPEventResponse
 * @see com.verisign.epp.framework.EPPEvent
 * @see com.verisign.epp.framework.EPPAssemblerException
 */
public interface EPPByteArrayAssembler {
	/**
	 * Takes an <code> EPPEventResponse </code> and serializes the response as a
	 * byte array.
	 * 
	 * @param aResponse
	 *        The response that will be serialized
	 * @param aData
	 *        A data object which can be used to store context information.
	 * @exception EPPAssemblerException
	 *            Error converting the <code>EPPEventResponse</code> to a byte
	 *            array.
	 */
	public byte[] encode ( EPPEventResponse aResponse, Object aData )
			throws EPPAssemblerException;


	/**
	 * Takes an <code> byte </code> array and creates a <code> EPPEvent
	 * </code>
	 * 
	 * @param aInputBytes
	 *        The byte array containing the request data.
	 * @param aData
	 *        A data object which can be used to store context information.
	 * @return EPPEvent The <code> EPPEvent </code> that is created from the input
	 *         bytes.
	 * @exception EPPAssemblerException
	 *            Error creating the <code> EPPEvent
	 * 			  </code>
	 */
	public EPPEvent decode ( byte[] aInputBytes, Object aData )
			throws EPPAssemblerException;
}
