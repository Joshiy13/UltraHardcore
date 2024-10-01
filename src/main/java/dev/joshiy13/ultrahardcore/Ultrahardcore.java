package dev.joshiy13.ultrahardcore;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
import net.minecraft.world.Difficulty;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class Ultrahardcore implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register(this::onServerTickEnd);
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
    }

    private void onServerStarted(MinecraftServer server) {
        server.getGameRules().get(GameRules.NATURAL_REGENERATION).set(false, server);
        server.setDifficulty(Difficulty.HARD, true);
    }

    private void onServerTickEnd(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach(player -> {
            if (player.isOnFire() && !player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                player.setFireTicks(20);
            }
            if (!player.hasStatusEffect(StatusEffects.HUNGER)) {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HUNGER, Integer.MAX_VALUE, 0));
            }
        });
    }
}