package nxlogin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

import nxlogin.commands.LoginCommand;
import nxlogin.commands.RegisterCommand;
import nxlogin.data.UserData;
import nxlogin.tasks.UnLoginPlayerTask;

/*
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.scheduler.Task;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
import cn.nukkit.entity.Entity;

import java.net.InetSocketAddress;
import java.util.List;

import cn.nukkit.event.player.PlayerChatEvent;
*/

public class Main extends PluginBase implements Listener {
	public static ArrayList<Player> unLogins = new ArrayList<>();
	public static Main instance = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getLogger().info(TextFormat.colorize("&6nxLogin Plugin is Enable"));
		this.getServer().getScheduler().scheduleRepeatingTask(new UnLoginPlayerTask(this), 40);
		this.getServer().getCommandMap().register("회원가입", new RegisterCommand());
		this.getServer().getCommandMap().register("로그인", new LoginCommand());
		new UserData(this);
		Main.instance = this;
	}

	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public void onDisable() {
		UserData.getInstance().save();
	}


	@EventHandler
	public void onPreJoin(PlayerPreLoginEvent event) {
		unLogins.add(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event) {
		UserData.getInstance().ipLogin(event.getPlayer());
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (unLogins.contains(event.getPlayer())) {
			Set<CommandSender> recipients = new HashSet<CommandSender>();
			event.getRecipients().stream().filter(re -> re.isPlayer() && unLogins.contains((Player) re))
					.forEach(re -> recipients.add(re));
			event.setRecipients(recipients);
			return;
		}
		UserData.getInstance().passwordLogin(event.getPlayer(), event.getMessage());
		event.setCancelled(true);

	}

	@EventHandler
	public void commandChat(PlayerCommandPreprocessEvent event) {
		if (unLogins.contains(event.getPlayer())) {
			String command = event.getMessage().split(" ")[0];
			command = command.substring(1, command.length());
			if (!(command.contentEquals("로그인") | command.contentEquals("회원가입") | command.contentEquals("login")
					| command.contentEquals("register"))) {
				event.getPlayer().sendMessage(Main.message("로그인 혹은 회원가입 후 서버를 즐겨주세요"));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void openInventory(InventoryOpenEvent event) {
		if (Main.unLogins.contains(event.getPlayer())) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (unLogins.contains(event.getPlayer())) {
			unLogins.remove(event.getPlayer());
			return;
		}
	}

	public static String message(String message) {
		return new StringBuilder().append("§a§l[알림] §r§7").append(message).toString();
	}

	public static String alert(String message) {
		return new StringBuilder().append("§c§l[알림] §r§7").append(message).toString();
	}

	public static String command(String message) {
		return new StringBuilder().append("§6§l[알림] §r§7").append(message).toString();
	}

	public static String success(String message) {
		return new StringBuilder().append("§b§l[안내] §r§7").append(message).toString();
	}
}
