package org.rij.minecraft.PineTreePlugin.Bukkit.DevKit;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.rij.minecraft.PineTreePlugin.common.ResourceFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitFile implements ResourceFile {

    //private final FileConfiguration config;
    private FileConfiguration config;
    private File file;
    private final Logger logger;
    private final String fileName;
    private final Path dataDirectory;

    public BukkitFile(Logger logger, Path dataDirectory, String fileName) {
        this.logger = logger;
        this.fileName = fileName;
        this.dataDirectory = dataDirectory;
    }


    @Override
    public void init(Class clazz) {

        File folder = dataDirectory.toFile();
        if (!folder.exists() && !folder.mkdir()) {
            logger.info("Can't create the folder!");
            return;
        }

        if (getFileFromPath(dataDirectory, fileName).exists()) {
            Load();
            return;
        }

        InputStream is = clazz.getClassLoader().getResourceAsStream(fileName);

        try {
            Files.copy(is, dataDirectory.resolve(fileName));
            is.close();
            Load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to create file " + fileName + ": ");
            logger.log(Level.SEVERE,e.getMessage());
        }
    }

    private void Load() {
        File file = getFileFromPath(dataDirectory, fileName);
        this.file = file;

        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public String getStringByPath(String path) {
        return config.getString(path);
    }

    @Override
    public int getIntByPath(String path) {
        return config.getInt(path);
    }

    @Override
    public boolean getboolByPath(String path) {
        return config.getBoolean(path);
    }

    @Override
    public Object getValue(String path) {
        return config.get(path);
    }

    @Override
    public void save(String path, Object object) {
        config.set(path, object);

        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
