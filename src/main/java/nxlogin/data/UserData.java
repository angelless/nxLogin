package nxlogin.data;

import java.io.File;
import java.util.LinkedHashMap;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import nxlogin.Main;

public class UserData {
	private static UserData instance = null;
	public static LinkedHashMap<String, Object> password = new LinkedHashMap<>();
	public static LinkedHashMap<String, Object> address = new LinkedHashMap<>();
	private static Main plugin = null;

	@SuppressWarnings({ "deprecation", "serial" })
	public UserData(Main plugin) {
		plugin.getDataFolder().mkdirs();
		Config password = new Config(new File(plugin.getDataFolder(), "password.json"), Config.JSON,
				new ConfigSection(new LinkedHashMap<String, Object>() {
					{
						put("steve", "mynameissteve");
					}
				}));
		Config address = new Config(new File(plugin.getDataFolder(), "address.json"), Config.JSON,
				new ConfigSection(new LinkedHashMap<String, Object>() {
					{
						put("steve", "mynameissteve");
					}
				}));

		UserData.password = configSet(password);
		UserData.address = configSet(address);
		UserData.plugin = plugin;
		instance = this;
	}

	public LinkedHashMap<String, Object> configSet(Config config) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map = (LinkedHashMap<String, Object>) config.getAll();
		return map;
	}

	public void save() {
		Config password = new Config(new File(Main.getInstance().getDataFolder(), "password.json"), Config.JSON);
		Config address = new Config(new File(Main.getInstance().getDataFolder(), "address.json"), Config.JSON);
		password.setAll(UserData.password);
		address.setAll(UserData.address);
		password.save();
		address.save();
	}

	public static UserData getInstance() {
		return instance;
	}

	@SuppressWarnings("serial")
	public void register(String user, String password, String ip) {
		UserData.address.put(user.toLowerCase(), ip);
		UserData.password.put(user.toLowerCase(), password);
	}

	public boolean login(String user, String password, String ip) {
		if (address.get(user.toLowerCase()).equals(ip)) {
			return true;
		}
		if (UserData.password.get(user.toLowerCase()).equals(password.toLowerCase())) {
			address.replace(user.toLowerCase(), ip);
			return true;
		}
		return false;
	}

	public void unregister(String user) {
		password.remove(user.toLowerCase());
		address.remove(user.toLowerCase());
	}

	public boolean isRegister(String user) {
		return password.containsKey(user.toLowerCase());
	}

	public String getLastIp(String user) {
		return (address.get(user.toLowerCase())+"");
	}

	public boolean isLastIp(String user, String address) {
		if (getLastIp(user).equals(address)) {
			return true;
		}
		return false;
	}

}
