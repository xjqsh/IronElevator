package me.xjqsh.ironelevator;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.UUID;

public class ElevatorListener implements Listener {
    private final HashMap<UUID,Long> coolDownList = new HashMap<>();

    @EventHandler
    public void elevatorDown(PlayerToggleSneakEvent event){
        Player player = event.getPlayer();
        if(!isCoolDown(player))return;
        if(player.getGameMode()==GameMode.SPECTATOR || player.isFlying() || !event.isSneaking())return;

        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(!Elevator.isElevator(block))return;

        coolDown(player);
        player.sendMessage("down");
        Elevator.getElevator(block).use(player,block,false);
    }

    @EventHandler
    public void elevatorUp(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(!isCoolDown(player))return;
        if(player.getGameMode()==GameMode.SPECTATOR || player.isFlying())return;

        Location loc1 = event.getTo();
        Location loc2 = event.getFrom();
        if(loc1==null || loc1.getY()<=loc2.getY())return;

        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if(!Elevator.isElevator(block))return;

        coolDown(player);
        player.sendMessage("up");
        Elevator.getElevator(block).use(player,block,true);
    }

    public void coolDown(Player player){
        coolDownList.put(player.getUniqueId(),System.currentTimeMillis());
    }

    public boolean isCoolDown(Player player){
        if(!coolDownList.containsKey(player.getUniqueId()))return true;
        return System.currentTimeMillis()-(10)*50>coolDownList.get(player.getUniqueId());
    }
}
