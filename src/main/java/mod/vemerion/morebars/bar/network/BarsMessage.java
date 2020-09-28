package mod.vemerion.morebars.bar.network;

import java.util.function.Supplier;

import mod.vemerion.morebars.bar.Bars;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class BarsMessage {
	private CompoundNBT compound;

	public BarsMessage(CompoundNBT compound) {
		this.compound = compound;
	}

	public static void encode(final BarsMessage msg, final PacketBuffer buffer) {
		buffer.writeCompoundTag(msg.compound);
	}

	public static BarsMessage decode(final PacketBuffer buffer) {
		return new BarsMessage(buffer.readCompoundTag());
	}

	public static void handle(final BarsMessage msg, final Supplier<NetworkEvent.Context> supplier) {
		final NetworkEvent.Context context = supplier.get();
		context.setPacketHandled(true);
		context.enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			Bars bars = Bars.getBarsClient(Minecraft.getInstance().player);
			bars.load(msg.compound);
		}));
	}
}
