import SwiftUI
import shared


class NotesListViewModel: ObservableObject {
  @Published var state: [Note]

  init(notes: [Note]) {
    self.state = notes
  }
}

struct NotesListView: View {
  @ObservedObject var viewModel: NotesListViewModel

  var body: some View {
    List(viewModel.state, id: \.id) { note in
      VStack(alignment: .leading) {
        Text(note.title)
          .font(.headline)
        Text(note.text)
          .font(.subheadline)
      }
      .padding()
    }
  }
}
