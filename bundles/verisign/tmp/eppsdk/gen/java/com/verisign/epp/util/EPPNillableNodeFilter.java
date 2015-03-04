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

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;


/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 *
 * @author Colin W. Lloyd
 * @version 1.0
 */
public class EPPNillableNodeFilter implements NodeFilter {
	/**
	 * DOCUMENT ME!
	 *
	 * @param n DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public short acceptNode(Node n) {
		if (n.hasAttributes()) {
			NamedNodeMap map	  = n.getAttributes();
			Node		 nillAttr = map.getNamedItem("xsi:nil");

			if (nillAttr != null) {
				String nilled = nillAttr.getNodeValue();

				if (nilled.equalsIgnoreCase("true")) {
					return FILTER_ACCEPT;
				}
			}
		}

		return FILTER_SKIP;
	}
}
