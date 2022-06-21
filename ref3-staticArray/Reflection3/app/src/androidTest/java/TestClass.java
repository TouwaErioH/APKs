
import android.util.Base64;
import org.junit.Test;
import android.util.Log;


public class TestClass {

    @Test
    public void test1(){
        Log.v("Base64 test",Base64.encodeToString("getImei".getBytes(), Base64.DEFAULT));
        String enc=Base64.encodeToString("getImei".getBytes(), Base64.DEFAULT);
        String dec=new String(Base64.decode(enc,Base64.DEFAULT));
        Log.v("Base64 test",dec);
    }
}


