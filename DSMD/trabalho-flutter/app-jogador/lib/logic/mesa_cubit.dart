import 'package:flutter_bloc/flutter_bloc.dart';

import '../data/models/mesa_model.dart';
import '../data/repositories/mesa_repository.dart';

part 'mesa_state.dart';

/// Lista as mesas locais e entra em novas mesas (validando o código no servidor).
class MesaCubit extends Cubit<MesaState> {
  final MesaRepository _repository;

  MesaCubit(this._repository) : super(const MesaCarregando());

  Future<void> carregar() async {
    emit(const MesaCarregando());
    try {
      emit(MesaCarregada(await _repository.mesasLocais()));
    } catch (e) {
      emit(MesaErro('Não foi possível carregar as mesas: $e'));
    }
  }

  /// Entra em uma mesa. Retorna a mesa em caso de sucesso; caso contrário,
  /// emite [MesaErro] e retorna null.
  Future<MesaModel?> entrar({
    required String ip,
    required String codigo,
  }) async {
    try {
      final mesa = await _repository.entrar(ip: ip, codigo: codigo);
      emit(MesaCarregada(await _repository.mesasLocais()));
      return mesa;
    } catch (e) {
      emit(MesaErro(e.toString().replaceFirst('Exception: ', '')));
      return null;
    }
  }
}
