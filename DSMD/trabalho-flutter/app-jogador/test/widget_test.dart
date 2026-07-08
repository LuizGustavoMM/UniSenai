// Teste unitário simples (não depende de device/sqflite).

import 'package:flutter_test/flutter_test.dart';
import 'package:app_jogador/data/repositories/mesa_repository.dart';

void main() {
  group('MesaRepository.normalizarIp', () {
    test('prefixa http:// quando ausente', () {
      expect(MesaRepository.normalizarIp('192.168.137.1:8000'),
          'http://192.168.137.1:8000');
    });

    test('mantém http:// e remove barra final', () {
      expect(MesaRepository.normalizarIp('http://10.0.0.5:8000/'),
          'http://10.0.0.5:8000');
    });
  });
}
