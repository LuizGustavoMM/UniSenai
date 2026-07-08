import 'package:flutter/material.dart';

import 'screens/minhas_mesas_screen.dart';
import 'screens/nova_mesa_screen.dart';

/// Esqueleto principal do painel: barra lateral recolhível + área de conteúdo.
///
/// A barra ([NavigationRail]) começa recolhida, mostrando apenas os ícones.
/// O botão de "3 linhas" (hambúrguer) no topo abre/fecha a barra; quando
/// aberta, os rótulos ("Nova mesa", "Minhas mesas") aparecem ao lado dos
/// ícones.
class HomeShell extends StatefulWidget {
  const HomeShell({super.key});

  @override
  State<HomeShell> createState() => _HomeShellState();
}

class _HomeShellState extends State<HomeShell> {
  int _selectedIndex = 0;
  bool _extended = false;

  static const List<Widget> _telas = <Widget>[
    NovaMesaScreen(),
    MinhasMesasScreen(),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Row(
        children: [
          NavigationRail(
            extended: _extended,
            labelType: NavigationRailLabelType.none,
            selectedIndex: _selectedIndex,
            onDestinationSelected: (int index) {
              setState(() => _selectedIndex = index);
            },
            // Ícone "hambúrguer" (3 linhas) que abre/fecha a barra lateral.
            leading: IconButton(
              icon: const Icon(Icons.menu),
              tooltip: _extended ? 'Recolher menu' : 'Abrir menu',
              onPressed: () => setState(() => _extended = !_extended),
            ),
            destinations: const [
              NavigationRailDestination(
                icon: Icon(Icons.add_circle_outline),
                selectedIcon: Icon(Icons.add_circle),
                label: Text('Nova mesa'),
              ),
              NavigationRailDestination(
                // Ícone de mapa (o "mapa de tesouro" — Minhas mesas).
                icon: Icon(Icons.map_outlined),
                selectedIcon: Icon(Icons.map),
                label: Text('Minhas mesas'),
              ),
            ],
          ),
          const VerticalDivider(width: 1, thickness: 1),
          Expanded(child: _telas[_selectedIndex]),
        ],
      ),
    );
  }
}
