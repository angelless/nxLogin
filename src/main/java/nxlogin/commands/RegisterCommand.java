package nxlogin.commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;
import cn.nukkit.utils.TextFormat;
import nxlogin.Main;
import nxlogin.data.UserData;

public class RegisterCommand extends Command {
	public RegisterCommand() {
		super("회원가입", "회원가입합니다", "/회원가입 비밀번호", new String[] { "register" });
		this.commandParameters.clear();
		Map<String, CommandParameter[]> parameters = new HashMap<>();
		parameters.put("회원가입",
				new CommandParameter[] { new CommandParameter("설정 할 비밀번호") });
		this.setCommandParameters(parameters);
		this.setPermission(Permission.DEFAULT_TRUE);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (UserData.getInstance().isRegister(sender.getName())) {
			sender.sendMessage(Main.alert("이 계정은 이미 회원가입이 되어있는 계정입니다. 다른 닉네임으로 접속해주시거나 비밀번호를 찾아주세요"));
			return true;
		}

		if (!(args.length >= 1)) {
			sender.sendMessage(Main.alert("정확하게 명령어를 입력하여주세요"));
			return true;
		}

		if (args[0].length() < 4) {
			sender.sendMessage(Main.alert("비밀번호는 최소 4글자이상으로 작성하여주세요"));
			return true;

		}

		sender.sendMessage(Main.success("비밀번호 : §b" + args[0] ));
		UserData.getInstance().register(sender.getName().toLowerCase(), args[0],
				((Player) sender).getAddress());
		Main.unLogins.remove(sender.getName().toLowerCase());
		sender.sendMessage(Main.success("정상적으로 로그인 되셨습니다"));
		return true;
	}
}
