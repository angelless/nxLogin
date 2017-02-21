package nxlogin.commands;

import java.util.HashMap;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import nxlogin.Main;
import nxlogin.data.UserData;

public class LoginCommand extends Command {
	Main plugin = null;

	public LoginCommand(Main plugin) {
		super("로그인", "로그인 명령어", "/로그인  <비밀번호>", new String[] { "login" });
		CommandParameter[] commands = new CommandParameter[] {
				new CommandParameter("비밀번호:password", CommandParameter.ARG_TYPE_RAW_TEXT, true) };
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			{
				put("로그인", commands);
			}
		});
		this.setPermission(Permission.DEFAULT_TRUE);
		this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!Main.unLogins.contains(sender.getName().toLowerCase())) {
			sender.sendMessage(Main.alert("당신은 이미 로그인 되어있습니다"));
			return true;
		}

		if (!(args.length <= 1)) {
			sender.sendMessage(Main.alert("비밀번호를 입력하여 주세요"));
			return true;
		}

		if (UserData.getInstance().login(sender.getName(), args[0], "")) {
			Main.unLogins.remove(sender.getName().toLowerCase());
			sender.sendMessage(Main.success("정상적으로 로그인 되셨습니다"));
			return true;
		}

		return false;

	}
}
