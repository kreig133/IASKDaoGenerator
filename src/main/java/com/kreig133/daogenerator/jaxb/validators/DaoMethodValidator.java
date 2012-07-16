package com.kreig133.daogenerator.jaxb.validators;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoMethodValidator {
    public static boolean checkDaoMethods( List<DaoMethod> daoMethods, boolean isAnalyticMode ) {
        System.out.println( "---------ЭТАП ВАЛИДАЦИИ ВХОДНЫХ XML-ФАЙЛОВ------------" );
        boolean allIsOk = true;
        for ( DaoMethod daoMethod : daoMethods ) {
            System.out.println( " Проверка метода " + getMethodName( daoMethod ) );
            allIsOk = checkJavaClassNames( daoMethod, isAnalyticMode ) && allIsOk;
            allIsOk = checkRenameTos     ( daoMethod                 ) && allIsOk;
            allIsOk = checkAccordingTypeAndNameWithHungarianNotation( daoMethod ) && allIsOk;
        }
        return allIsOk;
    }

    static boolean checkRenameTos( DaoMethod daoMethod ) {
        List<ParameterType> parameter = new ArrayList<ParameterType>( daoMethod.getInputParametrs().getParameter() );
        parameter.addAll( daoMethod.getOutputParametrs().getParameter() );

        boolean containsEmptyRenameTo = false;

        for ( ParameterType parameterType : parameter ) {
            if( StringUtils.isBlank( parameterType.getRenameTo() ) ) {
                containsEmptyRenameTo = true;
                System.out.println( ParametersType.RENAME_TO_ERROR + " с пустыми значениями!" );
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
        String errorMessage = "\tERROR! Для метода не указано javaClassName для %s модели!";
        if( DaoJavaClassGenerator.checkToNeedOwnInClass( daoMethod ) ){
            if( StringUtils.isBlank(daoMethod.getInputParametrs().getJavaClassName() ) ) {
                System.out.println( String.format( errorMessage, "входной" ) );
                isOk = false;
            }
        }
        if( DaoJavaClassGenerator.checkToNeedOwnOutClass( daoMethod ) ){
            if( StringUtils.isBlank(daoMethod.getOutputParametrs().getJavaClassName() ) ){
                System.out.println(String.format(
                        errorMessage,
                        "выходной"
                )
                );
                isOk = false;
            }
        }
        return isOk || analyticMode;
    }

    static boolean checkAccordingTypeAndNameWithHungarianNotation( DaoMethod daoMethod ) {
        Iterable<ParameterType> filtered =
                Iterables.filter( daoMethod.getOutputParametrs().getParameter(), new Predicate<ParameterType>() {
                    @Override
                    public boolean apply( @Nullable ParameterType type ) {
                        boolean result = !  type.getType().isNameAccordHungarianNotation( type.getName() );
                        if ( result ) {
                            System.out.println( String.format (
                                    "Название параметра %s не соответствует венгерской нотации. Есть " +
                                    "вероятность, что генератор неправильно определил тип переменной",
                                    type.getName()
                            ) );
                        }
                        return result;
                    }
                } );
        return  Iterables.isEmpty( filtered );
    }

}
