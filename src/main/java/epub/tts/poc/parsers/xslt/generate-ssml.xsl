<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:epub="http://www.idpf.org/2007/ops"
    xmlns:html="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="no"/>
    <xsl:template match="html:*">
        <xsl:for-each-group
            group-adjacent="ancestor-or-self::html:*[@lang][1]/@lang"
            select=".//html:span[@class eq 'sentence']">
            <ssml lang="{current-grouping-key()}">
                <xsl:apply-templates select="current-group()"/>
            </ssml>
        </xsl:for-each-group>
    </xsl:template>
    <xsl:template match="text()">
        <xsl:copy/>
    </xsl:template>
    <!-- Disable SSML markup of emphasis for the following reasons:
        (1) all italics are marked up as <em>;
        (2) even the "moderate" emphasis is much too strong -->
    <!--<xsl:template match="html:em|html:strong">
        <emphasis strength="moderate">
            <xsl:apply-templates select=".//text()"/>
        </emphasis>
    </xsl:template>-->
    <xsl:template match="text()[ancestor::html:a/@epub:type eq 'noteref']">
        <break strength="weak"/>
        <sub alias="{'note ' || .}">
            <xsl:copy/>
        </sub>
        <break strength="strong"/>
    </xsl:template>
    <xsl:template
        match="text()[ancestor::html:abbr/@epub:type eq 'z3998:initialism']">
        <say-as interpret-as="characters">
            <xsl:copy/>
        </say-as>
    </xsl:template>
    <xsl:template match="html:span[@class eq 'sentence']">
        <s>
            <xsl:apply-templates select=".//text()"/>
        </s>
    </xsl:template>
    <xsl:template mode="SENTENCE_INPUT"
        match="html:span[@class eq 'sentence']">
        <ssml lang="{ancestor-or-self::html:*[@lang][1]/@lang}">
            <s>
                <xsl:apply-templates select=".//text()"/>
            </s>
        </ssml>
    </xsl:template>
</xsl:stylesheet>