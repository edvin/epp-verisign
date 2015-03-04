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

// PoolMan Imports
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.codestudio.util.GenericPool;
import com.codestudio.util.GenericPoolManager;
import com.codestudio.util.GenericPoolMetaData;
import com.verisign.epp.codec.gen.EPPCodec;
import com.verisign.epp.codec.gen.EPPComponentNotFoundException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPMessage;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.serverstub.SessionData;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.EPPTransformer;
import com.verisign.epp.util.EPPXMLStream;

/**
 * The <code>EPPXMLAssembler</code> class provides an implementation of
 * EPPAssembler that can assemble/disassemble <code>EPPMessage</code>s and
 * <code>EPPEventResponse</code>s from java Input and Outputstreams that
 * contain streamed XML. <br>
 * <br>
 * 
 * @author $Author: jim $
 * 
 * @see EPPAssembler
 */
public class EPPXMLAssembler implements EPPAssembler {
	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPXMLAssembler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** Has the XML parser been initialized? */
	private static boolean _parserInitialized = false;

	/**
	 * An EPPCodec is delegated to to do the real work.
	 * <code>EPPXMLAssemler</code> just wraps it to provide the EPPAssembler
	 * interface.
	 */
	private EPPCodec codec;

	/**
	 * Construct and instance of an <code>EPPXMLAssembler</code>
	 */
	public EPPXMLAssembler() {
		codec = EPPCodec.getInstance();

		initParserPool();
	}

	/**
	 * Takes an <code> EPPEventResponse </code> and serializes it to an
	 * <code>OutputStream </code> in XML Format.
	 * 
	 * @param aResponse
	 *            The response that will be serialized
	 * @param aOutputStream
	 *            The OutputStream that the response will be serialized to.
	 * @param aData
	 *            DOCUMENT ME!
	 * 
	 * @exception EPPAssemblerException
	 *                Error serializing the <code>EPPEventResponse</code>
	 */
	public void toStream(EPPEventResponse aResponse,
			OutputStream aOutputStream, Object aData)
			throws EPPAssemblerException {
		cat.debug("toStream(EPPEventResponse, OutputStream): Enter");

		try {
			/**
			 * First, get the message and convert it to a DOM Document using the
			 * codec
			 */
			EPPMessage response = aResponse.getResponse();
			Document domDocument = codec.encode(response);

			/** Now, serialize the DOM Document through the output stream */
			EPPXMLStream xmlStream = new EPPXMLStream(
					EPPSchemaCachingParser.POOL,
					EPPTransformer.POOL);
			xmlStream.write(domDocument, aOutputStream);
		}
		catch (EPPEncodeException e) {
			cat.error("toStream(EPPEventResponse, OutputStream)", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.MISSINGPARAMETER);
		}
		catch (EPPException e) {
			cat.error("toStream(EPPEventResponse, OutputStream)", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.FATAL);
		}

		cat.debug("toStream(EPPEventResponse, OutputStream): Return");
	}

	/**
	 * Takes an <code>InputStream</code> and reads XML from it to create an
	 * <code>EPPEvent</code>
	 * 
	 * @param aStream
	 *            The InputStream to read data from.
	 * @param aData
	 *            DOCUMENT ME!
	 * 
	 * @return EPPEvent The <code> EPPEvent </code> that is created from the
	 *         InputStream
	 * 
	 * @exception EPPAssemblerException
	 *                Error creating the <code> EPPEvent
	 * 			  </code>
	 */
	public EPPEvent toEvent(InputStream aStream, Object aData)
			throws EPPAssemblerException {
		cat.debug("toEvent(InputStream): Enter");

		EPPMessage message = null;

		/**
		 * First, take an XML input stream and convert it to a DOM Document
		 */
		try {
			/** Declare an instance of the EPPXMLStream class */
			EPPXMLStream xmlStream = new EPPXMLStream(
					EPPSchemaCachingParser.POOL,
					EPPTransformer.POOL);

			/**
			 * Take the DOM Document and convert it to an EPPMessage using the
			 * EPPCodec
			 */
			byte[] thePacket = xmlStream.readPacket(aStream);
			Document domDocument = xmlStream.decodePacket(thePacket);
			message = codec.decode(domDocument);
			
			// Store the original XML packet in the session data for future use.
			if ((aData != null) && (aData instanceof SessionData)) {
				SessionData sessionData = (SessionData) aData;
				sessionData.setAttribute("PACKET", thePacket);
			}
		}
		catch (EPPComponentNotFoundException e) {
			cat.error("toEvent(InputStream):", e);
			
			switch (e.getKind()) {
				case EPPComponentNotFoundException.COMMAND:
					throw new EPPAssemblerException(e.getMessage(),
							EPPAssemblerException.COMMANDNOTFOUND);
				case EPPComponentNotFoundException.EXTENSION:
					throw new EPPAssemblerException(e.getMessage(),
							EPPAssemblerException.EXTENSIONNOTFOUND);
				case EPPComponentNotFoundException.RESPONSE:
					throw new EPPAssemblerException(e.getMessage(),
							EPPAssemblerException.RESPONSENOTFOUND);
			}
			
		}
		catch (EPPDecodeException e) {
			cat.error("toEvent(InputStream):", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.MISSINGPARAMETER);
		}
		catch (EPPAssemblerException e) {
			cat.error("toEvent(InputStream):", e);
			throw e;
		}
		catch (EPPException e) {
			cat.error("toEvent(InputStream):", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.XML);
		}
		catch (InterruptedIOException e) {
			cat.debug("toEvent(InputStream):", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.INTRUPTEDIO);
		}
		catch (IOException e) {
			cat.error("toEvent(InputStream):", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.CLOSECON);
		}

		cat.debug("toEvent(InputStream): Return");

		return new EPPEvent(message);
	}

	/**
	 * Initialize the XML parser pool, with the name EPPXMLParser.POOL and with
	 * the "com.verisign.epp.util.EPPXMLParser" as the object type. The
	 * remaining configuration settings are retrieved from the
	 * EPPEnv.getServerParser methods. If there is any error initializing the
	 * pool, and error diagnostic is logged, and the program with stop with a
	 * call to <code>System.exit(1)</code>, since this represents a fatal
	 * error. The <code>EPPEnv</code> settings referenced include:<br>
	 * <br>
	 * 
	 * <ul>
	 * <li> getServerParserInitObjs() </li>
	 * <li> getServerParserMinSize </li>
	 * <li> getServerParserMaxSize() </li>
	 * <li> getServerParserMaxSoft() </li>
	 * <li> getServerParserObjTimeout() </li>
	 * <li> getServerParserUserTimeout() </li>
	 * <li> getServerParserSkimmerFreq() </li>
	 * <li> getServerParserShrinkBy() </li>
	 * <li> getServerParserLogFile() </li>
	 * <li> getServerParserDebug() </li>
	 * </ul>
	 */
	private void initParserPool() {
		// Pool does not exist?
		if (!_parserInitialized) {
			// Create parser pool
			GenericPoolMetaData parserMeta = new GenericPoolMetaData();

			parserMeta.setName( EPPSchemaCachingParser.POOL );
			parserMeta.setObjectType( "com.verisign.epp.util.EPPSchemaCachingParser" );
			parserMeta.setInitialObjects( EPPEnv.getServerParserInitObjs() );
			parserMeta.setMinimumSize( EPPEnv.getServerParserMinSize() );
			parserMeta.setMaximumSize( EPPEnv.getServerParserMaxSize() );
			parserMeta.setMaximumSoft( EPPEnv.getServerParserMaxSoft() );
			parserMeta.setObjectTimeout( EPPEnv.getServerParserObjTimeout() );
			parserMeta.setUserTimeout( EPPEnv.getServerParserUserTimeout() );
			parserMeta.setSkimmerFrequency( EPPEnv.getServerParserSkimmerFreq() );
			parserMeta.setShrinkBy( EPPEnv.getServerParserShrinkBy() );
			parserMeta.setLogFile( EPPEnv.getServerParserLogFile() );
			parserMeta.setDebugging( EPPEnv.getServerParserDebug() );

			GenericPool parserPool = new GenericPool( parserMeta );
			GenericPoolManager.getInstance().addPool( EPPSchemaCachingParser.POOL,
					parserPool );
			
			// Create transformer pool
			GenericPoolMetaData transformerMeta = new GenericPoolMetaData();

			transformerMeta.setName( EPPTransformer.POOL );
			transformerMeta.setObjectType( "com.verisign.epp.util.EPPTransformer" );
			transformerMeta.setInitialObjects( EPPEnv.getServerParserInitObjs() );
			transformerMeta.setMinimumSize( EPPEnv.getServerParserMinSize() );
			transformerMeta.setMaximumSize( EPPEnv.getServerParserMaxSize() );
			transformerMeta.setMaximumSoft( EPPEnv.getServerParserMaxSoft() );
			transformerMeta.setObjectTimeout( EPPEnv.getServerParserObjTimeout() );
			transformerMeta.setUserTimeout( EPPEnv.getServerParserUserTimeout() );
			transformerMeta.setSkimmerFrequency( EPPEnv.getServerParserSkimmerFreq() );
			transformerMeta.setShrinkBy( EPPEnv.getServerParserShrinkBy() );
			transformerMeta.setLogFile( EPPEnv.getServerParserLogFile() );
			transformerMeta.setDebugging( EPPEnv.getServerParserDebug() );

			GenericPool transformerPool = new GenericPool( transformerMeta );
			GenericPoolManager.getInstance().addPool( EPPTransformer.POOL,
					transformerPool );

			_parserInitialized = true;
		}
	}

	// End EPPXMLAssembler.initParserPool()
}

// End class EPPXMLAssembler
