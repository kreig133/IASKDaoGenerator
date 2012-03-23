<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        xmlns:apl="com.aplana.dao-generator"
        extension-element-prefixes="apl"
        >

    <xsl:output method="text" encoding="windows-1251" />

    <xsl:template match="/">

        <xsl:text>h3. Описание </xsl:text>
        <xsl:choose>
            <xsl:when test="//apl:configuration/@type = 'CALL'">
                <xsl:text>хранимой процедуры </xsl:text>
                <xsl:value-of select="//apl:spName"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="//apl:configuration/@type"/>
                <xsl:text>-запроса </xsl:text>
                <xsl:value-of select="//apl:methodName"/>
                <xsl:text>&#10;{code:lang=sql}&#10;</xsl:text>
                <xsl:value-of select="//apl:query"/>
                <xsl:text>{code}&#10;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>|TYPE|</xsl:text>
        <xsl:value-of select="//apl:configuration/@type" />
        <xsl:text>|&#10;</xsl:text>
        <xsl:text>|MULTIPLE|</xsl:text>
        <xsl:value-of select="//apl:configuration/@multipleResult" />
        <xsl:text>|&#10;----&#10;</xsl:text>


        <xsl:text>h5. Назначение:&#10;</xsl:text>
        <xsl:value-of select="//apl:common/apl:comment/text()"/>
        <xsl:text>----&#10;</xsl:text>

        <xsl:text>h5. Входные параметры&#10;</xsl:text>
        <xsl:text> || № пп || Название || Rename || SQL-тип || IN / OUT || Default || Комментарии ||&#10; </xsl:text>
        <xsl:for-each select="//apl:inputParametrs/apl:parameter">
            <xsl:text>| </xsl:text>
            <xsl:value-of select="position()" />
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
            <xsl:text>|&#xA;</xsl:text>
        </xsl:for-each>

        <xsl:text>----&#xA;&#xA;</xsl:text>

        <xsl:text>h5. Выходные данные&#10;</xsl:text>
        <xsl:text> || № пп || Название || SQL-тип || Комментарии || &#xA;</xsl:text>
        <xsl:for-each select="//apl:outputParametrs/apl:parameter">
            <xsl:text>| </xsl:text>
            <xsl:value-of select="position()" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@name" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@sqlType" />
            <xsl:text>| </xsl:text>
            <xsl:value-of select="@comment" />
            <xsl:text>|&#xA;</xsl:text>
        </xsl:for-each>
        <xsl:text>----&#xA;&#xA;</xsl:text>

        <xsl:text>h5. Пример выполнения: &#xA;</xsl:text>
        <xsl:text>{code:language=sql}</xsl:text>
        <xsl:choose>
            <xsl:when test="//apl:configuration/@type = 'CALL'">
                <xsl:text>execute dbo.</xsl:text>
                <xsl:value-of select="//apl:spName"/>
                <xsl:text>;1&#xA;</xsl:text>
                <xsl:for-each select="//apl:inputParametrs/apl:parameter">
                    <xsl:text>    @</xsl:text>
                    <xsl:value-of select="@name"/>
                    <xsl:text> = </xsl:text>

                    <xsl:call-template name="printTestValue">
                        <xsl:with-param name="type" select="@type"/>
                        <xsl:with-param name="testValue" select="@testValue" />
                    </xsl:call-template>

                    <xsl:if test="not( position() = last() )">
                        <xsl:text>,</xsl:text>
                    </xsl:if>

                    <xsl:text>&#xA;</xsl:text>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="query" select="//apl:query/text()"/>
                <xsl:call-template name="fillQuery">
                    <xsl:with-param name="query" select="$query"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>

        <xsl:text>&#xA;{code}</xsl:text>

    </xsl:template>

    <xsl:template name="fillQuery">
        <xsl:param name="query" />

        <xsl:variable name="before" select="substring-before($query, '${')" as="xs:string"/>

        <xsl:value-of select="$before"/>

        <xsl:variable name="nameOfParamWithTail" select="substring-after( $query, '${')"/>
        <xsl:variable name="nameOfParam" select="substring-before( $nameOfParamWithTail, ';')"/>

        <xsl:call-template name="findParam">
            <xsl:with-param name="name" select="$nameOfParam"/>
        </xsl:call-template>

        <xsl:variable name="newQuery" select="substring-after($query, '}' )"/>

        <xsl:choose>
            <xsl:when test=" string-length($newQuery) &gt; 0 ">
                <xsl:call-template name="fillQuery">
                    <xsl:with-param name="query" select="$newQuery"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$query"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="findParam">
        <xsl:param name="name"/>

        <xsl:call-template name="printTestValue">
            <xsl:with-param name="type" select="//apl:inputParametrs/apl:parameter[@name = $name ]/@type"/>
            <xsl:with-param name="testValue" select="//apl:inputParametrs/apl:parameter[@name = $name ]/@testValue"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="printTestValue">
        <xsl:param name="type" as="xs:string"/>
        <xsl:param name="testValue" as="xs:string"/>
        
        <xsl:choose>
            <xsl:when test="$testValue=null">
                <xsl:text>NULL</xsl:text>
            </xsl:when>
            <xsl:when test="$type='String' or $type='Date'">
                <xsl:text>'</xsl:text>
                <xsl:value-of select="$testValue"/>
                <xsl:text>'</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$testValue"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>