package main.java.com.example.main_menu.helpers;

public class CurveFitJNI
{
    static {
        System.loadLibrary("native");
    }
    public double[] vectorProject(double[] positionData, int degree)
    {
        return new CurveFitJNI().vectorProjection(positionData, degree);
    }
    private native double[] vectorProjection(double[] positionData, int degree);
}
