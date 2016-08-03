// Generated code from Butter Knife. Do not modify!
package com.Dinggrn.weiliao.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocationActivity$$ViewBinder<T extends com.Dinggrn.weiliao.ui.LocationActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131361890, "field 'headerView'");
    target.headerView = view;
    view = finder.findRequiredView(source, 2131361909, "field 'mapView'");
    target.mapView = finder.castView(view, 2131361909, "field 'mapView'");
  }

  @Override public void unbind(T target) {
    target.headerView = null;
    target.mapView = null;
  }
}
