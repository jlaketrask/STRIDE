/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.eventHelper;

import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author jltrask
 */
public class ModelCreator {
    /**
     * Creates the model for the period selection combo box. The type designates
     * whether it is for start period (0) selection or end period selection (1).
     * End period selection indicates that the periods go through the end
     * period, and the clock time displayed is the time at the end of the period
     * (as opposed to clock time at the beginning of the period for start
     * times).
     * 
     * @param seed
     * @param type
     * @return 
     */
    public static DefaultComboBoxModel periodCBModelCreator(Seed seed, int type) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_PERIOD)];
        //tempArr[0] = "<Select Period>";
        int currHour = seed.getStartTime().hour;
        int currMin = seed.getStartTime().minute;
        if (type == 1) {
            currMin += 15;
        }
        for (int perIdx = 1; perIdx <= tempArr.length; perIdx++) {
            if (currMin == 60) {
                currMin = 0;
                currHour++;
            }
            if (currMin == 0) {
                tempArr[perIdx - 1] = String.valueOf(perIdx) + "  (" + currHour + ":00)";
            } else {
                tempArr[perIdx - 1] = String.valueOf(perIdx) + "  (" + currHour + ":" + currMin + ")";
            }
            currMin += 15;
        }

        DefaultComboBoxModel model = new DefaultComboBoxModel(tempArr);
        return model;
    }
    
    /**
     * Creates the model for the period selection combo box. The type designates
     * whether it is for start period (0) selection or end period selection (1).
     * End period selection indicates that the periods go through the end
     * period, and the clock time displayed is the time at the end of the period
     * (as opposed to clock time at the beginning of the period for start
     * times).
     *
     * @param seed
     * @param type
     * @param startPeriod
     * @return
     */
    public static DefaultComboBoxModel periodCBModelCreator(Seed seed, int type, int startPeriod) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_PERIOD) - (startPeriod - 1)];
        //tempArr[0] = "<Select Period>";
        int currHour = seed.getStartTime().hour;
        int currMin = seed.getStartTime().minute;
        if (type == 1) {
            currMin += 15;
        }
        int currIdx = 0;
        for (int perIdx = 1; perIdx <= seed.getValueInt(CEConst.IDS_NUM_PERIOD); perIdx++) {
            if (currMin == 60) {
                currMin = 0;
                currHour++;
            }
            if (perIdx >= startPeriod) {
                if (currMin == 0) {
                    tempArr[currIdx] = String.valueOf(perIdx) + "  (" + currHour + ":00)";
                } else {
                    tempArr[currIdx] = String.valueOf(perIdx) + "  (" + currHour + ":" + currMin + ")";
                }
                currIdx++;
            }
            currMin += 15;

        }

        return new DefaultComboBoxModel(tempArr);
    }

    /**
     * Creates the model for the segment selection combo box.
     *
     * @param seed
     * @return
     */
    public static DefaultComboBoxModel segmentCBModelCreator(Seed seed) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_SEGMENT)];

        for (int seg = 1; seg <= tempArr.length; seg++) {
            tempArr[seg - 1] = String.valueOf(seg);
        }

        return new DefaultComboBoxModel(tempArr);
    }

    /**
     * Creates the model for the segment selection combo box.
     * 
     * @param seed
     * @param startSegment
     * @return 
     */
    public static DefaultComboBoxModel segmentCBModelCreator(Seed seed, int startSegment) {
        String[] tempArr = new String[seed.getValueInt(CEConst.IDS_NUM_SEGMENT) - (startSegment-1)];

        int currSeg = 0;
        for (int seg = startSegment; seg <= seed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            tempArr[currSeg] = String.valueOf(seg);
            currSeg++;
        }

        return new DefaultComboBoxModel(tempArr);
    }

}
