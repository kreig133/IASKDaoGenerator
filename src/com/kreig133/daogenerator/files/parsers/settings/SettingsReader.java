package com.kreig133.daogenerator.files.parsers.settings;

import com.kreig133.daogenerator.common.settings.OperationSettings;
import com.kreig133.daogenerator.enums.InputOrOutputType;
import com.kreig133.daogenerator.enums.Type;
import com.sun.org.apache.bcel.internal.generic.RETURN;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SettingsReader {
    public static final String NAME     = "name";
    public static final String TYPE     = "type";
    public static final String DEFAULT  = "default";
    public static final String COMMENT  = "comment";
    public static final String IN_OUT   = "inout";

    public static final String[] KEYS = new String[]{ NAME, TYPE, DEFAULT, COMMENT, IN_OUT };

    public static final String RETURN   = "return";
    public static final String SELECT   = "select";
    public static final String TEST     = "test";

    private static final Properties properties = new Properties();
    private static final String pathToProperties ="./src/com/kreig133/daogenerator/files/parsers/settings/parse.properties";

    private static Map<String, Integer> settings = new HashMap<String, Integer>( 10 );

    public static void readProperties( OperationSettings operationSettings ) throws IOException {
        final File fileWithSettings = operationSettings.getFileWithSettings();

        final FileInputStream fileInputStream = new FileInputStream(
                fileWithSettings == null ? new File( pathToProperties ) : fileWithSettings
        );

        properties.load( fileInputStream );

        for ( String key : KEYS ){
            String   param  = properties.getProperty( key );
            String[] params = param.split( "," );

            settings.put( key + InputOrOutputType.IN, convertToInteger( params[0] ) );

            if( params.length == 2 ){
                settings.put( key + InputOrOutputType.OUT, convertToInteger( params[1] ) );
            } else {
                settings.put( key + InputOrOutputType.OUT, convertToInteger( params[0] ) );
            }
        }

        settings.put( RETURN, convertToInteger( properties.getProperty( RETURN ) ) );
        settings.put( SELECT, convertToInteger( properties.getProperty( SELECT ) ) );
        settings.put( TEST  , convertToInteger( properties.getProperty( TEST   ) ) );

        operationSettings.setParameterPlaces( settings );
        fileInputStream.close();
    }

    private static Integer convertToInteger( String param ){
        if( "".equals( param.trim() ) ){
            return null;
        }
        return new Integer( param.trim() );
    }

    public static void main( String[] args ) throws IOException {
        OperationSettings settings = new OperationSettings() {
            @Override
            public Type getType() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setType( Type type ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOperationName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOperationName( String operationName ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getOutputPath() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOutputPath( String outputPath ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getEntityPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setEntityPackage( String entityPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getMapperPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setMapperPackage( String mapperPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getDaoPackage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setDaoPackage( String daoPackage ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getSourcePath() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setSourcePath( String path ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean skipTesting() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setSkipTesting( boolean skipTesting ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public File getFileWithSettings() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setFileWithSettings( File fileWithSettings ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Integer getPlaceOfParameter( String parameterKey ) {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setParameterPlaces( Map<String, Integer> settings ) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        readProperties( settings );
    }

}
