package fishshopsystem;

public class FishShop {
    private String shopName;
    private double cash;
    // private FishInventory inventory; 
    // private ArrayList<Transaction> transactions;
    
    public FishShop(String shopName, double initialCash, int capacity) { 
        this.shopName = shopName;
        this.cash = initialCash;
        // this.inventory = new FishInventory(capacity);
        // this.transactions = new ArrayList<>();
    }
    
    public String getShopName() {
        return shopName;
    }
    
    public double getCash() {
        return cash;
    }
    
    public void setCash(double amount) {
        this.cash = amount;
    }
    
    // Methods to implement when dependencies are ready:
    // public boolean buyFish(Fish fish) throws InsufficientFundsException {}
    // public boolean sellFish(String fishId, String customer) throws FishNotFoundException {}
    // public FishInventory getInventory() {}
    // public ArrayList<Transaction> getTransactions() {}
}
