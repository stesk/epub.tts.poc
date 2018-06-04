<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:html="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="#all"
    version="3.0">
    <xsl:output method="xml" indent="yes"/>
    <xsl:param name="HTML_FILE_NAME" as="xs:string?"/>
    <xsl:param name="ID_VALUES" as="xs:string*"/>
    <xsl:param name="MP3_FILE_NAME" as="xs:string?"/>
    <xsl:param name="START_VALUES" as="xs:string*"/>
    <xsl:param name="END_VALUES" as="xs:string*"/>
    <xsl:template match="/html:html">
        <smil>
            <head>
                <xsl:apply-templates
                    select="html:head/html:meta[@name = ('dc:title',
                            'dc:format', 'dc:identifier')]"/>
                <meta name="dc:format" content="Daisy 2.02" />
                <meta name="ncc:generator" content="EPUB TTS POC"/>
                <layout>
                    <region id="txtView"/>
                </layout>
            </head>
            <body>
                <seq>
                    <xsl:for-each select="$ID_VALUES">
                        <xsl:variable name="position" as="xs:integer"
                            select="position()"/>
                        <xsl:variable name="positionString" as="xs:string"
                            select="format-number($position, '0000')"/>
                        <par id="{'par' || $positionString}" endsync="last">
                            <text id="{.}" src="{$HTML_FILE_NAME || '#' || .}"/>
                            <audio id="{'aud' || $positionString}"
                                clip-begin="{'npt=' || $START_VALUES[$position] || 's'}"
                                clip-end="{'npt=' || $END_VALUES[$position] || 's'}"
                                src="{$MP3_FILE_NAME}"/>
                        </par>
                    </xsl:for-each>
                </seq>
            </body>
        </smil>
    </xsl:template>
    <xsl:template match="html:meta">
        <meta xmlns="">
            <xsl:copy-of select="@*"/>
        </meta>
    </xsl:template>
</xsl:stylesheet>