package top.alex3236.alphabotany;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ConfigHandler {
    public static class Common {
        public final ForgeConfigSpec.BooleanValue generatorEnabled;
        public final ForgeConfigSpec.BooleanValue bufferEnabled;

        public final ForgeConfigSpec.BooleanValue chargerEnabled;



        public final ForgeConfigSpec.ConfigValue<Integer> mgMaxEnergy;

        public final ForgeConfigSpec.ConfigValue<Integer> mgConvert;

        public final ForgeConfigSpec.ConfigValue<Integer> mgTransferSpeed;


        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("mana_generator");
            generatorEnabled = builder
                    .comment("Whether to enable Flux Manafield")
                    .define("craftingAllowed", true);
            mgMaxEnergy = builder
                    .comment("Max Energy Stored")
                    .define("mgMaxEnergy", 40000);
            mgConvert = builder
                    .comment("1000 FE converts to how much. Default is 99")
                    .define("mgConvert", 99);
            mgTransferSpeed = builder
                    .comment("the speed it transfer mana to spreader")
                    .define("mgTransferSpeed", 200);
            builder.pop();
            builder.push("quantum_manabuffer");
            bufferEnabled = builder
                    .comment("Whether to enable Quantum Mana Buffer")
                    .define("craftingAllowed", true);
            builder.pop();
            builder.push("mana_charger");
            chargerEnabled = builder
                    .comment("Whether to enable Mana Charger")
                    .define("craftingAllowed", true);
            builder.pop();
        }

    }

    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }


}
