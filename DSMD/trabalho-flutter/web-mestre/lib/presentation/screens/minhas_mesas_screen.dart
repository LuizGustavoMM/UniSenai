import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../logic/mesa_cubit.dart';
import 'painel_mesa_screen.dart';

/// Lista das mesas do mestre (lidas do cache do navegador).
class MinhasMesasScreen extends StatelessWidget {
  const MinhasMesasScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text('Minhas mesas'),
        backgroundColor: theme.colorScheme.inversePrimary,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Atualizar',
            onPressed: () => context.read<MesaCubit>().carregar(),
          ),
        ],
      ),
      body: BlocBuilder<MesaCubit, MesaState>(
        builder: (context, state) {
          return switch (state) {
            MesaCarregando() || MesaInicial() =>
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
                          Navigator.of(context).push(
                            MaterialPageRoute(
                              builder: (_) => PainelMesaScreen(mesa: mesa),
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
            Icon(Icons.map, size: 72, color: theme.colorScheme.primary),
            const SizedBox(height: 16),
            Text('Nenhuma mesa ainda', style: theme.textTheme.headlineSmall),
            const SizedBox(height: 8),
            Text('Crie uma mesa na aba "Nova mesa".',
                style: theme.textTheme.bodyMedium),
          ],
        ),
      );
}
