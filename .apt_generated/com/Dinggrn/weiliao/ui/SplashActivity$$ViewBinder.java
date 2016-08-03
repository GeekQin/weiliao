// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SplashActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.SplashActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361950, "field 'tvstart'");
    target.tvstart = finder.castView(view, 2131361950, "field 'tvstart'");
  }

  @Override public void unbind(T target) {
    target.tvstart = null;
  }
}
