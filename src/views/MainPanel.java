package views;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    private double square1_initial_height;
    private double square1_x1;
    private double square1_y1;
    private double square1_x2;
    private double square1_y2;
    private double hole1_y;
    private double hole1_x;
    private double streamLandingX1;

    private double square2_initial_height;
    private double square2_x1;
    private double square2_y1;
    private double square2_x2;
    private double square2_y2;
    private double hole2_y;
    private double hole2_x;
    private double streamLandingX2;

    private double scale_factor;
    private double h_distance_difference;
    private double v_distance_difference;

    private boolean secondTankOverflowed = false;
    private boolean wasting = false;

    private double tank2_wasted_volume;

    private double seconds;

    private double time_to_close;

    public MainPanel() {
        initComponents();
    }

    private void initComponents() {
        setBackground(Color.black);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        paintWater(square1_x1, square1_y1, square1_x2, square1_y2, g2);
        paintTankEdges(square1_x1, square1_initial_height, square1_x2, square1_y2, g2);
        paintTankEdges(square2_x1, square2_initial_height, square2_x2, square2_y2, g2);
        paintHole(hole1_x, hole1_y, g2);
        paintHole(hole2_x, square2_y2 - 3 * scale_factor, g2);
        if (streamLandingX1 > square1_x2) {
            paintStream(hole1_x, hole1_y, streamLandingX1, square2_y2 * 3 / 2 - v_distance_difference * scale_factor - 8, g2);
        }
        if (streamLandingX2 > square2_x2) {
            paintStream(hole2_x, hole2_y, streamLandingX2, square2_y2 * 3 / 2 - v_distance_difference * scale_factor - 8, g2);
        }
        if (square2_y1 < square2_initial_height) {
            secondTankOverflowed = true;
            paintWater(square2_x1, square2_initial_height - 5, square2_x2, square2_y2, g2);
            g2.setColor(Color.blue);
            g2.setStroke(new BasicStroke(2.8f));
            g2.drawLine((int) square2_x1 - 3, (int) square2_initial_height - 5, (int) square2_x1 - 3, (int) square2_y2);
            g2.drawLine((int) square2_x2 + 3, (int) square2_initial_height - 5, (int) square2_x2 + 3, (int) square2_y2);
        } else {
            secondTankOverflowed = false;
            paintWater(square2_x1, square2_y1, square2_x2, square2_y2, g2);
        }
        if (streamLandingX1 < square2_x1) {
            time_to_close = (time_to_close == 0) ? seconds : time_to_close;
            write("Time To Close Tank1: " + time_to_close + " Secs", 500, 80, Color.red, g2);
            wasting = true;
        }

        write("Wasted Volume: " + tank2_wasted_volume, 500, 40, Color.red, g2);
        write("seconds: " + seconds, 300, 40, Color.green, g2);
    }

    private void write(String string, double x, double y, Color c, Graphics2D g2) {
        g2.setColor(c);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(string, (int) x, (int) y);
    }

    private void paintTankEdges(double x1, double y1, double x2, double y2, Graphics2D g2) {
        double sq_width = x2 - x1;
        double sq_height = y2 - y1;
        g2.setColor(Color.green);
        g2.setStroke(new BasicStroke(3));
        g2.drawRect((int) x1, (int) y1, (int) sq_width, (int) sq_height);
        g2.setStroke(new BasicStroke(1));
    }

    private void paintHole(double x, double y, Graphics2D g2) {
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine((int) x, (int) y - 1, (int) x, (int) y);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1));
    }

    private void paintWater(double x1, double y1, double x2, double y2, Graphics2D g2) {
        double sq_width = x2 - x1;
        double sq_height = y2 - y1;
        g2.setColor(Color.blue);
        g2.fillRect((int) x1, (int) y1, (int) sq_width, (int) sq_height);
    }

    private void paintStream(double x1, double y1, double x2, double y2, Graphics2D g2) {
        g2.setColor(Color.blue);
        g2.setStroke(new BasicStroke(2.8f));
        //g2.fillOval((int) x2 - 5, (int) y2 + 2, 10, 10);
        g2.setStroke(new BasicStroke(3.0f));
        g2.drawLine((int) x1, (int) y1, (int) ((x1 - (x2 - x1) / 2) + (3 * (x2 - x1) / 2) / 2), (int) y1);
        g2.drawArc((int) (x1 - (x2 - x1) / 2), (int) (y1), (int) (3 * (x2 - x1) / 2), (int) (y2 - y1), 0, 90);
        g2.setStroke(new BasicStroke(1));
    }


    public void setStreamLandingX1(double streamLandingX1) {
        this.streamLandingX1 = streamLandingX1;
    }

    public void setSquare1_x1(double square1_x1) {
        this.square1_x1 = square1_x1;
    }

    public void setSquare1_y1(double square1_y1) {
        this.square1_y1 = square1_y1;
    }

    public void setSquare1_x2(double square1_x2) {
        this.square1_x2 = square1_x2;
    }

    public void setSquare1_y2(double square1_y2) {
        this.square1_y2 = square1_y2;
    }

    public double getSquare1_x1() {
        return square1_x1;
    }

    public double getSquare1_y2() {
        return square1_y2;
    }

    public void setSquare1_initial_height(double square1_initial_height) {
        this.square1_initial_height = square1_initial_height;
    }

    public double getSquare1_y1() {
        return square1_y1;
    }

    public double getSquare1_x2() {
        return square1_x2;
    }

    public void setHole1_x(double hole1_x) {
        this.hole1_x = hole1_x;
    }

    public void setHole1_y(double hole1_y) {
        this.hole1_y = hole1_y;
    }

    public double getStreamLandingX1() {
        return streamLandingX1;
    }

    public void setSquare2_initial_height(double square2_initial_height) {
        this.square2_initial_height = square2_initial_height;
    }

    public void setSquare2_x1(double square2_x1) {
        this.square2_x1 = square2_x1;
    }

    public void setSquare2_x2(double square2_x2) {
        this.square2_x2 = square2_x2;
    }

    public void setSquare2_y1(double square2_y1) {
        this.square2_y1 = square2_y1;
    }

    public void setSquare2_y2(double square2_y2) {
        this.square2_y2 = square2_y2;
    }

    public void setHole2_x(double hole2_x) {
        this.hole2_x = hole2_x;
    }

    public void setHole2_y(double hole2_y) {
        this.hole2_y = hole2_y;
    }

    public void setStreamLandingX2(double streamLandingX2) {
        this.streamLandingX2 = streamLandingX2;
    }

    public double getSquare2_y2() {
        return square2_y2;
    }

    public double getSquare2_x2() {
        return square2_x2;
    }

    public double getSquare2_x1() {
        return square2_x1;
    }

    public void setScale_factor(double scale_factor) {
        this.scale_factor = scale_factor;
    }

    public void setH_distance_difference(double h_distance_difference) {
        this.h_distance_difference = h_distance_difference;
    }

    public void setV_distance_difference(double v_distance_difference) {
        this.v_distance_difference = v_distance_difference;
    }

    public boolean isWasting() {
        return wasting;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public boolean isSecondTankOverflowed() {
        return secondTankOverflowed;
    }

    public void setTank2_wasted_volume(double tank2_wasted_volume) {
        this.tank2_wasted_volume = tank2_wasted_volume;
    }
}
