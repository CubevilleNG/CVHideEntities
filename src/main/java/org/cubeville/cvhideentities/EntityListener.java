package org.cubeville.cvhideentities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityListener {

    private CVHideEntities plugin;
    private EntityManager entityManager;
    private Logger logger;
    private ProtocolManager protocolManager;

    public EntityListener(CVHideEntities plugin, EntityManager entityManager) {
        this.plugin = plugin;
        this.entityManager = entityManager;
        this.logger = this.plugin.getLogger();
        this.protocolManager = this.entityManager.getProtocolManager();

        spawnEntityLivingListener();
    }

    private void spawnEntityLivingListener() {
        protocolManager.addPacketListener(new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY_LIVING) {
            @Override
            public void onPacketSending(PacketEvent event) {
                UUID entityUUID = (UUID) event.getPacket().getModifier().getValues().get(1);
                if(entityUUID != null) {
                    if(!entityManager.canPlayerSeeEntity(event.getPlayer().getUniqueId(), entityUUID)) {
                        event.setCancelled(true);
                        logger.log(Level.INFO, "Living Entity " + Bukkit.getEntity(entityUUID).getType() + ":" + entityUUID + " hidden from player " + event.getPlayer().getName());
                    } else {
                        logger.log(Level.INFO, "Living Entity " + Bukkit.getEntity(entityUUID).getType() + ":" + entityUUID + " shown to player " + event.getPlayer().getName());
                    }
                }
            }
        });
    }
}
