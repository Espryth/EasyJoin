package me.espryth.easyjoin.listeners.authme;

import fr.xephi.authme.events.RegisterEvent;
import me.espryth.easyjoin.Core;
import me.espryth.easyjoin.format.Format;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterListener implements Listener {

    private final Core core;

    public RegisterListener(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerRegister(RegisterEvent event) {

        Player player  = event.getPlayer();

        List<Integer> priorityList = new ArrayList<>();

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier())) {
                priorityList.add(format.getPriority());
            }
        }

        int highPriority = Collections.<Integer>max(priorityList);

        for(Format format : core.getFormatMap().values()) {
            if(player.hasPermission("easyjoin." + format.getIdentifier()) && format.getPriority() == highPriority) {
                if(format.isFirstJoinFormat()) {
                    format.getAuthMeActions().forEach(action -> action.execute(player));
                }
                return;
            }
        }
    }
}
