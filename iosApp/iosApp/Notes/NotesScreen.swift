import SwiftUI
import shared

class NoteViewModelAdapter: ObservableObject {
  @Published var state: NoteState
  private let viewModel: NoteViewModel

  init(viewModel: NoteViewModel) {
    self.state = .Loading(mockedNotes: [])
    self.viewModel = viewModel

    viewModel.collectState { state in
      self.state = state
    }
  }
}

extension NoteViewModelAdapter {
  func loadNotes() {
    viewModel.onIntent(intent: NoteIntent.LoadNotes())
  }
}

import SwiftUI
import shared

struct NotesScreen: View {
  @ObservedObject private var viewModel: NoteViewModelAdapter = .init(viewModel: .init(loadNotesUseCase: .init()))

  var body: some View {
    ZStack(alignment: .bottom) {
      VStack {
        switch viewModel.state {
        case let loadingState as NoteState.Loading:
          NotesListView(viewModel: .init(notes: loadingState.mockedNotes))
            .redacted(reason: .placeholder)

        case let loadedState as NoteState.Loaded where !loadedState.notes.isEmpty:
          NotesListView(viewModel: .init(notes: loadedState.notes))

        case let loadedState as NoteState.Loaded where loadedState.notes.isEmpty:
          Text("No notes available.")
            .font(.subheadline)
            .foregroundColor(.gray)
            .padding()

        case let errorState as NoteState.Error:
          Text(errorState.errorMessage)
            .foregroundColor(.red)
            .padding()
            
        default:
          EmptyView()
        }

        Spacer()
      }

      Button(action: {
        viewModel.loadNotes()
      }) {
        Text("Reload")
          .frame(maxWidth: .infinity)
          .padding()
          .background(Color.blue)
          .foregroundColor(.white)
          .cornerRadius(8)
      }
      .padding()
      .padding(.top, 16)
      .background(
        LinearGradient(gradient: Gradient(colors: [.white.opacity(0.6), .white, .white]), startPoint: .top, endPoint: .bottom)
      )
    }
    .onAppear {
      viewModel.loadNotes()
    }
  }
}


struct MainScreen_Previews: PreviewProvider {
  static var previews: some View {
    NotesScreen()
  }
}

private extension Note {
  static var mock: Note {
    .init(id: UUID().uuidString, title: "This is a mock", text: "This is a mock to show text")
  }
}
