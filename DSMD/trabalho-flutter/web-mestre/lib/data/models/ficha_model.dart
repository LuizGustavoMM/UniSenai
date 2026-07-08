/// Ficha de um jogador em uma mesa (visão do mestre).
///
/// O conteúdo (`dados`) é um JSON opaco enviado pelo APK do jogador — o painel
/// só o exibe de forma genérica por enquanto.
class FichaModel {
  final String id;
  final String mesaId;
  final String jogadorId;
  final String nomePersonagem;
  final int versao;
  final Map<String, dynamic> dados;
  final String? updatedAt;

  const FichaModel({
    required this.id,
    required this.mesaId,
    required this.jogadorId,
    required this.nomePersonagem,
    required this.versao,
    required this.dados,
    this.updatedAt,
  });

  factory FichaModel.fromJson(Map<String, dynamic> json) => FichaModel(
        id: json['id'] as String,
        mesaId: json['mesa_id'] as String,
        jogadorId: json['jogador_id'] as String,
        nomePersonagem: json['nome_personagem'] as String,
        versao: json['versao'] as int,
        dados: (json['dados'] as Map?)?.cast<String, dynamic>() ?? {},
        updatedAt: json['updated_at'] as String?,
      );
}
