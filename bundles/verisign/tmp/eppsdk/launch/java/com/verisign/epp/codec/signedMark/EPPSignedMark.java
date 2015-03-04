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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.mark.EPPMark;
import com.verisign.epp.exception.EPPException;
import com.verisign.epp.util.EPPCatFactory;
import com.verisign.epp.util.EPPSchemaCachingParser;
import com.verisign.epp.util.EPPXMLErrorHandler;
import com.verisign.epp.util.EqualityUtil;

/**
 * Class for the signed mark, which contains the mark ({@link EPPMark}), and
 * additional elements associated with the signing of the mark like the serial
 * number of the signed mark, the expiration of the signed mark, and the
 * <code>XMLSignature</code> itself.
 */
public class EPPSignedMark implements EPPCodecComponent {

	/**
	 * Serial version id for this class.
	 */
	private static final long serialVersionUID = 3210389145062853193L;

	/**
	 * Log4j category for logging
	 */
	private static Logger cat = Logger.getLogger( EPPSignedMark.class.getName(),
			EPPCatFactory.getInstance().getFactory() );

	/** Namespace URI associated with EPPLaunchExtFactory. */
	public static final String NS = "urn:ietf:params:xml:ns:signedMark-1.0";

	/** Namespace prefix associated with EPPLaunchExtFactory. */
	public static final String NS_PREFIX = "signedMark";

	/** XML Schema definition for EPPLaunchExtFactory */
	public static final String NS_SCHEMA =
			"urn:ietf:params:xml:ns:signedMark-1.0 signedMark-1.0.xsd";

	/**
	 * Constant for the mark local name for signedMark element
	 */
	public static final String ELM_SIGNED_MARK_LOCALNAME = "signedMark";

	/**
	 * Constant for the mark tag for signedMark element
	 */
	public static final String ELM_SIGNED_MARK_NAME = NS_PREFIX + ":"
			+ ELM_SIGNED_MARK_LOCALNAME;

	/**
	 * Element local name for serial number for the signed mark
	 */
	private static final String ELM_ID = "id";

	/**
	 * Element local name for the date the signed mark was created
	 */
	private static final String ELM_NOT_BEFORE = "notBefore";

	/**
	 * Element local name for the date of expiration of the signed mark
	 */
	private static final String ELM_NOT_AFTER = "notAfter";

	/**
	 * The ID attribute name
	 */
	private static final String ATTR_ID = "id";

	/**
	 * The ID attribute value used for the signed mark
	 */
	private static final String ATTR_ID_VALUE = "signedMark";

	/**
	 * XML Signature Factory
	 */
	private static XMLSignatureFactory sigFactory = XMLSignatureFactory
			.getInstance( "DOM" );

	/**
	 * Case where constructing SMD with mark data (i.e. without signature), 
	 * XML is not valid as per schema (Signature is required as per schema).
	 * In this case, ignore the XML parsing exception and create a DOM object. 
	 */
	private static final boolean IGNORE_XML_PARSE_EXCEPTION = true;
	
	/**
	 * When we construct the SMD object from EPP request, 
	 * throw any parsing exception back to caller. 
	 */
	private static final boolean DO_NOT_IGNORE_XML_PARSE_EXCEPTION = false;
	
	/**
	 * XML local name for the root element of the signed mark. This should be set
	 * to <code>ELM_SIGNED_MARK_LOCALNAME</code>.
	 */
	private String localName = ELM_SIGNED_MARK_LOCALNAME;

	/**
	 * Value used for the ID attribute name with the default of
	 * <code>ATTR_ID_VALUE</code>.
	 */
	private String attrIdValue = ATTR_ID_VALUE;

	/**
	 * Identifier of the signed mark
	 */
	private String id;

	/**
	 * Issuer of the signed mark.
	 */
	private EPPIssuer issuer;

	/**
	 * the date of creation of the signed mark
	 */
	private Date notBefore;

	/**
	 * the date of expiration of the signed mark
	 */
	private Date notAfter;

	/**
	 * Mark associated with the signed mark
	 */
	private EPPMark mark;

	/**
	 * Sign mark DOM element.
	 */
	private Element signedMarkElement = null;


	/**
	 * Create an <code>EPPSignedMark</code> instance.
	 */
	public EPPSignedMark () {
	}

	/**
	 * Convert an <code>EPPEncodedSignedMark</code> into an 
	 * <code>EPPSignedMark</code>.
	 * 
	 * @param aEncodedSignedMark <code>EPPEncodedSignedMark</code> to convert from.
	 */
	public EPPSignedMark(EPPEncodedSignedMark aEncodedSignedMark) {
		
		EPPSignedMark encodedSignedMark = (EPPSignedMark) aEncodedSignedMark;
		
		this.id = encodedSignedMark.id;
		this.issuer = encodedSignedMark.issuer;
		this.notAfter = encodedSignedMark.notAfter;
		this.notBefore = encodedSignedMark.notBefore;
		this.mark = encodedSignedMark.mark;
		this.signedMarkElement = encodedSignedMark.signedMarkElement;
	}

	/**
	 * Create an <code>EPPSignedMark</code> with the id, issuer, not before date,
	 * not after date, and the mark attributes of the signed mark. The default
	 * encoding is XML and the signature must be generated by calling
	 * {@link #sign(PrivateKey)}. Once object is created using this constructor,
	 * one should not update the Mark object. In case mark object get updated,
	 * changes will not be included in XML/signature.
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
	public EPPSignedMark ( String aId, EPPIssuer aIssuer, Date aNotBefore,
			Date aNotAfter, EPPMark aMark ) throws EPPEncodeException,
			EPPDecodeException {
		parseAndSetSignedMarkElement( aId, aIssuer, aNotBefore, aNotAfter, aMark );
	}


	/**
	 * Create the <code>EPPSignedMark</code> object from the input
	 * <code>byte[]</code> (XML).
	 * 
	 * @param aSignedMarkArray
	 *        <code>byte[]</code> to decode the attribute values
	 * @throws EPPDecodeException
	 *         Error decoding the <code>byte[]</code>.
	 */
	public EPPSignedMark ( byte[] aSignedMarkArray ) throws EPPDecodeException {
		cat.debug( "EPPSignedMark(byte[]): enter" );
		this.decode( aSignedMarkArray );
		cat.debug( "EPPSignedMark.decode(byte[]): exit" );
	}


	/**
	 * Decode the <code>EPPSignedMark</code> component
	 * 
	 * @param aElement
	 *        Root element of the <code>EPPSignedMark</code>
	 * @throws EPPDecodeException
	 *         Error decoding the <code>EPPSignedMark</code>
	 */
	@Override
	public void decode ( Element aElement ) throws EPPDecodeException {
		cat.debug( "EPPSignedMark.decode(Element): enter" );
		decodeSignedMarkElement( aElement );
		cat.debug( "EPPSignedMark.decode(Element): exit - normal" );
	}


	/**
	 * Create a DOM document from byte array. Initialized the instance variables
	 * like mark, issuer etc. Sets the signedMarkElement.
	 * 
	 * @param aSignedMarkArray
	 *        <code>byte[]</code> to decode the attribute values
	 * @throws EPPDecodeException
	 *         Error decoding the <code>byte[]</code>.
	 */
	protected void decode ( byte[] aSignedMarkArray ) throws EPPDecodeException {
		Element elm;
		elm = parseAndGetDocElement( aSignedMarkArray );
		initializeObject( elm );
		this.signedMarkElement = elm;
	}


	/**
	 * Initialized the signedMark elements from DOM element and sets localName,
	 * attributeIdValue, notBefore, notAfter, issuer and mark object to instance
	 * variables. Once these variables are set, can not be changed.
	 * 
	 * @param aElement
	 *        Root element of the <code>EPPSignedMark</code>
	 * @throws EPPDecodeException
	 *         Error decoding the <code>EPPSignedMark</code>
	 */
	private void initializeObject ( Element aElement ) throws EPPDecodeException {

		this.localName = aElement.getLocalName();

		// ID attribute name
		this.attrIdValue = aElement.getAttribute( ATTR_ID );

		// Id
		this.id = EPPUtil.decodeString( aElement, NS, ELM_ID );

		// Issuer
		this.issuer =
				(EPPIssuer) EPPUtil.decodeComp( aElement, EPPSignedMark.NS,
						EPPIssuer.ELM_LOCALNAME, EPPIssuer.class );

		// Not Before
		this.notBefore = EPPUtil.decodeTimeInstant( aElement, NS, ELM_NOT_BEFORE );

		// Not After
		this.notAfter = EPPUtil.decodeTimeInstant( aElement, NS, ELM_NOT_AFTER );

		// Mark
		this.mark =
				(EPPMark) EPPUtil.decodeComp( aElement, EPPMark.NS,
						EPPMark.ELM_LOCALNAME, EPPMark.class );
	}


	/**
	 * This method decode the specified DOM element. Initialized the instance
	 * variables like mark, issuer etc. Sets the signedMarkElement.
	 * 
	 * @param aElement
	 *        Root element of the <code>EPPSignedMark</code>
	 * @throws EPPDecodeException
	 *         Error decoding the <code>EPPSignedMark</code>
	 */
	private void decodeSignedMarkElement ( Element aElement )
			throws EPPDecodeException {
		initializeObject( aElement );
		try {
			this.signedMarkElement =
					parseAndGetDocElement( getByteArrayForElement( aElement ) );
		}
		catch ( EPPEncodeException e ) {
			throw new EPPDecodeException( e );
		}
	}


	/**
	 * Encode the signed mark to a <code>byte[]</code>.
	 * 
	 * @return <code>byte[]</code> representing signed mark
	 * @throws EPPEncodeException
	 *         Error encoding the signed mark
	 */
	public byte[] encode () throws EPPEncodeException {
		return getByteArrayForElement( this.signedMarkElement );
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
	@Override
	public Element encode ( Document aDocument ) throws EPPEncodeException {
		cat.debug( "EPPSignedMark.encode(Document): enter" );

		if ( aDocument == null ) {
			throw new EPPEncodeException( "aDocument is null"
					+ " on in EPPSignedMark.encode(Document)" );
		}

		// By this time, signedMark element must have been set.
		if ( this.signedMarkElement == null ) {
			throw new EPPEncodeException( "Signed mark is not decoded properly." );
		}
		Node newAddedNode = aDocument.importNode( this.signedMarkElement, true );

		cat.debug( "EPPSignedMark.encode(Document): exit - normal" );

		return (Element) newAddedNode;
	}


	/**
	 * Validates required attributes. Throws EPPEncodeException if required
	 * attributes is not set.
	 * 
	 * @throws EPPEncodeException
	 *         Thrown if any required parameter is missing.
	 */
	private void validateRequiredAttributes () throws EPPEncodeException {
		// Validate required attributes
		if ( this.id == null ) {
			throw new EPPEncodeException( "Signed mark id is required." );
		}

		if ( this.issuer == null ) {
			throw new EPPEncodeException( "Signed mark issuer is required." );
		}

		if ( this.notBefore == null ) {
			throw new EPPEncodeException( "Signed mark notBefore is required." );
		}

		if ( this.notAfter == null ) {
			throw new EPPEncodeException( "Signed mark notAfter is required." );
		}

		if ( this.mark == null ) {
			throw new EPPEncodeException( "Signed mark mark is required." );
		}
	}


	/**
	 * Encode all attributes and create a DOM signed mark element. * @param aId
	 * Identifier of signed mark
	 * 
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
	 */
	private void parseAndSetSignedMarkElement ( String aId, EPPIssuer aIssuer,
			Date aNotBefore, Date aNotAfter, EPPMark aMark )
			throws EPPEncodeException {

		this.id = aId;
		this.issuer = aIssuer;
		this.notBefore = aNotBefore;
		this.notAfter = aNotAfter;
		this.mark = aMark;
		validateRequiredAttributes();

		Document document = new DocumentImpl();

		Element root =
				document.createElementNS( NS, NS_PREFIX + ":" + this.localName );

		// Add the "id" Attribute
		root.setAttribute( ATTR_ID, this.attrIdValue );
		root.setIdAttribute( ATTR_ID, true );

		// Id
		EPPUtil
				.encodeString( document, root, this.id, NS, NS_PREFIX + ":" + ELM_ID );

		// Issuer
		EPPUtil.encodeComp( document, root, this.issuer );

		// Not Before
		EPPUtil.encodeTimeInstant( document, root, this.notBefore, NS, NS_PREFIX
				+ ":" + ELM_NOT_BEFORE );

		// Not After
		EPPUtil.encodeTimeInstant( document, root, this.notAfter, NS, NS_PREFIX
				+ ":" + ELM_NOT_AFTER );

		// Mark
		EPPUtil.encodeComp( document, root, this.mark );

		document.appendChild( root );

		byte[] xmlBytes = getByteArrayForElement( root );

		// Creating signedMark element from byte array instead of assigning the root
		// element to signedMarkElement. This way, byte array remains the same even if
		// we do encode/docode multiple time. Also sign method sign the xml
		// properly.
		try {
			this.signedMarkElement = parseAndGetDocElement( xmlBytes, IGNORE_XML_PARSE_EXCEPTION );
		}
		catch ( EPPDecodeException e ) {
			throw new EPPEncodeException( e );
		}
	}


	/**
	 * Clone <code>EPPSignedMark</code>. Signature element is not cloned.
	 * 
	 * @return clone of <code>EPPSignedMark</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	public Object clone () throws CloneNotSupportedException {
		EPPSignedMark clone = (EPPSignedMark) super.clone();

		// Issuer
		if ( this.issuer != null ) {
			clone.issuer = (EPPIssuer) this.issuer.clone();
		}

		// Mark
		if ( this.mark != null ) {
			clone.mark = (EPPMark) this.mark.clone();
		}

		return clone;
	}


	/**
	 * Digitally sign the signed mark using the passed private key. No
	 * certificates will be added using this method. If certificates need to be
	 * added use {@link #sign(PrivateKey, Certificate[])}.
	 * 
	 * @param aPrivateKey
	 *        Private key used to sign the signed mark
	 * @throws EPPException
	 *         Error creating the digital signature
	 */
	public void sign ( PrivateKey aPrivateKey ) throws EPPException {
		cat.debug( "EPPSignedMark.sign(PrivateKey): enter" );
		this.sign( aPrivateKey, null );
		cat.debug( "EPPSignedMark.sign(PrivateKey): exit" );
	}


	/**
	 * Digitally sign the signed mark using the passed private key and a chain of
	 * certificates. 
	 * 
	 * @param aPrivateKey
	 *        Private key used to sign the signed mark
	 * @param aCertChain
	 *        Certificate chain to include in the XMLSignature associated with the
	 *        private key. Pass <code>null</code> to not include the certificate
	 *        chain in the XMLSignature.
	 * @throws EPPException
	 *         Error creating the digital signature
	 */
	public void sign ( PrivateKey aPrivateKey, Certificate[] aCertChain )
			throws EPPException {
		cat.debug( "EPPSignedMark.sign(PrivateKey, Certificate[]): enter" );

		// Required parameter is null?
		if ( aPrivateKey == null ) {
			throw new EPPException(
					"EPPSignedMark.sign(PrivateKey, Certificate[]): null aPrivateKey parameter" );
		}

		try {
			DigestMethod digestMethod =
					sigFactory.newDigestMethod( DigestMethod.SHA256, null );

			List<Transform> transforms = new ArrayList<Transform>();
			transforms.add( sigFactory.newTransform( Transform.ENVELOPED,
					(TransformParameterSpec) null ) );
			Reference xmlSigRef =
					sigFactory.newReference( "#" + this.attrIdValue, digestMethod,
							transforms, null, null );

			// Create the SignedInfo
			SignedInfo signedInfo =
					sigFactory.newSignedInfo( sigFactory.newCanonicalizationMethod(
							CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
							(C14NMethodParameterSpec) null ), sigFactory.newSignatureMethod(
							SignatureMethod.RSA_SHA1, null ), Collections
							.singletonList( xmlSigRef ) );

			// Add certificate chain to signature?
			KeyInfo keyInfo = null;
			if ( aCertChain != null ) {
				cat
						.debug( "EPPSignedMark.sign(PrivateKey, Certificate[]): certificate chain passed" );

				KeyInfoFactory keyInfoFactory = sigFactory.getKeyInfoFactory();
				List<X509Certificate> certChain = new ArrayList<X509Certificate>();
				for ( Certificate cert : aCertChain ) {
					if ( cert == null || !(cert instanceof X509Certificate) ) {
						throw new EPPException(
								"EPPSignedMark.sign(PrivateKey, Certificate[]): Null or invalid certificate type" );
					}
					certChain.add( (X509Certificate) cert );
					cat
							.debug( "EPPSignedMark.sign(PrivateKey, Certificate[]): Added certificate ["
									+ cert + "] to X509Certificate list" );
				}
				List<X509Data> certDataList = new ArrayList<X509Data>();
				certDataList.add( keyInfoFactory.newX509Data( certChain ) );
				keyInfo = keyInfoFactory.newKeyInfo( certDataList );
			}
			else {
				cat
						.debug( "EPPSignedMark.sign(PrivateKey, Certificate[]): null certificate chain passed, no certificates added" );
			}

			DOMSignContext signContext =
					new DOMSignContext( aPrivateKey, this.signedMarkElement );
			signContext.setDefaultNamespacePrefix( "dsig" );
			XMLSignature signature = sigFactory.newXMLSignature( signedInfo, keyInfo );
			signature.sign( signContext );

		}
		catch ( Exception ex ) {
			ex.printStackTrace();
			cat.error( "Error signing the EPPSignedMark: " + ex );
			throw new EPPException(
					"EPPSignedMark.sign(PrivateKey, Certificate[]): Error signing the EPPSignedMark" );
		}
		cat.debug( "EPPSignedMark.sign(PrivateKey, Certificate[]): exit" );
	}


	/**
	 * Validate the signature attribute against the signed mark attributes by
	 * using the public key of the certificate or the top certificate in the
	 * certificate chain contained in the <code>XMLSignature</code> with using the
	 * passed PKIX parameters to the PKIX <code>CertPathValidator</code>
	 * algorithm. The trust store can be loaded and used to create an instance of
	 * <code>PKIXParameters</code> to verify the certificate chain included in the
	 * <code>XMLSignature</code> with the trust anchors included in the trust
	 * store. This method will automatically synchronize the
	 * <code>aPKIXParameters</code> parameter when used, since it is not
	 * thread-safe. Use {@link #validate(PKIXParameters, boolean)} to explicitly
	 * set the <code>aPKIXParameters</code> synchronization setting.
	 * 
	 * @param aPKIXParameters
	 *        Parameters used as input for the PKIX <code>CertPathValidator</code>
	 *        algorithm.
	 * @return <code>true</code> if valid; <code>false</code> otherwise.
	 */
	public boolean validate ( PKIXParameters aPKIXParameters ) {
		return this.validate( aPKIXParameters, true );
	}


	/**
	 * Returns the signature element from signed mark DOM object.
	 * 
	 * @return signature element from signedMark DOM object.
	 */
	private Element findSignatureElement () {
		return EPPUtil.getElementByTagNameNS( this.signedMarkElement,
				XMLSignature.XMLNS, "Signature" );
	}


	/**
	 * Validate the signature attribute against the signed mark attributes by
	 * using the public key of the certificate or the top certificate in the
	 * certificate chain contained in the <code>XMLSignature</code> with using the
	 * passed PKIX parameters to the PKIX <code>CertPathValidator</code>
	 * algorithm. The trust store can be loaded and used to create an instance of
	 * <code>PKIXParameters</code> to verify the certificate chain included in the
	 * <code>XMLSignature</code> with the trust anchors included in the trust
	 * store.
	 * 
	 * @param aPKIXParameters
	 *        Parameters used as input for the PKIX <code>CertPathValidator</code>
	 *        algorithm.
	 * @param aSynchronizePKIXParameters
	 *        Should the <code>aPKIXParameters</code> be synchronized inside the
	 *        method? If there is no reason to synchronize, then
	 *        <code>false</code> can be passed to increase performance.
	 * @return <code>true</code> if valid; <code>false</code> otherwise.
	 */
	public boolean validate ( PKIXParameters aPKIXParameters,
			boolean aSynchronizePKIXParameters ) {

		cat.debug( "validate(PKIXParameters): enter" );

		boolean valid = false;

		try {
			Element sigElement = findSignatureElement();

			DOMStructure domStructure = new DOMStructure( sigElement );
			XMLSignature signature = sigFactory.unmarshalXMLSignature( domStructure );

			// No key info found?
			if ( signature.getKeyInfo() == null ) {
				throw new Exception( "No key info found in Signature" );
			}

			List<X509Certificate> certificates = null;

			List<XMLStructure> keyContent = signature.getKeyInfo().getContent();

			// For each signature keyInfo item
			for ( XMLStructure currInfo : keyContent ) {

				// X509 data?
				if ( currInfo instanceof X509Data ) {

					List<?> x509Data = ((X509Data) currInfo).getContent();

					if ( x509Data == null ) {
						continue;
					}

					// For each X509Data element
					for ( Object currX509Data : x509Data ) {

						// X509Certificate?
						if ( currX509Data instanceof X509Certificate ) {

							if ( certificates == null ) {
								certificates = new ArrayList<X509Certificate>();
							}

							X509Certificate x509Cert = (X509Certificate) currX509Data;

							// Check validity of certificate
							x509Cert.checkValidity();

							cat.debug( "validate(PKIXParameters): Found X509Certificate ["
									+ x509Cert + "]" );
							certificates.add( x509Cert );
						}
					}
				}

				// No Certificates found?
				if ( certificates == null || certificates.isEmpty() ) {
					throw new Exception( "No certificates found in Signature" );
				}

				CertificateFactory certFactory =
						CertificateFactory.getInstance( "X.509" );
				CertPath certPath = certFactory.generateCertPath( certificates );

				// Validate certificate path against trust anchors
				// (aPKIXParameters)
				CertPathValidator pathValidator =
						CertPathValidator.getInstance( "PKIX" );

				// Must synchronize around the use of PKIXParameters
				// since it is NOT thread-safe.
				if ( aSynchronizePKIXParameters ) {
					synchronized (aPKIXParameters) {
						pathValidator.validate( certPath, aPKIXParameters );
					}
				}
				else {
					pathValidator.validate( certPath, aPKIXParameters );
				}
			}

			// Get the public key from top certificate to validate signature
			X509Certificate cert = certificates.get( 0 );
			cat
					.debug( "validate(PKIXParameters): Getting public key from top certificate ["
							+ cert + "]" );
			PublicKey publicKey = cert.getPublicKey();

			valid = this.validate( sigElement, publicKey );
		}
		catch ( Exception ex ) {
			cat
					.error( "validate(PKIXParameters): Error validating the EPPSignedMark: "
							+ ex );
			valid = false;
		}

		cat.debug( "validate(PKIXParameters): exit, valid = " + valid );
		return valid;

	}


	/**
	 * Validate the signature attribute against the signed mark attributes.
	 * 
	 * @param aPublicKey
	 *        Public used to validate the signature
	 * @return <code>true</code> if valid; <code>false</code> otherwise.
	 */
	public boolean validate ( PublicKey aPublicKey ) {
		cat.debug( "validate(PublicKey): enter" );

		boolean valid = false;

		try {
			Element sigElm = this.findSignatureElement();
			valid = this.validate( sigElm, aPublicKey );
		}
		catch ( Exception ex ) {
			cat.error( "validate(PublicKey): Error validating the EPPSignedMark: "
					+ ex );
			valid = false;
		}

		cat.debug( "validate(PublicKey): exit, valid = " + valid );
		return valid;
	}


	/**
	 * Generate the byte array for specified DOM element and return the byte
	 * array.
	 * 
	 * @param aElement
	 *        DOM element for which to get the byte array
	 * @return <code>byte[]</code> representing DOM element
	 * @throws EPPEncodeException
	 *         thrown if any errors prevent transforming.
	 */
	private byte[] getByteArrayForElement ( Element aElement )
			throws EPPEncodeException {
		cat.debug( "EPPSignedMark.getByteArrayForElement(): enter" );

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {

			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer trans = transFac.newTransformer();

			trans.transform( new DOMSource( aElement ), new StreamResult( os ) );
		}
		catch ( Exception ex ) {
			cat.error( "Error encoding signed mark to byte[]: " + ex );
			throw new EPPEncodeException( "Error encoding signed mark to byte[]" );
		}

		cat.debug( "EPPSignedMark.getByteArrayForElement(): exit" );
		return os.toByteArray();
	}


	/**
	 * Parse the input byte array and return the DOM document element.
	 * 
	 * @param aSignedMarkArray
	 *        <code>byte[]</code> to parse to create DOM element.
	 * @return DOM element created from <code>byte[]</code>.
	 * @throws EPPDecodeException
	 *         thrown if any errors parsing <code>byte[]</code>.
	 */
	private Element parseAndGetDocElement ( byte[] aSignedMarkArray )
			throws EPPDecodeException {
		return parseAndGetDocElement(aSignedMarkArray, DO_NOT_IGNORE_XML_PARSE_EXCEPTION);
	}

	/**
	 * Parse the input byte array and return the DOM document element.
	 * 
	 * @param aSignedMarkArray
	 *        <code>byte[]</code> to parse to create DOM element.
	 * @return DOM element created from <code>byte[]</code>.
	 * @throws EPPDecodeException
	 *         thrown if any errors parsing <code>byte[]</code>.
	 */
	private Element parseAndGetDocElement ( byte[] aSignedMarkArray, boolean aIgnoreParsingError )
			throws EPPDecodeException {
		Element elm;
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream( aSignedMarkArray );

			EPPSchemaCachingParser parser = new EPPSchemaCachingParser();
			
			parser.setFeature(EPPSchemaCachingParser.NORMALIZE_DATA, false);
			if(!aIgnoreParsingError) {
				parser.setErrorHandler(new EPPXMLErrorHandler());
			}
			Document doc = parser.parse( is );

			elm = doc.getDocumentElement();
			elm.setIdAttribute( "id", true );
			return elm;
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
	}


	/**
	 * implements a deep <code>EPPSignedMark</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPSignedMark</code> instance to compare with
	 * @return true if equal false otherwise
	 */
	public boolean equals ( Object aObject ) {

		if ( !(aObject instanceof EPPSignedMark) ) {
			cat.error( "EPPSignedMark.equals(): aObject is not an EPPSignedMark" );
			return false;
		}

		EPPSignedMark other = (EPPSignedMark) aObject;

		// Id
		if ( !EqualityUtil.equals( this.id, other.id ) ) {
			cat.error( "EPPSignedMark.equals(): id not equal" );
			return false;
		}

		// Issuer
		if ( !EqualityUtil.equals( this.issuer, other.issuer ) ) {
			cat.error( "EPPSignedMark.equals(): issuer not equal" );
			return false;
		}

		// Not Before
		if ( !EqualityUtil.equals( this.notBefore, other.notBefore ) ) {
			cat.error( "EPPSignedMark.equals(): notBefore not equal" );
			return false;
		}

		// Not After
		if ( !EqualityUtil.equals( this.notAfter, other.notAfter ) ) {
			cat.error( "EPPSignedMark.equals(): notAfter not equal" );
			return false;
		}

		// Mark
		if ( !EqualityUtil.equals( this.mark, other.mark ) ) {
			cat.error( "EPPSignedMark.equals(): mark not equal" );
			return false;
		}

		return true;
	}


	/**
	 * Gets the XML local name for the signed mark.
	 * 
	 * @return Either <code>ELM_SIGNED_MARK_LOCALNAME</code> or
	 *         <code>ELM_ENCODED_SIGNED_MARK_LOCALNAME</code>
	 */
	public String getLocalName () {
		return this.localName;
	}


	/**
	 * Gets the identifier of the signed mark.
	 * 
	 * @return The identifier for the signed mark if set; <code>null</code>
	 *         otherwise.
	 */
	public String getId () {
		return this.id;
	}


	/**
	 * Gets issuer of the signed mark.
	 * 
	 * @return The issuer of the signed mark if defined: <code>null</code>
	 *         otherwise.
	 */
	public EPPIssuer getIssuer () {
		return this.issuer;
	}


	/**
	 * Gets the date of creation of the signed mark.
	 * 
	 * @return the date of creation of the signed mark if set; <code>null</code>
	 *         otherwise.
	 */
	public Date getNotBefore () {
		return this.notBefore;
	}


	/**
	 * Gets the date of expiration of the signed mark.
	 * 
	 * @return the date of expiration of the signed mark if set; <code>null</code>
	 *         otherwise.
	 */
	public Date getNotAfter () {
		return this.notAfter;
	}


	/**
	 * Gets the mark associated with the signed mark.
	 * 
	 * @return The mark associated with the signed mark if defined:
	 *         <code>null</code> otherwise.
	 */
	public EPPMark getMark () {
		return this.mark;
	}


	/**
	 * Gets the &quot;id&quot; attribute value.
	 * 
	 * @return Value of the &quot;id&quot; attribute value.
	 */
	public String getAttrIdValue () {
		return this.attrIdValue;
	}


	/**
	 * Validate the signature attribute against the signed mark attributes given
	 * the <code>Signature</code> DOM <code>Element</code>.
	 * 
	 * @param aSigElm
	 *        DOM <code>Signature Element</code>
	 * @param aPublicKey
	 *        Public used to validate the signature
	 * @return <code>true</code> if valid; <code>false</code> otherwise.
	 */
	private boolean validate ( Element aSigElm, PublicKey aPublicKey ) {
		cat.debug( "validate(Element, PublicKey): enter" );

		boolean valid = false;

		try {
			DOMValidateContext valContext =
					new DOMValidateContext( aPublicKey, aSigElm );
			XMLSignature signature = sigFactory.unmarshalXMLSignature( valContext );

			if ( signature.validate( valContext ) ) {
				valid = true;
			}
			else {
				valid = false;
				cat.error( "validate(Element, PublicKey): validation status = "
						+ signature.getSignatureValue().validate( valContext ) );
				Iterator<?> i = signature.getSignedInfo().getReferences().iterator();

				for ( int j = 0; i.hasNext(); j++ ) {
					Reference next = (Reference) i.next();

					cat.error( "validate(Element, PublicKey): ref[" + j + "], URI = "
							+ next.getURI() + ", validity status = "
							+ next.validate( valContext ) );
				}
			}
		}
		catch ( Exception ex ) {
			cat
					.error( "validate(Element, PublicKey): Error validating the EPPSignedMark: "
							+ ex );
			valid = false;
		}

		cat.debug( "validate(Element, PublicKey): exit, valid = " + valid );
		return valid;
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	public String toString () {
		return EPPUtil.toString( this );
	}

}