package folk.sisby.switchy.presets;

import com.mojang.brigadier.StringReader;
import com.mojang.datafixers.util.Pair;
import folk.sisby.switchy.api.exception.InvalidWordException;
import folk.sisby.switchy.api.module.presets.SwitchyDisplayPreset;
import folk.sisby.switchy.client.api.SwitchySwitchScreenPosition;
import folk.sisby.switchy.client.api.module.SwitchyDisplayModule;
import folk.sisby.switchy.client.api.module.SwitchyDisplayModuleRegistry;
import io.wispforest.owo.ui.core.Component;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sisby folk
 * @see SwitchyDisplayPreset
 * @since 1.9.1
 */
@ClientOnly
public class SwitchyDisplayPresetImpl extends SwitchyPresetDataImpl<SwitchyDisplayModule> implements SwitchyDisplayPreset {
	/**
	 * Constructs an instance of the object.
	 *
	 * @param name    the desired name for the new preset.
	 * @param modules the enabled status of modules from the display presets object.
	 * @throws InvalidWordException when the specified preset name is not a word ({@link StringReader#isAllowedInUnquotedString(char)}).
	 */
	public SwitchyDisplayPresetImpl(String name, Map<Identifier, Boolean> modules) throws InvalidWordException {
		super(name, modules, SwitchyDisplayModuleRegistry::supplyModule);
	}

	@Override
	public Map<Identifier, Pair<Component, SwitchySwitchScreenPosition>> getDisplayComponents() {
		Map<Identifier, Pair<Component, SwitchySwitchScreenPosition>> map = new HashMap<>();
		getModules().forEach((id, module) -> {
			@Nullable Pair<Component, SwitchySwitchScreenPosition> component = module.getDisplayComponent();
			if (component != null) {
				map.put(id, component);
			}
		});
		return map;
	}
}
