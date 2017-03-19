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
		} else {
			for (Player p : Main.unLogins) {
				if (p.getPosition().equals(owner.getServer().getDefaultLevel().getSpawnLocation())) {
					p.sendTip(Main.alert("로그인 후 서버를 플레이해주세요"));
					return;
				} else {
					p.sendTip(Main.alert("로그인 후 서버를 플레이해주세요"));
					p.teleport(owner.getServer().getDefaultLevel().getSpawnLocation());
					p.sendMessage(Main.alert("회원가입이나 로그인 후 서버를 플레이 해 주세요"));
				}
			}
		}

	}
}
