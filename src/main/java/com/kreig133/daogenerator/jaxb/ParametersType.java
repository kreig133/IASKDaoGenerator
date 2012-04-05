//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.03.13 at 10:35:51 AM YEKT
//


package com.kreig133.daogenerator.jaxb;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;

import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Java class for parametersType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parametersType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="parent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="javaClassName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parameter" type="{com.aplana.dao-generator}parameterType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parametersType", propOrder = {
    "parent",
    "javaClassName",
    "parameter"
})
public class ParametersType {

    @XmlElement( defaultValue = "java.lang.Object" )
    protected String parent;
    @XmlElement( defaultValue = "Default" )
    protected String javaClassName;
    @XmlElement( required = true )
    protected List<ParameterType> parameter;

    /**
     * Gets the value of the parent property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParent() {
        return parent;
    }

    /**
     * Sets the value of the parent property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParent(String value) {
        this.parent = value;
    }

    /**
     * Gets the value of the javaClassName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getJavaClassName() {
        return javaClassName;
    }

    /**
     * Sets the value of the javaClassName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setJavaClassName(String value) {
        this.javaClassName = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterType }
     * @return
     */
    public List<ParameterType> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<ParameterType>();
        }
        return this.parameter;
    }

    public ParameterType getParameterByName( String name ) {
        return getParameterByName( name, getParameter() );
    }
    
    public static ParameterType getParameterByName( final String name, List<ParameterType> parameters ){
        return Iterators.tryFind( parameters.iterator(), new Predicate<ParameterType>() {
            @Override
            public boolean apply( @Nullable ParameterType input ) {
                return checkNotNull( input, "Неожиданно NPE в ParametersType" ).getName().equalsIgnoreCase( name );
            }
        } ).orNull();
    }
    
    public List<Integer> getIndexOfUnnamedParameters() {
        final List<Integer> result = new LinkedList<Integer>();

        for ( ParameterType parameterType : getParameter() ) {
            if ( "".equals( parameterType.getName().trim() ) ) {
                result.add( getParameter().indexOf( parameterType ) );
            }
        }
        return result;
    }

    public boolean containsDates(){
        for ( ParameterType parameterType : getParameter() ) {
            if ( parameterType.getType() == JavaType.DATE ) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isWithPaging() {
        return isWithPaging( getParameter() );
    }

    public boolean containsSameNames() {
        return containsSameNames( getParameter() );
    }

    public static boolean containsSameNames( List<ParameterType> parameterTypes ) {
        Set<String> names = new HashSet<String>();
        for ( ParameterType parameterType : parameterTypes ) {
            if ( names.contains( parameterType.getName() ) ) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWithPaging( List<ParameterType> parameterTypes ) {
        Map<Enum, Boolean> map = new HashMap<Enum, Boolean>();
        for ( WithPagingType withPagingType : WithPagingType.values() ) {
            map.put( withPagingType, Boolean.FALSE );
        }
        for ( ParameterType parameterType : parameterTypes ) {
            if( WithPagingType.inEnum( parameterType.getName() ) ) {
                map.put( WithPagingType.getBySqlName( parameterType.getName() ), Boolean.TRUE );
            }
        }
        for ( Enum anEnum : map.keySet() ) {
            if ( ! map.get( anEnum ) ) {
                return false;
            }
        }
        return true;
    }
        
    public enum WithPagingType{
         ID_SESSION_DS("idSession"),
         I_START("startRowNumber"),
         I_PAGE_LIMIT("pageLimit"),
         I_END("endRowNumber"),
         S_SORT("sort"),
         I_ROW_COUNT("rowCount");

        final String fieldName;

        private WithPagingType( String fieldName ) {
            this.fieldName = fieldName;
        }

        public String fieldName() {
            return fieldName;
        }

        public static boolean inEnum( String name ){
            return getBySqlName( name ) != null;
        }

        public static WithPagingType getBySqlName( String name ) {
            try {
                return valueOf( NamingUtils.convertNameForEnum( name ) );
            } catch ( Exception e ) {
                return null;
            }
        }
    }
}
