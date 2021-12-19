package me.espryth.easyjoin.adapt.legacy.v1_12_R1;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.adapt.BookSender;
import me.espryth.easyjoin.adapt.TitleSender;

public class AdapterModule1_12_R1
  implements StoranceModule {

  @Override
  public void configure(Binder binder) {
    binder.bind(ActionbarSender.class).toInstance(new ActionbarSender1_12_R1());
    binder.bind(BookSender.class).toInstance(new BookSender1_12_R1());
    binder.bind(TitleSender.class).toInstance(new TitleSender1_12_R1());
  }
}
