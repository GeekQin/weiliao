// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddFriendActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.AddFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361891, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131361891, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361894, "field 'ptrListView'");
    target.ptrListView = finder.castView(view, 2131361894, "field 'ptrListView'");
    view = finder.findRequiredView(source, 2131361892, "method 'search'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.search(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361893, "method 'searchMore'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.searchMore(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etUsername = null;
    target.headerView = null;
    target.ptrListView = null;
  }
}
