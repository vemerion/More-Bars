package mod.vemerion.morebars.bar;

import java.util.Random;

import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.network.BleedingMessage;
import mod.vemerion.morebars.bar.network.Network;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

public class BloodBar extends Bar {
	private static final DamageSource BLEEDING = new DamageSource("bleeding");

	private int bleeding;

	public BloodBar() {
		super(10000);
	}

	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);

		if (bleeding == 0) {
			addValue(1);
		} else {
			if (player.ticksExisted % 20 == 0) {
				addValue(-bleeding);
				bleeding--;
			}
			
			if (value == 0) {
				player.attackEntityFrom(BLEEDING, 3);
			}
		}

		if (player.world.isRemote) {
			if (player.ticksExisted % 20 == 0 && bleeding > 0)
				addBleedingParticles(player);
		}
	}

	private void addBleedingParticles(PlayerEntity player) {
		Random rand = player.getRNG();
		Vec3d direction = Vec3d.fromPitchYaw(player.getPitchYaw());
		Vec3d right = direction.rotateYaw(-90);
		Vec3d center = player.getPositionVec().add(direction.x * 0.4, 1.6 + direction.y * 0.6, direction.z * 0.4);
		Vec3d origin = center.add(right.x * (rand.nextDouble() - 0.5), rand.nextDouble() - 0.5,
				right.z * (rand.nextDouble() - 0.5));
		int count = bleeding / 2;
		for (int i = 0; i < count; i++) {
			Vec3d position = origin.add(right.x * (rand.nextDouble() * 0.2 - 0.1), rand.nextDouble() * 0.2 - 0.1,
					right.z * (rand.nextDouble() * 0.2 - 0.1));
			player.world.addParticle(Main.BLOOD_PARTICLE_TYPE, position.x, position.y, position.z, 0, 0, 0);
		}
	}
	
	public void atDeath() {
		addFraction(1);
		bleeding = 0;
	}
	
	public void sendBleedingMessage(PlayerEntity player) {
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new BleedingMessage());
	}

	@Override
	public void save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("bleeding", bleeding);
	}

	@Override
	public void load(CompoundNBT compound) {
		super.load(compound);
		if (compound.contains("bleeding"))
			bleeding = compound.getInt("bleeding");
	}

	@Override
	public int maxValue() {
		return 10000;
	}

	@Override
	public String getName() {
		return "BloodBar";
	}

	public void addBleeding() {
		this.bleeding = 90;
	}

	public void bandAid() {
		this.bleeding = 0;
	}
	
	public boolean isBleeding() {
		return bleeding > 0;
	}
}
