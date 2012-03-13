package com.kreig133.daogenerator;

import com.kreig133.daogenerator.jaxb.DaoMethod;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;

/**
 * @author kreig133
 * @version 1.0
 */
public class JaxbHandler {
    private static JAXBContext jc;
    private static Unmarshaller unmarshaller;
    private static Marshaller marshaller;

    public static DaoMethod unmarshallFile(
            File fileWithData
    ) {
        try {
            return ( DaoMethod ) getUnmarshaller().unmarshal( fileWithData );
        } catch ( Throwable e ) {
            throw new RuntimeException( e );
        }
    }

    public static void marshallInFile( File file, DaoMethod daoMethod ){

        try {
            if ( !file.exists() ) {
                file.createNewFile();
            }
            getMarshaller().marshal( daoMethod, file );
        } catch ( JAXBException e ) {
            e.printStackTrace(); //TODO
        } catch ( IOException e ) {
            e.printStackTrace(); //TODO
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
            } catch ( JAXBException e ) {
                throw new RuntimeException( e );
            }
        }
        return marshaller;
    }
}
