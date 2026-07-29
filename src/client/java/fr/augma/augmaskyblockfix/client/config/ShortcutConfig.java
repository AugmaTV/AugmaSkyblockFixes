package fr.augma.augmaskyblockfix.client.config;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;
import io.github.notenoughupdates.moulconfig.observer.Property;
import lombok.AccessLevel;
import lombok.Getter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@Getter
public class ShortcutConfig {

	@Expose
	@ConfigOption(name = "Label", desc = "Text shown under the icon")
	@ConfigEditorText
	public Property<String> label = Property.of("");

	@Expose
	@ConfigOption(name = "Icon", desc = "Item id used as icon, for example minecraft:ender_pearl")
	@ConfigEditorText
	public Property<String> icon = Property.of("minecraft:barrier");

	@Expose
	@ConfigOption(name = "Command", desc = "Command to run, without the leading slash. Leave empty to turn this entry into a sub menu")
	@ConfigEditorText
	public Property<String> command = Property.of("");

	@ConfigEditorButton(buttonText = "Remove")
	public transient Runnable remove = () -> {
	};

	@Getter(AccessLevel.NONE)
	private transient String parsedIcon;

	@Getter(AccessLevel.NONE)
	private transient ItemStack stack;

	public ItemStack stack() {
		final String raw = this.icon.get();
		if (!raw.equals(this.parsedIcon)) {
			this.parsedIcon = raw;
			this.stack = resolve(raw);
		}
		return this.stack;
	}

	public boolean hasCommand() {
		return !this.command.get().trim().isEmpty();
	}

	private static ItemStack resolve(final String raw) {
		final Identifier identifier = Identifier.tryParse(raw.trim());
		if (identifier == null || !BuiltInRegistries.ITEM.containsKey(identifier)) {
			return Items.BARRIER.getDefaultInstance();
		}
		return BuiltInRegistries.ITEM.getValue(identifier).getDefaultInstance();
	}

}