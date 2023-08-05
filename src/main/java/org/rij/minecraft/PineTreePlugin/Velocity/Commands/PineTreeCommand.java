package org.rij.minecraft.PineTreePlugin.Velocity.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.rij.minecraft.PineTree.PineTree;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PineTreeCommand implements SimpleCommand  {

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {
        CommandSource source = invocation.source();
        // Get the arguments after the command alias
        String[] args = invocation.arguments();

        if(args.length != 1) {
            source.sendMessage(Component.text("Error").color(NamedTextColor.DARK_RED));
            return;
        }
        if(args[0].equals("getextensions")) {
            StringBuilder result = new StringBuilder();

            for (String record : PineTree.getPluginsNames()) {
                result.append("\n").append(record); // Double space here
            }

            source.sendMessage(Component.text(result.toString()).color(NamedTextColor.GREEN));
        }
    }

    @Override
    public boolean hasPermission(final SimpleCommand.Invocation invocation) {
        return invocation.source().hasPermission("PineTree.commands");
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final SimpleCommand.Invocation invocation) {
        List<String> poss = new ArrayList<>();
        if(invocation.arguments().length < 1) {
            poss.add("getextensions");
        }

        return CompletableFuture.completedFuture(poss);
    }

}
