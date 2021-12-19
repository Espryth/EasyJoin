package me.espryth.easyjoin.adapt.legacy.v1_9_R2;

import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PlayerConnection;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packets {
  public static void send(Player player, Packet<?>... packets) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    for (Packet<?> packet : packets) {
      connection.sendPacket(packet);
    }
  }
}
