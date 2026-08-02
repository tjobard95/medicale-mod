package com.example.medicalmod.command;

import com.example.medicalmod.config.MedicalModConfig;
import com.example.medicalmod.injury.InjuryManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * /medicalmod ... : reglage du systeme de blessures sans recompiler ni relancer.
 */
public final class MedicalModCommand {

    private MedicalModCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess access,
                                CommandManager.RegistrationEnvironment environment) {

        dispatcher.register(CommandManager.literal("medicalmod")
                .then(CommandManager.literal("info")
                        .executes(ctx -> info(ctx.getSource())))
                .then(CommandManager.literal("reload")
                        .requires(s -> s.hasPermissionLevel(2))
                        .executes(ctx -> reload(ctx.getSource())))
                .then(CommandManager.literal("injuries")
                        .requires(s -> s.hasPermissionLevel(2))
                        .then(CommandManager.literal("on").executes(ctx -> toggle(ctx.getSource(), true)))
                        .then(CommandManager.literal("off").executes(ctx -> toggle(ctx.getSource(), false))))
                .then(CommandManager.literal("difficulty")
                        .requires(s -> s.hasPermissionLevel(2))
                        .then(CommandManager.argument("multiplier", FloatArgumentType.floatArg(0.0F, 3.0F))
                                .executes(ctx -> setMultiplier(ctx.getSource(),
                                        FloatArgumentType.getFloat(ctx, "multiplier")))))
                .then(CommandManager.literal("heal")
                        .requires(s -> s.hasPermissionLevel(2))
                        .executes(ctx -> heal(ctx.getSource()))));
    }

    private static int info(ServerCommandSource source) {
        MedicalModConfig c = MedicalModConfig.get();
        source.sendFeedback(() -> Text.literal("Blessures : ")
                .append(Text.literal(c.injuriesEnabled ? "activees" : "desactivees")
                        .formatted(c.injuriesEnabled ? Formatting.GREEN : Formatting.RED)), false);
        source.sendFeedback(() -> Text.literal("  difficulte (multiplicateur) : " + c.globalChanceMultiplier), false);
        source.sendFeedback(() -> Text.literal("  max simultanees : " + c.maxSimultaneousInjuries
                + "  ·  repit : " + c.injuryCooldownSeconds + " s"), false);
        source.sendFeedback(() -> Text.literal(String.format(
                "  fracture : des %.0f degats de chute, %.0f%% par point (max %.0f%%)",
                c.fallDamageThreshold, c.fallChancePerPoint * 100, c.fallChanceMax * 100)), false);
        source.sendFeedback(() -> Text.literal(String.format(
                "  saignement : des %.0f degats, %.0f%%  ·  infection : %.0f%%",
                c.bleedDamageThreshold, c.bleedChance * 100, c.infectionChance * 100)), false);
        return 1;
    }

    private static int reload(ServerCommandSource source) {
        MedicalModConfig.load();
        source.sendFeedback(() -> Text.literal("Config Medical Mod rechargee.").formatted(Formatting.GREEN), true);
        return 1;
    }

    private static int toggle(ServerCommandSource source, boolean enabled) {
        MedicalModConfig.get().injuriesEnabled = enabled;
        MedicalModConfig.save();
        source.sendFeedback(() -> Text.literal("Blessures " + (enabled ? "activees" : "desactivees"))
                .formatted(enabled ? Formatting.GREEN : Formatting.YELLOW), true);
        return 1;
    }

    private static int setMultiplier(ServerCommandSource source, float value) {
        MedicalModConfig.get().globalChanceMultiplier = value;
        MedicalModConfig.save();
        source.sendFeedback(() -> Text.literal(String.format(
                "Difficulte des blessures : x%.2f (1.0 = normal, 0.5 = deux fois moins)", value))
                .formatted(Formatting.GREEN), true);
        return 1;
    }

    /** Soigne toutes les blessures du joueur qui tape la commande. */
    private static int heal(ServerCommandSource source) {
        try {
            ServerPlayerEntity player = source.getPlayerOrThrow();
            InjuryManager.clear(player);
            source.sendFeedback(() -> Text.literal("Toutes tes blessures sont soignees.")
                    .formatted(Formatting.GREEN), false);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
}
