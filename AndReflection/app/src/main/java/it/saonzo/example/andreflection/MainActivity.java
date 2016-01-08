package it.saonzo.example.andreflection;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    public static final String LOGTAG = "--andreflection--";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        try {
            Class exampleClass = Class.forName("it.saonzo.example.andreflection.ExampleClass");

            Constructor[] aClassConstructors = exampleClass.getDeclaredConstructors();
            for(Constructor c : aClassConstructors){
                Log.v(LOGTAG, c.toString());
            }

            Method[] methods = exampleClass.getMethods();
            for (Method m : methods) {
                Log.v(LOGTAG, m.toString());
            }

            Field[] fields = exampleClass.getFields();
            for (Field f : fields) {
                Log.v(LOGTAG, f.toString());
            }


            // we need an instance of ExampleClass for calling a virtual method
            ExampleClass exampleInstance = new ExampleClass(0.1);

            playWithPrivate(exampleClass, exampleInstance);

            Log.v("***", "");

            //callMethodFromExteralDex();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void playWithPrivate(Class exampleClass, ExampleClass exampleInstance) {
        try {
            Method privMeth = exampleClass.getDeclaredMethod("it.saonzo.example.andreflection.ExampleClass.privMeth");
            privMeth.setAccessible(true);
            privMeth.invoke(exampleInstance, 8);
            int i = exampleInstance.pubMeth();
            assert (i == 42 + 8);

            Field field = exampleClass.getDeclaredField("privateField");
            field.setAccessible(true);
            field.set(exampleInstance, 33);
            int j = (int) field.get(exampleInstance);
            assert (j == 33);

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void callMethodFromExteralDex() {
        File sdcard = Environment.getExternalStorageDirectory();
        File externalApk = new File(sdcard,"example.apk");

        if (externalApk.exists()) {
            ClassLoader classLoader = new DexClassLoader(
                    externalApk.toString(),
                    sdcard.toString(),
                    null,
                    ClassLoader.getSystemClassLoader());
            try {
                Class externalClass = classLoader.loadClass("ExternalClass");
                Log.v("***", "");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
