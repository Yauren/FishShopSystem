package fishshopsystem;

import java.io.*;
import java.util.ArrayList;

public class FileManager {
    private static final String SAVE_DIR = "shops";

    private static String getFileNameForShop(String shopName) {
        String safeName = shopName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return SAVE_DIR + File.separator + "shop_" + safeName + ".ser";
    }

    public static boolean saveShop(FishShop shop) {
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = getFileNameForShop(shop.getShopName());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(shop);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving shop to " + fileName + ": " + e.getMessage());
            return false;
        }
    }

    public static FishShop loadShop(String shopName) {
        String fileName = getFileNameForShop(shopName);
        File file = new File(fileName);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (FishShop) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading shop from " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> getAvailableShops() {
        ArrayList<String> shopNames = new ArrayList<>();
        File dir = new File(SAVE_DIR);
        File[] files = dir.listFiles((d, name) -> name.startsWith("shop_") && name.endsWith(".ser"));

        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                String rawName = fileName.substring(5, fileName.length() - 4);
                String displayName = rawName.replace('_', ' ');
                shopNames.add(displayName);
            }
        }
        return shopNames;
    }
}