package top.alex3236.alphabotany.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import top.alex3236.alphabotany.AlphaBotany;

public class ModBlocks {
    public static final Block manaGenerator = new BlockManaGenerator(AbstractBlock.Properties.of(Material.STONE));
    public static final Block quantumManaBuffer = new BlockQuantumManaBuffer(AbstractBlock.Properties.of(Material.STONE));

    public static final Block manaCharger = new BlockManaCharger(AbstractBlock.Properties.of(Material.WOOD).
            sound(SoundType.WOOD).
            harvestTool(ToolType.AXE).
            strength(1)
    );

    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        IForgeRegistry<Block> r = evt.getRegistry();
        register(r, "mana_generator", manaGenerator);
        register(r, "quantum_manabuffer", quantumManaBuffer);
        register(r, "mana_charger", manaCharger);
    }

    public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
//        r.register(manaGenerator);
        Item.Properties props = AlphaBotany.defaultBuilder();
        register(r, Registry.BLOCK.getKey(manaGenerator), new BlockItem(manaGenerator, props));
        register(r, Registry.BLOCK.getKey(quantumManaBuffer), new BlockItem(quantumManaBuffer, props));
        register(r, Registry.BLOCK.getKey(manaCharger), new BlockItem(manaCharger, props));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing) {
        register(reg, prefix(name), thing);
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation("alphabotany", path);
    }
}
