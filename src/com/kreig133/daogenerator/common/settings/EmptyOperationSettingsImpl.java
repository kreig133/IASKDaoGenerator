package com.kreig133.daogenerator.common.settings;

import com.kreig133.daogenerator.enums.Type;

import java.io.File;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class EmptyOperationSettingsImpl implements OperationSettings{
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setParameterPlaces( Map<String, Integer> settings ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
