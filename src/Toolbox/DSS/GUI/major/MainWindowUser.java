package Toolbox.DSS.GUI.major;

import Toolbox.DSS.GUI.major.MainWindowStart;
import Toolbox.DSS.Core.DataStruct.ATMUpdater;
import Toolbox.DSS.Core.DataStruct.PeriodATM;
import Toolbox.DSS.Core.DataStruct.UserLevelParameterSet;
import Toolbox.DSS.GUI.GraphicHelper.DSSGraphicHelper;
import Toolbox.DSS.GUI.IOHelper.DSSProject;
import GUI.major.menuHelper.AboutDialog;
import GUI.seedEditAndIOHelper.ConfigIO;
import GUI.seedEditAndIOHelper.ExcelAdapter;
import GUI.seedEditAndIOHelper.SeedFillDataDialog;
import GUI.seedEditAndIOHelper.SeedGlobalDialog;
import GUI.seedEditAndIOHelper.SeedIOHelper;
import GUI.settingHelper.GraphicSettingDialog;
import GUI.settingHelper.TableSettingDialog;
import coreEngine.Helper.ASCIISeedFileAdapter_GPMLFormat;
import coreEngine.Helper.CEConst;
import coreEngine.Seed;
import coreEngine.atdm.DataStruct.ATDMScenario;
import coreEngine.reliabilityAnalysis.DataStruct.Scenario;
import coreEngine.reliabilityAnalysis.DataStruct.ScenarioInfo;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class is the main window of FREEVAL. All seeds are contained in this
 * class. All central control functions are in this class.
 *
 * @author Shu Liu
 */
public class MainWindowUser extends MainWindow {

    private static MainWindowUser mainWindow;
    private MainWindowStart mainWindowStart;

    private ArrayList<Seed> seedList = new ArrayList<>();
    private Seed activeSeed;
    private int activeScen = 0;
    private int activePeriod = 0;
    private int dssProgress = 0;
    private int activeATDM = -1;
    private boolean isShowingInput = true;
    private boolean numPeriodChanged = false;
    private boolean isOutputEnabled = true;
    //public static final Font DEFAULT_TABLE_FONT = new Font("Arial", Font.PLAIN, 17);
    private static Font tableFont = DEFAULT_TABLE_FONT;

    private static final DefaultComboBoxModel GPML_MODEL = new DefaultComboBoxModel(new String[]{"GP Only", "ML Only", "GP & ML"});
    private static final DefaultComboBoxModel GP_ONLY_MODEL = new DefaultComboBoxModel(new String[]{"GP Only"});
    private static final DefaultComboBoxModel INPUT_OUTPUT_MODEL = new DefaultComboBoxModel(new String[]{"Input", "Output"});
    private static final DefaultComboBoxModel INPUT_ONLY_MODEL = new DefaultComboBoxModel(new String[]{"Input"});

    private final UserLevelParameterSet userParams;

    private ATDMScenario activeATM;
    private final ATMUpdater atmUpdater;
    private final PeriodATM[] periodATM;
    private final ArrayList<PeriodATM[]> completedRunsPeriodATM;
    private final ArrayList<ATDMScenario> completedRunsATDMScen;

    //private final TableDisplay tableDisplay;
    private final TableDisplaySegmentATM tableDisplaySegmentATM;

    private boolean resetReady = false;

    /**
     * Version of the FREEVAL
     */
    //public final String VERSION = "Alpha 09242014";
    // <editor-fold defaultstate="collapsed" desc="CONSTRUCTOR">
    /**
     * Constructor. Creates new form mainWindow
     *
     * @param mainWindowStart
     * @param dssProject
     */
    public MainWindowUser(MainWindowStart mainWindowStart, DSSProject dssProject) {
        super();
        mainWindow = this;
        this.mainWindowStart = mainWindowStart;
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

        initComponents();

        activeSeed = dssProject.getSeed();
        this.userParams = dssProject.getUserLevelParameterSet();

        // Setting up ATM
        this.activeATM = new ATDMScenario(activeSeed.getValueInt(CEConst.IDS_NUM_SEGMENT), activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD));
        periodATM = new PeriodATM[activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD)];
        for (int per = 0; per < periodATM.length; per++) {
            periodATM[per] = new PeriodATM(activeSeed, per, this.userParams.atm);
        }

        atmUpdater = new ATMUpdater(activeSeed, activeATM, periodATM, this.userParams);
        if (activeSeed.getValueInt(CEConst.IDS_NUM_SCEN) == 0) {
            // Adding blank scenario
            ArrayList<ScenarioInfo> scenarioInfos = new ArrayList();
            scenarioInfos.add(new ScenarioInfo());
            activeSeed.setRLScenarios(new Scenario(1, activeSeed.getValueInt(CEConst.IDS_NUM_SEGMENT), activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD)), null, scenarioInfos);
        }

        HashMap<Integer, ATDMScenario[]> atmHolder = new HashMap();
        atmHolder.put(1, new ATDMScenario[]{activeATM, null});
        activeSeed.addATDMSet(atmHolder);

        completedRunsPeriodATM = new ArrayList();
        completedRunsATDMScen = new ArrayList();

        // Preparing Window Components
        tableDisplaySegmentATM = userIOTableDisplay.getTableDisplaySegmentATM();
        setLocationRelativeTo(this.getRootPane()); //center starting position
        connect();
        addSeed(activeSeed);
        update();

        //setNullSeed();
        //toolbox.setNullSeed();
        //menuBar.setNullSeed();
        setVisible(true);

        tableDisplay.setCellSettings(ConfigIO.loadTableConfig(this));
        graphicDisplay.showIncidents(false);
        graphicDisplay.setScaleColors(ConfigIO.loadGraphicConfig(this));

        graphicDisplay.setSegmentColorStyle(userParams.SEGMENT_COLOR_STYLE);
        int minFFS = 150;
        for (int seg = 0; seg < activeSeed.getValueInt(CEConst.IDS_NUM_SEGMENT); seg++) {
            if (activeSeed.getValueInt(CEConst.IDS_MAIN_FREE_FLOW_SPEED, seg) < minFFS) {
                minFFS = activeSeed.getValueInt(CEConst.IDS_MAIN_FREE_FLOW_SPEED, seg);
            }
        }
        DSSGraphicHelper.setColorRange(Math.round(minFFS / 2.0f), minFFS);

        inOutCB.setSelectedIndex(1);
        inOutCB.setEnabled(false);

        numPeriodChanged = true;
        selectSeedScen(activeSeed, 1);
        selectPeriod(0);
        //activeScen = 1;
        activeATDM = 0;

        //showInputButton.setVisible(false);
        //showOutputButton.setVisible(false);
    }

    /**
     * Connect major components for central control
     */
    private void connect() {
        menuBar.setMainWindow(this);
        toolbox.setMainWindow(this);
        graphicDisplay.setMainWindow(this);
        navigator.setMainWindow(this);
        userIOTableDisplay.activate(this);
        userIOTableDisplay.updateTitle();
        tableDisplay.setMainWindow(this);
        tableDisplaySegmentATM.setMainWindow(this);
        tableDisplaySegmentATM.setScrollModel(tableDisplay.getScrollModel());
        //comparePanel.setMainWindow(this);
    }
    // </editor-fold>

    public void applyATM() {
        atmUpdater.update(activePeriod);

        selectPeriod(activePeriod + 1);
        dssProgress += 1;
    }

    public boolean validateATM(int validationType) {
        if (validationType == 1) {
            return atmUpdater.validate(activePeriod);
        } else {
            return periodATM[activePeriod].checkEmpty();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="FLOATING WINDOW">
    //private static final String TOOLBOX = "Toolbox";
    private static final String CONTOUR = "Result Contours";
    //private static final String COMPARE = "Compare Scenarios";
    //private static final String SINGLE = "Single Scenario I/O";
    private static final String PERIOD = "Analysis Period Summary";
    private static final String SEGMENT = "Segment & Facility Summary";
    //private static final String LOG = "Log";
    //private static final String NAVIGATOR = "Navigator";
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CENTRAL CONTROL FUNCTIONS">
    // <editor-fold defaultstate="collapsed" desc="SEED IO FUNCTIONS">
    /**
     * Create a new seed
     */
    @Override
    public void newSeed() {
        SeedGlobalDialog seedCreaterDialog = new SeedGlobalDialog(null, this);
        seedCreaterDialog.setVisible(true);
        Seed seed = seedCreaterDialog.getSeed();
        if (seed != null) {
            addSeed(seed);
            printLog("New seed created");
        }
    }

    /**
     * Open a .seed file
     */
    @Override
    public void openSeed() {
        Seed seed = SeedIOHelper.openSeed();
        if (seed != null) {
            if (openedSeed(seed)) {
                JOptionPane.showMessageDialog(this, "This seed file is already opened", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                addSeed(seed);
            }
        } else {
            printLog("Fail to open seed");
        }
    }

    /**
     * Check whether a seed file is ready opened
     *
     * @param seed seed to be checked
     * @return whether a seed file is ready opened
     */
    private boolean openedSeed(Seed seed) {
        for (Seed openedSeed : seedList) {
            try {
                if (openedSeed.getValueString(CEConst.IDS_SEED_FILE_NAME).equals(seed.getValueString(CEConst.IDS_SEED_FILE_NAME))) {
                    return true;
                }
            } catch (Exception e) {
                //skip this seed
            }
        }
        return false;
    }

    /**
     * Save active seed to file
     */
    @Override
    public void saveSeed() {
        saveSeed(activeSeed);
    }

    /**
     * Save a seed to file
     *
     * @param seed seed to be saved
     */
    @Override
    public void saveSeed(Seed seed) {
        printLog(SeedIOHelper.saveSeed(seed));
        update();
    }

    /**
     * Save active seed to another seed file
     */
    @Override
    public void saveAsSeed() {
        printLog(SeedIOHelper.saveAsSeed(activeSeed));
        update();
    }

    /**
     * Close active seed
     *
     *
     */
    @Override
    public void closeSeed() {
        printLog(navigator.closeSeed());
    }

    /**
     * Import a seed from ASCII file
     */
    public void importASCII() {
        ASCIISeedFileAdapter_GPMLFormat textSeed = new ASCIISeedFileAdapter_GPMLFormat();
        Seed _seed = textSeed.importFromASCII();
        if (_seed != null) {
            printLog("Seed file added from ASCII file : " + _seed.getValueString(CEConst.IDS_SEED_FILE_NAME));
            _seed.setValue(CEConst.IDS_SEED_FILE_NAME, null);
            addSeed(_seed);
        } else {
            printLog("Fail to import ASCII file");
        }
    }

    /**
     * Export active seed to ASCII file
     */
    public void exportASCII() {
        if (activeSeed != null) {
            ASCIISeedFileAdapter_GPMLFormat exporter = new ASCIISeedFileAdapter_GPMLFormat();
            String fileName = exporter.exportToASCII(activeSeed);
            //ASCIISeedFileAdapter exporter = new ASCIISeedFileAdapter();
            //String fileName = exporter.exportToFile(activeSeed);
            if (fileName != null) {
                printLog("Exported seed to ASCII file : " + fileName);
            } else {
                printLog("Fail to exported seed to ASCII file");
            }
        }
    }

    /**
     * Add a seed to seed list
     *
     * @param seed seed to be added
     */
    @Override
    public void addSeed(Seed seed) {
        seedList.add(seed);
        navigator.seedAdded(seed);
    }

    /**
     * Show global input for active seed
     */
    @Override
    public void globalInput() {
        if (activeSeed != null) {
            SeedGlobalDialog seedCreaterDialog = new SeedGlobalDialog(activeSeed, this);
            seedCreaterDialog.setVisible(true);

            mainWindow.updateSeed();
            mainWindow.update();
        }
    }

    /**
     * Show fill data dialog
     */
    @Override
    public void fillData() {
        if (activeSeed != null) {
            SeedFillDataDialog fillDataDialog = new SeedFillDataDialog(activeSeed, this);
            fillDataDialog.setVisible(true);
            mainWindow.update();
        }
    }

    /**
     * Copy table in table display to clipboard
     *
     * @param firstColumnTable
     * @param restColumnTable
     */
    @Override
    public void copyTable(JTable firstColumnTable, JTable restColumnTable) {
        printLog(ExcelAdapter.copySplitTable(firstColumnTable, restColumnTable));
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="DISPLAY CONTROL FUNCTIONS">

    /**
     * Update display when seed and scenario selected
     *
     * @param seed seed selected
     * @param scen index of the selected RL scenario
     */
    @Override
    public void selectSeedScen(Seed seed, int scen) {
        selectSeedScen(seed, scen, -1);
    }

    /**
     * Update display when seed and scenario selected
     *
     * @param seed seed selected
     * @param scen index of the selected RL scenario
     * @param atdm index of the selected ATDM scenario
     */
    @Override
    public void selectSeedScen(Seed seed, int scen, int atdm) {
        if (activeScen == scen && activeATDM == atdm
                && activeSeed != null && seed != null && activeSeed.equals(seed)) {
            return;
        }

        try {
            if (activeSeed == null || !activeSeed.equals(seed)) {
                activeSeed = seed;
                selectPeriod(0);
                updateTitle();
            }

            if (activeSeed == null || scen < 0 || scen > activeSeed.getValueInt(CEConst.IDS_NUM_SCEN)) {
                activeScen = 0;
            } else {
                activeScen = scen;
            }

            activeATDM = atdm;

            if (activeSeed != null) {
                printLog(seed.toString() + " Scen#" + activeScen
                        + (activeATDM >= 0 ? " ATDM#" + (activeATDM + 1) : "") + " selected");

                tableDisplay.selectSeedScenATDMPeriod(activeSeed, activeScen, activeATDM, activePeriod);
                tableDisplaySegmentATM.selectSeedScenPeriod(activeSeed, activePeriod);
                graphicDisplay.selectSeedScenATDMPeriod(activeSeed, activeScen, activeATDM, activePeriod);
                //contourPanel.selectSeedScenATDM(activeSeed, activeScen, activeATDM);
                //periodSummaryPanel.selectSeedScenATDM(activeSeed, activeScen, activeATDM);
                //segmentSummaryPanel.selectSeedScenATDM(activeSeed, activeScen, activeATDM);
            } else {
                tableDisplay.selectSeedScenATDMPeriod(null, 0, - 1, 0);
                tableDisplaySegmentATM.selectSeedScenPeriod(null, 0);
                graphicDisplay.selectSeedScenATDMPeriod(null, 0, -1, 0);
                //contourPanel.selectSeedScenATDM(null, 0, -1);
                //periodSummaryPanel.selectSeedScenATDM(null, 0, -1);
                //segmentSummaryPanel.selectSeedScenATDM(null, 0, -1);
            }
            update();
        } catch (Exception e) {
            e.printStackTrace();
            printLog("Error in selectSeedScen : " + e.toString());
        }
    }

    private void setNonNullSeed() {
        APPanel.setEnabled(true);
        firstButton.setEnabled(true);
        jumpText.setEnabled(true);
        jumpToButton.setEnabled(true);
        lastButton.setEnabled(true);
        nextButton.setEnabled(true);
        periodLabel.setEnabled(true);
        timeLabel.setEnabled(true);
        previousButton.setEnabled(true);
        tableDisplayOptionPanel.setEnabled(true);
        //inOutCB.setEnabled(true);
        GPMLCB.setEnabled(true);
        //showInputButton.setEnabled(true);
        //showOutputButton.setEnabled(true);
    }

    private void setNullSeed() {
        APPanel.setEnabled(false);
        firstButton.setEnabled(false);
        jumpText.setEnabled(false);
        jumpToButton.setEnabled(false);
        lastButton.setEnabled(false);
        nextButton.setEnabled(false);
        periodLabel.setEnabled(false);
        timeLabel.setEnabled(false);
        previousButton.setEnabled(false);
        tableDisplayOptionPanel.setEnabled(false);
        inOutCB.setEnabled(false);
        GPMLCB.setEnabled(false);
        //showInputButton.setEnabled(false);
        //showOutputButton.setEnabled(false);
    }

    private boolean hasOutput() {
        return activeSeed.hasValidOutput(activeScen, activeATDM);
    }

    /**
     * Configure toolbox and show input in graphic display and table display
     */
    @Override
    public void showInput() {
        isShowingInput = true;
        tableDisplay.showInput();
        //graphicDisplay.showInput();
        //tabPanel.setSelectedComponent(singleScenSplitPanel);
        showInputButton.setSelected(true);
        showOutputButton.setSelected(false);
        inOutCB.setSelectedIndex(0);
    }

    /**
     * Configure toolbox and show output in graphic display and table display
     */
    @Override
    public void showOutput() {
        isShowingInput = false;
        //toolbox.showOutput();
        tableDisplay.showOutput();
        tableDisplaySegmentATM.update();
        //graphicDisplay.showOutput();
        showInputButton.setSelected(false);
        showOutputButton.setSelected(true);
        inOutCB.setSelectedIndex(1);
    }

    private void enableOutput() {
        isOutputEnabled = true;
        menuBar.enableOutput();

        showOutputButton.setEnabled(true);
        if (!inOutCB.getModel().equals(INPUT_OUTPUT_MODEL)) {
            inOutCB.setModel(INPUT_OUTPUT_MODEL);
        }
        //MARKDEDFORDELETION
//        if (tabPanel.indexOfComponent(contourPanel) < 0) {
//            tabPanel.addTab(CONTOUR, contourPanel);
//        }
//
//        if (tabPanel.indexOfComponent(periodSummaryPanel) < 0) {
//            tabPanel.addTab(PERIOD, periodSummaryPanel);
//        }
//
//        if (tabPanel.indexOfComponent(segmentSummaryPanel) < 0) {
//            tabPanel.addTab(SEGMENT, segmentSummaryPanel);
//        }

    }

    private void disableOutput() {
        isOutputEnabled = false;
        showInput();
        menuBar.disableOutput();

        showOutputButton.setEnabled(false);
        if (!inOutCB.getModel().equals(INPUT_ONLY_MODEL)) {
            inOutCB.setModel(INPUT_ONLY_MODEL);
        }
        //MARKEDFORDENSITY
//        if (tabPanel.indexOfComponent(contourPanel) >= 0) {
//            if (tabPanel.getSelectedComponent() == contourPanel) {
//                tabPanel.setSelectedComponent(singleScenSplitPanel);
//            }
//            tabPanel.remove(contourPanel);
//        }
//
//        if (tabPanel.indexOfComponent(periodSummaryPanel) >= 0) {
//            if (tabPanel.getSelectedComponent() == periodSummaryPanel) {
//                tabPanel.setSelectedComponent(singleScenSplitPanel);
//            }
//            tabPanel.remove(periodSummaryPanel);
//        }
//
//        if (tabPanel.indexOfComponent(segmentSummaryPanel) >= 0) {
//            if (tabPanel.getSelectedComponent() == segmentSummaryPanel) {
//                tabPanel.setSelectedComponent(singleScenSplitPanel);
//            }
//            tabPanel.remove(segmentSummaryPanel);
//        }

    }

    private void enableManagedLane() {
        if (!GPMLCB.getModel().equals(GPML_MODEL)) {
            GPMLCB.setModel(GPML_MODEL);
        }
        toolbox.enableML();
        menuBar.enableML();
        configGPMLDisplay();
    }

    private void disableManagedLane() {
        if (!GPMLCB.getModel().equals(GP_ONLY_MODEL)) {
            GPMLCB.setModel(GP_ONLY_MODEL);
        }
        toolbox.disableML();
        menuBar.disableML();
        configGPMLDisplay();
    }

    /**
     * Show table settings window
     */
    public static void showTableSettings() {
        TableSettingDialog settingDialog = new TableSettingDialog(mainWindow, mainWindow.tableDisplay.getSegIOTable());
        settingDialog.setVisible(true);

        mainWindow.update();
    }

    /**
     * Show graphic settings window
     */
    public static void showGraphicSettings() {
        GraphicSettingDialog settingDialog = new GraphicSettingDialog(mainWindow, mainWindow.graphicDisplay);//, mainWindow.tableDisplay.getSegIOTable());
        settingDialog.setVisible(true);

        mainWindow.update();
    }

    /**
     * Print log message in log display area in main window
     *
     * @param msg message to be print
     */
    public static void printLog(String msg) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String time = dateFormat.format(now);
        mainWindow.logText.append("\n" + time + " " + msg);
        mainWindow.logText.setCaretPosition(mainWindow.logText.getText().length());
    }

    /**
     * Update date main window title
     */
    private void updateTitle() {
        if (activeSeed == null) {
            this.setTitle("FREEVAL");
        } else {
            if (activeSeed.getValueString(CEConst.IDS_SEED_FILE_NAME) == null) {
                this.setTitle("FREEVAL-DSS (USER)  - Not Saved New File");
            } else {
                this.setTitle("FREEVAL-DSS (USER) - " + activeSeed.getValueString(CEConst.IDS_SEED_FILE_NAME));
            }
        }
    }

    /**
     * Show first analysis period data
     */
    @Override
    public void showFirstPeriod() {
        selectPeriod(0);
    }

    /**
     * Show last analysis period data
     */
    @Override
    public void showLastPeriod() {
        if (activeSeed != null) {
            selectPeriod(activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD) - 1);
        }
    }

    /**
     * Show previous analysis period data
     */
    @Override
    public void showPrevPeriod() {
        selectPeriod(activePeriod - 1);
    }

    /**
     * Show next analysis period data
     */
    @Override
    public void showNextPeriod() {
        selectPeriod(activePeriod + 1);
    }

    /**
     * Show a particular analysis period data
     *
     * @param period index of period selected
     */
    @Override
    public void selectPeriod(int period) {
        if (activeSeed == null) {
            period = -1;
        } else {
            activeSeed.singleRun(activeScen, activeATDM);
            if (period >= activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD)) {
                period = 0;
            } else {
                if (period < 0) {
                    period = activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD) - 1;
                }
            }
        }

        if (activePeriod != period || numPeriodChanged) {
            activePeriod = period;
            numPeriodChanged = false;
            tableDisplay.selectSeedScenATDMPeriod(activeSeed, activeScen, activeATDM, activePeriod);
            tableDisplaySegmentATM.selectSeedScenPeriod(activeSeed, activePeriod);
            graphicDisplay.selectSeedScenATDMPeriod(activeSeed, activeScen, activeATDM, activePeriod);

            //toolbox.selectPeriod(period);
            if (period < 0) {
                periodLabel.setText("A.P.");
                timeLabel.setText("-");
            } else {
                periodLabel.setText("A.P. " + (period + 1) + "/" + activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD));
                timeLabel.setText(activeSeed.getValueString(CEConst.IDS_PERIOD_TIME, 0, period));
            }

            if (activePeriod >= 0) {
                printLog("Analysis period " + (period + 1) + " selected");
            }
            userIOTableDisplay.updateTitle();
        }
    }

    /**
     * Update display when a particular segment is selected by graphic display
     *
     * @param seg segment index (start with 0)
     */
    @Override
    public void segmentSelectedByGraph(int seg) {
        tableDisplay.setHighlight(seg);
        tableDisplaySegmentATM.setHighlight(seg);
    }

    /**
     * Update display when a particular segment is selected by table display
     *
     * @param seg segment index (start with 0)
     */
    @Override
    public void segmentSelectedByTable(int seg) {
        graphicDisplay.setHighlight(seg);
        tableDisplaySegmentATM.setHighlight(seg);
        tableDisplay.setHighlight(seg);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ANALYSIS CONTROL FUNCTIONS">
    /**
     * Run single scenario
     */
    @Override
    public void runSingle() {
        printLog(runSeedSingle());
        update();
    }

    private String runSeedSingle() {
        if (activeSeed == null) {
            JOptionPane.showMessageDialog(this, "No seed is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return "Fail to run seed (no seed is selected)";
        }

        //timing starts
        long timingStart = new Date().getTime();

        activeSeed.singleRun(activeScen, activeATDM);

        //timing ends
        long timingEnd = new Date().getTime();

        //MARKEDFORDELETION
        //contourPanel.selectSeedScenATDM(activeSeed, activeScen, activeATDM);
        return ("Run seed finished ("
                + ((timingEnd - timingStart) > 1000
                        ? (timingEnd - timingStart) / 1000 + " s)"
                        : (timingEnd - timingStart) + " ms)"));
    }

//    /**
//     * Generate scenario
//     */
//    public void generateRL() {
//        if (activeSeed != null) {
//            activeSeed.singleRun(0, -1);
//
////            final ScenarioGeneratorDialog dlg = new ScenarioGeneratorDialog(activeSeed, this, true);
////            dlg.setLocationRelativeTo(this.getRootPane());
////            dlg.setVisible(true);
////            if (dlg.scenariosGenerated()) {
////                Scenario scenarios = dlg.getScenarios();
////                printLog(scenarios.size() + " scenarios fully generated");
////                printLog(activeSeed.setRLScenarios(scenarios, dlg.getScenarioInfoList()));
////
////                if (dlg.toggleRun) {
////                    runBatchRL();
////                }
////
////                updateSeed();
////                update();
////            } else {
////                printLog("Scenarios not generated");
////            }
////            dlg.dispose();
//        }
//    }
//    /**
//     * Delete all scenarios (including RL and ATDM)
//     */
//    public void deleteAllScen() {
//        if (activeSeed != null && activeSeed.getValueInt(CEConst.IDS_NUM_SCEN) > 0) {
//            int n = JOptionPane.showConfirmDialog(this,
//                    "Warning: Delete scenarios cannot be undone",
//                    "Warning",
//                    JOptionPane.OK_CANCEL_OPTION,
//                    JOptionPane.WARNING_MESSAGE);
//            if (n == JOptionPane.OK_OPTION) {
//                activeSeed.cleanScenarios();
//                updateSeed();
//                update();
//                printLog("All Scenarios Deleted");
//            }
//        }
//    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SEED MODIFICATION FUNCTIONS">
//    /**
//     * Add one or multiple segments
//     */
//    public void addSegment() {
//        if (activeSeed != null) {
//            JTextField indexText = new JTextField(3);
//            JTextField numText = new JTextField(3);
//
//            JPanel myPanel = new JPanel();
//            myPanel.add(new JLabel("Insert Before Segment"));
//            myPanel.add(indexText);
//            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
//            myPanel.add(new JLabel("Number of Segments"));
//            myPanel.add(numText);
//
//            int result = JOptionPane.showConfirmDialog(this, myPanel,
//                    "Add Segment", JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                try {
//                    printLog(activeSeed.addSegment(Integer.parseInt(indexText.getText()) - 1, Integer.parseInt(numText.getText())));
//                    update();
//                } catch (Exception e) {
//                    printLog("Fail to add segment.");
//                }
//            }
//        }
//    }
//
//    /**
//     * Delete one or multiple segments
//     */
//    public void delSegment() {
//        if (activeSeed != null) {
//            JTextField firstIndexText = new JTextField(3);
//            JTextField lastIndexTest = new JTextField(3);
//
//            JPanel myPanel = new JPanel();
//            myPanel.add(new JLabel("Delete from segment"));
//            myPanel.add(firstIndexText);
//            myPanel.add(new JLabel("to segment"));
//            myPanel.add(lastIndexTest);
//
//            int result = JOptionPane.showConfirmDialog(this, myPanel,
//                    "Delete Segment", JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                try {
//                    int n = JOptionPane.showConfirmDialog(this,
//                            "Warning: Delete segment cannot be undone",
//                            "Warning",
//                            JOptionPane.OK_CANCEL_OPTION,
//                            JOptionPane.WARNING_MESSAGE);
//                    if (n == JOptionPane.OK_OPTION) {
//                        printLog(activeSeed.delSegment(Integer.parseInt(firstIndexText.getText()) - 1, Integer.parseInt(lastIndexTest.getText()) - 1));
//                        update();
//                    }
//                } catch (Exception e) {
//                    printLog("Fail to delete segment.");
//                }
//            }
//        }
//    }
//
//    /**
//     * Add one or multiple periods
//     */
//    public void addPeriod() {
//        if (activeSeed != null) {
//            JTextField numText = new JTextField(3);
//            JComboBox positionCombo = new JComboBox();
//            positionCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"beginning", "end"}));
//
//            JPanel myPanel = new JPanel();
//            myPanel.add(new JLabel("Add"));
//            myPanel.add(numText);
//            myPanel.add(new JLabel("analysis periods at the"));
//            myPanel.add(positionCombo);
//
//            int result = JOptionPane.showConfirmDialog(this, myPanel,
//                    "Add Period", JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                try {
//
//                    printLog(activeSeed.addPeriod(Integer.parseInt(numText.getText()), positionCombo.getSelectedIndex() == 0));
//                    numPeriodChanged = true;
//                    update();
//
//                } catch (Exception e) {
//                    printLog("Fail to add period.");
//                }
//            }
//        }
//    }
//
//    /**
//     * Delete one or multiple periods
//     */
//    public void delPeriod() {
//        if (activeSeed != null) {
//            JTextField numText = new JTextField(3);
//            JComboBox positionCombo = new JComboBox();
//            positionCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"beginning", "end"}));
//
//            JPanel myPanel = new JPanel();
//            myPanel.add(new JLabel("Delete"));
//            myPanel.add(numText);
//            myPanel.add(new JLabel("analysis periods from the"));
//            myPanel.add(positionCombo);
//
//            int result = JOptionPane.showConfirmDialog(this, myPanel,
//                    "Delete Period", JOptionPane.OK_CANCEL_OPTION);
//            if (result == JOptionPane.OK_OPTION) {
//                try {
//                    int n = JOptionPane.showConfirmDialog(this,
//                            "Warning: Delete period cannot be undone",
//                            "Warning",
//                            JOptionPane.OK_CANCEL_OPTION,
//                            JOptionPane.WARNING_MESSAGE);
//                    if (n == JOptionPane.OK_OPTION) {
//                        printLog(activeSeed.delPeriod(Integer.parseInt(numText.getText()), positionCombo.getSelectedIndex() == 0));
//                        numPeriodChanged = true;
//                        update();
//                    }
//                } catch (Exception e) {
//                    printLog("Fail to Delete period.");
//                }
//            }
//        }
//    }
//
//    public void seedDataChanged() {
//        navigator.updateSeed(activeSeed);
//        update();
//    }
    /**
     * Update display, data and/or setting
     */
    private void update() {
        if (activeSeed != null) {
            setNonNullSeed();
            toolbox.setNonNullSeed();
            menuBar.setNonNullSeed();

            if (!hasOutput()) {
                runSeedSingle();
            }

            enableOutput();
            if (isShowingInput) {
                showInput();
            } else {
                showOutput();
            }

            if (activeSeed.isManagedLaneUsed()) {
                enableManagedLane();
            } else {
                disableManagedLane();
            }

            if (activeSeed.getValueInt(CEConst.IDS_ATDM_SET_NUM) > 0) {
                toolbox.turnOffML();
                toolbox.disableML();
                menuBar.turnOffML();
                menuBar.disableML();
            } else {
                toolbox.enableML();
                menuBar.enableML();
                if (activeSeed.isManagedLaneUsed()) {
                    toolbox.turnOnML();
                    menuBar.turnOnML();
                } else {
                    toolbox.turnOffML();
                    menuBar.turnOffML();
                }
            }
        } else {
            disableOutput();
            disableManagedLane();
            showInput();
            setNullSeed();
            toolbox.setNullSeed();
            menuBar.setNullSeed();
        }

        selectPeriod(activePeriod);
        tableDisplay.update();
        tableDisplaySegmentATM.update();
        graphicDisplay.update();
        updateTitle();
        //comparePanel.updateList();

        //TODO integrate and add more updates here
    }

    /**
     * @deprecated
     */
    private void updateSeed() {
        navigator.updateSeed(activeSeed);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="OTHER MENU ITEM FUNCTIONS">
    /**
     * Show local JavaDoc files in system default browser
     */
    @Override
    public void showJavaDoc() {
        try {
            Desktop.getDesktop().browse(new File("javadoc/index.html").toURI());
        } catch (Exception e) {
            printLog("Cannot open browser " + e.toString());
        }
    }

    /**
     * Show FREEVAL version and contact information
     */
    @Override
    public void showAbout() {
        AboutDialog aboutDialog = new AboutDialog(this);
        aboutDialog.setVisible(true);
    }
//
//    /**
//     * Add a scenario to compare
//     *
//     * @param seed seed
//     * @param scen scenario index
//     * @param atdm atdm index
//     * @param name scenario name
//     */
//    public void addScenarioToCompare(Seed seed, int scen, int atdm, String name) {
//        if (!seed.hasValidOutput(scen, atdm)) {
//            seed.singleRun(scen, atdm);
//        }
//        //MARKEDFORDELETION
//        // comparePanel.addScenarioToCompare(seed, scen, atdm, name);
//    }
    // </editor-fold>
    // </editor-fold>

    public ATMUpdater getATMUpdater() {
        return this.atmUpdater;
    }

    private void proceedOnly() {
        showNextPeriod();
        dssProgress += 1;
    }

    public void updateButtons() {
        if (activePeriod == 0) {
            reviewPreviousPeriodButton.setEnabled(false);
            if (activePeriod != dssProgress) {
                proceedOnlyButton.setText("Review Next Period");
                takeActionButton.setText("Return to Active Period");
            } else {
                proceedOnlyButton.setText("Proceed With No Action");
                takeActionButton.setText("Take Action and Proceed");
            }
        } else if (activePeriod == activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD) - 1 && activePeriod == dssProgress) {
            reviewPreviousPeriodButton.setEnabled(true);
            proceedOnlyButton.setText("Generate Summary");
            if (this.resetReady) {
                takeActionButton.setText("Run Again");
                takeActionButton.setEnabled(true);
            } else {
                takeActionButton.setEnabled(false);
            }

        } else {
            takeActionButton.setEnabled(true);
            reviewPreviousPeriodButton.setEnabled(true);
            if (activePeriod != dssProgress) {
                proceedOnlyButton.setText("Review Next Period");
                takeActionButton.setText("Return to Active Period");
            } else {
                proceedOnlyButton.setText("Proceed With No Action");
                takeActionButton.setText("Take Action and Proceed");
            }
        }
    }

    private void updateEditLock() {
        if (activePeriod != dssProgress) {
            userIOTableDisplay.setEditLock(true);
        } else {
            userIOTableDisplay.setEditLock(false);
        }
    }

    private void generateSummary() {
        activeSeed.singleRun(activeScen, -1);
        activeSeed.singleRun(activeScen, activeATDM);
        ATMSummaryDialog atdmSetSummaryDialog = new ATMSummaryDialog(activeSeed, activeATDM, true, this);
        atdmSetSummaryDialog.setVisible(true);

        resetReady = true;
        updateButtons();
    }

    private void resetATM() {
        //activePeriod = 0;
        //dssProgress = 0;

        // Save periodATM and activeATDM
        completedRunsPeriodATM.add(periodATM.clone());
        completedRunsATDMScen.add(activeATM);

        // Create new of each
        for (int per = 0; per < periodATM.length; per++) {
            periodATM[per] = new PeriodATM(activeSeed, per, userParams.atm);
        }
        activeATM = new ATDMScenario(activeSeed.getValueInt(CEConst.IDS_NUM_SEGMENT), activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD));
        HashMap<Integer, ATDMScenario[]> atmHolder = new HashMap();
        atmHolder.put(1, new ATDMScenario[]{activeATM, null});
        activeSeed.addATDMSet(atmHolder);
        activeATDM++;
        // Reset window
        dssProgress = 0;
        //selectScenSeed
        selectPeriod(0);
    }

    @Override
    public UserLevelParameterSet getUserLevelParameters() {
        return userParams;
    }

//    public void showATDMSummary() {
//        if (checkATDMHasFullResult()) {
//            SummaryTypeSelectionDialog summaryTypeSelectionDialog = new SummaryTypeSelectionDialog(this, activeSeed);
//            summaryTypeSelectionDialog.setVisible(true);
//
//            if (summaryTypeSelectionDialog.getReturnStatus() == SummaryTypeSelectionDialog.RET_OK) {
//                int atdmIndex = summaryTypeSelectionDialog.getAtdmSetSelected();
//                ATDMSetSummaryDialog atdmSetSummaryDialog;
//                switch (summaryTypeSelectionDialog.getAtdmSummaryType()) {
//                    case SummaryTypeSelectionDialog.ATDM_SINGLE_SET_SET_ONLY:
//                        atdmSetSummaryDialog = new ATDMSetSummaryDialog(activeSeed, atdmIndex, true, this);
//                        atdmSetSummaryDialog.setVisible(true);
//                        break;
//                    case SummaryTypeSelectionDialog.ATDM_SINGLE_SET_ALL:
//                        runBatchRL();
//                        atdmSetSummaryDialog = new ATDMSetSummaryDialog(activeSeed, atdmIndex, false, this);
//                        atdmSetSummaryDialog.setVisible(true);
//                        break;
//                }
//            }
//        }
//    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        navigator = new GUI.major.Navigator();
        toolbox = new Toolbox.DSS.GUI.major.ToolboxDSS();
        jScrollPane2 = new javax.swing.JScrollPane();
        toolboxSplitPanel = new javax.swing.JSplitPane();
        navigatorSplitPanel = new javax.swing.JSplitPane();
        logSplitPanel = new javax.swing.JSplitPane();
        logScrollPanel = new javax.swing.JScrollPane();
        logText = new javax.swing.JTextArea();
        tableDisplayOptionPanel = new javax.swing.JPanel();
        inOutCB = new javax.swing.JComboBox();
        GPMLCB = new javax.swing.JComboBox();
        APPanel = new javax.swing.JPanel();
        firstButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        lastButton = new javax.swing.JButton();
        jumpToButton = new javax.swing.JButton();
        jumpText = new javax.swing.JTextField();
        timeLabel = new javax.swing.JLabel();
        periodLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        showInputButton = new javax.swing.JToggleButton();
        showOutputButton = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        reviewPreviousPeriodButton = new javax.swing.JButton();
        proceedOnlyButton = new javax.swing.JButton();
        takeActionButton = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        userIOTableDisplay = new Toolbox.DSS.GUI.major.UserIOTableDisplay();
        jPanel4 = new javax.swing.JPanel();
        singleScenSplitPanel = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        graphicDisplay = new GUI.major.GraphicDisplay();
        tableDisplay = new GUI.major.TableDisplay();
        menuBar = new GUI.major.MenuBar();

        jScrollPane2.setBorder(null);

        toolboxSplitPanel.setBorder(null);
        toolboxSplitPanel.setDividerLocation(48);
        toolboxSplitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        navigatorSplitPanel.setBorder(null);
        navigatorSplitPanel.setDividerLocation(280);
        navigatorSplitPanel.setResizeWeight(0.15);
        navigatorSplitPanel.setToolTipText("");
        toolboxSplitPanel.setRightComponent(navigatorSplitPanel);

        logSplitPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        logSplitPanel.setDividerLocation(600);
        logSplitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        logSplitPanel.setResizeWeight(0.85);

        logScrollPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Log"));

        logText.setLineWrap(true);
        logText.setWrapStyleWord(true);
        logScrollPanel.setViewportView(logText);

        tableDisplayOptionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Table Display Options"));
        tableDisplayOptionPanel.setLayout(new java.awt.GridLayout(1, 2));

        inOutCB.setBackground(new java.awt.Color(255, 255, 153));
        inOutCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Input" }));
        inOutCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inOutCBActionPerformed(evt);
            }
        });
        tableDisplayOptionPanel.add(inOutCB);

        GPMLCB.setBackground(new java.awt.Color(255, 255, 153));
        GPMLCB.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "GP Only" }));
        GPMLCB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GPMLCBActionPerformed(evt);
            }
        });
        tableDisplayOptionPanel.add(GPMLCB);

        APPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Analysis Period (A.P.) Control"));
        APPanel.setLayout(new java.awt.GridLayout(1, 7));

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
        jumpText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jumpTextKeyPressed(evt);
            }
        });
        APPanel.add(jumpText);

        timeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timeLabel.setText("-");

        periodLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        periodLabel.setText("A.P.");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel2.setMaximumSize(new java.awt.Dimension(791, 42));

        showInputButton.setText("Input");
        showInputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInputButtonActionPerformed(evt);
            }
        });

        showOutputButton.setText("Output");
        showOutputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showOutputButtonActionPerformed(evt);
            }
        });

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));

        reviewPreviousPeriodButton.setText("Review Previous Period");
        reviewPreviousPeriodButton.setEnabled(false);
        reviewPreviousPeriodButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reviewPreviousPeriodButtonActionPerformed(evt);
            }
        });
        jPanel3.add(reviewPreviousPeriodButton);

        proceedOnlyButton.setText("Proceed With No Action");
        proceedOnlyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                proceedOnlyButtonActionPerformed(evt);
            }
        });
        jPanel3.add(proceedOnlyButton);

        takeActionButton.setText("Take Action and Proceed");
        takeActionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takeActionButtonActionPerformed(evt);
            }
        });
        jPanel3.add(takeActionButton);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(showInputButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(showOutputButton)
                .addGap(242, 242, 242)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(showOutputButton)
                .addComponent(showInputButton))
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jSplitPane2.setDividerLocation(390);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        userIOTableDisplay.setPreferredSize(new java.awt.Dimension(750, 503));
        jSplitPane2.setRightComponent(userIOTableDisplay);

        singleScenSplitPanel.setBorder(null);
        singleScenSplitPanel.setDividerLocation(160);
        singleScenSplitPanel.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        singleScenSplitPanel.setResizeWeight(0.35);

        javax.swing.GroupLayout graphicDisplayLayout = new javax.swing.GroupLayout(graphicDisplay);
        graphicDisplay.setLayout(graphicDisplayLayout);
        graphicDisplayLayout.setHorizontalGroup(
            graphicDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        graphicDisplayLayout.setVerticalGroup(
            graphicDisplayLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(graphicDisplay);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        singleScenSplitPanel.setLeftComponent(jPanel1);
        singleScenSplitPanel.setRightComponent(tableDisplay);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1057, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addComponent(singleScenSplitPanel)
                    .addGap(4, 4, 4)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(singleScenSplitPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jSplitPane2.setLeftComponent(jPanel4);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSplitPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 704, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        for (Seed seed : seedList) {
//            int result = JOptionPane.showConfirmDialog(this, "Do you want to save the changes you made to seed \"" + seed.getValueString(CEConst.IDS_PROJECT_NAME) + "\"?",
//                    "Save Files", JOptionPane.YES_NO_OPTION);
//            if (result == JOptionPane.YES_OPTION) {
//                saveSeed(seed);
//            }
//        }
//        ConfigIO.saveSeedListToConfig(seedList);

        mainWindowStart.setVisible(true);
    }//GEN-LAST:event_formWindowClosing

    private void showInputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInputButtonActionPerformed
        showInput();
    }//GEN-LAST:event_showInputButtonActionPerformed

    private void showOutputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showOutputButtonActionPerformed
        showOutput();
    }//GEN-LAST:event_showOutputButtonActionPerformed

    private void firstButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_firstButtonActionPerformed
        showFirstPeriod();
    }//GEN-LAST:event_firstButtonActionPerformed

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        showPrevPeriod();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        showNextPeriod();
    }//GEN-LAST:event_nextButtonActionPerformed

    private void lastButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lastButtonActionPerformed
        showLastPeriod();
    }//GEN-LAST:event_lastButtonActionPerformed

    private void jumpToButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jumpToButtonActionPerformed
        try {
            selectPeriod(Integer.parseInt(jumpText.getText()) - 1);
            jumpText.setText("");
        } catch (Exception e) {
            printLog("Invalid period");
            jumpText.setText("");
        }
    }//GEN-LAST:event_jumpToButtonActionPerformed

    private void inOutCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inOutCBActionPerformed
        switch (inOutCB.getSelectedIndex()) {
            case 0:
                showInput();
                break;
            case 1:
                showOutput();
                break;
        }
    }//GEN-LAST:event_inOutCBActionPerformed

    private void GPMLCBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GPMLCBActionPerformed
        configGPMLDisplay();
    }//GEN-LAST:event_GPMLCBActionPerformed

    private void jumpTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jumpTextKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jumpToButtonActionPerformed(null);
        }
    }//GEN-LAST:event_jumpTextKeyPressed

    private void proceedOnlyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_proceedOnlyButtonActionPerformed
        if (activePeriod < dssProgress) {
            showNextPeriod();
        } else if (activePeriod == dssProgress && activePeriod < activeSeed.getValueInt(CEConst.IDS_NUM_PERIOD) - 1) {
            if (validateATM(0)) {
                proceedOnly();
            }
        } else {
            generateSummary();
        }
        updateButtons();
        updateEditLock();
    }//GEN-LAST:event_proceedOnlyButtonActionPerformed

    private void takeActionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takeActionButtonActionPerformed

        if (activePeriod == dssProgress) {
            if (this.resetReady) {
                resetReady = false;
                resetATM();
            } else {
                if (validateATM(1)) {
                    applyATM();
                }
            }
        } else {
            selectPeriod(dssProgress);
        }
        updateButtons();
        updateEditLock();
    }//GEN-LAST:event_takeActionButtonActionPerformed

    private void reviewPreviousPeriodButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reviewPreviousPeriodButtonActionPerformed
        if (activePeriod > 0) {
            showPrevPeriod();
        }
        updateButtons();
        updateEditLock();
    }//GEN-LAST:event_reviewPreviousPeriodButtonActionPerformed

    private void configGPMLDisplay() {
        switch (GPMLCB.getSelectedIndex()) {
            case 0:
                showGPOnly();
                break;
            case 1:
                showMLOnly();
                break;
            case 2:
                showGPML();
                break;
        }
    }

    @Override
    public void showGPOnly() {
        tableDisplay.showGPOnly();
    }

    @Override
    public void showMLOnly() {
        tableDisplay.showMLOnly();
    }

    @Override
    public void showGPML() {
        tableDisplay.showGPML();
    }
//
//    public void toggleManagedLane() {
//        if (activeSeed != null) {
//            if (activeSeed.isManagedLaneUsed()) {
//                int result = JOptionPane.showConfirmDialog(this,
//                        "Warning: Disable managed lanes will delete all existing managed lanes data. This cannot be undone.",
//                        "Warning",
//                        JOptionPane.OK_CANCEL_OPTION,
//                        JOptionPane.WARNING_MESSAGE);
//                if (result == JOptionPane.OK_OPTION) {
//                    activeSeed.setManagedLaneUsed(false);
//                }
//            } else {
//                activeSeed.setManagedLaneUsed(true);
//            }
//            update();
//        }
//    }
    // <editor-fold defaultstate="collapsed" desc="setter and getters">

    /**
     * Getter for active seed
     *
     * @return active seed instance
     */
    @Override
    public Seed getActiveSeed() {
        return activeSeed;
    }

    /**
     * Getter for active scenario index
     *
     * @return active scenario index
     */
    @Override
    public int getActiveScen() {
        return activeScen;
    }

    /**
     * Getter for active period index
     *
     * @return active period index (start with 0)
     */
    @Override
    public int getActivePeriod() {
        return activePeriod;
    }

    /**
     * Getter for opened seed list
     *
     * @return opened seed list
     */
    @Override
    public ArrayList<Seed> getSeedList() {
        return seedList;
    }

    /**
     * Getter for table font
     *
     * @return table font
     */
    public static Font getTableFont() {
        return tableFont;
    }

    /**
     * Setter for table font
     *
     * @param newTableFont new table font
     */
    @Override
    public void setTableFont(Font newTableFont) {
        tableFont = newTableFont;
        tableDisplay.setTableFont(newTableFont);
        tableDisplaySegmentATM.setTableFont(newTableFont);
    }

    public boolean isOutputEnabled() {
        return isOutputEnabled;
    }
    // </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel APPanel;
    private javax.swing.JComboBox GPMLCB;
    private javax.swing.JButton firstButton;
    private GUI.major.GraphicDisplay graphicDisplay;
    private javax.swing.JComboBox inOutCB;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextField jumpText;
    private javax.swing.JButton jumpToButton;
    private javax.swing.JButton lastButton;
    private javax.swing.JScrollPane logScrollPanel;
    private javax.swing.JSplitPane logSplitPanel;
    private javax.swing.JTextArea logText;
    private GUI.major.MenuBar menuBar;
    private GUI.major.Navigator navigator;
    private javax.swing.JSplitPane navigatorSplitPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel periodLabel;
    private javax.swing.JButton previousButton;
    private javax.swing.JButton proceedOnlyButton;
    private javax.swing.JButton reviewPreviousPeriodButton;
    private javax.swing.JToggleButton showInputButton;
    private javax.swing.JToggleButton showOutputButton;
    private javax.swing.JSplitPane singleScenSplitPanel;
    public GUI.major.TableDisplay tableDisplay;
    private javax.swing.JPanel tableDisplayOptionPanel;
    private javax.swing.JButton takeActionButton;
    private javax.swing.JLabel timeLabel;
    private Toolbox.DSS.GUI.major.ToolboxDSS toolbox;
    private javax.swing.JSplitPane toolboxSplitPanel;
    private Toolbox.DSS.GUI.major.UserIOTableDisplay userIOTableDisplay;
    // End of variables declaration//GEN-END:variables
}
