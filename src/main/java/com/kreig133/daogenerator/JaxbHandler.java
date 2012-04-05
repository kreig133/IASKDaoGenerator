package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.DaoMethod;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author kreig133
 * @version 1.0
 */
public class JaxbHandler {
    private static JAXBContext jc;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;

    @NotNull
    public static DaoMethod unmarshallFile(
            File fileWithData
    ) {
        try {
            return ( DaoMethod ) getUnmarshaller().unmarshal( fileWithData );
        } catch ( Throwable e ) {
            throw new RuntimeException( e );
        }
    }

    @NotNull
    public static DaoMethod unmarshallStream(
            InputStream stream
    ) {
        try {
            return ( DaoMethod ) getUnmarshaller().unmarshal( stream );
        } catch ( JAXBException e ) {
            throw new RuntimeException( e );
        }
    }

    public static void marshallInFile( @NotNull File file, @NotNull DaoMethod daoMethod ){
        //TODO костыль
        daoMethod.getCommon().setQuery(
                daoMethod.getCommon().getQuery().replaceAll( "[\\n\\r][\\n\\r]", "\n" )
        );
        try {
            if ( !file.exists() ) {
                file.createNewFile();
            }
            getMarshaller().marshal( daoMethod, file );
        } catch ( JAXBException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    protected static JAXBContext getJaxbContext() {
        if( jc == null ) {
            try {
                jc = JAXBContext.newInstance( "com.kreig133.daogenerator.jaxb" );
            } catch ( JAXBException e ) {
                throw new RuntimeException( "Ошибка при получении JAXBContext'а", e );
            }
        }
        return jc;
    }

    protected static Unmarshaller getUnmarshaller(){
        if( unmarshaller == null ){
            try {
                unmarshaller = getJaxbContext().createUnmarshaller();
            } catch ( JAXBException e ) {
                throw new RuntimeException( e );
            }
        }

        return unmarshaller;
    }

    protected static Marshaller getMarshaller(){
        if ( marshaller == null ) {
            try {
                marshaller = getJaxbContext().createMarshaller();
                marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
                marshaller.setProperty( Marshaller.JAXB_SCHEMA_LOCATION,
                        "http://77.72.129.146/xsd/dao-generator.xsd" );
                marshaller.setProperty( Marshaller.JAXB_ENCODING, "UTF-8" );
            } catch ( JAXBException e ) {
                throw new RuntimeException( e );
            }
        }
        return marshaller;
    }
}
