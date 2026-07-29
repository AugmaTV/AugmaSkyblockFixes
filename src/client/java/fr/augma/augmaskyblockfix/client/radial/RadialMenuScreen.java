package fr.augma.augmaskyblockfix.client.radial;

import fr.augma.augmaskyblockfix.client.config.ModConfig;
import fr.augma.augmaskyblockfix.client.config.RadialConfig;
import fr.augma.augmaskyblockfix.client.config.ShortcutConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class RadialMenuScreen extends Screen {

	private static final int RADIUS = 70;

	private static final int SLOT_HALF = 14;

	private static final int DEAD_ZONE = 22;

	private static final int BACKDROP = 0x88000000;

	private static final int SLOT_COLOR = 0xAA202020;

	private static final int HOVER_COLOR = 0xFFFFFFFF;

	private final String path;

	private final List<String> children;

	private int hovered = -1;

	public RadialMenuScreen(final String path) {
		super(Component.literal("Radial menu"));
		this.path = path;
		this.children = ModConfig.get().getRadial().childrenOf(path);
	}

	public static void open(final String path) {
		if (!ModConfig.get().getRadial().childrenOf(path).isEmpty()) {
			Minecraft.getInstance().setScreenAndShow(new RadialMenuScreen(path));
		}
	}

	@Override
	public void extractRenderState(final GuiGraphicsExtractor gui, final int mouseX, final int mouseY, final float partialTick) {
		super.extractRenderState(gui, mouseX, mouseY, partialTick);

		final int centerX = gui.guiWidth() / 2;
		final int centerY = gui.guiHeight() / 2;
		gui.fill(0, 0, gui.guiWidth(), gui.guiHeight(), BACKDROP);

		this.hovered = hoveredIndex(mouseX - centerX, mouseY - centerY, this.children.size());

		final RadialConfig radial = ModConfig.get().getRadial();
		for (int index = 0; index < this.children.size(); index++) {
			final ShortcutConfig entry = radial.getEntries().get(this.children.get(index));
			if (entry == null) {
				continue;
			}

			final double angle = angleOf(index, this.children.size());
			final int slotX = centerX + (int) Math.round(Math.cos(angle) * RADIUS);
			final int slotY = centerY + (int) Math.round(Math.sin(angle) * RADIUS);

			gui.fill(slotX - SLOT_HALF, slotY - SLOT_HALF, slotX + SLOT_HALF, slotY + SLOT_HALF, SLOT_COLOR);
			if (index == this.hovered) {
				gui.outline(slotX - SLOT_HALF, slotY - SLOT_HALF, SLOT_HALF * 2, SLOT_HALF * 2, HOVER_COLOR);
			}

			gui.item(entry.stack(), slotX - 8, slotY - 8);

			final String label = entry.getLabel().get().isEmpty() ? RadialConfig.nameOf(this.children.get(index)) : entry.getLabel().get();
			gui.centeredText(this.font, Component.literal(label), slotX, slotY + SLOT_HALF + 2, -1);
		}
	}

	@Override
	public boolean mouseClicked(final MouseButtonEvent click, final boolean doubled) {
		if (this.hovered < 0 || this.hovered >= this.children.size()) {
			return super.mouseClicked(click, doubled);
		}

		final String selected = this.children.get(this.hovered);
		final ShortcutConfig entry = ModConfig.get().getRadial().getEntries().get(selected);
		if (entry == null) {
			return super.mouseClicked(click, doubled);
		}

		if (entry.hasCommand()) {
			this.onClose();
			RadialActions.run(entry.getCommand().get());
		} else {
			open(selected);
		}
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

	private static double angleOf(final int index, final int count) {
		return index * 2 * Math.PI / count - Math.PI / 2;
	}

	private static int hoveredIndex(final int deltaX, final int deltaY, final int count) {
		if (count == 0 || Math.sqrt(deltaX * deltaX + deltaY * deltaY) < DEAD_ZONE) {
			return -1;
		}

		double angle = Math.atan2(deltaY, deltaX) + Math.PI / 2;
		while (angle < 0) {
			angle += 2 * Math.PI;
		}
		final double slice = 2 * Math.PI / count;
		return (int) Math.round(angle / slice) % count;
	}

}