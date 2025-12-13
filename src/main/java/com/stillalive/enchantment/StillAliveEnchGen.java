package com.stillalive.enchantment;

import java.util.concurrent.CompletableFuture;

import com.stillalive.enchantment.effect.EnergizeEnchantmentEffect;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentTarget;
import net.minecraft.world.item.enchantment.LevelBasedValue;

public class StillAliveEnchGen extends FabricDynamicRegistryProvider {
	public StillAliveEnchGen(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
		System.out.println("REGISTERING ENCHANTS");
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		// Our new enchantment, "Thundering."
		this.register(entries, StillAliveEnchEffects.ENERGIZE, Enchantment.enchantment(
				Enchantment.definition(
					registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
					// this is the "weight" or probability of our enchantment showing up in the table
					10,
					// the maximum level of the enchantment
					3,
					// base cost for level 1 of the enchantment, and min levels required for something higher
					Enchantment.dynamicCost(1, 10),
					// same fields as above but for max cost
					Enchantment.dynamicCost(1, 15),
					// anvil cost
					5,
					// valid slots
					EquipmentSlotGroup.HAND
				)
			)
					.withEffect(
						// enchantment occurs POST_ATTACK
						EnchantmentEffectComponents.POST_ATTACK,
						EnchantmentTarget.ATTACKER,
						EnchantmentTarget.VICTIM,
						new EnergizeEnchantmentEffect(LevelBasedValue.perLevel(0.4f, 0.2f)) // scale the enchantment linearly.
					)
		);
	}

	private void register(Entries entries, ResourceKey<Enchantment> key, Enchantment.Builder builder, ResourceCondition... resourceConditions) {
		entries.add(key, builder.build(key.location()), resourceConditions);
	}

	@Override
	public String getName() {
		return "StillAliveEnchGen";
	}
}