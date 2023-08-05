package org.rij.minecraft.PineTreePlugin.Bukkit.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.rij.minecraft.PineTree.PineTree;

import java.util.ArrayList;
import java.util.List;

public class PineTreeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(args.length != 1) {
            commandSender.sendMessage(Component.text("Error").color(NamedTextColor.DARK_RED));
            return false;
        }
        if(args[0].equals("getextensions")) {
            StringBuilder result = new StringBuilder();

            for (String record : PineTree.getPluginsNames()) {
                result.append("\n").append(record); // Double space here
            }

            commandSender.sendMessage(Component.text(result.toString()).color(NamedTextColor.GREEN));
        }

        return true;
    }


    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args){
        List<String> poss = new ArrayList<>();
        if(args.length <= 1) {
            poss.add("getextensions");
        }

        return poss;
    }

}
