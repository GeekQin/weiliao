// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361916, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131361916, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131361920, "field 'ivTips'");
    target.ivTips = finder.castView(view, 2131361920, "field 'ivTips'");
    view = finder.findRequiredView(source, 2131361917, "field 'mtiMessage' and method 'setCurrentFragment'");
    target.mtiMessage = finder.castView(view, 2131361917, "field 'mtiMessage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361918, "field 'badgeView'");
    target.badgeView = finder.castView(view, 2131361918, "field 'badgeView'");
    view = finder.findRequiredView(source, 2131361919, "field 'mtiFriend' and method 'setCurrentFragment'");
    target.mtiFriend = finder.castView(view, 2131361919, "field 'mtiFriend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361922, "field 'mtiSetting' and method 'setCurrentFragment'");
    target.mtiSetting = finder.castView(view, 2131361922, "field 'mtiSetting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361921, "field 'mtiFind' and method 'setCurrentFragment'");
    target.mtiFind = finder.castView(view, 2131361921, "field 'mtiFind'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.viewPager = null;
    target.ivTips = null;
    target.mtiMessage = null;
    target.badgeView = null;
    target.mtiFriend = null;
    target.mtiSetting = null;
    target.mtiFind = null;
  }
}
