import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author eshangareev
 * @version 1.0
 */
public class Test {
    SimpleDateFormat format = new SimpleDateFormat( "M-d-yyyy H:m:s.SSS" );


    @org.junit.Test
    public void test() throws ParseException {
        System.out.println( format.parse( "3-22-1990 23:59:59.000" ));
    }
}
