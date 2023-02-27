package folk.sisby.switchy.presets;

import folk.sisby.switchy.api.module.presets.SwitchyDisplayPreset;
import folk.sisby.switchy.api.module.presets.SwitchyDisplayPresets;
import folk.sisby.switchy.client.SwitchyClient;
import folk.sisby.switchy.client.api.module.SwitchyDisplayModule;
import folk.sisby.switchy.client.api.module.SwitchyDisplayModuleRegistry;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.minecraft.ClientOnly;

import java.util.HashMap;

/**
 * @author Sisby folk
 * @see SwitchyDisplayPresets
 * @since 1.9.1
 */
@ClientOnly
public class SwitchyDisplayPresetsImpl extends SwitchyPresetsDataImpl<SwitchyDisplayModule, SwitchyDisplayPreset> implements SwitchyDisplayPresets {
	/**
	 * Returns an empty display presets object
	 */
	public SwitchyDisplayPresetsImpl() {
		super(new HashMap<>(), SwitchyDisplayPresetImpl::new, SwitchyDisplayModuleRegistry::supplyModule, true, SwitchyClient.LOGGER);
	}

	@Override
	void toggleModulesFromNbt(NbtList list, Boolean enabled, Boolean silent) {
		// Don't Log. Don't check for existence. `modules` is expected to be desync'd from the DisplayModules.
		list.forEach((e) -> {
			Identifier id;
			if (e instanceof NbtString s && (id = Identifier.tryParse(s.asString())) != null) {
				getModules().put(id, enabled);
			}
		});
	}
}
