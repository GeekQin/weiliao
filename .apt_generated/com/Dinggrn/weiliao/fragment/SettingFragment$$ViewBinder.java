// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingFragment$$ViewBinder<T extends com.Dinggrn.weiliao.fragment.SettingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361986, "field 'ivVibrate' and method 'switchVibrate'");
    target.ivVibrate = finder.castView(view, 2131361986, "field 'ivVibrate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.switchVibrate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361981, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131361981, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131361985, "field 'ivSound' and method 'switchSound'");
    target.ivSound = finder.castView(view, 2131361985, "field 'ivSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.switchSound(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361984, "field 'ivNotification' and method 'switchNotification'");
    target.ivNotification = finder.castView(view, 2131361984, "field 'ivNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.switchNotification(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361982, "method 'browseUserInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.browseUserInfo(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361983, "method 'toBlackActivity'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.toBlackActivity(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361987, "method 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivVibrate = null;
    target.tvUsername = null;
    target.ivSound = null;
    target.headerView = null;
    target.ivNotification = null;
  }
}
