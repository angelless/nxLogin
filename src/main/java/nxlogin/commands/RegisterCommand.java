package nxlogin.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import nxlogin.Main;
import nxlogin.data.UserData;

public class RegisterCommand extends Command {
	public RegisterCommand() {
		super("회원가입", "회원가입합니다", "/회원가입 비밀번호", new String[] { "register" });
		this.commandParameters.clear();
		this.commandParameters.put("회원가입", new CommandParameter[] { new CommandParameter("설정 할 비밀번호") });

		this.setPermission("");
		this.commandData.permission = "";
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(Main.command("명령어를 정확하게 입력하여주세요"));
			return true;
		}
		UserData.getInstance().register(sender.getName(), args[0]);
		Server.getInstance().getCommandMap().getCommand("로그인").execute(sender, commandLabel, args);
		return true;
	}
}
