package org.cubeville.cvhideentities;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CVHideEntities extends JavaPlugin {

    private Logger logger;

    private EntityManager entityManager;
    private EntityListener entityListener;

    public void onEnable() {
        this.logger = this.getLogger();

        this.entityManager = new EntityManager(this);
        this.entityListener = new EntityListener(this, this.entityManager);

        logger.info(ChatColor.LIGHT_PURPLE + "Plugin Enabled Successfully");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        if(command.getName().equalsIgnoreCase("cvhideentities")) {
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("hideall")) {
                    int i = 0;
                    for(LivingEntity e : Bukkit.getPlayer(sender.getName()).getWorld().getLivingEntities()) {
                        try {
                            entityManager.hideEntityFromAll(e.getUniqueId());
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        }
                        i++;
                    }
                    logger.log(Level.INFO, i + " entities hidden from all players");
                } else if(args[0].equalsIgnoreCase("showall")) {
                    int i = 0;
                    for(LivingEntity e : Bukkit.getPlayer(sender.getName()).getWorld().getLivingEntities()) {
                        entityManager.showEntityToAll(e.getUniqueId());
                        i++;
                    }
                    logger.log(Level.INFO, i + " entities shown to all players");
                } else if(args[0].equalsIgnoreCase("hideforme")) {
                    int i = 0;
                    for(LivingEntity e : Bukkit.getPlayer(sender.getName()).getWorld().getLivingEntities()) {
                        entityManager.hideEntityFromPlayer(((Player) sender).getUniqueId(), e.getUniqueId());
                        i++;
                    }
                    logger.log(Level.INFO, i + " entities hidden from player " + sender.getName());
                } else if(args[0].equalsIgnoreCase("showforme")) {
                    int i = 0;
                    for(LivingEntity e : Bukkit.getPlayer(sender.getName()).getWorld().getLivingEntities()) {
                        entityManager.showEntityToPlayer(((Player) sender).getUniqueId(), e.getUniqueId());
                        i++;
                    }
                    logger.log(Level.INFO, i + " entities shown to player " + sender.getName());
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
