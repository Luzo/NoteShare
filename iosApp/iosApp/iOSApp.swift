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

class NoteRouterViewModelAdapter: ObservableObject {
  @Published var state: KotlinPair<NoteRoute, NavigationDirection>
  let viewModel: NoteRouterViewModel

  init(viewModel: NoteRouterViewModel) {
    self.state = .init(first: NoteRoute.noteslist, second: nil)
    self.viewModel = viewModel

    viewModel.collectState { state in
      self.state = state
    }
  }
}

struct AppNavigation: View {
  @StateObject private var router = NoteRouterViewModelAdapter(viewModel: .init())
  @Namespace private var animation

  var body: some View {
    NavigationStack {
      view(forRoute: router.state)
        .navigationDestination(for: KotlinPair<NoteRoute, NavigationDirection>.self) { route in
          view(forRoute: route)
        }
    }
    .animation(.easeInOut(duration: 0.5), value: router.state)
  }

  @ViewBuilder
  func view(forRoute route: KotlinPair<NoteRoute, NavigationDirection>) -> some View {
    switch route.first {
    case .noteslist:
      NotesListView(
        viewModel: .init(
          viewModel: NoteListViewModel(
            loadNotesUseCase: .init(),
            deleteNotesUseCase: .init(),
            router: router.viewModel
          )
        )
      )
      .transition(transition(forDirection: route.second))

    case .addnote:
      AddNoteView(
        viewModel: .init(
          viewModel: AddNoteViewModel(
            addNoteUseCase: .init(),
            router: router.viewModel
          )
        )
      )
      .transition(transition(forDirection: route.second))

    default:
      EmptyView()
    }
  }

  private func transition(forDirection direction: NavigationDirection?) -> AnyTransition {
    switch direction {
    case .forward:
      return .move(edge: .trailing)
    case .backward:
      return .move(edge: .leading)
    default:
      return .opacity
    }
  }
}
