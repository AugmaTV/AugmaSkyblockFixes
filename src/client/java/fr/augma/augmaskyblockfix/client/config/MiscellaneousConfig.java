package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

@Getter
public class MiscellaneousConfig {

	@Expose
	@ConfigOption(name = "Centered plants", desc = "Remove the random offset applied to plants so they stay centered on their block")
	@ConfigEditorBoolean
	public Property<Boolean> centeredPlants = Property.of(false);

	@Expose
	@ConfigOption(name = "Level gradient on nametags", desc = "Blend the skyblock level colour towards the next tier colour on the names shown above players")
	@ConfigEditorBoolean
	public boolean levelGradientNametags = false;

	@Expose
	@ConfigOption(name = "Level gradient in tab", desc = "Same gradient in the player list, incompatible with the custom tab list of other mods")
	@ConfigEditorBoolean
	public boolean levelGradientTab = false;

	@Expose
	@ConfigOption(name = "Copy item nbt", desc = "Press the key below while hovering an item in any container to copy its nbt to the clipboard")
	@ConfigEditorBoolean
	public boolean copyItemNbt = false;

	@Expose
	@ConfigOption(name = "Copy nbt keybind", desc = "Key that copies the hovered item nbt")
	@ConfigEditorKeybind(defaultKey = GLFW.GLFW_KEY_N)
	public int copyItemNbtKey = GLFW.GLFW_KEY_N;

	public boolean isCenteredPlants() {
		return this.centeredPlants.get();
	}

}