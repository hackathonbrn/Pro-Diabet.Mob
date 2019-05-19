package raitoningu.pro_diabet.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.shawnlin.numberpicker.NumberPicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import raitoningu.pro_diabet.Model.DataBase.Entity.NoteEntity;
import raitoningu.pro_diabet.R;
import raitoningu.pro_diabet.ViewModel.NoteViewModel;

public class AddEditFragment extends DialogFragment {
    /*Sugar*/
    private TextView sugarUnit;
    private CardView sugarCard;
    private RelativeLayout sugarPickersLayout;
    private CheckBox sugarCheck;
    private NumberPicker pickerSugar;
    private NumberPicker pickerSugarSec;
    private float sugar;
    /*Bread*/
    private TextView breadUnit;
    private CardView breadCard;
    private RelativeLayout breadPickersLayout;
    private NumberPicker pickerBread;
    private NumberPicker pickerBreadSec;
    private float bread;
    /*Insulin*/
    private TextView insulinUnit;
    private CardView insulinCard;
    private RelativeLayout insulinPickersLayout;
    private NumberPicker pickerInsulin;
    private NumberPicker pickerInsulinSec;
    private TextView insulinShortText;
    private TextView insulinLongText;
    private float insulinShort, insulinLong;
    private String insulinText;
    /*Comment*/
    private TextInputEditText commentText;
    private String comment;
    /*Date*/
    private TextView dateText;
    private TextView timeText;
    private Calendar calendar;
    private DateFormat dateFormat = new SimpleDateFormat("dd MMM ''yy", Locale.ROOT);
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.ROOT);
    /*Navigation*/
    private ImageView back;
    private ImageView done;

    private TextView title;
    private NoteEntity note;
    private boolean isAdd;

    private NoteViewModel noteViewModel;

    public AddEditFragment(Calendar calendar) {
        this.calendar = new GregorianCalendar();
        this.calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        sugar = 0;
        bread = 0;
        insulinShort = 0;
        insulinLong = 0;
        comment = "";
        isAdd = true;
    }

    public AddEditFragment(NoteEntity note) {
        this.note = note;
        this.calendar = new GregorianCalendar();
        this.calendar.setTimeInMillis(note.getDatetime());
        sugar = note.getSugar();
        bread = note.getBread();
        insulinShort = note.getShort_insulin();
        insulinLong = note.getLong_insulin();
        comment = note.getComment();
        isAdd = false;
    }

    private boolean saveNote() {
        if (sugar == 0 && bread == 0 && insulinShort == 0 && insulinLong == 0) {
            Toast.makeText(getContext(), "Note cannot be with all zeros", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        comment = commentText.getText().toString();
        if (isAdd) {
            NoteEntity noteEntity = new NoteEntity(calendar.getTimeInMillis(), sugar, bread,
                    insulinShort, insulinLong, comment);
            noteViewModel.insert(noteEntity);
        } else {
            note.setDatetime(calendar.getTimeInMillis());
            note.setSugar(sugar);
            note.setBread(bread);
            note.setShort_insulin(insulinShort);
            note.setLong_insulin(insulinLong);
            note.setComment(comment);
            noteViewModel.update(note);
            isAdd = true;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_add_edit, null);

        findByIds(view);
        setListeners();
        builder.setView(view);
        if (isAdd) title.setText(getResources().getString(R.string.add_note));
        else title.setText(getResources().getString(R.string.edit_note));

        setText();

        pickerSugar.setValue(Math.round(sugar));
        pickerSugarSec.setValue(Math.round((sugar - Math.round(sugar))*10));
        pickerBread.setValue(Math.round(bread));
        pickerBreadSec.setValue(Math.round((bread - Math.round(bread))*10));
        pickerInsulin.setValue(Math.round(insulinShort));
        pickerInsulinSec.setValue(Math.round((insulinShort - Math.round(insulinShort))*10));
        return builder.create();
    }

    private void findByIds(@NonNull View view) {
        sugarCard = view.findViewById(R.id.note_add_card_sugar);
        sugarPickersLayout = view.findViewById(R.id.add_sugar_picker_ll);
        sugarUnit = view.findViewById(R.id.add_sugar_value);
        pickerSugar = view.findViewById(R.id.add_sugar_picker);
        pickerSugarSec = view.findViewById(R.id.add_sugar_picker_float);
        sugarCheck = view.findViewById(R.id.add_sugar_check);

        breadCard = view.findViewById(R.id.note_add_card_bread);
        breadPickersLayout = view.findViewById(R.id.add_bread_picker_ll);
        breadUnit = view.findViewById(R.id.add_bread_value);
        pickerBread = view.findViewById(R.id.add_bread_picker);
        pickerBreadSec = view.findViewById(R.id.add_bread_picker_float);

        insulinCard = view.findViewById(R.id.note_add_card_insulin);
        insulinPickersLayout = view.findViewById(R.id.add_insulin_picker_ll);
        insulinUnit = view.findViewById(R.id.add_insulin_value);
        pickerInsulin = view.findViewById(R.id.add_insulin_picker);
        pickerInsulinSec = view.findViewById(R.id.add_insulin_picker_float);
        insulinShortText = view.findViewById(R.id.add_insulin_text_short);
        insulinLongText = view.findViewById(R.id.add_insulin_text_long);

        commentText = view.findViewById(R.id.add_comment_text);

        dateText = view.findViewById(R.id.note_add_date);
        timeText = view.findViewById(R.id.note_add_time);

        title = view.findViewById(R.id.note_title_add_edit);
        back = view.findViewById(R.id.note_add_back_arrow);
        done = view.findViewById(R.id.note_button_add);
    }

    private void setListeners() {
        sugarCard.setOnClickListener(v -> {
            if (sugarPickersLayout.getVisibility() == View.GONE) {
                sugarPickersLayout.setVisibility(View.VISIBLE);
                breadPickersLayout.setVisibility(View.GONE);
                insulinPickersLayout.setVisibility(View.GONE);
                sugarCheck.setChecked(false);
            } else sugarPickersLayout.setVisibility(View.GONE);
        });
        sugarCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                sugarPickersLayout.setVisibility(View.GONE);
                sugarUnit.setTextColor(getResources().getColor(R.color.greyColor));
                sugar = 0;
            } else {
                sugarPickersLayout.setVisibility(View.VISIBLE);
                if (sugar != 0)
                    sugarUnit.setTextColor(getResources().getColor(R.color.primaryColor));
            }
        });
        pickerSugar.setOnValueChangedListener((picker, oldVal, newVal) -> {
            sugar = pickerSugar.getValue() + (float) pickerSugarSec.getValue() / 10;
            sugarUnit.setText(String.valueOf(sugar));
            if (sugar == 0) sugarUnit.setTextColor(getResources().getColor(R.color.greyColor));
            else sugarUnit.setTextColor(getResources().getColor(R.color.primaryColor));
        });
        pickerSugarSec.setOnValueChangedListener((picker, oldVal, newVal) -> {
            sugar = pickerSugar.getValue() + (float) pickerSugarSec.getValue() / 10;
            sugarUnit.setText(String.valueOf(sugar));
            if (sugar == 0) sugarUnit.setTextColor(getResources().getColor(R.color.greyColor));
            else sugarUnit.setTextColor(getResources().getColor(R.color.primaryColor));
        });

        breadCard.setOnClickListener(v -> {
            if (breadPickersLayout.getVisibility() == View.GONE) {
                breadPickersLayout.setVisibility(View.VISIBLE);
                sugarPickersLayout.setVisibility(View.GONE);
                insulinPickersLayout.setVisibility(View.GONE);
            } else breadPickersLayout.setVisibility(View.GONE);
        });
        pickerBread.setOnValueChangedListener((picker, oldVal, newVal) -> {
            bread = pickerBread.getValue() + (float) pickerBreadSec.getValue() / 10;
            breadUnit.setText(String.valueOf(bread));
            if (bread == 0) breadUnit.setTextColor(getResources().getColor(R.color.greyColor));
            else breadUnit.setTextColor(getResources().getColor(R.color.primaryColor));
        });
        pickerBreadSec.setOnValueChangedListener((picker, oldVal, newVal) -> {
            bread = pickerBread.getValue() + (float) pickerBreadSec.getValue() / 10;
            breadUnit.setText(String.valueOf(bread));
            if (bread == 0) breadUnit.setTextColor(getResources().getColor(R.color.greyColor));
            else breadUnit.setTextColor(getResources().getColor(R.color.primaryColor));
        });

        insulinCard.setOnClickListener(v -> {
            if (insulinPickersLayout.getVisibility() == View.GONE) {
                breadPickersLayout.setVisibility(View.GONE);
                sugarPickersLayout.setVisibility(View.GONE);
                insulinPickersLayout.setVisibility(View.VISIBLE);
            } else insulinPickersLayout.setVisibility(View.GONE);
        });
        insulinShortText.setOnClickListener(v -> {
            insulinShortText.setTextColor(getResources().getColor(R.color.primaryColor));
            insulinLongText.setTextColor(getResources().getColor(R.color.greyColor));
            pickerInsulin.setOnValueChangedListener(insulinShortListener);
            pickerInsulinSec.setOnValueChangedListener(insulinShortListener);
            pickerInsulin.setValue(Math.round(insulinShort));
            pickerInsulinSec.setValue(Math.round((insulinShort - Math.round(insulinShort))*10));
        });
        insulinLongText.setOnClickListener(v -> {
            insulinLongText.setTextColor(getResources().getColor(R.color.primaryColor));
            insulinShortText.setTextColor(getResources().getColor(R.color.greyColor));
            pickerInsulin.setOnValueChangedListener(insulinLongListener);
            pickerInsulinSec.setOnValueChangedListener(insulinLongListener);
            pickerInsulin.setValue(Math.round(insulinLong));
            pickerInsulinSec.setValue(Math.round((insulinLong - Math.round(insulinLong))*10));
        });
        pickerInsulin.setOnValueChangedListener(insulinShortListener);
        pickerInsulinSec.setOnValueChangedListener(insulinShortListener);

        timeText.setOnClickListener(v -> callTimeDialog());
        dateText.setOnClickListener(v -> callDateDialog());
        back.setOnClickListener(v -> dismiss());

        done.setOnClickListener(v -> {
                    saveNote();
                    dismiss();
                }
        );
    }

    private NumberPicker.OnValueChangeListener insulinShortListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            insulinShort = pickerInsulin.getValue() + (float)pickerInsulinSec.getValue()/10;
            if (insulinShort == 0 && insulinLong == 0) {
                insulinText = "0";
                insulinUnit.setTextColor(getResources().getColor(R.color.greyColor));
            } else {
                insulinUnit.setTextColor(getResources().getColor(R.color.primaryColor));
                if (insulinShort != 0 && insulinLong == 0)
                    insulinText = String.valueOf(insulinShort);
                else if (insulinShort == 0 && insulinLong != 0)
                    insulinText = insulinLong + " дл.";
                else insulinText = insulinShort + " + " + insulinLong + " дл.";
            }
            insulinUnit.setText(insulinText);
        }
    };
    private NumberPicker.OnValueChangeListener insulinLongListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            insulinLong = pickerInsulin.getValue() + (float)pickerInsulinSec.getValue()/10;
            if (insulinShort == 0 && insulinLong == 0) {
                insulinText = "0.0";
                insulinUnit.setTextColor(getResources().getColor(R.color.greyColor));
            } else {
                insulinUnit.setTextColor(getResources().getColor(R.color.primaryColor));
                if (insulinShort != 0 && insulinLong == 0)
                    insulinText = String.valueOf(insulinShort);
                else if (insulinShort == 0 && insulinLong != 0)
                    insulinText = insulinLong + " дл.";
                else insulinText = insulinShort + " + " + insulinLong + " дл.";
            }
            insulinUnit.setText(insulinText);
        }
    };

    private void callDateDialog() {
        final long tmpCalendar = calendar.getTimeInMillis();
        DatePickerDialog dateDialog = new DatePickerDialog(this.getContext(),
                null,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ок",
                (dialog, which) -> setDateText());
        dateDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена",
                (dialog, which) -> calendar.setTimeInMillis(tmpCalendar));
        dateDialog.getDatePicker().init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                (datePicker, y, m, d) -> calendar.set(y, m, d));
        dateDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 80000);
        dateDialog.show();
    }

    private void callTimeDialog() {
        final long tmpCalendar = calendar.getTimeInMillis();
        TimePickerDialog timeDialog = new TimePickerDialog(this.getContext(),
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ок",
                ((dialog, which) -> {
                    if (calendar.getTimeInMillis() > System.currentTimeMillis())
                        calendar.setTimeInMillis(System.currentTimeMillis());
                    setTimeText();
                }));
        timeDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Отмена",
                ((dialog, which) -> {
                    calendar.setTimeInMillis(tmpCalendar);
                    setTimeText();
                }));
        timeDialog.show();
    }

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            setTimeText();
        }
    };

    private void setDateText() {
        dateText.setText(dateFormat.format(calendar.getTime()));
    }

    private void setTimeText() {
        timeText.setText(timeFormat.format(calendar.getTime()));
    }

    private void setText() {
        sugarUnit.setText(String.valueOf(sugar));
        breadUnit.setText(String.valueOf(bread));
        if (insulinShort == 0 && insulinLong == 0) {
            insulinText = "0";
            insulinUnit.setTextColor(getResources().getColor(R.color.greyColor));
        } else {
            insulinUnit.setTextColor(getResources().getColor(R.color.primaryColor));
            if (insulinShort != 0 && insulinLong == 0)
                insulinText = String.valueOf(insulinShort);
            else if (insulinShort == 0 && insulinLong != 0)
                insulinText = insulinLong + " дл.";
            else insulinText = insulinShort + " + " + insulinLong + " дл.";
        }
        insulinUnit.setText(insulinText);
        commentText.setText(comment);

        setDateText();
        setTimeText();
    }
}
