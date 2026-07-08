part of 'jogador_cubit.dart';

sealed class JogadorState {
  const JogadorState();
}

class JogadorCarregando extends JogadorState {
  const JogadorCarregando();
}

/// Primeira execução: ainda não há identidade salva.
class SemJogador extends JogadorState {
  const SemJogador();
}

class ComJogador extends JogadorState {
  final JogadorModel jogador;
  const ComJogador(this.jogador);
}
