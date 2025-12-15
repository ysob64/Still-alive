package com.stillalive.events;

import org.joml.Math;

import com.stillalive.StillAlive;

import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.network.;

import net.theblackcat.endurance.status_effects.EnduranceStatusEffects;

//Override because it change some in game behavior, even if thats not a mixin...
public class OverrideEvents
{
	@SuppressWarnings("unchecked")
	public static void RegisterEvents()
	{
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			BlockState state = world.getBlockState(pos);
			MobEffectInstance noMining = new MobEffectInstance(StillAlive.effects.CANNOT_MINE, -1, 0, true, false, false);
			Block currentBlock = state.getBlock();
			ItemStack i = player.getMainHandItem();
			
			player.getAttribute(Attributes.MINING_EFFICIENCY).setBaseValue(-1.0f);
			
			boolean tools = (i.is(ItemTags.PICKAXES) || i.is(ItemTags.SHOVELS) || i.is(ItemTags.SWORDS) || i.is(ItemTags.HOES) || i.is(ItemTags.AXES));
			boolean isHolding = !player.getMainHandItem().isEmpty();
			
			if(player.gameMode() == GameType.CREATIVE)
			{
				if(player.hasEffect(StillAlive.effects.CANNOT_MINE))
				{
					player.removeEffect(StillAlive.effects.CANNOT_MINE);
				}
				return InteractionResult.PASS;
			}
			
			if(i.isCorrectToolForDrops(state) || (currentBlock == Blocks.DIRT
					|| currentBlock == Blocks.GRASS_BLOCK
					|| currentBlock == Blocks.SAND
					|| currentBlock == Blocks.GRAVEL
					|| currentBlock == Blocks.HAY_BLOCK)
					|| state.getBlock().defaultDestroyTime() <= 0.5f
			)
			{
				if(player.hasEffect(StillAlive.effects.CANNOT_MINE))
				{
					player.getAttribute(Attributes.BLOCK_BREAK_SPEED).setBaseValue(1);
					player.removeEffect(StillAlive.effects.CANNOT_MINE);
				}
				return InteractionResult.PASS;
			}
			
			if (world instanceof ServerLevel serverWorld && !tools)
			{
				player.addEffect(noMining);
				if(!isHolding) player.hurtServer(serverWorld, world.damageSources().generic(), 2.0F);
				
				return InteractionResult.PASS;
			}
			else if(isHolding && state.requiresCorrectToolForDrops() && world instanceof ServerLevel)
			{	
				if(!player.hasEffect(StillAlive.effects.CANNOT_MINE))
				{
					player.addEffect(noMining);
				}
				
				if(tools)
				{
					if(i.nextDamageWillBreak())
					{
						i.hurtAndBreak(1, player, player.getUsedItemHand());
					}
						
					i.setDamageValue(i.getDamageValue() + 2);
				}
				
				return InteractionResult.PASS;
			}

			return InteractionResult.PASS;
		});
		
		//This event is in the case where player bypassed the mining fatigue, so we prevent the block from breaking
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			ItemStack i = player.getMainHandItem();
			if((i.isCorrectToolForDrops(state) 
					|| !state.requiresCorrectToolForDrops()) 
					|| (player.gameMode() == GameType.CREATIVE)
					|| (state.getBlock().defaultDestroyTime() <= 0.5f))
			{
				return true;
			}
			else
			{
				return false;
			}
		});
		
	    ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
	        if (entity instanceof Player && amount >= entity.getHealth() 
	        	&& entity.hasEffect(EnduranceStatusEffects.ENDURANCE) && !entity.hasEffect(EnduranceStatusEffects.DEEP_WOUND))
	        {
	        	entity.addEffect(new MobEffectInstance(EnduranceStatusEffects.DEEP_WOUND, 1200, 2, true, false, false));
	        	entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1200, 1, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 1200, 2, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 1200, 10, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 1200, 2, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 1200, 2, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1200, 5, true, false, false));
	        	return false;
	        }
	        return true;
	    });
	    
	    
	    ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, base, taken, blocked) -> {
	    	if(entity instanceof Player) { //Global if
	    	
	    	if(entity.getHealth() <= 4.0f && !entity.hasEffect(EnduranceStatusEffects.DEEP_WOUND))
	    	{
	    		entity.addEffect(new MobEffectInstance(EnduranceStatusEffects.ENDURANCE, 500, 0, true, false, false));
	       		entity.addEffect(new MobEffectInstance(MobEffects.SPEED, 500, 1, true, false, false));
	       		entity.addEffect(new MobEffectInstance(MobEffects.HUNGER, 500, 1, true, false, false));
	       		entity.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 500, 1, true, false, false));
	       		
	       		//do not return since other damage effect can still apply
	    	}
	    		
	    	if(blocked)
	    	{
	    		//preventing abuse of the shield, maybe too op in pve only (but actually good at pvp)
	    		entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, true, false, false));
	    		return;
	    	}
	    	
	    	if (source.is(DamageTypes.FALL)) 
	    	{
	    		ItemStack boots = ((Player)entity).getItemBySlot(EquipmentSlot.FEET);
	    		
	    		String fl = ((Player)entity).level().registryAccess().getOrThrow(Enchantments.FEATHER_FALLING).value().toString();
	    		int flLevel = 0;
	    		Holder<Enchantment> STOPTHISTORTURE = null;
	    		Object[] ench = boots.getEnchantments().keySet().toArray();
	    		
	    		for(Object enchant : ench)
	    		{
	    			if(enchant.toString().contains(fl))
	    			{
	    				STOPTHISTORTURE = (Holder<Enchantment>) enchant;
	    				break;
	    			}
	    		}
	    			
	    		if(STOPTHISTORTURE != null)
	    		{
	    			flLevel = boots.getEnchantments().getLevel(STOPTHISTORTURE);
	    		}
	    		
	    		if(flLevel == 4)
	    		{
	    			return;
	    		}
	    		
	    		if(taken >= 8.0f)
	            {
	            	int doEnchFl = flLevel > 0 ? 3-flLevel : 2;
	    			entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 1800, doEnchFl, true, false, false));
	            }
	            else if(taken >= 3.0f && taken < 8.0f && flLevel < 3)
	            {
	            	int doEnchFl = flLevel > 0 ? 2-flLevel : 1;
	            	entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int)taken*60, doEnchFl, true, false, false));
	            }
	            else if(taken < 3.0f && flLevel == 0)
	            {
	                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int)taken*60, 0, true, false, false));
	            }
	    		
	    		return; // :]
	        }
	    	
	    	if ((source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.PLAYER_EXPLOSION)))
	    	{
	    		//making sure that armor is not inferior to one
	    		float armor = ((Player)entity).getArmorValue() < 1.0f ? 1.0f : ((Player)entity).getArmorValue();
	    		
	    		if(armor < 12.0f)
	    		{
	        		entity.addEffect(new MobEffectInstance(EnduranceStatusEffects.DEEP_WOUND, 250, 0, true, false, false));
	    		}
	    		
	    		armor /= 10;
	    		entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, (int)(600/armor), 1, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, (int)(600/armor), 1, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, (int)(200/armor), 0, true, false, false));
	    		
	    		return;
	    	}
	    	
	    	if((source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) 
	    		|| source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)))
	    	{
	    		float armor = ((Player)entity).getArmorValue() < 1.0f ? 1.0f : ((Player)entity).getArmorValue();
	    		float prob = (0.1f*(10.0f-armor/2.0f)+0.01f); //So you end up having 1% of chance to getting infected with the full armor bar
	    		
	    		entity.addEffect(new MobEffectInstance(MobEffects.SPEED, 60, 0, true, false, false));
	    		if(Math.random() <= prob)
	    		{
	    			entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 1, true, false, false));
	    		}
	    		
	    		return;
	    	}
	    	
	    	if(source.is(DamageTypes.ON_FIRE))
	    	{
	    		entity.setRemainingFireTicks(4000);
	    		return;
	    	}
	    	
	    	if(source.is(DamageTypes.LAVA))
	    	{
	    		//Not sure if thats really safe
	    		ServerLevel lvl = (ServerLevel)((Player)entity).level();
	    		entity.hurtServer(lvl, source, 10.0f);
	    		return;
	    	}

	    	if(source.is(DamageTypes.ARROW) || source.is(DamageTypes.TRIDENT))
	    	{
	    		if(Math.random() < 0.21f)
	    		{
	    			entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 1, true, false, false));
	    		}
	    		return;
	    	}
	    	
	    	if(source.is(DamageTypes.STARVE))
	    	{
	    		entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 400, 0, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 400, 0, true, false, false));
	    		entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 400, 2, true, false, false));
	    		return;
	    	}
	    	
	    	}//End Global if
	    	
	    });
	    
	    EntitySleepEvents.ALLOW_RESETTING_TIME.register((pl) -> {
	    	return false;
	    });
	    
	    EntitySleepEvents.START_SLEEPING.register((entity, pos) -> {
	    	int plCount=0;
	    	Player pl=null;
	    	
	    	if(entity instanceof Player)
	    	{
	    		pl = (Player)entity;
	    		plCount = pl.level().getServer().getPlayerList().getPlayerCount();
	    	}
	    	else
	    	{
	    		return;
	    	}
	    	
	    	if(plCount == 1)
	    	{
	    		pl.removeAllEffects(); //because they end up being at 0:00 forever because of the sudden tick rate change
	    		pl.level().tickRateManager().setTickRate(200);
	    	}
	    	else if (plCount > 1)
	    	{
	    		int plAsleep=0;
	    		for(ServerPlayer player : pl.level().getServer().getPlayerList().getPlayers())
	    		{
	    			if(player.isSleeping()) plAsleep++;
	    		}
	    		
	    		if(plAsleep==plCount)
	    		{
	    			pl.removeAllEffects();
	    			pl.level().tickRateManager().setTickRate(200);
	    		}
	    	}
	    });
	    
	    EntitySleepEvents.STOP_SLEEPING.register((entity, pos) -> {
	    	if(entity instanceof Player pl)
	    	{
	    		pl.level().tickRateManager().setTickRate(20);
	    		
	    		if(Math.random() <= 0.15f && pl.level().getDayTime() >= 23450)
	    		{
	    			pl.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0, false, false, false));
	    			pl.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 400, 0, false, false, false));
	    		}
	    		else if(Math.random() <= 0.20f && pl.level().getDayTime() >= 23450)
	    		{
	    			pl.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 400, 0, false, false, false));
	    			pl.addEffect(new MobEffectInstance(MobEffects.SPEED, 400, 0, false, false, false));
	    		}
	    	}
	    });
	}

}
