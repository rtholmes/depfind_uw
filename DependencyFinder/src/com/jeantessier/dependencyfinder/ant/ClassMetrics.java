/*
 *  Copyright (c) 2001-2003, Jean Tessier
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

package com.jeantessier.dependencyfinder.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.*;

import com.jeantessier.classreader.*;

public class ClassMetrics extends Task {
	private boolean list = false;
	private File    destfile;
	private Path    path;

	public boolean getList() {
		return list;
	}
	
	public void setList(boolean list) {
		this.list = list;
	}

	public File getDestfile() {
		return destfile;
	}
	
	public void setDestfile(File destfile) {
		this.destfile = destfile;
	}
	
	public Path createPath() {
		if (path == null) {
			path = new Path(getProject());
		}

		return path;
	}
	
	public Path getPath() {
		return path;
	}
	
	public void execute() throws BuildException {
        // first off, make sure that we've got what we need

        if (getPath() == null) {
            throw new BuildException("path must be set!");
        }

        if (getDestfile() == null) {
            throw new BuildException("destfile must be set!");
        }

		VerboseListener verbose_listener = new VerboseListener(this);

		ClassfileLoader loader = new AggregatingClassfileLoader();
		loader.addLoadListener(verbose_listener);
		loader.Load(Arrays.asList(getPath().list()));

		MetricsGatherer metrics = new MetricsGatherer();
		Iterator i = loader.Classfiles().iterator();
		while (i.hasNext()) {
			((Classfile) i.next()).Accept(metrics);
		}

		log("Saving class metrics to " + getDestfile().getAbsolutePath());
		
		try {
			PrintWriter out = new PrintWriter(new FileWriter(getDestfile()));

			out.println(metrics.Classes().size() + " class(es)");
			if (getList()) {
				Iterator j = metrics.Classes().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println(metrics.Interfaces().size() + " interface(s)");
			if (getList()) {
				Iterator j = metrics.Interfaces().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println();
			out.println(metrics.Methods().size() + " method(s) (average " + (metrics.Methods().size() / (metrics.Classes().size() + (double) metrics.Interfaces().size())) + " per class/interface)");
			out.println(metrics.Fields().size() + " field(s) (average " + (metrics.Fields().size() / (metrics.Classes().size() + (double) metrics.Interfaces().size())) + " per class/interface)");
			out.println();
			
			PrintCFM(out, " synthetic element(s)", metrics.SyntheticClasses(), metrics.SyntheticFields(), metrics.SyntheticMethods());
			PrintCFM(out, " deprecated element(s)", metrics.DeprecatedClasses(), metrics.DeprecatedFields(), metrics.DeprecatedMethods());
			PrintCFMIC(out, " public element(s)", metrics.PublicClasses(), metrics.PublicFields(), metrics.PublicMethods(), metrics.PublicInnerClasses());
			PrintFMIC(out, " protected element(s)", metrics.ProtectedFields(), metrics.ProtectedMethods(), metrics.ProtectedInnerClasses());
			PrintFMIC(out, " private element(s)", metrics.PrivateFields(), metrics.PrivateMethods(), metrics.PrivateInnerClasses());
			PrintCFMIC(out, " package element(s)", metrics.PackageClasses(), metrics.PackageFields(), metrics.PackageMethods(), metrics.PackageInnerClasses());
			PrintCMIC(out, " abstract element(s)", metrics.AbstractClasses(), metrics.AbstractMethods(), metrics.AbstractInnerClasses());
			
			PrintFMIC(out, " static element(s)", metrics.StaticFields(), metrics.StaticMethods(), metrics.StaticInnerClasses());
			PrintCFMIC(out, " final element(s)", metrics.FinalClasses(), metrics.FinalFields(), metrics.FinalMethods(), metrics.FinalInnerClasses());
			
			out.println(metrics.SynchronizedMethods().size() + " synchronized method(s)");
			if (getList()) {
				Iterator j = metrics.SynchronizedMethods().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println(metrics.NativeMethods().size() + " native method(s)");
			if (getList()) {
				Iterator j = metrics.NativeMethods().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println(metrics.VolatileFields().size() + " volatile field(s)");
			if (getList()) {
				Iterator j = metrics.VolatileFields().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println(metrics.TransientFields().size() + " transient field(s)");
			if (getList()) {
				Iterator j = metrics.TransientFields().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}
			
			out.println(metrics.CustomAttributes().size() + " custom attribute(s)");
			if (getList()) {
				Iterator j = metrics.CustomAttributes().iterator();
				while (j.hasNext()) {
					out.println("        " + j.next());
				}
			}

			out.close();
		} catch (IOException ex) {
			throw new BuildException(ex);
		}
	}

	private void PrintCMIC(PrintWriter out, String label, Collection classes, Collection methods, Collection inner_classes) {
		out.println((classes.size() +
					 methods.size() +
					 inner_classes.size()) + label);
		if (getList()) {
			Iterator j;

			out.println("    " + classes.size() + " class(es)");
			j = classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + methods.size() + " method(s)");
			j = methods.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + inner_classes.size() + " inner class(es)");
			j = inner_classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}
		} else {
			out.println("    " + classes.size() + " class(es)");
			out.println("    " + methods.size() + " method(s)");
			out.println("    " + inner_classes.size() + " inner class(es)");
		}
	}

	private void PrintCFMIC(PrintWriter out, String label, Collection classes, Collection fields, Collection methods, Collection inner_classes) {
		out.println((classes.size() +
					 fields.size() +
					 methods.size() +
					 inner_classes.size()) + label);
		if (getList()) {
			Iterator j;

			out.println("    " + classes.size() + " class(es)");
			j = classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + fields.size() + " field(s)");
			j = fields.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + methods.size() + " method(s)");
			j = methods.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + inner_classes.size() + " inner class(es)");
			j = inner_classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}
		} else {
			out.println("    " + classes.size() + " class(es)");
			out.println("    " + fields.size() + " fields(s)");
			out.println("    " + methods.size() + " method(s)");
			out.println("    " + inner_classes.size() + " inner class(es)");
		}
	}

	private void PrintCFM(PrintWriter out, String label, Collection classes, Collection fields, Collection methods) {
		out.println((classes.size() +
					 fields.size() +
					 methods.size()) + label);
		if (getList()) {
			Iterator j;

			out.println("    " + classes.size() + " class(es)");
			j = classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + fields.size() + " field(s)");
			j = fields.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + methods.size() + " method(s)");
			j = methods.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}
		} else {
			out.println("    " + classes.size() + " class(es)");
			out.println("    " + fields.size() + " fields(s)");
			out.println("    " + methods.size() + " method(s)");
		}
	}

	private void PrintFMIC(PrintWriter out, String label, Collection fields, Collection methods, Collection inner_classes) {
		out.println((fields.size() +
					 methods.size() +
					 inner_classes.size()) + label);
		if (getList()) {
			Iterator j;

			out.println("    " + fields.size() + " field(s)");
			j = fields.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + methods.size() + " method(s)");
			j = methods.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}

			out.println("    " + inner_classes.size() + " inner class(es)");
			j = inner_classes.iterator();
			while (j.hasNext()) {
				out.println("        " + j.next());
			}
		} else {
			out.println("    " + fields.size() + " fields(s)");
			out.println("    " + methods.size() + " method(s)");
			out.println("    " + inner_classes.size() + " inner class(es)");
		}
	}
}