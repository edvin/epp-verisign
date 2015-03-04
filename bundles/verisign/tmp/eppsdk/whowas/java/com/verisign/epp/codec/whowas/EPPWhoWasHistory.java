/***********************************************************
 Copyright (C) 2010 VeriSign, Inc.

 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-0107  USA

 http://www.verisign.com/nds/naming/namestore/techdocs.html
 ***********************************************************/

package com.verisign.epp.codec.whowas;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.util.EPPCatFactory;

/**
 * Represents the &lt;whowas:history&gt; element returned as child element of
 * &lt;whowas:infData&gt; element in the EPP WhoWas Response. This class will
 * contain list of one or more {@link EPPWhoWasRecord}. Each
 * {@link EPPWhoWasRecord} will be represented by &lt;whowas:rec:&gt; element.
 * 
 * @author Deepak Deshpande
 * @version 1.0
 * @see EPPWhoWasInfoResp
 * @see EPPWhoWasRecord
 */
public class EPPWhoWasHistory implements EPPCodecComponent {

	/** Log4j category for logging */
	private static final Logger cat =
			Logger.getLogger( EPPWhoWasHistory.class.getName(), EPPCatFactory
					.getInstance()
					.getFactory() );

	/** The {@link List} of WhoWas Records. */
	private List records = null;


	/**
	 * Default constructor
	 */
	public EPPWhoWasHistory () {
		// default constructor
	}


	/**
	 * Decodes the {@link EPPWhoWasHistory} from the <code>aElement</code> DOM
	 * Element tree and creates list of {@link EPPWhoWasRecord} objects.
	 * 
	 * @param aElement
	 *        - Root DOM Element to decode {@link EPPWhoWasHistory} from.
	 * @exception EPPDecodeException
	 *            Unable to decode aElement
	 */
	public void decode ( Element aElement ) throws EPPDecodeException {

		this.records =
				EPPUtil.decodeCompList( aElement, EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_REC, EPPWhoWasRecord.class );

		if ( this.records == null || this.records.isEmpty() ) {
			throw new EPPDecodeException( "EPPWhoWasHistory has null records list" );
		}
	}


	/**
	 * Returns the Root DOM Element tree created from {@link EPPWhoWasHistory}
	 * instance.
	 * 
	 * @param aDocument
	 *        - DOM Document that is being built. Used as an Element factory.
	 * @return the Root DOM Element tree created from {@link EPPWhoWasHistory}
	 *         instance.
	 * @exception EPPEncodeException
	 *            - Unable to encode {@link EPPWhoWasRecord} instance.
	 */
	public Element encode ( Document aDocument ) throws EPPEncodeException {

		if ( this.records == null || this.records.isEmpty() ) {
			throw new EPPEncodeException(
					"EPPWhoWasHistory has null or empty records list" );
		}

		Element root =
				aDocument.createElementNS( EPPWhoWasMapFactory.NS,
						EPPWhoWasConstants.ELM_HISTORY );

		EPPUtil.encodeCompList( aDocument, root, this.records );

		return root;
	}


	/**
	 * Adds a <code>aEPPWhoWasRec</code> to the list of records.
	 * 
	 * @param aEPPWhoWasRec
	 *        EPPWhoWasRec to add
	 * @throws IllegalArgumentException
	 *         if passed aEPPWhoWasRec is null
	 */
	public void addRecord ( EPPWhoWasRecord aEPPWhoWasRec ) {

		if ( aEPPWhoWasRec == null )
			throw new IllegalArgumentException( "Cannot add a null EPPWhoWasRecord" );

		if ( this.records == null ) {
			this.records = new ArrayList();
		}

		this.records.add( aEPPWhoWasRec );
	}


	/**
	 * Does a deep clone of the {@link EPPWhoWasHistory} instance.
	 * 
	 * @return Cloned instance
	 */
	public Object clone () throws CloneNotSupportedException {

		EPPWhoWasHistory clone = (EPPWhoWasHistory) super.clone();

		if ( this.records != null ) {
			clone.records = new ArrayList();
			for ( int i = 0; i < this.records.size(); i++ ) {
				EPPWhoWasRecord record = (EPPWhoWasRecord) this.records.get( i );
				clone.addRecord( (EPPWhoWasRecord) record.clone() );
			}
		}

		return clone;
	}


	/**
	 * Compares two {@link EPPWhoWasHistory} instances.
	 * 
	 * @return <code>true</code> if equal;<code>false</code> otherwise.
	 */
	public boolean equals ( Object o ) {

		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {

			EPPWhoWasHistory other = (EPPWhoWasHistory) o;

			if ( !EPPUtil.equalLists( this.records, other.records ) ) {
				cat.error( "EPPWhoWasHistory.equals: records are not equal" );
				return false;
			}

			return true;
		}
		else {
			cat.error( "EPPWhoWasHistory.equals(): not EPPWhoWasHistory instance" );
			return false;
		}
	}


	/**
	 * Returns the records
	 * 
	 * @return the records
	 */
	public List getRecords () {
		return this.records;
	}


	/**
	 * Sets records value to <code>aRecords</code>.
	 * 
	 * @param aRecords
	 *        the records to set
	 */
	public void setRecords ( List aRecords ) {
		this.records = aRecords;
	}
}
