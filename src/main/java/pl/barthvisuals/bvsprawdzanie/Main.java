package pl.barthvisuals.bvsprawdzanie;

import org.bukkit.plugin.java.JavaPlugin;
import pl.barthvisuals.bvsprawdzanie.commands.PrzyznajeCommand;
import pl.barthvisuals.bvsprawdzanie.commands.SprawdzCommand;
import pl.barthvisuals.bvsprawdzanie.commands.ZglosCommand;
import pl.barthvisuals.bvsprawdzanie.listeners.PlayerPreProcessCommandListener;
import pl.barthvisuals.bvsprawdzanie.listeners.PlayerQuitListener;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig();

        this.getLogger().info("Autor pluginu: BarthVisuals");
        this.getLogger().info("Source code: github.com/barthvisuals");
        this.getLogger().info("Dzień powstania: 10.05.2021");

        SprawdzCommand spr = new SprawdzCommand(this);
        this.getCommand("sprawdzanie").setExecutor(spr);
        this.getCommand("zglos").setExecutor(new ZglosCommand(this));
        this.getCommand("przyznaje").setExecutor(new PrzyznajeCommand(spr, this));
        this.getServer().getPluginManager().registerEvents(new PlayerPreProcessCommandListener(spr, this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(spr, this), this);

    }
}
