package top.alex3236.alphabotany.blocks.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.alex3236.alphabotany.AlphaBotany;
import top.alex3236.alphabotany.blocks.tile.ModTiles;
import top.alex3236.alphabotany.blocks.tile.TileManaCharger;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AlphaBotany.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RendererManaCharger extends TileEntityRenderer<TileManaCharger> {
    public RendererManaCharger(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileManaCharger charger, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!charger.getLevel().isLoaded(charger.getBlockPos()))
            return;

        ItemStack stack = charger.getItemHandler().getStackInSlot(0);
        if (stack.isEmpty())
            return;


//        RenderSystem.pushMatrix();
//        {
        matrixStackIn.pushPose();
        {
//            matrixStackIn.translate(1, 0, 0);
            matrixStackIn.mulPose(Vector3f.XP.rotationDegrees(90));
            matrixStackIn.scale(0.5f, 0.5f, 0.5f);
            matrixStackIn.translate(1, 1, -0.4f);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel ibakedmodel = itemRenderer.getModel(stack, charger.getLevel(), null);
            itemRenderer.render(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
        }
        matrixStackIn.popPose();
        //        }
//        RenderSystem.popMatrix();
    }

    @SubscribeEvent
    static void bindTesr(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntityRenderer(ModTiles.MANA_CHARGER, RendererManaCharger::new);
    }
}