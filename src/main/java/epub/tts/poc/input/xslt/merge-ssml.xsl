<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="3.0">
    <xsl:param name="SSML_DOCUMENTS" as="document-node(element(ssml))*"/>
    <xsl:param name="PLAIN_TEXT_STRING" as="xs:string?"/>
    <xsl:template match="/ssml">
        <ssml>
            <xsl:copy-of select="@*|node()"/>
            <xsl:copy-of
                select="if ($SSML_DOCUMENTS)
                        then (' ', $SSML_DOCUMENTS/ssml/node())
                        else if ($PLAIN_TEXT_STRING)
                        then (' ' || $PLAIN_TEXT_STRING)
                        else ''"/>
        </ssml>
    </xsl:template>
</xsl:stylesheet>