import 'package:flutter_bloc/flutter_bloc.dart';

import '../data/models/mesa_model.dart';
import '../data/repositories/mesa_repository.dart';

part 'mesa_state.dart';

/// Gerencia as mesas do mestre: lê o cache do navegador e cria novas mesas.
class MesaCubit extends Cubit<MesaState> {
  final MesaRepository _repository;

  MesaCubit(this._repository) : super(const MesaInicial());

  /// Carrega as mesas guardadas no navegador (chamada ao abrir o app).
  Future<void> carregar() async {
    emit(const MesaCarregando());
    try {
      emit(MesaCarregada(await _repository.mesasDoCache()));
    } catch (e) {
      emit(MesaErro('Não foi possível carregar as mesas: $e'));
    }
  }

  /// Cria uma mesa no servidor e atualiza a lista. Retorna a mesa criada
  /// (com o código gerado) para a tela exibir, ou `null` em caso de erro.
  Future<MesaModel?> criar(String nome) async {
    try {
      final mesa = await _repository.criarMesa(nome);
      emit(MesaCarregada(await _repository.mesasDoCache()));
      return mesa;
    } catch (e) {
      emit(MesaErro('Não foi possível criar a mesa: $e'));
      return null;
    }
  }

  /// Remove a mesa (servidor + cache) e atualiza a lista.
  Future<void> remover(String id) async {
    try {
      await _repository.removerMesa(id);
      emit(MesaCarregada(await _repository.mesasDoCache()));
    } catch (e) {
      emit(MesaErro('Não foi possível remover a mesa: $e'));
    }
  }
}
