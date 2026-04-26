package plants;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlantListManager {
    List<Plant> plantList = new ArrayList<>(); //  kolekce, uchovávající objekty s informacemi o květinách

    public List<Plant> getPlantList() {
        return plantList;
    }

    // Metody

    // přidání nové květiny
    public void addNewPlant(Plant plant) {
        plantList.add(plant);
    }

    // získání květiny na zadaném indexu
    public Plant getPlantByIndex(int index) {
        if (index < 0 || index >= plantList.size()) {
            throw new IndexOutOfBoundsException("Neplatný index: " + index + ".");
        }
        return plantList.get(index);
    }

    // odebrání květiny ze seznamu
    public void removePlantByIndex(int index) {
        if (index < 0 || index >= plantList.size()) {
            throw new IndexOutOfBoundsException("Neplatný index: " + index + ".");
        }
        plantList.remove(index);
    }

    // získání kopie seznamu květin
    public void copyPlantList(List<Plant> plantList) {
        this.plantList = new ArrayList<>(plantList);
    }

    // metoda, která vrátí seznam rostlin, které je třeba zalít
    // (Jejich datum poslední zálivky je starší než počet dnů, kdy mají být zalité.)
    public PlantListManager getPlantsNeedToWater() {
        PlantListManager plantsNeedToWater = new PlantListManager();
        LocalDate now = LocalDate.now();

        for (Plant plant : this.plantList) {
            LocalDate recommendedWatering = plant.getWatering().plusDays(plant.getFrequencyOfWatering());

            if (recommendedWatering.isBefore(now)) {
                plantsNeedToWater.addNewPlant(plant);
            }
        }
        return plantsNeedToWater;
    }

    // načtení květin ze souboru
    public PlantListManager readPlantsListFromFile(String fileName) throws PlantException {
        PlantListManager plantList = new PlantListManager();
        String path = "data" + File.separator + fileName;

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(path)))) {
            while (scanner.hasNextLine()) {
                try {
                    Plant plant = Plant.parsePlant(scanner.nextLine());
                    plantList.addNewPlant(plant);

                } catch (PlantException e) {
                    throw new PlantException("Nepodařilo se přidat rostlinu: " +  e.getMessage() + ".");
                }
            }
        } catch (FileNotFoundException e) {
            throw new PlantException("Soubor nenalezen: " +  e.getMessage() + ".");
        } catch (IOException e) {
            throw new PlantException("Obecná chyba při načítání: " +  e.getMessage() + ".");
        }
        return plantList;
    }

    // uložení květin do souboru
    public void writePlantListToFile(String fileName, PlantListManager plantList) throws PlantException {
        String path = "data" + File.separator + fileName;
        String delimiter = "\t";

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))
        ) {
            for (Plant plant : plantList.getPlantList()) {
                writer.println(plant.getName() + delimiter
                             + plant.getNotes() + delimiter
                             + plant.getFrequencyOfWatering()
                             + delimiter + plant.getWatering()
                             + delimiter + plant.getPlanted());
            }
        } catch (IOException e) {
            throw new PlantException("Chyba při zápisu do souboru: " + fileName + " " + e.getMessage() + ".");
        }
    }
}
