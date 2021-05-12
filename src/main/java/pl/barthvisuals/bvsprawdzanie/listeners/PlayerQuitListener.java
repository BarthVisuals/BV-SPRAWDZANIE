package pl.barthvisuals.bvsprawdzanie.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.barthvisuals.bvsprawdzanie.Main;
import pl.barthvisuals.bvsprawdzanie.commands.SprawdzCommand;
import java.util.Map;
import java.util.Objects;

public class PlayerQuitListener implements Listener {
    private Main plugin;
    SprawdzCommand spr;
    public PlayerQuitListener(SprawdzCommand spr, Main plugin)
    {
        this.plugin = plugin;
        this.spr = spr;
    }
    @EventHandler()
    public void onPlayerQuit(PlayerQuitEvent e){
        PlayerInteractEvent event;
        Player p = e.getPlayer();
        if(spr.log1.containsValue(p.getName())){
            Bukkit.broadcastMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-logout-message-on-chat"))
                    .replace("&", "§")
                    .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                    .replace("{SPR_PLAYER}", p.getName())));
            spr.log1.remove(getKeyFromValue(spr.log1, p.getName()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Objects.requireNonNull(plugin.getConfig().getString("spr-logout-command")).replace("{SPR_PLAYER}", p.getName()));
        }
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
