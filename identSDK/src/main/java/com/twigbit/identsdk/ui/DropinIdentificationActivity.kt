package com.twigbit.identsdk.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.twigbit.identsdk.IdentificationActivity
import com.twigbit.identsdk.Message
import com.twigbit.identsdk.R

class DropinIdentificationActivity : IdentificationActivity() {

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
        setContentView(R.layout.activity_dropin_identification)
    }
}
