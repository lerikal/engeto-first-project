import java.time.LocalDate;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        // test Úkol 1: Model dat
        Plant newPlant1 = new Plant("Plant1", "test", LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 1), 5);
        Plant newPlant2 = new Plant("Plant2", 3);
        Plant newPlant3 = new Plant("Plant3");

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
        Plant newPlant4 = new Plant("Plant4", "test", LocalDate.of(2026, 4, 1), LocalDate.of(2026, 3, 1), 2);
        newPlantList.addNewPlant(newPlant4);

        PlantListManager PlantsNeedToWater = newPlantList.getPlantsNeedToWater();

        for (Plant plant : PlantsNeedToWater.getPlantList()) {
            System.out.println("Potřebuji zalit " + plant.getName());
        }
    }
}