package it.killernik.scarletantikillboost.manager;

import it.killernik.scarletantikillboost.utils.MU;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    private long resetInterval;
    private int alertInterval;

    private List<String> helpMessages;
    private String configReloadedMessage;
    private String viewFlags;
    private String flagsResetMessage;
    private String incorrectSyntaxMessage;
    private String specifyPlayerMessage;
    private String alertMessage;

    private boolean timeBasedCheckEnabled;
    private int timeBasedStartHour;
    private int timeBasedEndHour;
    private int timeBasedFlagWeight;

    private boolean sameIpCheckEnabled;
    private int sameIpFlagWeight;

    private boolean killFrequencyCheckEnabled;
    private int maxKillsPerMinute;
    private int killFrequencyFlagWeight;

    private boolean samePlayerKillsCheckEnabled;
    private int maxKillsPerPlayerPerMinute;
    private int samePlayerKillsFlagWeight;

    private boolean webhookAlertEnabled;
    private String webhookUrl;
    private String webhookAvatarUrl;
    private String webhookThumbnailUrl;
    private String webhookUsername;
    private String webhookTitle;
    private String webhookDescription;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
        reloadConfig();
    }

    private void saveDefaultConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        loadConfigValues();
    }

    private void loadConfigValues() {
        resetInterval = config.getLong("SETTINGS.reset-interval", 20);
        alertInterval = config.getInt("SETTINGS.alert-interval", 5);

        helpMessages = config.getStringList("LANG.help");
        configReloadedMessage = MU.color(config.getString("LANG.config-reloaded", "&4&lSCARLETANTIKILLBOOST &8// &aConfig ricaricato!"));
        viewFlags = MU.color(config.getString("LANG.view-flags", "&4&lSCARLETANTIKILLBOOST &8// &aFlags di %player%: &b%flags%."));
        flagsResetMessage = MU.color(config.getString("LANG.flags-reset", "&4&lSCARLETANTIKILLBOOST &8// &aFlags resettati con successo a %player%!"));
        incorrectSyntaxMessage = MU.color(config.getString("LANG.incorrect-syntax", "&4&lSCARLETANTIKILLBOOST &8// &cErrore: Sintassi errata!"));
        specifyPlayerMessage = MU.color(config.getString("LANG.specify-player", "&4&lSCARLETANTIKILLBOOST &8// &cErrore: Specifica un giocatore!"));
        alertMessage = MU.color(config.getString("LANG.alert-message", "&4&lSCARLETANTIKILLBOOST &8// &c%player% e' sospetto per kill-boosting! Flags: &4%flags%"));

        timeBasedCheckEnabled = config.getBoolean("CHECKS.TIME-BASED.enabled", true);
        timeBasedStartHour = config.getInt("CHECKS.TIME-BASED.start-hour", 0);
        timeBasedEndHour = config.getInt("CHECKS.TIME-BASED.end-hour", 8);
        timeBasedFlagWeight = config.getInt("CHECKS.TIME-BASED.flag-weight", 5);

        sameIpCheckEnabled = config.getBoolean("CHECKS.SAME-IP.enabled", true);
        sameIpFlagWeight = config.getInt("CHECKS.SAME-IP.flag-weight", 2);

        killFrequencyCheckEnabled = config.getBoolean("CHECKS.KILL-FREQUENCY.enabled", true);
        maxKillsPerMinute = config.getInt("CHECKS.KILL-FREQUENCY.max-kills-per-minute", 5);
        killFrequencyFlagWeight = config.getInt("CHECKS.KILL-FREQUENCY.flag-weight", 3);

        samePlayerKillsCheckEnabled = config.getBoolean("CHECKS.SAME-PLAYER-KILLS.enabled", true);
        maxKillsPerPlayerPerMinute = config.getInt("CHECKS.SAME-PLAYER-KILLS.max-kills-per-player-per-minute", 3);
        samePlayerKillsFlagWeight = config.getInt("CHECKS.SAME-PLAYER-KILLS.flag-weight", 4);

        webhookAlertEnabled = config.getBoolean("WEBHOOKS.ALERT.enabled", false);
        webhookUrl = config.getString("WEBHOOKS.ALERT.webhook-url", "YOUR_WEBHOOK_URL");
        webhookAvatarUrl = config.getString("WEBHOOKS.ALERT.avatar-url", "YOUR_AVATAR_URL");
        webhookThumbnailUrl = config.getString("WEBHOOKS.ALERT.thumbnail-url", "YOUR_THUMBNAIL_URL");
        webhookUsername = config.getString("WEBHOOKS.ALERT.username", "SCARLETANTIKILLBOOST");
        webhookTitle = config.getString("WEBHOOKS.ALERT.title", "ALLERTA KILLBOOST");
        webhookDescription = config.getString("WEBHOOKS.ALERT.description", "%player% e' sospetto di kill-boosting! Flags: &4%flag%");
    }

    public long getResetInterval() { return resetInterval; }

    public int getAlertInterval() {
        return alertInterval;
    }

    public List<String> getHelpMessages() {
        return helpMessages;
    }

    public String getConfigReloadedMessage() {
        return configReloadedMessage;
    }
    public String getViewFlagsMessage() { return viewFlags; }

    public String getFlagsResetMessage() {
        return flagsResetMessage;
    }

    public String getIncorrectSyntaxMessage() {
        return incorrectSyntaxMessage;
    }

    public String getSpecifyPlayerMessage() {
        return specifyPlayerMessage;
    }
    public String getAlertMessage() {
        return MU.color(alertMessage);
    }

    public boolean isTimeBasedCheckEnabled() {
        return timeBasedCheckEnabled;
    }

    public int getTimeBasedStartHour() {
        return timeBasedStartHour;
    }

    public int getTimeBasedEndHour() {
        return timeBasedEndHour;
    }

    public int getTimeBasedFlagWeight() {
        return timeBasedFlagWeight;
    }

    public boolean isSameIpCheckEnabled() {
        return sameIpCheckEnabled;
    }

    public int getSameIpFlagWeight() {
        return sameIpFlagWeight;
    }

    public boolean isKillFrequencyCheckEnabled() {
        return killFrequencyCheckEnabled;
    }

    public int getMaxKillsPerMinute() {
        return maxKillsPerMinute;
    }

    public int getKillFrequencyFlagWeight() {
        return killFrequencyFlagWeight;
    }

    public boolean isSamePlayerKillsCheckEnabled() {
        return samePlayerKillsCheckEnabled;
    }

    public int getMaxKillsPerPlayerPerMinute() {
        return maxKillsPerPlayerPerMinute;
    }

    public int getSamePlayerKillsFlagWeight() {
        return samePlayerKillsFlagWeight;
    }

    public boolean isWebhookAlertEnabled() {
        return webhookAlertEnabled;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public String getWebhookAvatarUrl() {
        return webhookAvatarUrl;
    }

    public String getWebhookThumbnailUrl() {
        return webhookThumbnailUrl;
    }

    public String getWebhookUsername() {
        return webhookUsername;
    }

    public String getWebhookTitle() {
        return webhookTitle;
    }

    public String getWebhookDescription(String player, int flag) {
        return MU.color(webhookDescription.replace("%player%", player).replace("%flag%", String.valueOf(flag)));
    }
}
