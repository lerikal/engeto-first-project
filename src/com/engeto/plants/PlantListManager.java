package com.engeto.plants;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlantListManager {
    private final List<Plant> plantList = new ArrayList<>(); //  kolekce, uchovávající objekty s informacemi o květinách

    private static final String DELIMITER = "\t";

    /**
     * Vrátí kopii seznamu rostlin.
     *
     * @return seznam rostlin (kopie interního seznamu)
     */
    public List<Plant> getPlantList() {
        return new ArrayList<>(plantList);
    }

    // Metody
    /**
     * Přidá novou rostlinu do seznamu.
     *
     * @param plant rostlina, která se má přidat
     */
    public void addNewPlant(Plant plant) {
        plantList.add(plant);
    }

    /**
     * Vrátí rostlinu na zadaném indexu.
     *
     * @param index pozice v seznamu
     * @return rostlina na daném indexu
     * @throws IndexOutOfBoundsException pokud je index mimo rozsah
     */
    public Plant getPlantByIndex(int index) {
        if (index < 0 || index >= plantList.size()) {
            throw new IndexOutOfBoundsException("Neplatný index: " + index + ".");
        }
        return plantList.get(index);
    }

    /**
     * Odebere rostlinu ze seznamu na zadaném indexu.
     *
     * @param index pozice v seznamu
     * @throws IndexOutOfBoundsException pokud je index mimo rozsah
     */
    public void removePlantByIndex(int index) {
        if (index < 0 || index >= plantList.size()) {
            throw new IndexOutOfBoundsException("Neplatný index: " + index + ".");
        }
        plantList.remove(index);
    }

    /**
     * Přepíše aktuální seznam rostlin kopií jiného seznamu.
     *
     * @param plantList seznam rostlin ke zkopírování
     */
    public void copyPlantList(List<Plant> plantList) {
        this.plantList.clear();
        this.plantList.addAll(plantList);
    }

    /**
     * Vrátí seznam rostlin, které je potřeba zalít.
     *
     * Rostliny jsou vybrány podle toho, zda už uplynul jejich doporučený interval zálivky.
     *
     * @return nový objekt PlantListManager s rostlinami k zalití
     */
    public PlantListManager getPlantsNeedToWater() {
        PlantListManager plantsNeedToWater = new PlantListManager();
        LocalDate now = LocalDate.now();

        for (Plant plant : this.plantList) {
            LocalDate recommendedWatering = plant.getWatering().plusDays(plant.getFrequencyOfWatering());

            if (!recommendedWatering.isAfter(now)) {
                plantsNeedToWater.addNewPlant(plant);
            }
        }
        return plantsNeedToWater;
    }

    /**
     * Načte seznam rostlin ze souboru.
     *
     * Každý řádek souboru reprezentuje jednu rostlinu.
     *
     * @param fileName název souboru
     * @return objekt PlantListManager s načtenými rostlinami
     * @throws PlantException pokud dojde k chybě při načítání nebo parsování
     */
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

    /**
     * Uloží seznam rostlin do souboru.
     *
     * @param fileName název souboru
     * @param plantList seznam rostlin k uložení
     * @throws PlantException pokud dojde k chybě při zápisu
     */
    public void writePlantListToFile(String fileName, PlantListManager plantList) throws PlantException {
        String path = "data" + File.separator + fileName;

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))
        ) {
            for (Plant plant : plantList.getPlantList()) {
                writer.println(plant.getName() + DELIMITER
                             + plant.getNotes() + DELIMITER
                             + plant.getFrequencyOfWatering()
                             + DELIMITER + plant.getWatering()
                             + DELIMITER + plant.getPlanted());
            }
        } catch (IOException e) {
            throw new PlantException("Chyba při zápisu do souboru: " + fileName + " " + e.getMessage() + ".");
        }
    }
}
