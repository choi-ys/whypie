package me.whypie.domain.redis.common.config

import java.util.*

/**
 * @author : choi-ys
 * @date : 2021-10-03 오후 7:07
 */
enum class OS(command: String, shell: List<String>) {
    WINDOWS("netstat -nao | find \"LISTEN\" | find \"%d\"", listOf("cmd.exe", "/y", "/c")),
    OTHERS("netstat -nat | grep LISTEN | grep %d", listOf("/bin/sh", "-c"));

    private val command: String
    var shell: List<String>
    fun mapTo(port: Int): Array<String> {
        val mutableShellCommandList = ArrayList<String>()
        mutableShellCommandList.addAll(shell)
        mutableShellCommandList.add(String.format(command, port))
        return mutableShellCommandList.toTypedArray()
    }

    init {
        this.command = command
        this.shell = shell
    }
}