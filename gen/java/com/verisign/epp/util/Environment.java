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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class loads properties from a properties configuration file with one of
 * the <code>envInitialize</code> methods. After the properties have been
 * loaded, the values can be retrieved by using one of the <code>get</code>
 * methods.
 */
public abstract class Environment {

	/**
	 * Properties loaded from the configuration file
	 */
	protected static Properties properties = new Properties();

	/**
	 * Loads configuration file properties into a <code>Properties</code> 
	 * attribute in the following search order:<br>
	 * <br><ol>
	 * <li>File system 
	 * <li>System classpath
	 * <li><code>Environment</code> class ClassLoader
	 * </ol>
	 * <br>
	 * If the configuration file can not be located, an
	 * <code>EnvException</code> is thrown.
	 * 
	 * @param aConfigFile -
	 *            File name of configuration file
	 * 
	 * @exception EnvException Error finding or loading 
	 * configuration file
	 */
	public void envInitialize(String aConfigFile) throws EnvException {
		InputStream myPropStream = null;
		File myPropFile = new File(aConfigFile);

		// File exists in file system?
		if (myPropFile.exists()) {
			// Read the configuration file
			try {
				myPropStream = new FileInputStream(aConfigFile);
			}
			catch (FileNotFoundException ex) {
				throw new EnvException("Env : " + aConfigFile
						+ " file not found " + ex.getMessage());
			}
			catch (SecurityException ex) {
				throw new EnvException(
						"Env : "
								+ aConfigFile
								+ " Security manager prevented from Reading the Configuration file "
								+ ex.getMessage());
			}
		}
		else { // Attempt to load file from classpath or custom ClassLoader
			myPropStream = ClassLoader.getSystemResourceAsStream(aConfigFile);

			if (myPropStream == null) {
				myPropStream = Environment.class.getClassLoader()
						.getResourceAsStream(aConfigFile);
			}
		}

		// Could not load the file?
		if (myPropStream == null) { throw new EnvException("Env : "
				+ aConfigFile + " file could not be loaded"); }

		properties = new Properties();

		try {
			properties.load(myPropStream);
		}
		catch (IOException ex) {
			throw new EnvException(
					"Env : IO Problem is Encountered in reading " + aConfigFile
							+ ex.getMessage());
		}

	}

	/**
	 * Loads configuration file properties into a <code>Properties</code> 
	 * attribute using a custom <code>ClassLoader</code>.:
	 * 
	 * @param aConfigFile -
	 *            File name of configuration file
	 * 
	 * @exception EnvException Error finding or loading 
	 * configuration file
	 */
	public void envInitialize(String aConfigFile, ClassLoader aClassLoader)
			throws EnvException {
		InputStream myPropStream = null;

		if (aClassLoader == null) { throw new EnvException(
				": ClassLoader parameter was null"); }

		InputStream configIs = aClassLoader.getResourceAsStream("/"
				+ aConfigFile);

		// Could not load the file?
		if (configIs == null) { throw new EnvException("Env : [" + aConfigFile
				+ "] file could not be loaded" + " with classloader ["
				+ aClassLoader + "]" + " not found in classpath\n"); }

		properties = new Properties();

		try {
			properties.load(configIs);
		}
		catch (IOException ex) {
			throw new EnvException(
					"Env : IO Problem is Encountered in reading " + aConfigFile
							+ ex.getMessage());
		}

	}

	/**
	 * This Method gets the given value from properties. It is to provide the
	 * higher layers easy access to properties
	 * 
	 * @param aProperty -
	 *            this String contains property name.
	 * 
	 * @return Trimmed property value
	 * 
	 * @exception EnvException
	 *                Property is not defined
	 */
	public static String getEnv(String aProperty) throws EnvException {

		String theValue = properties.getProperty(aProperty);

		if (theValue == null) { throw new EnvException(
				"Unable to Extract Environmental variable " + aProperty); }

		return theValue.trim();
	}

	/**
	 * This Method gets a property value, but will not throw an exception if it
	 * does not exist.
	 * 
	 * @param aProperty
	 *            Property name
	 * 
	 * @return Trimmed property value if defined; <code>null</code> otherwise
	 */
	public static String getOption(String aProperty) {
		String theValue = properties.getProperty(aProperty);

		if (theValue != null) { return theValue.trim(); }

		return theValue;
	}

	/**
	 * Gets a property loaded from the EPP configuration file. This method
	 * simply returns the raw property value with no trimming.
	 * 
	 * @param aProperty
	 *            Property name
	 * 
	 * @return Property value if defined; <code>null</code> otherwise
	 */
	public static String getProperty(String aProperty) {
		return properties.getProperty(aProperty);
	}

	/**
	 * Gets a property loaded from the EPP configuration file and if it doesn't
	 * exist, will return the passed default value (<code>aDefaultValue</code>).
	 * 
	 * @param aProperty
	 *            Property name
	 * @param aDefaultValue
	 *            Default value if property is not found
	 * @return Property value if defined; <code>aDefaultValue</code> otherwise
	 */
	public static String getProperty(String aProperty, String aDefaultValue) {
		return properties.getProperty(aProperty, aDefaultValue);
	}
	
	/**
	 * Sets the <code>Environment</code> properties using a client 
	 * <code>Properties</code> object.  This is an option to using 
	 * a configuration file with one of the <code>envInitialize</code>
	 * methods.
	 * 
	 * @param aProperties A set of EPP configuration properties
	 */
	public static void setProperties(Properties aProperties) {
		properties = aProperties;
	}
	
	/**
	 * Sets an individual property.  If <code>aValue</code> is <code>null</code> than 
	 * the property will be removed.
	 * 
	 * @param aProperty Name of property
	 * @param aValue Property value
	 */
	public static void setProperty(String aProperty, String aValue) {
		if (aValue != null)
			properties.setProperty(aProperty, aValue);
		else 
			properties.remove(aProperty);
	}
}