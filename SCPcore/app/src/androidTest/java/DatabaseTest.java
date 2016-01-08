
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;


import com.uni.ailab.scp.receiver.SQLiteHelper;

import java.lang.Exception;
import java.lang.Override;

public class DatabaseTest extends AndroidTestCase {

    private SQLiteHelper db;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        db = new SQLiteHelper(context);
    }

    @Override
    protected void tearDown() throws Exception {
        db.close();
        super.tearDown();
    }


    @SmallTest
    public void testMultiply() {

    }
}