package nxlogin.commands;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import nxlogin.data.UserData;

public class LoginCommand extends Command {
	public LoginCommand() {
		super("로그인", "로그인 명령어", "/로그인  <비밀번호>", new String[] { "login" });
		this.setCommandParameters(new HashMap<String, CommandParameter[]>() {
			/**
			 * serialVersionUID waring
			 */
			private static final long serialVersionUID = 5572221817236667426L;

			{
				put("로그인", new CommandParameter[] {
						new CommandParameter("비밀번호:password", CommandParameter.ARG_TYPE_RAW_TEXT, true) });
			}
		});
		this.setPermission("");
		this.commandData.permission = "";

	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		String password;
		Player user;
		if (!sender.isPlayer()) {
			sender.sendMessage("인게임명령어");
			return true;
		}
		try {
			password = args[0];
		} catch (Exception e) {
			password = " ";
		}
		user = (Player) sender;
		UserData.getInstance().passwordLogin(user, password);
		return true;

	}
}
