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


// Log4j Imports
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;

// Xerces imports
import org.apache.xerces.xni.parser.XMLInputSource;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

// XML imports
import org.xml.sax.SAXException;

import java.io.File;

// Java imports
import java.io.IOException;
import java.io.InputStream;


/**
 * EPPSchemaCachingEntityResolver is the entity resolver that goes with
 * EPPSchemaCachingParser.  It supports caching schemas for the parser
 * instance and looking up schema instances in the Classpath
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
public class EPPSchemaCachingEntityResolver implements XMLEntityResolver,
													   EntityResolver {
	/** Count of how many times resolveEntity() has been called */
	private static int count;

	/** Log4j category for logging */
	private static Logger cat =
		Logger.getLogger(
						 EPPSchemaCachingEntityResolver.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Reference to the Schema cache object that is using this entity resolver
	 * instance
	 */
	private EPPSchemaCacher schemaCache;

	/**
	 * Create a new instance of EPPSchemaCachingEntityResolver.  Resolves XML
	 * Schemas by looking them up in the classpath.  As a param takes an
	 * instance of EPPSchemaCacher to call back on to cache a schema once it
	 * loads it.
	 *
	 * @param aSchemaCache An instance of EPPSchemaCacher to call back on to
	 * 		  cache a schema once it loads it.
	 */
	public EPPSchemaCachingEntityResolver(EPPSchemaCacher aSchemaCache) {
		cat.debug("EPPSchemaCachingEntityResolver(EPPSchemaCacher) enter");
		schemaCache = aSchemaCache;
		cat.debug("EPPSchemaCachingEntityResolver(EPPSchemaCacher) exit");
	}

	/**
	 * Resolves the entity passed in when parsing the instance document.  Will
	 * try to find the schema from the classpath.
	 *
	 * @param aPublicId DOCUMENT ME!
	 * @param aSystemId DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 *
	 * @throws SAXException DOCUMENT ME!
	 * @throws IOException DOCUMENT ME!
	 * @throws XNIException DOCUMENT ME!
	 */
	public InputSource resolveEntity(String aPublicId, String aSystemId)
							  throws SAXException, IOException {
		cat.debug("resolveEntity(String aPublicId, String aSystemId) enter");

		cat.info("Entity not found in grammar pool - public Id: " + aPublicId
				 + " systemId: " + aSystemId);

		File   tmpFile   = new File(aSystemId);
		String literalId = tmpFile.getName();

		// lookup the file name in this classes's classpath under "schemas"
		InputStream schemaStream =
			getClass().getClassLoader().getResourceAsStream("schemas/"
															+ literalId);

		// Use this stream to add to the cache
		try {
			schemaCache.addSchemaToCache(new XMLInputSource(
															literalId, literalId,
															aSystemId,
															schemaStream, null));
		}
		 catch (EPPParserException e) {
			cat.error("EPPParserException caught while trying to add schema to cache");
			throw new XNIException(e);
		}

		// get another instance of InputStream that has the schema.  This will
		// be returned for the initial parse episode
		InputStream retStream =
			getClass().getClassLoader().getResourceAsStream("schemas/"
															+ literalId);

		// If the resource wasn't found then throw an exception
		if (retStream == null) {
			throw new SAXException("Couldn't locate resource: " + literalId);
		}

		InputSource retSource = new InputSource(retStream);

		cat.debug("resolveEntity(aPublicId, aSystemId) exit");

		// return the schema as an InputSource for this go parsing episode.  The
		// next time this schema is referenced it will be found in the cache.
		return retSource;
	}

	/**
	 * Implemented because of the XNI interface.  The entity resolver tries to
	 * find the resource in the classpath.  This interface imlementation
	 * allows this resolver to be used as either the standard EntityResolver
	 * or one that can be used by the Xerces XNI components.  If the resource
	 * can't be found an exception is thrown
	 *
	 * @param aResourceIdentifier The resource identifier that should be
	 * 		  resolved.
	 *
	 * @return The XMLInputSource of the resource if found.  Never returns
	 * 		   null.
	 *
	 * @throws XNIException
	 * @throws IOException
	 */
	public XMLInputSource resolveEntity(XMLResourceIdentifier aResourceIdentifier)
								 throws XNIException, IOException {
		cat.debug("resolvEntity(XMLResourceIdentifier) enter");

		String publicId = aResourceIdentifier.getPublicId();
		String systemId = aResourceIdentifier.getBaseSystemId();

		String literalId = aResourceIdentifier.getLiteralSystemId();

		cat.info("Entity not found in grammar pool - public Id: " + publicId
				 + " systemId: " + systemId + " literalId: " + literalId);

		// lookup the file name in this classes's classpath under "schemas"
		InputStream schemaStream =
			getClass().getClassLoader().getResourceAsStream("schemas/"
															+ literalId);

		if (schemaStream == null) {
			throw new XNIException("Couldn't locate resource: " + literalId);
		}

		// Use this stream to add to the cache
		try {
			schemaCache.addSchemaToCache(new XMLInputSource(
															literalId, literalId,
															systemId,
															schemaStream, null));
		}
		 catch (EPPParserException e) {
			cat.error("EPPParserException caught while trying to add schema to cache");
			throw new XNIException(e);
		}

		// get another instance of InputStream that has the schema.  This will
		// be returned for the initial parse episode
		InputStream retStream =
			getClass().getClassLoader().getResourceAsStream("schemas/"
															+ literalId);

		XMLInputSource retSource =
			new XMLInputSource(literalId, literalId, systemId, retStream, null);
		cat.debug("resolveEntity(aPublicId, aSystemId) exit");

		// return the schema as an InputSource for this go parsing episode.  The
		// next time this schema is referenced it will be found in the cache.
		return retSource;
	}
}
