package top.alex3236.alphabotany;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static top.alex3236.alphabotany.AlphaBotany.defaultBuilder;
import static top.alex3236.alphabotany.blocks.ModBlocks.*;

public class AlphaBotanyGroup extends ItemGroup {

    NonNullList<ItemStack> list;

    public AlphaBotanyGroup() {
        super("alphabotany");
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(Item.byBlock(manaGenerator));
    }

    @Override
    public boolean hasSearchBar() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemList(NonNullList<ItemStack> list) {
        this.list = list;
        addBlock(manaGenerator);
        addBlock(quantumManaBuffer);
        addBlock(manaCharger);
    }


    private void addBlock(Block block) {
        new BlockItem(block, defaultBuilder()).fillItemCategory(this, list);
    }

}
