package com.pluralsight.rxjava2.nitrite;

import org.dizitart.no2.Nitrite;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class NitriteTestDatabase implements Closeable {

    private Nitrite nitriteDatabase;
    private Path nitriteDatabasePath;
    private NitriteSchema nitriteSchema;

    public NitriteTestDatabase(Optional<NitriteSchema> schema) throws IOException {
        internalConstruct("testDatabase.nitrite", schema);
    }

    public NitriteTestDatabase(String databaseFilename, Optional<NitriteSchema> schema) throws IOException {
        internalConstruct(databaseFilename, schema);
    }

    private void internalConstruct(String databaseFilename, Optional<NitriteSchema> schema) throws IOException {
        String userHomeDirectory = System.getProperty("user.home");
        Path testDirectory = Path.of(userHomeDirectory, "PluralSight");
        Path testDatabase = Path.of(userHomeDirectory, "PluralSight", databaseFilename);

        if( !Files.exists(testDirectory) )
        {
            Files.createDirectory(testDirectory);
        }

        if( Files.exists(testDatabase)) {
            Files.delete(testDatabase);
        }

        nitriteDatabasePath = testDatabase;
        nitriteSchema = schema.orElse(null);

        nitriteDatabase = Nitrite.builder()
                .compressed()
                .filePath(testDatabase.toFile())
                .openOrCreate("admin", "admin");

        // If a schema was supplied, ask it to apply itself.
        if( schema.isPresent()) {
            schema.get().applySchema(this.nitriteDatabase);
        }
    }

    @Override
    public void close() throws IOException {
        nitriteDatabase.close();

        // Get rid of the testing file
        Files.delete(nitriteDatabasePath);
    }

    public Nitrite getNitriteDatabase() {
        return nitriteDatabase;
    }
}
