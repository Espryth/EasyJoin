package me.espryth.easyjoin.adapt.newer;

import dev.henko.storance.Binder;
import dev.henko.storance.StoranceModule;
import me.espryth.easyjoin.adapt.ActionbarSender;
import me.espryth.easyjoin.adapt.BookSender;
import me.espryth.easyjoin.adapt.TitleSender;

public class NewerAdapterModule
  implements StoranceModule {

  @Override
  public void configure(Binder binder) {
    binder.bind(ActionbarSender.class).toInstance(new NewerActionbarSender());
    binder.bind(BookSender.class).toInstance(new NewerBookSender());
    binder.bind(TitleSender.class).toInstance(new NewerTitleSender());
  }
}
