// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class EmoAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.EmoAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362012, "field 'ivEmo'");
    target.ivEmo = finder.castView(view, 2131362012, "field 'ivEmo'");
  }

  @Override public void unbind(T target) {
    target.ivEmo = null;
  }
}
