<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:d2e="http://www.nota.dk/dtbook2epub"
    xmlns:epub="http://www.idpf.org/2007/ops"
    xmlns:html="http://www.w3.org/1999/xhtml"
    xmlns="http://www.w3.org/1999/xhtml"
    exclude-result-prefixes="xs d2e html"
    version="3.0">
    <xsl:output method="xml" indent="no"/>
    <xsl:param name="ALLOWED_CLASSES" as="xs:string*"
        select="'acknowledgments', 'authorbiography', 'bridgehead',
                'end_greeting', 'end_greeting_signature', 'frontcover',
                'leftflap', 'note_identifier', 'other_titles', 'part', 'poem',
                'rearcover', 'render_by_both', 'render_by_col',
                'render_by_row', 'rightflap', 'roman', 'summary',
                'this_edition'"/>
    <xsl:param name="LANG" as="xs:string"
        select="if (/dtbook/book/@lang) then /dtbook/book/@lang else 'da'"/>
    <xsl:param name="PID" as="xs:string"
        select="if (/dtbook/head/meta[@name eq 'dc:identifier'])
                then /dtbook/head/meta[@name eq 'dc:identifier']/@content
                else '000000'"/>
    <xsl:param name="TITLE" as="xs:string" select="/dtbook/head/title"/>
    <xsl:template match="/dtbook">
        <html lang="{$LANG}" xml:lang="{$LANG}"
            xmlns:epub="http://www.idpf.org/2007/ops">
            <head>
                <meta charset="UTF-8"/>
                <title>
                    <xsl:value-of select="$TITLE"/>
                </title>
                <meta name="dc:identifier" content="{$PID}"/>
                <meta name="viewport" content="width=device-width"/>
            </head>
            <body>
                <xsl:apply-templates
                    select="book/(frontmatter|bodymatter|rearmatter)/level"/>
            </body>
        </html>
    </xsl:template>
    <xsl:template match="@*"/>
    <xsl:template match="@alt|@colspan|@height|@href|@id|@rowspan|@width">
        <xsl:copy/>
    </xsl:template>
    <xsl:template match="@class">
        <xsl:variable name="values" as="xs:string*"
            select="distinct-values(tokenize(., '\s+'))[. = $ALLOWED_CLASSES]"/>
        <xsl:if test="count($values) gt 0">
            <xsl:attribute name="class" select="$values"/>
        </xsl:if>
    </xsl:template>
    <xsl:template match="@class[. eq 'bridgehead']">
        <xsl:attribute name="epub:type" select="'bridgehead'"/>
        <xsl:next-match/>
    </xsl:template>
    <xsl:template match="@lang">
        <xsl:attribute name="lang" select="."/>
        <xsl:attribute name="xml:lang" select="."/>
    </xsl:template>
    <xsl:template match="@lang[. eq 'xx']"/>
    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="acronym">
        <abbr epub:type="z3998:initialism">
            <xsl:apply-templates select="@*|node()"/>
        </abbr>
    </xsl:template>
    <xsl:template match="div[@class eq 'blockquote']">
        <blockquote>
            <xsl:apply-templates select="@*|node()"/>
        </blockquote>
    </xsl:template>
    <xsl:template match="div[@class eq 'poem']">
        <section epub:type="z3998:verse">
            <xsl:apply-templates select="@*|node()"/>
        </section>
    </xsl:template>
    <xsl:template match="div[@class eq 'stanza']">
        <div class="linegroup">
            <xsl:apply-templates select="@* except @class|node()"/>
        </div>
    </xsl:template>
    <xsl:template match="hd">
        <p epub:type="bridgehead" class="bridgehead">
            <xsl:apply-templates select="@*|node()"/>
        </p>
    </xsl:template>
    <xsl:template match="img">
        <xsl:variable name="imgFileName" as="xs:string"
            select="tokenize(@src, '/')[last()]"/>
        <img src="{concat('images/', $imgFileName)}">
            <xsl:apply-templates select="@*"/>
        </img>
    </xsl:template>
    <xsl:template match="imggroup">
        <xsl:call-template name="FIGURE">
            <xsl:with-param name="attributes" as="attribute()*" select="@*"/>
        </xsl:call-template>
    </xsl:template>
    <xsl:template match="imggroup[count(img) gt 1]">
        <figure class="image-series">
            <xsl:apply-templates select="@* except @class"/>
            <xsl:for-each-group group-starting-with="img" select="*">
                <xsl:variable name="group" as="node()+"
                    select="current-group()"/>
                <xsl:choose>
                    <xsl:when
                        test="count($group) eq 1 and $group[self::prodnote]">
                        <xsl:apply-templates select="$group"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="FIGURE">
                            <xsl:with-param name="contents" as="node()+"
                                select="$group"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each-group>
        </figure>
    </xsl:template>
    <xsl:template match="level|level1|level2|level3|level4|level5|level6">
        <section id="{if (@id) then @id else generate-id(.)}">
            <xsl:apply-templates select="@* except @id|node()"/>
        </section>
    </xsl:template>
    <xsl:template match="levelhd|h1|h2|h3|h4|h5|h6">
        <xsl:variable name="depth" as="xs:integer"
            select="count(ancestor::level|ancestor::level1|ancestor::level2|
                    ancestor::level3|ancestor::level4|ancestor::level5|
                    ancestor::level6)"/>
        <xsl:element name="{concat('h', $depth)}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="levelhd[normalize-space() eq '']"/>
    <xsl:template match="li|list/hd">
        <li>
            <xsl:apply-templates select="@*|node()"/>
        </li>
    </xsl:template>
    <xsl:template match="lic">
        <span class="lic">
            <xsl:apply-templates select="@* except @class|node()"/>
        </span>
    </xsl:template>
    <xsl:template match="line">
        <p class="line">
            <xsl:apply-templates select="@* except @class|node()"/>
        </p>
    </xsl:template>
    <xsl:template match="linenum">
        <span class="linenum">
            <xsl:apply-templates select="@* except @class|node()"/>
        </span>
    </xsl:template>
    <xsl:template match="list">
        <xsl:variable name="class" as="attribute(class)?">
            <xsl:apply-templates select="@class"/>
        </xsl:variable>
        <xsl:variable name="type" as="xs:string"
            select="if (@type = ('ol', 'ul')) then @type else 'ul'"/>
        <xsl:element name="{$type}">
            <xsl:apply-templates select="@* except @class"/>
            <xsl:if test="@bullet eq 'none'">
                <xsl:attribute name="class"
                    select="'list-style-type-none', $class"/>
            </xsl:if>
            <xsl:apply-templates
                select="* except pagenum[not(following-sibling::li)]"/>
        </xsl:element>
        <xsl:apply-templates select="pagenum[not(following-sibling::li)]">
            <xsl:with-param name="inBlockContext" as="xs:boolean"
                select="true()"/>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template mode="#default CONVERT_NOTES" match="note">
        <xsl:variable name="classes" as="xs:string*"
            select="tokenize(@class, '\s+')"/>
        <xsl:choose>
            <xsl:when test="$classes = ('endnote', 'footnote', 'rearnote')">
                <xsl:variable name="type" as="xs:string"
                    select="if ($classes = ('endnote', 'rearnote'))
                            then 'rearnote' else 'footnote'"/>
                <li id="{@id}" epub:type="{$type}" class="notebody">
                    <xsl:call-template name="SKIP_SINGLE_PARAGRAPH"/>
                </li>
            </xsl:when>
            <xsl:otherwise>
                <aside id="{@id}" epub:type="note" class="notebody">
                    <xsl:apply-templates/>
                </aside>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template
        match="note[tokenize(@class, '\s+') = ('endnote', 'footnote', 'rearnote')]">
        <ol class="list-style-type-none">
            <xsl:for-each
                select="self::note|following-sibling::* except
                        following-sibling::*[not(self::note|self::pagenum
                        [d2e:is-followed-by-note(.)])][1]/(self::*|
                        following-sibling::*)">
                <xsl:apply-templates mode="CONVERT_NOTES" select="."/>
            </xsl:for-each>
        </ol>
    </xsl:template>
    <xsl:template priority="1"
        match="note[d2e:is-foot-or-rearnote(.)][d2e:is-preceded-by-note(.)]"/>
    <xsl:template match="noteref">
        <a epub:type="noteref" class="noteref"
            href="{replace(@idref, '^#', '')}">
            <xsl:apply-templates select="@* except @class|node()"/>
        </a>
    </xsl:template>
    <xsl:template
        match="p[@class = ('precedingemptyline', 'precedingseparator')]">
        <hr class="{replace(@class, '^preceding', '')}"/>
        <xsl:next-match/>
    </xsl:template>
    <xsl:template mode="#default CONVERT_NOTES" match="pagenum">
        <xsl:param name="inBlockContext" as="xs:boolean"
            select="d2e:is-in-block-context(.)"/>
        <xsl:element
            name="{if ($inBlockContext) then 'div' else 'span'}">
            <xsl:copy-of select="@id"/>
            <xsl:attribute name="epub:type" select="'pagebreak'"/>
            <xsl:attribute name="title" select="text()"/>
            <xsl:attribute name="class" select="concat('page-', @page)"/>
        </xsl:element>
    </xsl:template>
    <xsl:template match="pagenum[d2e:is-followed-by-note(.)]"/>
    <xsl:template match="prodnote">
        <aside epub:type="z3998:production" class="desc">
            <xsl:apply-templates select="@* except @class|node()"/>
        </aside>
    </xsl:template>
    <xsl:template match="prodnote[@class eq 'caption']">
        <figcaption>
            <xsl:apply-templates select="@* except @class"/>
            <xsl:call-template name="SKIP_SINGLE_PARAGRAPH"/>
        </figcaption>
    </xsl:template>
    <xsl:template match="sidebar">
        <aside epub:type="sidebar" class="sidebar">
            <xsl:apply-templates select="@* except @class|node()"/>
        </aside>
    </xsl:template>
    <xsl:template match="span[@lang eq 'xx' and count(@* except @id) eq 1]">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="table[not(thead|tbody|tfoot)]">
        <table>
            <xsl:apply-templates select="@*"/>
            <tbody>
                <xsl:apply-templates/>
            </tbody>
        </table>
    </xsl:template>
    <!-- NAMED TEMPLATES -->
    <xsl:template name="FIGURE" as="element(html:figure)">
        <xsl:param name="attributes" as="attribute()*"/>
        <xsl:param name="contents" as="node()*" select="node()"/>
        <figure class="image">
            <xsl:apply-templates select="$attributes except @class"/>
            <xsl:apply-templates select="$contents[self::img]"/>
            <xsl:apply-templates
                select="$contents[self::prodnote][@class eq 'caption']"/>
            <xsl:apply-templates
                select="$contents[self::prodnote][not(@class eq 'caption')]"/>
        </figure>
    </xsl:template>
    <xsl:template name="SKIP_SINGLE_PARAGRAPH" as="node()*">
        <xsl:choose>
            <xsl:when test="count(p) eq 1 and count(*) eq 1">
                <xsl:apply-templates select="p/node()"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <!-- FUNCTIONS -->
    <xsl:function name="d2e:is-followed-by-note" as="xs:boolean">
        <xsl:param name="n" as="node()"/>
        <xsl:value-of
            select="$n/exists(following-sibling::*[not(self::pagenum)][1]/
                    self::note[d2e:is-foot-or-rearnote(.)])"/>
    </xsl:function>
    <xsl:function name="d2e:is-foot-or-rearnote" as="xs:boolean">
        <xsl:param name="e" as="element()"/>
        <xsl:value-of
            select="tokenize($e/@class, '\s+') = ('footnote', 'endnote',
                    'rearnote')"/>
    </xsl:function>
    <xsl:function name="d2e:is-in-block-context" as="xs:boolean">
        <xsl:param name="n" as="node()"/>
        <xsl:value-of
            select="$n/exists(parent::div|parent::imggroup|parent::level|
                    parent::prodnote|parent::sidebar)"/>
    </xsl:function>
    <xsl:function name="d2e:is-preceded-by-note" as="xs:boolean">
        <xsl:param name="n" as="node()"/>
        <xsl:value-of
            select="$n/exists(preceding-sibling::*[not(self::pagenum)][1]/
                    self::note[d2e:is-foot-or-rearnote(.)])"/>
    </xsl:function>
</xsl:stylesheet>