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
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_MATERIAL_TYPE, material.getMaterialType());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT_TYPE, material.getContentType());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_NAME, material.getName());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_TOPIC, material.getTopic());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_PARTIAL, material.getPartial());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_DATE, material.getDate());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT, material.getContent());

            newRowId = db.insert(DataBaseSchema.MaterialTable.TABLE_NAME, null, values);

            if(material.getContentType().equals("Note")) {
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

    public ArrayList<Material> getAllMaterials(String materialType, String contentType) {

        ArrayList<Material> materials = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DataBaseSchema.MaterialTable.TABLE_NAME +
                            " WHERE " + DataBaseSchema.MaterialTable.COLUMN_NAME_MATERIAL_TYPE + " = \"" + materialType + "\"" +
                            " AND " + DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT_TYPE + " = \"" + contentType + "\"";

        try {
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor.moveToFirst()) {
                do {
                    String noteType = cursor.getString(2);

                    if (noteType.equals("Note")) {
                        long noteId = Integer.parseInt(cursor.getString(0));
                        ArrayList<byte[]> images = getNoteImages(noteId);
                        Material material = new Material(
                                noteId,
                                cursor.getString(1),
                                noteType,
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7),
                                images
                        );

                        materials.add(material);
                    } else {
                        Material material = new Material(
                                Integer.parseInt(cursor.getString(0)),
                                cursor.getString(1),
                                noteType,
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5),
                                cursor.getString(6),
                                cursor.getString(7)
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

    public long updateMaterial(Material material) {
        long newRowId = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_MATERIAL_TYPE, material.getMaterialType());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT_TYPE, material.getContentType());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_NAME, material.getName());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_TOPIC, material.getTopic());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_PARTIAL, material.getPartial());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_DATE, material.getDate());
            values.put(DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT, material.getContent());

            newRowId = db.update(DataBaseSchema.MaterialTable.TABLE_NAME, values, "_id = " + material.getId(), null);

            if(material.getContentType().equals("Note")) {
                deleteAllNoteImages(material.getId());

                ArrayList<byte[]> images = material.getImages();
                for(byte[] image : images){
                    addNoteImage(image, newRowId);
                }
            }

        } catch (SQLException e) {
            Log.e("SQLUPDATE", e.toString());
        }

        return newRowId;
    }

    private void deleteAllNoteImages(long noteId) {
        try {
            db.delete(DataBaseSchema.NoteImageTable.TABLE_NAME,
            DataBaseSchema.NoteImageTable.COLUMN_NAME_NOTE_ID + " = " + noteId, null);
        } catch (SQLException e) {
            Log.e("SQLDELETE", e.toString());
        }

    }

    public void deleteMaterial(Material material) {
        try {
            db.delete(DataBaseSchema.MaterialTable.TABLE_NAME,
                    "_id = " + material.getId(), null);

            if(material.getContentType().equals("Note")) {
                deleteAllNoteImages(material.getId());
            }

        } catch (SQLException e) {
            Log.e("SQLDELETE", e.toString());
        }

    }



}
