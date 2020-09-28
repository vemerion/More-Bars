package mod.vemerion.morebars.bar.network;

import java.util.function.Supplier;

import mod.vemerion.morebars.bar.Bars;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class BleedingMessage {

	
	public BleedingMessage() {
	}

	public static void encode(final BleedingMessage msg, final PacketBuffer buffer) {
	}

	public static BleedingMessage decode(final PacketBuffer buffer) {
		return new BleedingMessage();
	}

	public static void handle(final BleedingMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			Bars bars = Bars.getBarsClient(Minecraft.getInstance().player);
			bars.getBlood().addBleeding();
		}));
	}
}
