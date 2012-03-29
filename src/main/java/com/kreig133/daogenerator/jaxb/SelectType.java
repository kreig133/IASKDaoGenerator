//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.10 at 06:03:21 PM GMT+05:00 
//


package com.kreig133.daogenerator.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for selectType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="selectType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CALL"/>
 *     &lt;enumeration value="SELECT"/>
 *     &lt;enumeration value="GENERATE"/>
 *     &lt;enumeration value="GENEROUT"/>
 *     &lt;enumeration value="INSERT"/>
 *     &lt;enumeration value="DELETE"/>
 *     &lt;enumeration value="UPDATE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "selectType")
@XmlEnum
public enum SelectType {

    CALL  ( "Select" ),
    SELECT( "Select" ),
    INSERT( "Insert" ),
    DELETE( "Delete" ),
    UPDATE( "Update" );

    private final String annotation;


    public String getAnnotation() {
        return annotation;
    }


    SelectType( String annotation ) {
        this.annotation = annotation;
    }

    public static SelectType getByName( String name ){
        name = name.trim().toLowerCase();
        for( SelectType selectType: SelectType.values() ){
            if( selectType.toString().toLowerCase().equals( name ) ){
                return selectType;
            }
        }

        return name.startsWith( "exec" )  ? CALL : null;
    }

    public boolean isQuery(  ){
        return
                this == SELECT ||
                this == INSERT ||
                this == DELETE ||
                this == UPDATE;
    }

}
