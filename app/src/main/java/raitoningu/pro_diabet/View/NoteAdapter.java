package raitoningu.pro_diabet.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import raitoningu.pro_diabet.R;

public class NoteAdapter extends ListAdapter<NoteEntity, NoteAdapter.NoteHolder> {
    private OnNoteClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<NoteEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull NoteEntity oldItem, @NonNull NoteEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteHolder(view);
    }

    public NoteEntity getNoteAt(int pos) {
        return getItem(pos);
    }

    public float[] getListData(List<NoteEntity> noteEntities) {
        float[] data = new float[noteEntities.size()];
        int count = 0;
        int j = 0;
        float[] f = {0};
        if (noteEntities.isEmpty())
            return f;
        else {
            for (int i = 0; i < noteEntities.size(); i++) {
                data[i] = noteEntities.get(noteEntities.size() - i - 1).getSugar();
                if (data[i] > 0) count++;
            }
            if (count == 0) return f;
            else {
                float[] out = new float[count];
                for (int i = 0; i < noteEntities.size(); i++) {
                    if (data[i] > 0) {
                        out[j] = data[i];
                        j++;
                    }
                }
                return out;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        NoteEntity currentNote = getItem(position);
        String insulinText;
        float insulinShort = currentNote.getShort_insulin();
        float insulinLong = currentNote.getLong_insulin();
        holder.textTime.setText(
                new SimpleDateFormat("HH:mm", Locale.ROOT)
                        .format(new Date(currentNote.getDatetime())));
        holder.textSugar.setText(String.valueOf(currentNote.getSugar()));
        holder.textBread.setText(String.valueOf(currentNote.getBread()));
        if (insulinShort == 0 && insulinLong == 0) {
            insulinText = "0";
        } else {
            if (insulinShort != 0 && insulinLong == 0)
                insulinText = String.valueOf(insulinShort);
            else if (insulinShort == 0 && insulinLong != 0)
                insulinText = insulinLong + " дл.";
            else insulinText = insulinShort + " + " + insulinLong + " дл.";
        }
        holder.textInsulin.setText(insulinText);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textTime;
        private TextView textSugar;
        private TextView textBread;
        private TextView textInsulin;

        public NoteHolder(@NonNull View view) {
            super(view);
            textTime = view.findViewById(R.id.note_text_time);
            textBread = view.findViewById(R.id.note_text_bread);
            textInsulin = view.findViewById(R.id.note_text_insulin);
            textSugar = view.findViewById(R.id.note_text_sugar);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION)
                        listener.onNoteClick(getItem(pos));
                }
            });
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(NoteEntity noteEntity);
    }

    public void setOnNoteClickListener(OnNoteClickListener listener) {
        this.listener = listener;
    }
}
