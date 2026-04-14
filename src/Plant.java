import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Plant implements Comparable<Plant> {
    private String name; // název
    private String notes; // poznámky
    private LocalDate planted; // datum, kdy byly zasazena
    private LocalDate watering; // datum poslední zálivky
    private int frequencyOfWatering; // běžnou frekvenci zálivky ve dnech

    // Konsturktory

    // jeden pro nastavení všech atributů
    public Plant(String name, String notes, LocalDate planted, LocalDate watering, int frequencyOfWatering) {
        this.name = name;
        this.notes = notes;
        this.planted = planted;
        this.watering = watering;
        this.frequencyOfWatering = frequencyOfWatering;
    }

    // druhý nastaví jako poznámku prázdný řetězec a datum zasazení i datum poslední zálivky nastaví na dnešní datum
    public Plant(String name, int frequencyOfWatering) {
        this(name, "", LocalDate.now(), LocalDate.now(), frequencyOfWatering);
    }

    // třetí nastaví totéž co druhý a navíc výchozí frekvenci zálivky na 7 dnů
    public Plant(String name) {
        this(name, "", LocalDate.now(), LocalDate.now(), 7);
    }

    // Výchozí přístupové metody pro všechny atributy
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public void setPlanted(LocalDate planted) {
        this.planted = planted;
    }

    public LocalDate getWatering() {
        return watering;
    }

    public void setWatering(LocalDate watering) {
        this.watering = watering;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) {
        this.frequencyOfWatering = frequencyOfWatering;
    }

    // Metody

    // Vrátí textovou informaci obsahující název květiny, datum poslední zálivky a datum doporučené další zálivky.
    public String getWateringInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy"); // format pro datum
        LocalDate recommendedWatering = this.getWatering().plusDays(this.frequencyOfWatering);

        return "Název květiny: " + this.getName()
           + ", datum poslední zálivky: " + this.getWatering().format(formatter)
           + ", datum doporučené další zálivky: " + recommendedWatering.format(formatter)
           ;
    }

    // Nastaví datum poslední zálivky na dnešní den.
    public void doWateringNow() {
        this.watering = LocalDate.now();
    }

    // Možnost seřadit rostliny v seznamu podle názvu.
    // Řazení podle názvu rostliny nastav jako výchozí variantu řazení rostlin.
    @Override
    public int compareTo(Plant other) {
        return this.name.compareTo(other.name);
    }
}
