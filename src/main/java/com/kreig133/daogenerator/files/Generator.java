package com.kreig133.daogenerator.files;

import com.kreig133.daogenerator.common.Utils;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Generator {
    protected StringBuilder builder;
    protected Integer nestingLevel;

    public void setNestingLevel( Integer nestingLevel ) {
        this.nestingLevel = nestingLevel;
    }

    protected void increaseNestingLevel() {
        ++ nestingLevel;
    }

    protected void decreaseNestingLevel() {
        -- nestingLevel;
    }

    public void setBuilder( StringBuilder builder ) {
        this.builder = builder;
    }

    protected StringBuilder insertLine() {
        return builder.append( "\n" );
    }

    protected StringBuilder insertTabs() {
        Utils.insertTabs( builder, nestingLevel );
        return builder;
    }
}
