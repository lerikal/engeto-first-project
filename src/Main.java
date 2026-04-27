import com.engeto.plants.Plant;
import com.engeto.plants.PlantException;
import com.engeto.plants.PlantListManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;


public class Main {

    public static void main(String[] args) throws PlantException {
        //interniTesty(); // testy pro vyvoj
        //System.out.println(System.getProperty("user.dir"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy"); // format pro datum

        // Načti seznam květin ze souboru kvetiny.txt.
        PlantListManager myPlantList = new PlantListManager();
        myPlantList = myPlantList.readPlantsListFromFile("kvetiny.txt");

        // Vypiš na obrazovku informace o zálivce pro všechny květiny ze seznamu.
        System.out.println("Informace o zálivce:");
        for (Plant plant: myPlantList.getPlantList()) {
            System.out.println(plant.getWateringInfo());
        }

        // Přidej novou květinu do seznamu (údaje si vymysli).
        Plant myNewPlant= new Plant("Citron", "vyrostl z pecky", LocalDate.of(2022, 4, 15), LocalDate.of(2026, 4, 10), 5);
        myPlantList.addNewPlant(myNewPlant);

        // Přidej 10 rostlin s popisem „Tulipán na prodej 1“ až „Tulipán na prodej 10“. Zasazeny byly dnes, zality také, frekvence zálivky je 14 dnů.
        for (int i = 1; i<11; i++) {
            Plant newPlant = new Plant("Tulipán "+i, "Tulipán na prodej "+i, LocalDate.now(), LocalDate.now(), 14);
            myPlantList.addNewPlant(newPlant);
        }
        System.out.println("-------------------------");

        // Květinu na třetí pozici odeber ze seznamu (prodali jsme ji).
        myPlantList.removePlantByIndex(2);

        // Ulož seznam květin do nového souboru a ověř, že je jeho obsah odpovídá provedeným změnám.
        myPlantList.writePlantListToFile("kvetiny_new.txt", myPlantList);

        // Vyzkoušej opětovné načtení vygenerovaného souboru.
        PlantListManager myNewPlantList = new PlantListManager();
        myNewPlantList = myNewPlantList.readPlantsListFromFile("kvetiny_new.txt");

        // Vyzkoušej seřazení rostlin ve správci seznamu podle různých kritérií a výpis seřazeného seznamu.

        // podle názvu -  jako výchozí varianta řazení rostlin
        System.out.println("Seřazený list rostlin podle názvu:");
        Collections.sort(myNewPlantList.getPlantList());

        for (int i = 0; i < myNewPlantList.getPlantList().size(); i++) {
            Plant plant = myNewPlantList.getPlantList().get(i);

            if (i == myNewPlantList.getPlantList().size() - 1) {
                System.out.print(plant.getName() + ".");
            } else {
                System.out.print(plant.getName() + ", ");
            }
        }
        System.out.println();
        System.out.println("-------------------------");

        // podle data poslední zálivky
        System.out.println("Seřazený list poslední zálivky:");
        myNewPlantList.getPlantList().sort(Comparator.comparing(Plant::getWatering));

        for (Plant plant : myNewPlantList.getPlantList()) {
            System.out.println("Název rostliny: " + plant.getName() + ", Datum podlední zálivky: " + plant.getWatering().format(formatter) + ".");
        }
        System.out.println("-------------------------");
    }

    public static void interniTesty() throws PlantException {
        // test Úkol 1: Model dat
        Plant newPlant1 = new Plant("Plant3", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), 5);
        Plant newPlant2 = new Plant("Plant2", 3);
        Plant newPlant3 = new Plant("Plant1");

        // metoda getWateringInfo
        System.out.println(newPlant1.getWateringInfo());
        System.out.println(newPlant2.getWateringInfo());
        System.out.println(newPlant3.getWateringInfo());

        // metoda doWateringNow
        newPlant1.doWateringNow();
        System.out.println(newPlant1.getWateringInfo());

        // test Úkol 4: Správa seznamu květin
        PlantListManager newPlantList = new PlantListManager();

        // metoda addNewPlant
        newPlantList.addNewPlant(newPlant1);
        newPlantList.addNewPlant(newPlant2);
        newPlantList.addNewPlant(newPlant3);

        for (Plant plant : newPlantList.getPlantList()) {
            System.out.println(plant.getName());
        }

        // metoda getPlantByIndex
        System.out.println("Nazev prvni rostliny v seznamu: " + newPlantList.getPlantByIndex(0).getName());

        // metoda removePlantByIndex
        newPlantList.removePlantByIndex(0);
        System.out.println("Nazev nove prvni rostliny v seznamu: " + newPlantList.getPlantByIndex(0).getName());

        // metoda copyPlantList
        PlantListManager copyNewPlantList1 = new PlantListManager();
        copyNewPlantList1.copyPlantList(newPlantList.getPlantList());

        for (Plant plant : copyNewPlantList1.getPlantList()) {
            System.out.println("Kopie rostliny" + plant.getName());
        }

        // metoda getPlantsNeedToWater
        Plant newPlant4 = new Plant("Plant4", "test", LocalDate.of(2025, 4, 1), LocalDate.of(2026, 2, 1), 2);
        newPlantList.addNewPlant(newPlant4);

        PlantListManager PlantsNeedToWater = newPlantList.getPlantsNeedToWater();

        for (Plant plant : PlantsNeedToWater.getPlantList()) {
            System.out.println("Potřebuji zalit " + plant.getName());
        }

        //Řazení a práce s rostlinami

        // podle názvu -  jako výchozí varianta řazení rostlin
        System.out.println("Seřazený list podle názvu:");
        Collections.sort(newPlantList.getPlantList());
        for (Plant plant : newPlantList.getPlantList()) {
            System.out.println(plant.getName());
        }

        // podle data poslední zálivky
        System.out.println("Seřazený list poslední zálivky:");
        newPlantList.getPlantList().sort(Comparator.comparing(Plant::getWatering));

        for (Plant plant : newPlantList.getPlantList()) {
            System.out.println(plant.getName() + " " + plant.getWatering());
        }

        // test chybových hlášek
        System.out.println("Test chybových hlášek:");

        // prázdný string jako název rostliny
        try {
            Plant newPlant99 = new Plant("", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), 5);
        } catch (PlantException e) {
            System.out.println("Chyba při vytváření položky: " + e.getMessage());
        }

        // jedna mezera jako název rostliny
        try {
            Plant newPlant98 = new Plant(" ", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), 5);
        } catch (PlantException e) {
            System.out.println("Chyba při vytváření položky: " + e.getMessage());
        }

        // nastaveni data poslední zálivky
        try {
            Plant newPlant97 = new Plant("newPlant97", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2022, 3, 1), 5);
        } catch (PlantException e) {
            System.out.println("Chyba při vytváření položky: " + e.getMessage());
        }

        // nastaveni zadávání frekvence zálivky - hodnota 0
        try {
            Plant newPlant96 = new Plant("newPlant96", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), 0);
        } catch (PlantException e) {
            System.out.println("Chyba při vytváření položky: " + e.getMessage());
        }

        // nastaveni zadávání frekvence zálivky - záporná hodnota
        try {
            Plant newPlant96 = new Plant("newPlant96", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), -5);
        } catch (PlantException e) {
            System.out.println("Chyba při vytváření položky: " + e.getMessage());
        }

        // získání květiny na špatném indexu
        try {
            System.out.println("Nazev prvni rostliny v seznamu: " + newPlantList.getPlantByIndex(100).getName());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Chyba při získání rostliny: " + e.getMessage());
        }

        // odebrání květiny ze seznamu na špatném indexu
        try {
            newPlantList.removePlantByIndex(100);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Chyba při odebrání rostliny ze seznamu: " + e.getMessage());
        }

        // test report kvetiny-spatne-datum.txt
        PlantListManager newPlantListManager = new PlantListManager();
        try {
            newPlantListManager.readPlantsListFromFile("kvetiny-spatne-datum.txt");
        } catch (PlantException e) {
            System.out.println("Chyba při načítaní souboru: " + e.getMessage());
        }

        // test report kvetiny-spatne-frekvence.txt
        PlantListManager newPlantListManager2 = new PlantListManager();
        try {
            newPlantListManager2.readPlantsListFromFile("kvetiny-spatne-frekvence.txt");
        } catch (PlantException e) {
            System.out.println("Chyba při načítaní souboru: " + e.getMessage());
        }

        // test report kvetiny-prazdny-radek.txt
        PlantListManager newPlantListManager3 = new PlantListManager();
        try {
            newPlantListManager3.readPlantsListFromFile("kvetiny-prazdny-radek.txt");
        } catch (PlantException e) {
            System.out.println("Chyba při načítaní souboru: " + e.getMessage());
        }

        // test report kvetiny-malo-hodnot.txt
        PlantListManager newPlantListManager4 = new PlantListManager();
        try {
            newPlantListManager4.readPlantsListFromFile("kvetiny-malo-hodnot.txt");
        } catch (PlantException e) {
            System.out.println("Chyba při načítaní souboru: " + e.getMessage());
        }
    }
}