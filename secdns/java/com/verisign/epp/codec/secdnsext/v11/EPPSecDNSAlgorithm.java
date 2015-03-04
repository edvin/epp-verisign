/***********************************************************
Copyright (C) 2010 VeriSign, Inc.

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
package com.verisign.epp.codec.secdnsext.v11;

/**
 * http://www.ietf.org/rfc/rfc4034.txt<br/>
 * A.1.  DNSSEC Algorithm Types<br/>
 *
 * The DNSKEY, RRSIG, and DS RRs use an 8-bit number to identify the
 * security algorithm being used.  These values are stored in the
 * "Algorithm number" field in the resource record RDATA.
 *
 * Some algorithms are usable only for zone signing (DNSSEC), some only
 * for transaction security mechanisms (SIG(0) and TSIG), and some for
 * both.  Those usable for zone signing may appear in DNSKEY, RRSIG, and
 * DS RRs.  Those usable for transaction security would be present in
 * SIG(0) and KEY RRs, as described in [RFC2931].
 * <br/>
 * <table>
 * <tr><td/><td/><td>Zone</td><td/></tr>
 * <tr><td>Value</td> <td>Algorithm [Mnemonic]</td>  <td>Signing</td>  <td>References</td></tr>
 * <tr><td>-----</td> <td>--------------------</td> <td>---------</td> <td>----------</td></tr>
 * <tr><td>  0</td>   <td>reserved</td></tr>
 * <tr><td>  1</td>   <td>RSA/MD5 [RSAMD5]</td>         <td>n</td>      <td>[RFC2537] </td></tr>
 * <tr><td>  2</td>   <td>Diffie-Hellman [DH]</td>      <td>n</td>      <td>[RFC2539]</td></tr>
 * <tr><td>  3</td>   <td>DSA/SHA-1 [DSA]</td>          <td>y</td>      <td>[RFC2536]</td></tr>
 * <tr><td>  4</td>   <td>Elliptic Curve [ECC]</td>     <td/>         <td>TBA </td></tr>
 * <tr><td>  5</td>   <td>RSA/SHA-1 [RSASHA1]</td>      <td>y</td>      <td>[RFC3110] </td></tr>
 * <tr><td>  8</td>   <td>RSA/SHA-256 [RSASHA256]</td>      <td>y</td>      <td>[draft-ietf-dnsext-dnssec-rsasha256-14] </td></tr>
 * <tr><td>  10</td>   <td>RSA/SHA-512 [RSASHA512]</td>      <td>y</td>      <td>[draft-ietf-dnsext-dnssec-rsasha256-14] </td></tr>
 * <tr><td>  13</td>   <td>ECDSA Curve P-256 with SHA-256</td>      <td>y</td>      <td>[draft-ietf-dnsext-ecdsa] </td></tr>
 * <tr><td>  14</td>   <td>ECDSA Curve P-384 with SHA-384</td>      <td>y</td>      <td>[draft-ietf-dnsext-ecdsa] </td></tr>
 * <tr><td> 252</td>  <td>Indirect [INDIRECT]</td>      <td>n</td> <td/></tr>
 * <tr><td> 253</td>  <td>Private [PRIVATEDNS]</td>     <td>y</td> <td/></tr>  
 * <tr><td> 254</td>  <td>Private [PRIVATEOID]</td>     <td>y</td> <td/></tr>
 * <tr><td> 255</td>  <td>reserved </td></tr>
 * </table>
 * <br/>6 - 251  Available for assignment by IETF Standards Action.
 * 
 *
 */
public class EPPSecDNSAlgorithm {

	private EPPSecDNSAlgorithm() {
		// prevent instantiation
	}

	/** RSA/MD5 public key (deprecated) */
    public static final int RSAMD5 = 1;

    /** Diffie Hellman key */
    public static final int DH = 2;

    /** DSA public key */
    public static final int DSA = 3;

    /** Elliptic Curve key */
    public static final int ECC = 4;

    /** RSA/SHA1 public key */
    public static final int RSASHA1 = 5;
    
    /** DSA-NSEC3-SHA1 */
    public static final int DSANSEC3SHA1 = 6;
    
    /** RSASHA1-NSEC3-SHA1 */
    public static final int RSASHA1NSEC3SHA1 = 7;     
    
    /** RSA/SHA256 public key */
    public static final int RSASHA256 = 8;

    /** RSA/SHA512 public key */
    public static final int RSASHA512 = 10;
    
    /** ECDSA Curve P-256 with SHA-256 */
    public static final int ECDSAP256SHA256 = 13;
    
    /** ECDSA Curve P-384 with SHA-384 */
    public static final int ECDSAP384SHA384 = 14;
    
    /** Indirect keys; the actual key is elsewhere. */
    public static final int INDIRECT = 252;

    /** Private algorithm, specified by domain name */
    public static final int PRIVATEDNS = 253;

    /** Private algorithm, specified by OID */
    public static final int PRIVATEOID = 254;

}
