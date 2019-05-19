package raitoningu.pro_diabet.Model.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import raitoningu.pro_diabet.Model.DataBase.AppDatabase;
import raitoningu.pro_diabet.Model.DataBase.Dao.NoteDao;
import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;

public class AppRepository {
    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allNotes, dayNotes;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(NoteEntity note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public void update(NoteEntity note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public void delete(NoteEntity note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(NoteEntity... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public void deleteAllNotes() {
        new DeleteAllTaskAsyncTask(noteDao).execute();
    }
    private static class DeleteAllTaskAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;
        private  DeleteAllTaskAsyncTask(NoteDao noteDao) {this.noteDao = noteDao;}
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public LiveData<List<NoteEntity>> getDayNotes(long startDay, long endDay) {
        dayNotes = noteDao.getDayNotes(startDay, endDay);
        return dayNotes;
    }
}
