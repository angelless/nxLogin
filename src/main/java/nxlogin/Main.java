package nxlogin;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerCommandPreprocessEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.permission.Permission;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.TextFormat;
import nxlogin.commands.LoginCommand;
import nxlogin.commands.RegisterCommand;
import nxlogin.data.OperatorData;
import nxlogin.data.UserData;
import nxlogin.tasks.UnLoginPlayerTask;

public class Main extends PluginBase implements Listener {
	public static ArrayList<String> unLogins = new ArrayList<>();
	public static Main instance = null;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getLogger().info(TextFormat.colorize("&6nxLogin Plugin is Enable"));
		this.getServer().getScheduler().scheduleRepeatingTask(new UnLoginPlayerTask(this), 40);

		new UserData(this);
		this.getServer().getCommandMap().register("회원가입", new RegisterCommand());
		this.getServer().getCommandMap().register("로그인", new LoginCommand(this));
		Main.instance = this;
	}

	public static Main getInstance() {
		return Main.instance;
	}

	@Override
	public void onDisable() {
		UserData.getInstance().save();
	}

	/*
	 * 회원가입 아이디 비밀번호 로그인 아이디 비밀번호 관리진 로그인 아이디 비밀번호 2차 비밃번호
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		/*
		 * 이미 회원가입되어있는 계쩡인지 확인
		 */
		if (UserData.getInstance().isRegister(event.getPlayer().getName())) {
			/*
			 * 마지막으로 접속한 아이피랑 같은 아이피인지 대조
			 */
			if (UserData.getInstance().isLastIp(event.getPlayer().getName().toLowerCase(),
					event.getPlayer().getAddress())) {
				event.getPlayer().sendMessage(Main.success("로그인이 완료되셨습니다"));
				return;
			}
			event.getPlayer().sendMessage(Main.alert("서버를 즐기시기 전에 로그인해주세요! 방법 : 채팅창에 비밀번호를 입력 or /로그인 비밀번호"));
			event.getPlayer().sendMessage(Main.alert("해당 계정이 본인의 계쩡이 아닐 시 다른 닉네임으로 다시 접속을 시도 해 주세요"));
			unLogins.add(event.getPlayer().getName().toLowerCase());
			return;
		} else {
			event.getPlayer().sendMessage(message("회원가입 후 서버를 플레이 해주세요 명령어: §a/회원가입 <설정할 비밀번호>"));
			unLogins.add(event.getPlayer().getName().toLowerCase());
			return;
		}

	}

	@EventHandler
	public void commandChat(PlayerCommandPreprocessEvent event) {
		if (unLogins.contains(event.getPlayer().getName().toLowerCase())) {

			String command = event.getMessage().split(" ")[0];
			command = command.substring(1, command.length());
			event.getPlayer()
					.sendMessage(Main.command(event.getMessage() + " => " + command + " == 로그인메세지들 ="
							+ (command.contentEquals("login") | command.contentEquals("register")
									| command.contentEquals("로그인") | command.contentEquals("회원가입"))));
			if (command.contentEquals("login") | command.contentEquals("register") | command.contentEquals("로그인")
					| command.contentEquals("회원가입")) {

			} else {
				event.getPlayer().sendMessage(Main.message("로그인 혹은 회원가입 후 서버를 즐겨주세요"));
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		if (Main.unLogins.contains(event.getPlayer().getName().toLowerCase())) {
			event.setCancelled();
			return;
		}
	}

	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		if (Main.unLogins.contains(event.getPlayer().getName().toLowerCase())) {
			event.setCancelled();
			return;
		}
	}

	@EventHandler
	public void OpenInventory(InventoryOpenEvent event) {
		if (Main.unLogins.contains(event.getPlayer().getName().toLowerCase())) {
			event.setCancelled();
			return;
		}
	}

	@EventHandler
	public void onChat(PlayerChatEvent event) {
		if (unLogins.contains(event.getPlayer().getName().toLowerCase())) {

			if (UserData.getInstance().isRegister(event.getPlayer().getName())) {

				if (UserData.getInstance().login(event.getPlayer().getName().toLowerCase(), event.getMessage(),
						event.getPlayer().getAddress())) {

					event.getPlayer().sendMessage(Main.success("정상적으로 로그인 되었습니다!"));
					unLogins.remove(event.getPlayer().getName().toLowerCase());
					event.setCancelled();
					return;
				}
				event.getPlayer().sendMessage(Main.alert("로그인 후 서버를 플레이 해 주세요, 본인의 계정이 아니면 다른 닉네임으로 회원가입해주시길 바랍니다"));
				event.setCancelled();
				return;

			}
			event.getPlayer().sendMessage(Main.alert("회원가입 후 서버를 플레이 해 주세요"));
			event.setCancelled();
			return;

		}
		event.getRecipients().removeAll(Main.unLogins);
		return;
	}

	// public LinkedHashMap<Player, String> share;
	// public ArrayList<String> list;
	// @EventHandler
	// public void onTouch(PlayerInteractEvent event){
	// Player player=event.getPlayer();
	// String level=player.getLevel().getFolderName();
	// Block block=event.getBlock();
	// if(this.isDoor(block)||this.isChest(block)){
	// String door=(block.getDamage()<=7) ?
	// block.getFloorX()+":"+(block.getFloorY()+1)+":"+block.getFloorZ()+":"+level
	// :
	// block.getFloorX()+":"+block.getFloorY()+":"+block.getFloorZ()+":"+level;
	// String
	// chest=block.getFloorX()+":"+block.getFloorY()+":"+block.getFloorZ()+":"+level;
	// String xyz=(this.isChest(block)) ? chest : door;
	// if(share.containsKey(player)){
	// if(!this.config.get(xyz).toString().contains("$"+player.getName())){
	// player.sendMessage("[Lock] 당신의 것이 아닙니다.");
	// event.setCancelled();
	// }
	// else if(this.config.get(xyz).toString().contains(player.getName())){
	// player.sendMessage("[Lock] 공유 해제");
	// this.config.set(xyz,this.config.get(xyz).toString().replace(share.get(player),
	// "null"));
	// share.remove(player);
	// event.setCancelled();
	// }
	// else{
	// player.sendMessage("[Lock] 공유");
	// this.config.set(xyz,this.config.get(xyz).toString()+","+share.get(player));
	// share.remove(player);
	// event.setCancelled();
	// }
	// }
	// if(list.contains(player)){
	// if(this.config.get(xyz).toString().startsWith("$"+player.getName())){
	// player.sendMessage("[Lock] 잠금 해제");
	// this.config.set(xyz, "false");
	// list.remove(player);
	// event.setCancelled();
	// }
	// else if(this.config.get(xyz).toString().equals("false")){
	// player.sendMessage("[Lock] 잠금");
	// this.config.set(xyz, "$"+player.getName());
	// list.remove(player);
	// event.setCancelled();
	// }
	// else{
	// player.sendMessage("[Lock] 당신의 것이 아닙니다.");
	// event.setCancelled();
	// }
	// }
	// else{
	// if(player.isOp()){
	// return;
	// }
	// if(this.config.get(xyz).toString().contains(player.getName())){
	// return;
	// }
	// else{
	// player.sendMessage("[Lock] 권한이 없습니다.");
	// event.setCancelled();
	// }
	// }
	// }
	// // TODO : 상자 잠금 기능 추가
	// }
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (unLogins.contains(event.getPlayer().getName().toLowerCase())) {
			unLogins.remove(event.getPlayer().getName().toLowerCase());
			return;
		}
	}

	public static String message(String message) {
		return "§a§l[알림] §r§7" + message;
	}

	public static String alert(String message) {
		return "§c§l[알림] §r§7" + message;
	}

	public static String command(String message) {
		return "§l§6[알림]§r§7 " + message;
	}

	public static String success(String message) {
		return "§l§b[안내]§r§7 " + message;
	}
}
