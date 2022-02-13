package com.example.sportsballistics.data.listeners

class Listeners {

    interface DialogInteractionListener {
        fun dismissDialog()
        fun addDialog()
        fun addErrorDialog()
        fun addErrorDialog(msg: String?)
    }
    interface NewDialogInteractionListener {
        fun dismissDialog()
        fun addDialog()
        fun addErrorDialog()
        fun addErrorDialog(msg: String?)
        fun makeListEmpty()
    }
}