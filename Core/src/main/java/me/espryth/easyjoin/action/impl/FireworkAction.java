package me.espryth.easyjoin.action.impl;

import me.espryth.easyjoin.action.AbstractAction;
import me.espryth.easyjoin.action.ActionType;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkAction extends AbstractAction {

    public FireworkAction(String line) {
        super(ActionType.FIREWORK, line);
    }

    @Override
    public void execute(Player player) {

        String[] fireworkValues = getLine().split(";");

        FireworkEffect.Type fireworkEffect = FireworkEffect.Type.valueOf(fireworkValues[0]);

        Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        int amount = Integer.parseInt(fireworkValues[1])-1;
        int power = Integer.parseInt(fireworkValues[2]);

        String[] colorValues = fireworkValues[3].split(",");
        String[] otherColorValues = fireworkValues[4].split(",");
        String[] fadeValues = fireworkValues[5].split(",");
        String[] otherFadeValues = fireworkValues[6].split(",");

        Color color = Color.fromBGR(
                Integer.parseInt(colorValues[0]), Integer.parseInt(colorValues[1]), Integer.parseInt(colorValues[2]));
        Color otherColor = Color.fromBGR(
                Integer.parseInt(otherColorValues[0]), Integer.parseInt(otherColorValues[1]), Integer.parseInt(otherColorValues[2]));
        Color fade = Color.fromBGR(
                Integer.parseInt(fadeValues[0]), Integer.parseInt(fadeValues[1]), Integer.parseInt(fadeValues[2]));
        Color otherFade = Color.fromBGR(
                Integer.parseInt(otherFadeValues[0]), Integer.parseInt(otherFadeValues[1]), Integer.parseInt(otherFadeValues[2]));

        fireworkMeta.setPower(power);
        fireworkMeta.addEffect(
                FireworkEffect.builder()
                        .with(fireworkEffect)
                        .withColor(color, otherColor)
                        .withFade(fade, otherFade)
                        .trail(true)
                        .build());

        firework.setFireworkMeta(fireworkMeta);

        for(int i = 0; i < amount; i++) {
            Firework otherFirework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            otherFirework.setFireworkMeta(fireworkMeta);
        }
    }
}
