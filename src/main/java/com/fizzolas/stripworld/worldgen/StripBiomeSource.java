package com.fizzolas.stripworld.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;

import java.util.stream.Stream;

public class StripBiomeSource extends MultiNoiseBiomeSource {
    public static final Codec<StripBiomeSource> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Preset.CODEC.fieldOf("preset").forGetter(source -> source.preset)
        ).apply(instance, StripBiomeSource::new)
    );

    private final Preset preset;
    private static final int STRIP_WIDTH_CHUNKS = 2;

    public StripBiomeSource(Preset preset) {
        super(preset.provider);
        this.preset = preset;
    }

    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        // Constrain X coordinate to strip boundaries for consistent biome selection
        // This ensures biomes repeat across parallel strips
        int strippedX = constrainToStrip(x);
        return super.getNoiseBiome(strippedX, y, z, sampler);
    }

    private int constrainToStrip(int x) {
        // Map all X coordinates to their position within a single strip
        int stripWidthQuarts = STRIP_WIDTH_CHUNKS * 4; // Convert chunks to quart-blocks (biome scale)
        int modX = x % stripWidthQuarts;
        
        // Normalize to 0 to stripWidth range
        if (modX < 0) {
            modX += stripWidthQuarts;
        }
        
        return modX;
    }

    public static StripBiomeSource overworldSource(HolderGetter<Biome> biomes) {
        return new StripBiomeSource(Preset.OVERWORLD.apply(biomes));
    }

    public static StripBiomeSource netherSource(HolderGetter<Biome> biomes) {
        return new StripBiomeSource(Preset.NETHER.apply(biomes));
    }
}