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

import java.io.InputStream;

// Core Java Imports
import java.io.OutputStream;

// EPP imports
import com.verisign.epp.codec.gen.EPPMessage;


/**
 * The <code>EPPSerialAssembler</code> class provides an implementation of
 * EPPAssembler that can assemble/disassemble <code>EPPMessage</code>s and
 * <code>EPPEventResponse</code>s from standard java Input and Outputstreams
 * using java serialization.     <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 *
 * @see EPPAssembler
 */
public class EPPSerialAssembler implements EPPAssembler {
	/**
	 * Construct and instance of an <code>EPPSerialAssembler</code>
	 */
	public EPPSerialAssembler() {
	}

	/**
	 * Takes an <code> EPPEventResponse </code> and serializes it to an
	 * <code>OutputStream </code> using java serialization.
	 *
	 * @param aMessage The response that will be serialized
	 * @param aOutputStream The OutputStream that the response will be
	 * 		  serialized to.
	 * @param aData DOCUMENT ME!
	 *
	 * @exception EPPAssemblerException Error serializing the
	 * 			  <code>EPPEventResponse</code>
	 */
	public void toStream(
						 EPPEventResponse aMessage, OutputStream aOutputStream,
						 Object aData) throws EPPAssemblerException {
	}

	/**
	 * Takes an <code>InputStream</code> and uses java serialization to create
	 * an <code>EPPEvent</code>
	 *
	 * @param aStream The InputStream to read data from.
	 * @param aData DOCUMENT ME!
	 *
	 * @return EPPEvent The <code> EPPEvent </code> that is created from the
	 * 		   InputStream
	 *
	 * @exception EPPAssemblerException Error creating the <code> EPPEvent
	 * 			  </code>
	 */
	public EPPEvent toEvent(InputStream aStream, Object aData)
					 throws EPPAssemblerException {
		return null;
	}
}
