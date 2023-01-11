package me.xjqsh.ironelevator;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Elevator {

    private int minDistance;
    private int maxDistance;

    private boolean isIA;

    private String command;

    private Material material;

    private String iaNameSpaceId;

    public Elevator(){

    }

    public static boolean isElevator(Block block){
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        if(customBlock==null){
            return ConfigManager.getElevator(block.getType())!=null;
        }else {
            return ConfigManager.getElevator(customBlock.getNamespacedID())!=null;
        }
    }

    public static Elevator getElevator(Block block){
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        if(customBlock==null){
            return ConfigManager.getElevator(block.getType());
        }else {
            return ConfigManager.getElevator(customBlock.getNamespacedID());
        }
    }

    public boolean use(Player player,Block block,boolean up){
        Location location = block.getLocation();
        for(int i=minDistance;i<=maxDistance;i++){
            Location target = location.add(0,(up ? 1 : -1)*i,0);
            if(isIA){
                CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
                if(customBlock!=null && customBlock.getNamespacedID().equals(iaNameSpaceId)){
                    if(safeTeleport(player,target))return true;
                }
            }
            else if(target.getBlock().getType() == material){
                if(safeTeleport(player,target))return true;
            }
        }
        return false;
    }

    private boolean safeTeleport(Player player,Location target){
        if(target.add(0,1,0).getBlock().getType()==Material.AIR && target.add(0,2,0).getBlock().getType()==Material.AIR){
            player.teleport(target);
            return true;
        }
        return false;
    }
}
