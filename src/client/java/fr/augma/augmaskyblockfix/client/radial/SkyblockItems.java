package fr.augma.augmaskyblockfix.client.radial;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkyblockItems {

	private static final String REPOSITORY = "https://raw.githubusercontent.com/NotEnoughUpdates/NotEnoughUpdates-REPO/master/items/";

	private static final Pattern NAME = Pattern.compile("[A-Z0-9][A-Z0-9_;:\\-]*");

	private static final Pattern TEXTURE = Pattern.compile("Value:\"([A-Za-z0-9+/=]+)\"");

	private static final Gson GSON = new Gson();

	private static final Map<String, ItemStack> RESOLVED = new ConcurrentHashMap<>();

	private static final Set<String> REQUESTED = ConcurrentHashMap.newKeySet();

	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor(runnable -> {
		final Thread thread = new Thread(runnable, "augmaskyblockfix-neu");
		thread.setDaemon(true);
		return thread;
	});

	public static boolean looksLikeSkyblockItem(final String raw) {
		return NAME.matcher(raw).matches();
	}

	public static ItemStack get(final String name) {
		final ItemStack resolved = RESOLVED.get(name);
		if (resolved != null) {
			return resolved;
		}
		if (REQUESTED.add(name)) {
			EXECUTOR.execute(() -> load(name));
		}
		return null;
	}

	private static void load(final String name) {
		try {
			final String json = read(name);
			if (json != null) {
				RESOLVED.put(name, parse(json));
			}
		} catch (final Exception exception) {
			RESOLVED.put(name, Items.BARRIER.getDefaultInstance());
		}
	}

	private static String read(final String name) throws IOException, InterruptedException {
		final Path cached = cacheDirectory().resolve(name + ".json");
		if (Files.isRegularFile(cached)) {
			return Files.readString(cached);
		}

		final HttpRequest request = HttpRequest.newBuilder(URI.create(REPOSITORY + name + ".json"))
				.timeout(Duration.ofSeconds(10))
				.header("User-Agent", "AugmaSkyblockFixes")
				.GET()
				.build();
		final HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			return null;
		}

		Files.createDirectories(cached.getParent());
		Files.writeString(cached, response.body());
		return response.body();
	}

	private static ItemStack parse(final String json) {
		final JsonObject object = GSON.fromJson(json, JsonObject.class);
		final String nbt = object.has("nbttag") ? object.get("nbttag").getAsString() : "";

		final Matcher matcher = TEXTURE.matcher(nbt);
		if (matcher.find()) {
			return SkullFactory.create(matcher.group(1));
		}

		final Identifier identifier = object.has("itemid") ? Identifier.tryParse(object.get("itemid").getAsString()) : null;
		if (identifier == null || !BuiltInRegistries.ITEM.containsKey(identifier)) {
			return Items.BARRIER.getDefaultInstance();
		}
		return BuiltInRegistries.ITEM.getValue(identifier).getDefaultInstance();
	}

	private static Path cacheDirectory() {
		return FabricLoader.getInstance().getConfigDir().resolve("augmaskyblockfix").resolve("neu-items");
	}

}