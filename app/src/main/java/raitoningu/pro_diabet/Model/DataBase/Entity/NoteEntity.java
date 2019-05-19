package raitoningu.pro_diabet.Model.DataBase.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "notes_table")
public class NoteEntity {
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public int id;
    @ColumnInfo(name = "date")
    @SerializedName("date")
    public long datetime;
    @SerializedName("sugar")
    public float sugar;
    @SerializedName("bread")
    public float bread;
    @SerializedName("short_insulin")
    public float short_insulin;
    @SerializedName("long_insulin")
    public float long_insulin;
    @SerializedName("comment")
    public String comment;

    public NoteEntity(long datetime, float sugar, float bread, float short_insulin, float long_insulin, String comment) {
        this.datetime = datetime;
        this.sugar = sugar;
        this.bread = bread;
        this.short_insulin = short_insulin;
        this.long_insulin = long_insulin;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public float getSugar() {
        return sugar;
    }

    public void setSugar(float sugar) {
        this.sugar = sugar;
    }

    public float getBread() {
        return bread;
    }

    public void setBread(float bread) {
        this.bread = bread;
    }

    public float getShort_insulin() {
        return short_insulin;
    }

    public void setShort_insulin(float short_insulin) {
        this.short_insulin = short_insulin;
    }

    public float getLong_insulin() {
        return long_insulin;
    }

    public void setLong_insulin(float long_insulin) {
        this.long_insulin = long_insulin;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
