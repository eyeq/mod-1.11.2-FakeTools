package eyeq.faketools;

import eyeq.faketools.item.ItemFakeTool;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.creativetab.UCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

import static eyeq.faketools.FakeTools.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:*")
@Mod.EventBusSubscriber
public class FakeTools {
    public static final String MOD_ID = "eyeq_faketools";

    @Mod.Instance(MOD_ID)
    public static FakeTools instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static final CreativeTabs TAB_FAKE = new UCreativeTab("FakeTools", () -> new ItemStack(Items.WOODEN_SHOVEL));

    public static ItemFakeTool fakeTool;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ItemFakeTool.init();
        if(event.getSide().isServer()) {
            return;
        }
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        fakeTool = (ItemFakeTool) new ItemFakeTool().setUnlocalizedName("fakeTool");

        GameRegistry.register(fakeTool, resource.createResourceLocation("fake_tool"));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        for(int i = 0; i < ItemFakeTool.META_TO_ITEM_MAPPING.size(); i++) {
            Item item = ItemFakeTool.getItemFromMeta(i);
            ModelLoader.setCustomModelResourceLocation(fakeTool, i, ResourceLocationFactory.createModelResourceLocation(item.getRegistryName()));
        }
    }

    public static void createFiles() {
        File project = new File("../1.11.2-FakeTools");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, TAB_FAKE, "FakeTools");
        language.register(LanguageResourceManager.JA_JP, TAB_FAKE, "偽ツール");

        ULanguageCreator.createLanguage(project, MOD_ID, language);
    }
}
