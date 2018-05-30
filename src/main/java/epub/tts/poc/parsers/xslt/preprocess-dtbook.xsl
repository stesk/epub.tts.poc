<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="no"/>
    <xsl:template match="text()">
        <xsl:copy/>
    </xsl:template>
    <xsl:template match="/dtbook">
        <html lang="{book/@lang}" xml:lang="{book/@lang}">
            <head>
                <meta http-equiv="content-type"
                    content="application/xhtml+xml; charset=UTF-8" />
                <title><xsl:value-of select="head/title"/></title>
            </head>
            <body>
                <xsl:apply-templates
                    select="book/(frontmatter|bodymatter|rearmatter)/level"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="@*"/>
    <xsl:template match="@height|@id|@src|@width">
        <xsl:copy/>
    </xsl:template>
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:call-template name="INSERT_ID_IF_MISSING"/>
            <xsl:apply-templates select="@*|node()"/>
       	</xsl:element>
    </xsl:template>
    <xsl:template match="imggroup">
        <div class="image">
            <xsl:apply-templates select="@*|node()"/>
        </div>
    </xsl:template>
    <xsl:template match="level">
        <section>
            <xsl:apply-templates select="@*|node()"/>
        </section>
    </xsl:template>
    <xsl:template match="levelhd">
        <xsl:element name="{'h' || count(ancestor::level)}">
            <xsl:call-template name="INSERT_ID_IF_MISSING"/>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="list">
        <xsl:element name="{@type}">
            <xsl:if test="@bullet eq 'none'">
                <xsl:attribute name="class" select="'list-style-type-none'"/>
            </xsl:if>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="note">
        <div class="note">
            <xsl:call-template name="INSERT_ID_IF_MISSING"/>
            <xsl:apply-templates select="@*|node()"/>
        </div>
    </xsl:template>
    <xsl:template match="noteref">
        <a href="{'#' || @idref}">
            <xsl:call-template name="INSERT_ID_IF_MISSING"/>
            <xsl:apply-templates select="@*|node()"/>
        </a>
    </xsl:template>
    <!-- TODO: Implement conversion of page breaks -->
    <xsl:template match="pagenum"/>
    <xsl:template match="prodnote[@class eq 'caption']">
        <div>
            <xsl:apply-templates select="@*|node()"/>
        </div>
    </xsl:template>
    <xsl:template name="INSERT_ID_IF_MISSING">
        <xsl:if test="not(@id)">
            <xsl:attribute name="id" select="generate-id()"/>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>