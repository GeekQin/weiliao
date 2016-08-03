// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.FriendAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362013, "field 'tvSortLetter'");
    target.tvSortLetter = finder.castView(view, 2131362013, "field 'tvSortLetter'");
    view = finder.findRequiredView(source, 2131362015, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131362015, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131362014, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131362014, "field 'ivAvatar'");
  }

  @Override public void unbind(T target) {
    target.tvSortLetter = null;
    target.tvUsername = null;
    target.ivAvatar = null;
  }
}
