import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private static AssetManager assetManager;
    private static List<Asset> loadedAssets = new ArrayList<>();
    private static List<Group> loadedGroups = new ArrayList<>();
    private static final int ASSET_WIDTH = 100;
    private static final int ASSET_HEIGHT = 100;

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/assetmanagementdb";
        String username = "root";
        String password = "iamroot";

        Database database = new Database(jdbcUrl, username, password);

        assetManager = new AssetManager(database, Main::readImageData);
        //Group enemiesGroup = assetManager.loadGroupByType("Enemy");
        //Group alliesGroup = assetManager.loadGroupByType("Ally");

        JFrame frame = new JFrame("Example Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };

        JButton addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> {
            Asset newAsset = assetManager.loadAsset(1);
            if (newAsset != null) {
                loadedAssets.add(newAsset);
                gamePanel.repaint();
            }
        });

        /*JButton addEnemyButton = new JButton("Add Enemies");
        addEnemyButton.addActionListener(e -> {
            Group newEnemiesGroup = assetManager.loadGroupByType("Enemy");
            if(newEnemiesGroup != null) {
                loadedGroups.add(newEnemiesGroup);
                List<Asset> enemyAssets = newEnemiesGroup.getAssets();
                loadedAssets.addAll(enemyAssets);
                gamePanel.repaint();
            }
        });*/

        /*JButton addAllyButton = new JButton("Add Allies");
        addAllyButton.addActionListener(e -> {
            Group newAlliesGroup = assetManager.loadGroupByType("Ally");
            if(newAlliesGroup != null) {
                loadedGroups.add(newAlliesGroup);
                gamePanel.repaint();
            }
        });*/

        JButton removeButton = new JButton("Remove Asset");
        removeButton.addActionListener(e -> {
            if (!loadedAssets.isEmpty()) {
                loadedAssets.remove(loadedAssets.size() - 1);
                gamePanel.repaint();
            }
        });

        /*JButton addGroupButton = new JButton("Add Enemy Assets");
        addGroupButton.addActionListener(e-> {
            if(enemiesGroup != null) {
                loadedGroups.add(enemiesGroup);
                gamePanel.repaint();
            }
        });*/

        /*JButton removeGroupedButton = new JButton("Remove Grouped Assets");
        removeGroupedButton.addActionListener(e -> {
            if(!loadedGroups.isEmpty()) {
                loadedGroups.remove(loadedGroups.size() - 1);
                gamePanel.repaint();
            }
        });*/

        Asset secondAsset = assetManager.loadAsset(2);
        if(secondAsset != null) {
            loadedAssets.add(secondAsset);
        }

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(addButton, BorderLayout.NORTH);
        frame.add(removeButton, BorderLayout.SOUTH);
        //frame.add(addGroupButton, BorderLayout.WEST);
        //frame.add(removeGroupedButton, BorderLayout.EAST);
        frame.setVisible(true);
    }

    private static void drawGame(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 1920, 1080);

        int x = 100;
        int scaledWidth = 100;

        for(Asset asset : loadedAssets){
            BufferedImage image = asset.getImageData();

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();

            double scaleFactor = (double) scaledWidth / originalWidth;
            int scaledHeight = (int) (originalHeight * scaleFactor);

            g.drawImage(image, x, 100, scaledWidth, scaledHeight, null);
            x += scaledWidth + 20;
        }

        /*int y = 500;
        for(Group group: loadedGroups) {
            if("Enemy Group".equals(group.getGroupName())) {
                List<Asset> assets = group.getAssets();
                drawGroup(g, group, x, y);
                y += ASSET_HEIGHT + 20;
            }

        }*/
    }

    /*private static void drawGroup(Graphics g, Group group, int startX, int y) {
        int x = startX;
        for(Asset asset : group.getAssets()) {
            BufferedImage image = asset.getImageData();
            if(image == null) {
                System.out.println("Image is null for asset.");
            } else {
                System.out.println("Drawing asset at x: " + x + ", y: " + y);
                g.drawImage(image, x, 100, ASSET_WIDTH, ASSET_HEIGHT, null);
                x+= ASSET_WIDTH + 20;
            }
        }
    }*/
}