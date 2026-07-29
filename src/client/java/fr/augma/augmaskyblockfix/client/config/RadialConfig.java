package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class RadialConfig {

	public static final String SEPARATOR = "/";

	@Expose
	@ConfigOption(name = "Enabled", desc = "Enable the radial menu")
	@ConfigEditorBoolean
	public boolean enabled = false;

	@Expose
	@ConfigOption(name = "Keybind", desc = "Key that opens the radial menu")
	@ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_R)
	public int keybind = GLFW.GLFW_KEY_R;

	@Expose
	public Map<String, ShortcutConfig> entries = new LinkedHashMap<>();

	@ConfigOption(name = "New shortcut", desc = "Path of the shortcut to add. Use a slash to nest it, for example warps/hub")
	@ConfigEditorText
	public transient Property<String> newShortcut = Property.of("");

	@ConfigEditorButton(buttonText = "Add")
	public transient Runnable add = () -> {
	};

	public List<String> childrenOf(final String path) {
		final List<String> children = new ArrayList<>();
		for (final String candidate : this.entries.keySet()) {
			if (parentOf(candidate).equals(path)) {
				children.add(candidate);
			}
		}
		return children;
	}

	public static String parentOf(final String path) {
		final int index = path.lastIndexOf(SEPARATOR);
		return index < 0 ? "" : path.substring(0, index);
	}

	public static String nameOf(final String path) {
		final int index = path.lastIndexOf(SEPARATOR);
		return index < 0 ? path : path.substring(index + 1);
	}

	public static int depthOf(final String path) {
		int depth = 0;
		for (int index = 0; index < path.length(); index++) {
			if (path.charAt(index) == '/') {
				depth++;
			}
		}
		return depth;
	}

}