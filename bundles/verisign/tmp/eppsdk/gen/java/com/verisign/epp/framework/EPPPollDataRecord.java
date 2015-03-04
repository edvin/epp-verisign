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
package com.verisign.epp.framework;


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// Java Core Imports
import java.util.Date;


// EPP Imports

/**
 * The &lt;EPPPollDataRecord&gt; is a class to store the data information.
 * <br><br>
 *
 * @author $Author: jim $
 * @version $Revision: 1.3 $
 */
public class EPPPollDataRecord {
	/**
	 * Default value for the size attribute. The size attribute should be set
	 * by the queue when the record is dequeued.
	 */
	public static final int UNDEFINED = -1;

	/** Kind of message, which is usually the XML namespace */
	private String kind = null;

	/** Message data */
	private Object data = null;

	/** Message id */
	private String msgId = null;

	/** Date the message was enqueued */
	private Date qDate = null;

	/** Number of messages in the queue */
	private int size = 0;

	/**
	 * Default constructor
	 */
	public EPPPollDataRecord() {
	}

	/**
	 * Constructor when creating a <code>EPPPollDataRecord</code> to place into
	 * a queue. The size attribute should be set using <code>setSize</code> by
	 * the queue when a record is dequeued. By default, the size is set to
	 * <code>EPPPollDataRecord.UNDEFINED</code>.
	 *
	 * @param aKind Type of response message
	 * @param aData Object
	 * @param aMsgId Message Id
	 */
	public EPPPollDataRecord(String aKind, Object aData, String aMsgId) {
		kind	  = aKind;
		data	  = aData;
		msgId     = aMsgId;
		qDate     = new Date();
		size	  = UNDEFINED;
	}

	/**
	 * Creates a new EPPPollDataRecord object.
	 *
	 * @param aKind DOCUMENT ME!
	 * @param aData DOCUMENT ME!
	 * @param aMsgId DOCUMENT ME!
	 * @param queuedDate DOCUMENT ME!
	 * @param queueSize DOCUMENT ME!
	 */
	public EPPPollDataRecord(
							 String aKind, Object aData, String aMsgId,
							 Date queuedDate, int queueSize) {
		kind	  = aKind;
		data	  = aData;
		msgId     = aMsgId;
		qDate     = queuedDate;
		size	  = queueSize;
	}

	/**
	 * Sets type of response message
	 *
	 * @param aKind Type of response message
	 */
	public void setKind(String aKind) {
		kind = aKind;
	}

	/**
	 * Sets data
	 *
	 * @param aData Object
	 */
	public void setData(Object aData) {
		data = aData;
	}

	/**
	 * Sets the message id
	 *
	 * @param aMsgId Message Id
	 */
	public void setMsgId(String aMsgId) {
		msgId = aMsgId;
	}

	/**
	 * Sets the date of a message was stored
	 *
	 * @param aQDate date of message was stored
	 */
	public void setQDate(Date aQDate) {
		qDate = aQDate;
	}

	/**
	 * Sets number of messages
	 *
	 * @param aSize number of messages
	 */
	public void setSize(int aSize) {
		size = aSize;
	}

	/**
	 * Gets the type of response message
	 *
	 * @return kind Type of response message.
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * Gets the Object
	 *
	 * @return data Object
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Gets Message Id
	 *
	 * @return msgId Message Id
	 */
	public String getMsgId() {
		return msgId;
	}

	/**
	 * Gets QDate
	 *
	 * @return qDate Date of the message was stored
	 */
	public Date getQDate() {
		return qDate;
	}

	/**
	 * Gets number of messages in queue
	 *
	 * @return size number of messages
	 */
	public int getSize() {
		return size;
	}
}
