package GUI.settingHelper;

import GUI.seedEditAndIOHelper.ConfigIO;
import GUI.major.MainWindow;
import GUI.major.tableHelper.SegIOTableWithSetting;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;
import say.swing.JFontChooser;

/**
 * This class is the table setting dialog
 *
 * @author Shu Liu
 */
public class TableSettingDialog extends javax.swing.JDialog {

    private final SegIOTableWithSetting wrapper;
    private final MainWindow mainWindow;

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form TableSettingDialog
     *
     * @param mainWindow MainWindow instance that calls this dialog
     * @param wrapper table data structure that contains existing settings
     */
    public TableSettingDialog(MainWindow mainWindow, SegIOTableWithSetting wrapper) {
        super(mainWindow, true);
        initComponents();

        this.mainWindow = mainWindow;
        this.wrapper = wrapper;
        Font currentTableFont = MainWindow.getTableFont();
        currentFontLabel.setText("Current Table Font: " + currentTableFont.getFamily()
                + " size: " + currentTableFont.getSize());
        currentFontLabel.setFont(currentTableFont);
        tableSettingJTable.setFont(currentTableFont);
        tableSettingJTable.setRowHeight(currentTableFont.getSize() + 2);
        tableSettingJTable.setCellSettings(wrapper.getCellSettings());

        //set starting position
        this.setLocationRelativeTo(this.getRootPane());

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * Getter for return status
     *
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        saveAsDefaultButton = new javax.swing.JButton();
        resetButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSettingJTable = new GUI.settingHelper.TableSettingJTable();
        jPanel1 = new javax.swing.JPanel();
        currentFontLabel = new javax.swing.JLabel();
        changeFontButton = new javax.swing.JButton();

        setTitle("Table Settings");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        okButton.setText("OK");
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

        saveAsDefaultButton.setText("Save as Default");
        saveAsDefaultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsDefaultButtonActionPerformed(evt);
            }
        });

        resetButton.setText("Reset All");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Single Scenario Table Display Items"));

        tableSettingJTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tableSettingJTable);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Table Font"));

        currentFontLabel.setText("Current Table Font");

        changeFontButton.setText("Change Table Font");
        changeFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFontButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(currentFontLabel)
                    .addComponent(changeFontButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(currentFontLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(changeFontButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(saveAsDefaultButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addGap(6, 6, 6))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelButton, okButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(okButton)
                    .addComponent(saveAsDefaultButton)
                    .addComponent(resetButton))
                .addContainerGap())
        );

        getRootPane().setDefaultButton(okButton);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doClose(RET_OK);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose(RET_CANCEL);
    }//GEN-LAST:event_cancelButtonActionPerformed

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void saveAsDefaultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsDefaultButtonActionPerformed
        wrapper.setCellSettings(tableSettingJTable.getCellSettings());
        mainWindow.printLog(ConfigIO.saveTableConfig(tableSettingJTable.getCellSettings(), currentFontLabel.getFont()));
    }//GEN-LAST:event_saveAsDefaultButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        wrapper.resetCellSettings();
        tableSettingJTable.setCellSettings(wrapper.getCellSettings());

        Font newFont = MainWindow.DEFAULT_TABLE_FONT;
        currentFontLabel.setFont(newFont);
        currentFontLabel.setText("Current Table Font: " + newFont.getFamily()
                + " size: " + newFont.getSize());
        tableSettingJTable.setFont(newFont);
        tableSettingJTable.setRowHeight(newFont.getSize() + 2);
        mainWindow.setTableFont(currentFontLabel.getFont());
        
        mainWindow.printLog("Table settings reset");
    }//GEN-LAST:event_resetButtonActionPerformed

    private void changeFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeFontButtonActionPerformed
        JFontChooser fontChooser = new JFontChooser(sizeRange);
        fontChooser.setSelectedFont(MainWindow.getTableFont());
        int result = fontChooser.showDialog(this);
        if (result == JFontChooser.OK_OPTION) {
            Font newFont = fontChooser.getSelectedFont();
            currentFontLabel.setFont(newFont);
            currentFontLabel.setText("Current Table Font: " + newFont.getFamily()
                    + " size: " + newFont.getSize());
            tableSettingJTable.setFont(newFont);
            tableSettingJTable.setRowHeight(newFont.getSize() + 2);
        }
    }//GEN-LAST:event_changeFontButtonActionPerformed

    private void doClose(int retStatus) {
        if (retStatus == RET_OK) {
            wrapper.setCellSettings(tableSettingJTable.getCellSettings());
            mainWindow.setTableFont(currentFontLabel.getFont());
        }

        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton changeFontButton;
    private javax.swing.JLabel currentFontLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveAsDefaultButton;
    private GUI.settingHelper.TableSettingJTable tableSettingJTable;
    // End of variables declaration//GEN-END:variables

    private int returnStatus = RET_CANCEL;
    private final static String[] sizeRange = {"15", "16", "17", "18", "19", "20", "21", "22"};
}
