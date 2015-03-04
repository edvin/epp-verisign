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
package com.verisign.epp.pool;


/**
 * Interface implemented by object's managed by 
 * the {@link EPPSessionPoolableFactory} or derived class.  
 * The created time is used to determine if the session has 
 * exceeded the absolute session timeout and the last touched 
 * time is used to determine if a keep alive message needs to 
 * be sent to keep the session active.  
 */
public interface EPPPooledSession {
	/**
	 * Gets the time the pooled object was created.
	 * 
	 * @return Epoch time of creation
	 */
	public abstract long getCreatedTime();
	/**
	 * Gets the last time the pooled object was touched.
	 * 
	 * @return Epoch time of touch
	 */
	public abstract long getLastTouched();
	
	/**
	 * Set the last touched to the current time.
	 */
	public abstract void touch();
}