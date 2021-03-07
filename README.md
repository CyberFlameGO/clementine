# clementine

A simple Brigadier command manager for Paper.
Brigadier is the software that lets the default minecraft commands have pretty colors!

### Using clementine
#### 1) Setup your Build System
##### Gradle:
```kotlin
repositories {
    maven("https://repo.notom3ga.me/")
}

dependencies {
    implementation("me.notom3ga", "clementine", "1.0")
}

tasks {
    shadowJar {
        exclude("com.mojang.brigadier")
        relocate("me.notom3ga.clementine", "[YOUR PLUGIN PACKAGE].clementine")
    }

    build {
        dependsOn(shadowJar)
    }
}
```

##### Maven:
```xml
<dependencies>
    <dependency>
        <groupId>me.notom3ga</groupId>
        <artifactId>clementine</artifactId>
        <version>1.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>notom3ga-repo</id>
        <url>https://repo.notom3ga.me/</url>
    </repository>
</repositories>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.2.1</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <artifactSet>
                            <includes>
                                <include>me.notom3ga:clementine</include>
                            </includes>
                        </artifactSet>
                        <relocations>
                            <relocation>
                                <pattern>me.notom3ga.clementine</pattern>
                                <shadedPattern>[YOUR PLUGIN PACKAGE].clementine</shadedPattern>
                            </relocation>
                        </relocations>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

#### 2) Configure your project

```java
package me.notom3ga.example;  
  
import me.notom3ga.clementine.Clementine;  
import org.bukkit.plugin.java.JavaPlugin;  
import me.notom3ga.example.TimeCommand;  
  
public class ClementineExample extends JavaPlugin {  
  
  private Clementine clementine;  
  
  @Override  
  public void onEnable() {  
	  // Initialize clementine  
	  clementine = new Clementine(this);  
  
      // Setup clementine to run async  
	  clementine.setupAsync();  
  
      // Register new command  
	  clementine.register(new TimeCommand(clementine));  
  }  
}
```
```java
package me.notom3ga.example;  
  
import com.mojang.brigadier.arguments.IntegerArgumentType;  
import com.mojang.brigadier.builder.LiteralArgumentBuilder;  
import com.mojang.brigadier.builder.RequiredArgumentBuilder;  
import com.mojang.brigadier.tree.LiteralCommandNode;  
import me.notom3ga.clementine.Clementine;  
import me.notom3ga.clementine.ClementineCommand;  
import org.bukkit.command.CommandSender;  
import org.jetbrains.annotations.NotNull;  
  
public class TimeCommand extends Command {  
  
  public TimeCommand(Clementine clementine) {  
	  super(clementine, "time");  
      this.setPermission("command.time");  
  }  
  
  @Override  
  public void handle(CommandSender sender, String name, String[] args) {  
	  // Run code here  
  }  
  
  @Override  
  public @NotNull LiteralCommandNode<?> getStructure() {  
	  // Example of the default minecraft time command  
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