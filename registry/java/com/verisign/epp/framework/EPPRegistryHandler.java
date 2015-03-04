/*********************************************************************
 *                                                                   *
 * The information in this document is proprietary to VeriSign, Inc. *
 * It may not be used, reproduced or disclosed without the written   *
 * approval of VeriSign.                                             *
 *                                                                   *
 * VERISIGN PROPRIETARY & CONFIDENTIAL INFORMATION                   *
 *                                                                   *
 *                                                                   *
 * Copyright (c) 2012 VeriSign, Inc.  All rights reserved.           *
 *                                                                   *
 ********************************************************************/

package com.verisign.epp.framework;

import org.apache.log4j.Logger;

import com.verisign.epp.codec.gen.EPPCodecException;
import com.verisign.epp.codec.gen.EPPFactory;
import com.verisign.epp.codec.gen.EPPMessage;
import com.verisign.epp.codec.registry.EPPRegistryCheckCmd;
import com.verisign.epp.codec.registry.EPPRegistryCreateCmd;
import com.verisign.epp.codec.registry.EPPRegistryDeleteCmd;
import com.verisign.epp.codec.registry.EPPRegistryInfoCmd;
import com.verisign.epp.codec.registry.EPPRegistryMapFactory;
import com.verisign.epp.codec.registry.EPPRegistryUpdateCmd;
import com.verisign.epp.util.EPPCatFactory;

/**
 * The <code>EPPRegistryHandler</code> class provides an interface for handling
 * EPP Registry Commands in a Server implementation. EPPEvents are handled by
 * the handleEvent() method here and routed to the appropriate abstract member
 * function. Subclasses should override the abstract methods to define the
 * desired behavior of a particular command when it is received. A subclassed
 * instance of <code>EPPRegistryHandler</code> should be registered with the
 * <code>EPPDispatcher</code> so that EEPEvents related to the Registry Mapping
 * will be handled there. <br>
 * <br>
 * 
 * 
 * @see EPPEventHandler
 * @see EPPEvent
 */
public abstract class EPPRegistryHandler implements EPPEventHandler {
	/** Log4j category for logging */
	private static final Logger cat = Logger.getLogger(EPPRegistryHandler.class
			.getName(), EPPCatFactory.getInstance().getFactory());

	/**
	 * Construct an instance of <code>EPPRegistryHandler</code>
	 */
	/**
	 * Whenever an EPPRegistryHandler instance is created load the corresponding
	 * Map Factory into the Codec
	 */
	public EPPRegistryHandler() {
		try {
			EPPFactory.getInstance().addMapFactory(
					EPPRegistryMapFactory.class.getName());
		} catch (EPPCodecException e) {
			cat.error(
					"Couldn't load the Map Factory associated with the Registry Mapping",
					e);
			System.exit(1);
		}
	}

	/**
	 * This method receives an <code>EPPEvent</code> and routes it to the
	 * appropriate abstract method.
	 * 
	 * @param aEvent
	 *            An <code>EPPEvent</code> that contains the
	 *            <code>EPPCommand</code>
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code> instance
	 * 
	 * @return DOCUMENT ME!
	 * 
	 * @exception EPPEventException
	 *                Thrown if an unrecognized <code>EPPEvent</code> is
	 *                received
	 */
	public EPPEventResponse handleEvent(EPPEvent aEvent, Object aData)
			throws EPPEventException {
		try {
			this.preHandleEvent(aEvent, aData);
		} catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		EPPMessage message = aEvent.getMessage();
		EPPEventResponse response = null;

		if (message instanceof EPPRegistryInfoCmd) {
			response = doRegistryInfo(aEvent, aData);
		}
		if (message instanceof EPPRegistryCheckCmd) {
			response = doRegistryCheck(aEvent, aData);
		}
		if (message instanceof EPPRegistryCreateCmd) {
			response = doRegistryCreate(aEvent, aData);
		}
		if (message instanceof EPPRegistryUpdateCmd) {
			response = doRegistryUpdate(aEvent, aData);
		}
		if (message instanceof EPPRegistryDeleteCmd) {
			response = doRegistryDelete(aEvent, aData);
		}
		
		try {
			this.postHandleEvent(aEvent, aData);
		}
		catch (EPPHandleEventException e) {
			return new EPPEventResponse(e.getResponse());
		}

		return response;
	}

	/**
	 * Invoked when a Registry Info command is received. Subclasses should
	 * define the behavior when a Registry Info command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doRegistryInfo(EPPEvent aEvent,
			Object aData);

	/**
	 * Invoked when a Registry Check command is received. Subclasses should
	 * define the behavior when a Registry Check command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doRegistryCheck(EPPEvent aEvent,
			Object aData);

	/**
	 * Invoked when a Registry Create command is received. Subclasses should
	 * define the behavior when a Registry Create command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doRegistryCreate(EPPEvent aEvent,
			Object aData);

	/**
	 * Invoked when a Registry Update command is received. Subclasses should
	 * define the behavior when a Registry Update command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doRegistryUpdate(EPPEvent aEvent,
			Object aData);

	/**
	 * Invoked when a Registry Delete command is received. Subclasses should
	 * define the behavior when a Registry Delete command is received.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @return EPPEventResponse The response that should be sent back to the
	 *         client.
	 */
	protected abstract EPPEventResponse doRegistryDelete(EPPEvent aEvent,
			Object aData);

	public String getNamespace() {
		return EPPRegistryMapFactory.NS;
	}

	/**
	 * Handles any common behavior that all registry commands need to execute
	 * before they execute their command specific behavior. The default
	 * implementation does nothing.
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void preHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {
	}

	/**
	 * Handles any common behavior that all registry commands need to execute
	 * after they execute their command specific behavior. The default
	 * implementation does nothing
	 * 
	 * @param aEvent
	 *            The <code>EPPEvent</code> that is being handled
	 * @param aData
	 *            Any data that a Server needs to send to this
	 *            <code>EPPRegistryHandler</code>
	 * 
	 * @exception EPPHandleEventException
	 *                Thrown if an error condition occurs. It must contain an
	 *                <code>EPPEventResponse</code>
	 */
	protected void postHandleEvent(EPPEvent aEvent, Object aData)
			throws EPPHandleEventException {
	}
}
