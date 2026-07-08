/// Uma mesa de RPG criada pelo mestre.
///
/// `codigo` é o que o mestre passa aos jogadores; `id` é o identificador
/// interno usado para atrelar as fichas.
class MesaModel {
  final String id;
  final String codigo;
  final String nome;
  final String? createdAt;

  const MesaModel({
    required this.id,
    required this.codigo,
    required this.nome,
    this.createdAt,
  });

  factory MesaModel.fromJson(Map<String, dynamic> json) => MesaModel(
        id: json['id'] as String,
        codigo: json['codigo'] as String,
        nome: json['nome'] as String,
        createdAt: json['created_at'] as String?,
      );

  Map<String, dynamic> toJson() => {
        'id': id,
        'codigo': codigo,
        'nome': nome,
        'created_at': createdAt,
      };
}
