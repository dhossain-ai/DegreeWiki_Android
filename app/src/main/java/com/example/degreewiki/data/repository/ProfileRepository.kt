package com.example.degreewiki.data.repository

import com.example.degreewiki.data.local.SavedItemDao
import com.example.degreewiki.data.local.SavedItemEntity
import com.example.degreewiki.data.network.DegreeWikiApiService
import com.example.degreewiki.data.network.dto.SaveProgramRequestDto
import com.example.degreewiki.data.network.dto.SavedProgramDto
import com.example.degreewiki.domain.model.ProfileDetails
import com.example.degreewiki.domain.model.Program
import com.example.degreewiki.domain.model.SavedProgram
import com.example.degreewiki.domain.model.UserProfile
import com.example.degreewiki.domain.model.distinctByProgramId
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import retrofit2.HttpException

sealed interface ProfileState {
    data object Loading : ProfileState
    data object LoggedOut : ProfileState
    data object SessionExpired : ProfileState
    data class Authenticated(val profile: UserProfile) : ProfileState
    data class Error(val hasCachedSavedPrograms: Boolean) : ProfileState
}

data class SavedProgramsState(
    val items: List<SavedProgram> = emptyList(),
    val savedItemIdsByProgramId: Map<String, String> = emptyMap(),
    val pendingProgramIds: Set<String> = emptySet(),
    val isRefreshing: Boolean = false,
    val refreshFailed: Boolean = false,
    val hasLoadedFromServer: Boolean = false
) {
    fun isSaved(programId: String): Boolean = savedItemIdsByProgramId.containsKey(programId)
    fun isPending(programId: String): Boolean = programId in pendingProgramIds
}

fun SavedProgramsState.clearedForLogout(): SavedProgramsState = SavedProgramsState()

sealed interface SaveProgramResult {
    data object Success : SaveProgramResult
    data object LoginRequired : SaveProgramResult
    data class Failure(val message: String) : SaveProgramResult
}

interface ProfileRepository {
    val profileState: StateFlow<ProfileState>
    val savedProgramsState: StateFlow<SavedProgramsState>
    suspend fun refresh()
    suspend fun toggleSavedProgram(program: Program): SaveProgramResult
    suspend fun removeSavedProgram(programId: String): SaveProgramResult
}

@Singleton
class DefaultProfileRepository @Inject constructor(
    private val apiService: DegreeWikiApiService,
    private val savedItemDao: SavedItemDao,
    private val authRepository: AuthRepository
) : ProfileRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val mutationMutex = Mutex()
    private var activeUserId: String? = null
    private var savedItemsJob: Job? = null

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Loading)
    override val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _savedProgramsState = MutableStateFlow(SavedProgramsState())
    override val savedProgramsState: StateFlow<SavedProgramsState> =
        _savedProgramsState.asStateFlow()

    init {
        scope.launch {
            authRepository.authState.collectLatest { authState ->
                when (authState) {
                    AuthState.Loading -> _profileState.value = ProfileState.Loading
                    AuthState.Unauthenticated -> transitionToLoggedOut()
                    is AuthState.Authenticated -> transitionToAuthenticated(authState.userId)
                }
            }
        }
    }

    override suspend fun refresh() {
        val userId = activeUserId ?: return
        refreshForUser(userId)
    }

    override suspend fun toggleSavedProgram(program: Program): SaveProgramResult =
        if (_savedProgramsState.value.isSaved(program.id)) {
            removeSavedProgram(program.id)
        } else {
            saveProgram(program)
        }

    override suspend fun removeSavedProgram(programId: String): SaveProgramResult =
        mutationMutex.withLock {
            val userId = activeUserId ?: return@withLock SaveProgramResult.LoginRequired
            val savedItemId = _savedProgramsState.value.savedItemIdsByProgramId[programId]
                ?: return@withLock SaveProgramResult.Success
            setPending(programId, true)
            try {
                apiService.deleteSavedItem(savedItemId)
                savedItemDao.deleteSavedItem(userId, savedItemId)
                SaveProgramResult.Success
            } catch (error: Exception) {
                handleMutationFailure(error, removing = true)
            } finally {
                setPending(programId, false)
            }
        }

    private suspend fun saveProgram(program: Program): SaveProgramResult =
        mutationMutex.withLock {
            val userId = activeUserId ?: return@withLock SaveProgramResult.LoginRequired
            if (_savedProgramsState.value.isSaved(program.id)) {
                return@withLock SaveProgramResult.Success
            }
            setPending(program.id, true)
            try {
                val response = apiService.saveProgram(SaveProgramRequestDto(entityId = program.id))
                val mutation = response.item
                savedItemDao.insertSavedProgram(
                    SavedItemEntity(
                        ownerUserId = userId,
                        savedItemId = mutation.savedItemId,
                        programId = mutation.entityId,
                        slug = program.slug,
                        title = program.title,
                        universityName = program.universityName,
                        countryName = program.countryName,
                        degreeLevel = program.degreeLevel,
                        subject = program.subject,
                        tuitionDisplay = null,
                        durationMonths = null,
                        duration = program.duration,
                        savedAt = mutation.savedAt
                    )
                )
                runCatching { refreshSavedPrograms(userId) }
                    .onSuccess {
                        _savedProgramsState.value = _savedProgramsState.value.copy(
                            hasLoadedFromServer = true,
                            refreshFailed = false
                        )
                    }
                SaveProgramResult.Success
            } catch (error: Exception) {
                handleMutationFailure(error, removing = false)
            } finally {
                setPending(program.id, false)
            }
        }

    private suspend fun transitionToAuthenticated(userId: String) {
        if (activeUserId != userId) {
            activeUserId = userId
            observeSavedPrograms(userId)
        }
        _profileState.value = ProfileState.Loading
        refreshForUser(userId)
    }

    private suspend fun transitionToLoggedOut() {
        val previousUserId = activeUserId
        activeUserId = null
        savedItemsJob?.cancel()
        savedItemsJob = null
        _savedProgramsState.value = _savedProgramsState.value.clearedForLogout()
        if (_profileState.value !is ProfileState.SessionExpired) {
            _profileState.value = ProfileState.LoggedOut
        }
        previousUserId?.let { savedItemDao.deleteForUser(it) }
    }

    private fun observeSavedPrograms(userId: String) {
        savedItemsJob?.cancel()
        savedItemsJob = scope.launch {
            savedItemDao.observeSavedPrograms(userId).collectLatest { entities ->
                val items = entities.map(SavedItemEntity::toDomain).distinctByProgramId()
                _savedProgramsState.value = _savedProgramsState.value.copy(
                    items = items,
                    savedItemIdsByProgramId = items.associate { it.programId to it.savedItemId }
                )
            }
        }
    }

    private suspend fun refreshForUser(userId: String) {
        _savedProgramsState.value = _savedProgramsState.value.copy(
            isRefreshing = true,
            refreshFailed = false
        )
        val response = try {
            apiService.getProfile()
        } catch (error: Exception) {
            if (error.isUnauthorized()) {
                expireSession()
            } else {
                _profileState.value = ProfileState.Error(
                    hasCachedSavedPrograms = _savedProgramsState.value.items.isNotEmpty()
                )
                _savedProgramsState.value = _savedProgramsState.value.copy(refreshFailed = true)
            }
            _savedProgramsState.value = _savedProgramsState.value.copy(isRefreshing = false)
            return
        }

        _profileState.value = ProfileState.Authenticated(
            UserProfile(
                userId = response.user.id,
                email = response.user.email,
                authDisplayName = response.user.displayName,
                createdAt = response.user.createdAt,
                profile = response.profile?.let {
                    ProfileDetails(it.displayName, it.avatarUrl, it.accountStatus)
                },
                savedProgramCount = response.savedSummary.programCount
            )
        )

        try {
            refreshSavedPrograms(userId)
            _savedProgramsState.value = _savedProgramsState.value.copy(
                refreshFailed = false,
                hasLoadedFromServer = true
            )
        } catch (error: Exception) {
            if (error.isUnauthorized()) {
                expireSession()
            } else {
                _savedProgramsState.value = _savedProgramsState.value.copy(refreshFailed = true)
            }
        } finally {
            _savedProgramsState.value = _savedProgramsState.value.copy(isRefreshing = false)
        }
    }

    private suspend fun refreshSavedPrograms(userId: String) {
        val response = apiService.getSavedItems()
        savedItemDao.replaceForUser(
            userId,
            response.items
                .filter { it.entityType == "program" }
                .distinctBy { it.entityId }
                .map { it.toEntity(userId) }
        )
    }

    private suspend fun handleMutationFailure(
        error: Exception,
        removing: Boolean
    ): SaveProgramResult {
        if (error.isUnauthorized()) {
            expireSession()
            return SaveProgramResult.LoginRequired
        }
        return SaveProgramResult.Failure(
            if (removing) {
                "We couldn’t remove this program. Try again."
            } else {
                "We couldn’t save this program. Try again."
            }
        )
    }

    private suspend fun expireSession() {
        _profileState.value = ProfileState.SessionExpired
        runCatching { authRepository.logout() }
    }

    private fun setPending(programId: String, pending: Boolean) {
        val current = _savedProgramsState.value.pendingProgramIds
        _savedProgramsState.value = _savedProgramsState.value.copy(
            pendingProgramIds = if (pending) current + programId else current - programId
        )
    }
}

private fun SavedProgramDto.toEntity(ownerUserId: String) = SavedItemEntity(
    ownerUserId = ownerUserId,
    savedItemId = savedItemId,
    programId = entityId,
    slug = program.slug,
    title = program.title,
    universityName = program.universityName,
    countryName = program.countryName,
    degreeLevel = program.degreeLevel,
    subject = program.subject,
    tuitionDisplay = program.tuitionDisplay,
    durationMonths = program.durationMonths,
    duration = program.duration,
    savedAt = savedAt
)

private fun SavedItemEntity.toDomain() = SavedProgram(
    savedItemId = savedItemId,
    programId = programId,
    slug = slug,
    title = title,
    universityName = universityName,
    countryName = countryName,
    degreeLevel = degreeLevel,
    subject = subject,
    tuitionDisplay = tuitionDisplay,
    durationMonths = durationMonths,
    duration = duration,
    savedAt = savedAt
)

internal fun Throwable.isUnauthorized(): Boolean = this is HttpException && code() == 401
