// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecentAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.RecentAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362026, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131362026, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131362025, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131362025, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131362027, "field 'tvMsgtime'");
    target.tvMsgtime = finder.castView(view, 2131362027, "field 'tvMsgtime'");
    view = finder.findRequiredView(source, 2131362028, "field 'tvMessage'");
    target.tvMessage = finder.castView(view, 2131362028, "field 'tvMessage'");
    view = finder.findRequiredView(source, 2131362029, "field 'bvUnreadCount'");
    target.bvUnreadCount = finder.castView(view, 2131362029, "field 'bvUnreadCount'");
  }

  @Override public void unbind(T target) {
    target.tvUsername = null;
    target.ivAvatar = null;
    target.tvMsgtime = null;
    target.tvMessage = null;
    target.bvUnreadCount = null;
  }
}
