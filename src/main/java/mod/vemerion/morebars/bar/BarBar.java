package mod.vemerion.morebars.bar;

import net.minecraft.entity.player.PlayerEntity;

public class BarBar extends Bar {
	private static final int QUATER = 20 * 60 * 15;
	
	public BarBar() {
		super(QUATER);
	}
	
	@Override
	public void tick(PlayerEntity player) {
		super.tick(player);
		
		addValue(-1);
		if (getValue() == 0 && !player.world.isRemote) {
			Bars bars = Bars.getBars(player);
			bars.randomize();
			setValue(maxValue());
			bars.sendMessage(player);
		}
	}

	@Override
	public int maxValue() {
		return QUATER;
	}

	@Override
	public String getName() {
		return "BarBar";
	}

}
