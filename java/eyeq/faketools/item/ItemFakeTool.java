package eyeq.faketools.item;

import com.google.common.collect.Multimap;
import eyeq.faketools.FakeTools;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class ItemFakeTool extends Item {
    public static final List<Class> ITEM_TOOL_CLASS = new ArrayList<>();
    public static final List<Item> META_TO_ITEM_MAPPING = new ArrayList<>();

    static {
        ITEM_TOOL_CLASS.add(ItemBow.class);
        ITEM_TOOL_CLASS.add(ItemBucket.class);
        ITEM_TOOL_CLASS.add(ItemCarrotOnAStick.class);
        ITEM_TOOL_CLASS.add(ItemFlintAndSteel.class);
        ITEM_TOOL_CLASS.add(ItemFishingRod.class);
        ITEM_TOOL_CLASS.add(ItemHoe.class);
        ITEM_TOOL_CLASS.add(ItemShears.class);
        ITEM_TOOL_CLASS.add(ItemSword.class);
        ITEM_TOOL_CLASS.add(ItemTool.class);
    }

    public ItemFakeTool() {
        this.setFull3D();
        this.setMaxStackSize(1);
        this.setCreativeTab(FakeTools.TAB_FAKE);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getItemStackLimit(new ItemStack(item));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getUnlocalizedName(new ItemStack(item));
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getAttributeModifiers(slot, new ItemStack(item));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.hasEffect(new ItemStack(item));
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        if(player.isCreative()) {
            return false;
        }
        World world = player.getEntityWorld();
        if(world.isRemote) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state);
        world.notifyBlockUpdate(pos, state, state, 3);

        state = world.getBlockState(pos.up());
        world.notifyBlockUpdate(pos.up(), state, state, 3);

        state = world.getBlockState(pos.down());
        world.notifyBlockUpdate(pos.down(), state, state, 3);
        return true;
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getStrVsBlock(new ItemStack(item), state);
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.canHarvestBlock(state, new ItemStack(item));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getMaxItemUseDuration(new ItemStack(item));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        Item item = getItemFromMeta(stack.getMetadata());
        return item.getItemUseAction(new ItemStack(item));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for(int i = 0; i < META_TO_ITEM_MAPPING.size(); i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }

    public static boolean isTool(Item item) {
        if(item == null) {
            return false;
        }
        Class itemClass = item.getClass();
        for(Class aClass : ITEM_TOOL_CLASS) {
            if(aClass.isAssignableFrom(itemClass)) {
                return true;
            }
        }
        return false;
    }

    public static void init() {
        for(Item item : Item.REGISTRY) {
            if(isTool(item)) {
                META_TO_ITEM_MAPPING.add(item);
            }
        }
    }

    public static Item getItemFromMeta(int meta) {
        return META_TO_ITEM_MAPPING.get(meta);
    }
}
