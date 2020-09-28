package mod.vemerion.morebars;

import mod.vemerion.morebars.bar.Bars;
import mod.vemerion.morebars.config.Config;
import net.minecraft.item.Item;
import net.minecraft.particles.BasicParticleType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Main.MODID)
public class Main {
	
	public Main() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
	}
	
	public static boolean DEBUG = false;
	
	public static final String MODID = "more-bars";
	
	@ObjectHolder(Main.MODID + ":blood_particle_type")
	public static final BasicParticleType BLOOD_PARTICLE_TYPE = null;
	
	@ObjectHolder(Main.MODID + ":band_aid_item")
	public static final Item BAND_AID_ITEM = null;

	
	@CapabilityInject(Bars.class)
	public static final Capability<Bars> BARS_CAP = null;

}
