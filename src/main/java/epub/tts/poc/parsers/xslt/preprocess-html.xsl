<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:epub="http://www.idpf.org/2007/ops"
    xmlns:f="http://www.nota.dk/xslt"
    xpath-default-namespace="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="no"/>
    <xsl:template mode="#all" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="body//*">
        <xsl:copy>
            <xsl:call-template name="CREATE_ID_IF_NONE"/>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="caption">
        <xsl:call-template name="KEEP_BLOCKS_OR_CREATE_PARAGRAPH"/>
    </xsl:template>
    <xsl:template match="figcaption">
        <xsl:call-template name="KEEP_BLOCKS_OR_CREATE_PARAGRAPH"/>
    </xsl:template>
    <xsl:template match="figure">
        <div>
            <xsl:call-template name="CREATE_ID_IF_NONE"/>
            <xsl:apply-templates select="@*|node()"/>
        </div>
    </xsl:template>
    <xsl:template match="figure[parent::figure]">
        <xsl:apply-templates select="@*|node()"/>
    </xsl:template>
    <xsl:template match="li[f:get-blocks(.)]">
        <xsl:copy>
            <xsl:call-template name="CREATE_ID_IF_NONE"/>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="section">
        <section>
            <xsl:call-template name="CREATE_ID_IF_NONE"/>
            <xsl:apply-templates select="@*|node()[not(self::section)]"/>
        </section>
        <xsl:apply-templates select="section"/>
    </xsl:template>
    <xsl:template match="table">
        <xsl:apply-templates select="caption"/>
        <xsl:copy>
            <xsl:call-template name="CREATE_ID_IF_NONE"/>
            <xsl:apply-templates select="@*|node()[not(self::caption)]"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template name="CREATE_ID_IF_NONE" as="attribute(id)?">
        <xsl:if test="not(@id)">
            <xsl:attribute name="id" select="generate-id()"/>
        </xsl:if>
    </xsl:template>
    <xsl:template name="KEEP_BLOCKS_OR_CREATE_PARAGRAPH">
        <xsl:choose>
            <xsl:when test="f:get-blocks(.)">
                <xsl:apply-templates/>
            </xsl:when>
            <xsl:otherwise>
                <p>
                    <xsl:call-template name="CREATE_ID_IF_NONE"/>
                    <xsl:apply-templates select="@*|node()"/>
                </p>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:function name="f:get-blocks" as="element()*">
        <xsl:param name="e" as="element()"/>
        <xsl:sequence
            select="$e//(aside|blockquote|div|dl|figure|h1|h2|h3|h4|h5|h6|hr|
                    ol|p|table|ul)"/>
    </xsl:function>
</xsl:stylesheet>