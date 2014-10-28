/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.major.ATMParamHelper;

import GUI.major.MainWindow;
import GUI.major.tableHelper.SplitTableJPanel;
import GUI.major.tableHelper.TableSelectionCellEditor;
import GUI.seedEditAndIOHelper.ConfigIO;
import coreEngine.Helper.CEConst;
import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author jltrask
 */
public class ATMDiversionDialog extends javax.swing.JDialog {

    private final MainWindow mainWindow;
    private boolean returnStatus = false;
    private final Boolean[] setDiversionAvailable;
    private final float[] OFRdiversion;
    private final float[] ONRdiversion;

    /**
     * Creates new form ATMDiversionDialog.
     * 
     * @param parent
     * @param modal 
     */
    public ATMDiversionDialog(MainWindow parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        this.mainWindow = parent;
        
        // Creating diversionAvailable array
        setDiversionAvailable = mainWindow.getUserLevelParameters().atm.diversionAtSeg.clone();
        ONRdiversion = mainWindow.getUserLevelParameters().atm.ONRdiversion.clone();
        OFRdiversion = mainWindow.getUserLevelParameters().atm.OFRdiversion.clone();
        
        // Setting up graphicDisplay
        graphicDisplay.setMainWindow(mainWindow);
        graphicDisplay.update();
        graphicDisplay.setScaleColors(ConfigIO.loadGraphicConfig(mainWindow));
        graphicDisplay.selectSeedScenATDMPeriod(mainWindow.getActiveSeed(), 0, -1, mainWindow.getActivePeriod());
        
        // Creating Table and Table Models
        JTable diversionTableFirst = new JTable();
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        DiversionRowNamesModel diversionTableRowNames = new DiversionRowNamesModel();
        diversionTableFirst.setModel(diversionTableRowNames);
        diversionTableFirst.setRowHeight(MainWindow.getTableFont().getSize()+2);
        diversionTableFirst.setFont(MainWindow.getTableFont());
        diversionTableFirst.setDefaultRenderer(Object.class, rightRenderer);
        diversionTableFirst.getTableHeader().setReorderingAllowed(false);
        
        JTable diversionTableRest = new DiversionSignLocationJTable();
        diversionTableRest.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //restColumnTable.getColumnModel().getColumn(0).setPreferredWidth(275);
        for (int colIdx = 0; colIdx < diversionTableRest.getModel().getColumnCount(); colIdx++) {
            diversionTableRest.getColumnModel().getColumn(colIdx).setPreferredWidth(75);
        }
        
        diversionRowNameScrollPane.setViewportView(diversionTableFirst);
        diversionTableScrollPane.setViewportView(diversionTableRest);
        diversionSplitPane.setDividerLocation(270);
    }

    private void doClose() {
        if (returnStatus) {
            mainWindow.getUserLevelParameters().atm.diversionAtSeg = setDiversionAvailable.clone();
            mainWindow.getUserLevelParameters().atm.OFRdiversion = OFRdiversion.clone();
            mainWindow.getUserLevelParameters().atm.ONRdiversion = ONRdiversion.clone();
        }
        this.setVisible(false);
    }

    //<editor-fold defaultstate="collapsed" desc="Table, Table Models, Renderers and Editors">
    private class DiversionSignLocationTableModel extends AbstractTableModel {
        
        private final CheckBoxRenderer checkBoxRenderer;
        private final DefaultTableCellRenderer centerRenderer;
        private final DefaultTableCellRenderer blackOutRenderer;
        private final TableSelectionCellEditor defaultCellEditor;
        private final DefaultCellEditor checkBoxEditor;
        
        public DiversionSignLocationTableModel() {
            centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            checkBoxRenderer = new CheckBoxRenderer();
            checkBoxRenderer.setHorizontalAlignment(JLabel.CENTER);
            blackOutRenderer = new DefaultTableCellRenderer();
            blackOutRenderer.setForeground(Color.DARK_GRAY);
            blackOutRenderer.setBackground(Color.DARK_GRAY);
            // Creating Default cell editor
            defaultCellEditor = new TableSelectionCellEditor(true);
            
            // Creating CheckBox cell editor
            JCheckBox editorCB = new JCheckBox();
            editorCB.setHorizontalAlignment(JLabel.CENTER);
            editorCB.setBackground(Color.WHITE);
            editorCB.setForeground(Color.WHITE);
            checkBoxEditor = new DefaultCellEditor(editorCB);
        }
        
        @Override
        public int getColumnCount() {
            return mainWindow.getActiveSeed().getValueInt(CEConst.IDS_NUM_SEGMENT);
        }
        
        @Override
        public int getRowCount() {
            return 3;
        }
        
        @Override
        public String getColumnName(int col) {
            return "Seg "+(col+1);
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            switch (row) {
                default:
                case 0:
                    return setDiversionAvailable[col];
                case 1:
                    return OFRdiversion[col];
                case 2:
                    return ONRdiversion[col];
            }
            
        }
        
        @Override
        public void setValueAt(Object val, int row, int col) {
            switch (row) {
                case 0:
                    setDiversionAvailable[col] = (boolean) val;
                    this.fireTableCellUpdated(row+1, col);
                    this.fireTableCellUpdated(row+2, col);
                    break;
                case 1:
                    OFRdiversion[col] = Float.parseFloat((String) val);
                    break;
                case 2:
                    ONRdiversion[col] = Float.parseFloat((String) val);
                    break;
            }
        }
        
        @Override
        public boolean isCellEditable(int row, int col) {
            switch (row) {
                case 0:
                    return (mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_ONR || 
                            mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_OFR ||
                            mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_W);
                case 1:
                    return ((mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_OFR ||
                            mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_W) &&
                            setDiversionAvailable[col]);
                case 2:
                    return ((mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_ONR ||
                            mainWindow.getActiveSeed().getValueInt(CEConst.IDS_SEGMENT_TYPE, col) == CEConst.SEG_TYPE_W) &&
                            setDiversionAvailable[col]);
                default:
                    return false;
            }
        }
        
        public TableCellRenderer getRenderer(int row, int col) {
            switch (row) {
                case 0:
                    if (isCellEditable(row,col)) {
                        return checkBoxRenderer;
                    } else {
                        return blackOutRenderer;
                    }
                case 1:
                case 2:
                    if (isCellEditable(row,col)) {
                        return centerRenderer;
                    } else {
                        return blackOutRenderer;
                    }
                default:
                    return centerRenderer;
            }
            
        }
        
        public TableCellEditor getEditor(int row, int col) {
            switch (row) {
                case 0:
                    return checkBoxEditor;
                default:
                    return defaultCellEditor;
            }
        }
        
    };
    
    private class DiversionRowNamesModel extends AbstractTableModel {
        
        
        public DiversionRowNamesModel() {
        }
        
        @Override
        public int getColumnCount() {
            return 1;
        }
        
        @Override
        public int getRowCount() {
            return 3;
        }
        
        @Override
        public String getColumnName(int col) {
            return "Segment";
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            switch (row) {
                default:
                case 0:
                    return "Enable Diversion Sign Location";
                case 1:
                    return "Off-Ramp Diversion";
                case 2:
                    return "On-Ramp Diversion";
            }
            
        }
        
        
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
        
    };
    
    
    private class DiversionSignLocationJTable extends JTable {
        
        private final DiversionSignLocationTableModel tableModel;
        
        public DiversionSignLocationJTable() {
            super();
            this.tableModel = new DiversionSignLocationTableModel();
            this.setModel(tableModel);
            this.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
            this.setFont(MainWindow.getTableFont());
            setRowHeight(MainWindow.getTableFont().getSize()+2);
            resetModel();
        }
        
        private void resetModel() {
            getTableHeader().setReorderingAllowed(false);
            this.rowSelectionAllowed = false;
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        /**
         * Highlight a column
         *
         * @param col column to be highlighted
         */
        public void setHighlightCol(int col) {
            try {
                setColumnSelectionInterval(col, col);
            } catch (Exception e) {
                //skip
            }
        }
        
        @Override
        public TableCellEditor getCellEditor(int row, int col) {
            return ((DiversionSignLocationTableModel) this.getModel()).getEditor(row,col);
        }
        
        @Override
        public TableCellRenderer getCellRenderer(int row, int col) {
            return ((DiversionSignLocationTableModel) this.getModel()).getRenderer(row, col);
        }
    }
    
    private class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            try {
                this.setSelected((boolean) value);
            } catch (Exception e) {
                this.setSelected(false);
            }
            return this;
        }
    }
//</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        graphicDisplay = new GUI.major.GraphicDisplay();
        diversionSplitPane = new javax.swing.JSplitPane();
        diversionRowNameScrollPane = new javax.swing.JScrollPane();
        diversionTableScrollPane = new javax.swing.JScrollPane();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        javax.swing.GroupLayout graphicDisplayLayout = new javax.swing.GroupLayout(graphicDisplay);
        graphicDisplay.setLayout(graphicDisplayLayout);
        graphicDisplayLayout.setHorizontalGroup(
            graphicDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 983, Short.MAX_VALUE)
        );
        graphicDisplayLayout.setVerticalGroup(
            graphicDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(graphicDisplay);

        diversionSplitPane.setLeftComponent(diversionRowNameScrollPane);
        diversionSplitPane.setRightComponent(diversionTableScrollPane);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(diversionSplitPane)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 985, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diversionSplitPane, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(865, Short.MAX_VALUE)
                .addComponent(okButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cancelButton, okButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        returnStatus = false;
        doClose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        returnStatus = true;
        doClose();
    }//GEN-LAST:event_okButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane diversionRowNameScrollPane;
    private javax.swing.JSplitPane diversionSplitPane;
    private javax.swing.JScrollPane diversionTableScrollPane;
    private GUI.major.GraphicDisplay graphicDisplay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
