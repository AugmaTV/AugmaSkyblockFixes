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

	private static final int MINIMUM_SLICES = 3;

	private static final int CENTER_HALF = 12;

	private static final int CENTER_HOVER_RADIUS_SQUARED = 144;

	private static final int MAUVE = 0xFFCBA6F7;

	private static final int TEXT = 0xFFCDD6F4;

	private static final int SUBTEXT0 = 0xFFA6ADC8;

	private static final int OVERLAY0 = 0xFF6C7086;

	private static final int SURFACE2 = 0xFF585B70;

	private static final int SURFACE1 = 0xFF45475A;

	private static final int BASE = 0xFF1E1E2E;

	@Getter
	private static RadialMenuScreen current;

	private final String path;

	private final List<String> children;

	private int hovered = -1;

	private boolean centerHovered;

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
		if (this.centerHovered) {
			this.onClose();
			return;
		}

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
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float partialTick) {
		final RadialConfig radial = ModConfig.get().getRadial();
		if (radial.isBlur()) {
			graphics.blurBeforeThisStratum();
		}
		graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), radial.backgroundRgb());
	}

	@Override
	public void extractRenderState(final GuiGraphicsExtractor gui, final int mouseX, final int mouseY, final float partialTick) {
		super.extractRenderState(gui, mouseX, mouseY, partialTick);

		final RadialConfig radial = ModConfig.get().getRadial();
		final int centerX = gui.guiWidth() / 2;
		final int centerY = gui.guiHeight() / 2;
		final int slices = Math.max(MINIMUM_SLICES, this.children.size());
		final float inner = radial.getInnerRadius();
		final float outer = radial.getOuterRadius();

		final int deltaX = mouseX - centerX;
		final int deltaY = mouseY - centerY;
		this.centerHovered = deltaX * deltaX + deltaY * deltaY < CENTER_HOVER_RADIUS_SQUARED;
		this.hovered = this.centerHovered ? -1 : RadialSlices.hitTest(mouseX, mouseY, centerX, centerY, slices, inner, outer, radial.isGeneralDirection());
		if (this.hovered >= this.children.size()) {
			this.hovered = -1;
		}

		gui.guiRenderState.addGuiElement(new RadialSlices(gui.pose(), centerX, centerY, slices, inner, outer, radial.sliceRgb(), radial.hoverRgb(), this.hovered));

		for (int index = 0; index < this.children.size(); index++) {
			final ShortcutConfig entry = radial.getEntries().get(this.children.get(index));
			if (entry == null) {
				continue;
			}

			final int[] center = RadialSlices.centerOf(centerX, centerY, slices, inner, outer, index);
			gui.item(entry.stack(), center[0] - 8, center[1] - 8);
		}

		this.renderCenterButton(gui, centerX, centerY);
		this.renderLabel(gui, mouseX, mouseY);
	}

	private void renderCenterButton(final GuiGraphicsExtractor gui, final int centerX, final int centerY) {
		final boolean back = !this.path.isEmpty();
		final String glyph = back ? "←" : "✕";

		gui.fill(centerX - CENTER_HALF, centerY - CENTER_HALF, centerX + CENTER_HALF, centerY + CENTER_HALF, this.centerHovered ? SURFACE2 : SURFACE1);
		gui.outline(centerX - CENTER_HALF, centerY - CENTER_HALF, CENTER_HALF * 2, CENTER_HALF * 2, this.centerHovered ? MAUVE : OVERLAY0);
		gui.text(this.font, Component.literal(glyph), centerX - this.font.width(glyph) / 2, centerY - this.font.lineHeight / 2, this.centerHovered ? MAUVE : SUBTEXT0);
	}

	private void renderLabel(final GuiGraphicsExtractor gui, final int mouseX, final int mouseY) {
		final String label = this.labelFor();
		if (label == null) {
			return;
		}

		final int width = this.font.width(label);
		final int x = mouseX + 12;
		final int y = mouseY - 4;

		gui.fill(x - 5, y - 5, x + width + 5, y + this.font.lineHeight + 5, BASE);
		gui.outline(x - 5, y - 5, width + 10, this.font.lineHeight + 10, MAUVE);
		gui.text(this.font, Component.literal(label), x, y, TEXT);
	}

	private String labelFor() {
		if (this.centerHovered) {
			return this.path.isEmpty() ? "Exit" : "Back";
		}
		if (this.hovered < 0 || this.hovered >= this.children.size()) {
			return null;
		}

		final String selected = this.children.get(this.hovered);
		final ShortcutConfig entry = ModConfig.get().getRadial().getEntries().get(selected);
		if (entry == null) {
			return null;
		}
		final String raw = entry.getLabel().get();
		return raw.isEmpty() ? RadialConfig.nameOf(selected) : raw;
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