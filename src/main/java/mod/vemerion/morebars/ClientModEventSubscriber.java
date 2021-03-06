package mod.vemerion.morebars;

import mod.vemerion.morebars.renderer.BarsRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {
	
	@SubscribeEvent
	public static void onRegisterParticleFactories(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particles.registerFactory(Main.BLOOD_PARTICLE_TYPE,
				sprite -> new BloodParticle.Factory(sprite));
	}

	@SubscribeEvent
	public static void onClientSetupEvent(FMLClientSetupEvent event) {
		new BarsRenderer();
	}
}
