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

// PoolMan Imports
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.verisign.epp.exception.EPPException;
import com.verisign.epp.framework.EPPAssemblerException;

/**
 * <code>EPPXMLStream</code> is a utility class for reading and writing EPP
 * messages to/from streams. DOM Document are read and written to the streams.
 * An XML parser is required when reading from the stream. There is one
 * constructor that will create an XML parser per call to
 * <code>read(InputStream)</code> and one that will use a parser pool. Use of a
 * parser pool is recommended.
 */
public class EPPXMLStream {

	/**
	 * Default Maximum packet size of bytes accepted to ensure that the client
	 * is not overrun with an invalid packet or a packet that exceeds the
	 * maximum size. This setting could be made configurable in the future.
	 */
	public static final int DEFAULT_MAX_PACKET_SIZE = 355000;

	/**
	 * Maximum packet size of bytes accepted to ensure that the client is not
	 * overrun with an invalid packet or a packet that exceeds the maximum size.
	 * This setting defaults to {@link DEFAULT_MAX_PACKET_SIZE} and can be
	 * overridden with the &quot;EPP.MaxPacketSize&quot; configuration property.
	 */
	private static int maxPacketSize;

	/** Document Builder Factory for creating a parser per operation. */
	private static DocumentBuilderFactory factory = null;

	/**
	 * Used to encode and decode the packet byte arrays
	 */
	EPPXMLByteArray byteArray;

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(EPPXMLStream.class.getName(),
			EPPCatFactory.getInstance().getFactory());

	private static Logger packetCat = Logger.getLogger(
			EPPXMLStream.class.getName() + ".packet", EPPCatFactory
					.getInstance().getFactory());

	static {
		// Initialize the Document Builder Factory
		factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(true);

		maxPacketSize = DEFAULT_MAX_PACKET_SIZE;

		String maxPacketSizeProp = Environment.getOption("EPP.MaxPacketSize");
		if (maxPacketSizeProp != null) {
			try {
				maxPacketSize = Integer.parseInt(maxPacketSizeProp);
			}
			catch (NumberFormatException ex) {
				System.err
						.println("EPPXMLStream: EPP.MaxPacketSize property format error: "
								+ ex);
			}
		}

		cat.info("maxPacketSize = " + maxPacketSize);
	}

	/**
	 * Default constructor for <code>EPPXMLStream</code>. When using this
	 * constructor, a parser instance will be created on each call to
	 * <code>read(InputStream)</code> and a transformer instance will be created
	 * on each call to <code>write(Document,OutputStream)</code>. .
	 */
	public EPPXMLStream() {
		this.byteArray = new EPPXMLByteArray();
	}

	/**
	 * Construct <code>EPPXMLStream</code> to use a parser pool and a default
	 * transformer pool. The <code>aParserPoolName</code> parameter has to be a
	 * pool of <code>EPPParserPool</code> subclasses. When using this
	 * constructor, a parser instance will be checked out and checkin as needed
	 * on each call to <code>read(InputStream)</code>. The
	 * <code>Transformer</code> pool used is defined by the
	 * <code>EPPTransformer.POOL</code> constant.
	 * 
	 * @param aParserPoolName
	 *            Parser pool name to use
	 */
	public EPPXMLStream(String aParserPoolName) {
		this.byteArray = new EPPXMLByteArray(aParserPoolName);
	}

	/**
	 * Construct <code>EPPXMLStream</code> to use a parser pool and a
	 * transformer pool. The <code>aParserPoolName</code> parameter has to be a
	 * pool of <code>EPPParserPool</code> subclasses. When using this
	 * constructor, a parser instance will be checked out and checkin as needed
	 * on each call to <code>read(InputStream)</code>. The
	 * <code>Transformer</code> pool used is defined by the
	 * <code>EPPTransformer.POOL</code> constant. The
	 * <code>aTransformerPoolName</code> parameters is the name of pool
	 * containing <code>Transformer</code> instances.
	 * 
	 * @param aParserPoolName
	 *            Parser pool name to use
	 * @param aTransformerPoolName
	 *            Transformer pool name to use
	 */
	public EPPXMLStream(String aParserPoolName, String aTransformerPoolName) {
		this.byteArray = new EPPXMLByteArray(aParserPoolName,
				aTransformerPoolName);
	}

	/**
	 * Reads an EPP packet from the stream based on a search for the End Of
	 * Message (EOM) string (&lt;/epp&gt;).
	 * 
	 * @param aStream
	 *            Stream to read packet from
	 * 
	 * @return EPP packet <code>String</code>
	 * 
	 * @exception EPPException
	 *                Error reading packet from stream. The stream should be
	 *                closed.
	 * @exception InterruptedIOException
	 *                Time out reading for packet
	 * @exception IOException
	 *                Exception from the input stream
	 */
	public byte[] readPacket(InputStream aStream) throws EPPException,
			InterruptedIOException, IOException {
		cat.debug("readPacket(): enter");

		// Validate argument
		if (aStream == null) {
			cat.error("readPacket() : null stream passed");
			throw new EPPException(
					"EPPXMLStream.readPacket() : null stream passed");
		}

		// Read network header (32 bits) that defines the total length
		// of the EPP data unit measured in octets in network (big endian)
		// byte order.
		DataInputStream theStream = new DataInputStream(aStream);
		int thePacketSize = -1;
		byte[] thePacket = null;

		try {
			// Read the packet size which includes network header itself.
			thePacketSize = theStream.readInt();

			if (thePacketSize > maxPacketSize) {
				cat.error("readPacket(InputStream): Packet header specifies a packet larger that the maximum of "
						+ maxPacketSize + " bytes");
				throw new EPPException(
						"EPPXMLStream.readPacket() : Packet header exceeds the maximum of "
								+ maxPacketSize + " bytes");
			}

			cat.debug("readPacket(): Received network header with value = "
					+ thePacketSize);

			thePacket = new byte[thePacketSize - 4];

			// Read the packet
			theStream.readFully(thePacket);
		}
		catch (EOFException ex) {
			cat.error("readPacket(InputStream): EOFException while attempting to read packet, size = "
					+ thePacketSize + ", packet = [" + thePacket + "]: " + ex);
			throw ex;
		}
		catch (InterruptedIOException ex) {
			cat.debug("readPacket(InputStream): InterruptedIOException while attempting to read packet: "
					+ ex);
			throw ex;
		}
		catch (IOException ex) {
			cat.error("readPacket(InputStream): IOException while attempting to read packet, size = "
					+ thePacketSize + ", packet = [" + thePacket + "]: " + ex);
			throw ex;
		}

		cat.debug("readPacket(): Received packet [" + new String(thePacket)
				+ "]");
		cat.debug("readPacket(): exit");

		return thePacket;
	}

	/**
	 * Reads an EPP packet from the <code>aStream</code> parameter,
	 * parses/validates it, and returns the associated DOM Document. The XML
	 * parser is either created per call, or is retrieved from a parser pool
	 * when <code>EPPXMLStream(GenericPoolManager)</code> is used. Use of a
	 * parser pool is recommended.
	 * 
	 * @param aStream
	 *            Input stream to read for an EPP packet.
	 * 
	 * @return Parsed DOM Document of packet
	 * 
	 * @exception EPPException
	 *                Error with received packet or end of stream. It is
	 *                recommended that the stream be closed.
	 * @exception EPPAssemblerException
	 *                Error parsing packet
	 * @exception IOException
	 *                Error reading packet from stream
	 */
	public Document read(InputStream aStream) throws EPPAssemblerException,
			EPPException, IOException {
		cat.debug("read(InputStream): enter");

		// Validate argument
		if (aStream == null) {
			throw new EPPException(
					"EPPXMLStream.read() : BAD ARGUMENT (aStream)");
		}

		Document theDoc = null;

		byte[] thePacket = this.readPacket(aStream);
		theDoc = this.byteArray.decode(thePacket);

		cat.debug("read(InputStream): exit");

		return theDoc;
	}
	
	/**
	 * Decodes the passed in packet <code>byte[]</code> into a DOM 
	 * <code>Document</code>.
	 * 
	 * @param aPacket Input packet to decode to DOM <code>Document</code>.
	 * @return Decoded DOM <code>Document</code>
	 * 
	 * @throws EPPException Error decoding the packet.
	 * @throws IOException Basic IO error decoding the packet.
	 */
	public Document decodePacket(byte[] aPacket) throws EPPException, IOException {
		cat.debug("decodePacket(byte[]): enter");
		
		Document theDoc = this.byteArray.decode(aPacket);
		
		cat.debug("decodePacket(byte[]): exit");
		
		return theDoc;
	}
	

	/**
	 * Writes a DOM Document to the output stream. The DOM Document will be
	 * serialized to XML and written to the output stream.
	 * 
	 * @param aDoc
	 *            DOM Document to write to stream
	 * @param aOutput
	 *            Output stream to write to
	 * 
	 * @exception EPPException
	 *                Error writing to stream. It is recommended that the stream
	 *                be closed.
	 */
	public void write(Document aDoc, OutputStream aOutput) throws EPPException {
		cat.debug("write(Document, InputStream): enter");

		// Validate arguments
		if (aOutput == null) {
			cat.error("write(Document, InputStream): aOutput == null");
			throw new EPPException(
					"EPPXMLStream.write() : BAD ARGUMENT (aOutput)");
		}

		if (aDoc == null) {
			cat.error("write(Document, InputStream): aDoc == null");
			throw new EPPException("EPPXMLStream.write() : BAD ARGUMENT (aDoc)");
		}

		byte[] thePacket = this.byteArray.encode(aDoc);

		packetCat.debug("write() : Sending [" + new String(thePacket) + "]");

		// Write to stream
		try {
			DataOutputStream theStream = new DataOutputStream(aOutput);
			theStream.writeInt(thePacket.length + 4);
			aOutput.write(thePacket);
			aOutput.flush();
		}
		catch (IOException ex) {
			cat.error("write(Document, InputStream) : Writing to stream :" + ex);
			throw new EPPException("EPPXMLStream.write() : Writing to stream "
					+ ex);
		}

		cat.debug("write(Document, InputStream): exit");
	}

}

// End class EPPXMLStream
