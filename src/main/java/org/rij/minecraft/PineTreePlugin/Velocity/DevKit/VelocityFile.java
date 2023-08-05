package org.rij.minecraft.PineTreePlugin.Velocity.DevKit;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.rij.minecraft.PineTreePlugin.common.ResourceFile;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class VelocityFile implements ResourceFile {

    private final Logger logger;
    private final Path dataDirectory;
    private ConfigurationNode rootNode;
    private ConfigurationLoader<?> loader;
    private final String fileName;


    public VelocityFile(Logger logger, Path dataDirectory, String fileName) {
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.fileName = fileName;
    }

    public void init(Class clazz) {

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
            logger.error("Unable to create file " + fileName + ": ");
            logger.trace(e.getMessage());
        }
    }

    private void Load() {

        Path filePath = getFileFromPath(dataDirectory, fileName).toPath();

        try {
            loader = YAMLConfigurationLoader.builder()
                    .setPath(filePath)
                    .build();

            rootNode = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStringByPath(String path) {
        ConfigurationNode node = rootNode.getNode(path.split("\\."));

        if (node.isVirtual()) {
            throw new IllegalArgumentException("Path is invalid.");
        }

        return node.getString();
    }

    public int getIntByPath(String path) {
        ConfigurationNode node = rootNode.getNode(path.split("\\."));

        if (node.isVirtual()) {
            throw new IllegalArgumentException("Path is invalid.");
        }

        return node.getInt();


    }

    public boolean getboolByPath(String path) {
        ConfigurationNode node = rootNode.getNode(path.split("\\."));

        if (node.isVirtual()) {
            throw new IllegalArgumentException("Path is invalid.");
        }

        return node.getBoolean();

    }

    public <T> List<T> getListByPath(String path, Class<T> elementType) {
        ConfigurationNode node = rootNode.getNode(path.split("\\."));

        if (node.isVirtual()) {
            return null;
        }

        List<T> list = null;
        try {
            list = new ArrayList<>(node.getList(TypeToken.of(elementType)));
        } catch (ObjectMappingException ignored) {
        }
        return list;
    }

    public Object getValue(String path) {

        ConfigurationNode node = rootNode.getNode(path.split("\\."));

        if (node.isVirtual()) {
            return null;
        }

        return node.getValue();


    }

    public void save(String path, Object object) {
        try {
            ConfigurationNode node = rootNode.getNode(path.split("\\."));

            node.setValue(object);
            loader.save(rootNode);

            rootNode = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
