// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InvitationAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.InvitationAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362024, "field 'tvAdded'");
    target.tvAdded = finder.castView(view, 2131362024, "field 'tvAdded'");
    view = finder.findRequiredView(source, 2131362021, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131362021, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131362022, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131362022, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131362023, "field 'btnAdd'");
    target.btnAdd = finder.castView(view, 2131362023, "field 'btnAdd'");
  }

  @Override public void unbind(T target) {
    target.tvAdded = null;
    target.ivAvatar = null;
    target.tvUsername = null;
    target.btnAdd = null;
  }
}
