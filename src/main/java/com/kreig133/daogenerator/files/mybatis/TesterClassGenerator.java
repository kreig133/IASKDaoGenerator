package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.common.Utils;
import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.enums.Scope;
import com.kreig133.daogenerator.files.JavaClassGenerator;
import com.kreig133.daogenerator.files.mybatis.mapping.MappingGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author eshangareev
 * @version 1.0
 */
public class TesterClassGenerator extends JavaClassGenerator{

    @Override
    public File getFile() throws IOException {
        final File file = new File(
                Settings.settings().getPathForGeneratedTests() + "/" + getFileName() + JAVA_EXTENSION
        );
        createDirsAndFile( file.getParentFile() );
        return file;
    }

    @Override
    public void generateHead() throws IOException {
        insertPackageLine( Settings.settings().getMapperPackage() );
        insertLine();
        insertImport( "org.junit.Test" );
        insertImport( "org.junit.runner.RunWith" );
        insertImport( "org.springframework.beans.factory.annotation.Autowired" );
        insertImport( "org.springframework.test.context.ContextConfiguration" );
        insertImport( "org.springframework.test.context.junit4.SpringJUnit4ClassRunner" );
        insertLine();
        insertImport( "java.text.SimpleDateFormat" );
        insertLine();
        builder.append( "@RunWith(SpringJUnit4ClassRunner.class)" );
        insertLine();
        builder.append( "@ContextConfiguration(locations = \"").append( MavenProjectGenerator.getConfigName() )
                .append( ".xml\" )" );
        insertLine();
        insertClassDeclaration( ClassType.Class, getFileName(), null, null );
        insertTabs( 1 ).append( "SimpleDateFormat format = new SimpleDateFormat( \"M-d-yyyy H:m:s.SSS\" );" );
        insertLine();
        insertLine();
        insertTabs( 1 ).append( "@Autowired" );
        insertLine();
        insertTabs( 1 ).append( MappingGenerator.instance().getFileName() ).append( " " )
                .append( convertNameForNonClassNaming( MappingGenerator.instance().getFileName() ) ).append( ";" );
        insertLine();
    }

    @Override
    public void generateBody( DaoMethod daoMethod ) throws IOException {
        insertLine();
        insertTabs( 1 ).append( "@Test" );
        insertLine();
        generateMethodSignature(
                Scope.PUBLIC,
                null,
                daoMethod.getCommon().getMethodName() + "RunTest",
                null,
                Arrays.asList( "ParseException" ),
                false
        );


        insertTabs( 1 ).append( "}" );
        insertLine();
    }

    @Override
    public String getFileName() {
        return MappingGenerator.instance().getFileName() + "Tester";
    }
}
