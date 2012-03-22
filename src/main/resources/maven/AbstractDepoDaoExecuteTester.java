package com.aplana.sbrf;

import org.apache.ibatis.annotations.Param;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author eshangareev
 * @version 1.0
 */
public class AbstractDepoDaoExecuteTester {

    private SimpleDateFormat format = new SimpleDateFormat( "M-d-yyyy H:m:s.SSS" );

    protected Object invokeInMethodWithOneInputParam(
            Class<?> aClass,
            Map<String, String> values
    ) throws Exception {
        if ( values.size() == 1 ) {
            return getValueByType( aClass, values.values().iterator().next() );
        }

        return fillObjectWithData( aClass.newInstance(), values );
    }

    protected Object fillObjectWithData(
            Object object, Map<String, String> values
    ) throws Exception {

        for ( PropertyDescriptor pd : Introspector.getBeanInfo( object.getClass() ).getPropertyDescriptors() ) {
            if ( pd.getWriteMethod() != null && ! "class".equals( pd.getName() ) ) {

                Class<?> aClass = pd.getWriteMethod().getParameterTypes()[ 0 ];
                String value = values.get( pd.getName() );
                pd.getWriteMethod().invoke( object, getValueByType( aClass, value ) );
            }
        }

        return object;
    }

    protected Object getValueByType(
            Class<?> aClass, String value
    ) throws Exception {
        if ( value == null ) {
            return null;
        } else if ( aClass == java.util.Date.class ) {
            return format.parse( value );
        } else if ( aClass == java.lang.String.class ) {
            return value;
        } else if (
                aClass == java.lang.Long.class || aClass == java.lang.Byte.class || aClass == java.lang.Double.class
                ) {
            if ( value.equals( "" ) ) {
                return null;
            } else {
                Method valueOf = aClass.getMethod( "valueOf", String.class );
                return valueOf.invoke( aClass, value );
            }
        }

        throw new IllegalArgumentException();
    }

    protected Method findMethodByName( String methodName, Class<?> aClass ) {
        final Method[] methods = aClass.getMethods();

        Method method = null;

        for ( Method m : methods ) {
            if ( m.getName().equals( methodName ) ) {
                method = m;
                break;
            }
        }
        return method;
    }

    protected Object invoke( Object dao, Map<String, String> map, String methodName ) {
        try {
            Method method = findMethodByName( methodName, dao.getClass() );
            assert method != null : "Хренова, метод не нашелся :" + methodName;

            final Class<?>[] parameterTypes = method.getParameterTypes();

            Object invoke = null;

            switch ( parameterTypes.length ) {
                case 0:
                    invoke = method.invoke( dao );
                    break;
                case 1:
                    invoke = method.invoke(
                            dao, invokeInMethodWithOneInputParam( parameterTypes[ 0 ], map )
                    );
                    break;
                default:
                    List<String> names = new ArrayList<String>( parameterTypes.length );
                    for (
                            Annotation[] parameterAnnotation :
                            findMethodByName( methodName,
                                    dao.getClass().getInterfaces()[ 0 ] ).getParameterAnnotations()
                            ) {
                        names.add( ( String ) Param.class.getMethod( "value" ).invoke( parameterAnnotation[ 0 ] ) );
                    }

                    assert parameterTypes.length == names.size();

                    List<Object> values = new ArrayList<Object>( parameterTypes.length );
                    for ( int i = 0; i < names.size(); i++ ) {
                        values.add( getValueByType( parameterTypes[ i ], map.get( names.get( i ) ) ) );
                    }

                    invoke = method.invoke( dao, values.toArray() );
            }

            assert invoke != null;

            if ( invoke instanceof List ) {
                for ( Object object : ( List ) invoke ) {
                    System.out.println( object );
                }
            } else {
                System.out.println( invoke );
            }
            return invoke;
        } catch ( Exception ex ) {
            throw new RuntimeException( ex );
        }
    }
}
