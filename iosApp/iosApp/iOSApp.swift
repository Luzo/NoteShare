import FirebaseCore
import SwiftUI
import shared

@main
struct iOSApp: App {
  var body: some Scene {
    WindowGroup {
      AppNavigation()
        .onAppear {
          // TODO: With SPM it is impossible to call firebase initialization
          FirebaseApp.configure()
        }
    }
  }
}

import Foundation
import shared
import SwiftUI

@MainActor
class NoteRouterViewModelAdapter: ObservableObject {
  let viewModel: NoteRouterViewModel
  @Published var path = NavigationPath()
  @Published var root: NoteRoute = .noteslist
  @Published var direction: NavigationDirection = .forward

  init(viewModel: NoteRouterViewModel) {
    self.viewModel = viewModel
    viewModel.collectState { [weak self] state in
      guard
        let self,
        let route = state.first,
        let direction = state.second
      else { return }

      self.updatePath(for: route, direction: direction)
    }
  }

  private func updatePath(for route: NoteRoute, direction: NavigationDirection) {
    self.direction = direction

    switch route {
    case .noteslist:
      path = NavigationPath()
      root = .noteslist
    default:
      if direction == .forward {
        path.append(route)
      } else if direction == .backward {
        path.removeLast()
      }
    }
  }

  func navigateTo(_ route: NoteRoute) {
    viewModel.navigateTo(screen: route, direction: .forward)
  }

  func popBack() {
    viewModel.popBack()
  }

  func resetToRoot() {
    viewModel.resetToRoot()
  }
}

struct AppNavigation: View {
  @StateObject private var router = NoteRouterViewModelAdapter(viewModel: .init())

  var body: some View {
    NavigationStack(path: $router.path) {
      screenView(for: router.root)
        .navigationDestination(for: NoteRoute.self) { route in
          screenView(for: route)
        }
    }
  }

  @ViewBuilder
  func screenView(for route: NoteRoute) -> some View {
    switch route {
    case .noteslist:
      NotesListView(
        viewModel: .init(
          viewModel: NoteListViewModel(
            router: router.viewModel,
            loadNotesUseCase: .init(),
            deleteNotesUseCase: .init(),
            observeNotesUseCase: .init()
          )
        )
      )
    case .addnote:
      AddNoteView(
        viewModel: .init(
          viewModel: AddNoteViewModel(
            router: router.viewModel
          )
        )
      )
    default:
      fatalError("Not expected route")
    }
  }
}
