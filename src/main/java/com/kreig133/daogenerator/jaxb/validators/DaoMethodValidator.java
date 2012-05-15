package com.kreig133.daogenerator.jaxb.validators;

import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoMethodValidator {
    public static boolean checkDaoMethods( List<DaoMethod> daoMethods, boolean isAnalyticMode ) {
        boolean allIsOk = true;
        for ( DaoMethod daoMethod : daoMethods ) {
            allIsOk = checkJavaClassNames( daoMethod, isAnalyticMode ) && allIsOk;
            allIsOk = checkRenameTos     ( daoMethod ) && allIsOk;
        }
        return  allIsOk;
    }

    static boolean checkRenameTos( DaoMethod daoMethod ) {
        List<ParameterType> parameter = new ArrayList<ParameterType>( daoMethod.getInputParametrs().getParameter() );
        parameter.addAll( daoMethod.getOutputParametrs().getParameter() );

        boolean containsEmptyRenameTo = false;

        for ( ParameterType parameterType : parameter ) {
            if( StringUtils.isBlank( parameterType.getRenameTo() ) ) {
                containsEmptyRenameTo = true;
                System.out.println( String.format( "ERROR! В методе %s в RenameTo есть пустые значения!",
                        getMethodName( daoMethod ) )
                );
            }
        }

        boolean containsSameRenameToValues =
                daoMethod.getInputParametrs ().containsSameRenameTo() ||
                        daoMethod.getOutputParametrs().containsSameRenameTo();
        return ! ( containsSameRenameToValues ||  containsEmptyRenameTo );
    }

    private static String getMethodName( DaoMethod daoMethod ) {
        return StringUtils.isBlank( daoMethod.getCommon().getMethodName() ) ?
                "<Без названия>" : daoMethod.getCommon().getMethodName();
    }

    static boolean checkJavaClassNames( DaoMethod daoMethod, boolean analyticMode ) {
        boolean isOk = true;
        String errorMessage = "ERROR! Для метода %s не указано javaClassName для %s модели!";
        if( DaoJavaClassGenerator.checkToNeedOwnInClass( daoMethod ) ){
            if( StringUtils.isBlank(daoMethod.getInputParametrs().getJavaClassName() ) ){
                System.out.println(String.format(
                        errorMessage,
                        getMethodName( daoMethod ),
                        "входной"
                )
                );
                isOk = false;
            }
        }
        if( DaoJavaClassGenerator.checkToNeedOwnOutClass( daoMethod ) ){
            if( StringUtils.isBlank(daoMethod.getOutputParametrs().getJavaClassName() ) ){
                System.out.println(String.format(
                        errorMessage,
                        getMethodName( daoMethod ),
                        "выходной"
                )
                );
                isOk = false;
            }
        }
        return isOk || analyticMode;
    }
}
