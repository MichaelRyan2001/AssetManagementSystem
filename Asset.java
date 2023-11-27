import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Asset {
    private int assetID;
    private String assetName;
    private String assetType;
    private byte[] imageData;

    //2D asset variables

    //constructor for creating an Asset with its data
    public Asset(int assetID, String assetName, String assetType, byte[] imageData) {
        this.assetID = assetID;
        this.assetName = assetName;
        this.assetType = assetType;
        this.imageData = imageData;
    }
    //Getters and setters (if required)

    //getter method to retrieve the image data
    public BufferedImage getImageData(){
        try {
            //convert the binary image data into a BufferedImage
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;//return null if there's an error
        }
    }

    public void releaseImageData(){
        this.imageData = null;
    }
}