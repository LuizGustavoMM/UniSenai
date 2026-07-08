import 'dart:convert';

/// Ficha do jogador em uma mesa (armazenada localmente e sincronizada).
///
/// O conteúdo livre digitado pelo jogador fica em `dadosJson`, no formato
/// `{"conteudo": "<texto>"}`. `versao` incrementa a cada save; `sincronizada`
/// indica se já foi enviada ao servidor.
class FichaModel {
  final String id;
  final String mesaId;
  final String jogadorId;
  final String nomePersonagem;
  final String dadosJson;
  final int versao;
  final bool sincronizada;
  final String updatedAt;

  const FichaModel({
    required this.id,
    required this.mesaId,
    required this.jogadorId,
    required this.nomePersonagem,
    required this.dadosJson,
    required this.versao,
    required this.sincronizada,
    required this.updatedAt,
  });

  factory FichaModel.fromMap(Map<String, dynamic> map) => FichaModel(
        id: map['id'] as String,
        mesaId: map['mesa_id'] as String,
        jogadorId: map['jogador_id'] as String,
        nomePersonagem: map['nome_personagem'] as String,
        dadosJson: map['dados_json'] as String,
        versao: map['versao'] as int,
        sincronizada: (map['sincronizada'] as int) == 1,
        updatedAt: map['updated_at'] as String,
      );

  Map<String, dynamic> toMap() => {
        'id': id,
        'mesa_id': mesaId,
        'jogador_id': jogadorId,
        'nome_personagem': nomePersonagem,
        'dados_json': dadosJson,
        'versao': versao,
        'sincronizada': sincronizada ? 1 : 0,
        'updated_at': updatedAt,
      };

  /// Texto livre digitado pelo jogador (extraído do JSON).
  String get conteudo {
    final decodificado = jsonDecode(dadosJson);
    if (decodificado is Map && decodificado['conteudo'] is String) {
      return decodificado['conteudo'] as String;
    }
    return '';
  }

  /// Corpo enviado ao servidor em POST /mesa/fichas.
  Map<String, dynamic> paraEnvio() => {
        'jogador_id': jogadorId,
        'nome_personagem': nomePersonagem,
        'versao': versao,
        'dados': jsonDecode(dadosJson),
      };
}
