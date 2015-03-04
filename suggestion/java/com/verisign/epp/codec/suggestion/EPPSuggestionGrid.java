/***********************************************************
 Copyright (C) 2004 VeriSign, Inc.

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

package com.verisign.epp.codec.suggestion;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.util.EqualityUtil;

/**
 * The Grid response associates domain labels with tlds. The tlds are further
 * associated with scores and statuses within the Cell class.
 * 
 * @author jcolosi
 */
public class EPPSuggestionGrid implements EPPCodecComponent {

	private static final long serialVersionUID = -1986013047045847256L;

	static final String ELM_NAME = "suggestion:grid";

	private List<EPPSuggestionRecord> records = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionGrid () {
	}


	/**
	 * Constructor.
	 * 
	 * @param aElement
	 *        a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionGrid ( final Element aElement ) throws EPPDecodeException {
		decode( aElement );
	}


	/**
	 * Records getter.
	 * 
	 * @return records.
	 */
	public List<EPPSuggestionRecord> getRecords () {
		return records;
	}


	/**
	 * Add record to the grid.
	 * 
	 * @param aRecord
	 *        a record
	 * @throws InvalidValueException
	 */
	public void addRecord ( final EPPSuggestionRecord aRecord )
			throws InvalidValueException {
		if ( aRecord == null )
			throw new InvalidValueException( "Cannot add a null Record" );
		if ( records == null )
			resetRecords();
		records.add( aRecord );
	}


	/**
	 * Reset the records.
	 */
	public void resetRecords () {
		records = new ArrayList<EPPSuggestionRecord>();
	}


	/**
	 * Implementation of <code>Object.toString</code>, which will result in an
	 * indented XML <code>String</code> representation of the concrete
	 * <code>EPPCodecComponent</code>.
	 * 
	 * @return Indented XML <code>String</code> if successful;
	 *         <code>ERROR</code> otherwise.
	 */
	@Override
	public String toString () {
		return EPPUtil.toString( this );
	}


	@Override
	public Object clone () throws CloneNotSupportedException {
		return (EPPSuggestionGrid) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		NodeList nodes = aElement.getChildNodes();
		Node node = null;
		resetRecords();
		int size = nodes.getLength();
		for ( int i = 0; i < size; i++ ) {
			node = nodes.item( i );
			if ( node instanceof Element ) {
				records.add( new EPPSuggestionRecord( (Element) node ) );
			}
		}
		if ( records.size() == 0 )
			records = null;
	}


	@Override
	public Element encode ( final Document aDocument )
			throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );
		if ( records != null )
			EPPUtil.encodeCompList( aDocument, root, records );
		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionGrid other = (EPPSuggestionGrid) o;
			if ( !EqualityUtil.equals( this.records, other.records ) )
				return false;
			return true;
		}
		return false;
	}
}