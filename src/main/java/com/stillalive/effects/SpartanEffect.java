package com.stillalive.effects;

//import com.stillalive.StillAlive;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.player.Player;

public class SpartanEffect extends MobEffect {
	public SpartanEffect() {
		// category: StatusEffectCategory - describes if the effect is helpful (BENEFICIAL), harmful (HARMFUL) or useless (NEUTRAL)
		// color: int - Color is the color assigned to the effect (in RGB)
		super(MobEffectCategory.BENEFICIAL, 0xdc0f0f);
	}

	// Called every tick to check if the effect can be applied or not
	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) 
	{
		return true;
	}
	
	@Override
	public void onEffectStarted(LivingEntity ent, int ampl) 
	{
		/*if(ent instanceof Player pl)
		{
			pl.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4.0f+(ampl/10.0f));
		}*/
	}

	// Called when the effect is applied.
	@Override
	public boolean applyEffectTick(ServerLevel world, LivingEntity ent, int amplifier) 
	{
		/*Old code.
		 * if (ent instanceof Player pl 
			&& pl.getEffect(StillAlive.effects.SPARTAN).getDuration() <= 10.0f 
			&& pl.getEffect(StillAlive.effects.SPARTAN).getDuration() != -1) 
		{
			pl.getAttribute(Attributes.ATTACK_SPEED).setBaseValue(4.0f);
		}*/
		

		return super.applyEffectTick(world, ent, amplifier);
	}
}