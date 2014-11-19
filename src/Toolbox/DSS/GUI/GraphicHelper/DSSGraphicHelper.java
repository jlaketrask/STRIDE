/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Toolbox.DSS.GUI.GraphicHelper;

import java.awt.Color;

/**
 *
 * @author jltrask
 */
public class DSSGraphicHelper {
    
    private static float minValFFS = 0.0f;

    private static float midValFFS = 0.0f;

    private static float maxValFFS = 0.0f;

    private static final Color minColor = Color.red;

    private static final Color midColor = Color.yellow;

    private static final Color maxColor = Color.green;

    private static final float rMin = minColor.getRed() / 255.0f;

    private static final float rMid = midColor.getRed() / 255.0f;

    private static final float rMax = maxColor.getRed() / 255.0f;

    private static final float gMin = minColor.getGreen() / 255.0f;

    private static final float gMid = midColor.getGreen() / 255.0f;

    private static final float gMax = maxColor.getGreen() / 255.0f;

    private static final float bMin = minColor.getBlue() / 255.0f;

    private static final float bMid = midColor.getBlue() / 255.0f;

    private static final float bMax = maxColor.getBlue() / 255.0f;
    
    public static final String COLOR_BY_SPEED = "COLOR_BY_SPEED";
    
    public static final String COLOR_BY_LOS = "COLOR_BY_LOS";
    
    public static Color getSegmentColorBySpeed(float cellValue) {

        if (minValFFS == maxValFFS) {
            return Color.white;
        } else {

            if (cellValue < midValFFS) {
                float p = (Math.max(cellValue - minValFFS,0.0f)) / (midValFFS - minValFFS);
                return new Color(rMin * (1.0f - p) + rMid * p,
                        gMin * (1.0f - p) + gMid * p,
                        bMin * (1.0f - p) + bMid * p,
                        0.7f);
            } else {
                float p = (Math.min(cellValue,maxValFFS) - midValFFS) / (maxValFFS - midValFFS);
                return new Color(rMid * (1.0f - p) + rMax * p,
                        gMid * (1.0f - p) + gMax * p,
                        bMid * (1.0f - p) + bMax * p,
                        0.7f);
            }
        }
    }
    
    /**
     *
     * @param newMin
     * @param newMax
     */
    public static void setColorRange(float newMin, float newMax) {
        if (newMin <= newMax) {
            minValFFS = newMin;
            midValFFS = (newMin + newMax) / 2.0f;
            maxValFFS = newMax;
        }
    }
}
