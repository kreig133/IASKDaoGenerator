package com.kreig133.daogenerator.files.mybatis.test;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.intrface.InterfaceGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.settings.Settings;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TesterClassGenerator extends JavaClassGenerator{

    private final static TesterClassGenerator INSTANCE = new TesterClassGenerator();
    public static final String PARENT = "ru.sbrf.iask.dao.testing.AbstractDaoExecuteTest";

    private TesterClassGenerator() {
    }

    public static TesterClassGenerator instance(){
        return INSTANCE;
    }

    @NotNull
    @Override
    public File getFile() {
        final File file = new File(
                Settings.settings().getPathForGeneratedTests() + "/"
                        + PackageAndFileUtils.replacePointBySlash( Settings.settings().getMapperPackage() )+ "/"
                        + getFileName()
                        + JAVA_EXTENSION
        );
        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );
        return file;
    }

    @Override
    public void generateHead() {
        setPackage( Settings.settings().getMapperPackage() );
        insertLine();
        addImport( PARENT );
        addImport( "org.junit.Test" );
        addImport( "org.springframework.beans.factory.annotation.Autowired" );
        addImport( Settings.settings().getDaoPackage() + "." + InterfaceGenerator.instance().getFileName() );
        addImport( "java.util.HashMap" );
        addImport( "java.util.Map" );
        insertClassDeclaration( ClassType.CLASS, getFileName(), PackageAndFileUtils.getShortName( PARENT ), null );
        insertTabs().append( "@Autowired" );
        insertLine();
        insertTabs().append( InterfaceGenerator.instance().getFileName() ).append( " " )
                .append( "dao" ).append( ";" );
        insertLine();
    }

    @Override
    public void generateBody( @NotNull DaoMethod daoMethod ) {
        insertLine();
        insertTabs().append( "@Test" );
        insertLine();
        generateMethodSignature(
                Scope.PUBLIC,
                null,
                daoMethod.getCommon().getMethodName() + "RunTest",
                null,
                null,
                false
        );
        insertLine();
        insertTabs().append( "final Map<String, String> values = new HashMap<String, String>();" );
        insertLine();
        for ( ParameterType parameterType : daoMethod.getInputParametrs().getParameter() ) {
            insertTabs().append(
                    String.format( "values.put( \"%s\", \"%s\");",
                            parameterType.getRenameTo(),
                            parameterType.getTestValue() ));
            insertLine();
        }
        insertLine();
        insertTabs().append( "final String methodName = \"" ).append( daoMethod.getCommon().getMethodName() )
                .append( "\";" );
        insertLine();
        insertTabs().append( "Object invoke = invoke( dao, values, methodName );" );
        insertLine();
        insertTabs().append( "org.junit.Assert.assertNotNull( invoke );" );
        insertLine();
        closeMethodOrInnerClassDefinition();
    }

    @NotNull
    @Override
    public String getFileName() {
        return Settings.settings().getOperationName() + "DaoTest";
    }
}
