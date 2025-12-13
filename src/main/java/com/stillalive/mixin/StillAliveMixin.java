package com.stillalive.mixin;


import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class StillAliveMixin {
	
	@Inject(method = "getMaxStackSize()I", at = @At("RETURN"), cancellable = true)
	private void injected(CallbackInfoReturnable<Integer> cir) {
		if(cir.getReturnValue() == 64)
		{
			cir.setReturnValue(16);
		}
		else
		{
			cir.setReturnValue(cir.getReturnValue());
		}
		

	}
}