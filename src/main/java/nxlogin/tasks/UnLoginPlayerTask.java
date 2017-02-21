package nxlogin.tasks;

import cn.nukkit.Player;
import cn.nukkit.scheduler.PluginTask;
import nxlogin.Main;

public class UnLoginPlayerTask extends PluginTask<Main> {

	public UnLoginPlayerTask(Main owner) {
		super(owner);
	}

	@Override
	public void onRun(int currentTick) {
		if (Main.unLogins.isEmpty()) {
			return;
		}
		for (String p : Main.unLogins) {
			Player player = this.owner.getServer().getPlayerExact(p);
			if (player.getPosition().equals(owner.getServer().getDefaultLevel().getSpawnLocation())) {
				player.sendTip(Main.alert("로그인 후 서버를 플레이해주세요"));
				return;
			}
			player.sendTip(Main.alert("로그인 후 서버를 플레이해주세요"));
			player.teleport(owner.getServer().getDefaultLevel().getSpawnLocation());
			player.sendMessage(Main.alert("회원가입이나 로그인 후 서버를 플레이 해 주세요"));

		}

	}
}
