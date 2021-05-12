package pl.barthvisuals.bvsprawdzanie.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pl.barthvisuals.bvsprawdzanie.Main;

import java.util.Arrays;
import java.util.Objects;

public class ZglosCommand implements CommandExecutor {
    private Main plugin;
    public ZglosCommand(Main plugin)
    {
        this.plugin = plugin;
    }

    public static<T> T[] subArray(T[] array, int beg, int end) {
        return Arrays.copyOfRange(array, beg, end + 1);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        if(!(plugin.getConfig().getBoolean("zglos-on"))){
            return false;
        }
        else
        {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage(" ");
                sender.sendMessage("[bv-Sprawdzanie] Tej komendy uzywac mozna tylko z poziomu gracza! (/zglos)");
                sender.sendMessage(" ");
            } else {
                Player p = (Player) sender;
                if (!(strings.length > 1)) {
                    p.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("zglos-usage")).replace("&", "§"));
                    return false;
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if(player.hasPermission("sprawdzanie.zglos") || player.isOp())
                        {
                            String[] reason = subArray(strings, 1, strings.length - 1);
                            TextComponent messageclick = new TextComponent(Objects.requireNonNull(plugin.getConfig().getString("zglos-message-admins"))
                                    .replace("&", "§")
                                    .replace("{ZGLOS_PREFIX}", Objects.requireNonNull(plugin.getConfig().getString("zglos-prefix")).replace("&", "§"))
                                    .replace("{ZGLOS_PLAYER_SENDER}", p.getName())
                                    .replace("{ZGLOS_PLAYER_REPORTED}", strings[0])
                                    .replace("{ZGLOS_REASON}", String.join(" ", reason).replace("&", "§")));
                            messageclick.setColor(ChatColor.RED);
                            messageclick.setBold(true);
                            messageclick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + strings[0]));
                            messageclick.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder("Kliknij aby przeteleportować się do gracza.").color(ChatColor.GOLD).italic(true).create()));
                            player.spigot().sendMessage(messageclick);
                        }
                    });
                    p.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("zglos-pvmessage")).replace("&", "§"));
                }
            }
        }
        return false;
    }
}
