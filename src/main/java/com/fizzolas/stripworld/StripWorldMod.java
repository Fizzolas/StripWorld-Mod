package com.fizzolas.stripworld;

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
        
        // Register features and world generation
        ModFeatures.register(modEventBus);
        ModWorldGen.register(modEventBus);
        
        // Register setup method
        modEventBus.addListener(this::commonSetup);
        
        // Register ourselves for server and other game events
        MinecraftForge.EVENT_BUS.register(this);
        
        LOGGER.info("StripWorld Mod initialized!");
        LOGGER.info("- 2-chunk wide strip generation enabled");
        LOGGER.info("- Void patch generation enabled");
        LOGGER.info("- All dimensions supported (Overworld, Nether, End)");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("StripWorld common setup complete");
        LOGGER.info("World generation features registered successfully");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("StripWorld mod active on server");
        LOGGER.info("Strip world generation is ready!");
    }
}