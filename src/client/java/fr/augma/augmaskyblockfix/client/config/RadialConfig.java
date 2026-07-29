package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.ChromaColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider;
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
	@ConfigOption(name = "Hold to open", desc = "Hold the key and release over a shortcut to run it. When off, the key toggles the menu and you click to select")
	@ConfigEditorBoolean
	public boolean holdToOpen = true;

	@Expose
	@ConfigOption(name = "Background", desc = "Colour drawn over the screen behind the menu")
	@ConfigEditorColour
	public String background = ChromaColour.special(0, 120, 0, 0, 0);

	@Expose
	@ConfigOption(name = "Blur background", desc = "Blur the game behind the menu")
	@ConfigEditorBoolean
	public boolean blur = false;

	@Expose
	@ConfigOption(name = "Slice colour", desc = "Colour of the menu slices")
	@ConfigEditorColour
	public String sliceColour = ChromaColour.special(0, 220, 30, 30, 46);

	@Expose
	@ConfigOption(name = "Hover colour", desc = "Colour of the slice under the cursor")
	@ConfigEditorColour
	public String hoverColour = ChromaColour.special(0, 255, 137, 180, 250);

	@Expose
	@ConfigOption(name = "Inner radius", desc = "Inner radius of the ring, in percent")
	@ConfigEditorSlider(minValue = 10F, maxValue = 60F, minStep = 1F)
	public float innerRadius = 30F;

	@Expose
	@ConfigOption(name = "Outer radius", desc = "Outer radius of the ring, in percent")
	@ConfigEditorSlider(minValue = 20F, maxValue = 90F, minStep = 1F)
	public float outerRadius = 55F;

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

	public int backgroundRgb() {
		return ChromaColour.forLegacyString(this.background).getEffectiveColourRGB();
	}

	public int sliceRgb() {
		return ChromaColour.forLegacyString(this.sliceColour).getEffectiveColourRGB();
	}

	public int hoverRgb() {
		return ChromaColour.forLegacyString(this.hoverColour).getEffectiveColourRGB();
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