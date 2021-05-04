package com.orangeburrito.collectibles.common.blocks;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.orangeburrito.collectibles.core.ModSounds;
import com.orangeburrito.collectibles.common.entity.SeatEntity;
import com.orangeburrito.collectibles.core.proxy.ClientProxy;
import com.orangeburrito.collectibles.common.util.VoxelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ToiletBlock extends WaterloggedHorizontalBlock {
    public final ImmutableMap<BlockState, VoxelShape> SHAPES;

    public ToiletBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(DIRECTION, Direction.NORTH));
        SHAPES = this.generateShapes(this.getStateContainer().getValidStates());
    }

    private ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states) {
        final VoxelShape[] BACKREST = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(1.5999999999999996, 4.8, 9.6, 14.4, 18.35, 16), Direction.NORTH));
        final VoxelShape[] BASE = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(2.4, 4.8, 1.6, 13.6, 9.6, 10.4), Direction.NORTH));
        final VoxelShape[] LEGS = VoxelHelper.getRotatedShapes(VoxelHelper.rotate(Block.makeCuboidShape(4, 0, 3.2, 12, 4.8, 14.4), Direction.NORTH));

        ImmutableMap.Builder<BlockState, VoxelShape> builder = new ImmutableMap.Builder<>();
        for (BlockState state : states) {
            Direction direction = state.get(DIRECTION);
            List<VoxelShape> shapes = new ArrayList<>();
            shapes.add(BACKREST[direction.getHorizontalIndex()]);
            shapes.add(BASE[direction.getHorizontalIndex()]);
            builder.put(state, VoxelHelper.combineAll(shapes));
        }
        return builder.build();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state);
    }

    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return SHAPES.get(state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result) {
        if (playerEntity.isCrouching()) {
            world.playSound(null, pos, ModSounds.FLUSH, SoundCategory.PLAYERS, 0.8F, 1F);
            System.out.println("flush!");
            return ActionResultType.SUCCESS;
        } else {
            if (ClientProxy.fartKey.isKeyDown()) {
                world.playSound(null, pos, ModSounds.FART, SoundCategory.PLAYERS, 0.4F, 1F);
                System.out.println("fart sound");
            }
            return SeatEntity.create(world, pos, 0.4, playerEntity);
        }
    }
}

