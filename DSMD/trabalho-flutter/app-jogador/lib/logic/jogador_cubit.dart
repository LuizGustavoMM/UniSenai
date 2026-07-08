import 'package:flutter_bloc/flutter_bloc.dart';

import '../data/models/jogador_model.dart';
import '../data/repositories/jogador_repository.dart';

part 'jogador_state.dart';

/// Controla a identidade local do jogador (primeira execução vs. já existente).
class JogadorCubit extends Cubit<JogadorState> {
  final JogadorRepository _repository;

  JogadorCubit(this._repository) : super(const JogadorCarregando());

  Future<void> carregar() async {
    emit(const JogadorCarregando());
    final jogador = await _repository.carregar();
    emit(jogador == null ? const SemJogador() : ComJogador(jogador));
  }

  Future<void> definirApelido(String apelido) async {
    final jogador = await _repository.criar(apelido);
    emit(ComJogador(jogador));
  }
}
