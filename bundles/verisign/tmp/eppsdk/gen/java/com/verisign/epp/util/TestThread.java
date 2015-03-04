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


//----------------------------------------------
//
// imports...
//
//----------------------------------------------
// JUNIT Imports
import junit.framework.*;


/**
 * <code>Thread</code> wrapper for running a JUNIT <code>TestSuite</code> in
 * multiple threads.     <br><br>
 */
public class TestThread extends Thread {
	/** test suite to run */
	private Test test;

	/**
	 * Allocate a <code>TestSuiteThread</code> given a name and a
	 * <code>TestSuite</code>     to run.
	 *
	 * @param aName Name of the <code>TestThread</code>
	 * @param aTest Test suite to run.
	 */
	public TestThread(String aName, Test aTest) {
		super(aName);
		test = aTest;
	}

	// End TestThread.TestThread(String, TestSuite)

	/**
	 * Override of <code>Thread.run</code>.
	 */
	public void run() {
		junit.textui.TestRunner.run(test);
	}

	// End TestThread.run()
}


// End class TestThread
