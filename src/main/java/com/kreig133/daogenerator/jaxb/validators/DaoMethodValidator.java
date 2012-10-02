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
import java.util.regex.Pattern;

/**
 * Валидатор дао методов
 * 
 * @author eshangareev
 * @version 1.0
 */
public class DaoMethodValidator {
    
    private static final String CLASSNAME_PATTERN = "^([_a-z]+[_a-z0-9]*\\.)+([_a-zA-Z]+[_a-zA-Z0-9]*)$";
    private static final String METHODNAME_PATTERN = "^([a-z]+[a-zA-Z0-9]*)+$";
    
    public static boolean checkDaoMethods( List<DaoMethod> daoMethods, boolean isAnalyticMode ) {
        System.out.println( "---------ЭТАП ВАЛИДАЦИИ ВХОДНЫХ XML-ФАЙЛОВ------------" );
        boolean allIsOk = true;
        for ( DaoMethod daoMethod : daoMethods ) {
            System.out.println( " Проверка метода " + getMethodName( daoMethod ) );
            allIsOk = checkJavaClassNames( daoMethod, isAnalyticMode ) && allIsOk;
            allIsOk = checkRenameTos     ( daoMethod                 ) && allIsOk;
            allIsOk = checkMethodName    ( daoMethod                 ) && allIsOk;
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
            String javaClassName = daoMethod.getInputParametrs().getJavaClassName();
            if( StringUtils.isBlank(javaClassName ) ) {
                System.out.println( String.format( errorMessage, "входной" ) );
                isOk = false;
            }
            else
                isOk &= checkClassNameSyntax(javaClassName);
        }
        if( DaoJavaClassGenerator.checkToNeedOwnOutClass( daoMethod ) ){
            String javaClassName = daoMethod.getOutputParametrs().getJavaClassName();
            if( StringUtils.isBlank(javaClassName ) ){
                System.out.println(String.format(
                        errorMessage,
                        "выходной"
                )
                );
                isOk = false;
            }
            else
                isOk &= checkClassNameSyntax(javaClassName);
        }
        return isOk || analyticMode;
    }

    /**
     * Проверка потенциальных ошибок несоответствий типов и названий параметров. Носит информативный характер. На 
     * процесс генерации не оказывает никакого влияния
     * 
     * @param daoMethod метаданные о проверяемом методе
     * @return true - проверка не является критичной
     */
    static boolean checkAccordingTypeAndNameWithHungarianNotation( DaoMethod daoMethod ) {
        //Iterable<ParameterType> filtered =
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
        //return Iterables.isEmpty( filtered ); // (Marat Fayzullin, 2012.07.25) необязательная проверка
        return true;
    }
    
    /**
     * Проверяет валидность имени класса
     * 
     * @param className имя класса
     * @return результат проверки
     */
    static boolean checkClassNameSyntax(String className) {
        boolean isValid = Pattern.matches(CLASSNAME_PATTERN, className);
        if (!isValid) {
            System.out.println(String.format("Неверное название имени класса или пакета \"%s\" (лишние пробелы, " +
                    "недопустимые символы и т.д.). Допустим формат \"%s\"", className, CLASSNAME_PATTERN));
        }
        return isValid;
    }
    
    /** 
     * Проверяет валидность имени метода
     * 
     * @param daoMethod сведения о методе
     * @return результат проверки
     */
    static boolean checkMethodName( DaoMethod daoMethod ) {
        String methodName = daoMethod.getCommon().getMethodName();
        boolean isValid = Pattern.matches(METHODNAME_PATTERN, methodName);
        if (!isValid) {
            System.out.println(String.format("Неверное название метода \"%s\" (лишние пробелы, недопустимые символы " +
                    "подчеркивания и т.д.). Допустим формат \"%s\"", methodName, METHODNAME_PATTERN));
        }
        return isValid;
    }

}
