package com.fizzolas.stripworld.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record VoidPatchConfig(
    int minRadius,
    int maxRadius,
    float frequency
) implements FeatureConfiguration {
    
    public static final Codec<VoidPatchConfig> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            Codec.INT.fieldOf("min_radius").forGetter(VoidPatchConfig::minRadius),
            Codec.INT.fieldOf("max_radius").forGetter(VoidPatchConfig::maxRadius),
            Codec.FLOAT.fieldOf("frequency").forGetter(VoidPatchConfig::frequency)
        ).apply(instance, VoidPatchConfig::new)
    );

    public static final VoidPatchConfig DEFAULT = new VoidPatchConfig(5, 20, 0.15f);
}