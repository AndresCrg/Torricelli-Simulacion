package models;

public class MyTimeStamp {
    private double millis;

    public MyTimeStamp() {
        millis = System.currentTimeMillis();
    }

    public double stamp() {
        long startTime = System.nanoTime();
        long stamp = System.nanoTime();
        double elapsedTime = (stamp - startTime)/(double)1000000;
        millis = stamp;
        return elapsedTime;
    }
}
