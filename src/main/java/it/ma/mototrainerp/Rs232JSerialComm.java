package it.ma.mototrainerp;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortPacketListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Rs232JSerialComm {

    private final int PACKET_SIZE = 11;
    private SerialPort serialPort;
    private final PacketListener listener = new PacketListener();
    private final static Log LOGGER = LogFactory.getLog(Rs232JSerialComm.class);

    int serialPortNumber;

    Rs232JSerialComm() {
        this(0);
    }

    Rs232JSerialComm(int portNumber) {
        this.serialPortNumber = portNumber;
    }

    boolean open() {
        SerialPort[] sp;
        try {
            sp = SerialPort.getCommPorts();
            serialPort = sp[serialPortNumber];
        } catch (Exception ex) {
            LOGGER.error("Can't get RS232 ports description", ex);
            return false;
        }
        LOGGER.debug("Got RS232 port " + serialPort.toString());
        if (!serialPort.openPort()) {
            LOGGER.error("Can't open RS232");
            return false;
        }
        serialPort.addDataListener(listener);
        LOGGER.info("Opened RS232 port and added listener");
        return true;
    }

    void close() {
        if (serialPort!=null) {
            serialPort.removeDataListener();
            serialPort.closePort();
        }
        LOGGER.debug(((serialPort==null)?"Even if is null...":"")+"Close the RS232 Port");
    }

    private final class PacketListener implements SerialPortPacketListener {
        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public int getPacketSize() {
            return PACKET_SIZE;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte[] newData = event.getReceivedData();
            final byte[] lineBuf = new byte[9];  //da 0xF0 a 0xF7 non compresi, quindi ne basta 9
            if (newData.length != PACKET_SIZE){
                LOGGER.error("il pacchetto non è lungo :"+PACKET_SIZE);
                return;
            }
            for (int i = 1; i < lineBuf.length; i++)
                lineBuf[i] = newData[i - 1];    //rimuovo F0 & F7
            ArduinoData.getInstance().setData(lineBuf);
            LOGGER.debug("Received data of size: " + newData.length);
            for (int i = 0; i < newData.length; ++i)
                LOGGER.debug((char) newData[i]);
            LOGGER.debug("\n");
        }
    }

}
