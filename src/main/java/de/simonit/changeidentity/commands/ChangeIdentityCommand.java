package de.simonit.changeidentity.commands;

import com.bringholm.nametagchanger.NameTagChanger;
import com.bringholm.nametagchanger.skin.Skin;
import com.google.common.collect.Lists;
import de.simonit.changeidentity.ChangeIdentityPlugin;
import de.simonit.changeidentity.data.OldPlayerInfos;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChangeIdentityCommand extends AutoCommand<ChangeIdentityPlugin> {
	public ChangeIdentityCommand(ChangeIdentityPlugin plugin) {
		super(plugin, "changeidentity", "Change to another player", "speakas", "cid");
	}

	@Override
	public boolean execute(@NotNull CommandSender cs, @NotNull String label, @NotNull String[] args) {
		if (cs.isOp() || cs.hasPermission("changeIdentity.changeidentity")) {
			if (cs instanceof Player) {
				if (args.length > 0) {
					Player playerToCopy = Bukkit.getPlayerExact(args[0]);
					if (playerToCopy != null) {
						OldPlayerInfos oldPlayerInfos = new OldPlayerInfos();
						oldPlayerInfos.setDisplayName(((Player) cs).getDisplayName());
						((Player) cs).setDisplayName(playerToCopy.getDisplayName());
						oldPlayerInfos.setListName(((Player) cs).getPlayerListName());
						((Player) cs).setPlayerListName(playerToCopy.getPlayerListName());
						oldPlayerInfos.setCustomName(((Player) cs).getCustomName());
						((Player) cs).setCustomName(playerToCopy.getCustomName());
						((Player) cs).setCustomNameVisible(playerToCopy.isCustomNameVisible());
						Chat c = ChangeIdentityPlugin.getChat();
						if (c != null) {
							oldPlayerInfos.setPrefix(c.getPlayerPrefix((Player) cs));
							c.setPlayerPrefix((Player) cs, c.getPlayerPrefix(playerToCopy));
							oldPlayerInfos.setSuffix(c.getPlayerSuffix((Player) cs));
							c.setPlayerSuffix((Player) cs, c.getPlayerSuffix(playerToCopy));
						}
						getPlugin().getOldPlayerInfosMap().put((Player) cs, oldPlayerInfos);
						NameTagChanger changer = ChangeIdentityPlugin.getNameTagChanger();
						if (changer != null) {
							String newName = changer.getChangedName(playerToCopy);
							if (newName == null) newName = playerToCopy.getName();
							try {
								changer.changePlayerName((Player) cs, newName);
							} catch (RuntimeException e) {
								ChangeIdentityPlugin.getLog().warning("This error occurred\n" + e.toString() + "\nhttps://github.com/Alvin-LB/NameTagChanger/issues/13");
							}
							Skin newSkin = changer.getChangedSkin(playerToCopy);
							if (newSkin == null) newSkin = changer.getDefaultSkinFromPlayer(playerToCopy);
							changer.setPlayerSkin((Player) cs, newSkin);
							try {
								changer.updatePlayer((Player) cs);
							} catch (RuntimeException e) {
								ChangeIdentityPlugin.getLog().warning("This error occurred\n" + e.toString() + "\nhttps://github.com/Alvin-LB/NameTagChanger/issues/13");
							}
						}
						Map<Player, List<Player>> identityMap = getPlugin().getChangedIdentities();
						if (identityMap.containsKey(playerToCopy)) {
							identityMap.get(playerToCopy).add((Player) cs);
						} else {
							identityMap.put(playerToCopy, Lists.newArrayList((Player) cs));
						}
						return true;
					}
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
