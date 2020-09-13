package a3074100486f044f6ad28c509b66c7430;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.plugin.java.*;
import org.bukkit.util.*;

public class PluginMain extends JavaPlugin implements Listener {

	private static PluginMain instance;

	public void onEnable() {
		saveDefaultConfig();
		instance = this;
		getServer().getPluginManager().registerEvents(this, this);
		try {
			new File(String.valueOf(PluginMain.getInstance().getConfig())).createNewFile();
			reloadConfig();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] commandArgs) {
		if (command.getName().equalsIgnoreCase("menu")) {
			try {
				if (!PluginMain.createList(commandArgs).isEmpty()) {
					if (commandArgs[((int) 0d)].contains("reload")) {
						if (sender.hasPermission("potato.jsonmenu.reload")) {
							reloadConfig();
							sender.sendMessage(PluginMain.color("&2You have reloaded the config!"));
						} else if (!sender.hasPermission("potato.jsonmenu.reload")) {
							sender.sendMessage(PluginMain.color("&cYou do not have permissions to reload."));
						}
					} else if ((!PluginMain.createList(commandArgs).isEmpty()
							&& !PluginMain.createList(commandArgs).contains("reload"))) {
						sender.sendMessage(PluginMain.color("&cThat argument isn't supported!"));
					}
				}
				if (PluginMain.createList(commandArgs).isEmpty()) {
					((org.bukkit.entity.Player) sender).spigot().sendMessage(net.md_5.bungee.chat.ComponentSerializer
							.parse(String.valueOf(PluginMain.getInstance().getConfig().get("menu"))));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void procedure(String procedure, List<?> args) throws Exception {
	}

	public static Object function(String function, List<?> args) throws Exception {
		return null;
	}

	private static List<Object> createList(Object obj) {
		List<Object> list = new ArrayList<>();
		if (obj.getClass().isArray()) {
			int length = java.lang.reflect.Array.getLength(obj);
			for (int i = 0; i < length; i++) {
				list.add(java.lang.reflect.Array.get(obj, i));
			}
		} else if (obj instanceof Collection<?>) {
			list.addAll((Collection<?>) obj);
		} else {
			list.add(obj);
		}
		return list;
	}

	private static String color(String string) {
		return string != null ? ChatColor.translateAlternateColorCodes('&', string) : null;
	}

	public static PluginMain getInstance() {
		return instance;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoinEvent5(org.bukkit.event.player.PlayerJoinEvent event) throws Exception {
		if ((((java.lang.Boolean) PluginMain.getInstance().getConfig().get("onJoin")).booleanValue()
				&& event.getPlayer().hasPermission("potato.jsonmenu"))) {
			new org.bukkit.scheduler.BukkitRunnable() {
				public void run() {
					try {
						event.getPlayer().spigot().sendMessage(net.md_5.bungee.chat.ComponentSerializer
								.parse(String.valueOf(PluginMain.getInstance().getConfig().get("menu"))));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}.runTaskLater(PluginMain.getInstance(),
					Duration.fromTicks(((Number) PluginMain.getInstance().getConfig().get("onJoinDelay")).doubleValue())
							.getTicks());
		}
	}
}
