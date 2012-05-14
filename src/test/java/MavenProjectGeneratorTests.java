import com.kreig133.daogenerator.MavenProjectGenerator;
import com.kreig133.daogenerator.common.Utils;
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
        String s = Utils.streamToString(
                MavenProjectGeneratorTests.class.getClassLoader().getResourceAsStream( "xml/Example.xsd.xml" )
        );

        assert s != null;
        Assert.assertTrue( s.startsWith( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ) );
        Assert.assertTrue( s.endsWith( "</com:daoMethod>" ) );
    }


}
