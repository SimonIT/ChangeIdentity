package de.simonit.changeidentity;

import com.bringholm.nametagchanger.NameTagChanger;
import de.simonit.changeidentity.commands.ChangeIdentityCommand;
import de.simonit.changeidentity.commands.ResetIdentityCommand;
import de.simonit.changeidentity.data.OldPlayerInfos;
import de.simonit.changeidentity.listeners.ChangedIdentityListener;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ChangeIdentityPlugin extends JavaPlugin {

	@Getter
	private static final Logger log = Logger.getLogger("Minecraft");
	@Getter
	private static Economy econ = null;
	@Getter
	private static Permission perms = null;
	@Getter
	private static Chat chat = null;
	@Getter
	private static NameTagChanger nameTagChanger;

	@Getter
	private Map<Player, OldPlayerInfos> oldPlayerInfosMap = new HashMap<>();
	@Getter
	private Map<Player, List<Player>> changedIdentities = new HashMap<>();

	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
	}

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().getPlugin("Vault") != null) {
			setupPermissions();
			setupChat();
		} else {
			log.info("Vault not found - continue with initialisation");
		}
		nameTagChanger = NameTagChanger.INSTANCE;

		new ChangeIdentityCommand(this);
		new ResetIdentityCommand(this);

		getServer().getPluginManager().registerEvents(new ChangedIdentityListener(this), this);
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) return false;
		econ = rsp.getProvider();
		return true;
	}

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		if (rsp == null) return false;
		chat = rsp.getProvider();
		return true;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		if (rsp == null) return false;
		perms = rsp.getProvider();
		return true;
	}
}
