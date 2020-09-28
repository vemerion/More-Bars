package mod.vemerion.morebars.bar.network;

import java.util.function.Supplier;

import mod.vemerion.morebars.bar.Bars;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SleepMessage {

	private int value;
	
	public SleepMessage(int value) {
		this.value = value;
	}

	public static void encode(final SleepMessage msg, final PacketBuffer buffer) {
		buffer.writeInt(msg.value);
	}

	public static SleepMessage decode(final PacketBuffer buffer) {
		return new SleepMessage(buffer.readInt());
	}

	public static void handle(final SleepMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			Bars bars = Bars.getBarsClient(Minecraft.getInstance().player);
			bars.getSleep().setValue(msg.value);
		}));
	}
}
