package net.xijko.arche.item;

import net.minecraft.item.Item;
import net.xijko.arche.Arche;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArcheArtifactItem extends Item {
    public int slot;
    public int archeTier;
    private static final Logger LOGGER = LogManager.getLogger();

    public ArcheArtifactItem(Properties p_i48487_1_, int slot, int archeTier) {
        super(p_i48487_1_);
        this.slot = slot;
        this.archeTier = archeTier;
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
