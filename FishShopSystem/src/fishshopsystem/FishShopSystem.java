package fishshopsystem;

import java.util.Scanner;
import java.util.ArrayList;

public class FishShopSystem {
    private FishShop shop;
    private Scanner scanner;

    private static class FishTemplate {
        String species;
        double price;
        boolean isSaltwater;
        FishTemplate(String species, double price, boolean isSaltwater) {
            this.species = species;
            this.price = price;
            this.isSaltwater = isSaltwater;
        }
    }
    
    public FishShopSystem() {
        scanner = new Scanner(System.in);
    }

    
    public static void main(String[] args) {
        FishShopSystem system = new FishShopSystem();
        if (system.setup()) {
            system.run();
        } else {
            System.out.println("Exiting system.");
        }
    }

    private boolean setup() {
        System.out.println("================================================");
        System.out.println("|  Welcome to the Fish Shop Management System  |");
        System.out.println("================================================");

        while (true) {
            System.out.println("\n+----------------- Shop Setup -----------------+");
            System.out.println("| 1. Create a New Shop                         |");
            System.out.println("| 2. Load an Existing Shop                     |");
            System.out.println("| 0. Exit                                      |");
            System.out.println("+----------------------------------------------+");

            int choice = getIntInput("Your choice: ");

            switch (choice) {
                case 1:
                    handleCreateNewShop();
                    return true; 
                case 2:
                    if (handleLoadExistingShop()) {
                        return true; 
                    }
                  
                    break;
                case 0:
                    return false; 
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void run() {
        boolean running = true;
        
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    handleBuyFish();
                    pressEnterToContinue();
                    break;
                case 2:
                    handleSellFish();
                    pressEnterToContinue();
                    break;
                case 3:
                    handleViewInventory();
                    pressEnterToContinue();
                    break;
                case 4:
                    handleViewTransactions();
                    break;
                case 0:
                    running = false;
                    FileManager.saveShop(this.shop);
                    System.out.println("\n================================================");
                    System.out.println("| Thank you for using Fish Shop System!        |");
                    System.out.println("| Data saved successfully.                     |");
                    System.out.println("================================================");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
        
        scanner.close();
    }
    
    private void displayMainMenu() {
        System.out.println("\n\n\n");
        System.out.println("=================================================");
        System.out.println("|       Fish Shop and Management System         |");
        System.out.println("=================================================");
        System.out.printf("|  Shop: %-20s | Cash: $%-7.2f  |\n", shop.getShopName(), shop.getCash());
        System.out.println("=================================================");
        System.out.println("| 1. Buy Fish (from supplier)                   |");
        System.out.println("| 2. Sell Fish (to customer)                    |");
        System.out.println("| 3. View Inventory                             |");
        System.out.println("| 4. View Transactions                          |");
        System.out.println("| 0. Exit                                       |");
        System.out.println("=================================================");
    }
    
    private void handleBuyFish() {
        try {
            System.out.println("\n=================================================");
            System.out.println("|         Buy Fish (from Supplier)               |");
            System.out.println("=================================================");
            ArrayList<FishTemplate> supplierFish = getSupplierFishList();

            System.out.println("| Available fish from supplier:                |");
            System.out.println("=================================================");
            for (int i = 0; i < supplierFish.size(); i++) {
                FishTemplate f = supplierFish.get(i);
                String type = f.isSaltwater ? "Saltwater" : "Freshwater";
                String line = String.format("%d. %-15s (%-10s) - $%.2f", i + 1, f.species, type, f.price);
                System.out.printf("| %-45s |\n", line);
            }
            System.out.println("| 0. Cancel                                     |");
            System.out.println("=================================================");

            int choice = getIntInput("Choose a fish to buy: ");

            if (choice == 0) {
                System.out.println("Purchase cancelled.");
                return;
            }

            if (choice < 1 || choice > supplierFish.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            FishTemplate chosenFishTemplate = supplierFish.get(choice - 1);
            
            String confirm = getStringInput(String.format("Buy %s for $%.2f? (y/n): ", chosenFishTemplate.species, chosenFishTemplate.price));
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Purchase cancelled.");
                return;
            }

            Fish fishToBuy;

            if (chosenFishTemplate.isSaltwater) {
                fishToBuy = new SaltwaterFish(chosenFishTemplate.species, chosenFishTemplate.price);
            } else {
                fishToBuy = new FreshwaterFish(chosenFishTemplate.species, chosenFishTemplate.price);
            }

            String date = getStringInput("Enter purchase date (YYYY-MM-DD): ");

            if (shop.buyFish(fishToBuy, date)) {
                System.out.println("=================================================");
                String successMsg = String.format("SUCCESS! Bought %s for $%.2f", fishToBuy.getSpecies(), fishToBuy.getPrice());
                System.out.printf("| %-45s |\n", successMsg);
                String idMsg = String.format("New Fish ID: %s", fishToBuy.getId());
                System.out.printf("| %-45s |\n", idMsg);
                System.out.println("=================================================");
            } else {
                System.out.println("=================================================");
                System.out.println("| FAILED: Inventory is full.                    |");
                System.out.println("=================================================");
            }
        } catch (InsufficientFundsException e) {
            System.out.println("=================================================");
            System.out.println("|          TRANSACTION FAILED                   |");
            System.out.println("=================================================");
            String errorMsg = e.getMessage();
            System.out.printf("| %-45s |\n", errorMsg);
            System.out.println("=================================================");
        }
    }
    
    private void handleSellFish() {
        try {
            System.out.println("\n=================================================");
            System.out.println("|              Sell Fish                        |");
            System.out.println("=================================================");
            if (shop.getInventory().getFishCount() == 0) {
                System.out.println("| Inventory is empty. Nothing to sell.          |");
                System.out.println("=================================================");
                return;
            }
            
            System.out.println("| Current Fish in Stock:                        |");
            System.out.println("=================================================");
            handleViewInventory();

            String fishId = getStringInput("Enter fish ID to sell: ");
            
            Fish fishToSell = shop.getInventory().getFish(fishId);
            if (fishToSell == null) {
                System.out.println("=================================================");
                String errorMsg = String.format("FAILED: Fish ID '%s' not found.", fishId);
                System.out.printf("| %-45s |\n", errorMsg);
                System.out.println("=================================================");
                return;
            }

            String confirm = getStringInput(String.format("Sell %s (ID: %s) for $%.2f? (y/n): ", fishToSell.getSpecies(), fishToSell.getId(), fishToSell.getPrice()));
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Sale cancelled.");
                return;
            }

            String customer = getStringInput("Enter customer name: ");
            String date = getStringInput("Enter sale date (YYYY-MM-DD): ");

            if (shop.sellFish(fishId, customer, date)) {
                System.out.println("==============================================");
                String successMsg = String.format("SUCCESS! Sold %s (ID: %s)", fishToSell.getSpecies(), fishToSell.getId());
                System.out.printf("| %-42s |\n", successMsg);
                System.out.println("==============================================");
            }
        } catch (FishNotFoundException e) {
            System.out.println("==============================================");
            System.out.println("|          TRANSACTION FAILED                |");
            System.out.println("==============================================");
            String errorMsg = e.getMessage();
            System.out.printf("| %-42s |\n", errorMsg);
            System.out.println("==============================================");
        }
    }
    
    private void handleViewInventory() {
        System.out.println("\n=================================================");
        System.out.println("|                   INVENTORY                   |");
        System.out.println("=================================================");
        FishInventory inv = shop.getInventory();
        System.out.printf("| Capacity: %-35s |\n", inv.getFishCount() + "/" + inv.getCapacity());
        System.out.println("=================================================");
        
        ArrayList<Fish> allFish = inv.getFishStock();
        
        if (allFish.isEmpty()) {
            System.out.println("| No fish in inventory.                         |");
            System.out.println("=================================================");
        } else {
            for (Fish fish : allFish) {
                System.out.printf("| ID:      %-36s |\n", fish.getId());
                System.out.printf("| Species: %-36s |\n", fish.getSpecies());
                System.out.printf("| Price:   $%-35.2f |\n", fish.getPrice());

                ArrayList<String> wrappedDesc = wrapText(fish.getDescription(), 35);
                System.out.printf("| Desc:    %-36s |\n", wrappedDesc.get(0));
                for (int i = 1; i < wrappedDesc.size(); i++) {
                    System.out.printf("|          %-36s |\n", wrappedDesc.get(i));
                }
                
                System.out.println("=================================================");
            }
        }
    }
    
    private void handleViewTransactions() {
        boolean viewingTransactions = true;
        while (viewingTransactions) {
            System.out.println("\n=================================================");
            System.out.println("|            Transactions Menu                  |");
            System.out.println("=================================================");
            System.out.println("| 1. View Buy Transactions                      |");
            System.out.println("| 2. View Sell Transactions                     |");
            System.out.println("| 3. View All Transactions                      |");
            System.out.println("| 0. Back to Main Menu                          |");
            System.out.println("=================================================");

            int choice = getIntInput("Your choice: ");
            ArrayList<Transaction> txns = shop.getTransactions();

            if (txns.isEmpty()) {
                System.out.println("==============================================");
                System.out.println("| No transactions recorded yet.              |");
                System.out.println("==============================================");
                pressEnterToContinue();
                return;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n==============================================");
                    System.out.println("|       BUY TRANSACTION HISTORY              |");
                    System.out.println("==============================================");
                    boolean foundBuy = false;
                    for (Transaction txn : txns) {
                        if (txn.getTransactionType().equals("BUY")) {
                            System.out.println(txn);
                            System.out.println("==============================================");
                            foundBuy = true;
                        }
                    }
                    if (!foundBuy) {
                        System.out.println("| No buy transactions found.                 |");
                        System.out.println("==============================================");
                    }
                    pressEnterToContinue();
                    break;
                case 2:
                    System.out.println("\n==============================================");
                    System.out.println("|       SELL TRANSACTION HISTORY             |");
                    System.out.println("==============================================");
                    boolean foundSell = false;
                    for (Transaction txn : txns) {
                        if (txn.getTransactionType().equals("SELL")) {
                            System.out.println(txn);
                            System.out.println("==============================================");
                            foundSell = true;
                        }
                    }
                    if (!foundSell) {
                        System.out.println("| No sell transactions found.                |");
                        System.out.println("==============================================");
                    }
                    pressEnterToContinue();
                    break;
                case 3:
                    System.out.println("\n==============================================");
                    System.out.println("|       ALL TRANSACTION HISTORY              |");
                    System.out.println("==============================================");
                    for (Transaction txn : txns) {
                        System.out.println(txn);
                        System.out.println("==============================================");
                    }
                    pressEnterToContinue();
                    break;
                case 0:
                    viewingTransactions = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    
        private void handleCreateNewShop() {
        System.out.println("\n================================================");
        System.out.println("|            Create New Shop                   |");
        System.out.println("================================================");
        String name = getStringInput("Enter shop name: ");
        double cash = getDoubleInput("Enter initial cash: $");
        int capacity = getIntInput("Enter inventory capacity: ");
        this.shop = new FishShop(name, cash, capacity);
        System.out.println("================================================");
        System.out.println("|  Shop '" + name + "' created successfully!    ");
        System.out.println("================================================");
    }

    private boolean handleLoadExistingShop() {
        ArrayList<String> shopNames = FileManager.getAvailableShops();
        if (shopNames.isEmpty()) {
            System.out.println("\n================================================");
            System.out.println("|  No existing shops found.                    |");
            System.out.println("|  Please create a new one.                    |");
            System.out.println("================================================");
            return false;
        }

        System.out.println("\n================================================");
        System.out.println("|            Load Existing Shop                |");
        System.out.println("================================================");
        for (int i = 0; i < shopNames.size(); i++) {
            System.out.printf("| %d. %-42s|\n", i + 1, shopNames.get(i));
        }
        System.out.println("| 0. Cancel                                    |");
        System.out.println("================================================");

        int choice = getIntInput("Choose a shop to load: ");
        if (choice > 0 && choice <= shopNames.size()) {
            String shopNameToLoad = shopNames.get(choice - 1);
            FishShop loadedShop = FileManager.loadShop(shopNameToLoad);
            if (loadedShop != null) {
                this.shop = loadedShop;
                initializeCountersFromLoadedShop();
                System.out.println("================================================");
                System.out.println("| Shop '" + shopNameToLoad + "' loaded successfully!              |");
                System.out.println("================================================");
                return true;
            } else {
                System.out.println("Failed to load shop. Please try again.");
                return false;
            }
        } else {
            System.out.println("Load operation cancelled.");
            return false;
        }
    }

    private void initializeCountersFromLoadedShop() {
        if (this.shop == null) return;

        int maxFishId = this.shop.getInventory().getMaxFishIdNumber();
        Fish.setNextId(maxFishId + 1);

        int maxBuyId = 0;
        int maxSellId = 0;
        for (Transaction txn : this.shop.getTransactions()) {
            if (txn.getTransactionType().equals("BUY")) {
                if (txn.getIdNumber() > maxBuyId) maxBuyId = txn.getIdNumber();
            } else if (txn.getTransactionType().equals("SELL")) {
                if (txn.getIdNumber() > maxSellId) maxSellId = txn.getIdNumber();
            }
        }
        Transaction.setNextIds(maxBuyId + 1, maxSellId + 1);
    }
    
    private ArrayList<FishTemplate> getSupplierFishList() {
        ArrayList<FishTemplate> supplierFish = new ArrayList<>();
        supplierFish.add(new FishTemplate("Guppy", 1.50, false));
        supplierFish.add(new FishTemplate("Betta", 5.00, false));
        supplierFish.add(new FishTemplate("Neon Tetra", 2.00, false));
        supplierFish.add(new FishTemplate("Clownfish", 15.00, true));
        supplierFish.add(new FishTemplate("Blue Tang", 25.00, true));
        supplierFish.add(new FishTemplate("Yellow Tang", 20.00, true));
        return supplierFish;
    }
    
    
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
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
    private ArrayList<String> wrapText(String text, int lineLength) {
        ArrayList<String> lines = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            lines.add("");
            return lines;
        }

        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > lineLength) {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            } else {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            }
        }
        lines.add(currentLine.toString());
        return lines;
    }
    
}
