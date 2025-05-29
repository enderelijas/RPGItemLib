# RPGItemLib
A simple library for creating hypixel-styled RPG items in minecraft using SpigotMC.

## ⚠ Important ⚠
Note: the library is in the **early stages of its development**.
> Documentation will be added at a later date.

## QuickStart
### 1. Setup your Event class
```java
public class Events implements Listener {
    private RPGItemRegistry registry;
    public Events(RPGItemRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        registry.fromStack(item).ifPresent(stack -> stack.executeAbility(AbilityAction.BLOCK_BREAK, event));
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        registry.fromStack(item).ifPresent(stack -> stack.executeAbility(AbilityAction.RIGHT_CLICK, event));
    }

    // More event handlers to be added...
```
### 2. Create `RPGItemRegistry` and register Event class
```java
@Override
public void onEnable() {
    // Plugin startup logic
    RPGItemRegistry registry = new RPGItemRegistry();

    Bukkit.getPluginManager().registerEvents(new Events(registry), this);
}
```

### 3. Create custom item by extending `RPGItem` class
```java
public class ArtisanalShortbow extends RPGItem {
    public ArtisanalShortbow() {
        super(Material.BOW);

        abilities = Map.of(AbilityAction.RIGHT_CLICK, new Ability() {
            @Override
            public void execute(Event event) {
                onRightClick((PlayerInteractEvent) event);
            }

            @Override
            public String getDescription() {
                return "Instantly fires arrows!";
            }
        });

        setRPGMeta(item, ItemRarity.RARE, "Artisanal Shortbow");
    }

    public void onRightClick(PlayerInteractEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        Vector direction = player.getEyeLocation().getDirection();
        Entity arrow = player.getWorld().spawnEntity(player.getEyeLocation(), EntityType.ARROW);

        float yaw = (float) Math.toDegrees(Math.atan2(-direction.getX(), direction.getZ()));
        float pitch = (float) Math.toDegrees(-Math.asin(direction.getY()));

        arrow.setRotation(yaw, pitch);
        arrow.setVelocity(direction.multiply(1.2));
    }
}
```

### 4. Register the custom item
```java
@Override
public void onEnable() {
    // Plugin startup logic
    RPGItemRegistry registry = new RPGItemRegistry();
    ArtisanalShortbow shortbow = new ArtisanalShortbow();

    registry.register(shortbow);

    // <...>
}
```

### 5. Give item to player
```java
player.getInventory().addItem(shortbow.getItem());
```
---
> More examples to be added.
