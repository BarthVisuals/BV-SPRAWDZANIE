package pl.barthvisuals.bvsprawdzanie.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import pl.barthvisuals.bvsprawdzanie.Main;
import java.util.HashMap;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SprawdzCommand implements CommandExecutor {
    private Main plugin;
    private FileConfiguration config;
    public SprawdzCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    public static HashMap<String, String> log1 = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player)sender;

        if(p.hasPermission("sprawdzanie.all") || p.isOp()){
            if(args.length < 1){
                p.sendMessage("§cDostępne komendy:");
                p.sendMessage("§8» §7/sprawdzanie §cgracz §7<nick> §8- §7Rozpoczyna sprawdzanie gracza");
                p.sendMessage("§8» §7/sprawdzanie §cczysty §8- §7Uznaje gracza za niewinnego");
                p.sendMessage("§8» §7/sprawdzanie §cbw §8- §7Nadaje bana za brak współpracy");
                p.sendMessage("§8» §7/§cprzyznaje §8- §7Nadaje bana za przyznanie się");
                p.sendMessage("§8» §7/sprawdzanie §cset §8- §7Ustawienie miejsca sprawdzania");
                p.sendMessage("§8» §7/sprawdzanie §cautor §8- §7Autor pluginu");
            }
            else
            {
                switch(args[0]){
                    case "autor":{
                        p.sendMessage("§8» §cAutor pluginu: BarthVisuals");
                        p.sendMessage("§8» §cStrona internetowa: BarthVisuals.pl");
                        break;
                    }
                    case "set":{
                        File locationConfig = new File(plugin.getDataFolder(), "locations.yml");
                        if(!locationConfig.exists())
                        {
                            try {
                                locationConfig.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        config = YamlConfiguration.loadConfiguration(locationConfig);

                        config.createSection("sprawdz-spawn." + ".x");
                        config.createSection("sprawdz-spawn." + ".y");
                        config.createSection("sprawdz-spawn." + ".z");
                        config.set("sprawdz-spawn." + ".x", p.getLocation().getBlockX());
                        config.set("sprawdz-spawn." + ".y", p.getLocation().getBlockY());
                        config.set("sprawdz-spawn." + ".z", p.getLocation().getBlockZ());
                        p.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("spr-newspawn")));
                        plugin.getLogger().info(p.getName() + " ustawil nowe miejsce sprawdzania!");
                        try {
                            config.save(locationConfig);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case "gracz":{
                        if(args.length != 2){
                            p.sendMessage("§cDostępne komendy:");
                            p.sendMessage("§8» §7/sprawdzanie §cgracz §7<nick> §8- §7Rozpoczyna sprawdzanie gracza");
                            p.sendMessage("§8» §7/sprawdzanie §cczysty §8- §7Uznaje gracza za niewinnego");
                            p.sendMessage("§8» §7/sprawdzanie §cbw §8- §7Nadaje bana za brak współpracy");
                            p.sendMessage("§8» §7/§cprzyznaje §8- §7Nadaje bana za przyznanie się");
                            p.sendMessage("§8» §7/sprawdzanie §cset §8- §7Ustawienie miejsca sprawdzania");
                            p.sendMessage("§8» §7/sprawdzanie §cautor §8- §7Autor pluginu");
                            break;
                        }
                        else
                        {
                            Player cel = Bukkit.getPlayerExact(args[1]);
                            if(!(cel.isOnline()))
                            {
                                sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("spr-poffline-message")));
                                break;
                            }
                            if(cel.getName() == p.getName()){
                                sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("spr-checkme-message")));
                                break;
                            }
                            else
                            {
                                if(log1.containsKey(p.getName())){
                                    sender.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("spr-checksecoundplayer-message")));
                                    break;
                                }
                                else
                                {
                                    File locationConfig = new File(plugin.getDataFolder(), "locations.yml");
                                    if(!locationConfig.exists())
                                    {
                                        try {
                                            locationConfig.createNewFile();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    config = YamlConfiguration.loadConfiguration(locationConfig);
                                    Location tp = new Location(p.getLocation().getWorld(), config.getInt("sprawdz-spawn." + ".x"), config.getInt("sprawdz-spawn." + ".y"), config.getInt("sprawdz-spawn." + ".z"));
                                    p.teleport(tp);
                                    cel.teleport(tp);
                                    log1.put(p.getName(), cel.getName());
                                    Bukkit.broadcastMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-message-on-chat"))
                                            .replace("&", "§")
                                            .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                                            .replace("{SPR_ADMIN}", p.getName())
                                            .replace("{SPR_PLAYER}", cel.getName())));
                                    cel.sendMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-message-on-priv"))
                                            .replace("&", "§")
                                            .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                                            .replace("{SPR_ADMIN}", p.getName())
                                            .replace("{SPR_PLAYER}", cel.getName())));
                                    break;
                                }
                            }
                        }
                    }
                    case "bw":{
                        if(log1.containsKey(p.getName())){
                            p.performCommand((Objects.requireNonNull(plugin.getConfig().getString("spr-brakwspolpracy-command")).replace("{SPR_PLAYER}", log1.get(p.getName()))));
                            log1.remove(p.getName());
                            break;
                        }
                        else
                        {
                            p.sendMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-errorlist-message"))
                                    .replace("&", "§")
                                    .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                                    .replace("{SPR_ADMIN}", p.getName())));
                            break;
                        }
                    }
                    case "czysty":{
                        if(log1.containsKey(p.getName())){
                            Bukkit.broadcastMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-clear-message-on-chat"))
                                    .replace("&", "§")
                                    .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                                    .replace("{SPR_ADMIN}", p.getName())
                                    .replace("{SPR_PLAYER}", log1.remove(p.getName()))));
                            break;
                        }
                        else
                        {
                            p.sendMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-errorlist-message"))
                                    .replace("&", "§")
                                    .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                                    .replace("{SPR_ADMIN}", p.getName())));
                            break;
                        }
                    }
                }
            }
        }
        else
        {
            p.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("spr-nopermissions")));
        }
        return false;
    }
}
