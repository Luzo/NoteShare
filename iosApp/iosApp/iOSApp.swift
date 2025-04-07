import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
      NotesScreen(viewModel: .init(viewModel: .init(loadNotesUseCase: .init())))
		}
	}
}
