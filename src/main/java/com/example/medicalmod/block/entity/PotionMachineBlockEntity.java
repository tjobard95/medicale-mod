package com.example.medicalmod.block.entity;

import com.example.medicalmod.inventory.ImplementedInventory;
import com.example.medicalmod.item.ModItems;
import com.example.medicalmod.potion.ModPotions;
import com.example.medicalmod.screen.PotionMachineScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Cerveau du Distillateur nutritif.
 *
 * Les 3 potions exclusives du mod se fabriquent ici, et nulle part ailleurs :
 *   bouteille d'eau + tarte a la citrouille -> Saturation
 *   bouteille d'eau + seringue              -> Adrenaline
 *   bouteille d'eau + kit de soin           -> Serum de premiers secours
 *
 * Les ingredients restent craftables par tous : les joueurs peuvent t'en vendre,
 * mais seul le proprietaire de la machine peut les transformer en potion.
 */
public class PotionMachineBlockEntity extends BlockEntity
        implements NamedScreenHandlerFactory, ImplementedInventory {

    public static final int SLOT_BOTTLE = 0;
    public static final int SLOT_INGREDIENT = 1;
    public static final int SLOT_OUTPUT = 2;

    /** Duree de fabrication, en ticks (200 = 10 secondes). */
    private static final int CRAFT_TIME = 200;

    /** Table des recettes : ingredient -> potion produite. */
    public static final Map<Item, Potion> RECIPES = new LinkedHashMap<>();

    /** Rempli au chargement du mod, une fois les items enregistres. */
    public static void initRecipes() {
        RECIPES.put(Items.PUMPKIN_PIE, ModPotions.SATURATION);
        RECIPES.put(ModItems.SYRINGE, ModPotions.ADRENALINE);
        RECIPES.put(ModItems.MEDKIT, ModPotions.FIRST_AID);
    }

    public static boolean isValidIngredient(ItemStack stack) {
        return !stack.isEmpty() && RECIPES.containsKey(stack.getItem());
    }

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
    private int progress = 0;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return index == 0 ? progress : CRAFT_TIME;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                progress = value;
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public PotionMachineBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.POTION_MACHINE, pos, state);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.inventory;
    }

    // ---------------------------------------------------------------- ecran

    @Override
    public Text getDisplayName() {
        return Text.translatable("block.medicalmod.potion_machine");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new PotionMachineScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    // ----------------------------------------------------------- sauvegarde

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("Progress", this.progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("Progress");
    }

    // -------------------------------------------------------------- logique

    public static void tick(World world, BlockPos pos, BlockState state, PotionMachineBlockEntity machine) {
        if (world.isClient) {
            return;
        }

        Potion result = machine.currentResult();
        if (result != null) {
            machine.progress++;
            markDirty(world, pos, state);

            if (machine.progress >= CRAFT_TIME) {
                machine.craft(result);
                machine.progress = 0;
            }
        } else if (machine.progress != 0) {
            machine.progress = 0;
            markDirty(world, pos, state);
        }
    }

    /** @return la potion fabricable maintenant, ou null si la machine ne peut pas tourner. */
    @Nullable
    private Potion currentResult() {
        ItemStack bottle = this.getStack(SLOT_BOTTLE);
        ItemStack ingredient = this.getStack(SLOT_INGREDIENT);

        boolean waterOk = bottle.isOf(Items.POTION) && PotionUtil.getPotion(bottle) == Potions.WATER;
        if (!waterOk) {
            return null;
        }
        Potion result = RECIPES.get(ingredient.getItem());
        if (result == null) {
            return null;
        }

        // La sortie doit etre vide, ou contenir deja la meme potion sans etre pleine.
        ItemStack output = this.getStack(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return result;
        }
        boolean sameOutput = output.isOf(Items.POTION)
                && PotionUtil.getPotion(output) == result
                && output.getCount() < output.getMaxCount();
        return sameOutput ? result : null;
    }

    private void craft(Potion result) {
        this.removeStack(SLOT_BOTTLE, 1);
        this.removeStack(SLOT_INGREDIENT, 1);

        ItemStack output = this.getStack(SLOT_OUTPUT);
        if (output.isEmpty()) {
            this.setStack(SLOT_OUTPUT, PotionUtil.setPotion(new ItemStack(Items.POTION), result));
        } else {
            output.increment(1);
        }
        this.markDirty();
    }
}
