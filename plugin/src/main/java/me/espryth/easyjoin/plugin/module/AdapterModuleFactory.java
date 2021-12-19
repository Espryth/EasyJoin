package me.espryth.easyjoin.plugin.module;

import dev.henko.storance.StoranceModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static me.espryth.easyjoin.plugin.utils.ServerVersionProvider.*;

public class AdapterModuleFactory {

  private static final Constructor<?> CONSTRUCTOR;

  static {

    String constructorPackage = "me.espryth.easyjoin.adapt.";
    if(SERVER_VERSION_INT > 13) {
      constructorPackage += "newer.NewerAdapterModule";
    } else {
      constructorPackage += "legacy.v" + SERVER_VERSION + ".AdapterModule" + SERVER_VERSION;
    }

    try {
      CONSTRUCTOR = Class
        .forName(constructorPackage)
        .getConstructor();
    } catch (NoSuchMethodException | ClassNotFoundException e) {
      throw new IllegalArgumentException("EasyJoin cannot be executed on this server version!");
    }
  }


  public static StoranceModule create() {
    try {
      return (StoranceModule) CONSTRUCTOR.newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("An error has occurred while getting the AdapterModule", e);
    }
  }

}
