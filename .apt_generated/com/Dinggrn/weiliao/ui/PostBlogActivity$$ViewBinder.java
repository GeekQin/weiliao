// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostBlogActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.PostBlogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361926, "field 'ivBlogImg0'");
    target.ivBlogImg0 = finder.castView(view, 2131361926, "field 'ivBlogImg0'");
    view = finder.findRequiredView(source, 2131361935, "field 'npbProgressBar'");
    target.npbProgressBar = finder.castView(view, 2131361935, "field 'npbProgressBar'");
    view = finder.findRequiredView(source, 2131361937, "field 'ibPicture' and method 'addPicture'");
    target.ibPicture = finder.castView(view, 2131361937, "field 'ibPicture'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addPicture(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361929, "field 'ivDelImg1' and method 'delBlogPic'");
    target.ivDelImg1 = finder.castView(view, 2131361929, "field 'ivDelImg1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.delBlogPic(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361938, "field 'ibCamera'");
    target.ibCamera = finder.castView(view, 2131361938, "field 'ibCamera'");
    view = finder.findRequiredView(source, 2131361932, "field 'ivBlogImg3'");
    target.ivBlogImg3 = finder.castView(view, 2131361932, "field 'ivBlogImg3'");
    view = finder.findRequiredView(source, 2131361936, "field 'ibPlus' and method 'switchButtons'");
    target.ibPlus = finder.castView(view, 2131361936, "field 'ibPlus'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.switchButtons(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361933, "field 'ivDelImg3' and method 'delBlogPic'");
    target.ivDelImg3 = finder.castView(view, 2131361933, "field 'ivDelImg3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.delBlogPic(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361928, "field 'ivBlogImg1'");
    target.ivBlogImg1 = finder.castView(view, 2131361928, "field 'ivBlogImg1'");
    view = finder.findRequiredView(source, 2131361927, "field 'ivDelImg0' and method 'delBlogPic'");
    target.ivDelImg0 = finder.castView(view, 2131361927, "field 'ivDelImg0'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.delBlogPic(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361930, "field 'ivBlogImg2'");
    target.ivBlogImg2 = finder.castView(view, 2131361930, "field 'ivBlogImg2'");
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361925, "field 'etContent'");
    target.etContent = finder.castView(view, 2131361925, "field 'etContent'");
    view = finder.findRequiredView(source, 2131361939, "field 'ibLocation'");
    target.ibLocation = finder.castView(view, 2131361939, "field 'ibLocation'");
    view = finder.findRequiredView(source, 2131361931, "field 'ivDelImg2' and method 'delBlogPic'");
    target.ivDelImg2 = finder.castView(view, 2131361931, "field 'ivDelImg2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.delBlogPic(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361934, "field 'tvPicNumber'");
    target.tvPicNumber = finder.castView(view, 2131361934, "field 'tvPicNumber'");
  }

  @Override public void unbind(T target) {
    target.ivBlogImg0 = null;
    target.npbProgressBar = null;
    target.ibPicture = null;
    target.ivDelImg1 = null;
    target.ibCamera = null;
    target.ivBlogImg3 = null;
    target.ibPlus = null;
    target.ivDelImg3 = null;
    target.ivBlogImg1 = null;
    target.ivDelImg0 = null;
    target.ivBlogImg2 = null;
    target.headerView = null;
    target.etContent = null;
    target.ibLocation = null;
    target.ivDelImg2 = null;
    target.tvPicNumber = null;
  }
}
