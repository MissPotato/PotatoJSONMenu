package aeca826339eb143129bd6d538b05961b8;
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
			VariableManager.setValue(false, "./plugins/PotatoJSONMenu/menus/",
					new ArrayList(Arrays.asList("menusFolder")));
			VariableManager.setValue(false, "./plugins/PotatoJSONMenu/config.yml",
					new ArrayList(Arrays.asList("configFile")));
			VariableManager.setValue(false, new java.lang.Double(1.03d), new ArrayList(Arrays.asList("pluginVersion")));
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Variables set"));
			}
			if (!new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder")))))
					.exists()) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: No folder found!"));
				}
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder")))))
						.mkdirs();
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Folder now exists!"));
				}
			}
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Checking configs"));
			}
			if ((((Number) PluginMain.getInstance().getConfig().get("configVersion"))
					.doubleValue() < ((Number) VariableManager.getValue(false,
							new ArrayList(Arrays.asList("pluginVersion")))).doubleValue())) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Version Checking"));
				}
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("Config version beind updating"));
				procedure("update", ((java.util.List) null));
				reloadConfig();
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Config update finish"));
				}
			}
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Running gen"));
			}
			procedure("genMenus", new ArrayList());
			procedure("menuCache", ((java.util.List) null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			ExpansionHandler.register(this);
		}
	}

	public void onDisable() {
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] commandArgs) {
		if (command.getName().equalsIgnoreCase("menu")) {
			try {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG:Sending command"));
				}
				if (PluginMain.createList(commandArgs).isEmpty()) {
					if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
						Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG:Command empty sending defualt"));
					}
					procedure("runMenu", new ArrayList(Arrays.asList(sender, new java.lang.Double(1d))));
				}
				if (!PluginMain.createList(commandArgs).isEmpty()) {
					if ((!UtilMethods.isNumber(String.valueOf(PluginMain.createList(commandArgs).get(((int) 0d))))
							|| (((Number) PluginMain.getInstance().getConfig().get("menus"))
									.doubleValue() < ((Number) Double.valueOf(
											String.valueOf(PluginMain.createList(commandArgs).get(((int) 0d)))))
													.doubleValue()))) {
						if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
							Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG:Invalid arguments"));
						}
						sender.sendMessage(PluginMain.color("&cInvalid Arguments!"));
					}
				}
				if (((!PluginMain.createList(commandArgs).isEmpty()
						&& UtilMethods.isNumber(String.valueOf(PluginMain.createList(commandArgs).get(((int) 0d)))))
						&& (((Number) Double
								.valueOf(String.valueOf(PluginMain.createList(commandArgs).get(((int) 0d)))))
										.doubleValue() <= ((Number) PluginMain.getInstance().getConfig().get("menus"))
												.doubleValue()))) {
					if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
						Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG:Specified menu found"));
					}
					procedure("runMenu", new ArrayList(Arrays.asList(sender,
							Double.valueOf(String.valueOf(PluginMain.createList(commandArgs).get(((int) 0d)))))));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (command.getName().equalsIgnoreCase("jsonmenu")) {
			try {
				if ((!PluginMain.createList(commandArgs).isEmpty()
						&& "edit,reload,version".contains(commandArgs[((int) 0d)]))) {
					if (commandArgs[((int) 0d)].contains("version")) {
						sender.sendMessage(PluginMain.color(("&2JSON Menus Version:&r " + String.valueOf(
								VariableManager.getValue(false, new ArrayList(Arrays.asList("pluginVersion")))))));
					}
					if (commandArgs[((int) 0d)].contains("edit")) {
						if (((UtilMethods.checkEquals(new java.lang.Integer(PluginMain.createList(commandArgs).size()),
								new java.lang.Double(3d))
								&& UtilMethods.isNumber(String.valueOf(Double
										.valueOf(String.valueOf(PluginMain.createList(commandArgs).get(((int) 1d)))))))
								&& (((Number) Double
										.valueOf(String.valueOf(PluginMain.createList(commandArgs).get(((int) 1d)))))
												.doubleValue() <= ((Number) PluginMain.getInstance().getConfig()
														.get("menus")).doubleValue()))) {
							((java.util.List) VariableManager.getValue(false,
									new ArrayList(Arrays.asList(("menuCache"
											+ String.valueOf(PluginMain.createList(commandArgs).get(((int) 1d))))))))
													.set(((int) 2d),
															PluginMain.createList(commandArgs).get(((int) 2d)));
							procedure("genMenus",
									new ArrayList(Arrays.asList(
											new java.lang.Long(Math.round(((Number) Double.valueOf(
													String.valueOf(PluginMain.createList(commandArgs).get(((int) 1d)))))
															.doubleValue())),
											PluginMain.createList(commandArgs).get(((int) 2d)))));
						} else if ((UtilMethods.checkEquals(
								new java.lang.Integer(PluginMain.createList(commandArgs).size()),
								new java.lang.Double(3d))
								&& ((0d >= ((Number) Double
										.valueOf(String.valueOf(PluginMain.createList(commandArgs).get(((int) 1d)))))
												.doubleValue())
										|| (((Number) PluginMain.getInstance().getConfig().get("menus"))
												.doubleValue() < ((Number) Double.valueOf(String
														.valueOf(PluginMain.createList(commandArgs).get(((int) 1d)))))
																.doubleValue())))) {
							sender.sendMessage(PluginMain.color("&cInvalid menu!"));
						} else {
							sender.sendMessage(PluginMain.color("&cFormat is: <menu number> <json>"));
						}
					}
					if (commandArgs[((int) 0d)].contains("reload")) {
						reloadConfig();
						procedure("genMenus", new ArrayList());
						procedure("menuCache", ((java.util.List) null));
						sender.sendMessage(PluginMain.color("&2You have reloaded the config!"));
					}
				} else {
					sender.sendMessage(PluginMain.color("&6This is the admin command for JSON Menus"));
					sender.sendMessage(PluginMain.color("&6Valid arguments are:&r edit, reload, version"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public static void procedure(String procedure, List<?> args) throws Exception {
		if (procedure.equalsIgnoreCase("menuCache")) {
			Object a703b35c49ddf825b94dd04d1d28896cf = null;
			Object a29e17c613c6aac53aab93212fbaceb70 = null;
			Object ac7e54802b7c5d9dab276d69f24f305c7 = null;
			ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(1d));
			while ((((Number) ac7e54802b7c5d9dab276d69f24f305c7)
					.doubleValue() <= ((Number) PluginMain.getInstance().getConfig().get("menus")).doubleValue())) {
				a703b35c49ddf825b94dd04d1d28896cf = new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")));
				VariableManager.setValue(false, new ArrayList(), new ArrayList(
						Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))));
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain
							.color(("DEBUG: Caching menu " + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
				}
				VariableManager.addToValue(false, org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("onJoin"),
						new ArrayList(
								Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))));
				VariableManager.addToValue(false, org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("onJoinDelay"),
						new ArrayList(
								Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))));
				VariableManager.addToValue(false, org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("JSON"),
						new ArrayList(
								Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))));
				VariableManager.addToValue(false, org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("permission"),
						new ArrayList(
								Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))));
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					a29e17c613c6aac53aab93212fbaceb70 = new java.lang.Double(0d);
					for (int abd9021d798cc4398b0635b884b792325 = 0; abd9021d798cc4398b0635b884b792325 < ((java.util.List) VariableManager
							.getValue(false,
									new ArrayList(Arrays.asList(
											("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
													.size(); abd9021d798cc4398b0635b884b792325++) {
						Bukkit.getConsoleSender().sendMessage(
								PluginMain.color(String.valueOf(((java.util.List) VariableManager.getValue(false,
										new ArrayList(Arrays.asList(
												("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
														.get(((Number) a29e17c613c6aac53aab93212fbaceb70)
																.intValue()))));
						a29e17c613c6aac53aab93212fbaceb70 = UtilMethods.addToObject(a29e17c613c6aac53aab93212fbaceb70,
								new java.lang.Double(1d));
					}
				}
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain
							.color(("DEBUG: Done caching menu " + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
				}
				ac7e54802b7c5d9dab276d69f24f305c7 = UtilMethods.addToObject(ac7e54802b7c5d9dab276d69f24f305c7,
						new java.lang.Double(1d));
				ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
						Math.round(((Number) ac7e54802b7c5d9dab276d69f24f305c7).doubleValue()));
			}
		}
		if (procedure.equalsIgnoreCase("genMenus")) {
			Object a703b35c49ddf825b94dd04d1d28896cf = null;
			Object a42f0255461bdb78c28dd7689b3b51752 = null;
			Object a206f29d3782cdea4d073bd356cebd6f2 = null;
			Object ac638de2c6894c7b946383722bdfaf809 = null;
			Object a1f75ee55d82f2f492d4c449fc408cf31 = null;
			Object ac7e54802b7c5d9dab276d69f24f305c7 = null;
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("Generating menus"));
			}
			if ((!PluginMain.createList(args).isEmpty()
					&& UtilMethods.isNumber(String.valueOf(Double.valueOf(String.valueOf(args.get(((int) 0d)))))))) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: User edited command"));
				}
				ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
						Math.round(((Number) args.get(((int) 0d))).doubleValue()));
				a42f0255461bdb78c28dd7689b3b51752 = org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("onJoin");
				a1f75ee55d82f2f492d4c449fc408cf31 = org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("onJoinDelay");
				a206f29d3782cdea4d073bd356cebd6f2 = args.get(((int) 1d));
				ac638de2c6894c7b946383722bdfaf809 = org.bukkit.configuration.file.YamlConfiguration
						.loadConfiguration(new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
						.get("permission");
				new File((String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
						+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).delete();
				new File((String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
						+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).createNewFile();
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(("################## MENU "
								+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + " ##################"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								"#onJoin: Do you want this menu to behave like an MOTD and be sent to players on connect? Default: false"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(("onJoin: " + String.valueOf(a42f0255461bdb78c28dd7689b3b51752))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								"#onJoinDelay: Set the time in ticks you want to hold the message on join. This can help with lining it up with an MOTD  or with other menus. Default is 5. Requires the perm to use command."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(("onJoinDelay: " + String.valueOf(a1f75ee55d82f2f492d4c449fc408cf31))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								"#menu: put your ENTIRE compacted JSON between the ''.  I use https://minecraftjson.com/ to help me make my JSON. "),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								"#Example: menu: '{\"text\":\"[test]\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"}}'"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(("JSON: '" + (String.valueOf(a206f29d3782cdea4d073bd356cebd6f2) + "'"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								("#Permision to be able to use this menu. Setting them to the same value as other menus works as you assume. Default: potato.jsonmenu.menu"
										+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								("permission: '" + (String.valueOf(ac638de2c6894c7b946383722bdfaf809) + "'"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(
								"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your menu and that makes it sad."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).toPath(),
						Collections.singleton(("menuVersion: " + String.valueOf(
								VariableManager.getValue(false, new ArrayList(Arrays.asList("pluginVersion")))))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
			} else {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: No args passed, doing full check"));
				ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(1d));
				while ((((Number) ac7e54802b7c5d9dab276d69f24f305c7)
						.doubleValue() <= ((Number) PluginMain.getInstance().getConfig().get("menus")).doubleValue())) {
					if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
						Bukkit.getConsoleSender().sendMessage(PluginMain.color(("DEBUG: Running check against menu "
								+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
					}
					a703b35c49ddf825b94dd04d1d28896cf = new File((String
							.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
							+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")));
					if (!new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).exists()) {
						new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).createNewFile();
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("################## MENU "
										+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + " ##################"))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										"#onJoin: Do you want this menu to behave like an MOTD and be sent to players on connect? Default: false"),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("onJoin: " + "false")), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										"#onJoinDelay: Set the time in ticks you want to hold the message on join. This can help with lining it up with an MOTD  or with other menus. Default is 5. Requires the perm to use command."),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("onJoinDelay: " + "5")), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										"#menu: put your ENTIRE compacted JSON between the ''.  I use https://minecraftjson.com/ to help me make my JSON. "),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										"#Example: menu: '{\"text\":\"[test]\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"}}'"),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("JSON: " + "''")), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										("#Permision to be able to use this menu. Setting them to the same value as other menus works as you assume. Default: potato.jsonmenu.menu"
												+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("permission: 'potato.jsonmenu.menu"
										+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + "'"))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(
										"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your menu and that makes it sad."),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(new File(String.valueOf(a703b35c49ddf825b94dd04d1d28896cf)).toPath(),
								Collections.singleton(("menuVersion: " + String.valueOf(VariableManager.getValue(false,
										new ArrayList(Arrays.asList("pluginVersion")))))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
					}
					if ((new File((String
							.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
							+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).exists()
							&& (((Number) PluginMain.getInstance().getConfig()
									.get("configVersion"))
											.doubleValue() < ((Number) org.bukkit.configuration.file.YamlConfiguration
													.loadConfiguration(
															new File((String
																	.valueOf(VariableManager.getValue(
																			false,
																			new ArrayList(
																					Arrays.asList("menusFolder"))))
																	+ (("menu" + String
																			.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))
																			+ ".yml"))))
													.get("menuVersion")).doubleValue()))) {
						Bukkit.getConsoleSender()
								.sendMessage(PluginMain.color(("&cSomething is wrong with the file for Menu "
										+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
						Bukkit.getConsoleSender()
								.sendMessage(PluginMain.color("&cConsider deleting it to regenerate file!"));
					}
					ac7e54802b7c5d9dab276d69f24f305c7 = UtilMethods.addToObject(ac7e54802b7c5d9dab276d69f24f305c7,
							new java.lang.Double(1d));
					ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
							Math.round(((Number) ac7e54802b7c5d9dab276d69f24f305c7).doubleValue()));
				}
			}
		}
		if (procedure.equalsIgnoreCase("runMenu")) {
			Object aaef2f2a36a2b31359a35a21eafceb685 = null;
			Object ac7e54802b7c5d9dab276d69f24f305c7 = null;
			ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
					Math.round(((Number) Double.valueOf(String.valueOf(args.get(((int) 1d))))).doubleValue()));
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Running menu"));
				Bukkit.getConsoleSender()
						.sendMessage(PluginMain.color(("DEBUG: Menu Arg0" + String.valueOf(args.get(((int) 0d))))));
				Bukkit.getConsoleSender()
						.sendMessage(PluginMain.color(("DEBUG: Menu Arg0" + String.valueOf(args.get(((int) 1d))))));
				Bukkit.getConsoleSender().sendMessage(PluginMain
						.color(("DEBUG: Menu Arg0" + String.valueOf(VariableManager.getValue(false, new ArrayList(
								Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)))))))));
			}
			if (!UtilMethods.checkEquals(PluginMain.createList(args).get(((int) 0d)), Bukkit.getConsoleSender())) {
				if (UtilMethods.checkEquals(args.get(((int) 1d)), new java.lang.Long(Math.round(-1d)))) {
					ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(1d));
					if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
						Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: -1, running on join"));
					}
					for (int afc7ff24855b24099916d7f7025497417 = 0; afc7ff24855b24099916d7f7025497417 < ((Number) PluginMain
							.getInstance().getConfig().get("menus")).intValue(); afc7ff24855b24099916d7f7025497417++) {
						ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
								Math.round(((Number) ac7e54802b7c5d9dab276d69f24f305c7).doubleValue()));
						if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
							Bukkit.getConsoleSender().sendMessage(PluginMain
									.color(("DEBUG: Doing loop " + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
						}
						if ((!((java.util.List) VariableManager.getValue(false,
								new ArrayList(Arrays
										.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
												.isEmpty()
								&& (!UtilMethods.checkEquals(((java.util.List) VariableManager.getValue(false,
										new ArrayList(Arrays.asList(
												("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
														.get(((int) 2d)),
										"")
										&& (((java.lang.Boolean) ((java.util.List) VariableManager.getValue(false,
												new ArrayList(Arrays.asList(("menuCache"
														+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
																.get(((int) 0d))).booleanValue()
												&& ((org.bukkit.permissions.Permissible) args.get(((int) 0d)))
														.hasPermission(String.valueOf(((java.util.List) VariableManager
																.getValue(false, new ArrayList(
																		Arrays.asList(("menuCache" + String.valueOf(
																				ac7e54802b7c5d9dab276d69f24f305c7))))))
																						.get(((int) 3d)))))))) {
							Bukkit.getConsoleSender().sendMessage(PluginMain.color(("DEBUG: Doing loop sending menu "
									+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
							if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
								aaef2f2a36a2b31359a35a21eafceb685 = me.clip.placeholderapi.PlaceholderAPI
										.setPlaceholders(
												((org.bukkit.OfflinePlayer) PluginMain.createList(args)
														.get(((int) 0d))),
												String.valueOf(((java.util.List) VariableManager.getValue(false,
														new ArrayList(Arrays.asList(("menuCache"
																+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
																		.get(((int) 2d))));
							}
							if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
								aaef2f2a36a2b31359a35a21eafceb685 = ((java.util.List) VariableManager.getValue(false,
										new ArrayList(Arrays.asList(
												("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
														.get(((int) 2d));
							}
							Object a9407552b68f24a35a8726b70f8a42ad6 = aaef2f2a36a2b31359a35a21eafceb685;
							new org.bukkit.scheduler.BukkitRunnable() {
								Object aab356115b64e4e088fe7c79749e7cd38 = a9407552b68f24a35a8726b70f8a42ad6;

								public void run() {
									try {
										((org.bukkit.entity.Player) args.get(((int) 0d))).spigot()
												.sendMessage(net.md_5.bungee.chat.ComponentSerializer
														.parse(String.valueOf(aab356115b64e4e088fe7c79749e7cd38)));
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}.runTaskLater(PluginMain.getInstance(),
									Duration.fromTicks(((Number) ((java.util.List) VariableManager.getValue(false,
											new ArrayList(Arrays.asList(("menuCache"
													+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
															.get(((int) 1d))).doubleValue())
											.getTicks());
						}
						if ((UtilMethods
								.checkEquals(((java.util.List) VariableManager.getValue(false,
										new ArrayList(Arrays.asList(
												("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
														.get(((int) 2d)),
										"")
								&& ((java.lang.Boolean) ((java.util.List) VariableManager.getValue(false,
										new ArrayList(Arrays.asList(
												("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
														.get(((int) 0d))).booleanValue())) {
							if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug"))
									.booleanValue()) {
								Bukkit.getConsoleSender().sendMessage(
										PluginMain.color(("&cDespite being told to onJoin there was no JSON for menu "
												+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
							}
						}
						ac7e54802b7c5d9dab276d69f24f305c7 = UtilMethods.addToObject(ac7e54802b7c5d9dab276d69f24f305c7,
								new java.lang.Double(1d));
					}
				}
				if ((((Number) Double.valueOf(String.valueOf(args.get(((int) 1d))))).doubleValue() >= 1d)) {
					if ((!UtilMethods.checkEquals(((java.util.List) VariableManager.getValue(false,
							new ArrayList(
									Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
											.get(((int) 2d)),
							"")
							&& ((org.bukkit.permissions.Permissible) args.get(((int) 0d))).hasPermission(String.valueOf(
									((java.util.List) VariableManager.getValue(false, new ArrayList(Arrays.asList(
											("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
													.get(((int) 3d)))))) {
						if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
							aaef2f2a36a2b31359a35a21eafceb685 = me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(
									((org.bukkit.OfflinePlayer) PluginMain.createList(args).get(((int) 0d))),
									String.valueOf(((java.util.List) VariableManager.getValue(false,
											new ArrayList(Arrays.asList(("menuCache"
													+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
															.get(((int) 2d))));
						}
						if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
							aaef2f2a36a2b31359a35a21eafceb685 = ((java.util.List) VariableManager.getValue(false,
									new ArrayList(Arrays.asList(
											("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
													.get(((int) 2d));
						}
						if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
							Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Run specified menu"));
						}
						((org.bukkit.entity.Player) args.get(((int) 0d))).spigot()
								.sendMessage(net.md_5.bungee.chat.ComponentSerializer
										.parse(String.valueOf(aaef2f2a36a2b31359a35a21eafceb685)));
					}
					if (UtilMethods.checkEquals(((java.util.List) VariableManager.getValue(false,
							new ArrayList(
									Arrays.asList(("menuCache" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))))))
											.get(((int) 2d)),
							"")) {
						if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
							Bukkit.getConsoleSender().sendMessage(PluginMain.color(("&cThere was no JSON for menu "
									+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
						}
						((org.bukkit.command.CommandSender) PluginMain.createList(args).get(((int) 0d)))
								.sendMessage(PluginMain.color("&Empty menu report this to server owner!"));
					}
				}
			}
			if (UtilMethods.checkEquals(PluginMain.createList(args).get(((int) 0d)), Bukkit.getConsoleSender())) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("&cConsole can not recieve JSON!"));
			}
		}
		if (procedure.equalsIgnoreCase("update")) {
			Object afb74d8cdcbacce1424af1ff3e3bebb39 = null;
			Object aa89d9df482d7466591062440e807ee2b = null;
			Object ac2edd451d6e16126b5c7aeb12c76b78f = null;
			Object a42f0255461bdb78c28dd7689b3b51752 = null;
			Object accb69c8bfbc31b9d514fb72cc21dc24c = null;
			Object a206f29d3782cdea4d073bd356cebd6f2 = null;
			Object a3e9c510676e205eee84d88589a797e9f = null;
			Object ac638de2c6894c7b946383722bdfaf809 = null;
			Object aa3544f8fbd1c4aad82174b30a4a48464 = null;
			Object a1f75ee55d82f2f492d4c449fc408cf31 = null;
			Object ac7e54802b7c5d9dab276d69f24f305c7 = null;
			Object a2d037a7f5c916ee046a675091428d625 = null;
			ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(1d));
			if (UtilMethods.checkEquals(PluginMain.getInstance().getConfig().get("configVersion"),
					new java.lang.Double(1.0d))) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("Config version found as 1.0 updating!"));
				}
				aa3544f8fbd1c4aad82174b30a4a48464 = PluginMain.getInstance().getConfig().get("onJoin");
				afb74d8cdcbacce1424af1ff3e3bebb39 = PluginMain.getInstance().getConfig().get("onJoinDelay");
				aa89d9df482d7466591062440e807ee2b = PluginMain.getInstance().getConfig().get("menu");
				accb69c8bfbc31b9d514fb72cc21dc24c = ("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7));
				Bukkit.getConsoleSender()
						.sendMessage(PluginMain.color(String.valueOf(aa89d9df482d7466591062440e807ee2b)));
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
						.delete();
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
						.createNewFile();
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder")))))
						.mkdirs();
				new File((String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
						+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).createNewFile();
				ac2edd451d6e16126b5c7aeb12c76b78f = VariableManager.getValue(false,
						new ArrayList(Arrays.asList("configFile")));
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton("################## GLOBAL SETTINGS ##################"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.WRITE);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(), Collections.singleton(
						"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your config and that makes it sad."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton("configVersion: 1.01"), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton("debug: false"), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton("#How many menus do you want? Default is 1. "),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton("menus: 1"), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(String.valueOf(ac2edd451d6e16126b5c7aeb12c76b78f)).toPath(),
						Collections.singleton(
								"#Menus have been moved to their own files for easier editing of multiple menus."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(("################## MENU "
								+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + " ##################"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								"#onJoin: Do you want this menu to behave like an MOTD and be sent to players on connect? Default: false"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(("onJoin: " + String.valueOf(aa3544f8fbd1c4aad82174b30a4a48464))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								"#onJoinDelay: Set the time in ticks you want to hold the message on join. This can help with lining it up with an MOTD  or with other menus. Default is 5. Requires the perm to use command."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(("onJoinDelay: " + String.valueOf(afb74d8cdcbacce1424af1ff3e3bebb39))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								"#menu: put your ENTIRE compacted JSON between the ''.  I use https://minecraftjson.com/ to help me make my JSON. "),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								"#Example: menu: '{\"text\":\"[test]\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"}}'"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(("JSON: '" + (String.valueOf(aa89d9df482d7466591062440e807ee2b) + "'"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								("#Permision to be able to use this menu. Setting them to the same value as other menus works as you assume. Default: potato.jsonmenu.menu"
										+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(("permission: 'potato.jsonmenu.menu"
								+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + "'"))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton(
								"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your menu and that makes it sad."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(new File(
						(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (String.valueOf(accb69c8bfbc31b9d514fb72cc21dc24c) + ".yml"))).toPath(),
						Collections.singleton("menuVersion: 1.01"), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
			}
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color("Config is newer than 1.01 updating!"));
			}
			ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(1d));
			if ((((Number) PluginMain.getInstance().getConfig().get("configVersion"))
					.doubleValue() < ((Number) VariableManager.getValue(false,
							new ArrayList(Arrays.asList("pluginVersion")))).doubleValue())) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender()
							.sendMessage(PluginMain.color("Older version detected, only minor update needed."));
				}
				a2d037a7f5c916ee046a675091428d625 = PluginMain.getInstance().getConfig().get("debug");
				a3e9c510676e205eee84d88589a797e9f = PluginMain.getInstance().getConfig().get("menus");
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
						.delete();
				new File(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
						.createNewFile();
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton("################## GLOBAL SETTINGS ##################"),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(("debug: " + String.valueOf(a2d037a7f5c916ee046a675091428d625))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton("#How many menus do you want? Default is 1. "),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(
								"#Menus have been moved to their own files for easier editing of multiple menus."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(("menus: " + String.valueOf(a3e9c510676e205eee84d88589a797e9f))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
						java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(
								"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your config and that makes it sad."),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				Files.write(
						new File(String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("configFile")))))
										.toPath(),
						Collections.singleton(("configVersion: " + String.valueOf(
								VariableManager.getValue(false, new ArrayList(Arrays.asList("pluginVersion")))))),
						java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
				while ((((Number) ac7e54802b7c5d9dab276d69f24f305c7)
						.doubleValue() <= ((Number) PluginMain.getInstance().getConfig().get("menus")).doubleValue())) {
					if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
						Bukkit.getConsoleSender().sendMessage(PluginMain.color(("DEBUG: Running check against menu "
								+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))));
					}
					if ((((Number) org.bukkit.configuration.file.YamlConfiguration
							.loadConfiguration(new File((String.valueOf(
									VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
									+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
							.get("menuVersion"))
									.doubleValue() < ((Number) VariableManager.getValue(false,
											new ArrayList(Arrays.asList("pluginVersion")))).doubleValue())) {
						a42f0255461bdb78c28dd7689b3b51752 = org.bukkit.configuration.file.YamlConfiguration
								.loadConfiguration(new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
								.get("onJoin");
						a1f75ee55d82f2f492d4c449fc408cf31 = org.bukkit.configuration.file.YamlConfiguration
								.loadConfiguration(new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
								.get("onJoinDelay");
						a206f29d3782cdea4d073bd356cebd6f2 = org.bukkit.configuration.file.YamlConfiguration
								.loadConfiguration(new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
								.get("JSON");
						ac638de2c6894c7b946383722bdfaf809 = org.bukkit.configuration.file.YamlConfiguration
								.loadConfiguration(new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))))
								.get("permission");
						new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml"))).delete();
						new File((String
								.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
										.createNewFile();
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(("################## MENU "
										+ (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + " ##################"))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										"#onJoin: Do you want this menu to behave like an MOTD and be sent to players on connect? Default: false"),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(("onJoin: " + String.valueOf(a42f0255461bdb78c28dd7689b3b51752))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										"#onJoinDelay: Set the time in ticks you want to hold the message on join. This can help with lining it up with an MOTD  or with other menus. Default is 5. Requires the perm to use command."),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										("onJoinDelay: " + String.valueOf(a1f75ee55d82f2f492d4c449fc408cf31))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										"#menu: put your ENTIRE compacted JSON between the ''.  I use https://minecraftjson.com/ to help me make my JSON. "),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										"#Example: menu: '{\"text\":\"[test]\",\"color\":\"dark_green\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://www.google.com\"}}'"),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										("JSON: '" + (String.valueOf(a206f29d3782cdea4d073bd356cebd6f2) + "'"))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										("#Permision to be able to use this menu. Setting them to the same value as other menus works as you assume. Default: potato.jsonmenu.menu"
												+ String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										("permission: '" + (String.valueOf(ac638de2c6894c7b946383722bdfaf809) + "'"))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(""), java.nio.charset.StandardCharsets.UTF_8,
								java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(
										"#Future proofing! Don't change this. I can't stop you, but doing so can corrupt your menu and that makes it sad."),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
						Files.write(
								new File((String.valueOf(
										VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
										+ (("menu" + String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7)) + ".yml")))
												.toPath(),
								Collections.singleton(("menuVersion: " + String.valueOf(VariableManager.getValue(false,
										new ArrayList(Arrays.asList("pluginVersion")))))),
								java.nio.charset.StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
					}
					ac7e54802b7c5d9dab276d69f24f305c7 = UtilMethods.addToObject(ac7e54802b7c5d9dab276d69f24f305c7,
							new java.lang.Double(1d));
					ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
							Math.round(((Number) ac7e54802b7c5d9dab276d69f24f305c7).doubleValue()));
				}
			}
		}
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
	public void onPlayerJoinEvent6(org.bukkit.event.player.PlayerJoinEvent event) throws Exception {
		Object ac7e54802b7c5d9dab276d69f24f305c7 = null;
		if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
			Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: Player Joined"));
		}
		ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(Math.round(0d));
		for (int a95f281c6b5e542c88d8ce12978bed654 = 0; a95f281c6b5e542c88d8ce12978bed654 < ((Number) PluginMain
				.getInstance().getConfig().get("menus")).intValue(); a95f281c6b5e542c88d8ce12978bed654++) {
			ac7e54802b7c5d9dab276d69f24f305c7 = UtilMethods.addToObject(ac7e54802b7c5d9dab276d69f24f305c7,
					new java.lang.Long(Math.round(1d)));
			ac7e54802b7c5d9dab276d69f24f305c7 = new java.lang.Long(
					Math.round(((Number) ac7e54802b7c5d9dab276d69f24f305c7).doubleValue()));
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color(("DEBUG: looking for onJoin in "
						+ (String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ ("Menu" + (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + ".yml"))))));
			}
			if (((java.lang.Boolean) org.bukkit.configuration.file.YamlConfiguration.loadConfiguration(new File(
					(String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
							+ ("Menu" + (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + ".yml")))))
					.get("onJoin")).booleanValue()) {
				if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
					Bukkit.getConsoleSender().sendMessage(PluginMain.color("DEBUG: onJoin found! "));
				}
				procedure("runMenu",
						new ArrayList(Arrays.asList(event.getPlayer(), new java.lang.Long(Math.round(-1d)))));
				break;
			}
			if (((java.lang.Boolean) PluginMain.getInstance().getConfig().get("debug")).booleanValue()) {
				Bukkit.getConsoleSender().sendMessage(PluginMain.color(("DEBUG: onJoin not found in  "
						+ (String.valueOf(VariableManager.getValue(false, new ArrayList(Arrays.asList("menusFolder"))))
								+ ("Menu" + (String.valueOf(ac7e54802b7c5d9dab276d69f24f305c7) + ".yml"))))));
			}
		}
	}
}
