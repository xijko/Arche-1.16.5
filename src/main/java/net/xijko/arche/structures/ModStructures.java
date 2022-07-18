package net.xijko.arche.structures;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ModStructures {

    private static final Logger LOGGER = LogManager.getLogger();

    private static void addBuildingToPool(MutableRegistry<JigsawPattern> templatePoolRegistry, ResourceLocation poolRL, String nbtPieceRL, int weight) {

        // Grab the pool we want to add to
        JigsawPattern pool = templatePoolRegistry.getOrDefault(poolRL);
        LOGGER.warn("Pool before adding: "+pool);
        if (pool == null) return;

        // Grabs the nbt piece and creates a SingleJigsawPiece of it that we can add to a structure's pool.
        JigsawPiece piece = JigsawPiece.func_242849_a(nbtPieceRL).apply(JigsawPattern.PlacementBehaviour.RIGID);


        for (int i = 0; i < weight; i++) {
            pool.jigsawPieces.add(piece);
        }

        List<Pair<JigsawPiece, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
        listOfPieceEntries.add(new Pair<>(piece, weight));
        pool.rawTemplates = listOfPieceEntries;
        LOGGER.warn("JigsawPieces after adding: "+pool.jigsawPieces);
        LOGGER.warn("RawTemplates after adding: "+pool.rawTemplates);

    }



    /**
     * We use FMLServerAboutToStartEvent as the dynamic registry exists now and all JSON worldgen files were parsed.
     * Mod compat is best done here.
     */
    public static void addNewVillageBuilding(final FMLServerAboutToStartEvent event) {

        //WorldGenRegistries.JIGSAW_POOL.getValueForKey()
        MutableRegistry<JigsawPattern> templatePoolRegistry = event.getServer().getDynamicRegistries().getRegistry(Registry.JIGSAW_POOL_KEY);

        LOGGER.warn("Patterns: "+templatePoolRegistry);
        // Adds our piece to all village houses pool
        // Note, the resourcelocation is getting the pool files from the data folder. Not assets folder.
        //addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/plains/houses"),"arche:structure_nbt_resourcelocation", 5);

        //addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/snowy/houses"),"modid:structure_nbt_resourcelocation", 5);

        //addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/savanna/houses"),"modid:structure_nbt_resourcelocation", 5);

        //addBuildingToPool(templatePoolRegistry, new ResourceLocation("minecraft:village/taiga/houses"),"modid:structure_nbt_resourcelocation", 5);

        addBuildingToPool(templatePoolRegistry, new ResourceLocation("village/desert/houses"),
                "arche:village/desert/houses/desert_museum", 50);

    }

    private void register(String rlString){

    }
}
