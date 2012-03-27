package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Utils;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Generator {
    protected StringBuilder builder;

    public void setBuilder( StringBuilder builder ) {
        this.builder = builder;
    }

    protected StringBuilder insertLine() {
        return builder.append( "\n" );
    }

    protected StringBuilder insertTabs( int tabsCount ) {
        Utils.insertTabs( builder, tabsCount );
        return builder;
    }
}
