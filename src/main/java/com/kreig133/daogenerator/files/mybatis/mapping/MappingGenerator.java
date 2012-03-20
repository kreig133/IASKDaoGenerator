package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.DaoJavaClassGenerator;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class MappingGenerator extends DaoJavaClassGenerator{

    private static MappingGenerator INSTANCE;

    public static MappingGenerator instance (){
        if ( INSTANCE == null ) {
            INSTANCE = Settings.settings().getType() == Type.IASK ?
                    new IaskMappingGenerator() :  new DepoMappingGenerator();
        }

        return INSTANCE;
    }

    @Override
    public File getFile() throws IOException {

        File file = new File(
                Settings.settings().getPathForGeneratedSource() + "/" +
                        replacePointBySlash( Settings.settings().getMapperPackage() ) + "/"
                        + Settings.settings().getOperationName() + getFileNameEnding()

                 );

        createDirsAndFile( file.getParentFile() );

        return file;
    }

    abstract protected String getFileNameEnding();


}
