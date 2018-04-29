package itesm.mx.organizatec;

import android.provider.BaseColumns;

public class DataBaseSchema {
    private DataBaseSchema() {}

    public static class MaterialTable implements BaseColumns {
        public static final String TABLE_NAME = "Material";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_TOPIC = "topic";
        public static final String COLUMN_NAME_PARTIAL = "partial";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_CONTENT = "content";
    }

    public static class NoteImageTable implements BaseColumns {
        public static final String TABLE_NAME = "NoteImage";
        public static final String COLUMN_NAME_NOTE_ID = "note_id";
        public static final String COLUMN_NAME_IMAGE = "image";

    }

}
