package com.example.databasetest.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyDialogFragment : DialogFragment () {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireContext().let {
            AlertDialog.Builder(it)
                .setTitle("Warning")
                .setMessage("We really need to delete this user?")
                .setPositiveButton("Yes"){dialog, button ->

                }
                .setNegativeButton("No"){dialog, button ->

                }
                .setNeutralButton("Cancel") {dialog, button ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .create()

        }
    }

    companion object {
        const val TAG = "MyDialogFragment"
    }

}