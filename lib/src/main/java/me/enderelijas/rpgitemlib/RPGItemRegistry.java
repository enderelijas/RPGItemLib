package me.enderelijas.rpgitemlib;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public class RPGItemRegistry {
    private final HashMap<UUID, RPGItem> items = new HashMap<>();
    private final NamespacedKey key;

    public RPGItemRegistry(JavaPlugin plugin) {
        this.key = new NamespacedKey(plugin, "id");
    }

    public void register(RPGItem rpgItem) {
        ItemStack item = rpgItem.getItem();
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data.has(key)) {
            UUID itemID = UUID.fromString(Objects.requireNonNull(data.get(key, PersistentDataType.STRING)));
            items.put(itemID, rpgItem);
        } else {
            UUID itemID = UUID.randomUUID();
            data.set(key, PersistentDataType.STRING, itemID.toString());
            item.setItemMeta(meta);
            items.put(itemID, rpgItem);
        }
    }

    public Optional<RPGItem> fromStack(ItemStack item) {

        if (item == null || !item.hasItemMeta()) return Optional.empty();

        String str = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (str == null) return Optional.empty();
        try {
            UUID id = UUID.fromString(str);
            return Optional.of(items.get(id));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
