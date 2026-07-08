part of 'mesa_cubit.dart';

/// Estados da lista de mesas do mestre (padrão Initial/Loading/Loaded/Error).
sealed class MesaState {
  const MesaState();
}

class MesaInicial extends MesaState {
  const MesaInicial();
}

class MesaCarregando extends MesaState {
  const MesaCarregando();
}

class MesaCarregada extends MesaState {
  final List<MesaModel> mesas;
  const MesaCarregada(this.mesas);
}

class MesaErro extends MesaState {
  final String mensagem;
  const MesaErro(this.mensagem);
}
