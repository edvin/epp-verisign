/***********************************************************
 Copyright (C) 2012 VeriSign, Inc.

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
 ***********************************************************/

package com.verisign.epp.codec.signedMark;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPSchemaCachingParser;

/**
 * Class for the encoded signed mark, which contains the mark ({@link EPPMark}),
 * and additional elements associated with the signing of the mark like the
 * serial number of the signed mark, the expiration of the signed mark, and the
 * <code>XMLSignature</code> itself. This class extends
 * <code>EPPSignedMark</code>.
 */
public class EPPEncodedSignedMark extends EPPSignedMark {

	/**
	 * Serial version id for this class.
	 */
	private static final long serialVersionUID = -2581814950269930902L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPEncodedSignedMark.class
			.getName(), EPPCatFactory.getInstance().getFactory() );

	/**
	 * Constant for the mark local name for encoded signedMark element
	 */
	public static final String ELM_ENCODED_SIGNED_MARK_LOCALNAME =
			"encodedSignedMark";

	/**
	 * Constant for the mark tag for signedMark element
	 */
	public static final String ELM_ENCODED_SIGNED_MARK_NAME = NS_PREFIX + ":"
			+ ELM_ENCODED_SIGNED_MARK_LOCALNAME;


	/**
	 * Create an <code>EPPEncodedSignedMark</code> instance.
	 */
	public EPPEncodedSignedMark () {
		super();
	}


	/**
	 * Construct Encoded SignMark object from SignMark object.
	 * 
	 * @param aSignedMark
	 *        SignMark object
	 * @throws EPPEncodeException
	 *         Error encoding the SignMark <code>byte[]</code>.
	 * @throws EPPDecodeException
	 *         Error decoding the encoded SignMark <code>byte[]</code>.
	 */
	public EPPEncodedSignedMark ( EPPSignedMark aSignedMark )
			throws EPPEncodeException, EPPDecodeException {
		// Encode existing signmark and contruct the new encoded signmark.
		// This is to make sure, all elements are cloned including signature
		// element.
		super( aSignedMark.encode() );
	}


	/**
	 * Create an <code>EPPEncodedSignedMark</code> with the id, issuer, not before
	 * date, not after date, and the mark attributes of the signed mark.
	 * 
	 * @param aId
	 *        Identifier of signed mark
	 * @param aIssuer
	 *        Signed mark issuer information
	 * @param aNotBefore
	 *        Date and time that the signed mark was created.
	 * @param aNotAfter
	 *        Date and time that the signed mark expires.
	 * @param aMark
	 *        Mark information
	 * @throws EPPEncodeException
	 *         Thrown if any errors prevent encoding.
	 * @throws EPPDecodeException
	 *         Error decoding the Issuer or Mark object.
	 */
	public EPPEncodedSignedMark ( String aId, EPPIssuer aIssuer, Date aNotBefore,
			Date aNotAfter, EPPMark aMark ) throws EPPEncodeException,
			EPPDecodeException {
		super( aId, aIssuer, aNotBefore, aNotAfter, aMark );
	}


	/**
	 * Creates an <code>EPPEncodedSignedMark</code> that is initialized by
	 * decoding the input <code>byte[]</code>.
	 * 
	 * @param aEncodedSignedMarkArray
	 *        <code>byte[]</code> to decode the attribute values
	 * @throws EPPDecodeException
	 *         Error decoding the input <code>byte[]</code>.
	 */
	public EPPEncodedSignedMark ( byte[] aEncodedSignedMarkArray )
			throws EPPDecodeException {
		cat.debug( "EPPSignedMark(byte[]): enter" );

		byte[] signedMarkXML = null;
		Element elm;
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream( aEncodedSignedMarkArray );
			DocumentBuilder parser = new EPPSchemaCachingParser();
			Document doc = parser.parse( is );
			elm = doc.getDocumentElement();
			String base64SignedMark = EPPUtil.getTextContent( elm );
			signedMarkXML = Base64.decodeBase64( base64SignedMark );
		}
		catch ( Exception ex ) {
			throw new EPPDecodeException( "Error decoding signed mark array: " + ex );
		}
		finally {
			if ( is != null ) {
				try {
					is.close();
					is = null;
				}
				catch ( IOException e ) {
				}
			}
		}

		super.decode( signedMarkXML );

		cat.debug( "EPPSignedMark.decode(byte[]): exit" );
	}


	/**
	 * Creates an <code>EPPEncodedSignedMark</code> by decoding the a Signed Mark
	 * Data (SMD) that is in a PEM-like input stream that includes the encoded
	 * signed mark with a leading line &quot;-----BEGIN ENCODED SMD-----&quot; and
	 * a trailing &quot;-----END ENCODED SMD-----&quot;.
	 * 
	 * @param aSMD
	 *        <code>InputStream</code> containing a Signed Mark Data (SMD)
	 * @throws EPPDecodeException
	 *         Error decoding the Signed Mark Data (SMD)
	 */
	public EPPEncodedSignedMark ( InputStream aSMD ) throws EPPDecodeException {
		cat.debug( "EPPEncodedSignedMark.EPPEncodedSignedMark(InputStream): enter" );

		// Parse for the encoded signed mark
		StringBuffer smdBuffer = new StringBuffer();
		BufferedReader bufferedReader =
				new BufferedReader( new InputStreamReader( aSMD ) );
		String currLine;
		boolean addToSMD = false;

		try {
			while ( (currLine = bufferedReader.readLine()) != null ) {

				if ( currLine.equals( "-----BEGIN ENCODED SMD-----" ) ) {
					addToSMD = true;
				}
				else if ( currLine.equals( "-----END ENCODED SMD-----" ) ) {
					addToSMD = false;
				}
				else if ( addToSMD ) {
					smdBuffer.append( currLine );
				}
				else {
					// Ignore line
				}

			}
		}
		catch ( IOException e ) {
			throw new EPPDecodeException( "Error reading SMD: " + e );
		}

		// Base64 decode encoded signed mark to signed mark
		byte[] signedMarkXML = Base64.decodeBase64( smdBuffer.toString() );

		// Decode the signed mark XML
		super.decode( signedMarkXML );

		cat.debug( "EPPEncodedSignedMark.EPPEncodedSignedMark(InputStream): exit" );
	}


	/**
	 * Decode the <code>EPPSignedMark</code> component
	 * 
	 * @param aElement
	 *        Root element of the <code>EPPSignedMark</code>
	 * @throws EPPDecodeException
	 *         Error decoding the <code>EPPSignedMark</code>
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {
		cat.debug( "EPPEncodedSignedMark.decode(Element): enter" );

		String base64SignedMark = EPPUtil.getTextContent( aElement );
		byte[] signedMarkXML = Base64.decodeBase64( base64SignedMark );
		super.decode( signedMarkXML );

		cat.debug( "EPPEncodedSignedMark.decode(Element): exit - normal" );
	}


	/**
	 * Encode the signed mark to a <code>byte[]</code>.
	 * 
	 * @return Encoded signed mark
	 * @throws EPPEncodeException
	 *         Error encoding the signed mark
	 */
	public byte[] encode () throws EPPEncodeException {
		cat.debug( "EPPEncodedSignedMark.encode(): enter" );

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			Document doc = new DocumentImpl();
			Element root = this.encode( doc );
			doc.appendChild( root );

			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();

			trans.transform( new DOMSource( root ), new StreamResult( os ) );
		}
		catch ( EPPEncodeException ex ) {
			throw ex;
		}
		catch ( Exception ex ) {
			cat.error( "Error encoding trademark to byte[]: " + ex );
			throw new EPPEncodeException( "Error encoding trademark to byte[]" );
		}

		cat.debug( "EPPEncodedSignedMark.encode(): exit" );
		return os.toByteArray();
	}


	/**
	 * Sets all this instance's data in the given XML document
	 * 
	 * @param aDocument
	 *        a DOM Document to attach data to.
	 * @return The root element of this component.
	 * @throws EPPEncodeException
	 *         Thrown if any errors prevent encoding.
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {
		cat.debug( "EPPEncodedSignedMark.encode(Document): enter" );

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPSignedMark.encode(Document)" );
		}

		Element root =
				aDocument.createElementNS( NS, NS_PREFIX + ":"
						+ ELM_ENCODED_SIGNED_MARK_LOCALNAME );

		byte[] signedMarkXml = super.encode();

		String base64EncodedText =
				new String( Base64.encodeBase64( signedMarkXml, true ) );

		Text currVal = aDocument.createTextNode( base64EncodedText );
		root.appendChild( currVal );
		cat.debug( "EPPEncodedSignedMark.encode(Document): exit - encoded" );
		return root;
	}


	/**
	 * Clone <code>EPPEncodedSignedMark</code>.
	 * 
	 * @return clone of <code>EPPEncodedSignedMark</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {
		return (EPPEncodedSignedMark) super.clone();
	}

}
