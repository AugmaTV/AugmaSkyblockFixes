package fr.augma.augmaskyblockfix.client.radial;

import com.google.common.collect.ImmutableMultimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class SkullFactory {

	private static final String PROFILE_NAME = "augmaskyblockfix";

	private static final String TEXTURES = "textures";

	public static ItemStack create(final String texture) {
		final String value = encode(texture.trim());
		if (value == null) {
			return Items.PLAYER_HEAD.getDefaultInstance();
		}

		final PropertyMap properties = new PropertyMap(ImmutableMultimap.of(TEXTURES, new Property(TEXTURES, value)));
		final UUID id = UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
		final GameProfile profile = new GameProfile(id, PROFILE_NAME, properties);

		final ItemStack stack = Items.PLAYER_HEAD.getDefaultInstance();
		stack.set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile));
		return stack;
	}

	private static String encode(final String texture) {
		if (texture.isEmpty()) {
			return null;
		}
		if (!texture.startsWith("http")) {
			return texture;
		}

		final String json = "{\"textures\":{\"SKIN\":{\"url\":\"" + texture + "\"}}}";
		return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
	}

}