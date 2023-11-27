
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    //variables
    private Connection connection;
    private List<Asset> loadedAssets;

    //constructor for the database with an existing connection
    public Database(Connection connection) {
        this.connection = connection;
    }

    //constructor to create a new database connection
    public Database(String jdbcUrl, String username, String password) {
        try{
            //establishes a new connection to the database
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Adds a new asset to the database
    public void addAsset(int assetID, String name, String type, byte[] imageData, String imagePath){
        try {
            //Prepares the SQL statement to insert data int the asset table
            PreparedStatement statement = connection.prepareStatement("INSERT INTO assets (asset_id, name, type, image_data, image_path) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, assetID);
            statement.setString(2, name);
            statement.setString(3, type);
            statement.setBytes(4, imageData);
            statement.setString(5, imagePath);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //purposed to retrieve a group by its type
    public Group getGroupByType(String type) {
        try {
            String query = "SELECT group_id, name FROM `groups` WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int groupID = resultSet.getInt("group_id");
                String groupName = resultSet.getString("name");
                List<Integer> assetIDs = getAssetsInGroup(groupID);

                Group group = new Group(groupID, groupName);
                for (int assetID : assetIDs) {
                    Asset asset = loadedAssets.get(assetID);
                    group.addAsset(asset);
                }
                return group;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //meant to add a new group to the database
    public void addGroup(int groupID, String name, List<Integer> assetIDs) throws SQLException{
        try {
            //System.out.println("Executing SQL: " + statement.toString());

            PreparedStatement statement = connection.prepareStatement("INSERT INTO `groups` (group_id, name) VALUES (?,?)");
            statement.setInt(1, groupID);
            statement.setString(2, name);
            statement.executeUpdate();

            statement = connection.prepareStatement("INSERT INTO group_assets (group_id, asset_id) VALUES (?,?)");
            for (int assetID : assetIDs){
                statement.setInt(1, groupID);
                statement.setInt(2, assetID);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    //retrieves asset data for a certain assetID
    public Map<String, String> getAssetData(int assetID) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT name, type, image_path FROM assets WHERE asset_id = ?");
            statement.setInt(1, assetID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Map<String, String> data = new HashMap<>();
                data.put("name", resultSet.getString("name"));
                data.put("type", resultSet.getString("type"));
                data.put("image_path", resultSet.getString("image_path"));
                return data;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //intended to retrieve a list of asset ID's for a given group
    public List<Integer> getAssetsInGroup(int groupID) {
        List<Integer> assetIDs = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT asset_id FROM group_assets WHERE group_id = ?");
            statement.setInt(1, groupID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                assetIDs.add(resultSet.getInt("asset_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assetIDs;
    }
}
