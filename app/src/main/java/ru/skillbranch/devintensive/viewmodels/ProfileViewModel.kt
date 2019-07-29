package ru.skillbranch.devintensive.viewmodels

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.repositories.PreferencesRepository
import ru.skillbranch.devintensive.utils.Utils

class ProfileViewModel : ViewModel() {
    private val repository = PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()
    private val isRepositoryInvalid = MutableLiveData<Boolean>()
    private val isRepositoryErrorEnabled = MutableLiveData<Boolean>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
    }

    fun getTheme(): LiveData<Int> = appTheme

    fun getProfileData(): LiveData<Profile> = profileData

    fun saveProfileData(profile: Profile) {
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun getIsRepositoryInvalid(): LiveData<Boolean> = isRepositoryInvalid

    fun getIsRepositoryErrorEnabled(): LiveData<Boolean> = isRepositoryErrorEnabled

    fun onRepositoryChanged(repository: String) {
        isRepositoryInvalid.value = !Utils.isRepositoryValid(repository)
    }

    fun afterRepositoryChanged(isError: Boolean) {
        isRepositoryErrorEnabled.value = isError
    }
}