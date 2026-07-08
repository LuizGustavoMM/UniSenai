/// Uma mesa em que o jogador entrou.
///
/// `id` e `nome` vêm do servidor (GET /mesa/info); `ipServidor` é o endereço
/// informado pelo jogador para reconectar depois.
class MesaModel {
  final String id;
  final String codigo;
  final String nome;
  final String ipServidor;
  final String? joinedAt;

  const MesaModel({
    required this.id,
    required this.codigo,
    required this.nome,
    required this.ipServidor,
    this.joinedAt,
  });

  /// Resposta do servidor em GET /mesa/info.
  factory MesaModel.fromServidor(
    Map<String, dynamic> json, {
    required String ipServidor,
  }) =>
      MesaModel(
        id: json['id'] as String,
        codigo: json['codigo'] as String,
        nome: json['nome'] as String,
        ipServidor: ipServidor,
      );

  factory MesaModel.fromMap(Map<String, dynamic> map) => MesaModel(
        id: map['id'] as String,
        codigo: map['codigo'] as String,
        nome: map['nome'] as String,
        ipServidor: map['ip_servidor'] as String,
        joinedAt: map['joined_at'] as String?,
      );

  Map<String, dynamic> toMap() => {
        'id': id,
        'codigo': codigo,
        'nome': nome,
        'ip_servidor': ipServidor,
        'joined_at': joinedAt,
      };
}
