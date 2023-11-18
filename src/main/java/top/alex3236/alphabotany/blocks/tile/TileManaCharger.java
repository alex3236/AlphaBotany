package top.alex3236.alphabotany.blocks.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaPool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class TileManaCharger extends TileEntity implements ITickableTileEntity {
    private static final int RATE = 1000;
    private static final Random rand = new Random();
    public int _rotation = rand.nextInt(360);
    private ItemHandler itemHandler;

    public TileManaCharger() {
        super(ModTiles.MANA_CHARGER);
    }

    @Override
    public void tick() {
        World world = getLevel();
        BlockPos pos = getBlockPos();

        if (!(world.getBlockEntity(pos.below()) instanceof IManaPool)) {
            world.destroyBlock(pos, true);
        }

        if (world.isClientSide) return;

        ItemStack stack = getItemHandler().getStackInSlot(0);
        if (stack.isEmpty()) return;

        Item item = stack.getItem();
        if (item instanceof IManaItem) {
            IManaItem manaItem = (IManaItem) item;
            TileEntity tilePool = world.getBlockEntity(pos.below());

            if (tilePool instanceof IManaPool) {
                IManaPool pool = (IManaPool) tilePool;

                if (pool.isOutputtingPower()) {
                    if (manaItem.canReceiveManaFromPool(stack, tilePool) && manaItem.getMana(stack) != manaItem.getMaxMana(stack) && pool.getCurrentMana() > 0) {
                        int mana = Math.min(manaItem.getMaxMana(stack) - manaItem.getMana(stack), RATE);
                        mana = Math.min(pool.getCurrentMana(), mana);
                        pool.receiveMana(-mana);
                        manaItem.addMana(stack, mana);
                        setChanged();
                    }
                } else {
                    if (manaItem.canExportManaToPool(stack, tilePool)) {
                        int currentManaInStack = manaItem.getMana(stack);
                        if (!pool.isFull() && currentManaInStack > 0) {
                            int mana = Math.min(currentManaInStack, RATE);
                            pool.receiveMana(mana);
                            manaItem.addMana(stack, -mana);
                            setChanged();
                        }
                    }
                }

            }
        }
    }

    public ActionResultType handleClick(PlayerEntity playerIn, Hand hand) {
        if (playerIn.isShiftKeyDown()) return ActionResultType.FAIL;

        ItemStack heldItem = playerIn.getItemInHand(hand);
        IItemHandler itemHandler = getItemHandler();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof IManaItem) {
            playerIn.setItemInHand(hand, itemHandler.insertItem(0, heldItem, false));
            return ActionResultType.SUCCESS;
        } else if (heldItem.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(playerIn, itemHandler.extractItem(0, 1, false));
            return ActionResultType.SUCCESS;
        } else return ActionResultType.FAIL;
    }

    public int getComparatorOutput() {
        ItemStack stack = getItemHandler().getStackInSlot(0);
        if (stack.isEmpty())
            return 0;

        IManaItem manaItem = (IManaItem) stack.getItem();
        int currentMana = manaItem.getMana(stack);
        int maxMana = manaItem.getMaxMana(stack);

        if (maxMana < 1 || currentMana < 1) return 1;

        return 1 + (int) ((currentMana / (float) maxMana) * 14) + 1;
    }

    public ItemHandler getItemHandler() {
        if (itemHandler == null) {
            itemHandler = new ItemHandler(this);
        }
        return itemHandler;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), -999, save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        load(getLevel().getBlockState(pkt.getPos()), pkt.getTag());
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        return getItemHandler().write(super.save(compound));
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        getItemHandler().read(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return LazyOptional.of(() -> (T) getItemHandler());
        return super.getCapability(cap, side);
    }

    public static class ItemHandler implements IItemHandler {
        private final TileManaCharger tile;
        private ItemStack item = ItemStack.EMPTY;

        public ItemHandler(TileManaCharger tile) {
            this.tile = tile;
        }

        @Override
        public int getSlots() {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return item;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (isItemValid(slot, stack)) {
                if (!item.isEmpty())
                    return stack;
                else {
                    ItemStack ret = stack.copy();
                    ret.shrink(1);
                    if (!simulate) {
                        ItemStack toInsert = stack.copy();
                        toInsert.setCount(1);
                        item = toInsert;
                        onContentChanged();
                    }
                    return ret;
                }
            }
            return stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (item.isEmpty())
                return ItemStack.EMPTY;
            else {
                ItemStack ret = item;
                if (!simulate) {
                    item = ItemStack.EMPTY;
                    onContentChanged();
                }
                return ret;
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof IManaItem;
        }

        private CompoundNBT write(CompoundNBT nbt) {
            nbt.put("Item", item.serializeNBT());
            System.out.println("write " + nbt);
            return nbt;
        }

        private void read(CompoundNBT nbt) {
            item = ItemStack.of(nbt.getCompound("Item"));
            System.out.println("read " + nbt + ' ' + item);
        }

        private void onContentChanged() {
            tile.setChanged();
            VanillaPacketDispatcher.dispatchTEToNearbyPlayers(tile);
        }
    }
}
