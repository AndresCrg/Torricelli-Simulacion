package controller;

import models.TorricelliEngine;
import views.MainFrame;

public class Controller {
    private MainFrame mf;
    private TorricelliEngine te;

    public Controller() {
        initComponents();
    }

    private void initComponents( ){
        mf = new MainFrame();
        te = new TorricelliEngine(20,2, 10,0.5, 10, 3, 10, 0.4, 2, mf);
    }
}
