<?xml version="1.0"?> 

<!--
    Copyright (c) 2001-2002, Jean Tessier
    All rights reserved.
    
    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:
    
    	* Redistributions of source code must retain the above copyright
    	  notice, this list of conditions and the following disclaimer.
    
    	* Redistributions in binary form must reproduce the above copyright
    	  notice, this list of conditions and the following disclaimer in the
    	  documentation and/or other materials provided with the distribution.
    
    	* Neither the name of Jean Tessier nor the names of his contributors
    	  may be used to endorse or promote products derived from this software
    	  without specific prior written permission.
    
    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
    "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
    LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
    A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
    CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
    EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
    PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
    PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
    NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
    SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" indent="yes"/>
    <xsl:strip-space elements="*"/> 

    <xsl:template match="differences">
	<html>

	<head>
	    <title><xsl:if test="name/text()"><xsl:value-of select="name"/> - </xsl:if>API Change History</title>
	</head>

	<body bgcolor="#ffffff">

	<h1><xsl:if test="name/text()"><xsl:value-of select="name"/> - </xsl:if>API Change History</h1>

	<h1><xsl:value-of select="old"/> to <xsl:value-of select="new"/></h1>

	<xsl:apply-templates/>

	</body>

	</html>
    </xsl:template>

    <xsl:template match="differences/name | old | new"></xsl:template>

    <xsl:template match="removed-packages">
	<h2>Removed Packages:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undocumented-packages">
	<h2>Formerly Documented Packages:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>

    <xsl:template match="removed-interfaces">
	<h2>Removed Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="removed-classes">
	<h2>Removed Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="deprecated-interfaces">
	<h2>Newly Deprecated Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="deprecated-classes">
	<h2>Newly Deprecated Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undocumented-interfaces">
	<h2>Formerly Documented Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undocumented-classes">
	<h2>Formerly Documented Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="modified-interfaces">
	<h2>Modified Interfaces:</h2>
	<blockquote>
	<xsl:apply-templates/>
	</blockquote>
    </xsl:template>
 
    <xsl:template match="modified-classes">
	<h2>Modified Classes:</h2>
	<blockquote>
	<xsl:apply-templates/>
	</blockquote>
    </xsl:template>
 
    <xsl:template match="documented-interfaces">
	<h2>Newly Documented Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="documented-classes">
	<h2>Newly Documented Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undeprecated-interfaces">
	<h2>Formerly Deprecated Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undeprecated-classes">
	<h2>Formerly Deprecated Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="new-packages">
	<h2>New Packages:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
  
    <xsl:template match="documented-packages">
	<h2>Newly Documented Packages:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>

    <xsl:template match="new-interfaces">
	<h2>New Interfaces:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="new-classes">
	<h2>New Classes:</h2>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="class">
	<h3><code><xsl:value-of select="name"/></code></h3>
	<blockquote>
	    <xsl:apply-templates/>
	</blockquote>
    </xsl:template>

    <xsl:template match="removed-fields">
	<h4>Removed Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="removed-constructors">
	<h4>Removed Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="removed-methods">
	<h4>Removed Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="deprecated-fields">
	<h4>Newly Deprecated Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="deprecated-constructors">
	<h4>Newly Deprecated Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="deprecated-methods">
	<h4>Newly Deprecated Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>

    <xsl:template match="undocumented-fields">
	<h4>Formerly Documented Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>

    <xsl:template match="undocumented-constructors">
	<h4>Formerly Documented Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>

    <xsl:template match="undocumented-methods">
	<h4>Formerly Documented Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
  
    <xsl:template match="modified-fields">
	<h4>Field Declaration Changes:</h4>
	<xsl:apply-templates/>
    </xsl:template>
 
    <xsl:template match="modified-constructors">
	<h4>Constructor Declaration Changes:</h4>
	<xsl:apply-templates/>
    </xsl:template>
 
    <xsl:template match="modified-methods">
	<h4>Method Declaration Changes:</h4>
	<xsl:apply-templates/>
    </xsl:template>
 
    <xsl:template match="documented-fields">
	<h4>Newly Documented Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="documented-constructors">
	<h4>Newly Documented Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="documented-methods">
	<h4>Newly Documented Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undeprecated-fields">
	<h4>Formerly Deprecated Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undeprecated-constructors">
	<h4>Formerly Deprecated Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="undeprecated-methods">
	<h4>Formerly Deprecated Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="new-fields">
	<h4>New Fields:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="new-constructors">
	<h4>New Constructors:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="new-methods">
	<h4>New Methods:</h4>
	<ul>
	    <xsl:apply-templates/>
	</ul>
    </xsl:template>
 
    <xsl:template match="class/name | feature/name"></xsl:template>

    <xsl:template match="class/modified-declaration">
	<h4>Declaration Changes:</h4>
	<blockquote>
	<p><nobr><code>
	<b>old:</b> <xsl:value-of select="old-declaration"/>
	<xsl:if test="old-declaration[@deprecated='yes']"> <b>[deprecated]</b></xsl:if>
	<br/>
	<b>new:</b> <xsl:value-of select="new-declaration"/>
	<xsl:if test="new-declaration[@deprecated='yes']"> <b>[deprecated]</b></xsl:if>
	</code></nobr></p>
	</blockquote>
    </xsl:template>

    <xsl:template match="modified-declaration">
	<blockquote>
	<p><nobr><code>
	<b>old:</b> <xsl:value-of select="old-declaration"/>
	<xsl:if test="old-declaration[@deprecated='yes']"> <b>[deprecated]</b></xsl:if>
	<br/>
	<b>new:</b> <xsl:value-of select="new-declaration"/>
	<xsl:if test="new-declaration[@deprecated='yes']"> <b>[deprecated]</b></xsl:if>
	</code></nobr></p>
	</blockquote>
    </xsl:template>

    <xsl:template match="new-packages/name[@deprecated='yes'] | new-interfaces/name[@deprecated='yes'] | new-classes/name[@deprecated='yes'] | new-fields/declaration[@deprecated='yes'] | new-constructors/declaration[@deprecated='yes'] | new-methods/declaration[@deprecated='yes']">
	<li><nobr><code><xsl:value-of select="."/> <b>[deprecated]</b></code></nobr></li>
    </xsl:template>

    <xsl:template match="name | declaration">
	<li><nobr><code><xsl:value-of select="."/></code></nobr></li>
    </xsl:template>

</xsl:stylesheet>

