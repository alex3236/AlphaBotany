package top.alex3236.alphabotany.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import top.alex3236.alphabotany.blocks.tile.TileManaGenerator;
import vazkii.botania.common.block.BlockMod;

import javax.annotation.Nullable;

public class BlockManaGenerator extends BlockMod implements ITileEntityProvider {

    public BlockManaGenerator(Properties builder) {
        super(builder);
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader iBlockReader) {
        return new TileManaGenerator();
    }
}
