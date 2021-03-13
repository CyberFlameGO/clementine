# clementine

A simple Brigadier command manager for Paper.
Brigadier is the software that lets the default minecraft commands have pretty colors!

### Using clementine
#### 1) Setup your Build System
##### Gradle:
```kotlin
plugins {
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    maven("https://repo.notom3ga.me/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation("me.notom3ga", "clementine", "1.1")
    compileOnly("com.mojang", "brigadier", "1.0.17")
}

tasks {
    shadowJar {
        relocate("me.notom3ga.clementine", "[YOUR PLUGIN PACKAGE].clementine")
    }

    build {
        dependsOn(shadowJar)
    }
}
```

#### 2) Configure your project

```java
public class ClementineExample extends JavaPlugin {
    private Clementine clementine;

    @Override
    public void onEnable() {
        // create the clementine object
        clementine = new Clementine(this);

        // setup clementine to run commands async
        clementine.setAsync(true);

        // register new command
        clementine.register(new TimeCommand(clementine));
    }
}
```
```java
public class TimeCommand extends me.notom3ga.clementine.Command {

    public TimeCommand(Clementine clementine) {
        // set command name and aliases
        super(clementine, "time", "timealias1", "settime");

        // set command properties
        this.setClementineDescription("Change the time!");
        this.setClementinePermission("command.time");
        this.setPlayerOnly(true);
        this.setPlayerOnlyMessage("This command can only be run by players!");
    }

    @Override
    public void handle(@NotNull CommandSender sender, @NotNull String name, @Nullable String[] args) {
        // run command
    }

    @Override
    public @NotNull LiteralCommandNode<?> getStructure() {
        // example of the default minecraft time command
        return LiteralArgumentBuilder.literal("time")
                .then(LiteralArgumentBuilder.literal("set")
                        .then(LiteralArgumentBuilder.literal("day"))
                        .then(LiteralArgumentBuilder.literal("noon"))
                        .then(LiteralArgumentBuilder.literal("night"))
                        .then(LiteralArgumentBuilder.literal("midnight"))
                        .then(RequiredArgumentBuilder.argument("time", IntegerArgumentType.integer())))
                .then(LiteralArgumentBuilder.literal("add")
                        .then(RequiredArgumentBuilder.argument("time", IntegerArgumentType.integer())))
                .then(LiteralArgumentBuilder.literal("query")
                        .then(LiteralArgumentBuilder.literal("daytime"))
                        .then(LiteralArgumentBuilder.literal("gametime"))
                        .then(LiteralArgumentBuilder.literal("day"))
                ).build();
    }
}
```

### BEWARE - Method names may not be what you think they are
The only methods in the `me.notom3ga.clementine.Command` added by clementine are:

```java
getClementineName()
getClementineAliases()
getClementinePermission()
setClementinePermission()
getClementineDescription()
setClementineDescription()
isPlayerOnly()
setPlayerOnly()
getPlayerOnlyMessage()
setPlayerOnlyMessage()
```

Other methods are added by the `org.bukkit.command.Command` class and do not work with clementine.
