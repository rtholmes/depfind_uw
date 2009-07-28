/*
 *  Copyright (c) 2001-2009, Jean Tessier
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

package com.jeantessier.dependency;

import java.util.*;

import org.jmock.*;
import org.jmock.integration.junit4.*;
import org.jmock.lib.legacy.*;
import org.junit.*;
import org.junit.runner.*;

@RunWith(JMock.class)
public class TestBasicTraversal {
    private Mockery context;

    private Visitor delegate;
    private TraversalStrategy strategy;

    private PackageNode packageNode;
    private ClassNode classNode;
    private FeatureNode featureNode;

    private BasicTraversal sut;

    @Before
    public void setUp() {
        context = new Mockery();
        context.setImposteriser(ClassImposteriser.INSTANCE);

        delegate = context.mock(Visitor.class);
        strategy = context.mock(TraversalStrategy.class);

        packageNode = context.mock(PackageNode.class);
        classNode = context.mock(ClassNode.class);
        featureNode = context.mock(FeatureNode.class);

        sut = new BasicTraversal(strategy);
        sut.setDelegate(delegate);
    }

    @Test
    public void testTraverseNodes() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (packageNode).accept(delegate);
        }});

        sut.traverseNodes(nodes);
    }

    @Test
    public void testTraverseInbound() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (packageNode).acceptInbound(delegate);
        }});

        sut.traverseInbound(nodes);
    }

    @Test
    public void testTraverseOutbound() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(packageNode);

        context.checking(new Expectations() {{
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (packageNode).acceptOutbound(delegate);
        }});

        sut.traverseOutbound(nodes);
    }

    @Test
    public void testVisitPackageNode_NotInScope() {
        context.checking(new Expectations() {{
            one (strategy).isInScope(packageNode);
                will(returnValue(false));
        }});

        sut.visitPackageNode(packageNode);
    }

    @Test
    public void testVisitPackageNode_InScope() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(classNode);

        context.checking(new Expectations() {{
            one (strategy).isInScope(packageNode);
                will(returnValue(true));

            // Process inbounds
            one (packageNode).getInboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (classNode).acceptInbound(delegate);

            // Process outbounds
            one (packageNode).getOutboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (classNode).acceptOutbound(delegate);

            // Process classes
            one (packageNode).getClasses();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (classNode).accept(delegate);
        }});

        sut.visitPackageNode(packageNode);
    }

    @Test
    public void testVisitInboundPackageNode() {
        sut.visitInboundPackageNode(packageNode);
    }

    @Test
    public void testVisitOutboundPackageNode() {
        sut.visitOutboundPackageNode(packageNode);
    }

    @Test
    public void testVisitClassNode_NotInScope() {
        context.checking(new Expectations() {{
            one (strategy).isInScope(classNode);
                will(returnValue(false));
        }});

        sut.visitClassNode(classNode);
    }

    @Test
    public void testVisitClassNode_InScope() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(featureNode);

        context.checking(new Expectations() {{
            one (strategy).isInScope(classNode);
                will(returnValue(true));

            // Process inbounds
            one (classNode).getInboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (featureNode).acceptInbound(delegate);

            // Process outbounds
            one (classNode).getOutboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (featureNode).acceptOutbound(delegate);

            // Process features
            one (classNode).getFeatures();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (featureNode).accept(delegate);
        }});

        sut.visitClassNode(classNode);
    }

    @Test
    public void testVisitInboundClassNode() {
        sut.visitInboundClassNode(classNode);
    }

    @Test
    public void testVisitOutboundClassNode() {
        sut.visitOutboundClassNode(classNode);
    }

    @Test
    public void testVisitFeatureNode_NotInScope() {
        context.checking(new Expectations() {{
            one (strategy).isInScope(featureNode);
                will(returnValue(false));
        }});

        sut.visitFeatureNode(featureNode);
    }

    @Test
    public void testVisitFeatureNode_InScope() {
        final Collection<? extends Node> nodes = new ArrayList<Node>();
        final Collection<Node> sortedNodes = new ArrayList<Node>();
        sortedNodes.add(featureNode);

        context.checking(new Expectations() {{
            one (strategy).isInScope(featureNode);
                will(returnValue(true));

            // Process inbounds
            one (featureNode).getInboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (featureNode).acceptInbound(delegate);

            // Process outbounds
            one (featureNode).getOutboundDependencies();
                will(returnValue(nodes));
            one (strategy).order(nodes);
                will(returnValue(sortedNodes));
            one (featureNode).acceptOutbound(delegate);
        }});

        sut.visitFeatureNode(featureNode);
    }

    @Test
    public void testVisitInboundFeatureNode() {
        sut.visitInboundFeatureNode(featureNode);
    }

    @Test
    public void testVisitOutboundFeatureNode() {
        sut.visitOutboundFeatureNode(featureNode);
    }
}