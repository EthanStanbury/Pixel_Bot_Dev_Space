package com.example.mischa.pixelbotui.UI;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Mischa on 13/04/2018.
 */

public class MainThread extends Thread {

    // THIS IS THE CLASS THAT CONTAINS THE THREAD THAT RUNS THE SIMULATION

    boolean running;
    Canvas canvas;
    Simulation simulation;
    SurfaceHolder surfaceHolder;

    public MainThread(SurfaceHolder holder, Simulation simulation) {
        super();
        this.surfaceHolder = holder;
        this.simulation = simulation;
    }

    @Override
    public void run() {
        while (running) {
            canvas = null;

            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    while (simulation.unfinishedBots.size() > 0 && simulation.runThread) {
                        this.simulation.run();
                    }
                }
            } catch (Exception e) {
            }
            finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
