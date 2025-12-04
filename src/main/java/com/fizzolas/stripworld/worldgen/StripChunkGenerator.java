package com.fizzolas.stripworld.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class StripChunkGenerator extends NoiseBasedChunkGenerator {
    public static final Codec<StripChunkGenerator> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
            BiomeSource.CODEC.fieldOf("biome_source").forGetter(gen -> gen.biomeSource),
            NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(gen -> gen.settings)
        ).apply(instance, StripChunkGenerator::new)
    );

    private static final int STRIP_WIDTH_CHUNKS = 2;
    private static final int STRIP_WIDTH_BLOCKS = STRIP_WIDTH_CHUNKS * 16;

    public StripChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
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
            return chunkAccess;
        });
    }

    private void applyStripGeneration(ChunkAccess chunk) {
        int chunkX = chunk.getPos().x;
        
        // Calculate if this chunk is outside the strip boundaries
        // Strips are aligned to world origin (0,0)
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        
        if (normalizedX >= STRIP_WIDTH_CHUNKS) {
            // This chunk is outside the strip - fill with air/void
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
                    // Use cave air below y=0 for better visuals
                    chunk.setBlockState(pos, y < 0 ? caveAir : air, false);
                }
            }
        }
    }

    @Override
    public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, 
                            BiomeSource biomeSource, StructureManager structureManager, 
                            ChunkAccess chunk, GenerationStep.Carving carving) {
        // Only apply carvers if chunk is within strip boundaries
        if (isChunkInStrip(chunk.getPos().x)) {
            super.applyCarvers(region, seed, randomState, biomeSource, structureManager, chunk, carving);
        }
    }

    @Override
    public void buildSurface(WorldGenRegion region, StructureManager structureManager, 
                            RandomState randomState, ChunkAccess chunk) {
        // Only build surface if chunk is within strip boundaries
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
        // Return minimum height for areas outside strips
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
            // Return empty column for areas outside strips
            return new NoiseColumn(level.getMinBuildHeight(), new BlockState[0]);
        }
        return super.getBaseColumn(x, z, level, randomState);
    }
}