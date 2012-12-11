package com.kreig133.daogenerator;

import com.kreig133.daogenerator.files.builder.FileBuilder;

import java.io.IOException;

/**
 * @author eshangareev
 * @version 1.0
 */
public class WikiGenerator {

    private static final String XML_TO_WIKI = "XmlToWiki";
    private static final String XML_TO_WIKI_FOR_DIRECTORY = "XmlToWikiForDirectory";

    public static void generateWiki( String path ) throws IOException, InterruptedException {

        final String[] xmlFileNamesInDirectory = FileBuilder.getXmlFileNamesInDirectory( path );

        for ( String s : xmlFileNamesInDirectory ) {
            generateWikiForXmlFile( path + "\\" +  s );
        }
    }

    public static void generateWikiForXmlFile(
            String xmlFileName
    ) throws IOException, InterruptedException {

        final String[] cmdarray = { "cmd", "/C",
                "java  -classpath DaoGenerator-" + DaoGenerator.VERSION + ".jar org.apache.xalan.xslt.Process " +
                        "-IN " + xmlFileName +
                        " -XSL " + XML_TO_WIKI + ".xsl "+
                        "-OUT " + xmlFileName + ".txt" };

        for ( String s1 : cmdarray ) {
            System.out.println( s1 );
        }
        Communicator.communicate( cmdarray ).waitFor();

        System.out.println( " END " );
    }
}
