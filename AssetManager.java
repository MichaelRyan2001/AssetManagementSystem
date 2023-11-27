import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class AssetManager {
    //manages the connection to the database
    private Database databaseConnection;
    //stores loaded assets
    private Map<Integer, Asset> loadedAssets;
    //stores asset groups by their type
    private Map<String, Group> typeGroups;
    //read image data from the given path
    private Function<String, byte[]> readImageDataMethod;

    //initializes the AssetManager
    public AssetManager(Database databaseConnection, Function<String, byte[]> readImageDataMethod) {
        this.databaseConnection = databaseConnection;
        this.loadedAssets = new HashMap<>();
        this.typeGroups = new HashMap<>();
        this.readImageDataMethod = readImageDataMethod;

    }

    //add asset to the db
    public void addAsset(int assetID, String name, String type, byte[] imageData, String imagePath){
        if(databaseConnection != null) {
            String base64ImageData = Base64.getEncoder().encodeToString(imageData);

            databaseConnection.addAsset(assetID, name, type, imageData, imagePath);
        }
    }

    //load asset from the db
    public Asset loadAsset(int assetID) {
        //retrieve the asset data
        Map<String, String> assetData = databaseConnection.getAssetData(assetID);

        if (assetData != null) {
            System.out.println("Loaded asset data: " + assetData);

            String type = assetData.get("type");

            String imagePath = assetData.get("image_path");

            if(imagePath != null) {
                System.out.println("Image Path = " + imagePath);
                //reads the image data through through the method
                byte[] imageData = readImageDataMethod.apply(imagePath);

                if (imageData != null) {
                    //creats the asset object and then stores it in loaded assets
                    Asset asset = new Asset(
                            assetID,
                            assetData.get("name"),
                            assetData.get("type"),
                            imageData
                    );
                    loadedAssets.put(assetID, asset);
                    return asset;
                } else {
                    System.out.println("Failed to load image data for path: " + imagePath);
                }
            } else {
                System.out.println("Image path is null");
            }
        } else {
            System.out.println("Failed to retrive asset data for ID: " + assetID);
        }
        return null;
    }

    public void unloadGroup(int groupID) {

    }
}
