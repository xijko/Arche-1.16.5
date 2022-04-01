package net.xijko.arche.item;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;

import static net.xijko.arche.block.ModBlocks.POOP_DEPOSIT;
import static net.xijko.arche.block.ModBlocks.DIRT_DEPOSIT;

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
            () -> new Item(new Item.Properties().group(ModItemGroup.ARCHE_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab
    public static final RegistryObject<Item> POOP_DEPOSIT_ITEM = ITEMS.register("poop_deposit",
            () -> new BlockItem(POOP_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab


    public static final RegistryObject<Item> DIRT_DEBRIS = ITEMS.register("dirt_debris",
            () -> new ArcheDebris(new Item.Properties().group(ModItemGroup.ARCHE_GROUP),1)); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab
    public static final RegistryObject<Item> DIRT_DEPOSIT_ITEM = ITEMS.register("dirt_deposit",
            () -> new BlockItem(DIRT_DEPOSIT.get(), new Item.Properties().group(ModItemGroup.ARCHE_GROUP))); //this determines the 'group' that this item belongs to - is sued by creative inventories to determine the tab




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
