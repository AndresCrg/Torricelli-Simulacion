package models;

import views.MainFrame;

public class TorricelliEngine implements Runnable {

    private MyTimeStamp myTimeStamp;
    private MyTimeStamp myTimeStamp2;
    private MainFrame mf;
    private static final double GRAVITY_C = 9.8;

    private double tank1_initial_h;
    private double mayor_h1;
    private double minor_h1;
    private boolean started = false;
    private double initial_water_height1;
    private double biggerHole1;
    private double smallerHole1;
    private double tank1_d1;
    private double tank1_d2;

    private double tank2_initial_h;
    private double mayor_h2;
    private double minor_h2;
    private double initial_water_height2;
    private double biggerHole2;
    private double smallerHole2;
    private double tank2_d1;
    private double tank2_d2;

    private double height_difference;
    private double stacked_volume;
    private double delta_wasted_volume;
    private double tank2_drained_volume;
    private double tank1_drained_volume;
    private double total_wasted_volume;
    private boolean overflow;
    private boolean wasting;
    private double seconds;
    private Thread thread;
    private boolean startedThread = false;

    public TorricelliEngine(double mayor_h1, double minor_h1, double tank1_d2, double tank1_d1,
                            double mayor_h2, double minor_h2, double tank2_d2, double tank2_d1, double height_difference, MainFrame mf) {
        this.mf = mf;
        this.height_difference = height_difference;

        this.mayor_h1 = mayor_h1;
        this.minor_h1 = minor_h1;
        this.tank1_initial_h =mayor_h1;
        this.initial_water_height1 = mayor_h1;
        this.tank1_d1 = tank1_d1;
        this.tank1_d2 = tank1_d2;
        this.biggerHole1 = (tank1_d2/(double)2)*(tank1_d2/(double)2)*Math.PI;
        this.smallerHole1 = (tank1_d1/(double)2)*(tank1_d1/(double)2)*Math.PI;

        this.mayor_h2 = mayor_h2;
        this.minor_h2 = minor_h2;
        this.tank2_initial_h =mayor_h2;
        this.initial_water_height2 = mayor_h2;
        this.tank2_d1 = tank2_d1;
        this.tank2_d2 = tank2_d2;
        this.biggerHole2 = (tank2_d2/(double)2)*(tank2_d2/(double)2)*Math.PI;
        this.smallerHole2 = (tank2_d1/(double)2)*(tank2_d1/(double)2)*Math.PI;

        thread = new Thread(this);
        startSimulator();
    }

    private double getDeltaVolume1(double delta_time) {
        stacked_volume += getStream1XSpeed()*smallerHole1*delta_time;
         return stacked_volume;
    }

    private void buildDeltaVolume1(double delta_time) {
        tank1_drained_volume = getStream2XSpeed()*smallerHole1*delta_time;
    }

    private void buildDeltaVolume2(double delta_time) {
        tank2_drained_volume = getStream2XSpeed()*smallerHole2*delta_time;
    }

    private void initialTank2WaterHeight(double delta_time) {
        initial_water_height2 = getDeltaVolume1(delta_time) / biggerHole2;
        mayor_h2 = initial_water_height2;
    }

    private void updateTank2WaterHeight(double delta_time) {
        double m = (Math.sqrt(2 * GRAVITY_C) * smallerHole2 / ((double) 2 * biggerHole2)) * delta_time;
        double left_factor = Math.sqrt(initial_water_height2) - m;
        mayor_h2 = Math.pow(left_factor, 2);
        if(overflow) {
            buildDeltaVolume2(delta_time);
        }
        initial_water_height2 = mayor_h2;
    }

    private void updateTank1WaterHeight(double delta_time) {
        double m = (Math.sqrt(2 * GRAVITY_C) * smallerHole1 / ((double)2 * biggerHole1))*delta_time;
        double left_factor = Math.sqrt(initial_water_height1) - m;
        mayor_h1 = Math.pow(left_factor, 2);
        if(overflow) {
            buildDeltaVolume1(delta_time);
        }
        initial_water_height1 = mayor_h1;
    }

    private void startSimulator() {
        myTimeStamp = new MyTimeStamp();
        while (mayor_h1 > minor_h1 || mayor_h2 > minor_h2) {
            mf.paintTank1Simulator(tank1_initial_h, mayor_h1, minor_h1, tank1_d2, getStream1LandingX(),
                    tank2_initial_h, mayor_h2, minor_h2, tank2_d2, getStream2LandingX(), started, this, total_wasted_volume);
            started = true;
            double stamp = myTimeStamp.stamp();
            if(mayor_h1 > minor_h1) {
                updateTank1WaterHeight(stamp);
                initialTank2WaterHeight(stamp);
            }
            updateTank2WaterHeight(stamp);
            if(overflow) {
                if(!wasting) {
                    delta_wasted_volume = tank1_drained_volume - tank2_drained_volume; // el volumen desperdiciado es = al volumen que sale del tanque 1 -  el que se escapa del tanque 2
                } else {
                    delta_wasted_volume = tank1_drained_volume; // cuando el chorro ya no entra al tanque 2, el volumen desperdiciado es = al que sale del tanque 1
                }
                total_wasted_volume += delta_wasted_volume;
            }
            if(!startedThread) {
                thread.start();
                startedThread = true;
            }
            mf.repaint();
        }
    }

    private double getStream1XSpeed() {
        double speed = Math.sqrt(2 * GRAVITY_C * (mayor_h1 - minor_h1));
        return (speed > 0) ? speed : 0 ;
    }

    private double getStream2XSpeed() {
        double speed = Math.sqrt(2 * GRAVITY_C * (mayor_h2 - minor_h2));
        return (speed > 0) ? speed : 0 ;
    }

    private double getStream1LandingTime() {
        return Math.sqrt(2 * minor_h1 / GRAVITY_C);
    }

    private double getStream2LandingTime() {
        return Math.sqrt(2 * minor_h2 / GRAVITY_C);
    }

    private double getStream1LandingX() {
        return getStream1XSpeed() * getStream1LandingTime();
    }

    private double getStream2LandingX() {
        return getStream2XSpeed() * getStream2LandingTime();
    }

    public void overflow(boolean flag) {
        overflow = flag;
    }

    public void wasting(boolean wasting) {
        this.wasting = wasting;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mf.setSeconds((seconds++)/(1000/50));
        }
    }
}
