// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddFriendAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.AddFriendAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361996, "field 'tvAdded'");
    target.tvAdded = finder.castView(view, 2131361996, "field 'tvAdded'");
    view = finder.findRequiredView(source, 2131361995, "field 'btnAdd'");
    target.btnAdd = finder.castView(view, 2131361995, "field 'btnAdd'");
    view = finder.findRequiredView(source, 2131361993, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131361993, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131361994, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131361994, "field 'tvUsername'");
  }

  @Override public void unbind(T target) {
    target.tvAdded = null;
    target.btnAdd = null;
    target.ivAvatar = null;
    target.tvUsername = null;
  }
}
