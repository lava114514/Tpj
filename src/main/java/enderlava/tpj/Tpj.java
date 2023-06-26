package enderlava.tpj;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Tpj extends JavaPlugin {

    private Map<Player, Player> teleportRequests;

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
                sender.sendMessage("该命令只能由玩家执行！");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage("用法: /tpj <玩家名>");
                return true;
            }

            Player player = (Player) sender;
            Player target = getServer().getPlayer(args[0]);

            if (target == null) {
                sender.sendMessage("找不到指定的玩家！");
                return true;
            }

            teleportRequests.put(target, player);
            sender.sendMessage("传送请求已发送给玩家 " + target.getName() + "，请等待对方的回应！");
            target.sendMessage("玩家 " + player.getName() + " 请求传送到你的位置。同意请使用命令: /tpjaccept");

            return true;
        }

        if (label.equalsIgnoreCase("tpjaccept")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("该命令只能由玩家执行！");
                return true;
            }

            Player target = (Player) sender;
            Player requester = teleportRequests.get(target);

            if (requester == null) {
                sender.sendMessage("没有待处理的传送请求！");
                return true;
            }

            requester.teleport(target.getLocation());
            requester.sendMessage("你已经传送到了玩家 " + target.getName() + " 的位置！");
            teleportRequests.remove(target);
            return true;
        }

        return false;
    }
}
