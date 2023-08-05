package org.rij.minecraft.PineTreePlugin.Velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.rij.minecraft.PineTree.PineTree;
import org.rij.minecraft.PineTreePlugin.Velocity.Commands.PineTreeServerCommand;
import org.rij.minecraft.PineTreePlugin.Velocity.Commands.PineTreeCommand;
import org.rij.minecraft.PineTreePlugin.Velocity.DevKit.VelocityFile;
import org.rij.minecraft.PineTreePlugin.common.PineTreePlugin;
import org.rij.minecraft.PineTreePlugin.common.ResourceFile;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;



@Plugin(
        id = "pinetreevelocity",
        name = "PineTreeVelocity",
        version = "1.0.0"
)
public class PTVelocity implements PineTreePlugin {

    public static PineTreePlugin pineTreePlugin = null;

    private final Logger logger;
    private final ProxyServer server;
    private final Path dataDirectory;
    public VelocityFile config;
    public static Servers servers;

    @Inject
    public PTVelocity(Logger logger, ProxyServer server, @DataDirectory Path dataDirectory){
        this.logger = logger;
        this.server = server;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        pineTreePlugin = this;
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        config = new VelocityFile(logger, dataDirectory, "config.yml");
        config.init(this.getClass());

        PineTree.init(Integer.parseInt(config.getStringByPath("PineTreePort")));

        CommandManager commandManager = server.getCommandManager();
        CommandMeta PTMeta = commandManager.metaBuilder("pineTree")
                .aliases("PT")
                .plugin(this)
                .build();

        commandManager.register(PTMeta, new PineTreeCommand());

        CommandMeta AddPTServerMeta = commandManager.metaBuilder("PineTreeServer")
                .aliases("PTServer")
                .plugin(this)
                .build();

        servers = new Servers(logger, dataDirectory);

        commandManager.register(AddPTServerMeta, new PineTreeServerCommand(logger, servers));

        DrawArt();
    }

    private void DrawArt(){
        logger.info("                v");
        logger.info("               >X<");
        logger.info("                A");
        logger.info("               d$b");
        logger.info("             .d\\$$b.");
        logger.info("           .d$i$$\\$$b.");
        logger.info("              d$$@b");
        logger.info("             d\\$$$ib");
        logger.info("           .d$$$\\$$$b");
        logger.info("         .d$$@$$$$\\$$ib.");
        logger.info("             d$$i$$b");
        logger.info("            d\\$$$$@$b");
        logger.info("         .d$@$$\\$$$$$@b.");
        logger.info("       .d$$$$i$$$\\$$$$$$b.");
        logger.info("               ###");
        logger.info("               ###");
        logger.info("               ###");
        logger.info("---PineTree running on port: " + config.getStringByPath("PineTreePort") + "---");
    }

    @Override
    public ResourceFile getPineTreeConfig(){
        return config;
    }

    @Override
    public Path getDataDirectory() {
        return null;
    }

}
