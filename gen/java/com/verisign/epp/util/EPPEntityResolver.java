/*===========================================================================

* Copyright (C) 2000 VeriSign Corporation

*

* This library is free software; you can redistribute it and/or

* modify it under the terms of the GNU Lesser General Public

* License as published by the Free Software Foundation; either

* version 2.1 of the License, or (at your option) any later version.

*

* This library is distributed in the hope that it will be useful,

* but WITHOUT ANY WARRANTY; without even the implied warranty of

* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU

* Lesser General Public License for more details.

*

* You should have received a copy of the GNU Lesser General Public

* License along with this library; if not, write to the Free Software

* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*

* VeriSign Corporation.

* 505 Huntmar Park Dr.

* Herndon, VA 20170

* ===========================================================================

* The EPP, APIs and Software are provided "as-is" and without any warranty

* of any kind. VeriSign Corporation EXPRESSLY DISCLAIMS ALL WARRANTIES

* AND/OR CONDITIONS, EXPRESS OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,

* THE IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY OR SATISFACTORY

* QUALITY AND FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD

* PARTY RIGHTS. VeriSign Corporation DOES NOT WARRANT THAT THE FUNCTIONS

* CONTAINED IN THE EPP, APIs OR SOFTWARE WILL MEET REGISTRAR'S REQUIREMENTS,

* OR THAT THE OPERATION OF THE EPP, APIs OR SOFTWARE WILL BE UNINTERRUPTED

* OR ERROR-FREE,OR THAT DEFECTS IN THE EPP, APIs OR SOFTWARE WILL BE CORRECTED.

* FURTHERMORE, VeriSign Corporation DOES NOT WARRANT NOR MAKE ANY

* REPRESENTATIONS REGARDING THE USE OR THE RESULTS OF THE EPP, APIs, SOFTWARE

* OR RELATED DOCUMENTATION IN TERMS OF THEIR CORRECTNESS, ACCURACY,

* RELIABILITY, OR OTHERWISE.  SHOULD THE EPP, APIs OR SOFTWARE PROVE DEFECTIVE,

* REGISTRAR ASSUMES THE ENTIRE COST OF ALL NECESSARY SERVICING, REPAIR OR

* CORRECTION.

* ========================================================================

*/



package com.verisign.epp.util;



// JDK Imports

import java.util.HashMap;

import java.io.IOException;

import java.io.InputStream;

import java.io.StringReader;
import java.io.ByteArrayInputStream;


// XML Imports

import org.xml.sax.EntityResolver;

import org.xml.sax.InputSource;

import org.xml.sax.SAXException;



// Log4j Imports

import org.apache.log4j.Logger;

import org.apache.log4j.Priority;



/**

 * EPP Entity Resolver used to cache and load EPP XML

 * schemas from the classpath.  The first time an

 * entity is encountered, <code>EPPEntityResolver</code>

 * will attempt to load it from the classpath and

 * than store it in an internal cache.  The cache

 * is a class attribute, so all instances of

 * <code>EPPEntityResolver</code> reference the same

 * cache.

 */

public class EPPEntityResolver implements EntityResolver {



	/**

	 * Implimentation of the <code>EntityResolver.resolveEntity</code>

	 * method, which is called before the parser loads an external

	 * entity.  This method will implement the loading of the

	 * external entity from the classpath and cache entities in

	 * an internal cache.  Additionally, if a custom <code>ClassLoader</code>

	 * was used to load <code>EPPEntityResolver</code>, than the

	 * custom <code>ClassLoader</code> will be used after

	 * <code>ClassLoader.getSystemResourceAsStream</code>.

	 *

	 * @param aPublicId Ignored

	 * @param aSystemId Name of entity to load

	 *

	 * @return <code>InputSource</code> instance associated with <code>aSystemId</code>

	 *

	 * @exception SAXException Currently not thrown, but part of the interface

	 * @exception IOException Error loading the entity.

	 */

	public InputSource resolveEntity(String aPublicId, String aSystemId)

		throws SAXException, IOException {



		_cat.debug("resolveEntity([" + aPublicId + "], [" + aSystemId + "]: enter");



		String theEntityStr = null;



		// Trim schema file name

		if (aSystemId != null) {

			aSystemId = aSystemId.trim();

		}





		// Get entity from cache?

		theEntityStr = (String) _cache.get(aSystemId);



		// entity not cached?

		if (theEntityStr == null) {



			// double-check lock

			synchronized (_cache) {



				// Get entity from cache?

				theEntityStr = (String) _cache.get(aSystemId);



				// entity not cached?

				if (theEntityStr == null) {



					_cat.debug(aSystemId + " not in cache, attempting to open");



					InputStream theEntity = ClassLoader.getSystemResourceAsStream("schemas/" + aSystemId);



					// OK, if the system class loader couldn't do it... try this class' loader?

					if (theEntity == null) {

					  theEntity = EPPEntityResolver.class.getClassLoader().getResourceAsStream("schemas/" + aSystemId);

					}



					// Unable to load the entity?

					if (theEntity == null) {

						_cat.error(aSystemId + " could not be opened.  Check the classpath");

						throw new SAXException(aSystemId + " could not be opened.  Check the classpath");

					}



					_cat.debug(aSystemId + " opened, adding to cache");



					// Add the entity to the cache

					int currChar;

					StringBuffer entityBuffer = new StringBuffer();

					while ((currChar = theEntity.read()) != -1) {

						entityBuffer.append((char) currChar);

					}

					theEntityStr = entityBuffer.toString();



					//_cat.debug(aSystemId + " entity = " + theEntityStr);

					_cache.put(aSystemId, theEntityStr);



					theEntity.close();



				}

				else {
					_cat.debug(aSystemId + "synchronized() in cache");
				}
			} // end synchronize (_cache)
		}
		else {
			_cat.debug(aSystemId + " in cache");
		}

		_cat.debug("resolveEntity(" + aPublicId + ", " + aSystemId + ": return");

		InputSource retSource = new InputSource(new StringReader(theEntityStr));
		retSource.setPublicId(aPublicId);
		retSource.setSystemId(aSystemId);

		return retSource;
	} // End EPPEntityResolver.resolveEntity(String, String)







    /**

     * Log4j category for logging

     */

    private static Logger _cat = Logger.getLogger(EPPEntityResolver.class.getName(),

													    EPPCatFactory.getInstance().getFactory());



	/**

	 * Cache of entity system ids (key) and entity byte arrays (value).

	 */

	private static HashMap _cache = new HashMap();



} // End class EPPEntityResolver

