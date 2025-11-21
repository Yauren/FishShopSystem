package fishshopsystem;

public class FreshwaterFish extends Fish{
    public FreshwaterFish(String s, double p) {
        super(s, p);
    }
    
    @Override
    public String getDescription() {
        return "The species '" + species + "' is a Freshwater Fish (lives in rivers and lakes).";
    }
}

