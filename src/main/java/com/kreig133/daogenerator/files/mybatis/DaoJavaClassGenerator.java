package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.enums.MethodType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.JavaType;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;

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
            DaoMethod daoMethod
    ) {
        final List<ParameterType> parameters = daoMethod.getInputParametrs().getParameter();

        final Type type = Settings.settings().getType();

        return  ( parameters.size() > 3 && type == Type.DEPO ) ||
                ( parameters.size() > 1 && type == Type.IASK );
    }

    protected void generateMethodSignature(
            final DaoMethod daoMethod,
            final MethodType methodType
    ) {

        if ( Settings.settings().getType() == Type.IASK )
            throw new IllegalArgumentException();

        final List<ParameterType>  inputParameterList = daoMethod.getInputParametrs().getParameter();
        final List<ParameterType> outputParameterList = daoMethod.getOutputParametrs().getParameter();
        final String methodName = daoMethod.getCommon().getMethodName();

        StringBuilder outputClass = new StringBuilder();

        if ( ! outputParameterList.isEmpty() ) {
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( "List<" );
            }
            if ( outputParameterList.size() == 1 ) {
                outputClass.append( outputParameterList.get( 0 ).getType().value() );
                if( outputParameterList.get( 0 ).getType() == JavaType.DATE ) {
                    addImport( DATE_IMPORT );
                }
            } else {
                outputClass.append(
                        PackageAndFileUtils.getShortName( daoMethod.getOutputParametrs().getJavaClassName() )
                );
                addImport( daoMethod.getOutputParametrs().getJavaClassName() );
            }
            if ( daoMethod.getCommon().getConfiguration().isMultipleResult() ) {
                outputClass.append( ">" );
            }
        }

        List<String> inputParams = new ArrayList<String>( inputParameterList.size() );
        if ( ! inputParameterList.isEmpty() ) {
            if ( checkToNeedOwnInClass( daoMethod ) ) {
                inputParams.add(
                        PackageAndFileUtils.getShortName( daoMethod.getInputParametrs().getJavaClassName() ) +
                                " request"
                );
                addImport( daoMethod.getInputParametrs().getJavaClassName() );
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
}
