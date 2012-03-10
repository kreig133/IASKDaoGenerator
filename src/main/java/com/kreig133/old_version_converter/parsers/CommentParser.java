package com.kreig133.old_version_converter.parsers;

import com.kreig133.daogenerator.jaxb.DaoMethod;

/**
 * @author eshangareev
 * @version 1.0
 */
public class CommentParser implements IParser{
    @Override
    public void parse(
            DaoMethod         daoMethod,
            String            lineForParse
    ) {
        daoMethod.getCommon().setComment(
                daoMethod.getCommon().getComment() + lineForParse + "\n"
        );
    }
}
