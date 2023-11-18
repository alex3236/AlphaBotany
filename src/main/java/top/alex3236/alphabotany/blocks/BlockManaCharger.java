package top.alex3236.alphabotany.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import top.alex3236.alphabotany.blocks.tile.TileManaCharger;
import vazkii.botania.api.mana.IManaPool;

import javax.annotation.Nullable;

public class BlockManaCharger extends Block {
    private static final VoxelShape SHAPE = VoxelShapes.create(new AxisAlignedBB((1f / 8) + (1f / 32), (1f / 16) + (1f / 32), (1f / 8) + (1f / 32), 1 - (1f / 8) - (1f / 32), (1f / 16) + (1f / 32) + (1f / 8), 1 - (1f / 8) - (1f / 32)));

    public BlockManaCharger(Block.Properties prop) {
        super(prop);
    }

    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) return ActionResultType.SUCCESS;

        TileEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof TileManaCharger)
            return ((TileManaCharger) tileEntity).handleClick(player, handIn);
        else
            return ActionResultType.SUCCESS;

    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (!stateIn.canSurvive(worldIn, currentPos)) {
            worldIn.getBlockTicks().scheduleTick(currentPos, this, 1);
        }

        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockEntity(pos.below()) instanceof IManaPool;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, World worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            TileEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof TileManaCharger) {
                return ((TileManaCharger) tileEntity).getComparatorOutput();
            }
        }
        return 0;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileManaCharger();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

}
