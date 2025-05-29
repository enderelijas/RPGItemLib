package me.enderelijas.rpgitemlib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

interface RPGItemInterface {
    @SuppressWarnings("unused")
    void executeAbility(AbilityAction action, Event event);

    @SuppressWarnings("unused")
    String getDescription(AbilityAction action);
    ItemStack getItem();
}

public class RPGItem implements RPGItemInterface {
    public ItemStack item;
    protected Map<AbilityAction, Ability> abilities;

    public RPGItem(Material material) {
        item = new ItemStack(material);
    }

    @SuppressWarnings("unused")
    protected void setRPGMeta(ItemStack item, ItemRarity rarity, String name) {
        Map<ItemRarity, ChatColor> colorMap = Map.ofEntries(
                new AbstractMap.SimpleEntry<>(ItemRarity.COMMON, ChatColor.WHITE),
                new AbstractMap.SimpleEntry<>(ItemRarity.UNCOMMON, ChatColor.GREEN),
                new AbstractMap.SimpleEntry<>(ItemRarity.RARE, ChatColor.BLUE),
                new AbstractMap.SimpleEntry<>(ItemRarity.EPIC, ChatColor.DARK_PURPLE),
                new AbstractMap.SimpleEntry<>(ItemRarity.LEGENDARY, ChatColor.GOLD),
                new AbstractMap.SimpleEntry<>(ItemRarity.MYTHIC, ChatColor.LIGHT_PURPLE)
        );

        ChatColor rarityColor = colorMap.get(rarity);
        ItemMeta meta = item.getItemMeta();

        assert meta != null;
        meta.setDisplayName(rarityColor + name);
        List<String> lore = getLore(rarity, rarityColor);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    @NotNull
    @SuppressWarnings("ConcatenationWithEmptyString")
    private List<String> getLore(ItemRarity rarity, ChatColor rarityColor) {
        List<String> lore = new ArrayList<>();

        for (Map.Entry<AbilityAction, Ability> entry : abilities.entrySet()) {
            AbilityAction action = entry.getKey();
            Ability ability = entry.getValue();

            lore.add(ChatColor.GOLD + "Ability: " + ChatColor.YELLOW + "" + ChatColor.BOLD + action.name().replace("_", " "));
            lore.add(ChatColor.GRAY + ability.getDescription());
            lore.add("");
        }

        lore.add(rarityColor + "" + ChatColor.BOLD + rarity.name() + " ITEM");
        return lore;
    }

    @Override
    public void executeAbility(AbilityAction action, Event event) {
        Ability ability = abilities.get(action);

        if (ability != null) {
            ability.execute(event);
        }
    }

    @Override
    public String getDescription(AbilityAction action) {
        Ability ability = abilities.get(action);
        if (ability != null) {
            return ability.getDescription();
        }

        return "";
    }



    public ItemStack getItem() {
        return item;
    }
}
