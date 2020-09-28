package mod.vemerion.morebars.renderer;

import mod.vemerion.morebars.bar.Bar;
import net.minecraft.util.ResourceLocation;

public class SimpleBarRenderer extends BarRenderer {

	private ResourceLocation texture;
	
	public SimpleBarRenderer(ResourceLocation texture) {
		this.texture = texture;
	}

	@Override
	protected ResourceLocation getTexture(Bar bar) {
		return texture;
	}
}
