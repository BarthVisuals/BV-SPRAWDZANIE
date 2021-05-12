package pl.barthvisuals.bvsprawdzanie.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.barthvisuals.bvsprawdzanie.Main;

import java.util.Map;
import java.util.Objects;

public class PrzyznajeCommand implements CommandExecutor {
    private Main plugin;
    SprawdzCommand spr;
    public PrzyznajeCommand(SprawdzCommand spr, Main plugin)
    {
        this.spr = spr;
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        if(spr.log1.containsValue(p.getName())){
            Bukkit.broadcastMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-przyznaje-message-on-chat"))
                    .replace("&", "§")
                    .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                    .replace("{SPR_PLAYER}", p.getName())));
            spr.log1.remove(getKeyFromValue(spr.log1, p.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Objects.requireNonNull(plugin.getConfig().getString("spr-przyznaje-command")).replace("{SPR_PLAYER}", p.getName()));
        }
        else
        {
            p.sendMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-errorw-mesage")).replace("&", "§")));
        }

        return false;
    }
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
