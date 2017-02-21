package nxlogin.commands;

import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.permission.Permission;

public class ExampleCommand extends Command {

	public ExampleCommand() {
		super("명령어", "명령어테스트용입니다", "/명렁어 <메세지 전송 할 타켓> [전송할 메세지]");
		
		setCommandParameters(null);
		// setCcommandParameters() 의 인자는 Map<String,CommandParameter[]>
		Map<String, CommandParameter[]> commandParameters = new HashMap<String, CommandParameter[]>();

		commandParameters.put("명령어",
				new CommandParameter[] { 
						new CommandParameter("메세지를 전송 할 타겟", CommandParameter.ARG_TYPE_TARGET, false),
						new CommandParameter("전솔 할 메세지", CommandParameter.ARG_TYPE_RAW_TEXT, true) });
		
		setCommandParameters(commandParameters);
		this.setPermission(Permission.DEFAULT_TRUE);
		
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {

		Player player = Server.getInstance().getPlayer(args[0].toLowerCase());

		String msg = "";
		for (int i = 1; i < args.length; i++) {
			msg += args[i] + " ";
		}

		player.sendMessage(msg);
		return true;
	}

}
