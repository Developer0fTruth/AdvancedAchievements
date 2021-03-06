package com.hm.achievement.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import com.hm.achievement.AdvancedAchievements;

public class AchieveEnchantListener implements Listener {

	private AdvancedAchievements plugin;

	public AchieveEnchantListener(AdvancedAchievements plugin) {

		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEnchantItem(EnchantItemEvent event) {

		Player player = event.getEnchanter();
		if (!player.hasPermission("achievement.count.enchantments")
				|| plugin.isRestrictCreative() && player.getGameMode() == GameMode.CREATIVE
				|| plugin.isInExludedWorld(player))
			return;

		int enchantments = plugin.getPoolsManager().getPlayerEnchantmentAmount(player) + 1;

		plugin.getPoolsManager().getEnchantmentHashMap().put(player.getUniqueId().toString(), enchantments);
		
		String configAchievement = "Enchantments." + enchantments;
		if (plugin.getReward().checkAchievement(configAchievement)) {

			plugin.getAchievementDisplay().displayAchievement(player, configAchievement);
			plugin.getDb().registerAchievement(player, plugin.getPluginConfig().getString(configAchievement + ".Name"),
					plugin.getPluginConfig().getString(configAchievement + ".Message"));

			plugin.getReward().checkConfig(player, configAchievement);
		}
	}
}
