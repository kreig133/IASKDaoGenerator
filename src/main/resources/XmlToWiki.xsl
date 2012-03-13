<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/">

        <xsl:text> || № пп || Название || Переименова в || SQL-тип || IN / OUT || Default || Комментарии || </xsl:text>

        <xsl:for-each select="//inputParametrs/parameter">
            <xsl:text>| </xsl:text>
            <xsl:value-of select="1" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@name" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@renameTo" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@sqlType" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@inOut" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@defaultValue" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@comment" />
            <xsl:text>| </xsl:text>
        </xsl:for-each>

        <xsl:text> || № пп || Название || Переименова в || SQL-тип || IN / OUT || Default || Комментарии || </xsl:text>
        <xsl:for-each select="//outputParametrs/node()">
            <xsl:text>| </xsl:text>
            <xsl:value-of select="1" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@name" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@renameTo" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@sqlType" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@inOut" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@defaultValue" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@comment" />
            <xsl:text>| </xsl:text>
        </xsl:for-each>
    </xsl:template>



</xsl:stylesheet>