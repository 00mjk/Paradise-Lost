package net.id.aether.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.aether.api.ConditionAPI;
import net.id.aether.api.MoaAPI;
import net.id.aether.api.MoaAttributes;
import net.id.aether.api.condition.ConditionProcessor;
import net.id.aether.api.condition.Persistance;
import net.id.aether.api.condition.Severity;
import net.id.aether.component.AetherComponents;
import net.id.aether.component.MoaGenes;
import net.id.aether.entities.passive.MoaEntity;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class ConditionCommand {

    public static final ConditionProcessorSuggester REGISTERED_CONDITIONS = new ConditionProcessorSuggester();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("condition")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(argument("target", EntityArgumentType.entities())
                                .then(literal("query")
                                        .then(argument("processor", IdentifierArgumentType.identifier()).suggests(REGISTERED_CONDITIONS)
                                                .executes((context -> printCondition(context.getSource(), EntityArgumentType.getEntities(context, "target"), IdentifierArgumentType.getIdentifier(context, "processor"))))))
                                .then(literal("assign").then(argument("processor", IdentifierArgumentType.identifier()).suggests(REGISTERED_CONDITIONS)
                                        .then(argument("value", FloatArgumentType.floatArg()).then(argument("permanence", StringArgumentType.word())
                                                .executes(context -> setCondition(context.getSource(), EntityArgumentType.getEntity(context, "target"), IdentifierArgumentType.getIdentifier(context, "processor"), FloatArgumentType.getFloat(context, "value"), StringArgumentType.getString(context, "permanence"))))))))
        );
    }

    private static int printCondition(ServerCommandSource source, Collection<? extends Entity> entities, Identifier attributeId) {
        entities.forEach(entity -> {
            if(entity instanceof LivingEntity target) {
                ConditionProcessor condition;

                try {
                    condition = ConditionAPI.getOrThrow(attributeId);
                } catch (NoSuchElementException e) {
                    source.sendError(new LiteralText(e.getMessage()));
                    return;
                }

                var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                var severity = Severity.getSeverity(rawSeverity);

                if (!condition.isExempt(target)) {
                    source.sendFeedback(new TranslatableText("commands.aether.condition.success.query", new TranslatableText(ConditionAPI.getTranslationString(condition)), new TranslatableText(severity.translation), rawSeverity), false);
                }
                else {
                    source.sendError(new TranslatableText("commands.aether.condition.failure", new TranslatableText(ConditionAPI.getTranslationString(condition))));
                }
            }
        });
        return 1;
    }

    private static int setCondition(ServerCommandSource source, Entity entity, Identifier attributeId, float value, String permanence) {
        if(entity instanceof LivingEntity target) {
            ConditionProcessor condition;
            Persistance persistance;

            try {
                condition = ConditionAPI.getOrThrow(attributeId);
                persistance = Persistance.valueOf(permanence);
            } catch (NoSuchElementException e) {
                source.sendError(new LiteralText(e.getMessage()));
                return 0;
            }

            var manager = ConditionAPI.getConditionManager(target);

            if(!condition.isExempt(target)) {
                if(manager.set(attributeId, persistance, value)) {
                    var rawSeverity = ConditionAPI.getConditionManager(target).getScaledSeverity(condition);
                    var severity = Severity.getSeverity(rawSeverity);

                    source.sendFeedback(new TranslatableText("commands.aether.condition.success.assign", new TranslatableText(ConditionAPI.getTranslationString(condition)), new TranslatableText(severity.translation), rawSeverity), false);
                    ConditionAPI.trySync(target);
                }
                else {
                    source.sendError(new LiteralText("...you tried setting a constant condition, didn't you?"));
                }
            }
        }
        return 1;
    }

    public static class ConditionProcessorSuggester implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            AetherRegistries.CONDITION_REGISTRY.getIds().forEach(id -> builder.suggest(id.toString()));
            return builder.buildFuture();
        }
    }

    public static class PermanenceSuggester implements SuggestionProvider<ServerCommandSource> {
        @Override
        public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
            builder.suggest(Persistance.TEMPORARY.name());
            builder.suggest(Persistance.CHRONIC.name());
            return builder.buildFuture();
        }
    }
}
