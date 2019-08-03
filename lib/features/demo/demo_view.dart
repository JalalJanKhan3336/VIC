import 'dart:async';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:app/data/model/demomodel_data.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:app/trans/translations.dart';
import 'package:flutter_redux/flutter_redux.dart';
import 'package:app/redux/app/app_state.dart';
import 'package:app/features/demo/demo_view_model.dart';
import 'package:app/redux/action_report.dart';
import 'package:app/utils/progress_dialog.dart';

class DemoView extends StatelessWidget {
  DemoView({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return StoreConnector<AppState, DemoViewModel>(
      distinct: true,
      converter: (store) => DemoViewModel.fromStore(store),
      builder: (_, viewModel) => DemoViewContent(
            viewModel: viewModel,
          ),
    );
  }
}

class DemoViewContent extends StatefulWidget {
  final DemoViewModel viewModel;

  DemoViewContent({Key key, this.viewModel}) : super(key: key);

  @override
  _DemoViewContentState createState() => _DemoViewContentState();
}

class _DemoViewContentState extends State<DemoViewContent> {
  final GlobalKey<ScaffoldState> _scaffoldKey = GlobalKey<ScaffoldState>();
  var _status;
  var _processBar;

  @override
  void initState() {
    super.initState();
  }

  void showError(String error) {
    final snackBar = SnackBar(content: Text(error));
    _scaffoldKey.currentState.showSnackBar(snackBar);
  }

  @override
  Widget build(BuildContext context) {
    var widget;

    widget = TabBarView(
      children: [
        Icon(Icons.directions_car),
        Icon(Icons.directions_transit),
        Icon(Icons.directions_bike),
      ],
    );
    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          bottom: TabBar(
            tabs: [
              Tab(icon: Icon(Icons.directions_car)),
              Tab(icon: Icon(Icons.directions_transit)),
              Tab(icon: Icon(Icons.directions_bike)),
            ],
          ),
          title: Text("Demo"),
        ),
        body: widget,
      ),
    );
  }
  void hideProcessBar() {
    if (_processBar != null && _processBar.isShowing()) {
      _processBar.hide();
      _processBar = null;
    }
  }

  void showProcessBar(String msg) {
    if (_processBar == null) {
      _processBar = new ProgressDialog(context);
    }
    _processBar.setMessage(msg);
    _processBar.show();
  }
}
