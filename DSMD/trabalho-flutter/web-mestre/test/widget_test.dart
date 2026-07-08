// Teste de fumaça do painel do mestre.

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:web_mestre/main.dart';

void main() {
  testWidgets('Abre na tela de criar mesa', (WidgetTester tester) async {
    SharedPreferences.setMockInitialValues({});
    await tester.pumpWidget(const PainelMestreApp());
    await tester.pump();

    // A tela inicial mostra a criação de mesa.
    expect(find.text('Criar nova mesa'), findsOneWidget);
    expect(find.text('Criar mesa'), findsOneWidget);
  });
}
