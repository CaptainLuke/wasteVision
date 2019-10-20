import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.BorderLayout;

public class Picture {
  public static void takePicture(String file) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    VideoCapture camera = new VideoCapture(0);
    Mat frame = new Mat();

    JFrame frame2 = new JFrame("Image Viewer");
    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    if (!camera.isOpened()) {
      System.out.println("Error");
    } else {
      while (true) {
        if (camera.read(frame)) {
          System.out.println("Frame Obtained");
          System.out.println("Captured Frame Width " + frame.width() + " Height " + frame.height());
          Imgcodecs.imwrite(file, frame);
          break;
        }
      }
    }
    camera.release();

    // Create an empty image in matching format
    BufferedImage empty =
        new BufferedImage(frame.width(), frame.height(), BufferedImage.TYPE_3BYTE_BGR);

    // Get the BufferedImage's backing array and copy the pixels directly into it
    byte[] data = ((DataBufferByte) empty.getRaster().getDataBuffer()).getData();
    frame.get(0, 0, data);

    ImageIcon img = new ImageIcon(empty);
    JLabel lbl = new JLabel(img);

    frame2.getContentPane().add(lbl, BorderLayout.CENTER);
    frame2.pack();
    frame2.setLocationRelativeTo(null);
    frame2.setVisible(true);
  }

}
