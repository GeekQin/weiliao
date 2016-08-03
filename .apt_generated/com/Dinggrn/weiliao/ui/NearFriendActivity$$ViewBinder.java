// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NearFriendActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.NearFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361923, "field 'ptrListView'");
    target.ptrListView = finder.castView(view, 2131361923, "field 'ptrListView'");
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
  }

  @Override public void unbind(T target) {
    target.ptrListView = null;
    target.headerView = null;
  }
}
