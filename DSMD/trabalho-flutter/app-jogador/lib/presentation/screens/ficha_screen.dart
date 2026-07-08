import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import '../../data/models/mesa_model.dart';
import '../../data/repositories/ficha_repository.dart';
import '../../logic/ficha_cubit.dart';

/// Criar/editar a ficha do jogador nesta mesa.
///
/// Uma caixa de texto livre guarda todos os campos da ficha (raça, subclasse,
/// equipamentos etc.). Ao salvar, grava localmente (versão + 1) e tenta enviar
/// ao servidor.
class FichaScreen extends StatefulWidget {
  final MesaModel mesa;
  final String jogadorId;

  const FichaScreen({super.key, required this.mesa, required this.jogadorId});

  @override
  State<FichaScreen> createState() => _FichaScreenState();
}

class _FichaScreenState extends State<FichaScreen> {
  final _nomeController = TextEditingController();
  final _textoController = TextEditingController();

  bool _carregando = true;
  int _versaoAtual = 0;
  bool? _sincronizada;

  static const _modelo = 'Raça: \n'
      'Classe: \n'
      'Subclasse: \n'
      'Nível: \n'
      'Antecedente: \n'
      'Atributos (FOR/DES/CON/INT/SAB/CAR): \n'
      'Pontos de vida: \n'
      'Equipamentos: \n'
      'Magias: \n'
      'Ouro: ';

  @override
  void initState() {
    super.initState();
    _carregarFicha();
  }

  Future<void> _carregarFicha() async {
    final ficha =
        await context.read<FichaRepository>().fichaDaMesa(widget.mesa.id);
    if (!mounted) return;
    setState(() {
      if (ficha != null) {
        _nomeController.text = ficha.nomePersonagem;
        _textoController.text = ficha.conteudo;
        _versaoAtual = ficha.versao;
        _sincronizada = ficha.sincronizada;
      } else {
        _textoController.text = _modelo;
      }
      _carregando = false;
    });
  }

  @override
  void dispose() {
    _nomeController.dispose();
    _textoController.dispose();
    super.dispose();
  }

  void _salvar() {
    final nome = _nomeController.text.trim();
    if (nome.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Informe o nome do personagem.')),
      );
      return;
    }
    context.read<FichaCubit>().salvarESincronizar(
          mesa: widget.mesa,
          jogadorId: widget.jogadorId,
          nomePersonagem: nome,
          textoLivre: _textoController.text,
        );
  }

  @override
  Widget build(BuildContext context) {
    final theme = Theme.of(context);
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.mesa.nome),
        backgroundColor: theme.colorScheme.inversePrimary,
      ),
      body: _carregando
          ? const Center(child: CircularProgressIndicator())
          : BlocConsumer<FichaCubit, FichaState>(
              listener: (context, state) {
                if (state is FichaSalva) {
                  setState(() {
                    _versaoAtual = state.ficha.versao;
                    _sincronizada = state.sincronizada;
                  });
                  ScaffoldMessenger.of(context).showSnackBar(
                    SnackBar(content: Text(state.mensagem)),
                  );
                } else if (state is FichaErro) {
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text(state.mensagem)));
                }
              },
              builder: (context, state) {
                final salvando = state is FichaSalvando;
                return Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      _statusVersao(theme),
                      const SizedBox(height: 12),
                      TextField(
                        controller: _nomeController,
                        decoration: const InputDecoration(
                          labelText: 'Nome do personagem',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 12),
                      Expanded(
                        child: TextField(
                          controller: _textoController,
                          expands: true,
                          maxLines: null,
                          textAlignVertical: TextAlignVertical.top,
                          decoration: const InputDecoration(
                            labelText: 'Ficha (texto livre)',
                            alignLabelWithHint: true,
                            border: OutlineInputBorder(),
                          ),
                        ),
                      ),
                      const SizedBox(height: 12),
                      FilledButton.icon(
                        onPressed: salvando ? null : _salvar,
                        icon: salvando
                            ? const SizedBox(
                                width: 18,
                                height: 18,
                                child:
                                    CircularProgressIndicator(strokeWidth: 2),
                              )
                            : const Icon(Icons.save),
                        label: const Text('Salvar e sincronizar'),
                      ),
                    ],
                  ),
                );
              },
            ),
    );
  }

  Widget _statusVersao(ThemeData theme) {
    final (cor, icone, texto) = switch (_sincronizada) {
      null => (theme.colorScheme.outline, Icons.fiber_new, 'Ficha nova'),
      true => (Colors.green, Icons.cloud_done, 'Sincronizada (v$_versaoAtual)'),
      false => (
          Colors.orange,
          Icons.cloud_off,
          'Pendente de envio (v$_versaoAtual)'
        ),
    };
    return Row(
      children: [
        Icon(icone, color: cor, size: 20),
        const SizedBox(width: 8),
        Text(texto, style: theme.textTheme.bodyMedium?.copyWith(color: cor)),
      ],
    );
  }
}
