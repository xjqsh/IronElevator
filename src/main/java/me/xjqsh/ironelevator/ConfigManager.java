package me.xjqsh.ironelevator;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager implements Listener {

    private static ConfigManager instance;

    private Configuration config;

    private final Map<Material,Elevator> vanillaElevators;
    private final Map<String,Elevator> iaElevators;


    public ConfigManager(){
        instance=this;
        config=IronElevator.getInstance().getConfig();
        vanillaElevators=new HashMap<>();
        iaElevators=new HashMap<>();
        if(Bukkit.getPluginManager().getPlugin("ItemsAdder")==null)initElevators(false);
        else Bukkit.getPluginManager().registerEvents(this,IronElevator.getInstance());
    }

    public static long getCoolDown(){
        return instance.config.getLong("CoolDown");
    }

    @EventHandler
    public void onIaReload(ItemsAdderLoadDataEvent event){
        initElevators(true);
    }

    public static String getMessage(String key){
        String message = instance.config.getString("Messages."+key);
        if(message!=null)return ChatColor.translateAlternateColorCodes('&',message);
        throw new NullPointerException("没有找到为"+key+"的消息");
    }

    @SuppressWarnings("DuplicatedCode")
    public static void initElevators(boolean isReload){
        if(isReload){
            IronElevator.getInstance().reloadConfig();
            instance.config=IronElevator.getInstance().getConfig();
        }
        instance.vanillaElevators.clear();
        instance.iaElevators.clear();
        ConfigurationSection section = instance.config.getConfigurationSection("Elevators");
        if(section==null) {
            IronElevator.getInstance().getLogger().warning("没有找到电梯配置");
            return;
        }
        for(String key : section.getKeys(false)){
            boolean isIA = section.getBoolean(key+".itemsadder");
            if(!isIA){
                String material = section.getString(key+".material");
                if(material==null){
                    IronElevator.getInstance().getLogger().warning(key+"的material属性为空");
                    continue;
                }

                Material m = Material.getMaterial(material);
                if(m==null || !m.isBlock()){
                    IronElevator.getInstance().getLogger().warning(key+"指定的不是一个正确的原版方块");
                    continue;
                }

                int minD=section.getInt(key+".minDistance");
                int maxD=section.getInt(key+".maxDistance");
                if(minD<=0 || maxD<=0 || minD>maxD){
                    IronElevator.getInstance().getLogger().warning(key+"指定的最大最小值错误");
                    continue;
                }

                List<String> upCommands = section.getStringList(key+".upCommands");
                List<String> downCommands = section.getStringList(key+".downCommands");

                Elevator elevator = new Elevator(key,minD,maxD,m,upCommands,downCommands);
                instance.vanillaElevators.put(m,elevator);
            }else{
                String iaNameSpaceID = section.getString(key+".material");
                if(iaNameSpaceID==null){
                    IronElevator.getInstance().getLogger().warning(key+"的itemsadder命名空间id为空");
                    continue;
                }

                CustomBlock customBlock = CustomBlock.getInstance(iaNameSpaceID);
                if(customBlock==null){
                    IronElevator.getInstance().getLogger().warning(key+"指定的不是一个正确的ia方块");
                    continue;
                }

                int minD=section.getInt(key+".minDistance");
                int maxD=section.getInt(key+".maxDistance");
                if(minD<=0 || maxD<=0 || minD>maxD){
                    IronElevator.getInstance().getLogger().warning(key+"指定的最大最小值错误");
                    continue;
                }

                List<String> upCommands = section.getStringList(key+".upCommands");
                List<String> downCommands = section.getStringList(key+".downCommands");

                Elevator elevator = new Elevator(key,minD,maxD,iaNameSpaceID,upCommands,downCommands);
                instance.iaElevators.put(iaNameSpaceID,elevator);
            }
        }
    }

    protected static Elevator getElevator(Material vanilla){
        return instance.vanillaElevators.get(vanilla);
    }

    protected static Elevator getElevator(String ia){
        return instance.iaElevators.get(ia);
    }
}
