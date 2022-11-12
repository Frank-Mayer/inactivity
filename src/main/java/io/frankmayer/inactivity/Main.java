package io.frankmayer.inactivity;

import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    public final static int stopTime = 20 * 60 * 30; // 30 minutes
    public final static int tickJump = 40; // 2 seconds
    private final static Runnable tickTask = new Runnable() {
        @Override
        public void run() {
            if (Main.isInactive) {
                Main.inactiveTicks += Main.tickJump;
                Main.server.getLogger().info("Inactive for " + Main.inactiveTicks + " ticks, shutting down in " + (Main.stopTime - Main.inactiveTicks) + " ticks");

                if (Main.inactiveTicks >= Main.stopTime) {
                    Main.server.shutdown();
                }
            }
        }
    };
    public static Server server;
    public static int inactiveTicks = 0;
    public static boolean isInactive = true;

    @Override
    public void onEnable() {
        Main.server = this.getServer();
        Main.server.getPluginManager().registerEvents(this, this);
        Main.server.getScheduler().scheduleSyncRepeatingTask(this, Main.tickTask, Main.tickJump, Main.tickJump);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.isInactive = false;
        Main.inactiveTicks = 0;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Main.isInactive = Main.server.getOnlinePlayers().size() <= 1;
        Main.inactiveTicks = 0;
    }
}
