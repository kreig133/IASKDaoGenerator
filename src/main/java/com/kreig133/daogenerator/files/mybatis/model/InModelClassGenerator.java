package com.kreig133.daogenerator.files.mybatis.model;

import com.kreig133.daogenerator.enums.ClassType;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import com.kreig133.daogenerator.jaxb.ParametersType;
import com.kreig133.daogenerator.jaxb.ParentType;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    public static ModelClassGenerator newInstance( @NotNull DaoMethod daoMethod ){
        return new InModelClassGenerator( daoMethod.getInputParametrs() );
    }

    @Override
    protected void insertClassDeclarationAndDetermineParent() {
        if ( parametersType.isWithPaging() ) {
            insertClassDeclarationAndParent( ParentType.WITH_PAGING );
        } else {
            super.insertClassDeclarationAndDetermineParent();
        }
    }

    @NotNull
    @Override
    protected List<ParameterType> filter( @NotNull List<ParameterType> parameter ) {
        if ( parametersType.isWithPaging() ) {
            List<ParameterType> result = new ArrayList<ParameterType>();
            for ( ParameterType parameterType : parameter ) {
                if (
                        ParametersType.WithPagingType.inEnum( parameterType.getName() )||
                        parameterType.getName().equals("s_like")
                ) {
                    if( parameterType.getName().equals("s_like")){
                        parameterType.setRenameTo("sLike");
                    } else {
                        parameterType.setRenameTo(
                            ParametersType.WithPagingType.getBySqlName(
                                    parameterType.getName()
                            ).fieldName()
                        );
                    }
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
