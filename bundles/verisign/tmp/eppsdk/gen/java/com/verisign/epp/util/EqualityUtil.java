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
package com.verisign.epp.util;

/**
 * A utility class to facilitate object comparisons.
 * @author jcolosi
 */
public class EqualityUtil {

	static public boolean equals(boolean[] a, boolean[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(byte[] a, byte[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(char[] a, char[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(double[] a, double[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(float[] a, float[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(int[] a, int[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(long[] a, long[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	static public boolean equals(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	static public boolean equals(Object[] a, Object[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (!a[i].equals(b[i])) return false;
		}
		return true;
	}

	static public boolean equals(short[] a, short[] b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) return false;
		}
		return true;
	}

	/**
	 * Compare two String objects.
	 * <p>
	 * It is common in XML to alter whitespace during the encoding process. For
	 * this reason it is a best practice to ignore whitespace when comparing XML
	 * String objects. For strict String equality cast the String objects into
	 * Object objects.
	 * <p>
	 * <code>boolean flag = EqualityUtil.equals((Object)a, (Object)b);</code>
	 */
	static public boolean equals(String a, String b) {
		if (a == null ^ b == null) return false;
		if (a == null) return true;
		return a.replaceAll("\\s+", "").equals(b.replaceAll("\\s+", ""));
	}

}