package top.alex3236.alphabotany.blocks.tile;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import top.alex3236.alphabotany.ConfigHandler;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.common.block.tile.TileMod;
import vazkii.botania.common.block.tile.mana.TileSpreader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileManaGenerator extends TileMod implements ITickableTileEntity, IManaReceiver, ISparkAttachable {
    private static final String TAG_MANA = "mana";
    private static final String TAG_ENERGY = "energy";
    int mana;
    public int energy = 0;
    private static final int MAX_ENERGY = ConfigHandler.COMMON.mgMaxEnergy.get();

    private final IEnergyStorage energyHandler = new IEnergyStorage() {
        @Override
        public int getEnergyStored() {
            return energy;
        }

        @Override
        public int getMaxEnergyStored() {
            return MAX_ENERGY;
        }

        @Override
        public boolean canExtract() {
            return getEnergyStored() > 0;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int available = this.getEnergyStored();
            if (!simulate)
                energy = Math.max(this.getEnergyStored() - maxExtract, 0);
            return Math.min(maxExtract, available);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int space = getMaxEnergyStored() - this.getEnergyStored();
            if (!simulate)
                energy = Math.min(this.getEnergyStored() + maxReceive, getMaxEnergyStored());
            return Math.min(space, maxReceive);
        }

        @Override
        public boolean canReceive() {
            return getEnergyStored() < getMaxEnergyStored();
        }
    };

    private final LazyOptional<IEnergyStorage> energyStorageLazyOptional = LazyOptional.of(() -> energyHandler);

    public TileManaGenerator() {
        super(ModTiles.MANA_GENERATOR);
    }

//    @Override
//    public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable Direction side) {
//        return cap == CapabilityEnergy.ENERGY || super.getCapability(cap, side).isPresent();
//    }

    @Override
    @Nullable
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return energyStorageLazyOptional.cast();
        } else
            return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        int speed = ConfigHandler.COMMON.mgTransferSpeed.get();

//        System.out.println(speed);

        for (Direction e : Direction.values()) {
            BlockPos neighbor = getBlockPos().offset(e.getNormal());
            if (!getLevel().isLoaded(neighbor))
                continue;

            TileEntity te = getLevel().getBlockEntity(neighbor);
            if (te == null)
                continue;

            IEnergyStorage storage = null;

            if (te.getCapability(CapabilityEnergy.ENERGY, e.getOpposite()).isPresent()) {
                storage = te.getCapability(CapabilityEnergy.ENERGY, e.getOpposite()).resolve().get();
            } else if (te.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {
                storage = te.getCapability(CapabilityEnergy.ENERGY, null).resolve().get();
            }

            if (storage != null) {
                recieveEnergy(storage.extractEnergy(1000, false));
            }

            if (te instanceof TileSpreader) {
                TileSpreader p = (TileSpreader) te;
                if (getCurrentMana() >= speed && p.getCurrentMana() < p.getMaxMana()) {
                    int current = Math.min(speed, p.getMaxMana() - p.getCurrentMana());
                    p.receiveMana(current);
                    receiveMana(-current);
                }
            }
        }

        if (energy >= 1000) {
            recieveEnergy(-1000);
            receiveMana(ConfigHandler.COMMON.mgConvert.get());
        }

    }

    @Override
    public boolean isFull() {
        return energy >= MAX_ENERGY;
    }

    @Override
    public boolean canReceiveManaFromBursts() {
        return true;
    }

    public void recieveEnergy(int mana) {
        this.energy = Math.min(MAX_ENERGY, this.energy + mana);
    }

    @Override
    public void receiveMana(int mana) {
        this.mana = Math.min(1000000, this.mana + mana);
    }

    @Override
    public int getCurrentMana() {
        return mana;
    }

    @Override
    public void writePacketNBT(CompoundNBT cmp) {
        cmp.putInt(TAG_MANA, mana);
        cmp.putInt(TAG_ENERGY, energy);
    }

    @Override
    public void readPacketNBT(CompoundNBT cmp) {
        mana = cmp.getInt(TAG_MANA);
        energy = cmp.getInt(TAG_ENERGY);
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return false;
    }

    @Override
    public void attachSpark(ISparkEntity arg0) {
    }

    @Override
    public boolean canAttachSpark(ItemStack arg0) {
        return true;
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List sparks = getLevel().getEntitiesOfClass(Entity.class, new AxisAlignedBB(getBlockPos().above(), getBlockPos().above().offset(1, 1, 1)),
                Predicates.instanceOf(ISparkEntity.class));
        if (sparks.size() == 1) {
            Entity e = (Entity) sparks.get(0);
            return (ISparkEntity) e;
        }
        return null;
    }

    @Override
    public int getAvailableSpaceForMana() {
        int space = Math.max(0, 1000000 - getCurrentMana());
        if (space > 0)
            return space;
        else
            return 0;
    }
}
