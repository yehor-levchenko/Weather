package com.yehorlevchenko.domain.usecase

import com.yehorlevchenko.domain.utils.formatters.IconIdFormatter

class GetConvertedIconIdAsIconUrl {

    operator fun invoke(iconId: String): String {
        return IconIdFormatter().getIconUrlFromIconId(iconId)
    }
}