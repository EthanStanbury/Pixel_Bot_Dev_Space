package com.example.mischa.pixelbotui.Intergration;

import android.graphics.Color;

import com.example.mischa.pixelbotui.Swarm.Grid;
import com.example.mischa.pixelbotui.UI.LayoutItem;
import com.example.mischa.pixelbotui.UI.MainActivity;
import com.example.mischa.pixelbotui.UI.PBCanvas;
import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class UIAdapter {

    public static Grid destinationGrid = new Grid(PBCanvas.xDimension, PBCanvas.yDimension);



    public static void createGridWpixel(){

        for (Pixel p : PBCanvas.uiGrid) {
            if(p.colour != Color.TRANSPARENT){
                destinationGrid.addDestination(p);
            }
        }



    }



}
