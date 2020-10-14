package org.meowcat.minecraft.forward

import com.github.shynixn.mccoroutine.registerSuspendingEvents
import com.github.shynixn.mccoroutine.setSuspendingExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.data.content
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.meowcat.minecraft.forward.data.Agent
import org.meowcat.minecraft.forward.data.ConfigService
import kotlin.collections.HashMap
import kotlin.collections.HashSet

class Forward : JavaPlugin() {

    companion object{
        val configSolver by lazy { ConfigService.create() }

        //保存登录的机器人对象
        val allBots by lazy { HashMap<Agent,Bot>() }
        //记录用于监听的bot
        val listeners by lazy { HashMap<Agent,Bot>() }
        //记录用于发言的bot
        val speakers by lazy { HashMap<Agent,Bot>() }

        //用于保存机器人创建者的map
        val operating by lazy { HashMap<Agent,String>() }
        //保存所有类型的群聊
        val targets by lazy  { HashSet<Long>() }
        val targetTo by lazy { HashSet<Long>() }
        val targetFrom by lazy { HashSet<Long>() }

        //记录机器人的类型
        val type by lazy { HashMap<String, Type>() }

    }

    override fun onEnable() {
        saveDefaultConfig();
        launch {
            subscribeAlways<GroupMessageEvent>(Dispatchers.Default) {
                when(group.id){
                    226556947L -> Bukkit.broadcastMessage("<${this.sender.nameCardOrNick}> ${message.content}")
                }
            }
        }

        logger.info("Forward Loading")
        //注册消息监听器
        server.pluginManager.registerSuspendingEvents(MessageListener(),this)

        //注册命令处理器
        server.getPluginCommand("forward")!!.setSuspendingExecutor(CommandExecutor())

    }

    override fun onDisable() {
    }

    @Serializable
    enum class Type(private val type: String) {
        LISTEN("LISTEN"),SPEAK("SPEAK"),BOTH("BOTH"),NONE("NONE");
        override fun toString(): String {
            return this.type
        }
    }
}