import java.util.ArrayList;
import java.util.List;

public class Group {
    private static int groupIDCounter = 1;

    private int groupID;
    private String groupName;
    private List<Asset> assets;

    public List<Asset> getAssets(){
        return assets;
    }

    public Group(int groupID, String groupName){
        this.groupID = groupID;
        this.groupName = groupName;
        this.assets = new ArrayList<>();
    }

    public String getGroupName(){
        return groupName;
    }

    public int getGroupID() {
        return this.groupID;
    }

    public String generateGroupName(String type) {
        if("Enemy".equalsIgnoreCase(type)) {
            return "Enemy Group";
        } else if ("Ally".equalsIgnoreCase(type)) {
            return "Ally Group";
        } else {
            return "Other Group";
        }
    }

    public void addAsset(Asset asset){
        assets.add(asset);
    }

    //Getters and setters (if required)
}