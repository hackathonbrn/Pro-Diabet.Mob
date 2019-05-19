package raitoningu.pro_diabet.View;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import raitoningu.pro_diabet.R;
import raitoningu.pro_diabet.ViewModel.NoteViewModel;

public class HomeFragment extends Fragment {
    private CardView add;
    private NoteViewModel noteViewModel;

    private TextView monthYear;
    private TextView date;
    private TextView week;
    private TextView lastValue;

    private ImageView chevronLeft;
    private ImageView chevronRight;

    private final Calendar calendar = new GregorianCalendar();
    private long startDay, endDay;

    private SparkView sparkView;
    private float[] data = {0};

    final private NoteAdapter adapter = new NoteAdapter();
    private AddEditFragment addDialog;

    static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home, container, false);
        monthYear = root.findViewById(R.id.main_text_month);
        date = root.findViewById(R.id.main_text_date);
        week = root.findViewById(R.id.main_text_day);
        lastValue = root.findViewById(R.id.main_text_last_value);
        sparkView = root.findViewById(R.id.sparkview);

        chevronLeft = root.findViewById(R.id.main_chevron_left);
        chevronRight = root.findViewById(R.id.main_chevron_right);

        RecyclerView recyclerView = root.findViewById(R.id.main_notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(adapter);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND,0);
        startDay = calendar.getTimeInMillis();
        endDay = startDay + 86400000 - 1;
        date.setOnClickListener(v -> callDateDialog());

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getDayNotes(startDay, endDay).observe(this, observer);
        setDateText();

        chevronLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(calendar.getTimeInMillis()-86400000);
                setDateText();
                noteViewModel.getDayNotes(startDay, endDay).removeObservers(getViewLifecycleOwner());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                startDay = calendar.getTimeInMillis();
                endDay = startDay + 86400000;
                setDateText();
                noteViewModel.getDayNotes(startDay, endDay).observe(getViewLifecycleOwner(), observer);
            }
        });
        chevronRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setTimeInMillis(calendar.getTimeInMillis()+86400000);
                if (System.currentTimeMillis()<calendar.getTimeInMillis()) calendar.setTimeInMillis(System.currentTimeMillis());
                else {
                    setDateText();
                    noteViewModel.getDayNotes(startDay, endDay).removeObservers(getViewLifecycleOwner());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.MILLISECOND,0);
                    startDay = calendar.getTimeInMillis();
                    endDay = startDay + 86400000 - 1;
                    setDateText();
                    noteViewModel.getDayNotes(startDay, endDay).observe(getViewLifecycleOwner(), observer);
                }
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnNoteClickListener(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(NoteEntity noteEntity) {
                AddEditFragment editFragment = new AddEditFragment(noteEntity);
                editFragment.show(getFragmentManager(), "Edit");
            }
        });
        /*Add note Dialog*/
        add = root.findViewById(R.id.main_note_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog = new AddEditFragment(calendar);
                addDialog.show(getFragmentManager(), "AddNote");
            }
        });
        /*End*/

        /*Spark Graph*/
        sparkView.setAdapter(new MyAdapter(data));
        /*End*/

        return root;
    }

    public class MyAdapter extends SparkAdapter {
        private float[] yData;

        public MyAdapter(float[] yData) {
            this.yData = yData;
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }

    private void callDateDialog() {
        final long tmpCalendar = calendar.getTimeInMillis();
        DatePickerDialog dateDialog = new DatePickerDialog(this.getContext(),
                null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    noteViewModel.getDayNotes(startDay, endDay).removeObservers(this);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.MILLISECOND,0);
                    startDay = calendar.getTimeInMillis();
                    endDay = startDay + 86400000 - 1;
                    setDateText();
                    noteViewModel.getDayNotes(startDay, endDay).observe(this, observer);

                });
        dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена",
                (dialog, which) -> calendar.setTimeInMillis(tmpCalendar));
        dateDialog.getDatePicker().init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                (datePicker, y, m, d) -> calendar.set(y, m, d));
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 80000);
        dateDialog.show();
    }

    public void setDateText() {
        monthYear.setText(new SimpleDateFormat("MMM.''yy", Locale.ROOT).format(calendar.getTime()));
        date.setText(new SimpleDateFormat("dd", Locale.ROOT).format(calendar.getTime()));
        week.setText(new SimpleDateFormat("EEE", Locale.ROOT).format(calendar.getTime()));
    }

    public Observer<List<NoteEntity>> observer = new Observer<List<NoteEntity>>() {
        @Override
        public void onChanged(List<NoteEntity> noteEntities) {
            adapter.submitList(noteEntities);
            data = adapter.getListData(noteEntities);
            if (data.length < 2) sparkView.setVisibility(View.GONE);
            else sparkView.setVisibility(View.VISIBLE);
            sparkView.setAdapter(new MyAdapter(data));
            if (!noteEntities.isEmpty()) lastValue.setText(String.valueOf(data[data.length-1]));
            else lastValue.setText("-.-");
        }
    };
}
