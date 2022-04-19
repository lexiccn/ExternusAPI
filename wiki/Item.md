# Item API 

The item api makes creating cross-version materials far easier. 

## Item Builder

The ItemBuilder allows for quick and easy manipulation and creation of items. An itembuilder is created as follows

```java
//create a sword
ItemStack mySword = new ItemBuilder(Material.DIAMOND_SWORD)
        .addEnchantment(Enchantment.DAMAGE_ALL,5) //add sharp V
        .addFlags(ItemFlag.HIDE_ENCHANTS) //hide enchants
        .addLoreLine("") //add a line of lore
        .addLoreLine(ChatColor.WHITE+"BEGONE") //add some more lore
        .setDisplayName(ChatColor.GOLD+"Gamer Sword") //add a display name
        .build();
```

### EMaterial

The EMaterial enum is a cross-version material enum. It represents a material from any version of the minecraft. The main power of the ItemBuilder is that it is compatible with the EMaterial enum. EMaterial contains all of the standard methods like matchMaterial. 

```java
ItemStack wool = new ItemBuilder(EMaterial.RED_WOOL).build(); //create a red wool item
```

## Potions

#### Simple Potions

The PotionBuilder allows for cross-version creation of potions. To create a standard potion (one you would find in the creative menu), simply make it as shown

```java
ItemStack splashInstantHealth2 = new ItemBuilder(PotionType.INSTANT_HEAL, 
        PotionBuilder.Type.SPLASH,true,false).build();
```

#### Custom Potions

If you want some more customisation with your potions you can use the Potion Builder. 

```java
ItemStack epicPotion = new ItemBuilder(EMaterial.POTION)
        .potion(potionBuilder -> { //make a PotionBuilder
            potionBuilder.addEffect(new PotionEffect(PotionEffectType.JUMP,1000,4)) //add jump 5
        .addEffect(new PotionEffect(PotionEffectType.POISON,50,2)) //add poison 3
        .setType(PotionBuilder.Type.LINGERING) //make it lingering
        .setColor(Color.BLUE); //make it blue
}).build(); //build into itemstack 
```

## Skulls

### Simple Skulls
Most simple skulls are covered by the default EMaterials. 
```java
ItemStack epicSkull = new ItemBuilder(EMaterial.CREEPER_HEAD).build();
```

### Texture Skulls
Skulls can be created from a texture or url with ease. This operation is potentially blocking it might be worth doing it asynchronously.

```java
ItemStack earth = new ItemBuilder(EMaterial.PLAYER_HEAD)
        .skull(skullBuilder -> {
            //sets the texture (horrible string from head DB)
        skullBuilder.setTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjliMjg4OTAyMDU4MzU2NWY4OGQ1MDUzNzg3MGM1OWFhZDgwMjU5NGZhYmQ4MzdlMWQxNGY1YTA2YWUzNDUwOSJ9fX0=")
}).build();
```

### Player Skulls

```java
ItemStack skull = new ItemBuilder(EMaterial.PLAYER_HEAD)
        .skull(skullBuilder -> {
            skullBuilder.setPlayer(UUID.randomUUID());
        }).build();
```

## NBT Support 

To use the Item API properly you will need the NBTAPI to be enabled. To do this you should add NBTAPI as a soft-depend. 

```yaml
softdepend: [NBTAPI]
```

You can now easily add tags with the ItemBuilder API. One cannot add NBT tags to any material. To check whether you can add NBT tags to the EMaterial use `EMaterial#noNBT();`

```java
ItemStack NBTEpic = new ItemBuilder(EMaterial.ITEM_FRAME)
        .addTag("Gamer","Gamer")
        .build();
```

If you want to use more advanced NBT you will need to add the NBTAPI as a dependency https://github.com/tr7zw/Item-NBT-API
