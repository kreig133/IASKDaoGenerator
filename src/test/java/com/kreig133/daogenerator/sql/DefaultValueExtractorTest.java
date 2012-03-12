package com.kreig133.daogenerator.sql;

import junit.framework.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DefaultValueExtractorTest {

    private String string = "asdfasCREATE PROCEDURE dbo.anyStoreProcedure( laksjfffфаывафыва AS ) AS AS";

    @Test
    public void test(){
        final Pattern pattern =
                Pattern.compile( "(?isu)create\\s+procedure(.*?)\\bas\\b" );

        final Matcher matcher = pattern.matcher( string );

        matcher.find();

        final int i = matcher.groupCount();

        System.out.println( "i = " + i );

        System.out.println( "pattern.matcher( string ).group() = " + matcher.group( 1 ) );

        System.out.println( "matches = " + matcher.matches() );
        final Matcher matcher1 = pattern.matcher( s );
        matcher1.find();
        System.out.println( matcher1.group( 1 ) );
    }

    @Test
    public void test1(){
        Pattern stringPattern = Pattern.compile( "(?isu)@sVSign\\s+[^@]+?=\\s*['\"](.*?)['\"]" );
        final Matcher matcher = stringPattern.matcher( s );
        matcher.find();
        System.out.println( " test1. " + matcher.group(1));
    }

    @Test
    public void test2(){
//        for ( String parameterName : new String[] {"tob", "acca", "accs", "naccform"}){
//
//            Pattern nullPattern = getNullPatternForParameter( parameterName );
//
//            final Matcher matcher = nullPattern.matcher( s );
//            Assert.assertTrue( matcher.find() );
//            System.out.println( "parameterName = "+ parameterName + "\n" +  matcher.group() );
//        }
    }

    @Test
    public void test3(){
//        Object parameterName;
//        Pattern numberPattern = Pattern.compile( String.format( "(?isu)@%s\\s+.+?=\\s*[\\d\\.]+\\b", parameterName ) );

    }



    String s = "\n" +
            "\n" +
            "CREATE PROCEDURE dbo.zvaSelectCarry_Bill \n" +
            "\t @dtb\t\tDateTime\t\t\t-- Дата начала периода\n" +
            "\t,@dte\t\tDateTime\t\t\t-- Дата окончания периода\n" +
            "\t,@AccA\t\tVarChar(50)\t= NULL\t\t-- Аналит. счет\n" +
            "\t,@AccS\t\tVarChar(50)\t= NULL\t\t-- Синтетический счет\n" +
            "\t,@nAccForm\tInt\t\t= NULL\t\t-- вид счетов (=0 общие, =1 индивидуальные, =2 всякие)\n" +
            "\t,@TOb\t\tInt\t\t= NULL\t\t-- Тип оборотов (-1 - все, 1 - основные, 2 - испр. осн.)\n" +
            "\t,@PPer\t\tInt\t\t= 1\t\t-- Переоценка\n" +
            "\t,@IDU\t\tInt\t\t= NULL\t\t-- Пользователь\n" +
            "\t,@sVSign\tVarChar(3)\t= '  =  '\t\t-- Знак для валюты\n" +
            "\t,@sVal\t\tVarChar(3)\t= NULL\t\t-- Код валюты (840, 810, ...)\n" +
            "\t,@sMerr\t\tVarChar(255)\t= NULL\tOUTPUT\t-- Сообщение об ошибке\n" +
            "\t,@TName\t\tVarChar(30)\t= NULL\t\t-- Имя таблицы для помещения результата\n" +
            "\t,@nPackID\tint\t\t= NULL\t\t-- Заданный ID договора\n" +
            "\t,@nViewSelect\tint\t\t= 0\t\t-- Вид выходного селекта\n" +
            "\t,@nAllAcc\tint\t\t= 0\t\t-- по всем счетам заданного договора\n" +
            "\t,@nCuidUser \tINT\t\t= NULL\t\t-- идентификатор пользователя ИАСК\n" +
            "\t,@nCuidOsb \tINT\t\t= NULL\t\t-- идентификатор ОСБ, по которому следует фильтровать договора\n" +
            "\t,@GetType\tINT \t\t= NULL\t\t-- тип запроса к merXGetPacks \n" +
            "\t\t\t\t\t\t\t--\t0 договора пользователя\n" +
            "\t\t\t\t\t\t\t--\t1 договора филиала\n" +
            "\t\t\t\t\t\t\t--\t2 договора ОСБ\n" +
            "\t,@nWithoutObsp\tINT\t\t= NULL\t\t-- без договоров обеспечения\n" +
            "\t,@nWithEqAcc\tINT\t\t= 0\t\t-- не показывать обороты по одинаковым счетам дебета/кредита\n" +
            "\t,@Timing\tINT\t\t= 0\t\t-- Отладочный режим с печатью времени выполнения\n" +
            "\t,@SOb\t\tINT\t\t= 1\t\t-- 1 - обороты <> 0 / 0 - все обороты\n" +
            "\t\t\t\t\t\t\t-- 2 - обороты <> 0 и/или остатки <> 0\n" +
            "\t,@nPlastic \tINT\t\t= NULL\t\t-- xxxx xxx1 отображать договора пластиковых карт\n" +
            "\t\t\t\t\t\t\t-- xxxx xx1x отображать все договора, кроме пластиковых карт\n" +
            "\t,@nStorno\tINT\t\t= 0\t\t-- не показывать ссылки на сторнирующие документы\n" +
            "\t,@n4Client\tINT\t\t= 0\t\t-- =1 выписка для клиента, =0 выписка для банка\n" +
            "AS\n" +
            "/*\t\tПроцедура подготовки выписок по счетам\n" +
            "\t13.02.12\n" +
            "\n" +
            "\tДобавлена возможность исключения из оборотов проводок с одинаковым счетов дебета/кредита\n" +
            "*/\n" +
            "/*\n" +
            "  Процедура формирования выписки.\n" +
            "  Возвращаемый набор:\n" +
            "\t IDOp\t\tInt\t\t-- ID операции\n" +
            "\t,DateOp\t\tDateTime\t-- Дата операции\n" +
            "\t,TurnType\tInt\t\t-- Тип оборотов\n" +
            "\t,OpName\t\tVarChar(255)\t-- Назначение платежа\n" +
            "\t,sInDoc\t\tVarChar(20)\t-- Номер внутреннего документа\n" +
            "\t,OutDoc\t\tVarChar(20)\t-- Номер внешнего документа\n" +
            "\t,AccA\t\tVarChar(50)\t-- Счет\n" +
            "\t,DVS\t\tDecimal(18,2)\t-- Валюта Дебет\n" +
            "\t,CVS\t\tDecimal(18,2)\t-- Валюта Кредит\n" +
            "\t,DRS\t\tDecimal(18,2)\t-- Рубли Дебет\n" +
            "\t,CRS\t\tDecimal(18,2)\t-- Рубли Кредит\n" +
            "\t,Flag\t\tInt\t\t-- 1 - Дебет/ 2 - Кредит\n" +
            "\t,NVDS\t\tDecimal(18,2)\t-- Вход. остатки Валюта Дебет\n" +
            "\t,NRDS\t\tDecimal(18,2)\t-- Вход. остатки Рубли Дебет\n" +
            "\t,NVCS\t\tDecimal(18,2)\t-- Вход. остатки Валюта Кредит\n" +
            "\t,NRCS\t\tDecimal(18,2)\t-- Вход. остатки Рубли Кредит\n" +
            "\t,EVDS\t\tDecimal(18,2)\t-- Исходящий остаток по дебету в валюте\n" +
            "\t,ERDS\t\tDecimal(18,2)\t-- Исходящий остаток по дебету в рублях\n" +
            "\t,EVCS\t\tDecimal(18,2)\t-- Исходящий остаток по кредиту в валюте\n" +
            "\t,ERCS\t\tDecimal(18,2)\t-- Исходящий остаток по кредиту в рублях\n" +
            "\t,nIDDoc\t\tInt\t\t-- ID документа\n" +
            "\t,sAcc\t\tVarChar(50)\t-- Номер счета\n" +
            "\t,dTB\t\tDateTime\t-- Дата начала периода\n" +
            "\t,dTE\t\tDateTime\t-- Дата конца периода\n" +
            "\t,LastDate\tDateTime\t-- Дата последней операции\n" +
            "\t,sName\t\tVarChar(255)\t-- Название счета\n" +
            "\t,AorP\t\tInt\t\t-- Акт/Пас\n" +
            "\t,nPackID\tint\t\t-- ID догвора\n" +
            "*/\n" +
            "--===============================================\n" +
            "\n" +
            "Declare\t@Result int, @CUID int,\n" +
            "\t@s4Like \tvarchar(255), @ER int,\n" +
            "\t@sValCode \tvarchar(3), @RetCode int,\n" +
            "\t@dFirstCurs \tdatetime,\n" +
            "\t@LastDate \tdatetime,\n" +
            "\t@nDeltaCR\tdecimal (22,2),\n" +
            "\t@nDeltaDR\tdecimal (22,2),\n" +
            "\t@nCurDeltaCR\tdecimal (22,2),\n" +
            "\t@nCurDeltaDR\tdecimal (22,2),\n" +
            "\t@NVDS\t\tdecimal (22,2),\n" +
            "\t@NRDS\t\tdecimal (22,2),\n" +
            "\t@NVCS\t\tdecimal (22,2),\n" +
            "\t@NRCS\t\tdecimal (22,2),\n" +
            "\t@nAorP\t\tint,\n" +
            "\t@nCountAcc\tint,\n" +
            "\t@nCreDataID\tint,\n" +
            "\t@PACKID\t\tint,\n" +
            "\t@txt\t\tvarchar(255),\n" +
            "\t@nSubSysType\tint,\n" +
            "\t@ans\t\tint, \n" +
            "\t@rowcount\tint\n" +
            "DECLARE\t@dBegin\tDATETIME\n" +
            "DECLARE\t@dEnd\tDATETIME\n";
}
