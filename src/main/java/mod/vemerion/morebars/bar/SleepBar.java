package mod.vemerion.morebars.bar;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.MathHelper;

public class SleepBar extends Bar {
	private int timer;

	private float fogIntensity, prevFogIntensity;

	public SleepBar() {
		super(100000);
	}

	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);

		addValue(-1);

		if (!player.world.isRemote) {
			if (getFraction() < 0.1 && timer-- < 0) {
				player.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 400));
				timer = 340;
			}
		} else {
			prevFogIntensity = fogIntensity;
			fogIntensity = (float) MathHelper.clampedLerp(0, 0.15f, 1 - getFraction() / getFogThreshhold());
		}
	}
	
	public float getFogThreshhold() {
		return 0.15f;
	}
	
	public float getFogIntensity(double partialTicks) {
		return MathHelper.lerp((float) partialTicks, prevFogIntensity, fogIntensity);
	}

	@Override
	public int maxValue() {
		return 10000;
	}

	@Override
	public String getName() {
		return "SleepBar";
	}

}
