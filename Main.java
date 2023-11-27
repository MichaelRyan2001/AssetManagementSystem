import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //AssetManager setup
        AssetManager assetManager = setupAssetManager();

        //data defined for the four assets
        String assetName1 = "Mario";
        String assetType1 = "Character";
        int assetID1 = 1;
        byte[] imageData1 = readImageData("/Sprites/Mario.png");

        String assetName2 = "Luigi";
        String assetType2 = "Character";
        int assetID2 = 2;
        byte[] imageData2 = readImageData("/Sprites/Luigi.png");

        String assetName3 = "Bowser";
        String assetType3 = "Enemy";
        int assetID3 = 3;
        byte[] imageData3 = readImageData("/Sprites/Bowser.png");

        String assetName4 = "Koopa Troopa";
        String assetType4 = "Enemy";
        int assetID4 = 4;
        byte[] imageData4 = readImageData("/Sprites/KoopaTroopa.png");

        //if the assetManager is set up add the assets into the database
        if(assetManager != null){
            assetManager.addAsset(assetID1, assetName1, assetType1, imageData1,"/Sprites/Mario.png");
            assetManager.addAsset(assetID2, assetName2, assetType2, imageData2, "/Sprites/Luigi.png");
            assetManager.addAsset(assetID3, assetName3, assetType3, imageData3, "/Sprites/Bowser.png");
            assetManager.addAsset(assetID4, assetName4, assetType4, imageData4, "/Sprites/KoopaTroopa.png");
        }
    }

    private static AssetManager setupAssetManager() {
        //JDBC connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/assetmanagementdb";
        String username = "root";
        String password = "iamroot";

        try {
            //establish database connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            //create database object to use with the database
            Database database = new Database(connection);

            //this segment was for defining a list of enemies in a group
            List<Integer> enemyAssetIDs = Arrays.asList(3, 4);
            try {
                database.addGroup(1, "Enemy Group", enemyAssetIDs);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //create and return an AssetManager
            return new AssetManager(database, Main::readImageData);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //read binary image data from files
    public static byte[] readImageData(String imagePath) {
        try {
            //checks the resource file
            InputStream inputStream = Main.class.getResourceAsStream(imagePath);
            if(inputStream != null) {
                return inputStream.readAllBytes();
            } else {
                System.out.println("Resource not found " + imagePath);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
