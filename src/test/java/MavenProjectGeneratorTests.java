import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.settings.Settings;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author eshangareev
 * @version 1.0
 */
public class MavenProjectGeneratorTests extends MavenProjectGenerator{

    @Test
    public void streamToStringTest(){
        String s = MavenProjectGenerator.streamToString(
                MavenProjectGeneratorTests.class.getClassLoader().getResourceAsStream( "xml/Example.xsd.xml" )
        );
        
        Assert.assertTrue( s.startsWith( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ) );
        Assert.assertTrue( s.endsWith( "</com:daoMethod>" ) );
    }
    
    @Test
    public void fillContextTemplateByDataTest() {
        Settings.settings().setMapperPackage( "com.kreig133" );
        Settings.settings().setSourcePath( "C:/Operation" );
        String s = MavenProjectGenerator.fillContextTemplateByData(
                "    <bean id=\"${beanName}\" class=\"${beanClass}\">" );
        Assert.assertEquals( "    <bean id=\"operationDao\" class=\"com.kreig133.OperationDao\">", s );
    }

}
