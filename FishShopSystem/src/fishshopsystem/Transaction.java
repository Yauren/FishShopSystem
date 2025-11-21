package fishshopsystem;
import java.io.Serializable;

public class Transaction implements Serializable{
    private static int buyCounter = 1;
    private static int sellCounter = 1;
    
    private final String transactionType;
    private String transactionId, customerName, fishId, fishSpecies, date;
    private double amount;
    
    public Transaction(String transactionType, String customerName, String fishId, String fishSpecies, double amount, String date) throws InvalidTransactionException{
        this.transactionType = transactionType;
        if(!transactionType.equals("SELL") && !transactionType.equals("BUY")){
            throw new InvalidTransactionException("Invalid choice.");
        }
        
        if(transactionType.equals("SELL")){
         this.transactionId = "S" + sellCounter++;   
        } else {
         this.transactionId = "B" + buyCounter++;
        }
        this.customerName = customerName;
        this.fishId = fishId;
        this.fishSpecies = fishSpecies;
        this.amount = amount;
        this.date = date;
    }
    public static void setNextIds(int nextBuyId, int nextSellId) {
        buyCounter = nextBuyId;
        sellCounter = nextSellId;
    }
    
    public int getIdNumber() {
        try {
            return Integer.parseInt(transactionId.substring(1));
        } catch (NumberFormatException e) {
            return 0; 
        }
    }
    public String getTransactionType(){return transactionType;}
    public String getCustomerName(){return customerName;}
    public String getFishId(){return fishId;}
    public String getFishSpecies(){return fishSpecies;}
    public double getAmount(){return amount;}
    public String getDate(){return date;}
    
    @Override
    public String toString(){
        String counterpartyLine;
        if (transactionType.equals("SELL")) {
            counterpartyLine = String.format("Customer: %s", this.customerName);
        } else {
            counterpartyLine = "Supplier";
        }

        return String.format(
            "-- Transaction: %s --\nType: %s\n%s\nFish ID: %s\nSpecies: %s\nAmount: $%.2f\nDate: %s",
            this.transactionId,
            this.transactionType,
            counterpartyLine,
            this.fishId,
            this.fishSpecies,
            this.amount,
            this.date
        );
    }
}