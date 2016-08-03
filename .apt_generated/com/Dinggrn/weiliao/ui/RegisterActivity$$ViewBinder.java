// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.RegisterActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361945, "field 'etRePassword'");
    target.etRePassword = finder.castView(view, 2131361945, "field 'etRePassword'");
    view = finder.findRequiredView(source, 2131361949, "field 'cpbRegist' and method 'registUser'");
    target.cpbRegist = finder.castView(view, 2131361949, "field 'cpbRegist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.registUser(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361940, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131361940, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131361943, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131361943, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131361944, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131361944, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131361942, "field 'npb'");
    target.npb = finder.castView(view, 2131361942, "field 'npb'");
    view = finder.findRequiredView(source, 2131361946, "field 'rgGender'");
    target.rgGender = finder.castView(view, 2131361946, "field 'rgGender'");
    view = finder.findRequiredView(source, 2131361941, "method 'setAvatar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAvatar(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.headerView = null;
    target.etRePassword = null;
    target.cpbRegist = null;
    target.ivAvatar = null;
    target.etUsername = null;
    target.etPassword = null;
    target.npb = null;
    target.rgGender = null;
  }
}
