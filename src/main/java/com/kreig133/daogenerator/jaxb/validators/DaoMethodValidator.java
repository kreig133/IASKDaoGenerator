package com.kreig133.daogenerator.jaxb.validators;

import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.jaxb.DaoMethod;
import com.kreig133.daogenerator.jaxb.ParameterType;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eshangareev
 * @version 1.0
 */
public class DaoMethodValidator {
    public static boolean checkDaoMethods( List<DaoMethod> daoMethods ) {
        boolean allIsOk = true;
        for ( DaoMethod daoMethod : daoMethods ) {
            allIsOk = checkRenameTos     ( daoMethod ) && allIsOk;
        }
        return  allIsOk;
    }

    static boolean checkRenameTos( DaoMethod daoMethod ) {
        List<ParameterType> parameter = new ArrayList<ParameterType>( daoMethod.getInputParametrs().getParameter() );
        parameter.addAll( daoMethod.getOutputParametrs().getParameter() );

        boolean containsEmptyRenameTo = false;

        for ( ParameterType parameterType : parameter ) {
            if( StringUtils.isBlank( parameterType.getRenameTo() ) ) {
                containsEmptyRenameTo = true;
                System.out.println( String.format( "ERROR! В методе %s в RenameTo есть пустые значения!",
                        getMethodName( daoMethod ) )
                );
            }
        }

        boolean containsSameRenameToValues =
                daoMethod.getInputParametrs ().containsSameRenameTo() ||
                        daoMethod.getOutputParametrs().containsSameRenameTo();
        return ! ( containsSameRenameToValues ||  containsEmptyRenameTo );
    }

    private static String getMethodName( DaoMethod daoMethod ) {
        return StringUtils.isBlank( daoMethod.getCommon().getMethodName() ) ?
                "<Без названия>" : daoMethod.getCommon().getMethodName();
    }
}
