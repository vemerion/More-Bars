package mod.vemerion.morebars.bar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.network.BarsMessage;
import mod.vemerion.morebars.bar.network.Network;
import mod.vemerion.morebars.bar.network.SleepMessage;
import mod.vemerion.morebars.config.Config;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.PacketDistributor;

public class Bars {
	private HeatBar heatBar;
	private IntoxicatedBar intoxicatedBar;
	private SleepBar sleepBar;
	private BloodBar bloodBar;
	private VitaminBar vitaminBar;
	private BarBar barBar;
	private List<Bar> bars;
	private int timer;

	public Bars() {
		heatBar = new HeatBar();
		intoxicatedBar = new IntoxicatedBar();
		sleepBar = new SleepBar();
		bloodBar = new BloodBar();
		vitaminBar = new VitaminBar();
		barBar = new BarBar();

		bars = new ArrayList<>();
		add(heatBar);
		add(intoxicatedBar);
		add(sleepBar);
		add(bloodBar);
		add(vitaminBar);
		add(barBar);
	}

	private void add(Bar bar) {
		if (!Config.isBarDisabled(bar.getClass())) {
			bars.add(bar);
		}
	}

	public void tick(PlayerEntity player) {
		if (Main.DEBUG || !player.isCreative()) {
			if (!player.world.isRemote) {
				timer++;
				if (timer > 20 * 10) {
					sendMessage(player);
					timer = 0;
				}
			}

			for (Bar bar : bars)
				bar.tick(player);
		}
	}

	public int getNbrOfBars() {
		return bars.size();
	}

	public List<Bar> getBarsForRendering() {
		return bars;
	}

	public HeatBar getHeat() {
		return heatBar;
	}

	public IntoxicatedBar getIntoxicated() {
		return intoxicatedBar;
	}

	public SleepBar getSleep() {
		return sleepBar;
	}

	public BloodBar getBlood() {
		return bloodBar;
	}

	public VitaminBar getVitamin() {
		return vitaminBar;
	}

	public BarBar getBar() {
		return barBar;
	}

	public CompoundNBT save() {
		CompoundNBT compound = new CompoundNBT();
		for (Bar bar : bars)
			bar.save(compound);
		return compound;
	}

	public void load(INBT nbt) {
		CompoundNBT compound = (CompoundNBT) nbt;
		for (Bar bar : bars)
			bar.load(compound);
	}

	public void sendSleepMessage(PlayerEntity player, int value) {
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new SleepMessage(value));
	}

	public void sendMessage(PlayerEntity player) {
		Network.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
				new BarsMessage(save()));
	}

	public static Bars getBars(LivingEntity entityLiving) {
		return entityLiving.getCapability(Main.BARS_CAP).orElse(new Bars());
	}

	// Used in the network message classes because of DistExecutor silliness
	@OnlyIn(Dist.CLIENT)
	public static Bars getBarsClient(ClientPlayerEntity player) {
		return getBars(player);
	}

	public void randomize() {
		Random rand = new Random();
		for (Bar bar : bars) {
			bar.addFraction(rand.nextDouble() * 0.4 - 0.2);
		}
	}
}
