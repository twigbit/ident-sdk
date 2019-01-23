package com.twigbit.identsdk.sample

import android.os.Bundle
import com.twigbit.identsdk.IdentificationActivity
import com.twigbit.identsdk.Message
import com.twigbit.identsdk.DropInRequest
import android.app.Activity
import android.content.Intent




class MainActivity : IdentificationActivity() {
    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessage(message: Message) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onComplete(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val REQUEST_CODE_IDENTIFICATION = 0;
    private fun startDropInIdentification(){
        val dropInRequest = DropInRequest("RmluZ2VycHJpbnQiOiI")
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_IDENTIFICATION)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_IDENTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                // Success. Update the UI to reflect the successful identification
                // and fetch the user data from the server where they were delivered.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the identification
            } else {
                // An error occured during the identification
            }
        }
    }
}
