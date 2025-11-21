package fishshopsystem;

abstract class Fish implements Sellable {
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


class FreshwaterFish extends Fish {
    FreshwaterFish(String s, double p) {
        super(s, p);
    }
    
    @Override
    public String getDescription() {
        return "The species '" + species + "' is a Freshwater Fish (lives in rivers and lakes).";
    }
}


class SaltwaterFish extends Fish {
    SaltwaterFish(String s, double p) {
        super(s, p);
    }
    
    @Override
    public String getDescription() {
        return "The species '" + species + "' is a Saltwater Fish (lives in oceans and seas).";
    }
}
