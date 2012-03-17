package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.*;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

/**
 * @author kreig133
 * @version 1.0
 */
public class ControllerTest {

    @BeforeClass
    public static void before(){
        DaoGenerator.getCurrentOperationSettings().setSourcePath( "xml" );
    }

    @Test
    public void getXmfFileNamesInDirectoryTest(){
        final String[] xmlFileNamesInDirectory = DaoGenerator.getXmlFileNamesInDirectory();
        for ( String s : xmlFileNamesInDirectory ) {
            System.out.println( "s = " + s );
        }
        Assert.assertTrue( xmlFileNamesInDirectory.length > 0 );
    }

    @Test
    public void testUnmarshallFile() {
        File xml = new File( "xml/Example.xsd.xml" );
        Assert.assertTrue( xml.isFile() && xml.exists() );
        final DaoMethod daoMethod = JaxbHandler.unmarshallFile( xml );
        Assert.assertTrue( daoMethod != null );
    }

}
