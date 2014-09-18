/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import DSS.DataStruct.PeriodATM;
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
        switch (row) {
            case 0:
                return periodATM[currPeriod].getCAF(col);
            case 1:
                return periodATM[currPeriod].getSAF(col);
            case 2:
                return periodATM[currPeriod].getRMRate(col);
            default:
                throw new RuntimeException("Invalid Row Index");
        }
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return col != 0;
    }
}
