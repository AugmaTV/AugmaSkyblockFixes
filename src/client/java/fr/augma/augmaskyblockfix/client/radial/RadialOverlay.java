package fr.augma.augmaskyblockfix.client.radial;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
import fr.augma.augmaskyblockfix.client.config.ShortcutConfig;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RadialOverlay {

	private static final int MINIMUM_SLICES = 3;

	private static final int CENTER_HALF = 12;

	private static final int CENTER_ACTIVE_RADIUS_SQUARED = 225;

	private static final int CENTER_HOVER_RADIUS_SQUARED = 144;

	private static final int MAUVE = 0xFFCBA6F7;

	private static final int TEXT = 0xFFCDD6F4;

	private static final int SUBTEXT0 = 0xFFA6ADC8;

	private static final int OVERLAY0 = 0xFF6C7086;

	private static final int SURFACE2 = 0xFF585B70;

	private static final int SURFACE1 = 0xFF45475A;

	private static final int BASE = 0xFF1E1E2E;

	@Getter
	private static boolean open;

	private static String path = "";

	private static List<String> children = List.of();

	private static int hovered = -1;

	private static boolean centerHovered;

	private static boolean centerActive;

	public static void open(final String target) {
		final List<String> entries = ModConfig.get().getRadial().childrenOf(target);
		if (entries.isEmpty()) {
			return;
		}

		path = target;
		children = entries;
		hovered = -1;
		if (!open) {
			open = true;
			Minecraft.getInstance().mouseHandler.releaseMouse();
		}
	}

	public static void close(final boolean regrab) {
		if (!open) {
			return;
		}

		open = false;
		path = "";
		children = List.of();
		hovered = -1;
		if (regrab) {
			Minecraft.getInstance().mouseHandler.grabMouse();
		}
	}

	public static void back() {
		final String parent = RadialConfig.parentOf(path);
		if (!path.isEmpty() && !ModConfig.get().getRadial().childrenOf(parent).isEmpty()) {
			open(parent);
			return;
		}
		close(true);
	}

	public static boolean activate() {
		if (centerActive) {
			back();
			return true;
		}
		if (hovered < 0 || hovered >= children.size()) {
			return false;
		}

		final String selected = children.get(hovered);
		final ShortcutConfig entry = ModConfig.get().getRadial().getEntries().get(selected);
		if (entry == null) {
			return false;
		}

		if (entry.hasCommand()) {
			close(true);
			RadialActions.run(entry.getCommand().get());
			return true;
		}
		if (ModConfig.get().getRadial().childrenOf(selected).isEmpty()) {
			return false;
		}
		open(selected);
		return true;
	}

	public static void render(final GuiGraphicsExtractor gui) {
		if (!open) {
			return;
		}

		final Minecraft minecraft = Minecraft.getInstance();
		final RadialConfig radial = ModConfig.get().getRadial();
		final int centerX = gui.guiWidth() / 2;
		final int centerY = gui.guiHeight() / 2;
		final int slices = Math.max(MINIMUM_SLICES, children.size());
		final float distance = radial.getCentreDistance();
		final float outer = radial.getOuterRadius() + distance;
		final float inner = Math.min(radial.getInnerRadius() + distance, outer - 1F);
		final float gap = radial.getSliceGap();

		final float mouseX = (float) minecraft.mouseHandler.getScaledXPos(minecraft.getWindow());
		final float mouseY = (float) minecraft.mouseHandler.getScaledYPos(minecraft.getWindow());
		final float deltaX = mouseX - centerX;
		final float deltaY = mouseY - centerY;
		final float distanceSquared = deltaX * deltaX + deltaY * deltaY;
		centerActive = distanceSquared < CENTER_ACTIVE_RADIUS_SQUARED;
		centerHovered = distanceSquared < CENTER_HOVER_RADIUS_SQUARED;
		hovered = centerActive ? -1 : RadialSlices.hitTest(mouseX, mouseY, centerX, centerY, slices, inner, outer, gap, radial.isGeneralDirection());

		gui.fill(0, 0, gui.guiWidth(), gui.guiHeight(), radial.backgroundRgb());
		gui.guiRenderState.addGuiElement(new RadialSlices(gui.pose(), centerX, centerY, slices, inner, outer, gap, radial.sliceRgb(), radial.hoverRgb(), hovered));
		gui.nextStratum();

		for (int index = 0; index < children.size(); index++) {
			final ShortcutConfig entry = radial.getEntries().get(children.get(index));
			if (entry == null) {
				continue;
			}

			final int[] center = RadialSlices.centerOf(centerX, centerY, slices, inner, outer, gap, index);
			gui.item(entry.stack(), center[0] - 8, center[1] - 8);
		}

		renderCenterButton(gui, minecraft, centerX, centerY);
		renderLabel(gui, minecraft, (int) mouseX, (int) mouseY);
	}

	private static void renderCenterButton(final GuiGraphicsExtractor gui, final Minecraft minecraft, final int centerX, final int centerY) {
		final String glyph = path.isEmpty() ? "✕" : "←";

		gui.fill(centerX - CENTER_HALF, centerY - CENTER_HALF, centerX + CENTER_HALF, centerY + CENTER_HALF, centerHovered ? SURFACE2 : SURFACE1);
		border(gui, centerX - CENTER_HALF, centerY - CENTER_HALF, CENTER_HALF * 2, CENTER_HALF * 2, centerHovered ? MAUVE : OVERLAY0);
		gui.text(minecraft.font, Component.literal(glyph), centerX - minecraft.font.width(glyph) / 2, centerY - minecraft.font.lineHeight / 2, centerHovered ? MAUVE : SUBTEXT0, false);
	}

	private static void renderLabel(final GuiGraphicsExtractor gui, final Minecraft minecraft, final int mouseX, final int mouseY) {
		final String label = labelFor();
		if (label == null) {
			return;
		}

		final int width = minecraft.font.width(label);
		final int x = mouseX + 12;
		final int y = mouseY - 4;

		gui.fill(x - 5, y - 5, x + width + 5, y + minecraft.font.lineHeight + 5, BASE);
		border(gui, x - 5, y - 5, width + 10, minecraft.font.lineHeight + 10, MAUVE);
		gui.text(minecraft.font, Component.literal(label), x, y, TEXT, false);
	}

	private static String labelFor() {
		if (centerHovered) {
			return path.isEmpty() ? "Exit" : "Back";
		}
		if (hovered < 0 || hovered >= children.size()) {
			return null;
		}

		final String selected = children.get(hovered);
		final ShortcutConfig entry = ModConfig.get().getRadial().getEntries().get(selected);
		if (entry == null) {
			return null;
		}
		final String raw = entry.getLabel().get();
		return raw.isEmpty() ? RadialConfig.nameOf(selected) : raw;
	}

	private static void border(final GuiGraphicsExtractor gui, final int x, final int y, final int width, final int height, final int color) {
		gui.fill(x - 1, y - 1, x + width + 1, y, color);
		gui.fill(x - 1, y + height, x + width + 1, y + height + 1, color);
		gui.fill(x - 1, y, x, y + height, color);
		gui.fill(x + width, y, x + width + 1, y + height, color);
	}

}