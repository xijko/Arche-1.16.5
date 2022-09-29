package net.xijko.arche.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.xijko.arche.Arche;
import net.xijko.arche.block.MuseumCatalogBlock;
import net.xijko.arche.block.artifact.VillageRingItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcheArtifactItem extends Item {
    public int slot;
    private static final Logger LOGGER = LogManager.getLogger();

    public ArcheArtifactItem(Properties p_i48487_1_, int slot) {
        super(p_i48487_1_);
        this.slot = slot;
        //this.item = (ArcheArtifactItem) itemIn;
        addToList(slot,this);
        LOGGER.warn("Adding item to ArtifactList: "+this.toString());
        LOGGER.warn("Result: ["+this.slot+"] = "+ Arche.ARTIFACT_ITEM_LISTS[this.slot].toString());
    }

    public void addToList(int slot, ArcheArtifactItem item){
        Arche.ARTIFACT_ITEM_LISTS[slot] = item;
    }
    /*
    public ArcheArtifactItem instantantiateList(int slot){

        for (RegistryObject<Item> itemRegistryObject: ModItems.ITEMS.getEntries()
             ) {
            Item item = itemRegistryObject.get();
            if(item.getClass().isInstance(ArcheArtifactItem.class)){
                int slot = ((ArcheArtifactItem) item).slot;
            }
        }
        return this;
    }
*/

}
