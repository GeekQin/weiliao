// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BlogAdapter$ViewHolder$$ViewBinder<T extends com.Dinggrn.weiliao.adapter.BlogAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131362003, "field 'tvComment'");
    target.tvComment = finder.castView(view, 2131362003, "field 'tvComment'");
    view = finder.findRequiredView(source, 2131361997, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131361997, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131361998, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131361998, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131361999, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131361999, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131362000, "field 'rlIamgesContainer'");
    target.rlIamgesContainer = finder.castView(view, 2131362000, "field 'rlIamgesContainer'");
    view = finder.findRequiredView(source, 2131362002, "field 'tvLove'");
    target.tvLove = finder.castView(view, 2131362002, "field 'tvLove'");
    view = finder.findRequiredView(source, 2131362004, "field 'llCommentsContainer'");
    target.llCommentsContainer = finder.castView(view, 2131362004, "field 'llCommentsContainer'");
    view = finder.findRequiredView(source, 2131362001, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131362001, "field 'tvTime'");
  }

  @Override public void unbind(T target) {
    target.tvComment = null;
    target.ivAvatar = null;
    target.tvUsername = null;
    target.tvContent = null;
    target.rlIamgesContainer = null;
    target.tvLove = null;
    target.llCommentsContainer = null;
    target.tvTime = null;
  }
}
