import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';

import 'data/repositories/mesa_repository.dart';
import 'logic/ficha_cubit.dart';
import 'logic/mesa_cubit.dart';
import 'presentation/home_shell.dart';

void main() {
  runApp(const PainelMestreApp());
}

/// Aplicação web do painel do mestre (Mesa D&D).
///
/// Injeção de dependência no mesmo padrão das aulas: `MultiRepositoryProvider`
/// registra o repositório e `MultiBlocProvider` registra os Cubits.
class PainelMestreApp extends StatelessWidget {
  const PainelMestreApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiRepositoryProvider(
      providers: [
        RepositoryProvider(create: (_) => MesaRepository()),
      ],
      child: MultiBlocProvider(
        providers: [
          BlocProvider(
            create: (ctx) => MesaCubit(ctx.read<MesaRepository>())..carregar(),
          ),
          BlocProvider(
            create: (ctx) => FichaCubit(ctx.read<MesaRepository>()),
          ),
        ],
        child: MaterialApp(
          title: 'Painel do Mestre',
          debugShowCheckedModeBanner: false,
          theme: ThemeData(
            colorScheme:
                ColorScheme.fromSeed(seedColor: const Color(0xFF6D4C1E)),
            useMaterial3: true,
          ),
          home: const HomeShell(),
        ),
      ),
    );
  }
}
