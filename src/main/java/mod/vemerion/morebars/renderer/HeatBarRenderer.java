package mod.vemerion.morebars.renderer;

import com.mojang.blaze3d.systems.RenderSystem;

import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.Bar;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class HeatBarRenderer extends BarRenderer {

	private static final ResourceLocation OVERLAY = new ResourceLocation(Main.MODID, "textures/bar/overlay.png");
	private static final ResourceLocation HOT = new ResourceLocation(Main.MODID, "textures/bar/heat.png");
	private static final ResourceLocation COLD = new ResourceLocation(Main.MODID, "textures/bar/cold.png");

	@Override
	protected ResourceLocation getTexture(Bar bar) {
		if (bar.getValue() > 500)
			return HOT;
		else
			return COLD;
	}
	
	@Override
	public void render(Bar bar, int x, int y, int windowWidth, int windowHeight) {
		super.render(bar, x, y, windowWidth, windowHeight);

		if (bar.getValue() > 900) {
			float intensity = (float) MathHelper.clampedLerp(0, 0.2f, (bar.getValue() - 900d) / 100d);
			RenderSystem.color4f(1, 0.5f, 0, intensity);
			RenderSystem.alphaFunc(516, 0);
			draw(OVERLAY, 0, 0, windowWidth, windowHeight, 0, 0, 1, 1);
			RenderSystem.color4f(1, 1, 1, 1f);
			RenderSystem.defaultAlphaFunc();
		} else if (bar.getValue() < 100) {
			float intensity = (float) MathHelper.clampedLerp(0.2f, 0, bar.getValue() / 100d);
			RenderSystem.color4f(0, 0.5f, 1, intensity);
			RenderSystem.alphaFunc(516, 0);
			draw(OVERLAY, 0, 0, windowWidth, windowHeight, 0, 0, 1, 1);
			RenderSystem.color4f(1, 1, 1, 1f);
			RenderSystem.defaultAlphaFunc();
		}
	}

}
