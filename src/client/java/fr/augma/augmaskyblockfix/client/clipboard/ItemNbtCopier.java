package fr.augma.augmaskyblockfix.client.clipboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;

public class ItemNbtCopier {

	public static void copy(final ItemStack stack) {
		final Minecraft minecraft = Minecraft.getInstance();
		final ClientPacketListener connection = minecraft.getConnection();
		if (connection == null || stack.isEmpty()) {
			return;
		}

		final RegistryOps<Tag> ops = RegistryOps.create(NbtOps.INSTANCE, connection.registryAccess());
		ItemStack.CODEC.encodeStart(ops, stack)
				.resultOrPartial(error -> feedback("Could not read the item nbt"))
				.ifPresent(tag -> {
					minecraft.keyboardHandler.setClipboard(tag.toString());
					feedback("Copied the item nbt to the clipboard");
				});
	}

	private static void feedback(final String message) {
		final Minecraft minecraft = Minecraft.getInstance();
		if (minecraft.player != null) {
			minecraft.player.sendSystemMessage(Component.literal(message));
		}
	}

}