// Imports the Google Cloud client library

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class QuickstartSample {

  public static String[] type = {"compost", "recycling", "trash"};
  public static ArrayList<Integer> positions = new ArrayList<Integer>();

  public static void main(String... args) throws Exception {
    // Instantiates a client
    int x = 0;
    while (x < 3) {
      try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

        // The path to the image file to annotate
        Picture.takePicture("camera" + x + ".jpg");
        String fileName = "camera" + x + ".jpg";
        ArrayList<String> imageClass = new ArrayList<String>();
        ArrayList<String> imageClassR = new ArrayList<String>();

        // Reads the image file into memory
        Path path = Paths.get(fileName);
        byte[] data = Files.readAllBytes(path);
        ByteString imgBytes = ByteString.copyFrom(data);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
            AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        for (AnnotateImageResponse res : responses) {
          if (res.hasError()) {
            System.out.printf("Error: %s\n", res.getError().getMessage());
            return;
          }

          for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
            annotation.getAllFields().forEach((k, v) -> imageClass.add(v.toString()));
          }
        }

        System.out.println();

        for (int i = 1; i < imageClass.size(); i += 4) {
          System.out.println(imageClass.get(i));
          imageClassR.add(imageClass.get(i));
        }

        pAdder(imageClassR);
      }
      x++;
    }
    serialWriter();

  }

  public static void pAdder(ArrayList<String> imageClassR) {
    boolean food = false;
    boolean recycling = false;

    for (String x : imageClassR) {
      x = x.toLowerCase();
      if (x.equals("food") || x.equals("fruit") || x.equals("vegtable"))
        food = true;
    }

    for (String x : imageClassR) {
      x = x.toLowerCase();
      if (x.equals("cardboard") || x.equals("glass") || x.equals("plastic") || x.equals("drink"))
        recycling = true;
    }

    int position;

    if (food)
      position = 1;
    else if (recycling)
      position = 2;
    else
      position = 3;

    System.out.println();
    System.out.println(type[position - 1]);
    System.out.println("----------");
    System.out.println();
    positions.add(position);
  }

  public static void serialWriter() {

    System.out.println(type[positions.get(0) - 1] + ", " + type[positions.get(1) - 1] + ", "
        + type[positions.get(2) - 1]);
    SimpleWrite.serialOutput(positions);
  }

}
