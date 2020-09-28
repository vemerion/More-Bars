package mod.vemerion.morebars.bar.capability;

import mod.vemerion.morebars.bar.Bars;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BarsStorage implements IStorage<Bars> {

	@Override
	public INBT writeNBT(Capability<Bars> capability, Bars instance, Direction side) {
		return instance.save();
		
	}

	@Override
	public void readNBT(Capability<Bars> capability, Bars instance, Direction side, INBT nbt) {
		instance.load(nbt);
	}
}
