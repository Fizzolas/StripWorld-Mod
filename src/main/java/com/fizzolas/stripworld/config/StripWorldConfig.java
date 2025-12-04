package com.fizzolas.stripworld.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class StripWorldConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Strip width configuration
    public static final ForgeConfigSpec.IntValue STRIP_WIDTH_CHUNKS;
    
    // Void patch configuration
    public static final ForgeConfigSpec.BooleanValue GENERATE_VOID_PATCHES;
    public static final ForgeConfigSpec.IntValue VOID_PATCH_MIN_RADIUS;
    public static final ForgeConfigSpec.IntValue VOID_PATCH_MAX_RADIUS;
    public static final ForgeConfigSpec.DoubleValue VOID_PATCH_FREQUENCY;
    
    // Dimension configuration
    public static final ForgeConfigSpec.BooleanValue ENABLE_NETHER_STRIPS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_END_STRIPS;
    
    // Compatibility configuration
    public static final ForgeConfigSpec.BooleanValue ALLOW_MOD_COMPAT;
    public static final ForgeConfigSpec.BooleanValue PRESERVE_STRUCTURE_SPACING;

    static {
        BUILDER.push("World Generation");
        
        STRIP_WIDTH_CHUNKS = BUILDER
            .comment("Width of the world strips in chunks (default: 2)")
            .defineInRange("stripWidthChunks", 2, 1, 16);
        
        GENERATE_VOID_PATCHES = BUILDER
            .comment("Whether to generate void patches beneath bedrock")
            .define("generateVoidPatches", true);
        
        VOID_PATCH_MIN_RADIUS = BUILDER
            .comment("Minimum radius of void patches in blocks")
            .defineInRange("voidPatchMinRadius", 5, 3, 32);
        
        VOID_PATCH_MAX_RADIUS = BUILDER
            .comment("Maximum radius of void patches in blocks")
            .defineInRange("voidPatchMaxRadius", 20, 3, 64);
        
        VOID_PATCH_FREQUENCY = BUILDER
            .comment("Frequency of void patches (0.0 to 1.0, higher = more patches)")
            .defineInRange("voidPatchFrequency", 0.15, 0.0, 1.0);
        
        ENABLE_NETHER_STRIPS = BUILDER
            .comment("Enable strip generation in the Nether")
            .define("enableNetherStrips", true);
        
        ENABLE_END_STRIPS = BUILDER
            .comment("Enable strip generation in the End")
            .define("enableEndStrips", true);
        
        BUILDER.pop();
        
        BUILDER.push("Compatibility");
        
        ALLOW_MOD_COMPAT = BUILDER
            .comment("Allow other mods to modify world generation")
            .define("allowModCompat", true);
        
        PRESERVE_STRUCTURE_SPACING = BUILDER
            .comment("Preserve vanilla structure spacing")
            .define("preserveStructureSpacing", true);
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "stripworld-common.toml");
    }
}