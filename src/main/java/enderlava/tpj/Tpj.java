package enderlava.tpj;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
public class Tpj extends JavaPlugin {

    private Map<Player, Player> teleportRequests;
    private Map<Player, Player> teleportAccepts = new HashMap<>();
    @Override
    public void onEnable() {
        teleportRequests = new HashMap<>();
        // 注册其他必要的逻辑和事件监听器
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
                sender.sendMessage(ChatColor.AQUA + "该命令只能由玩家执行！");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(ChatColor.AQUA + "用法: /tpj <玩家名>");
                return true;
            }

            Player player = (Player) sender;
            Player target = getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage(ChatColor.AQUA + "找不到指定的玩家！");
                return true;
            }

            teleportRequests.put(target, player);
            sender.sendMessage(ChatColor.AQUA + "传送请求已发送给玩家 " + target.getName() + "，请等待对方的回应！");
            target.sendMessage(ChatColor.AQUA + "玩家 " + player.getName() + " 请求传送到你的位置。同意请使用命令: /tpjaccept");

            return true;
        }

        if (label.equalsIgnoreCase("tpjaccept")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.AQUA + "该命令只能由玩家执行！");
                return true;
            }

            Player target = (Player) sender;
            Player requester = teleportRequests.get(target);

            if (requester == null) {
                sender.sendMessage(ChatColor.AQUA + "没有待处理的传送请求！");
                return true;
            }

            teleportAccepts.put(requester, target);
            requester.sendMessage(ChatColor.GREEN + "你的传送请求已被玩家 " + target.getName() + " 接受！请等待传送完成...");
            target.sendMessage(ChatColor.GREEN + "你已接受玩家 " + requester.getName() + " 的传送请求，正在传送中...");
            requester.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 5));
            // 使用 BukkitRunnable 创建一个延迟任务
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (teleportAccepts.containsKey(requester) && teleportAccepts.get(requester) == target) {
                        teleportAccepts.remove(requester);
                        requester.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3 * 20, 5));
                        requester.teleport(target.getLocation());
                        requester.sendMessage(ChatColor.GREEN + "你已经传送到了玩家 " + target.getName() + " 的位置！");
                    }
                }
            };

            if (label.equalsIgnoreCase("tpjhelp")) {
                sender.sendMessage(ChatColor.AQUA + "J志坤tp");
                sender.sendMessage(ChatColor.RED + "By Enderlava");
                sender.sendMessage(ChatColor.YELLOW + "v1.0");
                sender.sendMessage(ChatColor.YELLOW + "我劝你不要不识好歹");
                sender.sendMessage(ChatColor.AQUA + "=== TPJ 插件帮助 ===");
                sender.sendMessage(ChatColor.YELLOW + "/tpj <玩家名> - 发送传送请求给指定玩家");
                sender.sendMessage(ChatColor.YELLOW + "/tpjaccept - 接受传送请求");
                return true;
            }
            return false;
        }
        return false;
    }
}
