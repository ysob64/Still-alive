package com.stillalive.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

import com.stillalive.StillAlive;


public record EnergizeEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
	public static final MapCodec<EnergizeEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					LevelBasedValue.CODEC.fieldOf("amount").forGetter(EnergizeEnchantmentEffect::amount)
			).apply(instance, EnergizeEnchantmentEffect::new)
	);

	@Override
	public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
		if (target instanceof LivingEntity) {
			if (context.owner() != null && context.owner() instanceof Player player) {
				player.addEffect(new MobEffectInstance(StillAlive.effects.SPARTAN, 40, level, true, false, false));
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> codec() {
		return CODEC;
	}
}