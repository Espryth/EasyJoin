package me.espryth.easyjoin.plugin.action.impl;

import me.espryth.easyjoin.plugin.action.AbstractAction;
import me.espryth.easyjoin.plugin.action.ActionExecutionException;
import me.espryth.easyjoin.plugin.action.ActionQueue;
import org.bukkit.entity.Player;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;
import java.util.List;

public class FireworkAction extends AbstractAction {

    @Override
    public void execute(Player player, ActionQueue queue) throws ActionExecutionException{
        String[] fireworkValues = getLine().split(";");

        if(fireworkValues.length < 3) {
            throw new ActionExecutionException("Incorrect size of arguments for firework");
        }

        FireworkEffect.Type fireworkEffect;

        try {
            fireworkEffect = FireworkEffect.Type.valueOf(fireworkValues[0]);
        } catch (Exception e) {
            throw new ActionExecutionException("Invalid firework type");
        }

        Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        int amount = Integer.parseInt(fireworkValues[1])-1;
        int power = Integer.parseInt(fireworkValues[2]);

        List<Color> colors = new ArrayList<>();
        List<Color> fades = new ArrayList<>();

        for(int i = 3; i < fireworkValues.length; i++) {
            String[] colorValues = fireworkValues[i].split(",");

            if(colorValues.length != 3) {
                throw new ActionExecutionException("Invalid size of arguments for color");
            }

            int blue;
            int green;
            int red;

            try {
                blue = Integer.parseInt(colorValues[0]);
                green = Integer.parseInt(colorValues[1]);
                red = Integer.parseInt(colorValues[2]);
            } catch (NumberFormatException e) {
                throw new ActionExecutionException("Color RGB value isn't a number");
            }

            Color color = Color.fromRGB(blue, green, red);

            if(i % 2 == 0) {
                fades.add(color);
            } else {
                colors.add(color);
            }

        }

        fireworkMeta.setPower(power);
        fireworkMeta.addEffect(
                FireworkEffect.builder()
                        .with(fireworkEffect)
                        .withColor(colors)
                        .withFade(fades)
                        .trail(true)
                        .build());

        firework.setFireworkMeta(fireworkMeta);

        for(int i = 0; i < amount; i++) {
            Firework otherFirework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            otherFirework.setFireworkMeta(fireworkMeta);
        }
    }
}
