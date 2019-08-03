import 'dart:async';
import 'package:redux/redux.dart';
import 'package:app/data/model/demomodel_data.dart';
import 'package:app/redux/action_report.dart';
import 'package:app/redux/app/app_state.dart';
import 'package:app/features/action_callback.dart';
import 'package:app/redux/demomodel/demomodel_actions.dart';

class DemoViewModel {
  final DemoModel demomodel;

  DemoViewModel({
    this.demomodel,
  });

  static DemoViewModel fromStore(Store<AppState> store) {
    return DemoViewModel(
      demomodel: store.state.demomodelState.demomodel,
    );
  }
}
