package net.xijko.arche.item;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
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

    private static int durability;
    private static int maxDurability;

    public ArcheArtifactBroken(Item artifactOut, int archeTier, Item A, int a, Item B, int b, Item C, int c, Item D, int d) {
        super(new Properties().group(ModItemGroup.ARCHE_GROUP).maxStackSize(1));
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
        //this.durability = 1;
        //this.maxDurability = a+b+c+d+1;
        //this.getDefaultInstance().setDamage(this.maxDurability);
        //this.updateItemStackNBT(initNBT(this));
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

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
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
            //String line1 = this.comp1.getDisplayName(new ItemStack(this.comp1)) + " x"+quant1;
            ITextComponent line1 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp1.getTranslationKey()).getString() + " x"+quant1);
            ITextComponent line2 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp2.getTranslationKey()).getString() +" x"+quant2);
            ITextComponent line3 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp3.getTranslationKey()).getString()+" x"+quant3);
            ITextComponent line4 = ITextComponent.getTextComponentOrEmpty(new TranslationTextComponent(this.comp4.getTranslationKey()).getString()+" x"+quant4);
            if(Screen.hasShiftDown()) {
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

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return super.damageItem(stack, amount, entity, onBroken);
    }
}
