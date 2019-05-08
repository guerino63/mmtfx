package it.ma.mototrainerp;

import javafx.application.Application;
import javafx.stage.Stage;

import static it.ma.mototrainerp.EStage.SETUP;
import static it.ma.mototrainerp.EStage.VIDEO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Mototrainer extends Application {
//    static private Rs232_PureJavaComm RS232PureJavaComm;
    static private Rs232JSerialComm rs232;
    private final static Log LOGGER = LogFactory.getLog(Mototrainer.class);
    
    @Override
    public void start(Stage stage) {
        Prop.getInstance().oneShotLoadProperties(); //*** LASCIARE in testa!!!
        //Platform.isFxApplicationThread() is true
        /**
         * Caching...
         */
//        RS232PureJavaComm = new Rs232_PureJavaComm();
        rs232 = new Rs232JSerialComm();
        StageManager stageManager = new StageManager(Prop.Desc.NUMERO_SCHERMO.getValueInt()) {
            @Override
            public void postInit() {
                /**
                 * After loaded all Stages, we can cross binds stages
                 * Platform.isFxApplicati onThread() is true
                 */
                ((FXMLVideoController) StageManager.getController(VIDEO)).postInitialize();
                ((FXMLSetupController) StageManager.getController(SETUP)).postInitialize();
//                ((FXMLClipsController)StageManager.getController(CLIP)).postInitialize();
            }
        };
        
        //Provo ad aprire seriale
//        LOGGER.info(RS232PureJavaComm.open()? "RS232PureJavaComm Opened!":"RS232PureJavaComm Error:Not opened!");
        rs232.open();

        stageManager.init();
        StageManager.showStage(SETUP);

//        Utility.msgDebug(RS232PureJavaComm.open()? "RS232PureJavaComm Opened!":"RS232PureJavaComm Error:Not opened!");
//        StageManager.showStage(SETUP);
//        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (Event event) -> {
//            //***Attento! un throw od un consume event, e si rischia di non uscire più...
//            Utility.msgDebug("WindowEvent.WINDOW_CLOSE_REQUEST-->> ...exiting");
//        });
//
//        Utility.msgDebug("*IP : "+Host.getInstance().getIpAdress()+", MACADDRESS : "
//        +Host.getInstance().getMacAddress());
    }

    @Override
    public void stop() throws Exception {
        Prop.saveProperties();
        rs232.close();
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
