package me.espryth.easyjoin.adapt.legacy.v1_8_R3;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.adapt.BookSender;
import me.espryth.easyjoin.adapt.TitleSender;

public class AdapterModule1_8_R3
  implements StoranceModule {
  @Override
  public void configure(Binder binder) {
    binder.bind(ActionbarSender.class).toInstance(new ActionbarSender1_8_R3());
    binder.bind(BookSender.class).toInstance(new BookSender1_8_R3());
    binder.bind(TitleSender.class).toInstance(new TitleSender1_8_R3());
  }
}
