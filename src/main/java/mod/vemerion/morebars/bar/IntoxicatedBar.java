package mod.vemerion.morebars.bar;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class IntoxicatedBar extends Bar {
	public static final int MAX_VALUE = 500;
	private int timer;

	public IntoxicatedBar() {
		super(0);
	}

	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);

		if (player.ticksExisted % 10 == 0) {
			setValue(getValue() - 1);
		}

		if (!player.world.isRemote) {
			if (getValue() > maxValue() * 0.8f && timer-- < 0) {
				player.addPotionEffect(new EffectInstance(Effects.POISON, 400));
				player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 400));
				timer = 340;
			}
		}
	}

	@Override
	public int maxValue() {
		return MAX_VALUE;
	}

	@Override
	public String getName() {
		return "IntoxicatedBar";
	}
}
