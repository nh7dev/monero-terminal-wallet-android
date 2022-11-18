package dev.nh7.mtwa.backend

import android.content.Context
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File

class MoneroCli(context: Context) {

    //files

    private val appDirectory = context.filesDir
    private val walletDirectory = File(appDirectory, "wallets")
    private val logFile = File(appDirectory, "log")
    private val ringdbDirectory = File(appDirectory, "ringdb")

    fun getWallets(): List<File> {
        walletDirectory.mkdirs() //todo
        return walletDirectory
            .listFiles { file -> file.extension.isEmpty() }
            ?.sortedBy { file -> file.lastModified() }
            ?: emptyList()
    }

    //process

    private val libraryDirectory = File(context.applicationInfo.nativeLibraryDir)

    private var started = false

    private var inputStream: BufferedReader? = null
    private var outputStream: BufferedWriter? = null

    var messageListener: (message: String) -> Unit = {}

    var statusListener: (started: Boolean) -> Unit = {}

    fun startCreateWallet(walletName: String, password: String) {
        startWallet(File(walletDirectory, walletName), password, true)
    }

    fun startOpenWallet(walletFile: File, password: String) {
        startWallet(walletFile, password, false)
    }

    private fun startWallet(walletFile: File, password: String, create: Boolean) {
        log("starting MoneroCli")

        if (started) throw Exception("already started")
        started = true

        val process = ProcessBuilder()
            .command(
                "./monero.so",

                if (create) "--generate-new-wallet" else "--wallet-file",
                walletFile.absolutePath,

                "--password",
                password,

                /*"--use-english-language-names",
                "--mnemonic-language",
                "english",*/

                "--log-file",
                logFile.absolutePath,

                "--log-level",
                "4",

                "--shared-ringdb-dir",
                ringdbDirectory.absolutePath,
            )
            .directory(libraryDirectory)
            .redirectErrorStream(true)
            .start()

        inputStream = process.inputStream.bufferedReader()
        outputStream = process.outputStream.bufferedWriter()

        statusListener(true)

        Thread {
            while (started) {
                val buffer = CharArray(1024 * 16)
                val read = inputStream!!.read(buffer)
                if (read == -1) break
                val text = buffer.concatToString(endIndex = read)
                log("received: $text")
                messageListener(text)
            }
            stopWallet()
        }.start()
    }

    fun stopWallet() {
        log("stopping MoneroCli")

        statusListener(false)

        started = false
        inputStream = null
        outputStream = null
    }

    fun sendCommand(text: String): Boolean {
        log("sendCommand: $text")
        outputStream?.run {
            write(text.plus('\n'))
            flush() //todo
        } ?: return false
        return true
    }

}