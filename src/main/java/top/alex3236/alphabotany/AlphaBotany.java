package top.alex3236.alphabotany;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.alex3236.alphabotany.blocks.ModBlocks;
import top.alex3236.alphabotany.blocks.tile.ModTiles;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("alphabotany")
public class AlphaBotany {
    // Directly reference a log4j logger.
    public static final String MOD_ID = "alphabotany";
    private static final Logger LOGGER = LogManager.getLogger();

    public static ItemGroup itemGroup = new AlphaBotanyGroup();

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().tab(itemGroup);
    }

    public AlphaBotany() {
//        // Register the setup method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        // Register the enqueueIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        // Register the processIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//        // Register the doClientStuff method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, ModBlocks::registerItemBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, ModBlocks::registerBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, ModTiles::registerTiles);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }


//    private void setup(final FMLCommonSetupEvent event)
//    {
//        // some preinit code
//        LOGGER.info("HELLO FROM PREINIT");
//        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
//    }
//
//    private void doClientStuff(final FMLClientSetupEvent event) {
//        // do something that can only be done on the client
//        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().options);
//    }
//
//    private void enqueueIMC(final InterModEnqueueEvent event)
//    {
//        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo("alphabotany", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
//    }
//
//    private void processIMC(final InterModProcessEvent event)
//    {
//        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
//    }
//    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(FMLServerStartingEvent event) {
//        // do something when the server starts
//        LOGGER.info("HELLO from server starting");
//    }
//
//    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
//    // Event bus for receiving Registry Events)
//    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
//    public static class RegistryEvents {
//        @SubscribeEvent
//        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
//            // register a new block here
//            LOGGER.info("HELLO from Register Block");
//        }
//    }
}
