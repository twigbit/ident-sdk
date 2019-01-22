package com.twigbit.identsdk.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twigbit.identsdk.IdentificationActivity
import com.twigbit.identsdk.Message
import android.content.Intent.getIntent



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
        val dropInRequest = DropInRequest()
            .clientToken("RmluZ2VycHJpbnQiOiI")
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE_IDENTIFICATION)
    }
}
