package com.kreig133.daogenerator.common;

import com.kreig133.daogenerator.enums.ReturnType;
import com.kreig133.daogenerator.enums.SelectType;
import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.parameter.Parameter;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class SettingsImpl implements Settings{

    private final List<Parameter> INPUT_PARAMETER_LIST  = new ArrayList<Parameter>();
    private final List<Parameter> OUTPUT_PARAMETER_LIST = new ArrayList<Parameter>();

    private StringBuilder QUERY = new StringBuilder();
    
    private String FUNCTION_NAME;
    private String OPERATION_NAME;
    private String ENTITY_PACKAGE;
    private String DAO_PACKAGE;
    private String MAPPER_PACKAGE;
    private String OUTPUT_PATH;
    private String SOURCE_PATH;

    private Type       TYPE         ; 
    private SelectType SELECT_TYPE  ;
    private ReturnType RETURN_TYPE  ;

    //= "com.aplana.sbrf.deposit.persistence.custom.entity.accounts.operation" +
    //".administrative.closecount";


    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public SelectType getSelectType() {
        return SELECT_TYPE;
    }

    @Override
    public List<Parameter> getInputParameterList() {
        return INPUT_PARAMETER_LIST;
    }
    @Override
    public List<Parameter> getOutputParameterList() {
        return OUTPUT_PARAMETER_LIST;
    }
    @Override
    public StringBuilder getSelectQuery() {
        return QUERY;
    }
    @Override
    public String getFunctionName() {
        return FUNCTION_NAME;
    }

    @Override
    public ReturnType getReturnType() {
        return RETURN_TYPE;
    }


    @Override
    public String getOutputPath() {
        return OUTPUT_PATH;
    }

    @Override
    public String getOperationName() {
        return OPERATION_NAME;
    }

    @Override
    public String getEntityPackage() {
        return ENTITY_PACKAGE;
    }

    @Override
    public String getMapperPackage() {
        return MAPPER_PACKAGE;
    }

    @Override
    public String getDaoPackage() {
        return DAO_PACKAGE;
    }

    @Override
    public void setType( Type type ) {
        if( TYPE == null ){
            this.TYPE = type;
        } else {
            throw new RuntimeException( "TYPE уже был установлен!" );
        }
    }

    @Override
    public void setSelectType( SelectType selectType ) {
        this.SELECT_TYPE = selectType;
    }

    @Override
    public void setReturnType( ReturnType returnType ) {
        RETURN_TYPE = returnType;
    }

    @Override
    public void setFunctionName( String functionName ) {
        FUNCTION_NAME = functionName;
    }

    @Override
    public void setOperationName( String operationName ) {
        if( OPERATION_NAME == null ){
            OPERATION_NAME = operationName;
        } else {
            throw new RuntimeException( "OPERATION_NAME уже установлен!" );
        }
    }

    @Override
    public void setOutputPath( String outputPath ) {
        if( OUTPUT_PATH == null ){
            OUTPUT_PATH = outputPath;
        } else {
            throw new RuntimeException( "OUTPUT_PATH уже установлен!" );
        }
    }

    @Override
    public void setEntityPackage( String entityPackage ) {
        if( ENTITY_PACKAGE == null ){
            ENTITY_PACKAGE = entityPackage;
        } else {
            throw new RuntimeException( "ENTITY_PACKAGE уже установлен!" );
        }
    }

    @Override
    public void setMapperPackage( String mapperPackage ) {
        if( MAPPER_PACKAGE == null ){
            MAPPER_PACKAGE = mapperPackage;
        } else {
            throw new RuntimeException( "MAPPER_PACKAGE уже установлен!" );
        }
    }

    @Override
    public void setDaoPackage( String daoPackage ) {
        if( DAO_PACKAGE == null ){
            DAO_PACKAGE = daoPackage;
        } else {
            throw new RuntimeException( "DAO_PACKAGE уже установлен!" );
        }
    }

    @Override
    public String getSourcePath() {
        if( SOURCE_PATH == null ){
            throw new RuntimeException( "SOURCE_PATH еще не установлен." );
        }
        return SOURCE_PATH;
    }

    @Override
    public void setSourcePath( String path ) {
        if( SOURCE_PATH == null ){
            SOURCE_PATH = path;
        } else {
            throw new RuntimeException( "SOURCE_PATH уже установле!" );
        }
    }

    @Override
    public void clearSelectQuery() {
        QUERY = new StringBuilder();
    }
}
