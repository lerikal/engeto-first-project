import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlantListManager {
    List<Plant> plantList; //  kolekce, uchovávající objekty s informacemi o květinách

    // Konstruktor
    public PlantListManager() {
        this.plantList = new ArrayList<>();
    }

    // Výchozí přístupové metody
    public List<Plant> getPlantList() {
        return plantList;
    }

    public void setPlantList(List<Plant> plantList) {
        this.plantList = plantList;
    }

    // Metody

    // přidání nové květiny
    public void addNewPlant(Plant plant) {
        plantList.add(plant);
    }

    // získání květiny na zadaném indexu
    public Plant getPlantByIndex(int index) {
        return plantList.get(index);
    }
    // odebrání květiny ze seznamu
    public void removePlantByIndex(int index) {
        plantList.remove(index);
    }

    // získání kopie seznamu květin
    public List<Plant> copyPlantList(List<Plant> original) {
        List<Plant> copy  = new ArrayList<>();
        copy.addAll(original);
        return copy;
    }

    // metoda, která vrátí seznam rostlin, které je třeba zalít
    // (Jejich datum poslední zálivky je starší než počet dnů, kdy mají být zalité.)
    public PlantListManager getPlantsNeedToWater() {
        PlantListManager plantsNeedToWater = new PlantListManager();
        LocalDate now = LocalDate.now();


        for (Plant plant : this.plantList) {
            LocalDate recommendedWatering = plant.getWatering().plusDays(plant.getFrequencyOfWatering());
            if (recommendedWatering.isBefore(now)) {
                plantsNeedToWater.add(plant);
            }
        }
        return plantsNeedToWater;
    }
}
