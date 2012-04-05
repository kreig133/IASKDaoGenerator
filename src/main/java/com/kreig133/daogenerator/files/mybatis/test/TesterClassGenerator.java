package com.kreig133.daogenerator.files.mybatis.test;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
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

    private TesterClassGenerator() {
    }

    private static TesterClassGenerator INSTANCE;

    public static TesterClassGenerator instance(){
        if ( INSTANCE == null ) {
            INSTANCE = new TesterClassGenerator();
        }
        return INSTANCE;
    }

    public static final String TEST_CONFIG = "gwt-rpc-servlet.xml";

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
        addImport( "com.aplana.sbrf.deposit.AbstractDepoDaoExecuteTest" );
        addImport( "org.junit.Test" );
        addImport( "org.springframework.beans.factory.annotation.Autowired" );
        addImport( "org.springframework.test.context.ContextConfiguration" );
        addImport( "org.springframework.test.context.junit4.SpringJUnit4ClassRunner" );
        addImport( Settings.settings().getMapperPackage() + "." + MappingGenerator.instance().getFileName() );
        addImport( "java.util.HashMap" );
        addImport( "java.util.Map" );
        insertClassDeclaration( ClassType.CLASS, getFileName(), "AbstractDepoDaoExecuteTest", null );
        insertTabs().append( "@Autowired" );
        insertLine();
        insertTabs().append( MappingGenerator.instance().getFileName() ).append( " " )
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
        return MappingGenerator.instance().getFileName() + "Test";
    }
}
