package com.kreig133.daogenerator.files;

import java.io.File;

/**
 * @author kreig133
 * @version 1.0
 */
public interface Appender  {
    
    void appendStringToFile( File file, String string );
}
