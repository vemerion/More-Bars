package mod.vemerion.morebars.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.BarBar;
import mod.vemerion.morebars.bar.BloodBar;
import mod.vemerion.morebars.bar.HeatBar;
import mod.vemerion.morebars.bar.IntoxicatedBar;
import mod.vemerion.morebars.bar.SleepBar;
import mod.vemerion.morebars.bar.VitaminBar;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;
	static {
		final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == Config.COMMON_SPEC) {
			bakeConfig();
		}
	}
	
	private static Class<?>[] bars;
	
	private static Map<Class<?>, Boolean> isBarDisabled;
	
	public static boolean isBarDisabled(Class<?> bar) {
		return isBarDisabled.get(bar);
	}

	public static void bakeConfig() {
		isBarDisabled = new HashMap<>();
		
		for (Class<?> bar : bars) {
			isBarDisabled.put(bar, COMMON.isBarDisabled.get(bar).get());
		}
	}

	public static class CommonConfig {
		public final Map<Class<?>, BooleanValue> isBarDisabled = new HashMap<>();

		public CommonConfig(ForgeConfigSpec.Builder builder) {
			bars = new Class<?>[] { BarBar.class, BloodBar.class, HeatBar.class, IntoxicatedBar.class, SleepBar.class, VitaminBar.class };
			
			builder.push("Enable/Disable more bars");
			
			for (Class<?> bar : bars) {
				String name = bar.getSimpleName() + "Disable";
				isBarDisabled.put(bar, builder.comment("Set true to disable " + bar.getSimpleName()).define(name,  false));
			}
			
			builder.pop();
			
		}

	}
}
