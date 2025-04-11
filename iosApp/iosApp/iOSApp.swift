import shared
import FirebaseCore
import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
      NotesScreen(viewModel: .init(viewModel: .init(loadNotesUseCase: .init())))
        .onAppear {
          // TODO: With SPM it is impossible to call firebase initialization
          FirebaseApp.configure()
        }
		}
	}
}
