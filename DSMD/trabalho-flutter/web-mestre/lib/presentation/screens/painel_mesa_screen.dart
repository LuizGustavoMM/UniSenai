import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../data/models/ficha_model.dart';
import '../../data/models/mesa_model.dart';
import '../../logic/ficha_cubit.dart';

/// Painel de uma mesa: mostra o código e todas as fichas dos jogadores
/// (buscadas no servidor). O mestre vê a ficha de todos que conectaram.
class PainelMesaScreen extends StatefulWidget {
  final MesaModel mesa;
  const PainelMesaScreen({super.key, required this.mesa});

  @override
  State<PainelMesaScreen> createState() => _PainelMesaScreenState();
}

class _PainelMesaScreenState extends State<PainelMesaScreen> {
  @override
  void initState() {
    super.initState();
    // Busca as fichas ao abrir a tela (padrão REST + Cubit das aulas).
    context.read<FichaCubit>().carregar(widget.mesa.id);
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.mesa.nome),
        backgroundColor: theme.colorScheme.inversePrimary,
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            tooltip: 'Atualizar fichas',
            onPressed: () => context.read<FichaCubit>().carregar(widget.mesa.id),
          ),
        ],
      ),
      body: Column(
        children: [
          _cabecalhoCodigo(theme),
          const Divider(height: 1),
          Expanded(
            child: BlocBuilder<FichaCubit, FichaState>(
              builder: (context, state) {
                return switch (state) {
                  FichaCarregando() || FichaInicial() =>
                    const Center(child: CircularProgressIndicator()),
                  FichaErro(:final mensagem) => Center(child: Text(mensagem)),
                  FichaCarregada(:final fichas) => fichas.isEmpty
                      ? const Center(
                          child: Text('Nenhuma ficha enviada ainda.'),
                        )
                      : ListView(
                          children:
                              fichas.map((f) => _FichaTile(ficha: f)).toList(),
                        ),
                };
              },
            ),
          ),
        ],
      ),
    );
  }

  Widget _cabecalhoCodigo(ThemeData theme) => Container(
        width: double.infinity,
        padding: const EdgeInsets.all(16),
        color: theme.colorScheme.surfaceContainerHighest,
        child: Row(
          children: [
            const Icon(Icons.vpn_key),
            const SizedBox(width: 12),
            Text('Código da mesa: ', style: theme.textTheme.titleMedium),
            SelectableText(
              widget.mesa.codigo,
              style: theme.textTheme.titleMedium?.copyWith(
                fontWeight: FontWeight.bold,
                letterSpacing: 3,
              ),
            ),
          ],
        ),
      );
}

/// Cada ficha aparece como um item expansível mostrando o JSON opaco.
class _FichaTile extends StatelessWidget {
  final FichaModel ficha;
  const _FichaTile({required this.ficha});

  @override
  Widget build(BuildContext context) {
    final jsonBonito =
        const JsonEncoder.withIndent('  ').convert(ficha.dados);
    return ExpansionTile(
      leading: const Icon(Icons.person),
      title: Text(ficha.nomePersonagem),
      subtitle: Text('Jogador: ${ficha.jogadorId}  •  versão ${ficha.versao}'),
      childrenPadding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
      children: [
        Align(
          alignment: Alignment.centerLeft,
          child: SelectableText(
            jsonBonito,
            style: const TextStyle(fontFamily: 'monospace'),
          ),
        ),
      ],
    );
  }
}
