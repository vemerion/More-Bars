package mod.vemerion.morebars;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mod.vemerion.morebars.bar.Bars;
import mod.vemerion.morebars.bar.BloodBar;
import mod.vemerion.morebars.bar.SleepBar;
import mod.vemerion.morebars.bar.capability.BarsProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class ForgeEventSubscriber {
	public static final ResourceLocation BARS_CAP = new ResourceLocation(Main.MODID, "bars");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity)
			event.addCapability(BARS_CAP, new BarsProvider());
	}

	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event) {
		if (event.phase == Phase.START) {
			PlayerEntity player = event.player;
			Bars bars = Bars.getBars(player);
			bars.tick(player);
		}
	}

	private static final Set<Potion> nonIntoxicating = ImmutableSet.of(Potions.EMPTY, Potions.WATER, Potions.AWKWARD,
			Potions.MUNDANE);

	@SubscribeEvent
	public static void getDrunk(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof PlayerEntity && event.getItem().getItem() instanceof PotionItem) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (nonIntoxicating.contains(PotionUtils.getPotionFromItem(event.getItem())))
				return;
			Bars bars = Bars.getBars(player);
			bars.getIntoxicated().addFraction(0.25);
		}
	}

	@SubscribeEvent
	public static void eatVitaminFood(LivingEntityUseItemEvent.Finish event) {
		if (event.getEntity() instanceof PlayerEntity && event.getItem().getItem().isFood()) {
			Bars.getBars(event.getEntityLiving()).getVitamin().eat(event.getItem().getItem());
		}
	}

	@SubscribeEvent
	public static void synchBarsAtLogin(PlayerLoggedInEvent event) {
		PlayerEntity player = event.getPlayer();
		Bars.getBars(player).sendMessage(player);
	}

	@SubscribeEvent
	public static void regainSleep(SleepFinishedTimeEvent event) {
		for (PlayerEntity player : event.getWorld().getPlayers()) {
			if (player.isSleeping()) {
				Bars bars = Bars.getBars(player);
				SleepBar sleepBar = bars.getSleep();
				sleepBar.addFraction(0.5);
				bars.sendSleepMessage(player, sleepBar.getValue());
			}
		}
	}

	@SubscribeEvent
	public static void bleedOnHit(LivingDamageEvent event) {
		if (event.getSource() instanceof EntityDamageSource && event.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntity();
			if (player.getRNG().nextDouble() < 0.1) {
				Bars bars = Bars.getBars(player);
				BloodBar bloodBar = bars.getBlood();
				bloodBar.addBleeding();
				bloodBar.sendBleedingMessage(player);
			}
		}
	}

	@SubscribeEvent
	public static void carryOverBars(PlayerEvent.Clone event) {
		Bars cloneBars = Bars.getBars(event.getPlayer());
		Bars originalBars = Bars.getBars(event.getOriginal());
		originalBars.getBlood().atDeath();
		cloneBars.load(originalBars.save());
	}

	@SubscribeEvent
	public static void synchBarsAtRespawn(PlayerRespawnEvent event) {
		Bars.getBars(event.getPlayer()).sendMessage(event.getPlayer());
	}

	@SubscribeEvent
	public static void synchBarsAtDimensionChange(PlayerChangedDimensionEvent event) {
		Bars.getBars(event.getPlayer()).sendMessage(event.getPlayer());
	}
}
