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
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ChangeIdentityPlugin extends JavaPlugin {

	@Getter
	private static final Logger log = Logger.getLogger("Minecraft");
	@Getter @Nullable
	private static Economy econ = null;
	@Getter @Nullable
	private static Permission perms = null;
	@Getter @Nullable
	private static Chat chat = null;
	@Getter @Nullable
	private static NameTagChanger nameTagChanger;

	@Getter
	private final Map<Player, OldPlayerInfos> oldPlayerInfosMap = new HashMap<>();
	@Getter
	private final Map<Player, List<Player>> changedIdentities = new HashMap<>();

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
		try {
			nameTagChanger = NameTagChanger.INSTANCE;
		} catch (ExceptionInInitializerError e)  {
			log.warning("NameTagChanger is incompatible to this bukkit version. Resuming without it. " + e.toString());
		}

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
