package me.deltaorion.extapi.item;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 *  Enum that represents all of the 1.8 materials.
 */

public enum EMaterial {

    AIR(0, org.bukkit.Material.AIR, 0),
    STONE(1, org.bukkit.Material.STONE, 0),
    GRANITE(1, org.bukkit.Material.STONE, 1),
    POLISHED_GRANITE(1, org.bukkit.Material.STONE, 2),
    DIORITE(1, org.bukkit.Material.STONE, 3),
    POLISHED_DIORITE(1, org.bukkit.Material.STONE, 4),
    ANDESITE(1, org.bukkit.Material.STONE, 5),
    POLISHED_ANDESITE(1, org.bukkit.Material.STONE, 6),
    GRASS_BLOCK(2, org.bukkit.Material.GRASS, 0),
    DIRT(3, org.bukkit.Material.DIRT, 0),
    COARSE_DIRT(3, org.bukkit.Material.DIRT, 1),
    PODZOL(3, org.bukkit.Material.DIRT, 2),
    COBBLESTONE(4, org.bukkit.Material.COBBLESTONE, 0),
    OAK_WOOD_PLANK(5, org.bukkit.Material.WOOD, 0),
    SPRUCE_WOOD_PLANK(5, org.bukkit.Material.WOOD, 1),
    BIRCH_WOOD_PLANK(5, org.bukkit.Material.WOOD, 2),
    JUNGLE_WOOD_PLANK(5, org.bukkit.Material.WOOD, 3),
    ACACIA_WOOD_PLANK(5, org.bukkit.Material.WOOD, 4),
    DARK_OAK_WOOD_PLANK(5, org.bukkit.Material.WOOD, 5),
    OAK_SAPLING(6, org.bukkit.Material.SAPLING, 0),
    SPRUCE_SAPLING(6, org.bukkit.Material.SAPLING, 1),
    BIRCH_SAPLING(6, org.bukkit.Material.SAPLING, 2),
    JUNGLE_SAPLING(6, org.bukkit.Material.SAPLING, 3),
    ACACIA_SAPLING(6, org.bukkit.Material.SAPLING, 4),
    DARK_OAK_SAPLING(6, org.bukkit.Material.SAPLING, 5),
    BEDROCK(7, org.bukkit.Material.BEDROCK, 0),
    FLOWING_WATER(8, org.bukkit.Material.WATER, 0),
    STILL_WATER(9, org.bukkit.Material.STATIONARY_WATER, 0),
    FLOWING_LAVA(10, org.bukkit.Material.LAVA, 0),
    STILL_LAVA(11, org.bukkit.Material.STATIONARY_LAVA, 0),
    SAND(12, org.bukkit.Material.SAND, 0),
    RED_SAND(12, org.bukkit.Material.SAND, 1),
    GRAVEL(13, org.bukkit.Material.GRAVEL, 0),
    GOLD_ORE(14, org.bukkit.Material.GOLD_ORE, 0),
    IRON_ORE(15, org.bukkit.Material.IRON_ORE, 0),
    COAL_ORE(16, org.bukkit.Material.COAL_ORE, 0),
    OAK_WOOD(17, org.bukkit.Material.LOG, 0),
    SPRUCE_WOOD(17, org.bukkit.Material.LOG, 1),
    BIRCH_WOOD(17, org.bukkit.Material.LOG, 2),
    JUNGLE_WOOD(17, org.bukkit.Material.LOG, 3),
    OAK_LEAVES(18, org.bukkit.Material.LEAVES, 0),
    SPRUCE_LEAVES(18, org.bukkit.Material.LEAVES, 1),
    BIRCH_LEAVES(18, org.bukkit.Material.LEAVES, 2),
    JUNGLE_LEAVES(18, org.bukkit.Material.LEAVES, 3),
    SPONGE(19, org.bukkit.Material.SPONGE, 0),
    WET_SPONGE(19, org.bukkit.Material.SPONGE, 1),
    GLASS(20, org.bukkit.Material.GLASS, 0),
    LAPIS_LAZULI_ORE(21, org.bukkit.Material.LAPIS_ORE, 0),
    LAPIS_LAZULI_BLOCK(22, org.bukkit.Material.LAPIS_BLOCK, 0),
    DISPENSER(23, org.bukkit.Material.DISPENSER, 0),
    SANDSTONE(24, org.bukkit.Material.SANDSTONE, 0),
    CHISELED_SANDSTONE(24, org.bukkit.Material.SANDSTONE, 1),
    SMOOTH_SANDSTONE(24, org.bukkit.Material.SANDSTONE, 2),
    NOTE_BLOCK(25, org.bukkit.Material.NOTE_BLOCK, 0),
    BED(26, org.bukkit.Material.BED_BLOCK, 0),
    POWERED_RAIL(27, org.bukkit.Material.POWERED_RAIL, 0),
    DETECTOR_RAIL(28, org.bukkit.Material.DETECTOR_RAIL, 0),
    STICKY_PISTON(29, org.bukkit.Material.PISTON_STICKY_BASE, 0),
    COBWEB(30, org.bukkit.Material.WEB, 0),
    DEAD_SHRUB(31, org.bukkit.Material.LONG_GRASS, 0),
    GRASS(31, org.bukkit.Material.LONG_GRASS, 1),
    FERN(31, org.bukkit.Material.LONG_GRASS, 2),
    DEAD_BUSH(32, org.bukkit.Material.DEAD_BUSH, 0),
    PISTON(33, org.bukkit.Material.PISTON_BASE, 0),
    PISTON_HEAD(34, org.bukkit.Material.PISTON_EXTENSION, 0),
    WHITE_WOOL(35, org.bukkit.Material.WOOL, 0),
    ORANGE_WOOL(35, org.bukkit.Material.WOOL, 1),
    MAGENTA_WOOL(35, org.bukkit.Material.WOOL, 2),
    LIGHT_BLUE_WOOL(35, org.bukkit.Material.WOOL, 3),
    YELLOW_WOOL(35, org.bukkit.Material.WOOL, 4),
    LIME_WOOL(35, org.bukkit.Material.WOOL, 5),
    PINK_WOOL(35, org.bukkit.Material.WOOL, 6),
    GRAY_WOOL(35, org.bukkit.Material.WOOL, 7),
    LIGHT_GRAY_WOOL(35, org.bukkit.Material.WOOL, 8),
    CYAN_WOOL(35, org.bukkit.Material.WOOL, 9),
    PURPLE_WOOL(35, org.bukkit.Material.WOOL, 10),
    BLUE_WOOL(35, org.bukkit.Material.WOOL, 11),
    BROWN_WOOL(35, org.bukkit.Material.WOOL, 12),
    GREEN_WOOL(35, org.bukkit.Material.WOOL, 13),
    RED_WOOL(35, org.bukkit.Material.WOOL, 14),
    BLACK_WOOL(35, org.bukkit.Material.WOOL, 15),
    DANDELION(37, org.bukkit.Material.YELLOW_FLOWER, 0),
    POPPY(38, org.bukkit.Material.RED_ROSE, 0),
    BLUE_ORCHID(38, org.bukkit.Material.RED_ROSE, 1),
    ALLIUM(38, org.bukkit.Material.RED_ROSE, 2),
    AZURE_BLUET(38, org.bukkit.Material.RED_ROSE, 3),
    RED_TULIP(38, org.bukkit.Material.RED_ROSE, 4),
    ORANGE_TULIP(38, org.bukkit.Material.RED_ROSE, 5),
    WHITE_TULIP(38, org.bukkit.Material.RED_ROSE, 6),
    PINK_TULIP(38, org.bukkit.Material.RED_ROSE, 7),
    OXEYE_DAISY(38, org.bukkit.Material.RED_ROSE, 8),
    BROWN_MUSHROOM(39, org.bukkit.Material.BROWN_MUSHROOM, 0),
    RED_MUSHROOM(40, org.bukkit.Material.RED_MUSHROOM, 0),
    GOLD_BLOCK(41, org.bukkit.Material.GOLD_BLOCK, 0),
    IRON_BLOCK(42, org.bukkit.Material.IRON_BLOCK, 0),
    DOUBLE_STONE_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 0),
    DOUBLE_SANDSTONE_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 1),
    DOUBLE_WOODEN_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 2),
    DOUBLE_COBBLESTONE_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 3),
    DOUBLE_BRICK_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 4),
    DOUBLE_STONE_BRICK_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 5),
    DOUBLE_NETHER_BRICK_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 6),
    DOUBLE_QUARTZ_SLAB(43, org.bukkit.Material.DOUBLE_STEP, 7),
    STONE_SLAB(44, org.bukkit.Material.STEP, 0),
    SANDSTONE_SLAB(44, org.bukkit.Material.STEP, 1),
    WOODEN_SLAB(44, org.bukkit.Material.STEP, 2),
    COBBLESTONE_SLAB(44, org.bukkit.Material.STEP, 3),
    BRICK_SLAB(44, org.bukkit.Material.STEP, 4),
    STONE_BRICK_SLAB(44, org.bukkit.Material.STEP, 5),
    NETHER_BRICK_SLAB(44, org.bukkit.Material.STEP, 6),
    QUARTZ_SLAB(44, org.bukkit.Material.STEP, 7),
    BRICKS(45, org.bukkit.Material.BRICK, 0),
    TNT(46, org.bukkit.Material.TNT, 0),
    BOOKSHELF(47, org.bukkit.Material.BOOKSHELF, 0),
    MOSS_STONE(48, org.bukkit.Material.MOSSY_COBBLESTONE, 0),
    OBSIDIAN(49, org.bukkit.Material.OBSIDIAN, 0),
    TORCH(50, org.bukkit.Material.TORCH, 0),
    FIRE(51, org.bukkit.Material.FIRE, 0),
    MONSTER_SPAWNER(52, org.bukkit.Material.MOB_SPAWNER, 0),
    OAK_WOOD_STAIRS(53, org.bukkit.Material.WOOD_STAIRS, 0),
    CHEST(54, org.bukkit.Material.CHEST, 0),
    REDSTONE_WIRE(55, org.bukkit.Material.REDSTONE_WIRE, 0),
    DIAMOND_ORE(56, org.bukkit.Material.DIAMOND_ORE, 0),
    DIAMOND_BLOCK(57, org.bukkit.Material.DIAMOND_BLOCK, 0),
    CRAFTING_TABLE(58, org.bukkit.Material.WORKBENCH, 0),
    WHEAT_CROPS(59, org.bukkit.Material.CROPS, 0),
    FARMLAND(60, org.bukkit.Material.SOIL, 0),
    FURNACE(61, org.bukkit.Material.FURNACE, 0),
    BURNING_FURNACE(62, org.bukkit.Material.BURNING_FURNACE, 0),
    STANDING_SIGN_BLOCK(63, org.bukkit.Material.SIGN_POST, 0),
    OAK_DOOR_BLOCK(64, org.bukkit.Material.WOODEN_DOOR, 0),
    LADDER(65, org.bukkit.Material.LADDER, 0),
    RAIL(66, org.bukkit.Material.RAILS, 0),
    COBBLESTONE_STAIRS(67, org.bukkit.Material.COBBLESTONE_STAIRS, 0),
    WALL_MOUNTED_SIGN_BLOCK(68, org.bukkit.Material.WALL_SIGN, 0),
    LEVER(69, org.bukkit.Material.LEVER, 0),
    STONE_PRESSURE_PLATE(70, org.bukkit.Material.STONE_PLATE, 0),
    IRON_DOOR_BLOCK(71, org.bukkit.Material.IRON_DOOR_BLOCK, 0),
    WOODEN_PRESSURE_PLATE(72, org.bukkit.Material.WOOD_PLATE, 0),
    REDSTONE_ORE(73, org.bukkit.Material.REDSTONE_ORE, 0),
    GLOWING_REDSTONE_ORE(74, org.bukkit.Material.GLOWING_REDSTONE_ORE, 0),
    REDSTONE_TORCH__OFF(75, org.bukkit.Material.REDSTONE_TORCH_OFF, 0),
    REDSTONE_TORCH__ON(76, org.bukkit.Material.REDSTONE_TORCH_ON, 0),
    STONE_BUTTON(77, org.bukkit.Material.STONE_BUTTON, 0),
    SNOW(78, org.bukkit.Material.SNOW, 0),
    ICE(79, org.bukkit.Material.ICE, 0),
    SNOW_BLOCK(80, org.bukkit.Material.SNOW_BLOCK, 0),
    CACTUS(81, org.bukkit.Material.CACTUS, 0),
    CLAY(82, org.bukkit.Material.CLAY, 0),
    SUGAR_CANES(83, org.bukkit.Material.SUGAR_CANE_BLOCK, 0),
    JUKEBOX(84, org.bukkit.Material.JUKEBOX, 0),
    OAK_FENCE(85, org.bukkit.Material.FENCE, 0),
    PUMPKIN(86, org.bukkit.Material.PUMPKIN, 0),
    NETHERRACK(87, org.bukkit.Material.NETHERRACK, 0),
    SOUL_SAND(88, org.bukkit.Material.SOUL_SAND, 0),
    GLOWSTONE(89, org.bukkit.Material.GLOWSTONE, 0),
    NETHER_PORTAL(90, org.bukkit.Material.PORTAL, 0),
    JACK_O_LANTERN(91, org.bukkit.Material.JACK_O_LANTERN, 0),
    CAKE_BLOCK(92, org.bukkit.Material.CAKE_BLOCK, 0),
    REDSTONE_REPEATER_BLOCK_OFF(93, org.bukkit.Material.DIODE_BLOCK_OFF, 0),
    REDSTONE_REPEATER_BLOCK_ON(94, org.bukkit.Material.DIODE_BLOCK_ON, 0),
    WHITE_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 0),
    ORANGE_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 1),
    MAGENTA_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 2),
    LIGHT_BLUE_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 3),
    YELLOW_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 4),
    LIME_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 5),
    PINK_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 6),
    GRAY_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 7),
    LIGHT_GRAY_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 8),
    CYAN_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 9),
    PURPLE_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 10),
    BLUE_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 11),
    BROWN_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 12),
    GREEN_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 13),
    RED_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 14),
    BLACK_STAINED_GLASS(95, org.bukkit.Material.STAINED_GLASS, 15),
    WOODEN_TRAPDOOR(96, org.bukkit.Material.TRAP_DOOR, 0),
    STONE_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 0),
    COBBLESTONE_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 1),
    STONE_BRICK_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 2),
    MOSSY_STONE_BRICK_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 3),
    CRACKED_STONE_BRICK_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 4),
    CHISELED_STONE_BRICK_MONSTER_EGG(97, org.bukkit.Material.MONSTER_EGGS, 5),
    STONE_BRICKS(98, org.bukkit.Material.SMOOTH_BRICK, 0),
    MOSSY_STONE_BRICKS(98, org.bukkit.Material.SMOOTH_BRICK, 1),
    CRACKED_STONE_BRICKS(98, org.bukkit.Material.SMOOTH_BRICK, 2),
    CHISELED_STONE_BRICKS(98, org.bukkit.Material.SMOOTH_BRICK, 3),
    BROWN_MUSHROOM_BLOCK(99, org.bukkit.Material.HUGE_MUSHROOM_1, 0),
    RED_MUSHROOM_BLOCK(100, org.bukkit.Material.HUGE_MUSHROOM_2, 0),
    IRON_BARS(101, org.bukkit.Material.IRON_FENCE, 0),
    GLASS_PANE(102, org.bukkit.Material.THIN_GLASS, 0),
    MELON_BLOCK(103, org.bukkit.Material.MELON_BLOCK, 0),
    PUMPKIN_STEM(104, org.bukkit.Material.PUMPKIN_STEM, 0),
    MELON_STEM(105, org.bukkit.Material.MELON_STEM, 0),
    VINES(106, org.bukkit.Material.VINE, 0),
    OAK_FENCE_GATE(107, org.bukkit.Material.FENCE_GATE, 0),
    BRICK_STAIRS(108, org.bukkit.Material.BRICK_STAIRS, 0),
    STONE_BRICK_STAIRS(109, org.bukkit.Material.SMOOTH_STAIRS, 0),
    MYCELIUM(110, org.bukkit.Material.MYCEL, 0),
    LILY_PAD(111, org.bukkit.Material.WATER_LILY, 0),
    NETHER_BRICK(112, org.bukkit.Material.NETHER_BRICK, 0),
    NETHER_BRICK_FENCE(113, org.bukkit.Material.NETHER_FENCE, 0),
    NETHER_BRICK_STAIRS(114, org.bukkit.Material.NETHER_BRICK_STAIRS, 0),
    NETHER_WART(115, org.bukkit.Material.NETHER_WARTS, 0),
    ENCHANTMENT_TABLE(116, org.bukkit.Material.ENCHANTMENT_TABLE, 0),
    BREWING_STAND(117, org.bukkit.Material.BREWING_STAND, 0),
    CAULDRON(118, org.bukkit.Material.CAULDRON, 0),
    END_PORTAL(119, org.bukkit.Material.ENDER_PORTAL, 0),
    END_PORTAL_FRAME(120, org.bukkit.Material.ENDER_PORTAL_FRAME, 0),
    END_STONE(121, org.bukkit.Material.ENDER_STONE, 0),
    DRAGON_EGG(122, org.bukkit.Material.DRAGON_EGG, 0),
    REDSTONE_LAMP_INACTIVE(123, org.bukkit.Material.REDSTONE_LAMP_OFF, 0),
    REDSTONE_LAMP_ACTIVE(124, org.bukkit.Material.REDSTONE_LAMP_ON, 0),
    DOUBLE_OAK_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 0),
    DOUBLE_SPRUCE_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 1),
    DOUBLE_BIRCH_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 2),
    DOUBLE_JUNGLE_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 3),
    DOUBLE_ACACIA_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 4),
    DOUBLE_DARK_OAK_WOOD_SLAB(125, org.bukkit.Material.WOOD_DOUBLE_STEP, 5),
    OAK_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 0),
    SPRUCE_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 1),
    BIRCH_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 2),
    JUNGLE_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 3),
    ACACIA_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 4),
    DARK_OAK_WOOD_SLAB(126, org.bukkit.Material.WOOD_STEP, 5),
    COCOA(127, org.bukkit.Material.COCOA, 0),
    SANDSTONE_STAIRS(128, org.bukkit.Material.SANDSTONE_STAIRS, 0),
    EMERALD_ORE(129, org.bukkit.Material.EMERALD_ORE, 0),
    ENDER_CHEST(130, org.bukkit.Material.ENDER_CHEST, 0),
    TRIPWIRE_HOOK(131, org.bukkit.Material.TRIPWIRE_HOOK, 0),
    TRIPWIRE(132, org.bukkit.Material.TRIPWIRE, 0),
    EMERALD_BLOCK(133, org.bukkit.Material.EMERALD_BLOCK, 0),
    SPRUCE_WOOD_STAIRS(134, org.bukkit.Material.SPRUCE_WOOD_STAIRS, 0),
    BIRCH_WOOD_STAIRS(135, org.bukkit.Material.BIRCH_WOOD_STAIRS, 0),
    JUNGLE_WOOD_STAIRS(136, org.bukkit.Material.JUNGLE_WOOD_STAIRS, 0),
    COMMAND_BLOCK(137, org.bukkit.Material.COMMAND, 0),
    BEACON(138, org.bukkit.Material.BEACON, 0),
    COBBLESTONE_WALL(139, org.bukkit.Material.COBBLE_WALL, 0),
    MOSSY_COBBLESTONE_WALL(139, org.bukkit.Material.COBBLE_WALL, 1),
    FLOWER_POT(140, org.bukkit.Material.FLOWER_POT, 0),
    CARROTS(141, org.bukkit.Material.CARROT, 0),
    POTATOES(142, org.bukkit.Material.POTATO, 0),
    WOODEN_BUTTON(143, org.bukkit.Material.WOOD_BUTTON, 0),
    MOB_HEAD(144, Material.SKULL_ITEM, 0),
    ANVIL(145, org.bukkit.Material.ANVIL, 0),
    TRAPPED_CHEST(146, org.bukkit.Material.TRAPPED_CHEST, 0),
    WEIGHTED_PRESSURE_PLATE_LIGHT(147, org.bukkit.Material.GOLD_PLATE, 0),
    WEIGHTED_PRESSURE_PLATE_HEAVY(148, org.bukkit.Material.IRON_PLATE, 0),
    REDSTONE_COMPARATOR_INACTIVE(149, org.bukkit.Material.REDSTONE_COMPARATOR_OFF, 0),
    REDSTONE_COMPARATOR_ACTIVE(150, org.bukkit.Material.REDSTONE_COMPARATOR_ON, 0),
    DAYLIGHT_SENSOR(151, org.bukkit.Material.DAYLIGHT_DETECTOR, 0),
    REDSTONE_BLOCK(152, org.bukkit.Material.REDSTONE_BLOCK, 0),
    NETHER_QUARTZ_ORE(153, org.bukkit.Material.QUARTZ_ORE, 0),
    HOPPER(154, org.bukkit.Material.HOPPER, 0),
    QUARTZ_BLOCK(155, org.bukkit.Material.QUARTZ_BLOCK, 0),
    CHISELED_QUARTZ_BLOCK(155, org.bukkit.Material.QUARTZ_BLOCK, 1),
    PILLAR_QUARTZ_BLOCK(155, org.bukkit.Material.QUARTZ_BLOCK, 2),
    QUARTZ_STAIRS(156, org.bukkit.Material.QUARTZ_STAIRS, 0),
    ACTIVATOR_RAIL(157, org.bukkit.Material.ACTIVATOR_RAIL, 0),
    DROPPER(158, org.bukkit.Material.DROPPER, 0),
    WHITE_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 0),
    ORANGE_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 1),
    MAGENTA_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 2),
    LIGHT_BLUE_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 3),
    YELLOW_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 4),
    LIME_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 5),
    PINK_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 6),
    GRAY_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 7),
    LIGHT_GRAY_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 8),
    CYAN_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 9),
    PURPLE_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 10),
    BLUE_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 11),
    BROWN_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 12),
    GREEN_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 13),
    RED_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 14),
    BLACK_HARDENED_CLAY(159, org.bukkit.Material.STAINED_CLAY, 15),
    WHITE_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 0),
    ORANGE_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 1),
    MAGENTA_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 2),
    LIGHT_BLUE_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 3),
    YELLOW_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 4),
    LIME_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 5),
    PINK_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 6),
    GRAY_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 7),
    LIGHT_GRAY_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 8),
    CYAN_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 9),
    PURPLE_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 10),
    BLUE_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 11),
    BROWN_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 12),
    GREEN_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 13),
    RED_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 14),
    BLACK_STAINED_GLASS_PANE(160, org.bukkit.Material.STAINED_GLASS_PANE, 15),
    ACACIA_LEAVES(161, org.bukkit.Material.LEAVES_2, 0),
    DARK_OAK_LEAVES(161, org.bukkit.Material.LEAVES_2, 1),
    ACACIA_WOOD(162, org.bukkit.Material.LOG_2, 0),
    DARK_OAK_WOOD(162, org.bukkit.Material.LOG_2, 1),
    ACACIA_WOOD_STAIRS(163, org.bukkit.Material.ACACIA_STAIRS, 0),
    DARK_OAK_WOOD_STAIRS(164, org.bukkit.Material.DARK_OAK_STAIRS, 0),
    SLIME_BLOCK(165, org.bukkit.Material.SLIME_BLOCK, 0),
    BARRIER(166, org.bukkit.Material.BARRIER, 0),
    IRON_TRAPDOOR(167, org.bukkit.Material.IRON_TRAPDOOR, 0),
    PRISMARINE(168, org.bukkit.Material.PRISMARINE, 0),
    PRISMARINE_BRICKS(168, org.bukkit.Material.PRISMARINE, 1),
    DARK_PRISMARINE(168, org.bukkit.Material.PRISMARINE, 2),
    SEA_LANTERN(169, org.bukkit.Material.SEA_LANTERN, 0),
    HAY_BALE(170, org.bukkit.Material.HAY_BLOCK, 0),
    WHITE_CARPET(171, org.bukkit.Material.CARPET, 0),
    ORANGE_CARPET(171, org.bukkit.Material.CARPET, 1),
    MAGENTA_CARPET(171, org.bukkit.Material.CARPET, 2),
    LIGHT_BLUE_CARPET(171, org.bukkit.Material.CARPET, 3),
    YELLOW_CARPET(171, org.bukkit.Material.CARPET, 4),
    LIME_CARPET(171, org.bukkit.Material.CARPET, 5),
    PINK_CARPET(171, org.bukkit.Material.CARPET, 6),
    GRAY_CARPET(171, org.bukkit.Material.CARPET, 7),
    LIGHT_GRAY_CARPET(171, org.bukkit.Material.CARPET, 8),
    CYAN_CARPET(171, org.bukkit.Material.CARPET, 9),
    PURPLE_CARPET(171, org.bukkit.Material.CARPET, 10),
    BLUE_CARPET(171, org.bukkit.Material.CARPET, 11),
    BROWN_CARPET(171, org.bukkit.Material.CARPET, 12),
    GREEN_CARPET(171, org.bukkit.Material.CARPET, 13),
    RED_CARPET(171, org.bukkit.Material.CARPET, 14),
    BLACK_CARPET(171, org.bukkit.Material.CARPET, 15),
    HARDENED_CLAY(172, org.bukkit.Material.HARD_CLAY, 0),
    BLOCK_OF_COAL(173, org.bukkit.Material.COAL_BLOCK, 0),
    PACKED_ICE(174, org.bukkit.Material.PACKED_ICE, 0),
    SUNFLOWER(175, org.bukkit.Material.DOUBLE_PLANT, 0),
    LILAC(175, org.bukkit.Material.DOUBLE_PLANT, 1),
    DOUBLE_TALLGRASS(175, org.bukkit.Material.DOUBLE_PLANT, 2),
    LARGE_FERN(175, org.bukkit.Material.DOUBLE_PLANT, 3),
    ROSE_BUSH(175, org.bukkit.Material.DOUBLE_PLANT, 4),
    PEONY(175, org.bukkit.Material.DOUBLE_PLANT, 5),
    FREE_STANDING_BANNER(176, org.bukkit.Material.STANDING_BANNER, 0),
    WALL_MOUNTED_BANNER(177, org.bukkit.Material.WALL_BANNER, 0),
    INVERTED_DAYLIGHT_SENSOR(178, org.bukkit.Material.DAYLIGHT_DETECTOR_INVERTED, 0),
    RED_SANDSTONE(179, org.bukkit.Material.RED_SANDSTONE, 0),
    CHISELED_RED_SANDSTONE(179, org.bukkit.Material.RED_SANDSTONE, 1),
    SMOOTH_RED_SANDSTONE(179, org.bukkit.Material.RED_SANDSTONE, 2),
    RED_SANDSTONE_STAIRS(180, org.bukkit.Material.RED_SANDSTONE_STAIRS, 0),
    DOUBLE_RED_SANDSTONE_SLAB(181, org.bukkit.Material.DOUBLE_STONE_SLAB2, 0),
    RED_SANDSTONE_SLAB(182, org.bukkit.Material.STONE_SLAB2, 0),
    SPRUCE_FENCE_GATE(183, org.bukkit.Material.SPRUCE_FENCE_GATE, 0),
    BIRCH_FENCE_GATE(184, org.bukkit.Material.BIRCH_FENCE_GATE, 0),
    JUNGLE_FENCE_GATE(185, org.bukkit.Material.JUNGLE_FENCE_GATE, 0),
    DARK_OAK_FENCE_GATE(186, org.bukkit.Material.DARK_OAK_FENCE_GATE, 0),
    ACACIA_FENCE_GATE(187, org.bukkit.Material.ACACIA_FENCE_GATE, 0),
    SPRUCE_FENCE(188, org.bukkit.Material.SPRUCE_FENCE, 0),
    BIRCH_FENCE(189, org.bukkit.Material.BIRCH_FENCE, 0),
    JUNGLE_FENCE(190, org.bukkit.Material.JUNGLE_FENCE, 0),
    DARK_OAK_FENCE(191, org.bukkit.Material.DARK_OAK_FENCE, 0),
    ACACIA_FENCE(192, org.bukkit.Material.ACACIA_FENCE, 0),
    SPRUCE_DOOR_BLOCK(193, org.bukkit.Material.SPRUCE_DOOR, 0),
    BIRCH_DOOR_BLOCK(194, org.bukkit.Material.BIRCH_DOOR, 0),
    JUNGLE_DOOR_BLOCK(195, org.bukkit.Material.JUNGLE_DOOR, 0),
    ACACIA_DOOR_BLOCK(196, org.bukkit.Material.ACACIA_DOOR, 0),
    DARK_OAK_DOOR_BLOCK(197, org.bukkit.Material.DARK_OAK_DOOR, 0),
    IRON_SHOVEL(256, org.bukkit.Material.IRON_SPADE, 0),
    IRON_PICKAXE(257, org.bukkit.Material.IRON_PICKAXE, 0),
    IRON_AXE(258, org.bukkit.Material.IRON_AXE, 0),
    FLINT_AND_STEEL(259, org.bukkit.Material.FLINT_AND_STEEL, 0),
    APPLE(260, org.bukkit.Material.APPLE, 0),
    BOW(261, org.bukkit.Material.BOW, 0),
    ARROW(262, org.bukkit.Material.ARROW, 0),
    COAL(263, org.bukkit.Material.COAL, 0),
    CHARCOAL(263, org.bukkit.Material.COAL, 1),
    DIAMOND(264, org.bukkit.Material.DIAMOND, 0),
    IRON_INGOT(265, org.bukkit.Material.IRON_INGOT, 0),
    GOLD_INGOT(266, org.bukkit.Material.GOLD_INGOT, 0),
    IRON_SWORD(267, org.bukkit.Material.IRON_SWORD, 0),
    WOODEN_SWORD(268, org.bukkit.Material.WOOD_SWORD, 0),
    WOODEN_SHOVEL(269, org.bukkit.Material.WOOD_SPADE, 0),
    WOODEN_PICKAXE(270, org.bukkit.Material.WOOD_PICKAXE, 0),
    WOODEN_AXE(271, org.bukkit.Material.WOOD_AXE, 0),
    STONE_SWORD(272, org.bukkit.Material.STONE_SWORD, 0),
    STONE_SHOVEL(273, org.bukkit.Material.STONE_SPADE, 0),
    STONE_PICKAXE(274, org.bukkit.Material.STONE_PICKAXE, 0),
    STONE_AXE(275, org.bukkit.Material.STONE_AXE, 0),
    DIAMOND_SWORD(276, org.bukkit.Material.DIAMOND_SWORD, 0),
    DIAMOND_SHOVEL(277, org.bukkit.Material.DIAMOND_SPADE, 0),
    DIAMOND_PICKAXE(278, org.bukkit.Material.DIAMOND_PICKAXE, 0),
    DIAMOND_AXE(279, org.bukkit.Material.DIAMOND_AXE, 0),
    STICK(280, org.bukkit.Material.STICK, 0),
    BOWL(281, org.bukkit.Material.BOWL, 0),
    MUSHROOM_STEW(282, org.bukkit.Material.MUSHROOM_SOUP, 0),
    GOLDEN_SWORD(283, org.bukkit.Material.GOLD_SWORD, 0),
    GOLDEN_SHOVEL(284, org.bukkit.Material.GOLD_SPADE, 0),
    GOLDEN_PICKAXE(285, org.bukkit.Material.GOLD_PICKAXE, 0),
    GOLDEN_AXE(286, org.bukkit.Material.GOLD_AXE, 0),
    STRING(287, org.bukkit.Material.STRING, 0),
    FEATHER(288, org.bukkit.Material.FEATHER, 0),
    GUNPOWDER(289, org.bukkit.Material.SULPHUR, 0),
    WOODEN_HOE(290, org.bukkit.Material.WOOD_HOE, 0),
    STONE_HOE(291, org.bukkit.Material.STONE_HOE, 0),
    IRON_HOE(292, org.bukkit.Material.IRON_HOE, 0),
    DIAMOND_HOE(293, org.bukkit.Material.DIAMOND_HOE, 0),
    GOLDEN_HOE(294, org.bukkit.Material.GOLD_HOE, 0),
    WHEAT_SEEDS(295, org.bukkit.Material.SEEDS, 0),
    WHEAT(296, org.bukkit.Material.WHEAT, 0),
    BREAD(297, org.bukkit.Material.BREAD, 0),
    LEATHER_HELMET(298, org.bukkit.Material.LEATHER_HELMET, 0),
    LEATHER_TUNIC(299, org.bukkit.Material.LEATHER_CHESTPLATE, 0),
    LEATHER_PANTS(300, org.bukkit.Material.LEATHER_LEGGINGS, 0),
    LEATHER_BOOTS(301, org.bukkit.Material.LEATHER_BOOTS, 0),
    CHAINMAIL_HELMET(302, org.bukkit.Material.CHAINMAIL_HELMET, 0),
    CHAINMAIL_CHESTPLATE(303, org.bukkit.Material.CHAINMAIL_CHESTPLATE, 0),
    CHAINMAIL_LEGGINGS(304, org.bukkit.Material.CHAINMAIL_LEGGINGS, 0),
    CHAINMAIL_BOOTS(305, org.bukkit.Material.CHAINMAIL_BOOTS, 0),
    IRON_HELMET(306, org.bukkit.Material.IRON_HELMET, 0),
    IRON_CHESTPLATE(307, org.bukkit.Material.IRON_CHESTPLATE, 0),
    IRON_LEGGINGS(308, org.bukkit.Material.IRON_LEGGINGS, 0),
    IRON_BOOTS(309, org.bukkit.Material.IRON_BOOTS, 0),
    DIAMOND_HELMET(310, org.bukkit.Material.DIAMOND_HELMET, 0),
    DIAMOND_CHESTPLATE(311, org.bukkit.Material.DIAMOND_CHESTPLATE, 0),
    DIAMOND_LEGGINGS(312, org.bukkit.Material.DIAMOND_LEGGINGS, 0),
    DIAMOND_BOOTS(313, org.bukkit.Material.DIAMOND_BOOTS, 0),
    GOLDEN_HELMET(314, org.bukkit.Material.GOLD_HELMET, 0),
    GOLDEN_CHESTPLATE(315, org.bukkit.Material.GOLD_CHESTPLATE, 0),
    GOLDEN_LEGGINGS(316, org.bukkit.Material.GOLD_LEGGINGS, 0),
    GOLDEN_BOOTS(317, org.bukkit.Material.GOLD_BOOTS, 0),
    FLINT(318, org.bukkit.Material.FLINT, 0),
    RAW_PORKCHOP(319, org.bukkit.Material.PORK, 0),
    COOKED_PORKCHOP(320, org.bukkit.Material.GRILLED_PORK, 0),
    PAINTING(321, org.bukkit.Material.PAINTING, 0),
    GOLDEN_APPLE(322, org.bukkit.Material.GOLDEN_APPLE, 0),
    ENCHANTED_GOLDEN_APPLE(322, org.bukkit.Material.GOLDEN_APPLE, 1),
    SIGN(323, org.bukkit.Material.SIGN, 0),
    OAK_DOOR(324, org.bukkit.Material.WOOD_DOOR, 0),
    BUCKET(325, org.bukkit.Material.BUCKET, 0),
    WATER_BUCKET(326, org.bukkit.Material.WATER_BUCKET, 0),
    LAVA_BUCKET(327, org.bukkit.Material.LAVA_BUCKET, 0),
    MINECART(328, org.bukkit.Material.MINECART, 0),
    SADDLE(329, org.bukkit.Material.SADDLE, 0),
    IRON_DOOR(330, org.bukkit.Material.IRON_DOOR, 0),
    REDSTONE(331, org.bukkit.Material.REDSTONE, 0),
    SNOWBALL(332, org.bukkit.Material.SNOW_BALL, 0),
    OAK_BOAT(333, org.bukkit.Material.BOAT, 0),
    LEATHER(334, org.bukkit.Material.LEATHER, 0),
    MILK_BUCKET(335, org.bukkit.Material.MILK_BUCKET, 0),
    BRICK_ITEM(336, org.bukkit.Material.CLAY_BRICK, 0),
    CLAY_BALL(337, org.bukkit.Material.CLAY_BALL, 0),
    SUGAR_CANE_ITEM(338, org.bukkit.Material.SUGAR_CANE, 0),
    PAPER(339, org.bukkit.Material.PAPER, 0),
    BOOK(340, org.bukkit.Material.BOOK, 0),
    SLIMEBALL(341, org.bukkit.Material.SLIME_BALL, 0),
    MINECART_WITH_CHEST(342, org.bukkit.Material.STORAGE_MINECART, 0),
    MINECART_WITH_FURNACE(343, org.bukkit.Material.POWERED_MINECART, 0),
    EGG(344, org.bukkit.Material.EGG, 0),
    COMPASS(345, org.bukkit.Material.COMPASS, 0),
    FISHING_ROD(346, org.bukkit.Material.FISHING_ROD, 0),
    CLOCK(347, org.bukkit.Material.WATCH, 0),
    GLOWSTONE_DUST(348, org.bukkit.Material.GLOWSTONE_DUST, 0),
    RAW_FISH(349, org.bukkit.Material.RAW_FISH, 0),
    RAW_SALMON(349, org.bukkit.Material.RAW_FISH, 1),
    CLOWNFISH(349, org.bukkit.Material.RAW_FISH, 2),
    PUFFERFISH(349, org.bukkit.Material.RAW_FISH, 3),
    COOKED_FISH(350, org.bukkit.Material.COOKED_FISH, 0),
    COOKED_SALMON(350, org.bukkit.Material.COOKED_FISH, 1),
    INK_SACK(351, org.bukkit.Material.INK_SACK, 0),
    ROSE_RED(351, org.bukkit.Material.INK_SACK, 1),
    CACTUS_GREEN(351, org.bukkit.Material.INK_SACK, 2),
    COCO_BEANS(351, org.bukkit.Material.INK_SACK, 3),
    LAPIS_LAZULI(351, org.bukkit.Material.INK_SACK, 4),
    PURPLE_DYE(351, org.bukkit.Material.INK_SACK, 5),
    CYAN_DYE(351, org.bukkit.Material.INK_SACK, 6),
    LIGHT_GRAY_DYE(351, org.bukkit.Material.INK_SACK, 7),
    GRAY_DYE(351, org.bukkit.Material.INK_SACK, 8),
    PINK_DYE(351, org.bukkit.Material.INK_SACK, 9),
    LIME_DYE(351, org.bukkit.Material.INK_SACK, 10),
    DANDELION_YELLOW(351, org.bukkit.Material.INK_SACK, 11),
    LIGHT_BLUE_DYE(351, org.bukkit.Material.INK_SACK, 12),
    MAGENTA_DYE(351, org.bukkit.Material.INK_SACK, 13),
    ORANGE_DYE(351, org.bukkit.Material.INK_SACK, 14),
    BONE_MEAL(351, org.bukkit.Material.INK_SACK, 15),
    BONE(352, org.bukkit.Material.BONE, 0),
    SUGAR(353, org.bukkit.Material.SUGAR, 0),
    CAKE(354, org.bukkit.Material.CAKE, 0),
    BED_ITEM(355, org.bukkit.Material.BED, 0),
    REDSTONE_REPEATER(356, org.bukkit.Material.DIODE, 0),
    COOKIE(357, org.bukkit.Material.COOKIE, 0),
    MAP(358, org.bukkit.Material.MAP, 0),
    SHEARS(359, org.bukkit.Material.SHEARS, 0),
    MELON(360, org.bukkit.Material.MELON, 0),
    PUMPKIN_SEEDS(361, org.bukkit.Material.PUMPKIN_SEEDS, 0),
    MELON_SEEDS(362, org.bukkit.Material.MELON_SEEDS, 0),
    RAW_BEEF(363, org.bukkit.Material.RAW_BEEF, 0),
    STEAK(364, org.bukkit.Material.COOKED_BEEF, 0),
    RAW_CHICKEN(365, org.bukkit.Material.RAW_CHICKEN, 0),
    COOKED_CHICKEN(366, org.bukkit.Material.COOKED_CHICKEN, 0),
    ROTTEN_FLESH(367, org.bukkit.Material.ROTTEN_FLESH, 0),
    ENDER_PEARL(368, org.bukkit.Material.ENDER_PEARL, 0),
    BLAZE_ROD(369, org.bukkit.Material.BLAZE_ROD, 0),
    GHAST_TEAR(370, org.bukkit.Material.GHAST_TEAR, 0),
    GOLD_NUGGET(371, org.bukkit.Material.GOLD_NUGGET, 0),
    NETHER_WART_ITEM(372, org.bukkit.Material.NETHER_STALK, 0),
    POTION(373, org.bukkit.Material.POTION, 0),
    GLASS_BOTTLE(374, org.bukkit.Material.GLASS_BOTTLE, 0),
    SPIDER_EYE(375, org.bukkit.Material.SPIDER_EYE, 0),
    FERMENTED_SPIDER_EYE(376, org.bukkit.Material.FERMENTED_SPIDER_EYE, 0),
    BLAZE_POWDER(377, org.bukkit.Material.BLAZE_POWDER, 0),
    MAGMA_CREAM(378, org.bukkit.Material.MAGMA_CREAM, 0),
    BREWING_STAND_ITEM(379, org.bukkit.Material.BREWING_STAND_ITEM, 0),
    CAULDRON_ITEM(380, org.bukkit.Material.CAULDRON_ITEM, 0),
    EYE_OF_ENDER(381, org.bukkit.Material.EYE_OF_ENDER, 0),
    GLISTERING_MELON(382, org.bukkit.Material.SPECKLED_MELON, 0),
    SPAWN_ELDER_GUARDIAN_EGG(383, org.bukkit.Material.MONSTER_EGG, 4),
    SPAWN_WITHER_SKELETON_EGG(383, org.bukkit.Material.MONSTER_EGG, 5),
    SPAWN_STRAY_EGG(383, org.bukkit.Material.MONSTER_EGG, 6),
    SPAWN_HUSK_EGG(383, org.bukkit.Material.MONSTER_EGG, 23),
    SPAWN_ZOMBIE_VILLAGER_EGG(383, org.bukkit.Material.MONSTER_EGG, 27),
    SPAWN_SKELETON_HORSE_EGG(383, org.bukkit.Material.MONSTER_EGG, 28),
    SPAWN_ZOMBIE_HORSE_EGG(383, org.bukkit.Material.MONSTER_EGG, 29),
    SPAWN_DONKEY_EGG(383, org.bukkit.Material.MONSTER_EGG, 31),
    SPAWN_MULE_EGG(383, org.bukkit.Material.MONSTER_EGG, 32),
    SPAWN_EVOKER_EGG(383, org.bukkit.Material.MONSTER_EGG, 34),
    SPAWN_VEX_EGG(383, org.bukkit.Material.MONSTER_EGG, 35),
    SPAWN_VINDICATOR_EGG(383, org.bukkit.Material.MONSTER_EGG, 36),
    SPAWN_CREEPER_EGG(383, org.bukkit.Material.MONSTER_EGG, 50),
    SPAWN_SKELETON_EGG(383, org.bukkit.Material.MONSTER_EGG, 51),
    SPAWN_SPIDER_EGG(383, org.bukkit.Material.MONSTER_EGG, 52),
    SPAWN_ZOMBIE_EGG(383, org.bukkit.Material.MONSTER_EGG, 54),
    SPAWN_SLIME_EGG(383, org.bukkit.Material.MONSTER_EGG, 55),
    SPAWN_GHAST_EGG(383, org.bukkit.Material.MONSTER_EGG, 56),
    SPAWN_ZOMBIE_PIGMAN_EGG(383, org.bukkit.Material.MONSTER_EGG, 57),
    SPAWN_ENDERMAN_EGG(383, org.bukkit.Material.MONSTER_EGG, 58),
    SPAWN_CAVE_SPIDER_EGG(383, org.bukkit.Material.MONSTER_EGG, 59),
    SPAWN_SILVERFISH_EGG(383, org.bukkit.Material.MONSTER_EGG, 60),
    SPAWN_BLAZE_EGG(383, org.bukkit.Material.MONSTER_EGG, 61),
    SPAWN_MAGMA_CUBE_EGG(383, org.bukkit.Material.MONSTER_EGG, 62),
    SPAWN_BAT_EGG(383, org.bukkit.Material.MONSTER_EGG, 65),
    SPAWN_WITCH_EGG(383, org.bukkit.Material.MONSTER_EGG, 66),
    SPAWN_ENDERMITE_EGG(383, org.bukkit.Material.MONSTER_EGG, 67),
    SPAWN_GUARDIAN_EGG(383, org.bukkit.Material.MONSTER_EGG, 68),
    SPAWN_SHULKER_EGG(383, org.bukkit.Material.MONSTER_EGG, 69),
    SPAWN_PIG_EGG(383, org.bukkit.Material.MONSTER_EGG, 90),
    SPAWN_SHEEP_EGG(383, org.bukkit.Material.MONSTER_EGG, 91),
    SPAWN_COW_EGG(383, org.bukkit.Material.MONSTER_EGG, 92),
    SPAWN_CHICKEN_EGG(383, org.bukkit.Material.MONSTER_EGG, 93),
    SPAWN_SQUID_EGG(383, org.bukkit.Material.MONSTER_EGG, 94),
    SPAWN_WOLF_EGG(383, org.bukkit.Material.MONSTER_EGG, 95),
    SPAWN_MOOSHROOM_EGG(383, org.bukkit.Material.MONSTER_EGG, 96),
    SPAWN_OCELOT_EGG(383, org.bukkit.Material.MONSTER_EGG, 98),
    SPAWN_HORSE_EGG(383, org.bukkit.Material.MONSTER_EGG, 100),
    SPAWN_RABBIT_EGG(383, org.bukkit.Material.MONSTER_EGG, 101),
    SPAWN_POLAR_BEAR_EGG(383, org.bukkit.Material.MONSTER_EGG, 102),
    SPAWN_LLAMA_EGG(383, org.bukkit.Material.MONSTER_EGG, 103),
    SPAWN_PARROT_EGG(383, org.bukkit.Material.MONSTER_EGG, 105),
    SPAWN_VILLAGER_EGG(383, org.bukkit.Material.MONSTER_EGG, 120),
    BOTTLE_O_ENCHANTING(384, org.bukkit.Material.EXP_BOTTLE, 0),
    FIRE_CHARGE(385, org.bukkit.Material.FIREBALL, 0),
    BOOK_AND_QUILL(386, org.bukkit.Material.BOOK_AND_QUILL, 0),
    WRITTEN_BOOK(387, org.bukkit.Material.WRITTEN_BOOK, 0),
    EMERALD(388, org.bukkit.Material.EMERALD, 0),
    ITEM_FRAME(389, org.bukkit.Material.ITEM_FRAME, 0),
    FLOWER_POT_ITEM(390, org.bukkit.Material.FLOWER_POT_ITEM, 0),
    CARROT(391, org.bukkit.Material.CARROT_ITEM, 0),
    POTATO(392, org.bukkit.Material.POTATO_ITEM, 0),
    BAKED_POTATO(393, org.bukkit.Material.BAKED_POTATO, 0),
    POISONOUS_POTATO(394, org.bukkit.Material.POISONOUS_POTATO, 0),
    EMPTY_MAP(395, org.bukkit.Material.EMPTY_MAP, 0),
    GOLDEN_CARROT(396, org.bukkit.Material.GOLDEN_CARROT, 0),
    MOB_HEAD_SKELETON(397, org.bukkit.Material.SKULL_ITEM, 0),
    MOB_HEAD_WITHER_SKELETON(397, org.bukkit.Material.SKULL_ITEM, 1),
    MOB_HEAD_ZOMBIE(397, org.bukkit.Material.SKULL_ITEM, 2),
    MOB_HEAD_HUMAN(397, org.bukkit.Material.SKULL_ITEM, 3),
    MOB_HEAD_CREEPER(397, org.bukkit.Material.SKULL_ITEM, 4),
    MOB_HEAD_DRAGON(397, org.bukkit.Material.SKULL_ITEM, 5),
    CARROT_ON_A_STICK(398, org.bukkit.Material.CARROT_STICK, 0),
    NETHER_STAR(399, org.bukkit.Material.NETHER_STAR, 0),
    PUMPKIN_PIE(400, org.bukkit.Material.PUMPKIN_PIE, 0),
    FIREWORK_ROCKET(401, org.bukkit.Material.FIREWORK, 0),
    FIREWORK_STAR(402, org.bukkit.Material.FIREWORK_CHARGE, 0),
    ENCHANTED_BOOK(403, org.bukkit.Material.ENCHANTED_BOOK, 0),
    REDSTONE_COMPARATOR(404, org.bukkit.Material.REDSTONE_COMPARATOR, 0),
    NETHER_BRICK_ITEM(405, org.bukkit.Material.NETHER_BRICK_ITEM, 0),
    NETHER_QUARTZ(406, org.bukkit.Material.QUARTZ, 0),
    MINECART_WITH_TNT(407, org.bukkit.Material.EXPLOSIVE_MINECART, 0),
    MINECART_WITH_HOPPER(408, org.bukkit.Material.HOPPER_MINECART, 0),
    PRISMARINE_SHARD(409, org.bukkit.Material.PRISMARINE_SHARD, 0),
    PRISMARINE_CRYSTALS(410, org.bukkit.Material.PRISMARINE_CRYSTALS, 0),
    RAW_RABBIT(411, org.bukkit.Material.RABBIT, 0),
    COOKED_RABBIT(412, org.bukkit.Material.COOKED_RABBIT, 0),
    RABBIT_STEW(413, org.bukkit.Material.RABBIT_STEW, 0),
    RABBIT_S_FOOT(414, org.bukkit.Material.RABBIT_FOOT, 0),
    RABBIT_HIDE(415, org.bukkit.Material.RABBIT_HIDE, 0),
    ARMOR_STAND(416, org.bukkit.Material.ARMOR_STAND, 0),
    IRON_HORSE_ARMOR(417, org.bukkit.Material.IRON_BARDING, 0),
    GOLDEN_HORSE_ARMOR(418, org.bukkit.Material.GOLD_BARDING, 0),
    DIAMOND_HORSE_ARMOR(419, org.bukkit.Material.DIAMOND_BARDING, 0),
    LEAD(420, org.bukkit.Material.LEASH, 0),
    NAME_TAG(421, org.bukkit.Material.NAME_TAG, 0),
    MINECART_WITH_COMMAND_BLOCK(422, org.bukkit.Material.COMMAND_MINECART, 0),
    RAW_MUTTON(423, org.bukkit.Material.MUTTON, 0),
    COOKED_MUTTON(424, org.bukkit.Material.COOKED_MUTTON, 0),
    BANNER(425, org.bukkit.Material.BANNER, 0),
    SPRUCE_DOOR(427, org.bukkit.Material.SPRUCE_DOOR_ITEM, 0),
    BIRCH_DOOR(428, org.bukkit.Material.BIRCH_DOOR_ITEM, 0),
    JUNGLE_DOOR(429, org.bukkit.Material.JUNGLE_DOOR_ITEM, 0),
    ACACIA_DOOR(430, org.bukkit.Material.ACACIA_DOOR_ITEM, 0),
    DARK_OAK_DOOR(431, org.bukkit.Material.DARK_OAK_DOOR_ITEM, 0),
    DISC_13(2256, org.bukkit.Material.GOLD_RECORD, 0),
    CAT_DISC(2257, org.bukkit.Material.GREEN_RECORD, 0),
    BLOCKS_DISC(2258, org.bukkit.Material.RECORD_3, 0),
    CHIRP_DISC(2259, org.bukkit.Material.RECORD_4, 0),
    FAR_DISC(2260, org.bukkit.Material.RECORD_5, 0),
    MALL_DISC(2261, org.bukkit.Material.RECORD_6, 0),
    MELLOHI_DISC(2262, org.bukkit.Material.RECORD_7, 0),
    STAL_DISC(2263, org.bukkit.Material.RECORD_8, 0),
    STRAD_DISC(2264, org.bukkit.Material.RECORD_9, 0),
    WARD_DISC(2265, org.bukkit.Material.RECORD_10, 0),
    DISC_11(2266, org.bukkit.Material.RECORD_11, 0),
    ;

    private static final Map<String,EMaterial> byName;
    private static final Map<Integer,EMaterial> byLegacyID;
    private static final Map<Material, List<EMaterial>> byMaterialId;

    static {
        synchronized (EMaterial.class) {
            Map<String,EMaterial> byNameTemp = new HashMap<>();
            Map<Integer,EMaterial> byLegacyTemp = new HashMap<>();
            Map<Material,List<EMaterial>> byIdTemp = new HashMap<>();
            for (EMaterial material : EMaterial.values()) {

                byNameTemp.put(material.toString(), material);
                byLegacyTemp.put(material.legacyID, material);
                if (byIdTemp.containsKey(material.bukkitMaterial)) {
                    byIdTemp.get(material.bukkitMaterial).add(material);
                } else {
                    List<EMaterial> list = new ArrayList<>();
                    list.add(material);
                    byIdTemp.put(material.bukkitMaterial, list);
                }
            }
            byName = Collections.unmodifiableMap(byNameTemp);
            byLegacyID = Collections.unmodifiableMap(byLegacyTemp);
            byMaterialId = Collections.unmodifiableMap(byIdTemp);
        }
    }

    private final int legacyID;
    private final org.bukkit.Material bukkitMaterial;
    private short itemData;

    EMaterial(int legacyID, org.bukkit.Material bukkitMaterial, int id) {
        this.legacyID = legacyID;
        this.bukkitMaterial = bukkitMaterial;
        this.itemData = (short) id;
    }

    EMaterial(int legacyID, org.bukkit.Material bukkitMaterial) {
        this.legacyID = legacyID;
        this.bukkitMaterial = bukkitMaterial;
        this.itemData = 0;
    }

    public short getItemData() {
        return itemData;
    }

    public Material getBukkitMaterial() {
        return bukkitMaterial;
    }

    public boolean isSolid() {
        return this.bukkitMaterial.isSolid();
    }

    public boolean isOccluding(EMaterial material) {
        return this.bukkitMaterial.isOccluding();
    }

    public boolean isBlock(EMaterial material) {
        return this.bukkitMaterial.isBlock();
    }

    public boolean isBurnable(EMaterial material) {
        return this.bukkitMaterial.isBurnable();
    }

    public boolean hasGravity(EMaterial material) {
        return this.bukkitMaterial.hasGravity();
    }

    public boolean isEdible(EMaterial material) {
        return this.bukkitMaterial.isEdible();
    }

    public boolean isFlammable(EMaterial material) {
        return this.bukkitMaterial.isFlammable();
    }

    public boolean isTransparent(EMaterial material) {
        return this.bukkitMaterial.isTransparent();
    }

    public boolean isRecord() {
        return this.bukkitMaterial.isRecord();
    }

    public boolean isSapling() {
        return this.bukkitMaterial.equals(Material.SAPLING);
    }

    public boolean isFlower() {
        switch (this.bukkitMaterial) {
            case YELLOW_FLOWER:
            case RED_ROSE:
            case DOUBLE_PLANT:
                return true;
            default:
                return false;
        }
    }

    //checks whether the material will not make a valid itemstack. This essentially checks whether
    //the material is air, or a liquid material such as lava or water
    public boolean rawGasLiquid() {
        return rawGasLiquid(this.bukkitMaterial);
    }

    public static boolean rawGasLiquid(Material material) {
        switch (material) {
            case AIR:
            case LAVA:
            case WATER:
            case STATIONARY_LAVA:
            case STATIONARY_WATER:
                return true;
            default:
                return false;
        }
    }

    public Class<? extends MaterialData> getData() {
        return this.bukkitMaterial.getData();
    }

    public boolean isSlab() {
        switch (this.bukkitMaterial) {
            case STEP:
            case DOUBLE_STEP:
            case WOOD_STEP:
            case WOOD_DOUBLE_STEP:
                return true;
            default:
                return false;
        }
    }

    public int getMaxStackSize() {
        return this.bukkitMaterial.getMaxStackSize();
    }

    public short getMaxDurability() {
        return this.bukkitMaterial.getMaxDurability();
    }
    @Nullable
    public static EMaterial matchMaterial(String material) {
        return byName.get(material);
    }
    @Nullable
    public static EMaterial matchBukkitMaterial(Material material, short itemData) {
        List<EMaterial> candidates = byMaterialId.get(material);
        if(candidates==null) {
            return null;
        }

        if(candidates.size()==0) {
            return null;
        }

        for(EMaterial material1 : candidates) {
            if(material1.getItemData() == itemData) {
                return material1;
            }
        }
        return candidates.get(0);
    }

    @Nullable
    public static EMaterial byLegacyID(int id) {
        return byLegacyID.get(id);
    }
}
