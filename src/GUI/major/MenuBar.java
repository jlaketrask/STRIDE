package GUI.major;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * This class is the menu bar in main window. Most of the methods provide a link
 * to call methods in mainWindow instead of containing actual codes.
 *
 * @author Shu Liu
 */
public class MenuBar extends JMenuBar {

    private MainWindow mainWindow;

    /**
     * Constructor
     */
    public MenuBar() {
        super();

        // <editor-fold defaultstate="collapsed" desc="create File menu">
        fileMenu = new JMenu("File");
        add(fileMenu);

        newMenuItem = new JMenuItem("New");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(newMenuItem);

        openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem = new JMenuItem("Save As");
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveAsMenuItem);

        closeMenuItem = new JMenuItem("Close");
        closeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                closeMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(closeMenuItem);

        fileMenu.add(new javax.swing.JSeparator());
        importASCIIMenuItem = new JMenuItem("Import from ASCII File");
        importASCIIMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                importASCIIMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(importASCIIMenuItem);

        exportASCIIMenuItem = new JMenuItem("Export to ASCII File");
        exportASCIIMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                exportASCIIItemActionPerformed(evt);
            }
        });
        fileMenu.add(exportASCIIMenuItem);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Run menu">
        analyzeMenu = new JMenu("Analyze");
        add(analyzeMenu);

//        runSingleMenuItem = new JMenuItem("Single Run");
//        runSingleMenuItem.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public final void actionPerformed(java.awt.event.ActionEvent evt) {
//                runSingleMenuItemActionPerformed(evt);
//            }
//        });
//        runMenu.add(runSingleMenuItem);
//        runMenu.add(new javax.swing.JSeparator());
        toggleMLMenuItem = new JMenuItem("Turn On Managed Lanes");
        toggleMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleMLMenuItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(toggleMLMenuItem);

        analyzeMenu.add(new javax.swing.JSeparator());

        geneScenMenuItem = new JMenuItem("Generate Reliability Scenarios");
        geneScenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                geneScenMenuItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(geneScenMenuItem);

        deleteScenMenuItem = new JMenuItem("Delete All Reliability Scenarios");
        deleteScenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteScenMenuItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(deleteScenMenuItem);

//        runBatchMenuItem = new JMenuItem("Run Reliability Scenarios");
//        runBatchMenuItem.addActionListener(new java.awt.event.ActionListener() {
//            @Override
//            public final void actionPerformed(java.awt.event.ActionEvent evt) {
//                runBatchMenuItemActionPerformed(evt);
//            }
//        });
//        runMenu.add(runBatchMenuItem);
        showRLSummaryMenuItem = new JMenuItem("Show Reliability Summary");
        showRLSummaryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showRLSummaryMenuItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(showRLSummaryMenuItem);

        analyzeMenu.add(new javax.swing.JSeparator());

        assignATDMItem = new JMenuItem("Assign ATDM Plans");
        assignATDMItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                assignATDMItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(assignATDMItem);

        deleteATDMItem = new JMenuItem("Delete All ATDM Sets");
        deleteATDMItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteATDMItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(deleteATDMItem);

        showATDMSummaryItem = new JMenuItem("Show ATDM Summary");
        showATDMSummaryItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showATDMSummaryItemActionPerformed(evt);
            }
        });
        analyzeMenu.add(showATDMSummaryItem);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Edit menu">
        editMenu = new JMenu("Edit");
        add(editMenu);

        globalInputMenuItem = new JMenuItem("Global Input");
        globalInputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                globalInputMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(globalInputMenuItem);

        fillDataMenuItem = new JMenuItem("Fill Data");
        fillDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                fillDataMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(fillDataMenuItem);

        editMenu.add(new javax.swing.JSeparator());

        copyTableMenuItem = new JMenuItem("Copy Table");
        copyTableMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                copyTableMenuItemActionPerformed(evt);
            }
        });
        //editMenu.add(copyTableMenuItem);

        //editMenu.add(new javax.swing.JSeparator());
        insertSegMenuItem = new JMenuItem("Add Segment");
        insertSegMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                insertSegMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(insertSegMenuItem);

        deleteSegMenuItem = new JMenuItem("Delete Segment");
        deleteSegMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteSegMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deleteSegMenuItem);

        editMenu.add(new javax.swing.JSeparator());

        insertPeriodMenuItem = new JMenuItem("Add Analysis Period");
        insertPeriodMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                insertPeriodMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(insertPeriodMenuItem);

        deletePeriodMenuItem = new JMenuItem("Delete Analysis Period");
        deletePeriodMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                deletePeriodMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(deletePeriodMenuItem);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create View menu">
        viewMenu = new JMenu("View");
        add(viewMenu);

        singleScenIOMenu = new JMenu("Singele Scenario Input/Output");
        viewMenu.add(singleScenIOMenu);

        showInputMenuItem = new JMenuItem("Show Input");
        showInputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showInputItemActionPerformed(evt);
            }
        });
        singleScenIOMenu.add(showInputMenuItem);

        showOutputMenuItem = new JMenuItem("Show Output");
        showOutputMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showOutputItemActionPerformed(evt);
            }
        });
        singleScenIOMenu.add(showOutputMenuItem);

        singleScenIOMenu.add(new javax.swing.JSeparator());

        showGPOnlyMenuItem = new JMenuItem("Show GP in Table Only");
        showGPOnlyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showGPOnlyMenuItemActionPerformed(evt);
            }
        });
        singleScenIOMenu.add(showGPOnlyMenuItem);

        showMLOnlyMenuItem = new JMenuItem("Show ML in Table Only");
        showMLOnlyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showMLOnlyMenuItemActionPerformed(evt);
            }
        });
        singleScenIOMenu.add(showMLOnlyMenuItem);

        showGPMLMenuItem = new JMenuItem("Show GP & ML in Table");
        showGPMLMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                showGPMLMenuItemActionPerformed(evt);
            }
        });
        singleScenIOMenu.add(showGPMLMenuItem);

        viewMenu.add(new javax.swing.JSeparator());

        firstAPMenuItem = new JMenuItem("Show First Analysis Period");
        firstAPMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                firstAPMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(firstAPMenuItem);

        previousAPMenuItem = new JMenuItem("Show Previous Analysis Period");
        previousAPMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                previousAPMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(previousAPMenuItem);

        nextAPMenuItem = new JMenuItem("Show Next Analysis Period");
        nextAPMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                nextAPMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(nextAPMenuItem);

        lastAPMenuItem = new JMenuItem("Show Last Analysis Period");
        lastAPMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                lastAPMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(lastAPMenuItem);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Settings menu">
        settingMenu = new JMenu("Settings");
        add(settingMenu);

        tableSettingMenuItem = new JMenuItem("Table Display Settings");
        tableSettingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                tableSettingItemActionPerformed(evt);
            }
        });
        settingMenu.add(tableSettingMenuItem);

        graphicSettingMenuItem = new JMenuItem("Graphic Display Settings");
        graphicSettingMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                graphicSettingMenuItemActionPerformed(evt);
            }
        });
        settingMenu.add(graphicSettingMenuItem);

        settingMenu.add(new javax.swing.JSeparator());

        createFloatingWindowItem = new JMenuItem("Create Floating Window");
        createFloatingWindowItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                createFloatingWindowItemActionPerformed(evt);
            }
        });
        settingMenu.add(createFloatingWindowItem);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="create Help menu">
        helpMenu = new JMenu("Help");
        add(helpMenu);

        helpDocumentItem = new JMenuItem("Source Code Documentation");
        helpDocumentItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                helpDocumentItemActionPerformed(evt);
            }
        });
        helpMenu.add(helpDocumentItem);

        helpMenu.add(new javax.swing.JSeparator());

        aboutMenuItem = new JMenuItem("About/Contact");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public final void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);
        // </editor-fold>

        turnOffML();
        disableATDM();
    }

    /**
     * Enable show output option
     */
    public final void enableOutput() {
        showOutputMenuItem.setEnabled(true);
    }

    /**
     * Disable show output option
     */
    public final void disableOutput() {
        showOutputMenuItem.setEnabled(false);
    }

    public final void enableRL() {
        geneScenMenuItem.setEnabled(true);
        deleteScenMenuItem.setEnabled(true);
        showRLSummaryMenuItem.setEnabled(true);
    }

    public final void disableRL() {
        geneScenMenuItem.setEnabled(false);
        deleteScenMenuItem.setEnabled(false);
        showRLSummaryMenuItem.setEnabled(false);
    }

    public final void enableATDM() {
        assignATDMItem.setEnabled(true);
        showATDMSummaryItem.setEnabled(true);
        deleteATDMItem.setEnabled(true);
    }

    public final void disableATDM() {
        assignATDMItem.setEnabled(false);
        showATDMSummaryItem.setEnabled(false);
        deleteATDMItem.setEnabled(false);
    }

    public final void turnOnML() {
        showMLOnlyMenuItem.setEnabled(true);
        showGPMLMenuItem.setEnabled(true);
        toggleMLMenuItem.setText("Turn Off Managed Lane");
    }

    public final void turnOffML() {
        showMLOnlyMenuItem.setEnabled(false);
        showGPMLMenuItem.setEnabled(false);
        toggleMLMenuItem.setText("Turn On Managed Lane");
    }

    public void enableML() {
        toggleMLMenuItem.setEnabled(true);
    }

    public void disableML() {
        toggleMLMenuItem.setEnabled(false);
    }

    /**
     * Configure display when the seed is null
     */
    public final void setNullSeed() {
        analyzeMenu.setEnabled(false);
        editMenu.setEnabled(false);
        viewMenu.setEnabled(false);
    }

    /**
     * Configure display when the seed is not null
     */
    public final void setNonNullSeed() {
        analyzeMenu.setEnabled(true);
        editMenu.setEnabled(true);
        viewMenu.setEnabled(true);
    }

    // <editor-fold defaultstate="collapsed" desc="File menu actions">
    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.newSeed();
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.openSeed();
    }

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.saveSeed();
    }

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.saveAsSeed();
    }

    private void closeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.closeSeed();
    }

    private void importASCIIMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.importASCII();
    }

    private void exportASCIIItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.exportASCII();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Run menu actions">
//    private void runSingleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
//        mainWindow.runSingle();
//    }
    private void toggleMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.toggleManagedLane();
    }

    private void geneScenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.generateRL();
    }

    private void deleteScenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.deleteAllScen();
    }

//    private void runBatchMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
//        mainWindow.runBatchRL();
//    }
    private void showRLSummaryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.showRLSummary();
    }

    private void assignATDMItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.generateATDM();
    }

    private void deleteATDMItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.deleteAllATDM();
    }

    private void showATDMSummaryItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.showATDMSummary();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Edit menu actions">
    private void globalInputMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.globalInput();
    }

    private void fillDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.fillData();
    }

    private void copyTableMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.copyTable();
    }

    private void insertSegMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.addSegment();
    }

    private void deleteSegMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.delSegment();
    }

    private void insertPeriodMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.addPeriod();
    }

    private void deletePeriodMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.delPeriod();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="View & Settings menu actions">
    private void showInputItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showInput();
    }

    private void showOutputItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showOutput();
    }

    private void showGPOnlyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showGPOnly();
    }

    private void showMLOnlyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showMLOnly();
    }

    private void showGPMLMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showGPML();
    }

    private void tableSettingItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showTableSettings();
    }

    private void graphicSettingMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showGraphicSettings();
    }

    private void createFloatingWindowItemActionPerformed(java.awt.event.ActionEvent evt) {
        //mainWindow.createFloatingWindow();
    }

    private void firstAPMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showFirstPeriod();
    }

    private void previousAPMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showPrevPeriod();
    }

    private void nextAPMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showNextPeriod();
    }

    private void lastAPMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showLastPeriod();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Help menu actions">
    private void helpDocumentItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showJavaDoc();
    }

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.showAbout();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="setter and getters">
    /**
     * Setter for mainWindow connection
     *
     * @param mainWindow main window instance
     */
    public final void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }
    // </editor-fold>

    private final JMenu fileMenu, analyzeMenu, editMenu, viewMenu, settingMenu, helpMenu, singleScenIOMenu;

    private final JMenuItem newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem, closeMenuItem, importASCIIMenuItem, exportASCIIMenuItem;

    private final JMenuItem toggleMLMenuItem, geneScenMenuItem, deleteScenMenuItem, showRLSummaryMenuItem;
    private final JMenuItem assignATDMItem, deleteATDMItem, showATDMSummaryItem;

    private final JMenuItem globalInputMenuItem, fillDataMenuItem, copyTableMenuItem;
    private final JMenuItem insertSegMenuItem, deleteSegMenuItem, insertPeriodMenuItem, deletePeriodMenuItem;

    private final JMenuItem showInputMenuItem, showOutputMenuItem, showGPOnlyMenuItem, showMLOnlyMenuItem, showGPMLMenuItem;
    private final JMenuItem firstAPMenuItem, previousAPMenuItem, nextAPMenuItem, lastAPMenuItem;

    private final JMenuItem tableSettingMenuItem, graphicSettingMenuItem, createFloatingWindowItem;

    private final JMenuItem helpDocumentItem, aboutMenuItem;
}
