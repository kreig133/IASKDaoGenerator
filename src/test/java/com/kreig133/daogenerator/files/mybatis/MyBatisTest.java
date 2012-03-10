package com.kreig133.daogenerator.files.mybatis;

import com.kreig133.daogenerator.DaoGenerator;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.Appender;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author kreig133
 * @version 1.0
 */
public class MyBatisTest {
    @BeforeClass
    public static void before(){
        DaoGenerator.getCurrentOperationSettings().setType( Type.IASK );
    }

    @Test
    public void prepareFile() throws IOException {
        MyBatis.prepareFiles( new Appender() {
            @Override
            public void appendStringToFile( File file, String string ) {
                System.out.println(string);

                Assert.assertTrue(
                        string.startsWith( "package" )
                        ||
                        string.startsWith( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" )
                );
            }
        } );
    }
}
