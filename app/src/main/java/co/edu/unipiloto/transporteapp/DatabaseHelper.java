package co.edu.unipiloto.transporteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PublicacionesDB";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_PUBLICACIONES = "publicaciones";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UBICACION = "ubicacion";
    public static final String COLUMN_NOMBRE_CARGA = "nombre_carga";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_CORREO = "correo";
    public static final String COLUMN_DESTINO = "destino";
    public static final String COLUMN_NUMERO = "numero";
    public static final String COLUMN_ESTADO = "estado";
    public static final String COLUMN_OBSERVACION = "observacion";
    public static final String COLUMN_MENSAJE = "mensaje";
    public static final String COLUMN_CONDUCTOR = "conductor";



    private static final String DATABASE_CREATE = "create table "
            + TABLE_PUBLICACIONES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_UBICACION
            + " text not null, " + COLUMN_NOMBRE_CARGA
            + " text not null, " + COLUMN_PRECIO
            + " text not null, " + COLUMN_CORREO
            + " text not null, " + COLUMN_DESTINO
            + " text not null, " + COLUMN_NUMERO
            + " text not null, " + COLUMN_ESTADO
            + " text not null, " + COLUMN_OBSERVACION
            + " text not null, " + COLUMN_MENSAJE
            + " text not null, " + COLUMN_CONDUCTOR
            + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_PUBLICACIONES);
        onCreate(db);
    }
}