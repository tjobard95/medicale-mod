package com.example.medicalmod.block;

import com.example.medicalmod.block.entity.ModBlockEntities;
import com.example.medicalmod.block.entity.PotionMachineBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Distillateur nutritif : machine exclusive au proprietaire du serveur.
 *
 * Aucune recette de craft n'existe pour ce bloc : il ne s'obtient que via
 * /give (niveau OP) ou l'inventaire creatif. C'est ce qui garantit le monopole,
 * bien plus surement qu'une recette a base d'items rares.
 */
public class PotionMachineBlock extends Block implements BlockEntityProvider {

    public PotionMachineBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PotionMachineBlockEntity(pos, state);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PotionMachineBlockEntity machine) {
                player.openHandledScreen(machine);
            }
        }
        return ActionResult.SUCCESS;
    }

    /** Le contenu tombe au sol si on casse la machine. */
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos,
                                BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PotionMachineBlockEntity machine) {
                ItemScatterer.spawn(world, pos, machine);
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state,
                                                                 BlockEntityType<T> type) {
        if (world.isClient) {
            return null;
        }
        return validateTicker(type, ModBlockEntities.POTION_MACHINE,
                (w, pos, s, be) -> PotionMachineBlockEntity.tick(w, pos, s, be));
    }
}
