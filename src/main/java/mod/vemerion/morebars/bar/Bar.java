package mod.vemerion.morebars.bar;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

public abstract class Bar {

	protected int value;

	public Bar(int value) {
		this.setValue(value);
	}

	public abstract int maxValue();

	public abstract String getName();

	public void load(CompoundNBT compound) {
		if (compound.contains(getName())) {
			setValue(compound.getInt(getName()));
		}
	}

	public void save(CompoundNBT compound) {
		compound.putInt(getName(), getValue());
	}

	public int getValue() {
		return value;
	}

	public double getFraction() {
		return (double) value / maxValue();
	}

	public void setValue(int value) {
		this.value = MathHelper.clamp(value, 0, maxValue());
	}

	public void addValue(int value) {
		setValue(getValue() + value);
	}

	public void addFraction(double fraction) {
		setValue((int) (getValue() + maxValue() * fraction));
	}

	public void tick(PlayerEntity player) {
	}
}
