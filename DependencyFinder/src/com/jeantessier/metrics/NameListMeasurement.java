/*
 *  Copyright (c) 2001-2002, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  	* Redistributions of source code must retain the above copyright
 *  	  notice, this list of conditions and the following disclaimer.
 *  
 *  	* Redistributions in binary form must reproduce the above copyright
 *  	  notice, this list of conditions and the following disclaimer in the
 *  	  documentation and/or other materials provided with the distribution.
 *  
 *  	* Neither the name of Jean Tessier nor the names of his contributors
 *  	  may be used to endorse or promote products derived from this software
 *  	  without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jeantessier.metrics;

import java.util.*;

import org.apache.log4j.*;

/**
 *  <pre>
 *  &lt;init-text&gt;
 *      [SET | LIST]
 *  &lt;/init-text&gt;
 *  </pre>
 *
 *  <p>Defaults to SET (i.e., does not count duplicates).</p>
 */
public class NameListMeasurement extends MeasurementBase implements CollectionMeasurement {
	private Collection values;

	public NameListMeasurement(MeasurementDescriptor descriptor, Metrics context, String init_text) {
		super(descriptor, context, init_text);

		if (init_text != null) {
			if (init_text.trim().equalsIgnoreCase("list")) {
				values = new LinkedList();
			} else if (init_text.trim().equalsIgnoreCase("set")) {
				values = new HashSet();
			} else {
				Logger.getLogger(getClass()).debug("Cannot initialize with \"" + init_text + "\", using SET");
				values = new HashSet();
			}
		} else {
			Logger.getLogger(getClass()).debug("Cannot initialize with null text, using SET");
			values = new HashSet();
		}
	}

	public void Add(Object object) {
		values.add(object);
	}

	public void Accept(MeasurementVisitor visitor) {
		visitor.VisitNameListMeasurement(this);
	}

	public Number Value() {
		return new Integer(values.size());
	}

	protected double Compute() {
		return values.size();
	}

	public Collection Values() {
		return Collections.unmodifiableCollection(values);
	}
}