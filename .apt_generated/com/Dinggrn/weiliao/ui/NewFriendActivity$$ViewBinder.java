// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewFriendActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.NewFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361924, "field 'listView'");
    target.listView = finder.castView(view, 2131361924, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.headerView = null;
    target.listView = null;
  }
}
