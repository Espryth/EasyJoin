package me.espryth.easyjoin.adapt.legacy.v1_10_R1;

import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packets {
  public static void send(Player player, Packet<?>... packets) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    for (Packet<?> packet : packets) {
      connection.sendPacket(packet);
    }
  }
}
