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

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerFactory;


/**
 * Title: Description: Copyright:    Copyright (c) 2001 Company:      Verisign
 *
 * @author Colin Lloyd
 * @version 1.0
 */
public class EPPCatFactory {
	/** DOCUMENT ME! */
	private static EPPCatFactory instance = new EPPCatFactory();

	/** DOCUMENT ME! */
	private LoggerFactory factory = new DefaultFactory();

	/**
	 * Creates a new EPPCatFactory object.
	 */
	private EPPCatFactory() {
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public static EPPCatFactory getInstance() {
		return instance;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public LoggerFactory getFactory() {
		return factory;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param catFactory DOCUMENT ME!
	 */
	public void setFactory(LoggerFactory catFactory) {
		factory = catFactory;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author $author$
	 * @version $Revision: 1.2 $
	 */
	class DefaultFactory implements LoggerFactory {
		/**
		 * Creates a new DefaultFactory object.
		 */
		DefaultFactory() {
		}

		/**
		 * DOCUMENT ME!
		 *
		 * @param name DOCUMENT ME!
		 *
		 * @return DOCUMENT ME!
		 */
		public Logger makeNewLoggerInstance(String name) {
			return new DefaultLogger(name);
		}
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @author $author$
	 * @version $Revision: 1.2 $
	 */
	class DefaultLogger extends Logger {
		/**
		 * Creates a new DefaultLogger object.
		 *
		 * @param name DOCUMENT ME!
		 */
		DefaultLogger(String name) {
			super(name);
		}
	}
}
