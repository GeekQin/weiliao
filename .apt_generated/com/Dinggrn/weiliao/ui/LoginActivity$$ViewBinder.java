// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361890, "field 'headerview'");
    target.headerview = view;
    view = finder.findRequiredView(source, 2131361915, "field 'tvRegist' and method 'toRegist'");
    target.tvRegist = finder.castView(view, 2131361915, "field 'tvRegist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toRegist(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361914, "field 'btLogin' and method 'doLogin'");
    target.btLogin = finder.castView(view, 2131361914, "field 'btLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.doLogin(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361913, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131361913, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131361912, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131361912, "field 'etUsername'");
  }

  @Override public void unbind(T target) {
    target.headerview = null;
    target.tvRegist = null;
    target.btLogin = null;
    target.etPassword = null;
    target.etUsername = null;
  }
}
