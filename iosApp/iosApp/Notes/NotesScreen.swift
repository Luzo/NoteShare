import SwiftUI
import shared

class NoteViewModelAdapter: ObservableObject {
  @Published var state: NoteState = NoteState.companion.Loading
  private let viewModel: NoteViewModel

  init(viewModel: NoteViewModel) {
    self.viewModel = viewModel

    viewModel.collectState { state in
      self.state = state.transformLoadingState()
    }
  }
}

// TODO: probably move to shared
private extension NoteState {
  func transformLoadingState()  -> NoteState {
    if isLoading {

      return .init(notes: [.mock, .mock, .mock], isLoading: true, errorMessage: nil)
    }

    return self
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
        Group {
          if !viewModel.state.notes.isEmpty {
            NotesListView(viewModel: .init(notes: viewModel.state.notes))
              .redacted(reason: viewModel.state.isLoading ? .placeholder : [])
          } else {
            Text("No notes available.")
              .font(.subheadline)
              .foregroundColor(.gray)
              .padding()
          }
        }

        if let errorMessage = viewModel.state.errorMessage {
          Text(errorMessage)
            .foregroundColor(.red)
            .padding()
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
