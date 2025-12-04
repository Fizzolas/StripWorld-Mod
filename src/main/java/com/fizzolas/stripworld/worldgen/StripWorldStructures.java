package com.fizzolas.stripworld.worldgen;

import com.fizzolas.stripworld.StripWorldMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.List;

/**
 * Handles structure set configuration for strip worlds.
 * Ensures structures generate with appropriate spacing within the constraints.
 */
public class StripWorldStructures {
    
    /**
     * Bootstrap structure sets for strip world generation.
     * This adjusts vanilla structure placement to work better with strips.
     */
    public static void bootstrap(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);
        
        // Note: Structure sets are typically defined via data packs.
        // This class provides utilities and can be extended for custom structure sets.
        // Vanilla structures will be automatically adjusted by the mixin.
    }

    /**
     * Create a structure placement that respects strip boundaries.
     * Reduces spacing to account for limited X-axis space.
     */
    public static RandomSpreadStructurePlacement createStripAwarePlacement(
            int spacing,
            int separation,
            RandomSpreadType spreadType
    ) {
        // Reduce spacing slightly to account for strip constraints
        // This helps ensure structures still generate at reasonable density
        int adjustedSpacing = Math.max(spacing / 2, separation + 1);
        
        return new RandomSpreadStructurePlacement(
            adjustedSpacing,
            separation,
            spreadType,
            123 // Salt for randomization
        );
    }
}