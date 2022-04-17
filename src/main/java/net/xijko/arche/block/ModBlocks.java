package net.xijko.arche.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.item.ModItemGroup;
import net.xijko.arche.item.ModItems;
import net.xijko.arche.storages.examplestorage.ExampleStorageBlock;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, Arche.MOD_ID);
    public static final DeferredRegister<Item> ITEMS
            = DeferredRegister.create(ForgeRegistries.ITEMS, Arche.MOD_ID);

    public static final RegistryObject<Block> AMETHYST_ORE = registerBlock("amethyst_ore",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f))
            );

    public static final RegistryObject<Block> AMETHYST_BLOCK = registerBlock("amethyst_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().hardnessAndResistance(5f))
    );

    public static final RegistryObject<ExampleStorageBlock> EXAMPLE_STORAGE = BLOCKS.register("storage", ExampleStorageBlock::new);

    public static final RegistryObject<Block> POOP_DEPOSIT = BLOCKS.register("poop_deposit",PoopDeposit::new);

    public static final RegistryObject<Block> DIRT_DEPOSIT = BLOCKS.register("dirt_deposit",DirtDeposit::new);

    public static final RegistryObject<Block> SAND_DEPOSIT = BLOCKS.register("sand_deposit",SandDeposit::new);

    public static final RegistryObject<Block> STONE_DEPOSIT = BLOCKS.register("stone_deposit",StoneDeposit::new);



    /*
    public static final RegistryObject<Block> POOP_DEPOSIT = registerBlock("poop_deposit",
            () -> new Block(AbstractBlock.Properties.create(Material.EARTH).harvestLevel(0).harvestTool(ToolType.SHOVEL).hardnessAndResistance(0f))
    );*/

    //google "generics registry object"
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.ARCHE_GROUP)
                ));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}

