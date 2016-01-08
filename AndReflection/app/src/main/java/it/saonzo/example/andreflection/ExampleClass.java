package it.saonzo.example.andreflection;


public class ExampleClass {
    private int privateField = 42;
    public double publicField;

    public ExampleClass(double d) { publicField = d; }

    private void privMeth(int i) {
        privateField +=i;
    }

    public int pubMeth() {
        return privateField;
    }
}
