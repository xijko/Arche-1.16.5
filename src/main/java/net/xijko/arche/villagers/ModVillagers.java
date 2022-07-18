package net.xijko.arche.villagers;

import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;
import net.xijko.arche.block.ModBlocks;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, Arche.MOD_ID);

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, Arche.MOD_ID);

    public static final RegistryObject<PointOfInterestType> MUSEUM_CURATOR_POI = POI_TYPES.register("museum_curator_poi",
            () -> new PointOfInterestType("museum_curator_poi", PointOfInterestType.getAllStates(ModBlocks.RESTORE_TABLE.get()),1,6));

    public static final RegistryObject<VillagerProfession> MUSEUM_CURATOR = VILLAGER_PROFESSIONS.register("museum_curator",
            () -> new VillagerProfession("museum_curator", MUSEUM_CURATOR_POI.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_CARTOGRAPHER));
    //First = what they can hold in their inventory
    //Second = what blocks they can work at. Replace second one with the artifact cases with items in it, when possible

    public static void registerPOIs(){
        try{
            ObfuscationReflectionHelper.findMethod(PointOfInterestType.class,"registerBlockStates",PointOfInterestType.class).invoke(null,MUSEUM_CURATOR_POI.get());
            //ObfuscationReflectionHelper.findMethod(PointOfInterestType.class,"registerBlockStates",PointOfInterestType.class).invoke(null,MUSEUM_CURATOR_POI.get());
        }catch(InvocationTargetException | IllegalAccessException exception){
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus e){
        POI_TYPES.register(e);
        VILLAGER_PROFESSIONS.register(e);
    }

}
