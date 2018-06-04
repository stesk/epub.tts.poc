<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="node()"/>
    <xsl:template match="/html:html">
        <smil>
            <head>
                <xsl:apply-templates select="html:head/html:meta"/>
                <meta name="dc:format" content="Daisy 2.02" />
                <meta name="ncc:generator" content="EPUB TTS POC"/>
                <layout>
                    <region id="txtView"/>
                </layout>
            </head>
            <body>
                <xsl:apply-templates
                    select="html:body/html:section"/>
            </body>
        </smil>
    </xsl:template>
    <xsl:template match="html:meta[@name = ('dc:identifier', 'dc:title')]">
        <meta name="{@name}" content="{@content}"/>
    </xsl:template>
    <xsl:template match="html:section">
        <xsl:variable name="element" as="element()"
            select="(html:h1|html:h2|html:h3|html:h4|html:h5|html:h6)[1]"/>
        <xsl:variable name="title" as="xs:string"
            select="string-join($element//text()[not(parent::a/
                    @class = 'noteref')], '')"/>
        <xsl:variable name="position" as="xs:integer"
            select="count(preceding-sibling::html:section) + 1"/>
        <ref id="{'smil' || format-number($position, '0000')}"
            src="{format-number($position, '0000') || '.smil'}"
            title="{$title}"/>
    </xsl:template>
</xsl:stylesheet>