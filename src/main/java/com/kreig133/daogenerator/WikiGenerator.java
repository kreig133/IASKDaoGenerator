package com.kreig133.daogenerator;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WikiGenerator {
    public static void generateWiki( String path ) throws IOException, InterruptedException {

        final String[] xmlFileNamesInDirectory = DaoGenerator.getXmlFileNamesInDirectory( path );

        for ( String s : xmlFileNamesInDirectory ) {
            generateWikiForXmlFile( path + "\\" +  s );
        }
    }

    public static void generateWikiForXmlFile( String xmlFileName ) throws IOException, InterruptedException {
        final String[] cmdarray = { "cmd", "/C",
                "java  -classpath DaoGenerator-2.6.jar org.apache.xalan.xslt.Process " +
                        "-IN " + xmlFileName +
                        " -XSL XmlToWiki.xsl " +
                        "-OUT " + xmlFileName + ".txt" };

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
        }
        Process exec = Runtime.getRuntime().exec( cmdarray );
        Communicator.communicate( exec,
                new OutputStreamWriter( System.out, "Cp866" ),
                new OutputStreamWriter( System.err, "Cp866" )
        );
        exec.waitFor();

        System.out.println( " END " );
    }
}
