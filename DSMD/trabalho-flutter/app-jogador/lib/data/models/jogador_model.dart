/// Identidade local do jogador (sem login): UUID gerado no app + apelido.
class JogadorModel {
  final String id;
  final String apelido;

  const JogadorModel({required this.id, required this.apelido});

  factory JogadorModel.fromMap(Map<String, dynamic> map) => JogadorModel(
        id: map['id'] as String,
        apelido: map['apelido'] as String,
      );

  Map<String, dynamic> toMap() => {'id': id, 'apelido': apelido};
}
