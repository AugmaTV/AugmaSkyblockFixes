package fr.augma.augmaskyblockfix.client.radial;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
import fr.augma.augmaskyblockfix.client.config.ShortcutConfig;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RadialMenuScreen extends Screen {

	@Getter
	private static RadialMenuScreen current;

	private final String path;

	private final List<String> children;

	private int hovered = -1;

	private RadialMenuScreen(final String path) {
		super(Component.literal("Radial menu"));
		this.path = path;
		this.children = ModConfig.get().getRadial().childrenOf(path);
	}

	public static void open(final String path) {
		if (!ModConfig.get().getRadial().childrenOf(path).isEmpty()) {
			Minecraft.getInstance().setScreenAndShow(new RadialMenuScreen(path));
		}
	}

	public void activateHovered() {
		if (this.hovered < 0 || this.hovered >= this.children.size()) {
			close();
			return;
		}

		final String selected = this.children.get(this.hovered);
		final ShortcutConfig entry = ModConfig.get().getRadial().getEntries().get(selected);
		if (entry == null) {
			close();
			return;
		}

		if (entry.hasCommand()) {
			close();
			RadialActions.run(entry.getCommand().get());
		} else {
			open(selected);
		}
	}

	private static void close() {
		Minecraft.getInstance().setScreenAndShow(null);
	}

	@Override
	protected void init() {
		current = this;
	}

	@Override
	public void removed() {
		super.removed();
		if (current == this) {
			current = null;
		}
	}

	@Override
	protected void extractBlurredBackground(final GuiGraphicsExtractor graphics) {
		if (ModConfig.get().getRadial().isBlur()) {
			super.extractBlurredBackground(graphics);
		}
	}

	@Override
	public void extractRenderState(final GuiGraphicsExtractor gui, final int mouseX, final int mouseY, final float partialTick) {
		super.extractRenderState(gui, mouseX, mouseY, partialTick);

		final RadialConfig radial = ModConfig.get().getRadial();
		final int centerX = gui.guiWidth() / 2;
		final int centerY = gui.guiHeight() / 2;
		final int count = this.children.size();
		if (count == 0) {
			return;
		}

		gui.fill(0, 0, gui.guiWidth(), gui.guiHeight(), radial.backgroundRgb());
		this.hovered = RadialSlices.hitTest(mouseX, mouseY, centerX, centerY, count, radial.getInnerRadius(), radial.getOuterRadius());

		gui.guiRenderState.addGuiElement(new RadialSlices(gui.pose(), centerX, centerY, count, radial.getInnerRadius(), radial.getOuterRadius(), radial.sliceRgb(), radial.hoverRgb(), this.hovered));

		for (int index = 0; index < count; index++) {
			final ShortcutConfig entry = radial.getEntries().get(this.children.get(index));
			if (entry == null) {
				continue;
			}

			final int[] center = RadialSlices.centerOf(centerX, centerY, count, radial.getInnerRadius(), radial.getOuterRadius(), index);
			gui.item(entry.stack(), center[0] - 8, center[1] - 12);

			final String raw = entry.getLabel().get();
			final String label = raw.isEmpty() ? RadialConfig.nameOf(this.children.get(index)) : raw;
			gui.centeredText(this.font, Component.literal(label), center[0], center[1] + 4, -1);
		}
	}

	@Override
	public boolean mouseClicked(final MouseButtonEvent click, final boolean doubled) {
		this.activateHovered();
		return true;
	}

	@Override
	public void onClose() {
		final String parent = RadialConfig.parentOf(this.path);
		if (!this.path.isEmpty() && !ModConfig.get().getRadial().childrenOf(parent).isEmpty()) {
			Minecraft.getInstance().setScreenAndShow(new RadialMenuScreen(parent));
			return;
		}
		super.onClose();
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}