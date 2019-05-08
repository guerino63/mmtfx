module mmtfx.main {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires org.apache.commons.codec;
    requires commons.logging;
    requires log4j;
//    requires purejavacomm;
    requires com.fazecast.jSerialComm;
    opens it.ma.mototrainerp to javafx.fxml;
//    opens styles to javafx.fxml;
    exports it.ma.mototrainerp;
    exports it.ma.tables;
}