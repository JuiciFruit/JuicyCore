package juicydev.jcore.utils.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

/**
 * Convenience class to create ItemStacks without using multiple lines to set
 * ItemMeta etc.
 * 
 * @author JuicyDev
 */
public class ItemBuilder {

	private ItemStack stack;
	private ItemMeta meta;
	private boolean glow = false;

	private void setStack(ItemStack stack) {
		this.stack = stack;
	}

	private void setMeta(ItemMeta meta) {
		this.meta = meta;
		this.stack.setItemMeta(meta);
	}

	private void updateMeta() {
		setMeta(stack.getItemMeta());
	}

	private void updateStack() {
		stack.setItemMeta(meta);
	}

	/**
	 * Create a new ItemBuilder from an existing ItemStack
	 * 
	 * @param stack
	 *            ItemStack to copy
	 */
	public ItemBuilder(ItemStack stack) {
		setStack(stack);
		setMeta(stack.getItemMeta());
	}

	/**
	 * Create a new ItemBuilder with the specified material
	 * 
	 * @param material
	 *            Material type
	 */
	public ItemBuilder(Material material) {
		this(material, 1, (short) 0);
	}

	/**
	 * Create a new ItemBuilder with the specified material and size
	 * 
	 * @param material
	 *            Material type
	 * @param amount
	 *            Stack size
	 */
	public ItemBuilder(Material material, int amount) {
		this(material, amount, (short) 0);
	}

	/**
	 * Create a new ItemBuilder with the specified material, size and damage
	 * 
	 * @param material
	 *            Material type
	 * @param amount
	 *            Stack size
	 * @param damage
	 *            Damage on stack
	 */
	public ItemBuilder(Material material, int amount, short damage) {
		setStack(new ItemStack(material, amount, (short) 0));
		setMeta(stack.getItemMeta());
	}

	/**
	 * Set the material type
	 * 
	 * @param material
	 *            Material type to set
	 */
	public ItemBuilder setType(Material material) {
		this.stack.setType(material);
		updateMeta();
		return this;
	}

	/**
	 * Set the stack size
	 * 
	 * @param amount
	 *            Stack size to set
	 */
	public ItemBuilder setAmount(int amount) {
		stack.setAmount(amount);
		updateMeta();
		return this;
	}

	/**
	 * Set the durability of the stack
	 * 
	 * @param durability
	 *            Durability to set
	 */
	public ItemBuilder setDurability(short durability) {
		stack.setDurability(durability);
		updateMeta();
		return this;
	}

	/**
	 * Set the MaterialData of the stack
	 * 
	 * @param data
	 *            MaterialData to set
	 */
	public ItemBuilder setData(MaterialData data) {
		stack.setData(data);
		updateMeta();
		return this;
	}

	/**
	 * Set the lore
	 * 
	 * @param lore
	 *            List of strings
	 */
	public ItemBuilder setLore(List<String> lore) {
		this.meta.setLore(lore);
		updateStack();
		return this;
	}

	/**
	 * Set the lore
	 * 
	 * @param lore
	 *            Strings (null to remove lore)
	 */
	public ItemBuilder setLore(String... lore) {
		if (lore.length == 0)
			this.meta.setLore(null);
		else {
			List<String> list = new ArrayList<String>();
			for (String line : lore)
				list.add(line);
			this.meta.setLore(list);
		}
		updateStack();
		return this;
	}

	/**
	 * Set the name of the stack
	 * 
	 * @param name
	 *            Display name
	 */
	public ItemBuilder setName(String name) {
		this.meta.setDisplayName(name);
		updateStack();
		return this;
	}

	/**
	 * Add an enchantment to the stack
	 * 
	 * @param enchantment
	 *            Enchantment type
	 * @param level
	 *            Integer level of enchantment
	 */
	public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
		Validate.notNull(enchantment, "Enchantment cannot be null");
		stack.addUnsafeEnchantment(enchantment, level);
		updateMeta();
		return this;
	}

	/**
	 * Add a map of enchantments to the stack
	 * 
	 * @param enchantments
	 *            Map of enchantments to add
	 */
	public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
		Validate.notNull(enchantments, "The enchantment map cannot be null");
		Validate.noNullElements(enchantments.keySet(),
				"Cannot add null enchantments");
		Validate.noNullElements(enchantments.values(),
				"Cannot add enchantments with null levels");
		stack.addUnsafeEnchantments(enchantments);
		updateMeta();
		return this;
	}

	/**
	 * Remove an enchantment from the stack
	 * 
	 * @param enchantment
	 *            Enchantment to remove
	 */
	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		stack.removeEnchantment(enchantment);
		updateMeta();
		return this;
	}

	/**
	 * Remove all the enchantments from the stack
	 */
	public ItemBuilder clearEnchantments() {
		for (Enchantment ench : stack.getEnchantments().keySet())
			stack.removeEnchantment(ench);
		updateMeta();
		return this;
	}

	/**
	 * Set a fake enchantment glow on or off
	 * <p>
	 * Note: This will not remove the enchantment glow if the stack has
	 * enchantments on it
	 */
	public ItemBuilder setGlow(boolean bool) {
		if (!(bool == true || bool == false))
			this.glow = false;
		else
			this.glow = bool;
		return this;
	}

	/**
	 * Set the ItemMeta of the stack
	 * 
	 * @param meta
	 *            ItemMeta to set
	 */
	public ItemBuilder setItemMeta(ItemMeta meta) {
		setMeta(meta);
		updateStack();
		return this;
	}

	/**
	 * Get the stack that was built
	 * 
	 * @return The built stack
	 */
	public ItemStack getStack() {
		if (glow)
			return ItemGlow.addGlow(stack);
		else if (!stack.getItemMeta().hasEnchants())
			return ItemGlow.removeGlow(stack);
		return stack;
	}

	/**
	 * Get the ItemMeta of the ItemStack that was built
	 * 
	 * @return The ItemMeta of the stack built
	 */
	public ItemMeta getMeta() {
		return meta;
	}
}
