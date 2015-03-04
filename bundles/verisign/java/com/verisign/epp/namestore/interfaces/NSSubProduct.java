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
package com.verisign.epp.namestore.interfaces;

/**
 * NameStore sub-product id constants used with either 
 * <code>NSDomain.setSubProductID(String)</code>, <code>NSHost.setSubProductID(String)</code>, or <code>NSContact.setSubProductID(String).
 * This list will be changed as NameStore offerings change.     
 */
public class NSSubProduct {
	
	/**
	 * Constant when targeting the BZ registry/sub-product.
	 */
	public static final String BZ = "dotBZ";
	
	/**
	 * Constant when targeting the CC registry/sub-product.
	 */
	public static final String CC = "dotCC";

	/**
	 * Constant when targeting the TV registry/sub-product.
	 */
	public static final String TV = "dotTV";

	/**
	 * Constant when targeting the COM registry/sub-product.
	 */
	public static final String COM = "dotCOM";

	/**
	 * Constant when targeting the NET registry/sub-product.
	 */
	public static final String NET = "dotNET";
			
	/**
	 * Constant when targeting the EDU registry/sub-product.
	 */
	public static final String EDU = "dotEDU";
	
	/**
	 * Constant when targeting the JOBS registry/sub-product.
	 */
	public static final String JOBS = "dotJOBS";

	/**
	 * Constant when targeting the NAME registry/sub-product.  
	 * NAME does not use the NameStore Extension, so it 
	 * is defined as <code>null</code>.  A <code>null</code> 
	 * sub-product can be used when targeting a registry 
	 * that does not support sub-product routing.
	 */
	public static final String NAME = null;
	
} // End class NSSubProduct
