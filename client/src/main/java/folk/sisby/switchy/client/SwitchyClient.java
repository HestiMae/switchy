package folk.sisby.switchy.client;

import folk.sisby.switchy.Switchy;
import folk.sisby.switchy.client.api.SwitchyClientEvents;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mod initializer for client-side Switchy Client.
 *
 * @author Sisby folk
 * @since 1.7.0
 */
public class SwitchyClient implements ClientModInitializer {
	/**
	 * the Switchy Client namespace.
	 */
	public static final String ID = "switchy_client";
	/**
	 * the Switchy Client logger.
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(Switchy.ID + "-client");
	/**
	 * the path to export presets to, relative to .minecraft.
	 */
	public static final String EXPORT_PATH = "config/switchy";

	@Override
	public void onInitializeClient(ModContainer mod) {
		SwitchyClientReceivers.InitializeReceivers();
		SwitchyKeybindings.initializeKeybindings();
		SwitchyClientEvents.INIT.invoker().onInitialize();
		LOGGER.info("[Switchy Client] Initialized.");
	}
}
