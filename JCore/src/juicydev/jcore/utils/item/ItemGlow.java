package juicydev.jcore.utils.item;

import juicydev.jcore.utils.item.NbtFactory.NbtCompound;
import juicydev.jcore.utils.item.NbtFactory.NbtList;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemGlow {

	/**
	 * Adds a fake enchantment glow to an ItemStack, will be lost on restarts
	 * and if the player interacts with it in a creative mode inventory
	 * 
	 * @param stack
	 * @return The ItemStack
	 * @throws IllegalArgumentException
	 *             If the ItemStack is null or air
	 */
	public static ItemStack addGlow(ItemStack stack)
			throws IllegalArgumentException {
		Validate.notNull(stack, "Cannot add glow to a null ItemStack");
		if (stack.getType().equals(Material.AIR))
			throw new IllegalArgumentException("Cannot add glow to air");
		ItemStack craftStack = NbtFactory.getCraftItemStack(stack);
		NbtCompound tag = NbtFactory.fromItemTag(craftStack);
		NbtList ench = NbtFactory.createList();
		tag.put("ench", ench);
		NbtFactory.setItemTag(craftStack, tag);
		return craftStack;
	}

	/**
	 * Removes the enchantment glow
	 * <p>
	 * Note: this will remove any enchantments as well
	 * 
	 * @param stack
	 * @return The ItemStack
	 * @throws IllegalArgumentException
	 *             If the ItemStack is null or air
	 */
	public static ItemStack removeGlow(ItemStack stack)
			throws IllegalArgumentException {
		Validate.notNull(stack, "Cannot remove glow from a null ItemStack");
		if (stack.getType().equals(Material.AIR))
			throw new IllegalArgumentException("Cannot remove glow from air");
		ItemStack craftStack = NbtFactory.getCraftItemStack(stack);
		NbtCompound tag = NbtFactory.fromItemTag(craftStack);
		tag.remove("ench");
		NbtFactory.setItemTag(craftStack, tag);
		return craftStack;
	}
}