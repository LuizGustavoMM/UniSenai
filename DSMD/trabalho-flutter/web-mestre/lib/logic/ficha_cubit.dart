import 'package:flutter_bloc/flutter_bloc.dart';

import '../data/models/ficha_model.dart';
import '../data/repositories/mesa_repository.dart';

part 'ficha_state.dart';

/// Busca no servidor as fichas de uma mesa (visão do mestre).
class FichaCubit extends Cubit<FichaState> {
  final MesaRepository _repository;

  FichaCubit(this._repository) : super(const FichaInicial());

  Future<void> carregar(String mesaId) async {
    emit(const FichaCarregando());
    try {
      emit(FichaCarregada(await _repository.fichasDaMesa(mesaId)));
    } catch (e) {
      emit(FichaErro('Não foi possível carregar as fichas: $e'));
    }
  }
}
