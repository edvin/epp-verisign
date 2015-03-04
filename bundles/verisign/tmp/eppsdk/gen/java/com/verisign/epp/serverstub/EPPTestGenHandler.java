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
package com.verisign.epp.serverstub;


// Log4J Imports
import org.apache.log4j.*;

// EPP Imports
import com.verisign.epp.codec.gen.*;
import com.verisign.epp.framework.*;
import com.verisign.epp.util.EPPCatFactory;


/**
 * The <code>EPPTestGenHandler</code> class provides an test handler that loads
 * <code>EPPTestGenMapFactory</code> for testing the EPP General (gen)
 * package.  The handler only needs to load the
 * <code>EPPTestGenMapFatory</code> so that the EPP Greeting will include at
 * least one URI in the Object Services. <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.2 $
 *
 * @see EPPEventHandler
 * @see EPPEvent
 */
public class EPPTestGenHandler implements EPPEventHandler {
	/** The Namespace that this handler supports.  In this case, test. */
	private static final String NS = EPPTestGenMapFactory.NS;

	/** Log4j category for logging */
	private static final Logger cat =
		Logger.getLogger(
						 EPPTestGenHandler.class.getName(),
						 EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of <code>EPPTestGenHandler</code>
	 */
	/**
	 * Whenever an EPPTestGenHandler instance is created load the
	 * corresponsding Map Factory into the Codec
	 */
	public EPPTestGenHandler() {
		try {
			EPPFactory.getInstance().addMapFactory(EPPTestGenMapFactory.class
												   .getName());
		}
		 catch (EPPCodecException e) {
			cat.error("Couldn't load the Map Factory associated with the test Mapping");
			System.exit(1);
		}
	}

	/**
	 * Returns the Namespace that this handler supports. In this case, test.
	 *
	 * @return String The Namespace that this handler supports
	 */
	public final String getNamespace() {
		return NS;
	}

	/**
	 * This method receives an <code>EPPEvent</code> and routes it to the
	 * appropriate abstract method.
	 *
	 * @param aEvent An <code>EPPEvent</code> that contains the
	 * 		  <code>EPPCommand</code>
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPContactHandler</code> instance
	 *
	 * @return DOCUMENT ME!
	 *
	 * @exception EPPEventException Thrown if an unrecognized
	 * 			  <code>EPPEvent</code> is received
	 */
	public final EPPEventResponse handleEvent(EPPEvent aEvent, Object aData)
									   throws EPPEventException {
		throw new EPPEventException("EPPTestGenHandler does not support any messages!");
	}

	/**
	 * Handles any common behavior that all test commands need to execute
	 * before they execute their command specific behavior.  The default
	 * implementation does nothing.
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPTestGenHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
						   throws EPPHandleEventException {
	}

	/**
	 * Handles any common behavior that all test commands need to execute after
	 * they execute their command specific behavior.  The default
	 * implementation does nothing
	 *
	 * @param aEvent The <code>EPPEvent</code> that is being handled
	 * @param aData Any data that a Server needs to send to this
	 * 		  <code>EPPTestGenHandler</code>
	 *
	 * @exception EPPHandleEventException Thrown if an error condition occurs.
	 * 			  It must contain an <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
							throws EPPHandleEventException {
	}
}
