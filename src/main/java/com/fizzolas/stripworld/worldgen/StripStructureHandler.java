package com.fizzolas.stripworld.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Utility class for handling structure placement within strip boundaries.
 */
public class StripStructureHandler {
    private static final int STRIP_WIDTH_CHUNKS = 2;
    private static final int STRIP_WIDTH_BLOCKS = STRIP_WIDTH_CHUNKS * 16;

    /**
     * Check if a chunk position is within valid strip boundaries.
     */
    public static boolean isChunkInStrip(ChunkPos pos) {
        return isChunkInStrip(pos.x);
    }

    public static boolean isChunkInStrip(int chunkX) {
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        return normalizedX < STRIP_WIDTH_CHUNKS;
    }

    /**
     * Check if a block position is within valid strip boundaries.
     */
    public static boolean isBlockInStrip(BlockPos pos) {
        return isBlockInStrip(pos.getX());
    }

    public static boolean isBlockInStrip(int blockX) {
        int chunkX = blockX >> 4;
        return isChunkInStrip(chunkX);
    }

    /**
     * Adjust a chunk position to the nearest valid strip position.
     */
    public static ChunkPos adjustToNearestStrip(ChunkPos pos) {
        if (isChunkInStrip(pos)) {
            return pos;
        }

        int chunkX = pos.x;
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        
        // Calculate offset to nearest strip
        int offset = chunkX >= 0 ? -normalizedX : STRIP_WIDTH_CHUNKS - normalizedX;
        
        return new ChunkPos(chunkX + offset, pos.z);
    }

    /**
     * Check if a bounding box fits within strip boundaries.
     * Returns true if the entire structure can fit in the strip.
     */
    public static boolean doesStructureFitInStrip(BoundingBox box) {
        int minChunkX = box.minX() >> 4;
        int maxChunkX = box.maxX() >> 4;
        
        // Check if all chunks the structure spans are in the same strip alignment
        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            if (!isChunkInStrip(chunkX)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Get the width of a strip in blocks.
     */
    public static int getStripWidthBlocks() {
        return STRIP_WIDTH_BLOCKS;
    }

    /**
     * Get the width of a strip in chunks.
     */
    public static int getStripWidthChunks() {
        return STRIP_WIDTH_CHUNKS;
    }
}