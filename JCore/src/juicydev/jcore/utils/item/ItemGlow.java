package juicydev.jcore.utils.item;

import juicydev.jcore.utils.item.NbtFactory.NbtCompound;
import juicydev.jcore.utils.item.NbtFactory.NbtList;

import org.bukkit.inventory.ItemStack;

public class ItemGlow {

	public static ItemStack addGlow(ItemStack stack) {
		ItemStack craftStack = NbtFactory.getCraftItemStack(stack);
		NbtCompound tag = NbtFactory.fromItemTag(craftStack);
		NbtList ench = NbtFactory.createList();
		tag.put("ench", ench);
		NbtFactory.setItemTag(craftStack, tag);
		return craftStack;
	}
}
