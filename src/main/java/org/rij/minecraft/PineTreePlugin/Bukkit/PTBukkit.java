package org.rij.minecraft.PineTreePlugin.Bukkit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.rij.minecraft.PineTreePlugin.Bukkit.Commands.PineTreeCommand;
import org.rij.minecraft.PineTreePlugin.Bukkit.DevKit.BukkitFile;
import org.rij.minecraft.PineTree.PineTree;
import org.rij.minecraft.PineTreePlugin.common.PineTreePlugin;
import org.rij.minecraft.PineTreePlugin.common.ResourceFile;

import java.nio.file.Path;
import java.util.logging.Logger;

public class PTBukkit extends JavaPlugin implements PineTreePlugin {

    public static PineTreePlugin pineTreePlugin = null;
    private ResourceFile config;
    private Logger logger;


    @Override
    public void onEnable() {
        pineTreePlugin = this;

        logger = Bukkit.getLogger();

        config = new BukkitFile(logger, this.getDataFolder().toPath(), "bukkitconfig.yml");
        config.init(this.getClass());

        PineTree.init(Integer.parseInt(config.getStringByPath("PineTreePort")));


        PineTreeCommand pineTreeCommand = new PineTreeCommand();
        this.getCommand("pineTree").setExecutor(pineTreeCommand);
        getCommand("pineTree").setTabCompleter(pineTreeCommand);


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
    public void onDisable() {

    }


    @Override
    public ResourceFile getPineTreeConfig() {
        return config;
    }

    @Override
    public Path getDataDirectory() {
        return this.getDataFolder().toPath();
    }
}
