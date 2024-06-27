package it.killernik.scarletantikillboost.listeners;

import it.killernik.scarletantikillboost.ScarletAntiKillBoost;
import it.killernik.scarletantikillboost.manager.ChecksManager;
import it.killernik.scarletantikillboost.manager.ConfigManager;
import it.killernik.scarletantikillboost.utils.MU;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PlayerDeathEvent implements Listener {

    private final ChecksManager checksManager;
    private final ConfigManager configManager;

    public PlayerDeathEvent() {
        this.checksManager = ScarletAntiKillBoost.get().getChecksManager();
        this.configManager = ScarletAntiKillBoost.get().getConfigManager();
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDeath(org.bukkit.event.entity.PlayerDeathEvent e) {
        Player victim = e.getEntity();
        Player killer = victim.getKiller();

        if (killer == null || killer.hasPermission("sadk.bypass")) return;

        int flags = checksManager.check(killer, victim);
        int alertInterval = configManager.getAlertInterval();
        int playerAlertInterval = checksManager.getPlayerAlertInterval(killer);
        String alertMessage = configManager.getAlertMessage();

        if (flags >= alertInterval * playerAlertInterval) {
            MU.alert(alertMessage,killer.getName(),flags);
            checksManager.setPlayerAlertInterval(killer, playerAlertInterval + 1);
        }

    }

}
