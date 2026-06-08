package com.zap.villagerrestocker;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = VillagerRestocker.MODID)
@Config.LangKey("villagerrestocker.config.title")
public class ConfigHandler {

    @Config.Comment("How many ticks between restocks (20 ticks = 1 second). Default 12000 is 10 minutes.")
    @Config.RangeInt(min = 20)
    @Config.Name("Restock Delay")
    public static int restockDelay = 12000;

    @Mod.EventBusSubscriber(modid = VillagerRestocker.MODID)
    @SuppressWarnings("unused")
    private static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(VillagerRestocker.MODID)) {
                ConfigManager.sync(VillagerRestocker.MODID, Config.Type.INSTANCE);
            }
        }
    }
}