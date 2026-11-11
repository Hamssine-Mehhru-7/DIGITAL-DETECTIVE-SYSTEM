package com.digitaldetective;

import com.digitaldetective.database.DatabaseConnection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseStartup implements CommandLineRunner {
    @Override public void run(String... args) { DatabaseConnection.initializeDatabase(); }
}
