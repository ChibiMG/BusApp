package fr.istic.mob.busappmaudsaly.task;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import fr.istic.mob.busappmaudsaly.services.SiteConsultation;

public class SiteConsultationTask extends Worker {

    public SiteConsultationTask(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Intent intent = new Intent(getApplicationContext(), SiteConsultation.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(intent);
        }
        else {
            getApplicationContext().startService(intent);
        }
        return Result.success();
    }
}
