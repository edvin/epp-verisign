/***********************************************************
Copyright (C) 2004 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
***********************************************************/

/**
 * The information in this document is proprietary to VeriSign and the VeriSign
 * Registry Business. It may not be used, reproduced or disclosed without the
 * written approval of the General Manager of VeriSign Global Registry
 * Services. PRIVILEDGED AND CONFIDENTIAL VERISIGN PROPRIETARY INFORMATION
 * REGISTRY SENSITIVE INFORMATION Copyright (c) 2002 VeriSign, Inc.  All
 * rights reserved.
 */
package com.verisign.epp.codec.persreg;

import org.w3c.dom.Document;

// W3C Imports
import org.w3c.dom.Element;

//----------------------------------------------
// Imports
//----------------------------------------------
// SDK Imports
import com.verisign.epp.codec.gen.*;


/**
 * Personal Registration &ltcreData&gt extension element to a EPP Create
 * Response.  The bundled rate flag indicates if the bundled rate applies to
 * the &ltcreate&gt EPP Command. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.1.1.1 $
 */
public class EPPPersRegCreateData extends EPPPersRegGenData {
	/** XML root tag for <code>EPPPersRegCreateData</code>. */
	public static final String ELM_NAME = "persReg:creData";

	/**
	 * Default constructor.  Bundled flag defaults to <code>false</code>.
	 */
	public EPPPersRegCreateData() {
		// Do nothing
	}

	// End EPPPersRegCreateData.EPPPersRegCreateData()

	/**
	 * Constructor that sets the bundled flag.
	 *
	 * @param aIsBundledRate Does the bundled rate apply?
	 */
	public EPPPersRegCreateData(boolean aIsBundledRate) {
		super(aIsBundledRate);
	}

	// End EPPPersRegCreateData.EPPPersRegCreateData(boolean)

	/**
	 * Compare an instance of <code>EPPPersRegCreateData</code> with this
	 * instance.
	 *
	 * @param aObject Object to compare with.
	 *
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 */
	public boolean equals(Object aObject) {
		if (!(aObject instanceof EPPPersRegCreateData)) {
			return false;
		}

		return super.equals(aObject);
	}

	// End EPPPersRegCreateData.equals(Object)

	/**
	 * clone an <code>EPPCodecComponent</code>.
	 *
	 * @return clone of concrete <code>EPPPersRegCreateData</code>
	 *
	 * @exception CloneNotSupportedException standard Object.clone exception
	 */
	public Object clone() throws CloneNotSupportedException {
		EPPPersRegCreateData clone = (EPPPersRegCreateData) super.clone();

		return clone;
	}

	// End EPPPersRegCreateData.clone()

	/**
	 * Gets the root element name.
	 *
	 * @return "persReg:creData"
	 */
	protected String getRootElm() {
		return ELM_NAME;
	}

	// End EPPPersRegCreateData.getRootElm()
}


// End class EPPPersRegCreateData
