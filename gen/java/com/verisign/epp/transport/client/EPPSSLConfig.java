/***********************************************************
 Copyright (C) 2006 VeriSign, Inc.

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

package com.verisign.epp.transport.client;

import java.util.StringTokenizer;

import com.verisign.epp.exception.EPPException;

/**
 * The <code>EPPSSLConfig</code> class contains SSL configuration properties 
 * that can be used in conjunction with {@link EPPSSLImpl#initialize(EPPSSLConfig)} 
 * to initialize an {@link EPPSSLContext}.  The required properties include:<br>
 * <br><ol>
 * <li><code>sslProtocol</code> that defines the SSL protocol to use.  For example, 
 * &quot;TLS&quot;.
 * <li><code>identityStoreType</code> that defines the type of the identity KeyStore.  
 * For example, &quot;JKS&quot;. 
 * <li><code>identityFileName</code> that defines the name of the identity KeyStore file.  
 * For example, &quot;identity.jks&quot;.
 * <li><code>identityPassPhrase</code> that defines the passphrase/password to access 
 * the identity KeyStore file defined by the <code>identityFileName</code> property.  
 * </ol>
 * <br>
 * The optional properties include:<br>
 * <br><ol>
 * <li><code>identityKeyPassPhrase</code> that defines the passphrase/password for the 
 * private key stored in the identity KeyStore.  If not defined, the value of the 
 * <code>identityPassPhrase</code> will be used.
 * <li><code>trustStoreType</code> that defines the KeyStore type of the Trust Store.  
 * This is only required if the Trust Store is defined by the <code>trustStoreFileName</code> 
 * property.  For example, &quot;JKS&quot;.
 * <li><code>trustStoreFileName</code> that defines the name of the Trust Store file.  
 * For example, &quot;trust.jks&quot;.  If note defined, the default JDK Trust Store 
 * will be used that is located at the path <code>$JAVA_HOME/lib/security/cacerts</code>.
 * <li><code>trustStorePassPhrase</code> that defines the passphrase/password to access 
 * the identity KeyStore file defined by the <code>trustStoreFileName</code> property.  
 * This is only required if the Trust Store is defined by the <code>trustStoreFileName</code> 
 * property.   
 * <li><code>sslDebug</code> that defines that value of the SSL debug Java system property
 * <code>javax.net.debug</code>.  If not set, than the <code>javax.net.debug</code> 
 * system property will not set.  The possible values include &quot;none&quot; and 
 * &quot;all&quot; and since it sets a Java system property it will global apply across all
 * SSL connections om the Java process.   
 * <li><code>sslEnabledProtocols</code> that defines the support SSL protocols supported.  
 * If not defined, the default protocols provided by the JSSE provider will be used.  
 * For example, {&quot;TLSv1&quot;, &quot;SSLv3&quot;}.
 * <li><code>sslEnabledCipherSuites</code> that defines the support SSL cipher suites supported.  
 * If not defined, the default cipher suites provided by the JSSE provider will be used.  
 * For example, <code>SSL_RSA_WITH_RC4_128_MD5 SSL_RSA_WITH_RC4_128_SHA</code>. 
 * </ol>
 * 
 * @see EPPSSLImpl
 * @see EPPSSLContext
  */
public class EPPSSLConfig {
	
	/**
	 * Defines the SSL protocol to use.  For example, &quot;TLS&quot;
	 */
	private String sslProtocol = null;
	
	/**
	 * Defines the type of the identity KeyStore.  For example, &quot;JKS&quot;. 
	 */
	private String identityStoreType=null;
	
	/**
	 * Defines the name of the identity KeyStore file.  For example, &quot;identity.jks&quot;
	 */
	private String identityFileName=null;
	
	/**
	 * Defines the passphrase/password to access 
	 * the identity KeyStore file defined by the 
	 * <code>identityFileName</code> property.
	 */
	private String identityPassPhrase=null;
	
	/**
	 * Defines the passphrase/password for the 
	 * private key stored in the identity KeyStore.  If not defined, the value of the 
	 * <code>identityPassPhrase</code> will be used.
	 */
	private String identityKeyPassPhrase=null;
	
	/**
	 * Defines the KeyStore type of the Trust Store.  
	 * This is only required if the Trust Store is defined by the <code>trustStoreFileName</code> 
	 * property.  For example, &quot;JKS&quot;.
	 */
	private String trustStoreType = null;
		
	/**
	 * Defines the name of the Trust Store file.  
	 * For example, &quot;trust.jks&quot;.  If note defined, the default JDK Trust Store 
	 * will be used that is located at the path <code>$JAVA_HOME/lib/security/cacerts</code>.
	 */
	private String trustStoreFileName = null;
	
	/**
	 * Defines the passphrase/password to access 
	 * the Trust Store file defined by the <code>trustStoreFileName</code> property.  
	 * This is only required if the Trust Store is defined by the <code>trustStoreFileName</code> 
	 * property.
	 */
	private String trustStorePassPhrase = null;
	
	/**defines that value of the SSL debug Java system property
	 * <code>javax.net.debug</code>.  If not set, than the <code>javax.net.debug</code> 
	 * system property will not set.
	 */
	private String sslDebug = null;

	/**
	 * Defines the SSL protocols enabled.  
	 * If not defined, the default protocols provided by the JSSE provider will be used.  
	 * For example, {&quot;TLSv1&quot;, &quot;SSLv3&quot;}.	 */
	private String[] sslEnabledProtocols = null;
	
	/**
	 * Defines the support SSL cipher suites enabled.  
	 * If not defined, the default cipher suites provided by the JSSE provider will be used.  
	 * For example, {&quot;SSL RSA EXPORT WITH RC4 40 MD5quot;, &quot;SSL RSA WITH RC4 128 MD5&quot;}.	 */
	private String[] sslEnabledCipherSuites = null;
	
	
	/**
	 * Default constructor. 
	 */
	public EPPSSLConfig() {
		
	}
	
	/**
	 * Creates an instance of <code>EPPSSLConfig</code> that takes the required set of 
	 * attributes.
	 * 
	 * @param aSslProtocol SSL Protocol like &quot;TLS&quot;
	 * @param aIdentityStoreType Identity store type like &quot;JKS&quot;
	 * @param aIdentityFileName Identity store file name 
	 * @param aIdentityPassPhrase Identity store passphrase/password
	 */
	public EPPSSLConfig(String aSslProtocol, String aIdentityStoreType, String aIdentityFileName, 
			String aIdentityPassPhrase) {
		this.sslProtocol = aSslProtocol;
		this.identityStoreType = aIdentityStoreType;
		this.identityFileName = aIdentityFileName;
		this.identityPassPhrase = aIdentityPassPhrase;
	}
	
	/** 
	 * Gets the Identity File Name <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getIdentityFileName() {
		return this.identityFileName;
	}

	/** 
	 * Sets the Identity File Name <code>String</code>.
	 * 
	 * @param aIdentityFileName <code>String</code>. 
	 */
	public void setIdentityFileName(String aIdentityFileName) {
		this.identityFileName = aIdentityFileName;
	}

	/** 
	 * Gets the Identity Key Pass Phrase <code>String</code> using the 
	 * Identity Pass Phrase as the default value.  
	 * 
	 * @return Identity Key Pass Phrase if set; otherwise the Identity Pass Phrase
	 */
	public String getIdentityKeyPassPhrase() {
		if (this.identityKeyPassPhrase == null) {
			return this.identityPassPhrase;
		}
		else 
			return this.identityKeyPassPhrase;
	}
	
	/** 
	 * Gets the Identity Key Pass Phrase as <code>char[]</code> using the 
	 * Identity Pass Phrase as the default value.  
	 * 
	 * @return Identity Key Pass Phrase if set; otherwise the Identity Pass Phrase as <code>char[]</code>
	 */
	public char[] getIdentityKeyPassPhraseCharArray() {
		String theIdentityKeyPassPhrase = this.getIdentityKeyPassPhrase();
		if (theIdentityKeyPassPhrase == null) {
			return null;
		}
		else {
			return theIdentityKeyPassPhrase.toCharArray();
		}
	}
	

	/** 
	 * Sets the Identity Key Pass Phrase <code>String</code>.
	 * 
	 * @param aIdentityKeyPassPhrase <code>String</code>. 
	 */
	public void setIdentityKeyPassPhrase(String aIdentityKeyPassPhrase) {
		this.identityKeyPassPhrase = aIdentityKeyPassPhrase;
	}

	/** 
	 * Gets the Identity Pass Phrase <code>String</code>.
	 * 
	 * @return Identity Pass Phrase if set; <code>null</code> otherwise.
	 */
	public String getIdentityPassPhrase() {
		return this.identityPassPhrase;
	}
	
	/** 
	 * Gets the Identity Pass Phrase as a <code>char[]</code>.
	 * 
	 * @return Identity Pass Phrase if set; <code>null</code> otherwise.
	 */
	public char[] getIdentityPassPhraseCharArray() {
		if (this.identityPassPhrase == null) {
			return null;
		}
		else {
			return this.identityPassPhrase.toCharArray();
		}
	}

	/** 
	 * Sets the Identity Pass Phrase <code>String</code>.
	 * 
	 * @param aIdentityPassPhrase <code>String</code>. 
	 */
	public void setIdentityPassPhrase(String aIdentityPassPhrase) {
		this.identityPassPhrase = aIdentityPassPhrase;
	}

	/** 
	 * Gets the Identity Store Type <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getIdentityStoreType() {
		return this.identityStoreType;
	}

	/** 
	 * Sets the Identity Store Type <code>String</code>.
	 * 
	 * @param aIdentityStoreType <code>String</code>. 
	 */
	public void setIdentityStoreType(String aIdentityStoreType) {
		this.identityStoreType = aIdentityStoreType;
	}

	/** 
	 * Gets the SSL Debug <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getSslDebug() {
		return this.sslDebug;
	}

	/** 
	 * Sets the SSL Debug <code>String</code>.
	 * 
	 * @param aSslDebug <code>String</code>. 
	 */
	public void setSslDebug(String aSslDebug) {
		this.sslDebug = aSslDebug;
	}

	/** 
	 * Gets the SSL Protocol <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getSslProtocol() {
		return this.sslProtocol;
	}

	/** 
	 * Sets the SSL Protocol <code>String</code>.
	 * 
	 * @param aSslProtocol <code>String</code>. 
	 */
	public void setSslProtocol(String aSslProtocol) {
		this.sslProtocol = aSslProtocol;
	}
	
	/**
	 * Sets the required Trust Store properties if the Trust Store 
	 * is explicitely set.  
	 * 
	 * @param aTrustStoreType Keystore type of  the Trust Store like &quot;JKS&quot;
	 * @param aTrustStoreFileName Trust Store file name
	 * @param aTrustStorePassPhrase Trust Store passphrase/password
	 */
	public void setTrustStore(String aTrustStoreType, String aTrustStoreFileName, String aTrustStorePassPhrase) {
		this.trustStoreType = aTrustStoreType;
		this.trustStoreFileName = aTrustStoreFileName;
		this.trustStorePassPhrase = aTrustStorePassPhrase;
	}
	
	/** 
	 * Gets the Trust Store Type <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getTrustStoreType() {
		return this.trustStoreType;
	}

	/** 
	 * Sets the Trust Store Type <code>String</code>.
	 * 
	 * @param aTrustStoreType <code>String</code>. 
	 */
	public void setTrustStoreType(String aTrustStoreType) {
		this.trustStoreType = aTrustStoreType;
	}

	/** 
	 * Gets the Trust Store File Name <code>String</code>.
	 * 
	 * @return <code>String</code> if set; <code>null</code> otherwise.
	 */
	public String getTrustStoreFileName() {
		return this.trustStoreFileName;
	}

	/** 
	 * Sets the Trust Store File Name <code>String</code>.
	 * 
	 * @param aTrustStoreFileName <code>String</code>. 
	 */
	public void setTrustStoreFileName(String aTrustStoreFileName) {
		this.trustStoreFileName = aTrustStoreFileName;
	}

	/** 
	 * Gets the Trust Store Pass Phrase <code>String</code>.
	 * 
	 * @return Trust Store Pass Phrase if set; <code>null</code> otherwise.
	 */
	public String getTrustStorePassPhrase() {
		return this.trustStorePassPhrase;
	}
	
	/** 
	 * Gets the Trust Store Pass Phrase as <code>char[]</code>.
	 * 
	 * @return Trust Store Pass Phrase if set; <code>null</code> otherwise.
	 */
	public char[] getTrustStorePassPhraseCharArray() {
		if (this.trustStorePassPhrase == null) {
			return null;
		}
		else {
			return this.trustStorePassPhrase.toCharArray();
		}
	}
	

	/** 
	 * Sets the Trust Store Pass Phrase <code>String</code>.
	 * 
	 * @param aTrustStorePassPhrase <code>String</code>. 
	 */
	public void setTrustStorePassPhrase(String aTrustStorePassPhrase) {
		this.trustStorePassPhrase = aTrustStorePassPhrase;
	}
		
	
	/**
	 * Gets the optional SSL enabled protocols <code>String</code> array.  
	 * 
	 * @return <code>>String</code> array if set; <code>null</code> otherwise.
	 */
	public String[] getSSLEnabledProtocols() {
		return this.sslEnabledProtocols;
	}


	/**
	 * Sets the optional SSL enabled protocols <code>String</code> array.
	 * 
	 * @param aSslEnabledProtocols <code>String</code> array of enabled SSL protocols
	 */
	public void setSSLEnabledProtocols(String[] aSslEnabledProtocols) {
		this.sslEnabledProtocols = aSslEnabledProtocols;
	}
	
	/**
	 * Sets the optional SSL enabled protocols using a space delimited list of 
	 * protocols.
	 * 
	 * @param aSslEnabledProtocols space delimited list of enabled SSL protocols
	 */
	public void setSSLEnabledProtocols(String aSslEnabledProtocols) {
		if (aSslEnabledProtocols != null) {
			StringTokenizer theTokenizer = new StringTokenizer(aSslEnabledProtocols);
			int theNumTokens = theTokenizer.countTokens();
			this.sslEnabledProtocols = new String[theNumTokens];
			for (int i = 0; i <  theNumTokens && theTokenizer.hasMoreTokens(); i++) {
				this.sslEnabledProtocols[i] = theTokenizer.nextToken();
			}	
		}
		else {
			this.sslEnabledProtocols = null;
		}
	}
	
	/**
	 * Gets the optional SSL enabled cipher suites <code>String</code> array.  
	 * 
	 * @return <code>>String</code> array if set; <code>null</code> otherwise.
	 */
	public String[] getSSLEnabledCipherSuites() {
		return this.sslEnabledCipherSuites;
	}
	
	
	/**
	 * Sets the optional SSL enabled cipher suites <code>String</code> array.
	 * 
	 * @param aSslEnabledCipherSuites <code>String</code> array of enabled SSL cipher suites
	 */
	public void setSSLEnabledCipherSuites(String[] aSslEnabledCipherSuites) {
		this.sslEnabledCipherSuites = aSslEnabledCipherSuites;
	}
	
	/**
	 * Sets the optional SSL enabled cipher suites using a space delimited list of 
	 * cipher suites.
	 * 
	 * @param aSslEnabledCipherSuites space delimited list of enabled SSL cipher suites
	 */
	public void setSSLEnabledCipherSuites(String aSslEnabledCipherSuites) {
		if (aSslEnabledCipherSuites != null) {
			StringTokenizer theTokenizer = new StringTokenizer(aSslEnabledCipherSuites);
			int theNumTokens = theTokenizer.countTokens();
			this.sslEnabledCipherSuites = new String[theNumTokens];
			for (int i = 0; i <  theNumTokens && theTokenizer.hasMoreTokens(); i++) {
				this.sslEnabledCipherSuites[i] = theTokenizer.nextToken();
			}	
		}
		else {
			this.sslEnabledCipherSuites = null;
		}
	}
	
	
	/**
	 * Validates the properties of the <code>EPPSSLConfig</code> by checking 
	 * that the required properties are set. 
	 * 
	 * @throws EPPException On error
	 */
	public void validate() throws EPPException {
		
		// Check the required properties
		if (this.sslProtocol == null) {
			throw new EPPException("EPPSSLConfig: sslProtocol property is required");
		}

		if (this.identityStoreType == null) {
			throw new EPPException("EPPSSLConfig: identityStoreType property is required");
		}

		if (this.identityFileName == null) {
			throw new EPPException("EPPSSLConfig: identityFileName property is required");
		}

		if (this.identityPassPhrase == null) {
			throw new EPPException("EPPSSLConfig: identityPassPhrase property is required");
		}
		
	}
	
	/**
	 * Output the contents of the <code>EPPSSLConfig</code> instance 
	 * which includes a comma seperated list of <code>EPPSSLConfig</code> 
	 * properties with the values.  For example, &quot;sslProtocol = &lt;value&gt;&quot;.
	 * 
	 * @return <code>String</code> containing the <code>EPPSSLConfig</code> 
	 * properties and values
	 */
	public String toString() {
		StringBuffer theBuf = new StringBuffer();
		
		theBuf.append("sslProtocol = " + this.sslProtocol);
		theBuf.append(", identityStoreType = " + this.identityStoreType);
		theBuf.append(", identityFileName = " + this.identityFileName);
		theBuf.append(", identityPassPhrase = " + this.identityPassPhrase);
		theBuf.append(", identityKeyPassPhrase = " + this.identityKeyPassPhrase);
		theBuf.append(", trustStoreStoreType = " + this.trustStoreType);
		theBuf.append(", trustStoreFileName = " + this.trustStoreFileName);
		theBuf.append(", trustStorePassPhrase = " + this.trustStorePassPhrase);
		
		if (this.sslEnabledProtocols != null) {
			theBuf.append(", sslEnabledProtocols = [");
			for (int i = 0; i < this.sslEnabledProtocols.length; i++) {
				if (i != 0) {
					theBuf.append(" ");
				}
				theBuf.append(this.sslEnabledProtocols[i]);
			}
			theBuf.append("]");
		}
		else {
			theBuf.append(", sslEnabledProtocols = null");			
		}
		
		if (this.sslEnabledCipherSuites != null) {
			theBuf.append(", sslEnabledCipherSuites = [");
			for (int i = 0; i < this.sslEnabledCipherSuites.length; i++) {
				if (i != 0) {
					theBuf.append(" ");
				}
				theBuf.append(this.sslEnabledCipherSuites[i]);
			}
			theBuf.append("]");
		}
		else {
			theBuf.append(", sslEnabledCipherSuites = null");			
		}
		
		theBuf.append(", sslDebug = " + this.sslDebug);
		
		return theBuf.toString();
	}

}
