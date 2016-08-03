// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserInfoActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.UserInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361961, "field 'btnUpdateInfo' and method 'updateInfo'");
    target.btnUpdateInfo = finder.castView(view, 2131361961, "field 'btnUpdateInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.updateInfo(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361962, "field 'btnStartChat' and method 'startChat'");
    target.btnStartChat = finder.castView(view, 2131361962, "field 'btnStartChat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.startChat(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361960, "field 'ivGender'");
    target.ivGender = finder.castView(view, 2131361960, "field 'ivGender'");
    view = finder.findRequiredView(source, 2131361953, "field 'tvNickname'");
    target.tvNickname = finder.castView(view, 2131361953, "field 'tvNickname'");
    view = finder.findRequiredView(source, 2131361963, "field 'btnAddBlack'");
    target.btnAddBlack = finder.castView(view, 2131361963, "field 'btnAddBlack'");
    view = finder.findRequiredView(source, 2131361951, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131361951, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131361942, "field 'npb'");
    target.npb = finder.castView(view, 2131361942, "field 'npb'");
    view = finder.findRequiredView(source, 2131361958, "field 'ivNicknameEditor' and method 'setNickname'");
    target.ivNicknameEditor = finder.castView(view, 2131361958, "field 'ivNicknameEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361952, "field 'ivAvatarEditor' and method 'setAvatar'");
    target.ivAvatarEditor = finder.castView(view, 2131361952, "field 'ivAvatarEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAvatar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361954, "field 'nicknameEditContainer'");
    target.nicknameEditContainer = view;
    view = finder.findRequiredView(source, 2131361955, "field 'etNickname'");
    target.etNickname = finder.castView(view, 2131361955, "field 'etNickname'");
    view = finder.findRequiredView(source, 2131361959, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131361959, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131361957, "field 'btnCancelNickname' and method 'cancelNickname'");
    target.btnCancelNickname = finder.castView(view, 2131361957, "field 'btnCancelNickname'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361956, "field 'btnSaveNickname' and method 'saveNickname'");
    target.btnSaveNickname = finder.castView(view, 2131361956, "field 'btnSaveNickname'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveNickname(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnUpdateInfo = null;
    target.btnStartChat = null;
    target.ivGender = null;
    target.tvNickname = null;
    target.btnAddBlack = null;
    target.ivAvatar = null;
    target.npb = null;
    target.ivNicknameEditor = null;
    target.ivAvatarEditor = null;
    target.headerView = null;
    target.nicknameEditContainer = null;
    target.etNickname = null;
    target.tvUsername = null;
    target.btnCancelNickname = null;
    target.btnSaveNickname = null;
  }
}
