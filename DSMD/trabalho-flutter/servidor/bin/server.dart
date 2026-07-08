import 'dart:io';

import 'package:shelf/shelf.dart';
import 'package:shelf/shelf_io.dart' as io;

import 'package:servidor/src/db.dart';
import 'package:servidor/src/middleware.dart';
import 'package:servidor/src/router.dart';

Future<void> main() async {
  final db = await Db.connect();

  final handler = const Pipeline()
      .addMiddleware(logRequests())
      .addMiddleware(cors())
      .addMiddleware(tratarErros())
      .addHandler(buildRouter(db));

  final port = int.parse(Platform.environment['PORT'] ?? '8080');
  final server = await io.serve(handler, InternetAddress.anyIPv4, port);
  print('Servidor Mesa D&D ouvindo em http://${server.address.host}:${server.port}');
}
