package com.engeto.plants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Plant implements Comparable<Plant> {
    private String name; // název
    private String notes; // poznámky
    private LocalDate planted; // datum, kdy byly zasazena
    private LocalDate watering; // datum poslední zálivky
    private int frequencyOfWatering; // běžnou frekvenci zálivky ve dnech

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d. M. yyyy"); // format pro datum
    private static final String DELIMITER = "\t";

    // Konsturktory

    // jeden pro nastavení všech atributů
    public Plant(String name, String notes, LocalDate planted, LocalDate watering, int frequencyOfWatering) throws PlantException {
        this.setName(name);
        this.notes = notes;
        this.setPlanted(planted);
        this.setWatering(watering);
        this.setFrequencyOfWatering(frequencyOfWatering);
    }

    // druhý nastaví jako poznámku prázdný řetězec a datum zasazení i datum poslední zálivky nastaví na dnešní datum
    public Plant(String name, int frequencyOfWatering) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), frequencyOfWatering);
    }

    // třetí nastaví totéž co druhý a navíc výchozí frekvenci zálivky na 7 dnů
    public Plant(String name) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), 7);
    }

    // Výchozí přístupové metody pro všechny atributy
    public String getName() {
        return name;
    }

    public void setName(String name) throws PlantException {
        if (name == null || name.isBlank()) {
            throw new PlantException("Název rostliny nemůže být prázdný! Zadejte název rostliny korektně.");
        }
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

    public void setPlanted(LocalDate planted) throws PlantException {
        if (planted == null) {
            throw new PlantException("Datum zasazení rostliny nemůže být prázdný! Zadejte datum zasazení rostliny korektně.");
        }
        this.planted = planted;
    }

    public LocalDate getWatering() {
        return watering;
    }

    public void setWatering(LocalDate watering) throws PlantException {
        // zadávání data poslední zálivky — nesmí být starší než datum zasazení rostliny
        if (watering.isBefore(planted)) {
            throw new PlantException("Datum poslední zálivky nesmí být starší než datum zasazení rostliny! Datum zasazení je: " + planted.format(formatter) +
                    ". Datum poslední zálivky je: " + watering.format(formatter) + ".");
        }
        this.watering = watering;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) throws PlantException {
        // zadávání frekvence zálivky — pokud je parametrem 0 nebo záporné číslo
        if (frequencyOfWatering <= 0) {
            throw new PlantException("Frekvence zálivky nesmí být záporná nebo má být větší než 0! Zadaná hodnota je: " + frequencyOfWatering + ".");
        }
        this.frequencyOfWatering = frequencyOfWatering;
    }

    // Metody
    /**
     * Vrátí textovou informaci obsahující název květiny, datum poslední zálivky a datum doporučené další zálivky.
     * @return obsahující název květiny, datum poslední zálivky a datum doporučené další zálivky.
     */
    public String getWateringInfo() {
        LocalDate recommendedWatering = this.getWatering().plusDays(this.frequencyOfWatering);

        return "Název květiny: " + this.getName()
           + ", datum poslední zálivky: " + this.getWatering().format(formatter)
           + ", datum doporučené další zálivky: " + recommendedWatering.format(formatter)
           ;
    }
    /**
     * Nastaví datum poslední zálivky na dnešní den.
     */
    public void doWateringNow() throws PlantException {
        if (LocalDate.now().isBefore(planted)) {
            throw new PlantException("Nelze nastavit datum poslední zálevky na dnešní datum. Datum zasazení je větší než dnešní datum. Datum zasazení je: " + planted.format(formatter));
        }
        this.watering = LocalDate.now();
    }

    /**
     * Řazení podle názvu rostliny nastav jako výchozí variantu řazení rostlin.
     * @return celé číslo, které říká, jak si dva objekty stojí: záporné číslo (< 0) - this.name je lexikograficky menší než other.name, 0 → oba názvy jsou stejné, kladné číslo (> 0) → this.name je lexikograficky větší než other.name
     */
    @Override
    public int compareTo(Plant other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Metoda na parsování dat ze souboru.
     * @return objekt třidy Plant.
     */
    public static Plant parsePlant(String row) throws PlantException {
        if (row == null || row.isBlank()) {
            throw new PlantException(" Prázdný řádek v souboru.");
        }

        String[] items = row.split(DELIMITER, -1);
        if (items.length < 5) {
            throw new PlantException(" Na řádku je méně hodnot než je potřeba: " + items.length + " hodnot z 5. Řádek: " + row + ".");
        }

        try {
            String name = items[0];  // název
            String notes = items[1]; // poznámky
            LocalDate planted = LocalDate.parse(items[4]); // datum, kdy byly zasazena
            LocalDate watering = LocalDate.parse(items[3]); // datum poslední zálivky
            int frequencyOfWatering = Integer.parseInt(items[2]); // běžnou frekvenci zálivky ve dnech

            return new Plant(name, notes, planted, watering, frequencyOfWatering);

        } catch (NumberFormatException e) {
            throw new PlantException(" Neplatné číslo: " + items[2] + ".");
        } catch (DateTimeParseException e) {
            throw new PlantException(" Neplatný formát data. Řádek: " + row + ".");
        }
    }
}
