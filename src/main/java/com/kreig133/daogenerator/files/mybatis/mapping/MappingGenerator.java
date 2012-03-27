package com.kreig133.daogenerator.files.mybatis.mapping;

import com.kreig133.daogenerator.enums.Type;
import com.kreig133.daogenerator.files.mybatis.DaoJavaClassGenerator;
import com.kreig133.daogenerator.files.PackageAndFileUtils;
import com.kreig133.daogenerator.settings.Settings;

import java.io.File;
import java.io.IOException;

/**
 * @author kreig133
 * @version 1.0
 */
public abstract class MappingGenerator extends DaoJavaClassGenerator {

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
                        PackageAndFileUtils.replacePointBySlash( Settings.settings().getMapperPackage() ) + "/"
                        + getFileName() + getFileNameEnding()
                 );

        PackageAndFileUtils.createDirsAndFile( file.getParentFile() );

        return file;
    }

    abstract protected String getFileNameEnding();
}
