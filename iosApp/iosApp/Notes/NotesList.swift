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
    }
  }
}
