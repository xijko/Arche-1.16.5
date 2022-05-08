package net.xijko.arche.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class ArcheArtifactBroken extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    public int archeTier;

    //public List<Item> comps = new ArrayList<Item>();
    //public List<Integer> quants = new ArrayList<Integer>();

    public Item comp1;
    private int comp1Quant;

    public Item comp2;
    private int comp2Quant;

    public Item comp3;
    private int comp3Quant;

    public Item comp4;
    private int comp4Quant;

    public Item artifactOut;

    private int componentTotal;

    public ArcheArtifactBroken(Item artifactOut, int archeTier, Item A, int a, Item B, int b, Item C, int c, Item D, int d) {
        super(new Properties().group(ModItemGroup.ARCHE_GROUP).maxStackSize(1).maxDamage(a+b+c+d+1));
        this.artifactOut = artifactOut;
        this.archeTier = archeTier;
        this.comp1Quant = a;
        if (a != 0){
            this.comp1 = A;
        }else{
            this.comp1 = null;
        }
        this.comp2Quant = b;
        if (b != 0){
            this.comp2 = B;
        }else{
            this.comp2 = null;
        }
        this.comp3Quant = c;
        if (c != 0){
            this.comp3 = C;
        }else{
            this.comp3 = null;
        }
        this.comp4Quant = d;
        if (d != 0){
            this.comp4 = D;
        }else{
            this.comp4 = null;
        }
        this.componentTotal = a+b+c+d;
    }

    public int getComp1Quant(){
        return this.comp1Quant;
    }
    public int getComp2Quant(){
        return this.comp2Quant;
    }
    public int getComp3Quant(){
        return this.comp3Quant;
    }
    public int getComp4Quant(){
        return this.comp4Quant;
    }

    public int getArcheTier() {
        return this.archeTier;
    }

    public CompoundNBT getDefaultNBT(){
        CompoundNBT defaultNBT = new CompoundNBT();
        defaultNBT.putInt("comp1quant",this.comp1Quant);
        defaultNBT.putInt("comp2quant",this.comp2Quant);
        defaultNBT.putInt("comp3quant",this.comp3Quant);
        defaultNBT.putInt("comp4quant",this.comp4Quant);
        return defaultNBT;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if(stack.getTag() == null){
            ArcheArtifactBroken artifactItem = (ArcheArtifactBroken) stack.getItem();
            stack.setTag(artifactItem.getDefaultNBT());
        }
        return super.getUseAction(stack);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        LOGGER.warn("Max Damage is "+stack.getMaxDamage());
        LOGGER.warn("Current Damage is " + stack.getDamage());
        LOGGER.warn("NBT is "+stack.getTag());
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.getTag().contains("comp1quant");
    }



    private void setDamage(ItemStack stack){
        int quant1;
        int quant2;
        int quant3;
        int quant4;
        quant1 = stack.getTag().getInt("comp1quant");
        quant2 = stack.getTag().getInt("comp2quant");
        quant3 = stack.getTag().getInt("comp3quant");
        quant4 = stack.getTag().getInt("comp4quant");
        stack.setDamage(quant1+quant2+quant3+quant4);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        if (stack.getDamage()==0){
            stack.setTag(this.getDefaultNBT());
            this.setDamage(stack);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        int quant1;
        int quant2;
        int quant3;
        int quant4;

        if (stack.getTag() !=null){
            if(stack.getTag().getInt("comp1quant")>=0){
                quant1 = stack.getTag().getInt("comp1quant");
            }else{
                quant1 = this.comp1Quant;
            }
            if(stack.getTag().getInt("comp2quant")>=0){
                quant2 = stack.getTag().getInt("comp2quant");
            }else{
                quant2 = this.comp2Quant;
            }
            if(stack.getTag().getInt("comp3quant")>=0){
                quant3 = stack.getTag().getInt("comp3quant");
            }else{
                quant3 = this.comp3Quant;
            }
            if(stack.getTag().getInt("comp4quant")>=0){
                quant4 = stack.getTag().getInt("comp4quant");
            }else{
                quant4 = this.comp4Quant;
            }
            //this.setDamage(stack,quant1+quant2+quant3+quant4);
            //String line1 = this.comp1.getDisplayName(new ItemStack(this.comp1)) + " x"+quant1;
            //ITextComponent line0 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent("tooltip.arche.artifact_progress").getString() + progress+"/"+(maxDurability-1)+"("+this.getDamage(stack)+")");
            ITextComponent line1 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp1.getTranslationKey()).getString() + " x"+quant1);
            ITextComponent line2 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp2.getTranslationKey()).getString() +" x"+quant2);
            ITextComponent line3 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp3.getTranslationKey()).getString()+" x"+quant3);
            ITextComponent line4 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp4.getTranslationKey()).getString()+" x"+quant4);

            if(Screen.hasShiftDown()) {
                //tooltip.add(line0);
                tooltip.add(line1);
                tooltip.add(line2);
                tooltip.add(line3);
                tooltip.add(line4);
            } else {
                tooltip.add(new TranslationTextComponent("tooltip.arche.artifact_hover"));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);

    }
}
