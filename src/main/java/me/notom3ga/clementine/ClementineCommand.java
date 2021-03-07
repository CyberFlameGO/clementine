package me.notom3ga.clementine;

import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ClementineCommand extends Command {

    protected final Clementine clementine;
    private final String clementineName;
    private final List<String> clementineAliases;

    private String clementinePermission;
    private String clementineDescription;

    private Boolean playerOnly = false;
    private Component playerOnlyMessage = Component.text("Error: ").color(NamedTextColor.RED)
            .append(Component.text("Only players can run this command.").color(NamedTextColor.GRAY));

    /**
     * Create a new {@link Clementine} {@link ClementineCommand}.
     *
     * @param clementine The {@link Clementine} instance of the plugin.
     * @param name The main name of the command.
     */
    protected ClementineCommand(Clementine clementine, String name) {
        super(name);

        this.clementine = clementine;
        this.clementineName = name;
        this.clementineAliases = new ArrayList<>();
    }

    /**
     * Gets the name that can be used to execute the command.
     *
     * @return The name of the command.
     */
    @NotNull
    public String getClementineName() {
        return this.clementineName;
    }

    /**
     * Gets the aliases that can be used to execute the command.
     *
     * @return A {@link List} of all the command's aliases.
     */
    @NotNull
    public List<String> getClementineAliases() {
        return this.clementineAliases;
    }

    /**
     * Adds an alias that can be used to execute the command.
     *
     * @param alias An alias to add to the command.
     */
    public void addClementineAlias(String alias) {
        this.clementineAliases.add(alias);
    }

    /**
     * Gets the permission node the {@link CommandSender} must have to run the command.
     *
     * @return The permission node the {@link CommandSender} must have to run the command.
     */
    @Nullable
    public String getClementinePermission() {
        return this.clementinePermission;
    }

    /**
     * Sets the permission node the {@link CommandSender} must have to run the command.
     *
     * @param perm The permission node the {@link CommandSender} must have to run the command.
     */
    public void setClementinePermission(@Nullable String perm) {
        this.clementinePermission = perm;
    }

    /**
     * Gets the commands description for the {@link CommandSender} to see in the 'help' command.
     *
     * @return The description for the {@link CommandSender} to see in the 'help' command.
     */
    @Nullable
    public String getClementineDescription() {
        return this.clementineDescription;
    }

    /**
     * Sets the commands description for the {@link CommandSender} to see in the 'help' command.
     *
     * @param description The description for the {@link CommandSender} to see in the 'help' command.
     */
    public void setClementineDescription(@Nullable String description) {
        this.clementineDescription = description;
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
     * Sets if the command can only be used by a {@link Player}.
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
    public Component getPlayerOnlyMessage() {
        return this.playerOnlyMessage;
    }

    /**
     * Sets the message that the {@link CommandSender} will see if they are not a {@link Player}.
     *
     * @param playerOnlyMessage The message to send to the {@link CommandSender}.
     */
    public void setPlayerOnlyMessage(@NotNull Component playerOnlyMessage) {
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
