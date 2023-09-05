package com.jeanbarrossilva.orca.core.sample.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileProvider
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import java.net.URL

/** [Editor] that edits [SampleEditableProfile]s. **/
internal class SampleEditor(private val id: String) : Editor {
    override suspend fun setAvatarURL(avatarURL: URL) {
        edit {
            this.avatarURL = avatarURL
        }
    }

    override suspend fun setName(name: String) {
        edit {
            this.name = name
        }
    }

    override suspend fun setBio(bio: StyledString) {
        edit {
            this.bio = bio
        }
    }

    /**
     * Applies the [edit] to the [SampleEditableProfile] whose [ID][SampleEditableProfile.id]
     * matches [id].
     *
     * @param edit Editing to be made to the matching [SampleEditableProfile].
     **/
    private inline fun edit(crossinline edit: SampleEditableProfile.() -> Unit) {
        SampleProfileProvider.profilesFlow.value = SampleProfileProvider
            .profilesFlow
            .value
            .filterIsInstance<SampleEditableProfile>()
            .replacingOnceBy({ apply(edit) }) { it.id == id }
    }
}
