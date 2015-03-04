/***********************************************************
Copyright (C) 2011 VeriSign, Inc.

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
 * Exception used to identify that a duplicate extension was found 
 * in the list of extensions.  
 */
public class EPPDuplicateExtensionException extends EPPCodecException {
	
	// Duplicate extension class found
	EPPCodecComponent extension;
		
	
	/**
	 * Constructor for EPPComponentNotFoundException that takes an info string.
	 *
	 * @param aExtensionClass Duplicate extension class found 
	 */
	public EPPDuplicateExtensionException(EPPCodecComponent aExtensionClass) {
		super("Duplicate extension found: " + aExtensionClass.getClass().getName());
		this.extension = aExtensionClass;
	}

	
	/**
	 * Get the duplicate extension <code>Class</code> found.
	 * 
	 * @return The duplicate <code>EPPCodecComponent</code> extension found
	 */
	public EPPCodecComponent getExtension() {
		return this.extension;
	}
	
}
