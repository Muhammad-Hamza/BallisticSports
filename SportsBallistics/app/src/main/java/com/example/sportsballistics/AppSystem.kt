package com.example.sportsballistics


class AppSystem
{
    val TAG = AppSystem::class.java.name

    companion object{
        /*
        Volatile instance to make singleton
        thread safe
        */
        @Volatile
        private var sSoleInstance: AppSystem? = null

        fun getInstance(): AppSystem {

            //Double check locking pattern
            if (sSoleInstance == null) {

                //Check for the first time
                synchronized(AppSystem::class.java) {   //Check for the second time.
                    //if there is no instance available... create new one
                    if (sSoleInstance == null) {
                        sSoleInstance = AppSystem()
                    }
                }
            }

            return sSoleInstance!!
        }
    }
}