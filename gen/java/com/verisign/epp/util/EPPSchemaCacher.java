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

package com.verisign.epp.util;

import org.apache.xerces.xni.parser.XMLInputSource;

import java.io.InputStream;


/**
 * Iterface for classes that support caching schemas.
 * 
 * <p>
 * Title: EPP SDK
 * </p>
 * 
 * <p>
 * Description: EPP SDK for 1.0 Spec
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: VeriSign
 * </p>
 *
 * @author clloyd
 * @version 1.0
 */
public interface EPPSchemaCacher {
	/**
	 * Lock or unlock the schema cache.  If locked then no more schemas can be
	 * added.
	 *
	 * @param aBoolean True locks, False unlocks.
	 */
	public void setLockSchemaCache(boolean aBoolean);

	/**
	 * Adds a schema to the cache.  The schema instance should exist as an
	 * XMLInputSource and the EPPSchemaCacher instance should not be locked
	 *
	 * @param aSchema The schema to cache.
	 *
	 * @throws EPPParserException Thrown if the schema can't be cached.
	 */
	public void addSchemaToCache(XMLInputSource aSchema)
						  throws EPPParserException;
}
