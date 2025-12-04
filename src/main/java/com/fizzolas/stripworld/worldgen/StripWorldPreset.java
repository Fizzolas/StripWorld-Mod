package com.fizzolas.stripworld.worldgen;

import com.fizzolas.stripworld.StripWorldMod;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.presets.WorldPreset;

import java.util.Map;

public class StripWorldPreset {
    public static final ResourceKey<WorldPreset> STRIP_WORLD = ResourceKey.create(
        Registries.WORLD_PRESET,
        new ResourceLocation(StripWorldMod.MODID, "strip_world")
    );

    public static void bootstrap(BootstapContext<WorldPreset> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimensions = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        // Create overworld dimension with strip generation and void patches
        Holder<NoiseGeneratorSettings> overworldSettings = noiseSettings.getOrThrow(
            NoiseGeneratorSettings.OVERWORLD
        );
        BiomeSource overworldBiomes = StripBiomeSource.overworldSource(biomes);
        StripChunkGeneratorWithVoid overworldGenerator = new StripChunkGeneratorWithVoid(
            overworldBiomes, 
            overworldSettings
        );

        LevelStem overworldStem = new LevelStem(
            dimensions.getOrThrow(DimensionType.OVERWORLD_LOCATION),
            overworldGenerator
        );

        // Create nether dimension with strip generation and void patches
        Holder<NoiseGeneratorSettings> netherSettings = noiseSettings.getOrThrow(
            NoiseGeneratorSettings.NETHER
        );
        BiomeSource netherBiomes = StripBiomeSource.netherSource(biomes);
        StripChunkGeneratorWithVoid netherGenerator = new StripChunkGeneratorWithVoid(
            netherBiomes, 
            netherSettings
        );

        LevelStem netherStem = new LevelStem(
            dimensions.getOrThrow(DimensionType.NETHER_LOCATION),
            netherGenerator
        );

        // Create end dimension with strip generation and void patches
        Holder<NoiseGeneratorSettings> endSettings = noiseSettings.getOrThrow(
            NoiseGeneratorSettings.END
        );
        BiomeSource endBiomes = MultiNoiseBiomeSource.Preset.END.biomeSource(biomes);
        StripChunkGeneratorWithVoid endGenerator = new StripChunkGeneratorWithVoid(
            endBiomes, 
            endSettings
        );

        LevelStem endStem = new LevelSlem(
            dimensions.getOrThrow(DimensionType.END_LOCATION),
            endGenerator
        );

        context.register(STRIP_WORLD, new WorldPreset(Map.of(
            LevelStem.OVERWORLD, overworldStem,
            LevelStem.NETHER, netherStem,
            LevelStem.END, endStem
        )));
    }
}