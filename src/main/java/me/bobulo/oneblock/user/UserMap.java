package me.bobulo.oneblock.user;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class UserMap {

    private final Map<UUID, User> map = new HashMap<>();

    public User createAndRegisterUser(Player player) {
        User user = new User(player);
        user.setPhase(1);
        this.map.put(player.getUniqueId(), user);
        return user;
    }

    public void removeUser(Player player) {
        this.map.remove(player.getUniqueId());
    }

    public void removeUser(UUID uuid) {
        this.map.remove(uuid);
    }

    public User getUser(Player player) {
        return getUser(player.getUniqueId());
    }

    public User getUser(UUID uuid) {
        return this.map.get(uuid);
    }


}
