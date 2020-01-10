package de.simonit.changeidentity.commands;

import com.bringholm.nametagchanger.NameTagChanger;
import de.simonit.changeidentity.ChangeIdentityPlugin;
import de.simonit.changeidentity.data.OldPlayerInfos;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResetIdentityCommand extends AutoCommand<ChangeIdentityPlugin> {
	public ResetIdentityCommand(ChangeIdentityPlugin plugin) {
		super(plugin, "resetidentity", "Reset to own player", "cidexit");
	}

	@Override
	public boolean execute(@NotNull CommandSender cs, @NotNull String label, @NotNull String[] args) {
		if (cs.isOp() || cs.hasPermission("changeIdentity.resetidentity")) {
			if (cs instanceof Player) {
				OldPlayerInfos oldPlayerInfos = getPlugin().getOldPlayerInfosMap().remove(cs);
				if (oldPlayerInfos != null) {
					((Player) cs).setDisplayName(oldPlayerInfos.getDisplayName());
					((Player) cs).setPlayerListName(oldPlayerInfos.getListName());
					Chat c = ChangeIdentityPlugin.getChat();
					if (c != null) {
						c.setPlayerPrefix((Player) cs, oldPlayerInfos.getPrefix());
						c.setPlayerSuffix((Player) cs, oldPlayerInfos.getSuffix());
					}
					NameTagChanger changer = ChangeIdentityPlugin.getNameTagChanger();
					changer.resetPlayerName((Player) cs);
					changer.resetPlayerSkin((Player) cs);
					changer.updatePlayer((Player) cs);
					Map<Player, List<Player>> identi = getPlugin().getChangedIdentities();
					for (Map.Entry<Player, List<Player>> identities :  identi.entrySet()) {
						if (identities.getValue().contains(cs)) {
							identi.get(identities.getKey()).remove(cs);
							break;
						}
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public @NotNull List<String> tabComplete(@NotNull CommandSender cs, @NotNull String label, @NotNull String[] args) {
		return new ArrayList<>();
	}
}
