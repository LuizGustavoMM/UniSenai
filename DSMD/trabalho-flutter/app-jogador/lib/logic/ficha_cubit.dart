import 'package:flutter_bloc/flutter_bloc.dart';

import '../data/models/ficha_model.dart';
import '../data/models/mesa_model.dart';
import '../data/repositories/ficha_repository.dart';

part 'ficha_state.dart';

/// Salva a ficha localmente e tenta sincronizar com o servidor.
class FichaCubit extends Cubit<FichaState> {
  final FichaRepository _repository;

  FichaCubit(this._repository) : super(const FichaInicial());

  /// Salva no celular (sempre) e, em seguida, tenta enviar ao servidor.
  Future<void> salvarESincronizar({
    required MesaModel mesa,
    required String jogadorId,
    required String nomePersonagem,
    required String textoLivre,
  }) async {
    emit(const FichaSalvando());
    try {
      final ficha = await _repository.salvarLocal(
        mesaId: mesa.id,
        jogadorId: jogadorId,
        nomePersonagem: nomePersonagem,
        textoLivre: textoLivre,
      );
      final resultado = await _repository.sincronizar(mesa, ficha);
      emit(FichaSalva(
        ficha: ficha,
        sincronizada: resultado.sincronizada,
        mensagem: resultado.mensagem,
      ));
    } catch (e) {
      emit(FichaErro('Não foi possível salvar a ficha: $e'));
    }
  }
}
