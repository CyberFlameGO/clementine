package me.notom3ga.clementine;

import me.lucko.commodore.Commodore;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Represents a manager for dealing with Clementine.
 */
public class Clementine {

    private final JavaPlugin plugin;
    private final Commodore commodore;
    private CommandMap commandMap;

    private Executor asyncExecutor;
    private boolean shouldRunAsync = false;

    /**
     * Creates a new Clementine instance.
     *
     * @param plugin The {@link org.bukkit.plugin.java.JavaPlugin} instance to register Clementine under.
     */
    public Clementine(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.commodore = CommodoreProvider.getCommodore(plugin);

        try {
            Field cmdMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cmdMap.setAccessible(true);
            commandMap = (CommandMap) cmdMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new {@link ClementineCommand} under Clementine.
     *
     * @param command The {@link ClementineCommand} to register.
     */
    public void register(@NotNull ClementineCommand command) {
        commandMap.register(command.getName(), command);
        org.bukkit.command.Command bukkitCommand = Objects.requireNonNull(commandMap.getCommand(command.getName()));

        if (command.getClementinePermission() != null) {
            bukkitCommand.setPermission(command.getClementinePermission());
        }

        if (command.getClementineDescription() != null) {
            bukkitCommand.setDescription(command.getClementineDescription());
        }

        bukkitCommand.setAliases(command.getClementineAliases());

        commodore.register(command, command.getStructure());
    }

    /**
     * Checks if the executor is running async.
     *
     * @return If the executor is async.
     */
    public boolean isAsync() {
        return this.shouldRunAsync;
    }

    /**
     * Set if the executor should run async.
     *
     * @param setup If the executor should run async.
     */
    public void setAsync(boolean setup) {
        if (setup) {
            this.shouldRunAsync = true;
            this.asyncExecutor = Executors.newFixedThreadPool(1);
        } else {
            this.shouldRunAsync = false;
            this.asyncExecutor = null;
        }
    }

    public Executor getAsyncExecutor() {
        return this.asyncExecutor;
    }
}
