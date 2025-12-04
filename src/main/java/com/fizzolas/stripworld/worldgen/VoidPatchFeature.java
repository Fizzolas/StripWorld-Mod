package com.fizzolas.stripworld.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.RandomSupport;

import java.util.List;

public class VoidPatchFeature extends Feature<VoidPatchConfig> {
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();

    public VoidPatchFeature(Codec<VoidPatchConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<VoidPatchConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        VoidPatchConfig config = context.config();

        // Only generate in bedrock layer (y = -64 to -59 typically)
        int minY = level.getMinBuildHeight();
        int bedrockTop = minY + 5; // Typically -59 in overworld

        if (origin.getY() > bedrockTop) {
            return false;
        }

        // Random chance to generate based on config frequency
        if (random.nextFloat() > config.frequency()) {
            return false;
        }

        // Determine patch size
        int radius = Mth.randomBetweenInclusive(random, config.minRadius(), config.maxRadius());
        
        // Create Perlin noise for irregular shape
        PerlinSimplexNoise noise = new PerlinSimplexNoise(
            RandomSupport.newThreadSafeFork(random),
            List.of(0, 1)
        );

        // Carve out the void patch
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos checkPos = origin.offset(x, 0, z);
                double distance = Math.sqrt(x * x + z * z);
                
                // Use noise to create irregular edges
                double noiseValue = noise.getValue(checkPos.getX() * 0.1, checkPos.getZ() * 0.1, false);
                double effectiveRadius = radius + (noiseValue * radius * 0.3);
                
                if (distance <= effectiveRadius) {
                    // Remove bedrock and create void hole
                    for (int y = minY; y <= bedrockTop; y++) {
                        BlockPos voidPos = new BlockPos(checkPos.getX(), y, checkPos.getZ());
                        
                        // Don't remove ALL bedrock - keep some patches for visual interest
                        // Use noise to determine which blocks to keep
                        double keepChance = noise.getValue(
                            voidPos.getX() * 0.05,
                            voidPos.getY() * 0.2,
                            voidPos.getZ() * 0.05
                        );
                        
                        if (keepChance > 0.6) {
                            continue; // Keep this bedrock block
                        }
                        
                        level.setBlock(voidPos, CAVE_AIR, 2);
                    }
                }
            }
        }

        return true;
    }
}