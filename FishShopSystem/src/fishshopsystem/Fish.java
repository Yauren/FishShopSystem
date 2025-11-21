package fishshopsystem;
import java.io.Serializable;

abstract class Fish implements Sellable, Serializable {
    private static int idCounter = 1;
    
    private String id;
    protected final String species;
    private double price;
    
    Fish(String species, double price) {
        this.id = "FISH" + String.format("%03d", idCounter++);
        this.species = species;
        this.price = price;
    }
    
    public String getId() {
        return id;
    }

    public int getIdNumber() {
        try {
            return Integer.parseInt(id.substring(4));
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return 0;
        }
    }
    
    public static void setNextId(int nextId) {
        idCounter = nextId;
    }
    
    
    @Override
    public String getSpecies() {
        return species;
    }
    
    @Override
    public double getPrice() {
        return price;
    }
    
    @Override
    public String getDescription() {
        return "The species '" + species + "' is a common Fish.";
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    @Override
    public String toString() {
        return "ID: " + id + "Species: " + species + "Price: " + price;
    }
}
