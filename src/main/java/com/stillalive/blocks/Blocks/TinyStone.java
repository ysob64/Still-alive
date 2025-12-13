package com.stillalive.blocks.Blocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TinyStone extends VegetationBlock
{
	public static final MapCodec<TinyStone> CODEC = simpleCodec(TinyStone::new);
	
	private static final VoxelShape SHAPE = Block.column(3.0, 0.0, 2.0).move(new Vec3(0.3,0,0.015));
	
	public TinyStone(BlockBehaviour.Properties properties) 
	{
		super(properties);
	}
	
	@Override
	protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) 
	{
		return SHAPE;
	}
	
	@Override
	public MapCodec<TinyStone> codec() 
	{
		return CODEC;
	}

}