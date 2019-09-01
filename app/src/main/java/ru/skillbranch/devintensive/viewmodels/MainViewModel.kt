package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {
    private val chatRepository = ChatRepository

    private val query = mutableLiveData("")

    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        val archivedChats = chats.filter { it.isArchived }
        if (archivedChats.isEmpty()) {
            return@map chats.map { it.toChatItem() }.sortedBy { it.id.toInt() }
        } else {
            val chatsWithArchive = mutableListOf<ChatItem>()
            chatsWithArchive.add(0, Chat.toArchiveItem(archivedChats))
            chatsWithArchive.addAll(chats.filter { !it.isArchived }.map { it.toChatItem() }.sortedBy { it.id.toInt() })
            return@map chatsWithArchive
        }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryString = query.value!!
            val chatsList = chats.value!!
            result.value = if (queryString.isEmpty()) chatsList
            else chatsList.filter { it.title.contains(queryString, true) }
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }
}
