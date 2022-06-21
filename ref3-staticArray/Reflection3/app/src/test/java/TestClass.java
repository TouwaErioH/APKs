
import android.util.Base64;
import org.junit.Test;



public class TestClass {

    @Test
    public void test1(){
        System.out.println(Base64.encodeToString("getImei".getBytes(), Base64.DEFAULT));
    }
}


