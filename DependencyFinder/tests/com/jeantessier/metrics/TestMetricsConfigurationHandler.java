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

import junit.framework.*;

import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class TestMetricsConfigurationHandler extends TestCase {
	private MetricsConfigurationHandler handler;
	private XMLReader                   reader;
	
	public TestMetricsConfigurationHandler(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		handler = new MetricsConfigurationHandler();
		
		reader = XMLReaderFactory.createXMLReader();
		reader.setDTDHandler(handler);
		reader.setContentHandler(handler);
		reader.setErrorHandler(handler);
		reader.setFeature("http://xml.org/sax/features/validation", true);
	}
	
	public void testEmptyFile() throws IOException, SAXException {
		InputSource in = new InputSource(new StringReader(""));

		try {
			reader.parse(in);
			fail("Read empty file");
		} catch (SAXParseException ex) {
			// Ignore
		}

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());
	}

	public void testEmptyDocument() throws IOException, SAXException {
		InputSource in = new InputSource(new StringReader("<metrics-configuration/>"));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());
	}

	public void testNonWellFormedDocument() throws IOException, SAXException {
		InputSource in = new InputSource(new StringReader("<metrics-configuration>"));

		try {
			reader.parse(in);
			fail("Read non well formed file");
		} catch (SAXParseException ex) {
			// Ignore
		}

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());
	}

	public void testValidation() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements/>\n");
		document.append("    <group-measurements/>\n");
		document.append("    <class-measurements/>\n");
		document.append("    <method-measurements/>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());
	}

	public void testPackageMeasurement() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements>\n");
		document.append("        <measurement>\n");
		document.append("            <short-name>SLOC</short-name>\n");
		document.append("            <long-name>Single Lines of Code</long-name>\n");
		document.append("            <class>com.jeantessier.metrics.StatisticalMeasurement</class>\n");
		document.append("            <init>\n");
		document.append("                SLOC\n");
		document.append("                DISPOSE_SUM\n");
		document.append("            </init>\n");
		document.append("        </measurement>\n");
		document.append("    </project-measurements>\n");
		document.append("    <group-measurements/>\n");
		document.append("    <class-measurements/>\n");
		document.append("    <method-measurements/>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 1, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());

		MeasurementDescriptor descriptor = (MeasurementDescriptor) handler.MetricsConfiguration().ProjectMeasurements().get(0);
		assertEquals("SLOC", descriptor.ShortName());
		assertEquals("Single Lines of Code", descriptor.LongName());
		assertEquals(com.jeantessier.metrics.StatisticalMeasurement.class, descriptor.Class());
		assertEquals("SLOC\n                DISPOSE_SUM", descriptor.InitText());
		assertNull("descriptor.LowerThreshold()", descriptor.LowerThreshold());
		assertNull("descriptor.UpperThreshold()", descriptor.UpperThreshold());
	}

	public void testGroupMeasurement() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements/>\n");
		document.append("    <group-measurements>\n");
		document.append("        <measurement>\n");
		document.append("            <short-name>SLOC</short-name>\n");
		document.append("            <long-name>Single Lines of Code</long-name>\n");
		document.append("            <class>com.jeantessier.metrics.StatisticalMeasurement</class>\n");
		document.append("            <init>\n");
		document.append("                SLOC\n");
		document.append("                DISPOSE_SUM\n");
		document.append("            </init>\n");
		document.append("        </measurement>\n");
		document.append("    </group-measurements>\n");
		document.append("    <class-measurements/>\n");
		document.append("    <method-measurements/>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   1, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());

		MeasurementDescriptor descriptor = (MeasurementDescriptor) handler.MetricsConfiguration().GroupMeasurements().get(0);
		assertEquals("SLOC", descriptor.ShortName());
		assertEquals("Single Lines of Code", descriptor.LongName());
		assertEquals(com.jeantessier.metrics.StatisticalMeasurement.class, descriptor.Class());
		assertEquals("SLOC\n                DISPOSE_SUM", descriptor.InitText());
		assertNull("descriptor.LowerThreshold()", descriptor.LowerThreshold());
		assertNull("descriptor.UpperThreshold()", descriptor.UpperThreshold());
	}

	public void testClassMeasurement() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements/>\n");
		document.append("    <group-measurements/>\n");
		document.append("    <class-measurements>\n");
		document.append("        <measurement>\n");
		document.append("            <short-name>SLOC</short-name>\n");
		document.append("            <long-name>Single Lines of Code</long-name>\n");
		document.append("            <class>com.jeantessier.metrics.StatisticalMeasurement</class>\n");
		document.append("            <init>\n");
		document.append("                SLOC\n");
		document.append("            </init>\n");
		document.append("        </measurement>\n");
		document.append("    </class-measurements>\n");
		document.append("    <method-measurements/>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   1, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());

		MeasurementDescriptor descriptor = (MeasurementDescriptor) handler.MetricsConfiguration().ClassMeasurements().get(0);
		assertEquals("SLOC", descriptor.ShortName());
		assertEquals("Single Lines of Code", descriptor.LongName());
		assertEquals(com.jeantessier.metrics.StatisticalMeasurement.class, descriptor.Class());
		assertEquals("SLOC", descriptor.InitText());
		assertNull("descriptor.LowerThreshold()", descriptor.LowerThreshold());
		assertNull("descriptor.UpperThreshold()", descriptor.UpperThreshold());
	}

	public void testMethodMeasurement() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements/>\n");
		document.append("    <group-measurements/>\n");
		document.append("    <class-measurements/>\n");
		document.append("    <method-measurements>\n");
		document.append("        <measurement>\n");
		document.append("            <short-name>SLOC</short-name>\n");
		document.append("            <long-name>Single Lines of Code</long-name>\n");
		document.append("            <class>com.jeantessier.metrics.CounterMeasurement</class>\n");
		document.append("            <upper-threshold>50</upper-threshold>\n");
		document.append("        </measurement>\n");
		document.append("    </method-measurements>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  1, handler.MetricsConfiguration().MethodMeasurements().size());

		MeasurementDescriptor descriptor = (MeasurementDescriptor) handler.MetricsConfiguration().MethodMeasurements().get(0);
		assertEquals("SLOC", descriptor.ShortName());
		assertEquals("Single Lines of Code", descriptor.LongName());
		assertEquals(com.jeantessier.metrics.CounterMeasurement.class, descriptor.Class());
		assertNull("descriptor.Init()", descriptor.InitText());
		assertNull("descriptor.LowerThreshold()", descriptor.LowerThreshold());
		assertEquals("descriptor.UpperThreshold()", "50", descriptor.UpperThreshold());
	}

	public void testGroupDefinitions() throws IOException, SAXException {
		StringBuffer document = new StringBuffer();

		document.append("<!DOCTYPE metrics-configuration SYSTEM \"http://depfind.sourceforge.net/dtd/metrics-configuration.dtd\">\n");
		document.append("<metrics-configuration>\n");
		document.append("    <project-measurements/>\n");
		document.append("    <group-definitions>\n");
		document.append("        <group-definition>\n");
		document.append("            <name>Jean Tessier</name>\n");
		document.append("            <pattern>/^com.jeantessier/</pattern>\n");
		document.append("        </group-definition>\n");
		document.append("    </group-definitions>\n");
		document.append("    <group-measurements/>\n");
		document.append("    <class-measurements/>\n");
		document.append("    <method-measurements/>\n");
		document.append("</metrics-configuration>\n");

		InputSource in = new InputSource(new StringReader(document.toString()));

		reader.parse(in);

		assertEquals("ProjectMeasurements", 0, handler.MetricsConfiguration().ProjectMeasurements().size());
		assertEquals("GroupMeasurements",   0, handler.MetricsConfiguration().GroupMeasurements().size());
		assertEquals("ClassMeasurements",   0, handler.MetricsConfiguration().ClassMeasurements().size());
		assertEquals("MethodMeasurements",  0, handler.MetricsConfiguration().MethodMeasurements().size());

		assertEquals("groups for foobar",                  0, handler.MetricsConfiguration().Groups("foobar").size());
		assertEquals("groups for com.jeantessier.metrics", 1, handler.MetricsConfiguration().Groups("com.jeantessier.metrics").size());
		assertEquals("Jean Tessier", handler.MetricsConfiguration().Groups("com.jeantessier.metrics").iterator().next());
	}
}
