package dev.nh7.mtwa.backend

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {

    //wallets

    val wallets = mutableStateListOf<File>()

    //terminal messages

    val terminalMessages = mutableStateListOf<TerminalMessage>()

    //status

    val walletStarted = mutableStateOf(false)

    //cli

    private val moneroCli = MoneroCli(application.applicationContext)

    private var lastReceivedMessage: String = ""

    init {
        wallets.addAll(moneroCli.getWallets())

        moneroCli.messageListener = { message ->
            terminalMessages.add(TerminalMessage(false, message))
            lastReceivedMessage = message
        }

        moneroCli.statusListener = { started ->
            walletStarted.value = started

            if (!started) {
                terminalMessages.clear()
                wallets.clear()
                wallets.addAll(moneroCli.getWallets())
                Handler(Looper.getMainLooper()).post {
                    showToast(lastReceivedMessage) //todo
                }
            }
        }
    }

    fun sendCommand(command: String) {
        val success = moneroCli.sendCommand(command)
        if (success) {
            terminalMessages.add(TerminalMessage(true, command))
        }
    }

    fun openWallet(walletFile: File, password: String) =
        moneroCli.startOpenWallet(walletFile, password)

    fun createWallet(walletName: String, password: String) =
        moneroCli.startCreateWallet(walletName, password)

    fun stopWallet() = moneroCli.stopWallet()

    //other

    fun showToast(string: String) {
        Toast.makeText(getApplication(), string, Toast.LENGTH_SHORT).show()
    }

    fun showToast(stringId: Int) {
        Toast.makeText(getApplication(), stringId, Toast.LENGTH_SHORT).show()
    }

}