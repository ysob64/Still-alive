package com.stillalive.enchantment;

import com.mojang.serialization.MapCodec;
import com.stillalive.StillAlive;
import com.stillalive.enchantment.effect.EnergizeEnchantmentEffect;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;


public class StillAliveEnchEffects {
	public static final ResourceKey<Enchantment> ENERGIZE = of("energize");
	public static MapCodec<EnergizeEnchantmentEffect> FAST_ATTACK_EFFECT = register("fast_attack_effect", EnergizeEnchantmentEffect.CODEC);

	private static ResourceKey<Enchantment> of(String path) {
		ResourceLocation id = ResourceLocation.fromNamespaceAndPath(StillAlive.MOD_ID, path);
		return ResourceKey.create(Registries.ENCHANTMENT, id);
	}

	private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
		return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, ResourceLocation.fromNamespaceAndPath(StillAlive.MOD_ID, id), codec);
	}

	public static void registerModEnchantmentEffects() {
		StillAlive.LOGGER.info("Registering Enchantment Effects for " + StillAlive.MOD_ID);
	}
}