package fishshopsystem;

public class SaltwaterFish extends Fish{
    public SaltwaterFish(String s, double p) {
        super(s, p);
    }
    
    @Override
    public String getDescription() {
        return "The species '" + species + "' is a Saltwater Fish (lives in oceans and seas).";
    }
}

