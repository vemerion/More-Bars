package mod.vemerion.morebars.item;

import mod.vemerion.morebars.bar.Bars;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BandAidItem extends Item {

	public BandAidItem() {
		super(new Item.Properties().group(ItemGroup.MISC));
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 60;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
		Bars.getBars(entityLiving).getBlood().bandAid();
		stack.shrink(1);
		return stack;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (Bars.getBars(playerIn).getBlood().isBleeding()) {
			playerIn.setActiveHand(handIn);
			return ActionResult.resultConsume(itemstack);
		}

		return ActionResult.resultFail(itemstack);
	}

}
