package it.killernik.scarletantikillboost.manager;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.killernik.scarletantikillboost.ScarletAntiKillBoost;
import org.bukkit.entity.Player;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ChecksManager {

    private final ConfigManager config;

    private Cache<Player, Integer> killFrequency = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private Cache<Player, Map<Player, Integer>> samePlayerKills = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    private Map<Player, Integer> playerFlags = new HashMap<>();
    private Map<Player, Integer> playerAlertInterval = new HashMap<>();

    public ChecksManager(ScarletAntiKillBoost main) {
        config = main.getConfigManager();
    }

    public int check(Player killer, Player victim) {
        int totalFlags = 0;

        totalFlags += getPlayerFlags(killer);
        totalFlags += checkSameIP(killer, victim);
        totalFlags += checkKillFrequency(killer);
        totalFlags += checkSamePlayerKills(killer, victim);
        totalFlags += checkTimeBased();

        setPlayerFlags(killer, totalFlags);

        return totalFlags;
    }

    private int checkSameIP(Player killer, Player victim) {
        if (config.isSameIpCheckEnabled() && killer.getAddress().getAddress().getHostAddress().equals(victim.getAddress().getAddress().getHostAddress())) {
            return config.getSameIpFlagWeight();
        }
        return 0;
    }

    private int checkKillFrequency(Player attacker) {
        if (!config.isKillFrequencyCheckEnabled()) return 0;

        int maxKillsPerMinute = config.getMaxKillsPerMinute();
        Integer kills = killFrequency.getIfPresent(attacker);
        killFrequency.put(attacker, (kills == null ? 1 : kills + 1));

        if (kills != null && kills > maxKillsPerMinute) {
            return config.getKillFrequencyFlagWeight();
        }

        return 0;
    }

    private int checkSamePlayerKills(Player killer, Player victim) {
        if (!config.isSamePlayerKillsCheckEnabled()) return 0;

        int maxKillsPerPlayerPerMinute = config.getMaxKillsPerPlayerPerMinute();
        Map<Player, Integer> killsMap = samePlayerKills.getIfPresent(killer);

        if (killsMap == null) {
            killsMap = new HashMap<>();
            samePlayerKills.put(killer, killsMap);
        }

        Integer kills = killsMap.get(victim);
        killsMap.put(victim, (kills == null ? 1 : kills + 1));

        if (kills != null && kills > maxKillsPerPlayerPerMinute) {
            return config.getSamePlayerKillsFlagWeight();
        }

        return 0;
    }

    private int checkTimeBased() {
        if (!config.isTimeBasedCheckEnabled()) return 0;

        int startHour = config.getTimeBasedStartHour();
        int endHour = config.getTimeBasedEndHour();

        TimeZone timeZone = TimeZone.getTimeZone("Europe/Rome");
        Calendar cal = Calendar.getInstance(timeZone);
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        if (hour >= startHour && hour < endHour) {
            return config.getTimeBasedFlagWeight();
        }

        return 0;
    }

    public int getPlayerFlags(Player player) {
        return playerFlags.getOrDefault(player, 0);
    }
    public void setPlayerAlertInterval(Player player, int flags) {
        playerAlertInterval.put(player, flags);
    }
    public int getPlayerAlertInterval(Player player) {
        return playerAlertInterval.getOrDefault(player, 1);
    }
    public void setPlayerFlags(Player player, int flags) {
        playerFlags.put(player, flags);
    }
    public void clearAllFlags() {
        playerFlags.clear();
    }

}
