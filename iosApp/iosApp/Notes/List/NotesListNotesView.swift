import SwiftUI
import shared


class NotesListNotesViewModel: ObservableObject {
  @Published var state: [Note]

  init(notes: [Note]) {
    self.state = notes
  }
}

struct NotesListNotesView: View {
  @ObservedObject var viewModel: NotesListNotesViewModel
  var onDelete: ((Note) -> Void)? = nil

  var body: some View {
    if viewModel.state.isEmpty {
      Text("No notes available.")
        .font(.subheadline)
        .foregroundColor(.gray)
        .asStaticItem()
    } else {
      ForEach(viewModel.state, id: \.id) { note in
        VStack(alignment: .leading) {
          Text(note.title)
            .font(.headline)
          Text(note.text)
            .font(.subheadline)
        }
        .padding()
      }
      .onDelete {
        guard let index = $0.first else { return }
        onDelete?(viewModel.state[index])
      }
    }
  }
}
