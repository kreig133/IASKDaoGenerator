package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.*;
import com.kreig133.daogenerator.settings.Settings;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

/**
 * @author kreig133
 * @version 1.0
 */
public class ControllerTest {

    @BeforeClass
    public static void before(){
        Settings.settings().setSourcePath( "target\\test-classes\\xml" );
    }

    @Test
    public void getXmfFileNamesInDirectoryTest(){
        final String[] xmlFileNamesInDirectory = DaoGenerator.getXmlFileNamesInDirectory(
                Settings.settings().getSourcePath() );
        for ( String s : xmlFileNamesInDirectory ) {
            System.out.println( "s = " + s );
        }
        Assert.assertTrue( xmlFileNamesInDirectory.length > 0 );
    }

    @Test
    public void testUnmarshallFile() {
        InputStream resourceAsStream =
                ControllerTest.class.getClassLoader().getResourceAsStream( "xml/Example.xsd.xml" );
        Assert.assertTrue( resourceAsStream != null  );
        final DaoMethod daoMethod = JaxbHandler.unmarshallStream( resourceAsStream );
        Assert.assertTrue( daoMethod != null );
    }

}
