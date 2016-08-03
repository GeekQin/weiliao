// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361907, "field 'btnSend' and method 'sendTextMessage'");
    target.btnSend = finder.castView(view, 2131361907, "field 'btnSend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendTextMessage(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361901, "field 'etInput'");
    target.etInput = finder.castView(view, 2131361901, "field 'etInput'");
    view = finder.findRequiredView(source, 2131361895, "field 'listView'");
    target.listView = finder.castView(view, 2131361895, "field 'listView'");
    view = finder.findRequiredView(source, 2131361905, "field 'btnVoiceInput' and method 'record'");
    target.btnVoiceInput = finder.castView(view, 2131361905, "field 'btnVoiceInput'");
    view.setOnTouchListener(
      new android.view.View.OnTouchListener() {
        @Override public boolean onTouch(
          android.view.View p0,
          android.view.MotionEvent p1
        ) {
          return target.record(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361899, "field 'textinputContainer'");
    target.textinputContainer = finder.castView(view, 2131361899, "field 'textinputContainer'");
    view = finder.findRequiredView(source, 2131361906, "field 'btnAdd' and method 'showPicLocation'");
    target.btnAdd = finder.castView(view, 2131361906, "field 'btnAdd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showPicLocation(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361897, "field 'ivRecordVolume'");
    target.ivRecordVolume = finder.castView(view, 2131361897, "field 'ivRecordVolume'");
    view = finder.findRequiredView(source, 2131361896, "field 'voicerecordContainer'");
    target.voicerecordContainer = finder.castView(view, 2131361896, "field 'voicerecordContainer'");
    view = finder.findRequiredView(source, 2131361903, "field 'voiceinputContainer'");
    target.voiceinputContainer = finder.castView(view, 2131361903, "field 'voiceinputContainer'");
    view = finder.findRequiredView(source, 2131361898, "field 'tvRecordTips'");
    target.tvRecordTips = finder.castView(view, 2131361898, "field 'tvRecordTips'");
    view = finder.findRequiredView(source, 2131361908, "field 'morelayoutContainer'");
    target.morelayoutContainer = finder.castView(view, 2131361908, "field 'morelayoutContainer'");
    view = finder.findRequiredView(source, 2131361904, "method 'showTextInput'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showTextInput(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361900, "method 'showVoiceInput'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showVoiceInput(p0);
        }
      });
    view = finder.findRequiredView(source, 2131361902, "method 'showEmos'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showEmos(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnSend = null;
    target.etInput = null;
    target.listView = null;
    target.btnVoiceInput = null;
    target.headerView = null;
    target.textinputContainer = null;
    target.btnAdd = null;
    target.ivRecordVolume = null;
    target.voicerecordContainer = null;
    target.voiceinputContainer = null;
    target.tvRecordTips = null;
    target.morelayoutContainer = null;
  }
}
