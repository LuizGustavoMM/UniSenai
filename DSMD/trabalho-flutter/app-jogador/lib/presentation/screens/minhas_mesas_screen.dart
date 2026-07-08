import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../logic/jogador_cubit.dart';
import '../../logic/mesa_cubit.dart';
import 'entrar_mesa_screen.dart';
import 'ficha_screen.dart';

/// "Minhas mesas RPG": lista as mesas locais em que o jogador entrou.
class MinhasMesasScreen extends StatefulWidget {
  const MinhasMesasScreen({super.key});

  @override
  State<MinhasMesasScreen> createState() => _MinhasMesasScreenState();
}

class _MinhasMesasScreenState extends State<MinhasMesasScreen> {
  @override
  void initState() {
    super.initState();
    context.read<MesaCubit>().carregar();
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    final jogadorState = context.watch<JogadorCubit>().state;
    final apelido =
        jogadorState is ComJogador ? jogadorState.jogador.apelido : '';

    return Scaffold(
      appBar: AppBar(
        title: const Text('Minhas mesas'),
        backgroundColor: theme.colorScheme.inversePrimary,
        actions: [
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 12),
            child: Center(child: Text(apelido)),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton.extended(
        onPressed: () async {
          final mesaCubit = context.read<MesaCubit>();
          await Navigator.of(context).push(
            MaterialPageRoute(builder: (_) => const EntrarMesaScreen()),
          );
          mesaCubit.carregar();
        },
        icon: const Icon(Icons.add),
        label: const Text('Entrar em mesa'),
      ),
      body: BlocBuilder<MesaCubit, MesaState>(
        builder: (context, state) {
          return switch (state) {
            MesaCarregando() =>
              const Center(child: CircularProgressIndicator()),
            MesaErro(:final mensagem) => Center(child: Text(mensagem)),
            MesaCarregada(:final mesas) => mesas.isEmpty
                ? _vazio(theme)
                : ListView.builder(
                    itemCount: mesas.length,
                    itemBuilder: (context, i) {
                      final mesa = mesas[i];
                      return ListTile(
                        leading: const Icon(Icons.map),
                        title: Text(mesa.nome),
                        subtitle: Text('Código: ${mesa.codigo}'),
                        trailing: const Icon(Icons.chevron_right),
                        onTap: () {
                          final js = context.read<JogadorCubit>().state;
                          if (js is! ComJogador) return;
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (_) => FichaScreen(
                                mesa: mesa,
                                jogadorId: js.jogador.id,
                              ),
                            ),
                          );
                        },
                      );
                    },
                  ),
          };
        },
      ),
    );
  }

  Widget _vazio(ThemeData theme) => Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.map_outlined, size: 72, color: theme.colorScheme.primary),
            const SizedBox(height: 16),
            Text('Nenhuma mesa ainda', style: theme.textTheme.titleLarge),
            const SizedBox(height: 8),
            Text('Toque em "Entrar em mesa" e use o código do mestre.',
                textAlign: TextAlign.center, style: theme.textTheme.bodyMedium),
          ],
        ),
      );
}
