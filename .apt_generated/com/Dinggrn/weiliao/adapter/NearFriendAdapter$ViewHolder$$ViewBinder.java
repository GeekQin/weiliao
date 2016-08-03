// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NearFriendAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.NearFriendAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362016, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131362016, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131362020, "field 'btnAdd'");
    target.btnAdd = finder.castView(view, 2131362020, "field 'btnAdd'");
    view = finder.findRequiredView(source, 2131362018, "field 'tvDistance'");
    target.tvDistance = finder.castView(view, 2131362018, "field 'tvDistance'");
    view = finder.findRequiredView(source, 2131362019, "field 'tvLastTime'");
    target.tvLastTime = finder.castView(view, 2131362019, "field 'tvLastTime'");
    view = finder.findRequiredView(source, 2131362017, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131362017, "field 'tvUsername'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.btnAdd = null;
    target.tvDistance = null;
    target.tvLastTime = null;
    target.tvUsername = null;
  }
}
