package me.espryth.easyjoin.adapt.legacy.v1_8_R3;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Packets {

  public static void send(Player player, Packet<?>... packets) {
    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
    for (Packet<?> packet : packets) {
      connection.sendPacket(packet);
    }
  }

}
