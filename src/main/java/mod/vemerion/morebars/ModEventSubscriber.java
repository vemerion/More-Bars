package mod.vemerion.morebars;

import mod.vemerion.morebars.bar.Bars;
import mod.vemerion.morebars.bar.capability.BarsStorage;
import mod.vemerion.morebars.bar.network.SleepMessage;
import mod.vemerion.morebars.bar.network.BarsMessage;
import mod.vemerion.morebars.bar.network.BleedingMessage;
import mod.vemerion.morebars.bar.network.Network;
import mod.vemerion.morebars.item.BandAidItem;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {
	
	@SubscribeEvent
	public static void onRegisterItem(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(setup(new BandAidItem(), "band_aid_item"));
	}
	
	@SubscribeEvent
	public static void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(Bars.class, new BarsStorage(), Bars::new);

		Network.INSTANCE.registerMessage(0, BarsMessage.class, BarsMessage::encode,
				BarsMessage::decode, BarsMessage::handle);
		
		Network.INSTANCE.registerMessage(1, SleepMessage.class, SleepMessage::encode,
				SleepMessage::decode, SleepMessage::handle);
		
		Network.INSTANCE.registerMessage(2, BleedingMessage.class, BleedingMessage::encode,
				BleedingMessage::decode, BleedingMessage::handle);
	}
	
	@SubscribeEvent
	public static void onIParticleTypeRegistration(RegistryEvent.Register<ParticleType<?>> event) {
		event.getRegistry().register(setup(new BasicParticleType(true), "blood_particle_type"));
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Main.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
