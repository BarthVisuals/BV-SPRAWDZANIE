package pl.barthvisuals.bvsprawdzanie.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.barthvisuals.bvsprawdzanie.Main;
import pl.barthvisuals.bvsprawdzanie.commands.SprawdzCommand;

import java.util.Objects;

public class PlayerPreProcessCommandListener implements Listener {
    private Main plugin;
    SprawdzCommand spr;
    public PlayerPreProcessCommandListener(SprawdzCommand spr, Main plugin)
    {
        this.plugin = plugin;
        this.spr = spr;
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e)
    {
        if(spr.log1.containsValue(e.getPlayer().getName())){
            if(plugin.getConfig().getList("spr-allowed-commands").contains(e.getMessage())){
                return;
            }
            else
            {
                e.setCancelled(true);
                e.getPlayer().sendMessage((Objects.requireNonNull(plugin.getConfig().getString("spr-cmd-message-on-priv"))
                        .replace("&", "§")
                        .replace("{SPR_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("spr-prefix")).replace("&", "§"))
                        .replace("{SPR_PLAYER}", e.getPlayer().getName())));
            }
        }
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items)
    {
        for(int i =0; i < items.length; i++)
        {
            if(inputStr.contains(items[i]))
            {
                return true;
            }
        }
        return false;
    }
}
