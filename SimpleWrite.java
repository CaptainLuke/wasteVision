import java.io.*;
import java.util.*;
import gnu.io.*;

public class SimpleWrite {

  static Enumeration portList;
  static CommPortIdentifier portId;
  static String messageString;
  static SerialPort serialPort;
  static OutputStream outputStream;

  public static void serialOutput(ArrayList<Integer> positions) {
    portList = CommPortIdentifier.getPortIdentifiers();


    while (portList.hasMoreElements()) {

      portId = (CommPortIdentifier) portList.nextElement();
      if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

        if (portId.getName().equals("COM5")) {

          try {
            serialPort = (SerialPort) portId.open("SimpleWriteApp", 2000);
            Thread.sleep(4000);

            outputStream = serialPort.getOutputStream();

            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

            for (int x : positions) {
              messageString = String.valueOf(x);
              outputStream.write(messageString.getBytes());
              try {
                Thread.sleep(2000);
              } catch (InterruptedException ex) {
              }
            }

            outputStream.close();
            serialPort.close();
          } catch (IOException e) {
            System.out.println("err3");
          } catch (PortInUseException e) {
            System.out.println("err");
          } catch (UnsupportedCommOperationException e) {
            System.out.println("err2");
          } catch (InterruptedException e) {
          }
        }
      }
    }
  }
}

