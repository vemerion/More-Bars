package mod.vemerion.morebars.bar;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class VitaminBar extends Bar {
	private static final Set<Item> VITAMIN_FOOD = ImmutableSet.of(Items.COD, Items.COOKED_COD, Items.SALMON,
			Items.COOKED_SALMON, Items.TROPICAL_FISH);
	
	private static final int MINUTE = 20 * 60;

	private int debuffTimer;

	public VitaminBar() {
		super(15000);
	}

	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);

		if (player.world.canSeeSky(player.getPosition())) {
			addValue(1);
		} else {
			addValue(-1);
		}

		if (!player.world.isRemote) {
			if (getFraction() < 0.2 && debuffTimer-- < 0) {
				Random rand = player.getRNG();
				debuffTimer = MINUTE + rand.nextInt(MINUTE * 2);
				EffectInstance effect = rand.nextBoolean() ? new EffectInstance(Effects.WEAKNESS, MINUTE)
						: new EffectInstance(Effects.MINING_FATIGUE, MINUTE);
				player.addPotionEffect(effect);
			}
		}
	}

	@Override
	public int maxValue() {
		return 15000;
	}

	@Override
	public String getName() {
		return "VitaminBar";
	}

	public void eat(Item item) {
		if (VITAMIN_FOOD.contains(item)) {
			addFraction(0.1);
		}
	}
}
