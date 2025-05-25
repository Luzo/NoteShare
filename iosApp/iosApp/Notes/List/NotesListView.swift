import Combine
import SwiftUI
import shared

class NotesListViewModelAdapter: ObservableObject {
  @Published var state: NoteListState
  private let viewModel: NoteListViewModel
//  private var observeTask: Task<Void, Never>?

  init(viewModel: NoteListViewModel) {
    self.state = .Loading(mockedNotes: [])
    self.viewModel = viewModel

    Task {
      await observeState()
    }
  }

  private func observeState() async {
    for try await state in viewModel.state {
      await MainActor.run {
        self.state = state
      }
    }
  }
}

extension NotesListViewModelAdapter {
  func observeNotes() {
    viewModel.sendIntent(intent: NoteListIntent.ObserveNoteChanges())
  }

  func loadNotes() async {
    await MainActor.run {
      viewModel.sendIntent(intent: NoteListIntent.LoadNoteList())
    }
    await checkLoaded()
  }

  private func checkLoaded() async {
    if state is NoteListState.Loading {
      try? await Task.sleep(for: .seconds(1))
      await checkLoaded()
    }

    try? await Task.sleep(for: .seconds(1))
  }

  func addNoteTapped() {
    viewModel.sendIntent(intent: NoteListIntent.AddNoteTapped())
  }

  func deleteNoteTapped(note: Note) {
    viewModel.sendIntent(intent: NoteListIntent.DeleteNoteTapped(note: note))
  }
}

struct NotesListView: View {
  @StateObject var viewModel: NotesListViewModelAdapter

  var body: some View {
      ZStack(alignment: .bottom) {
        List {
          switch viewModel.state {
          case let loadingState as NoteListState.Loading:
            NotesListNotesView(viewModel: .init(notes: loadingState.mockedNotes))
              .redacted(reason: .placeholder)

          case let loadedState as NoteListState.Loaded:
            NotesListNotesView(
              viewModel: .init(notes: loadedState.notes),
              onDelete: viewModel.deleteNoteTapped
            )

          case let errorState as NoteListState.Error:
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

        viewModel.observeNotes()
      }
      .refreshable {
        await viewModel.loadNotes()
      }
      .navigationTitle("Your notes")
      .toolbar {
        Button(action: {
          viewModel.addNoteTapped()
        }) {
          Image(systemName: "plus")
            .padding(4)
            .background(Color.blue)
            .foregroundColor(.white)
            .clipShape(Circle())
        }
      }
      .onChange(of: viewModel.state, perform: { newValue in
        print("State changed to: \(newValue)")
      })
  }
}

private extension Note {
  static var mock: Note {
    .init(id: UUID().uuidString, title: "This is a mock", text: "This is a mock to show text")
  }
}
