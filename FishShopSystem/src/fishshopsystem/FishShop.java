package fishshopsystem;
import java.io.Serializable;

import java.util.ArrayList;

public class FishShop implements Serializable{
    private String shopName;
    private double cash;
    private FishInventory inventory; 
    private ArrayList<Transaction> transactions;
    
    public FishShop(String shopName, double initialCash, int capacity) { 
        this.shopName = shopName;
        this.cash = initialCash;
        this.inventory = new FishInventory(capacity);
        this.transactions = new ArrayList<>();
    }
    
    public String getShopName() { return shopName; }
    public double getCash() { return cash; }
    public void setCash(double amount) { this.cash = amount; }
    public FishInventory getInventory() { return inventory; }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    
    public boolean buyFish(Fish fish, String date) throws InsufficientFundsException {
        if (cash < fish.getPrice()) {
            throw new InsufficientFundsException("Not enough cash to buy this fish. Need: $" + fish.getPrice() + ", Have: $" + cash);
        }
        
        if (!inventory.addFish(fish)) {
            return false;
        }
        
        cash -= fish.getPrice();
        
        try {
            Transaction transaction = new Transaction("BUY", "Supplier", fish.getId(), fish.getSpecies(), fish.getPrice(), date);
            transactions.add(transaction);
        } catch (InvalidTransactionException e) {
            System.out.println("Error creating transaction: " + e.getMessage());
        }
        
        return true;
    }
    
    public boolean sellFish(String fishId, String customer, String date) throws FishNotFoundException {
        Fish fish = inventory.getFish(fishId);
        
        if (fish == null) {
            throw new FishNotFoundException("Fish with ID '" + fishId + "' not found in inventory.");
        }
        
        Fish removedFish = inventory.removeFish(fishId);
        
        if (removedFish != null) {
            cash += removedFish.getPrice();
            
            try {
                Transaction transaction = new Transaction("SELL", customer, removedFish.getId(), removedFish.getSpecies(), removedFish.getPrice(), date);
                transactions.add(transaction);
            } catch (InvalidTransactionException e) {
                System.out.println("Error creating transaction: " + e.getMessage());
            }
            
            return true;
        }
        
        return false;
    }
}