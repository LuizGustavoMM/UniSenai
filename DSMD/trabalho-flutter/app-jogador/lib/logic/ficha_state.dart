part of 'ficha_cubit.dart';

sealed class FichaState {
  const FichaState();
}

class FichaInicial extends FichaState {
  const FichaInicial();
}

class FichaSalvando extends FichaState {
  const FichaSalvando();
}

class FichaSalva extends FichaState {
  final FichaModel ficha;
  final bool sincronizada;
  final String mensagem;
  const FichaSalva({
    required this.ficha,
    required this.sincronizada,
    required this.mensagem,
  });
}

class FichaErro extends FichaState {
  final String mensagem;
  const FichaErro(this.mensagem);
}
