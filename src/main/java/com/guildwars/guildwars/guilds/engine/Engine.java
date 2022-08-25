package com.guildwars.guildwars.guilds.engine;

import com.guildwars.guildwars.GuildWars;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public abstract class Engine implements Listener, Runnable {

    // For runnable & listener
    public Engine(long period) {
        this.setPeriod(period);
        this.setDelay(this.getPeriod());
        this.setSync(true);
        this.setActive(true);
    }

    // For runnable only
    public Engine() {
        this.setActive(false);
    }

    // -------------------------------------------- //
    // Plugin
    // -------------------------------------------- //

    public GuildWars getPlugin() {
        return GuildWars.getInstance();
    }

    // -------------------------------------------- //
    // Period
    // -------------------------------------------- //

    private Long period;
    public Long getPeriod() {
        return this.period;
    }
    public void setPeriod(Long period) {
        this.period = period;
    }

    // -------------------------------------------- //
    // Delay
    // -------------------------------------------- //

    private Long delay;
    public Long getDelay() {
        return this.delay;
    }
    public void setDelay(Long delay) {
        this.delay = delay;
    }

    // -------------------------------------------- //
    // Sync
    // -------------------------------------------- //

    private boolean sync;
    public boolean isSync() {
        return this.sync;
    }
    public void setSync(boolean sync) {
        this.sync = sync;
    }

    // -------------------------------------------- //
    // Sync
    // -------------------------------------------- //

    private BukkitTask task;
    public BukkitTask getTask() { return this.task; }

    public void setActive(boolean activateTask) {
        this.setActiveListener();
        if (activateTask) {
            this.setActiveTask();
        }
    }

    public void setActiveListener() {
        Bukkit.getPluginManager().registerEvents(this, GuildWars.getInstance());
    }

    public void setActiveTask() {
        if (this.period != null) {
            if (this.isSync())
            {
                this.task = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), this, this.getDelay(), this.getPeriod());
            }
            else
            {
                this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(this.getPlugin(), this, this.getDelay(), this.getPeriod());
            }
        }
    }

    @Override
    public void run() {

    }
}
