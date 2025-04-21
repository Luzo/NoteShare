import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.noteshare.android.notes.list.NoteItem
import com.example.noteshare.notes.list.presentation.NoteListIntent
import com.example.noteshare.notes.list.presentation.NoteListViewModel
import com.example.noteshare.notes.model.Note

@Composable
fun LoadingNotesListView(modifier: Modifier = Modifier, notes: List<Note>) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(notes) { note ->
            NoteItem(true, note, onDeleteTapped = {})
        }
    }
}

@Composable
fun NotesListView(notes: List<Note>, viewModel: NoteListViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(notes) { note ->
            NoteItem(
                isLoading = false,
                note,
                onDeleteTapped = {
                    viewModel.sendIntent(NoteListIntent.DeleteNoteTapped(it))
                }
            )
        }
    }
}
