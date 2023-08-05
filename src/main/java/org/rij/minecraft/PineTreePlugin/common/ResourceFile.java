package org.rij.minecraft.PineTreePlugin.common;

import java.io.File;
import java.nio.file.Path;

public interface ResourceFile {
    void init(Class clazz);
    String getStringByPath(String path);
    int getIntByPath(String path);
    boolean getboolByPath(String path);

    Object getValue(String path);
    void save(String path, Object object);

    default File getFileFromPath(Path folderPath, String fileName) {
        return folderPath.resolve(fileName).toFile();
    }

}
