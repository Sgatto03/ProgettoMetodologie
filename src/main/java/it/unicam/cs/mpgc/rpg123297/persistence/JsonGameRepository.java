package it.unicam.cs.mpgc.rpg123297.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Implementazione di {@link GameRepository} che usa file JSON per la
 * persistenza.
 * Ogni slot di salvataggio corrisponde a un file JSON nella cartella dei
 * salvataggi.
 * Usa la libreria Gson per la serializzazione/deserializzazione.
 */
public class JsonGameRepository implements GameRepository {

    private static final String SAVE_DIR = "saves";
    private static final String FILE_EXTENSION = ".json";

    private final Gson gson;
    private final Path saveDirectory;

    /**
     * Crea un nuovo repository JSON con la directory di salvataggio predefinita.
     */
    public JsonGameRepository() {
        this(Paths.get(System.getProperty("user.dir"), SAVE_DIR));
    }

    /**
     * Crea un nuovo repository JSON con una directory personalizzata.
     * 
     * @param saveDirectory la cartella dove salvare i file JSON
     */
    public JsonGameRepository(Path saveDirectory) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.saveDirectory = saveDirectory;
        ensureSaveDirectoryExists();
    }

    @Override
    public boolean save(SaveData saveData, String slotName) {
        try {
            ensureSaveDirectoryExists();
            String json = gson.toJson(saveData);
            Path filePath = getFilePath(slotName);
            Files.writeString(filePath, json);
            return true;
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Optional<SaveData> load(String slotName) {
        try {
            Path filePath = getFilePath(slotName);
            if (!Files.exists(filePath)) {
                return Optional.empty();
            }
            String json = Files.readString(filePath);
            SaveData data = gson.fromJson(json, SaveData.class);
            return Optional.ofNullable(data);
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(String slotName) {
        try {
            Path filePath = getFilePath(slotName);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Errore durante l'eliminazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> listSaves() {
        List<String> saves = new ArrayList<>();
        if (!Files.exists(saveDirectory)) {
            return saves;
        }
        try (Stream<Path> stream = Files.list(saveDirectory)) {
            stream.filter(p -> p.toString().endsWith(FILE_EXTENSION))
                    .map(p -> p.getFileName().toString())
                    .map(name -> name.substring(0, name.length() - FILE_EXTENSION.length()))
                    .forEach(saves::add);
        } catch (IOException e) {
            System.err.println("Errore nel listare i salvataggi: " + e.getMessage());
        }
        return saves;
    }

    @Override
    public boolean exists(String slotName) {
        return Files.exists(getFilePath(slotName));
    }

    /** Costruisce il path del file per lo slot dato. */
    private Path getFilePath(String slotName) {
        return saveDirectory.resolve(slotName + FILE_EXTENSION);
    }

    /** Crea la directory di salvataggio se non esiste. */
    private void ensureSaveDirectoryExists() {
        try {
            Files.createDirectories(saveDirectory);
        } catch (IOException e) {
            System.err.println("Impossibile creare la directory dei salvataggi: " + e.getMessage());
        }
    }
}
