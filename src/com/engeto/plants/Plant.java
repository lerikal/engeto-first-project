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

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d. M. yyyy"); // format pro datum
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
        if (watering == null) {
            throw new PlantException("Datum poslední zálivky nesmí být prázdný.");
        }

        if (watering.isBefore(planted)) {
            throw new PlantException("Datum poslední zálivky nesmí být starší než datum zasazení rostliny! Datum zasazení je: " + planted.format(FORMATTER) +
                    ". Datum poslední zálivky je: " + watering.format(FORMATTER) + ".");
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
     * Vrátí textovou informaci o rostlině a její zálivce.
     *
     * Obsahuje:
     * - název rostliny
     * - datum poslední zálivky
     * - datum doporučené další zálivky
     *
     * @return textová informace o zálivce
     */
    public String getWateringInfo() {
        LocalDate recommendedWatering = this.getWatering().plusDays(this.frequencyOfWatering);

        return "Název květiny: " + this.getName()
           + ", datum poslední zálivky: " + this.getWatering().format(FORMATTER)
           + ", datum doporučené další zálivky: " + recommendedWatering.format(FORMATTER)
           ;
    }

    /**
     * Nastaví datum poslední zálivky na dnešní datum.
     *
     * Pokud je dnešní datum menší než datum zasazení, vyhodí výjimku.
     *
     * @throws PlantException pokud je dnešní datum před datem zasazení
     */
    public void doWateringNow() throws PlantException {
        if (LocalDate.now().isBefore(planted)) {
            throw new PlantException("Nelze nastavit datum poslední zálevky na dnešní datum. Datum zasazení je větší než dnešní datum. Datum zasazení je: " + planted.format(FORMATTER));
        }
        this.watering = LocalDate.now();
    }

    /**
     * Výchozí řazení rostlin podle názvu.
     *
     * Porovnává názvy rostlin lexikograficky.
     *
     * @param other druhá rostlina pro porovnání
     * @return záporné číslo, pokud je název této rostliny menší,
     *         0 pokud jsou názvy stejné,
     *         kladné číslo, pokud je název této rostliny větší
     */
    @Override
    public int compareTo(Plant other) {
        return this.name.compareTo(other.name);
    }

    /**
     * Vytvoří objekt rostliny z řádku načteného ze souboru.
     *
     * Očekávaný formát řádku:
     * název, poznámka, frekvence zálivky, datum zálivky, datum zasazení (odděleno tabulátorem)
     *
     * @param row řádek se záznamem o rostlině
     * @return objekt typu Plant
     * @throws PlantException pokud je řádek neplatný nebo obsahuje chybná data
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
