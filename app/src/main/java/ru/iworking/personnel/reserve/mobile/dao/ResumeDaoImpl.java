package ru.iworking.personnel.reserve.mobile.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.common.base.Optional;
import ru.iworking.personnel.reserve.mobile.entity.Resume;
import ru.iworking.personnel.reserve.mobile.util.SampleDataBaseSQLiteHelper;
import ru.iworking.personnel.reserve.mobile.util.SqlProperties;

import java.util.LinkedList;
import java.util.List;

public class ResumeDaoImpl implements ResumeDao {

    private static ResumeDaoImpl INSTANCE;

    public static synchronized ResumeDaoImpl getInstance(Context context) {
        if (INSTANCE == null) { INSTANCE = new ResumeDaoImpl(context); }
        return INSTANCE;
    }

    private final SQLiteOpenHelper sqLiteOpenHelper;

    private ResumeDaoImpl(Context context) {
        this.sqLiteOpenHelper = new SampleDataBaseSQLiteHelper(context);
    }
    
    private SQLiteDatabase getWritableDatabase() {
        return this.sqLiteOpenHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDatabase() {
        return this.sqLiteOpenHelper.getReadableDatabase();
    }

    @Override
    public Optional<Resume> findById(Long id) {
        if (id == null) Log.wtf(this.getClass().getSimpleName(), "id can not be null...");

        Resume resume = null;

        Cursor cursor = this.getReadableDatabase().rawQuery(SqlProperties.Resume.SELECT_TABLE_WHERE_ID, new String[]{ id.toString() });
        if (cursor.moveToFirst()) resume = this.mapCursorToResume(cursor);

        cursor.close();

        return Optional.fromNullable(resume);
    }

    @Override
    public List<Resume> findAll() {
        List<Resume> list = new LinkedList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery(SqlProperties.Resume.SELECT_TABLE, new String[]{});
        while (cursor.moveToNext()) {
            list.add(this.mapCursorToResume(cursor));
        }

        cursor.close();
        return list;
    }

    @Override
    public Optional<Resume> create(Resume obj) {
        if (obj == null) Log.wtf(this.getClass().getSimpleName(), "obj can not be null...");

        ContentValues values = this.mapResumeToContentValues(obj);

        long id = this.getWritableDatabase().insert(SqlProperties.Resume.TABLE_NAME, null, values);
        obj.setId(id);
        return Optional.of(obj);
    }

    @Override
    public Optional<Resume> update(Resume obj) {
        if (obj.getId() == null) Log.wtf(this.getClass().getSimpleName(), "obj.getId() can not be null...");

        ContentValues values = this.mapResumeToContentValues(obj);

        final String whereClause = String.format("%s = ?", SqlProperties.Resume.COLUMN_ID);
        this.getWritableDatabase().update(SqlProperties.Resume.TABLE_NAME, values, whereClause, new String[] { obj.getId().toString() });
        return Optional.of(obj);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) Log.wtf(this.getClass().getSimpleName(), "id can not be null...");
        final String whereClause = String.format("%s = ?", SqlProperties.Resume.COLUMN_ID);
        this.getWritableDatabase().delete(SqlProperties.Resume.TABLE_NAME, whereClause, new String[]{ id.toString() });
    }

    private ContentValues mapResumeToContentValues(Resume resume) {
        ContentValues values = new ContentValues();
        values.put(SqlProperties.Resume.COLUMN_FIRST_NAME, resume.getFirstName());
        values.put(SqlProperties.Resume.COLUMN_LAST_NAME, resume.getLastName());
        values.put(SqlProperties.Resume.COLUMN_MIDDLE_NAME, resume.getMiddleName());
        values.put(SqlProperties.Resume.COLUMN_PROFESSION, resume.getProfession());
        values.put(SqlProperties.Resume.COLUMN_ABOUT_ME, resume.getAboutMe());
        values.put(SqlProperties.Resume.COLUMN_AVATAR, resume.getAvatar());
        return values;
    }

    private Resume mapCursorToResume(Cursor cursor) {
        Resume resume = new Resume();
        resume.setId(cursor.getLong(0));
        resume.setLastName(cursor.getString(1));
        resume.setFirstName(cursor.getString(2));
        resume.setMiddleName(cursor.getString(3));
        resume.setProfession(cursor.getString(4));
        resume.setAboutMe(cursor.getString(5));
        resume.setAvatar(cursor.getBlob(6));
        return resume;
    }

}
