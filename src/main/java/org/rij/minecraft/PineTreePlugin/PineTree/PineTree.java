package org.rij.minecraft.PineTreePlugin.PineTree;


import org.rij.minecraft.PineTreePlugin.PineTree.REST.Spark;

import java.util.List;

public class PineTree {

    private static Registrator registrator = new Registrator();


    public static void init(PTScanType type, int BindPort){
        registrator.setType(type);
        Spark.startandAwait(BindPort);
    }

    @SuppressWarnings({})
    public static Registrator getRegistrator() {
        return registrator;
    }

    public static List<String> getPluginsNames(){
        return registrator.pluginNames;
    }
}
