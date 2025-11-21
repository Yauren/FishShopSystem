package fishshopsystem;
import java.util.ArrayList;
import java.io.Serializable;

public class FishInventory implements Serializable{
    private ArrayList<Fish> fishStock;
    private int capacity;

    public FishInventory(int capacity) {
        this.capacity = capacity;
        this.fishStock = new ArrayList<>();
    }

    public boolean addFish(Fish fish) {
        if (isFull()) {
            System.out.println("Inventory is Full");
            return false;
        }
        fishStock.add(fish);
        return true;
    }

    public Fish removeFish(String fishId) throws FishNotFoundException {
       for (int i = 0; i < fishStock.size(); i++) {
            if (fishStock.get(i).getId().equalsIgnoreCase(fishId)) {
                return fishStock.remove(i);
            }
        }
        throw new FishNotFoundException("Fish with ID '" + fishId + "' not found in inventory.");
    }

    public Fish getFish(String fishId) {
        for (Fish fish : fishStock) {
            if (fish.getId().equalsIgnoreCase(fishId)) {
                return fish;
            }
        }
        return null;
    }

    public ArrayList<Fish> getFishStock() {
        return new ArrayList<>(this.fishStock);
    }

    public ArrayList<Fish> getFishBySpecies(String species) {
        ArrayList<Fish> result = new ArrayList<>();
        for (Fish fish : fishStock) {
            if (fish.getSpecies().equalsIgnoreCase(species)) {
                result.add(fish);
            }
        }
        return result;
    }

    public int getFishCount() {
        return fishStock.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull() {
        return fishStock.size() >= capacity;
    }
    
    public int getMaxFishIdNumber() {
        int maxId = 0;
        for (Fish fish : fishStock) {
            int idNum = fish.getIdNumber();
            if (idNum > maxId) {
                maxId = idNum;
            }
        }
        return maxId;
    }
}