package com.example.mischa.pixelbotui.Intergration;

import com.example.mischa.pixelbotui.UI.MainActivity;
import com.example.mischa.pixelbotui.UI.PBCanvas;
import com.example.mischa.pixelbotui.UI.Pixel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 10/04/2018.
 */

public class UI_Adapter {

     public HashMap<Integer, Integer> GetBotAmounts(){

        return MainActivity.BotAmounts();


    }

    public ArrayList<ArrayList<Pixel>> GetGrid(){

        ArrayList<ArrayList<Pixel>> returnArray = new ArrayList<>();

            for (int i = 0; i < PBCanvas.xDimension; i++){
                ArrayList<Pixel> add = new ArrayList<>();
                int index = i;
                for(int x = 0; x < PBCanvas.yDimension; x++){
                    add.add(PBCanvas.uiGrid[index]);
                    index += PBCanvas.xDimension;
                }
                returnArray.add(add);
            }

        return  returnArray;
    }
}
