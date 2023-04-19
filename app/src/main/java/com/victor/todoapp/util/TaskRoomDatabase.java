package com.victor.todoapp.util;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.victor.todoapp.data.TaskDao;
import com.victor.todoapp.model.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Annotate the class with @Database and define the entity and version number
@Database(entities = {Task.class}, version = 1, exportSchema = false)
// Declare the abstract class and extend RoomDatabase
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {

    // Declare the number of threads for the executor service
    public static final int NUMBER_OF_THREADS = 4;

    // Declare the volatile INSTANCE variable
    public static volatile TaskRoomDatabase INSTANCE;

    // Declare the name of the database
    public static final String DATABASE_NAME = "todoapp database";

    // Create a fixed thread pool executor service
    public static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract TaskDao taskDao();

    // Declare a callback to be executed when the database is created
    public static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Execute a database write operation in a separate thread
            databaseWriterExecutor.execute(() -> {
                // Any required operations to initialize the database can be performed here
                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAll();
            });
        }
    };

    // Define a static method to get the database instance
    public static TaskRoomDatabase getDatabase(final Context context) {
        // Check if an instance of the database exists
        if (INSTANCE == null) {
            // Use a synchronized block to ensure only one thread creates an instance of the database
            synchronized (TaskRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create a new instance of the database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TaskRoomDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        // Return the instance of the database
        return INSTANCE;
    }

}

