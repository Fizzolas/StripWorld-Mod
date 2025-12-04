package com.fizzolas.stripworld.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.level.levelgen.RandomSupport;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StripChunkGeneratorWithVoid extends NoiseBasedChunkGenerator {
    public static final Codec<StripChunkGeneratorWithVoid> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)
        ).apply(instance, StripChunkGeneratorWithVoid::new)
    );

    private static final int STRIP_WIDTH_CHUNKS = 2;
    private static final int STRIP_WIDTH_BLOCKS = STRIP_WIDTH_CHUNKS * 16;

    public StripChunkGeneratorWithVoid(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
        super(biomeSource, settings);
    }

    @Override
    protected Codec<? extends ChunkGenerator> codec() {
        return CODEC;
    }

    @Override
    public CompletableFuture<ChunkAccess> fillFromNoise(Executor executor, Blender blender, RandomState randomState,
                                                         StructureManager structureManager, ChunkAccess chunk) {
        return super.fillFromNoise(executor, blender, randomState, structureManager, chunk).thenApply(chunkAccess -> {
            applyStripGeneration(chunkAccess);
            applyVoidPatches(chunkAccess, randomState);
            return chunkAccess;
        });
    }

    private void applyStripGeneration(ChunkAccess chunk) {
        int chunkX = chunk.getPos().x;
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        
        if (normalizedX >= STRIP_WIDTH_CHUNKS) {
            fillChunkWithVoid(chunk);
        }
    }

    private void fillChunkWithVoid(ChunkAccess chunk) {
        int minY = chunk.getMinBuildHeight();
        int maxY = chunk.getMaxBuildHeight();
        
        BlockState air = Blocks.AIR.defaultBlockState();
        BlockState caveAir = Blocks.CAVE_AIR.defaultBlockState();
        
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    chunk.setBlockState(pos, y < 0 ? caveAir : air, false);
                }
            }
        }
    }

    private void applyVoidPatches(ChunkAccess chunk, RandomState randomState) {
        if (!isChunkInStrip(chunk.getPos().x)) {
            return; // Don't apply void patches outside strips
        }

        int minY = chunk.getMinBuildHeight();
        int bedrockTop = minY + 5;
        
        // Create deterministic random based on chunk position
        WorldgenRandom random = new WorldgenRandom(RandomSupport.newThreadSafeFork(randomState.legacyLevelSeed()));
        random.setDecorationSeed(chunk.getPos().x, chunk.getPos().z);
        
        // Only generate patches occasionally
        if (random.nextFloat() > 0.15f) {
            return;
        }

        // Create noise for irregular patterns
        PerlinSimplexNoise noise = new PerlinSimplexNoise(
            RandomSupport.newThreadSafeFork(random),
            List.of(0, 1)
        );

        int patchCenterX = random.nextInt(16);
        int patchCenterZ = random.nextInt(16);
        int radius = Mth.randomBetweenInclusive(random, 5, 20);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                double distance = Math.sqrt(
                    Math.pow(x - patchCenterX, 2) + Math.pow(z - patchCenterZ, 2)
                );
                
                BlockPos worldPos = chunk.getPos().getWorldPosition().offset(x, 0, z);
                double noiseValue = noise.getValue(worldPos.getX() * 0.1, worldPos.getZ() * 0.1, false);
                double effectiveRadius = radius + (noiseValue * radius * 0.3);
                
                if (distance <= effectiveRadius) {
                    for (int y = minY; y <= bedrockTop; y++) {
                        BlockPos pos = new BlockPos(x, y, z);
                        
                        double keepChance = noise.getValue(
                            worldPos.getX() * 0.05,
                            y * 0.2,
                            worldPos.getZ() * 0.05
                        );
                        
                        if (keepChance <= 0.6) {
                            chunk.setBlockState(pos, Blocks.CAVE_AIR.defaultBlockState(), false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, 
                            BiomeSource biomeSource, StructureManager structureManager, 
                            ChunkAccess chunk, GenerationStep.Carving carving) {
        if (isChunkInStrip(chunk.getPos().x)) {
            super.applyCarvers(region, seed, randomState, biomeSource, structureManager, chunk, carving);
        }
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, 
                            RandomState randomState, ChunkAccess chunk) {
        if (isChunkInStrip(chunk.getPos().x)) {
            super.buildSurface(region, structureManager, randomState, chunk);
        }
    }

    private boolean isChunkInStrip(int chunkX) {
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        return normalizedX < STRIP_WIDTH_CHUNKS;
    }

    @Override
    public int getBaseHeight(int x, int z, Heightmap.Types heightmapType, LevelHeightAccessor level, RandomState randomState) {
        int chunkX = x >> 4;
        if (!isChunkInStrip(chunkX)) {
            return level.getMinBuildHeight();
        }
        return super.getBaseHeight(x, z, heightmapType, level, randomState);
    }

    @Override
    public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor level, RandomState randomState) {
        int chunkX = x >> 4;
        if (!isChunkInStrip(chunkX)) {
            return new NoiseColumn(level.getMinBuildHeight(), new BlockState[0]);
        }
        return super.getBaseColumn(x, z, level, randomState);
    }
}