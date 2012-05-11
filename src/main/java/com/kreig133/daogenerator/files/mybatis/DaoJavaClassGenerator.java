package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.JavaDocGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kreig133
 * @version 1.0
 */
abstract public class DaoJavaClassGenerator extends JavaClassGenerator {

    protected void startingLinesOfDaoFiles() {
        setPackage( Settings.settings().getDaoPackage() );
        addDaoFilesImports();
    }

    protected void addDaoFilesImports() {
        addImport( Settings.settings().getEntityPackage() + ".*" );
    }

    /**
     * Проверяет нужно ли создавать in-класс
     * @param daoMethod
     * @return
     */
    public static boolean checkToNeedOwnInClass(
            @NotNull DaoMethod daoMethod
    ) {
        final List<ParameterType> parameters = daoMethod.getInputParametrs().getParameter();

        return parameters.size() > 1;
    }

    public static boolean checkToNeedOwnOutClass(
            @NotNull DaoMethod daoMethod
    ) {
        return daoMethod.getOutputParametrs().getParameter().size() > 1;
    }

    protected void generateMethodSignature(
            @NotNull final DaoMethod daoMethod,
            final MethodType methodType
    ) {
        final List<ParameterType>  inputParameterList = daoMethod.getInputParametrs().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String methodName = daoMethod.getCommon().getMethodName();

        StringBuilder outputClass = new StringBuilder();

        if ( Utils.collectionNotEmpty( outputParameterList ) ) {
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( "List<" );
            }
            if ( ! checkToNeedOwnOutClass( daoMethod ) ) {
                outputClass.append( outputParameterList.get( 0 ).getType().value() );
                if( outputParameterList.get( 0 ).getType() == JavaType.DATE ) {
                    addImport( DATE_IMPORT );
                }
            } else {
                String outClassName = InOutClassGenerator.getOutClassName( daoMethod );
                outputClass.append(
                        PackageAndFileUtils.getShortName( outClassName )
                );
                addImport( outClassName );
            }
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( ">" );
            }
        }

        List<String> inputParams = new ArrayList<String>( inputParameterList.size() );
        if ( Utils.collectionNotEmpty( inputParameterList ) ) {
            if ( checkToNeedOwnInClass( daoMethod ) ) {
                String inClassName = InOutClassGenerator.getInClassName( daoMethod );
                inputParams.add(
                        PackageAndFileUtils.getShortName( inClassName ) +
                                " request"
                );
                addImport( inClassName );
            } else {
                for ( ParameterType p : inputParameterList ) {
                    if( p.getType() == JavaType.DATE ) {
                        addImport( DATE_IMPORT );
                    }

                    StringBuilder inputParam = new StringBuilder();
                    inputParam.append( "@Param(\"" ).append( p.getRenameTo() ).append( "\") " );
                    inputParam.append( p.getType().value() ).append( " " ).append( p.getRenameTo() );
                    inputParams.add( inputParam.toString() );
                }
            }
        }

        generateMethodSignature( Scope.PUBLIC, outputClass.toString(), methodName, inputParams, null, true );
    }


    protected void generateJavaDocForDaoMethod( DaoMethod daoMethod ) {
        JavaDocGenerator.JavaDocBuilder javaDocBuilder =
                jDoc.getBuilder().initialize().addComment( daoMethod.getCommon().getComment() );

        if( DaoJavaClassGenerator.checkToNeedOwnInClass( daoMethod ) ){
            javaDocBuilder.addParameter( "request" /**TODO хардкод*/, "объект, содержащий входные данные для запроса" );
        } else {
            for ( ParameterType type : daoMethod.getInputParametrs().getParameter() ) {
                javaDocBuilder.addParameter(
                        type.getRenameTo(),
                        StringUtils.isNotBlank( type.getComment() ) ?
                                type.getComment():
                                "входной параметр запроса"
                );
            }
        }
        if ( Utils.collectionNotEmpty( daoMethod.getOutputParametrs().getParameter() ) ) {
            javaDocBuilder.addReturn( "данные, которые вернул запрос" );
        }
        javaDocBuilder.close();
    }
}
