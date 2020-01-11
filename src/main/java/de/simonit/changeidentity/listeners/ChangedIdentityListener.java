package de.simonit.changeidentity.listeners;

import de.simonit.changeidentity.ChangeIdentityPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public class ChangedIdentityListener implements Listener {

	private ChangeIdentityPlugin plugin;

	public ChangedIdentityListener(ChangeIdentityPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(@NotNull AsyncPlayerChatEvent event) {
		for (Player recipient : event.getRecipients()) {
			if (plugin.getChangedIdentities().containsKey(recipient)) {
				for (Player newIdentity : plugin.getChangedIdentities().get(recipient)) {
					if (!newIdentity.equals(event.getPlayer()) && !event.getRecipients().contains(newIdentity)) {
						newIdentity.sendMessage(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()));
					}
				}
			}
		}
		if (plugin.getOldPlayerInfosMap().containsKey(event.getPlayer()))
			ChangeIdentityPlugin.getLog().info("Message by " + event.getPlayer().getName() + ":");
	}
}
