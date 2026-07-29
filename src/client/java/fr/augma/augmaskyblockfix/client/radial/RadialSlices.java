package fr.augma.augmaskyblockfix.client.radial;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.util.Mth;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;

public class RadialSlices implements GuiElementRenderState {

	private static final float SCALE = 130F;

	private static final float GAP = (float) Math.toRadians(4.0);

	private static final float HOVER_GROWTH = 2F;

	private final Matrix3x2f pose;

	private final ScreenRectangle bounds;

	private final int centerX;

	private final int centerY;

	private final int count;

	private final float inner;

	private final float outer;

	private final int color;

	private final int hoverColor;

	private final int hovered;

	public RadialSlices(final Matrix3x2fc pose, final int centerX, final int centerY, final int count, final float inner, final float outer, final int color, final int hoverColor, final int hovered) {
		this.pose = new Matrix3x2f(pose);
		this.centerX = centerX;
		this.centerY = centerY;
		this.count = count;
		this.inner = inner;
		this.outer = outer;
		this.color = color;
		this.hoverColor = hoverColor;
		this.hovered = hovered;

		final int radius = (int) ((outer + HOVER_GROWTH) / 100F * SCALE + 10F);
		this.bounds = new ScreenRectangle(centerX - radius, centerY - radius, radius * 2, radius * 2).transformMaxBounds(this.pose);
	}

	@Override
	public RenderPipeline pipeline() {
		return RenderPipelines.GUI;
	}

	@Override
	public TextureSetup textureSetup() {
		return TextureSetup.noTexture();
	}

	@Override
	public ScreenRectangle scissorArea() {
		return null;
	}

	@Override
	public ScreenRectangle bounds() {
		return this.bounds;
	}

	@Override
	public void buildVertices(final VertexConsumer consumer) {
		final float step = (float) (Math.PI * 2 / this.count);
		final float offset = (float) (-Math.PI / 2 - step / 2);

		for (int index = 0; index < this.count; index++) {
			final float start = index * step + GAP * 0.5F + offset;
			final float end = (index + 1) * step - GAP * 0.5F + offset;

			final float cosStart = Mth.cos(start);
			final float sinStart = Mth.sin(start);
			final float cosEnd = Mth.cos(end);
			final float sinEnd = Mth.sin(end);

			final boolean isHovered = index == this.hovered;
			final float growth = isHovered ? HOVER_GROWTH : 0F;
			final float innerRadius = (this.inner - growth) / 100F * SCALE;
			final float outerRadius = (this.outer + growth) / 100F * SCALE;
			final int slice = isHovered ? this.hoverColor : this.color;

			quad(consumer, this.pose, slice,
					this.centerX + outerRadius * cosStart, this.centerY + outerRadius * sinStart,
					this.centerX + innerRadius * cosStart, this.centerY + innerRadius * sinStart,
					this.centerX + innerRadius * cosEnd, this.centerY + innerRadius * sinEnd,
					this.centerX + outerRadius * cosEnd, this.centerY + outerRadius * sinEnd);
		}
	}

	public static int[] centerOf(final int centerX, final int centerY, final int count, final float inner, final float outer, final int index) {
		final float[] angles = anglesOf(count, index);
		final float innerRadius = inner / 100F * SCALE;
		final float outerRadius = outer / 100F * SCALE;

		final float x = (outerRadius * angles[0] + innerRadius * angles[0] + innerRadius * angles[2] + outerRadius * angles[2]) * 0.25F;
		final float y = (outerRadius * angles[1] + innerRadius * angles[1] + innerRadius * angles[3] + outerRadius * angles[3]) * 0.25F;
		return new int[]{Math.round(centerX + x), Math.round(centerY + y)};
	}

	public static int hitTest(final float mouseX, final float mouseY, final int centerX, final int centerY, final int count, final float inner, final float outer) {
		final float innerRadius = inner / 100F * SCALE;
		final float outerRadius = outer / 100F * SCALE;

		for (int index = 0; index < count; index++) {
			final float[] angles = anglesOf(count, index);
			final float x0 = centerX + outerRadius * angles[0];
			final float y0 = centerY + outerRadius * angles[1];
			final float x1 = centerX + innerRadius * angles[0];
			final float y1 = centerY + innerRadius * angles[1];
			final float x2 = centerX + innerRadius * angles[2];
			final float y2 = centerY + innerRadius * angles[3];
			final float x3 = centerX + outerRadius * angles[2];
			final float y3 = centerY + outerRadius * angles[3];

			if (inTriangle(mouseX, mouseY, x0, y0, x1, y1, x2, y2) || inTriangle(mouseX, mouseY, x0, y0, x2, y2, x3, y3)) {
				return index;
			}
		}
		return -1;
	}

	private static float[] anglesOf(final int count, final int index) {
		final float step = (float) (Math.PI * 2 / count);
		final float offset = (float) (-Math.PI / 2 - step / 2);
		final float start = index * step + GAP * 0.5F + offset;
		final float end = (index + 1) * step - GAP * 0.5F + offset;
		return new float[]{Mth.cos(start), Mth.sin(start), Mth.cos(end), Mth.sin(end)};
	}

	private static boolean inTriangle(final float px, final float py, final float ax, final float ay, final float bx, final float by, final float cx, final float cy) {
		final float d1 = cross(px, py, ax, ay, bx, by);
		final float d2 = cross(px, py, bx, by, cx, cy);
		final float d3 = cross(px, py, cx, cy, ax, ay);
		return !((d1 < 0 || d2 < 0 || d3 < 0) && (d1 > 0 || d2 > 0 || d3 > 0));
	}

	private static float cross(final float px, final float py, final float ax, final float ay, final float bx, final float by) {
		return (ax - px) * (by - py) - (ay - py) * (bx - px);
	}

	private static void quad(final VertexConsumer consumer, final Matrix3x2f pose, final int color, final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
		consumer.addVertexWith2DPose(pose, x0, y0).setColor(color);
		consumer.addVertexWith2DPose(pose, x1, y1).setColor(color);
		consumer.addVertexWith2DPose(pose, x2, y2).setColor(color);
		consumer.addVertexWith2DPose(pose, x3, y3).setColor(color);
	}

}