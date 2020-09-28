package mod.vemerion.morebars.renderer;

import com.mojang.blaze3d.systems.RenderSystem;

import mod.vemerion.morebars.bar.Bar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public abstract class BarRenderer {
	
	public void render(Bar bar, int x, int y, int windowWidth, int windowHeight) {
		int count = MathHelper.ceil((bar.getValue() / (float) bar.maxValue()) * 10);

		for (int i = 0; i < 10; i++) {
			if (i >= count)
				RenderSystem.color4f(0.1f, 0.1f, 0.1f, 0.35f);
			drawIcon(bar, x + i * 8, y);
		}
		RenderSystem.color4f(1, 1, 1, 1);
	}
	
	protected void draw(ResourceLocation texture, int x, int y, int width, int height, int texX, int texY, int texWidth, int texHeight) {
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		BufferBuilder builder = Tessellator.getInstance().getBuffer();
		builder.begin(7, DefaultVertexFormats.POSITION_TEX);
		builder.pos(x, y + height, -5).tex(texX, texY + texHeight).endVertex();
		builder.pos(x + width, y + height, -5).tex(texX + texWidth, texY + texHeight).endVertex();
		builder.pos(x + width, y, -5).tex(texX + texWidth, texY).endVertex();
		builder.pos(x, y, -5).tex(texX, texY).endVertex();
		builder.finishDrawing();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableBlend();
		WorldVertexBufferUploader.draw(builder);
	}
	
	private void drawIcon(Bar bar, int x, int y) {
		draw(getTexture(bar), x, y, 8, 8, 0, 0, 1, 1);
	}

	protected abstract ResourceLocation getTexture(Bar bar);
}
