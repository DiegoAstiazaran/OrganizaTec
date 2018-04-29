package itesm.mx.organizatec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;

public class MaterialOperations {

    private SQLiteDatabase db;
    private MaterialDBHelper dbHelper;

    public MaterialOperations(Context context) { dbHelper = new MaterialDBHelper (context); }

    public void open() throws SQLException {
        try{
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("SQLOPEN", e.toString());
        }
    }

    public void close() {
        db.close();
    }

    public long addMaterial(Material material) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_TYPE, material.getType());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_NAME, material.getName());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_TOPIC, material.getTopic());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_PARTIAL, material.getPartial());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_DATE, material.getDate());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT, material.getContent());

            newRowId = db.insert(DataBaseSchema.MaterialTable.TABLE_NAME, null, values);

            if(material.getType().equals("Note")) {
                ArrayList<byte[]> images = material.getImages();
                for(byte[] image : images){
                    addNoteImage(image, newRowId);
                }
            }

        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }

        return newRowId;
    }

    private long addNoteImage(byte[] image, long noteId) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.NoteImageTable._ID, image);
            values.put(DataBaseSchema.NoteImageTable.COLUMN_NAME_NOTE_ID, noteId);
            values.put(DataBaseSchema.NoteImageTable.COLUMN_NAME_IMAGE, image);

            newRowId = db.insert(DataBaseSchema.NoteImageTable.TABLE_NAME, null, values);

        } catch (SQLException e) {
            Log.e("SQLADD", e.toString());
        }

        return newRowId;
    }

    public ArrayList<Material> getAllMaterials() {

        ArrayList<Material> materials = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DataBaseSchema.MaterialTable.TABLE_NAME;

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    String noteType = cursor.getString(1);

                    if (noteType.equals("Note")) {
                        long noteId = Integer.parseInt(cursor.getString(0));
                        ArrayList<byte[]> images = getNoteImages(noteId);
                        Material material = new Material(
                                noteId,
                                noteType,
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                images
                        );

                        materials.add(material);
                    } else {
                        Material material = new Material(
                                Integer.parseInt(cursor.getString(0)),
                                noteType,
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6)
                        );

                        materials.add(material);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("SQLList", e.toString());
        }

        return materials;

    }

    private ArrayList<byte[]> getNoteImages(long noteId) {
        ArrayList<byte[]> images = new ArrayList<>();

        String query = "Select * FROM " + DataBaseSchema.NoteImageTable.TABLE_NAME + " WHERE " + DataBaseSchema.NoteImageTable.COLUMN_NAME_NOTE_ID + "=" + noteId;

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    images.add(cursor.getBlob(2));
                } while (cursor.moveToNext());
            }

            cursor.close();
        } catch (SQLiteException e){
            Log.e("SQLLISTIMAGES", e.toString());
        }

        return images;
    }

}
