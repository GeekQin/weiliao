// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FindFragment$$ViewBinder<T extends com.Dinggrn.weiliao.fragment.FindFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361976, "field 'btnSendComment' and method 'sendComment'");
    target.btnSendComment = finder.castView(view, 2131361976, "field 'btnSendComment'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendComment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361973, "field 'ivClose' and method 'closePhotoView'");
    target.ivClose = finder.castView(view, 2131361973, "field 'ivClose'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.closePhotoView(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361975, "field 'etComment'");
    target.etComment = finder.castView(view, 2131361975, "field 'etComment'");
    view = finder.findRequiredView(source, 2131361974, "field 'commentContainer'");
    target.commentContainer = finder.castView(view, 2131361974, "field 'commentContainer'");
    view = finder.findRequiredView(source, 2131361970, "field 'pulltorefreshListView'");
    target.pulltorefreshListView = finder.castView(view, 2131361970, "field 'pulltorefreshListView'");
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361972, "field 'photoView'");
    target.photoView = finder.castView(view, 2131361972, "field 'photoView'");
    view = finder.findRequiredView(source, 2131361971, "field 'photoviewContainer'");
    target.photoviewContainer = finder.castView(view, 2131361971, "field 'photoviewContainer'");
  }

  @Override public void unbind(T target) {
    target.btnSendComment = null;
    target.ivClose = null;
    target.etComment = null;
    target.commentContainer = null;
    target.pulltorefreshListView = null;
    target.headerView = null;
    target.photoView = null;
    target.photoviewContainer = null;
  }
}
