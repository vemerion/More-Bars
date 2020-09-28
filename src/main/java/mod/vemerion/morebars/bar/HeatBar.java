package mod.vemerion.morebars.bar;

import java.util.function.Predicate;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;

public class HeatBar extends Bar {

	public HeatBar() {
		super(500);
	}

	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);
		World world = player.world;
		if (player.ticksExisted % 10 == 0) {
			tickHeat(player);

			if (!world.isRemote) {
				if (value > 950) {
					player.setFire(1);
				} else if (value < 50) {
					if (player.ticksExisted % 10 == 0)
						player.attackEntityFrom(DamageSource.GENERIC.setDamageBypassesArmor(), 1);
				}
			}
		}
	}

	private void tickHeat(PlayerEntity player) {
		World world = player.world;

		// Biome
		Biome biome = world.getBiome(player.getPosition());
		TempCategory temperature = biome.getTempCategory();
		if (temperature == TempCategory.WARM) {
			setValue(value + 1);
		} else if (temperature == TempCategory.COLD) {
			setValue(value - 1);
		}

		// Rain
		if (world.isRainingAt(player.getPosition()))
			setValue(value - 1);

		// Water and Fire
		if (player.isInWater()) {
			setValue(value - 2);
		} else if (player.isInLava()) {
			setValue(value + 15);
		}

		// Fire
		if (nearFire(player)) {
			setValue(value + 2);
		}
	}

	private boolean nearFire(PlayerEntity player) {
		BlockPos pos = player.getPosition();
		MutableBoundingBox box = new MutableBoundingBox(pos.add(-2, -2, -2), pos.add(2, 2, 2));
		Predicate<BlockState> isFire = (state) -> state.getBlock() == Blocks.CAMPFIRE
				|| state.getBlock() == Blocks.FIRE;
		return BlockPos.getAllInBox(box).anyMatch((p) -> player.world.hasBlockState(p, isFire));
	}

	@Override
	public int maxValue() {
		return 1000;
	}

	@Override
	public String getName() {
		return "HeatBar";
	}

}
