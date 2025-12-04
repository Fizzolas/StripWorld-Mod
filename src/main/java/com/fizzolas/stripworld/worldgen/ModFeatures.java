package com.fizzolas.stripworld.worldgen;

import com.fizzolas.stripworld.StripWorldMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
        ForgeRegistries.FEATURES,
        StripWorldMod.MODID
    );

    public static final RegistryObject<VoidPatchFeature> VOID_PATCH = FEATURES.register(
        "void_patch",
        () -> new VoidPatchFeature(VoidPatchConfig.CODEC)
    );

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}