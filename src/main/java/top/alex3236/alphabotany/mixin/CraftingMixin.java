package top.alex3236.alphabotany.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.alex3236.alphabotany.ConfigHandler;
import top.alex3236.alphabotany.blocks.ModBlocks;

import java.util.Optional;

@Mixin(WorkbenchContainer.class)
public class CraftingMixin {
    @Inject(at = @At("HEAD"), method = "slotChangedCraftingGrid", cancellable = true)
    private static void onUpdateCraftingGrid(int p_217066_0_, World world, PlayerEntity player, CraftingInventory craftingInventory, CraftResultInventory craftResultInventory, CallbackInfo ci) {
        if (!world.isClientSide) {
            ServerPlayerEntity serverplayer = (ServerPlayerEntity) player;
            ItemStack craftResult = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftingInventory, world);
            if (optional.isPresent()) {
                ICraftingRecipe craftingrecipe = optional.get();
                if (craftResultInventory.setRecipeUsed(world, serverplayer, craftingrecipe)) {
                    craftResult = craftingrecipe.assemble(craftingInventory);
                }
            }

            if (craftResult.isEmpty()) return;

            if (craftResult.getItem() instanceof BlockItem) {
                Block block = ((BlockItem) craftResult.getItem()).getBlock();
                if (block == ModBlocks.manaGenerator && !ConfigHandler.COMMON.generatorEnabled.get()
                        || block == ModBlocks.quantumManaBuffer && !ConfigHandler.COMMON.bufferEnabled.get()
                        || block == ModBlocks.manaCharger && !ConfigHandler.COMMON.chargerEnabled.get()
                ) {
                    ci.cancel();
                    craftResultInventory.setItem(0, ItemStack.EMPTY);
                }
            }
        }
    }
}
