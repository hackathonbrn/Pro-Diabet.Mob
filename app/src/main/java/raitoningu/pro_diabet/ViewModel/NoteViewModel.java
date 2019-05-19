package raitoningu.pro_diabet.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import raitoningu.pro_diabet.Model.Repository.AppRepository;
import raitoningu.pro_diabet.Model.Repository.WebRepository;

public class NoteViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<NoteEntity>> allnotes, dayNotes;

    private final LiveData<List<NoteEntity>> retroObservable;
    private WebRepository webRepository;

    public NoteViewModel (@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allnotes = repository.getAllNotes();

        webRepository = new WebRepository();
        retroObservable = webRepository.providesWebService();

    }

    public void insert(NoteEntity note) {
        repository.insert(note);
    }

    public void update(NoteEntity note) {
        repository.update(note);
    }

    public void delete(NoteEntity note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {repository.deleteAllNotes();}

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allnotes;
    }
    public LiveData<List<NoteEntity>> getDayNotes(long start, long end) {
        dayNotes = repository.getDayNotes(start, end);
        return dayNotes;
    }
    public LiveData<List<NoteEntity>> getRetroObservable() {
        return retroObservable;
    }
}
