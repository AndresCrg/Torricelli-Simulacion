package views;

import models.TorricelliEngine;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private MainPanel mainPanel;

    public MainFrame() {
        initComponents();
        initProperties();
    }

    private void initComponents() {
        mainPanel = new MainPanel();
        add(mainPanel);
    }

    private void initProperties() {
        setVisible(true);
        int width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        setSize(800,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void paintTank1Simulator(double initial_h1, double water_height1, double minor_h1, double width1, double stream1LandingX,
                                    double initial_h2, double water_height2, double minor_h2, double width2, double stream2LandingX,
                                    boolean started, TorricelliEngine te, double tank2_wasted_volume) {
        int scale_factor = 10;
        int initial_x = 10;
        int v_distance_delta = 2;
        int h_distance_delta = 6;

        if(!started) {
            mainPanel.setScale_factor(scale_factor);
            mainPanel.setV_distance_difference(v_distance_delta);
            mainPanel.setH_distance_difference(h_distance_delta);

            mainPanel.setSquare1_initial_height(initial_h1*scale_factor - water_height1*scale_factor);
            mainPanel.setSquare1_y2(initial_h1*scale_factor);
            mainPanel.setSquare1_x1(initial_x*scale_factor);
            mainPanel.setSquare1_x2(mainPanel.getSquare1_x1() + (scale_factor*width1));

            mainPanel.setSquare2_initial_height(initial_h2*scale_factor - water_height2*scale_factor + mainPanel.getSquare1_y2() + v_distance_delta*scale_factor);
            mainPanel.setSquare2_y2(mainPanel.getSquare1_y2() + initial_h2*scale_factor + v_distance_delta*scale_factor);
            mainPanel.setSquare2_x1(h_distance_delta*scale_factor + mainPanel.getSquare1_x2());
            mainPanel.setSquare2_x2(mainPanel.getSquare2_x1() + (scale_factor*width2 ));
        }
        mainPanel.setSquare1_y1(mainPanel.getSquare1_y2() - water_height1*scale_factor);
        mainPanel.setHole1_x(mainPanel.getSquare1_x2());
        mainPanel.setHole1_y(mainPanel.getSquare1_y2() - minor_h1*scale_factor);
        mainPanel.setStreamLandingX1(mainPanel.getSquare1_x2() + stream1LandingX*scale_factor);

        mainPanel.setSquare2_y1(mainPanel.getSquare2_y2() - water_height2*scale_factor);
        mainPanel.setHole2_x(mainPanel.getSquare2_x2());
        mainPanel.setHole2_y(mainPanel.getSquare2_y2() - minor_h2*scale_factor);
        mainPanel.setStreamLandingX2(mainPanel.getSquare2_x2() + stream2LandingX*scale_factor);


        mainPanel.setTank2_wasted_volume(tank2_wasted_volume);
        te.overflow(mainPanel.isSecondTankOverflowed());
        te.wasting(mainPanel.isWasting());
    }

    public void setSeconds(double seconds) {
        mainPanel.setSeconds(seconds);
    }
}
