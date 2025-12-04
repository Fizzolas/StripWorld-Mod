package com.fizzolas.stripworld;

import com.fizzolas.stripworld.config.StripWorldConfig;
import com.fizzolas.stripworld.worldgen.ModFeatures;
import com.fizzolas.stripworld.worldgen.ModWorldGen;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(StripWorldMod.MODID)
public class StripWorldMod {
    public static final String MODID = "stripworld";
    private static final Logger LOGGER = LogUtils.getLogger();

    public StripWorldMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register configuration
        StripWorldConfig.register();
        
        // Register features and world generation
        ModFeatures.register(modEventBus);
        ModWorldGen.register(modEventBus);
        
        // Register setup method
        modEventBus.addListener(this::commonSetup);
        
        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("========================================");
        LOGGER.info("StripWorld Mod v1.0.0 Initialized!");
        LOGGER.info("========================================");
        LOGGER.info("Features:");
        LOGGER.info("  - 2-chunk wide strip generation");
        LOGGER.info("  - Buildable void patches beneath bedrock");
        LOGGER.info("  - Full dimension support (Overworld, Nether, End)");
        LOGGER.info("  - Structure adaptation for strip boundaries");
        LOGGER.info("  - Mod compatibility framework");
        LOGGER.info("========================================");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("StripWorld common setup complete");
        LOGGER.info("World generation features registered successfully");
        LOGGER.info("Configuration loaded from config/stripworld-common.toml");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("========================================");
        LOGGER.info("StripWorld mod active on server!");
        LOGGER.info("Strip world generation is ready!");
        LOGGER.info("Create a new world and select 'Strip World' preset");
        LOGGER.info("========================================");
    }
}