package GUI.major;

import GUI.major.ATMParamHelper.ATMParameterDialog;
import GUI.major.eventHelper.IncidentEventDialog;
import GUI.major.eventHelper.WeatherEventDialog;
import GUI.major.eventHelper.WorkZoneDialog;

/**
 * This class is the toolbox in main window. Most of the methods provide a link
 * to call methods in mainWindow instead of containing actual codes.
 *
 * @author Shu Liu
 */
public class Toolbox extends javax.swing.JPanel {

    private MainWindow mainWindow;

    /**
     * Creates new form Toolbox
     */
    public Toolbox() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        newButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        settingButton = new javax.swing.JButton();
        showInputButton = new javax.swing.JToggleButton();
        showOutputButton = new javax.swing.JToggleButton();
        APPanel = new javax.swing.JPanel();
        periodLabel = new javax.swing.JLabel();
        timeLabel = new javax.swing.JLabel();
        firstButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        lastButton = new javax.swing.JButton();
        jumpToButton = new javax.swing.JButton();
        jumpText = new javax.swing.JTextField();
        copyButton = new javax.swing.JButton();
        caPanel = new javax.swing.JPanel();
        singleRunButton = new javax.swing.JButton();
        batchRunButton = new javax.swing.JButton();
        atdmBatchRunButton = new javax.swing.JButton();
        cfgPanel = new javax.swing.JPanel();
        globalButton = new javax.swing.JButton();
        fillButton = new javax.swing.JButton();
        mlPanel = new javax.swing.JPanel();
        mlButton = new javax.swing.JButton();
        egPanel = new javax.swing.JPanel();
        weatherEventButton = new javax.swing.JButton();
        incidentEventButton = new javax.swing.JButton();
        workZoneEventButton = new javax.swing.JButton();
        cfgATMPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        newButton.setText("New");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        openButton.setText("Open");
        openButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        saveAsButton.setText("Save As");
        saveAsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        settingButton.setText("Settings");
        settingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingButtonActionPerformed(evt);
            }
        });

        showInputButton.setText("Show Input");
        showInputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInputButtonActionPerformed(evt);
            }
        });

        showOutputButton.setText("Show Output");
        showOutputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showOutputButtonActionPerformed(evt);
            }
        });

        APPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Period (A.P.) Control"));
        APPanel.setLayout(new java.awt.GridLayout(1, 7));

        periodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        periodLabel.setText("A.P. 10/20");
        APPanel.add(periodLabel);

        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("12:00 - 14:00");
        APPanel.add(timeLabel);

        firstButton.setText("First");
        firstButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstButtonActionPerformed(evt);
            }
        });
        APPanel.add(firstButton);

        previousButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/iconHelper/Back24.gif"))); // NOI18N
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });
        APPanel.add(previousButton);

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/iconHelper/Forward24.gif"))); // NOI18N
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });
        APPanel.add(nextButton);

        lastButton.setText("Last");
        lastButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lastButtonActionPerformed(evt);
            }
        });
        APPanel.add(lastButton);

        jumpToButton.setText("Jump To");
        jumpToButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jumpToButtonActionPerformed(evt);
            }
        });
        APPanel.add(jumpToButton);

        jumpText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        APPanel.add(jumpText);

        copyButton.setText("Copy Table");
        copyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyButtonActionPerformed(evt);
            }
        });

        caPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Core Analysis"));
        caPanel.setLayout(new java.awt.GridLayout(1, 0));

        singleRunButton.setText("Single Run");
        singleRunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleRunButtonActionPerformed(evt);
            }
        });
        caPanel.add(singleRunButton);

        batchRunButton.setText("Batch Run");
        batchRunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batchRunButtonActionPerformed(evt);
            }
        });

        atdmBatchRunButton.setText("Batch Run");
        atdmBatchRunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                atdmBatchRunButtonActionPerformed(evt);
            }
        });

        cfgPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Edit Seed Facility"));
        cfgPanel.setLayout(new java.awt.GridLayout(1, 4));

        globalButton.setText("Global Input");
        globalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                globalButtonActionPerformed(evt);
            }
        });
        cfgPanel.add(globalButton);

        fillButton.setText("Fill Data");
        fillButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fillButtonActionPerformed(evt);
            }
        });
        cfgPanel.add(fillButton);

        mlPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Managed Lanes"));
        mlPanel.setLayout(new java.awt.GridLayout(1, 0));

        mlButton.setText("Turn On");
        mlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mlButtonActionPerformed(evt);
            }
        });
        mlPanel.add(mlButton);

        egPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Scenario Events"));
        egPanel.setLayout(new java.awt.GridLayout(1, 3));

        weatherEventButton.setText("Add Weather Event");
        weatherEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weatherEventButtonActionPerformed(evt);
            }
        });
        egPanel.add(weatherEventButton);

        incidentEventButton.setText("Add Incident Event");
        incidentEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                incidentEventButtonActionPerformed(evt);
            }
        });
        egPanel.add(incidentEventButton);

        workZoneEventButton.setText("Add Work Zone");
        workZoneEventButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                workZoneEventButtonActionPerformed(evt);
            }
        });
        egPanel.add(workZoneEventButton);

        cfgATMPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Configure User Level Defaults"));
        cfgATMPanel.setLayout(new java.awt.GridLayout());

        jButton1.setText("ATM Parameters");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        cfgATMPanel.add(jButton1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(cfgPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(egPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cfgATMPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cfgPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(egPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cfgATMPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void singleRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleRunButtonActionPerformed
        mainWindow.runSingle();
    }//GEN-LAST:event_singleRunButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        mainWindow.newSeed();
    }//GEN-LAST:event_newButtonActionPerformed

    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openButtonActionPerformed
        mainWindow.openSeed();
    }//GEN-LAST:event_openButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        mainWindow.saveSeed();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsButtonActionPerformed
        mainWindow.saveAsSeed();
    }//GEN-LAST:event_saveAsButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        mainWindow.closeSeed();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void showInputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInputButtonActionPerformed
        mainWindow.showInput();
    }//GEN-LAST:event_showInputButtonActionPerformed

    private void showOutputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showOutputButtonActionPerformed
        mainWindow.showOutput();
    }//GEN-LAST:event_showOutputButtonActionPerformed

    private void batchRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batchRunButtonActionPerformed
        //mainWindow.runBatchRL();
    }//GEN-LAST:event_batchRunButtonActionPerformed

    private void globalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_globalButtonActionPerformed
        mainWindow.globalInput();
    }//GEN-LAST:event_globalButtonActionPerformed

    private void fillButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fillButtonActionPerformed
        mainWindow.fillData();
    }//GEN-LAST:event_fillButtonActionPerformed

    private void copyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyButtonActionPerformed
        //mainWindow.copyTable();
    }//GEN-LAST:event_copyButtonActionPerformed

    private void settingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingButtonActionPerformed
        mainWindow.showTableSettings();
    }//GEN-LAST:event_settingButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        mainWindow.showFirstPeriod();
    }//GEN-LAST:event_firstButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        mainWindow.showPrevPeriod();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        mainWindow.showNextPeriod();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        mainWindow.showLastPeriod();
    }//GEN-LAST:event_lastButtonActionPerformed

    private void jumpToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumpToButtonActionPerformed
        try {
            mainWindow.selectPeriod(Integer.parseInt(jumpText.getText()) - 1);
        } catch (Exception e) {
            mainWindow.printLog("Invalid period");
            jumpText.setText("");
        }
    }//GEN-LAST:event_jumpToButtonActionPerformed

    private void mlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mlButtonActionPerformed
        mainWindow.toggleManagedLane();
    }//GEN-LAST:event_mlButtonActionPerformed

    private void atdmBatchRunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_atdmBatchRunButtonActionPerformed
        //mainWindow.runBatchATDM();
    }//GEN-LAST:event_atdmBatchRunButtonActionPerformed

    private void weatherEventButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weatherEventButtonActionPerformed
        WeatherEventDialog weDialog = new WeatherEventDialog(null, true);
        weDialog.setSeed(mainWindow.getActiveSeed());
        weDialog.setLocationRelativeTo(null);
        weDialog.setVisible(true);
        if (weDialog.getReturnStatus()) {
            mainWindow.addWeatherEvent(weDialog.getWeatherEvent());
        }
        weDialog.dispose();

    }//GEN-LAST:event_weatherEventButtonActionPerformed

    private void incidentEventButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_incidentEventButtonActionPerformed
        IncidentEventDialog incDialog = new IncidentEventDialog(null, true);
        incDialog.setSeed(mainWindow.getActiveSeed());
        incDialog.setLocationRelativeTo(null);
        incDialog.setVisible(true);
        if (incDialog.getReturnStatus()) {
            mainWindow.addIncidentEvent(incDialog.getIncidentEvent());
        }
        incDialog.dispose();
    }//GEN-LAST:event_incidentEventButtonActionPerformed

    private void workZoneEventButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_workZoneEventButtonActionPerformed
        WorkZoneDialog wzDialog = new WorkZoneDialog(null, true);
        wzDialog.setSeed(mainWindow.getActiveSeed());
        wzDialog.setLocationRelativeTo(null);
        wzDialog.setVisible(true);
        if (wzDialog.getReturnStatus()) {
            mainWindow.addWorkZone(wzDialog.getWorkZoneEvent());
        }
        wzDialog.dispose();
    }//GEN-LAST:event_workZoneEventButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ATMParameterDialog atmParamDialog = new ATMParameterDialog(null, true);
        atmParamDialog.setSeed(mainWindow.getActiveSeed());
        atmParamDialog.setLocationRelativeTo(null);
        atmParamDialog.setVisible(true);
        if (atmParamDialog.getReturnStatus()) {
            mainWindow.setATMParameters(atmParamDialog.getATMParameterSet());
        }
        atmParamDialog.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

//    /**
//     * Show a particular analysis period data
//     *
//     * @param period index of period selected
//     */
//    public void selectPeriod(int period) {
////        if (period < 0) {
////            periodLabel.setText("A.P.");
////            timeLabel.setText("-");
////        } else {
////            periodLabel.setText("A.P. " + (period + 1) + "/" + mainWindow.getActiveSeed().getValueInt(CEConst.IDS_NUM_PERIOD));
////            timeLabel.setText(mainWindow.getActiveSeed().getValueString(CEConst.IDS_PERIOD_TIME, 0, period));
////        }
//    }
//    /**
//     * Show input
//     */
//    public void showInput() {
//        //showInputButton.setSelected(true);
//        //showOutputButton.setSelected(false);
//    }
//
//    /**
//     * Show output
//     */
//    public void showOutput() {
//        //showInputButton.setSelected(false);
//        //showOutputButton.setSelected(true);
//    }
//
//    /**
//     * Enable show output option
//     */
//    public void enableOutput() {
//        //showOutputButton.setEnabled(true);
////        showContourButton.setEnabled(true);
////        showFacilityButton.setEnabled(true);
//    }
//
//    /**
//     * Disable show output option
//     */
//    public void disableOutput() {
//        //showOutputButton.setEnabled(false);
////        showContourButton.setEnabled(false);
////        showFacilityButton.setEnabled(false);
//    }
    /**
     * Configure display when the seed is null
     */
    public void setNullSeed() {
        caPanel.setEnabled(false);
        APPanel.setEnabled(false);
        //IOPanel.setEnabled(false);
        //raPanel.setEnabled(false);
        //addPeriodButton.setEnabled(false);
        //addSegButton.setEnabled(false);
        batchRunButton.setEnabled(false);
        //showRLSummaryButton.setEnabled(false);
        cfgPanel.setEnabled(false);
        closeButton.setEnabled(false);
        copyButton.setEnabled(false);
        //delPeriodButton.setEnabled(false);
        //delSegButton.setEnabled(false);
        fillButton.setEnabled(false);
        firstButton.setEnabled(false);
        //geneScenButton.setEnabled(false);
        //deleteAllScenButton.setEnabled(false);
        globalButton.setEnabled(false);
        jumpText.setEnabled(false);
        jumpToButton.setEnabled(false);
        lastButton.setEnabled(false);
        newButton.setEnabled(false);
        nextButton.setEnabled(false);
        openButton.setEnabled(false);
        periodLabel.setEnabled(false);
        timeLabel.setEnabled(false);
        previousButton.setEnabled(false);
        saveAsButton.setEnabled(false);
        saveButton.setEnabled(false);
        settingButton.setEnabled(false);
        showInputButton.setEnabled(false);
        showOutputButton.setEnabled(false);
        singleRunButton.setEnabled(false);
        //atdmPanel.setEnabled(false);
        //atdmAssignButton.setEnabled(false);
        //atdmDeleteButton.setEnabled(false);
        atdmBatchRunButton.setEnabled(false);
        //atdmSummaryButton.setEnabled(false);
//        atdmButton3.setEnabled(false);

        mlPanel.setEnabled(false);
        mlButton.setEnabled(false);

        egPanel.setEnabled(false);
        weatherEventButton.setEnabled(false);
        incidentEventButton.setEnabled(false);
        workZoneEventButton.setEnabled(false);
    }

    /**
     * Configure display when the seed is not null
     */
    public void setNonNullSeed() {
        caPanel.setEnabled(true);
        APPanel.setEnabled(true);
        //IOPanel.setEnabled(true);
        //raPanel.setEnabled(true);
        //addPeriodButton.setEnabled(true);
        //addSegButton.setEnabled(true);
        batchRunButton.setEnabled(true);
        //showRLSummaryButton.setEnabled(true);
        cfgPanel.setEnabled(true);
        closeButton.setEnabled(true);
        copyButton.setEnabled(true);
        //delPeriodButton.setEnabled(true);
        //delSegButton.setEnabled(true);
        fillButton.setEnabled(true);
        firstButton.setEnabled(true);
        //geneScenButton.setEnabled(true);
        //deleteAllScenButton.setEnabled(true);
        globalButton.setEnabled(true);
        jumpText.setEnabled(true);
        jumpToButton.setEnabled(true);
        lastButton.setEnabled(true);
        newButton.setEnabled(true);
        nextButton.setEnabled(true);
        openButton.setEnabled(true);
        periodLabel.setEnabled(true);
        timeLabel.setEnabled(true);
        previousButton.setEnabled(true);
        saveAsButton.setEnabled(true);
        saveButton.setEnabled(true);
        settingButton.setEnabled(true);
        showInputButton.setEnabled(true);
        showOutputButton.setEnabled(true);
        singleRunButton.setEnabled(true);
        //atdmPanel.setEnabled(true);
        //atdmAssignButton.setEnabled(true);
        //atdmDeleteButton.setEnabled(true);
        atdmBatchRunButton.setEnabled(true);
        //atdmSummaryButton.setEnabled(true);
//        atdmButton3.setEnabled(true);

        mlPanel.setEnabled(true);
        mlButton.setEnabled(true);

        egPanel.setEnabled(true);
        weatherEventButton.setEnabled(true);
        incidentEventButton.setEnabled(true);
        workZoneEventButton.setEnabled(true);
    }

    public void enableML() {
        mlButton.setEnabled(true);
    }

    public void disableML() {
        mlButton.setEnabled(false);
    }

    public void turnOnML() {
        mlButton.setText("Turn Off");
    }

    public void turnOffML() {
        mlButton.setText("Turn On");
    }

    // <editor-fold defaultstate="collapsed" desc="setter and getters">
    /**
     * Setter for mainWindow connection
     *
     * @param mainWindow main window instance
     */
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    // </editor-fold>

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel APPanel;
    private javax.swing.JButton atdmBatchRunButton;
    private javax.swing.JButton batchRunButton;
    private javax.swing.JPanel caPanel;
    private javax.swing.JPanel cfgATMPanel;
    private javax.swing.JPanel cfgPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton copyButton;
    private javax.swing.JPanel egPanel;
    private javax.swing.JButton fillButton;
    private javax.swing.JButton firstButton;
    private javax.swing.JButton globalButton;
    private javax.swing.JButton incidentEventButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JTextField jumpText;
    private javax.swing.JButton jumpToButton;
    private javax.swing.JButton lastButton;
    private javax.swing.JButton mlButton;
    private javax.swing.JPanel mlPanel;
    private javax.swing.JButton newButton;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton openButton;
    private javax.swing.JLabel periodLabel;
    private javax.swing.JButton previousButton;
    private javax.swing.JButton saveAsButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton settingButton;
    private javax.swing.JToggleButton showInputButton;
    private javax.swing.JToggleButton showOutputButton;
    private javax.swing.JButton singleRunButton;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JButton weatherEventButton;
    private javax.swing.JButton workZoneEventButton;
    // End of variables declaration//GEN-END:variables
}
