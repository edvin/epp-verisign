/***********************************************************
Copyright (C) 2013 VeriSign, Inc.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/
package com.verisign.epp.util;

import java.util.Properties;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;

import org.apache.log4j.Logger;

/**
 * Wrapper class for the {@link javax.xml.transform.Transformer} class for
 * inclusion in a <code>GenericPoolManager</code>. The pool name used for
 * instances to <code>EPPTransformer</code> is defined by the {@link #POOL}
 * constant. The <code>EPPTransformer</code> creates an embedded instance of
 * {@link javax.xml.transform.Transformer} that is delegated all of the method
 * calls.
 */
public class EPPTransformer extends Transformer {
	/**
	 * Name of the EPPTransformer Pool managed by
	 * <code>GenericPoolManager</code>.
	 */
	public static final String POOL = "EPP_XML_TRANSFORMER_POOL";

	/** Log4j category for logging */
	private static Logger cat = Logger.getLogger(
			EPPTransformer.class.getName(), EPPCatFactory.getInstance()
					.getFactory());

	/**
	 * The Transformer implementation that EPPTransformer delegates to for the
	 * real work
	 */
	private Transformer transformerImpl = null;

	/**
	 * Create a new instance of EPPTransformer.
	 */
	public EPPTransformer() {
		cat.debug("EPPTransformer(): enter");
		TransformerFactory transFac = TransformerFactory.newInstance();
		try {
			this.transformerImpl = transFac.newTransformer();
		}
		catch (TransformerConfigurationException e) {
			cat.error("EPPTransformer(): Couldn't instantiate transformer instance", e);
		}
		cat.debug("EPPTransformer(): exit");
	}

	/**
	 * Wrapper for the {@link javax.xml.transform.Transformer#clearParameters()}
	 * method.
	 */
	public void clearParameters() {
		this.transformerImpl.clearParameters();

	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#getErrorListener()} method.
	 * 
	 * @return <code>ErrorListener</code> in embedded
	 *         {@link javax.xml.transform.Transformer}
	 */
	public ErrorListener getErrorListener() {
		return this.transformerImpl.getErrorListener();
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#getOutputProperties()} method.
	 * 
	 * @return <code>Properties</code> in embedded
	 *         {@link javax.xml.transform.Transformer}
	 */
	public Properties getOutputProperties() {
		return this.transformerImpl.getOutputProperties();
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#getOutputProperty(java.lang.String)}
	 * method.
	 * 
	 * @param aName
	 *            Output property name
	 * 
	 * @return <code>String</code> in embedded
	 *         {@link javax.xml.transform.Transformer}
	 */
	public String getOutputProperty(String aName)
			throws IllegalArgumentException {
		return this.transformerImpl.getOutputProperty(aName);
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#getParameter(java.lang.String)}
	 * method.
	 * 
	 * @param aName
	 *            Parameter name
	 * 
	 * @return <code>Object</code> in embedded
	 *         {@link javax.xml.transform.Transformer}
	 */
	public Object getParameter(String aName) {
		return this.transformerImpl.getParameter(aName);
	}

	/**
	 * Wrapper for the {@link javax.xml.transform.Transformer#getURIResolver()}
	 * method.
	 * 
	 * @return <code>URIResolver</code> in embedded
	 *         {@link javax.xml.transform.Transformer}
	 */
	public URIResolver getURIResolver() {
		return this.transformerImpl.getURIResolver();
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#setErrorListener(javax.xml.transform.ErrorListener)}
	 * method.
	 * 
	 * @param aListener
	 *            <code>ErrorListener</code> instance
	 */
	public void setErrorListener(ErrorListener aListener) {
		this.transformerImpl.setErrorListener(aListener);
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#setOutputProperties(java.util.Properties)}
	 * method.
	 * 
	 * @param aOFormat
	 *            <code>Properties</code> instance
	 */
	public void setOutputProperties(Properties aOFormat)
			throws IllegalArgumentException {
		this.transformerImpl.setOutputProperties(aOFormat);
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#setOutputProperty(java.lang.String, java.lang.String)}
	 * method.
	 * 
	 * @param aName
	 *            Output property name
	 * @param aValue
	 *            Output property value
	 */
	public void setOutputProperty(String aName, String aValue)
			throws IllegalArgumentException {
		this.setOutputProperty(aName, aValue);
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#setParameter(java.lang.String, java.lang.Object)}
	 * method.
	 * 
	 * @param aName
	 *            Property name
	 * @param aValue
	 *            Property value
	 */
	public void setParameter(String aName, Object aValue) {
		this.setParameter(aName, aValue);
	}

	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#setURIResolver(javax.xml.transform.URIResolver)}
	 * method.
	 * 
	 * @param aResolver
	 *            URI resolver
	 */
	public void setURIResolver(URIResolver aResolver) {
		this.setURIResolver(aResolver);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.transform.Transformer#transform(javax.xml.transform.Source,
	 * javax.xml.transform.Result)
	 */
	/**
	 * Wrapper for the
	 * {@link javax.xml.transform.Transformer#transform(javax.xml.transform.Source, javax.xml.transform.Result)}
	 * method.
	 * 
	 * @param aXmlSource
	 *            XML source
	 * @param aOutputTarget
	 *            Target if transformer
	 * 
	 * @throws TransformerException
	 *             IF an unrecoverable error occurs during the course of the
	 *             transformation.
	 */
	public void transform(Source aXmlSource, Result aOutputTarget)
			throws TransformerException {
		this.transformerImpl.transform(aXmlSource, aOutputTarget);
	}
}
