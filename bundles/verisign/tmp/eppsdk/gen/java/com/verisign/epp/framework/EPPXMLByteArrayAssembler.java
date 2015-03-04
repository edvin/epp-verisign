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

import java.io.IOException;
import java.io.InterruptedIOException;

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
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPEnv;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.EPPTransformer;
import com.verisign.epp.util.EPPXMLByteArray;

/**
 * The <code>EPPXMLByteArrayAssembler</code> class provides an implementation of
 * <code>EPPByteArrayAssembler</code> that can assemble/disassemble
 * <code>EPPMessage</code>s and <code>EPPEventResponse</code>s to and from
 * <code>byte</code> arrays that contain the streamed XML. <br>
 * <br>
 * 
 * @author Srikanth Veeramachaneni
 * @version 1.0 Dec 04, 2006
 * @see EPPByteArrayAssembler
 */
public class EPPXMLByteArrayAssembler implements EPPByteArrayAssembler {
	/** Log4j category for logging */
	private static Logger LOG = Logger.getLogger(EPPXMLByteArrayAssembler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/** Has the XML parser been initialized? */
	private static boolean _parserInitialized = false;

	/**
	 * Converts an integer to a byte array of size 4 with first array element
	 * containing the first 8 bits of the integer, the second array element
	 * containing the 9th to 16th bits of the integer, the third array element
	 * containing the 17th to 24th bits of the integer and the last array
	 * element containing the last 8 bits of the integer.
	 * 
	 * @param aInteger
	 *            The integer that needs to be converted to a byte array.
	 * @return The byte array representation of the integer.
	 */
	public static byte[] toBytes(int aInteger) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((aInteger >>> 24) & 0xFF);
		bytes[1] = (byte) ((aInteger >>> 16) & 0xFF);
		bytes[2] = (byte) ((aInteger >>> 8) & 0xFF);
		bytes[3] = (byte) ((aInteger >>> 0) & 0xFF);
		return bytes;
	}

	/**
	 * An EPPCodec is delegated to to do the real work.
	 * <code>EPPXMLByteArrayAssembler</code> just wraps it to provide the
	 * EPPAssembler interface.
	 */
	private EPPCodec codec;

	/**
	 * Construct and instance of an <code>EPPXMLByteArrayAssembler</code>
	 */
	public EPPXMLByteArrayAssembler() {
		this.codec = EPPCodec.getInstance();
		initParserPool();
	}

	/**
	 * Initialize the XML parser pool, with the name EPPXMLParser.POOL and with
	 * the "com.verisign.epp.util.EPPXMLParser" as the object type. The
	 * remaining configuration settings are retrieved from the
	 * EPPEnv.getServerParser methods. If there is any error initializing the
	 * pool, and error diagnostic is logged, and the program with stop with a
	 * call to <code>System.exit(1)</code>, since this represents a fatal error.
	 * The <code>EPPEnv</code> settings referenced include:<br>
	 * <br>
	 * <ul>
	 * <li>getServerParserInitObjs()</li>
	 * <li>getServerParserMinSize</li>
	 * <li>getServerParserMaxSize()</li>
	 * <li>getServerParserMaxSoft()</li>
	 * <li>getServerParserObjTimeout()</li>
	 * <li>getServerParserUserTimeout()</li>
	 * <li>getServerParserSkimmerFreq()</li>
	 * <li>getServerParserShrinkBy()</li>
	 * <li>getServerParserLogFile()</li>
	 * <li>getServerParserDebug()</li>
	 * </ul>
	 */
	private void initParserPool() {
		// Pool does not exist?
		if (!_parserInitialized) {
			LOG.debug("EPPXMLByteArray.initParserPool(): Creating parser pool");
			
			// Create parser pool
			GenericPoolMetaData parserMeta = new GenericPoolMetaData();

			parserMeta.setName(EPPSchemaCachingParser.POOL);
			parserMeta
					.setObjectType("com.verisign.epp.util.EPPSchemaCachingParser");
			parserMeta.setInitialObjects(EPPEnv.getServerParserInitObjs());
			parserMeta.setMinimumSize(EPPEnv.getServerParserMinSize());
			parserMeta.setMaximumSize(EPPEnv.getServerParserMaxSize());
			parserMeta.setMaximumSoft(EPPEnv.getServerParserMaxSoft());
			parserMeta.setObjectTimeout(EPPEnv.getServerParserObjTimeout());
			parserMeta.setUserTimeout(EPPEnv.getServerParserUserTimeout());
			parserMeta.setSkimmerFrequency(EPPEnv.getServerParserSkimmerFreq());
			parserMeta.setShrinkBy(EPPEnv.getServerParserShrinkBy());
			parserMeta.setLogFile(EPPEnv.getServerParserLogFile());
			parserMeta.setDebugging(EPPEnv.getServerParserDebug());

			GenericPool parserPool = new GenericPool(parserMeta);
			GenericPoolManager.getInstance().addPool(
					EPPSchemaCachingParser.POOL, parserPool);

			// Create transformer pool
			LOG.debug("EPPXMLByteArray.initParserPool(): Creating transformer pool");
			GenericPoolMetaData transformerMeta = new GenericPoolMetaData();

			transformerMeta.setName(EPPTransformer.POOL);
			transformerMeta
					.setObjectType("com.verisign.epp.util.EPPTransformer");
			transformerMeta.setInitialObjects(EPPEnv.getServerParserInitObjs());
			transformerMeta.setMinimumSize(EPPEnv.getServerParserMinSize());
			transformerMeta.setMaximumSize(EPPEnv.getServerParserMaxSize());
			transformerMeta.setMaximumSoft(EPPEnv.getServerParserMaxSoft());
			transformerMeta
					.setObjectTimeout(EPPEnv.getServerParserObjTimeout());
			transformerMeta.setUserTimeout(EPPEnv.getServerParserUserTimeout());
			transformerMeta.setSkimmerFrequency(EPPEnv
					.getServerParserSkimmerFreq());
			transformerMeta.setShrinkBy(EPPEnv.getServerParserShrinkBy());
			transformerMeta.setLogFile(EPPEnv.getServerParserLogFile());
			transformerMeta.setDebugging(EPPEnv.getServerParserDebug());

			GenericPool transformerPool = new GenericPool(transformerMeta);
			GenericPoolManager.getInstance().addPool(EPPTransformer.POOL,
					transformerPool);

			_parserInitialized = true;
		}
	}

	/**
	 * Takes an input <code>byte</code> array and reads XML from it to create an
	 * <code>EPPEvent</code>
	 * 
	 * @param aBytes
	 *            The byte array to read data from.
	 * @param aData
	 *            A data object which can be used to store context information.
	 * @return EPPEvent The <code> EPPEvent </code> that is created from the
	 *         InputStream
	 * @exception EPPAssemblerException
	 *                Error creating the <code>EPPEvent</code>
	 */
	public EPPEvent decode(byte[] aBytes, Object aData)
			throws EPPAssemblerException {
		LOG.debug("decode(): Enter");

		EPPMessage message = null;
		try {
			/** Declare an instance of the EPPXMLByteArray class */
			EPPXMLByteArray xmlByteArray = new EPPXMLByteArray(
					EPPSchemaCachingParser.POOL, EPPTransformer.POOL);

			// Get the DOM Document from the xml stream
			Document domDocument = xmlByteArray.decode(aBytes);
			// Convert the DOM Document to an EPPMessage using the EPPCodec
			message = this.codec.decode(domDocument);
		}
		catch (EPPComponentNotFoundException e) {
			LOG.debug("decode():", e);
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
			LOG.debug("decode():", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.MISSINGPARAMETER);
		}
		catch (EPPAssemblerException e) {
			LOG.debug("decode():", e);
			throw e;
		}
		catch (EPPException e) {
			LOG.debug("decode():", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.XML);
		}
		catch (InterruptedIOException e) {
			LOG.debug("decode():", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.INTRUPTEDIO);
		}
		catch (IOException e) {
			LOG.debug("decode():", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.CLOSECON);
		}

		LOG.debug("decode(): Return");

		return new EPPEvent(message);
	}

	/**
	 * Takes an <code> EPPEventResponse </code> and serializes it to a
	 * <code>byte</code> array in XML Format.
	 * 
	 * @param aResponse
	 *            The response that will be serialized
	 * @param aData
	 *            A data object which can be used to store context information.
	 * @exception EPPAssemblerException
	 *                Error serializing the <code>EPPEventResponse</code>
	 */
	public byte[] encode(EPPEventResponse aResponse, Object aData)
			throws EPPAssemblerException {
		LOG.debug("encode(): Enter");

		byte[] responseBytes = null;
		try {
			// First, get the message and convert it to a DOM Document using the
			// codec
			EPPMessage response = aResponse.getResponse();
			Document domDocument = this.codec.encode(response);

			EPPXMLByteArray xmlByteArray = new EPPXMLByteArray(
					EPPSchemaCachingParser.POOL, EPPTransformer.POOL);

			// convert the DOM document to bytes
			responseBytes = xmlByteArray.encode(domDocument);
		}
		catch (EPPEncodeException e) {
			LOG.debug("encode()", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.MISSINGPARAMETER);
		}
		catch (EPPException e) {
			LOG.debug("encode()", e);
			throw new EPPAssemblerException(e.getMessage(),
					EPPAssemblerException.FATAL);
		}

		// add the header bytes
		LOG.debug("EPP Packet Header = " + (responseBytes.length + 4));
		byte[] eppPacketHeader = toBytes(responseBytes.length + 4);
		byte[] eppPacket = new byte[responseBytes.length + 4];
		System.arraycopy(eppPacketHeader, 0, eppPacket, 0, 4);
		System.arraycopy(responseBytes, 0, eppPacket, 4, responseBytes.length);

		LOG.debug("encode(): Return");

		return eppPacket;
	}

} // End class EPPXMLByteArrayAssembler
