package com.stillalive.items;

import java.util.function.Function;

import com.stillalive.StillAlive;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;

public class StillAliveItems 
{
	public static final class ToolsMaterial
	{
		public static final ToolMaterial DIY_TOOL = new ToolMaterial(
				BlockTags.INCORRECT_FOR_WOODEN_TOOL, //is this the right tool for drop ?
				4, // durability
				0.5F, // mining speed
				0.1F, // attack bonus
				1, // enchantability (see enchantment "heaviness")
				null //repair material, here null because we don't care !
		);
	}
	
	public static Item register(String name, Function<Item.Properties, Item> itemFactory, Item.Properties settings) 
	{
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(StillAlive.MOD_ID, name));

		// Create the item instance.
		Item item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}
	
	public static final Item HANDLE = register("handle", Item::new, new Item.Properties());
	public static final Item PEBBLE = register("pebble", Item::new, new Item.Properties());
	public static final Item HEART = register("heart", Item::new, new Item.Properties());
	
	public static final Item DIY_AXE = register(
			"diy_axe",
			Item::new,
			new Item.Properties().axe(ToolsMaterial.DIY_TOOL, 0.4f, -2.5f)
	);

	public static final Item DIY_PICKAXE = register(
			"diy_pickaxe",
			Item::new,
			new Item.Properties().pickaxe(ToolsMaterial.DIY_TOOL, 0.4f, -2.5f)
	);
	
	public static final ResourceKey<CreativeModeTab> CUSTOM_ITEM_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(StillAlive.MOD_ID, "item_group"));
	public static final CreativeModeTab CUSTOM_ITEM_GROUP = FabricItemGroup.builder()
			.icon(() -> new ItemStack(HEART))
			.title(Component.translatable("itemGroup.still-alive"))
			.build();
	
	public static void Init()
	{
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_ITEM_GROUP_KEY, CUSTOM_ITEM_GROUP);
		
		ItemGroupEvents.modifyEntriesEvent(CUSTOM_ITEM_GROUP_KEY).register(itemGroup -> {
			itemGroup.accept(HANDLE);
			itemGroup.accept(PEBBLE);
			itemGroup.accept(DIY_AXE);
			itemGroup.accept(DIY_PICKAXE);
			itemGroup.accept(HEART);
		});
	}

}
