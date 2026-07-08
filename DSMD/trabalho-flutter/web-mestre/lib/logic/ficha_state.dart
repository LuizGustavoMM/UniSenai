part of 'ficha_cubit.dart';

/// Estados das fichas de uma mesa (padrão Initial/Loading/Loaded/Error).
sealed class FichaState {
  const FichaState();
}

class FichaInicial extends FichaState {
  const FichaInicial();
}

class FichaCarregando extends FichaState {
  const FichaCarregando();
}

class FichaCarregada extends FichaState {
  final List<FichaModel> fichas;
  const FichaCarregada(this.fichas);
}

class FichaErro extends FichaState {
  final String mensagem;
  const FichaErro(this.mensagem);
}
