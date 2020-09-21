package net.maroonangel.cultivation.lib.crop;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.maroonangel.cultivation.lib.dispenser.SeedDispenserBehavior;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.Material;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

public class CropUnfertilizable {

    public Identifier id;

    private Item item;
    private CropItem seed;
    private CropBlock block;
    private FoodComponent food;
    private Rarity rarity;

    private ItemGroup seedgroup;
    private ItemGroup itemgroup;

    private boolean seperateseed;
    private boolean twotall;
    private boolean partialharvest;
    private boolean customItem;

    private int tickRate;

    private VoxelShape[] growthBoundingBoxes;

    public CropUnfertilizable(Identifier id) {
        this.id = id;
        this.seperateseed = false;
        this.twotall = false;
        this.partialharvest = false;
        this.customItem = false;
        this.seedgroup = ItemGroup.MISC;
        this.itemgroup = ItemGroup.FOOD;
        this.food = null;
        this.growthBoundingBoxes = null;
        this.tickRate = 25;
        this.rarity = Rarity.COMMON;
    }

    public CropUnfertilizable setSeperateSeed(boolean seperateseed) {
        this.seperateseed = seperateseed;
        return this;
    }

    public Identifier getSeedId() {
        Identifier seedid = this.id;
        if (this.seperateseed) {
            seedid = new Identifier(this.id.getNamespace(), this.id.getPath() + "_seeds");
        }
        return seedid;
    }

    public Item getItem() {
        return this.item;
    }

    public Item getSeed() {
        return block.getSeedsItem().asItem();
    }

    public Block getBlock() {
        return this.block;
    }

    public CropUnfertilizable setFood(FoodComponent food) {
        this.food = food;
        return this;
    }

    public FoodComponent getFood() {
        return this.food;
    }

    public CropUnfertilizable setSeedGroup(ItemGroup group) {
        this.seedgroup = group;
        return this;
    }

    public CropUnfertilizable setItemGroup(ItemGroup group) {
        this.itemgroup = group;
        return this;
    }

    public CropUnfertilizable setPartialHarvest(boolean partialharvest) {
        this.partialharvest = partialharvest;
        return this;
    }

    public CropUnfertilizable setCropGrowthBoundingBoxes(VoxelShape[] growthBoundingBoxes) {
        this.growthBoundingBoxes = growthBoundingBoxes;
        return this;
    }

    public CropUnfertilizable setTwoTall(boolean twotall) {
        this.twotall = twotall;
        return this;
    }

    public CropUnfertilizable setItem(Item item) {
        this.item = item;
        this.customItem = true;
        return this;
    }

    public CropUnfertilizable setSeedRarity(Rarity rarity) {
        this.rarity = rarity;
        return this;
    }

    public CropUnfertilizable setTickRate(int rate) {
        this.tickRate = rate;
        return this;
    }

    public CropUnfertilizable build() {
        this.block = new CropBlock(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).build(), this.id, this.getSeedId())
                .setGrowthBoundingBoxes(this.growthBoundingBoxes)
                .setTwoTall(this.twotall)
                .setPartialHarvest(this.partialharvest)
                .setTickRate(this.tickRate)
                .setFertilizable(false);


        if (this.seperateseed) {
            this.seed = new CropItem(this.block, new Item.Settings().group(this.seedgroup).rarity(this.rarity));
            if (item == null)
                this.item = new Item(new Item.Settings().group(this.itemgroup).food(this.food));
        } else {
            this.item = new CropItem(this.block, new Item.Settings().group(this.itemgroup).food(this.food));
        }
        return this;
    }

    public void register() {
        if (!customItem)
            Registry.register(Registry.ITEM, this.id, this.item);
        this.block.register(this.id);
        if (seperateseed) {
            Registry.register(Registry.ITEM, this.getSeedId(), this.seed);
        }
        DispenserBlock.registerBehavior(this.getSeed(), new SeedDispenserBehavior());
    }


}