package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class InModelClassGenerator extends ModelClassGenerator {
    InModelClassGenerator( ParametersType parametersType ) {
        super( parametersType );
    }

    public static ModelClassGenerator newInstance( DaoMethod daoMethod ){
        return new InModelClassGenerator( daoMethod.getInputParametrs() );
    }

    @Override
    protected void insertClassDeclarationAndDetermineParent() {
        if ( parametersType.isWithPaging() ) {
            addImport( "com.aplana.sbrf.deposit.web.common.client.operation.data.DepoPagingModelData" );
            insertClassDeclaration(
                    ClassType.CLASS,
                    PackageAndFileUtils.getShortName( parametersType.getJavaClassName() ),
                    "DepoPagingModelData",
                    null
            );
        } else {
            super.insertClassDeclarationAndDetermineParent();
        }
    }

    @Override
    protected List<ParameterType> filter( List<ParameterType> parameter ) {
        if ( parametersType.isWithPaging() ) {
            List<ParameterType> result = new ArrayList<ParameterType>();
            for ( ParameterType parameterType : parameter ) {
                if ( ParametersType.WithPagingType.inEnum( parameterType.getName() ) ) {
                    parameterType.setRenameTo(
                            ParametersType.WithPagingType.getBySqlName( parameterType.getName() ).fieldName()
                    );
                } else {
                    result.add( parameterType );
                }
            }
            return result;
        } else {
            return parameter;
        }
    }
}
