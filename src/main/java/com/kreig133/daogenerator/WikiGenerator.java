package com.kreig133.daogenerator;

import com.kreig133.daogenerator.files.builder.FileBuilder;

import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WikiGenerator {
    public static void generateWiki( String path ) throws IOException, InterruptedException {

        final String[] xmlFileNamesInDirectory = FileBuilder.getXmlFileNamesInDirectory( path );

        for ( String s : xmlFileNamesInDirectory ) {
            generateWikiForXmlFile( path + "\\" +  s, 1 );
        }
    }

    public static void generateWikiForXmlFile( String xmlFileName, int mode ) throws IOException, InterruptedException {
        String xsltFileName;
         if (mode == 1)      {
             xsltFileName="XmlToWiki";
         } else {
             xsltFileName="XmlToWikiForDirectory";
         }

        final String[] cmdarray = { "cmd", "/C",
                "java  -classpath DaoGenerator-" + DaoGenerator.VERSION + ".jar org.apache.xalan.xslt.Process " +
                        "-IN " + xmlFileName +
                        " -XSL " + xsltFileName + ".xsl "+
                        "-OUT " + xmlFileName + ".txt" };

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
        }
        Communicator.communicate( cmdarray ).waitFor();

        System.out.println( " END " );
    }
}
