package enderlava.tpj;

import org.bukkit.plugin.java.JavaPlugin;

public final class Tpj extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tpj")) return true;
        else
            return false;
    }
}


