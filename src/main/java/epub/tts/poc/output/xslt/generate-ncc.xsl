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
                    select="html:head/
                            (html:title|html:meta[not(@name eq 'viewport')])"/>
            </head>
            <body>
                <xsl:apply-templates
                    select="html:body/html:section/
                            (html:h1|html:h2|html:h3|html:h4|html:h5|html:h6)"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="html:h1|html:h2|html:h3|html:h4|html:h5|html:h6">
        <xsl:variable name="position" as="xs:integer"
            select="count(parent::html:section/
                    preceding-sibling::html:section)"/>
        <xsl:copy copy-namespaces="no">
            <xsl:copy-of select="@id"/>
            <a href="{format-number($position, '0000') || '.smil' || '#' || @id}">
                <xsl:value-of
                    select="string-join(.//text()[not(parent::a/
                            @class = 'noteref')], '')"/>
            </a>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>