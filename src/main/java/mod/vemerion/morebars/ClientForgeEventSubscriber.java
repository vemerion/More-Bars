package mod.vemerion.morebars;

import java.util.Random;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.morebars.bar.Bars;
import mod.vemerion.morebars.bar.SleepBar;
import mod.vemerion.morebars.renderer.BarsRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

	@SubscribeEvent
	public static void renderBars(RenderGameOverlayEvent event) {
		if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			BarsRenderer.getInstance().render(Bars.getBarsClient(Minecraft.getInstance().player));
		}
	}

	@SubscribeEvent
	public static void sleepFogColor(EntityViewRenderEvent.FogColors event) {
		if (Bars.getBarsClient(Minecraft.getInstance().player).getSleep().getFraction() < 0.15) {
			event.setBlue(0.1f);
			event.setGreen(0.1f);
			event.setRed(0.1f);
		}
	}

	@SubscribeEvent
	public static void sleepFogIntensity(EntityViewRenderEvent.FogDensity event) {
		SleepBar sleepBar = Bars.getBarsClient(Minecraft.getInstance().player).getSleep();
		if (sleepBar.getFraction() < sleepBar.getFogThreshhold()) {
			event.setCanceled(true);
			event.setDensity(sleepBar.getFogIntensity(event.getRenderPartialTicks()));
		}
	}
	

	private static final double[] POSITIONS = { -0.4, 0.3, 0.05, 0, 0.3, 0.3 };
	
	@SubscribeEvent
	public static void renderBandAidAnimation(RenderHandEvent event) {
		AbstractClientPlayerEntity player = Minecraft.getInstance().player;
		ItemStack itemStack = event.getItemStack();
		Item item = itemStack.getItem();
		float partialTicks = event.getPartialTicks();
		if (item.equals(Main.BAND_AID_ITEM) && player.getActiveItemStack().equals(itemStack)) {
			Minecraft mc = Minecraft.getInstance();
			Random rand = new Random(7);
			int maxDuration = itemStack.getUseDuration();
			float duration = (float) maxDuration - ((float) player.getItemInUseCount() - partialTicks + 1.0f);
			MatrixStack matrix = event.getMatrixStack();
			matrix.push();
			for (int i = 0; i < 3; i++) {
				double x = POSITIONS[i * 2];
				double y = -0.2 + POSITIONS[i * 2 + 1];
				float rotation = rand.nextFloat() * 360;
				if (duration >= i * 20) {
					matrix.push();
					matrix.translate(x, y, -1 + MathHelper.lerp((duration - 20f * i) / 20f, 0, 1));
					matrix.scale(0.5f, 0.5f, 0.5f);
					matrix.rotate(new Quaternion(0, 0, rotation, true));
					mc.getItemRenderer().renderItem(new ItemStack(Main.BAND_AID_ITEM), TransformType.GUI, event.getLight(), OverlayTexture.NO_OVERLAY, matrix, event.getBuffers());
					matrix.pop();

				}
			}
			matrix.pop();
		}
	}
}
