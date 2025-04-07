import Combine
import SwiftUI
import shared

class NoteViewModelAdapter: ObservableObject {
  @Published var state: NoteState
  private let viewModel: NoteViewModel

  init(viewModel: NoteViewModel) {
    self.state = .Loading(mockedNotes: [])
    self.viewModel = viewModel

    viewModel.collectState { state in
      self.state = state.transform
    }
  }
}

// TODO: this is here just for quick debug, remove later
private extension NoteState {
  var transform: NoteState {
    if self is NoteState.Loading {
      return self
    }

    return self
//    return NoteState.Error(errorMessage: "Not loaded")
//    return NoteState.Loaded(notes: [])
  }
}

extension NoteViewModelAdapter {
  func loadNotes() async {
    viewModel.onIntent(intent: NoteIntent.LoadNotes())
    await checkLoaded()
  }

  private func checkLoaded() async {
    if state is NoteState.Loading {
      try? await Task.sleep(for: .seconds(1))
      await checkLoaded()
    }

    try? await Task.sleep(for: .seconds(1))
  }
}

struct NotesScreen: View {
  @ObservedObject var viewModel: NoteViewModelAdapter

  var body: some View {
    NavigationStack {
      ZStack(alignment: .bottom) {
        List {
          switch viewModel.state {
          case let loadingState as NoteState.Loading:
            NotesListView(viewModel: .init(notes: loadingState.mockedNotes))
              .redacted(reason: .placeholder)

          case let loadedState as NoteState.Loaded:
            NotesListView(viewModel: .init(notes: loadedState.notes))

          case let errorState as NoteState.Error:
            Text(errorState.errorMessage)
              .foregroundColor(.red)
              .asStaticItem()

          default:
            EmptyView()
          }
        }
      }
      .onAppear {
        Task {
          await viewModel.loadNotes()
        }
      }
      .refreshable {
        await viewModel.loadNotes()
      }
      .navigationTitle("Your notes")
      .toolbar {
        Button(action: {
          // TODO: add item
        }) {
          Image(systemName: "plus")
            .padding(4)
            .background(Color.blue)
            .foregroundColor(.white)
            .clipShape(Circle())
        }
      }
    }
  }
}

private extension Note {
  static var mock: Note {
    .init(id: UUID().uuidString, title: "This is a mock", text: "This is a mock to show text")
  }
}
