package de.devsnx.redisMessageBridge.listener;

import de.devsnx.redisMessageBridge.manager.ChatManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 24.10.2024 22:08
 */

public class PlayerChatListener implements Listener {

    private final ChatManager chatManager;

    public PlayerChatListener(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Formatierte Nachricht, die im Chat angezeigt wird
        String message = String.format("%s: %s", event.getPlayer().getDisplayName(), event.getMessage());
        String channel = "";

        // Sende die Nachricht an alle Server via Redis
        chatManager.publishChatMessage(message);

        // Optional: Nachricht lokal auf diesem Server unterdrücken, wenn sie nur global gesendet werden soll
        event.setCancelled(true);
    }

}