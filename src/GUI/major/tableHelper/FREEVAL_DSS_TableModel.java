/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import DSS.DataStruct.PeriodATM;
import GUI.major.MainWindowUser;
import coreEngine.CEConst;
import coreEngine.Seed;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jltrask
 */
public class FREEVAL_DSS_TableModel extends AbstractTableModel {
    
    //private String[] columnNames;
    private String[] rowNames = {"Capacity Adj. Factor",
                                 "Speed Adj. Factor",
                                 "Ramp Metering Rate",
                                };
    
    private final Seed seed;
    
    private PeriodATM[] periodATM;
    
    private int currPeriod;
    
    public FREEVAL_DSS_TableModel(Seed seed, PeriodATM[] periodATM) {
        this.seed = seed;
        this.periodATM = periodATM;
        currPeriod = 0;
    }
    
    @Override
    public int getColumnCount() {
        return seed.getValueInt(CEConst.IDS_NUM_SEGMENT)+1;
    }
    
    @Override
    public int getRowCount() {
        return rowNames.length;
    }
    
    @Override
    public String getColumnName(int col) {
        if (col == 0) {
            return "Segment";
        } else {
            return "Seg. "+col;
        }
    }
    
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return rowNames[row];
        } else {
            switch (row) {
                case 0:
                    return periodATM[currPeriod].getCAF(col-1);
                case 1:
                    return periodATM[currPeriod].getSAF(col-1);
                case 2:
                    return periodATM[currPeriod].getRMRate(col-1);
                default:
                    throw new RuntimeException("Invalid Row Index");
            }
        }
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        try {
            switch (row) {
                case 0:
                    periodATM[currPeriod].setCAF(Float.parseFloat((String) value), col-1);
                    break;
                case 1:
                    periodATM[currPeriod].setSAF(Float.parseFloat((String) value), col-1);
                    break;
                case 2:
                    periodATM[currPeriod].setRMRate(Integer.parseInt((String) value), col-1);
                    break;
                default:
                    throw new RuntimeException("Invalid Row Index");
            }
        } catch (NumberFormatException e) {
            MainWindowUser.printLog("Invalid Value Entered");
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
