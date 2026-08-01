package fr.augma.augmaskyblockfix.client.level;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class LevelRecolour {

	private static final Pattern LEGACY_BADGE = Pattern.compile("§.\\[§.(\\d+)§.\\]");

	private static final int MAX_DIGITS = 4;

	public static Component apply(final Component source) {
		final List<Component> siblings = source.getSiblings();
		for (int index = 0; index < siblings.size(); index++) {
			final Component structured = structuredAt(siblings, index);
			final Component replacement = structured != null ? structured : legacyOf(siblings.get(index));
			if (replacement != null) {
				final MutableComponent copy = source.copy();
				copy.getSiblings().set(index, replacement);
				return copy;
			}
		}
		return source;
	}

	private static Component structuredAt(final List<Component> siblings, final int index) {
		if (index == 0 || index + 1 >= siblings.size()) {
			return null;
		}

		final Component digits = siblings.get(index);
		final int level = levelOf(digits.getString());
		if (level < 0 || !siblings.get(index - 1).getString().endsWith("[") || !siblings.get(index + 1).getString().startsWith("]")) {
			return null;
		}
		return digits.copy().setStyle(digits.getStyle().withColor(TextColor.fromRgb(SkyblockLevel.gradientOf(level))));
	}

	private static Component legacyOf(final Component sibling) {
		if (!sibling.getSiblings().isEmpty()) {
			return null;
		}

		final String text = sibling.getString();
		final Matcher matcher = LEGACY_BADGE.matcher(text);
		if (!matcher.find()) {
			return null;
		}

		final int level = levelOf(matcher.group(1));
		if (level < 0) {
			return null;
		}
		return rebuild(text, matcher.start(1), matcher.end(1), SkyblockLevel.gradientOf(level), sibling.getStyle());
	}

	private static Component rebuild(final String text, final int start, final int end, final int rgb, final Style base) {
		final MutableComponent rebuilt = Component.empty().setStyle(base);
		final StringBuilder buffer = new StringBuilder();
		Style current = Style.EMPTY;
		int index = 0;

		while (index < text.length()) {
			if (index == start) {
				flush(rebuilt, buffer, current);
				rebuilt.append(Component.literal(text.substring(start, end)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(rgb))));
				index = end;
				continue;
			}

			final char character = text.charAt(index);
			if (character == '§' && index + 1 < text.length()) {
				flush(rebuilt, buffer, current);
				current = advance(current, text.charAt(index + 1));
				index += 2;
				continue;
			}

			buffer.append(character);
			index++;
		}

		flush(rebuilt, buffer, current);
		return rebuilt;
	}

	private static void flush(final MutableComponent target, final StringBuilder buffer, final Style style) {
		if (buffer.isEmpty()) {
			return;
		}

		target.append(Component.literal(buffer.toString()).setStyle(style));
		buffer.setLength(0);
	}

	private static Style advance(final Style style, final char code) {
		final char lower = Character.toLowerCase(code);
		if (lower == 'r') {
			return Style.EMPTY;
		}

		final ChatFormatting formatting = ChatFormatting.getByCode(lower);
		if (formatting == null) {
			return style;
		}
		return isColour(lower) ? Style.EMPTY.withColor(formatting) : style.applyFormat(formatting);
	}

	private static boolean isColour(final char code) {
		return (code >= '0' && code <= '9') || (code >= 'a' && code <= 'f');
	}

	private static int levelOf(final String text) {
		if (text.isEmpty() || text.length() > MAX_DIGITS) {
			return -1;
		}

		for (int index = 0; index < text.length(); index++) {
			if (!Character.isDigit(text.charAt(index))) {
				return -1;
			}
		}
		return Integer.parseInt(text);
	}

}
