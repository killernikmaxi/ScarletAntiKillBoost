package it.killernik.scarletantikillboost;

import it.killernik.scarletantikillboost.cmds.MainCMD;
import it.killernik.scarletantikillboost.listeners.PlayerDeathEvent;
import it.killernik.scarletantikillboost.manager.ChecksManager;
import it.killernik.scarletantikillboost.manager.ConfigManager;
import it.killernik.scarletantikillboost.tasks.ResetFlagsTask;
import jdk.jfr.internal.tool.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ScarletAntiKillBoost extends JavaPlugin {
    private static ScarletAntiKillBoost instance;
    private final Logger LOGGER = Bukkit.getLogger();
    private ConfigManager configManager;
    private ChecksManager checksManager;


    /*

        FIXARE SAME IP
        FIXARE INTERVALLO, SE FLAGGO 2, 2, 3, 2, 6 E NON ARRIVA
        AD UN MULTIPLO DI 5 NON FLAGGA
        METTERE I PERMS

    */

    @Override
    public void onEnable() {
        instance = this;
        initManagers();
        regListeners();
        regCommands();
        new ResetFlagsTask().scheduleTask(instance);
        LOGGER.info("[ScarletAntiKillBoost] by killernik abilitato con successo!");
    }

    @Override
    public void onDisable() {
        LOGGER.info("[ScarletAntiKillBoost] by killernik disabilitato con successo!");
    }

    private void initManagers() {
        configManager = new ConfigManager(instance);
        checksManager = new ChecksManager(instance);
    }

    private void regListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathEvent(), instance);
    }

    private void regCommands() {
        getCommand("scarletantikillboost").setExecutor(new MainCMD());
    }

    public static ScarletAntiKillBoost get() { return instance; }
    public ConfigManager getConfigManager() { return configManager; }

    public ChecksManager getChecksManager() { return checksManager; }

}
