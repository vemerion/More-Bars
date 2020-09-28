package mod.vemerion.morebars.renderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.Bar;
import mod.vemerion.morebars.bar.BarBar;
import mod.vemerion.morebars.bar.Bars;
import mod.vemerion.morebars.bar.BloodBar;
import mod.vemerion.morebars.bar.HeatBar;
import mod.vemerion.morebars.bar.IntoxicatedBar;
import mod.vemerion.morebars.bar.SleepBar;
import mod.vemerion.morebars.bar.VitaminBar;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BarsRenderer {

	private static BarsRenderer instance;

	private Map<Class<? extends Bar>, BarRenderer> renderers;

	public BarsRenderer() {
		instance = this;
		renderers = new HashMap<>();
		renderers.put(HeatBar.class, new HeatBarRenderer());
		renderers.put(IntoxicatedBar.class,
				new SimpleBarRenderer(new ResourceLocation(Main.MODID, "textures/bar/drunk.png")));
		renderers.put(SleepBar.class,
				new SimpleBarRenderer(new ResourceLocation(Main.MODID, "textures/bar/sleep.png")));
		renderers.put(BloodBar.class,
				new SimpleBarRenderer(new ResourceLocation(Main.MODID, "textures/bar/blood.png")));
		renderers.put(VitaminBar.class,
				new SimpleBarRenderer(new ResourceLocation(Main.MODID, "textures/bar/vitamin_d2.png")));
		renderers.put(BarBar.class, new SimpleBarRenderer(new ResourceLocation(Main.MODID, "textures/bar/barbar.png")));

	}

	public void render(Bars bars) {
		Minecraft mc = Minecraft.getInstance();
		MainWindow window = mc.getMainWindow();

		if (Main.DEBUG || !mc.player.isCreative()) {
			int x = window.getScaledWidth() / 2 - 91;
			int y = window.getScaledHeight() - 39 - 10 * (1 + (mc.player.getTotalArmorValue() == 0 ? 0 : 1));

			List<Bar> listOfBars = bars.getBarsForRendering();
			for (int i = 0; i < bars.getNbrOfBars(); i++) {
				renderers.get(listOfBars.get(i).getClass()).render(listOfBars.get(i), x, y - 10 * i,
						window.getScaledWidth(), window.getScaledHeight());
			}
		}
	}

	public static BarsRenderer getInstance() {
		return instance;
	}
}
