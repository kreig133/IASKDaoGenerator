package com.kreig133.daogenerator.db.extractors.in;

import com.kreig133.daogenerator.TestHelper;
import com.kreig133.daogenerator.db.extractors.SqlTypeHelper;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SpInputParameterExtractorTest{

    @Before
    public void before(){
        Settings.settings().setType( Type.TEST );
    }

    @Test
    public void extractInputParamsTest(){
        DaoMethod daoMethod = new DaoMethod();
        daoMethod.setCommon( new CommonType() );
        daoMethod.setInputParametrs( new ParametersType() );
        daoMethod.getCommon().setSpName( "sp_bilPg_GetBillMakerList" );

        final List<ParameterType> sp_bilPg_getBillMakerList =
                SpInputParameterExtractor.instance().extractInputParams( daoMethod  ).getInputParametrs().getParameter();

        assertEquals( 37, sp_bilPg_getBillMakerList.size() );
    }

    @Test
    public void extractDataFromResultSetRowTest() throws SQLException {
        ResultSet rs = Mockito.mock( ResultSet.class );

        Mockito.when( rs.getString( SqlTypeHelper.DATA_TYPE_COLUMN ) ).thenReturn( "int" );
        Mockito.when( rs.getString( SpInputParameterExtractor.PARAMETER_NAME_COLUMN ) ).thenReturn( "id" );
        Mockito.when( rs.getString( SpInputParameterExtractor.PARAMETER_MODE ) ).thenReturn( "IN" );

        final ParameterType parameterType =
               ( ( SpInputParameterExtractor ) SpInputParameterExtractor.instance() ).extractDataFromResultSetRow( rs );

        assertEquals( parameterType.getName(), "id" );
        assertEquals( parameterType.getType(), JavaType.LONG );
    }

    @Test
    public void getSPTextTest(){
        ( ( SpInputParameterExtractor ) SpInputParameterExtractor.instance() ).getSPText( "sp_bilPg_GetBillMakerList" );
        final String sp_bilPg_getBillMakerList = SpInputParameterExtractor.getSPText();
        System.out.println( "sp_bilPg_getBillMakerList = " + sp_bilPg_getBillMakerList );
        assertNotNull( sp_bilPg_getBillMakerList );

    }

    @Test
    public void fillTestValuesByInsertedQuery() {
        final ArrayList<ParameterType> inputParametrs = new ArrayList<ParameterType>();
        inputParametrs.add( getParameterType( "d_realdate", JavaType.STRING ) );
        inputParametrs.add( getParameterType( "d2_realdate", JavaType.DATE ) );
        inputParametrs.add( getParameterType( "id_mode", JavaType.DOUBLE ) );
        inputParametrs.add( getParameterType( "b_resident", JavaType.BYTE ) );
        inputParametrs.add( getParameterType( "id_user", JavaType.STRING ) );
        inputParametrs.add( getParameterType( "iexternal", JavaType.LONG ) );

        DaoMethod daoMethodForTest = TestHelper.getDaoMethodForTest();
        daoMethodForTest.getInputParametrs().getParameter().clear();
        daoMethodForTest.getInputParametrs().getParameter().addAll( inputParametrs );
        daoMethodForTest.getCommon().setQuery( TestHelper.spCall );

        SpInputParameterExtractor.instance().fillTestValuesByInsertedQuery( daoMethodForTest );

        Assert.assertEquals( inputParametrs.get( 0 ).getTestValue(), "3-22-1990 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 1 ).getTestValue(), "3-22-2000 0:0:0.000" );
        Assert.assertEquals( inputParametrs.get( 2 ).getTestValue(), "2" );
        Assert.assertEquals( inputParametrs.get( 3 ).getTestValue(), "-1" );
        Assert.assertEquals( inputParametrs.get( 4 ).getTestValue(), "null" );
        Assert.assertEquals( inputParametrs.get( 5 ).getTestValue(), "-1" );
    }

    @Test
    public void getParentSpNameTest() throws SQLException {
        ResultSet mock = Mockito.mock( ResultSet.class );
        Mockito.when( mock.getString( SpInputParameterExtractor.S_EXECUTE ) ).thenReturn(
                "insert into ##_usp_pg_journal_leads_new_mode1 exec dbo.usp_journal_leads_new @n_co_id=@n_co_id," +
                        "@n_u_id=@n_u_id,@tdivisionid=@tdivisionid,@dtdatefrom=@dtdatefrom,@dtdateto=@dtdateto," +
                        "@dtexecfrom=@dtexecfrom,@dtexecto=@dtexecto,@tvalueid=@tvalueid,@tflag=@tflag,@ddepo=@ddepo," +
                        "@dtype=@dtype,@drasd=@drasd,@dsynt=@dsynt,@cdepo=@cdepo,@ctype=@ctype,@crasd=@crasd," +
                        "@csynt=@csynt,@sregnum=@sregnum,@dtregdate=@dtregdate,@nbondnum=@nbondnum,@tuserid=@tuserid," +
                        "@treguserid=@treguserid,@kmin=@kmin,@flinclude=@flinclude,@i_mode=@i_mode;    " +
                        "select @i_rowCount = @@ROWCOUNT; update ##_SessionDataSet set iRowCount = @i_rowCount " +
                        "where iSessionDataSetID = @id_sessionDS;"
        );
        String parentSpName = ( ( SpInputParameterExtractor ) SpInputParameterExtractor.instance() ).getParentSpName(
                mock );

        Assert.assertEquals( "usp_journal_leads_new", parentSpName );
    }

    @Test
    public void getDefinitionFromSpText1(){
        Assert.assertEquals( ( ( SpInputParameterExtractor ) SpInputParameterExtractor.instance() ).getDefinitionFromSpText(
                s ), result );
    }
    @Test
    public void getDefinitionFromSpText2(){
        Assert.assertEquals( ( ( SpInputParameterExtractor ) SpInputParameterExtractor.instance() ).getDefinitionFromSpText(
                string ), " dbo.anyStoreProcedure( -- AS\n" +
                " laksjfffфаывафыва " );
    }

    @NotNull
    private ParameterType getParameterType( String name, JavaType type ) {
        ParameterType parameterType = new ParameterType();
        parameterType.setName( name );
        parameterType.setType( type );
        return parameterType;
    }

    @NotNull
    String string = "asdfasCREATE PROCEDURE dbo.anyStoreProcedure( -- AS\n laksjfffфаывафыва AS ) AS AS";

    @NotNull
    String result = " dbo.zvaSelectCarry_Bill \n" +
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
            "\t,@n4Client\tINT\t\t= 0\t\t-- =1 выписка для клиента, =0 выписка для банка\n";

    @NotNull
    String s =
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
