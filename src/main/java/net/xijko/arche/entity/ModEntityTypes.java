package net.xijko.arche.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.xijko.arche.Arche;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITIES, Arche.MOD_ID);

    public static final RegistryObject<EntityType<CandleEntity>> CANDLE =
            ENTITY_TYPES.register("candle_entity",
                    () -> EntityType.Builder.create(CandleEntity::new, EntityClassification.CREATURE)
                            .size(0.25F,0.5F)
                            .build(new ResourceLocation(Arche.MOD_ID, "candle_entity").toString()));


    public static final RegistryObject<EntityType<WhipProjectileEntity>> WHIP_PROJECTILE =
            ENTITY_TYPES.register("whip_projectile_entity",
                    () -> EntityType.Builder.create(WhipProjectileEntity::new, EntityClassification.MISC)
                            .build(new ResourceLocation(Arche.MOD_ID, "whip_projectile_entity").toString()));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }

}
