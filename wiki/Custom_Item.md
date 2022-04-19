# Custom Item API

The custom item api allows you to add fully custom items with your own defined behavior while removing common boilerplate code and encapsulating items better. 

##Adding the NBT API

In order to make any custom items you will need to ensure the NBTAPI is working. To do this simply add the NBTAPI to the soft-depends. This will make sure that the NBTAPI loads before your plugin correctly. 

```yaml
softdepend: [ NBTAPI ]
```

## Creating a Custom Item

A custom item is created by extending the custom item class. A `CustomItem` class is simply a factory for creating new physical items which can then be handed to players.  

```java
public class MyItem extends CustomItem {

    public MyItem() {
        super("MyItem", //Item Names must be UNIQUE!
                new ItemBuilder(EMaterial.DIAMOND_SWORD).build() //The default item to create 
        );
    }
}
```

## Registering the Custom Item

A custom item must be registered. Every custom item must have a unique name otherwise registration will fail

```java
@Override
public void onPluginEnable(){
    getCustomItemManager().registerItem(new MyItem());
}
```

## Adding Copies of Custom Item to World

As stated earlier the `CustomItem` is simply a factory for creating custom items. An actual itemstack can have as many "Custom Items" as needed applied to it. 

#### Making new custom item 

This will create an entirely new itemstack and make it custom. It will be created using the default itemstack template specified in the constructor. 

```java
CustomItem item = plugin.getCustomItemManager().getItem("MyItem");
ItemStack itemStack = item.newCustomItem();
ItemStack appliedStack = item.newCustomItem(new ItemBuilder(EMaterial.RED_WOOL).build());
//This will create a new custom item using the default stack.
ItemStack itemStack = item.newCustomItem();

```

#### Making an existing item "Custom"
This will make an existing itemstack a "custom item"
```java
//this will make the existing itemstack "this" custom item.
CustomItem item = plugin.getCustomItemManager().getItem("MyItem");
ItemStack redWool = new ItemBuilder(EMaterial.RED_WOOL).build();
item.makeCustom(redWool);
//We can apply as many "CustomItem" to an item as we please. Red wool will
//will now have the effects from MyItem and MyItem2
CustomItem item2 = plugin.getCustomItemManager().getItem("MyItem2");
item2.makeCustom(redWool);
```

#### Making an item "Not Custom"

A custom item can be stripped of being "custom" using the following methods

```java
CustomItem item = plugin.getCustomItemManager().getItem("MyItem");
```

## Custom Item Behavior

So far we have created fairly useless custom items. It is now fitting to give our custom items from behavior. To do this we register `CustomItemEvent`

```java
public class MyItem extends CustomItem {

    public MyItem() {
        super("MyItem", //Item Names must be UNIQUE!
                new ItemBuilder(EMaterial.DIAMOND_SWORD).build() //The default item to create
        );
    }

    @ItemEventHandler
    public void onDamage(CustomItemEvent<EntityDeathEvent> event) {
        //this will activate whenever the item is in the entities hand
        //and the entitiy dies
        Bukkit.broadcastMessage("Someone used my item '"+event.getEntity()+"'");
    }

}
```

## ItemEventHandler

The ItemEventHandler has many useful parameters which can be used to precisely control when your custom item event is activated

### Event Condition

This determines when the custom item behavior is activated. The default condition is `MainHand` which means the code in the event will only be run when the event occurs and the item is in their main hand. In the case of `Armor` it will activate if the item is present on any armor piece.  

```java
@ItemEventHandler(condition = EventCondition.ARMOR)
public void onDamage(CustomItemEvent<EntityDeathEvent> event) {
    Bukkit.broadcastMessage("Someone used my item '"+event.getEntity()+"'");
}
```

### Wrapper
The custom ItemAPI needs a way to extract the entity out of the event so that it can check if that entity is using the custom item. In some cases, especially where there are multiple entities or items involved it is ambiguous which entity should be checked. This is where the wrapper comes in. It allows you to quickly tell the itemAPI which entity to take from the event. 

```java
//this will ensure that the code in the event activates when this entity
//damages another entity
@ItemEventHandler(wrapper = CustomEventWrapper.DAMAGE_ANOTHER_ENTITY) 
public void onDamage(CustomItemEvent<EntityDamageByEntityEvent> event) {
    Bukkit.broadcastMessage("I damaged someone else with my item '"+event.getEntity()+"'");
}
```

### Priority and IgnoreCancelled 

This works exactly the same as bukkit 

```java
@ItemEventHandler(priority = EventPriority.HIGH,ignoreCancelled = true)
public void onDamage(CustomItemEvent<EntityDeathEvent> event) {
    Bukkit.broadcastMessage("Someone used my item '"+event.getEntity()+"'");
}
```

## Before Apply and Before Remove

You can modify the behavior of what happens to an itemstack before this custom item effect is applied, and before it is removed.

### BeforeApply

The following will run before an itemstack is "made custom". 

```java
public class MyItem extends CustomItem {

    public MyItem() {
        super("MyItem", //Item Names must be UNIQUE!
                new ItemBuilder(EMaterial.DIAMOND_SWORD).build() //The default item to create
        );

    }

    @Override
    protected boolean beforeApply(ItemStack itemStack) {
        ItemBuilder item = new ItemBuilder(itemStack);
        //if it is a diamond we will not make it custom
        if(item.getType().equals(EMaterial.DIAMOND)) {
            //returning true will cancel the apply making sure the item
            //doesn't actually become custom
            return true;
        }
        //otherwise we will add a lore line to the item.  
        item.addLoreLine("Epic Custom Item");
        return false;
    }
}
```

### Before Remove

Similarly we can intervene but instead with the `BeforeRemove` method.

```java
public class MyItem extends CustomItem {

    public MyItem() {
        super("MyItem", //Item Names must be UNIQUE!
                new ItemBuilder(EMaterial.DIAMOND_SWORD).build() //The default item to create
        );

    }

    @Override
    protected boolean beforeRemove(ItemStack itemStack) {
        ItemBuilder item = new ItemBuilder(itemStack);
        //if it is a diamond chestplate we will stop the removal
        if(item.getType().equals(EMaterial.DIAMOND_CHESTPLATE)) {
            //returning true will cancel the removal making sure the item
            //remains custom
            return true;
        }
        //otherwise we will remove the lore line we added earlier  
        item.setLore(itemStack.getItemMeta().getLore().remove(itemStack.getItemMeta().getLore().size()-1));
        return false;
    }
}
```

## Advanced 

Sometimes there is not always a wrapper for an event or the correct event condition. This will be especially true if the event is some kind of custom event not included in bukkit. This can be rectified by creating a custom event wrapper.

To do this you must register new `CustomItemEventListeners` in the onRegister method. This may NOT be done in the constructor

```java
public class MyItem extends CustomItem {

    private final BukkitPlugin plugin;
    
    public MyItem(BukkitPlugin plugin) {
        super("MyItem", //Item Names must be UNIQUE!
                new ItemBuilder(EMaterial.DIAMOND_SWORD).build() //The default item to create
        );
        this.plugin = plugin;
    }

    @Override
    public void onRegister() {
        CustomItemEventListener.register(plugin,
                EntityDamageByEntityEvent.class,
                this,
                EventPredicates.MAIN_HAND(), //You can also create custom event conditions
                new CustomEntityDamageByEntityEventWrapper(),
                this::onDamage,
                false,
                EventPriority.NORMAL,
                true);
    }
    
    public void onDamage(CustomItemEvent<EntityDamageByEntityEvent> event) {
        System.out.println("Damaged!");
    }
    
    //this could be an inner class, an anonymous class or just a regular class
    private static class CustomEntityDamageByEntityEventWrapper implements CIEventWrapper<EntityDamageByEntityEvent> {

        @Nullable @Override
        public LivingEntity getEntity(EntityDamageByEntityEvent event) {
            //pick a random entity 
            if(Math.random()>0.5)
                return asLiving(event.getEntity());
            
            return asLiving(event.getDamager());
        }
        
        private LivingEntity asLiving(Entity entity) {
            if(entity instanceof LivingEntity)
                return (LivingEntity) entity;
            
            //if it is not a living entity we cannot check their inventory so return null.
            //if we return null then it means this is not a custom item event.
            return null;
        }
    }
}
```