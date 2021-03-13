package me.notom3ga.clementine;

import com.mojang.brigadier.tree.LiteralCommandNode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    protected final Clementine clementine;
    private final String cName;
    private final List<String> cAliases;
    private String cPermission;
    private String cDescription;
    private Boolean playerOnly = false;
    private String playerOnlyMessage = ChatColor.translateAlternateColorCodes('&', "&cError: &7This command can only be ran by players.");

    /**
     * Create a new {@link Clementine} {@link Command}.
     *
     * @param clementine The {@link Clementine} instance of the plugin.
     * @param name The main name of the command.
     */
    protected Command(Clementine clementine, String name, String... aliases) {
        super(name);

        this.clementine = clementine;
        this.cName = name;

        this.cAliases = new ArrayList<>();
        cAliases.addAll(Arrays.asList(aliases));
        setAliases(cAliases);
    }

    /**
     * Gets the name that can be used to execute the command.
     *
     * @return The name of the command.
     */
    @NotNull
    public String getClementineName() {
        return this.cName;
    }

    /**
     * Gets the aliases that can be used to execute the command.
     *
     * @return A {@link List} of all the command's aliases.
     */
    @NotNull
    public List<String> getClementineAliases() {
        return this.cAliases;
    }

    /**
     * Gets the permission node the {@link CommandSender} must have to run the command.
     *
     * @return The permission node the {@link CommandSender} must have to run the command.
     */
    @Nullable
    public String getClementinePermission() {
        return this.cPermission;
    }

    /**
     * Sets the permission node the {@link CommandSender} must have to run the command. This can not be changed after registration.
     *
     * @param perm The permission node the {@link CommandSender} must have to run the command.
     */
    public void setClementinePermission(@Nullable String perm) {
        this.cPermission = perm;
    }

    /**
     * Gets the commands description for the {@link CommandSender} to see in the 'help' command.
     *
     * @return The description for the {@link CommandSender} to see in the 'help' command.
     */
    @Nullable
    public String getClementineDescription() {
        return this.cDescription;
    }

    /**
     * Sets the commands description for the {@link CommandSender} to see in the 'help' command. This can not be changed after registration.
     *
     * @param description The description for the {@link CommandSender} to see in the 'help' command.
     */
    public void setClementineDescription(@Nullable String description) {
        this.cDescription = description;
    }

    /**
     * Gets if the command can only be used by a {@link Player}.
     *
     * @return If the command can only be used by a {@link Player}.
     */
    @NotNull
    public Boolean isPlayerOnly() {
        return this.playerOnly;
    }

    /**
     * Sets if the command can only be used by a {@link Player}. This can be changed after registration.
     *
     * @param playerOnly If the command can only be used by a {@link Player}.
     */
    public void setPlayerOnly(@NotNull Boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    /**
     * Gets the message that the {@link CommandSender} will see if they are not a {@link Player}.
     *
     * @return The message to send to the {@link CommandSender}.
     */
    @NotNull
    public String getPlayerOnlyMessage() {
        return this.playerOnlyMessage;
    }

    /**
     * Sets the message that the {@link CommandSender} will see if they are not a {@link Player}. This can be changed after registration.
     *
     * @param playerOnlyMessage The message to send to the {@link CommandSender}.
     */
    public void setPlayerOnlyMessage(@NotNull String playerOnlyMessage) {
        this.playerOnlyMessage = playerOnlyMessage;
    }

    /**
     * Handles the execution of the command. May run async if {@link Clementine#isAsync()} is true.
     *
     * @param sender The sender of the command.
     * @param name The command name.
     * @param args The command arguments, may be null.
     */
    public abstract void handle(@NotNull CommandSender sender, @NotNull String name, @Nullable String[] args);

    /**
     * Get the command tab completions as a {@link LiteralCommandNode}.
     *
     * @return The {@link LiteralCommandNode} to register the command tab completions.
     */
    @NotNull
    public abstract LiteralCommandNode<?> getStructure();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String name, @NotNull String[] args) {
        if (cPermission != null && !sender.hasPermission(cPermission)) {
            sender.sendMessage(Bukkit.getServer().getPermissionMessage());
            return true;
        }

        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage(playerOnlyMessage);
            return true;
        }

        if (clementine.isAsync()) {
            clementine.getAsyncExecutor().execute(() -> this.handle(sender, name, args));
            return true;
        }

        this.handle(sender, name, args);
        return true;
    }
}
