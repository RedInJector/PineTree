package org.rij.minecraft.PineTreePlugin.Velocity.Commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.rij.minecraft.PineTreePlugin.Velocity.Servers;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PineTreeServerCommand implements SimpleCommand {

    private final Logger logger;
    private final Servers servers;

    public PineTreeServerCommand(Logger logger, Servers servers) {
        this.logger = logger;
        this.servers = servers;
    }

    @Override
    public void execute(final SimpleCommand.Invocation invocation) {
        CommandSource source = invocation.source();
        // Get the arguments after the command alias
        String[] args = invocation.arguments();

        if(args.length < 1 || args.length > 2) {
            source.sendMessage(Component.text("Error").color(NamedTextColor.DARK_RED));
            return;
        }
        if(args[0].equals("add")) {
            servers.addServer(args[1]);

            source.sendMessage(Component.text("Success! added " + args[1]).color(NamedTextColor.AQUA));
        }
        if(args[0].equals("remove")) {
            servers.removeServer(args[1]);

            source.sendMessage(Component.text("Success! removed" + args[1]).color(NamedTextColor.AQUA));
        }
        if(args[0].equals("show")) {
            StringBuilder s = new StringBuilder();

            for(String ip : servers.getServers())
                s.append(ip).append("\n");

            source.sendMessage(Component.text(s.toString()).color(NamedTextColor.AQUA));
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
            poss.add("add");
            poss.add("remove");
            poss.add("show");
        }

        return CompletableFuture.completedFuture(poss);
    }



}
