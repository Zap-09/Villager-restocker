package com.zap.villagerrestocker;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = VillagerRestocker.MODID, name = VillagerRestocker.NAME, version = VillagerRestocker.VERSION)
public class VillagerRestocker {
    public static final String MODID = "villagerrestocker";
    public static final String NAME = "Villager Restocker";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onVillagerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().world.isRemote || event.getEntity().world.getTotalWorldTime() % 20 != 0) {
            return;
        }

        if (event.getEntityLiving() instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) event.getEntityLiving();
            int delay = ConfigHandler.restockDelay;

            if (Math.abs(villager.ticksExisted + villager.getUniqueID().hashCode()) % delay < 20) {
                restockVillager(villager);
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void restockVillager(EntityVillager villager) {
        MerchantRecipeList recipes = villager.getRecipes(null);
        if (recipes != null) {
            boolean actuallyRestocked = false;

            for (MerchantRecipe recipe : recipes) {
                net.minecraft.nbt.NBTTagCompound tag = recipe.writeToTags();
                int currentUses = tag.getInteger("uses");
                int maxUses = recipe.getMaxTradeUses();

                if (currentUses >= maxUses) {
                    tag.setInteger("uses", 0);
                    recipe.readFromTags(tag);
                    actuallyRestocked = true;
                }
            }

            if (actuallyRestocked) {
                villager.world.setEntityState(villager, (byte) 14);
            }
        }
    }
}