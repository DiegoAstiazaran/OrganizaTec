package itesm.mx.organizatec;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MaterialDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MaterialDB.db";
    public static final int DATABASE_VERSION = 1;

    public MaterialDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MATERIAL_TABLE = "CREATE TABLE " +
                DataBaseSchema.MaterialTable.TABLE_NAME +
                "(" +
                DataBaseSchema.MaterialTable._ID + " INTEGER PRIMARY KEY, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_MATERIAL_TYPE + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT_TYPE + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_NAME + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_TOPIC + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_PARTIAL + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_DATE + " TEXT, " +
                DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT + " TEXT, " +
                "CHECK (" + DataBaseSchema.MaterialTable.COLUMN_NAME_MATERIAL_TYPE +
                " IN (\"Theory\", \"Practice\")), " +
                "CHECK (" + DataBaseSchema.MaterialTable.COLUMN_NAME_CONTENT_TYPE +
                " IN (\"Video\", \"Document\", \"Note\")), " +
                "CHECK (" + DataBaseSchema.MaterialTable.COLUMN_NAME_PARTIAL +
                " IN (\"Primer Parcial\", \"Segundo Parcial\", \"Tercer Parcial\", \"Final\"))" +
                ")";

        db.execSQL(CREATE_MATERIAL_TABLE);

        String CREATE_NOTEIMAGE_TABLE = "CREATE TABLE " +
                DataBaseSchema.NoteImageTable.TABLE_NAME +
                "(" +
                DataBaseSchema.NoteImageTable._ID + " INTEGER PRIMARY KEY, " +
                DataBaseSchema.NoteImageTable.COLUMN_NAME_NOTE_ID + " INTEGER, " +
                DataBaseSchema.NoteImageTable.COLUMN_NAME_IMAGE + " TEXT, " +
                "FOREIGN KEY(" + DataBaseSchema.NoteImageTable.COLUMN_NAME_NOTE_ID + ") REFERENCES " +
                DataBaseSchema.MaterialTable.TABLE_NAME +
                "(" + DataBaseSchema.MaterialTable._ID + ")" +
                ")";

        db.execSQL(CREATE_NOTEIMAGE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DELETE_MATERIAL_TABLE = "DROP TABLE IF EXISTS " +
                DataBaseSchema.MaterialTable.TABLE_NAME;

        String DELETE_NOTEIMAGE_TABLE = "DROP TABLE IF EXISTS " +
                DataBaseSchema.NoteImageTable.TABLE_NAME;

        db.execSQL(DELETE_MATERIAL_TABLE);
        db.execSQL(DELETE_NOTEIMAGE_TABLE);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
