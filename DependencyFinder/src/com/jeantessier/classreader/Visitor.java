/*
 *  Copyright (c) 2001-2006, Jean Tessier
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *  
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *  
 *      * Neither the name of Jean Tessier nor the names of his contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
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

package com.jeantessier.classreader;

import java.util.*;

public interface Visitor {
    // Classfile
    public void visitClassfiles(Collection<Classfile> classfiles);
    public void visitClassfile(Classfile classfile);

    // ConstantPool entries
    public void visitConstantPool(ConstantPool constantPool);
    public void visitClass_info(Class_info entry);
    public void visitFieldRef_info(FieldRef_info entry);
    public void visitMethodRef_info(MethodRef_info entry);
    public void visitInterfaceMethodRef_info(InterfaceMethodRef_info entry);
    public void visitString_info(String_info entry);
    public void visitInteger_info(Integer_info entry);
    public void visitFloat_info(Float_info entry);
    public void visitLong_info(Long_info entry);
    public void visitDouble_info(Double_info entry);
    public void visitNameAndType_info(NameAndType_info entry);
    public void visitUTF8_info(UTF8_info entry);

    // Features
    public void visitField_info(Field_info entry);
    public void visitMethod_info(Method_info entry);

    // Attributes
    public void visitConstantValue_attribute(ConstantValue_attribute attribute);
    public void visitCode_attribute(Code_attribute attribute);
    public void visitExceptions_attribute(Exceptions_attribute attribute);
    public void visitInnerClasses_attribute(InnerClasses_attribute attribute);
    public void visitSynthetic_attribute(Synthetic_attribute attribute);
    public void visitSourceFile_attribute(SourceFile_attribute attribute);
    public void visitLineNumberTable_attribute(LineNumberTable_attribute attribute);
    public void visitLocalVariableTable_attribute(LocalVariableTable_attribute attribute);
    public void visitDeprecated_attribute(Deprecated_attribute attribute);
    public void visitCustom_attribute(Custom_attribute attribute);

    // Attribute helpers
    public void visitExceptionHandler(ExceptionHandler helper);
    public void visitInnerClass(InnerClass helper);
    public void visitLineNumber(LineNumber helper);
    public void visitLocalVariable(LocalVariable helper);
}
