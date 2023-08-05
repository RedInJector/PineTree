package org.rij.minecraft.PineTreePlugin.Velocity;

import org.rij.minecraft.PineTreePlugin.Velocity.DevKit.VelocityFile;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Servers {
    private final Logger logger;
    private final Path dataDirectory;

    private static VelocityFile servers;

    public Servers(Logger logger, Path dataDirectory) {
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        servers = new VelocityFile(this.logger, this.dataDirectory, "servers.yml");
        servers.init(this.getClass());


        if(servers.getListByPath("servers", String.class) == null) {

            List<String> l = new ArrayList<>();
            servers.save("servers", l);
        }
    }

    public void addServer(String name){
        List<String> l = servers.getListByPath("servers", String.class);
        if(l.contains(name.toLowerCase()))
            return;

        l.add(name.toLowerCase());
        servers.save("servers", l);
    }

    public void removeServer(String name){
        List<String> l = servers.getListByPath("servers", String.class);
        l.remove(name.toLowerCase());
        servers.save("servers", l);
    }

    public List<String> getServers(){
        return servers.getListByPath("servers", String.class);
    }

    public static List<String> getServerIps(){
        return servers.getListByPath("servers", String.class);
    }
}
