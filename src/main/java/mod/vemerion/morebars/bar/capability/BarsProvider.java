package mod.vemerion.morebars.bar.capability;


import mod.vemerion.morebars.Main;
import mod.vemerion.morebars.bar.Bars;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class BarsProvider implements ICapabilitySerializable<INBT>{

	private LazyOptional<Bars> instance = LazyOptional.of(Main.BARS_CAP::getDefaultInstance);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return Main.BARS_CAP.orEmpty(cap, instance);
	}

	@Override
	public INBT serializeNBT() {
		return Main.BARS_CAP.getStorage().writeNBT(Main.BARS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null);
	}

	@Override
	public void deserializeNBT(INBT nbt) {
		Main.BARS_CAP.getStorage().readNBT(Main.BARS_CAP, instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")), null, nbt);
	}
}
