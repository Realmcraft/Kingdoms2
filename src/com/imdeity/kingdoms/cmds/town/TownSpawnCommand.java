package com.imdeity.kingdoms.cmds.town;

import org.bukkit.entity.Player;

import com.imdeity.deityapi.api.DeityCommandReceiver;
import com.imdeity.kingdoms.main.KingdomsConfigHelper;
import com.imdeity.kingdoms.main.KingdomsMain;
import com.imdeity.kingdoms.main.KingdomsMessageHelper;
import com.imdeity.kingdoms.obj.KingdomsManager;
import com.imdeity.kingdoms.obj.Resident;
import com.imdeity.kingdoms.obj.Town;

public class TownSpawnCommand extends DeityCommandReceiver {
    
    @Override
    public boolean onPlayerRunCommand(Player player, String[] args) {
        Resident resident = KingdomsManager.getResident(player.getName());
        if (resident == null) { return false; }
        Town town = null;
        if (args.length == 0) {
            if (!resident.hasTown()) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NOT_IN_TOWN);
                return true;
            }
            town = resident.getTown();
            
            if (!resident.isKing() && !resident.isMayor() && !resident.isSeniorAssistant() && !resident.isAssistant()) {
                double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SPAWN, town
                        .getSpawnLocation().getWorld().getName()));
                if (!resident.canPay(cost)) {
                    KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
                    return true;
                }
                resident.pay(town.getEconName(), cost, "Town Spawn");
            }
            resident.teleport(town.getSpawnLocation());
            KingdomsMain.plugin.chat.sendPlayerMessage(player, town.getTownBoard());
            return true;
        } else {
            String townName = args[0];
            town = KingdomsManager.getTown(townName);
            if (town == null) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player,
                        String.format(KingdomsMessageHelper.CMD_FAIL_CANNOT_FIND_TOWN, townName));
                return true;
            }
            double cost = KingdomsMain.plugin.config.getDouble(String.format(KingdomsConfigHelper.TOWN_PRICES_SPAWN, town
                    .getSpawnLocation().getWorld().getName()));
            if (!resident.canPay(cost)) {
                KingdomsMain.plugin.chat.sendPlayerMessage(player, KingdomsMessageHelper.CMD_FAIL_NO_MONEY);
                return true;
            }
            resident.teleport(town.getSpawnLocation());
            KingdomsMain.plugin.chat.sendPlayerMessage(player, town.getTownBoard());
            resident.pay(town.getEconName(), cost, "Town Spawn");
            return true;
        }
    }
    
    @Override
    public boolean onConsoleRunCommand(String[] args) {
        return false;
    }
}
