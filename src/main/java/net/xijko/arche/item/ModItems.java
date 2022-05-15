package net.xijko.arche.item;

import net.minecraft.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;

import static net.xijko.arche.block.ModBlocks.*;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Arche.MOD_ID);


    //ITEMS HERE
    //be sure to add to assets.MODID.lang.en_us.json for item names
    //be sure to add to assets.MODID.models.item.ITEMREGISTERNAME.json for sprite details/texture
    //be sure to add .png to assets.MODID.textures.item

    public static final RegistryObject<Item> AMETHYST = ITEMS.register("amethyst",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> POOP_DEBRIS = ITEMS.register("poop_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> POOP_DEPOSIT_ITEM = ITEMS.register("poop_deposit",
            () -> new BlockItem(POOP_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));

    //sieves/cleaning tools
    public static final RegistryObject<Item> DIRT_SIEVE = ITEMS.register("dirt_sieve",
            () -> new ArcheSieves(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).maxDamage(64),1));
    public static final RegistryObject<Item> STONE_SIEVE = ITEMS.register("stone_sieve",
            () -> new ArcheSieves(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).maxDamage(128),2));
    public static final RegistryObject<Item> TOOL_BELT = ITEMS.register("tool_belt",
            ToolBeltItem::new);

    /*public static final RegistryObject<Item> CLEANING_TABLE_ITEM = ITEMS.register("cleaning_table",
            () -> new BlockItem(CleaningTableBlock.get, new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));*/
    //public static final ContainerType<ToolBeltContainer> TOOL_BELT_CONTAINER = IForgeContainerType.create(ToolBeltContainer::createContainerClientSide);

    //deposit ores
    public static final RegistryObject<Item> DIRT_DEPOSIT_ITEM = ITEMS.register("dirt_deposit",
            () -> new BlockItem(DIRT_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> SAND_DEPOSIT_ITEM = ITEMS.register("sand_deposit",
            () -> new BlockItem(SAND_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> STONE_DEPOSIT_ITEM = ITEMS.register("stone_deposit",
            () -> new BlockItem(STONE_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> OBSIDIAN_DEPOSIT_ITEM = ITEMS.register("obsidian_deposit",
            () -> new BlockItem(OBSIDIAN_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));


    public static final RegistryObject<Item> DIRT_DEBRIS = ITEMS.register("dirt_debris",
            () -> new ArcheDebris(new Item.Properties().group(ModItemGroup.ARCHE_GROUP),1));
    public static final RegistryObject<Item> STONE_DEBRIS = ITEMS.register("stone_debris",
            () -> new ArcheDebris(new Item.Properties().group(ModItemGroup.ARCHE_GROUP), 2));

    //dirt tier mats

    //primary
    public static final RegistryObject<Item> DIRT_SHARD = ITEMS.register("dirt_shard",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> DIRT_BONE = ITEMS.register("dirt_bone",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> DIRT_ROCK = ITEMS.register("dirt_rock",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));

    //secondary
    public static final RegistryObject<Item> DIRT_PLATE = ITEMS.register("dirt_plate",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));

    //prog
    public static final RegistryObject<Item> MATTOCK_HEAD_SHARD = ITEMS.register("mattock_head_shard",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));

    //dirt tier artifacts
    //dirt coin
    public static final RegistryObject<Item> DIRT_COIN_LEFT = ITEMS.register("dirt_coin_left",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIRT_COIN_RIGHT = ITEMS.register("dirt_coin_right",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DIRT_COIN = ITEMS.register("dirt_coin",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).rarity(Rarity.RARE)));

    public static final RegistryObject<Item> DIRT_COIN_LEFT_DAMAGED = ITEMS.register("dirt_coin_left_damaged",
            () -> new ArcheArtifactBroken(ModItems.DIRT_COIN_LEFT.get(),1,Items.CLAY_BALL,8,ModItems.DIRT_SHARD.get(),4,ModItems.DIRT_PLATE.get(),2,Items.GOLD_NUGGET,1));
    public static final RegistryObject<Item> DIRT_COIN_RIGHT_DAMAGED = ITEMS.register("dirt_coin_right_damaged",
            () -> new ArcheArtifactBroken(ModItems.DIRT_COIN_RIGHT.get(),1,Items.CLAY_BALL,8,ModItems.DIRT_SHARD.get(),4,ModItems.DIRT_PLATE.get(),2,Items.GOLD_NUGGET,1));

    //stone tier mats

    //primary

    //secondary

    //prog

    //stone tier artifacts
    public static final RegistryObject<Item> STONE_BUCKLE_DAMAGED = ITEMS.register("stone_buckle_damaged",
            () -> new ArcheArtifactBroken(ModItems.TOOL_BELT.get(),1,Items.STRING,16,Items.IRON_INGOT,4,Items.LEATHER_LEGGINGS,1,Items.CHEST,1));


    public static final RegistryObject<Item> DIAMOND_SPLINT = ITEMS.register("diamond_splint",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));



    public static final RegistryObject<Item> MATTOCK_HEAD_TEMPLATE = ITEMS.register("mattock_head_template",
            () -> new MattockHeadTemplate(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> MATTOCK_HEAD_MOLD_RAW = ITEMS.register("mattock_head_mold_raw",
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP)));
    public static final RegistryObject<Item> MATTOCK_HEAD_MOLD = ITEMS.register("mattock_head_mold",
            () -> new MattockHeadMold(new Item.Properties().group(ModItemGroup.ARCHE_GROUP).maxDamage(4)));
    public static final RegistryObject<Item> MATTOCK_IRON = ITEMS.register("mattock_iron",
            () -> new MattockItem(1,-2.8F, ItemTier.IRON,(new Item.Properties()).group(ModItemGroup.ARCHE_GROUP)));

    /*
    public static final RegistryObject<Item> GRAVELDEBRIS = ITEMS.register("gravel_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> STONEDEBRIS = ITEMS.register("stone_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> IRONDEBRIS = ITEMS.register("iron_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> OBSIDIANDEBRIS = ITEMS.register("obsidian_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> NETHERRITEDEBRIS = ITEMS.register("netherrite_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

    public static final RegistryObject<Item> ENDDEBRIS = ITEMS.register("end_debris",
            () -> new Item(new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab

*/
    //END ITEMS


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
