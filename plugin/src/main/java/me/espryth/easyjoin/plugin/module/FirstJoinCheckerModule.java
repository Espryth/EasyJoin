package me.espryth.easyjoin.plugin.module;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.plugin.database.SQLSource;
import me.espryth.easyjoin.plugin.format.firstjoin.FirstJoinChecker;
import me.espryth.easyjoin.plugin.format.firstjoin.SQLFirstJoinChecker;
import me.espryth.easyjoin.plugin.format.firstjoin.VanillaFirstJoinChecker;
import me.espryth.easyjoin.plugin.utils.YamlFile;

public class FirstJoinCheckerModule implements StoranceModule {

  private final YamlFile config;
  private final YamlFile credentials;

  public FirstJoinCheckerModule(YamlFile config, YamlFile credentials) {
    this.config = config;
    this.credentials = credentials;
  }

  @Override
  public void configure(Binder binder) {
    String mode = config.getString("FirstJoinMode", "VANILLA");
    FirstJoinChecker checker;
    if(mode.equalsIgnoreCase("MYSQL")) {
      SQLSource source = new SQLSource(credentials.getConfigurationSection("credentials"));
      checker = new SQLFirstJoinChecker(source);
    } else {
      checker = new VanillaFirstJoinChecker();
    }
    binder.bind(FirstJoinChecker.class).toInstance(checker);
  }
}
