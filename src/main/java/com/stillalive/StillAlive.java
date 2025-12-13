package com.stillalive;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import com.stillalive.blocks.StillAliveBlocks;
import com.stillalive.effects.SpartanEffect;
import com.stillalive.enchantment.StillAliveEnchEffects;
import com.stillalive.events.OverrideEvents;
import com.stillalive.items.StillAliveItems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class StillAlive implements ModInitializer 
{
	public static final String MOD_ID = "still-alive";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	private long t = System.currentTimeMillis();
	
	
	public static final class effects
	{
		
		
		public static final Holder<MobEffect> SPARTAN =
				Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, 
						ResourceLocation.fromNamespaceAndPath(MOD_ID, "spartan_rage"), 
						//Basically the same as haste, except that it does apply to melee cooldown too.
						new SpartanEffect().addAttributeModifier(
								Attributes.ATTACK_SPEED, 
								ResourceLocation.fromNamespaceAndPath(MOD_ID, "spartan_rage") , 
								0.1F, 
								AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		
		public static final Holder<MobEffect> CANNOT_MINE =
				Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, 
						ResourceLocation.fromNamespaceAndPath(MOD_ID, "disable_mining"), 
						new SpartanEffect().addAttributeModifier(
								Attributes.BLOCK_BREAK_SPEED, 
								ResourceLocation.fromNamespaceAndPath(MOD_ID, "disable_mining") , 
								-1, 
								AttributeModifier.Operation.ADD_VALUE));
	}

	@Override
	public void onInitialize() 
	{	
		
		new effects(); //class creation for loading effects, I have very few of them so it feels "useless" to create a whole file for this
		
		StillAliveEnchEffects.registerModEnchantmentEffects();
		
		OverrideEvents.RegisterEvents();
		
		StillAliveItems.Init();
		StillAliveBlocks.Init();
		
        ResourceKey<PlacedFeature> DEAD_BUSH_PATCH = ResourceKey.create( Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "dead_bush_patched"));
        ResourceKey<PlacedFeature> PEBBLE_PATCH = ResourceKey.create( Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MOD_ID, "pebble_patch"));

        
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),  // apply to all biomes
                GenerationStep.Decoration.VEGETAL_DECORATION,
                DEAD_BUSH_PATCH
        );
        
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),  // apply to all biomes
                GenerationStep.Decoration.VEGETAL_DECORATION,
                PEBBLE_PATCH
        );
		
        
        LOGGER.info("Loading time : " + (System.currentTimeMillis()-t) + " ms");
		LOGGER.info("Still Alive !");
	}
}