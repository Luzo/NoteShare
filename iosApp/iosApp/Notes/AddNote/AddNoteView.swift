import Combine
import Foundation
import SwiftUI
import shared

@MainActor
class AddNoteViewModelAdapter: ObservableObject {
  @Published var state: AddNoteState = AddNoteState.Idle()

  private let viewModel: AddNoteViewModel

  init(viewModel: AddNoteViewModel) {
    self.viewModel = viewModel

    viewModel.collectState { [weak self] newState in
      DispatchQueue.main.async {
        self?.state = newState
      }
    }
  }

  func saveNote(title: String, text: String) {
    viewModel.sendIntent(
      intent: AddNoteIntent.AddNote(note: Note(title: title, text: text))
    )
  }

  func backButtonTapped() {
    viewModel.sendIntent(
      intent: AddNoteIntent.GoBackTapped()
    )
  }
}

struct AddNoteView: View {
  @StateObject var viewModel: AddNoteViewModelAdapter

  // TODO: This should be in view model
  @State private var title: String = ""
  @State private var text: String = ""

  var body: some View {
    Form {
      Section(header: Text("Title")) {
        TextField("Enter title", text: $title)
      }

      Section(header: Text("Body")) {
        TextEditor(text: $text)
          .frame(height: 150)
      }

      if let errorState = viewModel.state as? AddNoteState.Error {
        Section {
          Text(errorState.message)
            .foregroundColor(.red)
        }
      }
    }
    .disabled(viewModel.state is AddNoteState.Loading)
    .navigationTitle("New Note")
    .toolbar {
      ToolbarItem(placement: .cancellationAction) {
        Button("Cancel") {
          viewModel.backButtonTapped()
        }
      }

      ToolbarItem(placement: .confirmationAction) {
        Button("Save") {
          viewModel.saveNote(title: title, text: text)
        }
        .disabled(
          title.trimmingCharacters(in: .whitespaces).isEmpty || text.trimmingCharacters(in: .whitespaces).isEmpty)
      }
    }
  }
}
