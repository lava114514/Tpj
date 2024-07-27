package enderlava.tpj;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class Tpj extends JavaPlugin {

    private Map<Player, Player> teleportRequests;

    @Override
    public void onEnable() {
        teleportRequests = new HashMap<>();
        // 注册其他必要的逻辑和事件监听器

        // 注册 /tpjhelp 命令
        getCommand("tpjhelp").setExecutor(this);
    }

    @Override
    public void onDisable() {
        teleportRequests.clear();
        // 禁用时的清理逻辑
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("tpj")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.AQUA +"该命令只能由玩家执行！");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA +"用法: /tpj <玩家名>");
                return true;
            }

            Player player = (Player) sender;
            Player target = getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.AQUA +"找不到指定的玩家！");
                return true;
            }

            teleportRequests.put(target, player);
            sender.sendMessage(ChatColor.AQUA +"传送请求已发送给玩家 " + target.getName() + "，请等待对方的回应！");
            target.sendMessage(ChatColor.AQUA +"玩家 " + player.getName() + " 请求传送到你的位置。同意请使用命令: /tpjaccept 或 /y");

            return true;
        }

        if (label.equalsIgnoreCase("tpjaccept")||label.equalsIgnoreCase("y")||label.equalsIgnoreCase("tpyes")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.AQUA +"该命令只能由玩家执行！");
                return true;
            }

            Player target = (Player) sender;
            Player requester = teleportRequests.get(target);

            if (requester == null) {
                sender.sendMessage(ChatColor.AQUA +"没有待处理的传送请求！");
                return true;
            }

            requester.teleport(target.getLocation());
            requester.sendMessage(ChatColor.AQUA +"你已经传送到了玩家 " + target.getName() + " 的位置！");
            requester.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 5));
            teleportRequests.remove(target);
            return true;
        }

        if (label.equalsIgnoreCase("tpjhelp")) {
            sender.sendMessage(ChatColor.AQUA + "=== TPJ 插件帮助 ===");
            sender.sendMessage(ChatColor.YELLOW + "/tpj <玩家名> - 发送传送请求给指定玩家");
            sender.sendMessage(ChatColor.YELLOW + "/tpjaccept - 接受传送请求");
            sender.sendMessage(ChatColor.YELLOW + "/tpt <x> <y> <z> - 传送到指定坐标");
            return true;
        }

        if (label.equalsIgnoreCase("tpt")) {
            Player target = (Player) sender;
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.AQUA +"该命令只能由玩家执行！");
                return true;
            }

            if (args.length <= 2) {
                sender.sendMessage(ChatColor.AQUA +"用法: /tpt <x> <y> <z>");
                return true;
            }
            target.teleport(new Location(target.getWorld(), Double.parseDouble(args[0]),Double.parseDouble(args[1]),Double.parseDouble(args[2])));
            sender.sendMessage(ChatColor.AQUA +"成功传送到"+args[0]+" "+args[1]+" "+args[2]);
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 5));
            return true;
        }
        return false;
    }
}