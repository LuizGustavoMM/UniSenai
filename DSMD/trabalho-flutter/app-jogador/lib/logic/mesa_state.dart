part of 'mesa_cubit.dart';

sealed class MesaState {
  const MesaState();
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
