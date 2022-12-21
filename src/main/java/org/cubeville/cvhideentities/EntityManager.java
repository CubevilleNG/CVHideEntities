package org.cubeville.cvhideentities;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class EntityManager {

    private CVHideEntities plugin;
    private Logger logger;
    private ProtocolManager protocolManager;

    private HashMap<UUID, Set<UUID>> hiddenEntities;

    public EntityManager(CVHideEntities plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
        this.protocolManager = ProtocolLibrary.getProtocolManager();

        this.hiddenEntities = new HashMap<>();
    }

    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    public boolean isEntityHidden(UUID entityUUID) {
        return this.hiddenEntities.containsKey(entityUUID);
    }

    public void hideEntityFromAll(UUID entityUUID) throws InvocationTargetException {
        if(!isEntityHidden(entityUUID)) {
            this.hiddenEntities.put(entityUUID, new HashSet<>());
            destroyEntityForAll(entityUUID);
        }
    }

    public void showEntityToAll(UUID entityUUID) {
        this.hiddenEntities.remove(entityUUID);
    }

    public boolean canPlayerSeeEntity(UUID playerUUID, UUID entityUUID) {
        return !this.isEntityHidden(entityUUID) || this.hiddenEntities.get(entityUUID).contains(playerUUID);
    }

    public void hideEntityFromPlayer(UUID playerUUID, UUID entityUUID) {
        if(isEntityHidden(entityUUID)) {
            this.hiddenEntities.get(entityUUID).remove(playerUUID);
        }
    }

    public void showEntityToPlayer(UUID playerUUID, UUID entityUUID) {
        if(isEntityHidden(entityUUID)) {
            this.hiddenEntities.get(entityUUID).add(playerUUID);
        }
    }

    public void destroyEntityForAll(UUID entityUUID) throws InvocationTargetException {
        PacketContainer destroyEntity = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        //destroyEntity.getIntegers().write(0, 1);
        int[] uuid = {Integer.parseInt(String.valueOf(entityUUID))};
        destroyEntity.getIntegerArrays().write(0, uuid);
        for(Player p : Bukkit.getOnlinePlayers()) {
            protocolManager.sendServerPacket(p, destroyEntity);
        }
    }

    public void destroyEntityForPlayer(UUID playerUUID, UUID entityUUID) {

    }

    public void createEntityForAll(UUID entityUUID) {

    }

    public void createEntityForPlayer(UUID playerUUID, UUID entityUUID) {

    }
}
