// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendFragment$$ViewBinder<T extends com.Dinggrn.weiliao.fragment.FriendFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361977, "field 'listView'");
    target.listView = finder.castView(view, 2131361977, "field 'listView'");
    view = finder.findRequiredView(source, 2131361978, "field 'tvLetter'");
    target.tvLetter = finder.castView(view, 2131361978, "field 'tvLetter'");
    view = finder.findRequiredView(source, 2131361979, "field 'mlvLetters'");
    target.mlvLetters = finder.castView(view, 2131361979, "field 'mlvLetters'");
  }

  @Override public void unbind(T target) {
    target.headerView = null;
    target.listView = null;
    target.tvLetter = null;
    target.mlvLetters = null;
  }
}
