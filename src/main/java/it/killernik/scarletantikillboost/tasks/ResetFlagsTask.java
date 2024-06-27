package it.killernik.scarletantikillboost.tasks;

import it.killernik.scarletantikillboost.ScarletAntiKillBoost;
import it.killernik.scarletantikillboost.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ResetFlagsTask extends BukkitRunnable {

    private final ConfigManager config = ScarletAntiKillBoost.get().getConfigManager();

    @Override
    public void run() {
        ScarletAntiKillBoost.get().getChecksManager().clearAllFlags();
    }

    public void scheduleTask(JavaPlugin plugin) {
        new ResetFlagsTask().runTaskTimer(plugin, 0, config.getResetInterval() * 60 * 20);
    }
}
