package folk.sisby.switchy.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import folk.sisby.switchy.SwitchyPlayer;
import folk.sisby.switchy.SwitchyPreset;
import folk.sisby.switchy.SwitchyPresets;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;

import java.util.Objects;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SwitchyCommands {
	public static void InitializeCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(
				literal("switchy")
						.then(literal("help")
								.executes(SwitchyCommands::executeHelp))
						.then(literal("list")
								.executes(SwitchyCommands::executeList))
						.then(literal("new")
								.then(argument("preset", StringArgumentType.word())
										.executes(SwitchyCommands::executeNew)))
						.then(literal("set")
								.then(argument("preset", PresetArgumentType.preset())
										.executes(SwitchyCommands::executeSet)))
						.then(literal("delete")
								.then(argument("preset", PresetArgumentType.preset())
										.executes(SwitchyCommands::executeDelete)))
		));

		// switchy set alias
		CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> dispatcher.register(
				literal("switch")
						.then(argument("preset", PresetArgumentType.preset())
								.executes(SwitchyCommands::executeSet)))
		);
	}

	private static int executeHelp(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();

		inform(player, "Commands: new, set, delete, list");
		inform(player, "/switchy new {name} - create a new preset");
		inform(player, "/switch {name} OR /switchy set {name} - saves current preset and swaps to specified");
		inform(player, "/switchy delete {name} - delete a preset");
		inform(player, "/switchy list - list all created presets");
		return 1;
	}

	private static int executeList(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();

		SwitchyPresets presets = ((SwitchyPlayer) player).switchy$getPresets();
		player.sendMessage(Text.literal("Presets: ").append(Text.literal(Objects.toString(presets, "[]"))), false);
		player.sendMessage(Text.literal("Current Preset: ").append(Text.literal(presets != null ? Objects.toString(presets.getCurrentPreset(), "<None>") : "<None>")), false);
		return 1;
	}



	private static int executeNew(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
		String presetName = context.getArgument("preset", String.class);
		SwitchyPresets presets = validateSwitchyPlayer(player).switchy$getPresets();

		if (presets.addPreset(new SwitchyPreset(presetName))) {
			informWithPreset(player, "Successfully added preset ", presetName);
			return executeSet(context);
		} else {
			informWithPreset(player, "That preset already exists! - try /switchy set ", presetName);
			return 1;
		}
	}

	private static int executeSet(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
		String presetName = context.getArgument("preset", String.class);
		SwitchyPresets presets = validateSwitchyPlayer(player).switchy$getPresets();

		String oldPresetName = Objects.toString(presets.getCurrentPreset(), "<None>");
		if (presets.setCurrentPreset(presetName, true)) {
			informSwitch(player, oldPresetName, presetName);
		} else {
			inform(player, "That preset doesn't exist! /switchy list");
		}
		return 1;
	}

	private static int executeDelete(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().getPlayer();
		String presetName = context.getArgument("preset", String.class);
		SwitchyPresets presets = validateSwitchyPlayer(player).switchy$getPresets();

		if (presets.deletePreset(presetName)) {
			informWithPreset(player, "Preset deleted: ", presetName);
		} else {
			inform(player, "That preset doesn't exist! /switchy list");
		}
		return 1;
	}

	private static SwitchyPlayer validateSwitchyPlayer(ServerPlayerEntity player) {
		if (((SwitchyPlayer) player).switchy$getPresets() == null) {
			((SwitchyPlayer) player).switchy$setPresets(SwitchyPresets.fromNbt(player, new NbtCompound()));
		}
		return (SwitchyPlayer) player;
	}

	private static void informSwitch(ServerPlayerEntity player, String oldPreset, String newPreset) {
		player.sendMessage(Text.literal("You've switched from ")
						.setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
						.append(Text.literal(oldPreset).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
						.append(Text.literal(" to "))
						.append(Text.literal(newPreset).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
				, false);
	}

	private static void informWithPreset(ServerPlayerEntity player, String literal, String preset) {
		player.sendMessage(Text.literal(literal)
						.setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
						.append(Text.literal(preset).setStyle(Style.EMPTY.withColor(Formatting.WHITE)))
				, false);
	}

	private static void inform(ServerPlayerEntity player, String literal) {
		player.sendMessage(Text.literal(literal)
						.setStyle(Style.EMPTY.withColor(Formatting.YELLOW))
				, false);
	}

}
