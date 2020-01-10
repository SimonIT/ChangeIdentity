package de.simonit.changeidentity.listeners;

import de.simonit.changeidentity.ChangeIdentityPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChangedIdentityListener implements Listener {

	ChangeIdentityPlugin plugin;

	public ChangedIdentityListener(ChangeIdentityPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
		for (Player recipient : event.getRecipients()) {
			if (plugin.getChangedIdentities().containsKey(recipient)) {
				for (Player newIdentity : plugin.getChangedIdentities().get(recipient)) {
					newIdentity.sendMessage(event.getMessage());
				}
			}
		}
	}
}
