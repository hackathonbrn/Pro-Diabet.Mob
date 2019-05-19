package raitoningu.pro_diabet.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import raitoningu.pro_diabet.R;
import raitoningu.pro_diabet.ViewModel.NoteViewModel;

public class ChartsFragment extends Fragment {
    private NoteViewModel noteViewModel;
    private LineChart lineChart;

    static ChartsFragment newInstance() {
        return new ChartsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.graph, container, false);
        lineChart = root.findViewById(R.id.chart);
        lineChart.getDescription().setText("Ммоль/л");
        lineChart.getXAxis().setTextColor(getResources().getColor(R.color.whiteColor));
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                List<Entry> entries = new ArrayList<>();
                for (NoteEntity note : noteEntities) {
                    if (note.getSugar() > 0)
                        entries.add(new Entry(note.getDatetime(), note.getSugar()));
                }
                LineDataSet lineDataSet = new LineDataSet(entries, "Уровень сахара");
                LineData lineData = new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineChart.invalidate();
            }
        });

        return root;
    }
}
