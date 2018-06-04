<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="node()"/>
    <xsl:template match="/html:html">
        <html>
            <xsl:copy-of select="@*"/>
            <head>
                <xsl:copy-of copy-namespaces="no"
                    select="html:head/html:title"/>
                <xsl:copy-of copy-namespaces="no"
                    select="html:head/(html:meta[matches(@name, '^dc:') or
                            exists(@http-equiv)])"/>
                <meta name="dc:format" content="DAISY 2.02"/>
                <meta name="ncc:charset" content="utf-8"/>
                <meta name="dc:language" content="{/html:html/@lang}" />
                <meta name="ncc:multimediaType" content="audioFullText"/>
            </head>
            <body>
                <xsl:apply-templates
                    select="html:body/html:section"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="html:section">
        <xsl:variable name="heading" as="element()"
            select="(html:h1|html:h2|html:h3|html:h4|html:h5|html:h6)[1]"/>
    	<xsl:variable name="title" as="xs:string"
    		select="string-join($heading//text()[not(parent::a/
    		        @class = 'noteref')], '')"/>
        <xsl:variable name="position" as="xs:integer"
            select="count(preceding-sibling::html:section) + 1"/>
        <xsl:element name="{$heading/local-name()}">
            <xsl:copy-of select="$heading/@id"/>
            <a href="{format-number($position, '0000') || '.smil' || '#' ||
                $heading/@id}">
                <xsl:value-of select="$title"/>
            </a>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>