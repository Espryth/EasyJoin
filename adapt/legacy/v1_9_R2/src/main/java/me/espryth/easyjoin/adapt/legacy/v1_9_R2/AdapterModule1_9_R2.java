package me.espryth.easyjoin.adapt.legacy.v1_9_R2;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.adapt.BookSender;
import me.espryth.easyjoin.adapt.TitleSender;

public class AdapterModule1_9_R2
  implements StoranceModule {
  @Override
  public void configure(Binder binder) {
    binder.bind(ActionbarSender.class).toInstance(new ActionbarSender1_9_R2());
    binder.bind(BookSender.class).toInstance(new BookSender1_9_R2());
    binder.bind(TitleSender.class).toInstance(new TitleSender1_9_R2());
  }
}
