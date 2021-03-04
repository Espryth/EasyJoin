package me.espryth.easyjoin.listeners.authme;

import fr.xephi.authme.events.LoginEvent;
import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.format.Format;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginListener implements Listener {

    private Core core;

    public LoginListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {

        Player player  = event.getPlayer();

        List<Integer> priorityList = new ArrayList<>();

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier()) && !format.isFirstJoinFormat()) {
                priorityList.add(format.getPriority());
            }
        }

        if(priorityList.isEmpty()) return;

        int highPriority = Collections.<Integer>max(priorityList);

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier()) && format.getPriority() == highPriority) {
                if(!format.isFirstJoinFormat()) {
                    format.getAuthMeActions().forEach(action -> action.execute(player));
                }
                return;
            }
        }
    }
}
