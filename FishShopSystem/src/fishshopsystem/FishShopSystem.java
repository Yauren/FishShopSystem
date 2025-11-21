package fishshopsystem;

import java.util.Scanner;
import java.util.ArrayList;

public class FishShopSystem {
    private FishShop shop;
    private Scanner scanner;
    
    public FishShopSystem() {
        scanner = new Scanner(System.in);
        shop = new FishShop("Aqua Paradise", 1000.0, 50);
    }
    
    public static void main(String[] args) {
        FishShopSystem system = new FishShopSystem();
        system.run();
    }
    
    private void run() {
        boolean running = true;
        
        System.out.println("=======================================");
        System.out.println("    Fish Shop and Management System    ");
        System.out.println("=======================================\n");
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1:
                        handleBuyFish();
                        break;
                    case 2:
                        handleSellFish();
                        break;
                    case 3:
                        handleViewInventory();
                        break;
                    case 4:
                        handleViewTransactions();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using Fish Shop Management System!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("Shop: " + shop.getShopName());
        System.out.println("Cash: $" + String.format("%.2f", shop.getCash()));
        System.out.println("------------------");
        System.out.println("1. Buy Fish (from supplier)");
        System.out.println("2. Sell Fish (to customer)");
        System.out.println("3. View Inventory");
        System.out.println("4. View Transactions");
        System.out.println("0. Exit");
        System.out.println("==================");
    }
    
    private void handleBuyFish() {
        System.out.println("\n=== BUY FISH FROM SUPPLIER ===");
        System.out.println("Fish Types:");
        System.out.println("1. Freshwater Fish");
        System.out.println("2. Saltwater Fish");
        
        int type = getIntInput("Enter fish type (1 or 2): ");
        String species = getStringInput("Enter species name: ");
        double price = getDoubleInput("Enter purchase price: $");
        
        Fish fish = null;
        if (type == 1) {
            fish = new FreshwaterFish(species, price);
        } else if (type == 2) {
            fish = new SaltwaterFish(species, price);
        } else {
            System.out.println("Invalid fish type!");
            return;
        }
        
        System.out.println("\nYou are buying: " + fish);
        System.out.println(fish.getDescription());
        System.out.println("Cost: $" + String.format("%.2f", fish.getPrice()));
        
        // For now, I'll just add to a temporary list since FishInventory isn't ready
        System.out.println("\nFish purchased successfully!");
        System.out.println("Remaining cash: $" + String.format("%.2f", shop.getCash() - price));
        shop.setCash(shop.getCash() - price);
    }
    
    private void handleSellFish() {
        System.out.println("\n=== SELL FISH TO CUSTOMER ===");
        System.out.println("(waiting for FishInventory class)");
        
        // String fishId = getStringInput("Enter Fish ID to sell: ");
        // String customerName = getStringInput("Enter customer name: ");
        // shop.sellFish(fishId, customerName);
    }
    
    private void handleViewInventory() {
        System.out.println("\n=== CURRENT INVENTORY ===");
        System.out.println("(waiting for FishInventory class)");
        
        // FishInventory inventory = shop.getInventory();
        // for (Fish fish : inventory.getAllFish()) {
        //     System.out.println(fish + " - " + fish.getDescription());
        // }
    }
    
    private void handleViewTransactions() {
        System.out.println("\n=== TRANSACTION HISTORY ===");
        System.out.println("(waiting for Transaction class)");
        
        // ArrayList<Transaction> transactions = shop.getTransactions();
        // for (Transaction t : transactions) {
        //     System.out.println(t);
        // }
    }
    
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); 
        return value;
    }
    
    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. " + prompt);
            scanner.next();
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); 
        return value;
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}


