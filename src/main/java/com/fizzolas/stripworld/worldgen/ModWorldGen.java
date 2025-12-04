package com.fizzolas.stripworld.worldgen;

import com.fizzolas.stripworld.StripWorldMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModWorldGen {
    public static final DeferredRegister<WorldPreset> WORLD_PRESETS = DeferredRegister.create(
        Registries.WORLD_PRESET,
        StripWorldMod.MODID
    );

    public static void register(IEventBus eventBus) {
        WORLD_PRESETS.register(eventBus);
    }
}