package top.alex3236.alphabotany.blocks.tile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static top.alex3236.alphabotany.blocks.ModBlocks.*;

public class ModTiles {

    public static final TileEntityType<TileManaGenerator> MANA_GENERATOR = TileEntityType.Builder.of(TileManaGenerator::new, manaGenerator).build(null);

    public static final TileEntityType<TileQuantumManaBuffer> QUANTUM_MANA_BUFFER = TileEntityType.Builder.of(TileQuantumManaBuffer::new, quantumManaBuffer).build(null);

    public static final TileEntityType<TileManaCharger> MANA_CHARGER = TileEntityType.Builder.of(TileManaCharger::new, manaCharger).build(null);


    public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> evt) {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
        register(r, "mana_generator", MANA_GENERATOR);
        register(r, "quantum_manabuffer", QUANTUM_MANA_BUFFER);
        register(r, "mana_charger", MANA_CHARGER);

    }
}
