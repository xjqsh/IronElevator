package me.xjqsh.ironelevator;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class Elevator {

    private final int minDistance;
    private final int maxDistance;

    private final boolean isIA;

    private final List<String> upCommands;

    private final List<String> downCommands;

    private Material material;

    private String iaNameSpaceId;

    private final String name;

    protected Elevator(String name,int min, int max, Material material, List<String> upCommands, List<String> downCommands){
        isIA=false;
        this.maxDistance=max;
        this.minDistance=min;
        this.material=material;
        this.upCommands=upCommands;
        this.downCommands=downCommands;
        this.name=name;
    }

    protected Elevator(String name,int min, int max, String iaNameSpaceId, List<String> upCommands, List<String> downCommands){
        isIA=true;
        this.maxDistance=max;
        this.minDistance=min;
        this.iaNameSpaceId=iaNameSpaceId;
        this.upCommands=upCommands;
        this.downCommands=downCommands;
        this.name=name;
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

    public String getName(){
        return this.name;
    }

    public boolean use(Player player,Block block,boolean up){
        Location location = block.getLocation();
        for(int i=minDistance;i<=maxDistance;i++){
            Location target = location.add(0,(up ? 1 : -1),0);
//            player.sendMessage(target.toString());
            if(isIA){
                CustomBlock customBlock = CustomBlock.byAlreadyPlaced(target.getBlock());
                if(customBlock!=null && customBlock.getNamespacedID().equals(iaNameSpaceId)){
                    if(safeTeleport(player,target,up))return true;
                }
            }
            else if(target.getBlock().getType() == material){
//                player.sendMessage(target.getBlock().getType().name());
                if(safeTeleport(player,target,up))return true;
            }
        }
        return false;
    }

    private boolean safeTeleport(Player player,Location target,boolean up){
        Location s =  new Location(target.getWorld(),target.getX(),target.getY()+1,target.getZ());
        if(s.getBlock().getType()==Material.AIR && s.add(0,1,0).getBlock().getType()==Material.AIR){
            target.add(0.5,1.1,0.5);
            target.setPitch(player.getLocation().getPitch());
            target.setYaw(player.getLocation().getYaw());
            player.teleport(target, PlayerTeleportEvent.TeleportCause.PLUGIN);

            List<String> commands = (up ? upCommands : downCommands);

            if(commands !=null){
                CommandSender console = Bukkit.getConsoleSender();
                for (String command : commands){
                    Bukkit.dispatchCommand(console,command.replace("[playerName]",player.getName()));
                }
            }

            return true;
        }
        return false;
    }
}
