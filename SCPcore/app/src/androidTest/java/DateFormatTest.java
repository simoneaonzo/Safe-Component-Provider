
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;


import java.lang.Exception;
import java.lang.Override;

public class DateFormatTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }



    @SmallTest
    public void testMultiply() {

        assertEquals("10 x 5 must be 50", 50, 10*5);
    }
}