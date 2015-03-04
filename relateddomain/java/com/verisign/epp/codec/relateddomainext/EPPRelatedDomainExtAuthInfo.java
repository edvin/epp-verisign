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

package com.verisign.epp.codec.relateddomainext;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents authorization information which is a shared structure been used by
 * other mapping such as domain and contact mappings. This object structure is a
 * direct mapping from the data type <code>authInfo</code> in the EPP Shared
 * Structure Schema (with the name space <code>eppcom</code>).
 */
public class EPPRelatedDomainExtAuthInfo implements
		com.verisign.epp.codec.gen.EPPCodecComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1672662152727579147L;

	/** password auth info type */
	public final static short TYPE_PW = 0;

	/** Extensible auth info type. */
	public final static short TYPE_EXT = 1;

	/** XML Element name of <code>EPPRelatedDomainExtAuthInfo</code> root element. */
	final static String ELM_NAME = "relDom:authInfo";

	/** XML Element name password authorization type */
	protected final static String ELM_PW = "pw";

	/** XML Element name extensible authorization type */
	protected final static String ELM_EXT = "ext";

	/**
	 * XML Element roid attribute name of <code>EPPRelatedDomainExtAuthInfo</code>
	 * root element.
	 */
	protected final static String ATTR_ROID = "roid";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(
			EPPRelatedDomainExtAuthInfo.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/** Password authorization information. */
	protected String password = null;

	/** Extension authorization information. */
	protected EPPCodecComponent ext = null;

	/** type, and default value is <code>TYPE_PW</code> */
	protected short type = TYPE_PW;

	/** roid. */
	protected String roid = null;

	/**
	 * Root mapping name such as domain or contact (e.g.
	 * <code>domain:authInfo</code> for domain mapping). This attribute needs to
	 * be specified before calling encode(Document) method.
	 */
	private String rootName = ELM_NAME;

	/**
	 * XML namespace URI for the root element.
	 */
	private String rootNS = EPPRelatedDomainExtFactory.NS;


	/**
	 * Default constructor that must have the password or extension attributes set
	 * before calling <code>encode</code>.
	 */
	public EPPRelatedDomainExtAuthInfo () {
	}


	/**
	 * Constructor that takes just the authorization password.
	 * 
	 * @param aPassword
	 *        Authorization password
	 */
	public EPPRelatedDomainExtAuthInfo ( final String aPassword ) {
		this.setPassword( aPassword );
	}


	/**
	 * Constructor that takes the root element and the authorization password.
	 * 
	 * @param aRootNS
	 *        Root element namespace URI
	 * @param aRootName
	 *        Root element of auth info.
	 * @param aPassword
	 *        Authorization password
	 */
	public EPPRelatedDomainExtAuthInfo ( final String aRootNS,
			final String aRootName, final String aPassword ) {
		this.setRootName( aRootNS, aRootName );
		this.setPassword( aPassword );
	}


	/**
	 * Constructor that takes the root element, the authorization password, and
	 * the roid.
	 * 
	 * @param aRootNS
	 *        Root element namespace URI
	 * @param aRootName
	 *        Root element of auth info.
	 * @param aRoid
	 *        Roid of the Registrant
	 * @param aPassword
	 *        Authorization password
	 */
	public EPPRelatedDomainExtAuthInfo ( final String aRootNS,
			final String aRootName, final String aRoid, final String aPassword ) {
		this.setRootName( aRootNS, aRootName );
		this.roid = aRoid;
		this.setPassword( aPassword );
	}


	/**
	 * Constructor that takes just the authorization extension.
	 * 
	 * @param aExt
	 *        Extension authorization element
	 */
	public EPPRelatedDomainExtAuthInfo ( final EPPCodecComponent aExt ) {
		this.setExt( aExt );
	}


	/**
	 * Constructor that takes a root elemeent and the authorization extension.
	 * 
	 * @param aRootNS
	 *        Root element namespace URI
	 * @param aRootName
	 *        Root element of auth info.
	 * @param aExt
	 *        Extension authorization element
	 */
	public EPPRelatedDomainExtAuthInfo ( final String aRootNS,
			final String aRootName, final EPPCodecComponent aExt ) {
		this.setRootName( aRootNS, aRootName );
		this.setExt( aExt );
	}


	/**
	 * Get Registry Object IDentifier (ROID).
	 * 
	 * @return Registry Object IDentifier (ROID)
	 */
	public String getRoid () {
		return this.roid;
	}


	// End EPPRelatedDomainExtAuthInfo.getRoid()

	/**
	 * Set Registry Object IDentifier (ROID).
	 * 
	 * @param aRoid
	 *        The Registry Object IDentifier (ROID) value.
	 */
	public void setRoid ( final String aRoid ) {
		this.roid = aRoid;
	}


	// End EPPRelatedDomainExtAuthInfo.SetRoid()

	/**
	 * Gets the root element XML namespace URI.
	 * 
	 * @return root element XML namespace URI
	 */
	public String getRootNS () {
		return this.rootNS;
	}


	/**
	 * Get root name such as domain or contact.
	 * 
	 * @return String
	 */
	public String getRootName () {
		return this.rootName;
	}


	/**
	 * Set root name and XML namespace.
	 * 
	 * @param aRootNS
	 *        Root element namespace URI
	 * @param newRootName
	 *        String
	 */
	public void setRootName ( final String aRootNS, final String newRootName ) {
		this.rootNS = aRootNS;
		this.rootName = newRootName;
	}


	/**
	 * Gets the password authorization information.
	 * 
	 * @return Password
	 * @deprecated Replaced by {@link #getPassword()}.
	 */
	@Deprecated
	public String getAuthInfo () {
		return this.password;
	}


	/**
	 * Gets the password authorization information.
	 * 
	 * @return Authorization password
	 */
	public String getPassword () {
		return this.password;
	}


	/**
	 * Sets the password authorization information.
	 * 
	 * @param aPassword
	 *        Authorization password
	 */
	public void setPassword ( final String aPassword ) {
		this.password = aPassword;
		this.type = TYPE_PW;
	}


	/**
	 * Gets the extension authorization.
	 * 
	 * @return Authorization extension
	 */
	public EPPCodecComponent getExt () {
		return this.ext;
	}


	/**
	 * Sets the extension authorization information.
	 * 
	 * @param aExt
	 *        Authorization extension
	 */
	public void setExt ( final EPPCodecComponent aExt ) {
		this.ext = aExt;
		this.type = TYPE_EXT;
	}


	/**
	 * Sets the password authorization information.
	 * 
	 * @param aPassword
	 *        Authorization password
	 * @deprecated Replaced by {@link #setPassword(String)}.
	 */
	@Deprecated
	public void setAuthInfo ( final String aPassword ) {
		this.password = aPassword;
	}


	/**
	 * Get the type of the auth info.
	 * 
	 * @return One of the <code>TYPE_</code> constants.
	 */
	public short getType () {
		return this.type;
	}


	/**
	 * Set auth info type. The default value is <code>TYPE_PW</code>.
	 * 
	 * @param aType
	 *        One of the <code>TYPE_</code> constants
	 */
	public void setType ( final short aType ) {
		this.type = aType;
	}


	/**
	 * Clone <code>EPPRelatedDomainExtAuthInfo</code>.
	 * 
	 * @return clone of <code>EPPRelatedDomainExtAuthInfo</code>
	 * @exception CloneNotSupportedException
	 *            standard Object.clone exception
	 */
	@Override
	public Object clone () throws CloneNotSupportedException {
		EPPRelatedDomainExtAuthInfo clone = null;

		clone = (EPPRelatedDomainExtAuthInfo) super.clone();

		if ( this.ext != null ) {
			clone.ext = (EPPCodecComponent) this.ext.clone();
		}

		return clone;
	}


	/**
	 * Decode the EPPRelatedDomainExtAuthInfo attributes from the aElement DOM
	 * Element tree.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode EPPDomainContact from.
	 * @exception EPPDecodeException
	 *            - Unable to decode aElement.
	 */
	public void decode ( final Element aElement ) throws EPPDecodeException {
		// root name
		this.setRootName( aElement.getNamespaceURI(), aElement.getTagName() );

		final Element theTypeElm = EPPUtil.getFirstElementChild( aElement );

		// password provided?
		if ( theTypeElm == null ) {
			throw new EPPDecodeException(
					"EPPRelatedDomainExtAuthInfo.decode could not find type child element" );
		}
		else if ( theTypeElm.getLocalName().equals( ELM_PW ) ) {
			this.type = TYPE_PW;

			// Get password value
			final Node textNode = theTypeElm.getFirstChild();
			if ( textNode != null ) {
				this.password = textNode.getNodeValue();
			}
			else {
				this.password = "";
			}

			// Get roid
			if ( theTypeElm.getAttribute( ATTR_ROID ).equals( "" ) ) {
				this.roid = null;
			}
			else {
				this.roid = theTypeElm.getAttribute( ATTR_ROID );
			}
		} // extension type
		else if ( theTypeElm.getLocalName().equals( ELM_EXT ) ) {
			this.type = TYPE_EXT;

			final Element theExtElm = EPPUtil.getFirstElementChild( theTypeElm );

			// Create extension
			try {
				this.ext = EPPFactory.getInstance().createExtension( theExtElm );
			}
			catch ( final EPPCodecException e ) {
				throw new EPPDecodeException(
						"EPPRelatedDomainExtAuthInfo.decode unable to create authInfo extension object: "
								+ e );
			}
			this.ext.decode( theExtElm );
		}
		else {
			throw new EPPDecodeException(
					"EPPRelatedDomainExtAuthInfo.decode invalid type child element tag name of "
							+ theTypeElm.getTagName() );
		}
	}


	/**
	 * Encode a DOM Element tree from the attributes of the
	 * EPPRelatedDomainExtAuthInfo instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return Element - Root DOM Element representing the
	 *         EPPRelatedDomainExtAuthInfo instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode EPPRelatedDomainExtAuthInfo instance.
	 */
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		final Element root =
				aDocument
						.createElementNS( EPPRelatedDomainExtFactory.NS, this.rootName );

		String nsPrefix = EPPUtil.getPrefix( this.rootName );
		if ( nsPrefix.length() != 0 ) {
			nsPrefix = nsPrefix + ":";
		}

		switch (this.type) {
		case TYPE_PW:

			if ( this.password == null ) {
				throw new EPPEncodeException(
						"EPPRelatedDomainExtAuthInfo: password is null on call to encode" );
			}

			final Element thePasswordElm =
					aDocument.createElementNS( this.rootNS, nsPrefix + ELM_PW );
			thePasswordElm.appendChild( aDocument.createTextNode( this.password ) );

			// roid
			if ( this.roid != null ) {
				thePasswordElm.setAttribute( ATTR_ROID, this.roid );
			}

			root.appendChild( thePasswordElm );

			break;

		case TYPE_EXT:

			if ( this.ext == null ) {
				throw new EPPEncodeException(
						"EPPRelatedDomainExtAuthInfo: ext is null on call to encode" );
			}

			final Element theExtElm =
					aDocument.createElementNS( this.rootNS, nsPrefix + ELM_EXT );
			EPPUtil.encodeComp( aDocument, theExtElm, this.ext );
			root.appendChild( theExtElm );

			break;

		default:
			throw new EPPEncodeException( "EPPRelatedDomainExtAuthInfo: invalid type"
					+ this.type );
		}

		return root;
	}


	// End EPPRelatedDomainExtAuthInfo.encode(Document)

	/**
	 * implements a deep <code>EPPRelatedDomainExtAuthInfo</code> compare.
	 * 
	 * @param aObject
	 *        <code>EPPRelatedDomainExtAuthInfo</code> instance to compare with
	 * @return <code>true</code> if equal; <code>false</code> otherwise
	 */
	@Override
	public boolean equals ( final Object aObject ) {
		if ( !(aObject instanceof EPPRelatedDomainExtAuthInfo) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): "
					+ aObject.getClass().getName()
					+ " not EPPRelatedDomainExtAuthInfo instance" );

			return false;
		}

		final EPPRelatedDomainExtAuthInfo theComp =
				(EPPRelatedDomainExtAuthInfo) aObject;

		// Root namespace
		if ( !this.rootNS.equals( theComp.rootNS ) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): rootNS not equal" );

			return false;
		}

		// Root name
		if ( !EPPUtil.getLocalName( this.rootName ).equals(
				EPPUtil.getLocalName( theComp.rootName ) ) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): rootName not equal" );

			return false;
		}

		// password
		if ( !((this.password == null) ? (theComp.password == null) : this.password
				.equals( theComp.password )) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): password not equal" );

			return false;
		}

		// ext
		if ( !((this.ext == null) ? (theComp.ext == null) : this.ext
				.equals( theComp.ext )) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): ext not equal" );

			return false;
		}

		// type
		if ( this.type != theComp.type ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): type not equal" );

			return false;
		}

		// roid
		if ( !((this.roid == null) ? (theComp.roid == null) : this.roid
				.equals( theComp.roid )) ) {
			cat.error( "EPPRelatedDomainExtAuthInfo.equals(): roid not equal" );

			return false;
		}

		return true;
	}


	// End EPPRelatedDomainExtAuthInfo.equals(Object)

	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful; <code>ERROR</code>
	 *         otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}

	// End EPPRelatedDomainExtAuthInfo.toString()
}

// End class EPPRelatedDomainExtAuthInfo
