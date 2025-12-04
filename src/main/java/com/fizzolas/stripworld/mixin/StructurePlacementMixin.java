package com.fizzolas.stripworld.mixin;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RandomSpreadStructurePlacement.class)
public abstract class StructurePlacementMixin {
    private static final int STRIP_WIDTH_CHUNKS = 2;

    /**
     * Adjust structure placement to respect strip boundaries.
     * This ensures structures only generate within the 2-chunk wide strips.
     */
    @Inject(method = "getPotentialStructureChunk", at = @At("RETURN"), cancellable = true)
    private void adjustStructurePlacementForStrips(
            long seed,
            int x,
            int z,
            CallbackInfoReturnable<ChunkPos> cir
    ) {
        ChunkPos originalPos = cir.getReturnValue();
        
        if (originalPos == null) {
            return;
        }

        // Check if the structure would be placed outside strip boundaries
        int chunkX = originalPos.x;
        int normalizedX = Math.abs(chunkX % STRIP_WIDTH_CHUNKS);
        
        if (normalizedX >= STRIP_WIDTH_CHUNKS) {
            // Structure is outside strip - shift it to nearest strip position
            int offset = chunkX >= 0 ? -normalizedX : STRIP_WIDTH_CHUNKS - normalizedX;
            ChunkPos adjustedPos = new ChunkPos(chunkX + offset, originalPos.z);
            cir.setReturnValue(adjustedPos);
        }
    }
}