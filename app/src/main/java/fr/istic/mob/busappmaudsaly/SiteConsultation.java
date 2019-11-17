package fr.istic.mob.busappmaudsaly;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author Maud Garcon & Saly Knab
 *
 * Service qui consulte le site : https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/export/
 * Qui regarde si on nouveau fichier CSV est disponible
 *
 * TODO : rechercher comment trouver le nouveau fihcier csv dispo sur le site
 * TODO : faire une notification comme quoi nouveau csv
 * TODO : cliqué et ouvre l'activity TelechargementActivity
 * TODO : enregister la verion (pas dans le service)
 * TODO : service qui regarde de temps en temps si nouvelle version (dans ce cas si tel echou ou aura toujours pas la bonne version donc une nouvelle notif plus tard)
 * TODO : test : lors de l'installation on télécharge automatiquement le 1 fichier JSON (mais pas zip qui est dedans)
 */

public class SiteConsultation extends Service {
    public SiteConsultation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
