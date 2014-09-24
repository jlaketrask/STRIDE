/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.tableHelper;

import GUI.major.MainWindowUser;
import coreEngine.Seed;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author jltrask
 */
public class SegATMTable {
    //private final TableFirstColumnRenderer tableFirstColumnRenderer = new TableFirstColumnRenderer(this);
    //private final DefaultTableCellRenderer tableFirstColumnRenderer = new DefaultTableCellRenderer();


    // <editor-fold defaultstate="collapsed" desc="TABLE AND TABLE MODELS">
    private final JTable firstColumnTable;
    private final FREEVAL_DSS_TableModel firstColumnModel;
    //private final FREEVAL_TableModel firstColumnModelOutput;
    private final FREEVAL_DSS_JTable restColumnTable;
    private final FREEVAL_DSS_TableModel restColumnModel;
    //private final FREEVAL_TableModel restColumnModelOutput;

    // </editor-fold>

    private Seed seed;
    private int period = 0;

    private MainWindowUser mainWindow;
    
    public SegATMTable() {
        //this.seed = seed;
        //this.mainWindow = mainWindow;
        //this.seed = mainWindow.getActiveSeed();
        
        //<editor-fold defaultstate="collapsed" desc="Building ATM Input Table">
        restColumnTable = new FREEVAL_DSS_JTable();
        //restColumnModel = new FREEVAL_DSS_TableModel(FREEVAL_DSS_TableModel.TYPE_ATM_INPUT, seed, periodATM, restColumnTable);
        restColumnModel = new FREEVAL_DSS_TableModel(FREEVAL_DSS_TableModel.TYPE_ATM_INPUT, restColumnTable);
        restColumnTable.setModel(restColumnModel);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        //restColumnTable.setFont(MainWindowUser.getTableFont());
        //restColumnTable.setRowHeight(MainWindowUser.getTableFont().getSize() + 4);
        restColumnTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //restColumnTable.getColumnModel().getColumn(0).setPreferredWidth(275);
        for (int colIdx = 0; colIdx < restColumnModel.getColumnCount(); colIdx++) {
            restColumnTable.getColumnModel().getColumn(colIdx).setPreferredWidth(75);
        }
        //</editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="Building First Column (Row Names)">
        firstColumnTable = new FREEVAL_DSS_JTable();
        firstColumnModel = new FREEVAL_DSS_TableModel(FREEVAL_DSS_TableModel.TYPE_ROW_NAMES, firstColumnTable);
        firstColumnTable.setModel(firstColumnModel);
        //</editor-fold>
        
    }
    
    public void activate() {
//        PeriodATM[] periodATM = new PeriodATM[seed.getValueInt(CEConst.IDS_NUM_PERIOD)];
//        for (int per = 0; per < periodATM.length; per++) {
//            periodATM[per] = new PeriodATM(seed, per);
//        }
//        restColumnModel.setPeriodATM(periodATM);
    }
    
    /**
     * Getter for first column table
     *
     * @return first column table
     */
    public JTable getFirstColumnTable() {
        return firstColumnTable;
    }

    /**
     * Getter for rest column table
     *
     * @return rest column table
     */
    public FREEVAL_DSS_JTable getRestColumnTable() {
        return restColumnTable;
    }
    
    public void update() {
        firstColumnModel.fireTableDataChanged();
        restColumnModel.fireTableDataChanged();
        //firstColumnModel.fireTableStructureChanged();
        //restColumnModel.fireTableStructureChanged();
        
    }
    
    private void updatePeriod() {
        restColumnModel.setPeriod(period);
        update();
    }
    
    public void selectSeedScenPeriod(Seed seed, int period) {
        this.seed = seed;
        this.period = period;
        activate();
        updatePeriod();
    }
    
    public void setMainWindow(MainWindowUser mainWindow) {
        this.mainWindow = mainWindow;
        firstColumnModel.setMainWindow(mainWindow);
        restColumnModel.setMainWindow(mainWindow);
        //restColumnModel.setATMScenario(mainWindow.getATMScenario());
        //s1electSeedScenPeriod(this.mainWindow.getActiveSeed(), 0);
        //System.out.println(seed);
    }
}
