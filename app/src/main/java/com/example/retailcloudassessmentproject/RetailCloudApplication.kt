package com.example.retailcloudassessmentproject

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RetailCloudApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
    }
}