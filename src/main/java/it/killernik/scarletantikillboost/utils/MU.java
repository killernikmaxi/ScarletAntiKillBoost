package it.killernik.scarletantikillboost.utils;

import it.killernik.scarletantikillboost.ScarletAntiKillBoost;
import it.killernik.scarletantikillboost.manager.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MU {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void message(Player player, String message) {
        player.sendMessage(color(message));
    }

    public static void alert(String message, String playerName, int flags) {
        String alertMessage = color(message.replace("%player%", playerName).replace("%flags%", String.valueOf(flags)));

        Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.hasPermission("sakb.alert"))
                .forEach(player -> player.sendMessage(alertMessage));

        ConfigManager config = ScarletAntiKillBoost.get().getConfigManager();

        if (!config.isWebhookAlertEnabled()) return;

        String webhookUrl = config.getWebhookUrl();
        String username = config.getWebhookUsername();
        String title = config.getWebhookTitle();
        String desc = config.getWebhookDescription(playerName, flags);
        String avatarUrl = config.getWebhookAvatarUrl();
        String thumbnailUrl = config.getWebhookThumbnailUrl();

        WebHookUtils webhook = new WebHookUtils(webhookUrl);

        webhook.setUsername(username);
        webhook.setAvatarUrl(avatarUrl);

        webhook.addEmbed(new WebHookUtils.EmbedObject()
                .setTitle(title)
                .setDescription(desc)
                .setThumbnail(thumbnailUrl));
        try {
            webhook.execute();
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

}
