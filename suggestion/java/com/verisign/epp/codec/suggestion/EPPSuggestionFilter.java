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
import java.util.HashMap;
import java.util.HashSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.verisign.epp.codec.gen.EPPCodecComponent;
import com.verisign.epp.codec.gen.EPPDecodeException;
import com.verisign.epp.codec.gen.EPPEncodeException;
import com.verisign.epp.codec.gen.EPPUtil;
import com.verisign.epp.codec.suggestion.util.InvalidValueException;
import com.verisign.epp.codec.suggestion.util.UnsignedShort;
import com.verisign.epp.codec.suggestion.util.WeightEnum;
import com.verisign.epp.util.EqualityUtil;

/**
 * A Filter encapsulates all of the configurable aspects of a Suggestion
 * request.
 * 
 * @author jcolosi
 */
public class EPPSuggestionFilter implements EPPCodecComponent {

	private static final long serialVersionUID = 3673430640817334863L;

	private static final String ATT_CONTENTFILTER = "contentfilter";
	private static final String ATT_CUSTOMFILTER = "customfilter";
	private static final String ATT_FORSALE = "forsale";
	private static final String ATT_MAXLENGTH = "maxlength";
	private static final String ATT_MAXRESULTS = "maxresults";
	private static final String ATT_USEHYPHENS = "usehyphens";
	private static final String ATT_USENUMBERS = "usenumbers";
	private static final String ATT_USEIDNS = "useidns";
	private static final String ATT_VIEW = "view";
	private static final String GRID_STRING = "grid";
	private static final Boolean GRID_VIEW = Boolean.FALSE;
	private static final String TABLE_STRING = "table";
	private static final Boolean TABLE_VIEW = Boolean.TRUE;
	private static final String ELM_NAME = "suggestion:filter";

	private HashMap<String, WeightEnum> actionMap = null;
	private ArrayList<EPPSuggestionAction> actions = null;
	private Boolean contentFilter = null;
	private Boolean customFilter = null;
	private WeightEnum forsale = new WeightEnum();
	private UnsignedShort maxLength = new UnsignedShort();
	private UnsignedShort maxResults = new UnsignedShort();
	private ArrayList<EPPSuggestionTld> tlds = null;
	private HashSet<String> tldSet = null;
	private EPPSuggestionGeo geo = null;
	private Boolean useHyphens = null;
	private Boolean useNumbers = null;
	private Boolean useIdns = null;
	private Boolean view = null;


	/**
	 * Constructor.
	 */
	public EPPSuggestionFilter () {
	}

	/**
	 * Constructor.
	 * 
	 * @param aElement a dom element
	 * @throws EPPDecodeException
	 */
	public EPPSuggestionFilter ( final Element aElement )
			throws EPPDecodeException {
		this();
		decode( aElement );
	}

/**
 * Add an action.
 * 
 * @param aAction an action
 * @throws InvalidValueException
 */
	public void addAction ( final EPPSuggestionAction aAction )
			throws InvalidValueException {
		if ( aAction == null )
			throw new InvalidValueException( "Cannot add a null action" );
		if ( actions == null )
			resetActions();
		actions.add( aAction );
		actionMap.put( aAction.getName().toLowerCase(), aAction.getWeight() );
	}


	/**
	 * Add a tld.
	 * 
	 * @param aTld a tld
	 * @throws InvalidValueException
	 */
	public void addTld ( final EPPSuggestionTld aTld )
			throws InvalidValueException {
		if ( aTld == null )
			throw new InvalidValueException( "Cannot add a null tld" );
		if ( tlds == null )
			resetTlds();
		tlds.add( aTld );
		tldSet.add( aTld.getTld() );
	}


	/**
	 * Geo setter.
	 * 
	 * @param aGeo a geo.
	 * @throws InvalidValueException
	 */
	public void setGeo ( final EPPSuggestionGeo aGeo ) throws InvalidValueException {
		this.geo = aGeo;
	}


	/**
	 * ActionMap getter.
	 * 
	 * @return an action map
	 */
	public HashMap<String, WeightEnum> getActionMap () {
		return actionMap;
	}

	/**
	 * Actions getter.
	 * 
	 * @return actions
	 */
	public ArrayList<EPPSuggestionAction> getActions () {
		return actions;
	}

	/**
	 * Geo getter.
	 * 
	 * @return a geo
	 */
	public EPPSuggestionGeo getGeo () {
		return geo;
	}


	/**
	 * Action Weight getter.
	 * 
	 * @param aActionName the action we want the weight
	 * @return a weight
	 */
	public WeightEnum getActionWeight ( final String aActionName ) {
		return (WeightEnum) actionMap.get( aActionName.toLowerCase() );
	}


	/**
	 * Content filter getter.
	 * 
	 * @return is content filtering enabled
	 */
	public Boolean getContentFilter () {
		return contentFilter;
	}


	/**
	 * Custom filter getter.
	 * 
	 * @return is custom filtering enabled
	 */
	public Boolean getCustomFilter () {
		return customFilter;
	}

	/**
	 * Forsale getter.
	 * 
	 * @return the weight of forsale parameter.
	 */
	public WeightEnum getForsale () {
		return forsale;
	}

	/**
	 * Max length of the suggestion getter.
	 * 
	 * @return max length of the suggestion
	 */
	public UnsignedShort getMaxLength () {
		return maxLength;
	}

	/**
	 * Max results getter.
	 * 
	 * @return number of max results.
	 */
	public UnsignedShort getMaxResults () {
		return maxResults;
	}

	/**
	 * Tlds getter.
	 * 
	 * @return tlds
	 */
	public ArrayList<EPPSuggestionTld> getTlds () {
		return tlds;
	}

	/**
	 * Tld set getter.
	 * 
	 * @return tld set
	 */
	public HashSet<String> getTldSet () {
		return tldSet;
	}

	/**
	 * Use hyphens getter.
	 * 
	 * @return is use hyphen enabled
	 */
	public Boolean getUseHyphens () {
		return useHyphens;
	}

	/**
	 * Use numbers getter.
	 * 
	 * @return is use numbers enabled
	 */
	public Boolean getUseNumbers () {
		return useNumbers;
	}

	/**
	 * Use idns getter.
	 * 
	 * @return is use idns enabled
	 */
	public Boolean getUseIdns () {
		return useIdns;
	}

	/**
	 * Which view is enabled.
	 * 
	 * @return true if table view, false if grid view
	 */
	public Boolean getView () {
		return view;
	}

	/**
	 * Return the string representation of the current view.
	 * 
	 * @return string
	 */
	public String getViewString () {
		if ( view == null )
			return null;
		if ( view.equals( TABLE_VIEW ) )
			return TABLE_STRING;
		else
			return GRID_STRING;
	}

	/**
	 * Has action been set.
	 * 
	 * @param aName action name
	 * @return true if it is set
	 */
	public boolean hasAction ( final String aName ) {
		return aName != null && actionMap != null
				&& actionMap.containsKey( aName.toLowerCase() );
	}

	/**
	 * Has a tld been set.
	 * 
	 * @param aTld tld
	 * @return <code>true</code> if it as been set; <code>false</code> otherwise.
	 */
	public boolean hasTld ( final String aTld ) {
		return aTld != null && tldSet != null && tldSet.contains( aTld );
	}


	/**
	 * Is geo set.
	 * 
	 * @return true if it has been set
	 */
	public boolean isGeo () {
		if ( this.geo != null )
			return true;
		else
			return false;
	}

	/**
	 * Is grid set.
	 * 
	 * @return true if it has been set
	 */
	public boolean isGrid () {
		if ( this.view != null )
			return !this.view.booleanValue();
		else
			return false;
	}

	/**
	 * Is table set.
	 * 
	 * @return true if it has been set
	 */
	public boolean isTable () {
		if ( this.view != null )
			return this.view.booleanValue();
		else
			return false;
	}


	/**
	 * Reset actions.
	 */
	public void resetActions () {
		actions = new ArrayList<EPPSuggestionAction>();
		actionMap = new HashMap<String, WeightEnum>();
	}

	/**
	 * Reset tlds.
	 */
	public void resetTlds () {
		tlds = new ArrayList<EPPSuggestionTld>();
		tldSet = new HashSet<String>();
	}

	/**
	 * Content filter setter.
	 * 
	 * @param aContentFilter content filter flag
	 */
	public void setContentFilter ( final boolean aContentFilter ) {
		this.contentFilter = new Boolean( aContentFilter );
	}

	/**
	 * Content filter setter.
	 * 
	 * @param aContentFilterAsString content filter flag as string
	 */
	public void setContentFilter ( final String aContentFilterAsString ) {
		if ( aContentFilterAsString != null && aContentFilterAsString.length() > 0 )
			this.contentFilter = new Boolean( aContentFilterAsString );
	}

	/**
	 * Custom filter setter.
	 * 
	 * @param aCustomfilter a custom filter flag
	 */
	public void setCustomFilter ( final boolean aCustomfilter ) {
		this.customFilter = new Boolean( aCustomfilter );
	}

	/**
	 * Custom filter setter.
	 * 
	 * @param aCustomfilterAsString a custom filter flag
	 */
	public void setCustomFilter ( final String aCustomfilterAsString ) {
		if ( aCustomfilterAsString != null && aCustomfilterAsString.length() > 0 )
			this.customFilter = new Boolean( aCustomfilterAsString );
	}

	/**
	 * For sale setter.
	 * 
	 * @param aForsale for sale flag
	 * @throws InvalidValueException
	 */
	public void setForSale ( final String aForsale ) throws InvalidValueException {
		this.forsale.set( aForsale );
	}

	/**
	 * Grid view setter.
	 */
	public void setGridView () {
		view = GRID_VIEW;
	}

	/**
	 * Max length setter.
	 * 
	 * @param aMaxLength max length
	 * @throws InvalidValueException
	 */
	public void setMaxLength ( final short aMaxLength ) throws InvalidValueException {
		this.maxLength.set( aMaxLength );
	}

	/**
	 * Max length setter.
	 * 
	 * @param aMaxLengthAsString max length as string
	 * @throws InvalidValueException
	 */
	public void setMaxLength ( final String aMaxLengthAsString ) throws InvalidValueException {
		this.maxLength.set( aMaxLengthAsString );
	}

	/**
	 * Max results setter.
	 * 
	 * @param aMaxResults max results
	 * @throws InvalidValueException
	 */
	public void setMaxResults ( final short aMaxResults ) throws InvalidValueException {
		this.maxResults.set( aMaxResults );
	}

	/**
	 * Max results setter.
	 * 
	 * @param aMaxResultsAsString max results as string
	 * @throws InvalidValueException
	 */
	public void setMaxResults ( final String aMaxResultsAsString )
			throws InvalidValueException {
		this.maxResults.set( aMaxResultsAsString );
	}

	/**
	 * Table view setter.
	 */
	public void setTableView () {
		view = TABLE_VIEW;
	}

	/**
	 * Use hyphens setter.
	 * 
	 * @param aUseHyphens use hyphen flag
	 */
	public void setUseHyphens ( final boolean aUseHyphens ) {
		this.useHyphens = new Boolean( aUseHyphens );
	}

	/**
	 * Use hyphens setter.
	 * 
	 * @param aUseHyphensAsString use hyphen flag as string
	 */
	public void setUseHyphens ( String aUseHyphensAsString ) {
		if ( aUseHyphensAsString != null && aUseHyphensAsString.length() > 0 )
			this.useHyphens = new Boolean( aUseHyphensAsString );
	}

	/**
	 * Use numbers flag setter.
	 * 
	 * @param aUseNumbers use number flag
	 */
	public void setUseNumbers ( final boolean aUseNumbers ) {
		this.useNumbers = new Boolean( aUseNumbers );
	}

	/**
	 * Use numbers flag setter.
	 * 
	 * @param aUseNumbersAsString use number flag as string
	 */
	public void setUseNumbers ( final String aUseNumbersAsString ) {
		if ( aUseNumbersAsString != null && aUseNumbersAsString.length() > 0 )
			this.useNumbers = new Boolean( aUseNumbersAsString );
	}

	/**
	 * Use Idns setter.
	 * 
	 * @param aUseIdns use idns flag
	 */
	public void setUseIdns ( final boolean aUseIdns ) {
		this.useIdns = new Boolean( aUseIdns );
	}
	
	/**
	 * Use Idns setter.
	 * 
	 * @param aUseIdnsAsString use idns flag as string
	 */
	public void setUseIdns ( final String aUseIdnsAsString ) {
		if ( aUseIdnsAsString != null && aUseIdnsAsString.length() > 0 )
			this.useIdns = new Boolean( aUseIdnsAsString );
	}

	/**
	 * View setter.
	 * 
	 * @param aViewAsString as view
	 * @throws InvalidValueException
	 */
	public void setView ( final String aViewAsString ) throws InvalidValueException {
		if ( aViewAsString == null || aViewAsString.length() == 0 )
			this.view = null;
		else if ( aViewAsString.equalsIgnoreCase( TABLE_STRING ) )
			setTableView();
		else if ( aViewAsString.equalsIgnoreCase( GRID_STRING ) )
			setGridView();
		else
			throw new InvalidValueException( aViewAsString );
	}

	/**
	 * Return string representation for logging purpose.
	 * 
	 * @return string
	 */
	public String toLogString () {
		StringBuffer out = new StringBuffer();
		out.append( "cf:" + abbreviate( contentFilter ) );
		out.append( " uf:" + abbreviate( customFilter ) );
		out.append( " fs:" + forsale.toLogString() );
		out.append( " ml:" + maxLength );
		out.append( " mr:" + maxResults );
		out.append( " uh:" + abbreviate( useHyphens ) );
		out.append( " un:" + abbreviate( useNumbers ) );
		out.append( " ui:" + abbreviate( useIdns ) );
		out.append( " vw:" + abbreviate( view ) );

		out.append( " action:" );
		if ( actions != null && actions.size() > 0 ) {
			int size = actions.size();
			if ( size > 0 )
				out.append( ((EPPSuggestionAction) actions.get( 0 ))
						.toLogString() );
			for ( int i = 1; i < size; i++ ) {
				out.append( ","
						+ ((EPPSuggestionAction) actions.get( i ))
								.toLogString() );
			}
		}

		out.append( " tld:" );
		if ( tlds != null && tlds.size() > 0 ) {
			int size = tlds.size();
			if ( size > 0 )
				out.append( ((EPPSuggestionTld) tlds.get( 0 )).toLogString() );
			for ( int i = 1; i < size; i++ ) {
				out.append( ","
						+ ((EPPSuggestionTld) tlds.get( i )).toLogString() );
			}
		}

		out.append( " geo:" );
		if ( geo != null ) {
			out.append( geo.toLogString() );
		}

		return out.toString();
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
		return (EPPSuggestionFilter) super.clone();
	}


	@Override
	public void decode ( final Element aElement ) throws EPPDecodeException {
		NodeList nodes = aElement.getChildNodes();
		Node node = null;
		int size = nodes.getLength();
		if ( size > 0 ) {
			String name = null;
			for ( int i = 0; i < size; i++ ) {
				node = nodes.item( i );
				if ( node instanceof Element ) {
					name = node.getLocalName();
					if ( name.equals( EPPUtil
							.getLocalName( EPPSuggestionTld.ELM_NAME ) ) ) {
						addTld( new EPPSuggestionTld( (Element) node ) );
					}
					else if ( name.equals( EPPUtil
							.getLocalName( EPPSuggestionAction.ELM_NAME ) ) ) {
						addAction( new EPPSuggestionAction( (Element) node ) );
					}
					else if ( name.equals( EPPUtil
							.getLocalName( EPPSuggestionGeo.ELM_NAME ) ) ) {
						setGeo( new EPPSuggestionGeo( (Element) node ) );
					}
				}
			}
		}

		setContentFilter( aElement.getAttribute( ATT_CONTENTFILTER ) );
		setCustomFilter( aElement.getAttribute( ATT_CUSTOMFILTER ) );
		setForSale( aElement.getAttribute( ATT_FORSALE ) );
		setMaxLength( aElement.getAttribute( ATT_MAXLENGTH ) );
		setMaxResults( aElement.getAttribute( ATT_MAXRESULTS ) );
		setUseHyphens( aElement.getAttribute( ATT_USEHYPHENS ) );
		setUseNumbers( aElement.getAttribute( ATT_USENUMBERS ) );
		setUseIdns( aElement.getAttribute( ATT_USEIDNS ) );
		setView( aElement.getAttribute( ATT_VIEW ) );
	}


	@Override
	public Element encode ( final Document aDocument ) throws EPPEncodeException {
		Element root =
				aDocument
						.createElementNS( EPPSuggestionMapFactory.NS, ELM_NAME );

		if ( actions != null )
			EPPUtil.encodeCompList( aDocument, root, actions );
		if ( tlds != null )
			EPPUtil.encodeCompList( aDocument, root, tlds );
		if ( geo != null )
			EPPUtil.encodeComp( aDocument, root, geo );

		if ( contentFilter != null )
			root.setAttribute( ATT_CONTENTFILTER, contentFilter.toString() );
		if ( customFilter != null )
			root.setAttribute( ATT_CUSTOMFILTER, customFilter.toString() );
		if ( forsale.isSet() )
			root.setAttribute( ATT_FORSALE, forsale.toString() );
		if ( maxLength.isSet() )
			root.setAttribute( ATT_MAXLENGTH, maxLength.toString() );
		if ( maxResults.isSet() )
			root.setAttribute( ATT_MAXRESULTS, maxResults.toString() );
		if ( useHyphens != null )
			root.setAttribute( ATT_USEHYPHENS, useHyphens.toString() );
		if ( useNumbers != null )
			root.setAttribute( ATT_USENUMBERS, useNumbers.toString() );
		if ( useIdns != null )
			root.setAttribute( ATT_USEIDNS, useIdns.toString() );
		if ( view != null )
			root.setAttribute( ATT_VIEW, getViewString() );

		return root;
	}


	@Override
	public boolean equals ( final Object o ) {
		if ( (o != null) && (o.getClass().equals( this.getClass() )) ) {
			EPPSuggestionFilter other = (EPPSuggestionFilter) o;
			if ( !EqualityUtil.equals( this.actions, other.actions ) )
				return false;
			if ( !EqualityUtil.equals( this.tlds, other.tlds ) )
				return false;
			if ( !EqualityUtil.equals( this.geo, other.geo ) )
				return false;
			if ( !EqualityUtil.equals( this.contentFilter, other.contentFilter ) )
				return false;
			if ( !EqualityUtil.equals( this.customFilter, other.customFilter ) )
				return false;
			if ( !this.forsale.equals( other.forsale ) )
				return false;
			if ( !this.maxLength.equals( other.maxLength ) )
				return false;
			if ( !this.maxResults.equals( other.maxResults ) )
				return false;
			if ( !EqualityUtil.equals( this.useHyphens, other.useHyphens ) )
				return false;
			if ( !EqualityUtil.equals( this.useNumbers, other.useNumbers ) )
				return false;
			if ( !EqualityUtil.equals( this.useIdns, other.useIdns ) )
				return false;
			if ( !EqualityUtil.equals( this.view, other.view ) )
				return false;
			return true;
		}
		return false;
	}


	private static String abbreviate ( final Boolean aBoolean ) {
		if ( aBoolean == null )
			return "";
		else if ( aBoolean.booleanValue() )
			return "t";
		else
			return "f";
	}

	
}
